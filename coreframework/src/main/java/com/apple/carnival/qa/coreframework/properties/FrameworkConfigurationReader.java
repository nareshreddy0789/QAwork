package com.apple.carnival.qa.coreframework.properties;

import com.apple.carnival.qa.coreframework.properties.exceptions.ConfigurationReaderRuntimeException;

/*
 * Class to maintain application configuration properties.
 * 
 * Usage:
 *    String value = PropertyManager.get(<property name>);
 * @author happanangari
 *
 */


public class FrameworkConfigurationReader implements Configuration {

	private PropertyFile framworkPropertyFile=null;
	private String frameworkPropertyFile =null;
	
	public FrameworkConfigurationReader(String propertyFile){
		this. frameworkPropertyFile = propertyFile;
		framworkPropertyFile = new PropertyFile(frameworkPropertyFile);		
	}
	@Override
	public String getProperty(String key) {
		String value=null;
		value= framworkPropertyFile.get(key);
		
		if(value == null)
			throw new ConfigurationReaderRuntimeException("[" + key + "] not found in  property file."+frameworkPropertyFile);
		
		return value;
	}

}
