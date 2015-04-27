package ch.fhnw.cantoneditor.datautils;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class Config {
	private final static Properties prop;
	static {
		prop = new Properties();
		try (FileInputStream fis = new FileInputStream(new File("config.properties"))) {
			prop.load(fis);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String getValue(String key) {
		return prop.getProperty(key);
	}

	public static int getValue(String key, int defaultValue) {
		try {
			return Integer.parseInt(prop.getProperty(key));
		} catch (NumberFormatException e) {
			return defaultValue;
		}
	}
}
