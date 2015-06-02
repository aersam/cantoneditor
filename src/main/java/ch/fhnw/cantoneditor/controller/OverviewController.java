package ch.fhnw.cantoneditor.controller;

import ch.fhnw.cantoneditor.datautils.DataStorage;
import ch.fhnw.cantoneditor.views.Overview;

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
            DataStorage.init();

            Overview view = new Overview();
            view.setAllCantons(DataStorage.getAllCantons());
            view.setCurrentCantonObservable(CantonHandler.getCurrentCantonObservable());
            view.setDetailView(new CantonEditPanelController().getView());
            view.show(this);
        } catch (Exception err) {
            err.printStackTrace();

        }
    }
}
