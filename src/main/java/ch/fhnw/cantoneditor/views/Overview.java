package ch.fhnw.cantoneditor.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTable;

import ch.fhnw.cantoneditor.datautils.CsvReader;
import ch.fhnw.cantoneditor.libs.GridBagManager;
import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.command.CommandController;
import ch.fhnw.observation.ComputedValue;

public class Overview {
    private TranslationManager tm = TranslationManager.getInstance();

    public void show() throws IOException {

        JFrame frame = new JFrame(tm.Translate("OverviewTitle"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel pane = new JPanel(new GridBagLayout());
        GridBagManager manager = new GridBagManager(pane);

        List<Canton> cantons = new ArrayList<Canton>();
        for (Canton c : CsvReader.readCantons()) {
            cantons.add(c);
        }
        JTable table = new JTable(new CantonTableModel(cantons));
        manager.reset();
        manager.setX(0).setY(1).setComp(table);
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

        manager.reset().setX(0).setY(0).setComp(undoButton);
        manager.reset().setX(1).setY(0).setComp(redoButton);

        pane.add(table, new GridBagConstraints());
        frame.add(pane);
        // frame.setBounds(new Rectangle(500, 200));
        frame.pack();
        frame.setVisible(true);
    }
}
