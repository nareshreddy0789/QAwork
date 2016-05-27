package com.apple.carnival.ui.localinstall;


import static org.testng.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.apple.carnival.qa.common.util.JcraftUtil;
import com.apple.carnival.qa.coreframework.properties.Configuration;
import com.apple.carnival.qa.coreframework.properties.FrameworkConfigurationReader;
import com.apple.carnival.ui.CarnivalUI;
import com.apple.carnival.ui.CreateRequestPage;
import com.apple.carnival.ui.EnvironmentInfoPage;
import com.apple.carnival.ui.HomePage;
import com.apple.carnival.ui.HostDetailPage;
import com.apple.carnival.ui.LoginPage;
import com.apple.carnival.ui.OverviewOfHostsPage;
import com.apple.carnival.ui.RunningRequestsPage;
import com.apple.carnival.ui.TaskDetailsPage;
import com.apple.carnival.ui.ViewActionLogsPage;
import com.apple.carnival.utils.TestStringUtils;
/**
 * 
 * @author harisha 
 * This POST request is based on ip authentication, which means,from any machine before this test is run, we need to grab the IP
 * address and let the carnival know the ip adress. To do that, we needto put the following property in carnival.properties (or
 * ci-dev.properties of Carnival) we need to add ip address in carnival.web_service.admin.ip_whitelist
 */
@SuppressWarnings("deprecation")
public class LocalInstallHotfixUsingNoForceInstallOptionWhenPackageIsAvailableTests {

	private Configuration config = null; 
	private String carnivalURL = null;
	private String project = null;
	private String packageName = null;
	private String requestID=null;
	private String domain = null;
	private String carnivalHost = null;
	private String wd40host1 = null;
	private String wd40host2 = null;
	private static final String LOCATION_OF_SER_FILE_ON_CARNIVAL_HOST = "/opt/ais/local/.wd40-inventory/Install/";
	
	HomePage home = new HomePage();
	OverviewOfHostsPage overviewHosts = new OverviewOfHostsPage();
	HostDetailPage hostPage = new HostDetailPage();
	CreateRequestPage createRequest = new CreateRequestPage();
	TaskDetailsPage taskDetailsPage = new TaskDetailsPage();
	
	@BeforeClass
	public void setConfig() {
		Reporter.log("Initializing and Loading Configuration",true);

		config = new FrameworkConfigurationReader("framework.properties");	
		carnivalURL = config.getProperty("carnival.url");
		String driverType = config.getProperty("webdriverType");
		CarnivalUI.startDriver(carnivalURL,driverType);
		LoginPage login = new LoginPage();
		login.loginToCarnival(config.getProperty("carnival.ui.user"), config.getProperty("carnival.ui.password"));

	}

	@Test
	@Parameters({"carnival.host","carnival.wd40host1","carnival.wd40host2","carnival.domain","carnival.project","carnival.package"})
	public void init(String carnivalHost,String wd40host1, String wd40host2,String domain,String project, String packageName){
		this.carnivalHost = carnivalHost;
		this.wd40host1 = wd40host1;
		this.wd40host2 = wd40host2;
		this.domain = domain;
		this.project = project;
		this.packageName = packageName;
		
		Reporter.log("LocalInstallHotfixUsingNoForceInstallOptionWhenPackageIsAvailableTests tests with the following details :",true);
		Reporter.log("Carnival Host :"+carnivalHost,true);
		Reporter.log("Domain  :"+domain,true);
		Reporter.log("PROEJCT :"+project,true);
		Reporter.log("PACKAGE  :"+packageName,true);

	}


