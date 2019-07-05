package com.savbill.implementation;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import com.savbill.customer.CustomerMasterBO;
import com.savbill.database.DBManagerResourceRelease;
import com.savbill.util.DatabaseImpl;
import com.savbill.util.ReferenceUtil;

import net.sf.json.JSONObject;

public class ConsumerMasterImpl {

	public static JSONObject GetConsumerRRNoDetails(JSONObject object) {
		// TODO Auto-generated method stub
		JSONObject jsonResponse = new JSONObject();
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			if(object != null){
			
				String RRNo = object.get("rrNumber").toString().toUpperCase();
				String Location = object.get("location").toString().toUpperCase();
				String ConnType = object.get("conntype").toString().toUpperCase();
				
				if(RRNo !=  null){
					
					jsonResponse.put("status", "success");
					
					//Validate RR Number Is in Processing State or not.
					jsonResponse.put("process_status", ReferenceUtil.validateRRStatus(RRNo,Location,ConnType));
					
					//retrieve rr_number details.
					
					jsonResponse.put("rrno_detail", getConsumerMaster(RRNo,Location,ConnType));
					
				}else{
					jsonResponse.put("status", "failure");
					jsonResponse.put("message", "RR Number Doesn't Exits.Try Again.");
				}
				
			}else{
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
			
			
		} catch (Exception e) {
			// TODO: handle exception
			System.out.println("Error Occured : "+e.getMessage());
		}
		
		
		return jsonResponse;
	}

