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

import com.apple.carnival.qa.common.util.DateUtil;
import com.apple.carnival.qa.common.util.JcraftUtil;
import com.apple.carnival.qa.coreframework.dataproviders.Data;
import com.apple.carnival.qa.coreframework.properties.Configuration;
import com.apple.carnival.qa.coreframework.properties.FrameworkConfigurationReader;
import com.apple.carnival.qa.parser.JsonParserUtil;
import com.apple.carnival.ui.SeleniumUtility;
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
public class CopyOfWD40HostClownBuildRequestTests {

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
		
		config = new FrameworkConfigurationReader("framework.properties");
		carnivalURL = config.getProperty("carnival.url");
		driverType = config.getProperty("webdriverType");
	
	}


	@Test(dataProvider = "JSONDataForCreateRequest", dataProviderClass = com.apple.carnival.qa.coreframework.dataproviders.JSONDataProvider.class)
	@Data(dataFile="src/main/resources/data/ClownWD40HostInstall.JSON")
	public void testClownWD40InstallCreatePostRequest(JSONObject jsonRequestObject) throws InterruptedException, JsonIOException, ClientProtocolException, JSONException, IOException  {

		String requestLocalInstallResource = config.getProperty("request.localinstall.resource");
		
		String jsonPostData = "src/main/resources/data/ClownWD40HostInstall.JSON";
		
		String expectedEntity = "^\\{\"status\":\"SUCCESS\",\"code\":\"201 Created\",\"description\":\""+ carnivalURL + "/TaskDetail.jsp\\?id=\\d{13}\"}$";
		int expectedStatusCode = 200;
	
		String scheduleDate = DateUtil.getDate("yyyy/MM/dd h:mm a");
		jsonRequestObject.put("scheduledDate", scheduleDate);
		String requestEntityString = jsonRequestObject.toString();
		Reporter.log("JSON POST REQUEST MEESSAGE  :\n" + requestEntityString);	
		Reporter.log("Sending POST Create Request To : " + requestLocalInstallResource);
		Thread.sleep(7000);
		RestMessageClient restClient = new RestMessageClient(requestLocalInstallResource);
		restClient.executePostAsJSON(requestEntityString);
		
		String reponseEntity = restClient.getReponseEntityAsString().trim();
		int responseStatusCode = restClient.getResponseStatusCode();

		requestID = restClient.getRequestIDFromReponseEntity();

		// TestNG Report logger
		Reporter.log("Output from Server .... \n");
		Reporter.log("Response Entity :" + reponseEntity);
		Reporter.log("Response Status Code :" + responseStatusCode);
		Reporter.log("The Request ID is :" + requestID);

		Reporter.log("Performing Assertions");
		Reporter.log("Expected status code is : " + expectedStatusCode+ " and got  status code from server as  : " + responseStatusCode);
		Reporter.log("Expected entity is : " + expectedEntity + " and got entity from server as  : " + reponseEntity);
		assertEquals(responseStatusCode, expectedStatusCode);
		assertTrue(reponseEntity.matches(expectedEntity),"Expected Entity and Actual Entity Do not Match");
		

	}

	@Test(dependsOnMethods = { "testClownWD40InstallCreatePostRequest" },dataProvider = "JSONDataForScheduleRequest", dataProviderClass = com.apple.carnival.qa.coreframework.dataproviders.JSONDataProvider.class)
	public void testClownWD40InstallSchedulePostRequest(JSONObject jsonRequestObject) throws JsonIOException, ClientProtocolException, IOException,InterruptedException, JSONException {
		String scheduleLocalInstallResource = config.getProperty("schedule.localinstall.resource");
		int expectedStatusCode = 200;
		String expectedEntity = "{\"status\":\"200-OK\",\"code\":\"SUCCESS\",\"description\":null,\"request\":{\"status\":\"QUEUED\"}}";
		
		jsonRequestObject.put("requestID", requestID);
		
		String requestEntityString = jsonRequestObject.toString();
		
		Reporter.log("JSON POST MEESSAGE  :\n" + requestEntityString);
		Reporter.log("Sending POST Schedule Request To : " + scheduleLocalInstallResource);

		Thread.sleep(7000);
		
		RestMessageClient restClient = new RestMessageClient(scheduleLocalInstallResource);
		restClient.executePostAsJSON(requestEntityString);
		
		String reponseEntity = restClient.getReponseEntityAsString().trim();
		int responseStatusCode = restClient.getResponseStatusCode();

		// TestNG Report logger
		Reporter.log("Output from Server .... \n");
		Reporter.log("Response Entity :" + reponseEntity);
		Reporter.log("Response Status Code :" + responseStatusCode);

		Reporter.log("Performing Assertions");
		Reporter.log("Expected status code is : " + expectedStatusCode + " and got  status code from server as  : " + responseStatusCode);
		Reporter.log("Expected entity is : " + expectedEntity + " and got entity from server as  : " + reponseEntity);

		// Validations
		assertEquals(responseStatusCode, expectedStatusCode);
		assertTrue(reponseEntity.equals(expectedEntity),"Actual Notice Reponse did not Match with Expected Notice");

		Thread.sleep(60000);

	}

	@Test(dependsOnMethods = { "testClownWD40InstallSchedulePostRequest" })
	public void sshCheckClownPackageExistsOnWD40Host() {
		String expectedOutput = "\\d+\\s+/opt/ais/local/CarnivalQA/clown/Current/\\s+";
		String shellCommand = "du -sk /opt/ais/local/CarnivalQA/clown/Current/";
		String commandOutPut = null;
		
		Reporter.log("Connecting to remote host using Jcraft Libraries to check if package exists on WD40 host");
	
		// Disk Usage
		if (domain != null || domain != "" || !domain.isEmpty())
			commandOutPut = JcraftUtil.executeShellCommand(hostToUninstall + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(hostToUninstall,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut);
		Reporter.log("Connection Successfull. Validating the existing of the folder");
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
		
		Reporter.log("The installed software is :\n :"+ installedSoftware);
		Thread.sleep(4000);
		assertTrue(installedSoftware.contains(packageToUninstall),"Element should have been Present");

	}

	@Test(dependsOnMethods = { "carnivalUICheckClownPackageExistsOnWD40Host" })
	public void uninstallClownPackageFromUI() throws InterruptedException {

		Reporter.log("Uninstalling the package from carnival UI");
		String expectedNotice = "User \\d{2}.\\d{2,3}.\\d{2,3}.\\d{2,3} requested uninstall of " + packageToUninstall + " on host " + hostToUninstall + ".";

		carnivalWebDriver.findElement(maintenanceLink).click();
		carnivalWebDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

		Select selectPackage = new Select(carnivalWebDriver.findElement(uninstallPackageSelectBox));
		selectPackage.selectByVisibleText(packageToUninstall);

		Reporter.log("THe selected application is :" + selectPackage.getFirstSelectedOption().getText());

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

		Reporter.log("Uninstall Successfull");
		Reporter.log("Performing Assertions");
		Reporter.log("Expected note is : " + expectedNotice + " and got  note from server as  : " + actualNotice);

		assertTrue(actualNotice.matches(expectedNotice),"Expected and Actual Do not Match");

		Thread.sleep(20000);
	}

	@Test(dependsOnMethods = { "uninstallClownPackageFromUI" })
	public void sshCheckClownPackageDoesNotExistsOnWD40Host() {
		String expectedOutput = "";
		String shellCommand = "du -sk /opt/ais/local/CarnivalQA/clown/Current/";
		String commandOutPut = null;

		Reporter.log("Connecting to remote host using Jcraft Libraries to validate package does not exists on WD40 host");

		// Disk Usage
		if (domain != null || domain != "" || !domain.isEmpty())
			commandOutPut = JcraftUtil.executeShellCommand(hostToUninstall + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(hostToUninstall,false, shellCommand);

		Reporter.log("The out put is : \n" + commandOutPut);

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
		Reporter.log("The installed software is :\n :"+ installedSoftware);
		carnivalWebDriver.findElement(logoutLink).click();
		Assert.assertFalse(installedSoftware.contains(packageToUninstall),"Element should not have been Present");
		Thread.sleep(2000);
	}

	private void loginToCarnival() {
		carnivalWebDriver = SeleniumUtility.getWebDriver(driverType);
		carnivalWebDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		carnivalWebDriver.get(carnivalURL);
		carnivalWebDriver.findElement(userField).sendKeys("admin");
		carnivalWebDriver.findElement(passwordField).sendKeys("admin_pass");
		carnivalWebDriver.findElement(submitButton).click();
		carnivalWebDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
	}

	@AfterClass
	public void cleanUp() throws InterruptedException {
		Reporter.log("\nQutting  Browser Session\n");
	
		carnivalWebDriver.quit();

	}

}