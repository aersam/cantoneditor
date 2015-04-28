package ch.fhnw.cantoneditor.datautils;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

public class ComputedValue<T> implements PropertyChangeable {
    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    private final Supplier<T> calculator;
    private T lastValue;
    private Map<PropertyChangeable, List<String>> dependencies;

    private PropertyChangeListener changeListener = new PropertyChangeListener() {

        @Override
        public void propertyChange(PropertyChangeEvent evt) {
            // TODO Auto-generated method stub
            notifyPropertyChanged(evt.getSource(), evt.getPropertyName());
        }
    };

    public ComputedValue(Supplier<T> calculator) {
        this.calculator = calculator;
    }

    private boolean isPropertyTracked(Object obj, String propertyName) {
        List<String> properties = dependencies.getOrDefault(obj, null);
        if (properties != null) {
            return properties.contains(propertyName) || properties.contains(null);
        }
        return false;
    }

    private void notifyPropertyChanged(Object object, String propertyName) {
        if (!isPropertyTracked(object, propertyName))
            return;

        T oldValue = this.lastValue;
        T newValue = this.getValueAndObserve();
        this.lastValue = newValue;
        boolean isSame = newValue == null ? oldValue == null : newValue.equals(oldValue);
        if (!isSame) {
            this.pcs.firePropertyChange(null, oldValue, newValue);
        }
    }

    public void addPropertyChangeListener(PropertyChangeListener listener) {
        if (this.dependencies == null) {
            this.getValueAndObserve();
        }
        this.pcs.addPropertyChangeListener(listener);
    }

    public void removePropertyChangeListener(PropertyChangeListener listener) {
        if (this.dependencies == null) {
            this.getValueAndObserve();
        }
        this.pcs.removePropertyChangeListener(listener);
    }

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

    private T getValueAndObserve() {
        if (ReadObserver.startObserving()) {
            lastValue = calculator.get();
            this.setDependenciesAndTrack(ReadObserver.endObserve());

        }
        return lastValue;
    }

    public T get() {
        if (dependencies == null)
            return this.getValueAndObserve();
        return lastValue;
    }

}
