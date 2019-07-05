/**
 * 
 */
package com.savbill.customer;

import java.io.Serializable;

/**
 * @author Admin
 *
 */
public class CustomerIntimationsBO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	private String ciIntmTyp;
	private String codeDescription;
	private String ciRrNo;
	private String ciIntmDt;
	private String ciLetrNo;
	private String ciUser;
	private String ciTmpstp;
	private String ciRmks;
	private String rowID;
	private String rowNUM;
	
	
	public String getRowID() {
		return rowID;
	}
	public void setRowID(String rowID) {
		this.rowID = rowID;
	}
	public String getCiIntmTyp() {
		return ciIntmTyp;
	}
	public void setCiIntmTyp(String ciIntmTyp) {
		this.ciIntmTyp = ciIntmTyp;
	}
	public String getCodeDescription() {
		return codeDescription;
	}
	public void setCodeDescription(String codeDescription) {
		this.codeDescription = codeDescription;
	}
	public String getCiRrNo() {
		return ciRrNo;
	}
	public void setCiRrNo(String ciRrNo) {
		this.ciRrNo = ciRrNo;
	}
	public String getCiIntmDt() {
		return ciIntmDt;
	}
	public void setCiIntmDt(String ciIntmDt) {
		this.ciIntmDt = ciIntmDt;
	}
	public String getCiLetrNo() {
		return ciLetrNo;
	}
	public void setCiLetrNo(String ciLetrNo) {
		this.ciLetrNo = ciLetrNo;
	}
	public String getCiUser() {
		return ciUser;
	}
	public void setCiUser(String ciUser) {
		this.ciUser = ciUser;
	}
	public String getCiTmpstp() {
		return ciTmpstp;
	}
	public void setCiTmpstp(String ciTmpstp) {
		this.ciTmpstp = ciTmpstp;
	}
	public String getCiRmks() {
		return ciRmks;
	}
	public void setCiRmks(String ciRmks) {
		this.ciRmks = ciRmks;
	}
	public static long getSerialversionuid() {
		return serialVersionUID;
	}
	public String getRowNUM() {
		return rowNUM;
	}
	public void setRowNUM(String rowNUM) {
		this.rowNUM = rowNUM;
	}
	
	

}
