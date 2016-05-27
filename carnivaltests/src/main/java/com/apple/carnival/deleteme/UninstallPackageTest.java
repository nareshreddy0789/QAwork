package com.apple.carnival.deleteme;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;
import org.testng.Reporter;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.apple.carnival.qa.coreframework.properties.Configuration;
import com.apple.carnival.qa.coreframework.properties.FrameworkConfigurationReader;
import com.apple.carnival.ui.SeleniumUtility;

public class UninstallPackageTest {
	private Configuration config = null; 
	private String driverType = null;
	private WebDriver carnivalWebDriver = null;
	private String carnivalURL=null;
	private By userField = By.name("username");
	private By passwordField = By.name("password");
	private By submitButton = By.xpath("//input[@value='Login']");
	private By createRequestLink = By.linkText("Create a Request");
	private By chooseActionSelectLocator= By.id("actionSelect");
	private String hostToUninstall = "vp21q01if-ztbu14114701";
	private String packageToUninstall = "clown14A148";
	private By packageToUninstallTextAreaLocator = By.xpath("//form[contains(@class,'ActionForm UnInstalls')]/table[@id='TABLE_25']//textarea[@id='projectInstallPackage']");
	private By uninstallIfCurrentRadioButtonLocator = By.id("forceUnnstallTrue");
	private By regexHostsRadioButtonLocator = By.xpath("//form[contains(@class,'ActionForm UnInstalls')]/table[@id='TABLE_25']//input[@id='carnivalOnlyInstallRegEx']");
	private By regexHostFieldLocator = By.xpath("//form[contains(@class,'ActionForm UnInstalls')]//table[@id='TABLE_25']//input[@id='hostRegex']");
	private By addToRequestButtonLocator = By.id("addUnInstallPackagesToRequest");
	private By uninstallRequestNotesTextFieldLocator = By.xpath("//table[@id='TABLE_26']//textarea[@id='taskNotesField']");
	private By scheduleNowButtonLocator = By.id("requestAndScheduleTaskId");
	
	@BeforeClass
	public void init() {
		Reporter.log("Initializing and Loading Configuration",true);
		
		config = new FrameworkConfigurationReader("framework.properties");
		driverType = config.getProperty("webdriverType");
		carnivalURL = config.getProperty("carnival.url");
		Reporter.log("Initialize Web Driver",true);
		carnivalWebDriver = SeleniumUtility.getWebDriver(driverType);
		carnivalWebDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);


	}
	
	@Test
	public void workFlowTest() throws InterruptedException{
		Reporter.log("Launch and Login to Carnival",true);
		carnivalWebDriver.get(carnivalURL);
		carnivalWebDriver.findElement(userField).sendKeys("admin");
		carnivalWebDriver.findElement(passwordField).sendKeys("admin_pass");
		carnivalWebDriver.findElement(submitButton).click();
		carnivalWebDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		carnivalWebDriver.findElement(createRequestLink).click();
		Select dropdown =  new Select(carnivalWebDriver.findElement(chooseActionSelectLocator));
		dropdown.selectByValue("UninstallPackageActionDiv");
		Thread.sleep(2000);
		carnivalWebDriver.findElement(packageToUninstallTextAreaLocator).sendKeys(packageToUninstall);
		carnivalWebDriver.findElement(uninstallIfCurrentRadioButtonLocator).click();
		carnivalWebDriver.findElement(regexHostsRadioButtonLocator).click();
		carnivalWebDriver.findElement(regexHostFieldLocator).sendKeys(hostToUninstall);
		carnivalWebDriver.findElement(addToRequestButtonLocator).click();
		carnivalWebDriver.findElement(uninstallRequestNotesTextFieldLocator).sendKeys("uninstall package :"+packageToUninstall+" from host :"+hostToUninstall);
		carnivalWebDriver.findElement(scheduleNowButtonLocator).click();
		//Thread.sleep(15000);
		Thread.sleep(2000);
		
	}

}
