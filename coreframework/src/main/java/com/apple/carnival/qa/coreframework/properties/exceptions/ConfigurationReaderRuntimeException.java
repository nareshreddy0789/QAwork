package com.apple.carnival.qa.coreframework.properties.exceptions;

public class ConfigurationReaderRuntimeException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Constructor  to print the property exception.
	 * @param message String containing the error message to pass on
	 */
	public ConfigurationReaderRuntimeException(String message){
		super(message);
	}
	
	/**
	 * Constructor  to print the property exception.
	 * @param message String containing the error message to pass on
	 * @param e Parent exception that was caught
	 */
	public ConfigurationReaderRuntimeException(String message, Exception e){
		super(message, e);
	}
}
