package ch.fhnw.cantoneditor.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JComponent;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ch.fhnw.cantoneditor.datautils.DataStorage;
import ch.fhnw.cantoneditor.datautils.NoDataFoundException;
import ch.fhnw.cantoneditor.libs.GridBagManager;
import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.cantoneditor.model.CantonTableModel;
import ch.fhnw.command.CommandController;
import ch.fhnw.observation.ComputedValue;
import ch.fhnw.oop.led.Led;
import ch.fhnw.oop.splitflap.GlobalTimer;
import ch.fhnw.oop.splitflap.SplitFlap;

import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

public class Overview {
    private TranslationManager tm = TranslationManager.getInstance();

    public void show() throws IOException, JsonIOException, JsonSyntaxException, ClassNotFoundException,
            ParseException, NoDataFoundException {
        // JFrame.setDefaultLookAndFeelDecorated(true);

        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        JFrame frame = new JFrame(tm.Translate("OverviewTitle"));
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        frame.addWindowListener(new WindowAdapter() {

            @Override
            public void windowClosing(WindowEvent arg0) {
                try {
                    DataStorage.save();
                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }

            @Override
            public void windowClosed(WindowEvent arg0) {
                System.exit(0);
            }
        });
        Dimension minDimension = new Dimension(500, 500);
        JPanel motherOfPanes = new JPanel(new GridBagLayout());
        JPanel upperPane = new JPanel(new GridBagLayout());
        upperPane.setMinimumSize(minDimension);

        JPanel lowerPane = new JPanel(new GridBagLayout());
        lowerPane.setMinimumSize(minDimension);

        CantonEditPanel editPanel = new CantonEditPanel();
        JComponent editComp = editPanel.getComponent(frame);
        editComp.setMinimumSize(minDimension);
        JSplitPane horizontalSplitPane = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, false, upperPane, editComp);

        JSplitPane verticalSplitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, false, horizontalSplitPane, lowerPane);

        GridBagManager motherOfPanesManager = new GridBagManager(motherOfPanes);
        GridBagManager upperManager = new GridBagManager(upperPane);
        GridBagManager lowerManager = new GridBagManager(lowerPane);

        // WHY doesnt it show headers?
        List<Canton> cantons = DataStorage.getAllCantons();
        CantonHandler.setCurrentCanton(cantons.get(0));
        CantonTableModel tableModel = new CantonTableModel(cantons);
        JTable table = new JTable(tableModel);
        table.setSelectionModel(tableModel.getSelectionModel());
        table.setMinimumSize(minDimension);
        table.setSize(500, 500);

        JScrollPane scroller = new JScrollPane(table);

        JButton undoButton = new JButton(tm.Translate("Undo", "Undo"));
        JButton redoButton = new JButton(tm.Translate("Redo", "Redo"));

        new ComputedValue<Boolean>(() -> {
            return CommandController.getDefault().getDoneCommands().iterator().hasNext();
        }).bindTo(undoButton::setEnabled);
        new ComputedValue<Boolean>(() -> {
            return CommandController.getDefault().getRedoCommands().iterator().hasNext();
        }).bindTo(redoButton::setEnabled);

        undoButton.addActionListener((e) -> CommandController.getDefault().undo());
        redoButton.addActionListener((e) -> CommandController.getDefault().redo());
        int x = 0;
        int y = 0;

        motherOfPanesManager.setFill(GridBagConstraints.NONE).setWeightX(0).setX(0).setY(y).setComp(undoButton);
        motherOfPanesManager.setFill(GridBagConstraints.NONE).setWeightX(0).setX(1).setY(y++).setComp(redoButton);
        motherOfPanesManager.setWeightX(1.0).setWidth(2).setX(0).setY(1).setComp(verticalSplitPane);

        upperManager.setWidth(2).setX(0).setY(y++).setComp(scroller);

        lowerManager.setWeightX(1).setWidth(3).setWidth(3).setX(0).setY(y++).setComp(getLedPanel());
        lowerManager.setWeightX(1).setWidth(3).setWidth(3).setX(0).setY(y++).setComp(initInhabitantsAndAreaDisplay());

