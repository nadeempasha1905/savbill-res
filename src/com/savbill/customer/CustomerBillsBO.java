/**
 * 
 */
package com.savbill.customer;

import java.io.Serializable;

/**
 * @author Nadeem
 *
 */
public class CustomerBillsBO implements Serializable{
	
	private    String 	  cbRRNO ;             
	private    String	  cbBillNo;           
	private    String	  cbBillDt ;           
	private    String	  cbPrevBillNo ;      
	private    String	  cbPrevBillDt  ;  
	private    String	  cbLastUploadDt ;    
	private    String	  cbLastDownloadDt ;  
	private    String	  cbMRIBillNo      ;  
	private    String	  cbMRIBillDt  ;      
	private    String	  cbUser   ;            
	private    String	  cbTMPSTP ;            
	private    String	      cbRRSts  ;           
	private    String	      cbPevSTS ;
	public String getCbRRNO() {
		return cbRRNO;
	}
	public void setCbRRNO(String cbRRNO) {
		this.cbRRNO = cbRRNO;
	}
	public String getCbBillNo() {
		return cbBillNo;
	}
	public void setCbBillNo(String cbBillNo) {
		this.cbBillNo = cbBillNo;
	}
	public String getCbBillDt() {
		return cbBillDt;
	}
	public void setCbBillDt(String cbBillDt) {
		this.cbBillDt = cbBillDt;
	}
	public String getCbPrevBillNo() {
		return cbPrevBillNo;
	}
	public void setCbPrevBillNo(String cbPrevBillNo) {
		this.cbPrevBillNo = cbPrevBillNo;
	}
	public String getCbPrevBillDt() {
		return cbPrevBillDt;
	}
	public void setCbPrevBillDt(String cbPrevBillDt) {
		this.cbPrevBillDt = cbPrevBillDt;
	}
	public String getCbLastUploadDt() {
		return cbLastUploadDt;
	}
	public void setCbLastUploadDt(String cbLastUploadDt) {
		this.cbLastUploadDt = cbLastUploadDt;
	}
	public String getCbLastDownloadDt() {
		return cbLastDownloadDt;
	}
	public void setCbLastDownloadDt(String cbLastDownloadDt) {
		this.cbLastDownloadDt = cbLastDownloadDt;
	}
	public String getCbMRIBillNo() {
		return cbMRIBillNo;
	}
	public void setCbMRIBillNo(String cbMRIBillNo) {
		this.cbMRIBillNo = cbMRIBillNo;
	}
	public String getCbMRIBillDt() {
		return cbMRIBillDt;
	}
	public void setCbMRIBillDt(String cbMRIBillDt) {
		this.cbMRIBillDt = cbMRIBillDt;
	}
	public String getCbUser() {
		return cbUser;
	}
	public void setCbUser(String cbUser) {
		this.cbUser = cbUser;
	}
	public String getCbTMPSTP() {
		return cbTMPSTP;
	}
	public void setCbTMPSTP(String cbTMPSTP) {
		this.cbTMPSTP = cbTMPSTP;
	}
	public String getCbRRSts() {
		return cbRRSts;
	}
	public void setCbRRSts(String cbRRSts) {
		this.cbRRSts = cbRRSts;
	}
	public String getCbPevSTS() {
		return cbPevSTS;
	}
	public void setCbPevSTS(String cbPevSTS) {
		this.cbPevSTS = cbPevSTS;
	}
	
	
	

}
