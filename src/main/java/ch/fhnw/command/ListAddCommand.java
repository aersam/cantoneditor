package ch.fhnw.command;

import java.util.Collection;

import ch.fhnw.cantoneditor.views.TranslationManager;

public class ListAddCommand<T> implements Executable {

    private final T toAdd;
    private final Collection<T> collection;
    private final String collectionName;
    private boolean hasBeenExecuted = false;

    public ListAddCommand(String collectionName, Collection<T> list, T toAdd) {
        this.collection = list;
        this.toAdd = toAdd;
        this.collectionName = collectionName;
    }

    @Override
    public boolean execute() {
        if (this.hasBeenExecuted || collection.contains(toAdd))
            return false;
        collection.add(toAdd);
        this.hasBeenExecuted = true;
        return true;
    }

    @Override
    public void undo() {
        this.collection.remove(toAdd);
    }

    @Override
    public String toString() {
        return TranslationManager.getInstance().Translate("AddItem", "Add \"{item}\" from \"{list}\"")
                .replace("{item}", toAdd == null ? "null" : toAdd.toString()).replace("{list}", this.collectionName);

    }
}
