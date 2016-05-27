package com.apple.carnival.deleteme;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class CountDuplicatesTest {
	
	@Test
	public void contDuplicatesPositiveTest(){
		int[] myArray = {2,4,2,5,7,8,9};
		
		Arrays.sort(myArray);
		
		Map<String,String> countDuplicates = getDuplicateCount(myArray);
	
	}

	private Map<String, String> getDuplicateCount(int[] myArray) {
		
		Map<String,String> duplicateCount = new HashMap<String,String>();
		for(int i=0;i<myArray.length;i++){
			
			int element = myArray[i];
			int count=0;
			for(int k=0;k <myArray.length;i++){
				if(i != k){
					if(element - myArray[k] == 0){
						if( duplicateCount.get(Integer.toString(element)) != null )
							duplicateCount.put(Integer.toString(element), Integer.toString(++count));
					}
						
				}
				
			}
		}
		
		return null;
	}


}
