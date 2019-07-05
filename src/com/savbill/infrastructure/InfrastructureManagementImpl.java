package com.savbill.infrastructure;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;

import com.savbill.database.DBManagerIMPL;
import com.savbill.database.DBManagerResourceRelease;
import com.savbill.database.DBQueries;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

public class InfrastructureManagementImpl implements IInfrastructureManagement {
	
	// database connection initialize
	DBManagerIMPL databaseObj = new DBManagerIMPL();
	Connection dbConnection;
	
	@Override
	public JSONObject getLocationDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String location_code = (String) object.get("location_code");
		String location_type = (String) object.get("location_type");
		String conn_type = (String) object.get("conn_type");
		
		System.out.println(object);
		
		try {
			if(!location_code.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				accountsCS=dbConnection.prepareCall(DBQueries.GET_LOCATION_DETAILS);
				accountsCS.setString(1, location_code);
				accountsCS.setString(2, location_type);
				
				accountsCS.registerOutParameter(3, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(3);
				
				while(accountsRS.next())
				{
					JSONObject ackobj=new JSONObject();
					
					ackobj.put("row_num", accountsRS.getString("row_num"));
					ackobj.put("key", accountsRS.getString("loc_cd"));
					ackobj.put("value", accountsRS.getString("loc_name"));
					ackobj.put("loc_cd", accountsRS.getString("loc_cd"));
					ackobj.put("loc_name", accountsRS.getString("loc_name"));
					ackobj.put("loc_type", accountsRS.getString("loc_type"));
	
					array.add(ackobj);
				}
				if(array.isEmpty()) {
					obj.put("status", "success");
					obj.put("message", "No Records Found !!!");
				} else{
					obj.put("status", "success");
					obj.put("location_details", array);
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter Proper Input");
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
	public JSONObject getStationDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_STATION_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("station_code"));
			accountsCS.setString(3, object.getString("station_type"));
			accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(4);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("stn_cd", accountsRS.getString("stn_cd"));
				ackobj.put("stn_name", accountsRS.getString("stn_name"));
				ackobj.put("comm_dt", accountsRS.getString("comm_dt"));
				ackobj.put("stn_typ", accountsRS.getString("stn_typ"));
				ackobj.put("stn_typ_name", accountsRS.getString("stn_typ_name"));
				ackobj.put("taluk_cd", accountsRS.getString("taluk_cd"));
				ackobj.put("taluk_name", accountsRS.getString("taluk_name"));
				ackobj.put("district_cd", accountsRS.getString("district_cd"));
				ackobj.put("district_name", accountsRS.getString("district_name"));
				ackobj.put("dvn_name", accountsRS.getString("dvn_name"));
				ackobj.put("dvn_cd", accountsRS.getString("dvn_cd"));
				ackobj.put("circle_name", accountsRS.getString("circle_name"));
				ackobj.put("circle_cd", accountsRS.getString("circle_cd"));
				ackobj.put("zone_name", accountsRS.getString("zone_name"));
				ackobj.put("zone_cd", accountsRS.getString("zone_cd"));
				ackobj.put("region_cd", accountsRS.getString("region_cd"));
				ackobj.put("region_name", accountsRS.getString("region_name"));
				ackobj.put("state_constcy_cd", accountsRS.getString("state_constcy_cd"));
				ackobj.put("state_constcy_name", accountsRS.getString("state_constcy_name"));
				ackobj.put("centrl_constcy_cd", accountsRS.getString("centrl_constcy_cd"));
				ackobj.put("centrl_constcy_name", accountsRS.getString("centrl_constcy_name"));
				ackobj.put("hotline_no", accountsRS.getString("hotline_no"));
				ackobj.put("plcc_no", accountsRS.getString("plcc_no"));
				ackobj.put("pabx_no", accountsRS.getString("pabx_no"));
				ackobj.put("tel_no", accountsRS.getString("tel_no"));
				ackobj.put("fax_no", accountsRS.getString("fax_no"));
				ackobj.put("misc_no", accountsRS.getString("misc_no"));
				ackobj.put("peak_ld", accountsRS.getString("peak_ld"));
				ackobj.put("peak_ld_dt", accountsRS.getString("peak_ld_dt"));
				ackobj.put("gen_mode", accountsRS.getString("gen_mode"));
				ackobj.put("gen_mode_name", accountsRS.getString("gen_mode_name"));
				ackobj.put("pm_schd_gen_flg_description", accountsRS.getString("pm_schd_gen_flg_description"));
				ackobj.put("pm_schd_gen_flg", accountsRS.getString("pm_schd_gen_flg"));
				ackobj.put("dc_ctrl_volt1", accountsRS.getString("dc_ctrl_volt1"));
				ackobj.put("dc_ctrl_volt2", accountsRS.getString("dc_ctrl_volt2"));
				ackobj.put("installed_cap", accountsRS.getString("installed_cap"));
				ackobj.put("stn_area", accountsRS.getString("stn_area"));
				ackobj.put("est_cost", accountsRS.getString("est_cost"));
				ackobj.put("reamrks", accountsRS.getString("reamrks"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));

				array.add(ackobj);
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Sub-Station Details Retrieved !!!");
				obj.put("station_details", array);
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
	public JSONObject upsertStationDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String conn_type = (String) object.get("conn_type");
		String db_option = (String) object.get("option");
		String stn_cd = (String) object.get("stn_cd");
		
		System.out.println(object);
		
		try {
			if(!stn_cd.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_STATION_DETAILS);
					accountsCS.setString(1, (String) object.get("location_code"));
					accountsCS.setString(2, stn_cd);
					accountsCS.setString(3, (String) object.get("stn_name"));
					accountsCS.setString(4, (String) object.get("stn_typ"));
					accountsCS.setString(5, (String) object.get("centrl_constcy_cd"));
					accountsCS.setString(6, (String) object.get("state_constcy_cd"));
					accountsCS.setString(7, (String) object.get("region_cd"));
					accountsCS.setString(8, (String) object.get("district_cd"));
					accountsCS.setString(9, (String) object.get("taluk_cd"));
					accountsCS.setString(10, (String) object.get("pm_schd_gen_flg"));
					accountsCS.setString(11, (String) object.get("gen_mode"));
					accountsCS.setString(12, (String) object.get("dc_ctrl_volt1"));
					accountsCS.setString(13, (String) object.get("dc_ctrl_volt2"));
					accountsCS.setString(14, (String) object.get("comm_dt"));
					accountsCS.setString(15, (String) object.get("tel_no"));
					accountsCS.setString(16, (String) object.get("plcc_no"));
					accountsCS.setString(17, (String) object.get("pabx_no"));
					accountsCS.setString(18, (String) object.get("misc_no"));
					accountsCS.setString(19, (String) object.get("hotline_no"));
					accountsCS.setString(20, (String) object.get("stn_area"));
					accountsCS.setString(21, (String) object.get("est_cost"));
					accountsCS.setString(22, (String) object.get("zone_cd"));
					accountsCS.setString(23, (String) object.get("circle_cd"));
					accountsCS.setString(24, (String) object.get("dvn_cd"));
					accountsCS.setString(25, (String) object.get("peak_ld"));
					accountsCS.setString(26, (String) object.get("peak_ld_dt"));
					accountsCS.setString(27, (String) object.get("fax_no"));
					accountsCS.setString(28, (String) object.get("installed_cap"));
					accountsCS.setString(29, (String) object.get("reamrks"));
					accountsCS.setString(30, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(31, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(31);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("dup_code")){
							obj.put("status", "error");
							obj.put("message", stn_cd+" Is Already Exists.. !");
						}else if(RESP.equalsIgnoreCase("dup_name")){
							obj.put("status", "error");
							obj.put("message", (String) object.get("stn_name")+" Is Already Exists.. !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Station Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" Successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Station Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_STATION_DETAILS);
					accountsCS.setString(1, (String) object.get("location_code"));
					accountsCS.setString(2, stn_cd);
					accountsCS.setString(3, (String) object.get("stn_name"));
					accountsCS.setString(4, (String) object.get("stn_typ"));
					accountsCS.setString(5, (String) object.get("centrl_constcy_cd"));
					accountsCS.setString(6, (String) object.get("state_constcy_cd"));
					accountsCS.setString(7, (String) object.get("region_cd"));
					accountsCS.setString(8, (String) object.get("district_cd"));
					accountsCS.setString(9, (String) object.get("taluk_cd"));
					accountsCS.setString(10, (String) object.get("pm_schd_gen_flg"));
					accountsCS.setString(11, (String) object.get("gen_mode"));
					accountsCS.setString(12, (String) object.get("dc_ctrl_volt1"));
					accountsCS.setString(13, (String) object.get("dc_ctrl_volt2"));
					accountsCS.setString(14, (String) object.get("comm_dt"));
					accountsCS.setString(15, (String) object.get("tel_no"));
					accountsCS.setString(16, (String) object.get("plcc_no"));
					accountsCS.setString(17, (String) object.get("pabx_no"));
					accountsCS.setString(18, (String) object.get("misc_no"));
					accountsCS.setString(19, (String) object.get("hotline_no"));
					accountsCS.setString(20, (String) object.get("stn_area"));
					accountsCS.setString(21, (String) object.get("est_cost"));
					accountsCS.setString(22, (String) object.get("zone_cd"));
					accountsCS.setString(23, (String) object.get("circle_cd"));
					accountsCS.setString(24, (String) object.get("dvn_cd"));
					accountsCS.setString(25, (String) object.get("peak_ld"));
					accountsCS.setString(26, (String) object.get("peak_ld_dt"));
					accountsCS.setString(27, (String) object.get("fax_no"));
					accountsCS.setString(28, (String) object.get("installed_cap"));
					accountsCS.setString(29, (String) object.get("reamrks"));
					accountsCS.setString(30, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(31, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(31);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Station Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" Successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Station Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
					
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a Valid Station Code");
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
	public JSONObject getFeederDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_FEEDER_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("station_code"));
			accountsCS.setString(3, object.getString("feeder_code"));
			accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(4);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("stn_cd", accountsRS.getString("stn_cd"));
				ackobj.put("stn_name", accountsRS.getString("stn_name"));
				ackobj.put("fdr_no", accountsRS.getString("fdr_no"));
				ackobj.put("fdr_name", accountsRS.getString("fdr_name"));
				ackobj.put("fdr_type", accountsRS.getString("fdr_type"));
				ackobj.put("fdr_typ_descr", accountsRS.getString("fdr_typ_descr"));
				ackobj.put("comm_dt", accountsRS.getString("comm_dt"));
				ackobj.put("fdr_volt", accountsRS.getString("fdr_volt"));
				ackobj.put("fdr_volt_descr", accountsRS.getString("fdr_volt_descr"));
				ackobj.put("cond_typ", accountsRS.getString("cond_typ"));
				ackobj.put("cond_typ_descr", accountsRS.getString("cond_typ_descr"));
				ackobj.put("fdr_source", accountsRS.getString("fdr_source"));
				ackobj.put("fdr_source_descr", accountsRS.getString("fdr_source_descr"));
				ackobj.put("cable_size", accountsRS.getString("cable_size"));
				ackobj.put("fdr_cap", accountsRS.getString("fdr_cap"));
				ackobj.put("load_typ", accountsRS.getString("load_typ"));
				ackobj.put("load_typ_descr", accountsRS.getString("load_typ_descr"));
				ackobj.put("conn_ld", accountsRS.getString("conn_ld"));
				ackobj.put("peak_ld", accountsRS.getString("peak_ld"));
				ackobj.put("peak_ld_dt", accountsRS.getString("peak_ld_dt"));
				ackobj.put("reserve_ld", accountsRS.getString("reserve_ld"));
				ackobj.put("fdr_len", accountsRS.getString("fdr_len"));
				ackobj.put("schd_gen_flg", accountsRS.getString("schd_gen_flg"));
				ackobj.put("schd_gen_flg_descr", accountsRS.getString("schd_gen_flg_descr"));
				ackobj.put("sts_cd", accountsRS.getString("sts_cd"));
				ackobj.put("sts_cd_descr", accountsRS.getString("sts_cd_descr"));
				ackobj.put("last_maint_dt", accountsRS.getString("last_maint_dt"));
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
				obj.put("message", "Feeder Details Retrieved !!!");
				obj.put("feeder_details", array);
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
	public JSONObject upsertFeederDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String conn_type = (String) object.get("conn_type");
		String db_option = (String) object.get("option");
		String fdr_no = (String) object.get("fdr_no");
		
		System.out.println(object);
		
		try {
			if(!fdr_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_FEEDER_DETAILS);
					accountsCS.setString(1, (String) object.get("location_code"));
					accountsCS.setString(2, (String) object.get("stn_cd"));
					accountsCS.setString(3, fdr_no);
					accountsCS.setString(4, (String) object.get("fdr_name"));
					accountsCS.setString(5, (String) object.get("fdr_type"));
					accountsCS.setString(6, (String) object.get("comm_dt"));
					accountsCS.setString(7, (String) object.get("fdr_volt"));
					accountsCS.setString(8, (String) object.get("fdr_cap"));
					accountsCS.setString(9, (String) object.get("schd_gen_flg"));
					accountsCS.setString(10, (String) object.get("fdr_source"));
					accountsCS.setString(11, (String) object.get("fdr_len"));
					accountsCS.setString(12, (String) object.get("cond_typ"));
					accountsCS.setString(13, (String) object.get("peak_ld"));
					accountsCS.setString(14, (String) object.get("peak_ld_dt"));
					accountsCS.setString(15, (String) object.get("last_maint_dt"));
					accountsCS.setString(16, (String) object.get("sts_cd"));
					accountsCS.setString(17, (String) object.get("conn_ld"));
					accountsCS.setString(18, (String) object.get("reserve_ld"));
					accountsCS.setString(19, (String) object.get("load_typ"));
					accountsCS.setString(20, (String) object.get("cable_size"));
					accountsCS.setString(21, (String) object.get("remarks"));
					accountsCS.setString(22, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(23, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(23);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("dup_code")){
							obj.put("status", "error");
							obj.put("message", fdr_no +" Is Already Exists.. !");
						}else if(RESP.equalsIgnoreCase("dup_name")){
							obj.put("status", "error");
							obj.put("message", (String) object.get("fdr_name")+" Is Already Exists.. !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Feeder Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" Successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Feeder Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_FEEDER_DETAILS);
					accountsCS.setString(1, (String) object.get("location_code"));
					accountsCS.setString(2, (String) object.get("stn_cd"));
					accountsCS.setString(3, fdr_no);
					accountsCS.setString(4, (String) object.get("fdr_name"));
					accountsCS.setString(5, (String) object.get("fdr_type"));
					accountsCS.setString(6, (String) object.get("comm_dt"));
					accountsCS.setString(7, (String) object.get("fdr_volt"));
					accountsCS.setString(8, (String) object.get("fdr_cap"));
					accountsCS.setString(9, (String) object.get("schd_gen_flg"));
					accountsCS.setString(10, (String) object.get("fdr_source"));
					accountsCS.setString(11, (String) object.get("fdr_len"));
					accountsCS.setString(12, (String) object.get("cond_typ"));
					accountsCS.setString(13, (String) object.get("peak_ld"));
					accountsCS.setString(14, (String) object.get("peak_ld_dt"));
					accountsCS.setString(15, (String) object.get("last_maint_dt"));
					accountsCS.setString(16, (String) object.get("sts_cd"));
					accountsCS.setString(17, (String) object.get("conn_ld"));
					accountsCS.setString(18, (String) object.get("reserve_ld"));
					accountsCS.setString(19, (String) object.get("load_typ"));
					accountsCS.setString(20, (String) object.get("cable_size"));
					accountsCS.setString(21, (String) object.get("remarks"));
					accountsCS.setString(22, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(23, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(23);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Feeder Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" Successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Feeder Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
					
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a Valid Feeder Code");
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
	public JSONObject getDTCDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_DTC_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("station_code"));
			accountsCS.setString(3, object.getString("feeder_code"));
			accountsCS.setString(4, object.getString("om_code"));
			accountsCS.setString(5, object.getString("dtc_code"));
			accountsCS.registerOutParameter(6, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(6);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("loc_cd", accountsRS.getString("loc_cd"));
				ackobj.put("loc_name", accountsRS.getString("loc_name"));
				ackobj.put("stn_cd", accountsRS.getString("stn_cd"));
				ackobj.put("stn_name", accountsRS.getString("stn_name"));
				ackobj.put("fdr_cd", accountsRS.getString("fdr_cd"));
				ackobj.put("fdr_name", accountsRS.getString("fdr_name"));
				ackobj.put("dtc_cd", accountsRS.getString("dtc_cd"));
				ackobj.put("dtc_name", accountsRS.getString("dtc_name"));
				ackobj.put("comm_dt", accountsRS.getString("comm_dt"));
				ackobj.put("make_cd", accountsRS.getString("make_cd"));
				ackobj.put("make_descr", accountsRS.getString("make_descr"));
				ackobj.put("sl_no", accountsRS.getString("sl_no"));
				ackobj.put("district_cd", accountsRS.getString("district_cd"));
				ackobj.put("district_descr", accountsRS.getString("district_descr"));
				ackobj.put("taluk_cd", accountsRS.getString("taluk_cd"));
				ackobj.put("taluk_descr", accountsRS.getString("taluk_descr"));
				ackobj.put("region_cd", accountsRS.getString("region_cd"));
				ackobj.put("region_descr", accountsRS.getString("region_descr"));
				ackobj.put("po_no", accountsRS.getString("po_no"));
				ackobj.put("po_dt", accountsRS.getString("po_dt"));
				ackobj.put("supply_dt", accountsRS.getString("supply_dt"));
				ackobj.put("gnt_upto", accountsRS.getString("gnt_upto"));
				ackobj.put("dtc_cap", accountsRS.getString("dtc_cap"));
				ackobj.put("dtc_conn_ld", accountsRS.getString("dtc_conn_ld"));
				ackobj.put("dtc_reserve_ld", accountsRS.getString("dtc_reserve_ld"));
				ackobj.put("volt_rating_cd", accountsRS.getString("volt_rating_cd"));
				ackobj.put("peak_ld", accountsRS.getString("peak_ld"));
				ackobj.put("peak_ld_dt", accountsRS.getString("peak_ld_dt"));
				ackobj.put("sts_flg", accountsRS.getString("sts_flg"));
				ackobj.put("sts_flg_descr", accountsRS.getString("sts_flg_descr"));
				ackobj.put("last_maint_dt", accountsRS.getString("last_maint_dt"));
				ackobj.put("sts_cd", accountsRS.getString("sts_cd"));
				ackobj.put("sts_cd_descr", accountsRS.getString("sts_cd_descr"));
				ackobj.put("town_cd", accountsRS.getString("town_cd"));
				ackobj.put("town_cd_descr", accountsRS.getString("town_cd_descr"));
				ackobj.put("dtc_instls", accountsRS.getString("dtc_instls"));
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
				obj.put("message", "DTC Details Retrieved !!!");
				obj.put("dtc_details", array);
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
	public JSONObject upsertDTCDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String conn_type = (String) object.get("conn_type");
		String db_option = (String) object.get("option");
		String dtc_cd = (String) object.get("dtc_cd");
		
		System.out.println(object);
		
		try {
			if(!dtc_cd.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_DTC_DETAILS);
					accountsCS.setString(1, (String) object.get("stn_cd"));
					accountsCS.setString(2, (String) object.get("fdr_cd"));
					accountsCS.setString(3, (String) object.get("loc_cd"));
					accountsCS.setString(4, dtc_cd);
					accountsCS.setString(5, (String) object.get("dtc_name"));
					accountsCS.setString(6, (String) object.get("make_cd"));
					accountsCS.setString(7, (String) object.get("sl_no"));
					accountsCS.setString(8, (String) object.get("region_cd"));
					accountsCS.setString(9, (String) object.get("district_cd"));
					accountsCS.setString(10, (String) object.get("taluk_cd"));
					accountsCS.setString(11, (String) object.get("comm_dt"));
					accountsCS.setString(12, (String) object.get("po_no"));
					accountsCS.setString(13, (String) object.get("po_dt"));
					accountsCS.setString(14, (String) object.get("sts_flg"));
					accountsCS.setString(15, (String) object.get("gnt_upto"));
					accountsCS.setString(16, (String) object.get("dtc_cap"));
					accountsCS.setString(17, (String) object.get("dtc_conn_ld"));
					accountsCS.setString(18, (String) object.get("dtc_reserve_ld"));
					accountsCS.setString(19, (String) object.get("volt_rating_cd"));
					accountsCS.setString(20, (String) object.get("sts_cd"));
					accountsCS.setString(21, (String) object.get("supply_dt"));
					accountsCS.setString(22, (String) object.get("last_maint_dt"));
					accountsCS.setString(23, (String) object.get("peak_ld"));
					accountsCS.setString(24, (String) object.get("peak_ld_dt"));
					accountsCS.setString(25, (String) object.get("remarks"));
					accountsCS.setString(26, (String) object.get("town_cd"));
					accountsCS.setString(27, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(28, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(28);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("dup_code")){
							obj.put("status", "error");
							obj.put("message", dtc_cd +" Is Already Exists.. !");
						}else if(RESP.equalsIgnoreCase("dup_name")){
							obj.put("status", "error");
							obj.put("message", (String) object.get("dtc_name")+" Is Already Exists.. !");
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Transformer Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" Successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Transformer Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
						
					}
														
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_DTC_DETAILS);
					accountsCS.setString(1, (String) object.get("stn_cd"));
					accountsCS.setString(2, (String) object.get("fdr_cd"));
					accountsCS.setString(3, (String) object.get("loc_cd"));
					accountsCS.setString(4, dtc_cd);
					accountsCS.setString(5, (String) object.get("dtc_name"));
					accountsCS.setString(6, (String) object.get("make_cd"));
					accountsCS.setString(7, (String) object.get("sl_no"));
					accountsCS.setString(8, (String) object.get("region_cd"));
					accountsCS.setString(9, (String) object.get("district_cd"));
					accountsCS.setString(10, (String) object.get("taluk_cd"));
					accountsCS.setString(11, (String) object.get("comm_dt"));
					accountsCS.setString(12, (String) object.get("po_no"));
					accountsCS.setString(13, (String) object.get("po_dt"));
					accountsCS.setString(14, (String) object.get("sts_flg"));
					accountsCS.setString(15, (String) object.get("gnt_upto"));
					accountsCS.setString(16, (String) object.get("dtc_cap"));
					accountsCS.setString(17, (String) object.get("dtc_conn_ld"));
					accountsCS.setString(18, (String) object.get("dtc_reserve_ld"));
					accountsCS.setString(19, (String) object.get("volt_rating_cd"));
					accountsCS.setString(20, (String) object.get("sts_cd"));
					accountsCS.setString(21, (String) object.get("supply_dt"));
					accountsCS.setString(22, (String) object.get("last_maint_dt"));
					accountsCS.setString(23, (String) object.get("peak_ld"));
					accountsCS.setString(24, (String) object.get("peak_ld_dt"));
					accountsCS.setString(25, (String) object.get("remarks"));
					accountsCS.setString(26, (String) object.get("town_cd"));
					accountsCS.setString(27, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(28, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(28);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Transformer Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" Successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Transformer Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
					}
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a Valid Transformer Code");
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
	public JSONObject getOMDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = (String) object.get("conn_type");
		
		try {
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			accountsCS=dbConnection.prepareCall(DBQueries.GET_OM_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(2);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("om_code", accountsRS.getString("om_code"));
				ackobj.put("om_name", accountsRS.getString("om_name"));
				ackobj.put("addr1", accountsRS.getString("addr1"));
				ackobj.put("addr2", accountsRS.getString("addr2"));
				ackobj.put("addr3", accountsRS.getString("addr3"));
				ackobj.put("addr4", accountsRS.getString("addr4"));
				ackobj.put("town_city", accountsRS.getString("town_city"));
				ackobj.put("pin_code", accountsRS.getString("pin_code"));
				ackobj.put("nature_of_loc", accountsRS.getString("nature_of_loc"));
				ackobj.put("acctng_flag", accountsRS.getString("acctng_flag"));
				ackobj.put("acctng_flag_descr", accountsRS.getString("acctng_flag_descr"));
				ackobj.put("org_cd", accountsRS.getString("org_cd"));
				ackobj.put("accting_cd", accountsRS.getString("accting_cd"));
				ackobj.put("city_typ", accountsRS.getString("city_typ"));
				ackobj.put("city_typ_descr", accountsRS.getString("city_typ_descr"));
				ackobj.put("office_typ", accountsRS.getString("office_typ"));
				ackobj.put("office_typ_descr", accountsRS.getString("office_typ_descr"));
				ackobj.put("auth_officer", accountsRS.getString("auth_officer"));
				ackobj.put("compilation_cd", accountsRS.getString("compilation_cd"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));

				array.add(ackobj);
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "O&M Details Retrieved !!!");
				obj.put("om_details", array);
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
	public JSONObject upsertOMDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String conn_type = (String) object.get("conn_type");
		String db_option = (String) object.get("option");
		
		try {
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				accountsCS=dbConnection.prepareCall(DBQueries.UPSERT_OM_DETAILS);
				accountsCS.setString(1, (String) object.get("location_code"));
				accountsCS.setString(2, (String) object.get("om_code"));
				accountsCS.setString(3, (String) object.get("om_name"));
				accountsCS.setString(4, (String) object.get("office_typ"));
				accountsCS.setString(5, (String) object.get("acctng_flag"));
				accountsCS.setString(6, (String) object.get("addr1"));
				accountsCS.setString(7, (String) object.get("addr2"));
				accountsCS.setString(8, (String) object.get("addr3"));
				accountsCS.setString(9, (String) object.get("addr4"));
				accountsCS.setString(10, (String) object.get("town_city"));
				accountsCS.setString(11, (String) object.get("city_typ"));
				accountsCS.setString(12, (String) object.get("pin_code"));
				accountsCS.setString(13, (String) object.get("auth_officer"));
				accountsCS.setString(14, (String) object.get("nature_of_loc")); 
				accountsCS.setString(15, (String) object.get("accting_cd"));
				accountsCS.setString(16, (String) object.get("org_cd"));
				accountsCS.setString(17, (String) object.get("compilation_cd"));
				accountsCS.setString(18, (String) object.get("userid"));
				
				accountsCS.registerOutParameter(19, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(19);
				
				if(accountsRS.next()){
					String RESP = accountsRS.getString("RESP");
					System.out.println("RESP : "+RESP);
					
					if(RESP.equalsIgnoreCase("success")){
						obj.put("status", "success");
						obj.put("message", " O&M Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
					}else if (RESP.equalsIgnoreCase("fail")) {
						obj.put("status", "error");
						obj.put("message", " O&M Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
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
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getTownDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = object.getString("conn_type");
		String type = object.getString("type");
		
		try {
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			accountsCS = dbConnection.prepareCall(DBQueries.GET_TOWN_DETAILS);
			accountsCS.setString(1, type);
			accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(2);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("type", accountsRS.getString("type"));
				ackobj.put("key", accountsRS.getString("town_cd"));
				ackobj.put("value", accountsRS.getString("town_descr"));
				ackobj.put("town_cd", accountsRS.getString("town_cd"));
				ackobj.put("town_descr", accountsRS.getString("town_descr"));
				ackobj.put("dist_descr", accountsRS.getString("dist_descr"));

				array.add(ackobj);
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("Town_List", array);
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
	public JSONObject getDTCTownMapDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_DTC_TOWN_MAP_DETLS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("station_code"));
			accountsCS.setString(3, object.getString("feeder_code"));
			accountsCS.setString(4, object.getString("om_code"));
			
			accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
			
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(5);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("dtc_cd", accountsRS.getString("dtc_cd"));
				ackobj.put("dtc_name", accountsRS.getString("dtc_name"));
				ackobj.put("stn_cd", accountsRS.getString("stn_cd"));
				ackobj.put("fdr_cd", accountsRS.getString("fdr_cd"));
				ackobj.put("om_cd", accountsRS.getString("om_cd"));
				ackobj.put("town_cd", accountsRS.getString("town_cd"));
				ackobj.put("town_descr", accountsRS.getString("town_descr"));
				ackobj.put("userid", accountsRS.getString("userid"));
				ackobj.put("tmpstp", accountsRS.getString("tmpstp"));

				array.add(ackobj);
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "DTC Town Mapping Details Retrieved !!!");
				obj.put("dtc_townmap_details", array);
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
	public JSONObject mapDTCTown(JSONObject data) {
		
		String userid = (String) data.get("userid");
		String conn_type = (String) data.get("conn_type");
		String town_code = (String) data.get("town_code");
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("data");
		    	
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		AckObj = mapIndividualDTCTown(objects, userid, conn_type, town_code);
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
	
	public JSONObject mapIndividualDTCTown(JSONObject object, String userid, String conn_type, String town_code) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		try {
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			accountsCS=dbConnection.prepareCall(DBQueries.MAP_DTC_TOWN);
			accountsCS.setString(1, object.getString("stn_cd"));
			accountsCS.setString(2, object.getString("fdr_cd"));
			accountsCS.setString(3, object.getString("om_cd"));
			accountsCS.setString(4, object.getString("dtc_cd"));
			accountsCS.setString(5, town_code);
			accountsCS.setString(6, userid);
			
			accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(7);
			
			if(accountsRS.next()){
				String RESP = accountsRS.getString("RESP");
				System.out.println("RESP : "+RESP);
				
				if(RESP.equalsIgnoreCase("success")){
					obj.put("status", "success");
					obj.put("message", town_code+" Town Mapped Successfully for Transformer "+object.getString("dtc_name"));
				}else if (RESP.equalsIgnoreCase("fail")) {
					obj.put("status", "error");
					obj.put("message", town_code+" Town Mapping failed for Transformer "+object.getString("dtc_name"));
				}
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
	public JSONObject getDTCDetailsForTransfer(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_DTC_TRANSFER_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("station_code"));
			accountsCS.setString(3, object.getString("feeder_code"));
			accountsCS.setString(4, object.getString("om_code"));
			accountsCS.setString(5, object.getString("dtc_code"));
			accountsCS.setString(6, object.getString("reading_day"));
			
			accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
			
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(7);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("mr_cd", accountsRS.getString("mr_cd"));
				ackobj.put("rdg_day", accountsRS.getString("rdg_day"));
				ackobj.put("spot_folio", accountsRS.getString("spot_folio"));
				ackobj.put("pole_no", accountsRS.getString("pole_no"));
				ackobj.put("status", accountsRS.getString("status"));
				ackobj.put("tariff", accountsRS.getString("tariff"));
				ackobj.put("load_kw", accountsRS.getString("load_kw"));
				ackobj.put("load_hp", accountsRS.getString("load_hp"));
				ackobj.put("load_kva", accountsRS.getString("load_kva"));

				array.add(ackobj);
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Transformer RR Number Details Retrieved !!!");
				obj.put("dtc_rrno_details", array);
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
	public JSONObject dtcTransfer(JSONObject data) {
		
		String conn_type = (String) data.get("conn_type");
		String userid = (String) data.get("userid");
		
		String to_station_code = (String) data.get("to_station_code");
		String to_feeder_code = (String) data.get("to_feeder_code");
		String to_om_code = (String) data.get("to_om_code");
		String to_transformer_code = (String) data.get("to_transformer_code");
		String to_reading_day = (String) data.get("to_reading_day");
		
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("data");
		    	
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		AckObj = dtcIndividualTransfer(objects, conn_type, userid, to_station_code, to_feeder_code, to_om_code, to_transformer_code, to_reading_day);
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
	
	public JSONObject dtcIndividualTransfer(JSONObject object, String conn_type, String userid, String to_station_code, String to_feeder_code, String to_om_code, String to_transformer_code, String to_reading_day) {
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
				
				accountsCS=dbConnection.prepareCall(DBQueries.DTC_TRANSFER);
				accountsCS.setString(1, rr_no);
				accountsCS.setString(2, to_station_code);
				accountsCS.setString(3, to_feeder_code);
				accountsCS.setString(4, to_om_code);
				accountsCS.setString(5, to_transformer_code);
				accountsCS.setString(6, to_reading_day);
				accountsCS.setString(7, userid);
				
				accountsCS.registerOutParameter(8, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(8);
				
				if(accountsRS.next()){
					String RESP = accountsRS.getString("RESP");
					System.out.println("RESP : "+RESP);
					
					if(RESP.equalsIgnoreCase("success")){
						obj.put("status", "success");
						obj.put("message", " DTC Transfer Successfull for the RR Number : "+(String) object.get("rr_no"));
					}else if (RESP.equalsIgnoreCase("fail")) {
						obj.put("status", "error");
						obj.put("message", " DTC Transfer Faild for the RR Number : "+(String) object.get("rr_no"));
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
		}
		return obj;
	}	

}
