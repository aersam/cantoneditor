package ch.fhnw.cantoneditor.views;

import javax.swing.JComponent;
import javax.swing.JFrame;

/** An interface for displaying a (partial) view */
public interface IView {
    JComponent getComponent(JFrame parentFrame);
}
