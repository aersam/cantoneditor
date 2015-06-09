package ch.fhnw.cantoneditor.views;

import java.awt.Dimension;

import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JFrame;
import javax.swing.JLabel;

import ch.fhnw.cantoneditor.datautils.TranslationManager;
import ch.fhnw.observation.ValueSubscribable;

/** For displaying a fatal error */
public class ErrorWindow {
    private final ValueSubscribable<String> title;
    private final Exception exception;

    public ErrorWindow(ValueSubscribable<String> title, Exception exception) {
        this.title = title;
        this.exception = exception;
    }

    public void show() {
        JFrame frame = new JFrame();
        TranslationManager.getInstance().translate("ErrorOccured", "An Error occured").bindTo(frame::setTitle);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.getContentPane().setLayout(new BoxLayout(frame.getContentPane(), BoxLayout.PAGE_AXIS));
        JLabel desc = new JLabel();
        this.title.bindTo((s) -> desc.setText(s + ": "));
        frame.getContentPane().add(desc);
        frame.getContentPane().add(Box.createVerticalStrut(20));
        frame.getContentPane().add(new JLabel(this.exception.toString()));
        frame.setMinimumSize(new Dimension(350, 200));
        frame.pack();
        frame.setVisible(true);
    }
}
