package ch.fhnw.cantoneditor.views;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

import ch.fhnw.observation.ComputedValue;
import ch.fhnw.observation.ObservableValue;
import ch.fhnw.observation.ValueSubscribable;

/** Helper class for translation the Application */
public class TranslationManager {
    private static TranslationManager instance;

    public static class TranslationLocale {
        public final String Local;
        public final String Name;

        public TranslationLocale(String locale, String name) {
            this.Local = locale;
            this.Name = name;
        }
    }

    public TranslationLocale[] SupportedLocales = new TranslationLocale[] { new TranslationLocale("de_DE", "Deutsch"),
            new TranslationLocale("de_CH", "Schwiizerdüütsch"), new TranslationLocale("en_US", "English") };

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

    public String getLocale() {
        return locale.get();
    }

    public void setLocale(String locale) {
        TranslationManager.locale.set(locale);
    }

    /** Gets the Translation for the given key */
    public ValueSubscribable<String> translate(String key) {
        return translate(key, key);
    }

    /**
     * Gets the Translation for the given key. If the key is not found, defaultValue will be
     * returned
     */
    public ValueSubscribable<String> translate(String key, String defaultValue) {
        if (locale.get() == null)
            locale.set(Locale.getDefault().getLanguage() + "_" + Locale.getDefault().getCountry());
        return new ComputedValue<String>(() -> {
            Locale sysloc = new Locale(locale.get().split("_")[0], locale.get().split("_")[1]);
            ResourceBundle rb = ResourceBundle.getBundle("ApplicationTranslation", sysloc);
            if (rb.containsKey(key)) {// Everything is fine, just return it
                    return rb.getString(key);
                } else {

                    if (WRITE_PROPERTIES) { // This code is just for development purposes. It is
                                            // here to
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

}       );
    }
}
