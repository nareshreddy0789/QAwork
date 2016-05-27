package com.apple.carnival.deleteme.copy;

import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;

public class BaseClass {
	
	public BaseClass(){
		System.out.println("BaseClass Constructor");
	}

	@BeforeClass
	public static void startUp(){
		
		System.out.println("StartUp method from BaseClass");
		
	}
	
	@AfterClass
	public static void shutDown(){
		System.out.println("ShutDown method from BaseClass");
		
	}
}
