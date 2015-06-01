package ch.fhnw.command;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.function.BiConsumer;
import java.util.function.Function;

import ch.fhnw.observation.ObservableList;
import ch.fhnw.observation.PropertyChangeable;
import ch.fhnw.observation.ValueSubscribable;

/** Controls undo/repeat Commands */
public class CommandController implements PropertyChangeable {

    final static String DONECOMMANDS_PROPERTY = "doneCommands";
    final static String REDOCOMMANDS_PROPERTY = "redoCommands";

    private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private static CommandController defaultController;

    private boolean isUndoing = false;

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
    private ObservableList<Executable> doneCommands = new ObservableList<>();

    /** Commands that can be repeated */
    private ObservableList<Executable> redoCommands = new ObservableList<>();

    /** Gets done commands */
    public ObservableList<Executable> getRedoCommands() {
        return redoCommands;
    }

    /** Gets done commands */
    public ObservableList<Executable> getDoneCommands() {
        return doneCommands;
    }

    /**
     * A simple helper to execute a set on a ObservableValue or on a ObservableComputed. Its just a
     * short cut for execute(new PropertySetCommand...)
     */
    public <T> void executePropertySet(T newValue, ValueSubscribable<T> toSet) {
        execute(new PropertySetCommand<T>(newValue, toSet));
    }

    /**
     * A simple helper to execute a set on a ObservableValue or on a ObservableComputed. Its just a
     * short cut for execute(new PropertySetCommand...)
     */
    public <TObj, TValue> void executePropertySet(TObj obj, TValue newValue, Function<TObj, TValue> getter,
            BiConsumer<TObj, TValue> setter) {
        execute(new ObjectSetCommand<TObj, TValue>(obj, newValue, getter, setter));
    }

    /** Executes a command and saves it for undo */
    public void execute(Executable cmd) {
        if (isUndoing)
            return;
        if (cmd.execute()) {
            boolean hadRedoCommands = this.redoCommands.size() != 0;

            this.redoCommands.clear();
            this.doneCommands.add(cmd);
            if (!hadRedoCommands)
                this.pcs.firePropertyChange(REDOCOMMANDS_PROPERTY, null, this.redoCommands);
            this.pcs.firePropertyChange(DONECOMMANDS_PROPERTY, null, this.doneCommands);
        }
    }

    private Executable pop(List<Executable> list) {
        Executable e = list.get(list.size() - 1);
        list.remove(list.size() - 1);
        return e;
    }

    /** Undoes the latest command. Returns false if there was nothing to undo */
    public boolean undo() {
        if (this.doneCommands.size() == 0)
            return false;

        Executable cmd = pop(this.doneCommands);
        isUndoing = true;
        cmd.undo();
        this.redoCommands.add(cmd);

        this.pcs.firePropertyChange(REDOCOMMANDS_PROPERTY, null, this.redoCommands);
        this.pcs.firePropertyChange(DONECOMMANDS_PROPERTY, null, this.doneCommands);

        isUndoing = false;
        return true;
    }

    /** Repeat the latest command. Returns false if there was nothing to repeat */
    public boolean redo() {
        if (this.redoCommands.size() == 0) {
            return false;
        }
        Executable cmd = pop(this.redoCommands);
        if (cmd != null) {
            cmd.execute();
            this.doneCommands.add(cmd);
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
