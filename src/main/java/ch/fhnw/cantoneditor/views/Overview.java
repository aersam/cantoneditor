package ch.fhnw.cantoneditor.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ch.fhnw.cantoneditor.datautils.DB4OConnector;
import ch.fhnw.cantoneditor.datautils.NoDataFoundException;
import ch.fhnw.cantoneditor.libs.GridBagManager;
import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.command.CommandController;
import ch.fhnw.observation.ComputedValue;
import ch.fhnw.oop.led.Led;
import ch.fhnw.oop.splitflap.SplitFlap;

public class Overview {
    private TranslationManager tm = TranslationManager.getInstance();

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

        JPanel pane = new JPanel(new GridBagLayout());
        GridBagManager manager = new GridBagManager(pane);

        // WHY doesnt it show headers?
        List<Canton> cantons = DB4OConnector.getAll(Canton.class);
        JTable table = new JTable(new CantonTableModel(cantons));
        table.setMinimumSize(new Dimension(400, 400));
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

        manager.setWeightX(0).setX(0).setY(0).setComp(undoButton);
        manager.setWeightX(0).setX(1).setY(0).setComp(redoButton);

        manager.setWidth(1).setX(0).setY(1).setComp(scroller);
        manager.setWeightX(1).setWidth(3).setWidth(3).setX(0).setY(2).setComp(initInhabitantsAndAreaDisplay());

        frame.add(pane);
        frame.pack();

        frame.setVisible(true);
    }

    private JPanel initControlPanel() {
        JPanel controlPanel = new JPanel();
        GridBagManager localGbm = new GridBagManager(controlPanel);

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
            flapper.init(30, 30);
            flapper.setSize(30, 30);
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
        String[] nums = new String[] { "", "'", "0", "1", "2", "3", "4", "5", "6", "7", "8", "9" };
        JPanel inhabPanel = new JPanel();
        GridBagManager localGbm = new GridBagManager(inhabPanel);
        // inhabPanel.setVisible(true);

        // Upper half of flaps
        SplitFlap InhabitantsFlap0 = new SplitFlap();
        InhabitantsFlap0.setSelection(nums);
        InhabitantsFlap0.setSize(20, 20);
        SplitFlap InhabitantsFlap1 = new SplitFlap();
        InhabitantsFlap1.setSelection(nums);
        InhabitantsFlap0.setSize(20, 20);
        SplitFlap InhabitantsFlap2 = new SplitFlap();
        InhabitantsFlap2.setSelection(nums);
        InhabitantsFlap0.setSize(20, 20);
        SplitFlap InhabitantsFlap3 = new SplitFlap();
        InhabitantsFlap3.setSelection(nums);
        InhabitantsFlap0.setSize(20, 20);
        SplitFlap InhabitantsFlap4 = new SplitFlap();
        InhabitantsFlap4.setSelection(nums);
        InhabitantsFlap0.setSize(20, 20);
        SplitFlap InhabitantsFlap5 = new SplitFlap();
        InhabitantsFlap5.setSelection(nums);
        InhabitantsFlap0.setSize(20, 20);
        SplitFlap InhabitantsFlap6 = new SplitFlap();
        InhabitantsFlap6.setSelection(nums);
        InhabitantsFlap0.setSize(20, 20);
        SplitFlap InhabitantsFlap7 = new SplitFlap();
        InhabitantsFlap7.setSelection(nums);
        InhabitantsFlap0.setSize(20, 20);
        SplitFlap InhabitantsFlap8 = new SplitFlap();
        InhabitantsFlap8.setSelection(nums);
        InhabitantsFlap0.setSize(20, 20);
        SplitFlap InhabitantsFlap9 = new SplitFlap();
        InhabitantsFlap9.setSelection(nums);
        InhabitantsFlap0.setSize(20, 20);
        // Lower half of flap
        SplitFlap areaFlap0 = new SplitFlap();
        areaFlap0.setSelection(nums);
        areaFlap0.setSize(20, 20);
        SplitFlap areaFlap1 = new SplitFlap();
        areaFlap0.setSelection(nums);
        areaFlap1.setSize(20, 20);
        SplitFlap areaFlap2 = new SplitFlap();
        areaFlap0.setSelection(nums);
        areaFlap2.setSize(20, 20);
        SplitFlap areaFlap3 = new SplitFlap();
        areaFlap0.setSelection(nums);
        areaFlap3.setSize(20, 20);
        SplitFlap areaFlap4 = new SplitFlap();
        areaFlap0.setSelection(nums);
        areaFlap4.setSize(20, 20);
        SplitFlap areaFlap5 = new SplitFlap();
        areaFlap0.setSelection(nums);
        areaFlap5.setSize(20, 20);
        SplitFlap areaFlap6 = new SplitFlap();
        areaFlap0.setSelection(nums);
        areaFlap6.setSize(20, 20);
        SplitFlap areaFlap7 = new SplitFlap();
        areaFlap0.setSelection(nums);
        areaFlap7.setSize(20, 20);
        SplitFlap areaFlap8 = new SplitFlap();
        areaFlap0.setSelection(nums);
        areaFlap8.setSize(20, 20);
        SplitFlap areaFlap9 = new SplitFlap();
        areaFlap0.setSelection(nums);
        areaFlap9.setSize(20, 20);

        int x = 0;
        int y = 0;

        localGbm.setWeightX(1.0).setX(x++).setY(y).setComp(new JLabel(""));
        for (int i = 0; i < 26; i++) {
            Led led = new Led();
            led.init(5, 5);
            inhabPanel.add(led);
            // localGbm.setWeightX(0).setWeightY(0).setX(x++).setY(y).setComp(led);
        }

        // led.setSize(1, 1);
        // led.setMinimumSize(new Dimension(3, 3));

        y++;
        x = 0;
        localGbm.setWeightX(1.0).setX(x++).setY(y).setComp(new JLabel(""));
        localGbm.setX(x++).setY(y).setComp(InhabitantsFlap0);
        localGbm.setX(x++).setY(y).setComp(InhabitantsFlap1);
        localGbm.setX(x++).setY(y).setComp(InhabitantsFlap2);
        localGbm.setX(x++).setY(y).setComp(InhabitantsFlap3);
        localGbm.setX(x++).setY(y).setComp(InhabitantsFlap4);
        localGbm.setX(x++).setY(y).setComp(InhabitantsFlap5);
        localGbm.setX(x++).setY(y).setComp(InhabitantsFlap6);
        localGbm.setX(x++).setY(y).setComp(InhabitantsFlap7);
        localGbm.setX(x++).setY(y).setComp(InhabitantsFlap8);
        localGbm.setX(x++).setY(y).setComp(InhabitantsFlap9);
        y++;
        x = 0;
        localGbm.setWeightX(1.0).setX(x++).setY(y).setComp(new JLabel(""));
        localGbm.setX(x++).setY(y).setComp(areaFlap0);
        localGbm.setX(x++).setY(y).setComp(areaFlap1);
        localGbm.setX(x++).setY(y).setComp(areaFlap2);
        localGbm.setX(x++).setY(y).setComp(areaFlap3);
        localGbm.setX(x++).setY(y).setComp(areaFlap4);
        localGbm.setX(x++).setY(y).setComp(areaFlap5);
        localGbm.setX(x++).setY(y).setComp(areaFlap6);
        localGbm.setX(x++).setY(y).setComp(areaFlap7);
        localGbm.setX(x++).setY(y).setComp(areaFlap8);
        localGbm.setX(x++).setY(y).setComp(areaFlap9);

        return inhabPanel;
    }
}
