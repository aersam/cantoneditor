package ch.fhnw.cantoneditor.model;

import java.beans.PropertyChangeListener;

public interface PropertyChangeable {
    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);
}
