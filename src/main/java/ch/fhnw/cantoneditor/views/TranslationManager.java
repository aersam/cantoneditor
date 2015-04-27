package ch.fhnw.cantoneditor.views;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

/** Helper class for translation the Application */
public class TranslationManager {
    private static TranslationManager instance;

    /**
     * This indicates that not existing values should be inserted into
     * ApplicationTranslation.properties file. This is only meaningful during development, we should
     * set this to false before release.
     * */
    public static final boolean WRITE_PROPERTIES = true;

    /** Gets the TranslationManager Default Instance */
    public static TranslationManager getInstance() {
        if (instance == null)
            instance = new TranslationManager();
        return instance;
    }

    private TranslationManager() {

    }

    /** Gets the Translation for the given key */
    public String Translate(String key) {
        return Translate(key, key);
    }

    /**
     * Gets the Translation for the given key. If the key is not found, defaultValue will be
     * returned
     */
    public String Translate(String key, String defaultValue) {
        Locale locale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("ApplicationTranslation", locale);
        if (rb.containsKey(key)) {// Everything is fine, just return it
            return rb.getString(key);
        } else {

            if (WRITE_PROPERTIES) { // This code is just for development purposes. It is here to
                                    // speed up development
                try (PrintWriter out = new PrintWriter(new BufferedWriter(new FileWriter(
                        "src/main/resources/ApplicationTranslation.properties", true)))) {
                    out.println(key + "=" + defaultValue);
                } catch (IOException e) {
                    return defaultValue; // Ugly, but should not occur
                }
            }
            return defaultValue;
        }
    }
}
