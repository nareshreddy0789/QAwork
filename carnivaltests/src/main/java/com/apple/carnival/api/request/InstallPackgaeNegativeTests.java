package com.apple.carnival.api.request;


import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.apple.carnival.qa.coreframework.properties.Configuration;
import com.apple.carnival.qa.coreframework.properties.FrameworkConfigurationReader;
import com.apple.carnival.ui.CarnivalUI;
import com.apple.carnival.ui.CreateRequestPage;
import com.apple.carnival.ui.HomePage;
import com.apple.carnival.ui.HostDetailPage;
import com.apple.carnival.ui.LoginPage;
import com.apple.carnival.ui.OverviewOfHostsPage;
/**
 * 
 * @author harisha 
 * This POST request is based on ip authentication, which means,from any machine before this test is run, we need to grab the IP
 * address and let the carnival know the ip adress. To do that, we needto put the following property in carnival.properties (or
 * ci-dev.properties of Carnival) we need to add ip address in carnival.web_service.admin.ip_whitelist
 */
@SuppressWarnings("deprecation")
public class InstallPackgaeNegativeTests {

	private Configuration config = null; 
	private String carnivalURL = null;
	private String project = null;


	HomePage home = new HomePage();
	OverviewOfHostsPage overviewHosts = new OverviewOfHostsPage();
	HostDetailPage hostPage = new HostDetailPage();

	@BeforeClass
	@Parameters({"url"})
	public void setConfig(@Optional String appUrl) {
		Reporter.log("Initializing and Loading Configuration",true);
		config = new FrameworkConfigurationReader("framework.properties");
		if(appUrl == null)
			carnivalURL = config.getProperty("carnival.url");
		else
			carnivalURL = appUrl;
		String driverType = config.getProperty("webdriverType");
		CarnivalUI.startDriver(carnivalURL,driverType);
		LoginPage login = new LoginPage();
		login.loginToCarnival(config.getProperty("carnival.ui.user"), config.getProperty("carnival.ui.password"));

	}


	@Test
	@Parameters({"carnival.project"})
	public void installPackageWithOutPackageName(String carnivalProject) throws InterruptedException {
	
		Reporter.log("Uninstalling the package from carnival UI",true);
		this.project= carnivalProject;
		String expectedErrorMessage = " No packages were requested.\nInvalid action submitted. Please correct it and try again.";

		home.getCreateRequestPage();
		CreateRequestPage createRequest = new CreateRequestPage();

		String actualErrorMessage = createRequest.peformLocalInstallBuild("100.0",project, "",true,2);
		
		System.out.println("The actual error message is :" + actualErrorMessage);
		Reporter.log("Uninstall Successfull",true);
		Reporter.log("Performing Assertions",true);
		Reporter.log("Expected error messsage is : " + expectedErrorMessage + " and got error message from server as  : " + actualErrorMessage,true);
		
		Assert.assertEquals(actualErrorMessage.trim(), expectedErrorMessage.trim());
	}


	@AfterClass
	public void cleanUp() throws InterruptedException {
		Thread.sleep(2000);
		Reporter.log("\nLogging out and closing  browser session\n",true);
		//home.logout();
		CarnivalUI.stopDriver();

	}

}