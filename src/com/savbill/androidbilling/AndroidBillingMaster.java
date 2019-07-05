/**
 * 
 */
package com.savbill.androidbilling;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import org.apache.log4j.lf5.viewer.configure.MRUFileManager;

import com.savbill.database.DBManagerIMPL;
import com.savbill.database.DBManagerResourceRelease;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author win
 *
 */
public class AndroidBillingMaster implements IAndroidBillingMaster {

	
	DBManagerIMPL databaseObj = new DBManagerIMPL();
	Connection dbConnection;
	
	@Override
	public JSONObject getBillingDataFromTable(JSONObject object) {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONObject jsonResponse = new JSONObject();
		String query_trfmain = "";
		String query_trffix = "";
		String query_trfrebate = "";
		String query_trfslab = "";
		String query_download_data = "";
		
		JSONArray trfmain_array = new JSONArray();
		JSONArray trffix_array = new JSONArray();
		JSONArray trfrebate_array = new JSONArray();
		JSONArray trfslab_array = new JSONArray();
		JSONArray customer_array = new JSONArray();
		
		JSONObject json = null;
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){
				
					String location_code = (String) object.get("location_code").toString().trim();
					String meter_code    = (String) object.get("meter_code").toString().trim();
					String reading_date = (String) object.get("meter_date").toString().trim();
					
					if((location_code.length() > 0 && location_code != null)){
						
						dbConnection = databaseObj.getDatabaseConnection();
						
						if(dbConnection != null){
							
							query_trfmain = "SELECT TM_TRF_CODE , TM_TRF_TYPE  , TM_TRF_PWR_UNIT  , TM_MIN_FXD  ,  TM_DC_UNTS  ,  TM_TRF_TAX , TM_CONST_CHRGS " +
									" FROM tariffmain WHERE TO_DATE('"+reading_date+"','dd/mm/yyyy') BETWEEN TM_FR_DT AND TM_TO_DT ORDER BY TM_TRF_CODE,TM_TRF_TYPE";
							query_trffix  = "SELECT TF_TRF_CODE , TF_ITEM , TF_CHARGE_TYP , TF_FROM_UNITS , TF_TO_UNITS , TF_TRF_AMT " +
									"FROM TARIFFFIX WHERE TO_DATE('"+reading_date+"','dd/mm/yyyy') BETWEEN TF_FR_DT AND TF_TO_DT";
							query_trfrebate = "SELECT TR_RBT_CODE , TR_CH_TYPE , TR_RBT , TR_MAX_RBT , TR_RBT_TYP " +
									"FROM TARIFFREBATE WHERE TO_DATE('"+reading_date+"','dd/mm/yyyy') BETWEEN TR_FR_DT AND TR_TO_DT ";
							query_trfslab = "SELECT TS_TRF_CODE , TS_ITEM , TS_FROM_UNITS , TS_TO_UNITS , TS_TRF_AMT " +
									"FROM TARIFFSLAB WHERE TO_DATE('"+reading_date+"','dd/mm/yyyy') BETWEEN TS_FR_DT AND TS_TO_DT ORDER BY TS_TRF_CODE,TS_ITEM";

							query_download_data = "SELECT HDD_LOC_CD ,  SUBSTR(HDD_RR_NO,8) HDD_RR_NO ,  HDD_LDGR_NO ,  HDD_ACTL_FOLIO_NO , "
									+ " LPAD(LTRIM(HDD_SPT_FOLIO_NO),5 , ' ')  HDD_SPT_FOLIO_NO,  LTRIM(TO_CHAR(TO_NUMBER(HDD_TRF_CODE),'009')) HDD_TRF_CODE ,"
									+ " LTRIM(TO_CHAR(HDD_BILL_NUM, '09999'))  HDD_BILL_NUM ,  TO_CHAR(HDD_BILL_DT,'DD-MM-YYYY') HDD_BILL_DT ,"
									+ " HDD_CONSMR_NAME ,  HDD_ADDR1 ,  NVL(HDD_ADDR2,'-') HDD_ADDR2 ,  NVL(HDD_ADDR3,'-')  HDD_ADDR3,  HDD_BILLING_MONTH , "
									+ " TO_CHAR(HDD_RDG_DT,'DD-MM-YYYY')  HDD_RDG_DT, NVL(HDD_MTR_RDR_CD,'0') HDD_MTR_RDR_CD ,  NVL(HDD_INSTAL_STS,0)  HDD_INSTAL_STS,"
									+ " NVL(HDD_LINE_MIN,0)  HDD_LINE_MIN,  NVL(HDD_SANCT_HP,0)  HDD_SANCT_HP, NVL(HDD_SANCT_KW,0)  HDD_SANCT_KW, "
									+ " NVL(HDD_CT_RATIO,0)  HDD_CT_RATIO,   NVL(HDD_PREV_RDG,0) HDD_PREV_RDG,  HDD_AVG_CONSMP ,  NVL(HDD_SOLAR_REBATE,0) HDD_SOLAR_REBATE,"
									+ " NVL(HDD_FL_REBATE,0) HDD_FL_REBATE, NVL(HDD_PH_REBATE,0) HDD_PH_REBATE, NVL(HDD_TELEP_REBATE,0) HDD_TELEP_REBATE, "
									+ " NVL(HDD_PWR_FACTOR,0)  HDD_PWR_FACTOR,TO_CHAR(HDD_MTRCHNG_DT1,'DD-MM-YYYY')  HDD_MTRCHNG_DT1,  HDD_MTRCHNG_RDG1 ,"
									+ " TO_CHAR(HDD_MTRCHN_GDT2,'DD-MM-YYYY')  HDD_MTRCHN_GDT2,  NVL(HDD_MTRCHN_GRDG2,0) HDD_MTRCHN_GRDG2 , NVL(HDD_BILL_AMT,0) HDD_BILL_AMT,  "
									+ " NVL(HDD_DEMAND_ARREARS,0) HDD_DEMAND_ARREARS, (NVL(HDD_INT_ARREARS,0) + NVL(HDD_3MMD_DEP_INT,0)) HDD_INT_ARRS_3MMD_DEP_INT,"
									+ " NVL(HDD_TAX_ARREARS,0)  HDD_TAX_ARREARS,  NVL(HDD_DELAY_INT,0) HDD_DELAY_INT,  NVL(HDD_AMT_PAID1,0) HDD_AMT_PAID1, "
									+ " TO_CHAR(HDD_PAID_DATE1,'DD-MM-YYYY') HDD_PAID_DATE1,NVL(HDD_AMT_PAID2,0) HDD_AMT_PAID2,  TO_CHAR(HDD_PAID_DATE2,'DD-MM-YYYY') HDD_PAID_DATE2,"
									+ " NVL(HDD_OTHERS,0) HDD_OTHERS, NVL(HDD_BILL_GEN_FLAG,0)  HDD_BILL_GEN_FLAG, NVL(HDD_REBATE_CAP,0) HDD_REBATE_CAP,  NVL(HDD_PREVIOUS_DEMAND1,0)  HDD_PREVIOUS_DEMAND1, "
									+ " NVL(HDD_PREVIOUS_DEMAND2,0) HDD_PREVIOUS_DEMAND2,NVL(HDD_PREVIOUS_DEMAND3,0) HDD_PREVIOUS_DEMAND3, NVL(HDD_BILLING_MODE,0) HDD_BILLING_MODE,  "
									+ " NVL(HDD_MTR_CONST,0) HDD_MTR_CONST,  NVL(HDD_DEMAND_BASED,0) HDD_DEMAND_BASED, NVL(HDD_RURAL_REBATE,0) HDD_RURAL_REBATE, NVL(HDD_NORMAL,0) HDD_NORMAL,"
									+ " NVL(HDD_APPEAL_AMOUNT,0) HDD_APPEAL_AMOUNT, NVL(HDD_INT_ON_APPEAL_AMT,0) HDD_INT_ON_APPEAL_AMT, NVL(HDD_KVAH,0) HDD_KVAH, "
									+ " NVL(HDD_METER_TVM,0) HDD_METER_TVM, NVL(HDD_INST_TYP,0) HDD_INST_TYP, (NVL(HDD_SANCT_HP,0)*100)  HDD_SANCT_HP,  (NVL(HDD_SANCT_KW,0)*100) HDD_SANCT_KW,"
									+ " TO_CHAR(ADD_MONTHS(HDD_BILL_DT, -1),'DD-MM-YYYY') HDD_BILL_DT, HDD_P_CREDIT ,  HDD_P_DEBIT ,  TO_CHAR(HDD_DUE_DATE,'DD-MM-YYYY') HDD_DUE_DATE,  "
									+ " HDD_IVRS_ID , HDD_ECS_FLG , TO_CHAR(LTRIM(NVL(HDD_PART_PERIOD,0)),'09.99') HDD_PART_PERIOD, NVL(HDD_DLMNR,0) HDD_DLMNR,"
									+ " NVL(HDD_TAX_FLG,'N') HDD_TAX_FLG, NVL(HDD_DC_FLG,'N') HDD_DC_FLG, (NVL(HDD_INTRST_ARRS,0) + NVL(HDD_DELAY_ARRS, 0)) HDD_INTRST_ARRS_HDD_DELAY_ARRS,NVL(HDD_INTRST_TAX,0) HDD_INTRST_TAX, "
									+ " NVL(HDD_FIRST_RDG_FLG,'N') HDD_FIRST_RDG_FLG,NVL(HDD_ERR_PRCNT,0) HDD_ERR_PRCNT, NVL(HDD_MNR_FLG,'N') HDD_MNR_FLG, NVL(HDD_OLD_MTR_CONSMP,0) HDD_OLD_MTR_CONSMP,"
									+ " NVL(HDD_INT_ON_TAX,0) HDD_INT_ON_TAX,HDD_SUBDIV_NAME ,HDD_NO_TAX_COMP ,  HDD_PREV_RDG_FLG , HDD_PL_FLG , HDD_PREV_CKWH ,  HDD_REG_PENALTY , "
									+ " NVL(HDD_CALC_PF_FLG,'N') HDD_CALC_PF_FLG, NVL(HDD_LAST_DL_DAYS,30) HDD_LAST_DL_DAYS, NVL(HDD_CUR_QRTR,0) HDD_CUR_QRTR, NVL(HDD_FREQUENCY,0) HDD_FREQUENCY, "
									+ " NVL(HDD_ANNUAL_MIN_FIX,0) HDD_ANNUAL_MIN_FIX, NVL(HDD_CAPACITY_RBT,0) HDD_CAPACITY_RBT,HDD_CHQ_DIS_FLG , HDD_ORPHANAGE_REBATE ,"
									+ " NVL(HDD_DEP_INT,0) HDD_DEP_INT, NVL(HDD_MC_FLG, 'N') HDD_MC_FLG, NVL(HDD_3MMD_DEP, 0) HDD_3MMD_DEP , "
									+ " NVL(HDD_NFC_CODE,0) HDD_NFC_CODE, NVL(HDD_NFC_TAG,'N') HDD_NFC_TAG  "
									+ " FROM   HHD_DOWNLOAD_DATA  "
									+ " WHERE "
									+ " HDD_LOC_CD  = '"+location_code+"' AND "
									+ " hdd_BILL_DT = TO_DATE('"+reading_date+"','dd/mm/yyyy')  AND "
									+ " hdd_mtr_rdr_cd='"+meter_code+"' "
									//+ " AND NVL(HDD_BILL_FLG,'N')='N' "
									+ " ORDER BY LPAD(LTRIM(HDD_SPT_FOLIO_NO),5 , ' ')";
							
							
							System.out.println(query_trfmain);
							ps = dbConnection.prepareStatement(query_trfmain);
							rs = ps.executeQuery();
							
							while(rs.next()){
								
								json = new JSONObject();
								
								json.put("TM_TRF_CODE", rs.getString("TM_TRF_CODE"));
								json.put("TM_TRF_TYPE", rs.getString("TM_TRF_TYPE"));
								json.put("TM_TRF_PWR_UNIT", rs.getString("TM_TRF_PWR_UNIT"));
								json.put("TM_MIN_FXD", rs.getString("TM_MIN_FXD"));
								json.put("TM_DC_UNTS", rs.getString("TM_DC_UNTS"));
								json.put("TM_TRF_TAX", rs.getString("TM_TRF_TAX"));
								json.put("TM_CONST_CHRGS", rs.getString("TM_CONST_CHRGS"));
								
								trfmain_array.add(json);
							}
							
							DBManagerResourceRelease.close(ps);
							
							System.out.println(query_trffix);
							ps = dbConnection.prepareStatement(query_trffix);
							rs = ps.executeQuery();
							
							while(rs.next()){
								
								json = new JSONObject();
								
								json.put("TF_TRF_CODE", rs.getString("TF_TRF_CODE"));
								json.put("TF_CHARGE_TYP", rs.getString("TF_CHARGE_TYP"));
								json.put("TF_ITEM", rs.getString("TF_ITEM"));
								json.put("TF_FROM_UNITS", rs.getString("TF_FROM_UNITS"));
								json.put("TF_TO_UNITS", rs.getString("TF_TO_UNITS"));
								json.put("TF_TRF_AMT", rs.getString("TF_TRF_AMT"));
								
								trffix_array.add(json);
							}
							
							DBManagerResourceRelease.close(ps);
							
							System.out.println(query_trfrebate);
							ps = dbConnection.prepareStatement(query_trfrebate);
							rs = ps.executeQuery();
							
							while(rs.next()){
								
								json = new JSONObject();
								
								
								json.put("TR_RBT_CODE", rs.getString("TR_RBT_CODE"));
								json.put("TR_CH_TYPE", rs.getString("TR_CH_TYPE"));
								json.put("TR_RBT", rs.getString("TR_RBT"));
								json.put("TR_MAX_RBT", rs.getString("TR_MAX_RBT"));
								json.put("TR_RBT_TYP", rs.getString("TR_RBT_TYP"));
								
								trfrebate_array.add(json);
							}
							
							DBManagerResourceRelease.close(ps);
							
							System.out.println(query_trfslab);
							ps = dbConnection.prepareStatement(query_trfslab);
							rs = ps.executeQuery();
							
							while(rs.next()){
								
								json = new JSONObject();
								
								json.put("TS_TRF_CODE", rs.getString("TS_TRF_CODE"));
								json.put("TS_ITEM", rs.getString("TS_ITEM"));
								json.put("TS_FROM_UNITS", rs.getString("TS_FROM_UNITS"));
								json.put("TS_TO_UNITS", rs.getString("TS_TO_UNITS"));
								json.put("TS_TRF_AMT", rs.getString("TS_TRF_AMT"));
								
								trfslab_array.add(json);
							}
							
							DBManagerResourceRelease.close(ps);
							
							System.out.println(query_download_data);
							ps = dbConnection.prepareStatement(query_download_data);
							rs = ps.executeQuery();
							
							while(rs.next()){
								
								json = new JSONObject();
								
								json.put("HDD_LOC_CD",rs.getString("HDD_LOC_CD"));
								json.put("HDD_RR_NO",rs.getString("HDD_RR_NO"));
								json.put("HDD_LDGR_NO",rs.getString("HDD_LDGR_NO"));
								json.put("HDD_ACTL_FOLIO_NO",rs.getString("HDD_ACTL_FOLIO_NO"));
								json.put("HDD_SPT_FOLIO_NO",rs.getString("HDD_SPT_FOLIO_NO"));
								json.put("HDD_TRF_CODE",rs.getString("HDD_TRF_CODE"));
								json.put("HDD_BILL_NUM",rs.getString("HDD_BILL_NUM"));
								json.put("HDD_BILL_DT",rs.getString("HDD_BILL_DT"));
								json.put("HDD_CONSMR_NAME",rs.getString("HDD_CONSMR_NAME"));
								json.put("HDD_ADDR1",rs.getString("HDD_ADDR1"));
								json.put("HDD_ADDR2",rs.getString("HDD_ADDR2"));
								json.put("HDD_ADDR3",rs.getString("HDD_ADDR3"));
								json.put("HDD_BILLING_MONTH",rs.getString("HDD_BILLING_MONTH"));
								json.put("HDD_RDG_DT",rs.getString("HDD_RDG_DT"));
								json.put("HDD_MTR_RDR_CD",rs.getString("HDD_MTR_RDR_CD"));
								json.put("HDD_INSTAL_STS",rs.getString("HDD_INSTAL_STS"));
								json.put("HDD_LINE_MIN",rs.getString("HDD_LINE_MIN"));
								json.put("HDD_SANCT_HP",rs.getString("HDD_SANCT_HP"));
								json.put("HDD_SANCT_KW",rs.getString("HDD_SANCT_KW"));
								json.put("HDD_CT_RATIO",rs.getString("HDD_CT_RATIO"));
								json.put("HDD_PREV_RDG",rs.getString("HDD_PREV_RDG"));
								json.put("HDD_AVG_CONSMP",rs.getString("HDD_AVG_CONSMP"));
								json.put("HDD_SOLAR_REBATE",rs.getString("HDD_SOLAR_REBATE"));
								json.put("HDD_FL_REBATE",rs.getString("HDD_FL_REBATE"));
								json.put("HDD_PH_REBATE",rs.getString("HDD_PH_REBATE"));
								json.put("HDD_TELEP_REBATE",rs.getString("HDD_TELEP_REBATE"));
								json.put("HDD_PWR_FACTOR",rs.getString("HDD_PWR_FACTOR"));
								json.put("HDD_MTRCHNG_DT1",rs.getString("HDD_MTRCHNG_DT1"));
								json.put("HDD_MTRCHNG_RDG1",rs.getString("HDD_MTRCHNG_RDG1"));
								json.put("HDD_MTRCHN_GDT2",rs.getString("HDD_MTRCHN_GDT2"));
								json.put("HDD_MTRCHN_GRDG2",rs.getString("HDD_MTRCHN_GRDG2"));
								json.put("HDD_BILL_AMT",rs.getString("HDD_BILL_AMT"));
								json.put("HDD_DEMAND_ARREARS",rs.getString("HDD_DEMAND_ARREARS"));
								json.put("HDD_INT_ARRS_3MMD_DEP_INT",rs.getString("HDD_INT_ARRS_3MMD_DEP_INT"));
								json.put("HDD_TAX_ARREARS",rs.getString("HDD_TAX_ARREARS"));
								json.put("HDD_DELAY_INT",rs.getString("HDD_DELAY_INT"));
								json.put("HDD_AMT_PAID1",rs.getString("HDD_AMT_PAID1"));
								json.put("HDD_PAID_DATE1",rs.getString("HDD_PAID_DATE1"));
								json.put("HDD_AMT_PAID2",rs.getString("HDD_AMT_PAID2"));
								json.put("HDD_PAID_DATE2",rs.getString("HDD_PAID_DATE2"));
								json.put("HDD_OTHERS",rs.getString("HDD_OTHERS"));
								json.put("HDD_BILL_GEN_FLAG",rs.getString("HDD_BILL_GEN_FLAG"));
								json.put("HDD_REBATE_CAP",rs.getString("HDD_REBATE_CAP"));
								json.put("HDD_PREVIOUS_DEMAND1",rs.getString("HDD_PREVIOUS_DEMAND1"));
								json.put("HDD_PREVIOUS_DEMAND2",rs.getString("HDD_PREVIOUS_DEMAND2"));
								json.put("HDD_PREVIOUS_DEMAND3",rs.getString("HDD_PREVIOUS_DEMAND3"));
								json.put("HDD_BILLING_MODE",rs.getString("HDD_BILLING_MODE"));
								json.put("HDD_MTR_CONST",rs.getString("HDD_MTR_CONST"));
								json.put("HDD_DEMAND_BASED",rs.getString("HDD_DEMAND_BASED"));
								json.put("HDD_RURAL_REBATE",rs.getString("HDD_RURAL_REBATE"));
								json.put("HDD_NORMAL",rs.getString("HDD_NORMAL"));
								json.put("HDD_APPEAL_AMOUNT",rs.getString("HDD_APPEAL_AMOUNT"));
								json.put("HDD_INT_ON_APPEAL_AMT",rs.getString("HDD_INT_ON_APPEAL_AMT"));
								json.put("HDD_KVAH",rs.getString("HDD_KVAH"));
								json.put("HDD_METER_TVM",rs.getString("HDD_METER_TVM"));
								json.put("HDD_INST_TYP",rs.getString("HDD_INST_TYP"));
								json.put("HDD_SANCT_HP",rs.getString("HDD_SANCT_HP"));
								json.put("HDD_SANCT_KW",rs.getString("HDD_SANCT_KW"));
								json.put("HDD_BILL_DT",rs.getString("HDD_BILL_DT"));
								json.put("HDD_P_CREDIT",rs.getString("HDD_P_CREDIT"));
								json.put("HDD_P_DEBIT",rs.getString("HDD_P_DEBIT"));
								json.put("HDD_DUE_DATE",rs.getString("HDD_DUE_DATE"));
								json.put("HDD_IVRS_ID",rs.getString("HDD_IVRS_ID"));
								json.put("HDD_ECS_FLG",rs.getString("HDD_ECS_FLG"));
								json.put("HDD_PART_PERIOD",rs.getString("HDD_PART_PERIOD"));
								json.put("HDD_DLMNR",rs.getString("HDD_DLMNR"));
								json.put("HDD_TAX_FLG",rs.getString("HDD_TAX_FLG"));
								json.put("HDD_DC_FLG",rs.getString("HDD_DC_FLG"));
								json.put("HDD_INTRST_ARRS_HDD_DELAY_ARRS",rs.getString("HDD_INTRST_ARRS_HDD_DELAY_ARRS"));
								json.put("HDD_INTRST_TAX",rs.getString("HDD_INTRST_TAX"));
								json.put("HDD_FIRST_RDG_FLG",rs.getString("HDD_FIRST_RDG_FLG"));
								json.put("HDD_ERR_PRCNT",rs.getString("HDD_ERR_PRCNT"));
								json.put("HDD_MNR_FLG",rs.getString("HDD_MNR_FLG"));
								json.put("HDD_OLD_MTR_CONSMP",rs.getString("HDD_OLD_MTR_CONSMP"));
								json.put("HDD_INT_ON_TAX",rs.getString("HDD_INT_ON_TAX"));
								json.put("HDD_SUBDIV_NAME",rs.getString("HDD_SUBDIV_NAME"));
								json.put("HDD_NO_TAX_COMP",rs.getString("HDD_NO_TAX_COMP"));
								json.put("HDD_PREV_RDG_FLG",rs.getString("HDD_PREV_RDG_FLG"));
								json.put("HDD_PL_FLG",rs.getString("HDD_PL_FLG"));
								json.put("HDD_PREV_CKWH",rs.getString("HDD_PREV_CKWH"));
								json.put("HDD_REG_PENALTY",rs.getString("HDD_REG_PENALTY"));
								json.put("HDD_CALC_PF_FLG",rs.getString("HDD_CALC_PF_FLG"));
								json.put("HDD_LAST_DL_DAYS",rs.getString("HDD_LAST_DL_DAYS"));
								json.put("HDD_CUR_QRTR",rs.getString("HDD_CUR_QRTR"));
								json.put("HDD_FREQUENCY",rs.getString("HDD_FREQUENCY"));
								json.put("HDD_ANNUAL_MIN_FIX",rs.getString("HDD_ANNUAL_MIN_FIX"));
								json.put("HDD_CAPACITY_RBT",rs.getString("HDD_CAPACITY_RBT"));
								json.put("HDD_CHQ_DIS_FLG",rs.getString("HDD_CHQ_DIS_FLG"));
								json.put("HDD_ORPHANAGE_REBATE",rs.getString("HDD_ORPHANAGE_REBATE"));
								json.put("HDD_DEP_INT",rs.getString("HDD_DEP_INT"));
								json.put("HDD_MC_FLG",rs.getString("HDD_MC_FLG"));
								json.put("HDD_3MMD_DEP",rs.getString("HDD_3MMD_DEP"));
								
								json.put("HDD_NFC_CODE",rs.getString("HDD_NFC_CODE"));
								json.put("HDD_NFC_TAG",rs.getString("HDD_NFC_TAG"));

								customer_array.add(json);
							}
							
							DBManagerResourceRelease.close(ps);
							
							jsonResponse.put("status", "success");
							jsonResponse.put("TARIFF_MAIN", trfmain_array);
							jsonResponse.put("TARIFF_FIX", trffix_array);
							jsonResponse.put("TARIFF_REBATE", trfrebate_array);
							jsonResponse.put("TARIFF_SLAB", trfslab_array);
							jsonResponse.put("CONSUMER_DATA", customer_array);
							jsonResponse.put("message", "Success !!! Consumer Data Retrieved Successfully.");
							
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
			jsonResponse.put("message", "Error In Retrieving Meter Reader Count.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
			//DatabaseImpl.CleanUp(con, ps, rs);
		}
		
		return jsonResponse;
	}

	/*@Override
	public JSONObject getRecordCountForTodays(JSONObject object) {
		// TODO Auto-generated method stub


		//Connection con = null;
				PreparedStatement ps = null;
				ResultSet rs = null;

				JSONObject jsonResponse = new JSONObject();
				String query = "";
				String RdgDay = "";
				
				try {
					if(!object.isEmpty() || !object.isNullObject()){
						
							String location_code = (String) object.get("location_code").toString().trim();
							String meter_code    = (String) object.get("meter_code").toString().trim();
							String reading_date = (String) object.get("meter_date").toString().trim();
							
							if((location_code.length() > 0 && location_code != null)){
								
								dbConnection = databaseObj.getDatabaseConnection();
								
								if(dbConnection != null){
									
									if(reading_date.length() > 0){
										String reading_date_split[] = reading_date.split("/");
										RdgDay = reading_date_split[0];
									}
									
									query =   " SELECT COUNT(9) CNT FROM  CUST_MASTER   "
											+ " WHERE  "
											+ " CM_MTR_RDR_CD = '"+meter_code+"' AND "
											+ " CM_MTR_RDG_DAY = "+RdgDay +" AND "
											+ " CM_CONSMR_STS IN ('1','9','10')  AND "
											+ " CM_RR_NO like '"+location_code+"%'";
									
									System.out.println(query);
									
									ps = dbConnection.prepareStatement(query);
									rs = ps.executeQuery();
									
									if (rs.next()) {
										
										if(rs.getInt("CNT") > 0){
											
											jsonResponse.put("status", "success");
											jsonResponse.put("message","Reading Exist. You Can Download Data . ");
											jsonResponse.put("record_count", rs.getInt("CNT"));
										}else{
											jsonResponse.put("status", "fail");
											jsonResponse.put("message","Today You Don't Have Reading");
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
					jsonResponse.put("message", "Error In Retrieving Meter Reader Count.");
					// TODO: handle exception
				}finally
				{
					DBManagerResourceRelease.close(rs, ps, dbConnection);
					//DatabaseImpl.CleanUp(con, ps, rs);
				}
				
				return jsonResponse;
	}

	@Override
	public JSONObject downloadDataToTable(JSONObject object) {
		// TODO Auto-generated method stub

		CallableStatement cs = null;
		ResultSet rs = null;

		JSONObject jsonResponse = new JSONObject();
		String RdgDay = "";
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){
				
					String location_code = (String) object.get("location_code").toString().trim();
					String meter_code    = (String) object.get("meter_code").toString().trim();
					String reading_date = (String) object.get("meter_date").toString().trim();
					
					if((location_code.length() > 0 && location_code != null)){
						
						dbConnection = databaseObj.getDatabaseConnection();
						
						if(dbConnection != null){
							
							if(reading_date.length() > 0){
								String reading_date_split[] = reading_date.split("/");
								RdgDay = reading_date_split[0];
							}
							
							cs = dbConnection.prepareCall("{CALL PKG_HHD.MOVE_TO_HHD(?,?,?,?,?,?)}");
							cs.setString(1,meter_code);
							cs.setString(2,"NADEEM");
							cs.setString(3,reading_date);
							cs.setString(4,"M");
							cs.setString(5,location_code);
							cs.registerOutParameter(6,java.sql.Types.VARCHAR);
							
							cs.execute();
							String StrOutSts = cs.getString(6);
							System.out.println("Output status : " + StrOutSts);
							
							jsonResponse.put("status", "sucesss");
							jsonResponse.put("message", "true");
							jsonResponse.put("output_status", StrOutSts);
							
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
			jsonResponse.put("message", "Error In Retrieving Meter Reader Count.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs);
			DBManagerResourceRelease.close(dbConnection);
			//DatabaseImpl.CleanUp(con, ps, rs);
		}
		
/*									+ " NVL(HDD_DEP_INT,0) HDD_DEP_INT, NVL(HDD_MC_FLG, 'N') HDD_MC_FLG, NVL(HDD_3MMD_DEP, 0) HDD_3MMD_DEP ,"
									+ " NVL(HDD_NFC_CODE,'-') HDD_NFC_CODE,  NVL(HDD_NFC_TAG,'N') HDD_NFC_TAG  "
									+ " FROM   HHD_DOWNLOAD_DATA  "
									+ " WHERE "
									+ " HDD_LOC_CD  = '"+location_code+"' AND "
									+ " hdd_BILL_DT = TO_DATE('"+reading_date+"','dd/mm/yyyy')  AND "
									+ " hdd_mtr_rdr_cd='"+meter_code+"' "
									//+ " AND NVL(HDD_BILL_FLG,'N')='N' "
									+ " ORDER BY LPAD(LTRIM(HDD_SPT_FOLIO_NO),5 , ' ')";
							
							
							System.out.println(query_trfmain);
							ps = dbConnection.prepareStatement(query_trfmain);
							rs = ps.executeQuery();
							
							while(rs.next()){
								
								json = new JSONObject();
								
								json.put("TM_TRF_CODE", rs.getString("TM_TRF_CODE"));
								json.put("TM_TRF_TYPE", rs.getString("TM_TRF_TYPE"));
								json.put("TM_TRF_PWR_UNIT", rs.getString("TM_TRF_PWR_UNIT"));
								json.put("TM_MIN_FXD", rs.getString("TM_MIN_FXD"));
								json.put("TM_DC_UNTS", rs.getString("TM_DC_UNTS"));
								json.put("TM_TRF_TAX", rs.getString("TM_TRF_TAX"));
								json.put("TM_CONST_CHRGS", rs.getString("TM_CONST_CHRGS"));
								
								trfmain_array.add(json);
							}
							
							DBManagerResourceRelease.close(ps);
							DBManagerResourceRelease.close(rs);
							
							System.out.println(query_trffix);
							ps = dbConnection.prepareStatement(query_trffix);
							rs = ps.executeQuery();
							
							while(rs.next()){
								
								json = new JSONObject();
								
								json.put("TF_TRF_CODE", rs.getString("TF_TRF_CODE"));
								json.put("TF_CHARGE_TYP", rs.getString("TF_CHARGE_TYP"));
								json.put("TF_ITEM", rs.getString("TF_ITEM"));
								json.put("TF_FROM_UNITS", rs.getString("TF_FROM_UNITS"));
								json.put("TF_TO_UNITS", rs.getString("TF_TO_UNITS"));
								json.put("TF_TRF_AMT", rs.getString("TF_TRF_AMT"));
								
								trffix_array.add(json);
							}
							
							DBManagerResourceRelease.close(ps);
							DBManagerResourceRelease.close(rs);
							
							System.out.println(query_trfrebate);
							ps = dbConnection.prepareStatement(query_trfrebate);
							rs = ps.executeQuery();
							
							while(rs.next()){
								
								json = new JSONObject();
								
								
								json.put("TR_RBT_CODE", rs.getString("TR_RBT_CODE"));
								json.put("TR_CH_TYPE", rs.getString("TR_CH_TYPE"));
								json.put("TR_RBT", rs.getString("TR_RBT"));
								json.put("TR_MAX_RBT", rs.getString("TR_MAX_RBT"));
								json.put("TR_RBT_TYP", rs.getString("TR_RBT_TYP"));
								
								trfrebate_array.add(json);
							}
							
							DBManagerResourceRelease.close(ps);
							DBManagerResourceRelease.close(rs);
							
							System.out.println(query_trfslab);
							ps = dbConnection.prepareStatement(query_trfslab);
							rs = ps.executeQuery();
							
							while(rs.next()){
								
								json = new JSONObject();
								
								json.put("TS_TRF_CODE", rs.getString("TS_TRF_CODE"));
								json.put("TS_ITEM", rs.getString("TS_ITEM"));
								json.put("TS_FROM_UNITS", rs.getString("TS_FROM_UNITS"));
								json.put("TS_TO_UNITS", rs.getString("TS_TO_UNITS"));
								json.put("TS_TRF_AMT", rs.getString("TS_TRF_AMT"));
								
								trfslab_array.add(json);
							}
							
							DBManagerResourceRelease.close(ps);
							DBManagerResourceRelease.close(rs);
							
							System.out.println(query_download_data);
							ps = dbConnection.prepareStatement(query_download_data);
							rs = ps.executeQuery();
							
							while(rs.next()){
								
								json = new JSONObject();
								
								json.put("HDD_LOC_CD",rs.getString("HDD_LOC_CD"));
								json.put("HDD_RR_NO",rs.getString("HDD_RR_NO"));
								json.put("HDD_LDGR_NO",rs.getString("HDD_LDGR_NO"));
								json.put("HDD_ACTL_FOLIO_NO",rs.getString("HDD_ACTL_FOLIO_NO"));
								json.put("HDD_SPT_FOLIO_NO",rs.getString("HDD_SPT_FOLIO_NO"));
								json.put("HDD_TRF_CODE",rs.getString("HDD_TRF_CODE"));
								json.put("HDD_BILL_NUM",rs.getString("HDD_BILL_NUM"));
								json.put("HDD_BILL_DT",rs.getString("HDD_BILL_DT"));
								json.put("HDD_CONSMR_NAME",rs.getString("HDD_CONSMR_NAME"));
								json.put("HDD_ADDR1",rs.getString("HDD_ADDR1"));
								json.put("HDD_ADDR2",rs.getString("HDD_ADDR2"));
								json.put("HDD_ADDR3",rs.getString("HDD_ADDR3"));
								json.put("HDD_BILLING_MONTH",rs.getString("HDD_BILLING_MONTH"));
								json.put("HDD_RDG_DT",rs.getString("HDD_RDG_DT"));
								json.put("HDD_MTR_RDR_CD",rs.getString("HDD_MTR_RDR_CD"));
								json.put("HDD_INSTAL_STS",rs.getString("HDD_INSTAL_STS"));
								json.put("HDD_LINE_MIN",rs.getString("HDD_LINE_MIN"));
								json.put("HDD_SANCT_HP",rs.getString("HDD_SANCT_HP"));
								json.put("HDD_SANCT_KW",rs.getString("HDD_SANCT_KW"));
								json.put("HDD_CT_RATIO",rs.getString("HDD_CT_RATIO"));
								json.put("HDD_PREV_RDG",rs.getString("HDD_PREV_RDG"));
								json.put("HDD_AVG_CONSMP",rs.getString("HDD_AVG_CONSMP"));
								json.put("HDD_SOLAR_REBATE",rs.getString("HDD_SOLAR_REBATE"));
								json.put("HDD_FL_REBATE",rs.getString("HDD_FL_REBATE"));
								json.put("HDD_PH_REBATE",rs.getString("HDD_PH_REBATE"));
								json.put("HDD_TELEP_REBATE",rs.getString("HDD_TELEP_REBATE"));
								json.put("HDD_PWR_FACTOR",rs.getString("HDD_PWR_FACTOR"));
								json.put("HDD_MTRCHNG_DT1",rs.getString("HDD_MTRCHNG_DT1"));
								json.put("HDD_MTRCHNG_RDG1",rs.getString("HDD_MTRCHNG_RDG1"));
								json.put("HDD_MTRCHN_GDT2",rs.getString("HDD_MTRCHN_GDT2"));
								json.put("HDD_MTRCHN_GRDG2",rs.getString("HDD_MTRCHN_GRDG2"));
								json.put("HDD_BILL_AMT",rs.getString("HDD_BILL_AMT"));
								json.put("HDD_DEMAND_ARREARS",rs.getString("HDD_DEMAND_ARREARS"));
								json.put("HDD_INT_ARRS_3MMD_DEP_INT",rs.getString("HDD_INT_ARRS_3MMD_DEP_INT"));
								json.put("HDD_TAX_ARREARS",rs.getString("HDD_TAX_ARREARS"));
								json.put("HDD_DELAY_INT",rs.getString("HDD_DELAY_INT"));
								json.put("HDD_AMT_PAID1",rs.getString("HDD_AMT_PAID1"));
								json.put("HDD_PAID_DATE1",rs.getString("HDD_PAID_DATE1"));
								json.put("HDD_AMT_PAID2",rs.getString("HDD_AMT_PAID2"));
								json.put("HDD_PAID_DATE2",rs.getString("HDD_PAID_DATE2"));
								json.put("HDD_OTHERS",rs.getString("HDD_OTHERS"));
								json.put("HDD_BILL_GEN_FLAG",rs.getString("HDD_BILL_GEN_FLAG"));
								json.put("HDD_REBATE_CAP",rs.getString("HDD_REBATE_CAP"));
								json.put("HDD_PREVIOUS_DEMAND1",rs.getString("HDD_PREVIOUS_DEMAND1"));
								json.put("HDD_PREVIOUS_DEMAND2",rs.getString("HDD_PREVIOUS_DEMAND2"));
								json.put("HDD_PREVIOUS_DEMAND3",rs.getString("HDD_PREVIOUS_DEMAND3"));
								json.put("HDD_BILLING_MODE",rs.getString("HDD_BILLING_MODE"));
								json.put("HDD_MTR_CONST",rs.getString("HDD_MTR_CONST"));
								json.put("HDD_DEMAND_BASED",rs.getString("HDD_DEMAND_BASED"));
								json.put("HDD_RURAL_REBATE",rs.getString("HDD_RURAL_REBATE"));
								json.put("HDD_NORMAL",rs.getString("HDD_NORMAL"));
								json.put("HDD_APPEAL_AMOUNT",rs.getString("HDD_APPEAL_AMOUNT"));
								json.put("HDD_INT_ON_APPEAL_AMT",rs.getString("HDD_INT_ON_APPEAL_AMT"));
								json.put("HDD_KVAH",rs.getString("HDD_KVAH"));
								json.put("HDD_METER_TVM",rs.getString("HDD_METER_TVM"));
								json.put("HDD_INST_TYP",rs.getString("HDD_INST_TYP"));
								json.put("HDD_SANCT_HP",rs.getString("HDD_SANCT_HP"));
								json.put("HDD_SANCT_KW",rs.getString("HDD_SANCT_KW"));
								json.put("HDD_BILL_DT",rs.getString("HDD_BILL_DT"));
								json.put("HDD_P_CREDIT",rs.getString("HDD_P_CREDIT"));
								json.put("HDD_P_DEBIT",rs.getString("HDD_P_DEBIT"));
								json.put("HDD_DUE_DATE",rs.getString("HDD_DUE_DATE"));
								json.put("HDD_IVRS_ID",rs.getString("HDD_IVRS_ID"));
								json.put("HDD_ECS_FLG",rs.getString("HDD_ECS_FLG"));
								json.put("HDD_PART_PERIOD",rs.getString("HDD_PART_PERIOD"));
								json.put("HDD_DLMNR",rs.getString("HDD_DLMNR"));
								json.put("HDD_TAX_FLG",rs.getString("HDD_TAX_FLG"));
								json.put("HDD_DC_FLG",rs.getString("HDD_DC_FLG"));
								json.put("HDD_INTRST_ARRS_HDD_DELAY_ARRS",rs.getString("HDD_INTRST_ARRS_HDD_DELAY_ARRS"));
								json.put("HDD_INTRST_TAX",rs.getString("HDD_INTRST_TAX"));
								json.put("HDD_FIRST_RDG_FLG",rs.getString("HDD_FIRST_RDG_FLG"));
								json.put("HDD_ERR_PRCNT",rs.getString("HDD_ERR_PRCNT"));
								json.put("HDD_MNR_FLG",rs.getString("HDD_MNR_FLG"));
								json.put("HDD_OLD_MTR_CONSMP",rs.getString("HDD_OLD_MTR_CONSMP"));
								json.put("HDD_INT_ON_TAX",rs.getString("HDD_INT_ON_TAX"));
								json.put("HDD_SUBDIV_NAME",rs.getString("HDD_SUBDIV_NAME"));
								json.put("HDD_NO_TAX_COMP",rs.getString("HDD_NO_TAX_COMP"));
								json.put("HDD_PREV_RDG_FLG",rs.getString("HDD_PREV_RDG_FLG"));
								json.put("HDD_PL_FLG",rs.getString("HDD_PL_FLG"));
								json.put("HDD_PREV_CKWH",rs.getString("HDD_PREV_CKWH"));
								json.put("HDD_REG_PENALTY",rs.getString("HDD_REG_PENALTY"));
								json.put("HDD_CALC_PF_FLG",rs.getString("HDD_CALC_PF_FLG"));
								json.put("HDD_LAST_DL_DAYS",rs.getString("HDD_LAST_DL_DAYS"));
								json.put("HDD_CUR_QRTR",rs.getString("HDD_CUR_QRTR"));
								json.put("HDD_FREQUENCY",rs.getString("HDD_FREQUENCY"));
								json.put("HDD_ANNUAL_MIN_FIX",rs.getString("HDD_ANNUAL_MIN_FIX"));
								json.put("HDD_CAPACITY_RBT",rs.getString("HDD_CAPACITY_RBT"));
								json.put("HDD_CHQ_DIS_FLG",rs.getString("HDD_CHQ_DIS_FLG"));
								json.put("HDD_ORPHANAGE_REBATE",rs.getString("HDD_ORPHANAGE_REBATE"));
								json.put("HDD_DEP_INT",rs.getString("HDD_DEP_INT"));
								json.put("HDD_MC_FLG",rs.getString("HDD_MC_FLG"));
								json.put("HDD_3MMD_DEP",rs.getString("HDD_3MMD_DEP"));
								
								json.put("HDD_NFC_CODE",rs.getString("HDD_NFC_CODE"));
								json.put("HDD_NFC_TAG",rs.getString("HDD_NFC_TAG"));
								
								customer_array.add(json);
							}
							
							DBManagerResourceRelease.close(ps);
							DBManagerResourceRelease.close(rs);
							
							jsonResponse.put("status", "success");
							jsonResponse.put("message", "Consumer Loaded Successfully ...!");
							jsonResponse.put("TARIFF_MAIN", trfmain_array);
							jsonResponse.put("TARIFF_FIX", trffix_array);
							jsonResponse.put("TARIFF_REBATE", trfrebate_array);
							jsonResponse.put("TARIFF_SLAB", trfslab_array);
							jsonResponse.put("CONSUMER_DATA", customer_array);
							
							
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
			jsonResponse.put("message", "Error In Retrieving Meter Reader Count.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps, dbConnection);
			//DatabaseImpl.CleanUp(con, ps, rs);
		}
		
		return jsonResponse;
	}*/

	@Override
	public JSONObject getRecordCountForTodays(JSONObject object) {
		// TODO Auto-generated method stub


		//Connection con = null;
				PreparedStatement ps = null;
				ResultSet rs = null;

				JSONObject jsonResponse = new JSONObject();
				String query = "";
				String RdgDay = "";
				
				try {
					if(!object.isEmpty() || !object.isNullObject()){
						
							String location_code = (String) object.get("location_code").toString().trim();
							String meter_code    = (String) object.get("meter_code").toString().trim();
							String reading_date = (String) object.get("meter_date").toString().trim();
							
							if((location_code.length() > 0 && location_code != null)){
								
								dbConnection = databaseObj.getDatabaseConnection();
								
								if(dbConnection != null){
									
									if(reading_date.length() > 0){
										String reading_date_split[] = reading_date.split("/");
										RdgDay = reading_date_split[0];
									}
									
									query =   " SELECT COUNT(9) CNT FROM  CUST_MASTER   "
											+ " WHERE  "
											+ " CM_MTR_RDR_CD = '"+meter_code+"' AND "
											+ " CM_MTR_RDG_DAY = "+RdgDay +" AND "
											+ " CM_CONSMR_STS IN ('1','9','10')  AND "
											+ " CM_RR_NO like '"+location_code+"%'";
									
									System.out.println(query);
									
									ps = dbConnection.prepareStatement(query);
									rs = ps.executeQuery();
									
									if (rs.next()) {
										
										if(rs.getInt("CNT") > 0){
											
											jsonResponse.put("status", "success");
											jsonResponse.put("message","Reading Exist. You Can Download Data . ");
											jsonResponse.put("record_count", rs.getInt("CNT"));
										}else{
											jsonResponse.put("status", "fail");
											jsonResponse.put("message","Today You Don't Have Reading");
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
					jsonResponse.put("message", "Error In Retrieving Meter Reader Count.");
					// TODO: handle exception
				}finally
				{
					DBManagerResourceRelease.close(rs, ps);
					//DatabaseImpl.CleanUp(con, ps, rs);
				}
				
				return jsonResponse;
	}

	@Override
	public JSONObject downloadDataToTable(JSONObject object) {
		// TODO Auto-generated method stub

		CallableStatement cs = null;
		ResultSet rs = null;

		JSONObject jsonResponse = new JSONObject();
		String RdgDay = "";
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){
				
					String location_code = (String) object.get("location_code").toString().trim();
					String meter_code    = (String) object.get("meter_code").toString().trim();
					String reading_date = (String) object.get("meter_date").toString().trim();
					
					if((location_code.length() > 0 && location_code != null)){
						
						dbConnection = databaseObj.getDatabaseConnection();
						
						if(dbConnection != null){
							
							if(reading_date.length() > 0){
								String reading_date_split[] = reading_date.split("/");
								RdgDay = reading_date_split[0];
							}
							
							cs = dbConnection.prepareCall("{CALL PKG_HHD.MOVE_TO_HHD(?,?,?,?,?,?)}");
							cs.setString(1,meter_code);
							cs.setString(2,"NADEEM");
							cs.setString(3,reading_date);
							cs.setString(4,"M");
							cs.setString(5,location_code);
							cs.registerOutParameter(6,java.sql.Types.VARCHAR);
							
							cs.execute();
							String StrOutSts = cs.getString(6);
							System.out.println("Output status : " + StrOutSts);
							
							jsonResponse.put("status", "sucesss");
							jsonResponse.put("message", "true");
							jsonResponse.put("output_status", StrOutSts);
							
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
			jsonResponse.put("message", "Error In Retrieving Meter Reader Count.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs);
			DBManagerResourceRelease.close(dbConnection);
			//DatabaseImpl.CleanUp(con, ps, rs);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject uploadsinglerecordfromjobscheduler(JSONObject object) {
		// TODO Auto-generated method stub
		
		JSONObject jsonResponse = new JSONObject();
		
		System.out.println("uploadsinglerecordfromjobscheduler : "+object);
		
		PreparedStatement ps = null;
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			
			String StrQueryInsert ="insert into HHD_UPLOAD_DATA "
					+ "(HUD_MTR_RDR_CD,"
					+ " HUD_RR_NO,"
					+ " HUD_BILL_NO,"
					+ " HUD_BILL_DT,"
					+ " HUD_MTR_RDG,"
					+ " HUD_MTR_RDG_STS,"
					+ " HUD_UNITS_CONSMP,"
					+ " HUD_FEC,"
					+ " HUD_TOT_DEMAND,"
					+ " HUD_TAX,"
					+ " HUD_REBATE,"
					+ " HUD_CR_AMT_FWD,"
					+ " HUD_BILLED_STS,"
					+ " HUD_FX_UN1,"
					+ " HUD_FX_RT1,"
					+ " HUD_FX_UN2,"
					+ " HUD_FX_RT2,"
					+ " HUD_FX_UN3,"
					+ " HUD_FX_RT3,"
					+ " HUD_FX_UN4,"
					+ " HUD_FX_RT4,"
					+ " HUD_FX_UN5,"
					+ " HUD_FX_RT5,"
					+ " HUD_EN_UN1,"
					+ " HUD_EN_RT1,"
					+ " HUD_EN_UN2,"
					+ " HUD_EN_RT2,"
					+ " HUD_EN_UN3,"
					+ " HUD_EN_RT3,"
					+ " HUD_EN_UN4,"
					+ " HUD_EN_RT4,"
					+ " HUD_EN_UN5,"
					+ " HUD_EN_RT5,"
					+ " HUD_EN_UN6,"
					+ " HUD_EN_RT6,"
					+ " HUD_DIFF_AMT,"
					+ " HUD_PF_PENALTY,"
					+ " HUD_PL_RBT,"
					+ " HUD_WKLY_MIN_AMT,"
					+ " HUD_PH_RBT,"
					+ " HUD_ACT_DL_CR,"
					+ " HUD_RR_REBATE,"
					+ " HUD_CAPACITY_RBT,"
					+ " HUD_ORPHANAGE_REBATE,"
					+ " HUD_USER, "
					+ " HUD_TMPSTP ) values "
					+ ""
					+ "('" + (String)object.get("KEY_READER_CODE") + "',"
					+ "'" + (String)object.get("KEY_LOCATION")+(String)object.get("KEY_RR_NO") + "',"
					+ "'" + (String)object.get("KEY_BILL_NO") + "',"
					+ "TO_DATE('" + ((String)object.get("KEY_BILL_DATE")).replace("-", "/") + "','DD/MM/YYYY'),"
					+ "'" + (String)object.get("PRESENT_RDG") + "',"
					+ "'" + (String)object.get("METER_STS") + "',"
					+ "'" + (String)object.get("N_UNITSCONSUMED") + "',"
					+ "'" + (String)object.get("TOTAL_FIXED_TARIFF") + "',"
					+ "'" + (String)object.get("P_NTOTAL_ENAERGY_TARIFF") + "',"
					+ "'" + (String)object.get("N_TAX") + "',"
					+ "" +   object.getDouble("N_REBATE") + ","
					+ "'" + (String)object.get("CREDIT_CF") + "',"
					+ "'" + (String)object.get("BILLPRINTED") + "',"
					+ "" + object.getLong("FIXED_UNIT_1") + ","
					+ "" + object.getLong("FIXED_RATE_1") + ","
					+ "" + object.getLong("FIXED_UNIT_2") + ","
					+ "" + object.getLong("FIXED_RATE_2") + ","
					+ "" + object.getLong("FIXED_UNIT_3") + ","
					+ "" + object.getLong("FIXED_RATE_3") + ","
					+ "" + object.getLong("FIXED_UNIT_4") + ","
					+ "" + object.getLong("FIXED_RATE_4") + ","
					+ "" + object.getLong("FIXED_UNIT_5") + ","
					+ "" + object.getLong("FIXED_RATE_5") + ","
					+ "" + object.getLong("ENERGY_UNIT_1") + ","
					+ "" + object.getLong("ENERGY_RATE_1") + ","
					+ "" + object.getLong("ENERGY_UNIT_2") + ","
					+ "" + object.getLong("ENERGY_RATE_2") + ","
					+ "" + object.getLong("ENERGY_UNIT_3") + ","
					+ "" + object.getLong("ENERGY_RATE_3") + ","
					+ "" + object.getLong("ENERGY_UNIT_4") + ","
					+ "" + object.getLong("ENERGY_RATE_4") + ","
					+ "" + object.getLong("ENERGY_UNIT_5") + ","
					+ "" + object.getLong("ENERGY_RATE_5") + ","
					+ "" + object.getLong("ENERGY_UNIT_6") + ","
					+ "" + object.getLong("ENERGY_RATE_6") + ","
					+ "'" + (String)object.get("DIFF_AMOUNT") + "',"
					+ "'" + (String)object.get("PF_PENALTY") + "',"
					+ "'" + (String)object.get("PL_REBATE") + "',"
					+ "'" + (String)object.get("PRSTCKWH") + "',"
					+ "'" + (String)object.get("PH_REBATE") + "',"
					+ "'" + (String)object.get("MORECLAIMED") + "',"
					+ "'" + (String)object.get("RR_REBATE") + "',"
					+ "'" + (String)object.get("CAPRBTAMT") + "',"
					+ "'" + (String)object.get("ORPHAN_AMOUNT") + "',"
					+ "'" + (String)object.get("USER_ID") + "',"
					+ "SYSDATE"
					+ ")";
			
			
			System.out.println("insert_image_query : "+StrQueryInsert);
			ps = dbConnection.prepareStatement(StrQueryInsert);
			int result = ps.executeUpdate();
			
			if(result > 0){
				
				ps.close();
				jsonResponse.put("status", "success");
				jsonResponse.put("message", "Row Inserted From Jobscheduler.");
				System.out.println("Row Inserted From Jobscheduler.");
			}else{
				jsonResponse.put("status", "fail");
				jsonResponse.put("message", "Row Not Inserted From Jobscheduler.");
				System.out.println("Row Not Inserted From Jobscheduler.");
			}
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonResponse.put("status", "fail");
		}finally {
			DBManagerResourceRelease.close(ps);
		}
		return jsonResponse;
	}
	
}
