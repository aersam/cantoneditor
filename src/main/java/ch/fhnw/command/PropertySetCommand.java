package ch.fhnw.command;

import java.security.AccessControlException;
import java.util.function.Consumer;
import java.util.function.Supplier;

import ch.fhnw.observation.ComputedValue;
import ch.fhnw.observation.ValueSubscribable;

/** A simple command that sets a Property of an Object */
public class PropertySetCommand<T> implements Executable {
    /** The old value that was used before setting anything */
    private T oldValue;

    private boolean hasBeenExecuted = false;

    /** The new value that will be applied */
    private final T newValue;

    /** A Function that returns the value of the property */
    private final Supplier<T> getValueFunction;

    /** A Function that set the value of the property */
    private final Consumer<T> setValueFunction;

    /** Creates a new PropertySetCommand with the newValue from the getFunction and the setFunction */
    public PropertySetCommand(T newValue, Supplier<T> getFunction, Consumer<T> setFunction) {
        this.newValue = newValue;
        this.getValueFunction = getFunction;
        this.setValueFunction = setFunction;
    }

    /** A simple helper to execute a set on a ObservableValue or on a ObservableComputed */
    public static <T> PropertySetCommand<T> executePropertySet(T newValue, ValueSubscribable<T> toSet) {
        return new PropertySetCommand<T>(newValue, toSet, toSet::set);
    }

    /** Sets the property and reads the old value to be able to undo it */
    public boolean execute() {
        this.hasBeenExecuted = true;
        if (this.getValueFunction instanceof ComputedValue<?>) {
            this.oldValue = ((ComputedValue<T>) this.getValueFunction).peek();
        } else {
            this.oldValue = this.getValueFunction.get();
        }
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

}
