package ch.fhnw.cantoneditor.views;

import java.util.List;
import java.util.function.Supplier;

import javax.swing.table.DefaultTableModel;

import ch.fhnw.cantoneditor.datautils.ComputedValue;
import ch.fhnw.cantoneditor.model.Canton;

@SuppressWarnings("serial")
public class CantonTableModel extends DefaultTableModel {

    final List<Canton> cantons;

    public CantonTableModel(List<Canton> cantons) {
        super(new String[] { TranslationManager.getInstance().Translate("Canton", "Canton"),
                TranslationManager.getInstance().Translate("CantonShortcut", "Shortcut"),
                TranslationManager.getInstance().Translate("CantonSwitzerlandEntry", "Entry to Switzerland"),
                TranslationManager.getInstance().Translate("CantonInhabitants", "Inhabitants"),
                TranslationManager.getInstance().Translate("CantonArea", "Area") }, cantons.size());
        this.cantons = cantons;
    }

    private <T> ComputedValue<T> getValue(int row, int column, Supplier<T> getFunction) {
        ComputedValue<T> value = new ComputedValue<T>(getFunction);
        value.addPropertyChangeListener((evt) -> {
            this.fireTableCellUpdated(row, column);
        });
        return value;
    }

    @Override
    public Object getValueAt(int row, int column) {
        if (column == 0)
            return getValue(row, column, () -> cantons.get(row).getName()).get();
        else if (column == 1)
            return getValue(row, column, () -> cantons.get(row).getShortCut()).get();
        else if (column == 2)
            return getValue(row, column, () -> cantons.get(row).getEntryYear()).get();
        else if (column == 3)
            return getValue(row, column, () -> cantons.get(row).getNrInhabitants()).get();
        else if (column == 4)
            return getValue(row, column, () -> cantons.get(row).getArea()).get();
        return null;
    }
}
