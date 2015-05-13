package ch.fhnw.cantoneditor.views;

import java.awt.GridLayout;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import ch.fhnw.command.CommandController;
import ch.fhnw.observation.ComputedValue;
import ch.fhnw.observation.SwingObservables;
import ch.fhnw.observation.ValueSubscribable;

public class CantonEditPanel {

    private TranslationManager tm = TranslationManager.getInstance();

    public JPanel getComponent() {
        JPanel simpleItems = new JPanel(new GridLayout(0, 4, 5, 5));
        simpleItems.add(new JLabel(tm.Translate("Canton")));
        simpleItems.add(getTextField(() -> {
            return CantonHandler.getCurrentCanton() == null ? "" : CantonHandler.getCurrentCanton().getName();
        }, (s) -> CantonHandler.getCurrentCanton().setName(s)));
        simpleItems.add(new JLabel(tm.Translate("CantonNr")));

        ComputedValue<String> cantonNrDisplay = new ComputedValue<String>(
                () -> CantonHandler.getCurrentCanton() == null ? "" : CantonHandler.getCurrentCanton().getId() + "");
        JLabel nrLabel = new JLabel(cantonNrDisplay.get());
        cantonNrDisplay.bindTo(nrLabel::setText);
        simpleItems.add(nrLabel);

        simpleItems.add(new JLabel(tm.Translate("CantonShortcut")));
        simpleItems.add(getTextField(() -> {
            return CantonHandler.getCurrentCanton() == null ? "" : CantonHandler.getCurrentCanton().getShortCut();
        }, (s) -> CantonHandler.getCurrentCanton().setShortCut(s)));

        simpleItems.add(new JLabel(tm.Translate("NrCouncilSeats")));
        simpleItems.add(getNumberField(() -> {
            return CantonHandler.getCurrentCanton() == null ? 1 : CantonHandler.getCurrentCanton().getNrCouncilSeats();
        }, (s) -> CantonHandler.getCurrentCanton().setNrCouncilSeats(s), 1, 2));

        simpleItems.add(new JLabel(tm.Translate("Capital")));
        simpleItems.add(getTextField(() -> {
            return CantonHandler.getCurrentCanton() == null ? "" : CantonHandler.getCurrentCanton().getCapital();
        }, (s) -> CantonHandler.getCurrentCanton().setCapital(s)));

        simpleItems.add(new JLabel(tm.Translate("CantonSwitzerlandEntry")));
        simpleItems.add(getNumberField(() -> {
            return CantonHandler.getCurrentCanton() == null ? 1000 : CantonHandler.getCurrentCanton().getEntryYear();
        }, (s) -> CantonHandler.getCurrentCanton().setEntryYear(s), 1000, 2100));

        simpleItems.add(new JLabel(tm.Translate("CantonInhabitants")));
        simpleItems.add(getNumberField(() -> {
            return CantonHandler.getCurrentCanton() == null ? 1000 : CantonHandler.getCurrentCanton()
                    .getNrInhabitants();
        }, (s) -> CantonHandler.getCurrentCanton().setNrInhabitants(s), 1000, 21000000));

        return simpleItems;

    }

    private JSpinner getNumberField(Supplier<Integer> getValue, Consumer<Integer> setValue, int minValue, int maxValue) {
        JSpinner jSpinner1 = new JSpinner(new SpinnerNumberModel(getValue.get().intValue(), minValue, maxValue, 1));
        ComputedValue<Integer> value = new ComputedValue<>(getValue, setValue);
        ValueSubscribable<Integer> tfObservable = SwingObservables.getFromNumber(jSpinner1);

        value.bindTo(tfObservable::set);
        tfObservable.bindTo((s) -> {
            if (!s.equals(value.get())) {
                CommandController.getDefault().executePropertySet(s, value);
            }
        });

        return jSpinner1;
    }

    private JTextField getTextField(Supplier<String> getValue, Consumer<String> setValue) {
        JTextField tf = new JTextField(getValue.get());
        ComputedValue<String> value = new ComputedValue<>(getValue, setValue);
        ValueSubscribable<String> tfObservable = SwingObservables.getFromTextField(tf);

        value.bindTo(tfObservable::set);
        tfObservable.bindTo((s) -> {
            if (!s.equals(value.get())) {
                CommandController.getDefault().executePropertySet(s, value);
            }
        });

        return tf;
    }
}
