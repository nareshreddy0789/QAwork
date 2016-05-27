package com.apple.carnival.qa.common.util;

import java.io.IOException;
import java.util.Properties;

/**
 * Reads the property file and provides methods to retrieve values from the
 * property file - config.properties
 */
public class PropertyReader {
	private static final Properties prop = new Properties();
	static {
		try {
			prop.load(PropertyReader.class.getClassLoader().getResourceAsStream("config.properties"));

		} catch (IOException ex) {
			ex.printStackTrace();
		}
	}

	/**
	 * Reads the property file and returns the value for the given property
	 * name.
	 */
	public static final String getProperty(String propertyName) {
		return prop.getProperty(propertyName);
	}
}
