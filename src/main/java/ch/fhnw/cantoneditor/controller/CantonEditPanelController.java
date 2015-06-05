package ch.fhnw.cantoneditor.controller;

import ch.fhnw.cantoneditor.views.CantonEditPanel;
import ch.fhnw.cantoneditor.views.IView;

/** Controller for CantonEditPanel. */
public class CantonEditPanelController implements IController {

    /** Always edits the currently selected canton */
    @Override
    public IView getView() {
        CantonEditPanel editPanel = new CantonEditPanel();
        CantonHandler.getCurrentCantonObservable().bindTo(editPanel::setCantonToEdit);
        return editPanel;
    }

}
