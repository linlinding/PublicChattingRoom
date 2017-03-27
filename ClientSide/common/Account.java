package com.trust.common;

import java.io.File;

public class Account implements java.io.Serializable {//have to use io.serializable when transfer object
	private String userId;
	private String password;
	private String tag;
	private String userIcon;
	
	public String getTag() {
		return tag;
	}
	public void setTag(String tag){
		this.tag = tag;
	}
	
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	
	public String getUserIcon() {
		return userIcon;
	}
	public void setUserIcon(String userIcon) {
		this.userIcon = userIcon;
	}

}
