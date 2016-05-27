package com.apple.carnival.deleteme;

import java.util.Arrays;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class DuplicatesTest {
	
	@Test
	public void duplicatesPositiveTest(){
		int[] myArray = {2,4,2,5,7,8,9};
		
		Arrays.sort(myArray);
		
		boolean hasDuplicates = checkIfArrayHasDuplicates(myArray);
		
		Assert.assertTrue(hasDuplicates,"The array should have had duplicate elements");
		
		Reporter.log("Array has duplicates",true);
	}
	
	@Test
	public void duplicatesNegativeTest(){
		int[] myArray = {2,4,5,7,8,9};
		
		boolean hasDuplicates = checkIfArrayHasDuplicates(myArray);
		
		Assert.assertFalse(hasDuplicates,"The array should not have had duplicate elements");
		
		Reporter.log("Array has does not have duplicates",true);
	}

	private boolean checkIfArrayHasDuplicates(int[] myArray) {
		
		for(int i=0;i<myArray.length;i++){
			
			int element = myArray[i];
			
			return substractAndCheck(myArray,element,i);
		}
		
		return false;
		
		
	}

	private boolean substractAndCheck(int[] myArray, int element,int index) {
		
		for(int i=0;i<myArray.length;i++){
			if(i != index){
				if(element - myArray[i] == 0)
					return true;
			}
			
		}
		return false;
	}

}
