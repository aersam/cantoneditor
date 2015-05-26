package ch.fhnw.cantoneditor.datautils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

import ch.fhnw.observation.PropertyChangeable;
import ch.fhnw.observation.ReadObserver;

import com.google.gson.annotations.Expose;

/** A base class for PropertyChangeable Support. */
public abstract class BaseModel implements PropertyChangeable {

    public static final String ID_PROPERTY = "id";

    protected int id = 0;

    /** The property Change Support Field */
    @Expose(serialize = false, deserialize = false)
    protected transient final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    /** Listen to changes made to this object */
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

    public int getId() {
        this.notifyPropertyRead(ID_PROPERTY);
        return id;
    }

    public void setId(int id) {

        if (id != this.id) {
            if (this.id != 0 || id == 0)
                throw new IllegalAccessError("Cannot change primary key!");
            Object oldValue = this.id;
            this.id = id;
            this.pcs.firePropertyChange(ID_PROPERTY, oldValue, id);
        }
    }

}
