package com.savbill.master;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.savbill.database.DBManagerIMPL;
import com.savbill.database.DBManagerResourceRelease;
import com.savbill.database.DBQueries;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

public class MasterManagementImpl implements IMasterManagement {

	// database connection initialize
		DBManagerIMPL databaseObj = new DBManagerIMPL();
		Connection dbConnection;
		
	@Override
	public JSONObject getCodeTypesforCodeDetails(JSONObject object) {
		
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
			
			accountsCS = dbConnection.prepareCall(DBQueries.GET_CODE_TYPES);
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("key", accountsRS.getString("code_type"));
				ackobj.put("value", accountsRS.getString("code_type"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("CodeType", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
			DBManagerResourceRelease.close(accountsRS, accountsCS);
		}
		
		return obj;
	
	}
	
	@Override
	public JSONObject getCodeDetailsForDropdowns(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = object.getString("conn_type");
		String code_type = object.getString("code_type");
		
		try {
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			accountsCS = dbConnection.prepareCall(DBQueries.GET_CODE_DETL_DROPDOWN);
			accountsCS.setString(1, code_type);
			accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(2);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("key", accountsRS.getString("code_value"));
				ackobj.put("value", accountsRS.getString("code_description"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("CodeDetailsDropDownList", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
			DBManagerResourceRelease.close(accountsRS, accountsCS);
		}
		
		return obj;
	
	}
		
	@Override
	public JSONObject getCodeDetails(JSONObject object) {
			
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_CODE_DETAILS);
			accountsCS.setString(1, object.getString("code_type"));
			accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(2);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("key", accountsRS.getString("code_value"));
				ackobj.put("value", accountsRS.getString("code_description"));
				ackobj.put("code_type", accountsRS.getString("code_type"));
				ackobj.put("code_value", accountsRS.getString("code_value"));
				ackobj.put("code_description", accountsRS.getString("code_description"));
				ackobj.put("code_status", accountsRS.getString("code_status"));
				ackobj.put("code_status_description", accountsRS.getString("code_status_description"));
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
				//obj.put("message", "Code Details Retirved !!!");
				obj.put("code_details", array);
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
	public JSONObject upsertCodeDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		String code_type = (String) object.get("code_type");
		
		System.out.println(object);
		
		try {
			if(!code_type.isEmpty() ){
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_CODE_DETAIL);
					accountsCS.setString(1, code_type);
					accountsCS.setString(2, (String) object.get("code_value"));
					accountsCS.setString(3, (String) object.get("code_description"));
					accountsCS.setString(4, (String) object.get("code_status"));
					accountsCS.setString(5, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(6, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(6);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("dup_value")){
							obj.put("status", "error");
							obj.put("message", "This Code Value Alredy Exits..!");
						}else if(RESP.equalsIgnoreCase("dup_description")){
							obj.put("status", "error");
							obj.put("message", "This Code Description Alredy Exits..!");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Code Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Code Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_CODE_DETAIL);
					accountsCS.setString(1, code_type);
					accountsCS.setString(2, (String) object.get("code_value"));
					accountsCS.setString(3, (String) object.get("code_description"));
					accountsCS.setString(4, (String) object.get("code_status"));
					accountsCS.setString(5, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(6, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(6);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Code Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Code Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
				}
				
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid ID");
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
	public JSONObject getConfigurationDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_CONFIG_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(2);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("location_code", accountsRS.getString("location_code"));
				ackobj.put("config_veriable", accountsRS.getString("config_veriable"));
				ackobj.put("config_value", accountsRS.getString("config_value"));
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
				obj.put("message", "Bill Configuration Details Retirved !!!");
				obj.put("configuration_details", array);
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
	public JSONObject upsertCloudbillConfig(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String config_veriable = (String) object.get("config_veriable");
		
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		
		System.out.println(object);
		
		try {
			if(!config_veriable.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_CLOUDBILL_CONFIG);
					accountsCS.setString(1, (String) object.get("location_code"));
					accountsCS.setString(2, config_veriable);
					accountsCS.setString(3, (String) object.get("config_value"));
					accountsCS.setString(4, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(5);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("dup_variable")){
							obj.put("status", "error");
							obj.put("message", "Entered Veriable "+config_veriable+" is Alredy Exists");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Config Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Config Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_CLOUDBILL_CONFIG);
					accountsCS.setString(1, (String) object.get("location_code"));
					accountsCS.setString(2, config_veriable);
					accountsCS.setString(3, (String) object.get("config_value"));
					accountsCS.setString(4, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(5);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Config Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Config Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
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
	public JSONObject getBillingParameters(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_BILLING_PARAMETERS);
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("id", accountsRS.getString("id"));
				ackobj.put("type", accountsRS.getString("type"));
				ackobj.put("value", accountsRS.getString("value"));
				ackobj.put("description", accountsRS.getString("description"));
				ackobj.put("effected_from_dt", accountsRS.getString("effected_from_dt"));
				ackobj.put("effected_to_dt", accountsRS.getString("effected_to_dt"));
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
				obj.put("message", "Billing Parameter Details Retirved !!!");
				obj.put("billing_parameters", array);
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
	public JSONObject upsertCloudbillParameter(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		String type = (String) object.get("type");
		
		System.out.println(object);
		
		try {
			if(!type.isEmpty() ){
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_CLOUDBILL_PARAM);
					accountsCS.setString(1, "");
					accountsCS.setString(2, type);
					accountsCS.setString(3, (String) object.get("value"));
					accountsCS.setString(4, (String) object.get("description"));
					accountsCS.setString(5, (String) object.get("effected_from_dt"));
					accountsCS.setString(6, (String) object.get("effected_to_dt"));
					accountsCS.setString(7, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(8, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(8);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("ACTIVE_RECORD")){
							obj.put("status", "error");
							obj.put("message", "Active Record Found for Given Type .  ");
						}else if(RESP.equalsIgnoreCase("dup")){
							obj.put("status", "error");
							obj.put("message", "Billing Parameter Is Already Exists For Given Date Range !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Billing Parameter "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
							obj.put("id", accountsRS.getString("ID"));
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Billing Parameter  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else if (RESP.equalsIgnoreCase("DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("MAX_DT"));
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " To Date : " +  accountsRS.getString("TO_DT") + " Should Be Greater Than From Date : "+ accountsRS.getString("FROM_DT"));
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_CLOUDBILL_PARAM);
					accountsCS.setString(1, (String) object.get("id"));
					accountsCS.setString(2, type);
					accountsCS.setString(3, (String) object.get("value"));
					accountsCS.setString(4, (String) object.get("description"));
					accountsCS.setString(5, (String) object.get("effected_from_dt"));
					accountsCS.setString(6, (String) object.get("effected_to_dt"));
					accountsCS.setString(7, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(8, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(8);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Billing Parameter "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("MAX_DT"));
						}else{
							obj.put("status", "error");
							obj.put("message", " Billing Parameter  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
					
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid ID");
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
	public JSONObject getFormDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_FORM_DETAILS);
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("form_cd", accountsRS.getString("form_cd"));
				ackobj.put("form_newcd", accountsRS.getString("form_newcd"));
				ackobj.put("form_name", accountsRS.getString("form_name"));
				ackobj.put("form_type", accountsRS.getString("form_type"));
				ackobj.put("status", accountsRS.getString("status"));
				ackobj.put("status_description", accountsRS.getString("status_description"));
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
				obj.put("message", "Form Details Retirved !!!");
				obj.put("form_details", array);
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
	public JSONObject upsertFormMaster(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		String form_cd = (String) object.get("form_cd");
		
		System.out.println(object);
		
		try {
			if(!form_cd.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_FORM_MASTER);
					accountsCS.setString(1, form_cd);
					accountsCS.setString(2, (String) object.get("form_name"));
					accountsCS.setString(3, (String) object.get("status"));
					accountsCS.setString(4, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
					
					accountsCS.executeUpdate();
					accountsRS = (ResultSet) accountsCS.getObject(5);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("dup_cd")){
							obj.put("status", "error");
							obj.put("message"," Entered Form Code  "+form_cd+" Is Already Exists!");
						}else if (RESP.equalsIgnoreCase("dup_name")) {
							obj.put("status", "error");
							obj.put("message"," Entered Form Name  "+(String) object.get("form_name")+" Is Already Exists!");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Form Details "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Form Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_FORM_MASTER);
					accountsCS.setString(1, form_cd);
					accountsCS.setString(2, (String) object.get("form_name"));
					accountsCS.setString(3, (String) object.get("status"));
					accountsCS.setString(4, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
					
					accountsCS.executeUpdate();
					accountsRS = (ResultSet) accountsCS.getObject(5);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("dup_cd")){
							obj.put("status", "error");
							obj.put("message"," Entered Form Code  "+form_cd+" Is Already Exists!");
						}else if (RESP.equalsIgnoreCase("dup_name")) {
							obj.put("status", "error");
							obj.put("message"," Entered Form Name  "+(String) object.get("form_name")+" Is Already Exists!");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Form Details "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Form Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}
						
					}
					
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid ID");
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
	public JSONObject getForms(JSONObject object) {
		
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
			
			accountsCS = dbConnection.prepareCall(DBQueries.GET_FORMS);
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("key", accountsRS.getString("form_cd"));
				ackobj.put("value", accountsRS.getString("form_name"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("FormsList", array);
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
	public JSONObject getFormPrevilageDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_FORM_PRIVILAGE_DETAILS);
			accountsCS.setString(1, object.getString("form_code"));
			accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(2);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("form_cd", accountsRS.getString("form_cd"));
				ackobj.put("form_name", accountsRS.getString("form_name"));
				ackobj.put("user_role", accountsRS.getString("user_role"));
				ackobj.put("user_role_description", accountsRS.getString("user_role_description"));
				ackobj.put("user_privilages", accountsRS.getString("user_privilages"));
				ackobj.put("select_priv", accountsRS.getString("select_priv"));
				ackobj.put("insert_priv", accountsRS.getString("insert_priv"));
				ackobj.put("update_priv", accountsRS.getString("update_priv"));
				ackobj.put("delete_priv", accountsRS.getString("delete_priv"));
				ackobj.put("select_priv_description", accountsRS.getString("select_priv_description"));
				ackobj.put("insert_priv_description", accountsRS.getString("insert_priv_description"));
				ackobj.put("update_priv_description", accountsRS.getString("update_priv_description"));
				ackobj.put("delete_priv_description", accountsRS.getString("delete_priv_description"));
				
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
				obj.put("message", "Forms Previlage Details Retirved !!!");
				obj.put("form_previlage_details", array);
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
	public JSONObject upsertFormPrevilageDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String conn_type = (String) object.get("conn_type");
		String db_option = (String) object.get("option");
		
		System.out.println(object);
		
		try {
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					accountsCS=dbConnection.prepareCall(DBQueries.UPSERT_FORM_PRIVILEGES);
					
					accountsCS.setString(1, (String) object.get("form_cd"));
					accountsCS.setString(2, (String) object.get("user_role"));
					accountsCS.setString(3, (String) object.get("select_priv"));
					accountsCS.setString(4, (String) object.get("insert_priv"));
					accountsCS.setString(5, (String) object.get("update_priv"));
					accountsCS.setString(6, (String) object.get("delete_priv"));
					accountsCS.setString(7, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(8, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(8);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Form Previlage Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Form Previlage Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
					}
														
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid Inputs");
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
	public JSONObject getSubDivisionDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_SUB_DIV_DETAILS);
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("sub_div_code", accountsRS.getString("sub_div_code"));
				ackobj.put("sub_div_name", accountsRS.getString("sub_div_name"));
				ackobj.put("office_typ", accountsRS.getString("office_typ"));
				ackobj.put("office_typ_description", accountsRS.getString("office_typ_description"));
				ackobj.put("addr1", accountsRS.getString("addr1"));
				ackobj.put("addr2", accountsRS.getString("addr2"));
				ackobj.put("addr3", accountsRS.getString("addr3"));
				ackobj.put("addr4", accountsRS.getString("addr4"));
				ackobj.put("city", accountsRS.getString("city"));
				ackobj.put("pin_cd", accountsRS.getString("pin_cd"));
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
				obj.put("message", "Sub-Division Details Retirved !!!");
				obj.put("sub_division_details", array);
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
	public JSONObject upsertSubDivisionDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String conn_type = (String) object.get("conn_type");
		String db_option = (String) object.get("option");
		
		System.out.println(object);
		
		try {
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					accountsCS=dbConnection.prepareCall(DBQueries.UPSERT_SUB_DIV_DETAILS);
					
					accountsCS.setString(1, (String) object.get("sub_div_code"));
					accountsCS.setString(2, (String) object.get("sub_div_name"));
					accountsCS.setString(3, (String) object.get("office_typ"));
					accountsCS.setString(4, (String) object.get("addr1"));
					accountsCS.setString(5, (String) object.get("addr2"));
					accountsCS.setString(6, (String) object.get("addr3"));
					accountsCS.setString(7, (String) object.get("addr4"));
					accountsCS.setString(8, (String) object.get("city"));
					accountsCS.setString(9, (String) object.get("pin_cd"));
					accountsCS.setString(10, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(11, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(11);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Sub Division Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Sub Division Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
					}
														
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid Inputs");
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
	public JSONObject getRebateMasterDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_REBATE_MASTER_DETAILS);
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rbt_code", accountsRS.getString("rbt_code"));
				ackobj.put("rbt_description", accountsRS.getString("rbt_description"));
				ackobj.put("effected_from_dt", accountsRS.getString("effected_from_dt"));
				ackobj.put("effected_to_dt", accountsRS.getString("effected_to_dt"));
				ackobj.put("rbt_type", accountsRS.getString("rbt_type"));
				ackobj.put("rbt_on", accountsRS.getString("rbt_on"));
				ackobj.put("chrg_cd", accountsRS.getString("chrg_cd"));
				ackobj.put("chrg_cd_description", accountsRS.getString("chrg_cd_description"));
				ackobj.put("rbt_unit", accountsRS.getString("rbt_unit"));
				ackobj.put("min_rbt", accountsRS.getString("min_rbt"));
				ackobj.put("max_rbt", accountsRS.getString("max_rbt"));
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
				obj.put("message", "Rebate Master Details Retirved !!!");
				obj.put("rebate_master_details", array);
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
	public JSONObject upsertRebateMasterDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		String rbt_code = (String) object.get("rbt_code");
		
		System.out.println(object);
		
		try {
			if(!rbt_code.isEmpty() ){
				//dbConnection = DatabaseImpl.GetLTSQLConnection();
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_REBATE_MASTER);
					accountsCS.setString(1, rbt_code);
					accountsCS.setString(2, (String) object.get("rbt_description"));
					accountsCS.setString(3, (String) object.get("effected_from_dt"));
					accountsCS.setString(4, (String) object.get("effected_to_dt"));
					accountsCS.setString(5, (String) object.get("rbt_type"));
					accountsCS.setString(6, (String) object.get("rbt_on"));
					accountsCS.setString(7, (String) object.get("chrg_cd"));
					accountsCS.setString(8, (String) object.get("rbt_unit"));
					accountsCS.setString(9, (String) object.get("min_rbt"));
					accountsCS.setString(10, (String) object.get("max_rbt"));
					accountsCS.setString(11, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(12, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(12);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("ACTIVE_RECORD")){
							obj.put("status", "error");
							obj.put("message", "Active Record Found for Given Rebate Code .  ");
						}else if(RESP.equalsIgnoreCase("dup")){
							obj.put("status", "error");
							obj.put("message", "Rebate Master Details Is Already Exists For Given Date Range !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Rebate Master Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Rebate Master Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else if (RESP.equalsIgnoreCase("DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("MAX_DT"));
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " To Date : " +  accountsRS.getString("TO_DT") + " Should Be Greater Than From Date : "+ accountsRS.getString("FROM_DT"));
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_REBATE_MASTER);
					accountsCS.setString(1, rbt_code);
					accountsCS.setString(2, (String) object.get("rbt_description"));
					accountsCS.setString(3, (String) object.get("effected_from_dt"));
					accountsCS.setString(4, (String) object.get("effected_to_dt"));
					accountsCS.setString(5, (String) object.get("rbt_type"));
					accountsCS.setString(6, (String) object.get("rbt_on"));
					accountsCS.setString(7, (String) object.get("chrg_cd"));
					accountsCS.setString(8, (String) object.get("rbt_unit"));
					accountsCS.setString(9, (String) object.get("min_rbt"));
					accountsCS.setString(10, (String) object.get("max_rbt"));
					accountsCS.setString(11, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(12, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(12);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Rebate Master Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("TO_DT"));
						}else{
							obj.put("status", "error");
							obj.put("message", " Rebate Master Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
					
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid Rebate Code");
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
	public JSONObject getBankDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = (String)object.get("conn_type");
		
		try {
			
			if(conn_type.equals("LT") || conn_type == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT") || conn_type == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			accountsCS=dbConnection.prepareCall(DBQueries.GET_BANK_DETAILS);
			accountsCS.setString(1, object.getString("micr_code"));
			accountsCS.setString(2, object.getString("city_code"));
			accountsCS.setString(3, object.getString("bank_code"));
			accountsCS.setString(4, object.getString("branch_code"));
			accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(5);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("row_id", accountsRS.getString("row_id"));
				ackobj.put("micr_cd", accountsRS.getString("micr_cd"));
				ackobj.put("city_cd", accountsRS.getString("city_cd"));
				ackobj.put("city_name", accountsRS.getString("city_name"));
				ackobj.put("bank_cd", accountsRS.getString("bank_cd"));
				ackobj.put("bank_name", accountsRS.getString("bank_name"));
				ackobj.put("branch_cd", accountsRS.getString("branch_cd"));
				ackobj.put("branch_name", accountsRS.getString("branch_name"));
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
				obj.put("message", "Bank Details Retirved !!!");
				obj.put("bank_details", array);
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
	public JSONObject upsertBankDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		
		System.out.println(object);
		
		try {
			if(!db_option.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_BANK_DETAILS);

					accountsCS.setString(1, (String) object.get("city_cd"));
					accountsCS.setString(2, (String) object.get("bank_cd"));
					accountsCS.setString(3, (String) object.get("branch_cd"));
					accountsCS.setString(4, (String) object.get("city_name"));
					accountsCS.setString(5, (String) object.get("bank_name"));
					accountsCS.setString(6, (String) object.get("branch_name"));
					accountsCS.setString(7, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(8, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(8);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("dup")){
							obj.put("status", "error");
							obj.put("message", "Entered Bank Details Is Already Exists !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Bank Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Bank Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_BANK_DETAILS);

					accountsCS.setString(1, (String) object.get("row_id"));
					accountsCS.setString(2, (String) object.get("city_cd"));
					accountsCS.setString(3, (String) object.get("bank_cd"));
					accountsCS.setString(4, (String) object.get("branch_cd"));
					accountsCS.setString(5, (String) object.get("city_name"));
					accountsCS.setString(6, (String) object.get("bank_name"));
					accountsCS.setString(7, (String) object.get("branch_name"));
					accountsCS.setString(8, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(9, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(9);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Bank Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else{
							obj.put("status", "error");
							obj.put("message", " Bank Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
					
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid Bank Details");
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
	public JSONObject getBankDetailsbyMicrCode(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject jsonResponse = new JSONObject();
		
		try {
			
			if(!object.isEmpty()){
				
				String CONN_TYPE = (String)object.get("conn_type");
				String micr_code   = (String)object.get("micr_code");
				
				if(CONN_TYPE.equals("LT") || CONN_TYPE == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(CONN_TYPE.equals("HT") || CONN_TYPE == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				accountsCS=dbConnection.prepareCall(DBQueries.GET_BANK_DETAILS_BY_MICR_CD);
				accountsCS.setString(1,micr_code);
				accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(2);
				
				if(accountsRS.next()) {
					jsonResponse.put("status", "success");
					jsonResponse.put("bank_name", accountsRS.getString("BANK_NAME"));
					jsonResponse.put("branch_name", accountsRS.getString("BRANCH_NAME"));
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
	public JSONObject getTariffMasterDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_TARIFF_MASTER_DETAILS);
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("version_no", accountsRS.getString("version_no"));
				ackobj.put("trf_code", accountsRS.getString("trf_code"));
				ackobj.put("trf_name", accountsRS.getString("trf_name"));
				ackobj.put("trf_description", accountsRS.getString("trf_description"));
				ackobj.put("effected_from_dt", accountsRS.getString("effected_from_dt"));
				ackobj.put("effected_to_dt", accountsRS.getString("effected_to_dt"));
				ackobj.put("billing_frequency", accountsRS.getString("billing_frequency"));
				ackobj.put("billing_frequency_description", accountsRS.getString("billing_frequency_description"));
				ackobj.put("power_unit", accountsRS.getString("power_unit"));
				ackobj.put("power_unit_description", accountsRS.getString("power_unit_description"));
				ackobj.put("fixed_min_chrgs", accountsRS.getString("fixed_min_chrgs"));
				ackobj.put("fec_chrgs", accountsRS.getString("fec_chrgs"));
				ackobj.put("const_chrgs", accountsRS.getString("const_chrgs"));
				ackobj.put("tax_percentage", accountsRS.getString("tax_percentage"));
				ackobj.put("trf_type", accountsRS.getString("trf_type"));
				ackobj.put("trf_type_description", accountsRS.getString("trf_type_description"));
				ackobj.put("status", accountsRS.getString("status"));
				ackobj.put("status_description", accountsRS.getString("status_description"));
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
				obj.put("message", "Tariff Details Retirved !!!");
				obj.put("tariff_master_details", array);
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
	public JSONObject upsertTariffMasterDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		String trf_code = (String) object.get("trf_code");
		
		System.out.println(object);
		
		try {
			if(!trf_code.isEmpty() ){

				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_TRF_MASTER);
					accountsCS.setString(1, "");
					accountsCS.setString(2, trf_code);
					accountsCS.setString(3, (String) object.get("trf_name"));
					accountsCS.setString(4, (String) object.get("trf_description"));
					accountsCS.setString(5, (String) object.get("effected_from_dt"));
					accountsCS.setString(6, (String) object.get("effected_to_dt"));
					accountsCS.setString(7, (String) object.get("billing_frequency"));
					accountsCS.setString(8, (String) object.get("power_unit"));
					accountsCS.setString(9, (String) object.get("fixed_min_chrgs"));
					accountsCS.setString(10, (String) object.get("fec_chrgs"));
					accountsCS.setString(11, (String) object.get("const_chrgs"));
					accountsCS.setString(12, (String) object.get("tax_percentage"));
					accountsCS.setString(13, (String) object.get("trf_type"));
					accountsCS.setString(14, (String) object.get("status"));
					accountsCS.setString(15, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(16, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(16);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("ACTIVE_RECORD")){
							obj.put("status", "error");
							obj.put("message", "Active Record Found for Given Tariff Code .  ");
						}else if(RESP.equalsIgnoreCase("dup")){
							obj.put("status", "error");
							obj.put("message", "Tariff Master Details Is Already Exists For Given Date Range !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Tariff Master Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Tariff Master Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else if (RESP.equalsIgnoreCase("DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("MAX_DT"));
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " To Date : " +  accountsRS.getString("TO_DT") + " Should Be Greater Than From Date : "+ accountsRS.getString("FROM_DT"));
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_TRF_MASTER);
					accountsCS.setString(1, (String) object.get("version_no"));
					accountsCS.setString(2, trf_code);
					accountsCS.setString(3, (String) object.get("trf_name"));
					accountsCS.setString(4, (String) object.get("trf_description"));
					accountsCS.setString(5, (String) object.get("effected_from_dt"));
					accountsCS.setString(6, (String) object.get("effected_to_dt"));
					accountsCS.setString(7, (String) object.get("billing_frequency"));
					accountsCS.setString(8, (String) object.get("power_unit"));
					accountsCS.setString(9, (String) object.get("fixed_min_chrgs"));
					accountsCS.setString(10, (String) object.get("fec_chrgs"));
					accountsCS.setString(11, (String) object.get("const_chrgs"));
					accountsCS.setString(12, (String) object.get("tax_percentage"));
					accountsCS.setString(13, (String) object.get("trf_type"));
					accountsCS.setString(14, (String) object.get("status"));
					accountsCS.setString(15, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(16, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(16);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Tariff Master Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("TO_DT"));
						}else{
							obj.put("status", "error");
							obj.put("message", " Tariff Master Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
					
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid Tariff Code");
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
	public JSONObject getPaymentAdjustmentPriortyDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_PYMNT_ADJ_PRIORITY_DETAILS);
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("row_id", accountsRS.getString("row_id"));
				ackobj.put("chrg_cd", accountsRS.getString("chrg_cd"));
				ackobj.put("chrg_cd_description", accountsRS.getString("chrg_cd_description"));
				ackobj.put("priority", accountsRS.getString("priority"));
				ackobj.put("jv_priority", accountsRS.getString("jv_priority"));
				ackobj.put("disp_priority", accountsRS.getString("disp_priority"));
				ackobj.put("chrg_typ", accountsRS.getString("chrg_typ"));
				ackobj.put("chrg_typ_descr", accountsRS.getString("chrg_typ_descr"));
				ackobj.put("slabs_flg", accountsRS.getString("slabs_flg"));
				ackobj.put("slabs_flg_descr", accountsRS.getString("slabs_flg_descr"));
				ackobj.put("rbt_flg", accountsRS.getString("rbt_flg"));
				ackobj.put("rbt_flg_descr", accountsRS.getString("rbt_flg_descr"));
				ackobj.put("arrs_cd", accountsRS.getString("arrs_cd"));
				ackobj.put("sign", accountsRS.getString("sign"));
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
				obj.put("message", "Payment Adjust Priority Details Retirved !!!");
				obj.put("payment_adjustment_priority_details", array);
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
	public JSONObject upsertPaymentAdjustmentPriortyDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String conn_type = (String) object.get("conn_type");
		String chrg_cd = (String) object.get("chrg_cd");
		String chrg_cd_description = (String) object.get("chrg_cd_description");
		String option = (String) object.get("option");
		
		try {
			if(!chrg_cd.isEmpty() && !chrg_cd_description.isEmpty()){

				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				accountsCS=dbConnection.prepareCall(DBQueries.UPSERT_PYMNT_ADJ_PRIORITY);
				accountsCS.setString(1, option);
				accountsCS.setString(2, (String) object.get("row_id"));
				accountsCS.setString(3, chrg_cd);
				accountsCS.setString(4, (String) object.get("arrs_cd"));
				accountsCS.setString(5, chrg_cd_description);
				accountsCS.setString(6, (String) object.get("chrg_typ"));
				accountsCS.setString(7, (String) object.get("priority"));
				accountsCS.setString(8, (String) object.get("jv_priority"));
				accountsCS.setString(9, (String) object.get("disp_priority"));
				accountsCS.setString(10, (String) object.get("slabs_flg"));
				accountsCS.setString(11, (String) object.get("rbt_flg"));
				accountsCS.setString(12, (String) object.get("sign"));
				accountsCS.setString(13, (String) object.get("userid"));
				
				accountsCS.registerOutParameter(14, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(14);
				
				if(accountsRS.next()){
					String RESP = accountsRS.getString("RESP");
					System.out.println("RESP : "+RESP);
					
					if(RESP.equalsIgnoreCase("success")){
						obj.put("status", "success");
						obj.put("message", " Paymant Adjustment Priority Details "+(option.equals("ADD") ? "  Inserted " : "  Updated")+" Successfully");
					}else  {
						obj.put("status", "error");
						obj.put("message", RESP);
					}
					
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
	public JSONObject getChequeDisPenaltyMasterDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_CHQDIS_PENLTY_MASTER_DETLS);
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("id", accountsRS.getString("id"));
				ackobj.put("from_amt", accountsRS.getString("from_amt"));
				ackobj.put("to_amt", accountsRS.getString("to_amt"));
				ackobj.put("penality_percentage", accountsRS.getString("penality_percentage"));
				ackobj.put("penality_max_amt", accountsRS.getString("penality_max_amt"));
				ackobj.put("status", accountsRS.getString("status"));
				ackobj.put("status_description", accountsRS.getString("status_description"));
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
				obj.put("message", "Cheque Dishoner Penality Master Details Retirved !!!");
				obj.put("chqdis_penalty_master_details", array);
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
	public JSONObject upsertChequeDisPenaltyMasterDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		
		System.out.println(object);
		
		try {
			if(!db_option.isEmpty()){
				//dbConnection = DatabaseImpl.GetLTSQLConnection();
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_CHQ_DIS_PENALITY);
					accountsCS.setString(1, "");
					accountsCS.setString(2, (String) object.get("from_amt"));
					accountsCS.setString(3, (String) object.get("to_amt"));
					accountsCS.setString(4, (String) object.get("penality_percentage"));
					accountsCS.setString(5, (String) object.get("penality_max_amt"));
					accountsCS.setString(6, (String) object.get("status"));
					accountsCS.setString(7, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(8, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(8);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("dup")){
							obj.put("status", "error");
							obj.put("message", "Cheque Dis Penality Details Is Already Exists !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Cheque Dis Penality Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
							obj.put("id", accountsRS.getString("ID"));
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Cheque Dis Penality Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_CHQ_DIS_PENALITY);
					
					accountsCS.setString(1, (String) object.get("id"));
					accountsCS.setString(2, (String) object.get("from_amt"));
					accountsCS.setString(3, (String) object.get("to_amt"));
					accountsCS.setString(4, (String) object.get("penality_percentage"));
					accountsCS.setString(5, (String) object.get("penality_max_amt"));
					accountsCS.setString(6, (String) object.get("status"));
					accountsCS.setString(7, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(8, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(8);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Contractor Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else{
							obj.put("status", "error");
							obj.put("message", " Contractor Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
					
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid Details");
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
	public JSONObject getContractorMasterDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String location_code = (String) object.get("location_code");
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_CONTRACTOR_MASTER_DETAILS);
			accountsCS.setString(1, location_code);
			accountsCS.setString(2, object.getString("license_number"));
			accountsCS.setString(3, object.getString("contractor_name"));
			accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(4);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("location_code", accountsRS.getString("location_code"));
				ackobj.put("licensce_no", accountsRS.getString("licensce_no"));
				ackobj.put("contr_name", accountsRS.getString("contr_name"));
				ackobj.put("address1", accountsRS.getString("address1"));
				ackobj.put("address2", accountsRS.getString("address2"));
				ackobj.put("phone_no", accountsRS.getString("phone_no"));
				ackobj.put("email", accountsRS.getString("email"));
				ackobj.put("status", accountsRS.getString("status"));
				ackobj.put("status_description", accountsRS.getString("status_description"));
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
				obj.put("message", "Contractor Details Retirved !!!");
				obj.put("contractor_master_details", array);
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
	public JSONObject upsertContractorMasterDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String location_code = (String) object.get("location_code");
		String licensce_no = (String) object.get("licensce_no");
		
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		
		System.out.println(object);
		
		try {
			if(!location_code.isEmpty() && !licensce_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_CONTRACTOR_MASTER);
					accountsCS.setString(1, location_code);
					accountsCS.setString(2, licensce_no);
					accountsCS.setString(3, (String) object.get("contr_name"));
					accountsCS.setString(4, (String) object.get("address1"));
					accountsCS.setString(5, (String) object.get("address2"));
					accountsCS.setString(6, (String) object.get("phone_no"));
					accountsCS.setString(7, (String) object.get("email"));
					accountsCS.setString(8, (String) object.get("status"));
					accountsCS.setString(9, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(10, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(10);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("dup")){
							obj.put("status", "error");
							obj.put("message", "Contractor Details Is Already Exists !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Contractor Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Contractor Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_CONTRACTOR_MASTER);
					accountsCS.setString(1, location_code);
					accountsCS.setString(2, licensce_no);
					accountsCS.setString(3, (String) object.get("contr_name"));
					accountsCS.setString(4, (String) object.get("address1"));
					accountsCS.setString(5, (String) object.get("address2"));
					accountsCS.setString(6, (String) object.get("phone_no"));
					accountsCS.setString(7, (String) object.get("email"));
					accountsCS.setString(8, (String) object.get("status"));
					accountsCS.setString(9, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(10, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(10);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Contractor Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else{
							obj.put("status", "error");
							obj.put("message", " Contractor Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
					
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid Details");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
		
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getMeterReaderDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_METER_READER_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("om_code"));
			accountsCS.setString(3, object.getString("mr_code"));
			accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(4);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("mr_cd", accountsRS.getString("mr_cd"));
				ackobj.put("mr_name", accountsRS.getString("mr_name"));
				ackobj.put("rdg_day", accountsRS.getString("rdg_day"));
				ackobj.put("mr_description", accountsRS.getString("mr_description"));
				ackobj.put("om_cd", accountsRS.getString("om_cd"));
				ackobj.put("om_name", accountsRS.getString("om_name"));
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
				obj.put("message", "Meter Reader Details Retirved !!!");
				obj.put("meter_reader_details", array);
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
	public JSONObject upsertMeterReaderDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String mr_cd = (String) object.get("mr_cd");
		String conn_type = (String) object.get("conn_type");
		
		try {
			if(!mr_cd.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				accountsCS=dbConnection.prepareCall(DBQueries.INSERT_MTR_RDR_MASTR);
				accountsCS.setString(1, mr_cd);
				accountsCS.setString(2, (String) object.get("rdg_day"));
				accountsCS.setString(3, (String) object.get("mr_name"));
				accountsCS.setString(4, (String) object.get("mr_description"));
				accountsCS.setString(5, (String) object.get("om_cd"));
				accountsCS.setString(6, (String) object.get("userid"));
				accountsCS.setString(7, (String) object.get("all_rdg_day"));
				
				accountsCS.registerOutParameter(8, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(8);
				
				if(accountsRS.next()){
					String RESP = accountsRS.getString("RESP");
					System.out.println("RESP : "+RESP);
					
					if(RESP.equalsIgnoreCase("success")){
						obj.put("status", "success");
						obj.put("message", " Meter Reader Details Inserted/Updated successfully for : " + mr_cd);
					}else{
						obj.put("status", "error");
						obj.put("message",RESP);
					}	
				}
			
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid Meter Reader Code");
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
	public JSONObject getPreDominantDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
	
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_PRE_DOMINANT_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("date"));
			accountsCS.registerOutParameter(3, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(3);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("station_cd", accountsRS.getString("station_cd"));
				ackobj.put("station_name", accountsRS.getString("station_name"));
				ackobj.put("feeder_cd", accountsRS.getString("feeder_cd"));
				ackobj.put("feeder_name", accountsRS.getString("feeder_name"));
				ackobj.put("no_of_intls", accountsRS.getString("no_of_intls"));
				ackobj.put("units", accountsRS.getString("units"));
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
				obj.put("message", "Pre-Dominant Details Retirved !!!");
				obj.put("pre_dominant_details", array);
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
	public JSONObject upsertPreDominantDetails(JSONObject data) {
		
		String userid = (String) data.get("userid");
		String conn_type = (String) data.get("conn_type");
		String location_code = (String) data.get("location_code");
		String month = (String) data.get("month");
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("data");
		    	
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		AckObj = upsertIndividualPreDominantDetails(objects, userid, conn_type, location_code, month);
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
	
	public JSONObject upsertIndividualPreDominantDetails(JSONObject object, String userid, String conn_type, String location_code, String month) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String station = (String) object.get("station");
		String feeder = (String) object.get("feeder");
		String units = (String) object.get("units");
		
		try {
			if(!location_code.isEmpty() && !station.isEmpty() && !feeder.isEmpty() && !month.isEmpty() && !units.isEmpty()){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					accountsCS=dbConnection.prepareCall(DBQueries.UPSERT_PREDOMINANT_UNITS);
					accountsCS.setString(1, location_code);
					accountsCS.setString(2, station);
					accountsCS.setString(3, feeder);
					accountsCS.setString(4, month);
					accountsCS.setString(5, units);
					accountsCS.setString(6, userid);
					
					accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					accountsRS = (ResultSet) accountsCS.getObject(7);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Predominant Units Insert Successfully");
						}else  {
							obj.put("status", "error");
							obj.put("message", RESP);
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
	public JSONObject getHHDUploadDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
	
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_HHD_DOWN_UP_DETAILS);
			accountsCS.setString(1, "UPLOAD");
			accountsCS.setString(2, object.getString("location_code"));
			accountsCS.setString(3, object.getString("rr_number"));
			accountsCS.setString(4, object.getString("bill_date"));
			accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(5);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("mtr_rdr_cd", accountsRS.getString("mtr_rdr_cd"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("bill_no", accountsRS.getString("bill_no"));
				ackobj.put("bill_dt", accountsRS.getString("bill_dt"));
				ackobj.put("mtr_rdg", accountsRS.getString("mtr_rdg"));
				ackobj.put("mtr_rdg_sts", accountsRS.getString("mtr_rdg_sts"));
				ackobj.put("units_consmp", accountsRS.getString("units_consmp"));
				ackobj.put("fec", accountsRS.getString("fec"));
				ackobj.put("tot_demand", accountsRS.getString("tot_demand"));
				ackobj.put("tax", accountsRS.getString("tax"));
				ackobj.put("rebate", accountsRS.getString("rebate"));
				ackobj.put("cr_amt_fwd", accountsRS.getString("cr_amt_fwd"));
				ackobj.put("billed_sts", accountsRS.getString("billed_sts"));
				ackobj.put("mudload_flg", accountsRS.getString("mudload_flg"));
				ackobj.put("fx_un1", accountsRS.getString("fx_un1"));
				ackobj.put("fx_rt1", accountsRS.getString("fx_rt1"));
				ackobj.put("fx_un2", accountsRS.getString("fx_un2"));
				ackobj.put("fx_rt2", accountsRS.getString("fx_rt2"));
				ackobj.put("fx_un3", accountsRS.getString("fx_un3"));
				ackobj.put("fx_rt3", accountsRS.getString("fx_rt3"));
				ackobj.put("fx_un4", accountsRS.getString("fx_un4"));
				ackobj.put("fx_rt4", accountsRS.getString("fx_rt4"));
				ackobj.put("fx_un5", accountsRS.getString("fx_un5"));
				ackobj.put("fx_rt5", accountsRS.getString("fx_rt5"));
				ackobj.put("en_un1", accountsRS.getString("en_un1"));
				ackobj.put("en_rt1", accountsRS.getString("en_rt1"));
				ackobj.put("en_un2", accountsRS.getString("en_un2"));
				ackobj.put("en_rt2", accountsRS.getString("en_rt2"));
				ackobj.put("en_un3", accountsRS.getString("en_un3"));
				ackobj.put("en_rt3", accountsRS.getString("en_rt3"));
				ackobj.put("en_un4", accountsRS.getString("en_un4"));
				ackobj.put("en_rt4", accountsRS.getString("en_rt4"));
				ackobj.put("en_un5", accountsRS.getString("en_un5"));
				ackobj.put("en_rt5", accountsRS.getString("en_rt5"));
				ackobj.put("en_un6", accountsRS.getString("en_un6"));
				ackobj.put("en_rt6", accountsRS.getString("en_rt6"));
				ackobj.put("pf_penalty", accountsRS.getString("pf_penalty"));
				ackobj.put("err_descr", accountsRS.getString("err_descr"));
				ackobj.put("diff_amt", accountsRS.getString("diff_amt"));
				ackobj.put("pnlty_excs_ld", accountsRS.getString("pnlty_excs_ld"));
				ackobj.put("prev_ckwh", accountsRS.getString("prev_ckwh"));
				ackobj.put("cur_ckwh", accountsRS.getString("cur_ckwh"));
				ackobj.put("submtr_fc", accountsRS.getString("submtr_fc"));
				ackobj.put("submtr_ec", accountsRS.getString("submtr_ec"));
				ackobj.put("pl_rbt", accountsRS.getString("pl_rbt"));
				ackobj.put("ph_rbt", accountsRS.getString("ph_rbt"));
				ackobj.put("act_dl_cr", accountsRS.getString("act_dl_cr"));
				ackobj.put("rr_rebate", accountsRS.getString("rr_rebate"));
				ackobj.put("capacity_rbt", accountsRS.getString("capacity_rbt"));
				ackobj.put("orphanage_rebate", accountsRS.getString("orphanage_rebate"));
				ackobj.put("wkly_min_amt", accountsRS.getString("wkly_min_amt"));
				ackobj.put("wkly_adj_amt", accountsRS.getString("wkly_adj_amt"));
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
				obj.put("message", "HHD Upload Details Retirved !!!");
				obj.put("hhd_upload_details", array);
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
	public JSONObject getHHDDownloadDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
	
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_HHD_DOWN_UP_DETAILS);
			accountsCS.setString(1, "DOWNLOAD");
			accountsCS.setString(2, object.getString("location_code"));
			accountsCS.setString(3, object.getString("rr_number"));
			accountsCS.setString(4, object.getString("bill_date"));
			accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(5);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("loc_cd", accountsRS.getString("loc_cd"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("ldgr_no", accountsRS.getString("ldgr_no"));
				ackobj.put("actl_folio_no", accountsRS.getString("actl_folio_no"));
				ackobj.put("spt_folio_no", accountsRS.getString("spt_folio_no"));
				ackobj.put("trf_code", accountsRS.getString("trf_code"));
				ackobj.put("bill_dt", accountsRS.getString("bill_dt"));
				ackobj.put("consmr_name", accountsRS.getString("consmr_name"));
				ackobj.put("addr1", accountsRS.getString("addr1"));
				ackobj.put("addr2", accountsRS.getString("addr2"));
				ackobj.put("addr3", accountsRS.getString("addr3"));
				ackobj.put("billing_month", accountsRS.getString("billing_month"));
				ackobj.put("rdg_dt", accountsRS.getString("rdg_dt"));
				ackobj.put("mtr_rdr_cd", accountsRS.getString("mtr_rdr_cd"));
				ackobj.put("instal_sts", accountsRS.getString("instal_sts"));
				ackobj.put("line_min", accountsRS.getString("line_min"));
				ackobj.put("sanct_hp", accountsRS.getString("sanct_hp"));
				ackobj.put("sanct_kw", accountsRS.getString("sanct_kw"));
				ackobj.put("ct_ratio", accountsRS.getString("ct_ratio"));
				ackobj.put("prev_rdg", accountsRS.getString("prev_rdg"));
				ackobj.put("avg_consmp", accountsRS.getString("avg_consmp"));
				ackobj.put("solar_rebate", accountsRS.getString("solar_rebate"));
				ackobj.put("fl_rebate", accountsRS.getString("fl_rebate"));
				ackobj.put("ph_rebate", accountsRS.getString("ph_rebate"));
				ackobj.put("telep_rebate", accountsRS.getString("telep_rebate"));
				ackobj.put("pwr_factor", accountsRS.getString("pwr_factor"));
				ackobj.put("mtrchng_dt1", accountsRS.getString("mtrchng_dt1"));
				ackobj.put("mtrchng_rdg1", accountsRS.getString("mtrchng_rdg1"));
				ackobj.put("mtrchn_gdt2", accountsRS.getString("mtrchn_gdt2"));
				ackobj.put("mtrchn_grdg2", accountsRS.getString("mtrchn_grdg2"));
				ackobj.put("bill_amt", accountsRS.getString("bill_amt"));
				ackobj.put("demand_arrears", accountsRS.getString("demand_arrears"));
				ackobj.put("int_arrears", accountsRS.getString("int_arrears"));
				ackobj.put("tax_arrears", accountsRS.getString("tax_arrears"));
				ackobj.put("delay_int", accountsRS.getString("delay_int"));
				ackobj.put("amt_paid1", accountsRS.getString("amt_paid1"));
				ackobj.put("paid_date1", accountsRS.getString("paid_date1"));
				ackobj.put("amt_paid2", accountsRS.getString("amt_paid2"));
				ackobj.put("paid_date2", accountsRS.getString("paid_date2"));
				ackobj.put("othrs", accountsRS.getString("othrs"));
				ackobj.put("bill_gen_flag", accountsRS.getString("bill_gen_flag"));
				ackobj.put("meter", accountsRS.getString("meter"));
				ackobj.put("rebate_cap", accountsRS.getString("rebate_cap"));
				ackobj.put("previous_demand1", accountsRS.getString("previous_demand1"));
				ackobj.put("previous_demand2", accountsRS.getString("previous_demand2"));
				ackobj.put("previous_demand3", accountsRS.getString("previous_demand3"));
				ackobj.put("billing_mode", accountsRS.getString("billing_mode"));
				ackobj.put("mtr_const", accountsRS.getString("mtr_const"));
				ackobj.put("demand_based", accountsRS.getString("demand_based"));
				ackobj.put("rural_rebate", accountsRS.getString("rural_rebate"));
				ackobj.put("normal", accountsRS.getString("normal"));
				ackobj.put("appeal_amount", accountsRS.getString("appeal_amount"));
				ackobj.put("int_on_appeal_amt", accountsRS.getString("int_on_appeal_amt"));
				ackobj.put("kvah", accountsRS.getString("kvah"));
				ackobj.put("meter_tvm", accountsRS.getString("meter_tvm"));
				ackobj.put("inst_typ", accountsRS.getString("inst_typ"));
				ackobj.put("p_credit", accountsRS.getString("p_credit"));
				ackobj.put("p_debit", accountsRS.getString("p_debit"));
				ackobj.put("due_date", accountsRS.getString("due_date"));
				ackobj.put("dlmnr", accountsRS.getString("dlmnr"));
				ackobj.put("bill_num", accountsRS.getString("bill_num"));
				ackobj.put("delay_arrs", accountsRS.getString("delay_arrs"));
				ackobj.put("int_on_tax", accountsRS.getString("int_on_tax"));
				ackobj.put("ivrs_id", accountsRS.getString("ivrs_id"));
				ackobj.put("ecs_flg", accountsRS.getString("ecs_flg"));
				ackobj.put("part_period", accountsRS.getString("part_period"));
				ackobj.put("yrly_units", accountsRS.getString("yrly_units"));
				ackobj.put("yrly_bill_amt", accountsRS.getString("yrly_bill_amt"));
				ackobj.put("yrly_amt_paid", accountsRS.getString("yrly_amt_paid"));
				ackobj.put("cur_qrtr", accountsRS.getString("cur_qrtr"));
				ackobj.put("reg_penalty", accountsRS.getString("reg_penalty"));
				ackobj.put("intrst_arrs", accountsRS.getString("intrst_arrs"));
				ackobj.put("tax_flg", accountsRS.getString("tax_flg"));
				ackobj.put("intrst_tax", accountsRS.getString("intrst_tax"));
				ackobj.put("dc_flg", accountsRS.getString("dc_flg"));
				ackobj.put("first_rdg_flg", accountsRS.getString("first_rdg_flg"));
				ackobj.put("err_prcnt", accountsRS.getString("err_prcnt"));
				ackobj.put("mnr_flg", accountsRS.getString("mnr_flg"));
				ackobj.put("old_mtr_consmp", accountsRS.getString("old_mtr_consmp"));
				ackobj.put("bill_flg", accountsRS.getString("bill_flg"));
				ackobj.put("bmd", accountsRS.getString("bmd"));
				ackobj.put("subdiv_name", accountsRS.getString("subdiv_name"));
				ackobj.put("prev_ckwh", accountsRS.getString("prev_ckwh"));
				ackobj.put("prsnt_ckwh", accountsRS.getString("prsnt_ckwh"));
				ackobj.put("bkwh", accountsRS.getString("bkwh"));
				ackobj.put("kva", accountsRS.getString("kva"));
				ackobj.put("submtr_flg", accountsRS.getString("submtr_flg"));
				ackobj.put("no_tax_comp", accountsRS.getString("no_tax_comp"));
				ackobj.put("prev_rdg_flg", accountsRS.getString("prev_rdg_flg"));
				ackobj.put("pl_flg", accountsRS.getString("pl_flg"));
				ackobj.put("oth_arrs", accountsRS.getString("oth_arrs"));
				ackobj.put("srvr_hhd", accountsRS.getString("srvr_hhd"));
				ackobj.put("calc_pf_flg", accountsRS.getString("calc_pf_flg"));
				ackobj.put("last_dl_days", accountsRS.getString("last_dl_days"));
				ackobj.put("frequency", accountsRS.getString("frequency"));
				ackobj.put("annual_min_fix", accountsRS.getString("annual_min_fix"));
				ackobj.put("capacity_rbt", accountsRS.getString("capacity_rbt"));
				ackobj.put("orfanage_rbt", accountsRS.getString("orfanage_rbt"));
				ackobj.put("chq_dis_flg", accountsRS.getString("chq_dis_flg"));
				ackobj.put("ecs_rebate", accountsRS.getString("ecs_rebate"));
				ackobj.put("orphanage_rebate", accountsRS.getString("orphanage_rebate"));
				ackobj.put("dep_int", accountsRS.getString("dep_int"));
				ackobj.put("mc_flg", accountsRS.getString("mc_flg"));
				ackobj.put("hud_wkly_min_amt", accountsRS.getString("hud_wkly_min_amt"));
				ackobj.put("mmd_dep", accountsRS.getString("mmd_dep"));
				ackobj.put("mmd_dep_int", accountsRS.getString("mmd_dep_int"));
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
				obj.put("message", "HHD Download Details Retirved !!!");
				obj.put("hhd_download_details", array);
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
	public JSONObject getAppealAmountDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_APPEAL_AMT_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("rr_number"));
			accountsCS.setString(3, object.getString("serial_number"));
			accountsCS.setString(4, object.getString("charge_code"));
			accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(5);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("tariff", accountsRS.getString("tariff"));
				ackobj.put("sl_no", accountsRS.getString("sl_no"));
				ackobj.put("amt", accountsRS.getString("amt"));
				ackobj.put("from_dt", accountsRS.getString("from_dt"));
				ackobj.put("to_dt", accountsRS.getString("to_dt"));
				ackobj.put("appl_cd", accountsRS.getString("appl_cd"));
				ackobj.put("appl_cd_description", accountsRS.getString("appl_cd_description"));
				ackobj.put("pre_dep_amt", accountsRS.getString("pre_dep_amt"));
				ackobj.put("rcpt_no", accountsRS.getString("rcpt_no"));
				ackobj.put("rcpt_dt", accountsRS.getString("rcpt_dt"));
				ackobj.put("appl_cd_by", accountsRS.getString("appl_cd_by"));
				ackobj.put("appl_disp_by", accountsRS.getString("appl_disp_by"));
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
				obj.put("message", "Appeal Amount Details Retirved !!!");
				obj.put("appeal_amount_details", array);
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
	public JSONObject upsertApealAmountDetails(JSONObject object) {
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
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_APPEAL_AMT);
					
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, "");
					accountsCS.setString(3, (String) object.get("appl_cd"));
					accountsCS.setString(4, (String) object.get("amt"));
					accountsCS.setString(5, (String) object.get("from_dt"));
					accountsCS.setString(6, (String) object.get("to_dt"));
					accountsCS.setString(7, (String) object.get("remarks"));
					accountsCS.setString(8, (String) object.get("pre_dep_amt"));
					accountsCS.setString(9, (String) object.get("rcpt_no"));
					accountsCS.setString(10, (String) object.get("rcpt_dt"));
					accountsCS.setString(11, (String) object.get("appl_cd_by"));
					accountsCS.setString(12, (String) object.get("appl_disp_by"));
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
							obj.put("message", "Apeal Amount Details Is Already Exists For Given Date Range !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Apeal Amount Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
							obj.put("sl_no", accountsRS.getString("SL_NO"));
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Apeal Amount Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else if (RESP.equalsIgnoreCase("DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("MAX_DT"));
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " To Date : " +  accountsRS.getString("TO_DT") + " Should Be Greater Than From Date : "+ accountsRS.getString("FROM_DT"));
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_APPEAL_AMT);
					
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, (String) object.get("sl_no"));
					accountsCS.setString(3, (String) object.get("appl_cd"));
					accountsCS.setString(4, (String) object.get("amt"));
					accountsCS.setString(5, (String) object.get("from_dt"));
					accountsCS.setString(6, (String) object.get("to_dt"));
					accountsCS.setString(7, (String) object.get("remarks"));
					accountsCS.setString(8, (String) object.get("pre_dep_amt"));
					accountsCS.setString(9, (String) object.get("rcpt_no"));
					accountsCS.setString(10, (String) object.get("rcpt_dt"));
					accountsCS.setString(11, (String) object.get("appl_cd_by"));
					accountsCS.setString(12, (String) object.get("appl_disp_by"));
					accountsCS.setString(13, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(14, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(14);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Apeal Amount Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " To Date : " +  accountsRS.getString("TO_DT") + " Should Be Greater Than From Date : "+ accountsRS.getString("FROM_DT"));
						}else{
							obj.put("status", "error");
							obj.put("message", " Apeal Amount Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
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
	public JSONObject getDesignationDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_DESIGNATION_DETAILS);
			accountsCS.setString(1, object.getString("designation_type"));
			accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(2);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("dsg_categ", accountsRS.getString("dsg_categ"));
				ackobj.put("dsg_categ_description", accountsRS.getString("dsg_categ_description"));
				ackobj.put("key", accountsRS.getString("dsg_cd"));
				ackobj.put("value", accountsRS.getString("dsg_descr"));
				ackobj.put("dsg_cd", accountsRS.getString("dsg_cd"));
				ackobj.put("dsg_del_flg", accountsRS.getString("dsg_del_flg"));
				ackobj.put("dsg_del_flg_descr", accountsRS.getString("dsg_del_flg_descr"));
				ackobj.put("dsg_descr", accountsRS.getString("dsg_descr"));
				ackobj.put("dsg_fde_cd", accountsRS.getString("dsg_fde_cd"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Designation Details Retirved !!!");
				obj.put("designation_details", array);
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
	public JSONObject upsertDesignationDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String dsg_cd = (String) object.get("dsg_cd");
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		
		System.out.println(object);
		
		try {
			if(!dsg_cd.isEmpty() ){
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				accountsCS=dbConnection.prepareCall(DBQueries.UPSERT_DESIGNATION_DETAILS);
				accountsCS.setString(1, dsg_cd);
				accountsCS.setString(2, (String) object.get("dsg_descr"));
				accountsCS.setString(3, (String) object.get("dsg_categ"));
				accountsCS.setString(4, (String) object.get("dsg_del_flg"));
				accountsCS.setString(5, db_option);
				accountsCS.setString(6, (String) object.get("userid"));
				
				accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(7);
				
				if(accountsRS.next()){
					String RESP = accountsRS.getString("RESP");
					System.out.println("RESP : "+RESP);
					
					if(RESP.equalsIgnoreCase("success")){
						obj.put("status", "success");
						obj.put("message", " Designation Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
					}else if (RESP.equalsIgnoreCase("fail")) {
						obj.put("status", "error");
						obj.put("message", " Designation Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
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
			//DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		
		return obj;
	}
	
}
