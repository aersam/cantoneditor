package ch.fhnw.cantoneditor.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.event.ActionEvent;
import java.awt.event.WindowListener;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import org.gpl.jsplitbutton.JSplitButton;
import org.gpl.jsplitbutton.SplitButtonActionListener;

import ch.fhnw.cantoneditor.datautils.CsvReader;
import ch.fhnw.cantoneditor.datautils.DataStorage;
import ch.fhnw.cantoneditor.datautils.Searcher;
import ch.fhnw.cantoneditor.datautils.TranslationManager;
import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.cantoneditor.model.CantonTableModel;
import ch.fhnw.command.CommandController;
import ch.fhnw.command.Executable;
import ch.fhnw.observation.ComputedValue;
import ch.fhnw.observation.ObservableList;
import ch.fhnw.observation.SwingObservables;
import ch.fhnw.observation.ValueSubscribable;
import ch.fhnw.oop.led.Led;
import ch.fhnw.oop.nixienumber.BackgroundPanel;

public class Overview {
    private TranslationManager tm = TranslationManager.getInstance();
    private List<Canton> allCantons;
    private ObservableList<Canton> filteredCantons;
    private JButton btnSave;

    private int searchCount = 0;

    private ValueSubscribable<Canton> currentCantonObservable;

    private IView detailView;

    public ValueSubscribable<Canton> getCurrentCantonObservable() {
        return currentCantonObservable;
    }

    public List<Canton> getAllCantons() {
        return allCantons;
    }

    public void setAllCantons(List<Canton> allCantons) {
        this.allCantons = allCantons;

    }

    public void setCurrentCantonObservable(ValueSubscribable<Canton> currentCantonObservable) {
        this.currentCantonObservable = currentCantonObservable;

    }

    private void searchCompleted(Collection<Canton> cantons, int searchIndex) {
        if (searchIndex == searchCount) {
            filteredCantons.reset(cantons);
        }
    }

    public void show(WindowListener onClose) {
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (Exception e) {

        }
        currentCantonObservable.set(allCantons.get(0));
        JFrame frame = new JFrame();
        tm.translate("OverviewTitle").bindTo(frame::setTitle);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        if (onClose != null)
            frame.addWindowListener(onClose);

        filteredCantons = new ObservableList<>(getAllCantons());
        CantonTableModel tableModel = new CantonTableModel(filteredCantons);
        JTable table = new JTable(tableModel);
        table.setSelectionModel(tableModel.getSelectionModel(this.currentCantonObservable));
        table.setMinimumSize(new Dimension(400, 400));
        JScrollPane scroller = new JScrollPane(table);

        JSplitPane splitter = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, scroller, this.detailView.getComponent(frame));
        CsvReader.class.getResourceAsStream("/Communes.txt");

        JPanel rootPane = new JPanel(new BorderLayout());
        rootPane.add(initButtonPanel(), BorderLayout.PAGE_START);

        rootPane.add(splitter, BorderLayout.CENTER);
        // rootPane.add(new CantonEditPanel().getComponent(frame), BorderLayout.LINE_END);

        JPanel pageEndPanel = new JPanel(new BorderLayout());
        pageEndPanel.add(getLedPanel(this.currentCantonObservable, this.allCantons), BorderLayout.PAGE_START);
        pageEndPanel.add(new InhabitantsAreaPanel(getAllCantons()), BorderLayout.PAGE_END);

        rootPane.add(pageEndPanel, BorderLayout.PAGE_END);

