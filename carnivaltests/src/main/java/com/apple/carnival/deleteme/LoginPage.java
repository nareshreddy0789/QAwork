package com.apple.carnival.deleteme;

import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;

public class LoginPage extends Browser{

	private By userFieldLocator = null;
	private By passwordFieldLocator = null;
	private By submitButtonLocator = null;
	private By roundCubeLinkLocator = null;


	public LoginPage() {

		userFieldLocator = By.id("userid");
		passwordFieldLocator = By.id("passwordid");
		submitButtonLocator = By.id("submitid");
		roundCubeLinkLocator = By.linkText("Read Mail Using RoundCube");
	}

	public void loginToApplication(String userEmail,String userPassword) {

		applicationDriver.findElement(userFieldLocator).sendKeys(userEmail);
		applicationDriver.findElement(passwordFieldLocator).sendKeys(userPassword);
		applicationDriver.findElement(submitButtonLocator).click();
		applicationDriver.manage().timeouts().pageLoadTimeout(6000, TimeUnit.SECONDS);
	
		applicationDriver.findElement(roundCubeLinkLocator).click();
		applicationDriver.manage().timeouts().pageLoadTimeout(6000, TimeUnit.SECONDS);
	}

}
