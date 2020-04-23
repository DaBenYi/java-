package com.sq.api_auto.copy;

public class RegisterDatas {
	String username;
	String password;
	String ostype;
	String devicetoken;
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getOstype() {
		return ostype;
	}
	public void setOstype(String ostype) {
		this.ostype = ostype;
	}
	public String getDevicetoken() {
		return devicetoken;
	}
	public void setDevicetoken(String devicetoken) {
		this.devicetoken = devicetoken;
	}
	@Override
	public String toString() {
		return "username"+username+"password"+password+"ostype"+ostype+"devicetoken"+devicetoken;
	}

}
