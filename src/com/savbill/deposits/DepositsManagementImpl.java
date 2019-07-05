package com.savbill.deposits;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.savbill.database.DBManagerIMPL;
import com.savbill.database.DBManagerResourceRelease;
import com.savbill.database.DBQueries;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

public class DepositsManagementImpl implements IDepositsManagement {
	
	// database connection initialize
	DBManagerIMPL databaseObj = new DBManagerIMPL();
	Connection dbConnection;
	
	@Override
	public JSONObject getCustomerDeposits(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = object.getString("conn_type");
		
		try {

			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			accountsCS=dbConnection.prepareCall(DBQueries.GET_DEPOSITS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("rr_number"));
			accountsCS.setString(3, object.getString("Ledger_number"));
			accountsCS.setString(4, object.getString("folio_number"));
			accountsCS.setString(5, object.getString("om_code"));
			accountsCS.setString(6, object.getString("mr_code"));
			accountsCS.setString(7, object.getString("reading_day"));
			accountsCS.registerOutParameter(8, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(8);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("tariff", accountsRS.getString("tariff"));
				ackobj.put("ldgr_no", accountsRS.getString("ldgr_no"));
				ackobj.put("folio_no", accountsRS.getString("folio_no"));
				ackobj.put("om_cd", accountsRS.getString("om_cd"));
				ackobj.put("mr_cd", accountsRS.getString("mr_cd"));
				ackobj.put("rdg_day", accountsRS.getString("rdg_day"));
				ackobj.put("mmd", accountsRS.getString("mmd"));
				ackobj.put("msd", accountsRS.getString("msd"));
				ackobj.put("ymd", accountsRS.getString("ymd"));
				ackobj.put("tot_dep", accountsRS.getString("tot_dep"));


				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Customer Deposits Retrieved !!!");
				obj.put("customer_deposits", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getCustomerDepositsDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = object.getString("conn_type");
		
		try {
			
			dbConnection = databaseObj.getDatabaseConnection();
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			accountsCS=dbConnection.prepareCall(DBQueries.GET_DEPOSITS_DETAILS);
			accountsCS.setString(1, object.getString("rr_number"));
			accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(2);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("deposit_amount", accountsRS.getString("deposit_amount"));
				ackobj.put("rcpt_no", accountsRS.getString("rcpt_no"));
				ackobj.put("rcpt_dt", accountsRS.getString("rcpt_dt"));
				ackobj.put("counter_no", accountsRS.getString("counter_no"));
				ackobj.put("counter_name", accountsRS.getString("counter_name"));
				ackobj.put("pymnt_purpose", accountsRS.getString("pymnt_purpose"));
				ackobj.put("pymnt_purpose_decription", accountsRS.getString("pymnt_purpose_decription"));
				ackobj.put("adj_sts", accountsRS.getString("adj_sts"));
				ackobj.put("adj_sts_description", accountsRS.getString("adj_sts_description"));
				ackobj.put("remarks", accountsRS.getString("remarks"));
				ackobj.put("dr_acct_cd", accountsRS.getString("dr_acct_cd"));
				ackobj.put("cr_acct_cd", accountsRS.getString("cr_acct_cd"));
				ackobj.put("adjusted_bill_no", accountsRS.getString("adjusted_bill_no"));
				ackobj.put("adjusted_bill_dt", accountsRS.getString("adjusted_bill_dt"));
				ackobj.put("valid_to_dt", accountsRS.getString("valid_to_dt"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Customer Deposits Details Retrieved !!!");
				obj.put("customer_deposit_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getDepositInstrestAndNmmdParameters(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = object.getString("conn_type");
		String location_code = object.getString("location_code");
		String type = object.getString("type");
		
		try {
			if(!location_code.isEmpty() && !type.isEmpty()){
				
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			accountsCS = dbConnection.prepareCall(DBQueries.GET_DEP_INT_NMMD_PARAMETERS);
			accountsCS.setString(1, location_code);
			accountsCS.setString(2, type);
			
			accountsCS.registerOutParameter(3, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			
			accountsRS = (ResultSet) accountsCS.getObject(3);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("year", accountsRS.getString("year"));
				ackobj.put("key", accountsRS.getString("year"));
				ackobj.put("value", accountsRS.getString("year"));
				ackobj.put("from_dt", accountsRS.getString("from_dt"));
				ackobj.put("to_dt", accountsRS.getString("to_dt"));
				ackobj.put("status", accountsRS.getString("status"));
				ackobj.put("nmmd", accountsRS.getString("nmmd"));
				ackobj.put("prompt_nmmd", accountsRS.getString("prompt_nmmd"));
				ackobj.put("sec_dep_int_percent", accountsRS.getString("sec_dep_int_percent"));
				ackobj.put("sec_dep_int_tds_maxamt", accountsRS.getString("sec_dep_int_tds_maxamt"));
				ackobj.put("sec_dep_int_tds_percent", accountsRS.getString("sec_dep_int_tds_percent"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("deposit_instrest_nmmd_parameters", array);
			}
		}else{
			obj.put("status", "error");
			obj.put("message", "Please Enter Valid Input");
		}		
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
		}
		
		return obj;
	
	}
	
	@Override
	public JSONObject getDepositInstrestYear(JSONObject object) {
			
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = object.getString("conn_type");
		String location_code = object.getString("location_code");
		String type = object.getString("type");
		
		try {
			if(!location_code.isEmpty() && !type.isEmpty()){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				accountsCS=dbConnection.prepareCall(DBQueries.GET_DEP_INT_NMMD_YEAR);
				accountsCS.setString(1, location_code);
				accountsCS.setString(2, type);
				accountsCS.registerOutParameter(3, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(3);
				
				while(accountsRS.next()){
					
					JSONObject ackobj=new JSONObject();
					
					ackobj.put("key", accountsRS.getString("key"));
					ackobj.put("value", accountsRS.getString("value"));
	
					array.add(ackobj);
				}
				if(array.isEmpty()) {
					obj.put("status", "success");
					obj.put("message", "No Records Found !!!");
				} else{
					obj.put("status", "success");
					obj.put("depint_mmd_year_details", array);
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter Valid Input");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getPendingDepositIntrestDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = object.getString("conn_type");
		
		try {

			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}			
			accountsCS=dbConnection.prepareCall(DBQueries.GET_DEPINT_DETLS_FOR_APPROVE);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("year"));
			accountsCS.setString(3, object.getString("mr_cd"));
			accountsCS.setString(4, object.getString("rdg_day"));
			
			accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(5);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("md_cd", accountsRS.getString("md_cd"));
				ackobj.put("rdg_day", accountsRS.getString("rdg_day"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("dep_dt", accountsRS.getString("dep_dt"));
				ackobj.put("total_deposit", accountsRS.getString("total_deposit"));
				ackobj.put("intrst_rate", accountsRS.getString("intrst_rate"));
				ackobj.put("deposit_interest", accountsRS.getString("deposit_interest"));
				ackobj.put("credited_amt", accountsRS.getString("credited_amt"));
				ackobj.put("credited_dt", accountsRS.getString("credited_dt"));
				ackobj.put("balance_deposit_int", accountsRS.getString("balance_deposit_int"));
				ackobj.put("user_id", accountsRS.getString("user_id"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));
				
				array.add(ackobj);
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Pending Deposit Interest Details Retrieved for Approval!!!");
				obj.put("pending_deposit_int_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject approveDepositsIntrest(JSONObject data) {
		
		String approverid = (String) data.get("approverid");
		String conn_type = (String) data.get("conn_type");
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("data");
		    	
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		AckObj = approveIndividualDepositIntrest(objects, conn_type, approverid);
			    }
		    	return AckObj;
			}
		} catch (Exception e) {
			AckObj.put("status", "error");
			e.printStackTrace();
			AckObj.put("message", "Insert failed");
		}
		
		return AckObj;
	}
	
	public JSONObject approveIndividualDepositIntrest(JSONObject object, String conn_type, String approverid) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("full_rr_no");
		
		try {
			if(!rr_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				accountsCS=dbConnection.prepareCall(DBQueries.DEPOSIT_INT_CREDIT_APPROVE);
				accountsCS.setString(1, rr_no);
				accountsCS.setString(2, (String) object.get("dep_dt"));
				accountsCS.setString(3, approverid);
				
				accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(4);
					
				if(accountsRS.next()){
					String RESP = accountsRS.getString("RESP");
					System.out.println("RESP : "+RESP);
					
					if(RESP.equalsIgnoreCase("success")){
						obj.put("status", "success");
						obj.put("message", " Deposit Intrest Approved successfully");
					}else if (RESP.equalsIgnoreCase("fail")) {
						obj.put("status", "error");
						obj.put("message", " Deposit Intres  Failed.");
					}else {
						obj.put("status", "error");
						obj.put("message", RESP);
					}						
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid RR Number");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
		}
		
		return obj;
	}

}
