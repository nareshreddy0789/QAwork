package com.apple.carnival.api.request;

import static org.testng.Assert.assertTrue;

import org.json.JSONException;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
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
public class CottonCandyPackageTests {

	private Configuration config = null; 
	//private String requestID = null;
	private String carnivalURL = null;
	private String project="CarnivalQA";
	private String packageToUninstall = "cottoncandyCONF14A146";
	private String hostToUninstall = "vp21q01if-ztbu14114701";
	private String domain = "vp.if1.apple.com";

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

	}


	@Test(dataProvider = "restapiTestData", dataProviderClass = com.apple.carnival.qa.coreframework.dataproviders.TestCaseDataProvider.class)
	@Data(dataFile="src/main/resources/data/POSTCottonCandyCreateAndScheduleRequestTestCaseData.JSON")
	public void createAndScheduleRequestTest(TestCase test) throws JSONException, InterruptedException{
		Thread.sleep(7000);
		System.out.print("Data Provider Create Request Test");
		MessageClient client = new RestMessageClient();
		MessageExchange restMessage = client.execute(test);
		//RestTestCaseData restRequestAndReponseData = (RestTestCaseData)test.getTestCaseData();
		RestTestCaseData restRequestAndReponseData = (RestTestCaseData)restMessage.getTestCaseData();

		ValidationUtils.validateTestCase(restRequestAndReponseData);

	}


	@Test(dependsOnMethods = { "createAndScheduleRequestTest" })
	public void carnivalUICheckClownPackageExistsOnWD40Host() throws InterruptedException {
		Thread.sleep(40000);
		LoginPage login = new LoginPage();
		login.loginToCarnival(config.getProperty("carnival.ui.user"), config.getProperty("carnival.ui.password"));
		
		home.getOverViewOfHostsPage();
		
		overviewHosts.selectHost(hostToUninstall);
		
		String installedSoftware = hostPage.getInstalledSoftware();
		
		Reporter.log("The installed software is :\n :"+ installedSoftware,true);
		
		assertTrue(installedSoftware.contains(packageToUninstall),"Element should have been Present");

	}

	@Test(dependsOnMethods = { "carnivalUICheckClownPackageExistsOnWD40Host" })
	public void uninstallPackageFromWD40IfNotCurrentOnCarnival() throws InterruptedException {

		Reporter.log("Uninstalling the package from carnival UI",true);
		//String expectedNotice = "User \\d{2}.\\d{2,3}.\\d{2,3}.\\d{2,3} requested uninstall of " + packageToUninstall + " on host " + hostToUninstall + ".";
		String expectedNotice = "Successfully scheduled task \\d{13} for execution!";

		home.getCreateRequestPage();
		CreateRequestPage createRequest = new CreateRequestPage();

		String actualNotice = createRequest.addRequestAndScheduleToUninstallPackage("100.0",project, packageToUninstall,false,3,hostToUninstall);

		Reporter.log("Uninstall Successfull",true);
		Reporter.log("Performing Assertions",true);
		Reporter.log("Expected note is : " + expectedNotice + " and got  note from server as  : " + actualNotice,true);
		
		assertTrue(actualNotice.matches(expectedNotice),"Expected and Actual Do not Match");
	}


	@Test(dependsOnMethods = { "uninstallPackageFromWD40IfNotCurrentOnCarnival" })
	public void carnivalUICheckClownPackageDoesNotExistsOnWD40Host() throws InterruptedException {
		home.getOverViewOfHostsPage();
		overviewHosts.selectHost(hostToUninstall);
	
		String installedSoftware = hostPage.getInstalledSoftware();
		Reporter.log("The installed software is :\n :"+ installedSoftware,true);	
		Assert.assertFalse(installedSoftware.contains(packageToUninstall),"Element should not have been Present");
		
	}


	@AfterClass
	public void cleanUp() throws InterruptedException {
		Thread.sleep(2000);
		Reporter.log("\nLogging out and closing  browser session\n",true);
		//home.logout();
		CarnivalUI.stopDriver();

	}

}