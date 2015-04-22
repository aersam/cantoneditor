package ch.fhnw.cantoneditor.model;

import java.util.Date;

import ch.fhnw.cantoneditor.datautils.BaseModel;

public class Commune extends BaseModel {

    public static final String DISTRICTNR_PROPERTY = "districtNr";
    public static final String BFSCOMMUNENR_PROPERTY = "bfsCommuneNr";
    public static final String OFFICIALNAME_PROPERTY = "officialName";
    public static final String NAME_PROPERTY = "name";
    public static final String DISTRICTNAME_PROPERTY = "DistrictName";

    public static final String LASTCHANGED_PROPERTY = "lastChanged";
    public static final String SHORTCUT_PROPERTY = "shortCut";
    public static final String CANTON_PROPERTY = "canton";

    private int districtNr;
    private int bfsCommuneNr;
    private String officialName;
    private String name;
    private String districtName;
    private Canton canton;
    private Date lastChanged;

    public Canton getCanton() {
        return canton;
    }

    public void setCanton(Canton canton) {
        if (canton != this.canton) {
            Canton oldValue = this.canton;
            if (oldValue != null) {
                oldValue.getCommunes().remove(this);
            }
            this.canton = canton;
            if (canton != null) {
                canton.getCommunes().add(this);
            }
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
            if (this.bfsCommuneNr != 0) {
                throw new IllegalAccessError("Cannot change a Primary Key!");
            }
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

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        if (districtName != this.districtName) {
            Object oldValue = this.districtName;
            this.districtName = districtName;
            this.pcs.firePropertyChange(DISTRICTNAME_PROPERTY, oldValue, districtName);
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

    @Override
    public int hashCode() {
        if (this.bfsCommuneNr != 0) { // Not a newly created object
            return this.bfsCommuneNr;// Primary Key
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (this.bfsCommuneNr == 0)
            return false;// Two newly created Communes are newer the same
        if (obj instanceof Commune) {
            return ((Commune) obj).getBfsCommuneNr() == this.bfsCommuneNr;
        }
        return false;
    };

    @Override
    public String toString() {
        return this.name;
    }
}
