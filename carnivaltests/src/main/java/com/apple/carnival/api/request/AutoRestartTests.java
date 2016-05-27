package com.apple.carnival.api.request;


import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.apple.carnival.qa.common.util.JcraftUtil;
import com.apple.carnival.qa.coreframework.data.pageobjects.BinaryConfLine;
import com.apple.carnival.qa.coreframework.data.pageobjects.PSConfLine;
import com.apple.carnival.qa.coreframework.data.pageobjects.WD40Host;
import com.apple.carnival.qa.coreframework.properties.Configuration;
import com.apple.carnival.qa.coreframework.properties.FrameworkConfigurationReader;
import com.apple.carnival.ui.CarnivalUI;
import com.apple.carnival.ui.CreateRequestPage;
import com.apple.carnival.ui.HomePage;
import com.apple.carnival.ui.HostDetailPage;
import com.apple.carnival.ui.LoginPage;
import com.apple.carnival.ui.OverviewOfHostsPage;


/**
 * @author harisha 
 * This POST request is based on ip authentication, which means,from any machine before this test is run, we need to grab the IP
 * address and let the carnival know the ip adress. To do that, we needto put the following property in carnival.properties (or
 * ci-dev.properties of Carnival) we need to add ip address in carnival.web_service.admin.ip_whitelist
 */
@SuppressWarnings("deprecation")
public class AutoRestartTests {

	private Configuration config = null; 
	private String carnivalURL = null;
	private String processID = null;
	
	private String wd40Host = null;
	private String domain = null;
	private String project = null;
	private String packageName = null;
	private String expectedAppName = null;
	private String expectedInstance = null;
	
	
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
		
