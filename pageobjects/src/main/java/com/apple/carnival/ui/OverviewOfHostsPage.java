package com.apple.carnival.ui;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import com.apple.carnival.qa.coreframework.data.pageobjects.WD40Host;
import com.apple.carnival.qa.parser.JsonParserUtil;
import com.apple.carnival.ui.utilities.ElementLocatorUtility;

public class OverviewOfHostsPage extends CarnivalUI {
	
	private static JSONObject root=null;
	
	public OverviewOfHostsPage(){
		
		if(root == null)
			root = JsonParserUtil.parseJsonFile("src/main/resources/locators/OverviewOfHostsPage.JSON");
	}


	public void selectHost(String hostName) throws InterruptedException{
		By wd40HostLinkLocator = By.linkText(hostName);
		carnivalWebDriver.findElement(wd40HostLinkLocator).click();
		carnivalWebDriver.manage().timeouts().pageLoadTimeout(20, TimeUnit.SECONDS);
		Thread.sleep(2000);
	}
	
	public List<WD40Host> getWD40HostList() {
	
		List<WD40Host> hostList = new ArrayList<WD40Host> ();
		
		WebElement hostTable = carnivalWebDriver.findElement(
				             ElementLocatorUtility.getWebDriverLocator(root, "HostOverviewTable"));
		List<WebElement> rows=hostTable.findElements(By.xpath(".//tbody/tr"));
		
		for(WebElement row:rows){
			
			List<WebElement> confCells = row.findElements(By.tagName("td"));
			WD40Host hostData = new WD40Host();
			
			for(int k=0;k<confCells.size();k++){
				String value = confCells.get(k).getText();
				if(k == 0)
					hostData.setHost(value);
				if(k == 1)
					hostData.setState(value);
				if(k == 2)
					hostData.setISAppNames(value);
				if(k == 3)
					hostData.setHostPhysicalRAM(value);
				if(k == 4)
					hostData.setSubscribedRAM(value);
				if(k == 5)
					hostData.setSubscribedRAMPct(value);
			}
			
			hostList.add(hostData);
		}
		
		
		return hostList;
	}
}
