package com.apple.carnival.ui;

import org.json.JSONObject;

import com.apple.carnival.qa.parser.JsonParserUtil;
import com.apple.carnival.ui.utilities.ElementLocatorUtility;

public class HomePage extends CarnivalUI {

	private String jsonCreateRequestLink = "CreateRequestLink";
	private String jsonOverViewOfHosts = "OverViewOfHosts";
	private String jsonLogout = "LogOut";
	
	private String jsonLocatorFile = "src/main/resources/locators/HomePage.JSON";
	private static JSONObject root=null;


	public HomePage(){
		if(root == null)
			root = JsonParserUtil.parseJsonFile(jsonLocatorFile);
	}
	public void getCreateRequestPage(){
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,jsonCreateRequestLink)).click();
	}

	public void getOverViewOfHostsPage(){
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,jsonOverViewOfHosts)).click();
	}

	public void logout() {
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,jsonLogout)).click();

	}
	public void getInstallBuildPage() {
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"InstallBuild")).click();
		
	}
	
	public void getEnvironmentInfoPage() throws InterruptedException {
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"EnvironmentInfoLink")).click();
		Thread.sleep(3000);
		
	}

}
