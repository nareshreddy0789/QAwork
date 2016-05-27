package com.apple.carnival.api.request;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

public class PostTests {


	@Test
	public void postJSONTest() throws JSONException, ClientProtocolException, IOException{
		int expectedStatusCode = 200;
		
		JSONObject json = new JSONObject();
		json.put("notes", "[CI Builder Create Request]");    
		json.put("scheduledDate", "2014/10/27 7:43 PM PDT");  
		json.put("actions", "[ { \"type\" : \"localinstallbuild\", \"project\" : \"CarnivalQA\",\"packages\" : \"clown14A148\",\"forceInstall\" : false,\"installOnHostsStyle\" : \"CONF_AND_CARNIVAL_INSTALL_STYLE\",\"acceptanceThreshold\" : \"100.0\"} ]");  
	
		Reporter.log("The JSON String is :" + json.toString(),true);
		HttpClient httpClient = new DefaultHttpClient();

		HttpPost postRequest = new HttpPost("http://vp21q01if-ztbu14114601.vp.if1.apple.com:8080/Carnival/services/1.0/workflow/request/schedule");
		
		StringEntity contentEntity = new StringEntity(json.toString());
		postRequest.addHeader("content-type", "application/json");
		postRequest.setEntity(contentEntity);
		
		HttpResponse response = httpClient.execute(postRequest);

		int actualStatusCode = response.getStatusLine().getStatusCode();

		Assert.assertEquals(actualStatusCode, expectedStatusCode);
		
	}

}
