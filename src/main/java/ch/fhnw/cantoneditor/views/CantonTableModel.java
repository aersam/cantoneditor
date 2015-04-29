package ch.fhnw.cantoneditor.views;

import java.util.List;
import java.util.function.Consumer;
import java.util.function.Supplier;

import javax.swing.table.DefaultTableModel;

import ch.fhnw.cantoneditor.model.Canton;
import ch.fhnw.observation.ComputedValue;
import ch.fhnw.observation.ValueSubscribable;

@SuppressWarnings("serial")
public class CantonTableModel extends DefaultTableModel {

    final List<Canton> cantons;
    private final ValueSubscribable[][] values;

    public CantonTableModel(List<Canton> cantons) {

        super(new String[] { TranslationManager.getInstance().Translate("Canton", "Canton"),
                TranslationManager.getInstance().Translate("CantonName", "CantonName"),
                TranslationManager.getInstance().Translate("CantonShortcut", "Shortcut"),
                TranslationManager.getInstance().Translate("CantonSwitzerlandEntry", "Entry to Switzerland"),
                TranslationManager.getInstance().Translate("CantonInhabitants", "Inhabitants"),
                TranslationManager.getInstance().Translate("CantonArea", "Area") }, cantons.size());
        values = new ValueSubscribable[cantons.size()][6];
        this.cantons = cantons;
    }

    private <T> ComputedValue<T> getValue(int row, int column, Supplier<T> getFunction, Consumer<T> writer) {
        ComputedValue<T> value = new ComputedValue<T>(getFunction, writer);
        value.addPropertyChangeListener((evt) -> {
            this.fireTableCellUpdated(row, column);
        });
        values[row][column] = value;
        return value;
    }

    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return true;
    };

    @Override
    public void setValueAt(Object aValue, int row, int column) {
        // TODO Auto-generated method stub
        values[row][column].set(aValue);

    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == 0)
            return getValue(row, column,
                    () -> cantons.get(row).getName() + " (" + cantons.get(row).getShortCut() + ")", (vl) -> {
                        Canton cnt = cantons.get(row);
                        String name, shortcut;
                        if (vl.contains("(") && vl.contains(")")) {
                            name = vl.substring(0, vl.indexOf('(') - 1).trim();
                            shortcut = vl.substring(vl.indexOf('(') + 1, vl.indexOf(')')).trim();
                        } else {
                            name = vl;
                            shortcut = null;
                        }
                        if (name != null)
                            cnt.setName(name);
                        if (shortcut != null)
                            cnt.setShortCut(shortcut);
                    });
        else if (column == 1)
            return getValue(row, column, cantons.get(row)::getName, cantons.get(row)::setName);
        else if (column == 2)
            return getValue(row, column, cantons.get(row)::getShortCut, cantons.get(row)::setShortCut);
        else if (column == 3)
            return getValue(row, column, cantons.get(row)::getEntryYear, cantons.get(row)::setEntryYear);
        else if (column == 4)
            return getValue(row, column, cantons.get(row)::getNrInhabitants, cantons.get(row)::setNrInhabitants);
        else if (column == 5)
            return getValue(row, column, cantons.get(row)::getArea, cantons.get(row)::setArea);
        return null;
    }
}
