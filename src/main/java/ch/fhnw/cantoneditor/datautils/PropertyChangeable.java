package ch.fhnw.cantoneditor.datautils;

import java.beans.PropertyChangeListener;

public interface PropertyChangeable {
    void addPropertyChangeListener(PropertyChangeListener listener);

    void removePropertyChangeListener(PropertyChangeListener listener);
}
