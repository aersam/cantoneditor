package ch.fhnw.cantoneditor.datautils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ch.fhnw.observation.PropertyChangeable;
import ch.fhnw.observation.ReadObserver;

/** A base class for PropertyChangeable Support. */
public abstract class BaseModel implements PropertyChangeable {

    /** The property Change Support Field */
    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /** Listen to changes made to this objct */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /** Remove Change Listener */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    /** Use this to notify about a property read. Is useful in conjunction with ComputedValue class */
    protected void notifyPropertyRead(String property) {
        ReadObserver.notifyRead(this, property);
    }

}
