package ch.fhnw.oop.nixienumber;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.LinearGradientPaint;
import java.awt.RenderingHints;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.geom.Point2D;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

/**
 * @author hansolo
 */
public class BackgroundPanel extends JPanel {
	private BufferedImage backgroundImage;

	public BackgroundPanel() {
		setOpaque(false);
		init();
	}

	private void init() {
		this.backgroundImage = null;
		addComponentListener(new ComponentAdapter() {
			@Override
			public void componentResized(ComponentEvent e) {
				backgroundImage = null;
			}
		});
	}

	@Override
	protected void paintComponent(java.awt.Graphics g) {
		super.paintComponent(g);

		final Graphics2D G2 = (Graphics2D) g.create();

		if (this.backgroundImage == null) {
			this.backgroundImage = createBackgroundImage();
		}

		G2.drawImage(backgroundImage, 0, 0, null);

		G2.dispose();
	}

	private BufferedImage createBackgroundImage() {
		final GraphicsConfiguration GFX_CONF = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
		final BufferedImage IMAGE = GFX_CONF.createCompatibleImage(getWidth(), getHeight(), java.awt.Transparency.TRANSLUCENT);

		final Graphics2D G2 = IMAGE.createGraphics();

		G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

		Point2D BACKGROUND_START = new Point2D.Double(0, 0);
		Point2D BACKGROUND_STOP = new Point2D.Double(0, getHeight());

		final float[] BACKGROUND_FRACTIONS =
				{
						0.0f,
						1.0f
				};

		final Color[] BACKGROUND_COLORS =
				{
						new Color(0x444444),
						new Color(0x222222)
				};

		//final java.awt.Shape BACKGROUND = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 10, 10);
		final java.awt.Shape BACKGROUND = new RoundRectangle2D.Double(0, 0, getWidth(), getHeight(), 0, 0);

		final LinearGradientPaint BACKGROUND_GRADIENT = new LinearGradientPaint(BACKGROUND_START, BACKGROUND_STOP, BACKGROUND_FRACTIONS, BACKGROUND_COLORS);

		G2.setPaint(BACKGROUND_GRADIENT);
		G2.fill(BACKGROUND);

		G2.dispose();

		return IMAGE;
	}
}
