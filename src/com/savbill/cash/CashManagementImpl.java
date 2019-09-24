/**
 * 
 */
package com.savbill.cash;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.savbill.database.DBManagerIMPL;
import com.savbill.database.DBManagerResourceRelease;
import com.savbill.database.DBQueries;

import oracle.jdbc.OracleTypes;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author NAdeem
 *
 */
public class CashManagementImpl implements ICashManagement {

	// database connection init
	static DBManagerIMPL databaseObj = new DBManagerIMPL();
	static Connection dbConnection;
	
	@Override
	public JSONObject getreceiptDetails(JSONObject object) {
		
		CallableStatement getreceiptCS = null;
		ResultSet getreceiptRS = null;
		
		JSONObject AckObj = new JSONObject();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		
		String conn_type = (String) object.get("conn_type").toString().trim();
		
		System.out.println("input : "+object);
		
		try {
				if(conn_type.equals("LT") || conn_type == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT") || conn_type == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				getreceiptCS = dbConnection.prepareCall(DBQueries.GET_RCPT_DETIALS);
				getreceiptCS.setString(1,object.getString("type"));
				getreceiptCS.setString(2,object.getString("rr_no"));
				getreceiptCS.setString(3,object.getString("receipt_date"));
				getreceiptCS.setString(4,object.getString("receipt_number"));
				getreceiptCS.setString(5,object.getString("counter_number"));
				getreceiptCS.setString(6,object.getString("purpose"));
				getreceiptCS.setString(7,object.getString("cheque_dd_date"));
				getreceiptCS.setString(8,object.getString("cheque_dd_number"));
				getreceiptCS.setString(9,object.getString("bank_name"));
				getreceiptCS.setString(10,object.getString("limit"));
				getreceiptCS.registerOutParameter(11, OracleTypes.CURSOR);
				getreceiptCS.executeUpdate();
				getreceiptRS = (ResultSet) getreceiptCS.getObject(11);
				
				if(object.getString("type").equalsIgnoreCase("CNT")){
					if(getreceiptRS.next()){
						AckObj.put("count", getreceiptRS.getString("cnt"));
						AckObj.put("status", "success");
					}else{
						AckObj.put("status", "error");
						AckObj.put("message", "Not able to find count.");
					}
				}else{
					while(getreceiptRS.next()){
						
						json.put("r", getreceiptRS.getString("r"));
						json.put("rcpt_no", getreceiptRS.getString("rcpt_no"));
						json.put("rcpt_dt", getreceiptRS.getString("rcpt_dt"));
						json.put("counter_no", getreceiptRS.getString("counter_no"));
						json.put("purpose", getreceiptRS.getString("purpose"));
						json.put("purpose_description", getreceiptRS.getString("purpose_description"));
						json.put("rr_no", getreceiptRS.getString("rr_no"));
						json.put("new_rr_no", getreceiptRS.getString("new_rr_no"));
						json.put("payee_name", getreceiptRS.getString("payee_name"));
						json.put("amt_paid", getreceiptRS.getString("amt_paid"));
						json.put("pymnt_mode", getreceiptRS.getString("pymnt_mode"));
						json.put("irp_pymnt_mode_description", getreceiptRS.getString("irp_pymnt_mode_description"));
						json.put("chq_dd_no", getreceiptRS.getString("chq_dd_no"));
						json.put("chq_dd_dt", getreceiptRS.getString("chq_dd_dt"));
						json.put("drawn_bank", getreceiptRS.getString("drawn_bank"));
						json.put("cancel_flg", getreceiptRS.getString("cancel_flg"));
						json.put("cancel_flg_description", getreceiptRS.getString("cancel_flg_description"));
						json.put("remarks", getreceiptRS.getString("remarks"));
						json.put("posted_sts", getreceiptRS.getString("posted_sts"));
						json.put("posted_sts_description", getreceiptRS.getString("posted_sts_description"));
						json.put("status", getreceiptRS.getString("status"));
						json.put("status_description", getreceiptRS.getString("status_description"));
						json.put("ldgr_no", getreceiptRS.getString("ldgr_no"));
						json.put("folio_no", getreceiptRS.getString("folio_no"));
						json.put("rcpt_typ", getreceiptRS.getString("rcpt_typ"));
						json.put("rrno_applno_flg", getreceiptRS.getString("rrno_applno_flg"));
						json.put("printed_flg", getreceiptRS.getString("printed_flg"));
						json.put("printed_flg_description", getreceiptRS.getString("printed_flg_description"));
						json.put("less_amt_realised", getreceiptRS.getString("less_amt_realised"));
						json.put("less_amt_posted_sts", getreceiptRS.getString("less_amt_posted_sts"));
						json.put("userid", getreceiptRS.getString("userid"));
						json.put("tmpstp", getreceiptRS.getString("tmpstp"));

						array.add(json);
					}
					if(array.isEmpty()){
						//no tasks for user
						AckObj.put("status", "success");
						AckObj.put("message", "No Records Found");
					}else{
						
						AckObj.put("status", "success");
						AckObj.put("receipt_details", array);
					}
			 	}
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptRS, getreceiptCS);
				
			}
		
		return AckObj;
				
	}

	@Override
	public JSONObject getreceiptPostedDetails(JSONObject object) {
		
		CallableStatement getreceiptCS = null;
		ResultSet getreceiptRS = null;
		
		JSONObject AckObj = new JSONObject();
		JSONArray array = new JSONArray();
		System.out.println(object);
		
		try {
				//dbConnection = DatabaseImpl.GetLTSQLConnection();
				dbConnection=databaseObj.getDatabaseConnection();
				
				//1.check for user ID
				getreceiptCS = dbConnection.prepareCall(DBQueries.GET_RCPT_POSTED_DETIALS);
				getreceiptCS.setString(1,object.getString("receipt_date"));
				getreceiptCS.setString(2,object.getString("receipt_number"));
				getreceiptCS.setString(3,object.getString("counter_number"));
				getreceiptCS.registerOutParameter(4, OracleTypes.CURSOR);
				getreceiptCS.executeUpdate();
				getreceiptRS = (ResultSet) getreceiptCS.getObject(4);
				
				while(getreceiptRS.next()){
					
					JSONObject json = new JSONObject();
					
					json.put("row_num", getreceiptRS.getString("row_num"));
					json.put("rr_no", getreceiptRS.getString("rr_no"));
					json.put("receipt_number", getreceiptRS.getString("rcpt_no"));
					json.put("receipt_date", getreceiptRS.getString("rcpt_dt"));
					json.put("counter_number", getreceiptRS.getString("countr_no"));
					json.put("countr_name", getreceiptRS.getString("countr_name"));
					json.put("purpose_id", getreceiptRS.getString("purpose"));
					json.put("purpose_description", getreceiptRS.getString("purpose_description"));
					json.put("receipt_type", getreceiptRS.getString("rcpt_typ"));
					json.put("payment_mode", getreceiptRS.getString("pymnt_mode"));
					json.put("amount_paid", getreceiptRS.getString("amt_paid"));
					json.put("payee_name",getreceiptRS.getString("payee_name"));
					json.put("cancel_flag", getreceiptRS.getString("cancel_flg"));
					json.put("remarks", getreceiptRS.getString("remarks"));
					json.put("posted_sts", getreceiptRS.getString("posted_sts"));
					json.put("printed_flag", getreceiptRS.getString("printed_flg"));
					json.put("rrno_applno_flg",getreceiptRS.getString("rrno_applno_flg"));
					json.put("cheque_dd_number",getreceiptRS.getString("chq_dd_no"));
					json.put("cheque_dd_date",getreceiptRS.getString("chq_dd_dt"));
					json.put("drawn_bank", getreceiptRS.getString("drawn_bank"));
					json.put("less_amt_realised", getreceiptRS.getString("less_amt_realised"));
					json.put("less_amt_posted_sts", getreceiptRS.getString("less_amt_posted_sts"));
					json.put("user_id", getreceiptRS.getString("usrid"));
					json.put("time_stamp", getreceiptRS.getString("tmpstp"));
					array.add(json);
				}
				if(array.isEmpty()){
					//no tasks for user
					AckObj.put("status", "success");
					AckObj.put("message", "No Records Found");
				}else{
					
					AckObj.put("status", "success");
					AckObj.put("receipt_posted_details", array);
				}
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptRS, getreceiptCS);
			}
		
