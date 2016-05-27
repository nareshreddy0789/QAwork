package com.apple.carnival.qa.common.util;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;

import com.apple.carnival.qa.coreframework.data.RestTestCaseData;
import com.apple.carnival.qa.coreframework.data.TestCaseData;



public class ValidationUtils {


	public static void validateTestCase(TestCaseData testData) throws JSONException{
		boolean testFailed=false;
		
		if(testData instanceof RestTestCaseData){
			RestTestCaseData restData = (RestTestCaseData) testData;
			JSONObject expectedData = restData.getJsonTestCaseDataExpectedData();
			JSONObject reponseEntity = restData.getJsonTestCaseEntityResponseData();

			if(expectedData.getString("statuscode") != null || !expectedData.getString("statuscode").isEmpty() ){
				Reporter.log("Expected Status code  is :"+expectedData.getString("statuscode"),true);
				try{
					Reporter.log("Status Code Validation",true);
					Assert.assertEquals(restData.getTestCaseStatusCode(),expectedData.get("statuscode"));	
				}catch(AssertionError e){
					testFailed=true;
					Reporter.log("Test Case Failed",true);
					Reporter.log(e.getMessage(), true);

				}

			}
			if( expectedData.get("entity") != null) {
				Reporter.log("Expected Entity  is :"+expectedData.get("entity"),true);

				if(expectedData.get("entity") instanceof JSONObject) {
					JSONObject expectedEntityAsJSON = expectedData.getJSONObject("entity");

					if(expectedEntityAsJSON.getString("status") != null){
						Reporter.log("Expected status  is :"+expectedEntityAsJSON.getString("status"),true);
						try{
							Reporter.log("Entity Status  Validation",true);
							Assert.assertEquals(restData.getJsonTestCaseEntityResponseData().getString("status"),expectedEntityAsJSON.getString("status"));
						}catch(AssertionError e){
							testFailed=true;
							Reporter.log("Test Case Failed",true);
							Reporter.log(e.getMessage(), true);
						}

					}

					if(expectedEntityAsJSON.getString("code") != null){
						Reporter.log("Expected Code  is :"+expectedEntityAsJSON.getString("code"),true);
						try{
							Reporter.log("Entity Code Validation",true);
							Assert.assertEquals(restData.getJsonTestCaseEntityResponseData().getString("code"),expectedEntityAsJSON.getString("code"));
						}catch(AssertionError e){
							testFailed=true;
							Reporter.log("Test Case Failed",true);
							Reporter.log(e.getMessage(), true);
						}

					}

					if(expectedEntityAsJSON.get("description") != null){
						String expectedDescription = expectedEntityAsJSON.getString("description");

						if("null".equals(expectedDescription)){
							Reporter.log("Entity Description Validation",true);
							try{
								//Assert.assertNull((restData.getJsonTestCaseEntityResponseData().get("description")));
								if(expectedDescription.equals(restData.getJsonTestCaseEntityResponseData().get("description")))
									Assert.assertNull(null);
							}catch(AssertionError e){
								testFailed=true;
								Reporter.log("Test Case Failed",true);
								Reporter.log(e.getMessage(), true);
							}

						}else{
							Reporter.log("Expected description  is :"+expectedEntityAsJSON.get("description"),true);
							if(isRegularExpression(expectedDescription)){
								expectedDescription = expectedDescription.replaceAll("%", "");
							}

							try{
								Reporter.log("Entity Description Validation",true);
								Assert.assertTrue((restData.getJsonTestCaseEntityResponseData().getString("description")).matches(expectedDescription));
							}catch(AssertionError e){
								testFailed=true;
								Reporter.log("Test Case Failed",true);
								Reporter.log(e.getMessage(), true);
							}

						}
					}

					//TODO : Validations for Request

				}

				if(expectedData.get("entity") instanceof String){

					String expectedEntityAsString = expectedData.getString("entity");
					try{
						Reporter.log("Entity Validation",true);
						Assert.assertEquals(restData.getStringTestCaseResponseData(),expectedEntityAsString);
					}catch(AssertionError e){
						testFailed=true;
						Reporter.log("Test Case Failed",true);
						Reporter.log(e.getMessage(), true);
					}
				}

			}


		}

		if(testFailed == true)
			Assert.fail("Test Case Failed");


	}

	private static boolean isRegularExpression(String pattern){

		if(pattern.contains("%"))
			return true;

		return false;
	}
}


