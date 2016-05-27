package com.apple.carnival.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.apple.carnival.qa.coreframework.data.pageobjects.BinaryConfLine;
import com.apple.carnival.qa.coreframework.data.pageobjects.PSConfLine;
import com.apple.carnival.qa.parser.JsonParserUtil;
import com.apple.carnival.ui.utilities.ElementLocatorUtility;

public class HostDetailPage extends CarnivalUI {
	
	private String jsonLocatorFile = "src/main/resources/locators/HostDetailPage.JSON";
	private static JSONObject root=null;
	
	public HostDetailPage(){
		
		if(root == null)
			root = JsonParserUtil.parseJsonFile(jsonLocatorFile);
	}

	public String getInstalledSoftware() throws InterruptedException{
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"ShowInstalledSoftwareLink")).click();
		carnivalWebDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		Thread.sleep(4000);
		String installedSoftware = carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"InstalledSoftwareDiv")).getText();
		Thread.sleep(4000);
		return installedSoftware;
	}

	public List<BinaryConfLine> getBinaryConfLines() throws InterruptedException {
	
		List<BinaryConfLine> confList = new ArrayList<BinaryConfLine> ();
		Thread.sleep(2000);
		WebElement binayrConfTable = carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"BinaryConfTable"));
		List<WebElement> rows=binayrConfTable.findElements(By.xpath(".//tbody/tr"));
		
		for(WebElement row:rows){
			
			List<WebElement> confCells = row.findElements(By.tagName("td"));
			BinaryConfLine confData = new BinaryConfLine();
			
			for(int k=0;k<confCells.size();k++){
				if(k == 0)
					confData.setIsAppName(confCells.get(k).getText());
				if(k == 1)
					confData.setInstance(confCells.get(k).getText());
				if(k == 2)
					confData.setPort(confCells.get(k).getText());
				if(k == 3)
					confData.setPartition(confCells.get(k).getText());
				if(k == 4)
					confData.setBuildVersion(confCells.get(k).getText());
				if(k == 5)
					confData.setDC(confCells.get(k).getText());
			}
			
			confList.add(confData);
		}
		
		
		return confList;
	}

	public List<PSConfLine> getCurrentlyRunningConfEntries() throws InterruptedException {

		List<PSConfLine> confList = new ArrayList<PSConfLine> ();
		Thread.sleep(2000);
		WebElement binayrConfTable = carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"PSConfTable"));
		List<WebElement> rows=binayrConfTable.findElements(By.xpath(".//tbody/tr"));
		
		for(WebElement row:rows){
			
			List<WebElement> confCells = row.findElements(By.tagName("td"));
			PSConfLine confData = new PSConfLine();
			
			for(int k=0;k<confCells.size();k++){
				if(k == 0)
					confData.setPID(confCells.get(k).getText());
				if(k == 1)
					confData.setPPID(confCells.get(k).getText());
				if(k == 2)
					confData.setStarted(confCells.get(k).getText());
				if(k == 3)
					confData.setHealthy(confCells.get(k).getText());
				if(k == 4)
					confData.setISAppName(confCells.get(k).getText());
				if(k == 5)
					confData.setInstance(confCells.get(k).getText());
				if(k == 6)
					confData.setPort(confCells.get(k).getText());
				if(k == 7)
					confData.setPartition(confCells.get(k).getText());
				if(k == 8)
					confData.setBuild(confCells.get(k).getText());
				if(k == 9)
					confData.setRevision(confCells.get(k).getText());
				if(k == 10)
					confData.setDC(confCells.get(k).getText());
				if(k == 11)
					confData.setAction(confCells.get(k).getText());
			}
			
			confList.add(confData);
		}
		
		
		return confList;

	}

}
