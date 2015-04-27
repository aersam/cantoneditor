package ch.fhnw.cantoneditor.datautils;

import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.apache.commons.beanutils.PropertyUtils;

import com.db4o.Db4oEmbedded;
import com.db4o.ObjectContainer;
import com.db4o.ObjectSet;
import com.db4o.config.EmbeddedConfiguration;

public class DB4OConnector {
	private static ObjectContainer db;
	static {
		EmbeddedConfiguration config = Db4oEmbedded.newConfiguration();
		config.common().activationDepth(0);
		db = Db4oEmbedded.openFile(Db4oEmbedded.newConfiguration(), Config.getValue("dbname"));
	}

	public static void addObject(Object add) {
		db.store(add);
	}

	public static <T> void updateObject(T update) throws NoDataFoundException {
		ObjectSet<T> result = db.queryByExample(update);
		if (result.size() <= 0) {
			throw new NoDataFoundException();
		}
		try {
			PropertyUtils.copyProperties(result, update);
		} catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {

		}
		db.store(result);
	}

	public static void removeObject(Object remove) throws NoDataFoundException {
		ObjectSet result = db.queryByExample(remove);
		if (result.size() <= 0) {
			throw new NoDataFoundException();
		}
		db.delete(result.get(0));
	}

	public static <T> T getObject(T example) throws NoDataFoundException {
		ObjectSet<T> result = db.queryByExample(example);
		if (result.size() <= 0) {
			throw new NoDataFoundException();
		}
		return result.get(0);
	}

	public static <T> List<T> getAll(Class<T> clazz) {
		return db.query(clazz);
	}
}
