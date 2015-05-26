package ch.fhnw.cantoneditor.main;

import ch.fhnw.cantoneditor.datautils.DataStorage;
import ch.fhnw.cantoneditor.views.Overview2;

public class Program {

    public static void main(String[] args) {
        try {
            DataStorage.init();

            Overview2 v = new Overview2();
            v.show();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
