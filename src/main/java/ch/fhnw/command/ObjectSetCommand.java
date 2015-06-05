package ch.fhnw.command;

import java.security.AccessControlException;
import java.util.function.BiConsumer;
import java.util.function.Function;

import ch.fhnw.cantoneditor.datautils.TranslationManager;
import ch.fhnw.observation.ReadObserver;

/** A command that sets a property of a given object */
public class ObjectSetCommand<TObject, TValue> implements Executable {

    private TValue oldValue;

    private boolean hasBeenExecuted = false;

    /** The new value that will be applied */
    private final TValue newValue;

    /** A Function that returns the value of the property. The parameter is the object */
    private final Function<TObject, TValue> getValueFunction;

    /** A Function that set the value of the property. The parameter is the object and the new value */
    private final BiConsumer<TObject, TValue> setValueFunction;

    private final TObject obj;

    public ObjectSetCommand(TObject obj, TValue newValue, Function<TObject, TValue> getValue,
            BiConsumer<TObject, TValue> setValue) {
        this.obj = obj;

        this.getValueFunction = getValue;
        this.setValueFunction = setValue;
        this.newValue = newValue;
    }

    public boolean execute() {
        this.hasBeenExecuted = true;

        // We do just want the old value and are not interested in too many events here
        ReadObserver.ignoreDependencies((isO) -> {
            this.oldValue = this.getValueFunction.apply(this.obj);
        });
        if (this.oldValue != null ? this.oldValue.equals(this.newValue) : this.newValue == null) {
            // Values are identical -> Do nothing
            return false;
        }

        this.setValueFunction.accept(this.obj, this.newValue);
        return true;
    }

    /** Sets the property to the old value */
    public void undo() {
        if (this.hasBeenExecuted) {
            this.setValueFunction.accept(this.obj, oldValue);
        } else {
            throw new AccessControlException("You cannot undo something that has not been executed!");
        }
    }

    @Override
    public String toString() {

        if (this.hasBeenExecuted) {
            String template = TranslationManager.getInstance()
                    .translate("PropertyChangeObject", "Change \"{old}\" to \"{new}\" of \"{obj}\"").get();

            return template.replace("{old}", this.oldValue == null ? "null" : this.oldValue.toString())
                    .replace("{new}", this.newValue == null ? "null" : this.newValue.toString())
                    .replace("{obj}", obj.toString());
        }
        return TranslationManager.getInstance().translate("PropertyChange", "Chanage property").get();
    }
}
