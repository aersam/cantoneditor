package ch.fhnw.cantoneditor.views;

import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;

import ch.fhnw.cantoneditor.datautils.CsvReader;
import ch.fhnw.cantoneditor.model.Canton;

public class Overview {
    private TranslationManager tm = TranslationManager.getInstance();

    public void show() throws IOException {
//        JFrame.setDefaultLookAndFeelDecorated(true);
        try {
            UIManager.setLookAndFeel(UIManager.getSystemLookAndFeelClassName());
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException
                | UnsupportedLookAndFeelException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        JFrame frame = new JFrame(tm.Translate("OverviewTitle"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 2));
        List<Canton> cantons = new ArrayList<Canton>();
        for (Canton c : CsvReader.readCantons()) {
            cantons.add(c);
        }
        JTable table = new JTable(new CantonTableModel(cantons));
        frame.setBounds(new Rectangle(500, 200));
        frame.add(table);
        frame.setVisible(true);
    }
}
