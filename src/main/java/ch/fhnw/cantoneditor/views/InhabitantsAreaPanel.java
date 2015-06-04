package ch.fhnw.cantoneditor.views;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.Rectangle;
import java.text.NumberFormat;
import java.util.List;

import javax.swing.JLabel;
import javax.swing.JPanel;

import ch.fhnw.cantoneditor.datautils.TranslationManager;
import ch.fhnw.cantoneditor.libs.GridBagManager;
import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.observation.ComputedValue;
import ch.fhnw.oop.nixienumber.BackgroundPanel;
import ch.fhnw.oop.splitflap.GlobalTimer;
import ch.fhnw.oop.splitflap.SplitFlap;

public class InhabitantsAreaPanel extends BackgroundPanel {
    TranslationManager tm;

    /**
     * Creates the lower part of the Frame, which contains the flap display to show the number of
     * citizen and the area
     */
    public InhabitantsAreaPanel(List<Canton> cantons, TranslationManager tm) {
        this.tm = tm;
        this.setMinimumSize(new Dimension(400, 200));
        GridBagManager localGbm = new GridBagManager(this);
        ComputedValue<Integer> inhabitantsHandler = new ComputedValue<>(() -> {
            int inhabs = 0;
            for (Canton c : cantons) {
                inhabs += c.getNrInhabitants();
            }
            return inhabs;
        });
        ComputedValue<Double> areaHandler = new ComputedValue<>(() -> {
            double area = 0;
            for (Canton c : cantons) {
                area += c.getArea();
            }
            return area;
        });

        int x = 0;
        int y = 0;
        SplitFlap[] inhabitantFlaps = new SplitFlap[10];
        SplitFlap[] areaFlaps = new SplitFlap[10];
        localGbm.setWeightX(1.0).setX(x++).setY(y).setComp(new JLabel(""));

        localGbm.setWeightX(0).setX(x).setY(y++).setComp(initSplitFlapPanel(inhabitantFlaps, "Inhabitants"));
        localGbm.setWeightX(0).setX(x).setY(y++).setComp(initSplitFlapPanel(areaFlaps, "Area"));

        inhabitantsHandler.bindTo(t -> {
            updateFlapText(t, inhabitantFlaps);
        });
        areaHandler.bindTo(t -> {
            updateFlapText((t == null) ? null : t.intValue(), areaFlaps);
        });
    }

    private JPanel initSplitFlapPanel(final SplitFlap[] flaps, String labelText) {
        BackgroundPanel panel = new BackgroundPanel();
        GridBagManager gbm = new GridBagManager(panel);
        int x = 0;
        for (int i = 0; i < flaps.length; i++) {
            flaps[i] = new SplitFlap();
            flaps[i].setBounds(new Rectangle(20, 20));
            flaps[i].setSize(new Dimension(20, 20));
            flaps[i].setSelection(SplitFlap.NUMERIC);
            gbm.setX(x++).setY(0).setComp(flaps[i]);
        }
        JLabel title = new JLabel();
        title.setForeground(new Color(255, 255, 255));
        title.setBackground(new Color(0, 0, 0, 1));
        Font font = new Font("Verdana", Font.BOLD, 15);
        title.setPreferredSize(new Dimension(100, 30));
        title.setFont(font);
        tm.translate(labelText).bindTo(title::setText);

        gbm.setX(x++).setFill(GridBagConstraints.HORIZONTAL).setY(0).setComp(title);
        GlobalTimer.INSTANCE.startTimer();
        return panel;
    }

    private void updateFlapText(final Integer flapValue, final SplitFlap[] flaps) {
        String flapText;
        if (flapValue == null) {
            flapText = "          ";
        } else {
            flapText = NumberFormat.getIntegerInstance().format(flapValue).replace(',', '\'');
        }
        if (flapText.length() < 10)
            flapText = new String(new char[10 - flapText.length()]).replace("\0", " ") + flapText;
        for (int i = 0; i < 10; i++) {
            flaps[i].setText(flapText.toUpperCase().substring(i, i + 1));
        }
    }
}
