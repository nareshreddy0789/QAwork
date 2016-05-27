package com.apple.carnival.deleteme;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

//import com.jayway.jsonpath.JsonPath;

public class ForAditya {
	
	
	@Test
	public void parseJsonTest() throws JSONException{
		
		//This string we will get from log files but for now I am hard coding
		String jsonStringFromLog = "{\"orderInfo\":{\"total\":\"101.99\",\"currencyCode\":\"USD\"} }";
		
		System.out.println("The json string is :"+jsonStringFromLog);
		
		JSONObject rootJson = new JSONObject(jsonStringFromLog);
		
		String actualAmount =getNodeValueFromJSONObjectAsString(rootJson,"orderInfo.total");
		String actualCurrentCode = getNodeValueFromJSONObjectAsString(rootJson,"orderInfo.currencyCode");
		
		Assert.assertEquals(actualAmount,"101.99");
		Assert.assertEquals(actualCurrentCode,"USD");
		
		
	}
	
	
	public static String  getNodeValueFromJSONObjectAsString(JSONObject root,String node) {

	
			//return object.getString(node);
			//return  JsonPath.read(root.toString(),"$."+node).toString();
			

return null;

	}

}
