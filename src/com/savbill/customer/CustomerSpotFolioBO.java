package com.savbill.customer;

import java.io.Serializable;
import java.math.BigDecimal;

public class CustomerSpotFolioBO implements Serializable{
	
	private String spUser;
	private String spTmpstp;
	private String spMrCode;
	private String spMrRdgDay;
	private String spFolioNo;
	private String spRRNo;
	public String getSpUser() {
		return spUser;
	}
	public void setSpUser(String spUser) {
		this.spUser = spUser;
	}
	public String getSpTmpstp() {
		return spTmpstp;
	}
	public void setSpTmpstp(String spTmpstp) {
		this.spTmpstp = spTmpstp;
	}
	public String getSpMrCode() {
		return spMrCode;
	}
	public void setSpMrCode(String spMrCode) {
		this.spMrCode = spMrCode;
	}
	public String getSpMrRdgDay() {
		return spMrRdgDay;
	}
	public void setSpMrRdgDay(String spMrRdgDay) {
		this.spMrRdgDay = spMrRdgDay;
	}
	public String getSpFolioNo() {
		return spFolioNo;
	}
	public void setSpFolioNo(String spFolioNo) {
		this.spFolioNo = spFolioNo;
	}
	public String getSpRRNo() {
		return spRRNo;
	}
	public void setSpRRNo(String spRRNo) {
		this.spRRNo = spRRNo;
	}
	
	

}
