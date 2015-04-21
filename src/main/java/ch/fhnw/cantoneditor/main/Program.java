package ch.fhnw.cantoneditor.main;

import ch.fhnw.cantoneditor.datautils.CsvReader;

public class Program {

    public static void main(String[] args) {
        try {
            CsvReader.readCantons();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
