package com.third;

import cn.bmob.v3.BmobObject;

public class CheckOutBmobBean extends BmobObject {
	private String account;
	private String leaveword;
	private Integer desknum;
	private StringBuffer shop_labuchang;
	private String totalprice;

	public String getTotalprice() {
		return totalprice;
	}

	public void setTotalprice(String totalprice) {
		this.totalprice = totalprice;
	}

	public StringBuffer getShop_labuchang() {
		return shop_labuchang;
	}

	public void setShop_labuchang(StringBuffer shop_labuchang) {
		this.shop_labuchang = shop_labuchang;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public String getLeaveword() {
		return leaveword;
	}

	public void setLeaveword(String leaveword) {
		this.leaveword = leaveword;
	}

	public Integer getDesknum() {
		return desknum;
	}

	public void setDesknum(Integer desknum) {
		this.desknum = desknum;
	}
}
