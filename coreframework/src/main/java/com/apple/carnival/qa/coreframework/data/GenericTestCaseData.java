package com.apple.carnival.qa.coreframework.data;

import java.util.Map;

public class GenericTestCaseData extends TestCaseData {
	
	private Map<String,String> genericTestCaseInputData=null;
	private Map<String,String> genericTestCaseExpectedData=null;
	private Map<String,String> genericTestCaseResponseData=null;
	
	private GenericTestCaseData(){
		
		
	}
	
	public GenericTestCaseData(Map<String,String> input,Map<String,String> expected){
		this.genericTestCaseInputData=input;
		this.genericTestCaseExpectedData=expected;
	}
	
	public void setJsonTestCaseReponseData(Map<String,String> testCaseResponseData) {
		this.genericTestCaseResponseData = testCaseResponseData;
	}
	public Map<String,String> getJsonTestCaseInput() {
		return genericTestCaseInputData;
	}
	
	public Map<String,String> getJsonTestCaseDataExpectedData() {
		return genericTestCaseExpectedData;
	}
	
	public Map<String,String> getJsonTestCaseResponseData() {
		return genericTestCaseResponseData;
	}

}
