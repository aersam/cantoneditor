package ch.fhnw.cantoneditor.model;

import java.util.Date;

public class Commune extends BaseModel {

    public static final String DISTRICTNR_PROPERTY = "districtNr";
    public static final String BFSCOMMUNENR_PROPERTY = "bfsCommuneNr";
    public static final String OFFICIALNAME_PROPERTY = "officialName";
    public static final String NAME_PROPERTY = "name";
    public static final String SHORTDISTRICTNAME_PROPERTY = "shortDistrictName";

    public static final String LASTCHANGED_PROPERTY = "lastChanged";
    public static final String SHORTCUT_PROPERTY = "shortCut";
    public static final String CANTON_PROPERTY = "canton";

    private int districtNr;
    private int bfsCommuneNr;
    private String officialName;
    private String name;
    private String shortDistrictName;
    private Canton canton;
    private Date lastChanged;

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        if (canton != this.canton) {
            Object oldValue = this.canton;
            this.canton = canton;
            this.pcs.firePropertyChange(CANTON_PROPERTY, oldValue, canton);
        }
    }

    public int getDistrictNr() {
        return districtNr;
    }

    public void setDistrictNr(int districtNr) {
        if (districtNr != this.districtNr) {
            Object oldValue = this.districtNr;
            this.districtNr = districtNr;
            this.pcs.firePropertyChange(DISTRICTNR_PROPERTY, oldValue, districtNr);
        }
    }

    public int getBfsCommuneNr() {
        return bfsCommuneNr;
    }

    public void setBfsCommuneNr(int bfsCommuneNr) {
        if (bfsCommuneNr != this.bfsCommuneNr) {
            Object oldValue = this.bfsCommuneNr;
            this.bfsCommuneNr = bfsCommuneNr;
            this.pcs.firePropertyChange(BFSCOMMUNENR_PROPERTY, oldValue, bfsCommuneNr);
        }
    }

    public String getOfficialName() {
        return officialName;
    }

    public void setOfficialName(String officialName) {
        if (officialName != this.officialName) {
            Object oldValue = this.officialName;
            this.officialName = officialName;
            this.pcs.firePropertyChange(OFFICIALNAME_PROPERTY, oldValue, officialName);
        }
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

    public String getShortDistrictName() {
        return shortDistrictName;
    }

    public void setShortDistrictName(String shortDistrictName) {
        if (shortDistrictName != this.shortDistrictName) {
            Object oldValue = this.shortDistrictName;
            this.shortDistrictName = shortDistrictName;
            this.pcs.firePropertyChange(SHORTDISTRICTNAME_PROPERTY, oldValue, shortDistrictName);
        }
    }

    public Date getLastChanged() {
        return lastChanged;
    }

    public void setLastChanged(Date lastChanged) {
        if (lastChanged != this.lastChanged) {
            Object oldValue = this.lastChanged;
            this.lastChanged = lastChanged;
            this.pcs.firePropertyChange(LASTCHANGED_PROPERTY, oldValue, lastChanged);
        }
    }

}
