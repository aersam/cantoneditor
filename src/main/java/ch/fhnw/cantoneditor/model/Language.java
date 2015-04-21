package ch.fhnw.cantoneditor.model;

public class Language {
    private String name;

    private String shortcut;

    private int id;

    private Language(int id, String name, String shortcut) {
        this.id = id;
        this.name = name;
        this.shortcut = shortcut;
    }

    public static final Language German = new Language(1, "deutsch", "de");
    public static final Language French = new Language(2, "französisch", "fr");
    public static final Language Italian = new Language(3, "italienisch", "it");
    public static final Language Rumantsch = new Language(4, "rätoromanisch", "rt");

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
        // TODO Auto-generated method stub
        return name;
    }
}
