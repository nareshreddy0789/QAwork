package com.apple.carnival.ui;

import org.json.JSONObject;

import com.apple.carnival.qa.parser.JsonParserUtil;
import com.apple.carnival.ui.utilities.ElementLocatorUtility;

public class RunningRequestsPage extends CarnivalUI {

	private String jsonLocatorFile = "src/main/resources/locators/RunningRequestsPage.JSON";
	private static JSONObject root=null;


	public RunningRequestsPage(){
		if(root == null)
			root = JsonParserUtil.parseJsonFile(jsonLocatorFile);
	}

	public void getTaskDetailsPage(String url){
		
		//carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"TaskLink")).click();
		carnivalWebDriver.get(url);
	}

}
