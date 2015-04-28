package ch.fhnw.cantoneditor.datautils;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public abstract class BaseModel implements PropertyChangeable {

    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    protected void notifyPropertyRead(String property) {
        ReadObserver.notifyRead(this, property);
    }

}
