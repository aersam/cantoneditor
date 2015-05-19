package ch.fhnw.cantoneditor.datautils;

/** Interface for searching */
public interface Searchable {

    /** MUST return LOWERCASE search strings */
    public String[] getSearchStrings();
}
