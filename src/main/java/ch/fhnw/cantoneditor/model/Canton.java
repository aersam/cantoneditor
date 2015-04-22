package ch.fhnw.cantoneditor.model;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ch.fhnw.cantoneditor.datautils.BaseModel;
import ch.fhnw.cantoneditor.datautils.ObservableSet;

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
    public static final String NRINHABITANTS_PROPERTY = "nrInhabitants";

    private String name;
    private String shortCut;
    private int cantonNr;
    private int nrCouncilSeats;
    private int entryYear;
    private double nrForeigners;
    private int nrInhabitants;

    private ObservableSet<Language> languageId = new ObservableSet<>();
    private String capital;
    private double area;

    // Einwohnerdichte
    private double inHabitantDensity;
    private int nrCommunes;

    private ObservableSet<Commune> communes = new ObservableSet<>();

    private static Map<Integer, Canton> cantons = new HashMap<Integer, Canton>();

    private Canton(int nr) {
        this.cantonNr = nr;
        if (nr != 0) { // Special for new entries without
            cantons.put(cantonNr, this);
        }

    }

    public static Canton CreateNew() {
        return new Canton(0);
    }

    public static Canton GetById(int nr, boolean createIfNotExists) {
        if (cantons.containsKey(nr))
            return cantons.get(nr);
        if (createIfNotExists) {
            Canton c = new Canton(nr);
            return c;
        }
        return null;
    }

    public static Canton GetByShortcut(String shortcut, boolean createIfNotExists) {
        for (Canton c : cantons.values()) {
            if (c.getShortCut().equals(shortcut)) {
                return c;
            }
        }
        if (createIfNotExists) {
            Canton c = new Canton(0);
            c.setShortCut(shortcut);
            return c;
        }
        return null;
    }

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
            if (this.cantonNr != 0)
                throw new IllegalAccessError("Cannot change primary key!");
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

    public ObservableSet<Language> getLanguages() {
        return languageId;
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

    public int getNrInhabitants() {
        return nrInhabitants;
    }

    public void setNrInhabitants(int nrInhabitants) {
        if (nrInhabitants != this.nrInhabitants) {
            Object oldValue = this.nrInhabitants;
            this.nrInhabitants = nrInhabitants;
            this.pcs.firePropertyChange(NRINHABITANTS_PROPERTY, oldValue, nrInhabitants);
        }
    }

}
