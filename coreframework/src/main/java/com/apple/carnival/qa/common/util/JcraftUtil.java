package com.apple.carnival.qa.common.util;

import java.io.InputStream;

import org.apache.log4j.Logger;

import com.apple.carnival.qa.coreframework.properties.Configuration;
import com.apple.carnival.qa.coreframework.properties.FrameworkConfigurationReader;
import com.apple.carnival.qa.sftp.util.MyUserInfo;
import com.jcraft.jsch.Channel;
import com.jcraft.jsch.ChannelExec;
import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.JSchException;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.UserInfo;

public class JcraftUtil {
	
	private JcraftUtil() {
		
	}

	/**
	 * This field - log is used for Logging Purpose.
	 */
	private static Logger	log	= Logger.getLogger(JcraftUtil.class);

	/**
	 * This method will execute a given command and shows output in the console
	 * if any, without any wait times.
	 * 
	 * @param serverInfo
	 * @param command
	 * @return
	 */
	public static String executeShellCommand(String[] serverInfo,String[] clientInfo, String command) {
		StringBuffer resBuf = new StringBuffer();
		Channel channel = null;
		Session session = null;
		ChannelSftp cmdProc = null;

		String server = serverInfo[0];
		String user = serverInfo[1];
		UserInfo ui = null;

		try {
			JSch jsch = new JSch();   
			session = jsch.getSession(user, server, 22);
			System.out.println("session created.");
			
			if(clientInfo !=null){
				String privateKey = clientInfo[0];
				String passPhrase = clientInfo[1];
				jsch.addIdentity(privateKey,passPhrase);
	            java.util.Properties config = new java.util.Properties();
	            config.put("StrictHostKeyChecking", "no");
	            session.setConfig(config);
			}else{
			// user name and password will be given via UserInfo interface.
			ui = new MyUserInfo(serverInfo[2]);
			session.setUserInfo(ui);
			}
			
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			cmdProc = (ChannelSftp) channel;

			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.connect();
			channel.setInputStream(System.in);
			// channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);
			InputStream in = channel.getInputStream();
			int ch;
			while ((ch = in.read()) != -1) {
				resBuf.append((char) ch);
			}
			in.close();
		} catch (JSchException e) {
			JcraftUtil.log.error("Unable to create a new SFTP session....", e);
			resBuf.append(e);
		} catch (Exception e) {
			JcraftUtil.log.error("error has occurred...", e);
			resBuf.append(e);
		} finally {
			if (cmdProc != null) {
				cmdProc.disconnect();
			}
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
				session = null;
			}
		}
		// Return from here.
		return resBuf.toString();
	}
	
	/**
	 * This method will execute a given command and shows output in the console
	 * if any, without any wait times.
	 * 
	 * @param serverInfo
	 * @param command
	 * @return
	 */
	public static String executeShellCommand(String serverInfo,boolean useKeys,String command) {
		StringBuffer resBuf = new StringBuffer();
		Channel channel = null;
		Session session = null;
		ChannelSftp cmdProc = null;

		Configuration config = new FrameworkConfigurationReader("framework.properties");
		String carnivalURL = config.getProperty("carnival.url");
		

		String user = config.getProperty("remote_user");
	
		UserInfo ui = null;

		try {
			JSch jsch = new JSch();   
			session = jsch.getSession(user, serverInfo, 22);
			System.out.println("session created.");
			
			if(useKeys){	
				String privateKey =config.getProperty("client_key");
				String passPhrase = config.getProperty("client_passphrase");
				jsch.addIdentity(privateKey,passPhrase);
	            java.util.Properties props = new java.util.Properties();
	            props.put("StrictHostKeyChecking", "no");
	            session.setConfig(props);
			}else{
			// user name and password will be given via UserInfo interface.
			ui = new MyUserInfo(config.getProperty("remote_password"));
			session.setUserInfo(ui);
			}
			
			session.connect();
			channel = session.openChannel("sftp");
			channel.connect();
			cmdProc = (ChannelSftp) channel;

			channel = session.openChannel("exec");
			((ChannelExec) channel).setCommand(command);
			channel.connect();
			channel.setInputStream(System.in);
			// channel.setInputStream(null);
			((ChannelExec) channel).setErrStream(System.err);
			InputStream in = channel.getInputStream();
			int ch;
			while ((ch = in.read()) != -1) {
				resBuf.append((char) ch);
			}
			in.close();
		} catch (JSchException e) {
			JcraftUtil.log.error("Unable to create a new SFTP session....", e);
			resBuf.append(e);
		} catch (Exception e) {
			JcraftUtil.log.error("error has occurred...", e);
			resBuf.append(e);
		} finally {
			if (cmdProc != null) {
				cmdProc.disconnect();
			}
			if (channel != null) {
				channel.disconnect();
			}
			if (session != null) {
				session.disconnect();
				session = null;
			}
		}
		
		System.out.println("Successfully executed the command");
		// Return from here.
		return resBuf.toString();
	}
	
/*	*//**This method will connect to a remote server defined in framework.properties or supplied via the test: returns true if connected else return false
	 * 
	 * @return boolean 
	 * @throws Exception
	 *//*
	public static boolean connect(String[] serverInfo) {

		String server = serverInfo[0];
		String user=serverInfo[1];
		JSch jsch = null;

		ui = null;
		MyUserInfo myUser;
		channel = null;

		try {
			jsch = new JSch();

			session = jsch.getSession(user, server, 22);

			// username and password will be given via UserInfo interface.
			ui = new MyUserInfo(serverInfo[2]);	
			session.setUserInfo(ui);
			session.connect();

			channel = session.openChannel("sftp");
			channel.connect();
			cmdProc = (ChannelSftp) channel;


		}catch (JSchException e) 
		{
			if (session != null) {
				session.disconnect();
				session = null;
			}
			System.out.println("Unable to create a new SFTP session...."+e);
			return false;
		}catch(Exception e){
			System.out.println(e);
		}

		return true;
	}//connect
	*/
}
