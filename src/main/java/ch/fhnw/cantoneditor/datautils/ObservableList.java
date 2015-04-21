package ch.fhnw.cantoneditor.datautils;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;

public class ObservableList<T> extends BaseModel implements List<T> {

    public static final String SET_ACTION = "set";
    public static final String ADDED_ACTION = "add";
    public static final String REMOVE_ACTION = "remove";
    public static final String RESET_ACTION = "reset";

    ArrayList<T> underlyingList = new ArrayList<T>();

    @Override
    public boolean add(T arg0) {
        boolean add = underlyingList.add(arg0);
        this.pcs.firePropertyChange(ADDED_ACTION, this, arg0);
        return add;
    }

    @Override
    public void add(int arg0, T arg1) {
        underlyingList.add(arg0, arg1);
        this.pcs.firePropertyChange(ADDED_ACTION, this, arg1);
    }

    @Override
    public boolean addAll(Collection<? extends T> arg0) {
        boolean add = this.underlyingList.addAll(arg0);
        this.pcs.firePropertyChange(ADDED_ACTION, this, arg0);
        return add;
    }

    @Override
    public boolean addAll(int arg0, Collection<? extends T> arg1) {
        boolean add = this.underlyingList.addAll(arg0, arg1);
        this.pcs.firePropertyChange(ADDED_ACTION, this, arg1);
        return add;
    }

    @Override
    public void clear() {
        this.underlyingList.clear();
        this.pcs.firePropertyChange(RESET_ACTION, this, this);
    }

    @Override
    public boolean contains(Object arg0) {
        return this.underlyingList.contains(arg0);
    }

    @Override
    public boolean containsAll(Collection<?> arg0) {
        return this.underlyingList.containsAll(arg0);
    }

    @Override
    public T get(int arg0) {
        return this.underlyingList.get(arg0);
    }

    @Override
    public int indexOf(Object arg0) {
        return this.underlyingList.indexOf(arg0);
    }

    @Override
    public boolean isEmpty() {
        return this.underlyingList.isEmpty();
    }

    @Override
    public Iterator<T> iterator() {
        return this.underlyingList.iterator();
    }

    @Override
    public int lastIndexOf(Object arg0) {
        return this.underlyingList.lastIndexOf(arg0);
    }

    @Override
    public ListIterator<T> listIterator() {
        return this.underlyingList.listIterator();
    }

    @Override
    public ListIterator<T> listIterator(int arg0) {
        return this.underlyingList.listIterator(arg0);
    }

    @Override
    public boolean remove(Object arg0) {
        boolean removed = this.underlyingList.remove(arg0);
        this.pcs.firePropertyChange(REMOVE_ACTION, this, removed);
        return removed;
    }

    @Override
    public T remove(int arg0) {
        T removed = this.underlyingList.remove(arg0);
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
    public T set(int arg0, T arg1) {
        Object oldValue = this.underlyingList.get(arg0);
        T setted = this.underlyingList.set(arg0, arg1);
        this.pcs.firePropertyChange(SET_ACTION, oldValue, setted);
        return setted;
    }

    @Override
    public int size() {
        return underlyingList.size();
    }

    @Override
    public List<T> subList(int fromIndex, int toIndex) {
        return underlyingList.subList(fromIndex, toIndex);
    }

    @Override
    public Object[] toArray() {
        return underlyingList.toArray();
    }

    @Override
    public <T> T[] toArray(T[] arg0) {
        return underlyingList.toArray(arg0);
    }

}
