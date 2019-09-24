package com.savbill.energyaudit;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.savbill.database.DBManagerIMPL;
import com.savbill.database.DBManagerResourceRelease;
import com.savbill.database.DBQueries;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

public class EnergyAuditManagementImpl implements IEnergyAuditManagement {
	
	// database connection initialize
	DBManagerIMPL databaseObj = new DBManagerIMPL();
	Connection dbConnection;
	
	@Override
	public JSONObject getDTCMeterMasterDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_DTC_MTR_MASTER_DETAILS);
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
				ackobj.put("stn_cd", accountsRS.getString("stn_cd"));
				ackobj.put("stn_name", accountsRS.getString("stn_name"));
				ackobj.put("fdr_cd", accountsRS.getString("fdr_cd"));
				ackobj.put("fdr_name", accountsRS.getString("fdr_name"));
				ackobj.put("om_cd", accountsRS.getString("om_cd"));
				ackobj.put("om_name", accountsRS.getString("om_name"));
				ackobj.put("dtc_cd", accountsRS.getString("dtc_cd"));
				ackobj.put("dtc_name", accountsRS.getString("dtc_name"));
				ackobj.put("dtc_sl_no", accountsRS.getString("dtc_sl_no"));
				ackobj.put("dtc_mtr_make", accountsRS.getString("dtc_mtr_make"));
				ackobj.put("dtc_mtr_make_descr", accountsRS.getString("dtc_mtr_make_descr"));
				ackobj.put("dtc_mtr_type", accountsRS.getString("dtc_mtr_type"));
				ackobj.put("dtc_mtr_type_descr", accountsRS.getString("dtc_mtr_type_descr"));
				ackobj.put("no_of_ph", accountsRS.getString("no_of_ph"));
				ackobj.put("no_of_ph_descr", accountsRS.getString("no_of_ph_descr"));
				ackobj.put("mtr_amp", accountsRS.getString("mtr_amp"));
				ackobj.put("mtr_volt", accountsRS.getString("mtr_volt"));
				ackobj.put("mtr_volt_descr", accountsRS.getString("mtr_volt_descr"));
				ackobj.put("max_mtr_no", accountsRS.getString("max_mtr_no"));
				ackobj.put("ct_ratio", accountsRS.getString("ct_ratio"));
				ackobj.put("mult_const", accountsRS.getString("mult_const"));
				ackobj.put("assign_dt", accountsRS.getString("assign_dt"));
				ackobj.put("release_dt", accountsRS.getString("release_dt"));
				ackobj.put("init_rdg", accountsRS.getString("init_rdg"));
				ackobj.put("final_rdg", accountsRS.getString("final_rdg"));
				ackobj.put("mtr_sts", accountsRS.getString("mtr_sts"));
				ackobj.put("mtr_sts_descr", accountsRS.getString("mtr_sts_descr"));
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
				obj.put("message", "DTC Meter Master Details Retrieved !!!");
				obj.put("dtc_meter_master_details", array);
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
	public JSONObject upsertDTCMeterMasterDetails(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String dtc_sl_no = (String) object.get("dtc_sl_no");
		String db_option = (String) object.get("option");
		String conn_type = (String) object.get("conn_type");
		
		try {
			if(!dtc_sl_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				if(db_option.equalsIgnoreCase("ADD")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_DTC_METER_DETAILS);
					accountsCS.setString(1, (String) object.get("stn_cd"));
					accountsCS.setString(2, (String) object.get("fdr_cd"));
					accountsCS.setString(3, (String) object.get("om_cd"));
					accountsCS.setString(4, (String) object.get("dtc_cd"));
					accountsCS.setString(5, dtc_sl_no);
					accountsCS.setString(6, (String) object.get("dtc_mtr_make"));
					accountsCS.setString(7, (String) object.get("dtc_mtr_type"));
					accountsCS.setString(8, (String) object.get("no_of_ph"));
					accountsCS.setString(9, (String) object.get("mtr_volt"));
					accountsCS.setString(10, (String) object.get("mtr_amp"));
					accountsCS.setString(11, (String) object.get("assign_dt"));
					accountsCS.setString(12, (String) object.get("release_dt"));
					accountsCS.setString(13, (String) object.get("init_rdg"));
					accountsCS.setString(14, (String) object.get("final_rdg"));
					accountsCS.setString(15, (String) object.get("ct_ratio"));
					accountsCS.setString(16, (String) object.get("mult_const"));
					accountsCS.setString(17, (String) object.get("max_mtr_no"));
					accountsCS.setString(18, (String) object.get("mtr_sts"));
					accountsCS.setString(19, (String) object.get("remarks"));
					accountsCS.setString(20, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(21, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(21);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("ACTIVE_RECORD")){
							obj.put("status", "error");
							obj.put("message", "Active Meter Details Found for Given Transformer...! ");
						}else if(RESP.equalsIgnoreCase("dup")){
							obj.put("status", "error");
							obj.put("message", "This Meter Details Alredy Found for "+accountsRS.getString("DTC_CD"));
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", "  Transformer Meter Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Transformer Meter Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else if (RESP.equalsIgnoreCase("DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("MAX_DT"));
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " To Date : " +  accountsRS.getString("TO_DT") + " Should Be Greater Than From Date : "+ accountsRS.getString("FROM_DT"));
						}
						
					}
				}else if(db_option.equalsIgnoreCase("UPDATE")){
					
					accountsCS=dbConnection.prepareCall(DBQueries.UPDATE_DTC_METER_DETAILS);
					accountsCS.setString(1, (String) object.get("stn_cd"));
					accountsCS.setString(2, (String) object.get("fdr_cd"));
					accountsCS.setString(3, (String) object.get("om_cd"));
					accountsCS.setString(4, (String) object.get("dtc_cd"));
					accountsCS.setString(5, dtc_sl_no);
					accountsCS.setString(6, (String) object.get("dtc_mtr_make"));
					accountsCS.setString(7, (String) object.get("dtc_mtr_type"));
					accountsCS.setString(8, (String) object.get("no_of_ph"));
					accountsCS.setString(9, (String) object.get("mtr_volt"));
					accountsCS.setString(10, (String) object.get("mtr_amp"));
					accountsCS.setString(11, (String) object.get("assign_dt"));
					accountsCS.setString(12, (String) object.get("release_dt"));
					accountsCS.setString(13, (String) object.get("init_rdg"));
					accountsCS.setString(14, (String) object.get("final_rdg"));
					accountsCS.setString(15, (String) object.get("ct_ratio"));
					accountsCS.setString(16, (String) object.get("mult_const"));
					accountsCS.setString(17, (String) object.get("max_mtr_no"));
					accountsCS.setString(18, (String) object.get("mtr_sts"));
					accountsCS.setString(19, (String) object.get("remarks"));
					accountsCS.setString(20, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(21, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(21);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Transformer Meter Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("FROM_DTP")) {
							obj.put("status", "error");
							obj.put("message", " From Date Should Be Greater Than "+ accountsRS.getString("TO_DT"));
						}else{
							obj.put("status", "error");
							obj.put("message", " Transformer Meter Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}
					}
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid Input");
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
	public JSONObject getDTCMeterReadingDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_DTC_MTR_RDG_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("station_code"));
			accountsCS.setString(3, object.getString("feeder_code"));
			accountsCS.setString(4, object.getString("om_code"));
			accountsCS.setString(5, object.getString("reading_date"));
			accountsCS.registerOutParameter(6, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(6);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("stn_cd", accountsRS.getString("stn_cd"));
				ackobj.put("stn_name", accountsRS.getString("stn_name"));
				ackobj.put("fdr_cd", accountsRS.getString("fdr_cd"));
				ackobj.put("fdr_name", accountsRS.getString("fdr_name"));
				ackobj.put("om_cd", accountsRS.getString("om_cd"));
				ackobj.put("om_name", accountsRS.getString("om_name"));
				ackobj.put("dtc_cd", accountsRS.getString("dtc_cd"));
				ackobj.put("dtc_name", accountsRS.getString("dtc_name"));
				ackobj.put("rdg_day", accountsRS.getString("rdg_day"));
				ackobj.put("no_of_intl", accountsRS.getString("no_of_intl"));
				ackobj.put("prev_rdg_dt", accountsRS.getString("prev_rdg_dt"));
				ackobj.put("prev_rdg", accountsRS.getString("prev_rdg"));
				ackobj.put("pres_rdg", accountsRS.getString("pres_rdg"));
				ackobj.put("rollover", accountsRS.getString("rollover"));
				ackobj.put("rollover_descr", accountsRS.getString("rollover_descr"));
				
				array.add(ackobj);
			}
			if(array.isEmpty()) {
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "DTC Meter Reading Details Retrieved !!!");
				obj.put("dtc_meter_reading_details", array);
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
	public JSONObject getFeederEnergyAuditDetails(JSONObject object) {
		
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
			accountsCS=dbConnection.prepareCall(DBQueries.GET_FDR_ENRGY_AUDIT_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("station_code"));
			accountsCS.setString(3, object.getString("feeder_code"));
			accountsCS.setString(4, object.getString("month_year"));
			accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(5);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("stn_cd", accountsRS.getString("stn_cd"));
				ackobj.put("stn_name", accountsRS.getString("stn_name"));
				ackobj.put("fdr_cd", accountsRS.getString("fdr_cd"));
				ackobj.put("fdr_name", accountsRS.getString("fdr_name"));
				ackobj.put("billed_units", accountsRS.getString("billed_units"));
				ackobj.put("assessed_units", accountsRS.getString("assessed_units"));
				ackobj.put("ht_billed_unis", accountsRS.getString("ht_billed_unis"));
				ackobj.put("ht_demand", accountsRS.getString("ht_demand"));
				ackobj.put("ht_collection", accountsRS.getString("ht_collection"));
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
				obj.put("message", "Feeder Energy Audit Details Retrieved !!!");
				obj.put("feeder_energy_audit_details", array);
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
		}
		return obj;
	}
	
	@Override
	public JSONObject upsertFeederEnergyAuditDetails(JSONObject data) {
		
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
		    		AckObj = upsertIndividualFeederEnergyAuditDetails(objects, userid, conn_type, location_code, month);
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
	
	public JSONObject upsertIndividualFeederEnergyAuditDetails(JSONObject object, String userid, String conn_type, String location_code, String month) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String station = (String) object.get("station");
		String feeder = (String) object.get("feeder");
		String units = (String) object.get("units");
		String remarks = (String) object.get("remarks");
		
		try {
			if(!location_code.isEmpty() && !station.isEmpty() && !feeder.isEmpty() && !month.isEmpty() && !units.isEmpty()){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				accountsCS=dbConnection.prepareCall(DBQueries.UPSERT_FEEDER_UNITS);
				accountsCS.setString(1, location_code);
				accountsCS.setString(2, station);
				accountsCS.setString(3, feeder);
				accountsCS.setString(4, month);
				accountsCS.setString(5, units);
				accountsCS.setString(6, remarks);
				accountsCS.setString(7, userid);
				
				accountsCS.registerOutParameter(8, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(8);
				
				if(accountsRS.next()){
					String RESP = accountsRS.getString("RESP");
					System.out.println("RESP : "+RESP);
					
					if(RESP.equalsIgnoreCase("success")){
						obj.put("status", "success");
						obj.put("message", " Feeder Units Insert Successfully");
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
		}
		return obj;
	}

	@Override
	public JSONObject getentryrecordsforassessed(JSONObject object) {
		// TODO Auto-generated method stub
		CallableStatement accountsCS = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jsonResponse = new JSONObject();
		JSONArray jsonarray = new JSONArray();
		
		try {
			
			if(!object.isEmpty()){
				
				String conn_type = (String)object.get("conn_type");
				
				if(conn_type.equals("LT") || conn_type == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT") || conn_type == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				
				String Query =  " SELECT TAU_NO_INST,TAU_ASSD_UNITS, TAU_TRF_CATG, TAU_TC_No FROM TRSFMR_ASSED_UNITS  " +
						" WHERE TAU_TC_No='"+(String)object.get("transformer_code")+"'  AND TAU_BCN_SUBSTN_NO ='"+(String)object.get("station_code")+"' AND" +
						" TAU_BCN_FDR_NO='"+(String)object.get("feeder_code")+"' AND " +
					    " TAU_BCN_OM_UNIT='"+(String)object.get("om_code")+"'  AND " + 
						" TAU_RDG_DT=TO_DATE('"+(String)object.get("reading_date")+"','dd/mm/yyyy' )"+ 
					    " ORDER BY TAU_TRF_CATG ASC ";
						
				
				System.out.println(Query);
					ps = dbConnection.prepareStatement(Query) ; 
					rs = ps.executeQuery();
					
					while(rs.next()) {
						JSONObject json = new JSONObject();
						
						json.put("no_of_installtions", rs.getString("TAU_NO_INST"));
						json.put("assessed_units", rs.getString("TAU_ASSD_UNITS"));
						json.put("transformer_no", rs.getString("TAU_TC_No"));
						json.put("tariff_category", rs.getString("TAU_TRF_CATG"));
						
						jsonarray.add(json);
					}
					
					if(!jsonarray.isEmpty()) {
						jsonResponse.put("message", "Assessed Consumption Details Found !!!");
						jsonResponse.put("status", "success");
						jsonResponse.put("dtc_assessedentry_details", jsonarray);
					}else {
						jsonResponse.put("message", "Assessed Consumption Details Not Found !!!");
						jsonResponse.put("status", "error");
						jsonResponse.put("dtc_assessedentry_details", jsonarray);
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
		}
		
		return jsonResponse;
	}	

}
