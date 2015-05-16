package ch.fhnw.observation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JFormattedTextField;
import javax.swing.JSpinner;
import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SwingObservables {
    public static ValueSubscribable<Integer> getFromNumber(JSpinner spinner) {
        ObservableValue<Integer> vl = new ObservableValue<>((Integer) spinner.getValue());
        spinner.addChangeListener(new ChangeListener() {

            @Override
            public void stateChanged(ChangeEvent e) {
                Integer value = (Integer) spinner.getValue();
                if (!value.equals(vl.get()))
                    vl.set(value);
            }
        });
        vl.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                Integer value = (Integer) spinner.getValue();
                if (!value.equals(vl.get()))
                    spinner.setValue(vl.get());
            }
        });
        return vl;
    }

    /**
     * Gets a listener that fires only after given delay
     * */
    public static DocumentListener getDelayListener(ActionListener onChange, int delay) {
        return new DocumentListener() {
            Timer updateTimer;

            private void change() {
                // This void is executed twice if setText is executed -> Therefore we need a timer

                if (updateTimer == null) {
                    updateTimer = new Timer(delay, new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            updateTimer.stop();
                            onChange.actionPerformed(e);
                        }
                    });
                }
                if (updateTimer.isRunning()) {
                    updateTimer.restart();
                } else {
                    updateTimer.start();
                }
            }

            @Override
            public void removeUpdate(DocumentEvent e) {
                change();
            }

            @Override
            public void insertUpdate(DocumentEvent e) {
                change();
            }

            @Override
            public void changedUpdate(DocumentEvent e) {
                change();
            }
        };
    }

    public static ValueSubscribable<String> getFromTextField(JTextField field) {
        ObservableValue<String> vl = new ObservableValue<String>(field.getText());

        field.getDocument().addDocumentListener(getDelayListener((e) -> vl.set(field.getText()), 500));
        vl.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!vl.get().equals(field.getText()))
                    field.setText(vl.get());
            }
        });
        return vl;
    }

    private static Double getDouble(Object input) {
        if (input == null)
            return null;
        if (input instanceof Double) {
            return (Double) input;
        }
        if (input instanceof Float) {
            return ((Float) input).doubleValue();
        }
        if (input instanceof Integer) {
            return ((Integer) input).doubleValue();
        }
        return Double.parseDouble(input.toString());// Not a great fallback, but why not? :)
    }

    public static ValueSubscribable<Double> getFromFormattedTextField(JFormattedTextField field) {
        ObservableValue<Double> vl = new ObservableValue<Double>(getDouble(field.getValue()));

        field.getDocument().addDocumentListener(getDelayListener((e) -> vl.set(getDouble(field.getValue())), 500));
        vl.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!vl.get().equals(field.getText()))
                    field.setValue(vl.get());
            }
        });
        return vl;
    }
}