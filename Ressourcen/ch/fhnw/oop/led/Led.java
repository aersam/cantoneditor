package ch.fhnw.oop.led;

import java.awt.Color;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GraphicsConfiguration;
import java.awt.GraphicsEnvironment;
import java.awt.Insets;
import java.awt.LinearGradientPaint;
import java.awt.Paint;
import java.awt.RadialGradientPaint;
import java.awt.Rectangle;
import java.awt.RenderingHints;
import java.awt.Transparency;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ComponentAdapter;
import java.awt.event.ComponentEvent;
import java.awt.event.ComponentListener;
import java.awt.geom.Ellipse2D;
import java.awt.geom.Point2D;
import java.awt.image.BufferedImage;
import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;
import javax.swing.JComponent;
import javax.swing.SwingConstants;
import javax.swing.Timer;


public class Led extends JComponent implements ActionListener{
    public static final String    COLOR_PROPERTY        = "COLOR";
    public static final String    BLINKING_PROPERTY     = "BLINKING";
    public static final String    ON_PROPERTY           = "ON";
    public static final String    FRAMEVISIBLE_PROPERTY = "FRAMEVISIBLE";
	private final Timer           blinkTimer;
    private Color                 color;
    private boolean               blinking;
    private boolean               on;
    private boolean               frameVisible;

    private PropertyChangeSupport propertySupport;
    private final Rectangle       INNER_BOUNDS = new Rectangle(0, 0, 100, 100);
    private final Point2D         CENTER;
    private int                   horizontalAlignment;
    private int                   verticalAlignment;
    private BufferedImage         frameImage;
    private BufferedImage         ledOffImage;
    private BufferedImage         ledOnImage;
    private BufferedImage         highlightImage;

    private boolean               square;
    private transient final       ComponentListener COMPONENT_LISTENER = new ComponentAdapter() {
        @Override
        public void componentResized(ComponentEvent event) {
            final int SIZE   = getWidth() <= getHeight() ? getWidth() : getHeight();
            Container parent = getParent();
            if ((parent != null) && (parent.getLayout() == null)) {
                if (SIZE < getMinimumSize().width || SIZE < getMinimumSize().height) {
                    setSize(getMinimumSize());
                } else if(square) {
					setSize(SIZE, SIZE);
				} else {
                    setSize(getWidth(), getHeight());
                }
            } else {
                if (SIZE < getMinimumSize().width || SIZE < getMinimumSize().height) {
                    setPreferredSize(getMinimumSize());
                } else if(square) {
					setPreferredSize(new Dimension(SIZE, SIZE));
				} else {
                    setPreferredSize(new Dimension(getWidth(), getHeight()));
                }
            }
            calcInnerBounds();
            init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        }
    };


    // ******************** Constructor ***************************************
    public Led() {
        super();
        propertySupport = new PropertyChangeSupport(this);
		blinkTimer      = new Timer(500, this);
        color           = new Color(0xFF0000);
        blinking        = false;
        on              = false;
        frameVisible    = true;

        CENTER          = new Point2D.Double();
        frameImage      = createImage(INNER_BOUNDS.width, INNER_BOUNDS.height, Transparency.TRANSLUCENT);
        ledOffImage     = createImage(INNER_BOUNDS.width, INNER_BOUNDS.height, Transparency.TRANSLUCENT);
        ledOnImage      = createImage(INNER_BOUNDS.width, INNER_BOUNDS.height, Transparency.TRANSLUCENT);
        highlightImage  = createImage(INNER_BOUNDS.width, INNER_BOUNDS.height, Transparency.TRANSLUCENT);

        horizontalAlignment = SwingConstants.CENTER;
        verticalAlignment   = SwingConstants.CENTER;
        square              = true;
        addComponentListener(COMPONENT_LISTENER);

    }


    // ******************** Initialization ************************************
    public final void init(final int WIDTH, final int HEIGHT) {
        if (WIDTH <= 1 || HEIGHT <= 1) {
            return;
        }
        if (frameImage != null) {
            frameImage.flush();
        }
        frameImage = createFrameImage(WIDTH, HEIGHT);
        if (ledOffImage != null) {
            ledOffImage.flush();
        }
        ledOffImage = createLedOffImage(WIDTH, HEIGHT);
        if (ledOnImage != null) {
            ledOnImage.flush();
        }
        ledOnImage = createLedOnImage(WIDTH, HEIGHT);
        if (highlightImage != null) {
            highlightImage.flush();
        }
        highlightImage = createHighlightImage(WIDTH, HEIGHT);

        CENTER.setLocation(WIDTH / 2.0, HEIGHT / 2.0);
    }


