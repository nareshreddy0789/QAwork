package com.apple.carnival.deleteme;

import org.testng.Assert;
import org.testng.annotations.Test;

import com.apple.carnival.qa.common.util.JcraftUtil;

public class JcraftUtilTestsWithKeys {

	
	@Test
	public void sftpUtilTest1(){
		
		String[] serverInfo = {"vp21q01if-ztbu14114601.vp.if1.apple.com","harish_appannagari"};
		String[] clientInfo = {"/Users/harisha/.ssh/id_rsa","Family"};
		
		String wd40Host = "vp21q01if-ztbu14114601.vp.if1.apple.com";
		String expectedOutput = "\\d+\\s+/opt/ais/local/CarnivalQA/clown/Current/\\s+";
		
		//Disk Usage
		String commandOutPut = JcraftUtil.executeShellCommand(serverInfo, clientInfo,"du -sk /opt/ais/local/CarnivalQA/clown/Current/");
		
		System.out.println("The out put is : \n"+ commandOutPut);
		
		Assert.assertTrue(commandOutPut.matches(expectedOutput) ,"Expected Out put is not same as Actual Out put");
	}
	
	
	@Test
	public void sftpUtilTest2(){
		
		String[] serverInfo = {"vp21q01if-ztbu14114701.vp.if1.apple.com","harish_appannagari"};
		String[] clientInfo = {"/Users/harisha/.ssh/id_rsa","Family"};
		
		String expectedOutput = "";
		
		//Disk Usage
		String commandOutPut = JcraftUtil.executeShellCommand(serverInfo, clientInfo,"du -sk /opt/ais/local/CarnivalQA/clown/Current/");
		
		System.out.println("The out put is : \n"+ commandOutPut);
		
		
		Assert.assertTrue(commandOutPut.matches(expectedOutput) ,"Expected Out put is not same as Actual Out put");
	}
}
