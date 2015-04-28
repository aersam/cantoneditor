package ch.fhnw.cantoneditor.model;

/**
 * A Class for handling Swiss Languages. Currently, these Languages are hard coded
 * 
 * @author Adrian Ehrsam, Stefan Mettler
 *
 */
public class Language {

    /**
     * The German name of the Language
     * */
    private final String name;

    /** The ISO 639-1 of the Language */
    private final String shortcut;

    /** A Unique id */
    private final int id;

    /** Creates a new language. Is currently private as new Languages are not intended to change */
    private Language(int id, String name, String shortcut) {
        this.id = id;
        this.name = name;
        this.shortcut = shortcut;
    }

    public static final Language German = new Language(1, "deutsch", "de");
    public static final Language French = new Language(2, "französisch", "fr");
    public static final Language Italian = new Language(3, "italienisch", "it");
    public static final Language Rumantsch = new Language(4, "rätoromanisch", "rm");

    public String getName() {
        return this.name;
    }

    public String getShortcut() {
        return this.shortcut;
    }

    public int getId() {
        return this.id;
    }

    @Override
    public String toString() {
        return name;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (obj instanceof Language)
            return ((Language) obj).getId() == this.id;
        return false;
    };

    @Override
    public int hashCode() {
        return id;
    }
}
