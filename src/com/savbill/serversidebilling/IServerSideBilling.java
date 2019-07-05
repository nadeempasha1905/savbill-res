/**
 * 
 */
package com.savbill.serversidebilling;

import java.sql.Connection;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

/**
 * @author Admin
 *
 */
public interface IServerSideBilling {
	
	
	JSONObject generateServerSideBill(JSONObject json, HttpServletRequest request);
	boolean    CompareBillDateAndServerDate(String BillDate);
	boolean    MoveToHHDProcedure(String Rrnumber,String BillDate,String Location,String BillingMode,String UserId);
	boolean    CallBillingProgram(String RrNumber,String BillDate,String InstalType,String Location,String UserID,String bill_contextPath, String recon_contextPath);
	boolean    MoveToHHDMrCodeProcedure(String MeterCode,String BillDate,String Location,String BillingMode,String UserId);
	boolean    CallBillingProgramForMrCode(String meterCode,String BillDate,String Location,String UserID);
	
	JSONObject mrToMrTransfer(JSONObject object);
	

}
