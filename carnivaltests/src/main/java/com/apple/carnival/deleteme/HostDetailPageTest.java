package com.apple.carnival.deleteme;

import java.util.List;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.apple.carnival.qa.coreframework.data.pageobjects.BinaryConfLine;
import com.apple.carnival.qa.coreframework.data.pageobjects.PSConfLine;
import com.apple.carnival.qa.coreframework.properties.Configuration;
import com.apple.carnival.qa.coreframework.properties.FrameworkConfigurationReader;
import com.apple.carnival.ui.CarnivalUI;
import com.apple.carnival.ui.HomePage;
import com.apple.carnival.ui.HostDetailPage;
import com.apple.carnival.ui.LoginPage;
import com.apple.carnival.ui.OverviewOfHostsPage;

public class HostDetailPageTest {
	
	private Configuration config = null; 
	private String carnivalURL = null;
	private String wd40Host = "vp21q01if-ztbu14114701";
	
	HomePage home = new HomePage();
	OverviewOfHostsPage overviewHosts = new OverviewOfHostsPage();
	HostDetailPage hostPage = new HostDetailPage();
	
	
	@BeforeClass
	public void init() {
		Reporter.log("Initializing and Loading Configuration",true);
		config = new FrameworkConfigurationReader("framework.properties");
		carnivalURL = config.getProperty("carnival.url");
		String driverType = config.getProperty("webdriverType");
		CarnivalUI.startDriver(carnivalURL,driverType);
		LoginPage login = new LoginPage();
		login.loginToCarnival(config.getProperty("carnival.ui.user"), config.getProperty("carnival.ui.password"));

	}
	
	@Test
	public void tableTest() throws InterruptedException{
		String expectedAppName1="clown";
		String expectedInstance1 = "1011";
		
		home.getOverViewOfHostsPage();
		overviewHosts.selectHost(wd40Host);	
		List<BinaryConfLine> confLineEntires = hostPage.getBinaryConfLines();
		
		Reporter.log("Peforming Binary Conf Validations",true);
		for(BinaryConfLine confLine:confLineEntires){
			if(confLine.getISAppName().equals(expectedAppName1)&&confLine.getInstance().equals(expectedInstance1)){
				Reporter.log("Binary ConfLine verified successfully",true);
			}
			
			
		}
		
		List<PSConfLine> psConfLineEntries = hostPage.getCurrentlyRunningConfEntries();
		String processID=null;
		Reporter.log("Peforming PS Conf Validations",true);
		for(PSConfLine confLine:psConfLineEntries){
			if(confLine.getISAppName().equals(expectedAppName1)&&confLine.getInstance().equals(expectedInstance1)){
				processID = confLine.getPID();
				Reporter.log("The process id is :"+processID,true);
				Reporter.log("PS ConfLine verified successfully",true);
			}
			
			
		}
		
	}
	
	@AfterClass
	public void cleanUp() throws InterruptedException {
		Thread.sleep(2000);
		Reporter.log("\nLogging out and closing  browser session\n",true);
		//home.logout();
		CarnivalUI.stopDriver();

	}

}
