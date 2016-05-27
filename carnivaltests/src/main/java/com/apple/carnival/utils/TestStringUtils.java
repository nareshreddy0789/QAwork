package com.apple.carnival.utils;

public class TestStringUtils {

	public static String parseStringToGetIdTokenValue(String str) {

		if(str != null){
			return str.substring(str.indexOf("=")+1,str.length() ).trim();
		}

		return null;

	}

	public static String parseNoticeMessageToRetrieveRequestID(String str){

		String[] myArray = str.split(" ");

		return myArray[3].trim();

	}

}
