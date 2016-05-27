package com.apple.carnival.qa.coreframework.properties.exceptions;

/*
 * 
 * @author harisha
 * Used to throw when there is any thing goes wrong in handling exception
 */
public class PropertyFileRuntimeException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	/**
	 * Constructor  to print the property exception.
	 * @param message String containing the error message to pass on
	 */
	public PropertyFileRuntimeException(String message){
		super(message);
	}

	/**
	 * Constructor  to print the property exception.
	 * @param message String containing the error message to pass on
	 * @param e Parent exception that was caught
	 */
	public PropertyFileRuntimeException(String message, Exception e){
		super(message, e);
	}
	
}
