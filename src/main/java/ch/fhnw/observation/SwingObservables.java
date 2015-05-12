package ch.fhnw.observation;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

import javax.swing.JTextField;
import javax.swing.Timer;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

public class SwingObservables {
    public static ValueSubscribable<String> getFromTextField(JTextField field) {
        ObservableValue<String> vl = new ObservableValue<String>(field.getText());

        field.getDocument().addDocumentListener(new DocumentListener() {
            Timer updateTimer;

            private void change() {
                // This void is executed twice if setText is executed -> Therefore we need a timer

                if (updateTimer == null) {
                    updateTimer = new Timer(500, new ActionListener() {

                        @Override
                        public void actionPerformed(ActionEvent e) {
                            updateTimer.stop();
                            vl.set(field.getText());
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
        });
        vl.addPropertyChangeListener(new PropertyChangeListener() {

            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if (!vl.get().equals(field.getText()))
                    field.setText(vl.get());
            }
        });
        return vl;
    }
}
