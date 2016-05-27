package com.apple.carnival.ui.utilities;
import org.openqa.selenium.By;
import org.testng.Reporter;

import com.jayway.jsonpath.JsonPath;

import org.json.JSONObject;

public class ElementLocatorUtility {


	public static By getWebDriverLocator(JSONObject root,String jsonNode){

		String locator = JsonPath.read(root.toString(),"$."+jsonNode+".locator").toString();
		String locatorType = JsonPath.read(root.toString(),"$."+jsonNode+".type").toString();

		//Reporter.log("The locator is :" +  locator,true);
		//Reporter.log("The locator type is :" + locatorType,true);


		if(locatorType.equals("name"))
			return By.name(locator);
		else if (locatorType.equals("xpath"))
			return By.xpath(locator);
		else if (locatorType.equals("id"))
			return By.id(locator);
		else if (locatorType.equals("css"))
			return By.cssSelector(locator);
		else if (locatorType.equals("class"))
			return By.className(locator);
		else if (locatorType.equals("link"))
			return By.linkText(locator);

		return null;

	}

}