    // ******************** Visualization *************************************
    @Override
    protected void paintComponent(final Graphics G) {
        // Create the Graphics2D object
        final Graphics2D G2 = (Graphics2D) G.create();

        // Set the rendering hints
        G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        // Take insets into account (e.g. used by borders)
        G2.translate(INNER_BOUNDS.x, INNER_BOUNDS.y);

        G2.drawImage(frameImage, 0, 0, null);
        G2.drawImage(ledOffImage, 0, 0, null);
        if (isOn()) {
			G2.drawImage(ledOnImage, 0, 0, null);
		} else {
            G2.drawImage(highlightImage, 0, 0, null);
        }

        // Dispose the temp graphics object
        G2.dispose();
    }


    // ******************** Methods *******************************************
    public final Color getColor() {
        return color;
    }

    public final void setColor(final Color COLOR) {
        Color oldColor = color;
        color          = COLOR;
        propertySupport.firePropertyChange(COLOR_PROPERTY, oldColor, color);
	    init(INNER_BOUNDS.width, INNER_BOUNDS.height);
        repaint(INNER_BOUNDS);
    }

    public final boolean isBlinking() {
        return blinking;
    }

    public final void setBlinking(final boolean BLINKING) {
        boolean oldBlinking = blinking;
        blinking            = BLINKING;
        propertySupport.firePropertyChange(BLINKING_PROPERTY, oldBlinking, blinking);
        if (BLINKING && !blinkTimer.isRunning()) {
            blinkTimer.start();
        } else {
            blinkTimer.stop();
            on = false;
            repaint(INNER_BOUNDS);
        }
        //repaint(INNER_BOUNDS);
    }

    public final boolean isOn() {
        return on;
    }

    public final void setOn(final boolean ON) {
        boolean wasOn = on;
        on = ON;
        propertySupport.firePropertyChange(ON_PROPERTY, wasOn, on);
        repaint(INNER_BOUNDS);
    }

    public final boolean isFrameVisible() {
        return frameVisible;
    }

    public final void setFrameVisible(final boolean FRAMEVISIBLE) {
        boolean oldFrameVisible = frameVisible;
        frameVisible = FRAMEVISIBLE;
        propertySupport.firePropertyChange(FRAMEVISIBLE_PROPERTY, oldFrameVisible, frameVisible);
        repaint(INNER_BOUNDS);
    }

    public int getHorizontalAlignment() {
        return horizontalAlignment;
    }

    public void setHorizontalAlignment(final int HORIZONTAL_ALIGNMENT) {
        horizontalAlignment = HORIZONTAL_ALIGNMENT;
    }

    public int getVerticalAlignment() {
        return verticalAlignment;
    }

    public void setVerticalAlignment(final int VERTICAL_ALIGNMENT) {
        verticalAlignment = VERTICAL_ALIGNMENT;
    }

    @Override
    public void addPropertyChangeListener(final PropertyChangeListener LISTENER) {
        if (isShowing()) {
            propertySupport.addPropertyChangeListener(LISTENER);
        }
    }

    @Override
    public void removePropertyChangeListener(final PropertyChangeListener LISTENER) {
        propertySupport.removePropertyChangeListener(LISTENER);
    }

    /**
    * Calculates the area that is available for painting the display
    */
    private void calcInnerBounds() {
        final Insets INSETS = getInsets();
        INNER_BOUNDS.setBounds(INSETS.left, INSETS.top, getWidth() - INSETS.left - INSETS.right, getHeight() - INSETS.top - INSETS.bottom);
    }

    /**
     * Returns a rectangle representing the available space for drawing the
     * component taking the insets into account (e.g. given through borders etc.)
     * @return a rectangle that represents the area available for rendering the component
     */
    public Rectangle getInnerBounds() {
        return INNER_BOUNDS;
    }

    @Override
    public Dimension getMinimumSize() {
        /* Return the default size of the component
         * which will be used by ui-editors for initialization
         */
        return new Dimension(100, 100);
    }

