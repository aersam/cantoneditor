package ch.fhnw.observation;

/** Interface for objects that require cleanup post-use. Call dispose() in finally block! */
public interface Disposable {
    public void dispose();
}
