package ch.fhnw.cantoneditor.model;

import java.util.List;

public class Canton extends BaseModel {
    private String name;
    private String shortCut;
    private int cantonNr;
    private int nrCouncilSeats;
    private int entryYear;
    private double nrForeigners;
    private List<Integer> languageId;
    private String capital;
    private double area;

    // Einwohnerdichte
    private double inHabitantDensity;
    private int nrCommunes;

    private List<Commune> communes;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name != this.name) {
            Object oldValue = this.name;
            this.name = name;
            this.pcs.firePropertyChange("name", oldValue, name);
        }
    }

    public String getShortCut() {
        return shortCut;
    }

    public void setShortCut(String shortCut) {
        if (shortCut != this.shortCut) {
            Object oldValue = this.shortCut;
            this.shortCut = shortCut;
            this.pcs.firePropertyChange("shortCut", oldValue, shortCut);
        }
    }

    public int getCantonNr() {
        return cantonNr;
    }

    public void setCantonNr(int cantonNr) {
        if (cantonNr != this.cantonNr) {
            Object oldValue = this.cantonNr;
            this.cantonNr = cantonNr;
            this.pcs.firePropertyChange("cantonNr", oldValue, cantonNr);
        }
    }

    public int getNrCouncilSeats() {
        return nrCouncilSeats;
    }

    public void setNrCouncilSeats(int nrCouncilSeats) {
        if (nrCouncilSeats != this.nrCouncilSeats) {
            Object oldValue = this.nrCouncilSeats;
            this.nrCouncilSeats = nrCouncilSeats;
            this.pcs.firePropertyChange("nrCouncilSeats", oldValue, nrCouncilSeats);
        }
    }

    public int getEntryYear() {
        return entryYear;
    }

    public void setEntryYear(int entryYear) {
        if (entryYear != this.entryYear) {
            Object oldValue = this.entryYear;
            this.entryYear = entryYear;
            this.pcs.firePropertyChange("entryYear", oldValue, entryYear);
        }
    }

    public double getNrForeigners() {
        return nrForeigners;
    }

    public void setNrForeigners(double nrForeigners) {
        if (nrForeigners != this.nrForeigners) {
            Object oldValue = this.nrForeigners;
            this.nrForeigners = nrForeigners;
            this.pcs.firePropertyChange("nrForeigners", oldValue, nrForeigners);
        }
    }

    public List<Integer> getLanguageId() {
        return languageId;
    }

    public void setLanguageId(List<Integer> languageId) {
        if (languageId != this.languageId) {
            Object oldValue = this.languageId;
            this.languageId = languageId;
            this.pcs.firePropertyChange("languageId", oldValue, languageId);
        }
    }

    public String getCapital() {
        return capital;
    }

    public void setCapital(String capital) {
        if (capital != this.capital) {
            Object oldValue = this.capital;
            this.capital = capital;
            this.pcs.firePropertyChange("capital", oldValue, capital);
        }
    }

    public double getArea() {
        return area;
    }

    public void setArea(double area) {
        if (area != this.area) {
            Object oldValue = this.area;
            this.area = area;
            this.pcs.firePropertyChange("area", oldValue, area);
        }
    }

    public double getInHabitantDensity() {
        return inHabitantDensity;
    }

    public void setInHabitantDensity(double inHabitantDensity) {
        if (inHabitantDensity != this.inHabitantDensity) {
            Object oldValue = this.inHabitantDensity;
            this.inHabitantDensity = inHabitantDensity;
            this.pcs.firePropertyChange("inHabitantDensity", oldValue, inHabitantDensity);
        }
    }

    public int getNrCommunes() {
        return nrCommunes;
    }

    public void setNrCommunes(int nrCommunes) {
        if (nrCommunes != this.nrCommunes) {
            Object oldValue = this.nrCommunes;
            this.nrCommunes = nrCommunes;
            this.pcs.firePropertyChange("nrCommunes", oldValue, nrCommunes);
        }
    }

    public List<Commune> getCommunes() {
        return communes;
    }

    public void setCommunes(List<Commune> communes) {
        if (communes != this.communes) {
            Object oldValue = this.communes;
            this.communes = communes;
            this.pcs.firePropertyChange("communes", oldValue, communes);
        }
    }

}
