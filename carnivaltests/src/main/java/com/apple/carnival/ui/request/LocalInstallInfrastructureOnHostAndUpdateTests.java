/**
 * 
 */
package com.apple.carnival.ui.request;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Platform;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import com.apple.carnival.qa.common.util.DateUtil;
import com.apple.carnival.qa.coreframework.properties.Configuration;
import com.apple.carnival.qa.coreframework.properties.FrameworkConfigurationReader;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;

/**
 * @author harisha
 *
 */
public class LocalInstallInfrastructureOnHostAndUpdateTests {
	
	private WebDriver carnivalDriver=null;
	private By userField = By.name("username");
	private By passwordField = By.name("password");
	private By submitButton = By.xpath("//input[@value='Login']");

	private By createRequestLink = By.linkText("Create a Request");
	private By chooseActionSelectBox = By.id("actionSelect");
	private By maintenanceLink = By.linkText("Local Installs Maintenance");
	private By uninstallPackageSelectBox = By.name("selectedUninstallPackage");
	//private By installPackageField = By.name("projectInstallPackage"); //For HtmlUnit
	private By installPackageField = By.xpath("//table[@id='TABLE_21']/tbody/tr/td/textarea[@name='projectInstallPackage']"); //For FireFox
	private By regExHostRadioButton = By.id("carnivalOnlyInstallRegEx");
	private By regExHostTextField = By.id("hostRegex");
	private By addRequestButton = By.id("addLocalInstallsInfraToRequest");
	
	private By scheduleForSelectBox = By.id("taskScheduledTimeSelect");

	private By taskScheduledTimeField = By.xpath("//input[contains(@id,'addLocalInstallsInfraToRequest')]");
	//private By taskNotesField = By.id("//input[@id='taskNotesField']");
	private By taskNotesField = By.xpath("//table[@id='TABLE_26']/tbody/tr[4]/td[2]/textarea");

	//private By scheduleNowButton = By.id("requestAndScheduleTaskId");
	private By scheduleNowButton = By.xpath("//button[contains(@id,'requestAndScheduleTaskId')]");
	private By noticeMessage = By.id("notice");
	private String packageName = "Ant/apache-ant-1.9.4-bin";
	private String hostName = "vp21q01if-ztbu14114601";
	
	@BeforeClass
	public void launchAndLoginToCarnival(){

		DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
		capabilities.setCapability("phantomjs.binary.path", "src/main/resources/phantomjs-1.9.7-macosx/bin/phantomjs");
		capabilities.setJavascriptEnabled(true);
		capabilities.setPlatform(Platform.ANY);
		carnivalDriver = new PhantomJSDriver(capabilities);
		//carnivalDriver = new FirefoxDriver();
		//carnivalDriver = new HtmlUnitDriver();
		//((HtmlUnitDriver)carnivalDriver).setJavascriptEnabled(true);

		carnivalDriver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
		Configuration config = new FrameworkConfigurationReader("framework.properties");
		String carnivalURL = config.getProperty("carnival.url");
		
		carnivalDriver.get(carnivalURL);
	
		carnivalDriver.findElement(userField).sendKeys("admin");
		carnivalDriver.findElement(passwordField).sendKeys("admin_pass");
		carnivalDriver.findElement(submitButton).click();
		carnivalDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);

	}
	
	@Test
	public void installAntOnAHostUsingCreateRequest() throws InterruptedException, JsonIOException, JsonSyntaxException, ClientProtocolException, IOException{
		String expectedResponseMessage ="\\Successfully scheduled task \\d{13} for execution!";
		carnivalDriver.findElement(createRequestLink).click();
		carnivalDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		
		Select select = new Select(carnivalDriver.findElement(chooseActionSelectBox));
		select.selectByValue("LocalInstallsInfrastructureActionDiv");
		carnivalDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);		
		
		carnivalDriver.findElement(installPackageField).sendKeys(packageName);
		carnivalDriver.findElement(regExHostRadioButton).click();
		carnivalDriver.findElement(regExHostTextField).sendKeys(hostName);
		carnivalDriver.findElement(addRequestButton).click();
		carnivalDriver.manage().timeouts().pageLoadTimeout(60, TimeUnit.SECONDS);
		
		//System.out.println("The java script is :\n" +doFormSubmitPost());
		//JavascriptExecutor executor = (JavascriptExecutor)carnivalDriver;
		// executor.executeScript(doFormSubmitPost());
		//apacheHttpPost();
		carnivalDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);	

		//carnivalDriver.navigate().refresh();

		WebDriverWait wait = new WebDriverWait(carnivalDriver, 20);
		wait.until(ExpectedConditions.presenceOfElementLocated(scheduleForSelectBox));
		Select scheduleFor = new Select(carnivalDriver.findElement(scheduleForSelectBox));	
		scheduleFor.selectByVisibleText("ASAP");
		wait.until(ExpectedConditions.presenceOfElementLocated(taskScheduledTimeField));
		String scheduleDate = DateUtil.getDate("yyyy/MM/dd h:mm a");
		carnivalDriver.findElement(taskScheduledTimeField).sendKeys(scheduleDate);
		String notes = "CI Sending request on :" + scheduleDate;
		System.out.println(notes);
		wait.until(ExpectedConditions.presenceOfElementLocated(taskNotesField));
		carnivalDriver.findElement(taskNotesField).sendKeys(notes);
		carnivalDriver.findElement(scheduleNowButton).click();
		carnivalDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		String actualResponseMessage = carnivalDriver.findElement(noticeMessage).getText();
		System.out.println("The reponse of request submission is :"+ actualResponseMessage);
		Assert.assertTrue(actualResponseMessage.matches(expectedResponseMessage),"Actual Notice Reponse did not Match with Expected Notice");
		
		
	}
	//@Test(dependsOnMethods={"installOpenMQ"})
	public void validateOpenMQInstallationOnTheFileSystem(){
		
		
	}