	/*
	 * Since we will be intalling a package that does not exist, it does not matter if we select
	 * force option or not
	 */
	@Test(dependsOnMethods = { "init" })
	public void installPackageUsingValidPackageName() throws InterruptedException {
	
		Reporter.log("Local Install Hotfix from carnival UI with valid package name",true);

		String expectedNotice = "Successfully scheduled task \\d{13} for execution!";
		home.getCreateRequestPage();
		//Install the package on  Carnival and hosts configured in conf
		//Force Install is set to yes. It Acually does not matter if force install is set to Yes or No when the
		//package is unavailable on the file system
		String actualNoticeMessage = createRequest.peformLocalInstallHotfix("100.0",project,packageName,false,2,"New feature");
		
		System.out.println("The server response is :" + actualNoticeMessage);
		Reporter.log("Performing Assertions",true);
		Reporter.log("Expected notice is : " +  expectedNotice  + " and got error message from server as  : " + actualNoticeMessage,true);
		assertTrue(actualNoticeMessage.matches(expectedNotice),"Expected and Actual Do not Match");
		requestID = TestStringUtils.parseNoticeMessageToRetrieveRequestID(actualNoticeMessage);
		Reporter.log("The request ID is :"+requestID,true);
		//wait to sync for the next test
		Thread.sleep(2000);
	}
	
	
	@Test(dependsOnMethods = { "installPackageUsingValidPackageName" })
	public void validateEnvironmentInfoPageToCheckPackageIsCurrent() throws InterruptedException {
	
		Reporter.log("Validate Environment Info Page to check the package is Current",true);
		Reporter.log("Wait for 15 mins for the Environemnt Info page to refresh",true);
				
		//Wait for 15 mins to change the status to Current on Environment Info Page
		Thread.sleep(950000);
		
		home.getEnvironmentInfoPage();
		EnvironmentInfoPage envInfoPage = new EnvironmentInfoPage();
		String packages = envInfoPage.getPackages(project);
		
		Reporter.log("The packages are :"+packages,true);
		Reporter.log("Validate the package :"+packageName+" is current",true);
		assertTrue(packages.contains("("+packageName+")"),"Element should have been Present");
		//wait to sync for the next test
		Thread.sleep(2000);
	}
	
	@Test(dependsOnMethods = { "validateEnvironmentInfoPageToCheckPackageIsCurrent" })
	public void reinstallExistingPackageUsingNoForceOption() throws InterruptedException {
	
		Reporter.log("Local Install Hotfix from carnival UI with an already existing package name",true);

		String expectedNotice = "Successfully scheduled task \\d{13} for execution!";
		home.getCreateRequestPage();
		//Install the package on  Carnival and hosts configured in conf
		//Force Install is set to yes. 
		//package is unavailable on the file system
		String actualNoticeMessage = createRequest.peformLocalInstallHotfix("100.0",project,packageName,false,2,"New feature");
		
		System.out.println("The server response is :" + actualNoticeMessage);
		Reporter.log("Performing Assertions",true);
		Reporter.log("Expected notice is : " +  expectedNotice  + " and got error message from server as  : " + actualNoticeMessage,true);
		assertTrue(actualNoticeMessage.matches(expectedNotice),"Expected and Actual Do not Match");
		requestID = TestStringUtils.parseNoticeMessageToRetrieveRequestID(actualNoticeMessage);
		Reporter.log("The request ID is :"+requestID,true);
		//wait to sync for the next test
		Thread.sleep(2000);
	}
	@Test(dependsOnMethods = { "reinstallExistingPackageUsingNoForceOption" })
	public void validateTaskDetailsPageAfterReinstall() throws InterruptedException {
	
		Reporter.log("Validate Task Details Page after reinstall",true);
		//Wait for the status to change to COMPLETE
		Thread.sleep(35000);
		RunningRequestsPage requestsPage = new RunningRequestsPage();
		String taskDetailsURL = carnivalURL+"/TaskDetail.jsp?id="+requestID;
		Reporter.log("The task detail url is :"+taskDetailsURL,true );
		requestsPage.getTaskDetailsPage(taskDetailsURL);
		
		String currentStatus = taskDetailsPage.getCurrentState();
		
		Reporter.log("The Current Status is : " + currentStatus,true);
		Assert.assertEquals(currentStatus, "COMPLETED");
		//wait to sync for the next test
		Thread.sleep(2000);
	}
	
