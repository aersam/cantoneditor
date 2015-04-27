package ch.fhnw.oop.nixienumber;

import java.awt.BorderLayout;

import javax.swing.JPanel;
import lib.miglayout.*;

/**
 * @author Dieter Holz
 */
public class NixiePanel extends JPanel {
	private final NixieNumber[] numbers = new NixieNumber[9];
	private BackgroundPanel backgroundPanel;
	private long number;

	public NixiePanel() {
		initializeComponents();
		layoutComponents();
	}

	private void initializeComponents() {
		for (int i = 0; i < numbers.length; i++) {
			numbers[i] = new NixieNumber();
		}
		backgroundPanel = new BackgroundPanel();
	}

	private void layoutComponents() {
		backgroundPanel.setLayout(new MigLayout("ins 1 2 2 2, gap 3 0", "[][][]10[][][]10[][][]", ""));
		for (NixieNumber nixie : numbers) {
			backgroundPanel.add(nixie);
		}
		setLayout(new BorderLayout());
		add(backgroundPanel, BorderLayout.CENTER);
	}

	public long getNumber() {
		return number;
	}

	public void setNumber(long newNumber) {
		number = newNumber;
		String numberString = (number < 0) ? "" : Long.toString(number);
		int panelSize = numbers.length;
		int numberSize = numberString.length();
		for (int i = 0; i < panelSize - numberSize; i++) {
			numbers[i].setNumber(-1);
		}
		int panelIndex = panelSize - numberSize;
		for (int i = 0; i < numberSize; i++) {
			String substring = numberString.substring(i, i + 1);
			numbers[panelIndex].setNumber(Integer.valueOf(substring));
			panelIndex++;
		}
	}

	public void setNumber(double newNumber) {
		setNumber(Math.round(newNumber));
	}
}
