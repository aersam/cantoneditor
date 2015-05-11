package ch.fhnw.observation;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;
import java.util.function.Supplier;

/** A class for handling computed values with support for automatic Dependency Resolving */
public class ComputedValue<T> implements ValueSubscribable<T>, Disposable {
    protected PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /** The access function */
    private final Supplier<T> reader;

    /** The write function. Not currently used */
    private final Consumer<T> writer;

    /**
     * The last value that Consumer returned. Will not be recalculated unless the dependencies
     * change
     */
    private T lastValue;

    /** The dependencies */
    private Map<PropertyChangeable, List<String>> dependencies;

    /** The Change Listener, wrapping notifyPropertyChanged function */
    private PropertyChangeListener changeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // TODO Auto-generated method stub
            notifyPropertyChanged(evt.getSource(), evt.getPropertyName());
        }
    };

    /** Creates a new Computed Value from a given function */
    public ComputedValue(Supplier<T> reader) {
        this.reader = reader;
        this.writer = null;
    }

    /** Creates a new Computed Value from a given function. You can write by using accept Function */
    public ComputedValue(Supplier<T> reader, Consumer<T> write) {
        this.reader = reader;
        this.writer = write;
    }

    /** Returns true if the property is being tracked */
    private boolean isPropertyTracked(Object obj, String propertyName) {
        List<String> properties = dependencies.getOrDefault(obj, null);
        if (properties != null) {
            return properties.contains(propertyName) || properties.contains(null);
        }
        return false;
    }

    /** Gets called if any property of any dependency is changed */
    private void notifyPropertyChanged(Object object, String propertyName) {
        if (!isPropertyTracked(object, propertyName))
            return;

        T oldValue = this.lastValue;
        if (!this.getValueAndObserve()) {
            dependencies = null;
        }
        T newValue = this.lastValue;
        boolean isSame = newValue == null ? oldValue == null : newValue.equals(oldValue);
        if (!isSame) {
            this.pcs.firePropertyChange(null, oldValue, newValue);
        }
    }

    /** Adds a change listener */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (this.dependencies == null) {
            this.getValueAndObserve();
        }
        this.pcs.addPropertyChangeListener(listener);
    }

    /** Removes a change listener */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (this.dependencies == null) {
            this.getValueAndObserve();
        }
        this.pcs.removePropertyChangeListener(listener);
    }

    /** Sets the dependencies property and attaches events */
    private void setDependenciesAndTrack(Map<PropertyChangeable, List<String>> newdependencies) {
        if (dependencies != null) {
            dependencies.forEach((obj, prop) -> {
                obj.removePropertyChangeListener(this.changeListener);
            });
        }
        dependencies = newdependencies;
        if (dependencies != null) {
            dependencies.forEach((obj, prop) -> {
                obj.addPropertyChangeListener(this.changeListener);
            });
        }
    }

    /** Gets the value and observes changes and dependencies */
    private boolean getValueAndObserve() {
        if (ReadObserver.startObserving()) {
            lastValue = reader.get();
            this.setDependenciesAndTrack(ReadObserver.endObserve());
            return true;
        }
        lastValue = reader.get();
        return false;
    }

    /** Gets the value */
    public T get() {
        if (dependencies == null) {
            this.getValueAndObserve();
        }
        ReadObserver.notifyRead(this, null);
        return lastValue;
    }

    /** Writes the value */
    public void set(T t) {
        if (writer == null)
            throw new IllegalAccessError("writer must be set in order to use this!");
        writer.accept(t);
    }

    @Override
    public String toString() {
        T value = this.get();
        if (value == null)
            return null;
        return value.toString();
    }

    /** Unsubscribes dependencies. Do not access this object anymore */
    @Override
    public void dispose() {
        this.setDependenciesAndTrack(null);
        this.pcs = null;
    }

}
