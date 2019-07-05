package com.savbill.master;

import net.sf.json.JSONObject;

public interface IMasterManagement {

	
	JSONObject getCodeTypesforCodeDetails(JSONObject object);
	JSONObject getCodeDetailsForDropdowns(JSONObject object);
	JSONObject getCodeDetails(JSONObject object);
	JSONObject upsertCodeDetails(JSONObject object);
	
	JSONObject getConfigurationDetails(JSONObject object);
	JSONObject upsertCloudbillConfig(JSONObject object);
	
	JSONObject getBillingParameters(JSONObject object);
	JSONObject upsertCloudbillParameter(JSONObject object);
	
	JSONObject getFormDetails(JSONObject object);
	JSONObject upsertFormMaster(JSONObject object);
	JSONObject getForms(JSONObject object);
	
	JSONObject getFormPrevilageDetails(JSONObject object);
	JSONObject upsertFormPrevilageDetails(JSONObject object);
	
	JSONObject getSubDivisionDetails(JSONObject object);
	JSONObject upsertSubDivisionDetails(JSONObject object);
	
	JSONObject getRebateMasterDetails(JSONObject object);
	JSONObject upsertRebateMasterDetails(JSONObject object);
	
	JSONObject getBankDetails(JSONObject object);
	JSONObject getBankDetailsbyMicrCode(JSONObject object);
	JSONObject upsertBankDetails(JSONObject object);
	
	JSONObject getTariffMasterDetails(JSONObject object);
	JSONObject upsertTariffMasterDetails(JSONObject object);
	
	JSONObject getPaymentAdjustmentPriortyDetails(JSONObject object);
	JSONObject upsertPaymentAdjustmentPriortyDetails(JSONObject object);
	
	JSONObject getChequeDisPenaltyMasterDetails(JSONObject object);
	JSONObject upsertChequeDisPenaltyMasterDetails(JSONObject object);
	
	JSONObject getContractorMasterDetails(JSONObject object);
	JSONObject upsertContractorMasterDetails(JSONObject object);
	
	JSONObject getMeterReaderDetails(JSONObject object);
	JSONObject upsertMeterReaderDetails(JSONObject object);
	
	JSONObject getPreDominantDetails(JSONObject object);
	JSONObject upsertPreDominantDetails(JSONObject data);
	
	JSONObject getHHDUploadDetails(JSONObject object);
	JSONObject getHHDDownloadDetails(JSONObject object);
	
	JSONObject getAppealAmountDetails(JSONObject object);
	JSONObject upsertApealAmountDetails(JSONObject object);
	
	JSONObject getDesignationDetails(JSONObject object);
	JSONObject upsertDesignationDetails(JSONObject object);
}
