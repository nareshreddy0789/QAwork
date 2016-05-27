package com.apple.carnival.qa.coreframework.data;

import java.util.Map;


public class TestScenario {
	
	private Map<String,String> testCaseInput=null;
	private Map<String,String> testCaseExpectedData=null;
	private Map<String,String> response=null;
	
	public TestScenario(Map<String,String> input,Map<String,String>  expected){
		this.testCaseInput=input;
		this.testCaseExpectedData=expected;
	}
	
	private TestScenario() {

	}

	public Map<String,String> getResponse() {
		return response;
	}

	public void setResponse(Map<String,String> response) {
		this.response = response;
	}
	
	public Map<String,String> getTestCaseInputData() {
		return testCaseInput;
	}


	public Map<String,String> getTestCaseExpectedData() {
		return testCaseExpectedData;
	}
	


	

}
