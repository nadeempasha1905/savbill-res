/**
 * 
 */
package com.savbill.preloadpicklist;

import java.io.Serializable;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import com.savbill.database.DBManagerIMPL;
import com.savbill.database.DBManagerResourceRelease;
import com.savbill.util.DatabaseImpl;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Nadeem
 *
 */
public class PreloadPicklistImpl implements IPreLoadPickList{

	// database connection init
		DBManagerIMPL databaseObj = new DBManagerIMPL();
		Connection dbConnection;
		JSONObject json =  new JSONObject();
		JSONArray json1 =  new JSONArray();
		
	/* (non-Javadoc)
	 * @see com.cloudbill.preloadpicklist.IPreLoadPickList#getMeterReaderCodeList(net.sf.json.JSONObject)
	 */
	@Override
	public JSONObject getMeterReaderCodeList(JSONObject object) {
		// TODO Auto-generated method stub

		//Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){
				
				
					
					String location_code = (String) object.get("LOCATION_CODE").toString().trim();
					String connType = (String) object.get("CONN_TYPE").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							System.out.println("6");
							ps = dbConnection.prepareStatement(" SELECT DISTINCT MRM_MTR_RDR_CD FROM MTR_RDR_MASTR " +
									  " WHERE MRM_MTR_RDR_STS ='Y' AND " +
									  " MRM_OM_UNIT_CD LIKE '" + location_code + "%' ORDER BY 1");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("MR_CODE", rs.getString("MRM_MTR_RDR_CD"));
								
								meter.put("key", rs.getString("MRM_MTR_RDR_CD"));
								meter.put("value", rs.getString("MRM_MTR_RDR_CD"));
								
								MeterList.add(meter);
								
								
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("METER_READER_CODE", MeterList);
								}
							}
							
							
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Meter Reader Code List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;

	}

	@Override
	public JSONObject getMeterReadingDayList(JSONObject object) {
		// TODO Auto-generated method stub
		
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("LOCATION_CODE").toString().trim();
				String connType = (String) object.get("CONN_TYPE").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT DISTINCT MRM_MTR_RDG_DAY " +
									" FROM MTR_RDR_MASTR WHERE MRM_MTR_RDR_STS='Y' " +
									" AND MRM_OM_UNIT_CD like '"+ location_code + "%' ORDER BY 1 ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("MR_DAY", rs.getString("MRM_MTR_RDG_DAY"));
								meter.put("key", rs.getString("MRM_MTR_RDG_DAY"));
								meter.put("value", rs.getString("MRM_MTR_RDG_DAY"));
								
								MeterList.add(meter);
								
								
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("METER_READING_DAY", MeterList);
								}
							}
							
							
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Meter Reading Day List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;

		
	}

	@Override
	public JSONObject getCashCounterNoFromInitialReceiptPayment(
			JSONObject object) {
		// TODO Auto-generated method stub
		
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("LOCATION_CODE").toString().trim();
				String connType = (String) object.get("CONN_TYPE").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT DISTINCT(IRP_CASH_COUNTR_NO) BIR_CASH_COUNTR_NO  " +
									"  FROM INITIAL_RCPT_PYMNT  where IRP_PURPOSE_KEY like '"+ location_code + "%'   " +
									"  AND IRP_CASH_COUNTR_NO like '"+ location_code + "%'  ORDER BY IRP_CASH_COUNTR_NO ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("BIR_CASH_COUNTR_NO", rs.getString("BIR_CASH_COUNTR_NO"));
								
								MeterList.add(meter);
								
								
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("CASH_COUNTER_NO", MeterList);
								}
							}
							
							
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Cash Counter Number List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;

	
	}

	@Override
	public JSONObject getCampList(JSONObject object) {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("LOCATION_CODE").toString().trim();
				String connType = (String) object.get("CONN_TYPE").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT DISTINCT NVL(CD_CORRES_ADDR4, '')  CD_CORRES_ADDR4   " +
									"  FROM CUST_DESCR where CD_RR_NO like '"+ location_code + "%' ORDER BY 1 ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("CAMP_NAME", rs.getString("CD_CORRES_ADDR4"));
								
								MeterList.add(meter);
								
								
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("CAMP_LIST", MeterList);
								}
							}
							
							
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Camp List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
		
	}

	@Override
	public JSONObject getZoneList(JSONObject object) {
		// TODO Auto-generated method stub

		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("LOCATION_CODE").toString().trim();
				String connType = (String) object.get("CONN_TYPE").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT DISTINCT CLC_NAME,clc_cd FROM LOCATION  " +
									" WHERE CLC_TYPE = 'ZN' and clc_cd like substr('"+ location_code + "',1,2) ORDER BY CLC_NAME ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("ZONE_NAME", rs.getString("CLC_NAME"));
								meter.put("ZONE_CODE", rs.getString("clc_cd"));
								
								MeterList.add(meter);
								
								
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("ZONE_LIST", MeterList);
								}
							}
							
							
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Zone List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getDivisionList(JSONObject object) {
		// TODO Auto-generated method stub

		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("LOCATION_CODE").toString().trim();
				String connType = (String) object.get("CONN_TYPE").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT DISTINCT CLC_NAME,clc_cd FROM LOCATION " +
									"  WHERE CLC_TYPE = 'DV' and clc_cd like '"+ location_code + "%'  ORDER BY CLC_NAME ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("DIVISION_NAME", rs.getString("CLC_NAME"));
								meter.put("DIVISION_CODE", rs.getString("clc_cd"));
								
								MeterList.add(meter);
								
								
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("DIVISION_LIST", MeterList);
								}
							}
							
							
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Division List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;

	}

	@Override
	public JSONObject getCircleList(JSONObject object) {
		// TODO Auto-generated method stub

		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("LOCATION_CODE").toString().trim();
				String connType = (String) object.get("CONN_TYPE").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							
							ps = dbConnection.prepareStatement(" SELECT DISTINCT CLC_NAME,clc_cd FROM LOCATION   " +
									"  WHERE CLC_TYPE = 'CR' and clc_cd like '" + location_code + "%' ORDER BY CLC_NAME ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("CIRCLE_NAME", rs.getString("CLC_NAME"));
								meter.put("CIRCLE_CODE", rs.getString("clc_cd"));
								
								MeterList.add(meter);
								
								
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("CIRCLE_LIST", MeterList);
								}
							}
							
							
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Circle List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getTariffList(JSONObject object) {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("LOCATION_CODE").toString().trim();
				String connType = (String) object.get("CONN_TYPE").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT TM_SDESCR,TM_TRF_CODE FROM trf_master  " +
									"  WHERE ((SYSDATE BETWEEN TM_EFF_FROM_DT AND NVL(TM_EFF_TO_DT, SYSDATE))  " +
									"  AND NVL(TRF_STATUS, 'Y') = 'Y') AND TM_SDESCR LIKE  '%" + connType + "%'   ORDER BY 1 ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("TARIFF_DESCR", rs.getString("TM_SDESCR"));
								meter.put("key", rs.getString("TM_TRF_CODE"));
								meter.put("value", rs.getString("TM_SDESCR"));
								
								MeterList.add(meter);
								
								
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("TARIFF_DESCR_LIST", MeterList);
								}
							}
							
							
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Tariff Description List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	
	}

	@Override
	/*public JSONObject getOMUnitList(JSONObject object) {
		// TODO Auto-generated method stub


		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("Location_Code").toString().trim();
				String connType = (String) object.get("Conn_Type").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" select clc_cd from LOCATION where clc_cd like '" + location_code + "%'  ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("OM_CODE", rs.getString("clc_cd"));
								
								MeterList.add(meter);
								
								
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("OM_CODE_LIST", MeterList);
								}
							}
							
							
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving OM Code List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps, dbConnection);
			//DatabaseImpl.CleanUp(con, ps, rs);
		}
		
		return jsonResponse;
	}
*/
	public JSONObject getOMUnitList(JSONObject object) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("Location_Code").toString().trim();
				String connType = (String) object.get("Conn_Type").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" select clc_cd,clc_name from LOCATION  "
									+ " where substr(clc_cd,1,7) = '"+location_code+"' and clc_type = 'OM'  ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("key", rs.getString("clc_cd"));
								meter.put("value", rs.getString("clc_name"));
								MeterList.add(meter);
							}
							
							if(!MeterList.isEmpty()){
								
								jsonResponse.put("status", "success");
								jsonResponse.put("OM_LIST", MeterList);
							}
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving OM Code List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	
	
	@Override
	public JSONObject getLocationList(JSONObject object) {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("LOCATION_CODE").toString().trim();
				String connType = (String) object.get("CONN_TYPE").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT DISTINCT CLC_CD,CLC_NAME FROM LOCATION " +
									"  where CLC_CD like '" + location_code + "%'  and clc_type = 'SD' ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("LOCATION_CODES", rs.getString("CLC_CD"));
								meter.put("LOCATION_NAME", rs.getString("CLC_NAME"));
								
								MeterList.add(meter);
								
								
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("LOCATION_CODE_LIST", MeterList);
								}
							}
							
							
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Location Code List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getChargeDescriptionList(JSONObject object) {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("LOCATION_CODE").toString().trim();
				String connType = (String) object.get("CONN_TYPE").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT BCR_DESCR from PYMNT_ADJ_PRIORITY ORDER BY 1  ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("PYMNT_ADJ_PRIORITY_DESCR", rs.getString("BCR_DESCR"));
								
								MeterList.add(meter);
								
								
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("PYMNT_ADJ_PRIORITY_LIST", MeterList);
								}
							}
							
							
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Payment Adj Priority List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getPaymentPurposeList(JSONObject object) {
		// TODO Auto-generated method stub


		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("LOCATION_CODE").toString().trim();
				String connType = (String) object.get("CONN_TYPE").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" select CCD_DESCR from code_detl where CCD_CCM_CD_TYP='PYMNT_PURP' order by 1  ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("PAYMENT_PURPOSE_DESCR", rs.getString("CCD_DESCR"));
								
								MeterList.add(meter);
								
								
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("PAYMENT_PURPOSE_DESCR_LIST", MeterList);
								}
							}
							
							
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Payment Adj Priority List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
		
	}

	@Override
	public JSONObject getLocationCounterDetails(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("LOCATION_CODE").toString().trim();
				String connType = (String) object.get("CONN_TYPE").toString().trim();
				String counter_type = (String) object.get("COUNTER_TYPE").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)
								&& (counter_type.length() > 0 && counter_type != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT  LC_CNTR_NO FROM LOC_COUNTERS  " +
									"  WHERE LC_CNTR_TYPE = '"+ counter_type + "' AND LC_CNTR_NO LIKE '"+location_code+"%'    ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("HRT_COUNTER_NO", rs.getString("LC_CNTR_NO"));
								
								MeterList.add(meter);
								
								
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("HRT_COUNTER_NO_LIST", MeterList);
								}
							}
							
							
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving HRT Counter List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONArray getCustomerType(JSONObject object) {
		// TODO Auto-generated method stub APLNT_TYP
		
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("ConsumerType","APLNT_TYP" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getOMCode(JSONObject object) {
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("Location_Code").toString().trim();
				String connType = (String) object.get("Conn_Type").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" select clc_cd,clc_name from LOCATION where substr(clc_cd,1,7) = '"+location_code+"' and clc_type = 'OM'  ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("key", rs.getString("clc_cd"));
								meter.put("value", rs.getString("clc_name"));
								MeterList.add(meter);
								
								
							}
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving OM Code List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return MeterList;
	}

	@Override
	public JSONArray getInstallationType(JSONObject object) {
		// TODO Auto-generated method stub  INSTL_TYP
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("InstallationType","INSTL_TYP" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getCustomerStatus(JSONObject object) {
		// TODO Auto-generated method stub CONN_STS
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("CustomerStatus","CONN_STS" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}
	
	@Override
	public JSONArray getReadingCycle(JSONObject object) {
		// TODO Auto-generated method stub CONN_STS
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("ReadingCycle","CHRG_FR" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}
	
	@Override
	public JSONArray getBeginingMonth(JSONObject object) {
		// TODO Auto-generated method stub CONN_STS
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("BeginingMonth","BEG_MONTH" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}
	
	@Override
	public JSONArray getRequiredPhase(JSONObject object) {
		// TODO Auto-generated method stub CONN_STS
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("RequiredPhase","REQ_PHASE" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getPowerPurpose(JSONObject object) {
		// TODO Auto-generated method stub PWR_PURPOS
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("PowerPurpose","PWR_PURPOS" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getIndustryCode(JSONObject object) {
		// TODO Auto-generated method stub  IND_PWR_CD
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("InsdustryCode","IND_PWR_CD" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getLightingType(JSONObject object) {
		// TODO Auto-generated method stub LIGHT_TYP
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("LightingType","LIGHT_TYP" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getPowerSanctionedBy(JSONObject object) {
		// TODO Auto-generated method stub SANCT_AUTH
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("PowerSanctionedBy","SANCT_AUTH" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getWellType(JSONObject object) {
		// TODO Auto-generated method stub WELL TYP
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("WellType","WELL_TYP" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getStarterType(JSONObject object) {
		// TODO Auto-generated method stub START_TYP
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("StarterType","START_TYP" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getJuridiction(JSONObject object) {
		// TODO Auto-generated method stub JURD
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("Juridiction","JURD" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getConnectionType(JSONObject object) {
		// TODO Auto-generated method stub CONN_TYP
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("ConnectionType","CONN_TYP" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getRegions(JSONObject object) {
		// TODO Auto-generated method stub REG_TYP
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("Regions","REG_TYP" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getTaluks(JSONObject object) {
		// TODO Auto-generated method stub TALUK
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("Taluk","TALUK" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getDistricts(JSONObject object) {
		// TODO Auto-generated method stub DISTRICT
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("District","DISTRICT" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getStateConstncy(JSONObject object) {
		// TODO Auto-generated method stub S_CONSTCY
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("StateConstncy","S_CONSTCY" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getCentralConsctncy(JSONObject object) {
		// TODO Auto-generated method stub C_CONSTCY
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("CentralConstncy","C_CONSTCY" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getDocumentType(JSONObject object) {
		// TODO Auto-generated method stub DOC_TYP
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("DocumentType","DOC_TYP" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getPurposeDetail(JSONObject object) {
		// TODO Auto-generated method stub PYMNT_PURP
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("PurposeDetail","PYMNT_PURP" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray getInimationType(JSONObject object) {
		// TODO Auto-generated method stub CONSMR_INT
		if(!object.isEmpty()){
			String ConnType = (String)object.get("Conn_Type");
			json1 = GenericPreLoadCodeDetail("ConsumerIntimation","CONSMR_INT" ,ConnType );
		}else{
			json.put("status", "error");
			json.put("message", "Sorry ! Cannot Load The Details.");
		}
		return json1;
	}

	@Override
	public JSONArray GenericPreLoadCodeDetail(String DetailType, String CodeType, String ConnType) {
		
		// TODO Auto-generated method stub

		JSONObject JsonResponse =  new JSONObject();
		JSONArray  jsonList    =  new JSONArray();
		JSONObject jsonCode    =  new JSONObject();
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String Query = "";
		
		try {
			
			if(ConnType.equals("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
				
			Query = " SELECT  CCD_CD_VAL,CCD_DESCR FROM CODE_DETL WHERE CCD_CCM_CD_TYP='"+CodeType+"' ORDER BY CCD_DESCR ";
			//System.out.println("Query = "+Query);
			
			ps = dbConnection.prepareStatement(Query);
			rs = ps.executeQuery();
			
			while(rs.next()){
				
				jsonCode.put("key", rs.getString("CCD_CD_VAL"));
				jsonCode.put("value", rs.getString("CCD_DESCR"));
				
				jsonList.add(jsonCode);
			}
			
			if(!jsonList.isEmpty()){
				JsonResponse.put("status", "success");
				//JsonResponse.put(DetailType, jsonList);
			}else{
				JsonResponse.put("status", "error");
				JsonResponse.put("message", "No Records Found For "+DetailType);
			}
			
		}catch (Exception e) {
			// TODO: handle exception
			JsonResponse.put("status", "fail");
			JsonResponse.put("message", "Database error occured for "+DetailType);
		}finally {
			DBManagerResourceRelease.close(rs, ps);
		}
		
		
		return jsonList;
		
	}

	@Override
	public JSONObject getCodeDescrByCodeValue(JSONObject object) {
		// TODO Auto-generated method stub

		PreparedStatement ps = null;
		ResultSet rs =  null;
		
		try {
			
			if(!object.isEmpty()){
				
				String Conn_Type = (String)object.get("conn_type");
				String Code_Type = (String)object.get("code_type");
				String Code_val  = (String)object.get("code_val");
				
				if(Conn_Type.equals("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(Conn_Type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				
				ps = dbConnection.prepareStatement("SELECT  CCD_DESCR FROM CODE_DETL  "
						+ "  WHERE CCD_CCM_CD_TYP='"+Code_Type+"'  AND  CCD_CD_VAL = '"+Code_val+"' ");
				
				rs = ps.executeQuery();
				
				if(rs.next()){
					
					json.put("status", "success");
					json.put("CODE_DESCRIPTION", rs.getString("CCD_DESCR"));
				}else{
					json.put("status", "error");
					json.put("status", "Code Description Not Found.");
				}
				
			}else{
				json.put("status", "error");
				json.put("message", "Sorry ! Invalid Inputs.");
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			json.put("status", "fail");
			json.put("message", "Database Error .");
		}finally {
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return json;
	}

	@Override
	public JSONObject getCodeValueByCodeDescription(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs =  null;
		
		try {
			
			if(!object.isEmpty()){
				
				String Conn_Type = (String)object.get("conn_type");
				String Code_Type = (String)object.get("code_type");
				String Code_Descr  = (String)object.get("code_descr");
				
				if(Conn_Type.equals("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(Conn_Type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				
				ps = dbConnection.prepareStatement("SELECT  CCD_CD_VAL  FROM CODE_DETL  "
						+ "  WHERE CCD_CCM_CD_TYP='"+Code_Type+"'  AND  CCD_DESCR = '"+Code_Descr+"' ");
				
				rs = ps.executeQuery();
				
				if(rs.next()){
					
					json.put("status", "success");
					json.put("CODE_VALUE", rs.getString("CCD_CD_VAL"));
				}else{
					json.put("status", "error");
					json.put("status", "Code Value Not Found.");
				}
				
			}else{
				json.put("status", "error");
				json.put("message", "Sorry ! Invalid Inputs.");
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			json.put("status", "fail");
			json.put("message", "Database Error .");
		}finally {
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return json;
	}

	@Override
	public JSONObject getConsumerMasterPickListValues(final JSONObject object) {
		// TODO Auto-generated method stub
		
		JSONObject jsonResponse = new JSONObject();
		JSONObject jsonPickList = new JSONObject();
		
		jsonPickList.put("ConsumerType",getCustomerType(object));
		jsonPickList.put("OM_CODE_LIST",getOMCode(object));
		jsonPickList.put("InstallationType",getInstallationType(object));
		jsonPickList.put("CustomerStatus",getCustomerStatus(object));
		jsonPickList.put("PowerPurpose",getPowerPurpose(object));
		jsonPickList.put("InsdustryCode",getIndustryCode(object));
		jsonPickList.put("LightingType",getLightingType(object));
		jsonPickList.put("PowerSanctionedBy",getPowerSanctionedBy(object));
		jsonPickList.put("WellType",getWellType(object));
		jsonPickList.put("StarterType",getStarterType(object));
		jsonPickList.put("Juridiction",getJuridiction(object));
		jsonPickList.put("ConnectionType",getConnectionType(object));
		jsonPickList.put("Regions",getRegions(object));
		jsonPickList.put("Taluk",getTaluks(object));
		jsonPickList.put("District",getDistricts(object));
		jsonPickList.put("StateConstncy",getStateConstncy(object));
		jsonPickList.put("CentralConstncy",getCentralConsctncy(object));
		jsonPickList.put("DocumentType",getDocumentType(object));
		jsonPickList.put("PurposeDetail",getPurposeDetail(object));
		jsonPickList.put("ConsumerIntimation",getInimationType(object));
		/*jsonPickList.put("MeterReaderCode",getCustomerMeterReaderCodeList(object));
		jsonPickList.put("MeterReadingDay",getCustomerMeterReadingDayList(object));*/
		jsonPickList.put("CashCounter",getCustomerCashCounterNoFromInitialReceiptPayment(object));
		jsonPickList.put("TariffCode",getCustomerTariffList(object));
		jsonPickList.put("StationCodeList",getStationList(object));
		jsonPickList.put("YesNo",getYesNoList());
		jsonPickList.put("ReadingCycle",getReadingCycle(object)); 
		jsonPickList.put("BeginingMonth",getBeginingMonth(object));
		jsonPickList.put("RequiredPhase",getRequiredPhase(object));
		
		
		if(!jsonPickList.isEmpty()){
			jsonResponse.put("status", "success");
			jsonResponse.put("CONSUMER_MASTER_PICKLIST", jsonPickList);
		}else{
			jsonResponse.put("status", "error");
			jsonResponse.put("message", "Database Error.");
		}
		
		
		
	/*	
		jsonArray.add(getCustomerType(object));
		//jsonArray.add(getOMCode(object));
		jsonArray.add(getOMUnitList(object));
		jsonArray.add(getInstallationType(object));
		jsonArray.add(getCustomerStatus(object));
		jsonArray.add(getPowerPurpose(object));
		jsonArray.add(getIndustryCode(object));
		jsonArray.add(getLightingType(object));
		jsonArray.add(getPowerSanctionedBy(object));
		jsonArray.add(getWellType(object));
		jsonArray.add(getStarterType(object));
		jsonArray.add(getJuridiction(object));
		jsonArray.add(getConnectionType(object));
		jsonArray.add(getRegions(object));
		jsonArray.add(getTaluks(object));
		jsonArray.add(getDistricts(object));
		jsonArray.add(getStateConstncy(object));
		jsonArray.add(getCentralConsctncy(object));
		jsonArray.add(getDocumentType(object));
		jsonArray.add(getPurposeDetail(object));
		jsonArray.add(getInimationType(object));
		
		jsonResponse.put("CONSUMER_MASTER_PICKLIST", jsonArray);*/
		
		return jsonResponse;
	}

	@Override
	public JSONObject getCustomerMeterReaderCodeList(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jsonResponse = new JSONObject();
		JSONArray MeterCodeList = new JSONArray();
		
		try {
			
			if(!object.isEmpty()){
				
				String LOCATION  = (String)object.get("location_code");
				String CONN_TYPE = (String)object.get("conn_type");
				
				if(CONN_TYPE.equals("LT") || CONN_TYPE == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(CONN_TYPE.equals("HT") || CONN_TYPE == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				/*jsonResponse.put("status", "success");
				jsonResponse.put("METERCODE", ReferenceUtil.getMeterCodeList(om_code, CONN_TYPE));*/
				
				if(dbConnection != null){
					ps = dbConnection.prepareStatement(" SELECT DISTINCT MRM_MTR_RDR_CD FROM MTR_RDR_MASTR " +
							  " WHERE MRM_MTR_RDR_STS ='Y' AND " +
							  " MRM_OM_UNIT_CD LIKE '" + LOCATION + "%' ORDER BY 1  ") ; 
					rs = ps.executeQuery();
					while (rs.next()) {
						JSONObject meterCode = new JSONObject();
						
						meterCode.put("key", rs.getString("MRM_MTR_RDR_CD"));
						meterCode.put("value", rs.getString("MRM_MTR_RDR_CD"));
						MeterCodeList.add(meterCode);
					}
				}
				
				jsonResponse.put("status", "success");
				jsonResponse.put("MeterReaderCode",MeterCodeList);
			
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Invalid Inputs / No Meter Reader Detials");
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
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getCustomerMeterReadingDayList(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> mtr_day = null;
		JSONObject jsonResponse = new JSONObject();
		JSONArray MeterDayList = new JSONArray();
		
		try {
			
			if(!object.isEmpty()){
				
				String LOCATION  = (String)object.get("location_code");
				String CONN_TYPE = (String)object.get("conn_type");
				String mr_code   = (String)object.get("mr_code");
				
				if(CONN_TYPE.equals("LT") || CONN_TYPE == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(CONN_TYPE.equals("HT") || CONN_TYPE == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					String qyr = " select mrm_mtr_rdg_day from mtr_rdr_mastr "
							   + " where mrm_mtr_rdr_cd = '"+mr_code+"' AND MRM_OM_UNIT_CD LIKE '"+LOCATION+"%' order by 1  ";

				ps = dbConnection.prepareStatement(qyr);
				rs = ps.executeQuery();
				
				/*while(rs.next()){
					
					mtr_day = new HashMap<String, String>();
					
					mtr_day.put("MTR_DAY", rs.getString("mrm_mtr_rdg_day"));
					
					jsonArray.add(mtr_day);
				}*/
				while (rs.next()) {
					JSONObject meterCode = new JSONObject();
					
					meterCode.put("key", rs.getString("mrm_mtr_rdg_day"));
					meterCode.put("value", rs.getString("mrm_mtr_rdg_day"));
					MeterDayList.add(meterCode);
				}
				
				jsonResponse.put("status", "success");
				jsonResponse.put("MeterReadingDay", MeterDayList);
			
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Invalid Inputs / No Meter Reading Day Detials");
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
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONArray getCustomerCashCounterNoFromInitialReceiptPayment(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray CounterList = new JSONArray();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("Location_Code").toString().trim();
				String connType = (String) object.get("Conn_Type").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT LC_CNTR_NO VALUE,LC_CNTR_DESC DESCR  "
									+ "  FROM LOC_COUNTERS  WHERE SUBSTR(LC_CNTR_NO,1,7)='"+location_code+"'   ") ; 
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject counter = new JSONObject();
								
								counter.put("key", rs.getString("VALUE"));
								counter.put("value", rs.getString("DESCR"));
								CounterList.add(counter);
							}
						}
					}
			}
		} catch (Exception e) {
			System.out.println("Error In Retrieving Counter List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		return CounterList;
	}

	@Override
	public JSONArray getCustomerTariffList(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray TariffList = new JSONArray();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("Location_Code").toString().trim();
				String connType = (String) object.get("Conn_Type").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT TM_TRF_CODE,TM_SDESCR FROM trf_master "
									+ "   WHERE ((SYSDATE BETWEEN TM_EFF_FROM_DT AND NVL(TM_EFF_TO_DT, SYSDATE))  "
									+ "   AND NVL(TRF_STATUS, 'Y') = 'Y') AND TM_SDESCR LIKE  '%LT%'  ORDER BY REPLACE(TM_SDESCR,'DB','Z')   ") ; 
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject tariff = new JSONObject();
								
								tariff.put("key", rs.getString("TM_TRF_CODE"));
								tariff.put("value", rs.getString("TM_SDESCR"));
								TariffList.add(tariff);
							}
						}
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error In Retrieving Tariff List.");
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		return TariffList;
	}

	@Override
	public JSONArray getStationList(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray StationList = new JSONArray();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String location_code = (String) object.get("Location_Code").toString().trim();
				String connType = (String) object.get("Conn_Type").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						if(dbConnection != null){
							String qyr = " SELECT  SM_STN_CD,SM_STN_NAME FROM STATION_MASTER WHERE SM_LOC_CD LIKE '"+location_code+"%' ORDER BY 1  ";
							
							ps = dbConnection.prepareStatement(qyr);
							rs = ps.executeQuery();

							while (rs.next()) {
								JSONObject counter = new JSONObject();
								
								counter.put("key", rs.getString("SM_STN_CD"));
								counter.put("value",rs.getString("SM_STN_NAME")+" ("+rs.getString("SM_STN_CD")+")");
								StationList.add(counter);
							}
						}
					}
			}
		} catch (Exception e) {
			System.out.println("Error In Retrieving Station List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		return StationList;
	}

	@Override
	public JSONArray getYesNoList() {
		// TODO Auto-generated method stub
		JSONArray yesNoList = new JSONArray();
		JSONObject json_Obj = new JSONObject();
		
		json_Obj.put("key", "Y");
		json_Obj.put("value", "Yes");
		
		yesNoList.add(json_Obj);
		
		json_Obj.put("key", "N");
		json_Obj.put("value", "No");
		
		yesNoList.add(json_Obj);
		
		return yesNoList ; 
	}

	@Override
	public JSONObject getrebatetypelist(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray RebateList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String connType = (String) object.get("conn_type").toString().trim();
					
					if((connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT RD_REBATE_CODE,RD_REBATE_DESC FROM REBATE_DETL  ") ; 
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject rebate = new JSONObject();
								
								rebate.put("key", rs.getString("RD_REBATE_CODE"));
								rebate.put("value", rs.getString("RD_REBATE_DESC"));
								RebateList.add(rebate);
							}
						}
					}
					
					if(!RebateList.isEmpty()){
						jsonResponse.put("status", "success");
						jsonResponse.put("RebateList",RebateList);
					}else{
						jsonResponse.put("status", "error");
						jsonResponse.put("message","Rebate Type Not Loaded.");
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error In Retrieving Rebate Type List.");
			jsonResponse.put("status", "fail");
			jsonResponse.put("message","Database Error.Rebate Not Loaded.");
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		return jsonResponse;
	}
	
	
	@Override
	public JSONObject getApealType(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray ApealTypeList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){

				String connType = (String) object.get("conn_type").toString().trim();
					
					if((connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT DISTINCT CCD_CD_VAL, CCD_DESCR "
															 + " FROM CLOUDBILL_APPEAL_AMT,CODE_DETL"
															 + " WHERE CCD_CCM_CD_TYP='APEAL_TYPE' AND CCD_CD_VAL=CAA_APPL_CD") ; 
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject apealtype = new JSONObject();
								
								apealtype.put("key", rs.getString("CCD_CD_VAL"));
								apealtype.put("value", rs.getString("CCD_DESCR"));
								ApealTypeList.add(apealtype);
							}
						}
					}
					
					if(!ApealTypeList.isEmpty()){
						jsonResponse.put("status", "success");
						jsonResponse.put("ApealTypeList",ApealTypeList);
					}else{
						jsonResponse.put("status", "error");
						jsonResponse.put("message","Apeal Type Not Loaded.");
					}
			}
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error In Retrieving Apeal Type List.");
			jsonResponse.put("status", "fail");
			jsonResponse.put("message","Database Error.Apeal Typ Loaded.");
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		return jsonResponse;
	}

	@Override
	public JSONObject getTransformerCodeList(JSONObject object) {
		// TODO Auto-generated method stub

		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){
					
					String location_code = (String) object.get("LOCATION_CODE").toString().trim();
					String connType = (String) object.get("CONN_TYPE").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							System.out.println("6");
							ps = dbConnection.prepareStatement(" SELECT TM_TRSFMR_CENTER_NO, TM_TRSFMR_CENTER_NAME FROM TRSFMR_MASTR "
									+ " WHERE TM_LOC_CD LIKE '"+location_code+"%'"
									+ " ORDER BY TM_NFM_NST_STN_CD, TM_NFM_FDR_NO, TM_TRSFMR_CENTER_NO ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("key", rs.getString("TM_TRSFMR_CENTER_NO"));
								meter.put("value", rs.getString("TM_TRSFMR_CENTER_NAME"));
								
								MeterList.add(meter);
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("TRANSFORMER_LIST", MeterList);
								}
							}
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Transformer List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getFeederList(JSONObject object) {
		// TODO Auto-generated method stub

		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){
					
					String location_code = (String) object.get("LOCATION_CODE").toString().trim();
					String connType = (String) object.get("CONN_TYPE").toString().trim();
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							System.out.println("6");
							ps = dbConnection.prepareStatement(" SELECT NFM_FDR_NO, NFM_FDR_DESCR FROM FEEDER_MASTER "
									+ " WHERE NFM_NST_LOC_CD LIKE '"+location_code+"%'"
									+ " ORDER BY NFM_NST_STN_CD, NFM_FDR_NO ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("key", rs.getString("NFM_FDR_NO"));
								meter.put("value", rs.getString("NFM_FDR_DESCR"));
								
								MeterList.add(meter);
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("FEEDER_LIST", MeterList);
								}
							}
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Feeder List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getUserIdList(JSONObject object) {
		// TODO Auto-generated method stub

		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){
					
					String location_code = (String) object.get("LOCATION_CODE").toString().trim();
					String connType = (String) object.get("CONN_TYPE").toString().trim();
					String userid = (String) object.get("userid").toString().trim();
					
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT USR_ID, USR_NAME, USR_DESCR FROM CLOUDBILL_USER "
									+ " WHERE USR_LOC LIKE '"+location_code+"%' AND USR_ID <> '"+userid+"' "
									+ " ORDER BY USR_LOC,USR_DESCR ,USR_ID  ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("key", rs.getString("USR_ID"));
								meter.put("value", rs.getString("USR_DESCR") + " - "+ rs.getString("USR_NAME"));
								
								MeterList.add(meter);
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("USERID_LIST", MeterList);
								}
							}
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving user_id List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getUserDetailsByUserId(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){
					
					String location_code = (String) object.get("LOCATION_CODE").toString().trim();
					String connType = (String) object.get("CONN_TYPE").toString().trim();
					String key = (String) object.get("key").toString().trim();
					
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT  USR_NAME, USR_DESCR FROM CLOUDBILL_USER "
									+ " WHERE USR_LOC LIKE '"+location_code+"%' AND USR_ID = '"+key+"' ");
							rs = ps.executeQuery();
							
							if (rs.next()) {
								
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "User Details Retrieved");
								jsonResponse.put("username", rs.getString("USR_NAME"));
								jsonResponse.put("userrole", rs.getString("USR_DESCR"));
							}else {
								
								jsonResponse.put("status", "fail");
								jsonResponse.put("message", "User Details Not Found !!!");
							}

						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving user details.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getUserRolesList(JSONObject object) {
		// TODO Auto-generated method stub

		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONArray MeterList = new JSONArray();
		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){
					
					String location_code = (String) object.get("LOCATION_CODE").toString().trim();
					String connType = (String) object.get("CONN_TYPE").toString().trim();
					
					
					if((location_code.length() > 0 && location_code != null) 
							&& (connType.length() > 0 && connType != null)){
						if(connType.equals("LT")){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							ps = dbConnection.prepareStatement(" SELECT CRM_ROLE_CD, CRM_ROLE_NAME FROM CLOUDBILL_ROLE_MASTER "
									+ " WHERE CRM_ROLE_STS = 'Y' "
									+ " ORDER BY CRM_ROLE_CD, CRM_ROLE_NAME  ");
							rs = ps.executeQuery();
							while (rs.next()) {
								JSONObject meter = new JSONObject();
								
								meter.put("key", rs.getString("CRM_ROLE_CD"));
								meter.put("value", rs.getString("CRM_ROLE_NAME"));
								
								MeterList.add(meter);
							}
							
							if(MeterList.isEmpty()){
								jsonResponse.put("status", "success");
								jsonResponse.put("message", "No Records Found For The Query.");
							}else{
								if(!MeterList.isEmpty()){
									jsonResponse.put("status", "success");
									jsonResponse.put("ROLE_LIST", MeterList);
								}
							}
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
					}else{
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Role List.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}
}