	@Override
	public void setPreferredSize(final Dimension DIM) {
	    final int SIZE = DIM.width <= DIM.height ? DIM.width : DIM.height;
	    if (square) {
	        super.setPreferredSize(new Dimension(SIZE, SIZE));
	    } else {
	        super.setPreferredSize(DIM);
	    }
	    calcInnerBounds();
	    init(INNER_BOUNDS.width, INNER_BOUNDS.height);
	}

	@Override
	public void setSize(final int WIDTH, final int HEIGHT) {
	    final int SIZE = WIDTH <= HEIGHT ? WIDTH : HEIGHT;
	    if (square) {
	        super.setSize(SIZE, SIZE);
	    } else {
	        super.setSize(WIDTH, HEIGHT);
	    }
	    calcInnerBounds();
	    init(INNER_BOUNDS.width, INNER_BOUNDS.height);
	}

	@Override
	public void setSize(final Dimension DIM) {
	    final int SIZE = DIM.width <= DIM.height ? DIM.width : DIM.height;
	    if (square) {
	        super.setSize(new Dimension(SIZE, SIZE));
	    } else {
	        super.setSize(DIM);
	    }
	    calcInnerBounds();
	    init(INNER_BOUNDS.width, INNER_BOUNDS.height);
	}

	@Override
	public void setBounds(final Rectangle BOUNDS) {
	    if (square) {
	        if (BOUNDS.width <= BOUNDS.height) {
                // vertical (taller than wide)
                int yNew;
                switch(verticalAlignment) {
                    case SwingConstants.TOP:
                        yNew = BOUNDS.y;
                        break;
                    case SwingConstants.BOTTOM:
                        yNew = BOUNDS.y + (BOUNDS.height - BOUNDS.width);
                        break;
                    case SwingConstants.CENTER:
                    default:
                        yNew = BOUNDS.y + ((BOUNDS.height - BOUNDS.width) / 2);
                        break;
                }
                super.setBounds(BOUNDS.x, yNew, BOUNDS.width, BOUNDS.width);
            } else {
                // horizontal (wider than tall)
                int xNew;
                switch(horizontalAlignment) {
                    case SwingConstants.LEFT:
                        xNew = BOUNDS.x;
                        break;
                    case SwingConstants.RIGHT:
                        xNew = BOUNDS.x + (BOUNDS.width - BOUNDS.height);
                        break;
                    case SwingConstants.CENTER:
                    default:
                        xNew = BOUNDS.x + ((BOUNDS.width - BOUNDS.height) / 2);
                        break;
                }
                super.setBounds(xNew, BOUNDS.y, BOUNDS.height, BOUNDS.height);
            }
	    } else {
	        super.setBounds(BOUNDS);
	    }
	    calcInnerBounds();
	    init(INNER_BOUNDS.width, INNER_BOUNDS.height);
	}

	@Override
	public void setBounds(final int X, final int Y, final int WIDTH, final int HEIGHT) {
	    if (square) {
	        if (WIDTH <= HEIGHT) {
                // vertical (taller than wide)
                int yNew;
                switch(verticalAlignment) {
                    case SwingConstants.TOP:
                        yNew = Y;
                        break;
                    case SwingConstants.BOTTOM:
                        yNew = Y + (HEIGHT - WIDTH);
                        break;
                    case SwingConstants.CENTER:
                    default:
                        yNew = Y + ((HEIGHT - WIDTH) / 2);
                        break;
                }
                super.setBounds(X, yNew, WIDTH, WIDTH);
            } else {
                // horizontal (wider than tall)
                int xNew;
                switch(horizontalAlignment) {
                    case SwingConstants.LEFT:
                        xNew = X;
                        break;
                    case SwingConstants.RIGHT:
                        xNew = X + (WIDTH - HEIGHT);
                        break;
                    case SwingConstants.CENTER:
                    default:
                        xNew = X + ((WIDTH - HEIGHT) / 2);
                        break;
                }
                super.setBounds(xNew, Y, HEIGHT, HEIGHT);
            }
	    } else {
	        super.setBounds(X, Y, WIDTH, HEIGHT);
	    }
	    calcInnerBounds();
	    init(INNER_BOUNDS.width, INNER_BOUNDS.height);
	}

