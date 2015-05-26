package ch.fhnw.cantoneditor.model;

import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.fhnw.cantoneditor.views.CantonHandler;

@SuppressWarnings("serial")
public class CantonTableSelectionModel extends DefaultListSelectionModel {

    private CantonTableModel table;

    public CantonTableSelectionModel(CantonTableModel model) {
        this.table = model;
        this.setSelectionMode(SINGLE_SELECTION);

        this.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting()) {
                    return;
                }
                if (isSelectionEmpty()) {
                    CantonHandler.setCurrentCanton(null);
                    return;
                }
                int index = getMaxSelectionIndex();
                CantonHandler.setCurrentCanton(model.cantons.get(index));
            }
        });
    }

}