	private static CustomerMasterBO getConsumerMaster(String rRNo, String location, String ConnType) {
		// TODO Auto-generated method stub
		
		
		Connection con = null;
		PreparedStatement pr = null;
		ResultSet rs = null; 
		CustomerMasterBO consumerBO = null;
		
		try {
			if(ConnType.equals("LT") || ConnType == "LT"){
				con = DatabaseImpl.GetLTSQLConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				con = DatabaseImpl.GetHTSQLConnection();
			}
			
			String qry = "SELECT * FROM CUST_MASTER where CM_RR_NO= ? AND CM_RR_NO  LIKE '"+location+"%' ";
			pr = con.prepareStatement(qry);
			pr.setString(1, rRNo);

			rs = pr.executeQuery();
			while (rs.next()) {
				consumerBO = new CustomerMasterBO();
				
				if (rs.getString("CM_RR_NO") != null && rs.getString("CM_RR_NO").length()>10) {
					consumerBO.setCmRrNo(rs.getString("CM_RR_NO").substring(7));
				} 
				
				consumerBO.setCmServiceDt(ReferenceUtil.reverseConvert(rs.getString("CM_SERVICE_DT")));
				
				if (rs.getString("CM_OLD_RR_NO") != null && rs.getString("CM_OLD_RR_NO").length()>10) {
					consumerBO.setCmOldRrNo(rs.getString("CM_OLD_RR_NO").substring(7));
				} 
				consumerBO.setCmKebOfficeFlg(rs.getString("CM_KEB_OFFICE_FLG"));
				consumerBO.setCmPinCd(rs.getString("CM_PIN_CD"));
				consumerBO.setCmTelephoneNo(rs.getString("CM_TELEPHONE_NO"));
				consumerBO.setCmTalukCd(rs.getString("CM_TALUK_CD"));
				consumerBO.setCmTalukDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_TALUK_CD"), "TALUK",ConnType));
				if(rs.getString("CM_SERVICE_DT")!=null && rs.getString("CM_TMP_DURN")!=null ){
					consumerBO.setCmRenewdate(ReferenceUtil.getRenewDate(ReferenceUtil.reverseConvert(rs.getString("CM_SERVICE_DT")), rs.getString("CM_TMP_DURN"))); 
				}
				
				consumerBO.setCmDistrictCd(rs.getString("CM_DISTRICT_CD"));
				consumerBO.setCmDistrictDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_DISTRICT_CD"), "DISTRICT",ConnType));
				consumerBO.setCmStateConstituencyCd(rs.getString("CM_STATE_CONSTITUENCY_CD"));
				consumerBO.setCmStateConstituencyDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_STATE_CONSTITUENCY_CD"), "S_CONSTCY",ConnType));
				consumerBO.setCmCentralConstituencyCd(rs.getString("CM_CENTRAL_CONSTITUENCY_CD"));
				consumerBO.setCmCentralConstituencyDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_CENTRAL_CONSTITUENCY_CD"),"C_CONSTCY",ConnType));
				consumerBO.setCmRegionCd(rs.getString("CM_REGION_CD"));
				consumerBO.setCmRegionDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_REGION_CD"), "REG_TYP",ConnType));
				consumerBO.setCmPwrPurpose(ReferenceUtil.getPowerPurposeCodeTOName(rs.getString("CM_PWR_PURPOSE"),ConnType));
				consumerBO.setCmIndustryCd(ReferenceUtil.getIndustrialCodeTOName(rs.getString("CM_INDUSTRY_CD"),ConnType));
				consumerBO.setCmIndustryDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_INDUSTRY_CD"), "IND_PWR_CD",ConnType));
				consumerBO.setCmApplntTyp(rs.getString("CM_APPLNT_TYP"));
				consumerBO.setCmApplntTypDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_APPLNT_TYP"), "APLNT_TYP",ConnType));
				consumerBO.setCmPowerSanctNo(rs.getString("CM_POWER_SANCT_NO"));
				consumerBO.setCmPowerSanctDt(ReferenceUtil.reverseConvert(rs.getString("CM_POWER_SANCT_DT")));
				consumerBO.setCmPowerSanctAuth(ReferenceUtil.getPwrSanctionedCodeTOName(rs.getString("CM_POWER_SANCT_AUTH"),ConnType));
				
				if (rs.getString("CM_NXT_RR_NO") != null && rs.getString("CM_NXT_RR_NO").length()>10) {
					consumerBO.setCmNxtRrNo(rs.getString("CM_NXT_RR_NO").substring(7));
				} 
				consumerBO.setCmLdgrNo(rs.getString("CM_LDGR_NO"));
				consumerBO.setCmLdgrOpenedDt(ReferenceUtil.reverseConvert(rs.getString("CM_LDGR_OPENED_DT")));
				consumerBO.setCmFolioNo(rs.getString("CM_FOLIO_NO"));
				consumerBO.setCmMtrRdgCycle(rs.getString("CM_MTR_RDG_CYCLE"));
				consumerBO.setCmMtrRdgDay(rs.getString("CM_MTR_RDG_DAY"));
				consumerBO.setCmMtrRdrCd(rs.getString("CM_MTR_RDR_CD"));
				consumerBO.setCmMtrRdrDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_MTR_RDR_CD"), "IND_PWR_CD",ConnType));
				consumerBO.setCmTariffcatogory(ReferenceUtil.getTariffDescByTariffCode(rs.getString("CM_TRF_CATG"),ConnType));
				consumerBO.setCmStationNo(rs.getString("CM_STATION_NO"));
				consumerBO.setCmStationName(ReferenceUtil.getStationName(rs.getString("CM_STATION_NO"),ConnType));
				consumerBO.setCmFdrNo(rs.getString("CM_FDR_NO"));
				consumerBO.setCmFdrName(ReferenceUtil.getFeederName(rs.getString("CM_FDR_NO"),rs.getString("CM_STATION_NO"),ConnType));
				consumerBO.setCmLineMin(rs.getString("CM_LINE_MIN"));
				consumerBO.setCmOmUnitCd(ReferenceUtil.getOMUnitCodeName(rs.getString("CM_OM_UNIT_CD"),ConnType));
				consumerBO.setCmOmUnitCdName(rs.getString("CM_OM_UNIT_CD"));
				consumerBO.setCmTrsfmrNo(rs.getString("CM_TRSFMR_NO"));
				consumerBO.setCmTrsfmrName(ReferenceUtil.getTransformerName(rs.getString("CM_TRSFMR_NO"),rs.getString("CM_FDR_NO"),rs.getString("CM_STATION_NO"),ConnType));
				consumerBO.setCmAvgConsmp(rs.getString("CM_AVG_CONSMP"));
				consumerBO.setCmFlConsumer(rs.getString("CM_FL_CONSUMER"));
				consumerBO.setCmPwrCutExemptFlg(rs.getString("CM_PWR_CUT_EXEMPT_FLG"));
				consumerBO.setCmConsmrSts(rs.getString("CM_CONSMR_STS"));
				consumerBO.setCmConsmrStsDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_CONSMR_STS"), "CONN_STS",ConnType));
				consumerBO.setCmChqBounceFlg(rs.getString("CM_CHQ_BOUNCE_FLG"));
				consumerBO.setCmDlCount(rs.getString("CM_DL_COUNT"));
				consumerBO.setCmDlSent(rs.getString("CM_DL_SENT"));
				consumerBO.setCmTotMmd(rs.getString("CM_TOT_MMD"));
				consumerBO.setCmLastMmdDt(rs.getString("CM_LAST_MMD_DT"));
				consumerBO.setCm100DmdEntlmnt(rs.getString("CM_100_ENERGY_ENTLMNT"));
				consumerBO.setCmTmpFolioNo(rs.getString("CM_TMP_FOLIO_NO"));
				consumerBO.setCm100DmdEntlmnt(rs.getString("CM_100_DMD_ENTLMNT"));
				consumerBO.setCmTrfCatg(rs.getString("CM_TRF_CATG"));
				consumerBO.setCmTrfCatgDescription(ReferenceUtil.getTariffDescByTariffCode(rs.getString("CM_TRF_CATG"),ConnType));
				consumerBO.setCmTrfEffectFrmDt(ReferenceUtil.reverseConvert(rs.getString("CM_TRF_EFFECT_FRM_DT")));
				consumerBO.setCmLdEffectFrmDt(ReferenceUtil.reverseConvert(rs.getString("CM_LD_EFFECT_FRM_DT")));
				consumerBO.setCmLdSanctHp(rs.getString("CM_LD_SANCT_HP"));
				consumerBO.setCmLdSanctKva(rs.getString("CM_LD_SANCT_KVA"));
				consumerBO.setCmLdSanctKw(rs.getString("CM_LD_SANCT_KW"));
				consumerBO.setCmLightLoad(rs.getString("CM_LIGHT_LOAD"));
				consumerBO.setCmHeatLoad(rs.getString("CM_HEAT_LOAD"));
				consumerBO.setCmMotivePower(rs.getString("CM_MOTIVE_POWER"));
				consumerBO.setCmSupplyVolt(rs.getString("CM_SUPPLY_VOLT"));
				consumerBO.setCmPurgeFlg(rs.getString("CM_PURGE_FLG"));
				consumerBO.setCmInstallTyp(rs.getString("CM_INSTALL_TYP"));

				consumerBO.setCmInstallTypDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_INSTALL_TYP"), "INSTL_TYP",ConnType));
				consumerBO.setCmMeteredFlg(rs.getString("CM_METERED_FLG"));
				consumerBO.setCmTlNo(rs.getString("CM_TL_NO"));
				consumerBO.setCmCapacitorCap(rs.getString("CM_CAPACITOR_CAP"));
				consumerBO.setCmStarterTyp(ReferenceUtil.getStarterTypeCodeTOName(rs.getString("CM_STARTER_TYP"),ConnType));
				consumerBO.setCmPremisJuris(ReferenceUtil.getJurisdictionTypeCodeTOName(rs.getString("CM_PREMIS_JURIS"),ConnType));
				consumerBO.setCmWellTyp(ReferenceUtil.getWellTypeCodeTOName(rs.getString("CM_WELL_TYP"),ConnType));
				consumerBO.setCmLightingTyp(ReferenceUtil.getLightingCodeTOName(rs.getString("CM_LIGHTING_TYP"),ConnType));
				consumerBO.setCmConnLdHp(rs.getString("CM_CONN_LD_HP"));
				consumerBO.setCmConnLdKw(rs.getString("CM_CONN_LD_KW"));
				consumerBO.setCmSupplyEffFrmDt(ReferenceUtil.reverseConvert(rs.getString("CM_SUPPLY_EFF_FRM_DT")));
				consumerBO.setCmPhaseOfInstln(rs.getString("CM_PHASE_OF_INSTLN"));
				consumerBO.setCmTaxExemptFlg(rs.getString("CM_TAX_EXEMPT_FLG"));
				consumerBO.setCmUnauthFlg(rs.getString("CM_UNAUTH_FLG"));
				consumerBO.setCmPwrPurEffFrmDt(ReferenceUtil.reverseConvert(rs.getString("CM_PWR_PUR_EFF_FRM_DT")));
				consumerBO.setCmIndEffFrmDt(ReferenceUtil.reverseConvert(rs.getString("CM_IND_EFF_FRM_DT")));
				consumerBO.setCmConnTyp(ReferenceUtil.getConnectionTypeCodeTOName(rs.getString("CM_CONN_TYP"),ConnType));
				consumerBO.setCmNtrLocCd(rs.getString("CM_NTR_LOC_CD"));
				consumerBO.setCmBjkjOutlet(rs.getString("CM_BJKJ_OUTLET"));
				consumerBO.setCmMinDemandEntl(rs.getString("CM_MIN_DEMAND_ENTL"));
				consumerBO.setCmBulkNoOfHouses(rs.getString("CM_BULK_NO_OF_HOUSES"));
				consumerBO.setCmIvrsId(rs.getString("CM_IVRS_ID"));
				consumerBO.setCmUser(rs.getString("CM_USER"));
				consumerBO.setCmTmpstp(ReferenceUtil.getTimestampe(rRNo,ConnType)); 
				consumerBO.setCmPoleNo(rs.getString("CM_POLE_NO"));
				consumerBO.setCmBegMnth(rs.getString("CM_BEG_MNTH"));
				consumerBO.setCmTmpDurn(rs.getString("CM_TMP_DURN"));
				consumerBO.setCmFirstBillDcFlg(rs.getString("CM_FIRST_BILL_DC_FLG"));
				consumerBO.setCmRmks(rs.getString("CM_RMKS"));
				consumerBO.setCmSubmeterFlg(rs.getString("CM_SUBMETER_FLG"));
				consumerBO.setCmDblMtrFlg(rs.getString("CM_DBL_MTR_FLG"));
				consumerBO.setCmSlumFlg(rs.getString("CM_SLUM_FLG"));
				consumerBO.setCmEmailId(rs.getString("CM_EMAIL_ID"));
				consumerBO.setCmCstNo(rs.getString("CM_CST_NO"));
				consumerBO.setCmKstNo(rs.getString("CM_KST_NO"));
				consumerBO.setCmTinNo(rs.getString("CM_TIN_NO"));
				consumerBO.setCmTodMeterFlag(rs.getString("CM_TOD_METER"));
				consumerBO.setCmTalukDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_TALUK_CD"), "TALUK",ConnType));
				consumerBO.setCmRenewdateExtends(ReferenceUtil.getRenewDateExtends());
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown " + e);
		} finally {
			//DatabaseImpl.CleanUp(con, pr, rs);
			DBManagerResourceRelease.close(rs, pr);
		}
		return consumerBO;
	}
	
	
	
	

}