	@Test(dependsOnMethods = { "validateTaskDetailsPageAfterReinstall" })
	public void validateTaskDetailsLogsAfterReinstall() throws InterruptedException {
		Reporter.log("Validate View Logs Page after reinstall",true);
		String expectedLogEntry1 = "Determining hosts missing package: "+packageName+".  Sending followup install requests to 0 active host(s).";
		String expectedLogEntry3 = "Install of "+packageName+" for project "+project+" succeeded on all required hosts.";
		Reporter.log("Validate Taks Details Logs",true);

		taskDetailsPage.getViewActionLogsPage();
		
		ViewActionLogsPage actionLogs = new ViewActionLogsPage();
		String viewLogs = actionLogs.getActionLog();
		
		Reporter.log("The Action Log is : " + viewLogs,true);
		
		Assert.assertTrue(viewLogs.contains(expectedLogEntry1),"Log Entry should have been there");
		Assert.assertTrue(viewLogs.contains(expectedLogEntry3),"Log Entry should have been there");
	}
	
	
	@Test(dependsOnMethods = { "validateTaskDetailsLogsAfterReinstall" })
	public void validateEnvironmentInfoPageToCheckPackageIsCurrentAfterReinstall() throws InterruptedException {
	
		Reporter.log("Validate Environment Info Page to check the package is Current",true);
		Reporter.log("Wait for 15 mins for the Environemnt Info page to refresh",true);
				
		//Wait for 15 mins to change the status to Current on Environment Info Page
		Thread.sleep(950000);
		
		home.getEnvironmentInfoPage();
		EnvironmentInfoPage envInfoPage = new EnvironmentInfoPage();
		String packages = envInfoPage.getPackages(project);
		
		Reporter.log("The packages are :"+packages,true);
		Reporter.log("Validate the package :"+packageName+" is current",true);
		assertTrue(packages.contains("("+packageName+")"),"Element should have been Present");
		//wait to sync for the next test
		Thread.sleep(2000);
	}
	
