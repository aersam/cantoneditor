package ch.fhnw.cantoneditor.main;

import java.util.ArrayList;

import ch.fhnw.cantoneditor.datautils.CsvReader;
import ch.fhnw.cantoneditor.datautils.DB4OConnector;
import ch.fhnw.cantoneditor.model.Canton;

public class Program {

    public static void main(String[] args) {
        try {
//            ArrayList<Canton> cantonList = (ArrayList<Canton>)CsvReader.readCantons();
//            for (Canton canton : cantonList) {
//                DB4OConnector.addObject(canton);
//            }
            Canton c = Canton.CreateNew();
            c.setCantonNr(1);
            System.out.println("Added Canton with Capital of: "+DB4OConnector.getObject(c).getCapital());
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
