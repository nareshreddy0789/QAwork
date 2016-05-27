package com.apple.carnival.api.request;

import static org.testng.Assert.assertEquals;

import java.io.IOException;
import java.io.InputStream;

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
import com.apple.carnival.qa.coreframework.dataproviders.TestCaseDataProvider;
import com.apple.carnival.qa.coreframework.properties.Configuration;
import com.apple.carnival.qa.coreframework.properties.FrameworkConfigurationReader;
import com.apple.messageclient.MessageClient;
import com.apple.messageclient.apache.http.RestMessageClient;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


@SuppressWarnings("deprecation")
public class CopyOfApplicationInstancesTests {
	
	
	@Test
	public void testGetInstancesForOpenMQISAppJson() throws ClientProtocolException, IOException {

		int expectedStatusCode = 200;
		String expectedEntity= "{\"code\":\"SUCCESS\",\"ISApplication\":{\"name\":\"WD40OpenMQ\",\"instances\":[1]}}";
		String application="WD40OpenMQ";
		Configuration config = new FrameworkConfigurationReader("framework.properties");
		String myResource = config.getProperty("hosts.applications.instances.resource")+application;

	
		Reporter.log("Sending GET request to : "+ myResource);
		System.out.println("Sending GET request to : "+ myResource);
		HttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(myResource);
		//getRequest.addHeader("Accept", "application/json");
		
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
	
	
	@Test
	public void testGetInstancesForCarnivalISAppJson() throws ClientProtocolException, IOException {

		int expectedStatusCode = 200;
		String expectedEntity= "{\"code\":\"SUCCESS\",\"ISApplication\":{\"name\":\"Carnival\",\"instances\":[1]}}";
		String application="Carnival";
		Configuration config = new FrameworkConfigurationReader("framework.properties");
		String myResource = config.getProperty("hosts.applications.instances.resource")+application;

	
		Reporter.log("Sending GET request to : "+ myResource);
		System.out.println("Sending GET request to : "+ myResource);
		HttpClient client = new DefaultHttpClient();
		HttpGet getRequest = new HttpGet(myResource);
		//getRequest.addHeader("Accept", "application/json");
		
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
