package com.apple.carnival.qa.parser;

import java.io.FileNotFoundException;
import java.io.FileReader;

import org.json.JSONException;
import org.json.JSONObject;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

public class JsonParserUtil {

	private String json=null;
	private JSONObject requestObject=null;
	public static JSONObject parseJsonFile(String jsonFile) {
		JSONObject requestObject = null;
		try{
			JsonParser parser = new JsonParser();
			JsonElement jsonElement = parser.parse(new FileReader(jsonFile));

			Gson gson = new Gson();
			String gsonString = gson.toJson(jsonElement);

			 requestObject = new JSONObject(gsonString);
		} catch (JsonIOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonSyntaxException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return requestObject;
	}

	public JsonParserUtil(String jsonString){
		this.json=jsonString;
	}

	public String getNodeValue(String node) {

		try {
			return new JSONObject(json).getString(node);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	}
	
	public static String  getNodeValueFromJSONObjectAsString(JSONObject object,String node) {

		try {
			return object.getString(node);
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return null;

	}
	
	
}
