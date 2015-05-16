package ch.fhnw.cantoneditor.views;

import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.function.BiConsumer;
import java.util.function.Function;

import javax.swing.JFormattedTextField;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.SpinnerNumberModel;

import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.command.CommandController;
import ch.fhnw.observation.ComputedValue;
import ch.fhnw.observation.SwingObservables;
import ch.fhnw.observation.ValueSubscribable;

public class CantonEditPanel {

    private TranslationManager tm = TranslationManager.getInstance();

    public JPanel getComponent() {
        JPanel simpleItems = new JPanel(new GridLayout(0, 4, 5, 5));
        simpleItems.add(new JLabel(tm.Translate("Canton")));
        simpleItems.add(getTextField(Canton::getName, Canton::setName));
        simpleItems.add(new JLabel(tm.Translate("CantonNr")));

        ComputedValue<String> cantonNrDisplay = new ComputedValue<String>(
                () -> CantonHandler.getCurrentCanton() == null ? "" : CantonHandler.getCurrentCanton().getId() + "");
        JLabel nrLabel = new JLabel(cantonNrDisplay.get());
        cantonNrDisplay.bindTo(nrLabel::setText);
        simpleItems.add(nrLabel);

        simpleItems.add(new JLabel(tm.Translate("CantonShortcut")));
        simpleItems.add(getTextField(Canton::getShortCut, Canton::setShortCut));

        simpleItems.add(new JLabel(tm.Translate("NrCouncilSeats")));
        simpleItems.add(getNumberField(Canton::getNrCouncilSeats, Canton::setNrCouncilSeats, 1, 2));

        simpleItems.add(new JLabel(tm.Translate("Capital")));
        simpleItems.add(getTextField(Canton::getCapital, Canton::setCapital));

        simpleItems.add(new JLabel(tm.Translate("CantonSwitzerlandEntry")));
        simpleItems.add(getNumberField(Canton::getEntryYear, Canton::setEntryYear, 1000, 2100));

        simpleItems.add(new JLabel(tm.Translate("CantonInhabitants")));
        simpleItems.add(getNumberField(Canton::getNrInhabitants, Canton::setNrInhabitants, 1000, 2100000));

        simpleItems.add(new JLabel(tm.Translate("CantonNrForeigners")));
        simpleItems.add(getFloatField(Canton::getNrForeigners, Canton::setNrForeigners,
                NumberFormat.getPercentInstance()));

        simpleItems.add(new JLabel(tm.Translate("CantonArea")));
        simpleItems.add(getFloatField(Canton::getArea, Canton::setArea, NumberFormat.getNumberInstance()));

        return simpleItems;

    }

    private JSpinner getNumberField(Function<Canton, Integer> getValue, BiConsumer<Canton, Integer> setValue,
            int minValue, int maxValue) {
        int initvalue = CantonHandler.getCurrentCanton() == null ? minValue : getValue.apply(
                CantonHandler.getCurrentCanton()).intValue();
        JSpinner jSpinner1 = new JSpinner(new SpinnerNumberModel(initvalue, minValue, maxValue, 1));
        ComputedValue<Integer> value = new ComputedValue<Integer>(() -> CantonHandler.getCurrentCanton() == null ? 0
                : getValue.apply(CantonHandler.getCurrentCanton()), (s) -> setValue.accept(
                CantonHandler.getCurrentCanton(), s));

        ValueSubscribable<Integer> tfObservable = SwingObservables.getFromNumber(jSpinner1);

        value.bindTo(tfObservable::set);
        tfObservable.bindTo((s) -> {
            if (!s.equals(value.get()) && CantonHandler.getCurrentCanton() != null) {
                CommandController.getDefault().executePropertySet(CantonHandler.getCurrentCanton(), s, getValue,
                        setValue);
            }
        });

        return jSpinner1;
    }

    private JFormattedTextField getFloatField(Function<Canton, Double> getValue, BiConsumer<Canton, Double> setValue,
            NumberFormat format) {
        JFormattedTextField floatField = new JFormattedTextField(format);

        ComputedValue<Double> value = new ComputedValue<Double>(() -> CantonHandler.getCurrentCanton() == null ? 0.0
                : getValue.apply(CantonHandler.getCurrentCanton()), (s) -> setValue.accept(
                CantonHandler.getCurrentCanton(), s));
        ValueSubscribable<Double> tfObservable = SwingObservables.getFromFormattedTextField(floatField);
        value.bindTo(tfObservable::set);
        tfObservable.bindTo((s) -> {
            if (!s.equals(value.get()) && CantonHandler.getCurrentCanton() != null) {
                CommandController.getDefault().executePropertySet(CantonHandler.getCurrentCanton(), s, getValue,
                        setValue);
            }
        });

        return floatField;

    }

    private JTextField getTextField(Function<Canton, String> getValue, BiConsumer<Canton, String> setValue) {
        JTextField tf = new JTextField(CantonHandler.getCurrentCanton() == null ? "" : getValue.apply(CantonHandler
                .getCurrentCanton()));
        ComputedValue<String> value = new ComputedValue<String>(() -> CantonHandler.getCurrentCanton() == null ? ""
                : getValue.apply(CantonHandler.getCurrentCanton()), (s) -> setValue.accept(
                CantonHandler.getCurrentCanton(), s));
        ValueSubscribable<String> tfObservable = SwingObservables.getFromTextField(tf);

        value.bindTo(tfObservable::set);
        tfObservable.bindTo((s) -> {
            if (!s.equals(value.get()) && CantonHandler.getCurrentCanton() != null) {
                CommandController.getDefault().executePropertySet(CantonHandler.getCurrentCanton(), s, getValue,
                        setValue);
            }
        });

        return tf;
    }
}
