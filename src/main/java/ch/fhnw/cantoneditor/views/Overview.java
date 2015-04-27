package ch.fhnw.cantoneditor.views;

import java.awt.GridLayout;
import java.awt.Rectangle;
import java.util.Locale;
import java.util.ResourceBundle;

import javax.swing.JFrame;

public class Overview {
    public void show() {
        Locale locale = Locale.getDefault();
        ResourceBundle rb = ResourceBundle.getBundle("ApplicationTranslation", locale);
        JFrame.setDefaultLookAndFeelDecorated(true);
        JFrame frame = new JFrame(rb.getString("OverviewTitle"));
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new GridLayout(3, 2));
        frame.setBounds(new Rectangle(500, 200));

        frame.setVisible(true);
    }
}
