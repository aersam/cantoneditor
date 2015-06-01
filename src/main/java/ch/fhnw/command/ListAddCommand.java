package ch.fhnw.command;

import java.util.Collection;

import ch.fhnw.cantoneditor.views.TranslationManager;

public class ListAddCommand<T> implements Executable {

    private final Collection<T> toAdd;
    private final Collection<T> collection;
    private final String collectionName;
    private boolean hasBeenExecuted = false;

    public ListAddCommand(String collectionName, Collection<T> list, Collection<T> toAdd) {
        this.collection = list;
        this.toAdd = toAdd;
        this.collectionName = collectionName;
    }

    @Override
    public boolean execute() {
        if (this.hasBeenExecuted || collection.contains(toAdd))
            return false;
        collection.addAll(toAdd);
        this.hasBeenExecuted = true;
        return true;
    }

    @Override
    public void undo() {
        this.collection.removeAll(toAdd);
    }

    @Override
    public String toString() {
        String itemString = "";
        if (toAdd == null) {
            itemString = "null";
        } else if (toAdd.size() == 1) {
            for (T item : toAdd) {
                itemString = item.toString();
            }
        } else {
            itemString = toAdd.toString();
        }
        return TranslationManager.getInstance().translate("AddItem", "Add \"{item}\" to \"{list}\"")
                .replace("{item}", itemString).replace("{list}", this.collectionName);

    }
}
