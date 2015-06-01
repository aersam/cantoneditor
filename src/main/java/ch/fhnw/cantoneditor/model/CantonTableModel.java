package ch.fhnw.cantoneditor.model;

import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;

import javax.swing.ListSelectionModel;
import javax.swing.table.DefaultTableModel;

import ch.fhnw.cantoneditor.views.TranslationManager;
import ch.fhnw.command.CommandController;
import ch.fhnw.observation.ComputedValue;
import ch.fhnw.observation.Disposable;
import ch.fhnw.observation.ObservableList;
import ch.fhnw.observation.ValueSubscribable;

@SuppressWarnings("serial")
public class CantonTableModel extends DefaultTableModel implements Disposable {

    final List<Canton> cantons;
    @SuppressWarnings("rawtypes")
    private ValueSubscribable[][] values;
    @SuppressWarnings("rawtypes")
    private Function[][] valueConverter;

    private static List<ValueSubscribable<String>> columnNames = Arrays.asList(TranslationManager.getInstance()
            .translate("Canton", "Canton"), TranslationManager.getInstance().translate("CantonName", "CantonName"),
            TranslationManager.getInstance().translate("CantonShortcut", "Shortcut"), TranslationManager.getInstance()
                    .translate("CantonSwitzerlandEntry", "Entry to Switzerland"), TranslationManager.getInstance()
                    .translate("CantonInhabitants", "Inhabitants"),
            TranslationManager.getInstance().translate("CantonArea", "Area"));

    public CantonTableModel(List<Canton> cantons) {

        super(cantons.size(), columnNames.size());
        for (ValueSubscribable<String> vl : columnNames) {
            vl.addPropertyChangeListener(l -> this.fireTableStructureChanged());
        }
        values = new ValueSubscribable[cantons.size()][6];
        valueConverter = new Function[cantons.size()][6];
        this.cantons = cantons;
        if (cantons instanceof ObservableList<?>) {
            ((ObservableList<Canton>) cantons).addPropertyChangeListener(l -> {
                for (int r = 0; r < values.length; r++) {
                    for (int c = 0; c < values[r].length; c++) {
                        if (values[r][c] instanceof Disposable) {
                            ((Disposable) values[r][c]).dispose();
                        }
                    }
                }
                for (int r = 0; r < valueConverter.length; r++) {
                    for (int c = 0; c < valueConverter[r].length; c++) {
                        if (valueConverter[r][c] instanceof Disposable) {
                            ((Disposable) valueConverter[r][c]).dispose();
                        }
                    }
                }

                values = new ValueSubscribable[cantons.size()][6];
                valueConverter = new Function[cantons.size()][6];

                this.fireTableDataChanged();
            });
        }
    }

    @Override
    public String getColumnName(int column) {
        return columnNames.get(column).get();
    };

    @Override
    public int getRowCount() {
        if (cantons == null)
            return super.getRowCount();
        return cantons.size();
    }

    public ListSelectionModel getSelectionModel() {
        return new CantonTableSelectionModel(this);
    }

    private <T> ValueSubscribable<?> getValue(int row, int column, Supplier<T> getFunction, Consumer<T> writer,
            Function<String, T> toString) {
        if (values[row][column] != null)
            return values[row][column];
        ComputedValue<T> value = new ComputedValue<T>(getFunction, writer);
        value.addPropertyChangeListener((evt) -> {
            this.fireTableCellUpdated(row, column);
        });
        valueConverter[row][column] = toString;
        values[row][column] = value;
        return value;
    }

    /** Cleans Event-Listeners up. Do not use the object after this anymore */
    @Override
    public void dispose() {
        if (valueConverter != null) {
            valueConverter = null;
        }
        if (values != null) {
            for (int row = 0; row < valueConverter.length; row++) {
                for (int column = 0; column < valueConverter[row].length; column++) {
                    if (values[row][column] instanceof Disposable) {
                        ((Disposable) values[row][column]).dispose();
                    }
                }
            }
            values = null;
        }
    }

    @Override
    public boolean isCellEditable(int arg0, int arg1) {
        return true;
    };

    @SuppressWarnings({ "rawtypes", "unchecked" })
    @Override
    public void setValueAt(Object aValue, int row, int column) {
        // TODO Auto-generated method stub

        Function converter = valueConverter[row][column];
        if (converter == null) {
            CommandController.getDefault().executePropertySet(aValue, values[row][column]);
        } else {
            CommandController.getDefault().executePropertySet(converter.apply(aValue), values[row][column]);
        }
    }

    @Override
    public Object getValueAt(int row, int column) {
        Canton canton = cantons.get(row);
        if (column == 0)
            return getValue(row, column, () -> canton.getName() + " (" + canton.getShortCut() + ")", (vl) -> {
                String name, shortcut;
                if (vl.contains("(") && vl.contains(")")) {
                    name = vl.substring(0, vl.indexOf('(') - 1).trim();
                    shortcut = vl.substring(vl.indexOf('(') + 1, vl.indexOf(')')).trim();
                } else {
                    name = vl;
                    shortcut = null;
                }
                if (name != null)
                    canton.setName(name);
                if (shortcut != null)
                    canton.setShortCut(shortcut);
            }, null);
        else if (column == 1)
            return getValue(row, column, canton::getName, canton::setName, null);
        else if (column == 2)
            return getValue(row, column, canton::getShortCut, canton::setShortCut, null);
        else if (column == 3)
            return getValue(row, column, canton::getEntryYear, canton::setEntryYear, Integer::parseInt);
        else if (column == 4)
            return getValue(row, column, canton::getNrInhabitants, canton::setNrInhabitants, Integer::parseInt);
        else if (column == 5)
            return getValue(row, column, canton::getArea, canton::setArea, Double::parseDouble);
        return null;
    }
}
