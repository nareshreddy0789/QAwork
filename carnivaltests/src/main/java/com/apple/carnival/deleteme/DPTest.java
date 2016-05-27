package com.apple.carnival.deleteme;

import org.testng.Reporter;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class DPTest {


	@Test(dataProvider="giveNames" , dataProviderClass = com.apple.carnival.deleteme.DataProviderFactory.class)
	public void test1(String name,int age){

		//String[] names = {"ninu","harish","sindhu"};

		Reporter.log("Name is :"+ name+ " age is :"+ age,true);



	}


/*	@DataProvider
	public Object[][] giveNames(){
		
		
		Object value="123";
		Integer x = (Integer) value;
		
		//int primitiveX = x.intValue();
		
		
		Object name = "bayamp";
		String company = (String) name;
		
	
		
	
		
		Object[] names1= {"ninu",20};
		Object[] names2= {"sindhu",15};
		Object[] names3= {"harish",40};

		Object[][] names = new Object[3][2];

		names[0]=names1;
		names[1]=names2;
		names[2]=names3;


		return names;

	}*/

}
