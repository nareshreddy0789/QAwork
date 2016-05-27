package com.apple.carnival.deleteme;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

import com.apple.carnival.qa.common.util.DateUtil;
import com.apple.carnival.qa.common.util.ValidationUtils;
import com.apple.carnival.qa.coreframework.data.RestTestCaseData;
import com.apple.carnival.qa.coreframework.data.TestCase;
import com.apple.carnival.qa.coreframework.dataproviders.Data;
import com.apple.carnival.qa.parser.JsonParserUtil;
import com.apple.carnival.utils.TestStringUtils;
import com.apple.messageclient.MessageClient;
import com.apple.messageclient.MessageExchange;
import com.apple.messageclient.apache.http.RestMessageClient;

public class DataProviderTests {
	
	
	private String requestID=null;

	//@Test(dataProvider = "JSONDataForCreateRequest", dataProviderClass = com.apple.carnival.qa.coreframework.dataproviders.JSONDataProvider.class)
	//@Data(dataFile="src/main/resources/data/ClownWD40HostInstall.JSON")
	public void dataProviderCreateRequestTest(JSONObject jsonRequestObject) throws JSONException{
		System.out.println("Data Provider Schedule Request Test");
		
		String scheduleDate = DateUtil.getDate("yyyy/MM/dd h:mm a");
		jsonRequestObject.put("scheduledDate", scheduleDate);
		
		String requestEntityString = jsonRequestObject.toString();
		System.out.println("JSON POST REQUEST MEESSAGE  :\n" + requestEntityString);
		
	}
	
	//@Test(dataProvider = "JSONDataForScheduleRequest", dataProviderClass = com.apple.carnival.qa.coreframework.dataproviders.JSONDataProvider.class)
	public void dataProviderScheduleRequestTest(JSONObject jsonRequestObject) throws JSONException{
		System.out.println("Data Provider Schedule Request Test");
		
		String requestID = "1234567890123";
		jsonRequestObject.put("requestID", requestID);
		
		String requestEntityString = jsonRequestObject.toString();
	
		
		System.out.println("JSON POST REQUEST MEESSAGE  :\n" + requestEntityString);
		
	}
	
	//@Test(dataProvider = "restapiTestData", dataProviderClass = com.apple.carnival.qa.coreframework.dataproviders.TestCaseDataProvider.class)
	@Data(dataFile="src/main/resources/data/POSTClownCreateRequestTestCaseData.JSON")
	public void dataProviderCreateRequestTest(TestCase test) throws JSONException, InterruptedException{
		Thread.sleep(7000);
		System.out.print("Data Provider Create Request Test");
		
		MessageClient client = new RestMessageClient();
		MessageExchange restMessage = client.execute(test);
		//MessageExchange requestAndresponse = client.execute(test);
		RestTestCaseData restData = (RestTestCaseData)test.getTestCaseData();
			
		String reponseEntity = null;
		if(restData.getJsonTestCaseEntityResponseData() != null){
			reponseEntity=restData.getJsonTestCaseEntityResponseData().toString();
			System.out.println("The response entity is :" + reponseEntity);
			
		}
		
		JSONObject expectedData = restData.getJsonTestCaseDataExpectedData();
		String expectedEntity = expectedData.get("entity").toString();
		
		System.out.println("Expected entity is :" + expectedEntity);
		
		JsonParserUtil parseJson = new JsonParserUtil(restData.getJsonTestCaseEntityResponseData().toString());
		String descritpion = parseJson.getNodeValue("description");
		requestID = TestStringUtils.parseStringToGetIdTokenValue(descritpion);
		System.out.println("The response status code is :" + restData.getTestCaseStatusCode());
		System.out.println("The request ID is: "+requestID);
		System.out.println("Peform Validations ");
		
		//Assert.assertEquals(restData.getTestCaseStatusCode(),expectedData.get("statuscode"));
		//Assert.assertEquals(restData.getJsonTestCaseEntityResponseData().get("status").toString(),((JSONObject)expectedData.get("entity")).get("status"));
		//Assert.assertEquals(restData.getJsonTestCaseEntityResponseData().get("code").toString(),((JSONObject)expectedData.get("entity")).get("code"));
		
		JSONObject obj= (JSONObject) expectedData.get("entity");
		String regExDescription = obj.getString("description").replaceAll("%", "");
		System.out.println("The description is :" + obj.get("description").toString());
		
		//Assert.assertTrue((restData.getJsonTestCaseEntityResponseData().getString("description")).matches(regExDescription));
		ValidationUtils.validateTestCase(restMessage.getTestCaseData());
		
	}
	
	//@Test(dependsOnMethods = { "dataProviderCreateRequestTest" },dataProvider = "restapiTestData", dataProviderClass = com.apple.carnival.qa.coreframework.dataproviders.TestCaseDataProvider.class)
	@Data(dataFile="src/main/resources/data/POSTClownScheduleRequestTestCaseData.JSON")
	public void dataProviderScheduleRequestTest(TestCase test) throws JSONException{
		System.out.print("Data Provider Schedule Request Test");
		
		RestTestCaseData restData = (RestTestCaseData)test.getTestCaseData();
		
		JSONObject entity = (JSONObject) restData.getJsonTestCaseInput().get("entity");
		entity.put("requestID", requestID);
		
		restData.setJsonTestCaseEntityReponseData(entity);
		
		MessageClient client = new RestMessageClient();
		MessageExchange restMessage = client.execute(test);

		 restData = (RestTestCaseData)test.getTestCaseData();
			
		String reponseEntity = null;
		if(restData.getJsonTestCaseEntityResponseData() != null){
			reponseEntity=restData.getJsonTestCaseEntityResponseData().toString();
			System.out.println("The response entity is :" + reponseEntity);
			
		}
		
		JSONObject expectedData = restData.getJsonTestCaseDataExpectedData();
		String expectedEntity = expectedData.get("entity").toString();
		
		System.out.println("Expected entity is :" + expectedEntity);
		System.out.println("The response status code is :" + restData.getTestCaseStatusCode());
		System.out.println("Peform Validations ");
		
		//Assert.assertEquals(restData.getTestCaseStatusCode(),expectedData.get("statuscode"));
		//Assert.assertEquals(restData.getJsonTestCaseEntityResponseData().get("status").toString(),((JSONObject)expectedData.get("entity")).get("status"));
		//Assert.assertEquals(restData.getJsonTestCaseEntityResponseData().get("code").toString(),((JSONObject)expectedData.get("entity")).get("code"));
		//Assert.assertEquals(restData.getJsonTestCaseEntityResponseData().get("description"), ((JSONObject)expectedData.get("entity")).get("description")) ;
		
		ValidationUtils.validateTestCase(restMessage.getTestCaseData());
		
	}
	
	//@Test(dataProvider = "restapiTestData", dataProviderClass = com.apple.carnival.qa.coreframework.dataproviders.TestCaseDataProvider.class)
	@Data(dataFile="src/main/resources/data/GETApplicationTestCaseData.JSON")
	public void getApplicationTest(TestCase test) throws JSONException{
		System.out.print("Data Provider Create Request Test");
		
		MessageClient client = new RestMessageClient();
		MessageExchange restMessage = client.execute(test);
		//MessageExchange requestAndresponse = client.execute(test);
		RestTestCaseData restData = (RestTestCaseData)test.getTestCaseData();
	
		System.out.println("The response is :" + restData.getStringTestCaseResponseData());
		
		ValidationUtils.validateTestCase(restMessage.getTestCaseData());
		
	}
}
