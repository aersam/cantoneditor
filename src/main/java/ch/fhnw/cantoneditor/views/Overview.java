package ch.fhnw.cantoneditor.views;

import java.awt.GridLayout;
import java.awt.Rectangle;

import javax.swing.JButton;
import javax.swing.JFrame;

public class Overview {
    private TranslationManager tm = TranslationManager.getInstance();

    public void show() {
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame(tm.Translate("OverviewTitle"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 2));
        frame.add(new JButton(tm.Translate("SampleButton", "Just a text")));
        frame.setBounds(new Rectangle(500, 200));
        frame.setVisible(true);
    }
}
