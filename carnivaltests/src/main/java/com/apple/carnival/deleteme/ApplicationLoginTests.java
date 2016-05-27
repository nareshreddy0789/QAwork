package com.apple.carnival.deleteme;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ApplicationLoginTests {

	@BeforeClass
    @Parameters({"url","driver"})
    public void start(String url,String driver){
    
       // Browser.launchApplication(url,driver);
		System.out.println("THe url is :"+ url);
		System.out.println("THe driver is :"+ driver);
        
    }
	
	@Test
	 @Parameters({"a"})
	public void composeEmailTest(String param1){
		System.out.println("The composeEmailTest Method parameter is :"+ param1);
		//LoginPage login = new LoginPage();
		//login.loginToApplication("user1@bayamp.com", "user1");
		
		//ComposeEmailPage email = new ComposeEmailPage();
		
		//email.send("test1234@gmailcom","subject","Hello Dear, How are you ? I am writing this email to you. please reply asap");
		
		//Validations
	}
	
	@AfterClass
	public void stop(){
		//Browser.quitApplication();
	}
}
