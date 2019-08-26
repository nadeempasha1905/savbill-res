package com.savbill.accounts;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;


import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

import com.savbill.database.DBManagerIMPL;
import com.savbill.database.DBManagerResourceRelease;
import com.savbill.database.DBQueries;
import com.savbill.util.ReferenceUtil;

public class AccountManagementImpl implements IAccountManagement {

	// database connection initialize
	DBManagerIMPL databaseObj = new DBManagerIMPL();
	Connection dbConnection;
	
	@Override
	public JSONObject getBills(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String location_code = (String) object.get("location_code");
		String rr_no = (String) object.get("rr_number");
		String bill_no = (String)object.get("bill_number");
		String bill_date = (String)object.get("bill_date");
		String conn_type = object.getString("conn_type");
		
		try {
			if(!rr_no.isEmpty() || !bill_no.isEmpty() || !bill_date.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}

				accountsCS=dbConnection.prepareCall(DBQueries.GET_BILLS);
				accountsCS.setString(1, location_code);
				accountsCS.setString(2, rr_no);
				accountsCS.setString(3, bill_date);
				accountsCS.setString(4, bill_no);
				accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(5);
				while(accountsRS.next())
				{
					JSONObject ackobj=new JSONObject();
					ackobj.put("row_no", accountsRS.getString("row_no"));
					ackobj.put("rr_no", accountsRS.getString("rr_no"));
					ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
					ackobj.put("tariff", accountsRS.getString("tariff"));
					ackobj.put("bill_type", accountsRS.getString("bill_type"));
					ackobj.put("consmr_sts", accountsRS.getString("consmr_sts"));
					ackobj.put("bill_no", accountsRS.getString("bill_no"));
					ackobj.put("bill_date", accountsRS.getString("bill_date"));
					ackobj.put("bill_due_date", accountsRS.getString("bill_due_date"));
					ackobj.put("bill_period_from", accountsRS.getString("bill_period_from"));
					ackobj.put("bill_period_to", accountsRS.getString("bill_period_to"));
					ackobj.put("mtr_rdr_cd", accountsRS.getString("mtr_rdr_cd"));
					ackobj.put("mtr_rdg_day", accountsRS.getString("mtr_rdg_day"));
					ackobj.put("bill_gen_mode", accountsRS.getString("bill_gen_mode"));
					ackobj.put("bill_kw", accountsRS.getString("bill_kw"));
					ackobj.put("bill_hp", accountsRS.getString("bill_hp"));
					ackobj.put("bill_kva", accountsRS.getString("bill_kva"));
					ackobj.put("bill_md", accountsRS.getString("bill_md"));
					ackobj.put("bill_pf", accountsRS.getString("bill_pf"));
					ackobj.put("prev_reading", accountsRS.getString("prev_reading"));
					ackobj.put("pres_reading", accountsRS.getString("pres_reading"));
					ackobj.put("mult_const", accountsRS.getString("mult_const"));
					ackobj.put("error_crep", accountsRS.getString("error_crep"));
					ackobj.put("unit_consmp", accountsRS.getString("unit_consmp"));
					ackobj.put("fl_consmr", accountsRS.getString("fl_consmr"));
					ackobj.put("fl_units", accountsRS.getString("fl_units"));
					ackobj.put("line_min", accountsRS.getString("line_min"));
					ackobj.put("bill_amt", accountsRS.getString("bill_amt"));
					ackobj.put("paid_amt", accountsRS.getString("paid_amt"));
					ackobj.put("credit_amt", accountsRS.getString("credit_amt"));
					ackobj.put("paid_date", accountsRS.getString("paid_date"));
					ackobj.put("bal_amt", accountsRS.getString("bal_amt"));
					ackobj.put("rebate_amt", accountsRS.getString("rebate_amt"));
					ackobj.put("wdrl_amt", accountsRS.getString("wdrl_amt"));
					ackobj.put("arr_bill_no", accountsRS.getString("arr_bill_no"));
					ackobj.put("arr_bill_date", accountsRS.getString("arr_bill_date"));
					ackobj.put("ct_ratio_avail", accountsRS.getString("ct_ratio_avail"));
					ackobj.put("pt_ratio_avail", accountsRS.getString("pt_ratio_avail"));
					ackobj.put("ct_ratio_num", accountsRS.getString("ct_ratio_num"));
					ackobj.put("pt_ratio_num", accountsRS.getString("pt_ratio_num"));
					ackobj.put("ct_ratio_den", accountsRS.getString("ct_ratio_den"));
					ackobj.put("pt_ratio_den", accountsRS.getString("pt_ratio_den"));
					ackobj.put("energy_entlment", accountsRS.getString("energy_entlment"));
					ackobj.put("demand_entlment", accountsRS.getString("demand_entlment"));
					ackobj.put("trf_chng_flg", accountsRS.getString("trf_chng_flg"));
					ackobj.put("bill_user", accountsRS.getString("bill_user"));
					ackobj.put("bill_tmpstp", accountsRS.getString("bill_tmpstp"));
					
					array.add(ackobj);
					
				}
				if(array.isEmpty()) {
					obj.put("status", "success");
					obj.put("message", "No Records Found !!!");
				} else{
					obj.put("status", "success");
					obj.put("message", "Bill Details Retrieved !!!");
					obj.put("bills", array);
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter atleast one of RR Number OR Bill Number or Bill Date");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getBillBreakup(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_number");
		String bill_date = (String)object.get("bill_date");
		String bill_no = (String)object.get("bill_number");
		String conn_type = object.getString("conn_type");
		
		try {
			if(!rr_no.isEmpty() || !bill_no.isEmpty() || !bill_date.isEmpty() ){

				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				accountsCS=dbConnection.prepareCall(DBQueries.GET_BILL_BREAKUP);
				accountsCS.setString(1, rr_no);
				accountsCS.setString(2, bill_date);
				accountsCS.setString(3, bill_no);
				accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(4);
				while(accountsRS.next())
				{
					JSONObject ackobj=new JSONObject();
					
					ackobj.put("row_id", accountsRS.getString("row_id"));
					ackobj.put("rr_no", accountsRS.getString("rr_no"));
					ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
					ackobj.put("bill_dt", accountsRS.getString("bill_dt"));
					ackobj.put("bill_no", accountsRS.getString("bill_no"));
					ackobj.put("chrg_cd", accountsRS.getString("chrg_cd"));
					ackobj.put("chrg_cd_descr", accountsRS.getString("chrg_cd_descr"));
					ackobj.put("billed_amt", accountsRS.getString("billed_amt"));
					ackobj.put("rbt_flg", accountsRS.getString("rbt_flg"));
					ackobj.put("rbt_amt", accountsRS.getString("rbt_amt"));
					ackobj.put("wdrls_amt", accountsRS.getString("wdrls_amt"));
					ackobj.put("total_amt", accountsRS.getString("total_amt"));
					ackobj.put("paid_amt", accountsRS.getString("paid_amt"));

					array.add(ackobj);
					
				}
				if(array.isEmpty()) {
					//no tasks for user
					obj.put("status", "success");
					obj.put("message", "No Records Found !!!");
				} else{
					obj.put("status", "success");
					obj.put("message", "Bill Breakup Details Retrieved !!!");
					obj.put("bill_breakup", array);
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter atleast one of RR Number OR Bill Number or Bill Date");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getBillBreakupSlabs(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_number");
		String bill_date = (String)object.get("bill_date");
		String bill_no = (String)object.get("bill_number");
		String charge_code = (String)object.get("charge_code");
		String conn_type = object.getString("conn_type");
		
		try {
			if(!rr_no.isEmpty() || !bill_no.isEmpty() || !bill_date.isEmpty() ){

				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				accountsCS=dbConnection.prepareCall(DBQueries.GET_BILL_BREAKUP_SLABS);
				accountsCS.setString(1, rr_no);
				accountsCS.setString(2, bill_date);
				accountsCS.setString(3, bill_no);
				accountsCS.setString(4, charge_code);
				accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(5);
				while(accountsRS.next())
				{
					JSONObject ackobj=new JSONObject();
					
					ackobj.put("row_id", accountsRS.getString("row_id"));
					ackobj.put("rr_no", accountsRS.getString("rr_no"));
					ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
					ackobj.put("bill_dt", accountsRS.getString("bill_dt"));
					ackobj.put("bill_no", accountsRS.getString("bill_no"));
					ackobj.put("slab_id", accountsRS.getString("slab_id"));
					ackobj.put("salb_unit", accountsRS.getString("salb_unit"));
					ackobj.put("slab_rate", accountsRS.getString("slab_rate"));
					ackobj.put("slab_amount", accountsRS.getString("slab_amount"));

					array.add(ackobj);
					
				}
				if(array.isEmpty()) {
					//no tasks for user
					obj.put("status", "success");
					obj.put("message", "No Records Found !!!");
				} else{
					obj.put("status", "success");
					obj.put("message", "Bill Breakup Slab Details Retrieved !!!");
					obj.put("bill_breakup_slabs", array);
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter atleast one of RR Number OR Bill Number or Bill Date");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getBillReceiptBreakupSlabs(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_number");
		String bill_date = (String)object.get("bill_date");
		String bill_no = (String)object.get("bill_number");
		String charge_code = (String)object.get("charge_code");
		String conn_type = object.getString("conn_type");
		
		try {
			if(!rr_no.isEmpty() || !bill_no.isEmpty() || !bill_date.isEmpty() ){

				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				accountsCS=dbConnection.prepareCall(DBQueries.GET_BILL_RECEIPT_BREAKUP_SLABS);
				accountsCS.setString(1, rr_no);
				accountsCS.setString(2, bill_date);
				accountsCS.setString(3, bill_no);
				accountsCS.setString(4, charge_code);
				accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(5);
				while(accountsRS.next())
				{
					JSONObject ackobj=new JSONObject();
					
					ackobj.put("row_id", accountsRS.getString("row_id"));
					ackobj.put("rr_no", accountsRS.getString("rr_no"));
					ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
					ackobj.put("bill_dt", accountsRS.getString("bill_dt"));
					ackobj.put("crb_bill_no", accountsRS.getString("crb_bill_no"));
					ackobj.put("chrg_cd", accountsRS.getString("chrg_cd"));
					ackobj.put("amt_paid", accountsRS.getString("amt_paid"));
					ackobj.put("rcpt_dt", accountsRS.getString("rcpt_dt"));
					ackobj.put("rcpt_detl", accountsRS.getString("rcpt_detl"));
					ackobj.put("rcpt_table", accountsRS.getString("rcpt_table"));
					ackobj.put("rcpt_user", accountsRS.getString("rcpt_user"));
					ackobj.put("rcpt_tmpstp", accountsRS.getString("rcpt_tmpstp"));


					array.add(ackobj);
					
				}
				if(array.isEmpty()) {
					//no tasks for user
					obj.put("status", "success");
					obj.put("message", "No Records Found !!!");
				} else{
					obj.put("status", "success");
					obj.put("message", "Bill Receipt Breakup Slab Details Retrieved !!!");
					obj.put("bill_receipt_breakup_slabs", array);
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter atleast one of RR Number OR Bill Number or Bill Date");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	

	@Override
	public JSONObject getListBillDetails(JSONObject object){

		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_number");
		String conn_type = object.getString("conn_type");
		
		try {
			if(!rr_no.isEmpty()){

				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				accountsCS=dbConnection.prepareCall(DBQueries.GET_BILLS_LIST);
				accountsCS.setString(1, (String) object.get("location_code"));
				accountsCS.setString(2, rr_no);
				accountsCS.registerOutParameter(3, OracleTypes.CURSOR);
				accountsCS.setString(4, (String)object.get("no_of_months"));
				accountsCS.setString(5, (String)object.get("fromdate"));
				accountsCS.setString(6, (String)object.get("todate"));
				accountsCS.setString(7, (String)object.get("om_code"));
				accountsCS.setString(8, (String)object.get("tariffs"));
				accountsCS.setString(9, (String)object.get("mrcode"));
				accountsCS.setString(10, (String)object.get("reading_day"));
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(3);
				while(accountsRS.next())
				{
					JSONObject ackobj=new JSONObject();
					
					ackobj.put("row_num", accountsRS.getString("row_num"));
					ackobj.put("rrno", accountsRS.getString("rrno"));
					ackobj.put("serv_dt", accountsRS.getString("serv_dt"));
					ackobj.put("kw", accountsRS.getString("kw"));
					ackobj.put("hp", accountsRS.getString("hp"));
					ackobj.put("kva", accountsRS.getString("kva"));
					ackobj.put("name", accountsRS.getString("name"));
					ackobj.put("adr1", accountsRS.getString("adr1"));
					ackobj.put("area", accountsRS.getString("area"));
					ackobj.put("ldgr", accountsRS.getString("ldgr"));
					ackobj.put("lf", accountsRS.getString("lf"));
					ackobj.put("mtrslnomake", accountsRS.getString("mtrslnomake"));
					ackobj.put("billdt", accountsRS.getString("billdt"));
					ackobj.put("billno", accountsRS.getString("billno"));
					ackobj.put("trf", accountsRS.getString("trf"));
					ackobj.put("curldkw", accountsRS.getString("curldkw"));
					ackobj.put("ir", accountsRS.getString("ir"));
					ackobj.put("fr", accountsRS.getString("fr"));
					ackobj.put("mlc", accountsRS.getString("mlc"));
					ackobj.put("consp", accountsRS.getString("consp"));
					ackobj.put("bmd", accountsRS.getString("bmd"));
					ackobj.put("bpf", accountsRS.getString("bpf"));
					ackobj.put("obrev", accountsRS.getString("obrev"));
					ackobj.put("obint", accountsRS.getString("obint"));
					ackobj.put("obtax", accountsRS.getString("obtax"));
					ackobj.put("obtot", accountsRS.getString("obtot"));
					ackobj.put("fc", accountsRS.getString("fc"));
					ackobj.put("ec", accountsRS.getString("ec"));
					ackobj.put("int", accountsRS.getString("int"));
					ackobj.put("tax", accountsRS.getString("tax"));
					ackobj.put("dbt", accountsRS.getString("dbt"));
					ackobj.put("rbt", accountsRS.getString("rbt"));
					ackobj.put("wdrl", accountsRS.getString("wdrl"));
					ackobj.put("totdmd", accountsRS.getString("totdmd"));
					ackobj.put("totbal", accountsRS.getString("totbal"));
					ackobj.put("col", accountsRS.getString("col"));
					ackobj.put("rcptdetls", accountsRS.getString("rcptdetls"));
					ackobj.put("credit", accountsRS.getString("credit"));
					ackobj.put("cb", accountsRS.getString("cb"));
					ackobj.put("mmd", accountsRS.getString("mmd"));
					ackobj.put("mmddetls", accountsRS.getString("mmddetls"));
					ackobj.put("msd", accountsRS.getString("msd"));
					ackobj.put("msddetls", accountsRS.getString("msddetls"));
					ackobj.put("mcrmks", accountsRS.getString("mcrmks"));
	
					array.add(ackobj);
					
				}
				if(array.isEmpty()) {
					//no tasks for user
					obj.put("status", "success");
					obj.put("message", "No Records Found !!!");
				} else{
					obj.put("status", "success");
					obj.put("message", "Bills List Retrieved !!!");
					obj.put("bills_list", array);
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter RR Number");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getRRDetailsForBillCancel(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String conn_type = object.getString("conn_type");
		String rr_no = object.getString("rr_no");
		
		try {
			if(!rr_no.isEmpty() ){
				
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			accountsCS = dbConnection.prepareCall(DBQueries.GET_RR_DETAILS_FOR_BILL_CANCEL);
			accountsCS.setString(1, rr_no);
			
			accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			
			accountsRS = (ResultSet) accountsCS.getObject(2);
			
			JSONObject ackobj=new JSONObject();
			if(accountsRS.next()){
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("bill_dt", accountsRS.getString("bill_dt"));
				ackobj.put("bill_no", accountsRS.getString("bill_no"));
				ackobj.put("paid_sts", accountsRS.getString("paid_sts"));
			}
			obj.put("status", "success");
			obj.put("rr_details_for_bill_cancel", ackobj);
			
		}else{
			obj.put("status", "error");
			obj.put("message", "Please Enter RR Number");
		}	
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	
	}
	
	@Override
	public JSONObject cancelBill(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String conn_type = (String) object.get("conn_type");

		String rr_no = (String) object.get("rr_no");
		
		System.out.println(object);
		
		try {
			if(!rr_no.isEmpty()){
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					accountsCS=dbConnection.prepareCall(DBQueries.CANCEL_BILL);
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, (String) object.get("userid"));
					accountsCS.registerOutParameter(3, OracleTypes.CURSOR);
					accountsCS.setString(4, (String) object.get("remarks"));

					accountsCS.executeUpdate();
					
					
					
					accountsRS = (ResultSet) accountsCS.getObject(3);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Bill Cancellation Successful for: " + rr_no.substring(7)) ;
						}else {
							obj.put("status", "error");
							obj.put("message", RESP ) ;
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getSpotFolioDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = object.getString("conn_type");
		String location_code = object.getString("location_code");
		
		try {
			if(!location_code.isEmpty()){
				
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			accountsCS = dbConnection.prepareCall(DBQueries.GET_SPOT_FOLIO_DETAILS);
			accountsCS.setString(1, location_code);
			accountsCS.setString(2, object.getString("mr_cd"));
			accountsCS.setString(3, object.getString("rdg_day"));
			
			accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			
			accountsRS = (ResultSet) accountsCS.getObject(4);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("folio_no", accountsRS.getString("folio_no"));
				ackobj.put("new_folio_no", accountsRS.getString("new_folio_no"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("spot_folio_details", array);
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	
	}
	
	@Override
	public JSONObject upsertSpotFolioDetails(JSONObject data) {
		
		String userid = (String) data.get("userid");
		
		String conn_type = (String) data.get("conn_type");
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("data");
		    	
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		AckObj = upsertIndividualSpotFolioDetails(objects, userid, conn_type);
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
	
	
	public JSONObject upsertIndividualSpotFolioDetails(JSONObject object, String userid, String conn_type) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_no");
		String new_folio_no = (String) object.get("new_folio_no");
		
		try {
			if(!rr_no.isEmpty() && !new_folio_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					accountsCS=dbConnection.prepareCall(DBQueries.UPSERT_SPOT_FOLIO_DETAILS);
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, new_folio_no);
					accountsCS.setString(3, userid);
					accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
					
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(4);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Spot Folio Details Updated Successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Spot Folio Details Updation Faild for RR Number:" +rr_no);
						}
						
					}
														
			}else{
				obj.put("status", "error");
				obj.put("message", "Invalid input...!");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}	
	
	@Override
	public JSONObject regenerateSpotFolioDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String conn_type = (String) object.get("conn_type");

		String location_code = (String) object.get("location_code");
		
		System.out.println(object);
		
		try {
			if(!location_code.isEmpty()){
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					accountsCS=dbConnection.prepareCall(DBQueries.REGENERATE_SPOT_FOLIO);
					accountsCS.setString(1, location_code);
					accountsCS.setString(2, (String) object.get("mr_cd"));
					accountsCS.setString(3,  (String) object.get("rdg_day"));
					accountsCS.setString(4, (String) object.get("userid"));
					accountsCS.registerOutParameter(5, OracleTypes.CURSOR);

					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(5);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Folio Re-Generation Successful for: " +(String) object.get("mr_cd")+ " " +(String) object.get("rdg_day") ) ;
						}else {
							obj.put("status", "error");
							obj.put("message", RESP) ;
						}
					}
						
			}else{
				obj.put("status", "error");
				obj.put("message", "Invalid input..!");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getFLDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String location_code = (String) object.get("location_code");
		String rr_no = (String) object.get("rr_number");
		String conn_type = object.getString("conn_type");
		
		try {
			if(!rr_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				accountsCS=dbConnection.prepareCall(DBQueries.GET_FL_DETAILS);
				accountsCS.setString(1, location_code);
				accountsCS.setString(2, rr_no);
				accountsCS.registerOutParameter(3, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(3);
				while(accountsRS.next())
				{
					JSONObject ackobj=new JSONObject();
					
					ackobj.put("row_num", accountsRS.getString("row_num"));
					ackobj.put("rr_no", accountsRS.getString("rr_no"));
					ackobj.put("sl_no", accountsRS.getString("sl_no"));
					ackobj.put("emp_no", accountsRS.getString("emp_no"));
					ackobj.put("emp_name", accountsRS.getString("emp_name"));
					ackobj.put("designation", accountsRS.getString("designation"));
					ackobj.put("designation_descr", accountsRS.getString("designation_descr"));
					ackobj.put("place_of_work", accountsRS.getString("place_of_work"));
					ackobj.put("ref_letter_no", accountsRS.getString("ref_letter_no"));
					ackobj.put("ref_letter_dt", accountsRS.getString("ref_letter_dt"));
					ackobj.put("from_dt", accountsRS.getString("from_dt"));
					ackobj.put("to_dt", accountsRS.getString("to_dt"));
					ackobj.put("remarks", accountsRS.getString("remarks"));
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
					obj.put("message", "Free Lighting Details Retrieved !!!");
					obj.put("fl_details", array);
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject doAddOrUpdateFLDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_no");
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		
		System.out.println(object);
		
		try {
			if(!rr_no.isEmpty() ){

				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_FL_SANCT);
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, "");
					accountsCS.setString(3, (String) object.get("ref_letter_no"));
					accountsCS.setString(4, (String) object.get("ref_letter_dt"));
					accountsCS.setString(5, (String) object.get("emp_name"));
					accountsCS.setString(6, (String) object.get("designation"));
					accountsCS.setString(7, (String) object.get("emp_no"));
					accountsCS.setString(8, (String) object.get("place_of_work"));
					accountsCS.setString(9, (String) object.get("from_dt"));
					accountsCS.setString(10, (String) object.get("to_dt"));
					accountsCS.setString(11, "");
					accountsCS.setString(12, "");
					accountsCS.setString(13, (String) object.get("remarks"));
					accountsCS.setString(14, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(15, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(15);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("ACTIVE_RECORD")){
							obj.put("status", "error");
							obj.put("message", "Active Record Found for Given RR-Number .  ");
						}else if(RESP.equalsIgnoreCase("dup")){
							obj.put("status", "error");
							obj.put("message", "Free Lighting Is Already Sanctioned For Given Date Range !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " FL Sancation :  Record successfully "+(db_option.equals("ADD") ? "  Inserted " : "  Updated"));
							obj.put("sl_no", accountsRS.getString("SL_NO"));
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " FL Sancation :  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else if (RESP.equalsIgnoreCase("DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("MAX_DT"));
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " To Date : " +  accountsRS.getString("TO_DT") + " Should Be Greater Than From Date : "+ accountsRS.getString("FROM_DT"));
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_FL_SANCT);
					accountsCS.setString(1, (String) object.get("rr_no"));
					accountsCS.setString(2,  (String) object.get("sl_no"));
					accountsCS.setString(3,  (String) object.get("ref_letter_no"));
					accountsCS.setString(4,  (String) object.get("ref_letter_dt"));
					accountsCS.setString(5,  (String) object.get("emp_name"));
					accountsCS.setString(6,  (String) object.get("designation"));
					accountsCS.setString(7,  (String) object.get("emp_no"));
					accountsCS.setString(8,  (String) object.get("place_of_work"));
					accountsCS.setString(9,  (String) object.get("from_dt"));
					accountsCS.setString(10,  (String) object.get("to_dt"));
					accountsCS.setString(11,  "");
					accountsCS.setString(12,  "");
					accountsCS.setString(13,  (String) object.get("remarks"));
					accountsCS.setString(14,  (String) object.get("userid"));
					
					
					accountsCS.registerOutParameter(15, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(15);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " FL Sancation :  Record Successfully Updated .");
						}else{
							obj.put("status", "error");
							obj.put("message", " FL Sancation :  Record Updation Failed !!! ");
						}
						
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getChargeCodeDetails(JSONObject object) {
		
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
			
			accountsCS = dbConnection.prepareCall(DBQueries.GET_CHARGE_CODE_DETAILS);
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("key", accountsRS.getString("charge_code"));
				ackobj.put("value", accountsRS.getString("charge_description"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("ChargeCodeList", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	
	}
	
	@Override
	public JSONObject getChargeCodeDetailsForRebateType(JSONObject object) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jsonResponse = new JSONObject();
		
		try {
			
			if(!object.isEmpty()){
				
				String conn_type = (String)object.get("conn_type");
				String rebate_type   = (String)object.get("rebate_type");
				
				if(conn_type.equals("LT") || conn_type == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT") || conn_type == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}

				String Query = "  SELECT RD_CHRG_CD CHRG_CD,BCR_DESCR CHRG_CD_DESCR,RD_REBATE_UNIT REBATE_UNIT,RD_MAX_REBATE MAX_REBATE	"
								+ "FROM REBATE_DETL,PYMNT_ADJ_PRIORITY	"
								+ "WHERE RD_REBATE_CODE = '"+rebate_type+"' "
								+ "AND SYSDATE BETWEEN RD_EFF_FROM_DT AND NVL(RD_EFF_TO_DT,SYSDATE)	"
								+ "AND BCR_CHRG_CD = RD_CHRG_CD  "; 
				
				if(dbConnection != null){
					ps = dbConnection.prepareStatement(Query) ; 
					rs = ps.executeQuery();
					if (rs.next()) {
						jsonResponse.put("status", "success");
						jsonResponse.put("charge_code", ReferenceUtil.ConvertIFNullToString(rs.getString("CHRG_CD_DESCR")));
						jsonResponse.put("charge_unit", ReferenceUtil.ConvertIFNullToString(rs.getString("REBATE_UNIT")));
						jsonResponse.put("max_rebate_amount", ReferenceUtil.ConvertIFNullToString(rs.getString("MAX_REBATE")));
					}
					else{
						jsonResponse.put("status", "error");
						jsonResponse.put("message", "No Records Found For Rebate Type.");
					}
				}else{
					jsonResponse.put("status", "error");
					jsonResponse.put("message", "No Records Found For Rebate Type.");
				}
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Invalid Inputs / No Records Found For Rebate Type.");
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
			//DBManagerResourceRelease.close(rs, ps, dbConnection);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getRebateDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String location_code = (String) object.get("location_code");
		String rr_no = (String) object.get("rr_number");
		String conn_type = object.getString("conn_type");
		
		try {
			if(!rr_no.isEmpty()){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				accountsCS=dbConnection.prepareCall(DBQueries.GET_REBATE_DETAILS);
				accountsCS.setString(1, location_code);
				accountsCS.setString(2, (String) object.get("rebate_description"));
				accountsCS.setString(3, rr_no);
				accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(4);
				while(accountsRS.next())
				{
					JSONObject ackobj=new JSONObject();
					
					ackobj.put("row_num", accountsRS.getString("row_num"));
					ackobj.put("rr_no", accountsRS.getString("rr_no"));
					ackobj.put("rebate_code", accountsRS.getString("rebate_code"));
					ackobj.put("rebate_description", accountsRS.getString("rebate_description"));
					ackobj.put("rebate_eff_from_dt", accountsRS.getString("rebate_eff_from_dt"));
					ackobj.put("rebate_eff_to_dt", accountsRS.getString("rebate_eff_to_dt"));
					ackobj.put("rebate_unit", accountsRS.getString("rebate_unit"));
					ackobj.put("max_rebate_amt", accountsRS.getString("max_rebate_amt"));
					ackobj.put("chrg_cd", accountsRS.getString("chrg_cd"));
					ackobj.put("chrg_description", accountsRS.getString("chrg_description"));
					ackobj.put("remarks", accountsRS.getString("remarks"));
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
					obj.put("message", "Rebate Details Retrieved !!!");
					obj.put("rr_rebate_details", array);
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please enter valid RR Number or Description");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject doAddOrUpdateRebateDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_no");
		
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		
		System.out.println(object);
		
		try {
			if(!rr_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_REBATE_DETAILS);
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, (String) object.get("rebate_code"));
					accountsCS.setString(3, (String) object.get("rebate_eff_from_dt"));
					accountsCS.setString(4, (String) object.get("rebate_eff_to_dt"));
					accountsCS.setString(5, (String) object.get("userid"));
					accountsCS.setString(6, (String) object.get("remarks"));
					
					accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(7);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("active")){
							obj.put("status", "error");
							obj.put("message", "Active Record Found for Given RR-Number .  ");
						}else if(RESP.equalsIgnoreCase("fl_active")){
							obj.put("status", "error");
							obj.put("message", "Free Lighting Is Already Sanctioned for given RR-Number . Cannot Proceed !");
						}else if(RESP.equalsIgnoreCase("DTP")){
							obj.put("status", "error");
							obj.put("message", "Rebate Details Is Already Sanctioned For Given Date Range !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Rebate Created Successfully!" );
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Rebate :  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else if(RESP.equalsIgnoreCase("tax_exempt_active")){
							obj.put("status", "error");
							obj.put("message", "Tax Excemption Is Already Sanctioned for given RR-Number . Cannot Proceed !");
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_REBATE_DETAILS);
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, (String) object.get("rebate_code"));
					accountsCS.setString(3, (String) object.get("rebate_eff_from_dt"));
					accountsCS.setString(4, (String) object.get("rebate_eff_to_dt"));
					accountsCS.setString(5, (String) object.get("userid"));
					accountsCS.setString(6, (String) object.get("remarks"));
					
					accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(7);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Rebate :  Record Successfully Updated .");
						}else{
							obj.put("status", "error");
							obj.put("message", " Rebate :  Record Updation Failed !!! ");
						}
						
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}

	@Override
	public JSONObject getRegularPenaltyDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String location_code = (String) object.get("location_code");
		String rr_no = (String) object.get("rr_number");
		String conn_type = object.getString("conn_type");
		
		try {
			if(!rr_no.isEmpty()){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				accountsCS=dbConnection.prepareCall(DBQueries.GET_REGULAR_PENALITY_DETAILS);
				accountsCS.setString(1, location_code);
				accountsCS.setString(2, rr_no);
				accountsCS.setString(3, (String) object.get("serial_number"));
				accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(4);
				while(accountsRS.next())
				{
					JSONObject ackobj=new JSONObject();
					
					ackobj.put("row_num", accountsRS.getString("row_num"));
					ackobj.put("rr_no", accountsRS.getString("rr_no"));
					ackobj.put("sl_no", accountsRS.getString("sl_no"));
					ackobj.put("amount", accountsRS.getString("amount"));
					ackobj.put("chrg_cd", accountsRS.getString("chrg_cd"));
					ackobj.put("chrg_description", accountsRS.getString("chrg_description"));
					ackobj.put("from_dt", accountsRS.getString("from_dt"));
					ackobj.put("to_dt", accountsRS.getString("to_dt"));
					ackobj.put("remarks", accountsRS.getString("remarks"));
					ackobj.put("userid", accountsRS.getString("userid"));
					ackobj.put("tmpstp", accountsRS.getString("tmpstp"));
					
					array.add(ackobj);
					
				}
				if(array.isEmpty()) {
					obj.put("status", "success");
					obj.put("message", "No Records Found !!!");
				} else{
					obj.put("status", "success");
					obj.put("message", "Penality Details Retrieved !!!");
					obj.put("penality_details", array);
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please enter valid RR Number or Description");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject upsertRegularPenaltyDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_no");
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		
		System.out.println(object);
		
		try {
			if(!rr_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_REGULAR_PENALTY_DETAILS);
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, "");
					accountsCS.setString(3, (String) object.get("from_dt"));
					accountsCS.setString(4, (String) object.get("to_dt"));
					accountsCS.setString(5, (String) object.get("amount"));
					accountsCS.setString(6, (String) object.get("chrg_cd"));
					accountsCS.setString(7, (String) object.get("remarks"));
					accountsCS.setString(8, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(9, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(9);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("ACTIVE_RECORD")){
							obj.put("status", "error");
							obj.put("message", "Active Record Found for Given RR-Number .  ");
						}else if(RESP.equalsIgnoreCase("dup")){
							obj.put("status", "error");
							obj.put("message", "Penality Is Already Exists For Given Date Range !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Penality Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
							obj.put("sl_no", accountsRS.getString("SL_NO"));
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Penality Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else if (RESP.equalsIgnoreCase("DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("MAX_DT"));
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " To Date : " +  accountsRS.getString("TO_DT") + " Should Be Greater Than From Date : "+ accountsRS.getString("FROM_DT"));
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_REGULAR_PENALTY_DETAILS);
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, (String) object.get("sl_no"));
					accountsCS.setString(3, (String) object.get("from_dt"));
					accountsCS.setString(4, (String) object.get("to_dt"));
					accountsCS.setString(5, (String) object.get("amount"));
					accountsCS.setString(6, (String) object.get("chrg_cd"));
					accountsCS.setString(7, (String) object.get("remarks"));
					accountsCS.setString(8, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(9, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(9);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Penality Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("MAX_DT"));
						}else{
							obj.put("status", "error");
							obj.put("message", " Penality Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}

	@Override
	public JSONObject getEcsBankAccountType(JSONObject object) {
		
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
			
			accountsCS = dbConnection.prepareCall(DBQueries.GET_ECS_BANK_ACCOUNT_TYPE);
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("key", accountsRS.getString("value"));
				ackobj.put("value", accountsRS.getString("description"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("ecs_account_type_list", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	
	}
	
	@Override
	public JSONObject getCustomerName(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject jsonResponse = new JSONObject();
		
		try {
			
			if(!object.isEmpty()){
				
				String CONN_TYPE = (String)object.get("conn_type");
				String rr_no   = (String)object.get("rr_no");
				
				if(CONN_TYPE.equals("LT") || CONN_TYPE == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(CONN_TYPE.equals("HT") || CONN_TYPE == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				accountsCS=dbConnection.prepareCall(DBQueries.GET_CUSTOMER_NAME);
				accountsCS.setString(1,rr_no);
				accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(2);
				
				if(accountsRS.next()) {
					jsonResponse.put("status", "success");
					jsonResponse.put("customer_name", accountsRS.getString("CUSTOMER_NAME"));
				}else{
					jsonResponse.put("status", "error");
				}
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Invalid Inputs / No Records Found.");
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
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return jsonResponse;
	}
	
	@Override
	public JSONObject getECSDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String location_code = (String) object.get("location_code");
		String rr_no = (String) object.get("rr_number");
		String conn_type = object.getString("conn_type");
		
		try {
			if(!rr_no.isEmpty()){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				accountsCS=dbConnection.prepareCall(DBQueries.GET_ECS_DETAILS);
				accountsCS.setString(1, location_code);
				accountsCS.setString(2, rr_no);
				accountsCS.setString(3, (String) object.get("ecs_number"));
				accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(4);
				while(accountsRS.next())
				{
					JSONObject ackobj=new JSONObject();
					
					ackobj.put("row_num", accountsRS.getString("row_num"));
					ackobj.put("rr_no", accountsRS.getString("rr_no"));
					ackobj.put("ecs_no", accountsRS.getString("ecs_no"));
					ackobj.put("customer_name", accountsRS.getString("customer_name"));
					ackobj.put("ecs_customer_name", accountsRS.getString("ecs_customer_name"));
					ackobj.put("acct_no", accountsRS.getString("acct_no"));
					ackobj.put("acct_holder_name", accountsRS.getString("acct_holder_name"));
					ackobj.put("bank_cd", accountsRS.getString("bank_cd"));
					ackobj.put("bank_name", accountsRS.getString("bank_name"));
					ackobj.put("branch_name", accountsRS.getString("branch_name"));
					ackobj.put("acct_type_cd", accountsRS.getString("acct_type_cd"));
					ackobj.put("acct_type_description", accountsRS.getString("acct_type_description"));
					ackobj.put("effect_from_dt", accountsRS.getString("effect_from_dt"));
					ackobj.put("effect_to_dt", accountsRS.getString("effect_to_dt"));
					ackobj.put("remarks", accountsRS.getString("remarks"));
					ackobj.put("userid", accountsRS.getString("userid"));
					ackobj.put("tmpstp", accountsRS.getString("tmpstp"));
					
					array.add(ackobj);
					
				}
				if(array.isEmpty()) {
					obj.put("status", "success");
					obj.put("message", "No Records Found !!!");
				} else{
					obj.put("status", "success");
					obj.put("message", "ECS Details Retrieved !!!");
					obj.put("ecs_details", array);
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please enter valid RR Number");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject upsertEcsDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_no");
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		
		System.out.println(object);
		
		try {
			if(!rr_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_ECS_DETAILS);
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, "");
					accountsCS.setString(3, (String) object.get("customer_name"));
					accountsCS.setString(4, (String) object.get("bank_name"));
					accountsCS.setString(5, (String) object.get("branch_name"));
					accountsCS.setString(6, (String) object.get("bank_cd"));
					accountsCS.setString(7, "0");
					accountsCS.setString(8, (String) object.get("acct_holder_name"));
					accountsCS.setString(9, (String) object.get("acct_type_cd"));
					accountsCS.setString(10, (String) object.get("acct_no"));
					accountsCS.setString(11, (String) object.get("effect_from_dt"));
					accountsCS.setString(12, (String) object.get("effect_to_dt"));
					accountsCS.setString(13, "");
					accountsCS.setString(14, "");
					accountsCS.setString(15, (String) object.get("remarks"));
					accountsCS.setString(16, "");
					accountsCS.setString(17, "");
					accountsCS.setString(18, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(19, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(19);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("ACTIVE_RECORD")){
							obj.put("status", "error");
							obj.put("message", "Active Record Found for Given RR-Number .  ");
						}else if(RESP.equalsIgnoreCase("dup")){
							obj.put("status", "error");
							obj.put("message", "ECS Is Already Exists For Given Date Range !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " ECS Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
							obj.put("ecs_no", accountsRS.getString("ECS_NO").substring(7));
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " ECS Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else if (RESP.equalsIgnoreCase("DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("MAX_DT"));
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " To Date : " +  accountsRS.getString("TO_DT") + " Should Be Greater Than From Date : "+ accountsRS.getString("FROM_DT"));
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_ECS_DETAILS);
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, (String) object.get("ecs_no"));
					accountsCS.setString(3, (String) object.get("customer_name"));
					accountsCS.setString(4, (String) object.get("bank_name"));
					accountsCS.setString(5, (String) object.get("branch_name"));
					accountsCS.setString(6, (String) object.get("bank_cd"));
					accountsCS.setString(7, "0");
					accountsCS.setString(8, (String) object.get("acct_holder_name"));
					accountsCS.setString(9, (String) object.get("acct_type_cd"));
					accountsCS.setString(10, (String) object.get("acct_no"));
					accountsCS.setString(11, (String) object.get("effect_from_dt"));
					accountsCS.setString(12, (String) object.get("effect_to_dt"));
					accountsCS.setString(13, "");
					accountsCS.setString(14, "");
					accountsCS.setString(15, (String) object.get("remarks"));
					accountsCS.setString(16, "");
					accountsCS.setString(17, "");
					accountsCS.setString(18, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(19, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(19);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " ECS Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("MAX_DT"));
						}else{
							obj.put("status", "error");
							obj.put("message", " ECS Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getChequeDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_CHQ_DIS_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("rr_number"));
			accountsCS.setString(3, object.getString("receipt_date"));
			accountsCS.setString(4, object.getString("cheque_number"));
			accountsCS.setString(5, object.getString("cheque_date"));
			accountsCS.setString(6, object.getString("bank"));
			accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(7);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("chq_dd_no", accountsRS.getString("chq_dd_no"));
				ackobj.put("chq_dd_dt", accountsRS.getString("chq_dd_dt"));
				ackobj.put("drawn_bank", accountsRS.getString("drawn_bank"));
				ackobj.put("chq_dd_amt", accountsRS.getString("chq_dd_amt"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));
				
				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Cheque Details Retrieved !!!");
				obj.put("cheque_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getOtherChequeDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_OTHER_CHQ_DIS_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("rr_number"));
			accountsCS.setString(3, object.getString("receipt_date"));
			accountsCS.setString(4, object.getString("cheque_number"));
			accountsCS.setString(5, object.getString("cheque_date"));
			accountsCS.setString(6, object.getString("bank"));
			accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(7);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("chq_dd_no", accountsRS.getString("chq_dd_no"));
				ackobj.put("chq_dd_dt", accountsRS.getString("chq_dd_dt"));
				ackobj.put("drawn_bank", accountsRS.getString("drawn_bank"));
				ackobj.put("chq_dd_amt", accountsRS.getString("chq_dd_amt"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));
				
				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Other Sub-Division Cheque Details Retrieved !!!");
				obj.put("other_cheque_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getBilledAvgUnits(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject jsonResponse = new JSONObject();
		
		try {
			
			if(!object.isEmpty()){
				
				String conn_type = (String)object.get("conn_type");
				String rr_no   = (String)object.get("rr_no");
				
				if(conn_type.equals("LT") || conn_type == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT") || conn_type == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				accountsCS=dbConnection.prepareCall(DBQueries.GET_BILL_AVG_UNITS);
				accountsCS.setString(1,rr_no);
				accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(2);
				
				if(accountsRS.next()) {
					jsonResponse.put("status", "success");
					jsonResponse.put("billed_avg_units", accountsRS.getString("BILLED_AVG_UNITS"));
				}else{
					jsonResponse.put("status", "error");
				}
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Invalid Inputs / No Records Found For Rebate Type.");
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
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getFixedAvgUnits(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String location_code = (String) object.get("location_code");
		String rr_no = (String) object.get("rr_number");
		String conn_type = object.getString("conn_type");
		
		try {
			if(!rr_no.isEmpty()){

				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}				accountsCS=dbConnection.prepareCall(DBQueries.GET_FIXED_AVG_UNITS);
				accountsCS.setString(1, location_code);
				accountsCS.setString(2, rr_no);
				accountsCS.registerOutParameter(3, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(3);
				while(accountsRS.next())
				{
					JSONObject ackobj=new JSONObject();
					
					ackobj.put("row_num", accountsRS.getString("row_num"));
					ackobj.put("rr_no", accountsRS.getString("rr_no"));
					ackobj.put("full_rr_number", accountsRS.getString("full_rr_no"));
					ackobj.put("from_dt", accountsRS.getString("from_dt"));
					ackobj.put("to_dt", accountsRS.getString("to_dt"));
					ackobj.put("billed_avg_units", accountsRS.getString("billed_avg_units"));
					ackobj.put("avg_units", accountsRS.getString("avg_units"));
					ackobj.put("remarks", accountsRS.getString("remarks"));
					ackobj.put("inserted_user", accountsRS.getString("inserted_user"));
					ackobj.put("inserted_tmpstp", accountsRS.getString("inserted_tmpstp"));
					ackobj.put("updated_user", accountsRS.getString("updated_user"));
					ackobj.put("updated_tmpstp", accountsRS.getString("updated_tmpstp"));
					
					array.add(ackobj);
					
				}
				if(array.isEmpty()) {
					//no tasks for user
					obj.put("status", "success");
					obj.put("message", "No Records Found !!!");
				} else{
					obj.put("status", "success");
					obj.put("message", "Customer Fixed Average Details Retrieved !!!");
					obj.put("fixed_avg_details", array);
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please enter valid RR Number");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject upsertFixedAvgUnits(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_no");
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		
		System.out.println(object);
		
		try {
			if(!rr_no.isEmpty() ){

				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_FIXED_AVG);
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, (String) object.get("from_dt"));
					accountsCS.setString(3, (String) object.get("to_dt"));
					accountsCS.setString(4, (String) object.get("avg_units"));
					accountsCS.setString(5, (String) object.get("remarks"));
					accountsCS.setString(6, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(7);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("ACTIVE_RECORD")){
							obj.put("status", "error");
							obj.put("message", "Active Record Found for Given RR-Number .  ");
						}else if(RESP.equalsIgnoreCase("dup")){
							obj.put("status", "error");
							obj.put("message", "Manual Average Is Already Exists For Given Date Range !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Manual Average "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Manual Average  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else if (RESP.equalsIgnoreCase("DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("MAX_DT"));
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " To Date : " +  accountsRS.getString("TO_DT") + " Should Be Greater Than From Date : "+ accountsRS.getString("FROM_DT"));
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_FIXED_AVG);
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, (String) object.get("from_dt"));
					accountsCS.setString(3, (String) object.get("to_dt"));
					accountsCS.setString(4, (String) object.get("avg_units"));
					accountsCS.setString(5, (String) object.get("remarks"));
					accountsCS.setString(6, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(7);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Manual Average "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("TO_DT"));
						}else{
							obj.put("status", "error");
							obj.put("message", " Manual Average  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getAdjustmentDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_ADJUSTMENT_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("receipt_number"));
			accountsCS.setString(3, object.getString("receipt_date"));
			accountsCS.setString(4, object.getString("counter_number"));
			accountsCS.setString(5, object.getString("from_rr_no"));
			accountsCS.setString(6, object.getString("to_rr_no"));
			accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(7);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));				
				ackobj.put("rcpt_no", accountsRS.getString("rcpt_no"));
				ackobj.put("rcpt_dt", accountsRS.getString("rcpt_dt"));
				ackobj.put("counter_no", accountsRS.getString("counter_no"));
				ackobj.put("counter_no_descr", accountsRS.getString("counter_no_descr"));
				ackobj.put("from_rr_no", accountsRS.getString("from_rr_no"));
				ackobj.put("from_pymnt_purp", accountsRS.getString("from_pymnt_purp"));
				ackobj.put("from_pymnt_purp_decription", accountsRS.getString("from_pymnt_purp_decription"));
				ackobj.put("to_rr_no", accountsRS.getString("to_rr_no"));
				ackobj.put("to_pymnt_purp", accountsRS.getString("to_pymnt_purp"));
				ackobj.put("to_pymnt_purp_decription", accountsRS.getString("to_pymnt_purp_decription"));
				ackobj.put("adj_jv_no", accountsRS.getString("adj_jv_no"));
				ackobj.put("adj_no", accountsRS.getString("adj_no"));
				ackobj.put("adj_dt", accountsRS.getString("adj_dt"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Adjustment Details Retrieved !!!");
				obj.put("adjustment_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getDisconnectionDetails(JSONObject object) {
		
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
			}			accountsCS=dbConnection.prepareCall(DBQueries.GET_DISCONNECTION_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("from_date"));
			accountsCS.setString(3, object.getString("to_date"));
			accountsCS.setDouble(4, object.getDouble("from_amount"));
			accountsCS.setDouble(5, object.getDouble("to_amount"));
			accountsCS.setString(6, object.getString("om_unit_code"));
			accountsCS.setString(7, object.getString("mr_code"));
			accountsCS.registerOutParameter(8, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(8);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("tariff", accountsRS.getString("tariff"));
				ackobj.put("customer_name", accountsRS.getString("customer_name"));
				ackobj.put("addr1", accountsRS.getString("addr1"));
				ackobj.put("addr2", accountsRS.getString("addr2"));
				ackobj.put("addr3", accountsRS.getString("addr3"));
				ackobj.put("addr4", accountsRS.getString("addr4"));
				ackobj.put("mr_cd", accountsRS.getString("mr_cd"));
				ackobj.put("rdg_day", accountsRS.getString("rdg_day"));
				ackobj.put("spot_folio", accountsRS.getString("spot_folio"));
				ackobj.put("arr", accountsRS.getString("arr"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Disconnection Details Retrieved !!!");
				obj.put("disconnection_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getReconnectionDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_RECONNECTION_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("disconnection_date"));
			accountsCS.setString(3, object.getString("rr_number"));
			accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(4);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("disconn_dt", accountsRS.getString("disconn_dt"));
				ackobj.put("final_rdg", accountsRS.getString("final_rdg"));
				ackobj.put("dr_fees", accountsRS.getString("dr_fees"));
				ackobj.put("recon_dt", accountsRS.getString("recon_dt"));
				ackobj.put("remarks", accountsRS.getString("remarks"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "Reconnection Details Retrieved !!!");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("reconnection_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getMRWiseProcessDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_MRWISE_PROCESS_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("rgd_day"));
			accountsCS.registerOutParameter(3, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(3);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("mr_cd", accountsRS.getString("mr_cd"));
				ackobj.put("bill_dt", accountsRS.getString("bill_dt"));
				ackobj.put("status", accountsRS.getString("status"));
				ackobj.put("no_of_intls", accountsRS.getString("no_of_intls"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "MR Wise Process Details Retrieved !!!");
				obj.put("mr_wise_process_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getRRNumberWiseProcessDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_RRNOWISE_PROCESS_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("mr_code"));
			accountsCS.setString(3, object.getString("rgd_day"));
			accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(4);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("mr_cd", accountsRS.getString("mr_cd"));
				ackobj.put("bill_dt", accountsRS.getString("bill_dt"));
				ackobj.put("status", accountsRS.getString("status"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "RR No Wise Process Details Retrieved !!!");
				obj.put("rr_nowise_process_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getReadingEntryData(JSONObject object) {
		CallableStatement accountsCS = null;
		JSONObject jsonResponse  = new JSONObject();
		JSONArray jsonList = new JSONArray();
		PreparedStatement ps	=	null;
		ResultSet rs			=	null;
		
		try
		{
			
			String conn_type = (String) object.get("conn_type").toString().trim();
			String location_code = (String) object.get("location_code").toString().trim();
			String mr_code   = (String) object.get("mr_code").toString().trim();
			String mr_day   = (String) object.get("mr_day").toString().trim();
			String rdg_date   = (String) object.get("rdg_date").toString().trim();
			String rr_no   = (String) object.get("rr_no").toString().trim().toUpperCase();
			String entry_type   = (String) object.get("entry_type").toString().trim();
			
			if(!conn_type.isEmpty() || !location_code.isEmpty() ){
				
				
				if(conn_type.equals("LT") || conn_type == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT") || conn_type == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(dbConnection != null){
					
					accountsCS=dbConnection.prepareCall(DBQueries.GET_MTR_RDG_DETAILS);
					
					accountsCS.setString(1, location_code);
					accountsCS.setString(2, rr_no);
					accountsCS.setString(3, mr_code);
					accountsCS.setString(4, mr_day );
					accountsCS.setString(5, rdg_date);
					accountsCS.setString(6, entry_type);
					
					accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
					
					
					accountsCS.executeUpdate();
					
					rs = (ResultSet) accountsCS.getObject(7);
					
					while(rs.next()){
						
						JSONObject record = new JSONObject();
						
						record.put("row_num", ReferenceUtil.ConvertIFNullToString(rs.getString("row_num")));
						record.put("rr_no", ReferenceUtil.ConvertIFNullToString(rs.getString("rr_no")));
						record.put("mr_cd", ReferenceUtil.ConvertIFNullToString(rs.getString("mr_cd")));
						record.put("rdg_day", ReferenceUtil.ConvertIFNullToString(rs.getString("rdg_day")));
						record.put("spot_folio", ReferenceUtil.ConvertIFNullToString(rs.getString("spot_folio")));
						record.put("install_type", ReferenceUtil.ConvertIFNullToString(rs.getString("install_type")));
						record.put("install_type_description", ReferenceUtil.ConvertIFNullToString(rs.getString("install_type_description")));
						record.put("tod_meter_flag", ReferenceUtil.ConvertIFNullToString(rs.getString("tod_meter_flag")));
						record.put("bill_sts", ReferenceUtil.ConvertIFNullToString(rs.getString("bill_sts")));
						record.put("process_sts", ReferenceUtil.ConvertIFNullToString(rs.getString("process_sts")));
						record.put("prev_rdg_dt", ReferenceUtil.ConvertIFNullToString(rs.getString("prev_rdg_dt")));
						record.put("prev_bkwh", ReferenceUtil.ConvertIFNullToString(rs.getString("prev_bkwh")));
						record.put("prev_ckwh", ReferenceUtil.ConvertIFNullToString(rs.getString("prev_ckwh")));
						record.put("prev_bmd", ReferenceUtil.ConvertIFNullToString(rs.getString("prev_bmd")));
						record.put("prev_bpf", ReferenceUtil.ConvertIFNullToString(rs.getString("prev_bpf")));
						record.put("prev_rdg_sts", ReferenceUtil.ConvertIFNullToString(rs.getString("prev_rdg_sts")));
						record.put("prev_mc_flg", ReferenceUtil.ConvertIFNullToString(rs.getString("prev_mc_flg")));
						record.put("prev_ir_fr", ReferenceUtil.ConvertIFNullToString(rs.getString("prev_ir_fr")));
						record.put("prev_observ", ReferenceUtil.ConvertIFNullToString(rs.getString("prev_observ")));
						record.put("pres_rdg_dt", ReferenceUtil.ConvertIFNullToString(rs.getString("pres_rdg_dt")));
						record.put("pres_bkwh", ReferenceUtil.ConvertIFNullToString(rs.getString("pres_bkwh")));
						record.put("pres_ckwh", ReferenceUtil.ConvertIFNullToString(rs.getString("pres_ckwh")));
						record.put("pres_bmd", ReferenceUtil.ConvertIFNullToString(rs.getString("pres_bmd")));
						record.put("pres_bpf", ReferenceUtil.ConvertIFNullToString(rs.getString("pres_bpf")));
						record.put("pres_rdg_sts", ReferenceUtil.ConvertIFNullToString(rs.getString("pres_rdg_sts")));
						record.put("pres_mc_flg", ReferenceUtil.ConvertIFNullToString(rs.getString("pres_mc_flg")));
						record.put("pres_ir_fr", ReferenceUtil.ConvertIFNullToString(rs.getString("pres_ir_fr")));
						record.put("pres_observ", ReferenceUtil.ConvertIFNullToString(rs.getString("pres_observ")));
						record.put("slot1_tod_prev_bkwh", ReferenceUtil.ConvertIFNullToString(rs.getString("slot1_tod_prev_bkwh")));
						record.put("slot1_tod_prev_ckwh", ReferenceUtil.ConvertIFNullToString(rs.getString("slot1_tod_prev_ckwh")));
						record.put("slot1_tod_prev_bmd", ReferenceUtil.ConvertIFNullToString(rs.getString("slot1_tod_prev_bmd")));
						record.put("slot1_tod_prev_bpf", ReferenceUtil.ConvertIFNullToString(rs.getString("slot1_tod_prev_bpf")));
						record.put("slot2_tod_prev_bkwh", ReferenceUtil.ConvertIFNullToString(rs.getString("slot2_tod_prev_bkwh")));
						record.put("slot2_tod_prev_ckwh", ReferenceUtil.ConvertIFNullToString(rs.getString("slot2_tod_prev_ckwh")));
						record.put("slot2_tod_prev_bmd", ReferenceUtil.ConvertIFNullToString(rs.getString("slot2_tod_prev_bmd")));
						record.put("slot2_tod_prev_bpf", ReferenceUtil.ConvertIFNullToString(rs.getString("slot2_tod_prev_bpf")));
						record.put("slot3_tod_prev_bkwh", ReferenceUtil.ConvertIFNullToString(rs.getString("slot3_tod_prev_bkwh")));
						record.put("slot3_tod_prev_ckwh", ReferenceUtil.ConvertIFNullToString(rs.getString("slot3_tod_prev_ckwh")));
						record.put("slot3_tod_prev_bmd", ReferenceUtil.ConvertIFNullToString(rs.getString("slot3_tod_prev_bmd")));
						record.put("slot3_tod_prev_bpf", ReferenceUtil.ConvertIFNullToString(rs.getString("slot3_tod_prev_bpf")));
						record.put("slot1_tod_pres_bkwh", ReferenceUtil.ConvertIFNullToString(rs.getString("slot1_tod_pres_bkwh")));
						record.put("slot1_tod_pres_ckwh", ReferenceUtil.ConvertIFNullToString(rs.getString("slot1_tod_pres_ckwh")));
						record.put("slot1_tod_pres_bmd", ReferenceUtil.ConvertIFNullToString(rs.getString("slot1_tod_pres_bmd")));
						record.put("slot1_tod_pres_bpf", ReferenceUtil.ConvertIFNullToString(rs.getString("slot1_tod_pres_bpf")));
						record.put("slot2_tod_pres_bkwh", ReferenceUtil.ConvertIFNullToString(rs.getString("slot2_tod_pres_bkwh")));
						record.put("slot2_tod_pres_ckwh", ReferenceUtil.ConvertIFNullToString(rs.getString("slot2_tod_pres_ckwh")));
						record.put("slot2_tod_pres_bmd", ReferenceUtil.ConvertIFNullToString(rs.getString("slot2_tod_pres_bmd")));
						record.put("slot2_tod_pres_bpf", ReferenceUtil.ConvertIFNullToString(rs.getString("slot2_tod_pres_bpf")));
						record.put("slot3_tod_pres_bkwh", ReferenceUtil.ConvertIFNullToString(rs.getString("slot3_tod_pres_bkwh")));
						record.put("slot3_tod_pres_ckwh", ReferenceUtil.ConvertIFNullToString(rs.getString("slot3_tod_pres_ckwh")));
						record.put("slot3_tod_pres_bmd", ReferenceUtil.ConvertIFNullToString(rs.getString("slot3_tod_pres_bmd")));
						record.put("slot3_tod_pres_bpf", ReferenceUtil.ConvertIFNullToString(rs.getString("slot3_tod_pres_bpf")));
						record.put("userid", ReferenceUtil.ConvertIFNullToString(rs.getString("userid")));
						record.put("tmpstp", ReferenceUtil.ConvertIFNullToString(rs.getString("tmpstp")));
						
						jsonList.add(record);
						
						
					}
					
					if(!jsonList.isEmpty())
					{
						jsonResponse.put("ReadingList", jsonList);
						jsonResponse.put("status", "success");
					}else{
						jsonResponse.put("status", "error");
						jsonResponse.put("message", "No Records Found For The Query.");
					}
				}else{
					jsonResponse.put("status", "fail");
					jsonResponse.put("message", "Database Not Connected.");
				}
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "No Records Found For The Query.");
			}

		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Database Not Connected.");
		}
		finally 
		{
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}
		
		return jsonResponse;
		
	}

	@Override
	public JSONObject upsertReadingEntryData(JSONObject object) {
		CallableStatement accountsCS = null;
		JSONObject jsonResponse  = new JSONObject();
		JSONArray jsonList = new JSONArray();
		PreparedStatement ps	=	null;
		ResultSet rs			=	null;
		
		try {
			
			
			String conn_type = (String) object.get("conn_type").toString().trim();
			String location_code = (String) object.get("location_code");
			String userid = (String) object.get("userid");
			
			if(!object.isEmpty()){
				
				if(conn_type.equals("LT") || conn_type == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT") || conn_type == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(dbConnection != null){
					
					JSONArray ReadingDataArray = object.getJSONArray("ReadingList");
					
					if(!ReadingDataArray.isEmpty()){
						
						accountsCS=dbConnection.prepareCall(DBQueries.UPSERT_METER_READING);
						
						for(int i = 0 ; i<ReadingDataArray.size();i++){
							
							JSONObject readings = ReadingDataArray.getJSONObject(i);
							
							String rr_no=location_code + (String) readings.get("rr_no").toString().trim();
							String save_type = (String) readings.get("save_type");
							
							if(save_type == null || save_type.isEmpty()){
								continue;
							}
							
							String row_num = (String) readings.get("row_num");
							
							
							accountsCS.setString(1, rr_no);
							accountsCS.setString(2, (String) readings.get("pres_rdg_dt"));
							accountsCS.setString(3, save_type);
							
							accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
							
							accountsCS.setString(5, (String) readings.get("mr_cd"));
							accountsCS.setString(6, (String) readings.get("spot_folio"));
							accountsCS.setString(7, (String) readings.get("install_type"));
							accountsCS.setString(8, (String) readings.get("tod_meter_flag"));
							accountsCS.setString(9, (String) readings.get("pres_bkwh"));
							accountsCS.setString(10, (String) readings.get("pres_ckwh"));
							accountsCS.setString(11, (String) readings.get("pres_bmd"));
							accountsCS.setString(12, (String) readings.get("pres_bpf"));
							accountsCS.setString(13, (String) readings.get("pres_rdg_sts"));
							accountsCS.setString(14, (String) readings.get("pres_mc_flg"));
							accountsCS.setString(15, (String) readings.get("pres_ir_fr"));
							accountsCS.setString(16, (String) readings.get("pres_observ"));
							accountsCS.setString(17, (String) readings.get("slot1_tod_pres_bkwh"));
							accountsCS.setString(18, (String) readings.get("slot1_tod_pres_ckwh"));
							accountsCS.setString(19, (String) readings.get("slot1_tod_pres_bmd"));
							accountsCS.setString(20, (String) readings.get("slot1_tod_pres_bpf"));
							accountsCS.setString(21, (String) readings.get("slot2_tod_pres_bkwh"));
							accountsCS.setString(22, (String) readings.get("slot2_tod_pres_ckwh"));
							accountsCS.setString(23, (String) readings.get("slot2_tod_pres_bmd"));
							accountsCS.setString(24, (String) readings.get("slot2_tod_pres_bpf"));
							accountsCS.setString(25, (String) readings.get("slot3_tod_pres_bkwh"));
							accountsCS.setString(26, (String) readings.get("slot3_tod_pres_ckwh"));
							accountsCS.setString(27, (String) readings.get("slot3_tod_pres_bmd"));
							accountsCS.setString(28, (String) readings.get("slot3_tod_pres_bpf"));
							accountsCS.setString(29, userid);
							
							accountsCS.executeUpdate();
							
							
							rs = (ResultSet) accountsCS.getObject(4);
							
							if(rs.next()){
								
								JSONObject response = new JSONObject();
								
								String resp = rs.getString("RESP");
								
								if(resp.equals("success")){
									response.put("status", "success");
									response.put("row_num", row_num);
								}else if(resp.indexOf("fail") > 0){
									response.put("status", "error");
									response.put("row_num", row_num);
									response.put("message", resp.replace("fail", ""));
								}
								jsonList.add(response);
							}
						}
						if(!jsonList.isEmpty()){
							jsonResponse.put("status", "success");
							jsonResponse.put("message", "New Meter Reader Entry Created.");
							jsonResponse.put("reading_response", jsonList);
						}else{
							jsonResponse.put("status", "error");
							jsonResponse.put("message", "No Records Inserted/Updated !");
						}
					}else{
						jsonResponse.put("status", "error");
						jsonResponse.put("message", "No Meter Readings To Insert/Update !");
					}
				}	
			}else{	
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Invalid Input !");
			}
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error Occured In Insertion/Updation .");
		}finally 
		{
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}
		return jsonResponse;
	}
	
	@Override
	public JSONObject getMeterDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = object.getString("conn_type");
		String location_code = object.getString("location_code");
		
		try {
			if(!location_code.isEmpty()){
				
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			accountsCS = dbConnection.prepareCall(DBQueries.GET_METER_DETAILS);
			accountsCS.setString(1, location_code);
			accountsCS.setString(2, object.getString("rr_no"));
			accountsCS.setString(3, object.getString("mtr_sl_no"));
			accountsCS.setString(4, object.getString("mtr_make"));
			accountsCS.setString(5, object.getString("mtr_type"));
			
			accountsCS.registerOutParameter(6, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			
			accountsRS = (ResultSet) accountsCS.getObject(6);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("mtr_sl_no", accountsRS.getString("mtr_sl_no"));
				ackobj.put("mtr_make", accountsRS.getString("mtr_make"));
				ackobj.put("mtr_make_descr", accountsRS.getString("mtr_make_descr"));
				ackobj.put("mtr_type", accountsRS.getString("mtr_type"));
				ackobj.put("mtr_type_descr", accountsRS.getString("mtr_type_descr"));
				ackobj.put("no_of_ph", accountsRS.getString("no_of_ph"));
				ackobj.put("no_of_ph_descr", accountsRS.getString("no_of_ph_descr"));
				ackobj.put("mtr_amp", accountsRS.getString("mtr_amp"));
				ackobj.put("mtr_volt", accountsRS.getString("mtr_volt"));
				ackobj.put("mtr_volt_descr", accountsRS.getString("mtr_volt_descr"));
				ackobj.put("mtr_sts", accountsRS.getString("mtr_sts"));
				ackobj.put("mtr_sts_descr", accountsRS.getString("mtr_sts_descr"));
				ackobj.put("mtr_rated_dt", accountsRS.getString("mtr_rated_dt"));
				ackobj.put("mtr_assign_sts", accountsRS.getString("mtr_assign_sts"));
				ackobj.put("mtr_assign_sts_descr", accountsRS.getString("mtr_assign_sts_descr"));
				ackobj.put("mra_assigned_dt", accountsRS.getString("mra_assigned_dt"));
				ackobj.put("mra_release_dt", accountsRS.getString("mra_release_dt"));
				ackobj.put("remarks", accountsRS.getString("remarks"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("meter_details", array);
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	
	}
	
	@Override
	public JSONObject getDesignations(JSONObject object) {
		
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
			
			accountsCS = dbConnection.prepareCall(DBQueries.GET_DESIGNATIONS);
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("key", accountsRS.getString("dsg_cd"));
				ackobj.put("value", accountsRS.getString("dsg_descr"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("designationList", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	
	}
	
	@Override
	public JSONObject getRatingDetails(JSONObject object) {
		
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
			accountsCS = dbConnection.prepareCall(DBQueries.GET_METER_RATING_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("rr_number"));
			accountsCS.setString(3, object.getString("rated_date"));
			accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(4);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("rated_dt", accountsRS.getString("rated_dt"));
				ackobj.put("rated_by", accountsRS.getString("rated_by"));
				ackobj.put("rated_by_description", accountsRS.getString("rated_by_description"));
				ackobj.put("nature_use", accountsRS.getString("nature_use"));
				ackobj.put("rated_ld", accountsRS.getString("rated_ld"));
				ackobj.put("rdt_rdg", accountsRS.getString("rdt_rdg"));
				ackobj.put("rated_pf", accountsRS.getString("rated_pf"));
				ackobj.put("rated_err_prcnt", accountsRS.getString("rated_err_prcnt"));
				ackobj.put("ref_no", accountsRS.getString("ref_no"));
				ackobj.put("ref_dt", accountsRS.getString("ref_dt"));
				ackobj.put("ter_dt", accountsRS.getString("ter_dt"));
				ackobj.put("remarks", accountsRS.getString("remarks"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));

				array.add(ackobj);
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Rating Details Retrieved !!!");
				obj.put("rating_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	
	}
	
	@Override
	public JSONObject upsertRatingDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_no");
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		
		System.out.println(object);
		
		try {
			if(!rr_no.isEmpty() ){

				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_METER_RATING_DETAILS);
					
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, (String) object.get("rated_dt"));
					accountsCS.setString(3, (String) object.get("rdt_rdg"));
					accountsCS.setString(4, (String) object.get("rated_ld"));
					accountsCS.setString(5, (String) object.get("rated_err_prcnt"));
					accountsCS.setString(6, (String) object.get("rated_pf"));
					accountsCS.setString(7, (String) object.get("nature_use"));
					accountsCS.setString(8, (String) object.get("rated_by"));
					accountsCS.setString(9, (String) object.get("ref_no"));
					accountsCS.setString(10, (String) object.get("ref_dt"));
					accountsCS.setString(11, (String) object.get("ter_dt"));
					accountsCS.setString(12, (String) object.get("remarks"));
					accountsCS.setString(13, (String) object.get("userid"));
					
					
					accountsCS.registerOutParameter(14, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(14);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("ACTIVE_RECORD")){
							obj.put("status", "error");
							obj.put("message", "Active Record Found for Given RR-Number .  ");
						}else if(RESP.equalsIgnoreCase("dup")){
							obj.put("status", "error");
							obj.put("message", "Rating Details Is Already Exists For Given Date Range !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Rating Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Rating Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else if (RESP.equalsIgnoreCase("DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("MAX_DT"));
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " To Date : " +  accountsRS.getString("TO_DT") + " Should Be Greater Than From Date : "+ accountsRS.getString("FROM_DT"));
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_METER_RATING_DETAILS);
					
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, (String) object.get("rated_dt"));
					accountsCS.setString(3, (String) object.get("rdt_rdg"));
					accountsCS.setString(4, (String) object.get("rated_ld"));
					accountsCS.setString(5, (String) object.get("rated_err_prcnt"));
					accountsCS.setString(6, (String) object.get("rated_pf"));
					accountsCS.setString(7, (String) object.get("nature_use"));
					accountsCS.setString(8, (String) object.get("rated_by"));
					accountsCS.setString(9, (String) object.get("ref_no"));
					accountsCS.setString(10, (String) object.get("ref_dt"));
					accountsCS.setString(11, (String) object.get("ter_dt"));
					accountsCS.setString(12, (String) object.get("remarks"));
					accountsCS.setString(13, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(14, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(14);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Rating Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " To Date : " +  accountsRS.getString("TO_DT") + " Should Be Greater Than From Date : "+ accountsRS.getString("FROM_DT"));
						}else{
							obj.put("status", "error");
							obj.put("message", " Rating Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getDebitDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_DEBIT_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("rr_number"));
			accountsCS.setString(3, object.getString("debit_date"));
			accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(4);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("debit_dt", accountsRS.getString("debit_dt"));
				ackobj.put("chrg_cd", accountsRS.getString("chrg_cd"));
				ackobj.put("chrg_cd_description", accountsRS.getString("chrg_cd_description"));
				ackobj.put("debit_amt", accountsRS.getString("debit_amt"));
				ackobj.put("bill_dt", accountsRS.getString("bill_dt"));
				ackobj.put("bill_no", accountsRS.getString("bill_no"));
				ackobj.put("acct_sts", accountsRS.getString("acct_sts"));
				ackobj.put("debit_remarks", accountsRS.getString("debit_remarks"));
				ackobj.put("ref_appr", accountsRS.getString("ref_appr"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Debit Details Retrieved !!!");
				obj.put("debit_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject insertDebit(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_no");
		String conn_type = (String) object.get("conn_type");
		
		try {
			if(!rr_no.isEmpty() ){

				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				accountsCS=dbConnection.prepareCall(DBQueries.INSERT_DEBIT_DETAILS);
				accountsCS.setString(1, rr_no);
				accountsCS.setString(2, (String) object.get("chrg_cd"));
				accountsCS.setString(3, (String) object.get("debit_amt"));
				accountsCS.setString(4, (String) object.get("debit_remarks"));
				accountsCS.setString(5, (String) object.get("userid"));
				
				accountsCS.registerOutParameter(6, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(6);
				
				if(accountsRS.next()){
					String RESP = accountsRS.getString("RESP");
					System.out.println("RESP : "+RESP);
					
					if(RESP.equalsIgnoreCase("success")){
						obj.put("status", "success");
						obj.put("message", " Debit Details Inserted successfully");
						obj.put("sl_no", accountsRS.getString("SL_NO"));
					}else if (RESP.equalsIgnoreCase("fail")) {
						obj.put("status", "error");
						obj.put("message", " Debit Details Insertion Failed.");
					}else if (RESP.equalsIgnoreCase("INVALID_RRNO")) {
						obj.put("status", "error");
						obj.put("message", " Entered RR Number"+ rr_no.substring(7)+" Not Found");
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}

	@Override
	public JSONObject getPendingDebits(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_DEBIT_DETAILS_FOR_APPROVAL);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(2);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("debit_dt", accountsRS.getString("debit_dt"));
				ackobj.put("sl_no", accountsRS.getString("sl_no"));
				ackobj.put("chrg_cd", accountsRS.getString("chrg_cd"));
				ackobj.put("chrg_cd_description", accountsRS.getString("chrg_cd_description"));
				ackobj.put("debit_amt", accountsRS.getString("debit_amt"));
				ackobj.put("debit_remarks", accountsRS.getString("debit_remarks"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));
				
				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Pending Debit Details Retrieved for Approval!!!");
				obj.put("pending_debits", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			///DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject approveDebits(JSONObject data) {
		
		String approverid = (String) data.get("approverid");
		String conn_type = (String) data.get("conn_type");
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("data");
		    	
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		AckObj = approveIndividualDebit(objects, conn_type, approverid);
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
	
	public JSONObject approveIndividualDebit(JSONObject object, String conn_type, String approverid) {
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
				accountsCS=dbConnection.prepareCall(DBQueries.APPROVE_DEBIT_DETAILS);
				accountsCS.setString(1, rr_no);
				accountsCS.setString(2, (String) object.get("debit_dt"));
				accountsCS.setString(3, (String) object.get("sl_no"));
				accountsCS.setString(4, (String) object.get("chrg_cd"));
				accountsCS.setString(5, (String) object.get("debit_amt"));
				accountsCS.setString(6, (String) object.get("debit_remarks"));
				accountsCS.setString(7, (String) object.get("userid"));
				accountsCS.setString(8, (String) object.get("tmpstp"));
				accountsCS.setString(9, approverid);
				
				accountsCS.registerOutParameter(10, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(10);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Debits Approved successfully");
							obj.put("sl_no", accountsRS.getString("SL_NO"));
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Debits Approve  Failed.");
						}else if (RESP.equalsIgnoreCase("INVALID_RRNO")) {
							obj.put("status", "error");
							obj.put("message", " Given RR Number"+ rr_no.substring(7)+" Not Found");
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject rejectDebits(JSONObject data) {
		
		String rejecterid = (String) data.get("rejecterid");
		String conn_type = (String) data.get("conn_type");
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("data");
		    	
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		AckObj = rejectIndividualDebit(objects, conn_type, rejecterid);
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
	
	public JSONObject rejectIndividualDebit(JSONObject object, String conn_type, String rejecterid) {
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
				accountsCS=dbConnection.prepareCall(DBQueries.REJECT_DEBIT_DETAILS);
				accountsCS.setString(1, rr_no);
				accountsCS.setString(2, (String) object.get("debit_dt"));
				accountsCS.setString(3, (String) object.get("sl_no"));
				accountsCS.setString(4, (String) object.get("chrg_cd"));
				accountsCS.setString(5, (String) object.get("debit_amt"));
				accountsCS.setString(6, (String) object.get("debit_remarks"));
				accountsCS.setString(7, (String) object.get("userid"));
				accountsCS.setString(8, (String) object.get("tmpstp"));
				accountsCS.setString(9, rejecterid);
				
				System.out.println("rejected user:"+(String) object.get("rejecterid"));
				
				accountsCS.registerOutParameter(10, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(10);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");

						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Debits Rejected successfully");
							obj.put("sl_no", accountsRS.getString("SL_NO"));
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Debits Rejection Failed.");
						}else if (RESP.equalsIgnoreCase("INVALID_RRNO")) {
							obj.put("status", "error");
							obj.put("message", " Given RR Number"+ rr_no.substring(7)+" Not Found");
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getCustomerCredits(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_CUSTOMER_CREDIT);
			accountsCS.setString(1, object.getString("rr_number"));
			accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(2);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("credit_dt", accountsRS.getString("credit_dt"));
				ackobj.put("credit_no", accountsRS.getString("credit_no"));
				ackobj.put("credit_amt", accountsRS.getString("credit_amt"));
				ackobj.put("credited_amt", accountsRS.getString("credited_amt"));
				ackobj.put("bill_dt", accountsRS.getString("bill_dt"));
				ackobj.put("bill_no", accountsRS.getString("bill_no"));
				ackobj.put("remarks", accountsRS.getString("remarks"));
				ackobj.put("credit_sts", accountsRS.getString("credit_sts"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));

				array.add(ackobj);
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Customer Credit Details Retrieved !!!");
				obj.put("customer_credits", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}

	@Override
	public JSONObject getCreditDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_CREDIT_DETAILS);
			accountsCS.setString(1, object.getString("rr_number"));
			accountsCS.setString(2, object.getString("credit_number"));
			accountsCS.registerOutParameter(3, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(3);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("cr_no", accountsRS.getString("cr_no"));
				ackobj.put("sl_no", accountsRS.getString("sl_no"));
				ackobj.put("cr_dt", accountsRS.getString("cr_dt"));
				ackobj.put("cr_amt", accountsRS.getString("cr_amt"));
				ackobj.put("cr_from_detl", accountsRS.getString("cr_from_detl"));
				ackobj.put("detl_table", accountsRS.getString("detl_table"));
				ackobj.put("remarks", accountsRS.getString("remarks"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));
				ackobj.put("apprv_by", accountsRS.getString("apprv_by"));
				ackobj.put("apprv_tmpstp", accountsRS.getString("apprv_tmpstp"));
				
				array.add(ackobj);
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Credit Details Retrieved !!!");
				obj.put("credit_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject insertCredit(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_no");
		String jv_sts = (String) object.get("jv_sts");
		String conn_type = (String) object.get("conn_type");
		
		try {
			if(!rr_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				accountsCS=dbConnection.prepareCall(DBQueries.INSERT_CREDIT_DETAILS);
				accountsCS.setString(1, rr_no);
				accountsCS.setString(2, (String) object.get("credit_amt"));
				accountsCS.setString(3, (String) object.get("remarks"));
				accountsCS.setString(4, jv_sts);
				accountsCS.setString(5, (String) object.get("userid"));
				
				accountsCS.registerOutParameter(6, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(6);
				
				if(accountsRS.next()){
					String RESP = accountsRS.getString("RESP");
					System.out.println("RESP : "+RESP);
					
					if(RESP.equalsIgnoreCase("success")){
						obj.put("status", "success");
						obj.put("message", " Credit Inserted successfully for the RR No:"+rr_no.substring(7));
					}else if (RESP.equalsIgnoreCase("fail")) {
						obj.put("status", "error");
						obj.put("message", " Crebit Insertion Failed for the RR No:"+rr_no.substring(7));
					}else if (RESP.equalsIgnoreCase("INVALID_RRNO")) {
						obj.put("status", "error");
						obj.put("message", " Entered RR Number"+ rr_no.substring(7)+" Not Found");
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getPendingCredits(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_CREDIT_DETLS_FOR_APPROVAL);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(2);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("credit_dt", accountsRS.getString("credit_dt"));
				ackobj.put("credit_amt", accountsRS.getString("credit_amt"));
				ackobj.put("inserted_user", accountsRS.getString("inserted_user"));
				ackobj.put("inserted_tmpstp", accountsRS.getString("inserted_tmpstp"));
				ackobj.put("remarks", accountsRS.getString("remarks"));
				
				array.add(ackobj);
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Pending Credit Details Retrieved for Approval!!!");
				obj.put("pending_credits", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getBulkCreditDetailsForApprove(JSONObject object) {
		
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
			
			accountsCS=dbConnection.prepareCall(DBQueries.GET_BULK_CREDIT_DETLS_APPROVE);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("from_date"));
			accountsCS.setString(3, object.getString("to_date"));
			accountsCS.setString(4, object.getString("from_consumption"));
			accountsCS.setString(5, object.getString("to_consumption"));
			accountsCS.setString(6, object.getString("om_code"));
			accountsCS.setString(7, object.getString("mr_code"));
			accountsCS.setString(8, object.getString("tarrifs"));
			accountsCS.registerOutParameter(9, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(9);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("tariff", accountsRS.getString("tariff"));
				ackobj.put("bill_dt", accountsRS.getString("bill_dt"));
				ackobj.put("credit_dt", accountsRS.getString("bill_dt"));
				ackobj.put("bill_no", accountsRS.getString("bill_no"));
				ackobj.put("om_cd", accountsRS.getString("om_cd"));
				ackobj.put("om_cd_description", accountsRS.getString("om_cd_description"));
				ackobj.put("mr_cd", accountsRS.getString("mr_cd"));
				ackobj.put("demand", accountsRS.getString("demand"));
				ackobj.put("int", accountsRS.getString("int"));
				ackobj.put("jv_adj_amt", accountsRS.getString("jv_adj_amt"));
				ackobj.put("credit_amt", accountsRS.getString("jv_adj_amt"));
				
				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Bulk Credit Details Retrieved for Approval!!!");
				obj.put("bulk_credit_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject approveCredits(JSONObject data) {
		
		String approverid = (String) data.get("approverid");
		String isBulk = (String) data.get("bulk");
		String conn_type = (String) data.get("conn_type");
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("data");
		    	
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		AckObj = approveIndividualCredit(objects, conn_type, approverid, isBulk);
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
	
	public JSONObject approveIndividualCredit(JSONObject object, String conn_type, String approverid, String isBulk) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("full_rr_no");
		String approve_sts = "Y";
		
		try {
			if(!rr_no.isEmpty() ){

				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				accountsCS=dbConnection.prepareCall(DBQueries.APPROVE_CREDIT_DETAILS);
				accountsCS.setString(1, rr_no);
				accountsCS.setString(2, (String) object.get("credit_dt"));
				accountsCS.setString(3, (String) object.get("credit_amt"));
				accountsCS.setString(4, approverid);
				if(isBulk == null){
					accountsCS.setString(5, (String) object.get("remarks"));
				}else{
					accountsCS.setString(5, isBulk);
				}
				accountsCS.setString(6, approve_sts);
				accountsCS.setString(7, "clb");
				accountsCS.setString(8, "clb");
				accountsCS.setString(9, (String) object.get("inserted_tmpstp"));
				
				accountsCS.registerOutParameter(10, OracleTypes.VARCHAR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(10);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Credits Approved successfully");
							obj.put("sl_no", accountsRS.getString("SL_NO"));
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Credits Approve  Failed.");
						}else if (RESP.equalsIgnoreCase("INVALID_RRNO")) {
							obj.put("status", "error");
							obj.put("message", " Given RR Number"+ rr_no.substring(7)+" Not Found");
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject rejectCredits(JSONObject data) {
		
		String rejecterid = (String) data.get("rejecterid");
		String conn_type = (String) data.get("conn_type");
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("data");
		    	
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		AckObj = rejectIndividualCredit(objects, conn_type, rejecterid);
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
	
	public JSONObject rejectIndividualCredit(JSONObject object, String conn_type, String rejecterid) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("full_rr_no");
		String approve_sts = "R";
		
		try {
			if(!rr_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				accountsCS=dbConnection.prepareCall(DBQueries.APPROVE_CREDIT_DETAILS);
				accountsCS.setString(1, rr_no);
				accountsCS.setString(2, (String) object.get("credit_dt"));
				accountsCS.setString(3, (String) object.get("credit_amt"));
				accountsCS.setString(4, rejecterid);
				accountsCS.setString(5, (String) object.get("remarks"));
				accountsCS.setString(6, approve_sts);
				accountsCS.setString(7, "clb");
				accountsCS.setString(8, "clb");
				accountsCS.setString(9, (String) object.get("inserted_tmpstp"));
				
				
				System.out.println("rejected user:"+(String) object.get("rejecterid"));
				
				accountsCS.registerOutParameter(10, OracleTypes.VARCHAR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(10);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");

						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Credits Rejected successfully");
							obj.put("sl_no", accountsRS.getString("SL_NO"));
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Credits Rejection Failed.");
						}else if (RESP.equalsIgnoreCase("INVALID_RRNO")) {
							obj.put("status", "error");
							obj.put("message", " Given RR Number"+ rr_no.substring(7)+" Not Found");
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}

	@Override
	public JSONObject getPendingDeposits(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_DEPOSIT_DETLS_FOR_APPROVAL);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("approve_year"));
			accountsCS.setString(3, object.getString("mr_code"));
			accountsCS.setString(4, object.getString("reading_day"));
			accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(5);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("mr_cd", accountsRS.getString("mr_cd"));
				ackobj.put("rdg_day", accountsRS.getString("rdg_day"));
				ackobj.put("tot_dep", accountsRS.getString("tot_dep"));
				ackobj.put("dep_int", accountsRS.getString("dep_int"));
				ackobj.put("dep_int_credited", accountsRS.getString("dep_int_credited"));
				ackobj.put("credited_dt", accountsRS.getString("credited_dt"));
				ackobj.put("dep_int_tobe_credit", accountsRS.getString("dep_int_tobe_credit"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));
				ackobj.put("dep_int_rate", accountsRS.getString("dep_int_rate"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Pending Deposit Intrest Details Retrieved for Approval!!!");
				obj.put("pending_deposits", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getWithdrawlDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_WDRL_DETAILS);
			accountsCS.setString(1, object.getString("rr_number"));
			accountsCS.setString(2, object.getString("withdrawl_date"));
			accountsCS.registerOutParameter(3, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(3);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("billdt", accountsRS.getString("billdt"));
				ackobj.put("bill_no", accountsRS.getString("bill_no"));
				ackobj.put("chrg_cd", accountsRS.getString("chrg_cd"));
				ackobj.put("chrg_cd_description", accountsRS.getString("chrg_cd_description"));
				ackobj.put("sl_no", accountsRS.getString("sl_no"));
				ackobj.put("wdrl_no", accountsRS.getString("wdrl_no"));
				ackobj.put("wdrl_dt", accountsRS.getString("wdrl_dt"));
				ackobj.put("amt_wdrn", accountsRS.getString("amt_wdrn"));
				ackobj.put("inserted_user", accountsRS.getString("inserted_user"));
				ackobj.put("inserted_tmpstp", accountsRS.getString("inserted_tmpstp"));
				ackobj.put("approved_user", accountsRS.getString("approved_user"));
				ackobj.put("approved_tmpstp", accountsRS.getString("approved_tmpstp"));
				ackobj.put("apprv_sts", accountsRS.getString("apprv_sts"));
				ackobj.put("acct_sts", accountsRS.getString("acct_sts"));
				ackobj.put("remarks", accountsRS.getString("remarks"));

				array.add(ackobj);
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Withdrawal Details Retrieved !!!");
				obj.put("withdrawl_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getWithdrawlDetailsForNewEntry(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = object.getString("conn_type");
		String rr_no = object.getString("rr_no");
		
		try {
			if(!rr_no.isEmpty()){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				accountsCS = dbConnection.prepareCall(DBQueries.GET_WDRL_DETLS_NEWENTRY);
				accountsCS.setString(1,rr_no);
				accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
				
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(2);
				
				while(accountsRS.next()){
					
					JSONObject ackobj=new JSONObject();
					
					ackobj.put("row_num", accountsRS.getString("row_num"));
					ackobj.put("rr_no", accountsRS.getString("rr_no"));
					ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
					ackobj.put("bill_no", accountsRS.getString("bill_no"));
					ackobj.put("bill_dt", accountsRS.getString("bill_dt"));
					ackobj.put("total_billed_amt", accountsRS.getString("total_billed_amt"));
					ackobj.put("bill_sts", accountsRS.getString("bill_sts"));
					ackobj.put("withdrawl_dt", accountsRS.getString("withdrawl_dt"));
					ackobj.put("chrg_cd", accountsRS.getString("chrg_cd"));
					ackobj.put("chrg_cd_descr", accountsRS.getString("chrg_cd_descr"));
					ackobj.put("billed_amt", accountsRS.getString("billed_amt"));
					ackobj.put("rbt_flg", accountsRS.getString("rbt_flg"));
					ackobj.put("rbt_amt", accountsRS.getString("rbt_amt"));
					ackobj.put("paid_amt", accountsRS.getString("paid_amt"));
					ackobj.put("withdrawn_amt", accountsRS.getString("withdrawn_amt"));
					ackobj.put("pending_wdrl_amt", accountsRS.getString("pending_wdrl_amt"));
					ackobj.put("withdrawl_amt", "");
					ackobj.put("adj_no", "");
					ackobj.put("remarks", "");
	
					array.add(ackobj);
				}
				if(array.isEmpty()) {
					obj.put("status", "success");
					obj.put("message", "No Records Found !!!");
				} else{
					obj.put("status", "success");
					obj.put("new_withdrawl_details", array);
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Invalid Input...!");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	
	}
	
	@Override
	public JSONObject insertWithdrawlDetails(JSONObject data) {
		
		String conn_type = (String) data.get("conn_type");
		String userid = (String) data.get("userid");
		
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("new_withdrawl_details");
		    	System.out.println(getArray);
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		if (!objects.getString("withdrawl_amt").isEmpty()){
		    			AckObj = insertIndividualWithdrawlDetails(objects, userid, conn_type);
		    		}
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
	
	
	public JSONObject insertIndividualWithdrawlDetails(JSONObject object, String userid, String conn_type) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String withdrawl_amt = object.getString("withdrawl_amt");
		
		try {
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			if(!withdrawl_amt.isEmpty()){
			
				accountsCS=dbConnection.prepareCall(DBQueries.INSERT_WITHDRAWAL_DETAILS);
				accountsCS.setString(1, object.getString("full_rr_no"));
				accountsCS.setString(2, object.getString("bill_dt"));
				accountsCS.setString(3, object.getString("bill_no"));
				accountsCS.setString(4, object.getString("withdrawl_dt"));
				accountsCS.setString(5, object.getString("chrg_cd"));
				accountsCS.setString(6, withdrawl_amt);
				accountsCS.setString(7, object.getString("adj_no"));
				accountsCS.setString(8, object.getString("remarks"));
				accountsCS.setString(9, userid);
				
				accountsCS.registerOutParameter(10, OracleTypes.CURSOR);
				
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(10);
				
				if(accountsRS.next()){
					String RESP = accountsRS.getString("RESP");
					System.out.println("RESP : "+RESP);
					
					if(RESP.equalsIgnoreCase("success")){
						obj.put("status", "success");
						obj.put("message", " Withdrawal Details Inserted Successfully for the RR Number : "+object.getString("rr_no"));
					}else if (RESP.equalsIgnoreCase("fail")) {
						obj.put("status", "error");
						obj.put("message",RESP);
					}
					
				}
			}
														
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}	
	
	@Override
	public JSONObject getWithdrawlDetailsForApproval(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_WDRL_DETAILS_FOR_APPROVAL);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(2);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_id", accountsRS.getString("row_id"));
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("wdrl_bill_dt", accountsRS.getString("wdrl_bill_dt"));
				ackobj.put("wdrl_bill_no", accountsRS.getString("wdrl_bill_no"));
				ackobj.put("wdrl_dt", accountsRS.getString("wdrl_dt"));
				ackobj.put("wdrl_no", accountsRS.getString("wdrl_no"));
				ackobj.put("chrg_cd", accountsRS.getString("chrg_cd"));
				ackobj.put("chrg_cd_description", accountsRS.getString("chrg_cd_description"));
				ackobj.put("billed_amt", accountsRS.getString("billed_amt"));
				ackobj.put("paid_amt", accountsRS.getString("paid_amt"));
				ackobj.put("pending_wdrl_amt", accountsRS.getString("pending_wdrl_amt"));
				ackobj.put("remarks", accountsRS.getString("remarks"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));

				array.add(ackobj);
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Pending Withdrawal Details Retrieved for Approval!!!");
				obj.put("pending_withdrawl", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject approveRejectWithdrawals(JSONObject data) {
		
		String approverid = (String) data.get("approverid");
		String conn_type = (String) data.get("conn_type");
		
		String option = (String) data.get("option");
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("data");
		    	
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		AckObj = approveRejectIndividualWithdrawals(objects, approverid, conn_type, option);
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
	
	public JSONObject approveRejectIndividualWithdrawals(JSONObject object,String approverid,String conn_type,String option) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("full_rr_no");
		
		System.out.println(object);
		
		try {
			if(!rr_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				accountsCS=dbConnection.prepareCall(DBQueries.APPROVE_WITHDRAWAL_DETAILS);
				accountsCS.setString(1, rr_no);
				accountsCS.setString(2, object.getString("wdrl_bill_dt"));
				accountsCS.setString(3, object.getString("wdrl_bill_no"));
				accountsCS.setString(4, object.getString("chrg_cd"));
				accountsCS.setString(5, object.getString("pending_wdrl_amt"));
				accountsCS.setString(6, option);
				accountsCS.setString(7, object.getString("row_id"));
				accountsCS.setString(8, approverid);
				
				accountsCS.registerOutParameter(9, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(9);
				
				if(accountsRS.next()){
					String RESP = accountsRS.getString("RESP");
					System.out.println("RESP : "+RESP);
					
					if(RESP.equalsIgnoreCase("success")){
						obj.put("status", "success");
						obj.put("message", " Withdrawals  "+(option.equals("APPROVE") ? "  Approved " : "  Rejected")+" Successfully");
					}else {
						obj.put("status", "error");
						obj.put("message", " Withdrawals "+(option.equals("APPROVE") ? "  Approval " : "  Rejection")+ RESP);
					}
				}
														
			}else{
				obj.put("status", "error");
				obj.put("message", "Invalid input...!");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}

}
