package com.apple.carnival.ui;

import java.util.List;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.apple.carnival.qa.parser.JsonParserUtil;
import com.apple.carnival.ui.utilities.ElementLocatorUtility;

public class TaskDetailsPage extends CarnivalUI {

	private String jsonLocatorFile = "src/main/resources/locators/TaskDetailsPage.JSON";
	private static JSONObject root=null;


	public TaskDetailsPage(){
		if(root == null)
			root = JsonParserUtil.parseJsonFile(jsonLocatorFile);
	}

	public String getCurrentState(){
		
		return carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"CurrentState")).getText();
	}

	public void getViewActionLogsPage() throws InterruptedException{
		
		//WebElement taskDetailTable = carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"ViewLogTable"));
		//WebElement viewLogLink = taskDetailTable.findElement(By.xpath(".//tbody/tr/td[4]/a"));
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"ViewLogTable")).click();
		Thread.sleep(2000);
		
	}
}
