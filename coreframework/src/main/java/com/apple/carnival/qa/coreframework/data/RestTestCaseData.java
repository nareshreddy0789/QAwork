package com.apple.carnival.qa.coreframework.data;

import org.json.JSONObject;

public class RestTestCaseData extends TestCaseData{
	private String testCaseID=null;
	private String testCaseDescription=null;
	private JSONObject jsonTestCaseInputData=null;
	private JSONObject jsonTestCaseExpectedData=null;
	private JSONObject jsonTestCaseEntityResponseData=null;
	private String stringTestCaseResponseData=null;
	private int reponseStatusCode=0;
	
	private RestTestCaseData(){
		
		
	}
	
	public RestTestCaseData(JSONObject input,JSONObject expected){
		this.jsonTestCaseInputData=input;
		this.jsonTestCaseExpectedData=expected;
	}
	
	public RestTestCaseData(JSONObject input,JSONObject expected,String id,String description){
		this.jsonTestCaseInputData=input;
		this.jsonTestCaseExpectedData=expected;
		this.testCaseID=id;
		this.testCaseDescription=description;
	}
	public void setJsonTestCaseEntityReponseData(JSONObject jsonTestCaseEntityResponseData) {
		this.jsonTestCaseEntityResponseData = jsonTestCaseEntityResponseData;
	}
	
	public void setJsonTestCaseReponseStatusCode(int jsonTestCaseResponseStatusCode) {
		this.reponseStatusCode = jsonTestCaseResponseStatusCode;
	}
	
	public void setStringTestCaseReponseData(String stringTestCaseResponseStatusCode) {
		this.stringTestCaseResponseData = stringTestCaseResponseStatusCode;
	}
	public JSONObject getJsonTestCaseInput() {
		return this.jsonTestCaseInputData;
	}
	
	public JSONObject getJsonTestCaseDataExpectedData() {
		return this.jsonTestCaseExpectedData;
	}
	
	public JSONObject getJsonTestCaseEntityResponseData() {
		return this.jsonTestCaseEntityResponseData;
	}
	
	public String getStringTestCaseResponseData(){
		return this.stringTestCaseResponseData;
	}

	public int getTestCaseStatusCode(){
		return this.reponseStatusCode;
	}
	
	public String getTestCaseId(){
		return this.testCaseID;
	}
	
	public String getTestCaseDescription(){
		return this.testCaseDescription;
	}
}
