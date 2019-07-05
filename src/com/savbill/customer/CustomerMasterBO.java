package com.savbill.customer;

import java.io.Serializable;

public class CustomerMasterBO implements Serializable {
	
	private String location;
	private String cmRrNo;
	private String cmServiceDt;
	private String cmOldRrNo;
	private String cmKebOfficeFlg;
	private String cmPinCd;
	private String cmTelephoneNo;
	private String cmTalukCd;
	private String cmTalukDescription;
	private String cmTariffcatogory;
	private String cmDistrictCd;
	private String cmDistrictDescription;
	private String cmLoanAmount;
	private String cmStateConstituencyCd;
	private String cmStateConstituencyDescription;
	private String cmRenewdate;
	private String cmRenewdateExtends;
	private String cmCentralConstituencyCd;
	private String cmCentralConstituencyDescription;
	private String cmTodMeterFlag;
	private String cmRegionCd;
	private String cmRegionDescription;
	
	private String cmPwrPurpose;
	
	private String cmIndustryCd;
	private String cmIndustryDescription;
	
	private String cmApplntTyp;
	private String cmApplntTypDescription;
	
	private String cmPowerSanctNo;
	private String cmPowerSanctDt;
	private String cmPowerSanctAuth;
	private String cmNxtRrNo;
	private String cmLdgrNo;
	private String cmLdgrOpenedDt;
	private String cmFolioNo;
	private String cmMtrRdgCycle;
	private String cmMtrRdgDay;
	private String cmNewMtrRdgDay;
	private String cmMtrRdrCd;
	private String cmNewMtrRdrCd;	
	private String cmMtrRdrDescription;
	private String cmStationNo;
	private String cmStationName;
	private String cmFdrNo;
	private String cmFdrName;
	private String cmLineMin;
	private String cmOmUnitCd;
	private String cmOmUnitCdName;
	
	private String cmTrsfmrNo;
	private String cmTrsfmrName;
	private String cmAvgConsmp;
	private String cmFlConsumer;
	private String cmPwrCutExemptFlg;
	private String cmConsmrSts;
	private String cmConsmrStsDescription;
	private String cmChqBounceFlg;
	private String cmDlCount;
	private String cmDlSent;
	private String cmTotMmd;
	private String cmLastMmdDt;
	private String cm100EnergyEntlmnt;
	private String cmTmpFolioNo;
	private String cm100DmdEntlmnt;
	private String cmTrfCatg;
	private String cmTrfCatgDescription;
	private String cmTrfEffectFrmDt;
	private String cmLdEffectFrmDt;
	private String cmLdSanctKw;
	private String cmLdSanctHp;
	private String cmLdSanctKva;
	private String cmLightLoad;
	private String cmHeatLoad;
	private String cmMotivePower;
	private String cmSupplyVolt;
	private String cmPurgeFlg;
	private String cmInstallTyp;
	private String cmInstallTypDescription;
	private String cmMeteredFlg;
	private String cmTlNo;
	private String    cmCapacitorCap;
	private String cmStarterTyp;
	private String cmPremisJuris;
	private String cmWellTyp;
	private String cmLightingTyp;
	private String cmConnLdHp;
	private String cmConnLdKw;
	private String cmSupplyEffFrmDt;
	private String cmPhaseOfInstln;
	private String cmTaxExemptFlg;
	private String cmUnauthFlg;
	private String cmPwrPurEffFrmDt;
	private String cmIndEffFrmDt;
	private String cmConnTyp;
	private String cmNtrLocCd;
	private String cmBjkjOutlet;
	private String cmMinDemandEntl;
	private String cmBulkNoOfHouses;
	private String cmIvrsId;
	private String cmUser;
	private String cmTmpstp;
	//private Timestamp fullTmpstp;
	private String cmPoleNo;
	private String cmBegMnth;
	private String cmTmpDurn;
	private String cmFirstBillDcFlg;
	private String cmRmks;
	private String cmSubmeterFlg;
	private String cmDblMtrFlg;
	private String cmSlumFlg;
	private String cmEmailId;
	private String cmCstNo;
	private String cmKstNo;
	private String cmTinNo;
	private String cmTransmissionLineNo;
	
