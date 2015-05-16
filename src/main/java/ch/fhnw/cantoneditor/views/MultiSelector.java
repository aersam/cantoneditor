package ch.fhnw.cantoneditor.views;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import java.util.Arrays;
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
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import ch.fhnw.observation.ObservableList;
import ch.fhnw.observation.PropertyChangeable;
import ch.fhnw.observation.ReadObserver;

public class MultiSelector<E> {
    JDialog dialog;
    JComponent displayPanel;
    private final JFrame frame;
    private final Iterable<E> sourceItems;
    SelectionManager<E> manager;
    JTextToggler toggler;

    private JComponent content;

    private boolean isCalculatedContent = false;

    public MultiSelector(JFrame frame, Iterable<E> items) {
        this.frame = frame;
        this.sourceItems = items;

    }

    private void onToggleButtonClick() {

        Point p = displayPanel.getLocation();
        JFrame f = (JFrame) displayPanel.getTopLevelAncestor();
        SwingUtilities.convertPointToScreen(p, f.getContentPane());
        dialog.setLocation(p.x, p.y + displayPanel.getHeight());
        dialog.setVisible(toggler.isSelected());

    }

    private void calcContent() {
        if (!this.isCalculatedContent) {
            MultiRenderer renderer = new MultiRenderer(manager);

            JList<E> list = new JList<E>();
            manager = new SelectionManager<E>(this.sourceItems, list);
            list.setBorder(BorderFactory.createLineBorder(Color.black));
            // you can ommit the manager and renderer and make multiple JList
            // selections by holding the control key down while selecting
            list.addListSelectionListener(manager);
            list.setCellRenderer(renderer);
            // toggle button

            toggler = new JTextToggler(manager.getSelectedString());
            toggler.setPreferredSize(new Dimension(150, 20));
            manager.addPropertyChangeListener(e -> toggler.setText(manager.getSelectedString()));
            // button.addActionListener(this);
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
            this.isCalculatedContent = true;
        }
    }

    public ObservableList<E> getSelectedItems() {
        this.calcContent();
        return manager.getSelectedItems();
    }

    private JComponent getContent() {
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

    static class SelectionManager<E> implements ListSelectionListener, PropertyChangeable {
        ObservableList<E> selectedItems = new ObservableList<E>();

        private Iterable<E> sourceItems;

        public static final String SelectedStringProperty = "SelectedString";
        private PropertyChangeSupport pcs = new PropertyChangeSupport(this);

        private JList<E> target;

        public SelectionManager(Iterable<E> sourceItems, JList target) {
            this.sourceItems = sourceItems;
            this.target = target;
            this.sourceChanged();
            if (sourceItems instanceof PropertyChangeable) {
                ((PropertyChangeable) sourceItems).addPropertyChangeListener(e -> {
                    this.sourceChanged();
                });
            }
        }

        public void sourceChanged() {
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
                return TranslationManager.getInstance().Translate("NoSelection", "Keine Auswahl");
            ReadObserver.notifyRead(this, SelectedStringProperty);
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

                this.pcs.firePropertyChange(SelectedStringProperty, selectedString, this.getSelectedString());
            }
        }

        public boolean isSelected(E value) {
            return selectedItems.contains(value);
        }

        public ObservableList<E> getSelectedItems() {
            return this.selectedItems;
        }

        @Override
        public void addPropertyChangeListener(PropertyChangeListener listener) {
            this.pcs.addPropertyChangeListener(listener);
        }

        @Override
        public void removePropertyChangeListener(PropertyChangeListener listener) {
            this.pcs.removePropertyChangeListener(listener);
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
