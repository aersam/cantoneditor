package ch.fhnw.observation;

import java.util.function.Supplier;

public interface ValueSubscribable<T> extends PropertyChangeable, Supplier<T> {
    /** Sets the value */
    void set(T value);
}