        frame.add(rootPane);
        frame.pack();
        this.currentCantonObservable.set(getAllCantons().get(0));
        frame.setVisible(true);

    }

    private JPanel initButtonPanel() {

        PlaceholderTextField tfSearch = new PlaceholderTextField();
        new ComputedValue<>(() -> tm.translate("Search", "Search").get() + "...").bindTo(tfSearch::setPlaceholder);

        tfSearch.setPreferredSize(new Dimension(100, 30));
        ValueSubscribable<String> searchText = SwingObservables.getFromTextField(tfSearch, 200);
        searchText.addPropertyChangeListener(l -> {
            Searcher<Canton> search = new Searcher<Canton>((String) l.getNewValue(), getAllCantons());
            searchCount++;
            search.setOnFinish(of -> {
                SwingUtilities.invokeLater(() -> {
                    searchCompleted(search.getResult(), searchCount);
                });
            });
            Thread th = new Thread(search);
            th.start();
        });

        BackgroundPanel buttonPanel = new BackgroundPanel();
        GridBagManager gbm = new GridBagManager(buttonPanel);
        int x = 0;
        ImageIcon iconUndo = new ImageIcon(Overview.class.getResource("/save-icon.png"), "undo");
        btnSave = new JButton(iconUndo);
        btnSave.setPreferredSize(new Dimension(30, 30));
        btnSave.addActionListener(e -> {
            try {
                DataStorage.save();
            } catch (Exception e1) {
                // TODO Auto-generated catch block
                e1.printStackTrace();
            }

        });

        gbm.setX(x++).setFill(GridBagConstraints.NONE).setComp(btnSave);
        gbm.setX(x++).setFill(GridBagConstraints.NONE).setComp(tfSearch);
        gbm.setX(x++).setFill(GridBagConstraints.NONE).setComp(getUndoButton());
        gbm.setX(x++).setFill(GridBagConstraints.NONE).setComp(getRedoButton());

        JComboBox<TranslationManager.TranslationLocale> languages = new JComboBox<>(
                TranslationManager.getInstance().SupportedLocales);

        TranslationManager.getInstance().getLocaleObservable().bindTwoWay(SwingObservables.getFromComboBox(languages));
        gbm.setX(x++).setFill(GridBagConstraints.NONE).setComp(languages);
        gbm.setX(x++).setFill(GridBagConstraints.HORIZONTAL).setWeightX(1.0).setComp(new JLabel());
        return buttonPanel;
    }

    private static JPanel getLedPanel(ValueSubscribable<Canton> currentCanton, List<Canton> allCantons) {

        BackgroundPanel panel = new BackgroundPanel();
        // panel.setSize(panel.getWidth(), 5);
        for (Canton cnt : allCantons) {
            Canton old = cnt.copyToNew();
            String oldCommunes = String.join(",", old.getCommunes());

            ComputedValue<Boolean> hasChanged = new ComputedValue<>(() -> {
                return !cnt.getName().equals(old.getName()) || !cnt.getCapital().equals(old.getCapital())
                        || !cnt.getShortCut().equals(old.getShortCut())
                        || !(cnt.getNrInhabitants() == old.getNrInhabitants()) || !(cnt.getArea() == old.getArea())
                        || !(cnt.getNrForeigners() == old.getNrForeigners())
                        || !(cnt.getEntryYear() == old.getEntryYear())
                        || !(cnt.getNrCouncilSeats() == old.getNrCouncilSeats())
                        || !(String.join(",", cnt.getCommunes()).equals(oldCommunes));
            });
            Led flapper = new Led();
            currentCanton.bindTo(c -> {
                if (c != null) {
                    flapper.setOn(c == cnt);
                }
            });

            flapper.init(30, 30);
            flapper.setSize(30, 30);
            hasChanged.bindTo((vl) -> {
                flapper.setColor(vl.booleanValue() ? Color.GREEN : Color.RED);
            });
            panel.add(flapper);
        }
        return panel;
    }

    private static JComponent getUndoRedoButton(String translationKey, ObservableList<Executable> commands,
            Supplier<Boolean> execute) {
        // Not a nice hack, but works :)

        ImageIcon iconUndo = new ImageIcon(Overview.class.getResource("/undo-icon.png"), "undo");
        ImageIcon iconRedo = new ImageIcon(Overview.class.getResource("/redo-icon.png"), "undo");

        JSplitButton undoButton = new JSplitButton(translationKey.equals("Undo") ? iconUndo : iconRedo);
        JPopupMenu popupMenu = new JPopupMenu();
        commands.bindTo(undos -> {
            popupMenu.removeAll();

            for (int last = undos.size() - 1; last >= 0; last--) {
                Executable exe = undos.get(last);
                JMenuItem item = new JMenuItem(exe.toString());

                item.addActionListener(l -> {
                    int index = undos.size() - undos.indexOf(item) - 1;
                    for (int i = 1; i <= index; i++) {
                        execute.get();
                    }
                });
                popupMenu.add(item);
            }
        });
        undoButton.setPopupMenu(popupMenu);

        new ComputedValue<Boolean>(() -> {
            return commands.iterator().hasNext();
        }).bindTo(undoButton::setEnabled);

        undoButton.addSplitButtonActionListener(new SplitButtonActionListener() {

            @Override
            public void splitButtonClicked(ActionEvent e, JComponent originalSource) {
            }

            @Override
            public void buttonClicked(ActionEvent e) {
                execute.get();
            }
        });

        return undoButton;

    }

    public static JComponent getUndoButton() {
        // Not a nice hack, but works :)
        return getUndoRedoButton("Undo", CommandController.getDefault().getDoneCommands(),
                CommandController.getDefault()::undo);
    }

    public static JComponent getRedoButton() {
        // Not a nice hack, but works :)
        return getUndoRedoButton("Redo", CommandController.getDefault().getRedoCommands(),
                CommandController.getDefault()::redo);
    }

    public IView getDetailView() {
        return detailView;
    }

    public void setDetailView(IView detailView) {

        this.detailView = detailView;

    }

}
