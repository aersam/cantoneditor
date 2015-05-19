package ch.fhnw.cantoneditor.views;

import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.NumberFormat;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;

import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.cantoneditor.model.Commune;
import ch.fhnw.cantoneditor.model.Language;
import ch.fhnw.command.CommandController;
import ch.fhnw.command.ListAddCommand;
import ch.fhnw.command.ListRemoveCommand;
import ch.fhnw.observation.ComputedValue;
import ch.fhnw.observation.ObservableList;
import ch.fhnw.observation.SwingObservables;
import ch.fhnw.observation.ValueSubscribable;

public class CantonEditPanel {

    private TranslationManager tm = TranslationManager.getInstance();

    public JPanel getComponent(JFrame frame) {
        JPanel panel = new JPanel();
        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.add(getEditingGrid(frame));

        panel.add(new JLabel(tm.Translate("CantonCommunities", "Communities")));

        panel.add(getTextArea(c -> String.join(", ",
                c.getCommunes().stream().map(s -> s.toString()).collect(Collectors.toList())), (c, str) -> {
            String[] communes = str.split(",");
            List<Commune> allCommunes = new java.util.ArrayList<Commune>();
            for (String commune : communes) {
                Optional<Commune> existing = c.getCommunes().stream().filter(s -> s.getName().equals(commune))
                        .findFirst();
                if (!existing.isPresent()) {
                    allCommunes.add(Commune.getByName(commune.trim(), true));
                } else {
                    allCommunes.add(existing.get());
                }
            }
        }));
        return panel;

    }

    private JPanel getEditingGrid(JFrame frame) {

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

        simpleItems.add(new JLabel(tm.Translate("CantonLanguage", "Language")));
        simpleItems.add(getMultiselector(frame, Language.getAllLanguages(), Canton::getLanguages,
                tm.Translate("CantonLanguage", "Language")).getContent());

        return simpleItems;

    }

    private <T> JComboBox<T> getComboBox(T[] list, Function<Canton, T> getValue, BiConsumer<Canton, T> setValue,
            Function<T, String> getText) {
        JComboBox<T> comboBox = new JComboBox<T>(list);
        comboBox.setRenderer(new ListCellRenderer<T>() {
            public java.awt.Component getListCellRendererComponent(javax.swing.JList<? extends T> list, T value,
                    int index, boolean isSelected, boolean cellHasFocus) {
                return new JTextField(getText.apply(value));
            };
        });
        comboBox.setSelectedItem(CantonHandler.getCurrentCanton() == null ? null : getValue.apply(CantonHandler
                .getCurrentCanton()));
        bindObservables(comboBox, getValue, setValue);
        return comboBox;
    }

    private <T> Collection<T> getCollection(Object o) {
        if (o instanceof Collection<?>) {
            return (Collection<T>) o;
        }
        T obj = (T) o;
        Collection<T> col = new java.util.ArrayList<T>();
        col.add(obj);
        return col;
    }

