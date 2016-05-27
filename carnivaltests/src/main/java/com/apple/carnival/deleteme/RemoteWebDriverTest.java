package com.apple.carnival.deleteme;

//import java.net.MalformedURLException;
//import java.net.URL;
//
//import org.openqa.selenium.WebDriver;
//import org.openqa.selenium.remote.DesiredCapabilities;
//import org.testng.annotations.Test;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.server.SeleniumServer;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.thoughtworks.selenium.DefaultSelenium;
import com.thoughtworks.selenium.Selenium;
public class RemoteWebDriverTest {

	//@BeforeClass
	public void startSelenimServer(){
		SeleniumServer server = null;
		if (server == null) {
			try {
				 server = new SeleniumServer();
				 server.start();
				 Selenium sel = new DefaultSelenium("localhost",4444,"*firefox","http://www.apple.com");
			
				  System.out.println(" selenium server " + server.toString());
				// sel.start();
			} catch (Exception e) {
				//e.printStackTrace();
			}
		}

	}

	@Test
	public void test1() throws Exception {
		String remoteURL="http://127.0.0.1:4444/wd/hub";
		WebDriver driver = null;
		DesiredCapabilities desiredBrowser = DesiredCapabilities.firefox();
		System.out.println("remoteURL----> " + remoteURL);

		try {
			driver =  new RemoteWebDriver(new URL(remoteURL),desiredBrowser);
			driver.get("http://www.apple.com");
			//			driver.manage().timeouts().implicitlyWait(20, TimeUnit.SECONDS);
			Thread.sleep(3000);
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		System.out.println("Harish appannagari :" + driver);


	}



}
