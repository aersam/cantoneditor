package ch.fhnw.cantoneditor.views;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Locale;
import java.util.ResourceBundle;

public class TranslationManager {
    private static TranslationManager instance;

    public static final boolean WRITE_PROPERTIES = true;

    public static TranslationManager getInstance() {
        if (instance == null)
            instance = new TranslationManager();
        return instance;
    }

    private TranslationManager() {

    }

    public String Translate(String key) {
        return Translate(key, key);
    }

    public String Translate(String key, String defaultValue) {
        Locale locale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("ApplicationTranslation", locale);
        if (rb.containsKey(key)) {
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
