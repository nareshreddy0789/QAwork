package com.apple.carnival.deleteme;

import org.testng.Assert;
import org.testng.annotations.Test;

public class AssertionTest {
	
	@Test
	public void assertTest(){
		
		try {
			Assert.assertEquals("200", "300");
		}catch(AssertionError e){
			System.out.println(e.getMessage());
		}
		
		System.out.println("$$$$$$$$$$$$$$$$ h a r i s h $$$$$$$$$$$$$$$$$$$$$$");
		
	}

}
