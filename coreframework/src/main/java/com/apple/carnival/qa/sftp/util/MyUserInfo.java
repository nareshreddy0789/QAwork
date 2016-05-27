package com.apple.carnival.qa.sftp.util;

import com.jcraft.jsch.UserInfo;

/**
 * @author harisha
 * 
 */
public class MyUserInfo implements UserInfo {

	/**
	 * This field - password is used for _.
	 */
	private String	password;

	/**
	 * 
	 */
	public MyUserInfo() {
		this.password = "";
	}

	/**
	 * @param pwd
	 */
	public MyUserInfo(String pwd) {
		this.password = pwd;
	}



	/*
	 *
	 */
	@Override
	public String getPassword() {
		return this.password;
	}

	/*
	 *
	 */
	@Override
	public boolean promptPassphrase(String message) {
		return true;
	}

	/*
	 * 
	 */
	@Override
	public boolean promptPassword(String message) {
		return true;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.jcraft.jsch.UserInfo#promptYesNo(java.lang.String)
	 */
	@Override
	public boolean promptYesNo(String str) {
		return true;
	}

	@Override
	public String getPassphrase() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void showMessage(String message) {
		// TODO Auto-generated method stub
		
	}



}
