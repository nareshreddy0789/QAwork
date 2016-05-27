package com.apple.carnival.api.request;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONException;
import org.json.JSONObject;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
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
import com.apple.carnival.qa.parser.JsonParserUtil;
import com.apple.carnival.ui.SeleniumUtility;
import com.apple.carnival.utils.TestStringUtils;
import com.apple.messageclient.MessageClient;
import com.apple.messageclient.MessageExchange;
import com.apple.messageclient.apache.http.RestMessageClient;
import com.google.gson.JsonIOException;

/**
 * 
 * @author harisha 
 * This POST request is based on ip authentication, which means,from any machine before this test is run, we need to grab the IP
 * address and let the carnival know the ip adress. To do that, we needto put the following property in carnival.properties (or
 * ci-dev.properties of Carnival) we need to add ip address in carnival.web_service.admin.ip_whitelist
 */
@SuppressWarnings("deprecation")
public class Copy_2_of_WD40HostClownBuildRequestTests {

	private Configuration config = null; 
	private String requestID = null;
	private WebDriver carnivalWebDriver = null;
	private String carnivalURL = null;
	private String packageToUninstall = "clown14A148";
	private String hostToUninstall = "vp21q01if-ztbu14114701";
	private String domain = "vp.if1.apple.com";

	private By userField = By.name("username");
	private By passwordField = By.name("password");
	private By submitButton = By.xpath("//input[@value='Login']");
	private By maintenanceLink = By.linkText("Local Installs Maintenance");
	private By uninstallPackageSelectBox = By.name("selectedUninstallPackage");
	private By uninstallFromHostSelectBox = By.name("selectedHost");
	private By uninstallButton = By.xpath("//input[@value='Uninstall']");
	private By noticeMessageText = By.id("notice");

	private By overViewOfHosts = By.xpath("//ul[contains(@id,'monitoringNav')]/li[2]/a");
	private By wd40HostLink = By.linkText(hostToUninstall);
	private By showInstalledSoftwareLink = By.linkText("Show Installed Software");
	private By installedSoftwareDiv = By.id("installedSoftwareDisplayDivContent");

	private By logoutLink = By.linkText("Logout");
	private String driverType = null;

	@BeforeClass
	public void init() {
		Reporter.log("Initializing and Loading Configuration",true);
		config = new FrameworkConfigurationReader("framework.properties");
		driverType = config.getProperty("webdriverType");
		carnivalURL = config.getProperty("carnival.url");
	}


	@Test(dataProvider = "restapiTestData", dataProviderClass = com.apple.carnival.qa.coreframework.dataproviders.TestCaseDataProvider.class)
	@Data(dataFile="src/main/resources/data/POSTClownCreateRequestTestCaseData.JSON")
	public void testClownWD40InstallCreatePostRequest(TestCase test) throws JSONException, InterruptedException{
		Thread.sleep(7000);
		Reporter.log("Create Request Test",true);

		MessageClient client = new RestMessageClient();
		MessageExchange restMessage = client.execute(test);

		RestTestCaseData restData = (RestTestCaseData) restMessage.getTestCaseData();

		JsonParserUtil parseJson = new JsonParserUtil(restData.getJsonTestCaseEntityResponseData().toString());
		String descritpion = parseJson.getNodeValue("description");
		requestID = TestStringUtils.parseStringToGetIdTokenValue(descritpion);

		Reporter.log("The request ID to Schedule Post Request is: "+requestID,true);
		Reporter.log("Peform Validations ",true);
		ValidationUtils.validateTestCase(restMessage.getTestCaseData());

	}


	@Test(dependsOnMethods = { "testClownWD40InstallCreatePostRequest" },dataProvider = "restapiTestData", dataProviderClass = com.apple.carnival.qa.coreframework.dataproviders.TestCaseDataProvider.class)
	@Data(dataFile="src/main/resources/data/POSTClownScheduleRequestTestCaseData.JSON")
	public void testClownWD40InstallSchedulePostRequest(TestCase test) throws JSONException, InterruptedException{
		Reporter.log("Schedule Request Test",true);

		RestTestCaseData restData = (RestTestCaseData)test.getTestCaseData();

		JSONObject entity = (JSONObject) restData.getJsonTestCaseInput().get("entity");
		entity.put("requestID", requestID);

		restData.setJsonTestCaseEntityReponseData(entity);
		Thread.sleep(5000);
		MessageClient client = new RestMessageClient();
		MessageExchange restMessage = client.execute(test);

		ValidationUtils.validateTestCase(restMessage.getTestCaseData());
		Thread.sleep(20000);

	}

