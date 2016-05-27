package com.apple.carnival.deleteme;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.safari.SafariDriver;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

public class CloudTest {

	private WebDriver driver = null;

	@BeforeClass
	public void init() throws InterruptedException{
	//	driver = new FirefoxDriver();
		driver = new SafariDriver();
		
		driver.get("https://beta.icloud.com/");
		driver.manage().timeouts().implicitlyWait(7, TimeUnit.SECONDS);
		Thread.sleep(2000);

	}
	@Test
	public void create() throws InterruptedException{
		driver.findElement(By.linkText("Create one now.")).click();
		//driver.manage().timeouts().pageLoadTimeout(5, TimeUnit.SECONDS);
		//Thread.sleep(2000);		 

		String xpath = "//iframe[contains(@id,'sc')]";
		WebElement frame = driver.findElement(By.xpath(xpath));
		driver.switchTo().frame(frame);
		
		driver.findElement(By.id("accountName")).sendKeys("harish_appannagari+123@apple.com");
		

		Thread.sleep(2000);
	}

	@AfterClass
	public void cleanUp(){
		driver.quit();

	}
}
