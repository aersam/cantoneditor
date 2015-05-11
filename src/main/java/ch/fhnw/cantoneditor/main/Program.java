package ch.fhnw.cantoneditor.main;

import java.io.IOException;
import java.text.ParseException;
import java.util.List;

import ch.fhnw.cantoneditor.datautils.CsvReader;
import ch.fhnw.cantoneditor.datautils.DB4OConnector;
import ch.fhnw.cantoneditor.datautils.NoDataFoundException;
import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.cantoneditor.model.Commune;
import ch.fhnw.cantoneditor.views.Overview;

public class Program {

    public static void main(String[] args) {
        try {
            loadData();

            Overview v = new Overview();
            v.show();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

    private static void loadData() throws IOException, ParseException, NoDataFoundException {
        List<Canton> cantons = DB4OConnector.getAll(Canton.class);
        if (cantons.size() == 0) {
            Iterable<Canton> cantonList = CsvReader.readCantons();
            for (Canton canton : cantonList) {
                DB4OConnector.addObject(canton);
            }
        }
        List<Commune> communes = DB4OConnector.getAll(Commune.class);
        if (communes.size() == 0) {
            Iterable<Commune> cantonList = CsvReader.readCommunes();
            for (Commune commune : cantonList) {
                DB4OConnector.addObject(commune);
            }
        }
    }
}
