package ch.fhnw.cantoneditor.controller;

import ch.fhnw.cantoneditor.views.CantonEditPanel;
import ch.fhnw.cantoneditor.views.IView;

public class CantonEditPanelController implements IController {

    @Override
    public IView getView() {
        CantonEditPanel editPanel = new CantonEditPanel();
        CantonHandler.getCurrentCantonObservable().bindTo(editPanel::setCantonToEdit);
        return editPanel;
    }

}
