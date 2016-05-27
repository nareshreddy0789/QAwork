package com.apple.carnival.deleteme;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.testng.annotations.Test;

import com.apple.carnival.qa.parser.JsonParserUtil;
//import com.jayway.jsonpath.JsonPath;

public class JSONParserUtilTest {
	private JSONObject root=null;	
	
	private String jsonFile = "src/main/resources/locators/LoginPage.JSON";
	private String jsonUserField = "userField";
	private String jsonPasswordField = "passwordField";
	private String jsonSubmitButton = "submitButton";

	

	@Test
	public void loginLocatorsTest(){

		root = JsonParserUtil.parseJsonFile(jsonFile);

		//String userField = JsonParserUtil.getNodeValueFromJSONObjectAsString(root,"userField");
	
		//String userFieldLocator = JsonParserUtil.getNodeValueFromJSONObjectAsString(new JSONObject(userField), "locator");
		//System.out.println("The locator is :" + userField);
		
		String jsonuserLocator = "$."+jsonUserField+".locator";
		String jsonuserLocatorType = "$."+jsonUserField+".type";
		
		
		
		//System.out.println("The locator is :" + JsonPath.read(root.toString(),jsonuserLocator).toString());

		//System.out.println("The locator type is is :" + JsonPath.read(root.toString(),jsonuserLocatorType).toString());

		By locator = getWebDriverLocator(jsonUserField);
		System.out.println(locator.toString());
	}
	
	
	private By getWebDriverLocator(String jsonNode){
		
/*		String locator = JsonPath.read(root.toString(),"$."+jsonNode+".locator").toString();
		String locatorType = JsonPath.read(root.toString(),"$."+jsonNode+".type").toString();
		
		String locator1 = JsonPath.read(root.toString(),"$."+jsonNode+".locator").toString();
		String locatorType1 = JsonPath.read(root.toString(),"$."+jsonNode+".type").toString();
		
		System.out.println("The locator is :" +  locator);
		System.out.println("The locator type is is :" + locatorType);
		
		if(locatorType.equals("name"))
			return By.name(locator);
		*/
		
		return null;
		
		
	}

}