    /**
     * Returns a compatible image of the given size and transparency
     * @param WIDTH
     * @param HEIGHT
     * @param TRANSPARENCY
     * @return a compatible image of the given size and transparency
     */
    private BufferedImage createImage(final int WIDTH, final int HEIGHT, final int TRANSPARENCY) {
        final GraphicsConfiguration GFX_CONF = GraphicsEnvironment.getLocalGraphicsEnvironment().getDefaultScreenDevice().getDefaultConfiguration();
        if (WIDTH <= 0 || HEIGHT <= 0) {
            return GFX_CONF.createCompatibleImage(1, 1, TRANSPARENCY);
        }
        final BufferedImage IMAGE = GFX_CONF.createCompatibleImage(WIDTH, HEIGHT, TRANSPARENCY);
        return IMAGE;
    }


	// ******************** Image methods *************************************
    private BufferedImage createFrameImage(final int WIDTH, final int HEIGHT) {
        if (WIDTH <= 0 || HEIGHT <= 0) {
            return createImage(1, 1, Transparency.TRANSLUCENT);
        }
        final BufferedImage IMAGE = createImage(WIDTH, HEIGHT, Transparency.TRANSLUCENT);
        final Graphics2D    G2    = IMAGE.createGraphics();
        G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH  = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();
        final Ellipse2D SHAPE = new Ellipse2D.Double(0.15 * IMAGE_WIDTH, 0.15 * IMAGE_HEIGHT,
                                                     0.7 * IMAGE_WIDTH, 0.7 * IMAGE_HEIGHT);
        final Paint SHAPE_FILL = new LinearGradientPaint(new Point2D.Double(0.25 * IMAGE_WIDTH, 0.25 * IMAGE_HEIGHT),
                                                         new Point2D.Double(0.7379036790187178 * IMAGE_WIDTH, 0.7379036790187178 * IMAGE_HEIGHT),
                                                         new float[]{
                                                             0.0f,
                                                             0.15f,
                                                             0.26f,
                                                             0.261f,
                                                             0.85f,
                                                             1.0f
                                                         },
                                                         new Color[]{
                                                             new Color(0.0784313725f, 0.0784313725f, 0.0784313725f, 0.6470588235f),
                                                             new Color(0.0784313725f, 0.0784313725f, 0.0784313725f, 0.6470588235f),
                                                             new Color(0.1607843137f, 0.1607843137f, 0.1607843137f, 0.6470588235f),
                                                             new Color(0.1607843137f, 0.1607843137f, 0.1607843137f, 0.6431372549f),
                                                             new Color(0.7843137255f, 0.7843137255f, 0.7843137255f, 0.4039215686f),
                                                             new Color(0.7843137255f, 0.7843137255f, 0.7843137255f, 0.3450980392f)
                                                         });
        G2.setPaint(SHAPE_FILL);
        G2.fill(SHAPE);

        G2.dispose();
        return IMAGE;
    }

    private BufferedImage createLedOffImage(final int WIDTH, final int HEIGHT) {
        if (WIDTH <= 0 || HEIGHT <= 0) {
            return createImage(1, 1, Transparency.TRANSLUCENT);
        }
        final BufferedImage IMAGE = createImage(WIDTH, HEIGHT, Transparency.TRANSLUCENT);
        final Graphics2D    G2    = IMAGE.createGraphics();
        G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH    = IMAGE.getWidth();
        final int IMAGE_HEIGHT   = IMAGE.getHeight();
        final Ellipse2D GLOWOFF  = new Ellipse2D.Double(0.25 * IMAGE_WIDTH, 0.25 * IMAGE_HEIGHT,
                                                        0.5 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT);
        final Paint GLOWOFF_FILL = new LinearGradientPaint(new Point2D.Double(0.33 * IMAGE_WIDTH, 0.33 * IMAGE_HEIGHT),
                                                           new Point2D.Double(0.6694112549695429 * IMAGE_WIDTH, 0.6694112549695427 * IMAGE_HEIGHT),
                                                           new float[]{
                                                               0.0f,
                                                               0.49f,
                                                               1.0f
                                                           },
                                                           new Color[]{
                                                               color.darker().darker(),
                                                               color.darker().darker().darker(),
                                                               color.darker().darker()
                                                           });
        G2.setPaint(GLOWOFF_FILL);
        G2.fill(GLOWOFF);

        G2.dispose();
        return IMAGE;
    }

