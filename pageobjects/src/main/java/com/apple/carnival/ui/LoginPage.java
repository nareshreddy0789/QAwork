/**
 * 
 */
package com.apple.carnival.ui;

import java.util.concurrent.TimeUnit;

import org.json.JSONObject;

import com.apple.carnival.qa.parser.JsonParserUtil;
import com.apple.carnival.ui.utilities.ElementLocatorUtility;
/**
 * @author harisha
 *
 */
public class LoginPage extends CarnivalUI {

	private String jsonUserField = "UserField";
	private String jsonPasswordField = "PasswordField";
	private String jsonSubmitButton = "SubmitButton";

	private String jsonLocatorFile = "src/main/resources/locators/LoginPage.JSON";
	private static JSONObject root=null;

	public LoginPage( ) {
		
		if(root == null)
			root = JsonParserUtil.parseJsonFile(jsonLocatorFile);
	}

	/*
	 * 
	 */
	public void loginToCarnival(String user,String password){
		
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, jsonUserField)).sendKeys("admin");
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, jsonPasswordField)).sendKeys("admin_pass");
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, jsonSubmitButton)).click();
		carnivalWebDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

	}
	

	

}
