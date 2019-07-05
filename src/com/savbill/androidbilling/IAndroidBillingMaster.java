/**
 * 
 */
package com.savbill.androidbilling;

import net.sf.json.JSONObject;

/**
 * @author win
 *
 */
public interface IAndroidBillingMaster {
	
	JSONObject getBillingDataFromTable(JSONObject object);	
	
	JSONObject getRecordCountForTodays(JSONObject object);

	JSONObject downloadDataToTable(JSONObject object);

	JSONObject uploadsinglerecordfromjobscheduler(JSONObject object);	

}
