package com.savbill.deposits;

import net.sf.json.JSONObject;

public interface IDepositsManagement {

	JSONObject getCustomerDeposits(JSONObject object);
	JSONObject getCustomerDepositsDetails(JSONObject object);
	
	JSONObject getDepositInstrestAndNmmdParameters(JSONObject object);
	
	JSONObject getDepositInstrestYear(JSONObject object);
	JSONObject getPendingDepositIntrestDetails(JSONObject object);
	JSONObject approveDepositsIntrest(JSONObject data);
}
