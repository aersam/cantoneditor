package ch.fhnw.observation;

import java.beans.PropertyChangeListener;

public interface PropertyChangeable {
    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);
}
