package ch.fhnw.cantoneditor.views;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import ch.fhnw.observation.ObservableValue;

/** Helper class for translation the Application */
public class TranslationManager {
    private static TranslationManager instance;

    private java.util.List<String> writtenProperties = new java.util.ArrayList<>();
    private static ObservableValue<String> locale = new ObservableValue<String>();

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
    public String translate(String key) {
        return translate(key, key);
    }

    /**
     * Gets the Translation for the given key. If the key is not found, defaultValue will be
     * returned
     */
    public String translate(String key, String defaultValue) {
        if (locale.get() == null)
            locale.set(Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        Locale sysloc = new Locale(locale.get().split("_")[0], locale.get().split("_")[1]);
        ResourceBundle rb = ResourceBundle.getBundle("ApplicationTranslation", sysloc);
        if (rb.containsKey(key)) {// Everything is fine, just return it
            return rb.getString(key);
        } else {

            if (WRITE_PROPERTIES) { // This code is just for development purposes. It is here to
                                    // speed up development
                if (writtenProperties.contains(key))
                    return defaultValue;// We do not want duplicate keys
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
