/**
 * 
 */
package com.savbill.customer;

import net.sf.json.JSONObject;

/**
 * @author Nadeem
 *
 */
public interface ICustomerManagement {
	
	JSONObject validateRrNumber (JSONObject object);
	
	JSONObject GetConsumerRRNoDetails (JSONObject object);
	
	JSONObject cashCounterList(JSONObject object);
	
	JSONObject GetAppealAmount(JSONObject object);
	
	JSONObject GetSideRRnumberValues(JSONObject object);
	
	JSONObject addOrUpdateConsumerDeposits(JSONObject object);
	JSONObject addOrUpdateConsumerDocuments(JSONObject object);
	JSONObject addOrUpdateConsumerCT_PT_RATIO(JSONObject object);
	JSONObject addOrUpdateConsumerIntimations(JSONObject object);

	JSONObject getStationList(JSONObject object);
	JSONObject getFeederList(JSONObject object);
	JSONObject getTransformerList(JSONObject object);
	JSONObject getMeterReaderCodesByOMUnit(JSONObject object);
	JSONObject getMeterReadingDayByMrCode(JSONObject object);
	
	JSONObject doAddOrUpdateConsumerMaster(JSONObject object);
	
	JSONObject temporaryRenewal(JSONObject object);
	
	JSONObject getCustomerHistory(JSONObject object);
	
	JSONObject getCustomerSearchDetails(JSONObject object);
	
	JSONObject getCustomerDetails(JSONObject object);
	
	JSONObject getCustomerRegionDetails(JSONObject object);
	JSONObject customerRegionMapping(JSONObject data);
	
	JSONObject getCustomerChangeTypes(JSONObject object);
	JSONObject getCustomerDetailsForUpdate(JSONObject object);
	JSONObject insertCustomerChangesInformation(JSONObject object);
	JSONObject getCustomerDetailsForVerify(JSONObject object);
	JSONObject verifyRejectCustomerChanges(JSONObject object);
	JSONObject getCustomerDetailsForApprove(JSONObject object);
	JSONObject approveRejectCustomerChanges(JSONObject object);
	
	JSONObject getSearchCriteriaList(JSONObject object);
	
	JSONObject fetchSearchtypeValues(JSONObject object);
}