    private <T> MultiSelector<T> getMultiselector(JFrame frame, Collection<T> allItems,
            Function<Canton, ObservableList<T>> getObservable, String name) {
        MultiSelector<T> selector = new MultiSelector<>(frame, allItems);
        if (CantonHandler.getCurrentCanton() != null) {
            selector.getSelectedItems().reset(getObservable.apply(CantonHandler.getCurrentCanton()));
        }
        ComputedValue<ObservableList<T>> value = new ComputedValue<>(
                () -> CantonHandler.getCurrentCanton() == null ? null : getObservable.apply(CantonHandler
                        .getCurrentCanton()));
        value.bindTo(selector.getSelectedItems()::reset);
        selector.getSelectedItems().addPropertyChangeListener(
                l -> {
<<<<<<< HEAD
                    if (value != null && selector.getSelectedItems().size() != value.get().size()) {
                        if (ObservableList.ADDED_ACTION.equals(l.getPropertyName())) {
                            CommandController.getDefault().execute(
                                    new ListAddCommand<T>(name, value.get(), getCollection(l.getNewValue())));
                        } else if (ObservableList.REMOVE_ACTION.equals(l.getPropertyName())) {
                            Collection<T> cols = getCollection(l.getOldValue());
                            for (T item : cols) {
=======
                    if (value.get() != null && selector.getSelectedItems() != null) {
                        if (selector.getSelectedItems().size() != value.get().size()) {
                            if (ObservableList.ADDED_ACTION.equals(l.getPropertyName())) {
>>>>>>> ee16a84593d299b9fde29416a0bf2b0c445c12c9
                                CommandController.getDefault().execute(
                                        new ListAddCommand<T>(name, value.get(), getCollection(l.getNewValue())));
                            } else if (ObservableList.REMOVE_ACTION.equals(l.getPropertyName())) {
                                Collection<T> cols = getCollection(l.getOldValue());
                                for (T item : cols) {
                                    CommandController.getDefault().execute(
                                            new ListRemoveCommand<T>(name, value.get(), item));
                                }
                            } else {
                                throw new IllegalArgumentException("List not supported!");
                            }
                        }
                    }
                });
        return selector;
    }

    private <T> void bindObservables(JComponent comp, Function<Canton, T> getValue, BiConsumer<Canton, T> setValue) {
        ComputedValue<T> value = new ComputedValue<T>(() -> CantonHandler.getCurrentCanton() == null ? null
                : getValue.apply(CantonHandler.getCurrentCanton()), (s) -> setValue.accept(
                CantonHandler.getCurrentCanton(), s));

        ValueSubscribable<T> tfObservable;
        if (comp.getClass() == JComboBox.class) {
            tfObservable = SwingObservables.getFromComboBox((JComboBox<T>) comp);
        } else if (comp.getClass() == JSpinner.class) {
            tfObservable = (ValueSubscribable<T>) SwingObservables.getFromNumber((JSpinner) comp);
        } else if (comp.getClass() == JFormattedTextField.class) {
            tfObservable = (ValueSubscribable<T>) SwingObservables
                    .getFromFormattedTextField((JFormattedTextField) comp);
        } else if (comp.getClass() == JTextField.class) {
            tfObservable = (ValueSubscribable<T>) SwingObservables.getFromTextField((JTextField) comp, 500);
        } else if (comp.getClass() == JTextArea.class) {
            tfObservable = (ValueSubscribable<T>) SwingObservables.getFromTextArea((JTextArea) comp);
        } else {
            throw new IllegalArgumentException("Component not supported!");
        }

        value.bindTo(tfObservable::set);
        tfObservable.bindTo((s) -> {
            T currentValue = value.get();
            if (!(s == null ? currentValue == null : s.equals(currentValue))
                    && CantonHandler.getCurrentCanton() != null) {
                CommandController.getDefault().executePropertySet(CantonHandler.getCurrentCanton(), s, getValue,
                        setValue);
            }
        });

    }

    private JSpinner getNumberField(Function<Canton, Integer> getValue, BiConsumer<Canton, Integer> setValue,
            int minValue, int maxValue) {
        int initvalue = CantonHandler.getCurrentCanton() == null ? minValue : getValue.apply(
                CantonHandler.getCurrentCanton()).intValue();
        JSpinner jSpinner1 = new JSpinner(new SpinnerNumberModel(initvalue, minValue, maxValue, 1));

        bindObservables(jSpinner1, getValue, setValue);
        return jSpinner1;
    }

    private JFormattedTextField getFloatField(Function<Canton, Double> getValue, BiConsumer<Canton, Double> setValue,
            NumberFormat format) {
        JFormattedTextField floatField = new JFormattedTextField(format);

        bindObservables(floatField, getValue, setValue);
        return floatField;

    }

    private JTextField getTextField(Function<Canton, String> getValue, BiConsumer<Canton, String> setValue) {
        JTextField tf = new JTextField(CantonHandler.getCurrentCanton() == null ? "" : getValue.apply(CantonHandler
                .getCurrentCanton()));
        bindObservables(tf, getValue, setValue);

        return tf;
    }

    private JTextArea getTextArea(Function<Canton, String> getValue, BiConsumer<Canton, String> setValue) {
        JTextArea tf = new JTextArea(CantonHandler.getCurrentCanton() == null ? "" : getValue.apply(CantonHandler
                .getCurrentCanton()));
        tf.setMaximumSize(new Dimension(500, 1000));
        tf.setWrapStyleWord(true);
        bindObservables(tf, getValue, setValue);

        return tf;
    }
}
