package ch.fhnw.command;

import java.security.AccessControlException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import ch.fhnw.cantoneditor.views.TranslationManager;
import ch.fhnw.observation.ReadObserver;
import ch.fhnw.observation.ValueSubscribable;

/** A simple command that sets a Property of an Object */
public class PropertySetCommand<T> implements Executable {
    /** The old value that was used before setting anything */
    protected T oldValue;

    protected boolean hasBeenExecuted = false;

    /** The new value that will be applied */
    protected final T newValue;

    /** A Function that returns the value of the property */
    protected final Supplier<T> getValueFunction;

    /** A Function that set the value of the property */
    protected final Consumer<T> setValueFunction;

    /** Creates a new PropertySetCommand with the newValue from the getFunction and the setFunction */
    public PropertySetCommand(T newValue, Supplier<T> getFunction, Consumer<T> setFunction) {
        this.newValue = newValue;
        this.getValueFunction = getFunction;
        this.setValueFunction = setFunction;
    }

    /**
     * Creates a new PropertySetCommand with the newValue from a ObservableValue or on a
     * ObservableComputed
     */
    public PropertySetCommand(T newValue, ValueSubscribable<T> toSet) {
        this.newValue = newValue;
        this.getValueFunction = toSet;
        this.setValueFunction = toSet::set;
    }

    /** Sets the property and reads the old value to be able to undo it */
    public boolean execute() {
        this.hasBeenExecuted = true;

        // We do just want the old value and are not interested in too many events here
        ReadObserver.ignoreDependencies((isO) -> {
            this.oldValue = this.getValueFunction.get();
        });
        if (this.oldValue != null ? this.oldValue.equals(this.newValue) : this.newValue == null) {
            // Values are identical -> Do nothing
            return false;
        }
        this.setValueFunction.accept(this.newValue);
        return true;
    }

    /** Sets the property to the old value */
    public void undo() {
        if (this.hasBeenExecuted) {
            this.setValueFunction.accept(oldValue);
        } else {
            throw new AccessControlException("You cannot undo something that has not been executed!");
        }
    }

    @Override
    public String toString() {
        if (this.hasBeenExecuted) {
            String template = TranslationManager.getInstance().translate("PropertyChangeObservable",
                    "Change \"{old}\" to \"{new}\"");

            return template.replace("{old}", this.oldValue == null ? "null" : this.oldValue.toString()).replace(
                    "{new}", this.newValue == null ? "null" : this.newValue.toString());
        }
        return TranslationManager.getInstance().translate("PropertyChange", "Chanage property");
    }

}
