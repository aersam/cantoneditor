package ch.fhnw.observation;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

/** Wrapper around ArrayList with Observability Support */
public class ObservableList<T> implements List<T>, PropertyChangeable {

    protected final PropertyChangeSupport pcs = new PropertyChangeSupport(this);

    public static final String SET_ACTION = "set";
    public static final String ADDED_ACTION = "add";
    public static final String REMOVE_ACTION = "remove";
    public static final String RESET_ACTION = "reset";

    ArrayList<T> underlyingList = new ArrayList<T>();

    public ObservableList() {

    }

    public ObservableList(Iterable<T> items) {
        for (T c : items) {
            underlyingList.add(c);
        }
    }

    @Override
    public void add(int index, T element) {
        underlyingList.add(index, element);
        this.pcs.firePropertyChange(ADDED_ACTION, this, element);
    }

    @Override
    public boolean addAll(int index, Collection<? extends T> c) {
        boolean add = underlyingList.addAll(index, c);
        this.pcs.firePropertyChange(ADDED_ACTION, this, c);
        return add;
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
    public T remove(int index) {
        T removed = this.underlyingList.remove(index);
        this.pcs.firePropertyChange(REMOVE_ACTION, this, removed);
        return removed;
    }

    @Override
    public T set(int index, T element) {
        T set = this.underlyingList.set(index, element);
        this.pcs.firePropertyChange(SET_ACTION, this, element);
        return set;
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
    public T get(int index) {
        ReadObserver.notifyRead(this, null);
        return this.underlyingList.get(index);
    }

    @Override
    public int indexOf(Object o) {
        ReadObserver.notifyRead(this, null);
        return this.underlyingList.indexOf(o);
    }

    @Override
    public int lastIndexOf(Object o) {
        ReadObserver.notifyRead(this, null);
        return this.underlyingList.lastIndexOf(o);
    }

    @Override
    public ListIterator<T> listIterator() {
        ReadObserver.notifyRead(this, null);
        return this.underlyingList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int index) {
        ReadObserver.notifyRead(this, null);
        return this.underlyingList.listIterator(index);
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        ReadObserver.notifyRead(this, null);
        return this.underlyingList.subList(fromIndex, toIndex);
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
