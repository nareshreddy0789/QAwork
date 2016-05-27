package com.apple.carnival.deleteme;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class AddTwoArrayElementsAndVerify {
	
	
	@Test
	public void arrayTest1(){
		
		int[] myArray = {3,4,1,2,5,8,7,13,14,9,4,5};
		
		boolean checked = checkSumOfAnyTwoArrayElements(myArray,7);
		
		Assert.assertTrue(checked,"The addition of two elemments in the array should have matched the expected total");
		
		Reporter.log("Checked that the sum of two elements will equal the expected value passsed",true);
	}

	private boolean checkSumOfAnyTwoArrayElements(int[] myArray, int numberToCheck) {

		for(int i=0;i<myArray.length;i++){
			
			int arrayElement = myArray[i];
			int searchElement = numberToCheck - arrayElement;
			
			if(searchElement < 0)
				continue;
			
			if(searchArray(myArray , searchElement))
				return true;
			
			
			
		}
		return false;
	}

	private boolean searchArray(int[] myArray, int searchElement) {
	
	//we should be doing binary search to improve peformance(complexity) but for now will do linear search
		
		for(int i=0;i<myArray.length;i++){
			if(myArray[i] == searchElement)
				return true;
		}
		return false;
	}

}
