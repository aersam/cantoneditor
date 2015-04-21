package ch.fhnw.cantoneditor.model;

import java.util.List;

import ch.fhnw.cantoneditor.datautils.BaseModel;
import ch.fhnw.cantoneditor.datautils.ObservableList;

public class Canton extends BaseModel {
    public static final String NAME_PROPERTY = "name";
    public static final String SHORTCUT_PROPERTY = "shortCut";
    public static final String CANTONR_PROPERTY = "cantonNr";
    public static final String NRCOUNCILSEATS_PROPERTY = "nrCouncilSeats";
    public static final String ENTRYYEAR_PROPERTY = "entryYear";
    public static final String NRFOREIGNERS_PROPERTY = "nrForeigners";
    public static final String LANGUAGEID_PROPERTY = "languageId";
    public static final String CAPITAL_PROPERTY = "capital";
    public static final String AREA_PROPERTY = "area";
    public static final String INHABITANTDENSITY_PROPERTY = "inHabitantDensity";
    public static final String NRCOMMUNES_PROPERTY = "nrCommunes";

    private String name;
    private String shortCut;
    private int cantonNr;
    private int nrCouncilSeats;
    private int entryYear;
    private double nrForeigners;
    private ObservableList<Integer> languageId = new ObservableList<>();
    private String capital;
    private double area;

    // Einwohnerdichte
    private double inHabitantDensity;
    private int nrCommunes;

    private ObservableList<Commune> communes = new ObservableList<>();

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != this.name) {
            Object oldValue = this.name;
            this.name = name;
            this.pcs.firePropertyChange(NAME_PROPERTY, oldValue, name);
        }
    }

    public String getShortCut() {
        return shortCut;
    }

    public void setShortCut(String shortCut) {
        if (shortCut != this.shortCut) {
            Object oldValue = this.shortCut;
            this.shortCut = shortCut;
            this.pcs.firePropertyChange(SHORTCUT_PROPERTY, oldValue, shortCut);
        }
    }

    public int getCantonNr() {
        return cantonNr;
    }

    public void setCantonNr(int cantonNr) {
        if (cantonNr != this.cantonNr) {
            Object oldValue = this.cantonNr;
            this.cantonNr = cantonNr;
            this.pcs.firePropertyChange(CANTONR_PROPERTY, oldValue, cantonNr);
        }
    }

    public int getNrCouncilSeats() {
        return nrCouncilSeats;
    }

    public void setNrCouncilSeats(int nrCouncilSeats) {
        if (nrCouncilSeats != this.nrCouncilSeats) {
            Object oldValue = this.nrCouncilSeats;
            this.nrCouncilSeats = nrCouncilSeats;
            this.pcs.firePropertyChange(NRCOUNCILSEATS_PROPERTY, oldValue, nrCouncilSeats);
        }
    }

    public int getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(int entryYear) {
        if (entryYear != this.entryYear) {
            Object oldValue = this.entryYear;
            this.entryYear = entryYear;
            this.pcs.firePropertyChange(ENTRYYEAR_PROPERTY, oldValue, entryYear);
        }
    }

    public double getNrForeigners() {
        return nrForeigners;
    }

    public void setNrForeigners(double nrForeigners) {
        if (nrForeigners != this.nrForeigners) {
            Object oldValue = this.nrForeigners;
            this.nrForeigners = nrForeigners;
            this.pcs.firePropertyChange(NRFOREIGNERS_PROPERTY, oldValue, nrForeigners);
        }
    }

    public List<Integer> getLanguageId() {
        return languageId;
    }

    public void setLanguageId(List<Integer> languageId) {
        if (languageId != this.languageId) {
            Object oldValue = this.languageId;
            this.languageId = languageId;
            this.pcs.firePropertyChange(LANGUAGEID_PROPERTY, oldValue, languageId);
        }
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        if (capital != this.capital) {
            Object oldValue = this.capital;
            this.capital = capital;
            this.pcs.firePropertyChange(CAPITAL_PROPERTY, oldValue, capital);
        }
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        if (area != this.area) {
            Object oldValue = this.area;
            this.area = area;
            this.pcs.firePropertyChange(AREA_PROPERTY, oldValue, area);
        }
    }

    public double getInHabitantDensity() {
        return inHabitantDensity;
    }

    public void setInHabitantDensity(double inHabitantDensity) {
        if (inHabitantDensity != this.inHabitantDensity) {
            Object oldValue = this.inHabitantDensity;
            this.inHabitantDensity = inHabitantDensity;
            this.pcs.firePropertyChange(INHABITANTDENSITY_PROPERTY, oldValue, inHabitantDensity);
        }
    }

    public int getNrCommunes() {
        return nrCommunes;
    }

    public void setNrCommunes(int nrCommunes) {
        if (nrCommunes != this.nrCommunes) {
            Object oldValue = this.nrCommunes;
            this.nrCommunes = nrCommunes;
            this.pcs.firePropertyChange(NRCOMMUNES_PROPERTY, oldValue, nrCommunes);
        }
    }

    public List<Commune> getCommunes() {
        return communes;
    }

}
