package ch.fhnw.cantoneditor.controller;

import ch.fhnw.cantoneditor.datautils.DataStorage;
import ch.fhnw.cantoneditor.views.Overview;

public class OverviewController {

    public OverviewController() {

    }

    public void show() {
        try {
            DataStorage.init();

            Overview view = new Overview();
            view.setCurrentCantonObservable(CantonHandler.getCurrentCantonObservable());
            view.setDetailView(new CantonEditPanelController().getView());
            view.show();
        } catch (Exception err) {
            err.printStackTrace();

        }
    }
}