    private BufferedImage createLedOnImage(final int WIDTH, final int HEIGHT) {
        if (WIDTH <= 0 || HEIGHT <= 0) {
            return createImage(1, 1, Transparency.TRANSLUCENT);
        }
        final BufferedImage IMAGE = createImage(WIDTH, HEIGHT, Transparency.TRANSLUCENT);
        final Graphics2D    G2    = IMAGE.createGraphics();
        G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH  = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();
        final Ellipse2D GLOW = new Ellipse2D.Double(0.15 * IMAGE_WIDTH, 0.15 * IMAGE_HEIGHT,
                                                    0.7 * IMAGE_WIDTH, 0.7 * IMAGE_HEIGHT);
        final Paint GLOW_FILL = new RadialGradientPaint(new Point2D.Double(0.5 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT),
                                                        0.35f * IMAGE_WIDTH,
                                                        new float[]{
                                                            0.0f,
                                                            0.5f,
                                                            0.73f,
                                                            1.0f
                                                        },
                                                        new Color[]{
                                                            new Color(1f, 1f, 1f, 0f),
                                                            new Color(1f, 1f, 1f, 0f),
                                                            new Color(color.getRed(), color.getGreen(), color.getBlue(), 153),
                                                            new Color(color.getRed(), color.getGreen(), color.getBlue(), 25)
                                                        });
        G2.setPaint(GLOW_FILL);
        G2.fill(GLOW);

        final Ellipse2D ON     = new Ellipse2D.Double(0.25 * IMAGE_WIDTH, 0.25 * IMAGE_HEIGHT,
                                                       0.5 * IMAGE_WIDTH, 0.5 * IMAGE_HEIGHT);
        final Paint ON_FILL    = new LinearGradientPaint(new Point2D.Double(0.33 * IMAGE_WIDTH, 0.33 * IMAGE_HEIGHT),
                                                         new Point2D.Double(0.6694112549695429 * IMAGE_WIDTH, 0.6694112549695427 * IMAGE_HEIGHT),
                                                         new float[]{
                                                             0.0f,
                                                             0.49f,
                                                             1.0f
                                                         },
                                                         new Color[]{
                                                             color,
                                                             color.darker(),
                                                             color.brighter()
                                                         });
        G2.setPaint(ON_FILL);
        G2.fill(ON);

        G2.dispose();
        return IMAGE;
    }

    private BufferedImage createHighlightImage(final int WIDTH, final int HEIGHT) {
        if (WIDTH <= 0 || HEIGHT <= 0) {
            return createImage(1, 1, Transparency.TRANSLUCENT);
        }
        final BufferedImage IMAGE = createImage(WIDTH, HEIGHT, Transparency.TRANSLUCENT);
        final Graphics2D    G2    = IMAGE.createGraphics();
        G2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

        final int IMAGE_WIDTH  = IMAGE.getWidth();
        final int IMAGE_HEIGHT = IMAGE.getHeight();
        final Ellipse2D SHAPE  = new Ellipse2D.Double(0.3 * IMAGE_WIDTH, 0.3 * IMAGE_HEIGHT,
                                                      0.4 * IMAGE_WIDTH, 0.4 * IMAGE_HEIGHT);
        final Paint SHAPE_FILL = new RadialGradientPaint(new Point2D.Double(0.35 * IMAGE_WIDTH, 0.35 * IMAGE_HEIGHT),
                                                         0.205f * IMAGE_WIDTH,
                                                         new float[]{
                                                             0.0f,
                                                             1.0f
                                                         },
                                                         new Color[]{
                                                             new Color(0.7843137255f, 0.7607843137f, 0.8156862745f, 1f),
                                                             new Color(0.7843137255f, 0.7607843137f, 0.8156862745f, 0f)
                                                         });
        G2.setPaint(SHAPE_FILL);
        G2.fill(SHAPE);

        G2.dispose();
        return IMAGE;
    }


    // ******************** Event handling ************************************
    @Override
	public void actionPerformed(final ActionEvent EVENT) {
	    if (EVENT.getSource().equals(blinkTimer)) {
			on ^= true;
			repaint(INNER_BOUNDS);
		}
	}

	@Override
	public String toString() {
		return "Led";
	}
}
