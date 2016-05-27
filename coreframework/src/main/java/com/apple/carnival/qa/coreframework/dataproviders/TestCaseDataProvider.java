package com.apple.carnival.qa.coreframework.dataproviders;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.lang.reflect.Method;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.testng.annotations.DataProvider;

import com.apple.carnival.qa.coreframework.data.RestTestCase;
import com.apple.carnival.qa.coreframework.data.RestTestCaseData;
import com.apple.carnival.qa.coreframework.data.TestCase;
import com.apple.carnival.qa.coreframework.data.TestCaseData;
import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonIOException;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;


public class TestCaseDataProvider {

	@DataProvider(name="restapiTestData",parallel=false)
	public static Object[][] getTesCaseData(Method m) throws JSONException, JsonIOException, JsonSyntaxException, FileNotFoundException{

		Object[][] testData=null;
		Data dataAnnotation = m.getAnnotation(Data.class);
		String jsonFile=null;

		if (!dataAnnotation.dataFile().isEmpty()) {
			jsonFile = dataAnnotation.dataFile();
		}

		System.out.println("The file is :" + jsonFile);
		JsonParser parser = new JsonParser();
		JsonElement jsonElement = parser.parse(new FileReader(jsonFile));

		if(jsonElement.isJsonArray()){
			Gson gson = new Gson();
			String gsonString = gson.toJson(jsonElement);

			JSONArray mJsonArray = new JSONArray(gsonString);
			System.out.println("The list size is " + mJsonArray.length());
			testData = new Object[mJsonArray.length()][1];

			for(int i=0; i<mJsonArray.length();i++){
				JSONObject mJsonObject = mJsonArray.getJSONObject(i);
				JSONObject inputData = (JSONObject) mJsonObject.get("input");
				JSONObject expectedData = (JSONObject) mJsonObject.get("expected");
				String testCaseId = (String) mJsonObject.get("test_id");
				String testDescription = (String) mJsonObject.get("test_description");
				//TestCaseData data = new RestTestCaseData(inputData,expectedData);
				TestCaseData data = new RestTestCaseData(inputData,expectedData,testCaseId,testDescription);
				TestCase test = new RestTestCase(data);

				testData[i][0]=test;

			}

		}else{
			testData = new Object[1][1];
			Gson gson = new Gson();
			String gsonString = gson.toJson(jsonElement);
			JSONObject mJsonObject = new JSONObject(gsonString);
			JSONObject inputData = (JSONObject) mJsonObject.get("input");
			JSONObject expectedData = (JSONObject) mJsonObject.get("expected");
			String testCaseId = (String) mJsonObject.get("test_id");
			String testDescription = (String) mJsonObject.get("test_description");
			
			//TestCaseData data = new RestTestCaseData(inputData,expectedData);
			TestCaseData data = new RestTestCaseData(inputData,expectedData,testCaseId,testDescription);
			TestCase test = new RestTestCase(data);

			testData[0][0] = test;

		}


		return testData;

	}

}
