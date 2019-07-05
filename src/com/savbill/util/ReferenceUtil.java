package com.savbill.util;

import java.sql.BatchUpdateException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.savbill.database.DBManagerIMPL;
import com.savbill.database.DBManagerResourceRelease;


public class ReferenceUtil {
	
	static DBManagerIMPL databaseObj = new DBManagerIMPL();
	static Connection dbConnection;
	
	public static boolean validateRRStatus(String rr_No,String location,String ConnType) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean status = false;
		try {
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

				String query = " SELECT CB_BILL_NO, CB_BILL_DT, NVL(CB_RR_STS,'N') FROM CUST_BILL WHERE CB_RR_NO=? " +
						" AND CB_RR_NO LIKE '"+location+"%' ";
			
			ps = dbConnection.prepareStatement(query);
			ps.setString(1, rr_No);
			rs = ps.executeQuery();
			if (rs.next()) {
				if (!(rs.getString(3).equals("N"))) {
					status = true;
				}

			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			//DatabaseImpl.CleanUp(con, ps, rs);
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}
		return status;
	}
	
	public static boolean validateRRStatus(String rrNumber,Connection Con) {

		PreparedStatement ps = null;
		ResultSet rs = null;

		try {

				String query = " SELECT CB_BILL_NO, CB_BILL_DT, NVL(CB_RR_STS,'N') CB_RR_STS FROM CUST_BILL WHERE CB_RR_NO = '"+rrNumber+"' ";
			
			ps = Con.prepareStatement(query);
			rs = ps.executeQuery();
			if (rs.next()) {
				if (!(rs.getString("CB_RR_STS").equals("N"))) {
					return false;
				}else{
					return true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		} finally {
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}
		return false;
	}

	public static String reverseConvert(String dob) {
		// TODO Auto-generated method stub
		String s=dob;
		if(dob!=null){
		String s1=s.substring(0, 4);
		String s2=s.substring(5, 7);
		String s3=s.substring(8,10);

		 
		 
	  return s3+"/"+s2+"/"+s1;
		}else{
			return null;	
		}
		
	}

	public static String getRenewDate(String serviDate, String tempDuration) {
		// TODO Auto-generated method stub
		String d = "", m = "", y = "", finalDate = "";

		try {
			d = serviDate.substring(0, 2);
			m = serviDate.substring(3, 5);
			y = serviDate.substring(6);

			Calendar c = Calendar.getInstance();
			c.set(Integer.parseInt(y), Integer.parseInt(m) - 1,
					Integer.parseInt(d) - 1);
			c.add(Calendar.DATE, Integer.parseInt(tempDuration));

			int dd = c.get(Calendar.DATE);
			int mm = (c.get(Calendar.MONTH) + 1);
			int yy = c.get(Calendar.YEAR);

			if (mm <= 9) {
				m = "0" + mm;
			} else {
				m = String.valueOf(mm);
			}

			if (dd <= 9) {
				d = "0" + dd;
			} else {
				d = String.valueOf(dd);
			}

			finalDate = d + "/" + m + "/" + yy;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalDate;
	}

	public static String getCodeDesc(String chrgCd,String codetype,
			String ConnType) {
		// TODO Auto-generated method stub
		PreparedStatement ps	=	null;
		ResultSet rs			=	null;
		String codeDescription	=	null;
		
		try
		{
			
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			String SELECT_CHRG_DESC= "SELECT  CCD_DESCR FROM CODE_DETL WHERE CCD_CCM_CD_TYP='"+codetype+"' AND CCD_CD_VAL=? ";
			ps=dbConnection.prepareStatement(SELECT_CHRG_DESC);
			ps.setString(1, chrgCd);
			rs = ps.executeQuery();
			if(rs.next())
			{
				codeDescription=rs.getString(1);
		 	}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
		}
		finally 
		{
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}
	 	return codeDescription;
	}
	
	public static String getPowerPurposeCodeTOName(String code,String ConnType) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			ps = dbConnection.prepareStatement(" SELECT CCD_DESCR  FROM CODE_DETL WHERE CCD_CCM_CD_TYP='PWR_PURPOS' "
					+ " AND CCD_CD_VAL='" + code + "' ORDER BY 1	 ");
			rs = ps.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);

			}

		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}
		return s;
	}
	
	public static String getIndustrialCodeTOName(String code,String ConnType) {

		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			
			pr = dbConnection
					.prepareStatement(" SELECT CCD_DESCR FROM CODE_DETL WHERE CCD_CCM_CD_TYP = 'IND_PWR_CD' "
					+ " AND CCD_CD_VAL='" + code + "'  ORDER BY ccd_cd_val");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}

		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		return s;
	}
	
	public static String getPwrSanctionedCodeTOName(String code,String ConnType) {
		
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			pr = dbConnection.prepareStatement(" SELECT ccd_descr FROM CODE_DETL WHERE CCD_CCM_CD_TYP = 'SANCT_AUTH' "
					+ " AND ccd_cd_val='" + code + "'  ORDER BY ccd_cd_val	 ");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}

		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);

		}
		return s;
	}
	
	
	
	public static  String getTariffDescByTariffCode(String tariffCode,String ConnType)
	{
		PreparedStatement ps	=	null;
		ResultSet rs			=	null;
		String codeDescription	=	null;
		try
		{
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			String SELECT_CHRG_DESC= "SELECT * FROM TRF_MASTER WHERE TM_TRF_CODE='"+tariffCode+"'" ;
			ps=dbConnection.prepareStatement(SELECT_CHRG_DESC);
			
			 
			rs = ps.executeQuery();
			if(rs.next())
			{
				codeDescription=rs.getString("TM_SDESCR");
				 
				 
			}
		}
		catch(Exception e) {
			System.out.println(e.getMessage());
			//log.info("Cannot Retreive withrawal Approval"+e.getMessage());
		}
		finally 
		{
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}
	 	return codeDescription;
	}
	
	
	public static String getStationName(String code,String ConnType) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = "";
		try {

			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			
			/*ps = con.prepareStatement("select NST_STN_NAME from station_master where NST_STN_CD='"*/
			ps = dbConnection.prepareStatement("select SM_STN_NAME NST_STN_NAME from station_master where SM_STN_CD='"
					
					+ code + "' ");
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getString(1);
			}
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}

		return result;
	}
	
	public static String getFeederName(String feedercode, String stcode,String ConnType) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = "";
		try {

			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			
			ps = dbConnection.prepareStatement("SELECT  NFM_FDR_DESCR FROM FEEDER_MASTER WHERE NFM_FDR_NO='"
					+ feedercode + "' AND NFM_NST_STN_CD='" + stcode + "' ");
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getString(1);
			}
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
			}
		return result;
	}
	
	public static String getOMUnitCodeName(String code,String ConnType) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String name = "";
		try {
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			String qyery = " SELECT CLC_NAME FROM LOCATION WHERE CLC_TYPE='OM' AND CLC_CD = '"
					+ code + "' ";
			ps = dbConnection.prepareStatement(qyery);
			rs = ps.executeQuery();
			while (rs.next()) {
				name = rs.getString(1);
			}
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}

		return name;
	}
	
	
	public static String getTransformerName(String tccode, String federcode,
			String stcode,String ConnType) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = "";
		try {

			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			ps = dbConnection.prepareStatement(" SELECT  TM_TRSFMR_CENTER_NAME NTR_TRSFMR_CENTER_NAME FROM TRSFMR_MASTR WHERE "
					+ " TM_TRSFMR_CENTER_NO='"
					+ tccode
					+ "' AND TM_NFM_FDR_NO='"
					+ federcode
					+ "' "
					+ " AND TM_NFM_NST_STN_CD='" + stcode + "' ");
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getString(1);
			}
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}

		return result;
	}
	

	public static String getStarterTypeCodeTOName(String code,String ConnType) {

		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			pr = dbConnection.prepareStatement(" SELECT CCD_DESCR FROM CODE_DETL WHERE CCD_CCM_CD_TYP = 'START_TYP' "
					+ " AND CCD_CD_VAL='" + code + "'  ORDER BY ccd_cd_val	 ");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}

		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		return s;
	}
	
	
	public static String getJurisdictionTypeCodeTOName(String code,String ConnType) {

		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			pr = dbConnection.prepareStatement(" SELECT CCD_DESCR FROM CODE_DETL WHERE CCD_CCM_CD_TYP='JURD' AND CCD_CD_VAL= '"
					+ code + "' ");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}

		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		return s;
	}
	
	
	public static String getWellTypeCodeTOName(String code,String ConnType) {

		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			pr = dbConnection.prepareStatement(" SELECT CCD_DESCR FROM CODE_DETL WHERE CCD_CCM_CD_TYP = 'WELL TYP' AND "
					+ " CCD_CD_VAL='" + code + "'  ORDER BY ccd_cd_val	 ");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}

		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		return s;
	}
	
	public static String getLightingCodeTOName(String code,String ConnType) {

		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {


			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			
			pr = dbConnection.prepareStatement("SELECT CCD_DESCR FROM CODE_DETL WHERE CCD_CCM_CD_TYP = 'LIGHT_TYP' "
					+ "AND CCD_CD_VAL='" + code + "' ORDER BY ccd_cd_val ");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}

		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		return s;
	}
	
	
	public static String getConnectionTypeCodeTOName(String code,String ConnType) {

		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			pr = dbConnection.prepareStatement(" SELECT CCD_DESCR FROM CODE_DETL WHERE CCD_CCM_CD_TYP ='CONN_TYP' AND CCD_CD_VAL='"
					+ code + "'");
			rs = pr.executeQuery();
			while (rs.next()) {
				s = rs.getString(1);
			}

		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		return s;
	}
	
	
	
	public static  String getTimestampe(String rrno,String ConnType) {

		PreparedStatement pr = null;
		ResultSet rs = null;
		String timeStamp=null;
		try{
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			
			pr = dbConnection.prepareStatement("select TO_CHAR(CM_TMPSTP ,'DD/MM/YYYY HH:MI:SS AM') from CUST_MASTER where CM_RR_NO=?");
				pr.setString(1, rrno);
				rs = pr.executeQuery();
				if(rs.next()) {
					timeStamp=rs.getString(1);
				}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);

		}
		return timeStamp;
	}
	public static String getRenewDateExtends() {
		String d = "", m = "", finalDate = "";

		try {

			Calendar c = Calendar.getInstance();
			int dd1 = c.get(Calendar.DATE);
			int mm1 = (c.get(Calendar.MONTH) + 1);
			int yy1 = c.get(Calendar.YEAR);

			c.set(yy1, (mm1 - 1), dd1);
			c.add(Calendar.DATE, 7);

			int dd = c.get(Calendar.DATE);
			int mm = (c.get(Calendar.MONTH) + 1);
			int yy = c.get(Calendar.YEAR);

			if (mm <= 9) {
				m = "0" + mm;
			} else {
				m = String.valueOf(mm);
			}

			if (dd <= 9) {
				d = "0" + dd;
			} else {
				d = String.valueOf(dd);
			}

			finalDate = d + "/" + m + "/" + yy;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return finalDate;
	}
	
	
	public static String GfnReturnCode(String pymtpurpose,String ConnType) {
		PreparedStatement pr = null;
		ResultSet rs = null;
		String val=null;
		try {
		
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			
			pr = dbConnection.prepareStatement("SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_DESCR = '"+pymtpurpose+"'  AND CCD_CCM_CD_TYP = 'PYMNT_PURP'  ");
			System.out.println("SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_DESCR = '"+pymtpurpose+"'  AND CCD_CCM_CD_TYP = 'PYMNT_PURP'");
			rs = pr.executeQuery();
			if (rs.next()) {
				val= (rs.getString(1));
			}
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		return val ;
	}

	public static String getIvrsID(String rrno,String ConnType) {

		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = "";
		try {
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			ps = dbConnection.prepareStatement(" SELECT IVRS_ID FROM IVRS_CUST_MASTR WHERE IVRS_BCN_RR_NO =? ");
			ps.setString(1, rrno);
			rs = ps.executeQuery();
			if (rs.next()) {
				result = rs.getString(1);
			}
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}

		return result;
	}

	public static List<Map<String,String>> getMeterCodeList(String code, String ConnType) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = "";
		Map<String, String> MeterMap = null;
		List<Map<String,String>> list = new ArrayList<Map<String,String>>();
		
		try {
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			
			String qyr = "  "; 
			
			
			if (code != null && code.length() > 0) {
				qyr = " SELECT DISTINCT MRM_MTR_RDR_CD FROM MTR_RDR_MASTR "
						+ " WHERE MRM_MTR_RDR_STS='Y' AND MRM_OM_UNIT_CD = '"
						+ code + "'   ORDER BY 1	 ";
			} else {
				qyr = "  select mrm_mtr_rdr_cd from mtr_rdr_mastr   "
					   + "  where  MRM_OM_UNIT_CD = '"+code+"' ";
			}
			
			

			ps = dbConnection.prepareStatement(qyr);
			rs = ps.executeQuery();

			while (rs.next()) {
				MeterMap = new HashMap<String, String>();
				
				MeterMap.put("meter_code", rs.getString("mrm_mtr_rdr_cd"));

				list.add(MeterMap);
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown " + e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			//DatabaseImpl.CleanUp(con, ps, rs);
			DBManagerResourceRelease.close(rs, ps);
		}

		return list;
	}

	public static String getOMUnitCode(String cmOmUnitCd, String ConnType) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		String code = "";
		try {
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			String qyery = " SELECT CLC_CD FROM LOCATION WHERE CLC_TYPE='OM' AND CLC_NAME LIKE '%"
					+ cmOmUnitCd + "%' ";
			ps = dbConnection.prepareStatement(qyery);
			rs = ps.executeQuery();
			while (rs.next()) {
				code = rs.getString(1);

			}
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}

		return code;
	}
	
	public static boolean CheckRRNumberTariffIsLT7(String rrNumber, Connection dbConnection) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		String tariff = "";
		
		String qry = "SELECT count(1) CNT  FROM CUST_MASTER, TRF_MASTER "
				+ " WHERE CM_RR_NO = " + "'" + rrNumber + "'"
				+ " AND TM_SDESCR = 'LT7' AND CM_TRF_CATG = TM_TRF_CODE ";
		try {
			System.out.println("checkTariffDescription qry " + qry);
			ps = dbConnection.prepareStatement(qry);
			rs = ps.executeQuery();
			if (rs.next()) {
				if(rs.getInt("CNT") > 0){
					return true;
				}else{
					return false;
				}
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return false;
		} finally {
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}
		return false;
	}
	
	public static String GetInstallationTypeByRRno(String rrNumber, Connection dbConnection) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		String Inst_Type = "";
		
		String qry = "SELECT CM_INSTALL_TYP FROM CUST_MASTER WHERE cm_rr_no = '" + rrNumber + "' ";
		
		try {
			System.out.println("InstallationTypeByRRNO qry " + qry);
			ps = dbConnection.prepareStatement(qry);
			rs = ps.executeQuery();

			if(rs.next()) {
				String s = rs.getString("CM_INSTALL_TYP");
				Inst_Type = s;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}
		return Inst_Type;
	}
	
	public static String validateTernaryCondition(String value) {
		String returnValue="";
		try {
			returnValue = (value=="") ? "0" : value;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return returnValue;
	}

	public static String getTalukTypeNameTOCode(String taluk, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "0";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}pr = dbConnection.prepareStatement(" SELECT  CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP='TALUK' AND CCD_DESCR='"
					+ taluk + "' 	 ");
			rs = pr.executeQuery();
			while (rs.next()) {
				s = rs.getString(1);
			}
			if (s != null && s.length() > 0) {
				return s;
			} else {
				return s;
			}
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getDistrictTypeNameTOCode(String district, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "0";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP='DISTRICT' AND CCD_DESCR='"
					+ district + "' 	 ");
			rs = pr.executeQuery();
			while (rs.next()) {
				s = rs.getString(1);
			}
			if (s != null && s.length() > 0) {
				return s;
			} else {
				return s;
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getStateConstencyNameTOCode(String stateconstcy, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "0";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP ='S_CONSTCY' AND CCD_DESCR='"
					+ stateconstcy + "'  ");
			rs = pr.executeQuery();
			while (rs.next()) {
				s = rs.getString(1);
			}
			if (s != null && s.length() > 0) {
				return s;
			} else {
				return s;
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getCentralConstencyNameTOCode(String centralConstcy, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "0";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP ='C_CONSTCY' AND CCD_DESCR='"
					+ centralConstcy + "'   ");
			rs = pr.executeQuery();
			while (rs.next()) {
				s = rs.getString(1);
			}
			if (s != null && s.length() > 0) {
				return s;
			} else {
				return s;
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getRegionConstencyNameTOCode(String region, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "0";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP ='REG_TYP' AND CCD_DESCR='"
					+ region + "'  ");
			rs = pr.executeQuery();
			while (rs.next()) {
				s = rs.getString(1);
			}
			if (s != null && s.length() > 0) {
				return s;
			} else {
				return s;
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getPowerPurposeNameTOCode(String pwrpurpose, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "0";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP='PWR_PURPOS' "
					+ " AND CCD_DESCR='" + pwrpurpose + "' ");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}
			if (s != null && s.length() > 0) {
				return s;
			} else {
				return s;
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getIndustrialNameTOCode(String indCode, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "0";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP = 'IND_PWR_CD'  "
					+ " AND CCD_DESCR='" + indCode + "' ");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getConsumerCodeType(String consumerType, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String result = "";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE   CCD_CCM_CD_TYP = 'APLNT_TYP' AND CCD_DESCR='"
					+ consumerType + "' ");
			rs = pr.executeQuery();
			if (rs.next()) {
				result = rs.getString(1);

			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return result;
	} 

	public static String getPwrSanctionedNameTOCode(String pwrSanctBy, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP = 'SANCT_AUTH'   AND CCD_DESCR='"
					+ pwrSanctBy + "'	 ");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getOMUnitCodes(String onmUnitCode, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String result = "";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CLC_CD FROM LOCATION WHERE CLC_TYPE='OM' AND CLC_NAME = '"
					+ onmUnitCode + "' ");
			rs = pr.executeQuery();
			if (rs.next()) {
				result = rs.getString(1);
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return result;
	}

	public static String getConsumerStatusNameTOCode(String consumerStatus, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP = 'CONN_STS'   AND CCD_DESCR='"
					+ consumerStatus + "'	 ");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getTariffCodeByName(String tarrifCategory, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			String qry = " SELECT TM_TRF_CODE FROM TRF_MASTER WHERE TM_SDESCR= ? ";
			pr = dbConnection.prepareStatement(qry);
			pr.setString(1, tarrifCategory);
			rs = pr.executeQuery();
			while (rs.next()) {
				s = rs.getString(1);
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getInstallationTypeCodes(String installationType, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP = 'INSTL_TYP'"
					+ " AND CCD_DESCR = '" + installationType + "' ");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getStarterTypeNameTOCode(String starterType, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP = 'START_TYP' "
					+ " AND CCD_DESCR='" + starterType + "' ");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getJurisdictionTypeNameTOCode(String jurisdiction, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP='JURD' AND CCD_DESCR='"
					+ jurisdiction + "'	 ");
			rs = pr.executeQuery();
			while (rs.next()) {
				s = rs.getString(1);
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getWellTypeNameTOCode(String wellType, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP = 'WELL TYP' "
					+ " AND CCD_DESCR='" + wellType + "' ");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getLightingNameTOCode(String ligthingType, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP = 'LIGHT_TYP' "
					+ " AND CCD_DESCR='" + ligthingType + "'  ");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}

	public static String getConnectionTypeNameTOCode(String connType, String connectionType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(connectionType.equals("LT") || connectionType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connectionType.equals("HT") || connectionType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP ='CONN_TYP' AND CCD_DESCR='"
					+ connType + "' ");
			rs = pr.executeQuery();
			while (rs.next()) {
				s = rs.getString(1);
			}
			if (s != null && s.length() > 0) {
				return s;
			} else {
				return s;
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}
	
	public static String ConvertIFNullToString(String value){
		
		return ((value == null || value == "") ? "" : value);
		
	}

	public static String getNameToCodeCommon(String docDescription, String type, String connType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		String s = "";
		try {

			if(connType.equals("LT") || connType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connType.equals("HT") || connType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			pr = dbConnection.prepareStatement(" SELECT CCD_CD_VAL FROM CODE_DETL WHERE CCD_CCM_CD_TYP = '"
					+ type + "'" + "  AND CCD_DESCR = '" + docDescription + "' ");
			rs = pr.executeQuery();
			if (rs.next()) {
				s = rs.getString(1);
			}
			
		} catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		
		return s;
	}
	
	
	public static int getMaxSLNO(String RRno,String tableName,String column_name,String Conntype, String WHERE_RR_NO) {
		PreparedStatement pr = null;
		ResultSet rs = null;
		int slno = 0;

		System.out.println("Inside slno");
		try {

			if(Conntype.equals("LT") || Conntype == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(Conntype.equals("HT") || Conntype == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			pr = dbConnection.prepareStatement(" select NVL(max(TO_NUMBER("+column_name+")),0)+1 slno from  "+tableName+" where "+WHERE_RR_NO+" = '"+RRno+"'" );
			System.out.println(" select NVL(max("+column_name+"),0)+1 slno from  "+tableName+" where "+WHERE_RR_NO+" = '"+RRno+"'");
			rs = pr.executeQuery();
			if (rs.next()) {
				System.out.println("slno :  " + rs.getString("slno"));
				slno = Integer.parseInt(rs.getString("slno")) ;
			}
		}  catch (Exception e) {
			System.out.println("Exception thrown " + e);
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		return slno;

	}
	
	public static boolean CheckRrNumberExists(String RrNumber){
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			
			String Query = " SELECT COUNT(*) CNT FROM IVRS_CUST_MASTR WHERE IVRS_BCN_RR_NO = '"+RrNumber+"' "; 
			
			dbConnection = databaseObj.getDatabaseConnection();
			ps = dbConnection.prepareStatement(Query);
			rs = ps.executeQuery();
			
			if(rs.next()){
				if(rs.getInt("CNT") > 0){
					return true;
				}else{
					return false;
				}
			}
			
		}catch(SQLException e){
			// TODO: handle exception
			System.out.println("Error Occured : "+ e.getMessage());
			return false;
		}
		catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error Occured : "+ e.getMessage());
			return false;
		}
		
		return false;
		
	}
	
	public static int[] genericBatchUpdate(ArrayList<String> al,String connType) {

		java.sql.Statement st = null;

		int[] i = { 0 };
		try {
			if(connType.equals("LT") || connType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(connType.equals("HT") || connType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}	st = dbConnection.createStatement();
			Iterator<String> it = al.iterator();
			while (it.hasNext()) {
				String s = it.next();
				//System.out.println("batch query" + s);
				st.addBatch(s);
			}
			i = st.executeBatch();
			System.out.println("return value BATCH===>" + i.length);

		} catch (BatchUpdateException b) {
			
			System.out.println("Exception thrown " + b + " count "
					+ b.getUpdateCounts().length);
			i = b.getUpdateCounts();

		}

		catch (SQLException e) {
			System.out.println("Exception thrown " + e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
/*			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);*/

		}

		return i;
	}
	
}
