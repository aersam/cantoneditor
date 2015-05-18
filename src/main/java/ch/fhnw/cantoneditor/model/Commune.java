package ch.fhnw.cantoneditor.model;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import ch.fhnw.cantoneditor.datautils.BaseModel;
import ch.fhnw.cantoneditor.datautils.DB4OConnector;
import ch.fhnw.cantoneditor.datautils.Initable;

public class Commune extends BaseModel implements Initable {

    public static final String DISTRICTNR_PROPERTY = "districtNr";
    public static final String BFSCOMMUNENR_PROPERTY = "bfsCommuneNr";
    public static final String OFFICIALNAME_PROPERTY = "officialName";
    public static final String NAME_PROPERTY = "name";
    public static final String DISTRICTNAME_PROPERTY = "DistrictName";

    public static final String LASTCHANGED_PROPERTY = "lastChanged";
    public static final String SHORTCUT_PROPERTY = "shortCut";
    public static final String CANTON_PROPERTY = "canton";

    private int districtNr;

    private String officialName;
    private String name;
    private String districtName;
    private Canton canton;
    private Date lastChanged;

    private static Map<Integer, Commune> communes = new HashMap<Integer, Commune>();

    private Commune(int bfsCommuneNr) {
        if (bfsCommuneNr != 0) {
            communes.put(bfsCommuneNr, this);
        }
        this.id = bfsCommuneNr;
    }

    public static Commune getById(int bfsCommuneNr, boolean createIfNotExists) {
        if (communes.containsKey(bfsCommuneNr))
            return communes.get(bfsCommuneNr);
        if (createIfNotExists) {
            Commune c = new Commune(bfsCommuneNr);
            return c;
        }
        return null;
    }

    public static Commune getByName(String name, boolean createIfNotExists) {
        for (Commune c : communes.values()) {
            if (c.getName().equals(name)) {
                return c;
            }
        }
        if (createIfNotExists) {
            Commune c = new Commune(0);

            c.setName(name);
            return c;
        }
        return null;
    }

    private boolean isInited = false;

    public void init() {
        if (isInited)
            return;
        isInited = true;
        this.addPropertyChangeListener((evt) -> {
            if (this.id != 0) {
                DB4OConnector.markChanged(this);
            }
        });
    }

    public Canton getCanton() {
        this.notifyPropertyRead(CANTON_PROPERTY);
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
        this.notifyPropertyRead(DISTRICTNR_PROPERTY);
        return districtNr;
    }

    public void setDistrictNr(int districtNr) {
        if (districtNr != this.districtNr) {
            Object oldValue = this.districtNr;
            this.districtNr = districtNr;
            this.pcs.firePropertyChange(DISTRICTNR_PROPERTY, oldValue, districtNr);
        }
    }

    public String getOfficialName() {
        this.notifyPropertyRead(OFFICIALNAME_PROPERTY);
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

    public String getDistrictName() {
        this.notifyPropertyRead(DISTRICTNAME_PROPERTY);
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
        this.notifyPropertyRead(LASTCHANGED_PROPERTY);
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
        if (this.id != 0) { // Not a newly created object
            return this.id;// Primary Key
        }
        return super.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this)
            return true;
        if (obj == null)
            return false;
        if (this.id == 0)
            return false;// Two newly created Communes are newer the same
        if (obj instanceof Commune) {
            return ((Commune) obj).getId() == this.id;
        }
        return false;
    };

    @Override
    public String toString() {
        return this.getName();
    }
}
