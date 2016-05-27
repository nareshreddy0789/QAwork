package com.apple.carnival.deleteme.copy;

import org.json.JSONException;
import org.json.JSONObject;
import org.testng.Assert;
import org.testng.annotations.Test;

public class RegExTest {

	@Test
	public void test(){
		//System.out.println("Expected :" );
		//regEx8();
		//containsMethod();
		//JSONStringTest();
		subStringTest();
	}

	private static void regEx1(){
		String expected = "^\\{\"status\":\"SUCCESS\",\"code\":\"\\d{4}\"}$";
		String actual="{\"status\":\"SUCCESS\",\"code\":\"1234\"}";

		//String expected ="\\Successfully scheduled task \\d{13} for execution!";
		//String actual = "Successfully scheduled task 1414315023124 for execution!";

		System.out.println("Expected :" + expected);
		System.out.println("Actual :" + actual);

		if(actual.matches(expected))
			System.out.println("HURRAY");
		else
			System.out.println("ALAS");
	}

	private static void regEx2(){
		//String expected = "^\\{\"status\":\"SUCCESS\"}$";
		//String actual="{\"status\":\"SUCCESS\"}";


		String expectedEntity= "{\"status\":\"200-OK\",\"code\":\"SUCCESS\",\"description\":null,\"request\":{\"status\":\"QUEUED\"}}";
		String actual = "Successfully scheduled task 1414315023124 for execution!";

		Assert.assertTrue(actual.matches(expectedEntity));
	}


	private static void regEx3(){
		//String expected = "^\\{\"status\":\"SUCCESS\"}$";
		//String actual="{\"status\":\"SUCCESS\"}";


		String expectedEntity= "http://vp21q01if-ztbu14114601.vp.if1.apple.com:8080/Carnival/TaskDetail.jsp\\?id=\\d{13}";
		//String expectedEntity= "\\d{13}";
		String actual = "http://vp21q01if-ztbu14114601.vp.if1.apple.com:8080/Carnival/TaskDetail.jsp?id=1414471400356";
		//String actual = "1414515609251";
		//String actual = "http://vp21q01if-ztbu14114601.vp.if1.apple.com:8080/Carnival/TaskDetail.jsp";
		//String actual = "1414515609251";
		Assert.assertTrue(actual.matches(expectedEntity));
	}

	private static void regEx4(){
		//String expected = "^\\{\"status\":\"SUCCESS\"}$";
		//String actual="{\"status\":\"SUCCESS\"}";


		String expectedEntity= "User \\d{2}.\\d{2,3}.\\d{2,3}.\\d{2,3} requested uninstall of clown14A148 on host vp21q01if-ztbu14114701.";
		//String expectedEntity= "\\d{13}";
		String actual = "User 17.115.164.166 requested uninstall of clown14A148 on host vp21q01if-ztbu14114701.";
		//String actual = "1414515609251";
		//String actual = "http://vp21q01if-ztbu14114601.vp.if1.apple.com:8080/Carnival/TaskDetail.jsp";
		//String actual = "1414515609251";

		if(actual.matches(expectedEntity))
			System.out.println("HURRAY");
		else
			System.out.println("ALAS");
	}

	private static void regEx5(){
		String carnivalURL="http://vp21q01if-ztbu14114601.vp.if1.apple.com:8080/Carnival";
		String expectedEntity= "^\\{\"status\":\"SUCCESS\",\"code\":\"201 Created\",\"description\":\""+carnivalURL+"/TaskDetail.jsp\\?id=\\d{13}\"}$";
		String actualEntity= "{\"status\":\"SUCCESS\",\"code\":\"201 Created\",\"description\":\"http://vp21q01if-ztbu14114601.vp.if1.apple.com:8080/Carnival/TaskDetail.jsp?id=1234567890123\"}";

		Assert.assertTrue(actualEntity.matches(expectedEntity));
	}

	private static void parseString(){

		String responseDescription = "http://vp21q01if-ztbu14114601.vp.if1.apple.com:8080/Carnival/TaskDetail.jsp?id=1414471400356";

		System.out.println(responseDescription.substring(responseDescription.indexOf("=")+1,responseDescription.length() ));
	}

	private static void regEx6(){
		String expected = "^\\{\"status\":\"SUCCESS\",\"description\":\"http://vp21q01if-ztbu14114601.vp.if1.apple.com:8080/Carnival/TaskDetail.jsp\\?id=\\d{13}\",\"code\":\"201 Created\"}";
		String actual = "{\"status\":\"SUCCESS\",\"description\":\"http://vp21q01if-ztbu14114601.vp.if1.apple.com:8080/Carnival/TaskDetail.jsp?id=1417823627761\",\"code\":\"201 Created\"}";

		Assert.assertTrue(actual.matches(expected));
	}

	private static void regEx7(){
		String expected = "Successfully scheduled task \\d{13} for execution!";
		String actual = "Successfully scheduled task 1418891214332 for execution!";

		Assert.assertTrue(actual.matches(expected));
	}

	private static void regEx8(){
		String expected = "\\$\\{\\w+\\-\\d{1,5}:response\\.\\w+\\}";
		String actual = "${TC1-1:response.requestID}";

		if(actual.matches(expected)){
			System.out.println("HURRAY    THEY MATCH !!!!!!");
		}
		else
			System.out.println("ALAS    THEY DO NOT MATCH !!!!!!");

		Assert.assertTrue(actual.matches(expected));

	}

	private static void containsMethod(){

		String entityString= "{\"requestID\" : \"${TC1-1:response.requestID}\",\"note\" : \"CI Builder Schedule Request\"}";

		if(entityString.contains("requestID")){
			System.out.println("Request ID is present in the entity String");
		}
		else
			System.out.println("Nope!!!!! Request ID is present in the entity String");
	}

	private static void JSONStringTest(){
		String entityString= "{\"requestID\" : \"${TC1-1:response.requestID}\",\"note\" : \"CI Builder Schedule Request\"}";
		try {
			JSONObject entity = new JSONObject(entityString);

			System.out.println("The request ID from JSON String is :" + entity.get("requestID"));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private static void subStringTest(){

		String requestId="${TC1-1:response.requestID}";

		System.out.println("The sub string is :" + requestId.substring(requestId.indexOf("{")+1, requestId.indexOf(":")));
	}






}
