package ch.fhnw.cantoneditor.controller;

import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import ch.fhnw.cantoneditor.datautils.DataStorage;
import ch.fhnw.cantoneditor.views.Overview;

/** The main controller for Displaying the whole view with editing and tables */
public class OverviewController {

    public OverviewController() {

    }

    public void save() {
        try {
            DataStorage.save();
        } catch (Exception err) {
            err.printStackTrace();

        }

    }

    public void show() {
        try {
            DataStorage.init();// Get the data

            Overview view = new Overview();
            view.setAllCantons(DataStorage.getAllCantons());// View all cantons. Don't exclude
                                                            // Zurich :)
            view.setCurrentCantonObservable(CantonHandler.getCurrentCantonObservable());
            view.setDetailView(new CantonEditPanelController().getView());
            view.show(new WindowAdapter() {

                @Override
                public void windowClosing(WindowEvent arg0) {
                    save();// Save data on close. This might be special, but makes handling simple
                }

                @Override
                public void windowClosed(WindowEvent arg0) {
                    System.exit(0);// Close the application reporting success
                }
            });
        } catch (Exception err) {// Should never occur, and if we are dead anyway :)
            err.printStackTrace();
            throw new RuntimeException("Fatal error when starting application", err);
        }
    }
}
