package com.apple.carnival.api.request;


import static org.testng.Assert.assertTrue;
import static org.testng.Assert.assertFalse;

import org.apache.commons.lang.StringUtils;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.apple.carnival.qa.common.util.JcraftUtil;
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
public class UninstallPackageFromCarnivalHostTests {

	private Configuration config = null; 
	private String carnivalURL = null;
	
	private String carnivalHost = null;
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
	@Parameters({"carnival.host","carnival.wd40host","carnival.domain","carnival.project","carnival.package"})
	public void init(String carnivalHost,String wd40Host, @Optional String domain,String project, String packageName){
		this.carnivalHost = carnivalHost;
		this.wd40Host= wd40Host;
		this.domain = domain;
		this.project = project;
		this.packageName = packageName;
		
		Reporter.log("UninstallPackageFromCarnivalHostTests tests with the following details :",true);
		Reporter.log("CARNIVA HOST :"+carnivalHost,true);
		Reporter.log("WD40 HOST :"+wd40Host,true);
		Reporter.log("DOMAIN :"+domain,true);
		Reporter.log("PROEJCT :"+project,true);
		Reporter.log("PACKAGE  :"+packageName,true);

	}
	
	@Test(dependsOnMethods = { "init" })
	public void createAndScheduleRequestTestToInstallPackageOnCarnival() throws InterruptedException {
		Reporter.log("Install the package :"+packageName+" from carnival UI",true);
		String expectedNotice = "Successfully scheduled task \\d{13} for execution!";
		
		home.getCreateRequestPage();
		CreateRequestPage createRequest = new CreateRequestPage();

		String actualNotice = createRequest.peformLocalInstallBuild("100.0",project, packageName,true,1);
		
		Reporter.log("Install Successfull",true);
		Reporter.log("Performing Assertions",true);
		Reporter.log("Expected note is : " + expectedNotice + " and got  note from server as  : " + actualNotice,true);

		assertTrue(actualNotice.matches(expectedNotice),"Expected and Actual Do not Match");
		
	}

	@Test(dependsOnMethods = { "createAndScheduleRequestTestToInstallPackageOnCarnival" })
	public void checkIfPackageIsCurrentOnCarnival() throws InterruptedException {
		//Wait for about 15 mins to see that package becomes current after force install
		Thread.sleep(950000);
		home.getOverViewOfHostsPage();

		overviewHosts.selectHost(carnivalHost);

		String installedSoftware = hostPage.getInstalledSoftware();

		Reporter.log("The installed software is :\n :"+ installedSoftware,true);

		assertTrue(installedSoftware.contains(packageName+" (Current)"),"Element should have been Present");

	}
	
	@Test(dependsOnMethods = { "checkIfPackageIsCurrentOnCarnival" })
	public void checkPackageDoesNotExistOnWD40_HOST() throws InterruptedException {
		//Wait to synchronize the next page
		Thread.sleep(4000);
		home.getOverViewOfHostsPage();

		overviewHosts.selectHost(wd40Host);

		String installedSoftware = hostPage.getInstalledSoftware();

		Reporter.log("The installed software is :\n :"+ installedSoftware,true);

		assertFalse(installedSoftware.contains(packageName),"Element should not have been Present");

	}
	
	@Test(dependsOnMethods = { "checkPackageDoesNotExistOnWD40_HOST" })
	public void uninstallPackageFromCarnivalHostWhileItsCurrent() throws InterruptedException {

		Reporter.log("Uninstalling the package from carnival UI",true);
		String expectedNotice = "Successfully scheduled task \\d{13} for execution!";

		home.getCreateRequestPage();
		CreateRequestPage createRequest = new CreateRequestPage();

		String actualNotice = createRequest.addRequestAndScheduleToUninstallPackage("100.0",project, packageName,true,1,null);

		Reporter.log("Uninstall Successfull",true);
		Reporter.log("Performing Assertions",true);
		Reporter.log("Expected note is : " + expectedNotice + " and got  note from server as  : " + actualNotice,true);

		assertTrue(actualNotice.matches(expectedNotice),"Expected and Actual Do not Match");
	}

	@Test(dependsOnMethods = { "uninstallPackageFromCarnivalHostWhileItsCurrent" })
	public void checkIfPackageIsNotCurrentOnCarnival() throws InterruptedException {
		//Wait till the operation from the previous test has been complete
		Thread.sleep(20000);
		home.getOverViewOfHostsPage();

		overviewHosts.selectHost(carnivalHost);

		String installedSoftware = hostPage.getInstalledSoftware();

		Reporter.log("The installed software is :\n :"+ installedSoftware,true);

		assertFalse(installedSoftware.contains(packageName+" (Current)"),"Element should not have been Present");

	}
	
	@Test(dependsOnMethods = { "checkIfPackageIsNotCurrentOnCarnival" })
	public void sshCheckPackageDoesNotExistsOnCarnival() throws InterruptedException {

		String expectedOutput = "";
		String shellCommand = "du -sk /opt/ais/local/CarnivalQA/cottoncandy_bin/Current/";
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to validate package does not exists on WD40 host",true);

		// Disk Usage
		if (!StringUtils.isEmpty(domain))
			commandOutPut = JcraftUtil.executeShellCommand(carnivalHost + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(carnivalHost,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);

		Assert.assertTrue(commandOutPut.matches(expectedOutput),"Expected Out put is not same as Actual Out put");
	}
	
	@Test(dependsOnMethods = { "sshCheckPackageDoesNotExistsOnCarnival" })
	public void sshCheckPackageDoesNotExistsOnWD40_HOST() throws InterruptedException {
	
		String expectedOutput = "";
		String shellCommand = "du -sk /opt/ais/local/CarnivalQA/cottoncandy_bin/Current/";
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to validate package does not exists on WD40 host",true);

		// Disk Usage
		if (!StringUtils.isEmpty(domain))
			commandOutPut = JcraftUtil.executeShellCommand(wd40Host + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(wd40Host,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);

		Assert.assertTrue(commandOutPut.matches(expectedOutput),"Expected Out put is not same as Actual Out put");
	}
	

	@AfterClass
	public void cleanUp() throws InterruptedException {
		//Wait 2 seconds before quitting the browser
		Thread.sleep(2000);
		Reporter.log("\nLogging out and closing  browser session\n",true);
		//home.logout();
		CarnivalUI.stopDriver();

	}

}