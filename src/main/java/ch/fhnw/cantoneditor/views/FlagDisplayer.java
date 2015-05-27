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

        Dimension size = getDimensions();

        setPreferredSize(size);
        setMinimumSize(size);
        setMaximumSize(size);
        setSize(size);
        if (repaint)
            repaint();
    }

    private Dimension getDimensions() {
        int width = 0;
        int height = 0;

        // width = 150;
        // height = 150;
        if (img != null) {
            width = img.getWidth(null);
            height = img.getHeight(null);
            int maxwidth = width / 2;
            int maxheight = 2000;
            if (width > maxwidth) {
                height = (int) (((double) maxwidth / width) * height);
                width = maxwidth;
            }
            if (height > maxheight) {
                width = (int) (((double) maxheight / width) * width);
                height = maxheight;
            }
        }
        return new Dimension(width, height);
    }

    public void setImage(Image image) {
        this.setImage(image, true);
    }

    public void paintComponent(Graphics g) {
        if (img != null) {
            Dimension dim = getDimensions();
            g.drawImage(img, 0, 0, (int) dim.getWidth(), (int) dim.getHeight(), null);
        }
    }
}
