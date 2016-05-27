package com.apple.carnival.qa.coreframework.data;



public class GenericTestCase  implements TestCase {
	

	private GenericTestCaseData genericTestCaseData=null;
	
	
	public GenericTestCase(TestCaseData input){
	
		this.genericTestCaseData=(GenericTestCaseData) input;
	}
	
	private GenericTestCase() {

	}



	@Override
	public TestCaseData getTestCaseData() {
		// TODO Auto-generated method stub
		return null;
	}



	

}
