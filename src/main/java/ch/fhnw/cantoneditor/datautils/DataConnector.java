package ch.fhnw.cantoneditor.datautils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map.Entry;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;
import com.google.gson.stream.JsonReader;

public class DataConnector {

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
        File dataFile = new File(getDataFolder(true), "db.json");
        if (!dataFile.exists())
            return new JsonObject();
        return (JsonObject) parser.parse(new JsonReader(new FileReader(dataFile)));

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

    public <T> void saveAll(Class<T> type, Collection<T> items) throws JsonIOException, JsonSyntaxException,
            FileNotFoundException {
        JsonObject data = getJson();

        Gson gson = new Gson();
        JsonElement el = gson.toJsonTree(items);
        JsonObject existing = getJson();
        if (existing.has(type.getName()))
            existing.remove(type.getName());
        existing.add(type.getName(), el);
        String json = gson.toJson(existing);
        JsonParser parser = new JsonParser();
        File dataFile = new File(getDataFolder(true), "db.json");
        PrintWriter writer = new PrintWriter(dataFile);
        writer.print(json);
        writer.close();
    }
}
