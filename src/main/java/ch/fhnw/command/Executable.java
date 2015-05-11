package ch.fhnw.command;

/** Represents a function that can be undone. Implementers should implement toString as well */
public interface Executable {
    /**
     * Executes the function. May return false to indicate that the method did nothing (should not
     * undo it)
     */
    public boolean execute();

    /** Undos the function */
    public void undo();

}
