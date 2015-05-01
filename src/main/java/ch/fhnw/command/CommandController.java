package ch.fhnw.command;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Stack;

import ch.fhnw.observation.PropertyChangeable;
import ch.fhnw.observation.ReadObserver;
import ch.fhnw.observation.ValueSubscribable;

/** Controls undo/repeat Commands */
public class CommandController implements PropertyChangeable {

    final static String DONECOMMANDS_PROPERTY = "doneCommands";
    final static String REDOCOMMANDS_PROPERTY = "redoCommands";

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private static CommandController defaultController;

    /**
     * Use this method to get the default CommandController which should be used to implement
     * undo/redo on application level
     */
    public static CommandController getDefault() {
        if (defaultController == null)
            defaultController = new CommandController();

        return defaultController;
    }

    /** Commands that were already executed */
    private Stack<Executable> doneCommands = new Stack<>();

    /** Commands that can be repeated */
    private Stack<Executable> redoCommands = new Stack<>();

    /** Gets done commands */
    public Iterable<Executable> getRedoCommands() {
        ReadObserver.notifyRead(this, REDOCOMMANDS_PROPERTY);
        return redoCommands;
    }

    /** Gets done commands */
    public Iterable<Executable> getDoneCommands() {
        ReadObserver.notifyRead(this, DONECOMMANDS_PROPERTY);
        return doneCommands;
    }

    /**
     * A simple helper to execute a set on a ObservableValue or on a ObservableComputed. Its just a
     * short cut for execute(new PropertySetCommand...)
     */
    public <T> void executePropertySet(T newValue, ValueSubscribable<T> toSet) {
        execute(new PropertySetCommand<T>(newValue, toSet));
    }

    /** Executes a command and saves it for undo */
    public void execute(Executable cmd) {
        if (cmd.execute()) {
            boolean hadRedoCommands = !this.redoCommands.empty();

            this.redoCommands.clear();
            this.doneCommands.push(cmd);
            if (!hadRedoCommands)
                this.pcs.firePropertyChange(REDOCOMMANDS_PROPERTY, null, this.redoCommands);
            this.pcs.firePropertyChange(DONECOMMANDS_PROPERTY, null, this.doneCommands);
        }
    }

    /** Undoes the latest command. Returns false if there was nothing to undo */
    public boolean undo() {
        if (this.doneCommands.empty())
            return false;
        Executable cmd = this.doneCommands.pop();
        cmd.undo();
        this.redoCommands.push(cmd);

        this.pcs.firePropertyChange(REDOCOMMANDS_PROPERTY, null, this.redoCommands);
        this.pcs.firePropertyChange(DONECOMMANDS_PROPERTY, null, this.doneCommands);
        return true;
    }

    /** Repeat the latest command. Returns false if there was nothing to repeat */
    public boolean redo() {
        if (this.redoCommands.empty()) {
            return false;
        }
        Executable cmd = this.redoCommands.pop();
        if (cmd != null) {
            cmd.execute();
            this.pcs.firePropertyChange(REDOCOMMANDS_PROPERTY, null, this.redoCommands);
            this.pcs.firePropertyChange(DONECOMMANDS_PROPERTY, null, this.doneCommands);
            return true;
        }
        return false;

    }

    @Override
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    @Override
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }
}
