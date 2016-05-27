package com.apple.carnival.deleteme.copy;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class ApplicationLoginTests {

	@BeforeClass
    @Parameters({"applicationUrl","browserType"})
    public void start(String url,String driver){
    
        Browser.launchApplication(url,driver);
        
    }
	
	@Test
	public void composeEmailTest(){
		
		LoginPage login = new LoginPage();
		login.loginToApplication("user1@bayamp.com", "user1");
		
		//ComposeEmailPage email = new ComposeEmailPage();
		
		//email.send("test1234@gmailcom","subject","Hello Dear, How are you ? I am writing this email to you. please reply asap");
		
		//Validations
	}
	
	@AfterClass
	public void stop(){
		Browser.quitApplication();
	}
}
