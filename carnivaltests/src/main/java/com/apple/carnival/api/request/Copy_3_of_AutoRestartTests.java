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
public class Copy_3_of_AutoRestartTests {

	private Configuration config = null; 
	private String carnivalURL = null;
	private String processID = null;
	
	private String wd40Host = null;
	private String vmBase = null;
	private String domain = null;
	private String project = null;
	private String packageToInstall = null;
	private String expectedAppName = null;
	private String expectedInstance = null;
	
	HomePage home = new HomePage();
	OverviewOfHostsPage overviewHosts = new OverviewOfHostsPage();
	HostDetailPage hostPage = new HostDetailPage();

	
	@BeforeClass
	@Parameters({"carnival.url","vm.base"})
	public void setConfig(@Optional String appUrl,@Optional String vmBase) {
		Reporter.log("Initializing and Loading Configuration",true);
		config = new FrameworkConfigurationReader("framework.properties");
		if(appUrl == null)
			carnivalURL = config.getProperty("carnival.url");
		else
			carnivalURL = appUrl;
		this.vmBase = vmBase;
		Reporter.log("Carnival URL is : "+carnivalURL,true);
		String driverType = config.getProperty("webdriverType");
		CarnivalUI.startDriver(carnivalURL,driverType);
		LoginPage login = new LoginPage();
		login.loginToCarnival(config.getProperty("carnival.ui.user"), config.getProperty("carnival.ui.password"));
	}
	
	@Test
	@Parameters({"carnival.domain","carnival.wd40host","carnival.project","carnival.package","carnival.appname"})
	public void init(@Optional String domain,String wd40Host,String project, String packageToInstall,String expectedAppName){
		this.domain = domain;
		this.wd40Host=wd40Host;
		this.project = project;
		this.packageToInstall = packageToInstall;
		this.expectedAppName = expectedAppName;
		
		Reporter.log("Auto restart tests with the following init test data :",true);
		Reporter.log("VM BASE :"+vmBase,true);
		Reporter.log("DOMAIN :"+domain,true);
		Reporter.log("PROEJCT :"+project,true);
		Reporter.log("PACKAGE  :"+packageToInstall,true);
		Reporter.log("EXPECTED APP NAME  :"+expectedAppName,true);
	
	}
	/*
	 * This test assumes that we have clownCONF14A162 package existing on Carnival and WD40 host
	 */
	@Test(dependsOnMethods = { "init" })
	public void createAndScheduleRequestTestToInstallPackage() throws InterruptedException {
		Reporter.log("Install the package :"+packageToInstall+" from carnival UI",true);
		String expectedNotice = "Successfully scheduled task \\d{13} for execution!";
		
		home.getCreateRequestPage();
		CreateRequestPage createRequest = new CreateRequestPage();

		String actualNotice = createRequest.peformLocalInstallBuild("100.0",project, packageToInstall,true,2);
		
		Reporter.log("Install the package : "+packageToInstall ,true);
		Reporter.log("Performing Assertions",true);
		Reporter.log("Expected note is : " + expectedNotice + " and got  note from server as  : " + actualNotice,true);

		assertTrue(actualNotice.matches(expectedNotice),"Expected and Actual Do not Match");
		
	}
	
	@Test(dependsOnMethods = { "createAndScheduleRequestTestToInstallPackage" })
	public void validateConfLineOnWD40Host() throws InterruptedException {
		//Wait untill the packaged is scheduled from createAndScheduleRequestTestToInstallPackage test
		Reporter.log("wait for 15 seconds until Package gets settled");
		Thread.sleep(30000);
		home.getOverViewOfHostsPage();

/*		for(WD40Host host : overviewHosts.getWD40HostList()) {
			if ( host.getISAppNames().contains(expectedAppName) && host.getISAppNames().contains(project) == false) {
				wd40Host = host.getHost();
				Reporter.log("The Host is  : "+wd40Host ,true);
				break;
			}
		}*/
		Assert.assertNotNull(wd40Host);
		overviewHosts.selectHost(wd40Host);	
		List<BinaryConfLine> confLineEntires = hostPage.getBinaryConfLines();
		
		Reporter.log("Peforming Binary Conf Validations",true);
		for(BinaryConfLine confLine:confLineEntires){
			Reporter.log("The app name is :"+ confLine.getISAppName(),true);
			Reporter.log("The instance is :"+ confLine.getInstance(),true);
			if(confLine.getISAppName().equals(expectedAppName) ) {
				Reporter.log("Binary ConfLine verified successfully",true);
				expectedInstance = confLine.getInstance();
				Reporter.log("The App Instance is :"+expectedInstance,true);
				break;
			}
			
			
		}
		
		List<PSConfLine> psConfLineEntries = hostPage.getCurrentlyRunningConfEntries();
		
		Reporter.log("Peforming PS Conf Validations",true);
		for(PSConfLine confLine:psConfLineEntries){
			Reporter.log("The app name is :"+ confLine.getISAppName(),true);
			Reporter.log("The instance is :"+ confLine.getInstance(),true);
			if(confLine.getISAppName().equals(expectedAppName)&&confLine.getInstance().equals(expectedInstance)){
				processID = confLine.getPID();
				Reporter.log("The process id is :"+processID,true);
				Reporter.log("PS ConfLine verified successfully",true);
				break;
			}
		}
		
		Assert.assertNotNull(processID);
	}
	
	@Test(dependsOnMethods = { "validateConfLineOnWD40Host" },invocationCount = 2)
	public void verifyAppInstanceRestart() throws InterruptedException{
		//Wait till the process is reported to carnival
		sshKillAppInstance();
		Reporter.log("Wait five(5) minutes so that process is restarted by carnival",true);
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
		
		Assert.assertNotNull(processID);
		
	}
	
	String executeRemoteCommand(String hostname, String vm_base, String shellCommand, boolean useKey) {
		if ( StringUtils.isEmpty(vm_base) ) {
			if (StringUtils.isNotEmpty(domain)) {
				hostname = hostname + "." + domain;
			}
			return JcraftUtil.executeShellCommand(hostname, useKey, shellCommand);
		} else {
			String vagrant_base = "~/.vagrant/WD40/vm/centos/";
			String vm_cmd = "cd " + vagrant_base + hostname + " && vagrant ssh -c '" + shellCommand + "'";
			Reporter.log("Executing command: '" + vm_cmd + "' via Jcraft on host: " + vm_base);
			return JcraftUtil.executeShellCommand(vm_base, true, vm_cmd);
		}
	}
	
	private void sshKillAppInstance() throws InterruptedException {
		Reporter.log("The app instance restart",true);
		String expectedOutput = "";
		//The ssh user in the framework.properties should have sudo accesss to kill the process
		String shellCommand = "sudo kill -9 "+processID;
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to kill the app instance on WD40 Host :"+wd40Host,true);

	        commandOutPut = executeRemoteCommand(wd40Host,vmBase, shellCommand, false);

		Reporter.log("The output is : \n" + commandOutPut,true);

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