	@Test(dependsOnMethods = { "validateEnvironmentInfoPageToCheckPackageIsCurrentAfterReinstall" })
	public void validateFileSystemOnWD40Host1ToCheckSerFileExists() throws InterruptedException {
	
		Reporter.log("Check to see .ser file exists under /opt/ais/local/.wd40-inventory/Install ",true);
		
		String expectedOutput = LOCATION_OF_SER_FILE_ON_CARNIVAL_HOST+packageName+".ser";
		String shellCommand = "ls "+LOCATION_OF_SER_FILE_ON_CARNIVAL_HOST+packageName+".ser";
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to validate package does exists on carnival host",true);
		Reporter.log(".ser file location is :"+expectedOutput,true);
		// Disk Usage
		if (!StringUtils.isEmpty(domain))
			commandOutPut = JcraftUtil.executeShellCommand(wd40host1 + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(wd40host1,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);

		Assert.assertTrue((commandOutPut.trim()).matches(expectedOutput),"Expected Out put is not same as Actual Out put");

		//wait to sync for the next test
		Thread.sleep(2000);
	}
	
	@Test(dependsOnMethods = { "validateFileSystemOnWD40Host1ToCheckSerFileExists" })
	public void validateFileSystemOnWD40Host2ToCheckSerFileExists() throws InterruptedException {
	
		Reporter.log("Check to see .ser file exists under /opt/ais/local/.wd40-inventory/Install ",true);
		
		String expectedOutput = LOCATION_OF_SER_FILE_ON_CARNIVAL_HOST+packageName+".ser";
		String shellCommand = "ls "+LOCATION_OF_SER_FILE_ON_CARNIVAL_HOST+packageName+".ser";
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to validate package does exists on carnival host",true);
		Reporter.log(".ser file location is :"+expectedOutput,true);
		// Disk Usage
		if (!StringUtils.isEmpty(domain))
			commandOutPut = JcraftUtil.executeShellCommand(wd40host2 + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(wd40host2,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);

		Assert.assertTrue((commandOutPut.trim()).matches(expectedOutput),"Expected Out put is not same as Actual Out put");

		//wait to sync for the next test
		Thread.sleep(2000);
	}
	@Test(dependsOnMethods = { "validateFileSystemOnWD40Host2ToCheckSerFileExists" })
	public void validateFileSystemOnCarnivalHostToCheckSerFileExists() throws InterruptedException {
	
		Reporter.log("Check to see .ser file exists under /opt/ais/local/.wd40-inventory/Install ",true);
		
		String expectedOutput = LOCATION_OF_SER_FILE_ON_CARNIVAL_HOST+packageName+".ser";
		String shellCommand = "ls "+LOCATION_OF_SER_FILE_ON_CARNIVAL_HOST+packageName+".ser";
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to validate package does exists on carnival host",true);
		Reporter.log(".ser file location is :"+expectedOutput,true);
		// Disk Usage
		if (!StringUtils.isEmpty(domain))
			commandOutPut = JcraftUtil.executeShellCommand(carnivalHost + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(carnivalHost,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);

		Assert.assertTrue((commandOutPut.trim()).matches(expectedOutput),"Expected Out put is not same as Actual Out put");

		//wait to sync for the next test
		Thread.sleep(2000);
	}
	@Test(dependsOnMethods = { "validateFileSystemOnCarnivalHostToCheckSerFileExists" })
	public void validateFileSystemOnWD40HostToCheckCorruptFileDoesNotExists() throws InterruptedException {
	
		Reporter.log("Check to see .corrupt file does not exists under /opt/ais/local/.wd40-inventory/Install ",true);
		
		String expectedOutput = "";
		String shellCommand = "ls "+LOCATION_OF_SER_FILE_ON_CARNIVAL_HOST+packageName+".corrupt";
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to validate package does exists on carnival host",true);
		Reporter.log(".ser file location is :"+expectedOutput,true);
		// Disk Usage
		if (!StringUtils.isEmpty(domain))
			commandOutPut = JcraftUtil.executeShellCommand(wd40host1 + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(wd40host1,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);

		Assert.assertTrue((commandOutPut.trim()).matches(expectedOutput),"Expected Out put is not same as Actual Out put");

		//wait to sync for the next test
		Thread.sleep(2000);
	}
	@Test(dependsOnMethods = { "validateFileSystemOnWD40HostToCheckCorruptFileDoesNotExists" })
	public void validateFileSystemOnCarnivalHostToCheckCorruptFileDoesNotExists() throws InterruptedException {
	
		Reporter.log("Check to see .corrupt file does not exists under /opt/ais/local/.wd40-inventory/Install ",true);
		
		String expectedOutput = "";
		String shellCommand = "ls "+LOCATION_OF_SER_FILE_ON_CARNIVAL_HOST+packageName+".corrupt";
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to validate package does exists on carnival host",true);
		Reporter.log(".ser file location is :"+expectedOutput,true);
		// Disk Usage
		if (!StringUtils.isEmpty(domain))
			commandOutPut = JcraftUtil.executeShellCommand(carnivalHost + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(carnivalHost,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);

		Assert.assertTrue((commandOutPut.trim()).matches(expectedOutput),"Expected Out put is not same as Actual Out put");

		//wait to sync for the next test
		Thread.sleep(2000);
	}
	@Test(dependsOnMethods = { "validateFileSystemOnCarnivalHostToCheckCorruptFileDoesNotExists" })
	public void uninstallPackageFromCarnivalHostWhileItsCurrent() throws InterruptedException {

		Reporter.log("Uninstalling the package from carnival UI",true);
		String expectedNotice = "Successfully scheduled task \\d{13} for execution!";

		home.getCreateRequestPage();
		CreateRequestPage createRequest = new CreateRequestPage();

		String actualNoticeMessage = createRequest.addRequestAndScheduleToUninstallPackage("100.0",project, packageName,true,2,null);

		requestID = TestStringUtils.parseNoticeMessageToRetrieveRequestID(actualNoticeMessage);
		
		Reporter.log("Uninstall Successfull",true);
		Reporter.log("Performing Assertions",true);
		Reporter.log("Expected note is : " + expectedNotice + " and got  note from server as  : " + actualNoticeMessage,true);

		assertTrue(actualNoticeMessage.matches(expectedNotice),"Expected and Actual Do not Match");
	}
	
	@Test(dependsOnMethods = { "uninstallPackageFromCarnivalHostWhileItsCurrent" })
	public void validateEnvironmentInfoPageToCheckPackageIsNotCurrent() throws InterruptedException {
	
		Reporter.log("Validate Environment Info Page to check the package is not Current",true);
		Reporter.log("Wait for 15 mins for the Environemnt Info page to refresh",true);
		
		//Wait for 15 mins to change the status to Current on Environment Info Page
		Thread.sleep(950000);
		
		home.getEnvironmentInfoPage();
		EnvironmentInfoPage envInfoPage = new EnvironmentInfoPage();
		String packages = envInfoPage.getPackages(project);
		
		Reporter.log("The packages are :"+packages,true);
		Reporter.log("Validate the package :"+packageName+" is not current",true);
		Assert.assertFalse(packages.contains(packageName),"Element should not have been Present");

	}
	
	@Test(dependsOnMethods = { "validateEnvironmentInfoPageToCheckPackageIsNotCurrent" })
	public void validateTaskDetailsPageAfterUninstall() throws InterruptedException {
	
		Reporter.log("Validate Task Details Page",true);
		//Wait for the status to change to COMPLETE
		Thread.sleep(35000);
		RunningRequestsPage requestsPage = new RunningRequestsPage();
		String taskDetailsURL = carnivalURL+"/TaskDetail.jsp?id="+requestID;
		Reporter.log("The task detail url is :"+taskDetailsURL,true );
		requestsPage.getTaskDetailsPage(taskDetailsURL);
		
		String currentStatus = taskDetailsPage.getCurrentState();
		
		Reporter.log("The Current Status is : " + currentStatus,true);
		Assert.assertEquals(currentStatus, "COMPLETED");
		//wait to sync for the next test
		Thread.sleep(2000);
	}
	
	@Test(dependsOnMethods = { "validateTaskDetailsPageAfterUninstall" })
	public void validateTaskDetailsLogsAfterUinstall() throws InterruptedException {
		String expectedLogEntry = "Uninstall of "+packageName+" for project "+project+" has succeeded on 100.0% ( 2 out of 2 ) of required hosts.  Acceptance Threshold entered by user: 100.0%.";
		
		Reporter.log("Validate Taks Details Logs",true);

		taskDetailsPage.getViewActionLogsPage();
		
		ViewActionLogsPage actionLogs = new ViewActionLogsPage();
		String viewLogs = actionLogs.getActionLog();
		
		Reporter.log("The Action Log is : " + viewLogs,true);
		
		Assert.assertTrue(viewLogs.contains(expectedLogEntry),"Log Entry should have been there");
	
	}
	
	@Test(dependsOnMethods = { "validateTaskDetailsLogsAfterUinstall" })
	public void validateFileSystemOnWD40Host1ToCheckSerFileDoesNotExists() throws InterruptedException {
	
		Reporter.log("Check to see .ser file does not exists under /opt/ais/local/.wd40-inventory/Install ",true);
		
		String expectedOutput = "";
		String shellCommand = "ls "+LOCATION_OF_SER_FILE_ON_CARNIVAL_HOST+packageName+".ser";
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to validate package does exists on carnival host",true);
		Reporter.log(".ser file location is :"+LOCATION_OF_SER_FILE_ON_CARNIVAL_HOST,true);
		// Disk Usage
		if (!StringUtils.isEmpty(domain))
			commandOutPut = JcraftUtil.executeShellCommand(wd40host1 + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(wd40host1,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);

		Assert.assertTrue((commandOutPut.trim()).matches(expectedOutput),"Expected Out put is not same as Actual Out put");

		//wait to sync for the next test
		Thread.sleep(2000);
	}
	

	@Test(dependsOnMethods = { "validateFileSystemOnWD40Host1ToCheckSerFileDoesNotExists" })
	public void validateFileSystemOnWD40Host2ToCheckSerFileDoesNotExists() throws InterruptedException {
	
		Reporter.log("Check to see .ser file exists under /opt/ais/local/.wd40-inventory/Install ",true);
		
		String expectedOutput = "";
		String shellCommand = "ls "+LOCATION_OF_SER_FILE_ON_CARNIVAL_HOST+packageName+".ser";
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to validate package does exists on carnival host",true);
		Reporter.log(".ser file location is :"+expectedOutput,true);
		// Disk Usage
		if (!StringUtils.isEmpty(domain))
			commandOutPut = JcraftUtil.executeShellCommand(wd40host2 + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(wd40host2,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);

		Assert.assertTrue((commandOutPut.trim()).matches(expectedOutput),"Expected Out put is not same as Actual Out put");

		//wait to sync for the next test
		Thread.sleep(2000);
	}
	
	@Test(dependsOnMethods = { "validateFileSystemOnWD40Host2ToCheckSerFileDoesNotExists" })
	public void validateFileSystemOnCarnivalHostToCheckSerFileDoesNotExists() throws InterruptedException {
	
		Reporter.log("Check to see .ser file exists under /opt/ais/local/.wd40-inventory/Install ",true);
		
		String expectedOutput = "";
		String shellCommand = "ls "+LOCATION_OF_SER_FILE_ON_CARNIVAL_HOST+packageName+".ser";
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to validate package does exists on carnival host",true);
		Reporter.log(".ser file location is :"+expectedOutput,true);
		// Disk Usage
		if (!StringUtils.isEmpty(domain))
			commandOutPut = JcraftUtil.executeShellCommand(carnivalHost + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(carnivalHost,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);

		Assert.assertTrue((commandOutPut.trim()).matches(expectedOutput),"Expected Out put is not same as Actual Out put");

		//wait to sync for the next test
		Thread.sleep(2000);
	}
	
	@AfterClass
	public void cleanUp() throws InterruptedException {
		Thread.sleep(2000);
		Reporter.log("\nLogging out and closing  browser session\n",true);
		//home.logout();
		CarnivalUI.stopDriver();

	}

}