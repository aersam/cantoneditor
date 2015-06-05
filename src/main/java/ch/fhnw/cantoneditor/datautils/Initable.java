package ch.fhnw.cantoneditor.datautils;

/** A little utility interface for objects which need to perform some work after being deserialised */
public interface Initable {
    /** A method to be executed after deserialisation */
    public void init();
}
