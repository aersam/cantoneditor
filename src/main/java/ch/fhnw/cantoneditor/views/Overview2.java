package ch.fhnw.cantoneditor.views;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ch.fhnw.cantoneditor.datautils.DB4OConnector;
import ch.fhnw.cantoneditor.datautils.NoDataFoundException;
import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.cantoneditor.model.CantonTableModel;
import ch.fhnw.command.CommandController;
import ch.fhnw.observation.ComputedValue;
import ch.fhnw.oop.led.Led;

public class Overview2 {
    private TranslationManager tm = TranslationManager.getInstance();

    public void show() {
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

        List<Canton> cantons = DB4OConnector.getAll(Canton.class);
        CantonTableModel tableModel = new CantonTableModel(cantons);
        JTable table = new JTable(tableModel);
        table.setSelectionModel(tableModel.getSelectionModel());
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

        JPanel buttonPanel = new JPanel();
        buttonPanel.add(undoButton);
        buttonPanel.add(redoButton);

        JPanel rootPane = new JPanel(new BorderLayout());
        rootPane.add(buttonPanel, BorderLayout.PAGE_START);
        rootPane.add(scroller, BorderLayout.CENTER);
        rootPane.add(getLedPanel(), BorderLayout.PAGE_END);
        rootPane.add(new CantonEditPanel().getComponent(), BorderLayout.LINE_END);
        frame.add(rootPane);
        frame.pack();
        frame.setVisible(true);
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
}
