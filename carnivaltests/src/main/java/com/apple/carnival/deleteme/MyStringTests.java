package com.apple.carnival.deleteme;

import org.testng.Assert;
import org.testng.annotations.Test;

public class MyStringTests {
	
	
	@Test
	public void lengthTest(){
		
		int expectedLen = 6;
		String s1 = new String("mumbai");
		
		int len = s1.length();
		
		Assert.assertEquals(len, expectedLen);
	}

}
