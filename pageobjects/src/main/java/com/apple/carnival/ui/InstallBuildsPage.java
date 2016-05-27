package com.apple.carnival.ui;

import org.apache.commons.lang.StringUtils;
import org.json.JSONObject;
import org.openqa.selenium.NoSuchElementException;
import org.testng.Reporter;

import com.apple.carnival.qa.coreframework.properties.Configuration;
import com.apple.carnival.qa.parser.JsonParserUtil;
import com.apple.carnival.ui.utilities.ElementLocatorUtility;

public class InstallBuildsPage extends CarnivalUI {

	private String jsonLocatorFile = "src/main/resources/locators/InstallBuildsPage.JSON";
	private static JSONObject root=null;
	private int waitTime=600000; //10 minutes default wait time

	public InstallBuildsPage(Configuration config){
		if(root == null)
			root = JsonParserUtil.parseJsonFile(jsonLocatorFile);
		String wait = config.getProperty("installBuild.waitTime");
		
		if(!StringUtils.isEmpty(wait))
			waitTime = Integer.parseInt(config.getProperty("installBuild.waitTime"));

	}
	public String installBuild(String build) throws InterruptedException {

		String message= null;

		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "BuildToInstallField")).sendKeys(build);
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "InstallButton")).click();

		if (StringUtils.isEmpty(build))
		{
			try {
				message = carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "InstallBuildFailureMessage")).getText();
				return message;
			}catch(NoSuchElementException elementException){

			}
		}
		Reporter.log("Waiting for :" + waitTime+" milli seconds");
		Thread.sleep(waitTime);
		message = carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "LogMessage")).getText();


		return message;
	}


}
