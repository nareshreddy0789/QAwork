package com.apple.carnival.deleteme;

import org.testng.Assert;
import org.testng.Reporter;
import org.testng.annotations.Test;

import com.apple.carnival.qa.common.util.JcraftUtil;

public class JcraftUtilTests {

	
//	@Test
	public void sftpUtilTest(){
		
		//String[] serverInfo = {"vp21q01if-ztbu14114601.vp.if1.apple.com","harish_appannagari","Automation1234"};
		String remoteHost = "vp21q01if-ztbu14114601";
		String expectedOutput = "\\d+\\s+/opt/ais/local/CarnivalQA/clown/Current/\\s+";
		String shellCommand = "du -sk /opt/ais/local/CarnivalQA/clown/Current/";
	
		//Disk Usage
		String commandOutPut = JcraftUtil.executeShellCommand(remoteHost, false, shellCommand);
		
		System.out.println("The out put is : \n"+ commandOutPut);
		
		Assert.assertTrue(commandOutPut.matches(expectedOutput) ,"Expected Out put is not same as Actual Out put");
	}
	
	//@Test
	public void sftpUtilTestWithKeys(){
		
		//String[] serverInfo = {"vp21q01if-ztbu14114601.vp.if1.apple.com","harish_appannagari","Automation1234"};
		String remoteHost = "vp21q01if-ztbu14114601.vp.if1.apple.com";
		String expectedOutput = "\\d+\\s+/opt/ais/local/CarnivalQA/clown/Current/\\s+";
		String shellCommand = "du -sk /opt/ais/local/CarnivalQA/clown/Current/";
	
		//Disk Usage
		String commandOutPut = JcraftUtil.executeShellCommand(remoteHost, false, shellCommand);
		
		System.out.println("The out put is : \n"+ commandOutPut);
		
		Assert.assertTrue(commandOutPut.matches(expectedOutput) ,"Expected Out put is not same as Actual Out put");
	}
	
	@Test
	public void sshCheckClownPackageDoesNotExistsOnWD40Host() {
		String expectedOutput = "";
		String shellCommand = "du -sk /opt/ais/local/CarnivalQA/clown/Current/";
		String commandOutPut = null;
		String domain = "vp.if1.apple.com";
		String hostToUninstall = "vp21q01if-ztbu14114601";
		Reporter.log("Connecting to remote host using Jcraft Libraries to validate package does not exists on WD40 host");

		// Disk Usage
		if (domain != null || domain != "" || !domain.isEmpty())
			commandOutPut = JcraftUtil.executeShellCommand(hostToUninstall + "." + domain, false, shellCommand);
		else
			commandOutPut = JcraftUtil.executeShellCommand(hostToUninstall,false, shellCommand);

		System.out.println("The out put is : \n" + commandOutPut);

		Assert.assertTrue(commandOutPut.matches(expectedOutput),"Expected Out put is not same as Actual Out put");
	}
}
