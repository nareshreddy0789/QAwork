package com.apple.carnival.api.request;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

import java.io.FileReader;
import java.io.IOException;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.apple.carnival.qa.coreframework.properties.Configuration;
import com.apple.carnival.qa.coreframework.properties.FrameworkConfigurationReader;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

/**
 * 
 * @author harisha
 * This POST request is based on ip authentication, which means, from any machine before this test is run,
 * we need to grab the IP address and let the carnival know the ip adress. To do that, we need to 
 * put the following property in carnival.properties (or ci-dev.properties of Carnkva)
 * we need to add ip address in  carnival.web_service.admin.ip_whitelist
 */
@SuppressWarnings("deprecation")
public class LocalInstallInfrastructureRequestTests {
	
	@Test
	public void testOpenMQLocalInstallInfrastructure() throws JsonIOException, JsonSyntaxException, ClientProtocolException, IOException, InterruptedException{
	
		Configuration config = new FrameworkConfigurationReader("framework.properties");
		String myResource  = config.getProperty("request.localinstall.resource");
		String carnivalURL = config.getProperty("carnival.url");
		int expectedStatusCode = 200;
		String expectedEntity= "^\\{\"status\":\"SUCCESS\",\"code\":\"201 Created\",\"description\":\""+carnivalURL+"/TaskDetail.jsp?id=\\\\d{13}$\"}";
		System.out.println(expectedEntity);
		
		Thread.sleep(7000);
		Reporter.log("Sending POST request to : "+ myResource);
		HttpClient client = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(myResource);
		System.out.println("The resource is "+ myResource);
		
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(new FileReader("src/main/resources/data/OpenMQLocalInstall.JSON"));
		
		Gson gson = new Gson();
		String myString = gson.toJson(jsonElement);
		
		System.out.println("The JSON message is :\n"+ myString);
		StringEntity input = new StringEntity(myString);
		input.setContentType("application/json");
		
		postRequest.setHeader("application","json");
		postRequest.setEntity(input);

		HttpResponse response = client.execute(postRequest);
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
		if(actualEntity.matches(expectedEntity))
		assertTrue(true,expectedEntity);
		
		client.getConnectionManager().shutdown();
		

	}
	
	@Test
	public void testNegativeOpenMQLocalInstallInfrastructure() throws JsonIOException, JsonSyntaxException, ClientProtocolException, IOException, InterruptedException{
	
		Configuration config = new FrameworkConfigurationReader("framework.properties");
		String myResource  = config.getProperty("request.localinstall.resource");
		String carnivalURL = config.getProperty("carnival.url");
		int expectedStatusCode = 200;
		String expectedEntity= "{\"status\":\"ERROR\",\"code\":\"400 Bad Request\",\"description\":\"scheduledDate is required for request.\"}";
		System.out.println(expectedEntity);
		
		Thread.sleep(7000);
		Reporter.log("Sending POST request to : "+ myResource);
		HttpClient client = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(myResource);
		System.out.println("The resource is "+ myResource);
		
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(new FileReader("src/main/resources/data/OpenMQLocalInstall_Negative.JSON"));
		
		Gson gson = new Gson();
		String myString = gson.toJson(jsonElement);
		
		System.out.println("The JSON message is :\n"+ myString);
		StringEntity input = new StringEntity(myString);
		input.setContentType("application/json");
		
		postRequest.setHeader("application","json");
		postRequest.setEntity(input);

		HttpResponse response = client.execute(postRequest);
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
	public void testAntLocalInstallInfrastructure() throws JsonIOException, JsonSyntaxException, ClientProtocolException, IOException, InterruptedException{
	
		Configuration config = new FrameworkConfigurationReader("framework.properties");
		String myResource  = config.getProperty("request.localinstall.resource");
		String carnivalURL = config.getProperty("carnival.url");
		int expectedStatusCode = 200;
		String expectedEntity= "^\\{\"status\":\"SUCCESS\",\"code\":\"201 Created\",\"description\":\""+carnivalURL+"/TaskDetail.jsp?id=\\\\d{13}$\"}";
		System.out.println(expectedEntity);
		
		Thread.sleep(7000);
		Reporter.log("Sending POST request to : "+ myResource);
		HttpClient client = new DefaultHttpClient();
		HttpPost postRequest = new HttpPost(myResource);
		System.out.println("The resource is "+ myResource);
		
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(new FileReader("src/main/resources/data/AntLocalInstall.JSON"));
		
		Gson gson = new Gson();
		String myString = gson.toJson(jsonElement);
		
		System.out.println("The JSON message is :\n"+ myString);
		StringEntity input = new StringEntity(myString);
		input.setContentType("application/json");
		
		postRequest.setHeader("application","json");
		postRequest.setEntity(input);

		HttpResponse response = client.execute(postRequest);
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
		if(actualEntity.matches(expectedEntity))
		assertTrue(true,expectedEntity);
		
		client.getConnectionManager().shutdown();
		

	}
}