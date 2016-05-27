package com.apple.carnival.qa.coreframework.data;



public class RestTestCase implements TestCase {

	private RestTestCaseData restTestCaseData=null;

	
	public RestTestCase(TestCaseData input){
		this.restTestCaseData=(RestTestCaseData)input;
	}
	
	private RestTestCase() {

	}

	@Override
	public TestCaseData getTestCaseData() {
	
		return this.restTestCaseData;
	}


}
