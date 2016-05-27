package com.apple.carnival.filer.tests;

import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;

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
import com.apple.carnival.ui.InstallBuildsPage;
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
public class FilerInstallTests {

	private Configuration config = null; 
	private String carnivalURL = null;
	private String domain = null;
	private String build = "Jingle15A30";
	private String carnivalHost = null;
	
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
	//@Parameters({"carnival.domain","carnival.host",carnival.build"})
	//public void init(String domain,String carnivalHost,String build){
	public void init(){
		//this.build = build;
/*		this.domain = domain;
		this.carnivalHost = carnivalHost;
		this.build = build;*/

		Reporter.log("PACKAGE  :"+build,true);

	}
	
	@Test(dependsOnMethods = { "init" })
	public void installPackage() throws InterruptedException {
		Reporter.log("Install the build :"+build+" from carnival UI",true);
		home.getInstallBuildPage();
		InstallBuildsPage filerInstall = new InstallBuildsPage(config);
		String actualMessage = filerInstall.installBuild(build);
		Reporter.log("Actual message is :"+actualMessage,true);
		Assert.assertTrue(actualMessage.contains("all done"));
		
		
	}

	@Test(dependsOnMethods = { "installPackage" })
	public void sshCheckClownPackageDoesNotExistsOnCarnivalHost() throws InterruptedException {
		//Wait till the package is removed on the WD40Host
		Thread.sleep(20000);
		String expectedOutput = "";
		String shellCommand = "du -sk /opt/ais/local/CarnivalQA/clown/Current/";
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

	@AfterClass
	public void cleanUp() throws InterruptedException {
		//Wait 2 seconds before quitting the browser
		Thread.sleep(2000);
		Reporter.log("\nLogging out and closing  browser session\n",true);
		//home.logout();
		CarnivalUI.stopDriver();

	}

}