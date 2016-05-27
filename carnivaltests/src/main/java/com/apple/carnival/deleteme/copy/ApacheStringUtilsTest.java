package com.apple.carnival.deleteme.copy;

import org.apache.commons.lang.StringUtils;
import org.testng.annotations.Test;

public class ApacheStringUtilsTest {
	
	
	@Test
	public void stringUtilsTest(){
		
		String domain = "";
		
		if(!StringUtils.isEmpty(domain))
			System.out.println("NOT EMPtY");
		else
			System.out.println(" EMPtY");
	}

}
