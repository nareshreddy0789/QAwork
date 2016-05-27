package com.apple.carnival.deleteme;

import org.testng.annotations.Parameters;
import org.testng.annotations.Test;

public class Class2 extends BaseClass {
	
	public Class2(){
		System.out.println("Class2 Constructor");
	}

	@Test
	 @Parameters({"company"})
	public void test1OfClass2(String param1){
		System.out.println("The test1OfClass2 Method parameter is :"+ param1);
	}
	
	@Test
	public void test2OfClass2(){
		System.out.println("test2OfClass2");
	}
	
}
