package com.example.a_shop;

import cn.bmob.v3.BmobObject;

public class UserBean extends BmobObject {
	private String user;
	private String pass;
	private String phone;

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPass() {
		return pass;
	}

	public void setPass(String pass) {
		this.pass = pass;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}
}
