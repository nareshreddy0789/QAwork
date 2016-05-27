package com.apple.carnival.api.request;

import static org.testng.Assert.assertEquals;

import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.apple.carnival.qa.coreframework.data.TestScenario;
import com.apple.carnival.qa.coreframework.properties.Configuration;
import com.apple.carnival.qa.coreframework.properties.FrameworkConfigurationReader;
import com.apple.messageclient.MessageClient;
import com.apple.messageclient.apache.http.RestMessageClient;


@SuppressWarnings("deprecation")
public class CopyOfAllHostsTests {
	
	
	@Test
	public void testGetAllHostsPlainText() throws ClientProtocolException, IOException {

		int expectedStatusCode = 200;
		String expectedEntity= "vp21q01if-hpba15080901,vp21q01if-ztbu14114601,vp21q01if-ztbu14114701";
		Configuration config = new FrameworkConfigurationReader("framework.properties");
		String myResource = config.getProperty("hosts.resource");

	
		Reporter.log("Sending GET request to : "+ myResource);
		HttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(myResource);
		
		HttpResponse response = client.execute(getRequest);
		
		HttpEntity entity = response.getEntity();
		
		String actualEntity = EntityUtils.toString(entity);
		int actualStatusCode = response.getStatusLine().getStatusCode();
		
	
		//For Local Console Logging
		System.out.println("Output from Server .... ");
		System.out.println("Entity:\n"+actualEntity);
		System.out.println("Status Code:"+actualStatusCode);
		System.out.println("\n");
		
		//TestNG Report logger 
		Reporter.log("Output from Server .... \n");
		Reporter.log("Response Entity :"+actualEntity);
		Reporter.log("Response Status Code :"+actualStatusCode);
		
		Reporter.log("Performing Assertions" );
		Reporter.log("Expected status code is : "+expectedStatusCode +" and got  status code from server as  : "+actualStatusCode);
		Reporter.log("Expected entity is : "+expectedEntity +" and got entity from server as  : "+actualEntity);
		assertEquals(actualStatusCode,expectedStatusCode);
		assertEquals(actualEntity,expectedEntity);
		
		client.getConnectionManager().shutdown();
	}
	

	
	

}
