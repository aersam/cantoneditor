package ch.fhnw.cantoneditor.model;

import javax.swing.DefaultListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.fhnw.observation.ValueSubscribable;

@SuppressWarnings("serial")
public class CantonTableSelectionModel extends DefaultListSelectionModel {

    private CantonTableModel table;

    public CantonTableSelectionModel(CantonTableModel model, ValueSubscribable<Canton> currentCanton) {
        this.table = model;
        this.setSelectionMode(SINGLE_SELECTION);

        this.addListSelectionListener(new ListSelectionListener() {

            @Override
            public void valueChanged(ListSelectionEvent e) {

                if (!e.getValueIsAdjusting()) {
                    return;
                }
                if (isSelectionEmpty()) {
                    return;
                }
                int index = getMaxSelectionIndex();
                currentCanton.set(model.cantons.get(index));
            }
        });
    }

}
