package ch.fhnw.cantoneditor.views;

import java.awt.GridLayout;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JFrame;
import javax.swing.JTable;

import ch.fhnw.cantoneditor.datautils.CsvReader;
import ch.fhnw.cantoneditor.model.Canton;

public class Overview {
    private TranslationManager tm = TranslationManager.getInstance();

    public void show() throws IOException {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame(tm.Translate("OverviewTitle"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 2));
        List<Canton> cantons = new ArrayList<Canton>();
        for (Canton c : CsvReader.readCantons()) {
            cantons.add(c);
        }
        JTable table = new JTable(new CantonTableModel(cantons));
        frame.add(table);
        frame.setBounds(new Rectangle(500, 200));
        frame.setVisible(true);
    }
}
