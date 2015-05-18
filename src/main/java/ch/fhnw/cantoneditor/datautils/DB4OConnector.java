package ch.fhnw.cantoneditor.datautils;

import java.util.HashSet;
import java.util.List;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;

public class DB4OConnector {
    private static ObjectContainer db;

    private static HashSet<BaseModel> changedItems;

    public static <T extends BaseModel> void markChanged(T item) {
        if (changedItems == null)
            changedItems = new HashSet<>();
        changedItems.add(item);
    }

    static {
        EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
        config.common().activationDepth(0);
        db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), Config.getValue("dbname"));
    }

    public static void terminate() {
        db.commit();
        db.close();
    }

    public static void addObject(Object add) {
        db.store(add);
    }

    public static void saveChanges() throws NoDataFoundException {
        if (changedItems == null)
            return;
        for (BaseModel c : changedItems) {
            db.store(c);
        }
    }

    public static void removeObject(Object remove) throws NoDataFoundException {
        db.delete(remove);
    }

    public static <T extends BaseModel> T getObject(T example) throws NoDataFoundException {
        ObjectSet<T> result = db.queryByExample(example);
        if (result.size() <= 0) {
            throw new NoDataFoundException();
        }
        return result.get(0);
    }

    public static <T extends BaseModel> List<T> getAll(Class<T> clazz) {
        List<T> items = db.query(clazz);
        for (T item : items) {
            if (item instanceof Initable) {
                ((Initable) item).init();
            }
        }
        return items;
    }
}
