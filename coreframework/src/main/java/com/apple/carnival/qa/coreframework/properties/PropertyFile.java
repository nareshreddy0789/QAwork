package com.apple.carnival.qa.coreframework.properties;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Properties;

import org.testng.log4testng.Logger;


import com.apple.carnival.qa.coreframework.properties.exceptions.PropertyFileRuntimeException;

public class PropertyFile {

	private static final Logger log = Logger.getLogger(PropertyFile.class);
	private static String propertyFile;
	private static Properties properties = null;
	
	public PropertyFile(String file){
		this.propertyFile = file;
		initialize();
	}


	private static synchronized void initialize() {

		try
		{
			if(properties==null){
				properties = new Properties();
				properties.load(Thread.currentThread().getContextClassLoader().getResourceAsStream(propertyFile));
			}
		}
		catch (FileNotFoundException e)
		{
			log.error("Can not find file [" + propertyFile + "] - " + e.getMessage());
			throw new PropertyFileRuntimeException(e.getMessage());
		}
		catch (IOException e)
		{
			log.error("Can not read file [" + propertyFile + "] - " + e.getMessage());
			throw new PropertyFileRuntimeException(e.getMessage());
		}    
	
		
	}
	
	public synchronized String get(String key){
		return properties.getProperty(key);
	}
}
