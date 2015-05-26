package ch.fhnw.cantoneditor.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Collectors;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.DefaultListModel;
import javax.swing.Icon;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.fhnw.cantoneditor.datautils.ListUtils;
import ch.fhnw.observation.ComputedValue;
import ch.fhnw.observation.ObservableList;
import ch.fhnw.observation.PropertyChangeable;

public class MultiSelector<E> {
    JDialog dialog;
    JComponent displayPanel;
    private final JFrame frame;
    private final Collection<E> sourceItems;
    SelectionManager<E> manager;
    JTextToggler toggler;

    private JComponent content;

    private boolean isCalculatedContent = false;

    public MultiSelector(JFrame frame, Collection<E> items) {
        this.frame = frame;
        this.sourceItems = items;
    }

    private void onToggleButtonClick() {
        Point p = displayPanel.getLocationOnScreen();
        dialog.setLocation(p.x, p.y + displayPanel.getHeight());
        dialog.setVisible(toggler.isSelected());
    }

    private void calcContent() {
        if (!this.isCalculatedContent) {

            JList<E> list = new JList<E>();
            manager = new SelectionManager<E>(this.sourceItems, list);
            manager.getSelectedItems().addPropertyChangeListener(l -> {
                if (l.getPropertyName() == ObservableList.RESET_ACTION) {
                    if (dialog != null)// Hacky, but working :)
                        dialog.setVisible(false);
                }
            });
            MultiRenderer<E> renderer = new MultiRenderer<E>(manager);
            list.setBorder(BorderFactory.createLineBorder(Color.black));
            // you can ommit the manager and renderer and make multiple JList
            // selections by holding the control key down while selecting
            list.addListSelectionListener(manager);
            list.setCellRenderer(renderer);
            // toggle button

            toggler = new JTextToggler(manager.getSelectedString());
            toggler.setPreferredSize(new Dimension(150, 20));

            new ComputedValue<>(manager::getSelectedString).bindTo((e) -> {
                toggler.setText(e);
            });

            toggler.addPropertyChangeListener(JTextToggler.IsSelectedProperty, e -> this.onToggleButtonClick());
            displayPanel = toggler;
            Dimension d = list.getPreferredSize();
            int width = toggler.getPreferredSize().width;
            d.width = d.width < width ? width : d.width;
            // an option for appearance
            list.setPreferredSize(d);
            // dialog
            dialog = new JDialog(this.frame, false);
            dialog.setUndecorated(true);
            dialog.getContentPane().add(list);
            dialog.pack();
            JPanel panel = new JPanel();
            panel.add(toggler);
            content = panel;
            frame.addMouseListener(new MouseAdapter() {
                public void mouseClicked(MouseEvent e) {
                    if (dialog != null)
                        dialog.setVisible(false);
                };
            });

            this.isCalculatedContent = true;
        }
    }

    public ObservableList<E> getSelectedItems() {
        this.calcContent();
        return manager.getSelectedItems();
    }

    public JComponent getContent() {
        this.calcContent();
        return content;
    }

    public static void main(String[] args) {
        JFrame f = new JFrame();
        f.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        f.getContentPane().add(
                new MultiSelector<String>(f, Arrays.asList("George", "Greta", "Jenny", "Anna", "Pieter", "Antonio",
                        "Susan", "Tom")).getContent());
        f.setSize(300, 145);
        f.setLocation(200, 200);
        f.setVisible(true);
    }

    static class JTextToggler extends JTextField implements PropertyChangeable {
        private boolean isSelected;

        public static final String IsSelectedProperty = "IsSelected";

        public JTextToggler(String text) {
            super(text);
            this.setEditable(false);
            this.isSelected = false;
            this.addMouseListener(new MouseAdapter() {
                @Override
                public void mouseClicked(MouseEvent e) {
                    // TODO Auto-generated method stub
                    super.mouseClicked(e);
                    isSelected = !isSelected;
                    firePropertyChange(IsSelectedProperty, isSelected, !isSelected);

                }
            });
        }

        public boolean isSelected() {
            return isSelected;
        }

        public void setSelected(boolean newValue) {
            if (newValue != isSelected) {
                isSelected = newValue;
                firePropertyChange(IsSelectedProperty, isSelected, !isSelected);
            }
        }
    }

    static class SelectionManager<E> implements ListSelectionListener {
        ObservableList<E> selectedItems = new ObservableList<E>();

        private Collection<E> sourceItems;

        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        private JList<E> target;

        public SelectionManager(Collection<E> sourceItems, JList target) {
            this.sourceItems = sourceItems;
            this.target = target;
            this.sourceChanged();
            if (sourceItems instanceof PropertyChangeable) {
                ((PropertyChangeable) sourceItems).addPropertyChangeListener(e -> {
                    this.sourceChanged();
                });
            }
        }

        private <T> Collection<T> getCollection(javax.swing.ListModel<T> model) {
            if (model == null)
                return null;
            Collection<T> items = new java.util.ArrayList<T>();
            for (int i = 0; i < model.getSize(); i++) {
                items.add(model.getElementAt(i));
            }
            return items;
        }

        public void sourceChanged() {
            if (ListUtils.contentEquals(this.sourceItems, getCollection(this.target.getModel()))) {
                return;
            }
            DefaultListModel<E> model = new DefaultListModel<E>();
            for (E item : this.sourceItems) {
                model.addElement(item);
            }

            for (E oldItem : this.selectedItems) {
                if (!model.contains(oldItem)) {
                    selectedItems.remove(oldItem);
                }
            }
            this.target.setModel(model);
        }

        public String getSelectedString() {

            if (selectedItems.size() == 0)
                return TranslationManager.getInstance().translate("NoSelection", "Keine Auswahl");
            return selectedItems.stream().map(s -> s.toString()).collect(Collectors.joining(", "));

        }

        public void valueChanged(ListSelectionEvent e) {
            if (!e.getValueIsAdjusting()) {

                E value = ((JList<E>) e.getSource()).getSelectedValue();
                String selectedString = this.getSelectedString();
                // Toggle the selection state for value.
                if (selectedItems.contains(value)) {
                    selectedItems.remove(value);
                } else {
                    selectedItems.add(value);
                }

            }
        }

        public boolean isSelected(E value) {
            return selectedItems.contains(value);
        }

        public ObservableList<E> getSelectedItems() {
            return this.selectedItems;
        }
    }

    /** Implementation copied from source code. */
    static class MultiRenderer<E> extends DefaultListCellRenderer {
        SelectionManager<E> selectionManager;

        public MultiRenderer(SelectionManager<E> sm) {
            selectionManager = sm;
        }

        public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected,
                boolean cellHasFocus) {
            setComponentOrientation(list.getComponentOrientation());

            if (selectionManager.isSelected((E) value)) {
                setBackground(list.getSelectionBackground());
                setForeground(list.getSelectionForeground());
            } else {
                setBackground(list.getBackground());
                setForeground(list.getForeground());
            }

            if (value instanceof Icon) {
                setIcon((Icon) value);
                setText("");
            } else {
                setIcon(null);
                setText((value == null) ? "" : value.toString());
            }
            setEnabled(list.isEnabled());
            setFont(list.getFont());
            setBorder((cellHasFocus) ? UIManager.getBorder("List.focusCellHighlightBorder") : noFocusBorder);
            return this;
        }
    }

}
