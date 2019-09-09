package com.savbill.accounts;

import javax.servlet.http.HttpServletRequest;

import net.sf.json.JSONObject;

public interface IAccountManagement {
	
		
	JSONObject getBills(JSONObject object);
	JSONObject getBillBreakup(JSONObject object);
	JSONObject getBillBreakupSlabs(JSONObject object);
	JSONObject getBillReceiptBreakupSlabs(JSONObject object);
	
	JSONObject getListBillDetails(JSONObject object);
	
	JSONObject getRRDetailsForBillCancel(JSONObject object);
	JSONObject cancelBill(JSONObject object);
	
	JSONObject getSpotFolioDetails(JSONObject object);
	JSONObject upsertSpotFolioDetails(JSONObject data);
	JSONObject regenerateSpotFolioDetails(JSONObject object);
	
	JSONObject getFLDetails(JSONObject object);
	JSONObject doAddOrUpdateFLDetails(JSONObject object);
	
	JSONObject getChargeCodeDetails(JSONObject object);
	JSONObject getChargeCodeDetailsForRebateType(JSONObject object);
	JSONObject getRebateDetails(JSONObject object);
	JSONObject doAddOrUpdateRebateDetails(JSONObject object);
	
	JSONObject getRegularPenaltyDetails(JSONObject object);
	JSONObject upsertRegularPenaltyDetails(JSONObject object);
	
	JSONObject getEcsBankAccountType(JSONObject object);
	JSONObject getCustomerName(JSONObject object);
	JSONObject getECSDetails(JSONObject object);
	JSONObject upsertEcsDetails(JSONObject object);
	
	JSONObject getChequeDetails(JSONObject object);
	
	JSONObject getOtherChequeDetails(JSONObject object);
	
	JSONObject getBilledAvgUnits(JSONObject object);
	JSONObject getFixedAvgUnits(JSONObject object);
	JSONObject upsertFixedAvgUnits(JSONObject object);
	
	JSONObject getAdjustmentDetails(JSONObject object);
	JSONObject getreceiptdetailstoadjust(JSONObject object);
	
	
	JSONObject getDisconnectionDetails(JSONObject object);
	
	JSONObject getReconnectionDetails(JSONObject object);
	
	JSONObject getMRWiseProcessDetails(JSONObject object);
	JSONObject getRRNumberWiseProcessDetails(JSONObject object);
	
	JSONObject getReadingEntryData(JSONObject object);
	JSONObject upsertReadingEntryData(JSONObject object);
	
	JSONObject getMeterDetails(JSONObject object);
	
	JSONObject getDesignations(JSONObject object);
	
	JSONObject getRatingDetails(JSONObject object);
	JSONObject upsertRatingDetails(JSONObject object);
	
	//Debits
	JSONObject getDebitDetails(JSONObject object);
	JSONObject insertDebit(JSONObject object);
	JSONObject getPendingDebits(JSONObject object);
	JSONObject approveDebits(JSONObject object);
	JSONObject rejectDebits(JSONObject object);
	
	//Credits
	JSONObject getCustomerCredits(JSONObject object);
	JSONObject getCreditDetails(JSONObject object);
	JSONObject insertCredit(JSONObject object);
	JSONObject getPendingCredits(JSONObject object);
	JSONObject getBulkCreditDetailsForApprove(JSONObject object);
	JSONObject approveCredits(JSONObject object);
	JSONObject rejectCredits(JSONObject object);

	JSONObject getPendingDeposits(JSONObject object);
	
	//Withdrawals
	JSONObject getWithdrawlDetails(JSONObject object);
	JSONObject getWithdrawlDetailsForNewEntry(JSONObject object);
	JSONObject insertWithdrawlDetails(JSONObject data);
	JSONObject getWithdrawlDetailsForApproval(JSONObject object);
	JSONObject approveRejectWithdrawals(JSONObject data);
	JSONObject getreceiptdetailsforchequedisno(JSONObject object);
	JSONObject dochequedishonour(JSONObject object);
	JSONObject dovalidaterrnumber(JSONObject object);
	JSONObject saveadjustmentrecord(JSONObject object, HttpServletRequest request);
	
}
