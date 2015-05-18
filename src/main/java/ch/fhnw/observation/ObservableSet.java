package ch.fhnw.observation;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Collection;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/** Wrapper around HashSet with Observability Support */
public class ObservableSet<T> implements Set<T>, PropertyChangeable {

    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public static final String SET_ACTION = "set";
    public static final String ADDED_ACTION = "add";
    public static final String REMOVE_ACTION = "remove";
    public static final String RESET_ACTION = "reset";

    HashSet<T> underlyingList = new HashSet<T>();

    public ObservableSet() {

    }

    public ObservableSet(Iterable<T> items) {
        for (T c : items) {
            underlyingList.add(c);
        }
    }

    /** Clears all existing items and adds the new ones */
    public void reset(Collection<? extends T> newItems) {
        underlyingList.clear();
        if (newItems != null) {
            underlyingList.addAll(newItems);
        }
        this.pcs.firePropertyChange(RESET_ACTION, this, newItems);
    }

    @Override
    public boolean add(T arg0) {
        boolean add = underlyingList.add(arg0);
        this.pcs.firePropertyChange(ADDED_ACTION, this, arg0);
        return add;
    }

    @Override
    public boolean addAll(Collection<? extends T> arg0) {
        boolean add = this.underlyingList.addAll(arg0);
        this.pcs.firePropertyChange(ADDED_ACTION, this, arg0);
        return add;
    }

    @Override
    public void clear() {
        this.underlyingList.clear();
        this.pcs.firePropertyChange(RESET_ACTION, this, this);
    }

    @Override
    public boolean contains(Object arg0) {
        ReadObserver.notifyRead(this, null);
        return this.underlyingList.contains(arg0);
    }

    @Override
    public boolean containsAll(Collection<?> arg0) {
        ReadObserver.notifyRead(this, null);
        return this.underlyingList.containsAll(arg0);
    }

    @Override
    public boolean isEmpty() {
        ReadObserver.notifyRead(this, null);
        return this.underlyingList.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        ReadObserver.notifyRead(this, null);
        return this.underlyingList.iterator();
    }

    @Override
    public boolean remove(Object arg0) {
        boolean removed = this.underlyingList.remove(arg0);
        this.pcs.firePropertyChange(REMOVE_ACTION, this, removed);
        return removed;
    }

    @Override
    public boolean removeAll(Collection<?> arg0) {
        boolean removed = this.underlyingList.removeAll(arg0);
        this.pcs.firePropertyChange(REMOVE_ACTION, this, arg0);
        return removed;
    }

    @Override
    public boolean retainAll(Collection<?> arg0) {
        boolean removed = this.underlyingList.retainAll(arg0);
        this.pcs.firePropertyChange(RESET_ACTION, this, arg0);
        return removed;
    }

    @Override
    public int size() {
        ReadObserver.notifyRead(this, null);
        return underlyingList.size();
    }

    @Override
    public Object[] toArray() {
        ReadObserver.notifyRead(this, null);
        return underlyingList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] arg0) {
        ReadObserver.notifyRead(this, null);
        // TODO Auto-generated method stub
        return underlyingList.toArray(arg0);
    }

    /** Listen to changes made to this Set */
    public void addPropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.addPropertyChangeListener(listener);
    }

    /** Remove Change Listener */
    public void removePropertyChangeListener(PropertyChangeListener listener) {
        this.pcs.removePropertyChangeListener(listener);
    }

    @Override
    public java.util.stream.Stream<T> stream() {
        ReadObserver.notifyRead(this, null);
        return this.underlyingList.stream();
    }

    @Override
    public boolean equals(Object arg0) {
        return this.underlyingList.equals(arg0);
    }

    @Override
    public int hashCode() {
        return this.underlyingList.hashCode();
    }
}
