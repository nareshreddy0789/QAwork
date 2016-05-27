package com.apple.carnival.api.request;

import static org.testng.Assert.assertTrue;

import org.apache.commons.lang.StringUtils;
import org.json.JSONException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Optional;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

import com.apple.carnival.qa.common.util.JcraftUtil;
import com.apple.carnival.qa.common.util.ValidationUtils;
import com.apple.carnival.qa.coreframework.data.RestTestCaseData;
import com.apple.carnival.qa.coreframework.data.TestCase;
import com.apple.carnival.qa.coreframework.dataproviders.Data;
import com.apple.carnival.qa.coreframework.properties.Configuration;
import com.apple.carnival.qa.coreframework.properties.FrameworkConfigurationReader;
import com.apple.carnival.ui.CarnivalUI;
import com.apple.carnival.ui.CreateRequestPage;
import com.apple.carnival.ui.HomePage;
import com.apple.carnival.ui.HostDetailPage;
import com.apple.carnival.ui.LoginPage;
import com.apple.carnival.ui.OverviewOfHostsPage;
import com.apple.messageclient.MessageClient;
import com.apple.messageclient.MessageExchange;
import com.apple.messageclient.apache.http.RestMessageClient;

/**
 * 
 * @author harisha 
 * This POST request is based on ip authentication, which means,from any machine before this test is run, we need to grab the IP
 * address and let the carnival know the ip adress. To do that, we needto put the following property in carnival.properties (or
 * ci-dev.properties of Carnival) we need to add ip address in carnival.web_service.admin.ip_whitelist
 */
@SuppressWarnings("deprecation")
public class WD40HostClownBuildRequestTests {

	private Configuration config = null; 
	private String carnivalURL = null;
	private String driverType = null;	
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
		driverType = config.getProperty("webdriverType");
		
	}

	@Test
	@Parameters({"carnival.wd40host","carnival.domain","carnival.project","carnival.package"})
	public void init(String wd40Host, @Optional String domain,String project, String packageName){
		this.wd40Host= wd40Host;
		this.domain = domain;
		this.project = project;
		this.packageName = packageName;
		
		Reporter.log("WD40HostClownBuildRequestTests tests with the following details :",true);
		Reporter.log("WD40 HOST :"+wd40Host,true);
		Reporter.log("DOMAIN :"+domain,true);
		Reporter.log("PROEJCT :"+project,true);
		Reporter.log("PACKAGE  :"+packageName,true);

	}

	@Test(dependsOnMethods = { "init" },dataProvider = "restapiTestData", dataProviderClass = com.apple.carnival.qa.coreframework.dataproviders.TestCaseDataProvider.class)
	@Data(dataFile="src/main/resources/data/POSTClownCreateAndScheduleRequestTestCaseData.JSON")
	public void createAndScheduleRequestTest(TestCase test) throws JSONException, InterruptedException{
		//Wait to address throttle issue
		Thread.sleep(7000);
		Reporter.log("Data Provider Create Request Test",true);
		MessageClient client = new RestMessageClient();
		MessageExchange restMessage = client.execute(test);
		RestTestCaseData restRequestAndReponseData = (RestTestCaseData)restMessage.getTestCaseData();

		ValidationUtils.validateTestCase(restRequestAndReponseData);

	}

	@Test(dependsOnMethods = { "createAndScheduleRequestTest" })
	public void carnivalUICheckClownPackageExistsOnWD40Host() throws InterruptedException {
		//Wait for 15 mins to change the status to Current on Environment Info Page
		Thread.sleep(950000);
		CarnivalUI.startDriver(carnivalURL,driverType);
		LoginPage login = new LoginPage();
		login.loginToCarnival(config.getProperty("carnival.ui.user"), config.getProperty("carnival.ui.password"));

		home.getOverViewOfHostsPage();

		overviewHosts.selectHost(wd40Host);

		String installedSoftware = hostPage.getInstalledSoftware();

		Reporter.log("The installed software is :\n :"+ installedSoftware,true);

		assertTrue(installedSoftware.contains(packageName),"Element should have been Present");
		

	}
	
	@Test(dependsOnMethods = { "carnivalUICheckClownPackageExistsOnWD40Host" })
	public void sshCheckClownPackageExistsOnWD40Host() throws InterruptedException {

		String expectedOutput = "\\d+\\s+/opt/ais/local/CarnivalQA/clown/Current/\\s+";
		String shellCommand = "du -sk /opt/ais/local/CarnivalQA/clown/Current/";
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to check if package exists on WD40 host",true);

		// Disk Usage
		if (!StringUtils.isEmpty(domain))
			commandOutPut = JcraftUtil.executeShellCommand(wd40Host + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(wd40Host,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);
		Reporter.log("Connection Successfull. Validating the existence of the folder",true);
		Assert.assertTrue(commandOutPut.matches(expectedOutput),"Expected Out put is not same as Actual Out put");
	}


	@Test(dependsOnMethods = { "sshCheckClownPackageExistsOnWD40Host" })
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
	}

	@Test(dependsOnMethods = { "uninstallClownPackageUsingCreateRequestWorkFlowWithForce" })
	public void sshCheckClownPackageDoesNotExistsOnWD40Host() throws InterruptedException {
		//Wait till the package is removed on the WD40Host
		Thread.sleep(20000);
		String expectedOutput = "";
		String shellCommand = "du -sk /opt/ais/local/CarnivalQA/clown/Current/";
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

	@Test(dependsOnMethods = { "sshCheckClownPackageDoesNotExistsOnWD40Host" })
	public void carnivalUICheckClownPackageDoesNotExistsOnWD40Host() throws InterruptedException {
		//Wait for 15 mins to change the status to Current on Environment Info Page
		Thread.sleep(950000);
		
		home.getOverViewOfHostsPage();
		overviewHosts.selectHost(wd40Host);

		String installedSoftware = hostPage.getInstalledSoftware();
		Reporter.log("The installed software is :\n :"+ installedSoftware,true);	
		Assert.assertFalse(installedSoftware.contains(packageName),"Element should not have been Present");

	}


	@AfterClass
	public void cleanUp() throws InterruptedException {
		//Wait before logging out and quitting the browser
		Thread.sleep(2000);
		Reporter.log("\nLogging out and closing  browser session\n",true);
		//home.logout();
		CarnivalUI.stopDriver();

	}

}