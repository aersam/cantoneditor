package ch.fhnw.cantoneditor.datautils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

public class DataConnector {

    private static final String DATA_FILE = "db.json";

    private static class PropertyInformation {
        public Method Getter;
        public Method Setter;
    }

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

    private JsonObject getJson() throws JsonIOException, JsonSyntaxException, FileNotFoundException {
        JsonParser parser = new JsonParser();
        File dataFile = new File(getDataFolder(true), DATA_FILE);
        if (!dataFile.exists())
            return new JsonObject();
        JsonElement el = parser.parse(new JsonReader(new FileReader(dataFile)));
        return (JsonObject) el;

    }

    private <T> HashMap<String, PropertyInformation> getTypeInformation(Class<T> type) {

        HashMap<String, PropertyInformation> parsers = new HashMap<>();
        for (Method m : type.getMethods()) {
            if (m.getName().startsWith("set")) {
                String normalName = m.getName().substring(3);
                if (!parsers.containsKey(normalName))
                    parsers.put(normalName, new PropertyInformation());
                parsers.get(normalName).Setter = m;
            }
            if (m.getName().startsWith("get")) {
                String normalName = m.getName().substring(3);
                if (!parsers.containsKey(normalName))
                    parsers.put(normalName, new PropertyInformation());
                parsers.get(normalName).Getter = m;
            }
        }
        for (Entry<String, PropertyInformation> ent : parsers.entrySet()) {
            if (ent.getValue().Getter == null || ent.getValue().Setter == null) {
                parsers.remove(ent.getKey());// Should we do this after the loop?
            }
        }
        return parsers;
    }

    @SuppressWarnings("unchecked")
    public <T> List<T> getAll(Class<T> type) throws JsonIOException, JsonSyntaxException, FileNotFoundException,
            ClassNotFoundException {
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
            FileNotFoundException, ClassNotFoundException, UnsupportedEncodingException {
        Gson gson = new Gson();
        Class<?> namedClass = Class.forName("[L" + type.getName() + ";");
        JsonElement el = gson.toJsonTree(items.toArray(), namedClass);
        JsonObject existing = getJson();
        if (existing.has(type.getName()))
            existing.remove(type.getName());
        existing.add(type.getName(), el);
        String json = gson.toJson(existing);
        File dataFile = new File(getDataFolder(true), DATA_FILE);
        PrintWriter writer = new PrintWriter(dataFile, "utf-8");
        writer.print(json);
        writer.close();
    }
}
