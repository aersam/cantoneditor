package ch.fhnw.cantoneditor.model;

import java.util.HashMap;
import java.util.Map;

import ch.fhnw.cantoneditor.datautils.BaseModel;
import ch.fhnw.cantoneditor.datautils.DB4OConnector;
import ch.fhnw.observation.ObservableSet;

public class Canton extends BaseModel {
    public static final String NAME_PROPERTY = "name";
    public static final String SHORTCUT_PROPERTY = "shortCut";
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

    private boolean isInited = false;

    private Canton(int nr) {

        this.cantonNr = nr;
        if (nr != 0) { // Special for new entries without
            cantons.put(cantonNr, this);

        }
    }

    public void init() {
        if (isInited)
            return;
        isInited = true;
        this.addPropertyChangeListener((evt) -> {
            if (this.cantonNr != 0) {
                DB4OConnector.markChanged(this);
            }
        });
    }

    public static Canton createNew() {
        return new Canton(0);
    }

    public static Canton getById(int nr, boolean createIfNotExists) {
        if (cantons.containsKey(nr))
            return cantons.get(nr);
        if (createIfNotExists) {
            Canton c = new Canton(nr);
            return c;
        }
        return null;
    }

    public static Canton getByShortcut(String shortcut, boolean createIfNotExists) {
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
        this.notifyPropertyRead(NAME_PROPERTY);
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
        this.notifyPropertyRead(SHORTCUT_PROPERTY);
        return shortCut;
    }

    public void setShortCut(String shortCut) {
        if (shortCut != this.shortCut) {
            Object oldValue = this.shortCut;
            this.shortCut = shortCut;
            this.pcs.firePropertyChange(SHORTCUT_PROPERTY, oldValue, shortCut);
        }
    }

    public int getNrCouncilSeats() {
        this.notifyPropertyRead(NRCOUNCILSEATS_PROPERTY);
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
        this.notifyPropertyRead(ENTRYYEAR_PROPERTY);
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
        this.notifyPropertyRead(NRFOREIGNERS_PROPERTY);
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
        this.notifyPropertyRead(CAPITAL_PROPERTY);
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
        this.notifyPropertyRead(AREA_PROPERTY);
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
        this.notifyPropertyRead(INHABITANTDENSITY_PROPERTY);
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
        this.notifyPropertyRead(NRCOMMUNES_PROPERTY);
        return nrCommunes;
    }

    public void setNrCommunes(int nrCommunes) {
        if (nrCommunes != this.nrCommunes) {
            Object oldValue = this.nrCommunes;
            this.nrCommunes = nrCommunes;
            this.pcs.firePropertyChange(NRCOMMUNES_PROPERTY, oldValue, nrCommunes);
        }
    }

    public ObservableSet<Commune> getCommunes() {
        return communes;
    }

    public int getNrInhabitants() {
        this.notifyPropertyRead(NRINHABITANTS_PROPERTY);
        return nrInhabitants;
    }

    public void setNrInhabitants(int nrInhabitants) {
        if (nrInhabitants != this.nrInhabitants) {
            Object oldValue = this.nrInhabitants;
            this.nrInhabitants = nrInhabitants;
            this.pcs.firePropertyChange(NRINHABITANTS_PROPERTY, oldValue, nrInhabitants);
        }
    }

    @Override
    public int hashCode() {
        if (this.cantonNr != 0) { // Not a newly created object
            return this.cantonNr;// Primary Key
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (this.cantonNr == 0)
            return false;// Two newly created Communes are newer the same
        if (obj instanceof Canton) {
            return ((Canton) obj).getId() == this.id;
        }
        return false;
    };

    @Override
    public String toString() {
        return this.getName();
    }

    /** Creates a new canton with the same properties as this canton */
    public Canton copyToNew() {
        Canton c = new Canton(0);
        c.name = this.name;
        c.shortCut = this.shortCut;
        c.nrCouncilSeats = this.nrCouncilSeats;
        c.entryYear = this.entryYear;
        c.nrForeigners = this.nrForeigners;
        c.nrInhabitants = this.nrInhabitants;
        c.capital = this.capital;
        c.area = this.area;
        c.inHabitantDensity = this.inHabitantDensity;
        c.nrCommunes = this.nrCommunes;
        for (Language lng : this.languageId) {
            c.getLanguages().add(lng);
        }
        for (Commune comm : this.communes) {
            c.getCommunes().add(comm);
        }
        return c;
    }
}
