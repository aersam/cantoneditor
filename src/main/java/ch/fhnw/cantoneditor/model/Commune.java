package ch.fhnw.cantoneditor.model;

import java.util.Date;

public class Commune extends BaseModel {
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
        if (shortCut != this.shortCut) {
            Object oldValue = this.shortCut;
            this.shortCut = shortCut;
            this.pcs.firePropertyChange("shortCut", oldValue, shortCut);
        }
    }

    public int getDistrictNr() {
        return districtNr;
    }

    public void setDistrictNr(int districtNr) {
        if (districtNr != this.districtNr) {
            Object oldValue = this.districtNr;
            this.districtNr = districtNr;
            this.pcs.firePropertyChange("districtNr", oldValue, districtNr);
        }
    }

    public int getBfsCommuneNr() {
        return bfsCommuneNr;
    }

    public void setBfsCommuneNr(int bfsCommuneNr) {
        if (bfsCommuneNr != this.bfsCommuneNr) {
            Object oldValue = this.bfsCommuneNr;
            this.bfsCommuneNr = bfsCommuneNr;
            this.pcs.firePropertyChange("bfsCommuneNr", oldValue, bfsCommuneNr);
        }
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        if (officialName != this.officialName) {
            Object oldValue = this.officialName;
            this.officialName = officialName;
            this.pcs.firePropertyChange("officialName", oldValue, officialName);
        }
    }

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

    public String getShortDistrictName() {
        return shortDistrictName;
    }

    public void setShortDistrictName(String shortDistrictName) {
        if (shortDistrictName != this.shortDistrictName) {
            Object oldValue = this.shortDistrictName;
            this.shortDistrictName = shortDistrictName;
            this.pcs.firePropertyChange("shortDistrictName", oldValue, shortDistrictName);
        }
    }

    public String getCantonName() {
        return cantonName;
    }

    public void setCantonName(String cantonName) {
        if (cantonName != this.cantonName) {
            Object oldValue = this.cantonName;
            this.cantonName = cantonName;
            this.pcs.firePropertyChange("cantonName", oldValue, cantonName);
        }
    }

    public Date getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        if (lastChanged != this.lastChanged) {
            Object oldValue = this.lastChanged;
            this.lastChanged = lastChanged;
            this.pcs.firePropertyChange("lastChanged", oldValue, lastChanged);
        }
    }

}
