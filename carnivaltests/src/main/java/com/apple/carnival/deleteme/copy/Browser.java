package com.apple.carnival.deleteme.copy;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class Browser {
	
	protected static WebDriver applicationDriver = null;
		
	public static void launchApplication(String driver,String url){
		
		if(applicationDriver==null){
			
			if("firefox".equals(driver))
				applicationDriver = new FirefoxDriver();
			else if("chrome".equals(driver)){
				//SET THE PATH TO CHROME DRIVER
				applicationDriver = new FirefoxDriver();
			}else{
				//set default as firefox driver
				applicationDriver = new FirefoxDriver();
			}
				
				applicationDriver.get(url);
		}
	}

	
	public static void quitApplication(){
		applicationDriver.quit();
	}
	
}
