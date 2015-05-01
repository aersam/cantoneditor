package ch.fhnw.observation;

import java.util.function.Consumer;
import java.util.function.Supplier;

public interface ValueSubscribable<T> extends PropertyChangeable, Supplier<T> {
    /** Sets the value */
    void set(T value);

    /** Sets the value of the given target whenever the value changes */
    default void bindTo(Consumer<T> target) {
        target.accept(this.get());
        this.addPropertyChangeListener((evt) -> target.accept(this.get()));
    }
}
