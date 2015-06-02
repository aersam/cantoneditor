package ch.fhnw.cantoneditor.views;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.Image;
import java.awt.Insets;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.text.NumberFormat;
import java.util.Arrays;
import java.util.Collection;
import java.util.function.BiConsumer;
import java.util.function.Function;
import java.util.stream.Collectors;

import javax.imageio.ImageIO;
import javax.swing.BoxLayout;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFormattedTextField;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSpinner;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.SpinnerNumberModel;
import javax.swing.border.EmptyBorder;

import ch.fhnw.cantoneditor.datautils.TranslationManager;
import ch.fhnw.cantoneditor.libs.GridBagManager;
import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.cantoneditor.model.Language;
import ch.fhnw.command.CommandController;
import ch.fhnw.command.ListAddCommand;
import ch.fhnw.command.ListRemoveCommand;
import ch.fhnw.observation.ComputedValue;
import ch.fhnw.observation.ObservableList;
import ch.fhnw.observation.ObservableValue;
import ch.fhnw.observation.SwingObservables;
import ch.fhnw.observation.ValueSubscribable;

public class CantonEditPanel implements IView {

    private TranslationManager tm = TranslationManager.getInstance();

    private ValueSubscribable<Canton> editingCanton = new ObservableValue<Canton>();

    public Canton getCantonToEdit(Canton c) {
        return this.editingCanton.get();
    }

    public void setCantonToEdit(Canton c) {
        this.editingCanton.set(c);
    }

    public JComponent getComponent(JFrame frame) {
        JPanel panel = new JPanel();

        panel.setLayout(new BoxLayout(panel, BoxLayout.PAGE_AXIS));
        panel.setBorder(new EmptyBorder(5, 5, 5, 5));
        FlagDisplayer displayer = new FlagDisplayer(null);
        ComputedValue<Image> flag = new ComputedValue<Image>(() -> {
            Canton current = this.editingCanton.get();
            if (current == null)
                return null;
            return getFlagFromCanton(current.getShortCut());
        });
        flag.bindTo(displayer::setImage);

        JPanel flagpanel = new JPanel(new BorderLayout());
        flagpanel.add(displayer, BorderLayout.LINE_START);

        panel.add(flagpanel);

        panel.add(getEditingGrid(frame));

        JLabel label = new JLabel();
        tm.translate("CantonCommunities", "Communities").bindTo(label::setText);
        JPanel labelpanel = new JPanel(new BorderLayout());
        labelpanel.add(label, BorderLayout.LINE_START);

        panel.add(labelpanel);

        JScrollPane commScroller = new JScrollPane(getTextArea(
                c -> String.join(", ", c.getCommunes()),
                (c, str) -> {

                    c.getCommunes().reset(
                            Arrays.asList(str.split(",")).stream().map(s -> s.trim()).collect(Collectors.toList()));
                }));
        commScroller.setPreferredSize(new Dimension(200, 100));
        commScroller.setHorizontalScrollBarPolicy(JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
        commScroller.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED);
        panel.add(commScroller);

        this.editingCanton.bindTo(t -> {
            if (panel.isVisible() != (t != null)) {
                panel.setVisible(t != null);
            }
        });

        return panel;

    }

    private BufferedImage getFlagFromCanton(String shortcut) {
        try {
            java.io.InputStream stream = CantonEditPanel.class.getResourceAsStream("/wappen/" + shortcut + ".png");
            if (stream == null)
                return null;
            BufferedImage img = ImageIO.read(stream);
            return img;
        } catch (IOException err) {
            return null;
        }
    }

    private JLabel getLabel(String translationKey) {
        JLabel label = new JLabel();
        tm.translate(translationKey).bindTo(label::setText);
        return label;
    }

