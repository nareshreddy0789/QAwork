package com.apple.carnival.ui;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;

/**
 * @author harisha
 *
 */
public abstract class CarnivalUI {

	protected static WebDriver carnivalWebDriver = null;


	public static void startDriver(String carnivalURL, String driverType){

		if(carnivalWebDriver == null){
			carnivalWebDriver = SeleniumUtility.getWebDriver(driverType);
			carnivalWebDriver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			carnivalWebDriver.manage().window().maximize();
			carnivalWebDriver.get(carnivalURL);
		}
		
	}

	public static void stopDriver(){
		carnivalWebDriver.quit();
		carnivalWebDriver=null;
	}


}
