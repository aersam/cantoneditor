package ch.fhnw.observation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
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

    default void bindTwoWay(ValueSubscribable<T> other) {
        T otherValue = other.get();
        if (otherValue == null ? this.get() != null : !otherValue.equals(get())) {
            other.set(get());// Both values must equal
        }
        other.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                set(other.get());
            }
        });
        this.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                other.set(get());
            }
        });

    }
}
