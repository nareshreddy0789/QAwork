package com.apple.carnival.api.request;


import static org.testng.Assert.assertTrue;

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
public class UninstallPackgaeNegativeTests {

	private Configuration config = null; 
	private String carnivalURL = null;
/*	private static final String PROJECT = "CarnivalQA";
	private static final String PACKAGE_TO_UNINSTALL = "clownCONF14A162";
	private static final String HOST_TO_UNINSTALL = "vp21q01if-ztbu14114701";
	private static final String DOMAIN = "vp.if1.apple.com";
*/

	private String wd40Host = null;
	private String domain = null;
	private String project = null;
	private String packageName = null;
	
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
	@Parameters({"carnival.wd40host","carnival.domain","carnival.project","carnival.package"})
	public void init(String wd40Host, @Optional String domain,String project, String packageName){
		this.wd40Host= wd40Host;
		this.domain = domain;
		this.project = project;
		this.packageName = packageName;
		
		Reporter.log("UninstallPackgaeNegativeTests tests with the following details :",true);
	
		Reporter.log("WD40 HOST :"+wd40Host,true);
		Reporter.log("DOMAIN :"+domain,true);
		Reporter.log("PROEJCT :"+project,true);
		Reporter.log("PACKAGE  :"+packageName,true);

	}

	@Test(dependsOnMethods = { "init" })
	public void uninstallPackageWithOutPackageName() throws InterruptedException {
		
		Reporter.log("UnInstall the package :"+packageName+" from carnival UI",true);
	
		String expectedErrorMessage = " No packages were requested.\nInvalid action submitted. Please correct it and try again.";

		home.getCreateRequestPage();
		CreateRequestPage createRequest = new CreateRequestPage();

		String actualErrorMessage = createRequest.addRequestAndScheduleToUninstallPackage("100.0",project, "",true,3,wd40Host);

		Reporter.log("The actual error message is :" + actualErrorMessage,true);
		Reporter.log("Uninstall Successfull",true);
		Reporter.log("Performing Assertions",true);
		Reporter.log("Expected error messsage is : " + expectedErrorMessage + " and got error message from server as  : " + actualErrorMessage,true);
		
		Assert.assertEquals(actualErrorMessage.trim(), expectedErrorMessage.trim());
	}
	
	@Test(dependsOnMethods = { "init" })
	public void uninstallACurrentPackageWhenCurrentOnCarnivalIsSetToNo() throws InterruptedException {
		
		Reporter.log("Uninstalling the package from carnival UI",true);
	
		String expectedErrorMessage = "Cannot uninstall requested package [clownCONF14A162]. It is Current on the Carnival host. If you would like to uninstall it anyway, please modify the request and remove this package OR choose 'ON' for Force Uninstall.Invalid action submitted. Please correct it and try again.";
		home.getCreateRequestPage();
		CreateRequestPage createRequest = new CreateRequestPage();

		String actualErrorMessage = createRequest.addRequestAndScheduleToUninstallPackage("100.0",project,packageName,false,3,wd40Host);

		Reporter.log("The actual error message is :" + actualErrorMessage,true);
		Reporter.log("Uninstall Successfull",true);
		Reporter.log("Performing Assertions",true);
		Reporter.log("Expected error messsage is : " + expectedErrorMessage + " and got error message from server as  : " + actualErrorMessage,true);
		
		assertTrue(actualErrorMessage.contains(expectedErrorMessage), "The error message should have been :" + expectedErrorMessage );
	}
	


	@AfterClass
	public void cleanUp() throws InterruptedException {
		Thread.sleep(2000);
		Reporter.log("\nLogging out and closing  browser session\n",true);
		//home.logout();
		CarnivalUI.stopDriver();

	}

}