    private JPanel getEditingGrid(JFrame frame) {

        JPanel simpleItems = new JPanel(new GridBagLayout());
        GridBagManager man = new GridBagManager(simpleItems);

        int x = 0;
        int y = 0;
        man.setX(x++).setY(y).setComp(getLabel("Canton"));
        man.setX(x++).setY(y).setComp(getTextField(Canton::getName, Canton::setName));
        man.setX(x++).setY(y).setComp(getLabel("CantonNr"));

        ComputedValue<String> cantonNrDisplay = new ComputedValue<String>(() -> this.editingCanton.get() == null ? ""
                : this.editingCanton.get().getId() + "");
        JLabel nrLabel = new JLabel(cantonNrDisplay.get());
        cantonNrDisplay.bindTo(nrLabel::setText);
        man.setX(x).setY(y++).setComp(nrLabel);

        x = 0;

        man.setX(x++).setY(y).setComp(getLabel("CantonShortcut"));
        man.setX(x++).setY(y).setComp(getTextField(Canton::getShortCut, Canton::setShortCut));

        man.setX(x++).setY(y).setComp(getLabel("NrCouncilSeats"));
        man.setX(x).setY(y++).setComp(getNumberField(Canton::getNrCouncilSeats, Canton::setNrCouncilSeats, 1, 2));

        x = 0;

        man.setX(x++).setY(y).setComp(getLabel("Capital"));
        man.setX(x++).setY(y).setComp(getTextField(Canton::getCapital, Canton::setCapital));

        man.setX(x++).setY(y).setComp(getLabel("CantonSwitzerlandEntry"));
        man.setX(x).setY(y++).setComp(getNumberField(Canton::getEntryYear, Canton::setEntryYear, 1000, 2100));

        x = 0;

        man.setX(x++).setY(y).setComp(getLabel("CantonInhabitants"));
        man.setX(x++).setY(y)
                .setComp(getNumberField(Canton::getNrInhabitants, Canton::setNrInhabitants, 1000, 2100000));

        man.setX(x++).setY(y).setComp(getLabel("CantonNrForeigners"));
        man.setX(x)
                .setY(y++)
                .setComp(
                        getFloatField(Canton::getNrForeigners, Canton::setNrForeigners,
                                NumberFormat.getPercentInstance()));

        x = 0;
        man.setX(x++).setY(y).setComp(getLabel("CantonArea"));
        man.setX(x++).setY(y)
                .setComp(getFloatField(Canton::getArea, Canton::setArea, NumberFormat.getNumberInstance()));

        man.setX(x++).setY(y).setComp(getLabel("CantonLanguage"));
        man.setX(x)
                .setY(y)
                .setComp(
                        getMultiselector(frame, Language.getAllLanguages(), Canton::getLanguages,
                                tm.translate("CantonLanguage", "Language")).getContent());

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
        comboBox.setSelectedItem(this.editingCanton.get() == null ? null : getValue.apply(this.editingCanton.get()));
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
            Function<Canton, ObservableList<T>> getObservable, ValueSubscribable<String> name) {
        MultiSelector<T> selector = new MultiSelector<>(frame, allItems);
        if (this.editingCanton.get() != null) {
            selector.getSelectedItems().reset(getObservable.apply(this.editingCanton.get()));
        }
        ComputedValue<ObservableList<T>> value = new ComputedValue<>(() -> this.editingCanton.get() == null ? null
                : getObservable.apply(this.editingCanton.get()));
        value.bindTo(selector.getSelectedItems()::reset);
        selector.getSelectedItems().addPropertyChangeListener(
                l -> {
                    if (value.get() != null && selector.getSelectedItems() != null) {
                        if (selector.getSelectedItems().size() != value.get().size()) {
                            if (ObservableList.ADDED_ACTION.equals(l.getPropertyName())) {
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
        ComputedValue<T> value = new ComputedValue<T>(() -> this.editingCanton.get() == null ? null
                : getValue.apply(this.editingCanton.get()), (s) -> setValue.accept(this.editingCanton.get(), s));

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
            if (!(s == null ? currentValue == null : s.equals(currentValue)) && this.editingCanton.get() != null) {
                CommandController.getDefault().executePropertySet(this.editingCanton.get(), s, getValue, setValue);
            }
        });

    }

    private JSpinner getNumberField(Function<Canton, Integer> getValue, BiConsumer<Canton, Integer> setValue,
            int minValue, int maxValue) {
        int initvalue = this.editingCanton.get() == null ? minValue : getValue.apply(this.editingCanton.get())
                .intValue();
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
        JTextField tf = new JTextField(this.editingCanton.get() == null ? "" : getValue.apply(this.editingCanton.get()));
        bindObservables(tf, getValue, setValue);
        tf.setMargin(new Insets(0, 0, 0, 0));
        return tf;
    }

    private JTextArea getTextArea(Function<Canton, String> getValue, BiConsumer<Canton, String> setValue) {
        JTextArea tf = new JTextArea(this.editingCanton.get() == null ? "" : getValue.apply(this.editingCanton.get()));
        tf.setMaximumSize(new Dimension(500, 1000));
        // tf.setPreferredSize(new Dimension(200, 100));
        tf.setLineWrap(true);
        tf.setWrapStyleWord(true);
        bindObservables(tf, getValue, setValue);

        return tf;
    }
}
