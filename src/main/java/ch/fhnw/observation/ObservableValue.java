package ch.fhnw.observation;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

/** A Helper class providing support for */
public class ObservableValue<T> implements ValueSubscribable<T> {
    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private T value;

    public ObservableValue() {

    }

    /** Creates a new instance of observable value with the given initial value */
    public ObservableValue(T value) {
        this.value = value;
    }

    /** Listen to changes made to this Set */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /** Remove Change Listener */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /** Gets the value */
    @Override
    public T get() {
        ReadObserver.notifyRead(this, null);
        return this.value;
    }

    @Override
    public String toString() {
        T value = this.get();
        if (value == null)
            return null;
        else
            return value.toString();
    };

    /** Sets the value */
    @Override
    public void set(T value) {
        if (value != this.value) {
            T oldValue = value;
            this.value = value;
            this.pcs.firePropertyChange(null, oldValue, value);
        }
    }

}
