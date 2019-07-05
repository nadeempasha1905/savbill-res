/**
 * 
 */
package com.savbill.customer;

import java.io.Serializable;

/**
 * @author Admin
 *
 */
public class CustomerDocumentBO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	public String cdocDocSubmDt;
	public String cdocUser;
	public String cdocTmpstp;
	public String cdocDocTyp;
	public String cdocSlNo;
	public String docDescription;
	public String cdocRrno;
	public String rowID;
	public String rowNUM;
	
	
	public String getRowID() {
		return rowID;
	}
	public void setRowID(String rowID) {
		this.rowID = rowID;
	}
	public String getCdocDocSubmDt() {
		return cdocDocSubmDt;
	}
	public void setCdocDocSubmDt(String cdocDocSubmDt) {
		this.cdocDocSubmDt = cdocDocSubmDt;
	}
	public String getCdocUser() {
		return cdocUser;
	}
	public void setCdocUser(String cdocUser) {
		this.cdocUser = cdocUser;
	}
	public String getCdocTmpstp() {
		return cdocTmpstp;
	}
	public void setCdocTmpstp(String cdocTmpstp) {
		this.cdocTmpstp = cdocTmpstp;
	}
	public String getCdocDocTyp() {
		return cdocDocTyp;
	}
	public void setCdocDocTyp(String cdocDocTyp) {
		this.cdocDocTyp = cdocDocTyp;
	}
	public String getCdocSlNo() {
		return cdocSlNo;
	}
	public void setCdocSlNo(String cdocSlNo) {
		this.cdocSlNo = cdocSlNo;
	}
	public String getDocDescription() {
		return docDescription;
	}
	public void setDocDescription(String docDescription) {
		this.docDescription = docDescription;
	}
	public String getCdocRrno() {
		return cdocRrno;
	}
	public void setCdocRrno(String cdocRrno) {
		this.cdocRrno = cdocRrno;
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
