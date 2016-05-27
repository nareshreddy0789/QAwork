package com.apple.carnival.deleteme;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Class1 extends BaseClass{

	public Class1(){
		System.out.println("Class1 Constructor");
	}
	
	@Test
	@Parameters({"company"})
	public void test1OfClass1(String company){
		System.out.println("test1OfClass1");
		System.out.println("The company is :" + company);
	}
	
	@Test
	public void test2OfClass1(){
		System.out.println("test2OfClass1");
	}
}