	private String spotFolioNo;
	private String toFolioNo;
	private String dtcConnLoad;
	private String dtcCapacity ;
	private String dtc;
	private String dtcLoad;
	private String noOfdays;
	private String cmFullRRno;
	private String cmRowNum;
	private String CM_VERSION;
	
	private String DTC_CAP;
	private String DTC_CONN_LD;
	private String DTC_CONN_PER;
	
	
	public String getDTC_CAP() {
		return DTC_CAP;
	}
	public void setDTC_CAP(String dTC_CAP) {
		DTC_CAP = dTC_CAP;
	}
	public String getDTC_CONN_LD() {
		return DTC_CONN_LD;
	}
	public void setDTC_CONN_LD(String dTC_CONN_LD) {
		DTC_CONN_LD = dTC_CONN_LD;
	}
	public String getDTC_CONN_PER() {
		return DTC_CONN_PER;
	}
	public void setDTC_CONN_PER(String dTC_CONN_PER) {
		DTC_CONN_PER = dTC_CONN_PER;
	}
	public String getLocation() {
		return location;
	}
	public void setLocation(String location) {
		this.location = location;
	}
	public String getCmRrNo() {
		return cmRrNo;
	}
	public void setCmRrNo(String cmRrNo) {
		this.cmRrNo = cmRrNo;
	}
	public String getCmServiceDt() {
		return cmServiceDt;
	}
	public void setCmServiceDt(String cmServiceDt) {
		this.cmServiceDt = cmServiceDt;
	}
	public String getCmOldRrNo() {
		return cmOldRrNo;
	}
	public void setCmOldRrNo(String cmOldRrNo) {
		this.cmOldRrNo = cmOldRrNo;
	}
	public String getCmKebOfficeFlg() {
		return cmKebOfficeFlg;
	}
	public void setCmKebOfficeFlg(String cmKebOfficeFlg) {
		this.cmKebOfficeFlg = cmKebOfficeFlg;
	}
	public String getCmPinCd() {
		return cmPinCd;
	}
	public void setCmPinCd(String cmPinCd) {
		this.cmPinCd = cmPinCd;
	}
	public String getCmTelephoneNo() {
		return cmTelephoneNo;
	}
	public void setCmTelephoneNo(String cmTelephoneNo) {
		this.cmTelephoneNo = cmTelephoneNo;
	}
	public String getCmTalukCd() {
		return cmTalukCd;
	}
	public void setCmTalukCd(String cmTalukCd) {
		this.cmTalukCd = cmTalukCd;
	}
	public String getCmTalukDescription() {
		return cmTalukDescription;
	}
	public void setCmTalukDescription(String cmTalukDescription) {
		this.cmTalukDescription = cmTalukDescription;
	}
	public String getCmTariffcatogory() {
		return cmTariffcatogory;
	}
	public void setCmTariffcatogory(String cmTariffcatogory) {
		this.cmTariffcatogory = cmTariffcatogory;
	}
	public String getCmDistrictCd() {
		return cmDistrictCd;
	}
	public void setCmDistrictCd(String cmDistrictCd) {
		this.cmDistrictCd = cmDistrictCd;
	}
	public String getCmDistrictDescription() {
		return cmDistrictDescription;
	}
	public void setCmDistrictDescription(String cmDistrictDescription) {
		this.cmDistrictDescription = cmDistrictDescription;
	}
	public String getCmLoanAmount() {
		return cmLoanAmount;
	}
	public void setCmLoanAmount(String cmLoanAmount) {
		this.cmLoanAmount = cmLoanAmount;
	}
	public String getCmStateConstituencyCd() {
		return cmStateConstituencyCd;
	}
	public void setCmStateConstituencyCd(String cmStateConstituencyCd) {
		this.cmStateConstituencyCd = cmStateConstituencyCd;
	}
	public String getCmStateConstituencyDescription() {
		return cmStateConstituencyDescription;
	}
	public void setCmStateConstituencyDescription(
			String cmStateConstituencyDescription) {
		this.cmStateConstituencyDescription = cmStateConstituencyDescription;
	}
	public String getCmRenewdate() {
		return cmRenewdate;
	}
	public void setCmRenewdate(String cmRenewdate) {
		this.cmRenewdate = cmRenewdate;
	}
	public String getCmRenewdateExtends() {
		return cmRenewdateExtends;
	}
	public void setCmRenewdateExtends(String cmRenewdateExtends) {
		this.cmRenewdateExtends = cmRenewdateExtends;
	}
	public String getCmCentralConstituencyCd() {
		return cmCentralConstituencyCd;
	}
	public void setCmCentralConstituencyCd(String cmCentralConstituencyCd) {
		this.cmCentralConstituencyCd = cmCentralConstituencyCd;
	}
	public String getCmCentralConstituencyDescription() {
		return cmCentralConstituencyDescription;
	}
	public void setCmCentralConstituencyDescription(
			String cmCentralConstituencyDescription) {
		this.cmCentralConstituencyDescription = cmCentralConstituencyDescription;
	}
	public String getCmTodMeterFlag() {
		return cmTodMeterFlag;
	}
	public void setCmTodMeterFlag(String cmTodMeterFlag) {
		this.cmTodMeterFlag = cmTodMeterFlag;
	}
	public String getCmRegionCd() {
		return cmRegionCd;
	}
	public void setCmRegionCd(String cmRegionCd) {
		this.cmRegionCd = cmRegionCd;
	}
	public String getCmRegionDescription() {
		return cmRegionDescription;
	}
	public void setCmRegionDescription(String cmRegionDescription) {
		this.cmRegionDescription = cmRegionDescription;
	}
	public String getCmPwrPurpose() {
		return cmPwrPurpose;
	}
	public void setCmPwrPurpose(String cmPwrPurpose) {
		this.cmPwrPurpose = cmPwrPurpose;
	}
	public String getCmIndustryCd() {
		return cmIndustryCd;
	}
	public void setCmIndustryCd(String cmIndustryCd) {
		this.cmIndustryCd = cmIndustryCd;
	}
	public String getCmIndustryDescription() {
		return cmIndustryDescription;
	}
	public void setCmIndustryDescription(String cmIndustryDescription) {
		this.cmIndustryDescription = cmIndustryDescription;
	}
	public String getCmApplntTyp() {
		return cmApplntTyp;
	}
	public void setCmApplntTyp(String cmApplntTyp) {
		this.cmApplntTyp = cmApplntTyp;
	}
	public String getCmApplntTypDescription() {
		return cmApplntTypDescription;
	}
	public void setCmApplntTypDescription(String cmApplntTypDescription) {
		this.cmApplntTypDescription = cmApplntTypDescription;
	}
	public String getCmPowerSanctNo() {
		return cmPowerSanctNo;
	}
	public void setCmPowerSanctNo(String cmPowerSanctNo) {
		this.cmPowerSanctNo = cmPowerSanctNo;
	}
	public String getCmPowerSanctDt() {
		return cmPowerSanctDt;
	}
	public void setCmPowerSanctDt(String cmPowerSanctDt) {
		this.cmPowerSanctDt = cmPowerSanctDt;
	}
	public String getCmPowerSanctAuth() {
		return cmPowerSanctAuth;
	}
	public void setCmPowerSanctAuth(String cmPowerSanctAuth) {
		this.cmPowerSanctAuth = cmPowerSanctAuth;
	}
	public String getCmNxtRrNo() {
		return cmNxtRrNo;
	}
	public void setCmNxtRrNo(String cmNxtRrNo) {
		this.cmNxtRrNo = cmNxtRrNo;
	}
	public String getCmLdgrNo() {
		return cmLdgrNo;
	}
	public void setCmLdgrNo(String cmLdgrNo) {
		this.cmLdgrNo = cmLdgrNo;
	}
	public String getCmLdgrOpenedDt() {
		return cmLdgrOpenedDt;
	}
	public void setCmLdgrOpenedDt(String cmLdgrOpenedDt) {
		this.cmLdgrOpenedDt = cmLdgrOpenedDt;
	}
	public String getCmFolioNo() {
		return cmFolioNo;
	}
	public void setCmFolioNo(String cmFolioNo) {
		this.cmFolioNo = cmFolioNo;
	}
	public String getCmMtrRdgCycle() {
		return cmMtrRdgCycle;
	}
	public void setCmMtrRdgCycle(String cmMtrRdgCycle) {
		this.cmMtrRdgCycle = cmMtrRdgCycle;
	}
	public String getCmMtrRdgDay() {
		return cmMtrRdgDay;
	}
	public void setCmMtrRdgDay(String cmMtrRdgDay) {
		this.cmMtrRdgDay = cmMtrRdgDay;
	}
	public String getCmNewMtrRdgDay() {
		return cmNewMtrRdgDay;
	}
	public void setCmNewMtrRdgDay(String cmNewMtrRdgDay) {
		this.cmNewMtrRdgDay = cmNewMtrRdgDay;
	}
	public String getCmMtrRdrCd() {
		return cmMtrRdrCd;
	}
	public void setCmMtrRdrCd(String cmMtrRdrCd) {
		this.cmMtrRdrCd = cmMtrRdrCd;
	}
	public String getCmNewMtrRdrCd() {
		return cmNewMtrRdrCd;
	}
	public void setCmNewMtrRdrCd(String cmNewMtrRdrCd) {
		this.cmNewMtrRdrCd = cmNewMtrRdrCd;
	}
	public String getCmMtrRdrDescription() {
		return cmMtrRdrDescription;
	}
	public void setCmMtrRdrDescription(String cmMtrRdrDescription) {
		this.cmMtrRdrDescription = cmMtrRdrDescription;
	}
	public String getCmStationNo() {
		return cmStationNo;
	}
	public void setCmStationNo(String cmStationNo) {
		this.cmStationNo = cmStationNo;
	}
	public String getCmStationName() {
		return cmStationName;
	}
	public void setCmStationName(String cmStationName) {
		this.cmStationName = cmStationName;
	}
	public String getCmFdrNo() {
		return cmFdrNo;
	}
	public void setCmFdrNo(String cmFdrNo) {
		this.cmFdrNo = cmFdrNo;
	}
	public String getCmFdrName() {
		return cmFdrName;
	}
	public void setCmFdrName(String cmFdrName) {
		this.cmFdrName = cmFdrName;
	}
	public String getCmLineMin() {
		return cmLineMin;
	}
	public void setCmLineMin(String cmLineMin) {
		this.cmLineMin = cmLineMin;
	}
	public String getCmOmUnitCd() {
		return cmOmUnitCd;
	}
	public void setCmOmUnitCd(String cmOmUnitCd) {
		this.cmOmUnitCd = cmOmUnitCd;
	}
	public String getCmOmUnitCdName() {
		return cmOmUnitCdName;
	}
	public void setCmOmUnitCdName(String cmOmUnitCdName) {
		this.cmOmUnitCdName = cmOmUnitCdName;
	}
	public String getCmTrsfmrNo() {
		return cmTrsfmrNo;
	}
	public void setCmTrsfmrNo(String cmTrsfmrNo) {
		this.cmTrsfmrNo = cmTrsfmrNo;
	}
	public String getCmTrsfmrName() {
		return cmTrsfmrName;
	}
	public void setCmTrsfmrName(String cmTrsfmrName) {
		this.cmTrsfmrName = cmTrsfmrName;
	}
	public String getCmAvgConsmp() {
		return cmAvgConsmp;
	}
	public void setCmAvgConsmp(String cmAvgConsmp) {
		this.cmAvgConsmp = cmAvgConsmp;
	}
	public String getCmFlConsumer() {
		return cmFlConsumer;
	}
	public void setCmFlConsumer(String cmFlConsumer) {
		this.cmFlConsumer = cmFlConsumer;
	}
	public String getCmPwrCutExemptFlg() {
		return cmPwrCutExemptFlg;
	}
	public void setCmPwrCutExemptFlg(String cmPwrCutExemptFlg) {
		this.cmPwrCutExemptFlg = cmPwrCutExemptFlg;
	}
	public String getCmConsmrSts() {
		return cmConsmrSts;
	}
	public void setCmConsmrSts(String cmConsmrSts) {
		this.cmConsmrSts = cmConsmrSts;
	}
	public String getCmConsmrStsDescription() {
		return cmConsmrStsDescription;
	}
	public void setCmConsmrStsDescription(String cmConsmrStsDescription) {
		this.cmConsmrStsDescription = cmConsmrStsDescription;
	}
	public String getCmChqBounceFlg() {
		return cmChqBounceFlg;
	}
	public void setCmChqBounceFlg(String cmChqBounceFlg) {
		this.cmChqBounceFlg = cmChqBounceFlg;
	}
	public String getCmDlCount() {
		return cmDlCount;
	}
	public void setCmDlCount(String cmDlCount) {
		this.cmDlCount = cmDlCount;
	}
	public String getCmDlSent() {
		return cmDlSent;
	}
	public void setCmDlSent(String cmDlSent) {
		this.cmDlSent = cmDlSent;
	}
	public String getCmTotMmd() {
		return cmTotMmd;
	}
	public void setCmTotMmd(String cmTotMmd) {
		this.cmTotMmd = cmTotMmd;
	}
	public String getCmLastMmdDt() {
		return cmLastMmdDt;
	}
	public void setCmLastMmdDt(String cmLastMmdDt) {
		this.cmLastMmdDt = cmLastMmdDt;
	}
	public String getCm100EnergyEntlmnt() {
		return cm100EnergyEntlmnt;
	}
	public void setCm100EnergyEntlmnt(String cm100EnergyEntlmnt) {
		this.cm100EnergyEntlmnt = cm100EnergyEntlmnt;
	}
	public String getCmTmpFolioNo() {
		return cmTmpFolioNo;
	}
	public void setCmTmpFolioNo(String cmTmpFolioNo) {
		this.cmTmpFolioNo = cmTmpFolioNo;
	}
	public String getCm100DmdEntlmnt() {
		return cm100DmdEntlmnt;
	}
	public void setCm100DmdEntlmnt(String cm100DmdEntlmnt) {
		this.cm100DmdEntlmnt = cm100DmdEntlmnt;
	}
	public String getCmTrfCatg() {
		return cmTrfCatg;
	}
	public void setCmTrfCatg(String cmTrfCatg) {
		this.cmTrfCatg = cmTrfCatg;
	}
	public String getCmTrfCatgDescription() {
		return cmTrfCatgDescription;
	}
	public void setCmTrfCatgDescription(String cmTrfCatgDescription) {
		this.cmTrfCatgDescription = cmTrfCatgDescription;
	}
	public String getCmTrfEffectFrmDt() {
		return cmTrfEffectFrmDt;
	}
	public void setCmTrfEffectFrmDt(String cmTrfEffectFrmDt) {
		this.cmTrfEffectFrmDt = cmTrfEffectFrmDt;
	}
	public String getCmLdEffectFrmDt() {
		return cmLdEffectFrmDt;
	}
	public void setCmLdEffectFrmDt(String cmLdEffectFrmDt) {
		this.cmLdEffectFrmDt = cmLdEffectFrmDt;
	}
	public String getCmLdSanctKw() {
		return cmLdSanctKw;
	}
	public void setCmLdSanctKw(String cmLdSanctKw) {
		this.cmLdSanctKw = cmLdSanctKw;
	}
	public String getCmLdSanctHp() {
		return cmLdSanctHp;
	}
	public void setCmLdSanctHp(String cmLdSanctHp) {
		this.cmLdSanctHp = cmLdSanctHp;
	}
	public String getCmLdSanctKva() {
		return cmLdSanctKva;
	}
	public void setCmLdSanctKva(String cmLdSanctKva) {
		this.cmLdSanctKva = cmLdSanctKva;
	}
	public String getCmLightLoad() {
		return cmLightLoad;
	}
	public void setCmLightLoad(String cmLightLoad) {
		this.cmLightLoad = cmLightLoad;
	}
	public String getCmHeatLoad() {
		return cmHeatLoad;
	}
	public void setCmHeatLoad(String cmHeatLoad) {
		this.cmHeatLoad = cmHeatLoad;
	}
	public String getCmMotivePower() {
		return cmMotivePower;
	}
	public void setCmMotivePower(String cmMotivePower) {
		this.cmMotivePower = cmMotivePower;
	}
	public String getCmSupplyVolt() {
		return cmSupplyVolt;
	}
	public void setCmSupplyVolt(String cmSupplyVolt) {
		this.cmSupplyVolt = cmSupplyVolt;
	}
	public String getCmPurgeFlg() {
		return cmPurgeFlg;
	}
	public void setCmPurgeFlg(String cmPurgeFlg) {
		this.cmPurgeFlg = cmPurgeFlg;
	}
	public String getCmInstallTyp() {
		return cmInstallTyp;
	}
	public void setCmInstallTyp(String cmInstallTyp) {
		this.cmInstallTyp = cmInstallTyp;
	}
	public String getCmInstallTypDescription() {
		return cmInstallTypDescription;
	}
	public void setCmInstallTypDescription(String cmInstallTypDescription) {
		this.cmInstallTypDescription = cmInstallTypDescription;
	}
	public String getCmMeteredFlg() {
		return cmMeteredFlg;
	}
	public void setCmMeteredFlg(String cmMeteredFlg) {
		this.cmMeteredFlg = cmMeteredFlg;
	}
	public String getCmTlNo() {
		return cmTlNo;
	}
	public void setCmTlNo(String cmTlNo) {
		this.cmTlNo = cmTlNo;
	}
	public String getCmCapacitorCap() {
		return cmCapacitorCap;
	}
	public void setCmCapacitorCap(String cmCapacitorCap) {
		this.cmCapacitorCap = cmCapacitorCap;
	}
	public String getCmStarterTyp() {
		return cmStarterTyp;
	}
	public void setCmStarterTyp(String cmStarterTyp) {
		this.cmStarterTyp = cmStarterTyp;
	}
	public String getCmPremisJuris() {
		return cmPremisJuris;
	}
	public void setCmPremisJuris(String cmPremisJuris) {
		this.cmPremisJuris = cmPremisJuris;
	}
	public String getCmWellTyp() {
		return cmWellTyp;
	}
	public void setCmWellTyp(String cmWellTyp) {
		this.cmWellTyp = cmWellTyp;
	}
	public String getCmLightingTyp() {
		return cmLightingTyp;
	}
	public void setCmLightingTyp(String cmLightingTyp) {
		this.cmLightingTyp = cmLightingTyp;
	}
	public String getCmConnLdHp() {
		return cmConnLdHp;
	}
	public void setCmConnLdHp(String cmConnLdHp) {
		this.cmConnLdHp = cmConnLdHp;
	}
	public String getCmConnLdKw() {
		return cmConnLdKw;
	}
	public void setCmConnLdKw(String cmConnLdKw) {
		this.cmConnLdKw = cmConnLdKw;
	}
	public String getCmSupplyEffFrmDt() {
		return cmSupplyEffFrmDt;
	}
	public void setCmSupplyEffFrmDt(String cmSupplyEffFrmDt) {
		this.cmSupplyEffFrmDt = cmSupplyEffFrmDt;
	}
	public String getCmPhaseOfInstln() {
		return cmPhaseOfInstln;
	}
	public void setCmPhaseOfInstln(String  cmPhaseOfInstln) {
		this.cmPhaseOfInstln = cmPhaseOfInstln;
	}
	public String getCmTaxExemptFlg() {
		return cmTaxExemptFlg;
	}
	public void setCmTaxExemptFlg(String cmTaxExemptFlg) {
		this.cmTaxExemptFlg = cmTaxExemptFlg;
	}
	public String getCmUnauthFlg() {
		return cmUnauthFlg;
	}
	public void setCmUnauthFlg(String cmUnauthFlg) {
		this.cmUnauthFlg = cmUnauthFlg;
	}
	public String getCmPwrPurEffFrmDt() {
		return cmPwrPurEffFrmDt;
	}
	public void setCmPwrPurEffFrmDt(String cmPwrPurEffFrmDt) {
		this.cmPwrPurEffFrmDt = cmPwrPurEffFrmDt;
	}
	public String getCmIndEffFrmDt() {
		return cmIndEffFrmDt;
	}
	public void setCmIndEffFrmDt(String cmIndEffFrmDt) {
		this.cmIndEffFrmDt = cmIndEffFrmDt;
	}
	public String getCmConnTyp() {
		return cmConnTyp;
	}
	public void setCmConnTyp(String cmConnTyp) {
		this.cmConnTyp = cmConnTyp;
	}
	public String getCmNtrLocCd() {
		return cmNtrLocCd;
	}
	public void setCmNtrLocCd(String cmNtrLocCd) {
		this.cmNtrLocCd = cmNtrLocCd;
	}
	public String getCmBjkjOutlet() {
		return cmBjkjOutlet;
	}
	public void setCmBjkjOutlet(String cmBjkjOutlet) {
		this.cmBjkjOutlet = cmBjkjOutlet;
	}
	public String getCmMinDemandEntl() {
		return cmMinDemandEntl;
	}
	public void setCmMinDemandEntl(String cmMinDemandEntl) {
		this.cmMinDemandEntl = cmMinDemandEntl;
	}
	public String getCmBulkNoOfHouses() {
		return cmBulkNoOfHouses;
	}
	public void setCmBulkNoOfHouses(String cmBulkNoOfHouses) {
		this.cmBulkNoOfHouses = cmBulkNoOfHouses;
	}
	public String getCmIvrsId() {
		return cmIvrsId;
	}
	public void setCmIvrsId(String cmIvrsId) {
		this.cmIvrsId = cmIvrsId;
	}
	public String getCmUser() {
		return cmUser;
	}
	public void setCmUser(String cmUser) {
		this.cmUser = cmUser;
	}
	public String getCmTmpstp() {
		return cmTmpstp;
	}
	public void setCmTmpstp(String cmTmpstp) {
		this.cmTmpstp = cmTmpstp;
	}
	public String getCmPoleNo() {
		return cmPoleNo;
	}
	public void setCmPoleNo(String cmPoleNo) {
		this.cmPoleNo = cmPoleNo;
	}
	public String getCmBegMnth() {
		return cmBegMnth;
	}
	public void setCmBegMnth(String cmBegMnth) {
		this.cmBegMnth = cmBegMnth;
	}
	public String getCmTmpDurn() {
		return cmTmpDurn;
	}
	public void setCmTmpDurn(String cmTmpDurn) {
		this.cmTmpDurn = cmTmpDurn;
	}
	public String getCmFirstBillDcFlg() {
		return cmFirstBillDcFlg;
	}
	public void setCmFirstBillDcFlg(String cmFirstBillDcFlg) {
		this.cmFirstBillDcFlg = cmFirstBillDcFlg;
	}
	public String getCmRmks() {
		return cmRmks;
	}
	public void setCmRmks(String cmRmks) {
		this.cmRmks = cmRmks;
	}
	public String getCmSubmeterFlg() {
		return cmSubmeterFlg;
	}
	public void setCmSubmeterFlg(String cmSubmeterFlg) {
		this.cmSubmeterFlg = cmSubmeterFlg;
	}
	public String getCmDblMtrFlg() {
		return cmDblMtrFlg;
	}
	public void setCmDblMtrFlg(String cmDblMtrFlg) {
		this.cmDblMtrFlg = cmDblMtrFlg;
	}
	public String getCmSlumFlg() {
		return cmSlumFlg;
	}
	public void setCmSlumFlg(String cmSlumFlg) {
		this.cmSlumFlg = cmSlumFlg;
	}
	public String getCmEmailId() {
		return cmEmailId;
	}
	public void setCmEmailId(String cmEmailId) {
		this.cmEmailId = cmEmailId;
	}
	public String getCmCstNo() {
		return cmCstNo;
	}
	public void setCmCstNo(String cmCstNo) {
		this.cmCstNo = cmCstNo;
	}
	public String getCmKstNo() {
		return cmKstNo;
	}
	public void setCmKstNo(String cmKstNo) {
		this.cmKstNo = cmKstNo;
	}
	public String getCmTinNo() {
		return cmTinNo;
	}
	public void setCmTinNo(String cmTinNo) {
		this.cmTinNo = cmTinNo;
	}
	public String getCmTransmissionLineNo() {
		return cmTransmissionLineNo;
	}
	public void setCmTransmissionLineNo(String cmTransmissionLineNo) {
		this.cmTransmissionLineNo = cmTransmissionLineNo;
	}
	public String getSpotFolioNo() {
		return spotFolioNo;
	}
	public void setSpotFolioNo(String spotFolioNo) {
		this.spotFolioNo = spotFolioNo;
	}
	public String getToFolioNo() {
		return toFolioNo;
	}
	public void setToFolioNo(String toFolioNo) {
		this.toFolioNo = toFolioNo;
	}
	public String getDtcConnLoad() {
		return dtcConnLoad;
	}
	public void setDtcConnLoad(String dtcConnLoad) {
		this.dtcConnLoad = dtcConnLoad;
	}
	public String getDtcCapacity() {
		return dtcCapacity;
	}
	public void setDtcCapacity(String dtcCapacity) {
		this.dtcCapacity = dtcCapacity;
	}
	public String getDtc() {
		return dtc;
	}
	public void setDtc(String dtc) {
		this.dtc = dtc;
	}
	public String getDtcLoad() {
		return dtcLoad;
	}
	public void setDtcLoad(String dtcLoad) {
		this.dtcLoad = dtcLoad;
	}
	public String getNoOfdays() {
		return noOfdays;
	}
	public void setNoOfdays(String noOfdays) {
		this.noOfdays = noOfdays;
	}
	public String getCmFullRRno() {
		return cmFullRRno;
	}
	public void setCmFullRRno(String cmFullRRno) {
		this.cmFullRRno = cmFullRRno;
	}
	public String getCmRowNum() {
		return cmRowNum;
	}
	public void setCmRowNum(String cmRowNum) {
		this.cmRowNum = cmRowNum;
	}
	public String getCM_VERSION() {
		return CM_VERSION;
	}
	public void setCM_VERSION(String cM_VERSION) {
		CM_VERSION = cM_VERSION;
	}
	
	
	

}