        frame.add(motherOfPanes);
        frame.pack();

        frame.setVisible(true);
    }

    private JPanel initControlPanel() {
        JPanel controlPanel = new JPanel();
        GridBagManager localGbm = new GridBagManager(controlPanel);
        controlPanel.add(new JButton("Bla"));
        return controlPanel;
    }

    private JPanel getLedPanel() {
        List<Canton> cantons = DataStorage.getAllCantons();
        JPanel panel = new JPanel();
        // panel.setSize(panel.getWidth(), 5);
        for (Canton cnt : cantons) {
            Canton old = cnt.copyToNew();

            ComputedValue<Boolean> hasChanged = new ComputedValue<>(() -> {
                return !cnt.getName().equals(old.getName()) || !cnt.getCapital().equals(old.getCapital())
                        || !cnt.getShortCut().equals(old.getShortCut())
                        || !(cnt.getNrInhabitants() == old.getNrInhabitants()) || !(cnt.getArea() == old.getArea())
                        || !(cnt.getCommunes().equals(old.getCommunes()));
            });
            Led flapper = new Led();
            flapper.init(20, 20);
            flapper.setSize(20, 20);
            hasChanged.bindTo((vl) -> {
                flapper.setColor(vl.booleanValue() ? Color.GREEN : Color.RED);
            });
            panel.add(flapper);
        }
        return panel;
    }

    /**
     * Creates the lower part of the Frame, which contains the flap display to show the number of
     * citizen and the area
     */
    private static JPanel initInhabitantsAndAreaDisplay() {

        JPanel inhabPanel = new JPanel();
        inhabPanel.setMinimumSize(new Dimension(400, 150));
        GridBagManager localGbm = new GridBagManager(inhabPanel);
        ComputedValue<Integer> inhabitantsHandler = new ComputedValue<>(() -> {
            return CantonHandler.getCurrentCanton() == null ? null : CantonHandler.getCurrentCanton()
                    .getNrInhabitants();
        });
        ComputedValue<Double> areaHandler = new ComputedValue<>(() -> {
            return CantonHandler.getCurrentCanton() == null ? null : CantonHandler.getCurrentCanton().getArea();
        });

        int x = 0;
        int y = 0;

        localGbm.setWeightX(1.0).setX(x++).setY(y).setComp(new JLabel(""));

        SplitFlap[] inhabitantFlaps = new SplitFlap[10];
        SplitFlap[] areaFlaps = new SplitFlap[10];

        localGbm.setWeightX(0).setX(x).setY(y++).setComp(initSplitFlapPanel(inhabitantFlaps));
        localGbm.setWeightX(0).setX(x).setY(y++).setComp(initSplitFlapPanel(areaFlaps));

        inhabitantsHandler.bindTo(t -> {
            updateFlapText(t, inhabitantFlaps);
        });
        areaHandler.bindTo(t -> {
            updateFlapText(t.intValue(), areaFlaps);
        });

        return inhabPanel;
    }

    private static JPanel initSplitFlapPanel(final SplitFlap[] flaps) {
        JPanel panel = new JPanel();
        GridBagManager gbm = new GridBagManager(panel);
        int x = 0;
        for (int i = 0; i < flaps.length; i++) {
            flaps[i] = new SplitFlap();
            flaps[i].setBounds(new Rectangle(20, 20));
            flaps[i].setSize(new Dimension(20, 20));
            flaps[i].setSelection(SplitFlap.NUMERIC);
            gbm.setX(x++).setY(0).setComp(flaps[i]);
        }

        GlobalTimer.INSTANCE.startTimer();
        return panel;
    }

    private static void updateFlapText(final Integer flapValue, final SplitFlap[] flaps) {
        String flapText;
        if (flapValue == null) {
            flapText = "          ";
        } else {
            flapText = NumberFormat.getIntegerInstance().format(flapValue).replace(',', '\'');
        }
        if (flapText.length() < 10)
            flapText = new String(new char[10 - flapText.length()]).replace("\0", " ") + flapText;
        for (int i = 0; i < 10; i++) {
            flaps[i].setText(flapText.toUpperCase().substring(i, i + 1));
        }
    }
}
