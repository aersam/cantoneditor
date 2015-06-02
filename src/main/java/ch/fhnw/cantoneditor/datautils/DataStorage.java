package ch.fhnw.cantoneditor.datautils;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.text.ParseException;
import java.util.Collection;
import java.util.List;

import ch.fhnw.cantoneditor.model.Canton;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class DataStorage {

    private static List<Canton> cantons;

    public static void init() throws JsonIOException, JsonSyntaxException, ClassNotFoundException, IOException,
            ParseException, NoDataFoundException {
        loadData(false);
    }

    private static void loadData(boolean reread) throws IOException, ParseException, NoDataFoundException,
            JsonIOException, JsonSyntaxException, ClassNotFoundException {
        if (reread) {
            DataConnector.getInstance().deleteDataFile();
        }
        cantons = DataConnector.getInstance().getAll(Canton.class);
        if (cantons == null) {
            CsvReader.readAll();
        }
    }

    public static List<Canton> getAllCantons() {

        return cantons;
    }

    public static void setAllCantons(List<Canton> value) {
        cantons = value;
    }

    public static void save() throws JsonIOException, JsonSyntaxException, FileNotFoundException,
            ClassNotFoundException, UnsupportedEncodingException {
        for (Canton c : cantons) {
            c.beforeSerialize();
        }
        DataConnector.getInstance().saveAll(Canton.class, cantons);
    }

    public static Canton getCantonById(int nr, boolean createIfNotExists) throws JsonIOException, JsonSyntaxException,
            ClassNotFoundException, IOException, ParseException, NoDataFoundException {
        Collection<Canton> cantons = DataStorage.getAllCantons();
        if (cantons != null) {
            for (Canton c : cantons) {
                if (c.getId() == nr)
                    return c;
            }
        }
        if (createIfNotExists) {
            Canton c = new Canton();
            c.setId(nr);
            DataStorage.getAllCantons().add(c);
            return c;
        }
        return null;
    }

    public static Canton getCantonByShortcut(String shortcut, boolean createIfNotExists) throws JsonIOException,
            JsonSyntaxException, ClassNotFoundException, IOException, ParseException, NoDataFoundException {
        Collection<Canton> cantons = DataStorage.getAllCantons();
        for (Canton c : cantons) {
            if (c.getShortCut().equals(shortcut))
                return c;
        }
        if (createIfNotExists) {
            Canton c = new Canton();
            c.setShortCut(shortcut);
            DataStorage.getAllCantons().add(c);
            return c;
        }
        return null;
    }

}