	@Test(dependsOnMethods = { "testClownWD40InstallSchedulePostRequest" })
	public void sshCheckClownPackageExistsOnWD40Host() {
		String expectedOutput = "\\d+\\s+/opt/ais/local/CarnivalQA/clown/Current/\\s+";
		String shellCommand = "du -sk /opt/ais/local/CarnivalQA/clown/Current/";
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to check if package exists on WD40 host",true);

		// Disk Usage
		if (domain != null || domain != "" || !domain.isEmpty())
			commandOutPut = JcraftUtil.executeShellCommand(hostToUninstall + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(hostToUninstall,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);
		Reporter.log("Connection Successfull. Validating the existing of the folder",true);
		Assert.assertTrue(commandOutPut.matches(expectedOutput),"Expected Out put is not same as Actual Out put");
	}

	@Test(dependsOnMethods = { "sshCheckClownPackageExistsOnWD40Host" })
	public void carnivalUICheckClownPackageExistsOnWD40Host() throws InterruptedException {
		loginToCarnival();
		carnivalWebDriver.findElement(overViewOfHosts).click();
		carnivalWebDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		carnivalWebDriver.findElement(wd40HostLink).click();
		carnivalWebDriver.findElement(showInstalledSoftwareLink).click();
		carnivalWebDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(carnivalWebDriver, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated(installedSoftwareDiv));

		Thread.sleep(4000);
		String installedSoftware = carnivalWebDriver.findElement(installedSoftwareDiv).getText();

		Reporter.log("The installed software is :\n :"+ installedSoftware,true);
		Thread.sleep(4000);
		assertTrue(installedSoftware.contains(packageToUninstall),"Element should have been Present");

	}

	@Test(dependsOnMethods = { "carnivalUICheckClownPackageExistsOnWD40Host" })
	public void uninstallClownPackageFromUI() throws InterruptedException {

		Reporter.log("Uninstalling the package from carnival UI",true);
		String expectedNotice = "User \\d{2}.\\d{2,3}.\\d{2,3}.\\d{2,3} requested uninstall of " + packageToUninstall + " on host " + hostToUninstall + ".";

		carnivalWebDriver.findElement(maintenanceLink).click();
		carnivalWebDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

		Select selectPackage = new Select(carnivalWebDriver.findElement(uninstallPackageSelectBox));
		selectPackage.selectByVisibleText(packageToUninstall);

		Reporter.log("THe selected application is :" + selectPackage.getFirstSelectedOption().getText(),true);

		Select selectHost = new Select(carnivalWebDriver.findElement(uninstallFromHostSelectBox));

		List<WebElement> selectItems = selectHost.getOptions();
		Reporter.log("Checking the Host List");
		for (WebElement selectItem : selectItems) {

			if (selectItem.getText().contains(hostToUninstall)) {
				Reporter.log("The host is :" + selectItem.getText());
				selectHost.selectByVisibleText(selectItem.getText());
				break;
			}

		}

		carnivalWebDriver.findElement(uninstallButton).click();
		carnivalWebDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

		String actualNotice = carnivalWebDriver.findElement(noticeMessageText).getText();

		Reporter.log("Uninstall Successfull",true);
		Reporter.log("Performing Assertions",true);
		Reporter.log("Expected note is : " + expectedNotice + " and got  note from server as  : " + actualNotice,true);

		assertTrue(actualNotice.matches(expectedNotice),"Expected and Actual Do not Match");

		Thread.sleep(20000);
	}

	@Test(dependsOnMethods = { "uninstallClownPackageFromUI" })
	public void sshCheckClownPackageDoesNotExistsOnWD40Host() {
		String expectedOutput = "";
		String shellCommand = "du -sk /opt/ais/local/CarnivalQA/clown/Current/";
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to validate package does not exists on WD40 host",true);

		// Disk Usage
		if (domain != null || domain != "" || !domain.isEmpty())
			commandOutPut = JcraftUtil.executeShellCommand(hostToUninstall + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(hostToUninstall,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut,true);

		Assert.assertTrue(commandOutPut.matches(expectedOutput),"Expected Out put is not same as Actual Out put");
	}

	@Test(dependsOnMethods = { "sshCheckClownPackageDoesNotExistsOnWD40Host" })
	public void carnivalUICheckClownPackageDoesNotExistsOnWD40Host() throws InterruptedException {
		carnivalWebDriver.findElement(overViewOfHosts).click();
		carnivalWebDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		carnivalWebDriver.findElement(wd40HostLink).click();
		carnivalWebDriver.findElement(showInstalledSoftwareLink).click();
		carnivalWebDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		WebDriverWait wait = new WebDriverWait(carnivalWebDriver, 10);
		wait.until(ExpectedConditions.presenceOfElementLocated(installedSoftwareDiv));
		Thread.sleep(7000);
		String installedSoftware = carnivalWebDriver.findElement(installedSoftwareDiv).getText();
		Reporter.log("The installed software is :\n :"+ installedSoftware,true);
		carnivalWebDriver.findElement(logoutLink).click();
		Assert.assertFalse(installedSoftware.contains(packageToUninstall),"Element should not have been Present");
		Thread.sleep(2000);
	}

	private void loginToCarnival() {
		Reporter.log("Initialize Web Driver",true);
		carnivalWebDriver = SeleniumUtility.getWebDriver(driverType);
		carnivalWebDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Reporter.log("Launch and Login to Carnival",true);
		carnivalWebDriver.get(carnivalURL);
		carnivalWebDriver.findElement(userField).sendKeys("admin");
		carnivalWebDriver.findElement(passwordField).sendKeys("admin_pass");
		carnivalWebDriver.findElement(submitButton).click();
		carnivalWebDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
	}

	@AfterClass
	public void cleanUp() throws InterruptedException {
		Reporter.log("\nQutting  Browser Session\n",true);

		carnivalWebDriver.quit();

	}

}