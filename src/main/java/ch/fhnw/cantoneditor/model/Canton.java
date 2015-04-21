package ch.fhnw.cantoneditor.model;

import java.util.List;

public class Canton {
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

}
