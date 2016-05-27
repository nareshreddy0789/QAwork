package com.apple.carnival.ui;

import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class SeleniumUtility {

	public static WebDriver getWebDriver(String driverType) {

		if (Driver.FF.getDriver().equals(driverType))
			return new FirefoxDriver();
		else if (Driver.CR.getDriver().equals(driverType))
			return new ChromeDriver();
		else if (Driver.HU.getDriver().equals(driverType))
			return new HtmlUnitDriver();
		else if (Driver.PJ.getDriver().equals(driverType)) {
			Platform current = Platform.getCurrent();
			DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();	
			if (Platform.MAC.is(current))
				capabilities.setCapability("phantomjs.binary.path","src/main/resources/phantomjs-1.9.7-macosx/bin/phantomjs");
			if (Platform.LINUX.is(current))
				capabilities.setCapability("phantomjs.binary.path","src/main/resources/phantomjs-1.9.8-linux-x86_64/bin/phantomjs");
			capabilities.setJavascriptEnabled(true);
			
			return new PhantomJSDriver(capabilities);
		}
		
		return null;
	}
}
