/**
 * 
 */
package com.savbill.preloadpicklist;

import com.mysql.jdbc.Connection;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/**
 * @author Nadeem
 *
 */
public interface IPreLoadPickList {
	
	JSONObject getMeterReaderCodeList(JSONObject object);
	JSONObject getMeterReadingDayList(JSONObject object);
	JSONObject getCashCounterNoFromInitialReceiptPayment(JSONObject object);
	JSONObject getTariffList(JSONObject object);
	JSONObject getCampList(JSONObject object);
	JSONObject getZoneList(JSONObject object);
	JSONObject getDivisionList(JSONObject object);
	JSONObject getCircleList(JSONObject object);
	JSONObject getOMUnitList(JSONObject object);
	JSONObject getLocationList(JSONObject object);
	JSONObject getChargeDescriptionList(JSONObject object);
	JSONObject getPaymentPurposeList(JSONObject object);
	JSONObject getLocationCounterDetails(JSONObject object);
	JSONArray getCustomerType(JSONObject object);
	JSONArray getOMCode(JSONObject object);
	JSONArray getInstallationType(JSONObject object);
	JSONArray getCustomerStatus(JSONObject object);
	JSONArray getPowerPurpose(JSONObject object);
	JSONArray getIndustryCode(JSONObject object);
	JSONArray getLightingType(JSONObject object);
	JSONArray getPowerSanctionedBy(JSONObject object);
	JSONArray getWellType(JSONObject object);
	JSONArray getStarterType(JSONObject object);
	JSONArray getJuridiction(JSONObject object);
	JSONArray getConnectionType(JSONObject object);
	JSONArray getRegions(JSONObject object);
	JSONArray getTaluks(JSONObject object);
	JSONArray getDistricts(JSONObject object);
	JSONArray getStateConstncy(JSONObject object);
	JSONArray getCentralConsctncy(JSONObject object);
	JSONArray getDocumentType(JSONObject object);
	JSONArray getPurposeDetail(JSONObject object);
	JSONArray getInimationType(JSONObject object);
	
	JSONObject getCustomerMeterReaderCodeList(JSONObject object);
	JSONObject getCustomerMeterReadingDayList(JSONObject object);
	JSONArray getCustomerCashCounterNoFromInitialReceiptPayment(JSONObject object);
	JSONArray getCustomerTariffList(JSONObject object);
	
	JSONArray getStationList(JSONObject object);
	JSONArray getYesNoList();
	
	
	JSONArray GenericPreLoadCodeDetail(String DetailType,String CodeType,String ConnType);
	
	
	JSONObject getCodeDescrByCodeValue(JSONObject object);
	JSONObject getCodeValueByCodeDescription(JSONObject object);
	JSONObject getConsumerMasterPickListValues(JSONObject object);
	
	/*
	 * Rebate Detsils picklist
	*/
	
	JSONObject getrebatetypelist(JSONObject object);
	JSONObject getApealType(JSONObject object);
	JSONArray getReadingCycle(JSONObject object);
	JSONArray getBeginingMonth(JSONObject object);
	JSONArray getRequiredPhase(JSONObject object);
	
	
	JSONObject getTransformerCodeList(JSONObject object);
	JSONObject getFeederList(JSONObject object);
	
	JSONObject getUserIdList(JSONObject object);
	JSONObject getUserRolesList(JSONObject object);
	JSONObject getUserDetailsByUserId(JSONObject object);
	

}
