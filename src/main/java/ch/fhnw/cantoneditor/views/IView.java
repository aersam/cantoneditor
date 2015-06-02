package ch.fhnw.cantoneditor.views;

import javax.swing.JComponent;
import javax.swing.JFrame;

public interface IView {
    JComponent getComponent(JFrame parentFrame);
}
