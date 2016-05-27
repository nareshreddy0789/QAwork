package com.apple.carnival.ui;

public enum Driver {
	
	FF("firefox"),CR("chrome"),PJ("phantomJS"),HU("htmlUnit");
	
	private String driver;
	
	private Driver(String driverType){
		this.driver = driverType;
	}
	
	public String getDriver(){
		return this.driver;
	}
	
}
