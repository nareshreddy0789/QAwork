package com.apple.carnival.qa.coreframework.dataproviders;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JSONDataProvider {
	
	@DataProvider(name="JSONDataForScheduleRequest")
	public static Object[][] getJSONDataForScheduleRequest() throws JSONException{
		
		JSONObject jsonRequestObject = new JSONObject();
		jsonRequestObject.put("requestID", "");
		jsonRequestObject.put("note", "CI Builder Create Schedule");
		
		Object[][] data = new Object[1][1];
		
		data[0][0] = jsonRequestObject;
		
		return data;
		
	}
	
	@DataProvider(name="JSONDataForCreateRequest")
	public static Object[][] getJSONDataForCreateRequest(Method m) throws JSONException, JsonIOException, JsonSyntaxException, FileNotFoundException{
		
		Data dataAnnotation = m.getAnnotation(Data.class);
		String jsonFile=null;
		
		if (!dataAnnotation.dataFile().isEmpty()) {
			jsonFile = dataAnnotation.dataFile();
		}
		
		System.out.println("The file is :" + jsonFile);
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(new FileReader(jsonFile));

		Gson gson = new Gson();
		String gsonString = gson.toJson(jsonElement);

		JSONObject jsonRequestObject = new JSONObject(gsonString);
		
		
		Object[][] data = new Object[1][1];
		
		data[0][0] = jsonRequestObject;
		
		return data;
		
	}
	

}
