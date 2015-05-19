package ch.fhnw.cantoneditor.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.ArrayList;
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

import ch.fhnw.cantoneditor.datautils.DB4OConnector;
import ch.fhnw.cantoneditor.datautils.NoDataFoundException;
import ch.fhnw.cantoneditor.libs.GridBagManager;
import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.cantoneditor.model.CantonTableModel;
import ch.fhnw.command.CommandController;
import ch.fhnw.observation.ComputedValue;
import ch.fhnw.oop.led.Led;
import ch.fhnw.oop.splitflap.SplitFlap;

public class Overview {
    private TranslationManager tm = TranslationManager.getInstance();
    List<SplitFlap> inhabFlaps = new ArrayList<SplitFlap>(10);
    String[] nums = new String[] { "'", "", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };

    public void show() throws IOException {
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
                    DB4OConnector.saveChanges();
                    DB4OConnector.terminate();
                } catch (NoDataFoundException e) {
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
        List<Canton> cantons = DB4OConnector.getAll(Canton.class);
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
        List<Canton> cantons = DB4OConnector.getAll(Canton.class);
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
    private JPanel initInhabitantsAndAreaDisplay() {

        JPanel inhabPanel = new JPanel();
        GridBagManager localGbm = new GridBagManager(inhabPanel);
        ComputedValue<Integer> inhabitantsHandler = new ComputedValue<>(() -> {
            return CantonHandler.getCurrentCanton() == null ? null : CantonHandler.getCurrentCanton()
                    .getNrInhabitants();
        });
        ComputedValue<Double> areaHandler = new ComputedValue<>(() -> {
            return CantonHandler.getCurrentCanton() == null ? null : CantonHandler.getCurrentCanton().getArea();
        });

        String areaString = areaHandler.get() == null ? "" : areaHandler.toString();
        String inhabitantsString = inhabitantsHandler.get() == null ? "" : inhabitantsHandler.toString();

        int x = 0;
        int y = 0;

        localGbm.setWeightX(1.0).setX(x++).setY(y).setComp(new JLabel(""));

        for (int i = 0; i < 10; i++) {
            // Upper half of flaps
            SplitFlap inhabitantsFlap0 = new SplitFlap();
            inhabFlaps.add(inhabitantsFlap0);
            inhabitantsFlap0.setSelection(nums);
            inhabitantsFlap0.setSize(20, 20);

            if (i == 3 && inhabitantsHandler.get() > 999 || i == 5 && inhabitantsHandler.get() > 999999) {
                SplitFlap apostrofFlap = new SplitFlap();
                localGbm.setX(x++).setY(y).setComp(apostrofFlap);
                apostrofFlap.setText("'");
                apostrofFlap.setSize(20, 20);
            }
            localGbm.setX(x++).setY(y).setComp(inhabitantsFlap0);
        }
        int bla = inhabFlaps.size();
        inhabitantsHandler.bindTo(nv -> {
            String value = nv.toString();
            int len = value.length();
            if (len > 0) {
                if (len < 10) {
                    for (int i = 0; i < 10 - len; i++) {
                        value = " " + value;
                    }
                }
                for (int i = 0; i < 10; i++) {
                    // inhabFlaps.get(i).setSelection(nums);
                    // inhabFlaps.get(i).setText(value.charAt(i) + "");
                if (value.charAt(i) != ' ') {
                    while (!inhabFlaps.get(i).getNextText().equals(value.charAt(i) + "")) {
                        inhabFlaps.get(i).flipForward();
                    }
                }
            }
        }
    })  ;

        // y++;
        // x = 0;
        // localGbm.setWeightX(1.0).setX(x++).setY(y).setComp(new JLabel(""));
        // for (int i = 0; i < 10; i++) {
        // // Lower half of flap
        // SplitFlap areaFlap0 = new SplitFlap();
        // areaFlap0.setSelection(nums);
        // areaFlap0.setSize(20, 20);
        // if (i == 3 && areaHandler.get() > 999 || i == 5 && areaHandler.get() > 999999) {
        // localGbm.setX(x++).setY(y).setComp(areaFlap0);
        // areaFlap0.setText("'");
        // areaFlap0 = new SplitFlap();
        // areaFlap0.setSelection(nums);
        // areaFlap0.setSize(20, 20);
        // }
        // localGbm.setX(x++).setY(y).setComp(areaFlap0);
        // if (!areaString.equals("")) {
        // areaFlap0.setText(areaString.charAt(areaString.length() - 1 - i) + "");
        // }
        // }

        return inhabPanel;
    }
}
