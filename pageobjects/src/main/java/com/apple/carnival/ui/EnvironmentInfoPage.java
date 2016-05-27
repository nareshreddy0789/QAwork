package com.apple.carnival.ui;

import java.util.List;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.apple.carnival.qa.parser.JsonParserUtil;
import com.apple.carnival.ui.utilities.ElementLocatorUtility;

public class EnvironmentInfoPage extends CarnivalUI {

	private String jsonLocatorFile = "src/main/resources/locators/EnvironmentInfoPage.JSON";
	private static JSONObject root=null;


	public EnvironmentInfoPage(){
		if(root == null)
			root = JsonParserUtil.parseJsonFile(jsonLocatorFile);
	}

	public String getPackages(String project){
		
		WebElement binayrConfTable = carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"CurrentLocalInstallsTable"));
		List<WebElement> rows=binayrConfTable.findElements(By.xpath(".//tbody/tr"));
		
		for(WebElement row:rows){
			
			List<WebElement> projectCells = row.findElements(By.tagName("td"));
			
			for(int k=0;k<projectCells.size();k++){
				
				if(projectCells.get(k).getText().equals(project) ){
					return projectCells.get(k+1).getText();
				}
				
			}
			
		}
		return  null;
	}

	
}
