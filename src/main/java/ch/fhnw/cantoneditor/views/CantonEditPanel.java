package ch.fhnw.cantoneditor.views;

import java.awt.GridLayout;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

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

        return simpleItems;
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