		return AckObj;
				
	}

	@Override
	public JSONObject getListOfPayments(JSONObject object) {
		
		CallableStatement getreceiptCS = null;
		ResultSet getreceiptRS = null;
		
		JSONObject AckObj = new JSONObject();
		JSONArray array = new JSONArray();
		
		String conn_type = (String) object.get("conn_type");
		
		try {
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				getreceiptCS = dbConnection.prepareCall(DBQueries.GET_LIST_OF_PAYMENTS);
				
				getreceiptCS.setString(1,object.getString("rr_number"));
				getreceiptCS.setString(2,object.getString("no_of_months"));
				getreceiptCS.setString(3,object.getString("from_date"));
				getreceiptCS.setString(4,object.getString("to_date"));
				getreceiptCS.registerOutParameter(5, OracleTypes.CURSOR);
				getreceiptCS.executeUpdate();
				getreceiptRS = (ResultSet) getreceiptCS.getObject(5);
				
				while(getreceiptRS.next()){
					
					JSONObject json = new JSONObject();
					
					json.put("row_num", getreceiptRS.getString("row_num"));
					json.put("rr_no", getreceiptRS.getString("rr_no"));
					json.put("customer_name", getreceiptRS.getString("customer_name"));
					json.put("premise_addr1", getreceiptRS.getString("premise_addr1"));
					json.put("rcpt_no", getreceiptRS.getString("rcpt_no"));
					json.put("rcpt_dt", getreceiptRS.getString("rcpt_dt"));
					json.put("countr_no", getreceiptRS.getString("countr_no"));
					json.put("pymnt_purpose", getreceiptRS.getString("pymnt_purpose"));
					json.put("pymnt_mode", getreceiptRS.getString("pymnt_mode"));
					json.put("amt_paid",getreceiptRS.getString("amt_paid"));
					json.put("chq_dd_no",getreceiptRS.getString("chq_dd_no"));
					json.put("chq_dd_dt",getreceiptRS.getString("chq_dd_dt"));
					json.put("drawn_bank", getreceiptRS.getString("drawn_bank"));
					json.put("dishonour_dt", getreceiptRS.getString("dishonour_dt"));
					array.add(json);
				}
				if(array.isEmpty()){
					//no tasks for user
					AckObj.put("status", "success");
					AckObj.put("message", "No Records Found");
				}else{
					
					AckObj.put("status", "success");
					AckObj.put("payments_list", array);
				}
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptRS, getreceiptCS);
			}
		
		return AckObj;
				
	}

	@Override
	public JSONObject getRecieptsForPost(JSONObject object) {
		
		CallableStatement getreceiptCS = null;
		ResultSet getreceiptRS = null;
		
		JSONObject AckObj = new JSONObject();
		JSONArray array = new JSONArray();
		
		
		try {
				//dbConnection = DatabaseImpl.GetLTSQLConnection();
				dbConnection=databaseObj.getDatabaseConnection();
				
				//1.check for user ID
				getreceiptCS = dbConnection.prepareCall(DBQueries.GET_RCPT_DETIALS_FOR_POST);
				getreceiptCS.setString(1,object.getString("location_code"));
				getreceiptCS.setString(2,object.getString("receipt_date"));
				getreceiptCS.setString(3,object.getString("counter_number"));
				getreceiptCS.registerOutParameter(4, OracleTypes.CURSOR);
				getreceiptCS.executeUpdate();
				getreceiptRS = (ResultSet) getreceiptCS.getObject(4);
				
				while(getreceiptRS.next()){
					
					JSONObject json = new JSONObject();
					
					json.put("row_num", getreceiptRS.getString("row_num"));
					json.put("rr_no", getreceiptRS.getString("rr_no"));
					json.put("rcpt_no", getreceiptRS.getString("rcpt_no"));
					json.put("rcpt_dt", getreceiptRS.getString("rcpt_dt"));
					json.put("countr_no", getreceiptRS.getString("countr_no"));
					json.put("countr_name", getreceiptRS.getString("countr_name"));
					json.put("pymnt_mode", getreceiptRS.getString("pymnt_mode"));
					json.put("pymnt_mode_description", getreceiptRS.getString("pymnt_mode_description"));
					json.put("purpose", getreceiptRS.getString("purpose"));
					json.put("purpose_descr", getreceiptRS.getString("purpose_descr"));
					json.put("rcpt_typ", getreceiptRS.getString("rcpt_typ"));
					json.put("payee_name", getreceiptRS.getString("payee_name"));
					json.put("cancel_flg", getreceiptRS.getString("cancel_flg"));
					json.put("remarks", getreceiptRS.getString("remarks"));
					json.put("posted_sts", getreceiptRS.getString("posted_sts"));
					json.put("printed_flg", getreceiptRS.getString("printed_flg"));
					json.put("rrno_applno_flg",getreceiptRS.getString("rrno_applno_flg"));
					json.put("chq_dd_no",getreceiptRS.getString("chq_dd_no"));
					json.put("chq_dd_dt",getreceiptRS.getString("chq_dd_dt"));
					json.put("drawn_bank", getreceiptRS.getString("drawn_bank"));
					json.put("less_amt_realised", getreceiptRS.getString("less_amt_realised"));
					json.put("less_amt_posted_sts", getreceiptRS.getString("less_amt_posted_sts"));
					json.put("userid", getreceiptRS.getString("userid"));
					json.put("tmpstp", getreceiptRS.getString("tmpstp"));
					json.put("ldgr_no", getreceiptRS.getString("ldgr_no"));
					json.put("folio_no",getreceiptRS.getString("folio_no"));
					json.put("status",getreceiptRS.getString("status"));
					json.put("description",getreceiptRS.getString("description"));
					json.put("amt_paid", getreceiptRS.getString("amt_paid"));
					json.put("purpose_key", getreceiptRS.getString("purpose_key"));
					json.put("new_purpose_key", getreceiptRS.getString("new_purpose_key"));
					
					array.add(json);
				}
				if(array.isEmpty()){
					//no tasks for user
					AckObj.put("status", "success");
					AckObj.put("message", "No Records Found");
				}else{
					
					AckObj.put("status", "success");
					AckObj.put("receipts_list", array);
				}
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptRS, getreceiptCS);
			}
		
		return AckObj;
				
	}

	@Override
	public JSONObject getbilldetailsinreceiptgen(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jsonResponse = new JSONObject();
		
		String conn_type = (String) object.get("conn_type");
		
		try {
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
			
			if(!object.isEmpty()){
				
				String rrno  = (String)object.get("rrno");
				
					
				
				
				if(dbConnection != null){
					String qry = "select   cd_rr_no, ivrs_id,CD_CONSMR_NAME,round(nvl(cBM_BILL_AMT,0) - nvl(cBM_AMT_PAID,0) -  nvl(cBM_CREDIT_AMT,0))  amount_paid  "
							+ "  from  CUST_DESCR, CUST_BILL , CUSTBILL_MASTER , IVRS_CUST_MASTR  "
							+ "  where  cd_rr_no = ivrs_bcn_rr_no and cd_rr_no = cb_rr_no and cb_rr_no = cbm_rr_no and cb_bill_dt = cbm_bill_dt and  "
							+ "  ( cb_rr_no = ( case when substr('"+rrno.toUpperCase()+"',1,7) = substr('"+rrno.toUpperCase()+"',8,7) then substr('"+rrno.toUpperCase()+"',8) else '"+rrno.toUpperCase()+"' end )  " + 
							" or ivrs_id = ( case when substr('"+rrno.toUpperCase()+"',1,7) = substr('"+rrno.toUpperCase()+"',8,7) then substr('"+rrno.toUpperCase()+"',8) else '"+rrno.toUpperCase()+"' end ) )  ";
					System.out.println(qry);
					ps = dbConnection.prepareStatement(qry) ; 
					
					rs = ps.executeQuery();
					if (rs.next()) {
						jsonResponse.put("consumer_name", rs.getString("CD_CONSMR_NAME"));
						jsonResponse.put("amount_paid", rs.getString("amount_paid"));
						jsonResponse.put("cd_rr_no", rs.getString("cd_rr_no"));
						jsonResponse.put("ivrs_id", rs.getString("ivrs_id"));
						jsonResponse.put("status", "success");
						jsonResponse.put("message","Bill Details Arrived !!!");
					}else {
						jsonResponse.put("status", "error");
						jsonResponse.put("message"," No Bill Details Found !!!");
					}
				}else {
					jsonResponse.put("status", "error");
					jsonResponse.put("message", "Database Query / Runtime Error .");
				}
			
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Invalid Inputs / No Agent Detials");
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown " + e);
			jsonResponse.put("status", "error");
			jsonResponse.put("message", "Database Query / Runtime Error .");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonResponse.put("status", "error");
			jsonResponse.put("message", " Database Query / Runtime Error.");
		} finally {
			DBManagerResourceRelease.close(rs);
			DBManagerResourceRelease.close(ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getreceiptsummarydetails(JSONObject object) {
		// TODO Auto-generated method stub
		CallableStatement getreceiptCS = null;
		ResultSet getreceiptRS = null;
		
		JSONObject AckObj = new JSONObject();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		
		String conn_type = (String) object.get("conn_type");
		
		try {
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
					
				
				getreceiptCS = dbConnection.prepareCall("{call PKG_CASH.GET_CASH_COUNTER_SUMMARY(?,?,?)}");
				getreceiptCS.setString(2,object.getString("receipt_date"));
				getreceiptCS.setString(3,object.getString("counter_number"));
				getreceiptCS.registerOutParameter(1, OracleTypes.CURSOR);
				getreceiptCS.executeUpdate();
				getreceiptRS = (ResultSet) getreceiptCS.getObject(1);
				
				if(getreceiptRS.next()) {
					
					json.put("first_receipt_number", getreceiptRS.getString("FIRST_RCPT_NUMBER"));
					json.put("last_receipt_number", getreceiptRS.getString("LAST_RCPT_NUMBER"));
					json.put("receipt_numbers", getreceiptRS.getString("RCPT_NUMBERS"));
					json.put("cash_receipts", getreceiptRS.getString("CASH_RCPTS"));
					json.put("cash_amount", getreceiptRS.getString("CASH_AMOUNT"));
					json.put("cheque_receipts", getreceiptRS.getString("CHQ_DD_RCPTS"));
					json.put("cheque_amount", getreceiptRS.getString("CHQ_DD_AMOUNT"));
					json.put("cancelled_recipts", getreceiptRS.getString("CANCELLED_RCPTS"));
					json.put("cancelled_amount", getreceiptRS.getString("CANCELLED_AMOUNT"));
					json.put("other_recipts", getreceiptRS.getString("OTH_RCPTS"));
					json.put("other_amount", getreceiptRS.getString("OTH_AMOUNT"));
					
					array.add(json);
				}
				
				
				if(array.isEmpty()) {
					AckObj.put("status", "error");
					AckObj.put("message", "No Summary Details !!!");
					AckObj.put("summarydetails", array);
				}else{
					AckObj.put("status", "success");
					AckObj.put("message", "Summary Details Arrived !!!");
					AckObj.put("summarydetails", array);
				}
				
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptCS);
				DBManagerResourceRelease.close(getreceiptRS);
			}
		
		return AckObj;
	}
	
	@Override
	public JSONObject getreceiptsummarydetailshrt(JSONObject object) {
		// TODO Auto-generated method stub
		CallableStatement getreceiptCS = null;
		ResultSet getreceiptRS = null;
		
		JSONObject AckObj = new JSONObject();
		JSONObject json = new JSONObject();
		JSONArray array = new JSONArray();
		
		String conn_type = (String) object.get("conn_type");
		
		try {
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
					
				
				getreceiptCS = dbConnection.prepareCall("{call PKG_CASH.GET_CASH_COUNTER_SUMMARY_HRT(?,?,?)}");
				getreceiptCS.setString(2,object.getString("receipt_date"));
				getreceiptCS.setString(3,object.getString("counter_number"));
				getreceiptCS.registerOutParameter(1, OracleTypes.CURSOR);
				getreceiptCS.executeUpdate();
				getreceiptRS = (ResultSet) getreceiptCS.getObject(1);
				
				if(getreceiptRS.next()) {
					
					json.put("first_receipt_number", getreceiptRS.getString("FIRST_RCPT_NUMBER"));
					json.put("last_receipt_number", getreceiptRS.getString("LAST_RCPT_NUMBER"));
					json.put("receipt_numbers", getreceiptRS.getString("RCPT_NUMBERS"));
					json.put("cash_receipts", getreceiptRS.getString("CASH_RCPTS"));
					json.put("cash_amount", getreceiptRS.getString("CASH_AMOUNT"));
					json.put("cheque_receipts", getreceiptRS.getString("CHQ_DD_RCPTS"));
					json.put("cheque_amount", getreceiptRS.getString("CHQ_DD_AMOUNT"));
					json.put("cancelled_recipts", getreceiptRS.getString("CANCELLED_RCPTS"));
					json.put("cancelled_amount", getreceiptRS.getString("CANCELLED_AMOUNT"));
					json.put("other_recipts", getreceiptRS.getString("OTH_RCPTS"));
					json.put("other_amount", getreceiptRS.getString("OTH_AMOUNT"));
					
					//pending uploads
					json.put("not_uploaded_cash_receipts", getreceiptRS.getString("NOT_UPLOADED_CASH_RCPTS"));
					json.put("not_uploaded_cash_amount", getreceiptRS.getString("NOT_UPLOADED_CASH_AMOUNT"));
					json.put("not_uploaded_cheque_receipts", getreceiptRS.getString("NOT_UPLOADED_CHQ_DD_RCPTS"));
					json.put("not_uploaded_cheque_amount", getreceiptRS.getString("NOT_UPLOADED_CHQ_DD_AMOUNT"));
					json.put("not_uploaded_cancelled_recipts", getreceiptRS.getString("NOT_UPLOADED_CANCELLED_RCPTS"));
					json.put("not_uploaded_cancelled_amount", getreceiptRS.getString("NOT_UPLOADED_CANCELLED_AMOUNT"));
					json.put("not_uploaded_other_recipts", getreceiptRS.getString("NOT_UPLOADED_OTH_RCPTS"));
					json.put("not_uploaded_other_amount", getreceiptRS.getString("NOT_UPLOADED_OTH_AMOUNT"));
					
					array.add(json);
				}
				
				
				if(array.isEmpty()) {
					AckObj.put("status", "error");
					AckObj.put("message", "No Summary Details !!!");
					AckObj.put("summarydetails", array);
				}else{
					AckObj.put("status", "success");
					AckObj.put("message", "Summary Details Arrived !!!");
					AckObj.put("summarydetails", array);
				}
				
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptCS);
				DBManagerResourceRelease.close(getreceiptRS);
			}
		
		return AckObj;
	}

	@Override
	public JSONObject savereceiptdetails(JSONObject object, HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		CallableStatement accountsCS = null;
		JSONObject jsonResponse  = new JSONObject();
		JSONArray jsonList = new JSONArray();
		PreparedStatement ps	=	null;
		ResultSet rs			=	null;
		boolean continue_execution = false;
		String TRANSACTION_ID = "";
		
		
		
		try {
			
			
			String location_code = (String) object.get("location");
			String userid = (String) object.get("user_id");
			String conn_type = (String) object.get("conn_type");
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			if(!object.isEmpty()){
				
					
				
				if(dbConnection != null){
					
					String transaction_fetch = "SELECT TO_CHAR(LPAD(NVL(MAX(TO_NUMBER(IRPT_TRANSACTION_ID)),0)+1,10,'0'))  TRANS_ID FROM IRP_TRANSACTIONS";
					
					ps = dbConnection.prepareStatement(transaction_fetch);
					rs = ps.executeQuery();
					
					if(rs.next()) {
						TRANSACTION_ID = rs.getString("TRANS_ID");
					}
					
					
					if(object.getString("payment_mode").equals("CHQ")) {
						
						accountsCS=dbConnection.prepareCall("{call PKG_CASH.INSERT_CHEQUE_MASTER(?,?,?,?,?,?,?,?)}");
						
						accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
						accountsCS.setString(2, ConvertIFNullToString(object.getString("cheque_number")));
						accountsCS.setString(3, ConvertIFNullToString(object.getString("cheque_date")));
						accountsCS.setString(4, ConvertIFNullToString(object.getString("drawee_bank")));
						accountsCS.setString(5, ConvertIFNullToString(object.getString("receipt_date")));
						accountsCS.setString(6, ConvertIFNullToString(object.getString("payment_mode")));
						accountsCS.setString(7, ConvertIFNullToString(object.getString("cheque_amount")));
						accountsCS.setString(8, ConvertIFNullToString(object.getString("user_id")));
						
						accountsCS.executeUpdate();
						rs = (ResultSet) accountsCS.getObject(1);
						
						if(rs.next()){
							
							String resp = rs.getString("RESP");
							
							System.out.println("resp : "+resp);
							
							if(resp.equalsIgnoreCase("SUCCESS")){
								continue_execution = true;
								System.out.println("111111111continue_execution : "+continue_execution);
							}else {
								continue_execution = false;
								System.out.println("22222222222continue_execution : "+continue_execution);
							}
						}
						
					}else {
						
						continue_execution = true;
					}
					
					System.out.println("continue_execution : "+continue_execution);
					if(continue_execution) {
						
						JSONArray receipt_details_Array = object.getJSONArray("receipt_details");
						
						if(!receipt_details_Array.isEmpty()){
							
							accountsCS=dbConnection.prepareCall("{call PKG_CASH.INSERT_RCPT_DETIALS(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
							
							for(int i = 0 ; i<receipt_details_Array.size();i++){
								
								JSONObject receipt = receipt_details_Array.getJSONObject(i);
								
								accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
								accountsCS.setString(2, TRANSACTION_ID);
								accountsCS.setString(3, ConvertIFNullToString(object.getString("receipt_date")));
								accountsCS.setString(4, ConvertIFNullToString(object.getString("cash_counter")));
								accountsCS.setString(5, ConvertIFNullToString(object.getString("payment_purpose")));
								accountsCS.setString(6, ConvertIFNullToString(receipt.getString("rrno")));
								accountsCS.setString(7, ConvertIFNullToString(object.getString("payment_mode")));
								accountsCS.setString(8, ConvertIFNullToString(receipt.getString("amount")));
								accountsCS.setString(9, ConvertIFNullToString(receipt.getString("name")));
								accountsCS.setString(10, ConvertIFNullToString(object.getString("remarks")));
								accountsCS.setString(11, ConvertIFNullToString(object.getString("cheque_number")));
								accountsCS.setString(12, ConvertIFNullToString(object.getString("cheque_date")));
								accountsCS.setString(13, ConvertIFNullToString(object.getString("drawee_bank")));
								accountsCS.setString(14, ConvertIFNullToString(object.getString("user_id")));
								
								System.out.println(accountsCS);
								
								accountsCS.executeUpdate();
								
								
								rs = (ResultSet) accountsCS.getObject(1);
								
								System.out.println("outside...................");
								System.out.println("rs : "+rs);
								
								if(rs.next()){
									
									JSONObject json_response = new JSONObject();
									
									String resp = rs.getString("RESP");
									
									System.out.println("resp : "+resp);
									
									if(resp.equalsIgnoreCase("success")){
										
										json_response.put("status", "success");
										json_response.put("message", "Receipt Generated Successfully");
										
									}else if(resp.indexOf("fail") > 0){
										json_response.put("status", "error");
										json_response.put("message", "Receipt not generated");
									}
									jsonList.add(json_response);
								}
							}
							if(!jsonList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "Receipt Generated Successfully");
								
								JSONObject temp_object = new JSONObject();
								temp_object.put("TRANSACTION_ID", TRANSACTION_ID);
								temp_object.put("filename", "APMC_RCPT");
								temp_object.put("exporttype", "PDF");
								temp_object.put("report_title", "Receipt Print");

								//JSONObject fileload_json = printObj.downloadbilltolocalpath(temp_object, request, response);
								//System.out.println(fileload_json);
								
								//jsonResponse.put("filepath", (String)fileload_json.get("filepath"));
								
							}else{
								jsonResponse.put("status", "error");
								jsonResponse.put("message", "Receipt not generated !");
							}
					}
					
					}else{
						jsonResponse.put("status", "error");
						jsonResponse.put("message", "No Receipts Found To Generate !");
					}
				}	
			}else{	
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Invalid Input !");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error Occured In Receipt Generation .");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally 
		{
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}
		return jsonResponse;
	}
	
	
	public static String ConvertIFNullToString(String value){
		
		return ((value == null || value == "") ? "" : value);
		
	}

	@Override
	public JSONObject dorecepitposting(JSONObject object, HttpServletRequest request) {
		// TODO Auto-generated method stub
		CallableStatement getreceiptCS = null;
		ResultSet getreceiptRS = null;
		
		JSONObject AckObj = new JSONObject();
		JSONArray array = new JSONArray();
		
		String conn_type = (String) object.get("conn_type");
		
		try {
				//dbConnection = DatabaseImpl.GetLTSQLConnection();
				//dbConn=dbObject.getDatabaseConnection();
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
				
				//1.check for user ID
				getreceiptCS = dbConnection.prepareCall("{call NEW_POST_RCPT(?,?,?,?,?)}");
				getreceiptCS.setString(1,object.getString("location_code"));
				getreceiptCS.setString(2,object.getString("receipt_date"));
				getreceiptCS.setString(3,object.getString("counter_number"));
				getreceiptCS.setString(4,object.getString("user_id"));
				getreceiptCS.registerOutParameter(5, OracleTypes.INTEGER);
				getreceiptCS.executeUpdate();
				
				System.out.println( getreceiptCS.getInt(5));
				
				int posted_count = getreceiptCS.getInt(5);
				
				if(posted_count > 0 ) {
					
					
					if(CallReconProgram(object.getString("receipt_date"),
										object.getString("location_code"),
										object.getString("user_id"),
										request.getServletContext().getRealPath("ReceiptGenerationImpl.java"))) {
						
						DBManagerResourceRelease.close(getreceiptCS);
						DBManagerResourceRelease.close(getreceiptRS);
				
					getreceiptCS = dbConnection.prepareCall("{call PKG_CASH.GET_RCPT_DETIALS_FOR_POST(?,?,?,?)}");
					getreceiptCS.setString(1,object.getString("location_code"));
					getreceiptCS.setString(2,object.getString("receipt_date"));
					getreceiptCS.setString(3,object.getString("counter_number"));
					getreceiptCS.registerOutParameter(4, OracleTypes.CURSOR);
					getreceiptCS.executeUpdate();
					getreceiptRS = (ResultSet) getreceiptCS.getObject(4);
					
					while(getreceiptRS.next()){
						
						JSONObject json = new JSONObject();
						
						json.put("row_num", getreceiptRS.getString("row_num"));
						json.put("rr_no", getreceiptRS.getString("rr_no"));
						json.put("rcpt_no", getreceiptRS.getString("rcpt_no"));
						json.put("rcpt_dt", getreceiptRS.getString("rcpt_dt"));
						json.put("countr_no", getreceiptRS.getString("countr_no"));
						json.put("countr_name", getreceiptRS.getString("countr_name"));
						json.put("pymnt_mode", getreceiptRS.getString("pymnt_mode"));
						json.put("pymnt_mode_description", getreceiptRS.getString("pymnt_mode_description"));
						json.put("purpose", getreceiptRS.getString("purpose"));
						json.put("purpose_descr", getreceiptRS.getString("purpose_descr"));
						json.put("rcpt_typ", getreceiptRS.getString("rcpt_typ"));
						json.put("payee_name", getreceiptRS.getString("payee_name"));
						json.put("cancel_flg", getreceiptRS.getString("cancel_flg"));
						json.put("remarks", getreceiptRS.getString("remarks"));
						json.put("posted_sts", getreceiptRS.getString("posted_sts"));
						json.put("printed_flg", getreceiptRS.getString("printed_flg"));
						json.put("rrno_applno_flg",getreceiptRS.getString("rrno_applno_flg"));
						json.put("chq_dd_no",getreceiptRS.getString("chq_dd_no"));
						json.put("chq_dd_dt",getreceiptRS.getString("chq_dd_dt"));
						json.put("drawn_bank", getreceiptRS.getString("drawn_bank"));
						json.put("less_amt_realised", getreceiptRS.getString("less_amt_realised"));
						json.put("less_amt_posted_sts", getreceiptRS.getString("less_amt_posted_sts"));
						json.put("userid", getreceiptRS.getString("userid"));
						json.put("tmpstp", getreceiptRS.getString("tmpstp"));
						json.put("ldgr_no", getreceiptRS.getString("ldgr_no"));
						json.put("folio_no",getreceiptRS.getString("folio_no"));
						json.put("status",getreceiptRS.getString("status"));
						json.put("description",getreceiptRS.getString("description"));
						json.put("amt_paid", getreceiptRS.getString("amt_paid"));
						json.put("purpose_key", getreceiptRS.getString("purpose_key"));
						json.put("new_purpose_key", getreceiptRS.getString("new_purpose_key"));
						
						array.add(json);
					}
					if(array.isEmpty()){
						//no tasks for user
						AckObj.put("status", "success");
						AckObj.put("status1", "fail");
						AckObj.put("message1", "No Records Found");
						AckObj.put("receipts_list", array);
					}else{
						AckObj.put("status", "success");
						AckObj.put("status1", "");
						AckObj.put("receipts_list", array);
						AckObj.put("message", "Reconcilation Done And"+" Successfully posted records : "+posted_count);
					}
				}else {
					AckObj.put("status", "fail");
					AckObj.put("status1", "");
					AckObj.put("message", "No Reconcilation Done !!!");
					AckObj.put("receipts_list", array);
				}
				}else {
					AckObj.put("status", "success");
					AckObj.put("status1", "fail");
					AckObj.put("message1", "No Records Found");
					//AckObj.put("message", "No Records Found");
				}
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptCS);
				DBManagerResourceRelease.close(getreceiptRS);
			}
		
		return AckObj;
	}
	
	public boolean CallReconProgram(String ReceiptDate,
			String Location,String UserID, String recon_contextPath) {
		// TODO Auto-generated method stub
		
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String db_ip = "";
		String database = "";
		String db_user = "";
		String db_pass = "";
		String recon_PATH = "";
		
		try{
			
			try{
		    	//resouce bundle to read string's specified in properties file
		    	ResourceBundle propsBundle=ResourceBundle.getBundle("savbilldb");
		        
		    	db_ip = propsBundle.getString("IP");
		        database = propsBundle.getString("DATABASE");
		        db_user = propsBundle.getString("USER");
		        db_pass = propsBundle.getString("PASS");
		        recon_PATH = propsBundle.getString("PATH");
		       
		       } 
		    catch(Exception e){
		        System.out.println("error" + e);
		       }	 
			
			//Call Reconcilation Program Here...................
			System.out.println("Call Reconcilation Program Here...........");
			
			
			System.out.println("Recon Program Starts Here...!");
			
			//Call The Respective Child Process...!
			System.out.println("java -jar "+recon_PATH+"Reconcilation.jar "+db_ip+" "+db_user+"/"+db_pass+" "+database+" "+ReceiptDate );
			
			
			Process p = null;
			try {
				p = Runtime.getRuntime().exec("java -jar "+recon_PATH+"Reconcilation.jar "+db_ip+" "+db_user+"/"+db_pass+" "+database+" "+ReceiptDate);
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 
	        final InputStream is = p.getInputStream();
	        Thread t = new Thread(new Runnable() {
	            public void run() {
	                InputStreamReader isr = new InputStreamReader(is);
	                int ch;
	                try {
	                    while ((ch = isr.read()) != -1) {
	                        System.out.print((char) ch);
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	        t.start();
	        try {
				p.waitFor();
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}finally{
			
		}
		return true;
	}

	@Override
	public JSONObject doreconcilation(JSONObject object, HttpServletRequest request) {
		// TODO Auto-generated method stub
JSONObject json_response = new JSONObject();
		
		if(!object.isEmpty()) {
			
			String location_code = (String) object.get("Location");
			String user_id = (String) object.get("UserId");
			String conn_type = (String) object.get("conn_type");
			String receipt_date = (String) object.get("recon_receipt_date");
			String receipt_no = (String) object.get("recon_receiptno");
			String receipt_selected = (String) object.get("recon_selected");
			String recon_rrno = ((String) object.get("recon_rrno"));
			
			
			if(receipt_selected.equals("rr_no_wise")) {
				
				if(CallReconProgram(
						recon_rrno,
						receipt_date,
						location_code,
						user_id,
						request.getServletContext().getRealPath("CashManagementImpl.java"))) {
						
					json_response.put("status", "success");
					json_response.put("message", "Reconcilation done successfully for receipt date : "+receipt_date + " , RR Number : "+recon_rrno );
				}else {
					json_response.put("status", "error");
					json_response.put("message", "Reconcialtion Unsuccessful !!!");
				}
				
			}else if(receipt_selected.equals("receipt_date_wise")) {
				if(CallReconProgram(
						receipt_date,
						location_code,
						user_id,
						request.getServletContext().getRealPath("ReceiptGenerationImpl.java"))) {
					
					json_response.put("status", "success");
					json_response.put("message", "Reconcilation done successfully for receipt date : "+receipt_date);
				}else {
					json_response.put("status", "error");
					json_response.put("message", "Reconcialtion Unsuccessful !!!");
				}
				
			}else {
				json_response.put("status", "success");
				json_response.put("message", "Cannot Do Reconcilation For Receipt No : "+receipt_no+ " receipt date : "+receipt_date + " , Shop No : "+recon_rrno);
			}
			
			
		}else {
			
		}
		
		return json_response;
	}
	
	public boolean CallReconProgram(String new_purpose_key,String ReceiptDate,
			String Location,String UserID, String recon_contextPath) {
		// TODO Auto-generated method stub
		
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String db_ip = "";
		String database = "";
		String db_user = "";
		String db_pass = "";
		String recon_PATH = "";
		
		try{
			
			try{
		    	//resouce bundle to read string's specified in properties file
		    	ResourceBundle propsBundle=ResourceBundle.getBundle("savbilldb");
		        
		    	db_ip = propsBundle.getString("IP");
		        database = propsBundle.getString("DATABASE");
		        db_user = propsBundle.getString("USER");
		        db_pass = propsBundle.getString("PASS");
		        recon_PATH = propsBundle.getString("PATH");
		       
		       } 
		    catch(Exception e){
		        System.out.println("error" + e);
		       }	 
			
			//Call Reconcilation Program Here...................
			System.out.println("Call Reconcilation Program Here...........");
			
			
			System.out.println("Recon Program Starts Here...!");
			
			//Call The Respective Child Process...!
			System.out.println("java -jar "+recon_PATH+"Reconcilation.jar "+db_ip+" "+db_user+"/"+db_pass+" "+database+" "+ReceiptDate + " "+new_purpose_key  );
			
			
			Process p = null;
			try {
				p = Runtime.getRuntime().exec("java -jar "+recon_PATH+"Reconcilation.jar "+db_ip+" "+db_user+"/"+db_pass+" "+database+" "+ReceiptDate+ " "+new_purpose_key );
			} catch (IOException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			 
	        final InputStream is = p.getInputStream();
	        Thread t = new Thread(new Runnable() {
	            public void run() {
	                InputStreamReader isr = new InputStreamReader(is);
	                int ch;
	                try {
	                    while ((ch = isr.read()) != -1) {
	                        System.out.print((char) ch);
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	        t.start();
	        try {
				p.waitFor();
				t.join();
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}finally{
			
		}
		return true;
	}

	@Override
	public JSONObject doreceiptreposting(JSONObject object, HttpServletRequest request) {
		// TODO Auto-generated method stub


		CallableStatement getreceiptCS = null;
		ResultSet getreceiptRS = null;
		
		JSONObject AckObj = new JSONObject();
		JSONArray array = new JSONArray();
		
		try {
			
				JSONObject json_resp = new JSONObject();
				
				JSONArray receipts_list = (JSONArray) object.getJSONArray("receipts_list");
				String location_code = (String) object.get("location_code");
				String user_id = (String) object.get("user_id");
				String conn_type = (String) object.get("conn_type");
				
				//dbConnection = DatabaseImpl.GetLTSQLConnection();
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				for(int i = 0 ; i < receipts_list.size();i++ ) {
					
					JSONObject json = (JSONObject) receipts_list.get(i);
					
					String status = (String) json.get("status") ; 
					String new_purpose_key = location_code+(String) json.get("new_purpose_key") ; 
					String rcpt_no = (String) json.get("rcpt_no") ; 
					String countr_no = (String) json.get("countr_no") ; 
					String rcpt_dt = (String) json.get("rcpt_dt") ; 
					
					if(status.equals("2") && new_purpose_key.length()>0) {
						
						getreceiptCS = dbConnection.prepareCall("{call NEW_REPOST(?,?,?,?,?,?)}");
						getreceiptCS.setString(1,rcpt_dt);
						getreceiptCS.setString(2,rcpt_no);
						getreceiptCS.setString(3,countr_no);
						getreceiptCS.setString(4,new_purpose_key);
						getreceiptCS.setString(5,user_id);
						getreceiptCS.registerOutParameter(6, OracleTypes.VARCHAR);
						getreceiptCS.executeUpdate();
						
						System.out.println( getreceiptCS.getString(6));
						
						String repost_status = getreceiptCS.getString(6);
						
						if(repost_status.equals("TRUE")) {
							
								if(CallReconProgram(new_purpose_key,rcpt_dt,
										location_code,
										user_id,
										request.getServletContext().getRealPath("CashManagementImpl.java"))) {
									
									json_resp.put("status", "success");
									
								}else {
									
								}
							
						}else  {
							
							
						}
					}
				}
				getreceiptCS.close();
				
				getreceiptCS = dbConnection.prepareCall("{call PKG_CASH.GET_RCPT_DETIALS_FOR_POST(?,?,?,?)}");
				getreceiptCS.setString(1,object.getString("location_code"));
				getreceiptCS.setString(2,object.getString("receipt_date"));
				getreceiptCS.setString(3,object.getString("counter_number"));
				getreceiptCS.registerOutParameter(4, OracleTypes.CURSOR);
				getreceiptCS.executeUpdate();
				getreceiptRS = (ResultSet) getreceiptCS.getObject(4);
				
				while(getreceiptRS.next()){
					
					JSONObject json = new JSONObject();
					
					json.put("row_num", getreceiptRS.getString("row_num"));
					json.put("rr_no", getreceiptRS.getString("rr_no"));
					json.put("rcpt_no", getreceiptRS.getString("rcpt_no"));
					json.put("rcpt_dt", getreceiptRS.getString("rcpt_dt"));
					json.put("countr_no", getreceiptRS.getString("countr_no"));
					json.put("countr_name", getreceiptRS.getString("countr_name"));
					json.put("pymnt_mode", getreceiptRS.getString("pymnt_mode"));
					json.put("pymnt_mode_description", getreceiptRS.getString("pymnt_mode_description"));
					json.put("purpose", getreceiptRS.getString("purpose"));
					json.put("purpose_descr", getreceiptRS.getString("purpose_descr"));
					json.put("rcpt_typ", getreceiptRS.getString("rcpt_typ"));
					json.put("payee_name", getreceiptRS.getString("payee_name"));
					json.put("cancel_flg", getreceiptRS.getString("cancel_flg"));
					json.put("remarks", getreceiptRS.getString("remarks"));
					json.put("posted_sts", getreceiptRS.getString("posted_sts"));
					json.put("printed_flg", getreceiptRS.getString("printed_flg"));
					json.put("rrno_applno_flg",getreceiptRS.getString("rrno_applno_flg"));
					json.put("chq_dd_no",getreceiptRS.getString("chq_dd_no"));
					json.put("chq_dd_dt",getreceiptRS.getString("chq_dd_dt"));
					json.put("drawn_bank", getreceiptRS.getString("drawn_bank"));
					json.put("less_amt_realised", getreceiptRS.getString("less_amt_realised"));
					json.put("less_amt_posted_sts", getreceiptRS.getString("less_amt_posted_sts"));
					json.put("userid", getreceiptRS.getString("userid"));
					json.put("tmpstp", getreceiptRS.getString("tmpstp"));
					json.put("ldgr_no", getreceiptRS.getString("ldgr_no"));
					json.put("folio_no",getreceiptRS.getString("folio_no"));
					json.put("status",getreceiptRS.getString("status"));
					json.put("description",getreceiptRS.getString("description"));
					json.put("amt_paid", getreceiptRS.getString("amt_paid"));
					json.put("purpose_key", getreceiptRS.getString("purpose_key"));
					json.put("new_purpose_key", getreceiptRS.getString("new_purpose_key"));
					
					array.add(json);
				}
				/*if(array.isEmpty()){
					//no tasks for user
					AckObj.put("status", "fail");
					AckObj.put("message", "No Records Found");
					AckObj.put("receipts_list", array);
				}else{
					AckObj.put("status", "success");
					AckObj.put("receipts_list", array);
					AckObj.put("message", "Reposting Done Successfully !!!");
				}*/
				if(!json_resp.isEmpty()) {
					AckObj.put("status", "success");
					AckObj.put("receipts_list", array);
					AckObj.put("message", "Reposting Done Successfully !!!");
				}else {
					AckObj.put("status", "fail");
					AckObj.put("message", "Failed to do reposting.");
					AckObj.put("receipts_list", array);
				}
				
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptCS);
				DBManagerResourceRelease.close(getreceiptRS);
			}
		return AckObj;
	}

	@Override
	public JSONObject verifyreceiptnumber(JSONObject object, HttpServletRequest request) {
		// TODO Auto-generated method stub


		PreparedStatement ps = null;
		PreparedStatement ps1 = null;
		ResultSet rs = null;
		ResultSet rs1 = null;
		JSONObject jsonResponse = new JSONObject();
		
		
		try {
			
			if(!object.isEmpty()){
				
				String receiptno  = (String)object.get("receiptno");
				String receiptdate  = (String)object.get("receiptdate");
				String counterno  = (String)object.get("counterno");
				String conn_type = (String) object.get("conn_type");
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(dbConnection != null){
					ps = dbConnection.prepareStatement("SELECT COUNT(*) CNT FROM INITIAL_RCPT_PYMNT WHERE IRP_RCPT_NO ='"+receiptno+"'AND "
							+ " IRP_RCPT_DT= TO_DATE('"+receiptdate+"','DD/MM/YYYY') AND  IRP_CASH_COUNTR_NO = '"+counterno+"'") ; 
					
					rs = ps.executeQuery();
					if (rs.next()) {
						jsonResponse.put("receipts_count", rs.getInt("CNT"));
						jsonResponse.put("status", "success");
						
							if(rs.getInt("CNT") == 0) {
								ps1 = dbConnection.prepareStatement("SELECT NVL(MAX(IRP_RCPT_NO),0)+1 NEW_RECEIPT_NUMBER "
										+ " FROM INITIAL_RCPT_PYMNT "
										+ " WHERE "
										+ " IRP_RCPT_DT= (  SELECT MAX(IRP_RCPT_DT) FROM INITIAL_RCPT_PYMNT "
														+ "	WHERE IRP_RCPT_DT< TO_DATE('"+receiptdate+"','DD/MM/YYYY') AND  "
														+ " IRP_CASH_COUNTR_NO = '"+counterno+"') AND  "
										+ " IRP_CASH_COUNTR_NO = '"+counterno+"' ") ; 
								
								rs1 = ps1.executeQuery();
								if (rs1.next()) {
									jsonResponse.put("NEW_RECEIPT_NUMBER", rs1.getInt("NEW_RECEIPT_NUMBER"));
									jsonResponse.put("new_receipt",true);
								}else {
									jsonResponse.put("NEW_RECEIPT_NUMBER", receiptno);
									jsonResponse.put("new_receipt",false);
								}
							}
						
					}else {
						jsonResponse.put("status", "error");
						jsonResponse.put("receipts_count", -1);
					}
				}else {
					jsonResponse.put("status", "error");
					jsonResponse.put("message", "Database Query / Runtime Error .");
				}
			
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Invalid Inputs / No Agent Detials");
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown " + e);
			jsonResponse.put("status", "error");
			jsonResponse.put("message", "Database Query / Runtime Error .");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonResponse.put("status", "error");
			jsonResponse.put("message", " Database Query / Runtime Error.");
		} finally {
			DBManagerResourceRelease.close(rs);
			DBManagerResourceRelease.close(ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject savereceiptdetailsManaualReceipts(JSONObject object, HttpServletRequest request,
			HttpServletResponse response) {
		// TODO Auto-generated method stub
		CallableStatement accountsCS = null;
		JSONObject jsonResponse  = new JSONObject();
		JSONArray jsonList = new JSONArray();
		PreparedStatement ps	=	null;
		ResultSet rs			=	null;
		boolean continue_execution = false;
		String TRANSACTION_ID = "";
		
		try {
			
			
			String location_code = (String) object.get("location");
			String userid = (String) object.get("user_id");
			String conn_type = (String) object.get("conn_type");
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			if(!object.isEmpty()){
				
					
				
				if(dbConnection != null){
					
					String transaction_fetch = "SELECT TO_CHAR(LPAD(NVL(MAX(TO_NUMBER(IRPT_TRANSACTION_ID)),0)+1,10,'0'))  TRANS_ID FROM IRP_TRANSACTIONS";
					
					ps = dbConnection.prepareStatement(transaction_fetch);
					rs = ps.executeQuery();
					
					if(rs.next()) {
						TRANSACTION_ID = rs.getString("TRANS_ID");
					}
					
					rs.close();
					
					if(object.getString("payment_mode").equals("CHQ")) {
						
						accountsCS=dbConnection.prepareCall("{call PKG_CASH.INSERT_CHEQUE_MASTER_HRT(?,?,?,?,?,?,?,?)}");
						
						accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
						accountsCS.setString(2, ConvertIFNullToString(object.getString("cheque_number")));
						accountsCS.setString(3, ConvertIFNullToString(object.getString("cheque_date")));
						accountsCS.setString(4, ConvertIFNullToString(object.getString("drawee_bank")));
						accountsCS.setString(5, ConvertIFNullToString(object.getString("receipt_date")));
						accountsCS.setString(6, ConvertIFNullToString(object.getString("payment_mode")));
						accountsCS.setString(7, ConvertIFNullToString(object.getString("cheque_amount")));
						accountsCS.setString(8, ConvertIFNullToString(object.getString("user_id")));
						
						accountsCS.executeUpdate();
						rs = (ResultSet) accountsCS.getObject(1);
						
						if(rs.next()){
							
							String resp = rs.getString("RESP");
							
							System.out.println("resp : "+resp);
							
							if(resp.equalsIgnoreCase("SUCCESS")){
								continue_execution = true;
								System.out.println("111111111continue_execution : "+continue_execution);
							}else {
								continue_execution = false;
								System.out.println("22222222222continue_execution : "+continue_execution);
							}
						}
						
					}else {
						
						continue_execution = true;
					}
					
					System.out.println("continue_execution : "+continue_execution);
					if(continue_execution) {
						
						JSONArray receipt_details_Array = object.getJSONArray("receipt_details");
						
						if(!receipt_details_Array.isEmpty()){
							
							accountsCS=dbConnection.prepareCall("{call PKG_CASH.INSERT_RCPT_DETIALS_HRT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");
							
							for(int i = 0 ; i<receipt_details_Array.size();i++){
								
								JSONObject receipt = receipt_details_Array.getJSONObject(i);
								
								accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
								accountsCS.setString(2, TRANSACTION_ID);
								accountsCS.setString(3, ConvertIFNullToString(receipt.getString("manualreceiptnumber")));
								accountsCS.setString(4, ConvertIFNullToString(object.getString("receipt_date")));
								accountsCS.setString(5, ConvertIFNullToString(object.getString("cash_counter")));
								accountsCS.setString(6, ConvertIFNullToString(object.getString("payment_purpose")));
								accountsCS.setString(7, ConvertIFNullToString(receipt.getString("rrno")));
								accountsCS.setString(8, ConvertIFNullToString(object.getString("payment_mode")));
								accountsCS.setString(9, ConvertIFNullToString(receipt.getString("amount")));
								accountsCS.setString(10, ConvertIFNullToString(receipt.getString("name")));
								accountsCS.setString(11, ConvertIFNullToString(object.getString("remarks")));
								accountsCS.setString(12, ConvertIFNullToString(object.getString("cheque_number")));
								accountsCS.setString(13, ConvertIFNullToString(object.getString("cheque_date")));
								accountsCS.setString(14, ConvertIFNullToString(object.getString("drawee_bank")));
								accountsCS.setString(15, ConvertIFNullToString(object.getString("user_id")));
								
								System.out.println(accountsCS);
								
								accountsCS.executeUpdate();
								
								
								rs = (ResultSet) accountsCS.getObject(1);
								
								System.out.println("outside...................");
								System.out.println("rs : "+rs);
								
								if(rs.next()){
									
									JSONObject json_response = new JSONObject();
									
									String resp = rs.getString("RESP");
									
									System.out.println("resp : "+resp);
									
									if(resp.equalsIgnoreCase("success")){
										
										json_response.put("status", "success");
										json_response.put("message", "Receipt Generated Successfully");
										
									}else if(resp.indexOf("fail") > 0){
										json_response.put("status", "error");
										json_response.put("message", "Receipt not generated");
									}
									jsonList.add(json_response);
								}
							}
							if(!jsonList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "Receipt Generated Successfully");
								
								JSONObject temp_object = new JSONObject();
								temp_object.put("TRANSACTION_ID", TRANSACTION_ID);
								temp_object.put("filename", "APMC_RCPT");
								temp_object.put("exporttype", "PDF");
								temp_object.put("report_title", "Receipt Print");

								//JSONObject fileload_json = printObj.downloadbilltolocalpath(temp_object, request, response);
								//System.out.println(fileload_json);
								
								//jsonResponse.put("filepath", (String)fileload_json.get("filepath"));
								
							}else{
								jsonResponse.put("status", "error");
								jsonResponse.put("message", "Receipt not generated !");
							}
					}
					
					}else{
						jsonResponse.put("status", "error");
						jsonResponse.put("message", "No Receipts Found To Generate !");
					}
				}	
			}else{	
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Invalid Input !");
			}
		} catch (SQLException e) {
			System.out.println(e.getMessage());
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error Occured In Receipt Generation .");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally 
		{
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}
		return jsonResponse;
	}

	@Override
	public JSONObject getfirstreceiptnumber(JSONObject object, HttpServletRequest request) {
		// TODO Auto-generated method stub
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		JSONObject jsonResponse = new JSONObject();
		
		
		try {
			
			if(!object.isEmpty()){
				
				String receiptdate  = (String)object.get("receiptdate");
				String counterno  = (String)object.get("counterno");
				String conn_type = (String) object.get("conn_type");
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(dbConnection != null){
					String qry = "SELECT NVL(MAX(IRPH_RCPT_NO),0)+1 NEW_RECEIPT_NUMBER "
							+ " FROM INITIAL_RCPT_PYMNT_HRT "
							+ " WHERE "
							+ " IRPH_RCPT_DT= (  SELECT MAX(IRPH_RCPT_DT) FROM INITIAL_RCPT_PYMNT_HRT "
											+ "	WHERE IRPH_RCPT_DT<= TO_DATE('"+receiptdate+"','DD/MM/YYYY') AND  "
											+ " IRPH_CASH_COUNTR_NO = '"+counterno+"') AND  "
							+ " IRPH_CASH_COUNTR_NO = '"+counterno+"' " ; 
					System.out.println(qry);
						ps1 = dbConnection.prepareStatement(qry) ; 
						
						rs1 = ps1.executeQuery();
						if (rs1.next()) {
							jsonResponse.put("NEW_RECEIPT_NUMBER", rs1.getInt("NEW_RECEIPT_NUMBER"));
							jsonResponse.put("new_receipt",true);
							jsonResponse.put("status", "success");
						}
				}
				else {
					jsonResponse.put("status", "error");
					jsonResponse.put("message", "Database Query / Runtime Error .");
				}
			}		
		} catch (SQLException e) {
			System.out.println("Exception thrown " + e);
			jsonResponse.put("status", "error");
			jsonResponse.put("message", "Database Query / Runtime Error .");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonResponse.put("status", "error");
			jsonResponse.put("message", " Database Query / Runtime Error.");
		} finally {
			DBManagerResourceRelease.close(rs1);
			DBManagerResourceRelease.close(ps1);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getreceiptdetailstocancel(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement getreceiptPS = null;
		ResultSet getreceiptRS = null;
		JSONObject AckObj = new JSONObject();
		
		try {
				//dbConn=dbObject.getDatabaseConnection();
			String conn_type = (String)object.get("conn_type");
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			String query = " select substr(IRP_PURPOSE_KEY,8) rr_number,IRP_PAYEE_NAME payee_name,IRP_AMT_PAID amount_paid,IRP_PYMNT_MD payment_mode,"
					+ "	   nvl((select CCD_DESCR from CODE_DETL where CCD_CD_VAL = IRP_PYMNT_MD and CCD_CCM_CD_TYP = 'PYMNT_MOD' ),IRP_PYMNT_MD) payment_mode_descr, "
					+ " 					     IRP_CANCEL_FLG cancel_flag,IRP_STATUS posted_status "
					+ " 		      from INITIAL_RCPT_PYMNT "
						 + " where "
						 //+ " nvl(IRP_CANCEL_FLG,'N') = 'N'"
						 //+ " and "
						 + " IRP_CASH_COUNTR_NO = '"+(String)object.get("counter")+"' "
						 + " and IRP_RCPT_NO = '"+(String)object.get("receiptno")+"' "
						 + " and IRP_RCPT_DT = to_date('"+(String)object.get("receiptdate")+"','DD/MM/YYYY') "
						 + " and  IRP_PYMNT_MD = 'C' "
						 //+ " and IRP_STATUS = '1' " 
						 ; 
			
			System.out.println(query);
				
				getreceiptPS = dbConnection.prepareStatement(query);
				getreceiptRS = getreceiptPS.executeQuery();
				
				if(getreceiptRS.next()) {
					
					
					if(getreceiptRS.getString("posted_status").equals("4")) {
						AckObj.put("message", "Receipt Posted . Cannot Cancel !!!");
						AckObj.put("status", "success");
						AckObj.put("sts", "error");
					}else if(getreceiptRS.getString("cancel_flag").equals("Y")) {
						AckObj.put("message", "Cannot Cancel . Receipt Already Cancelled !!!");
						AckObj.put("status", "success");
						AckObj.put("sts", "error");
					}else {
						AckObj.put("message", "Receipt Found !!!");
						AckObj.put("status", "success");
					}
					
					AckObj.put("shop_number",getreceiptRS.getString("rr_number"));
					AckObj.put("payee_name", getreceiptRS.getString("payee_name"));
					AckObj.put("amount_paid", getreceiptRS.getString("amount_paid"));
					AckObj.put("payment_mode", getreceiptRS.getString("payment_mode"));
					AckObj.put("payment_mode_descr", getreceiptRS.getString("payment_mode_descr"));
					AckObj.put("cancel_flag", getreceiptRS.getString("cancel_flag"));
					AckObj.put("posted_status", getreceiptRS.getString("posted_status"));
					
				}else {
					AckObj.put("status", "fail");
					AckObj.put("message", "Receipt Not Found !!!");
					
				}
				
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptRS);
				DBManagerResourceRelease.close(getreceiptPS);
			}
		return AckObj;
	}
	
	@Override
	public JSONObject getreceiptdetailstocancel_hrt(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement getreceiptPS = null;
		ResultSet getreceiptRS = null;
		JSONObject AckObj = new JSONObject();
		
		try {
				//dbConn=dbObject.getDatabaseConnection();
			String conn_type = (String)object.get("conn_type");
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			String query = " select substr(IRPH_PURPOSE_KEY,8) rr_number,IRPH_PAYEE_NAME payee_name,IRPH_AMT_PAID amount_paid,IRPH_PYMNT_MD payment_mode,"
					+ "	   nvl((select CCD_DESCR from CODE_DETL where CCD_CD_VAL = IRPH_PYMNT_MD and CCD_CCM_CD_TYP = 'PYMNT_MOD' ),IRPH_PYMNT_MD) payment_mode_descr, "
					+ " 					     IRPH_CANCEL_FLG cancel_flag,IRPH_STATUS posted_status "
					+ " 		      from INITIAL_RCPT_PYMNT_HRT "
						 + " where "
						 //+ " nvl(IRP_CANCEL_FLG,'N') = 'N'"
						 //+ " and "
						 + " IRPH_CASH_COUNTR_NO = '"+(String)object.get("counter")+"' "
						 + " and IRPH_RCPT_NO = '"+(String)object.get("receiptno")+"' "
						 + " and IRPH_RCPT_DT = to_date('"+(String)object.get("receiptdate")+"','DD/MM/YYYY') "
						 + " and  IRPH_PYMNT_MD = 'C' "
						 //+ " and IRP_STATUS = '1' " 
						 ; 
			
			System.out.println(query);
				
				getreceiptPS = dbConnection.prepareStatement(query);
				getreceiptRS = getreceiptPS.executeQuery();
				
				if(getreceiptRS.next()) {
					
					
					if(getreceiptRS.getString("posted_status").equals("4")) {
						AckObj.put("message", "Receipt Posted . Cannot Cancel !!!");
						AckObj.put("status", "success");
						AckObj.put("sts", "error");
					}else if(getreceiptRS.getString("cancel_flag").equals("Y")) {
						AckObj.put("message", "Cannot Cancel . Receipt Already Cancelled !!!");
						AckObj.put("status", "success");
						AckObj.put("sts", "error");
					}else {
						AckObj.put("message", "Receipt Found !!!");
						AckObj.put("status", "success");
					}
					
					AckObj.put("shop_number",getreceiptRS.getString("rr_number"));
					AckObj.put("payee_name", getreceiptRS.getString("payee_name"));
					AckObj.put("amount_paid", getreceiptRS.getString("amount_paid"));
					AckObj.put("payment_mode", getreceiptRS.getString("payment_mode"));
					AckObj.put("payment_mode_descr", getreceiptRS.getString("payment_mode_descr"));
					AckObj.put("cancel_flag", getreceiptRS.getString("cancel_flag"));
					AckObj.put("posted_status", getreceiptRS.getString("posted_status"));
					
				}else {
					AckObj.put("status", "fail");
					AckObj.put("message", "Receipt Not Found !!!");
					
				}
				
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptRS);
				DBManagerResourceRelease.close(getreceiptPS);
			}
		return AckObj;
	}

	@Override
	public JSONObject getchequedetailstocancel(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement getreceiptPS = null;
		ResultSet getreceiptRS = null;
		JSONObject AckObj = new JSONObject();
		JSONArray jsonarray = new JSONArray() ;
		
		try {
			//	dbConn=dbObject.getDatabaseConnection();
			
			String conn_type = (String)object.get("conn_type");
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
				
				String query = " SELECT IRP_RCPT_NO RCPT_NO,TO_CHAR(IRP_RCPT_DT,'DD/MM/YYYY') RCPT_DT,IRP_CASH_COUNTR_NO CASH_COUNTR_NO,"
						     + " SUBSTR(IRP_PURPOSE_KEY,8) RR_NUMBER,IRP_AMT_PAID AMOUNT_PAID,IRP_PAYEE_NAME PAYEE_NAME, "
						     + " IRP_BCM_CHQ_DD_NO CHEQUE_NUMBER,IRP_BCM_CHQ_DD_DT CHEQUE_DT,IRP_BCM_DRAWN_BANK DRAWN_BANK   "
						     + " FROM INITIAL_RCPT_PYMNT "
						     + " WHERE "
						     + " IRP_BCM_CHQ_DD_NO = '"+(String)object.get("cheque_no")+"' "
						     + " AND IRP_BCM_CHQ_DD_DT = TO_DATE('"+(String)object.get("cheque_date")+"','DD/MM/YYYY') "
						  //   + " AND IRP_BCM_DRAWN_BANK = '"+(String)object.get("cheque_drawee_bank")+"'" 
						     + " AND IRP_STATUS = '1' " 
						     ;
						 ; 
						 
				String cheque_query =  " SELECT COUNT(*) CNT FROM CHEQUE_MASTER " + 
									   " WHERE  "
									   + " BCM_CHQ_DD_NO = '"+(String)object.get("cheque_no")+"' "
									   + " AND BCM_CHQ_DD_DT = TO_DATE('"+(String)object.get("cheque_date")+"','DD/MM/YYYY')  "
									   + " AND BCM_CANCEL_FLG = 'Y' "
									// + " AND BCM_DRAWN_BANK = '"+(String)object.get("cheque_drawee_bank")+"' " 
									   ;
				
				System.out.println(cheque_query);
				getreceiptPS = dbConnection.prepareStatement(cheque_query);
				getreceiptRS = getreceiptPS.executeQuery();
				
				float total_cheque_amount = 0;
				int index = 0;
				String bank = "";
				if(getreceiptRS.next()) {
					
					if(getreceiptRS.getInt("CNT") == 0 ) {
						
						getreceiptPS.close();
						getreceiptRS.close();
						
						System.out.println(query);
						getreceiptPS = dbConnection.prepareStatement(query);
						getreceiptRS = getreceiptPS.executeQuery();
						
						while(getreceiptRS.next()) {
							
							JSONObject json = new JSONObject();
							
							if(index == 0) {
								
								bank = getreceiptRS.getString("DRAWN_BANK");
								index++;
							}
							
							json.put("RCPT_NO", getreceiptRS.getString("RCPT_NO"));
							json.put("RCPT_DT", getreceiptRS.getString("RCPT_DT"));
							json.put("CASH_COUNTR_NO", getreceiptRS.getString("CASH_COUNTR_NO"));
							json.put("RR_NUMBER", getreceiptRS.getString("RR_NUMBER"));
							json.put("AMOUNT_PAID", getreceiptRS.getString("AMOUNT_PAID"));
							json.put("PAYEE_NAME", getreceiptRS.getString("PAYEE_NAME"));
							json.put("CHEQUE_NUMBER", getreceiptRS.getString("CHEQUE_NUMBER"));
							json.put("CHEQUE_DT", getreceiptRS.getString("CHEQUE_DT"));
							json.put("DRAWN_BANK", getreceiptRS.getString("DRAWN_BANK"));
							
							total_cheque_amount =  total_cheque_amount +  getreceiptRS.getFloat("AMOUNT_PAID") ;
							
							jsonarray.add(json);
							
						}
						
						if(!jsonarray.isEmpty()) {
							AckObj.put("message", "Cheque Detail Found !!!");
							AckObj.put("status", "success");
							AckObj.put("cheque_list", jsonarray);
							AckObj.put("cancel_flag", "");
							AckObj.put("posted_status", "");
							AckObj.put("posted_status", "total_cheque_amount");
							AckObj.put("total_cheque_amount", total_cheque_amount+"");
							AckObj.put("bankname", bank);
							
						}else {
							AckObj.put("status", "fail");
							AckObj.put("message", "Cheque Not Found !!!");
							AckObj.put("cheque_list", jsonarray);
						}
						
					}else {
							AckObj.put("message", "Cannot Cancel . Cheque Already Cancelled !!!");
							AckObj.put("status", "success");
							AckObj.put("sts", "error");
							AckObj.put("cheque_list", jsonarray);
							AckObj.put("cancel_flag", "Y");
							AckObj.put("posted_status", "");
							AckObj.put("total_cheque_amount", total_cheque_amount+"");
							AckObj.put("bankname", bank);
							
					}
				}else {
					AckObj.put("message", " Cheque Detail Not Found !!!");
					AckObj.put("status", "error");
				}

			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptRS);
				DBManagerResourceRelease.close(getreceiptPS);
			}
		return AckObj;
	}
	
	@Override
	public JSONObject getchequedetailstocancel_hrt(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement getreceiptPS = null;
		ResultSet getreceiptRS = null;
		JSONObject AckObj = new JSONObject();
		JSONArray jsonarray = new JSONArray() ;
		
		try {
			//	dbConn=dbObject.getDatabaseConnection();
			
			String conn_type = (String)object.get("conn_type");
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
				
				String query = " SELECT IRPH_RCPT_NO RCPT_NO,TO_CHAR(IRPH_RCPT_DT,'DD/MM/YYYY') RCPT_DT,IRPH_CASH_COUNTR_NO CASH_COUNTR_NO,"
						     + " SUBSTR(IRPH_PURPOSE_KEY,8) RR_NUMBER,IRPH_AMT_PAID AMOUNT_PAID,IRPH_PAYEE_NAME PAYEE_NAME, "
						     + " IRPH_BCM_CHQ_DD_NO CHEQUE_NUMBER,IRPH_BCM_CHQ_DD_DT CHEQUE_DT,IRPH_BCM_DRAWN_BANK DRAWN_BANK   "
						     + " FROM INITIAL_RCPT_PYMNT_HRT "
						     + " WHERE "
						     + " IRPH_BCM_CHQ_DD_NO = '"+(String)object.get("cheque_no")+"' "
						     + " AND IRPH_BCM_CHQ_DD_DT = TO_DATE('"+(String)object.get("cheque_date")+"','DD/MM/YYYY') "
						  //   + " AND IRP_BCM_DRAWN_BANK = '"+(String)object.get("cheque_drawee_bank")+"'" 
						     + " AND IRPH_STATUS = '1' " 
						     ;
						 ; 
						 
				String cheque_query =  " SELECT COUNT(*) CNT FROM CHEQUE_MASTER_HRT " + 
									   " WHERE  "
									   + " BCM_CHQ_DD_NO = '"+(String)object.get("cheque_no")+"' "
									   + " AND BCM_CHQ_DD_DT = TO_DATE('"+(String)object.get("cheque_date")+"','DD/MM/YYYY')  "
									   + " AND BCM_CANCEL_FLG = 'Y' "
									// + " AND BCM_DRAWN_BANK = '"+(String)object.get("cheque_drawee_bank")+"' " 
									   ;
				
				System.out.println(cheque_query);
				getreceiptPS = dbConnection.prepareStatement(cheque_query);
				getreceiptRS = getreceiptPS.executeQuery();
				
				float total_cheque_amount = 0;
				int index = 0;
				String bank = "";
				if(getreceiptRS.next()) {
					
					if(getreceiptRS.getInt("CNT") == 0 ) {
						
						getreceiptPS.close();
						getreceiptRS.close();
						
						System.out.println(query);
						getreceiptPS = dbConnection.prepareStatement(query);
						getreceiptRS = getreceiptPS.executeQuery();
						
						while(getreceiptRS.next()) {
							
							JSONObject json = new JSONObject();
							
							if(index == 0) {
								
								bank = getreceiptRS.getString("DRAWN_BANK");
								index++;
							}
							
							json.put("RCPT_NO", getreceiptRS.getString("RCPT_NO"));
							json.put("RCPT_DT", getreceiptRS.getString("RCPT_DT"));
							json.put("CASH_COUNTR_NO", getreceiptRS.getString("CASH_COUNTR_NO"));
							json.put("RR_NUMBER", getreceiptRS.getString("RR_NUMBER"));
							json.put("AMOUNT_PAID", getreceiptRS.getString("AMOUNT_PAID"));
							json.put("PAYEE_NAME", getreceiptRS.getString("PAYEE_NAME"));
							json.put("CHEQUE_NUMBER", getreceiptRS.getString("CHEQUE_NUMBER"));
							json.put("CHEQUE_DT", getreceiptRS.getString("CHEQUE_DT"));
							json.put("DRAWN_BANK", getreceiptRS.getString("DRAWN_BANK"));
							
							total_cheque_amount =  total_cheque_amount +  getreceiptRS.getFloat("AMOUNT_PAID") ;
							
							jsonarray.add(json);
							
						}
						
						if(!jsonarray.isEmpty()) {
							AckObj.put("message", "Cheque Detail Found !!!");
							AckObj.put("status", "success");
							AckObj.put("cheque_list", jsonarray);
							AckObj.put("cancel_flag", "");
							AckObj.put("posted_status", "");
							AckObj.put("posted_status", "total_cheque_amount");
							AckObj.put("total_cheque_amount", total_cheque_amount+"");
							AckObj.put("bankname", bank);
							
						}else {
							AckObj.put("status", "fail");
							AckObj.put("message", "Cheque Not Found !!!");
							AckObj.put("cheque_list", jsonarray);
						}
						
					}else {
							AckObj.put("message", "Cannot Cancel . Cheque Already Cancelled !!!");
							AckObj.put("status", "success");
							AckObj.put("sts", "error");
							AckObj.put("cheque_list", jsonarray);
							AckObj.put("cancel_flag", "Y");
							AckObj.put("posted_status", "");
							AckObj.put("total_cheque_amount", total_cheque_amount+"");
							AckObj.put("bankname", bank);
							
					}
				}else {
					AckObj.put("message", " Cheque Detail Not Found !!!");
					AckObj.put("status", "error");
				}

			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptRS);
				DBManagerResourceRelease.close(getreceiptPS);
			}
		return AckObj;
	}

	@Override
	public JSONObject docancelreceipts(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement getreceiptPS = null;
		ResultSet getreceiptRS = null;
		JSONObject AckObj = new JSONObject();
		
		try {
			//	dbConn=dbObject.getDatabaseConnection();
			String conn_type = (String)object.get("conn_type");
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
				
				
				String query =    "  update "
						        + "  INITIAL_RCPT_PYMNT "
								+ "	 set IRP_CANCEL_FLG  = 'Y' "
								+ "  where "
								+ "  nvl(IRP_CANCEL_FLG,'N') = 'N' " 
								+ "  and IRP_CASH_COUNTR_NO = '"+(String)object.get("counter")+"' "
								+ "  and IRP_RCPT_NO = '"+(String)object.get("receiptno")+"'  "
								+ "  and IRP_RCPT_DT = to_date('"+(String)object.get("receiptdate")+"','DD/MM/YYYY') "
								+ "  and  IRP_PYMNT_MD = 'C' "
								+ "  and IRP_STATUS = '1' ";
				
				getreceiptPS = dbConnection.prepareStatement(query);
				int cancel_count = getreceiptPS.executeUpdate();
				
				if(cancel_count > 0) {
					
					AckObj.put("message", "Receipt Successfully Cancelled for Receipt No : "+(String)object.get("receiptno"));
					AckObj.put("status", "success");
					
				}else {
					AckObj.put("status", "error");
					AckObj.put("message", "Cannot Cancel. !!!");
					
				}
				
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptRS);
				DBManagerResourceRelease.close(getreceiptPS);
			}
		return AckObj;
	}
	
	@Override
	public JSONObject docancelreceipts_hrt(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement getreceiptPS = null;
		ResultSet getreceiptRS = null;
		JSONObject AckObj = new JSONObject();
		
		try {
			//	dbConn=dbObject.getDatabaseConnection();
			String conn_type = (String)object.get("conn_type");
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
				
				
				String query =    "  update "
						        + "  INITIAL_RCPT_PYMNT_HRT "
								+ "	 set IRPH_CANCEL_FLG  = 'Y' "
								+ "  where "
								+ "  nvl(IRPH_CANCEL_FLG,'N') = 'N' " 
								+ "  and IRPH_CASH_COUNTR_NO = '"+(String)object.get("counter")+"' "
								+ "  and IRPH_RCPT_NO = '"+(String)object.get("receiptno")+"'  "
								+ "  and IRPH_RCPT_DT = to_date('"+(String)object.get("receiptdate")+"','DD/MM/YYYY') "
								+ "  and IRPH_PYMNT_MD = 'C' "
								+ "  and IRPH_STATUS = '1' ";
				
				getreceiptPS = dbConnection.prepareStatement(query);
				int cancel_count = getreceiptPS.executeUpdate();
				
				if(cancel_count > 0) {
					
					AckObj.put("message", "Receipt Successfully Cancelled for Receipt No : "+(String)object.get("receiptno"));
					AckObj.put("status", "success");
					
				}else {
					AckObj.put("status", "error");
					AckObj.put("message", "Cannot Cancel. !!!");
					
				}
				
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptRS);
				DBManagerResourceRelease.close(getreceiptPS);
			}
		return AckObj;
	}

	@Override
	public JSONObject docancelcheques(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement getreceiptPS = null;
		ResultSet getreceiptRS = null;
		JSONObject AckObj = new JSONObject();
		
		try {
			//	dbConn=dbObject.getDatabaseConnection();
			String conn_type = (String)object.get("conn_type");
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
				
			
			dbConnection.setAutoCommit(false);
				
				String query =    " UPDATE CHEQUE_MASTER SET BCM_CANCEL_FLG = 'Y' "
						+ " WHERE BCM_CHQ_DD_NO = '"+(String)object.get("cheque_no")+"'"
						+ "  AND BCM_CHQ_DD_DT = TO_DATE('"+(String)object.get("cheque_date")+"','DD/MM/YYYY') "
						//+ "  AND BCM_DRAWN_BANK = 'NOBANK' "
						+ "  AND BCM_PYMNT_MD = 'CHQ' "
						+ "  AND EXISTS "
						+ "  (SELECT 'X' FROM INITIAL_RCPT_PYMNT "
						+ "   WHERE IRP_BCM_CHQ_DD_NO = BCM_CHQ_DD_NO AND IRP_BCM_CHQ_DD_DT = BCM_CHQ_DD_DT AND IRP_BCM_DRAWN_BANK = BCM_DRAWN_BANK AND IRP_STATUS = '1')";
				
				String rcpt_query = " UPDATE INITIAL_RCPT_PYMNT SET IRP_CANCEL_FLG = 'Y' "
						          + " WHERE "
						          + " IRP_BCM_CHQ_DD_NO = '"+(String)object.get("cheque_no")+"' AND "
						          + " IRP_BCM_CHQ_DD_DT = TO_DATE('"+(String)object.get("cheque_date")+"','DD/MM/YYYY') AND "
						       //   + " IRP_BCM_DRAWN_BANK = 'NOBANK' AND "
						          + "  IRP_PYMNT_MD = 'CHQ' AND IRP_STATUS = '1' " ;
	;
				
				
				
				getreceiptPS = dbConnection.prepareStatement(query);
				int cancel_count = getreceiptPS.executeUpdate();
				
				if(cancel_count > 0) {

					getreceiptPS.close();
					
					getreceiptPS = dbConnection.prepareStatement(rcpt_query);
					int cheque_cancel_count = getreceiptPS.executeUpdate();
					
					if(cheque_cancel_count > 0) {
						AckObj.put("message", "Cheque Successfully Cancelled for cheque No : "+(String)object.get("cheque_no"));
						AckObj.put("status", "success");
						dbConnection.commit();
					}else {
						AckObj.put("status", "error");
						AckObj.put("message", "Cannot Cancel Cheque !!!");
						try {
							dbConnection.rollback();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}else {
					AckObj.put("status", "error");
					AckObj.put("message", "Cannot Cancel. !!!");
					try {
						dbConnection.rollback();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} catch (Exception e) {
				AckObj.put("status", "failure");
				try {
					dbConnection.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptRS);
				DBManagerResourceRelease.close(getreceiptPS);
			}
		return AckObj;
	}
	
	@Override
	public JSONObject docancelcheques_hrt(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement getreceiptPS = null;
		ResultSet getreceiptRS = null;
		JSONObject AckObj = new JSONObject();
		
		try {
			//	dbConn=dbObject.getDatabaseConnection();
			String conn_type = (String)object.get("conn_type");
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
				
			
			dbConnection.setAutoCommit(false);
				
				String query =    " UPDATE CHEQUE_MASTER_HRT SET BCM_CANCEL_FLG = 'Y' "
						+ " WHERE BCM_CHQ_DD_NO = '"+(String)object.get("cheque_no")+"'"
						+ "  AND BCM_CHQ_DD_DT = TO_DATE('"+(String)object.get("cheque_date")+"','DD/MM/YYYY') "
						//+ "  AND BCM_DRAWN_BANK = 'NOBANK' "
						+ "  AND BCM_PYMNT_MD = 'CHQ' "
						+ "  AND EXISTS "
						+ "  (SELECT 'X' FROM INITIAL_RCPT_PYMNT_HRT "
						+ "   WHERE IRPH_BCM_CHQ_DD_NO = BCM_CHQ_DD_NO AND IRPH_BCM_CHQ_DD_DT = BCM_CHQ_DD_DT AND IRPH_BCM_DRAWN_BANK = BCM_DRAWN_BANK AND IRPH_STATUS = '1')";
				
				String rcpt_query = " UPDATE INITIAL_RCPT_PYMNT_HRT SET IRPH_CANCEL_FLG = 'Y' "
						          + " WHERE "
						          + " IRPH_BCM_CHQ_DD_NO = '"+(String)object.get("cheque_no")+"' AND "
						          + " IRPH_BCM_CHQ_DD_DT = TO_DATE('"+(String)object.get("cheque_date")+"','DD/MM/YYYY') AND "
						       //   + " IRP_BCM_DRAWN_BANK = 'NOBANK' AND "
						          + "  IRPH_PYMNT_MD = 'CHQ' AND IRPH_STATUS = '1' " ;
	;
				
				
				
				getreceiptPS = dbConnection.prepareStatement(query);
				int cancel_count = getreceiptPS.executeUpdate();
				
				if(cancel_count > 0) {

					getreceiptPS.close();
					
					getreceiptPS = dbConnection.prepareStatement(rcpt_query);
					int cheque_cancel_count = getreceiptPS.executeUpdate();
					
					if(cheque_cancel_count > 0) {
						AckObj.put("message", "Cheque Successfully Cancelled for cheque No : "+(String)object.get("cheque_no"));
						AckObj.put("status", "success");
						dbConnection.commit();
					}else {
						AckObj.put("status", "error");
						AckObj.put("message", "Cannot Cancel Cheque !!!");
						try {
							dbConnection.rollback();
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
					}
				}else {
					AckObj.put("status", "error");
					AckObj.put("message", "Cannot Cancel. !!!");
					try {
						dbConnection.rollback();
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
			} catch (Exception e) {
				AckObj.put("status", "failure");
				try {
					dbConnection.rollback();
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptRS);
				DBManagerResourceRelease.close(getreceiptPS);
			}
		return AckObj;
	}

	@Override
	public JSONObject uploadmanualreceipts(JSONObject object) {
		// TODO Auto-generated method stub
		CallableStatement getreceiptCS = null;
		ResultSet getreceiptRS = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		JSONObject jsonResponse = new JSONObject();
		
		try {
			
			if(!object.isEmpty()){
				
				String conn_type = (String)object.get("conn_type");
				
				if(conn_type.equals("LT") || conn_type == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT") || conn_type == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				boolean check_dcb_done = false, check_receipts_uploaded = false;
				
				String Query = " SELECT COUNT(1) CNT FROM dual WHERE TO_DATE('"+(String)object.get("receipt_date")+"', 'dd/mm/yyyy') > "+
		                	   " (SELECT NVL(MAX(LAST_DAY(TO_DATE('01-'||LMS_MONTH,'dd-mon-yyyy'))), "+ 
		                	   " TO_DATE('"+(String)object.get("receipt_date")+"', 'dd/mm/yyyy')-1) FROM LDGR_MONTH_STS)";
						
				
				System.out.println(Query);
					ps = dbConnection.prepareStatement(Query) ; 
					rs = ps.executeQuery();
					
					if(rs.next()) {
						if(rs.getInt("CNT") > 0) {
							check_dcb_done = true;
						}
					}
					
				Query = "SELECT COUNT(*) FROM INITIAL_RCPT_PYMNT_HRT "+
						" WHERE BIR_RCPT_DT = TO_DATE('"+(String)object.get("receipt_date")+"', 'dd/mm/yyyy') "+  
						" AND BIR_CASH_COUNTR_NO = '"+(String)object.get("counter_number")+"' AND NVL(BIR_UPD_STATUS,'N') = 'Y'";
				
				System.out.println(Query);
					ps1 = dbConnection.prepareStatement(Query) ; 
					rs1 = ps.executeQuery();
					
					if(rs1.next()) {
						if(rs1.getInt("CNT") > 0) {
							check_receipts_uploaded = true;
						}
					}
					
					
					if(!check_dcb_done) {
						
						if(!check_receipts_uploaded) {
							
							// Upload Receipts logic to be included.
							getreceiptCS = dbConnection.prepareCall(DBQueries.UPLOAD_MANUAL_RECEIPTS);
							getreceiptCS.registerOutParameter(1,OracleTypes.CURSOR);
							getreceiptCS.setString(2,(String)object.get("receipt_date"));
							getreceiptCS.setString(3,(String)object.get("counter_number"));
							getreceiptCS.setString(4, (String)object.get("user_id"));
							getreceiptCS.executeUpdate();
							getreceiptRS = (ResultSet) getreceiptCS.getObject(1);
							
							String result_sts = null ;
							String result_msg = null ;
							
							if(getreceiptRS.next()) {
								result_sts = (String)getreceiptRS.getString("RESP");
								result_msg = (String)getreceiptRS.getString("MESSAGE");
							}
							
							jsonResponse.put("status", result_sts);
							jsonResponse.put("message", result_msg);
							
							
						}else {
							jsonResponse.put("status", "error");
							jsonResponse.put("message", "Records Already Uploaded for Selected Date and Counter No .");
						}
						
					}else {
						jsonResponse.put("status", "error");
						jsonResponse.put("message", "Upload Failed! DCB is already generated for this date.");
					}
					
					
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Invalid Inputs ");
			}
		} catch (SQLException e) {
			System.out.println("Exception thrown " + e);
			jsonResponse.put("status", "error");
			jsonResponse.put("message", "Database Query / Runtime Error .");
		} catch (Exception e) {
			e.printStackTrace();
			jsonResponse.put("status", "error");
			jsonResponse.put("message", " Database Query / Runtime Error.");
		} finally {
			DBManagerResourceRelease.close(rs, ps);
			DBManagerResourceRelease.close(rs1, ps1);
			//DBManagerResourceRelease.close(rs, ps, dbConnection);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getprocessdetails(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement getreceiptPS = null;
		ResultSet getreceiptRS = null;
		JSONObject AckObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		try {
				//dbConn=dbObject.getDatabaseConnection();
			String conn_type = (String)object.get("conn_type");
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			String query =  " SELECT COUNT(9)  NO_OF_RR_NO, CM_MTR_RDR_CD,NVL(CB_RR_STS, 'N') CB_RR_STS,"
					+ " 	TO_CHAR(NVL(DECODE(CB_HHD_BILL_DT,NULL,CB_BILL_DT, CB_HHD_BILL_DT),TO_DATE('01/01/1701','DD/MM/YYYY')),'DD/MM/YYYY') CB_MRI_BILL_DT "
					+ "		FROM Cust_MASTER, Cust_BILL "
					+ " 	WHERE CM_MTR_RDG_DAY = "+(String)object.get("readingday")+" AND CB_RR_NO	=	CM_RR_NO AND "
					+ " 	NVL(CB_RR_STS, 'N') <>'N' AND CM_CONSMR_STS IN ('1','9','10') "
					+ "		GROUP BY CM_MTR_RDR_CD,CB_RR_STS,NVL(CB_RR_STS, 'N'),"
					+ "		TO_CHAR(NVL(DECODE(CB_HHD_BILL_DT,NULL,CB_BILL_DT, CB_HHD_BILL_DT),TO_DATE('01/01/1701','DD/MM/YYYY')),'DD/MM/YYYY') "
					+ " 	ORDER BY CM_MTR_RDR_CD " ; 
			
			System.out.println(query);
				
				getreceiptPS = dbConnection.prepareStatement(query);
				getreceiptRS = getreceiptPS.executeQuery();
				
				while(getreceiptRS.next()) {
					
					JSONObject json = new JSONObject();
					
					json.put("NO_OF_RR_NO",getreceiptRS.getString("NO_OF_RR_NO"));
					json.put("CB_RR_STS", getreceiptRS.getString("CB_RR_STS"));
					json.put("CB_MRI_BILL_DT", getreceiptRS.getString("CB_MRI_BILL_DT"));
					json.put("CM_MTR_RDR_CD", getreceiptRS.getString("CM_MTR_RDR_CD"));
					
					jsonArray.add(json);
					
				}
				
				if(!jsonArray.isEmpty()) {
					AckObj.put("status", "success");
					AckObj.put("message", "Process Details List");
					AckObj.put("process_list",jsonArray);
				}else {
					AckObj.put("status", "fail");
					AckObj.put("message", "No Records !!!");
					AckObj.put("process_list",jsonArray);
					
				}
				
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptRS);
				DBManagerResourceRelease.close(getreceiptPS);
			}
		return AckObj;
	}

	@Override
	public JSONObject getrrnumberdetails(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement getreceiptPS = null;
		ResultSet getreceiptRS = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject AckObj = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		
		try {
				//dbConn=dbObject.getDatabaseConnection();
			String conn_type = (String)object.get("conn_type");
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			boolean validate_dates_sts = false;
			String validate_dates = " SELECT NVL(ROUND(SYSDATE - TO_DATE('"+(String)object.get("billdate")+"','DD/MM/YYYY'),2),0) FROM dual";
			
			ps = dbConnection.prepareStatement(validate_dates);
			rs = ps.executeQuery();
			
			if(rs.next()) {
				double val =  Double.parseDouble(rs.getString(1)) ;
				if(val < 1)
				{
					validate_dates_sts = false;
				}
				else
				{
					validate_dates_sts = true;
				}
			}
			
			if(validate_dates_sts) {
				String query = "SELECT SUBSTR(CM_RR_NO,8) CM_RR_NO, NVL(CB_RR_STS, 'N') CB_RR_STS,CM_MTR_RDR_CD, "
						+ "		TO_CHAR(NVL(DECODE(CB_HHD_BILL_DT,NULL,CB_BILL_DT, CB_HHD_BILL_DT),TO_DATE('01/01/1701','DD/MM/YYYY')),'DD/MM/YYYY') CB_MRI_BILL_DT "
						+ " 	 FROM Cust_MASTER, Cust_BILL "
						+ " 	 WHERE CM_MTR_RDG_DAY = '"+(((String)object.get("billdate")).split("/"))[0]+"' AND CM_CONSMR_STS IN ('1','9','10') AND "
						+ "      CM_MTR_RDR_CD = '"+(String)object.get("meterreadercode")+"' AND NVL(CB_RR_STS, 'N') <>'N' AND "
						+ "     	CB_RR_NO = CM_RR_NO  " ; 
				
				System.out.println(query);
					
					getreceiptPS = dbConnection.prepareStatement(query);
					getreceiptRS = getreceiptPS.executeQuery();
					
					while(getreceiptRS.next()) {
						
						JSONObject json = new JSONObject();
						
						json.put("CM_RR_NO",getreceiptRS.getString("CM_RR_NO"));
						json.put("CB_RR_STS", getreceiptRS.getString("CB_RR_STS"));
						json.put("CM_MTR_RDR_CD", getreceiptRS.getString("CM_MTR_RDR_CD"));
						json.put("CB_MRI_BILL_DT", getreceiptRS.getString("CB_MRI_BILL_DT"));
						
						jsonArray.add(json);
						
					}
					
					if(!jsonArray.isEmpty()) {
						AckObj.put("status", "success");
						AckObj.put("message", "Process RR Number List");
						AckObj.put("process_rrnumber_list",jsonArray);
					}else {
						AckObj.put("status", "fail");
						AckObj.put("message", "No Records !!!");
						AckObj.put("process_rrnumber_list",jsonArray);
						
					}
			}else {
				AckObj.put("status", "fail");
				AckObj.put("message", "Cannot Reset Todays And Tommorows Records !!!");
				AckObj.put("process_rrnumber_list",jsonArray);
			}
			

				
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(getreceiptRS);
				DBManagerResourceRelease.close(getreceiptPS);
			}
		return AckObj;
	}

	@Override
	public JSONObject doprocessmrreset(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jsonresponse = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		CallableStatement cal = null;
		
		try {
			String conn_type = (String)object.get("conn_type");
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			if(((String)object.get("status")) != "B") {
				
				String update_query = " UPDATE CUST_BILL SET CB_RR_STS='B' " +
									  " WHERE CB_RR_STS='"+(String)object.get("status") +"' " +
									  " AND CB_BILL_DT=TO_DATE('"+(String)object.get("bill_date")+"','DD/MM/YYYY') " +
									  " AND CB_RR_NO IN(SELECT CM_RR_NO FROM CUST_MASTER WHERE CM_MTR_RDR_CD='"+(String)object.get("meter_reader_code")+"' "+"  "
									  		+ "	AND CM_MTR_RDG_DAY='"+(((String)object.get("bill_date")).split("/"))[0]+"')";
				
				System.out.println(update_query);
				ps = dbConnection.prepareStatement(update_query);
				int update_result = ps.executeUpdate();
				
				if(update_result > 0) {
					
					cal=dbConnection.prepareCall("{call PROC_RESET_MR(?,?,?)}");
					
					cal.setString(1, ConvertIFNullToString(object.getString("meter_reader_code")));
					cal.setString(2, ConvertIFNullToString(object.getString("bill_date")));
					cal.registerOutParameter(3, OracleTypes.VARCHAR);
					
					cal.execute();
					String val = cal.getString(3).toString().trim();
					if(val.equals("TRUE"))
					{
						jsonresponse.put("status", "success");
						jsonresponse.put("message", "MR Reset is successfull.");
					}
					else
					{
						jsonresponse.put("status", "error");
						jsonresponse.put("message", "MR Reset failed !!!");
					}
				}else {
					jsonresponse.put("status", "error");
					jsonresponse.put("message", "MR Reset failed due to bill-status!!!");
				}
			}else {
				jsonresponse.put("status", "error");
				jsonresponse.put("message", "MR Reset failed !!!");
			}
			} catch (Exception e) {
				jsonresponse.put("status", "failure");
				e.printStackTrace();
				jsonresponse.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(rs);
				DBManagerResourceRelease.close(ps);
				DBManagerResourceRelease.close(cal);
			}
		return jsonresponse;
	}

	@Override
	public JSONObject doprocessrrreset(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jsonresponse = new JSONObject();
		JSONArray jsonArray = new JSONArray();
		CallableStatement cal = null;
		
		try {
			String conn_type = (String)object.get("conn_type");
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			if(((String)object.get("status")) != "B") {
				
				String update_query = " UPDATE CUST_BILL  " +
									  " SET CB_RR_STS='B' " +
						              " WHERE CB_RR_NO ='"+ (String)object.get("rrno")+"' AND CB_RR_STS='"+(String)object.get("status")+"'";
				
				System.out.println(update_query);
				ps = dbConnection.prepareStatement(update_query);
				int update_result = ps.executeUpdate();
				
				if(update_result > 0) {
					
					cal=dbConnection.prepareCall("{call PROC_RESET_RR(?,?,?,?)}");
					
					cal.setString(1, ConvertIFNullToString(object.getString("meter_reader_code")));
					cal.setString(2, ConvertIFNullToString(object.getString("bill_date")));
					cal.setString(3, ConvertIFNullToString(object.getString("rrno")));
					cal.registerOutParameter(4, OracleTypes.VARCHAR);
					
					cal.execute();
					String val = cal.getString(4).toString().trim();
					if(val.equals("TRUE"))
					{
						jsonresponse.put("status", "success");
						jsonresponse.put("message", "RR Reset is successfull.");
					}
					else
					{
						jsonresponse.put("status", "error");
						jsonresponse.put("message", "RR Reset failed !!!");
					}
				}else {
					jsonresponse.put("status", "error");
					jsonresponse.put("message", "RR Reset failed due to updating bill-status!!!");
				}
			}else {
				jsonresponse.put("status", "error");
				jsonresponse.put("message", "RR Reset failed due to bill-status!!!");
			}
			} catch (Exception e) {
				jsonresponse.put("status", "failure");
				e.printStackTrace();
				jsonresponse.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(rs);
				DBManagerResourceRelease.close(ps);
				DBManagerResourceRelease.close(cal);
			}
		return jsonresponse;
	}
	
	
}
