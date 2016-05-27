package com.apple.carnival.ui;

import org.json.JSONObject;

import com.apple.carnival.qa.parser.JsonParserUtil;
import com.apple.carnival.ui.utilities.ElementLocatorUtility;

public class ViewActionLogsPage extends CarnivalUI {

	private String jsonLocatorFile = "src/main/resources/locators/ViewActionLogsPage.JSON";
	private static JSONObject root=null;


	public ViewActionLogsPage(){
		if(root == null)
			root = JsonParserUtil.parseJsonFile(jsonLocatorFile);
	}

	public String getActionLog(){
		
		return carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"ActionLog")).getText();
	}

	
}
