package ch.fhnw.command;

import java.util.Collection;
import java.util.List;

import ch.fhnw.cantoneditor.views.TranslationManager;

public class ListRemoveCommand<T> implements Executable {

    private final T toRemove;
    private final Collection<T> collection;

    private boolean hasBeenExecuted = false;
    private int oldIndex = -1;

    private String collectionName;

    public ListRemoveCommand(String collectionName, Collection<T> list, T toRemove) {
        this.collection = list;
        this.collectionName = collectionName;
        this.toRemove = toRemove;
    }

    @Override
    public boolean execute() {
        if (this.hasBeenExecuted || !collection.contains(toRemove))
            return false;
        if (this.collection instanceof java.util.List<?>) {
            this.oldIndex = ((java.util.List<T>) this.collection).indexOf(toRemove);
        } else {
            this.oldIndex = -1;
        }
        collection.remove(toRemove);
        this.hasBeenExecuted = true;
        return true;
    }

    @Override
    public void undo() {
        if (this.oldIndex != -1 && this.collection instanceof java.util.List<?>) {
            List<T> list = ((java.util.List<T>) this.collection);
            if (list.size() > this.oldIndex) {// Preserver order
                list.add(this.oldIndex, this.toRemove);
            } else {
                list.add(this.toRemove);
            }
        } else {
            this.collection.add(toRemove);
        }
    }

    @Override
    public String toString() {
        return TranslationManager.getInstance().Translate("RemoveItem", "Remove \"{item}\" from \"{list}\"")
                .replace("{item}", toRemove == null ? "null" : toRemove.toString())
                .replace("{list}", this.collectionName);

    }
}
