package com.apple.carnival.api.request;

import org.json.JSONException;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.apple.carnival.qa.common.util.ValidationUtils;
import com.apple.carnival.qa.coreframework.data.RestTestCaseData;
import com.apple.carnival.qa.coreframework.data.TestCase;
import com.apple.carnival.qa.coreframework.dataproviders.Data;
import com.apple.messageclient.MessageClient;
import com.apple.messageclient.MessageExchange;
import com.apple.messageclient.apache.http.RestMessageClient;


@SuppressWarnings("deprecation")
public class AllApplicationTests {


	@Test(dataProvider = "restapiTestData", dataProviderClass = com.apple.carnival.qa.coreframework.dataproviders.TestCaseDataProvider.class)
	@Data(dataFile="src/main/resources/data/GETApplicationTestCaseData.JSON")
	public void testGetAllApplsPlainText(TestCase test) throws JSONException{
		Reporter.log("Get All Applications Test",true);

		MessageClient client = new RestMessageClient();
		MessageExchange restMessage = client.execute(test);

		RestTestCaseData restData = (RestTestCaseData) restMessage.getTestCaseData();
		Reporter.log("The response is :" + restData.getStringTestCaseResponseData(),true);
		ValidationUtils.validateTestCase(restMessage.getTestCaseData());

	}
	
	@Test(dataProvider = "restapiTestData", dataProviderClass = com.apple.carnival.qa.coreframework.dataproviders.TestCaseDataProvider.class)
	@Data(dataFile="src/main/resources/data/GETApplicationTestCaseDataXML.JSON")
	public void testGetAllApplsAsXml(TestCase test) throws JSONException{
		Reporter.log("Get All Applications Test",true);

		MessageClient client = new RestMessageClient();
		MessageExchange restMessage = client.execute(test);

		RestTestCaseData restData = (RestTestCaseData) restMessage.getTestCaseData();
		Reporter.log("The response is :" + restData.getStringTestCaseResponseData(),true);
		ValidationUtils.validateTestCase(restMessage.getTestCaseData());

	}




}
