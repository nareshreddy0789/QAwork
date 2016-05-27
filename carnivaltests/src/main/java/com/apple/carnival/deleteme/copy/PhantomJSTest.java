package com.apple.carnival.deleteme.copy;

import java.net.URISyntaxException;

import org.openqa.selenium.Platform;
import org.openqa.selenium.phantomjs.PhantomJSDriver;
import org.openqa.selenium.remote.DesiredCapabilities;

public class PhantomJSTest {

	
	public static void main (String[] args) throws URISyntaxException, InterruptedException {
		
		DesiredCapabilities capabilities = DesiredCapabilities.phantomjs();
		//capabilities.setCapability("phantomjs.binary.path", file.getAbsolutePath());
		capabilities.setCapability("phantomjs.binary.path", "src/main/resources/phantomjs-1.9.7-macosx/bin/phantomjs");
		capabilities.setJavascriptEnabled(true);
		capabilities.setPlatform(Platform.ANY);
		PhantomJSDriver driver = new PhantomJSDriver(capabilities);
		Thread.sleep(3000);
		driver.quit();

	}
	

}