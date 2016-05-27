package com.apple.carnival.ui;

import org.json.JSONObject;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;

import com.apple.carnival.qa.parser.JsonParserUtil;
import com.apple.carnival.ui.utilities.ElementLocatorUtility;

public class CreateRequestPage extends CarnivalUI {

	private String jsonLocatorFile = "src/main/resources/locators/CreateRequestPage.JSON";
	private static JSONObject root=null;

	public CreateRequestPage(){
		if(root == null)
			root = JsonParserUtil.parseJsonFile(jsonLocatorFile);
	}

	/**
	 * This method takes different parameters to uninstall package(S)
	 * @param threshold
	 * @param project
	 * @param packages
	 * @param currentOnCarnival
	 * @param hostType
	 * @param regExHosts
	 * @throws InterruptedException 
	 */
	public String addRequestAndScheduleToUninstallPackage(String threshold,String project,String packages,boolean currentOnCarnival,int hostType, String regExHosts) throws InterruptedException{

		Select chooseAction =  new Select(carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "ActionSelectBox")));
		chooseAction.selectByValue("UninstallPackageActionDiv");

		Thread.sleep(2000);

		if(threshold!=null){
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "ThresholdField")).clear();
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "ThresholdField")).sendKeys(threshold);
		}

		Select chooseProject =  new Select(carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "ProjectSelectBox")));
		chooseProject.selectByVisibleText(project);

		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"PackageToUninstallTextArea")).sendKeys(packages);
		Thread.sleep(2000);
		
		if(currentOnCarnival)
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"IfCurrentRadioButton")).click();
		Thread.sleep(2000);
		
		if(hostType==1){
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"CarnivalHostsRadioButton")).click();
		}
		
		if(hostType==2){
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"CarnivalAndWD40HostsRadioButton")).click();
		}
		
		if(hostType==3){
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"RegexHostsRadioButton")).click();
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"RegexHostField")).sendKeys(regExHosts);
		}
		
		Thread.sleep(2000);

		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"AddToRequestButton")).click();
		
		Thread.sleep(4000);
		
		Boolean notPresent = true;
		try{	
			notPresent = ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(ElementLocatorUtility.getWebDriverLocator(root,"ErrorMessageLabel"))).apply(carnivalWebDriver);
		}catch(NoSuchElementException elementException){
			
		}
		
		if(!notPresent)
			return carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"ErrorMessageLabel")).getText();
		
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "UninstallRequestNotesTextField")).sendKeys("uninstall package :"+packages+" from host :"+regExHosts);
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"ScheduleNowButtonLocator")).click();
		
		Thread.sleep(4000);


		return carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"NoticeMessageText")).getText();
	}

	/**
	 * This method takes different parameters to install package(S)
	 * @param threshold
	 * @param project
	 * @param packages
	 * @param forceInstall
	 * @param hostType
	 * @throws InterruptedException 
	 */
	public String peformLocalInstallBuild(String threshold, String project,String packages, boolean forceInstall, int hostType) throws InterruptedException {
		Select chooseAction =  new Select(carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "ActionSelectBox")));
		chooseAction.selectByValue("LocalInstallsActionDiv");
		Thread.sleep(2000);

		if(threshold!=null){
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "InstallThresholdField")).clear();
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "InstallThresholdField")).sendKeys(threshold);
		}
		
		Select chooseProject =  new Select(carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "InstallProjectSelectBox")));
		chooseProject.selectByVisibleText(project);
		
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"InstallPackageTextArea")).sendKeys(packages);
		
		Thread.sleep(2000);
		
		if(forceInstall){
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "ForceInstallRadioButton")).click();
		}
		
		Thread.sleep(2000);
		
		if(hostType==1){
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"CarnivalOnlyInstall")).click();
		}
		
		Thread.sleep(2000);

		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"InstallAddToRequestButton")).click();
		
		Boolean notPresent = true;
		try{	
			notPresent = ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(ElementLocatorUtility.getWebDriverLocator(root,"ErrorMessageLabel"))).apply(carnivalWebDriver);
		}catch(NoSuchElementException elementException){
			
		}
		
		if(!notPresent)
			return carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"ErrorMessageLabel")).getText();
		
		Thread.sleep(2000);
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "InstallRequestNotesTextField")).sendKeys("Scheduling package :"+packages);
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"ScheduleNowButtonLocator")).click();
		
		Thread.sleep(2000);

		return carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"NoticeMessageText")).getText();
		
	}
	
	/**
	 * This method takes different parameters to install package(S)
	 * @param threshold
	 * @param project
	 * @param packages
	 * @param forceInstall
	 * @param hostType
	 * @throws InterruptedException 
	 */
	public String peformLocalInstallHotfix(String threshold, String project,String packages, boolean forceInstall, int hostType,String reason) throws InterruptedException {
		
		Select chooseAction =  new Select(carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "ActionSelectBox")));
		chooseAction.selectByValue("LocalInstallsHotFixActionDiv");
		Thread.sleep(2000);

		if(threshold!=null){
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "HotfixInstallThresholdField")).clear();
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "HotfixInstallThresholdField")).sendKeys(threshold);
		}
		
		Select chooseProject =  new Select(carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "HotfixInstallProjectSelectBox")));
		chooseProject.selectByVisibleText(project);
		
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"HotfixInstallPackageTextArea")).sendKeys(packages);
		
		Thread.sleep(2000);
		
		if(forceInstall){
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "HotfixForceInstallRadioButtonTrue")).click();
		}
		
		Thread.sleep(2000);
		
		if(hostType==1){
			carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"HotfixCarnivalOnlyInstallRadioButton")).click();
		}
		
		
		Select reasonForHotfix =  new Select(carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "ReasonForHotfix")));
		reasonForHotfix.selectByVisibleText(reason);
		
		Thread.sleep(2000);

		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"LocalInstallHotfixAddToRequestButton")).click();
		
		Boolean notPresent = true;
		try{	
			notPresent = ExpectedConditions.not(ExpectedConditions.presenceOfElementLocated(ElementLocatorUtility.getWebDriverLocator(root,"ErrorMessageLabel"))).apply(carnivalWebDriver);
		}catch(NoSuchElementException elementException){
			
		}
		
		if(!notPresent)
			return carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"ErrorMessageLabel")).getText();
		
		Thread.sleep(2000);
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root, "InstallRequestNotesTextField")).sendKeys("Scheduling package :"+packages);
		carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"ScheduleNowButtonLocator")).click();
		
		Thread.sleep(2000);

		return carnivalWebDriver.findElement(ElementLocatorUtility.getWebDriverLocator(root,"NoticeMessageText")).getText();
		
	}
	



}
