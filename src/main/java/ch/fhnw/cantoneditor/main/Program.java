package ch.fhnw.cantoneditor.main;

import ch.fhnw.cantoneditor.views.Overview;

public class Program {

    public static void main(String[] args) {
        try {
            Overview v = new Overview();
            v.show();
        } catch (Exception err) {
            err.printStackTrace();
        }
    }

}
