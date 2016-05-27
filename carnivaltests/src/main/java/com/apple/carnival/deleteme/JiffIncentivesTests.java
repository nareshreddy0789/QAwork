package com.apple.carnival.deleteme;

import java.io.FileReader;
import java.io.IOException;
import java.io.Reader;
import java.util.Properties;

import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;


public class JiffIncentivesTests {
	
	private Properties config = new Properties();
	private static String URL ="www.googe.com";
	
	@BeforeClass
	public void init() throws IOException{
		
		Reader reader = new FileReader("src/main/resources/framework.properties");
		
		config.load(reader);
	
		
	}
	
	@Test
	public void someTest(){
		String url = config.getProperty("carnival.url");
		
		System.out.println("The url is :"+ url);
	}

}