		Reporter.log("Carnival URL is : "+carnivalURL,true);
		String driverType = config.getProperty("webdriverType");
		CarnivalUI.startDriver(carnivalURL,driverType);
		LoginPage login = new LoginPage();
		login.loginToCarnival(config.getProperty("carnival.ui.user"), config.getProperty("carnival.ui.password"));
	}
	
	@Test
	@Parameters({"carnival.wd40host","carnival.domain","carnival.project","carnival.package","carnival.appname"})
	public void init(@Optional String wd40Host,@Optional String domain,String project, String packageName,String expectedAppName){
		this.wd40Host = wd40Host;
		this.domain = domain;
		this.project = project;
		this.packageName = packageName;
		this.expectedAppName = expectedAppName;

		Reporter.log("Auto restart tests with the following details :",true);
		Reporter.log("WD40 HOST :"+wd40Host,true);
		Reporter.log("DOMAIN :"+domain,true);
		Reporter.log("PROEJCT :"+project,true);
		Reporter.log("PACKAGE  :"+packageName,true);
		Reporter.log("EXPECTED APP NAME  :"+expectedAppName,true);
	
	}

	/*
	 * This test assumes that we have clownCONF14A162 package existing on Carnival and WD40 host
	 */
	@Test(dependsOnMethods = { "init" })
	public void createAndScheduleRequestTestToInstallPackage() throws InterruptedException {
		Reporter.log("Install the package :"+packageName+" from carnival UI",true);
		String expectedNotice = "Successfully scheduled task \\d{13} for execution!";
		
		home.getCreateRequestPage();
		CreateRequestPage createRequest = new CreateRequestPage();

		String actualNotice = createRequest.peformLocalInstallBuild("100.0",project, packageName,true,2);
		
		Reporter.log("Install the package : "+packageName ,true);
		Reporter.log("Performing Assertions",true);
		Reporter.log("Expected note is : " + expectedNotice + " and got  note from server as  : " + actualNotice,true);

		assertTrue(actualNotice.matches(expectedNotice),"Expected and Actual Do not Match");
		
	}
	
	@Test(dependsOnMethods = { "createAndScheduleRequestTestToInstallPackage" })
	public void validateConfLineOnWD40Host() throws InterruptedException {
		//Wait for 15 mins to change the status to Current on Environment Info Page
		Thread.sleep(950000);
		
		home.getOverViewOfHostsPage();
		
		if(StringUtils.isEmpty(wd40Host)){
			for(WD40Host host : overviewHosts.getWD40HostList()) {
				if ( host.getISAppNames().contains(expectedAppName) &&host.getISAppNames().contains("Carnival") == false) {
					wd40Host = host.getHost();
					break;
				}
			}
		}
		
		Assert.assertNotNull(wd40Host);
		overviewHosts.selectHost(wd40Host);
		List<BinaryConfLine> confLineEntires = hostPage.getBinaryConfLines();
		
		Reporter.log("Peforming Binary Conf Validations",true);
		for(BinaryConfLine confLine:confLineEntires){
			Reporter.log("The app name is :"+ confLine.getISAppName(),true);
			Reporter.log("The instance is :"+ confLine.getInstance(),true);
			if(confLine.getISAppName().equals(expectedAppName)){
				expectedInstance = confLine.getInstance();
				Reporter.log("Binary ConfLine verified successfully",true);
			}
			
			
		}
		
		Assert.assertNotNull(expectedInstance);
		
		List<PSConfLine> psConfLineEntries = hostPage.getCurrentlyRunningConfEntries();
		
		Reporter.log("Peforming PS Conf Validations",true);
		for(PSConfLine confLine:psConfLineEntries){
			Reporter.log("The app name is :"+ confLine.getISAppName(),true);
			Reporter.log("The instance is :"+ confLine.getInstance(),true);
			if(confLine.getISAppName().equals(expectedAppName)&&confLine.getInstance().equals(expectedInstance)){
				processID = confLine.getPID();
				Reporter.log("The process id is :"+processID,true);
				Reporter.log("PS ConfLine verified successfully",true);
			}
			
			
		}
		
		Assert.assertNotNull(processID);

	}
	
	@Test(dependsOnMethods = { "validateConfLineOnWD40Host" },invocationCount = 2)
	public void verifyAppInstanceRestart() throws InterruptedException{
		//Wait till the process is reported to carnival
		sshKillAppInstance();
		Reporter.log("Wait five(5) minutes so that process is reported to carnival",true);
		Thread.sleep(300000);
		String newProcessID=null;
		
		home.getOverViewOfHostsPage();
		overviewHosts.selectHost(wd40Host);
		
		List<PSConfLine> psConfLineEntries = hostPage.getCurrentlyRunningConfEntries();
		
		Reporter.log("Peforming PS Conf Validations",true);
		for(PSConfLine confLine:psConfLineEntries){
			Reporter.log("The app name is :"+ confLine.getISAppName(),true);
			Reporter.log("The instance is :"+ confLine.getInstance(),true);
			if(confLine.getISAppName().equals(expectedAppName)&&confLine.getInstance().equals(expectedInstance)){
				newProcessID = confLine.getPID();
				Reporter.log("The new process id is :"+newProcessID,true);
				Reporter.log("PS ConfLine verified successfully",true);
				Reporter.log("Verifying to check if new process id is generated and validate its not same as the old one",true);
				Assert.assertNotEquals(newProcessID, processID);
				processID = newProcessID;
			}
			
			
		}
		
		Assert.assertNotNull(newProcessID);
	}
	
	private void sshKillAppInstance() throws InterruptedException {
		Reporter.log("The app instance restart",true);
		String expectedOutput = "";
		//The ssh user in the framework.properties should have sudo accesss to kill the process
		String shellCommand = "sudo kill -9 "+processID;
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to kill the app instance on WD40 Host :"+wd40Host,true);

		// Disk Usage
		if (!StringUtils.isEmpty(domain))
			commandOutPut = JcraftUtil.executeShellCommand(wd40Host + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(wd40Host,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);

		Assert.assertTrue(commandOutPut.matches(expectedOutput),"Expected Out put is not same as Actual Out put");

	}
	
	@Test(dependsOnMethods = { "verifyAppInstanceRestart" })
	public void uninstallClownPackageUsingCreateRequestWorkFlowWithForce() throws InterruptedException {

		Reporter.log("Uninstalling the package from carnival UI",true);
		String expectedNotice = "Successfully scheduled task \\d{13} for execution!";

		home.getCreateRequestPage();
		CreateRequestPage createRequest = new CreateRequestPage();

		String actualNotice = createRequest.addRequestAndScheduleToUninstallPackage("100.0",project, packageName,true,2,null);

		Reporter.log("Uninstall Successfull",true);
		Reporter.log("Performing Assertions",true);
		Reporter.log("Expected note is : " + expectedNotice + " and got  note from server as  : " + actualNotice,true);

		assertTrue(actualNotice.matches(expectedNotice),"Expected and Actual Do not Match");
		
		//Wait for 30 seconds to allow packaged to be removed from file system
		Thread.sleep(30000);
	}


	@AfterClass
	public void cleanUp() throws InterruptedException {
		//Wait before logging out and quitting the webdriver session
		Thread.sleep(2000);
		Reporter.log("\nLogging out and closing  browser session\n",true);
		home.logout();
		CarnivalUI.stopDriver();

	}

}