//	@Test(dependsOnMethods={"validateOpenMQInstallationOnTheFileSystem"})
//	@Test
	public void unInstallOpenMQFromCarnivalUI(){
		Configuration config = new FrameworkConfigurationReader("framework.properties");
		String carnivalURL = config.getProperty("carnival.url");
		
		carnivalDriver.get(carnivalURL);
	
		carnivalDriver.findElement(userField).sendKeys("admin");
		carnivalDriver.findElement(passwordField).sendKeys("admin_pass");
		carnivalDriver.findElement(submitButton).click();
		carnivalDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		carnivalDriver.findElement(maintenanceLink).click();
		carnivalDriver.manage().timeouts().pageLoadTimeout(10, TimeUnit.SECONDS);
		
		Select select = new Select(carnivalDriver.findElement(uninstallPackageSelectBox));
		select.selectByVisibleText("cottoncandyCONF14A146");
		select.getFirstSelectedOption().getText();
		List<WebElement> selectItems= select.getOptions();
		
		for(WebElement selectItem:selectItems){
			Reporter.log("The Package is :"+ selectItem.getText());
			System.out.println("The Package is :"+ selectItem.getText());
		}
	}
//	@Test(dependsOnMethods={"unInstallOpenMQ"})
	public void validateOpenMQUnInstallationOnTheFileSystem(){
		
	}
	
	@AfterClass
	public void cleanUp(){
		//carnivalDriver.close();
		carnivalDriver.quit();
	}
	
	private String doFormSubmitPost(){
		
		String scheduleDate = DateUtil.getDate("yyyy/MM/dd h:mm a");
		
		String javaScriptText = "var form = document.createElement(\"form\");"
							+ "form.setAttribute(\"method\", \"post\");"
							+ "form.setAttribute(\"action\", \"/Carnival/WorkflowActionHandler\");"
							+ " var hiddenField1 = document.createElement(\"input\");"
							+ " hiddenField1.setAttribute(\"type\", \"hidden\");"
							+ " hiddenField1.setAttribute(\"name\", \"WorkflowActionType\");"
							+ " hiddenField1.setAttribute(\"value\", \"RequestAndScheduleTask\");"
							+ " var hiddenField2 = document.createElement(\"input\");"
							+ " hiddenField2.setAttribute(\"type\", \"hidden\");"
							+ " hiddenField2.setAttribute(\"name\", \"taskScheduledTime\");"
							+ " hiddenField2.setAttribute(\"value\","+ "\""+scheduleDate+ "\"" +");"
							+ " var hiddenField3 = document.createElement(\"input\");"
							+ " hiddenField3.setAttribute(\"type\", \"hidden\");"
							+ " hiddenField3.setAttribute(\"name\", \"taskNotes\");"
							+ " hiddenField3.setAttribute(\"value\", \"CI Notes\");"
							+ "form.appendChild(hiddenField1);"
							+ "form.appendChild(hiddenField2);"
							+ "form.appendChild(hiddenField3);"
							+ "document.body.appendChild(form);"
							+ "form.submit();";
		
		
		return javaScriptText;
							
	}
	
	private void apacheHttpPost() throws ClientProtocolException, IOException, ParseException{
		HttpClient client = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost("http://vp21q01if-ztbu14114601.vp.if1.apple.com:8080/Carnival/WorkflowActionHandler");
		System.out.println("The resource is : http://vp21q01if-ztbu14114601.vp.if1.apple.com:8080/Carnival/WorkflowActionHandler");
		//postRequest.setHeader("Content-Type", "text/html");
		postRequest.getParams().setParameter("WorkflowActionType","RequestAndScheduleTask");
		postRequest.getParams().setParameter("taskScheduledTime","2014/11/01 8:30 PM PDT");
		postRequest.getParams().setParameter("taskNotes","Apache CI Notes");
		
		HttpResponse response = client.execute(postRequest);
	
		HttpEntity entity = response.getEntity();
		String actualEntity = EntityUtils.toString(entity);
		int actualStatusCode = response.getStatusLine().getStatusCode();
		
		//For Local Console Logging
		System.out.println("Output from Server .... ");
		System.out.println("Entity:\n"+actualEntity);
		System.out.println("Status Code:"+actualStatusCode);
		System.out.println("\n");
	}
	

}
