package ch.fhnw.cantoneditor.model;

import java.util.Date;

public class Commune {
    private String shortCut;
    private int districtNr;
    private int bfsCommuneNr;
    private String officialName;
    private String name;
    private String shortDistrictName;
    private String cantonName;
    private Date lastChanged;
    
    public String getShortCut() {
        return shortCut;
    }
    public void setShortCut(String shortCut) {
        this.shortCut = shortCut;
    }
    public int getDistrictNr() {
        return districtNr;
    }
    public void setDistrictNr(int districtNr) {
        this.districtNr = districtNr;
    }
    public int getBfsCommuneNr() {
        return bfsCommuneNr;
    }
    public void setBfsCommuneNr(int bfsCommuneNr) {
        this.bfsCommuneNr = bfsCommuneNr;
    }
    public String getOfficialName() {
        return officialName;
    }
    public void setOfficialName(String officialName) {
        this.officialName = officialName;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getShortDistrictName() {
        return shortDistrictName;
    }
    public void setShortDistrictName(String shortDistrictName) {
        this.shortDistrictName = shortDistrictName;
    }
    public String getCantonName() {
        return cantonName;
    }
    public void setCantonName(String cantonName) {
        this.cantonName = cantonName;
    }
    public Date getLastChanged() {
        return lastChanged;
    }
    public void setLastChanged(Date lastChanged) {
        this.lastChanged = lastChanged;
    }
    
    
}
