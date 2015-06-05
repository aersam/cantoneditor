package ch.fhnw.cantoneditor.main;

import ch.fhnw.cantoneditor.controller.OverviewController;

/** The startclass. Launches OverviewController */
public class Program {

    public static void main(String[] args) {
        OverviewController cont = new OverviewController();
        cont.show();
    }

}
