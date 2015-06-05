package ch.fhnw.cantoneditor.datautils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.util.Collection;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

/**
 * A class which is responsible to save data of any class. There is no dependency on this projects
 * models (in contrast to DataStorage)
 */
class DataConnector {

    private static final String DATA_FILE = "db.json";

    private static DataConnector instance;

    private DataConnector() {
    }

    public static DataConnector getInstance() {
        if (instance == null)
            instance = new DataConnector();
        return instance;
    }

    private File getDataFolder(boolean create) {
        File userDir = new File(System.getProperty("user.home"));
        File dataDir1 = new File(userDir, ".cantoneditor");
        if (create && !dataDir1.exists())
            dataDir1.mkdir();
        return dataDir1;
    }

    private JsonObject getJson() throws JsonIOException, JsonSyntaxException, UnsupportedEncodingException, IOException {
        JsonParser parser = new JsonParser();
        File dataFile = new File(getDataFolder(true), DATA_FILE);
        if (!dataFile.exists())
            return new JsonObject();

        try (BufferedReader in = new BufferedReader(new InputStreamReader(new FileInputStream(dataFile), "UTF8"))) {

            JsonElement el = parser.parse(new JsonReader(in));
            return (JsonObject) el;

        }

    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAll(Class<T> type) throws JsonIOException, JsonSyntaxException, ClassNotFoundException,
            UnsupportedEncodingException, IOException {
        File dataFile = new File(getDataFolder(true), DATA_FILE);
        if (!dataFile.exists())
            return null;
        JsonObject js = getJson();
        JsonArray data = js.getAsJsonArray(type.getName());
        Gson gson = new Gson();

        Class<?> namedClass = Class.forName("[L" + type.getName() + ";");

        T[] items = (T[]) gson.fromJson(data, namedClass);
        List<T> list = new java.util.ArrayList<T>();
        for (T item : items) {
            if (item instanceof Initable) {
                ((Initable) item).init();
            }
            list.add(item);
        }
        return list;

    }

    public boolean deleteDataFile() {
        File dataFile = new File(getDataFolder(true), DATA_FILE);
        if (dataFile.exists()) {
            dataFile.delete();
            return true;
        }
        return false;
    }

    public <T> void saveAll(Class<T> type, Collection<T> items) throws JsonIOException, JsonSyntaxException,
            ClassNotFoundException, IOException {
        Gson gson = new Gson();
        Class<?> namedClass = Class.forName("[L" + type.getName() + ";");
        JsonElement el = gson.toJsonTree(items.toArray(), namedClass);
        JsonObject existing = getJson();
        if (existing.has(type.getName()))
            existing.remove(type.getName());
        existing.add(type.getName(), el);
        String json = gson.toJson(existing);
        File dataFile = new File(getDataFolder(true), DATA_FILE);
        PrintWriter writer = new PrintWriter(dataFile, "utf8");
        writer.write(json);
        writer.close();
    }
}
