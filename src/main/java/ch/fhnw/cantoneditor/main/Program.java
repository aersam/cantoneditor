package ch.fhnw.cantoneditor.main;

import ch.fhnw.cantoneditor.datautils.DB4OConnector;
import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.cantoneditor.views.Overview;

public class Program {

    public static void main(String[] args) {
        try {
            Overview v = new Overview();
            v.show();
            // ArrayList<Canton> cantonList = (ArrayList<Canton>)CsvReader.readCantons();
            // for (Canton canton : cantonList) {
            // DB4OConnector.addObject(canton);
            // }
            Canton c = Canton.CreateNew();
            c.setCantonNr(1);
            System.out.println("Added Canton with Capital of: " + DB4OConnector.getObject(c).getCapital());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
