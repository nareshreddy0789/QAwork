package com.apple.carnival.api.request;


import static org.testng.Assert.assertTrue;

import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.apple.carnival.qa.common.util.JcraftUtil;
import com.apple.carnival.qa.coreframework.data.pageobjects.BinaryConfLine;
import com.apple.carnival.qa.coreframework.data.pageobjects.PSConfLine;
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
public class CopyOfAutoRestartTests {

	private Configuration config = null; 
	private String carnivalURL = null;
	private static final String WD40_HOST = "vp21q01if-ztbu14114701";
	private static final String DOMAIN = "vp.if1.apple.com";
	private static final String EXPECTED_APP_NAME_1="clown";
	private static final String EXPECTED_INSTANCE_1="1011";
	private String processID = null;
	private static final String PROJECT = "CarnivalQA";
	private static final String PACKAGE_TO_INSTALL = "clown14A147";
	
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

	/*
	 * This test assumes that we have clownCONF14A162 package existing on Carnival and WD40 host
	 */
	@Test
	public void createAndScheduleRequestTestToInstallPackage() throws InterruptedException {
		Reporter.log("Install the package :"+PACKAGE_TO_INSTALL+" from carnival UI",true);
		String expectedNotice = "Successfully scheduled task \\d{13} for execution!";
		
		home.getCreateRequestPage();
		CreateRequestPage createRequest = new CreateRequestPage();

		String actualNotice = createRequest.peformLocalInstallBuild("100.0",PROJECT, PACKAGE_TO_INSTALL,true,2);
		
		Reporter.log("Install the package : "+PACKAGE_TO_INSTALL ,true);
		Reporter.log("Performing Assertions",true);
		Reporter.log("Expected note is : " + expectedNotice + " and got  note from server as  : " + actualNotice,true);

		assertTrue(actualNotice.matches(expectedNotice),"Expected and Actual Do not Match");
		
	}
	
	@Test(dependsOnMethods = { "createAndScheduleRequestTestToInstallPackage" })
	public void validateConfLineOnWD40Host() throws InterruptedException {
		//Wait untill the packaged is scheduled from createAndScheduleRequestTestToInstallPackage test
		Thread.sleep(30000);
		home.getOverViewOfHostsPage();
		overviewHosts.selectHost(WD40_HOST);	
		List<BinaryConfLine> confLineEntires = hostPage.getBinaryConfLines();
		
		Reporter.log("Peforming Binary Conf Validations",true);
		for(BinaryConfLine confLine:confLineEntires){
			Reporter.log("The app name is :"+ confLine.getISAppName(),true);
			Reporter.log("The instance is :"+ confLine.getInstance(),true);
			if(confLine.getISAppName().equals(EXPECTED_APP_NAME_1)&&confLine.getInstance().equals(EXPECTED_INSTANCE_1)){
				Reporter.log("Binary ConfLine verified successfully",true);
			}
			
			
		}
		
		List<PSConfLine> psConfLineEntries = hostPage.getCurrentlyRunningConfEntries();
		
		Reporter.log("Peforming PS Conf Validations",true);
		for(PSConfLine confLine:psConfLineEntries){
			Reporter.log("The app name is :"+ confLine.getISAppName(),true);
			Reporter.log("The instance is :"+ confLine.getInstance(),true);
			if(confLine.getISAppName().equals(EXPECTED_APP_NAME_1)&&confLine.getInstance().equals(EXPECTED_INSTANCE_1)){
				processID = confLine.getPID();
				Reporter.log("The process id is :"+processID,true);
				Reporter.log("PS ConfLine verified successfully",true);
			}
			
			
		}

	}
	
	@Test(dependsOnMethods = { "validateConfLineOnWD40Host" },invocationCount = 2)
	public void verifyAppInstanceRestart() throws InterruptedException{
		//Wait till the process is reported to carnival
		sshKillAppInstance();
		Reporter.log("Wait five(5) minutes so that process is reported to carnival",true);
		Thread.sleep(300000);
		String newProcessID=null;
		
		home.getOverViewOfHostsPage();
		overviewHosts.selectHost(WD40_HOST);
		
		List<PSConfLine> psConfLineEntries = hostPage.getCurrentlyRunningConfEntries();
		
		Reporter.log("Peforming PS Conf Validations",true);
		for(PSConfLine confLine:psConfLineEntries){
			Reporter.log("The app name is :"+ confLine.getISAppName(),true);
			Reporter.log("The instance is :"+ confLine.getInstance(),true);
			if(confLine.getISAppName().equals(EXPECTED_APP_NAME_1)&&confLine.getInstance().equals(EXPECTED_INSTANCE_1)){
				newProcessID = confLine.getPID();
				Reporter.log("The new process id is :"+newProcessID,true);
				Reporter.log("PS ConfLine verified successfully",true);
				Reporter.log("Verifying to check if new process id is generated and validate its not same as the old one",true);
				Assert.assertNotEquals(newProcessID, processID);
				processID = newProcessID;
			}
			
			
		}
		
		
	}
	
	private void sshKillAppInstance() throws InterruptedException {
		Reporter.log("The app instance restart",true);
		String expectedOutput = "";
		//The ssh user in the framework.properties should have sudo accesss to kill the process
		String shellCommand = "sudo kill -9 "+processID;
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to kill the app instance on WD40 Host :"+WD40_HOST,true);

		// Disk Usage
		if (!StringUtils.isEmpty(DOMAIN))
			commandOutPut = JcraftUtil.executeShellCommand(WD40_HOST + "." + DOMAIN, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(WD40_HOST,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);

		Assert.assertTrue(commandOutPut.matches(expectedOutput),"Expected Out put is not same as Actual Out put");

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