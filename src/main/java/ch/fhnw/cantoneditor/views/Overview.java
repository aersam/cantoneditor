package ch.fhnw.cantoneditor.views;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
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
import ch.fhnw.cantoneditor.libs.GridBagManager;
import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.command.CommandController;
import ch.fhnw.observation.ComputedValue;

public class Overview {
    private TranslationManager tm = TranslationManager.getInstance();

    public void show() throws IOException {
<<<<<<< HEAD
//        JFrame.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
=======

>>>>>>> origin/master
        JFrame frame = new JFrame(tm.Translate("OverviewTitle"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

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
<<<<<<< HEAD
        frame.setBounds(new Rectangle(500, 200));
        frame.add(table);
=======
        JScrollPane scroller = new JScrollPane(table);
        manager.reset();
        manager.setX(0).setY(1).setComp(scroller);
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
>>>>>>> origin/master
        frame.setVisible(true);
    }
}
