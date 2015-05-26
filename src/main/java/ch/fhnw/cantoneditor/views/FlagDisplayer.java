package ch.fhnw.cantoneditor.views;

import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

@SuppressWarnings("serial")
public class FlagDisplayer extends JPanel {
    private Image img;

    public FlagDisplayer(Image img) {
        setLayout(null);
        if (img != null)
            this.setImage(img, false);
    }

    private void setImage(Image image, boolean repaint) {
        this.img = image;

        Dimension size = image == null ? new Dimension(0, 0) : new Dimension(img.getWidth(null), img.getHeight(null));
        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        if (repaint)
            repaint();
    }

    public void setImage(Image image) {
        this.setImage(image, true);
    }

    public void paintComponent(Graphics g) {
        if (img != null)
            g.drawImage(img, 0, 0, null);
    }
}
