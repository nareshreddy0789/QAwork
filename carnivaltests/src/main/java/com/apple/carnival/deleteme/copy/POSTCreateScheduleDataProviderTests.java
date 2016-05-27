package com.apple.carnival.deleteme.copy;

import org.json.JSONException;
import org.testng.annotations.Test;

import com.apple.carnival.qa.common.util.ValidationUtils;
import com.apple.carnival.qa.coreframework.data.RestTestCaseData;
import com.apple.carnival.qa.coreframework.data.TestCase;
import com.apple.carnival.qa.coreframework.dataproviders.Data;
import com.apple.messageclient.MessageClient;
import com.apple.messageclient.MessageExchange;
import com.apple.messageclient.apache.http.RestMessageClient;

public class POSTCreateScheduleDataProviderTests {

	//private String requestID=null;

	@Test(dataProvider = "restapiTestData", dataProviderClass = com.apple.carnival.qa.coreframework.dataproviders.TestCaseDataProvider.class)
	@Data(dataFile="src/main/resources/data/POSTClownCreateAndScheduleRequestTestCaseData.JSON")
	public void dataProviderCreateRequestTest(TestCase test) throws JSONException, InterruptedException{
		Thread.sleep(7000);
		System.out.print("Data Provider Create Request Test");

		MessageClient client = new RestMessageClient();
		MessageExchange restMessage = client.execute(test);
		//MessageExchange requestAndresponse = client.execute(test);
		RestTestCaseData restData = (RestTestCaseData)test.getTestCaseData();

		//Assert.assertTrue((restData.getJsonTestCaseEntityResponseData().getString("description")).matches(regExDescription));
		ValidationUtils.validateTestCase(restMessage.getTestCaseData());

	}


}
