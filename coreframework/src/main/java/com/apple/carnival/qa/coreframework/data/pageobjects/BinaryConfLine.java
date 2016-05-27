package com.apple.carnival.qa.coreframework.data.pageobjects;

public class BinaryConfLine {

	private String ISAppName = null;
	private String instance = null;
	private String port = null;
	private String partition = null;
	private String buildVersion = null;
	private String DC=null;
	

	public String getISAppName() {
		return ISAppName;
	}
	public String getInstance() {
		return instance;
	}
	public String getPort() {
		return port;
	}
	public String getPartition() {
		return partition;
	}
	public String getBuildVersion() {
		return buildVersion;
	}
	public void setIsAppName(String isAppName) {
		this.ISAppName = isAppName;
	}
	public void setInstance(String instance) {
		this.instance = instance;
	}
	public void setPort(String port) {
		this.port = port;
	}
	public void setPartition(String partition) {
		this.partition = partition;
	}
	public void setBuildVersion(String buildVersion) {
		this.buildVersion = buildVersion;
	}
	public String getDC() {
		return DC;
	}
	public void setDC(String dC) {
		DC = dC;
	}
	
}
