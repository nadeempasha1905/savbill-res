package com.savbill.database;

public class DBQueries {
	
	//Master Module Procedures
	public static final String GET_CODE_TYPES = "{call PKG_MASTER.GET_CODE_TYPES(?)}";
	public static final String GET_CODE_DETAILS = "{call PKG_MASTER.GET_CODE_DETAILS(?,?)}";
	public static final String GET_CODE_DETL_DROPDOWN = "{call PKG_MASTER.GET_CODE_DETL_DROPDOWN(?,?)}";
	public static final String INSERT_CODE_DETAIL ="{call PKG_MASTER.INSERT_CODE_DETAIL(?,?,?,?,?,?)}";
	public static final String UPDATE_CODE_DETAIL ="{call PKG_MASTER.UPDATE_CODE_DETAIL(?,?,?,?,?,?)}";
	
	public static final String GET_CONFIG_DETAILS = "{call PKG_MASTER.GET_CONFIG_DETAILS(?,?)}";
	public static final String INSERT_CLOUDBILL_CONFIG = "{call PKG_MASTER.INSERT_CLOUDBILL_CONFIG(?,?,?,?,?)}";
	public static final String UPDATE_CLOUDBILL_CONFIG = "{call PKG_MASTER.UPDATE_CLOUDBILL_CONFIG(?,?,?,?,?)}";
	
	public static final String GET_BILLING_PARAMETERS = "{call PKG_MASTER.GET_BILLING_PARAMETERS(?)}";
	public static final String INSERT_CLOUDBILL_PARAM = "{call PKG_MASTER.INSERT_CLOUDBILL_PARAM(?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_CLOUDBILL_PARAM = "{call PKG_MASTER.UPDATE_CLOUDBILL_PARAM(?,?,?,?,?,?,?,?)}";
	
	public static final String GET_FORMS = "{call PKG_MASTER.GET_FORMS(?)}";
	public static final String GET_FORM_DETAILS = "{call PKG_MASTER.GET_FORM_DETAILS(?)}";
	public static final String INSERT_FORM_MASTER = "{call PKG_MASTER.INSERT_FORM_MASTER(?,?,?,?,?)}";
	public static final String UPDATE_FORM_MASTER = "{call PKG_MASTER.UPDATE_FORM_MASTER(?,?,?,?,?)}";
	
	public static final String GET_REBATE_MASTER_DETAILS = "{call PKG_MASTER.GET_REBATE_MASTER_DETAILS(?)}";
	public static final String INSERT_REBATE_MASTER = "{call PKG_MASTER.INSERT_REBATE_MASTER(?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_REBATE_MASTER = "{call PKG_MASTER.UPDATE_REBATE_MASTER(?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_SUB_DIV_DETAILS = "{call PKG_MASTER.GET_SUB_DIV_DETAILS(?)}";
	public static final String UPSERT_SUB_DIV_DETAILS = "{call PKG_MASTER.UPSERT_SUB_DIV_DETAILS(?,?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_BANK_DETAILS = "{call PKG_MASTER.GET_BANK_DETAILS(?,?,?,?,?)}";
	public static final String GET_BANK_DETAILS_BY_MICR_CD = "{call PKG_MASTER.GET_BANK_DETAILS_BY_MICR_CD(?,?)}";
	public static final String INSERT_BANK_DETAILS = "{call PKG_MASTER.INSERT_BANK_DETAILS(?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_BANK_DETAILS = "{call PKG_MASTER.UPDATE_BANK_DETAILS(?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_FORM_PRIVILAGE_DETAILS = "{call PKG_MASTER.GET_FORM_PRIVILAGE_DETAILS(?,?)}";
	public static final String UPSERT_FORM_PRIVILEGES = "{call PKG_MASTER.UPSERT_FORM_PRIVILEGES(?,?,?,?,?,?,?,?)}";
	
	public static final String GET_PYMNT_ADJ_PRIORITY_DETAILS = "{call PKG_MASTER.GET_PYMNT_ADJ_PRIORITY_DETAILS(?)}";
	public static final String UPSERT_PYMNT_ADJ_PRIORITY = "{call PKG_MASTER.UPSERT_PYMNT_ADJ_PRIORITY(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_TARIFF_MASTER_DETAILS = "{call PKG_MASTER.GET_TARIFF_MASTER_DETAILS(?)}";
	public static final String INSERT_TRF_MASTER = "{call PKG_MASTER.INSERT_TRF_MASTER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_TRF_MASTER = "{call PKG_MASTER.UPDATE_TRF_MASTER(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_CHQDIS_PENLTY_MASTER_DETLS = "{call PKG_MASTER.GET_CHQDIS_PENLTY_MASTER_DETLS(?)}";
	public static final String INSERT_CHQ_DIS_PENALITY = "{call PKG_MASTER.INSERT_CHQ_DIS_PENALITY(?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_CHQ_DIS_PENALITY = "{call PKG_MASTER.UPDATE_CHQ_DIS_PENALITY(?,?,?,?,?,?,?,?)}";
	
	public static final String GET_CONTRACTOR_MASTER_DETAILS = "{call PKG_MASTER.GET_CONTRACTOR_MASTER_DETAILS(?,?,?,?)}";
	public static final String INSERT_CONTRACTOR_MASTER = "{call PKG_MASTER.INSERT_CONTRACTOR_MASTER(?,?,?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_CONTRACTOR_MASTER = "{call PKG_MASTER.UPDATE_CONTRACTOR_MASTER(?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_METER_READER_DETAILS = "{call PKG_MASTER.GET_METER_READER_DETAILS(?,?,?,?)}";
	public static final String INSERT_MTR_RDR_MASTR = "{call PKG_MASTER.INSERT_MTR_RDR_MASTR(?,?,?,?,?,?,?,?)}";
	
	public static final String GET_PRE_DOMINANT_DETAILS = "{call PKG_MASTER.GET_PRE_DOMINANT_DETAILS(?,?,?)}";
	public static final String UPSERT_PREDOMINANT_UNITS = "{call PKG_MASTER.UPSERT_PREDOMINANT_UNITS(?,?,?,?,?,?,?)}";
	
	public static final String GET_HHD_DOWN_UP_DETAILS = "{call PKG_MASTER.GET_HHD_DOWN_UP_DETAILS(?,?,?,?,?)}";
	
	public static final String GET_APPEAL_AMT_DETAILS = "{call PKG_MASTER.GET_APPEAL_AMT_DETAILS(?,?,?,?,?)}";
	public static final String INSERT_APPEAL_AMT = "{call PKG_MASTER.INSERT_APPEAL_AMT(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_APPEAL_AMT = "{call PKG_MASTER.UPDATE_APPEAL_AMT(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_DESIGNATION_DETAILS = "{call PKG_MASTER.GET_DESIGNATION_DETAILS(?,?)}";
	public static final String UPSERT_DESIGNATION_DETAILS = "{call PKG_MASTER.UPSERT_DESIGNATION_DETAILS(?,?,?,?,?,?,?)}";
	
	//Master Module Procedures Ends
	
	//Customer Module Procedure 
	public static final String TEMPORARY_RENEWAL = "{call PKG_CUSTOMER.TEMPORARY_RENEWAL(?,?,?,?,?,?,?)}";
	
	public static final String GET_CUSTOMER_HISTORY_DETAILS = "{call PKG_CUSTOMER.GET_CUSTOMER_HISTORY_DETAILS(?,?)}";
	
	public static final String GET_CUSTOMER_SEARCH_DETAILS = "{call PKG_CUSTOMER.GET_CUSTOMER_SEARCH_DETAILS(?)}";
	
	public static final String GET_CUSTOMER_DETIALS = "{call PKG_CUSTOMER.GET_CUSTOMER_DETIALS(?,?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_CUSTOMER_REGION_DETAILS = "{call PKG_CUSTOMER.GET_CUSTOMER_REGION_DETAILS(?,?,?,?,?)}";
	public static final String CUSTOMER_REGION_MAPPING = "{call PKG_CUSTOMER.CUSTOMER_REGION_MAPPING(?,?,?,?)}";
	
	public static final String GET_CUSTOMER_CHANGE_TYPES = "{call PKG_CUSTOMER.GET_CUSTOMER_CHANGE_TYPES(?)}";
	public static final String GET_CUSTOMER_DETLS_FOR_UPDATE = "{call PKG_CUSTOMER.GET_CUSTOMER_DETLS_FOR_UPDATE(?,?)}";
	public static final String INSERT_CUSTOMER_CHANGES =  "{call PKG_CUSTOMER.INSERT_CUSTOMER_CHANGES(?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String GET_CUSTOMER_DETLS_FOR_VERIFY = "{call PKG_CUSTOMER.GET_CUSTOMER_DETLS_FOR_VERIFY(?,?,?)}";
	public static final String VERIFY_REJECT_CUSTOMER_CHANGES ="{call PKG_CUSTOMER.VERIFY_REJECT_CUSTOMER_CHANGES(?,?,?,?,?,?,?)}";
	public static final String GET_CUSTOMER_DETLS_FOR_APPROVE = "{call PKG_CUSTOMER.GET_CUSTOMER_DETLS_FOR_APPROVE(?,?,?)}";
	public static final String APPROVE_REJECT_CUSTOMER_CHANGE ="{call PKG_CUSTOMER.APPROVE_REJECT_CUSTOMER_CHANGE(?,?,?,?,?,?,?)}";
	
	public static final String GET_CUSTOMER_NAME = "{call PKG_CUSTOMER.GET_CUSTOMER_NAME(?,?)}";
	
	//Customer Module ends
	
	//Deposits Module Procedures
	public static final String GET_DEPOSITS = "{call PKG_DEPOSITS.GET_DEPOSITS(?,?,?,?,?,?,?,?)}";
	public static final String GET_DEPOSITS_DETAILS = "{call PKG_DEPOSITS.GET_DEPOSITS_DETAILS(?,?)}";
	public static final String GET_DEP_INT_NMMD_PARAMETERS = "{call PKG_DEPOSITS.GET_DEP_INT_NMMD_PARAMETERS(?,?,?)}";
	
	public static final String GET_DEP_INT_NMMD_YEAR = "{call PKG_DEPOSITS.GET_DEP_INT_NMMD_YEAR(?,?,?)}";
	public static final String GET_DEPINT_DETLS_FOR_APPROVE = "{call PKG_DEPOSITS.GET_DEPINT_DETLS_FOR_APPROVE(?,?,?,?,?)}";
	public static final String DEPOSIT_INT_CREDIT_APPROVE = "{call PKG_DEPOSITS.DEPOSIT_INT_CREDIT_APPROVE(?,?,?,?)}";
	
	//Deposits Module ends
	
	//Cash Section Module Procedures
	public static final String GET_RCPT_DETIALS = "{call PKG_CASH.GET_RCPT_DETIALS(?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String GET_RCPT_POSTED_DETIALS = "{call PKG_CASH.GET_RCPT_POSTED_DETAILS(?,?,?,?)}";
	public static final String GET_LIST_OF_PAYMENTS = "{call PKG_CASH.GET_LIST_OF_PAYMENTS(?,?,?,?,?)}";
	public static final String GET_RCPT_DETIALS_FOR_POST = "{call PKG_CASH.GET_RCPT_DETIALS_FOR_POST(?,?,?,?)}";
	public static final String UPLOAD_MANUAL_RECEIPTS = "{call UPLOAD_HRT_RCPTS(?,?,?,?)}";

	//Cash Section Module Ends
	
	//accounts Module Procedures Starts
	public static final String GET_CHARGE_CODE_DETAILS = "{call PKG_ACCOUNTS.GET_CHARGE_CODE_DETAILS(?)}";
	
	public static final String GET_BILLS = "{call PKG_ACCOUNTS.GET_BILLS(?,?,?,?,?)}";
	public static final String GET_BILL_BREAKUP = "{call PKG_ACCOUNTS.GET_BILL_BREAKUP(?,?,?,?)}";
	public static final String GET_BILL_BREAKUP_SLABS = "{call PKG_ACCOUNTS.GET_BILL_BREAKUP_SLABS(?,?,?,?,?)}";
	public static final String GET_BILL_RECEIPT_BREAKUP_SLABS = "{call PKG_ACCOUNTS.GET_BILL_RECEIPT_BREAKUP_SLABS(?,?,?,?,?)}";
	
	public static final String GET_BILLS_LIST = "{call PKG_ACCOUNTS.GET_LIST_BILL_DETAILS(?,?,?,?,?,?,?,?,?,?)}";

	public static final String GET_RR_DETAILS_FOR_BILL_CANCEL = "{call PKG_ACCOUNTS.GET_RR_DETAILS_FOR_BILL_CANCEL(?,?)}";
	public static final String CANCEL_BILL = "{call PKG_ACCOUNTS.CANCEL_BILL(?,?,?,?)}";
	
	public static final String GET_SPOT_FOLIO_DETAILS = "{call PKG_ACCOUNTS.GET_SPOT_FOLIO_DETAILS(?,?,?,?)}";
	public static final String UPSERT_SPOT_FOLIO_DETAILS = "{call PKG_ACCOUNTS.UPSERT_SPOT_FOLIO_DETAILS(?,?,?,?)}";
	public static final String REGENERATE_SPOT_FOLIO = "{call PKG_ACCOUNTS.REGENERATE_SPOT_FOLIO(?,?,?,?,?)}";
	
	public static final String GET_FL_DETAILS = "{call PKG_ACCOUNTS.GET_FL_DETAILS(?,?,?)}";
	public static final String INSERT_FL_SANCT = "{call PKG_ACCOUNTS.INSERT_FL_SANCT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_FL_SANCT = "{call PKG_ACCOUNTS.UPDATE_FL_SANCT(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_REBATE_TYPE_DETAILS = "{call PKG_ACCOUNTS.GET_REBATE_TYPE_DETAILS(?,?)}";
	public static final String GET_REBATE_DETAILS = "{call PKG_ACCOUNTS.GET_REBATE_DETAILS(?,?,?,?)}";
	public static final String INSERT_REBATE_DETAILS = "{call PKG_ACCOUNTS.INSERT_REBATE_DETAILS(?,?,?,?,?,?,?)}";
	public static final String UPDATE_REBATE_DETAILS = "{call PKG_ACCOUNTS.UPDATE_REBATE_DETAILS(?,?,?,?,?,?,?)}";
	
	public static final String GET_REGULAR_PENALITY_DETAILS = "{call PKG_ACCOUNTS.GET_REGULAR_PENALITY_DETAILS(?,?,?,?)}";
	public static final String INSERT_REGULAR_PENALTY_DETAILS = "{call PKG_ACCOUNTS.INSERT_REGULAR_PENALTY_DETAILS(?,?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_REGULAR_PENALTY_DETAILS = "{call PKG_ACCOUNTS.UPDATE_REGULAR_PENALTY_DETAILS(?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_ECS_DETAILS = "{call PKG_ACCOUNTS.GET_ECS_DETAILS(?,?,?,?)}";
	public static final String GET_ECS_BANK_ACCOUNT_TYPE = "{call PKG_ACCOUNTS.GET_ECS_BANK_ACCOUNT_TYPE(?)}";
	public static final String INSERT_ECS_DETAILS = "{call PKG_ACCOUNTS.INSERT_ECS_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_ECS_DETAILS = "{call PKG_ACCOUNTS.UPDATE_ECS_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_CHQ_DIS_DETAILS = "{call PKG_ACCOUNTS.GET_CHQ_DIS_DETAILS(?,?,?,?,?,?,?)}";
	public static final String GET_CHQ_DIS_RCPT_DETAILS = "{call PKG_ACCOUNTS.GET_CHQ_DIS_RCPT_DETAILS(?,?,?,?,?)}";
	public static final String DO_CHEQUE_BOUNCE = "{call PKG_CHQ_DISHONOUR.DO_CHEQUE_BOUNCE(?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_OTHER_CHQ_DIS_DETAILS = "{call PKG_ACCOUNTS.GET_OTH_CHQ_DIS_DETAILS(?,?,?,?,?,?,?)}";
	
	public static final String GET_BILL_AVG_UNITS = "{call PKG_ACCOUNTS.GET_BILL_AVG_UNITS(?,?)}";
	public static final String GET_FIXED_AVG_UNITS = "{call PKG_ACCOUNTS.GET_FIXED_AVG_UNITS(?,?,?)}";
	public static final String INSERT_FIXED_AVG = "{call PKG_ACCOUNTS.INSERT_FIXED_AVG(?,?,?,?,?,?,?)}";
	public static final String UPDATE_FIXED_AVG = "{call PKG_ACCOUNTS.UPDATE_FIXED_AVG(?,?,?,?,?,?,?)}";
	
	public static final String GET_ADJUSTMENT_DETAILS = "{call PKG_ACCOUNTS.GET_ADJUSTMENT_DETAILS(?,?,?,?,?,?,?)}";
	public static final String DO_ADJUSTMENT = "{call Bill_Adjustments(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_DISCONNECTION_DETAILS = "{call PKG_ACCOUNTS.GET_DISCONNECTION_DETAILS(?,?,?,?,?,?,?,?)}";
	
	public static final String GET_RECONNECTION_DETAILS = "{call PKG_ACCOUNTS.GET_RECONNECTION_DETAILS(?,?,?,?)}";
	
	public static final String GET_MRWISE_PROCESS_DETAILS = "{call PKG_ACCOUNTS.GET_MRWISE_PROCESS_DETAILS(?,?,?)}";
	public static final String GET_RRNOWISE_PROCESS_DETAILS = "{call PKG_ACCOUNTS.GET_RRNOWISE_PROCESS_DETAILS(?,?,?,?)}";
	
	//Reading entry And Modification.
	
	public static final String GET_MTR_RDG_DETAILS = "{call PKG_ACCOUNTS.GET_MTR_RDG_DETAILS(?,?,?,?,?,?,?)}";
	public static final String UPSERT_METER_READING = "{call PKG_ACCOUNTS.UPSERT_METER_READING(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_METER_DETAILS = "{call PKG_ACCOUNTS.GET_METER_DETAILS(?,?,?,?,?,?)}";
	public static final String SAVE_REMOVE_ASSIGN_METER = "{call UPSERT_METER_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_METER_RATING_DETAILS = "{call PKG_ACCOUNTS.GET_METER_RATING_DETAILS(?,?,?,?)}";
	public static final String INSERT_METER_RATING_DETAILS = "{call PKG_ACCOUNTS.INSERT_METER_RATING_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_METER_RATING_DETAILS = "{call PKG_ACCOUNTS.UPDATE_METER_RATING_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	//debits
	public static final String GET_DEBIT_DETAILS = "{call PKG_ACCOUNTS.GET_DEBIT_DETAILS(?,?,?,?)}";
	public static final String INSERT_DEBIT_DETAILS = "{call PKG_ACCOUNTS.INSERT_DEBIT_DETAILS(?,?,?,?,?,?)}";
	public static final String GET_DEBIT_DETAILS_FOR_APPROVAL = "{call PKG_ACCOUNTS.GET_DEBIT_DETAILS_FOR_APPROVAL(?,?)}";
	public static final String APPROVE_DEBIT_DETAILS = "{call PKG_ACCOUNTS.APPROVE_DEBIT_DETAILS(?,?,?,?,?,?,?,?,?,?)}";
	public static final String REJECT_DEBIT_DETAILS = "{call PKG_ACCOUNTS.REJECT_DEBIT_DETAILS(?,?,?,?,?,?,?,?,?,?)}";
	
	//credits
	public static final String GET_CUSTOMER_CREDIT = "{call PKG_ACCOUNTS.GET_CUSTOMER_CREDIT(?,?)}";
	public static final String GET_CREDIT_DETAILS = "{call PKG_ACCOUNTS.GET_CREDIT_DETAILS(?,?,?)}";
	public static final String INSERT_CREDIT_DETAILS = "{call PKG_ACCOUNTS.INSERT_CREDIT_DETAILS(?,?,?,?,?,?)}";
	public static final String GET_CREDIT_DETLS_FOR_APPROVAL = "{call PKG_ACCOUNTS.GET_CREDIT_DETLS_FOR_APPROVAL(?,?)}";
	public static final String GET_BULK_CR_DETLS_FOR_APPROVAL = "{call PKG_ACCOUNTS.GET_BULK_CR_DETLS_FOR_APPROVAL(?,?,?,?,?,?,?,?,?)}";
	public static final String GET_BULK_CREDIT_DETLS_APPROVE = "{call PKG_ACCOUNTS.GET_BULK_CREDIT_DETLS_APPROVE(?,?,?,?,?,?,?,?,?)}";
	public static final String APPROVE_CREDIT_DETAILS = "{call PKG_MANUAL_CREDIT.INS_CR_DETL(?,?,?,?,?,?,?,?,?,?)}";
	public static final String GET_DEPOSIT_DETLS_FOR_APPROVAL = "{call PKG_ACCOUNTS.GET_DEPOSIT_DETLS_FOR_APPROVAL(?,?,?,?,?)}";
	
	//Withdrawal
	public static final String GET_WDRL_DETAILS = "{call PKG_ACCOUNTS.GET_WDRL_DETAILS(?,?,?)}";
	public static final String GET_WDRL_DETLS_NEWENTRY = "{call PKG_ACCOUNTS.GET_WDRL_DETLS_NEWENTRY(?,?)}";
	public static final String INSERT_WITHDRAWAL_DETAILS = "{call PKG_ACCOUNTS.INSERT_WITHDRAWAL_DETAILS(?,?,?,?,?,?,?,?,?,?)}";
	public static final String GET_WDRL_DETAILS_FOR_APPROVAL = "{call PKG_ACCOUNTS.GET_WDRL_DETAILS_FOR_APPROVAL(?,?)}";
	public static final String APPROVE_WITHDRAWAL_DETAILS = "{call PKG_ACCOUNTS.APPROVE_WITHDRAWAL_DETAILS(?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_CASH_COUNTER_LIST = "{call PKG_ACCOUNTS.GET_CASH_COUNTER_LIST(?,?,?)}";
	
	public static final String GET_DESIGNATIONS = "{call PKG_ACCOUNTS.GET_DESIGNATIONS(?)}";

	//Accounts Module Ends
	
	//Main Module Procedure 
	public static final String TRANSFER_RR_NO_MR_TO_MR = "{call TRANSFER_RR_NO_MR_TO_MR(?,?,?,?,?,?,?,?,?)}";
	
	//Main Module Ends
	
	
	//Infrastructure Module Procedures 
	public static final String GET_LOCATION_DETAILS = "{call PKG_INFRASTRUCTURE.GET_LOCATION_DETAILS(?,?,?)}";
	
	public static final String GET_STATION_DETAILS = "{call PKG_INFRASTRUCTURE.GET_STATION_DETAILS(?,?,?,?)}";
	public static final String INSERT_STATION_DETAILS = "{call PKG_INFRASTRUCTURE.INSERT_STATION_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_STATION_DETAILS = "{call PKG_INFRASTRUCTURE.UPDATE_STATION_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_FEEDER_DETAILS = "{call PKG_INFRASTRUCTURE.GET_FEEDER_DETAILS(?,?,?,?)}";
	public static final String INSERT_FEEDER_DETAILS = "{call PKG_INFRASTRUCTURE.INSERT_FEEDER_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_FEEDER_DETAILS = "{call PKG_INFRASTRUCTURE.UPDATE_FEEDER_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_DTC_DETAILS = "{call PKG_INFRASTRUCTURE.GET_DTC_DETAILS(?,?,?,?,?,?)}";
	public static final String INSERT_DTC_DETAILS = "{call PKG_INFRASTRUCTURE.INSERT_DTC_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_DTC_DETAILS = "{call PKG_INFRASTRUCTURE.UPDATE_DTC_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
		
	public static final String GET_OM_DETAILS = "{call PKG_INFRASTRUCTURE.GET_OM_DETAILS(?,?)}";
	public static final String UPSERT_OM_DETAILS = "{call PKG_INFRASTRUCTURE.UPSERT_OM_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	
	public static final String GET_DTC_TRANSFER_DETAILS = "{call PKG_INFRASTRUCTURE.GET_DTC_TRANSFER_DETAILS(?,?,?,?,?,?,?)}";
	public static final String DTC_TRANSFER = "{call PKG_INFRASTRUCTURE.DTC_TRANSFER(?,?,?,?,?,?,?,?)}";
	
	public static final String GET_TOWN_DETAILS = "{call PKG_INFRASTRUCTURE.GET_TOWN_DETAILS(?,?)}";
	public static final String GET_DTC_TOWN_MAP_DETLS = "{call PKG_INFRASTRUCTURE.GET_DTC_TOWN_MAP_DETLS(?,?,?,?,?)}";
	public static final String MAP_DTC_TOWN ="{call PKG_INFRASTRUCTURE.MAP_DTC_TOWN(?,?,?,?,?,?,?)}";

	//Infrastructure Module Ends
	
	//Energy Audit Module Procedures
	public static final String GET_DTC_MTR_MASTER_DETAILS = "{call PKG_ENERGY_AUDIT.GET_DTC_MTR_MASTER_DETAILS(?,?,?,?,?,?)}";
	public static final String INSERT_DTC_METER_DETAILS = "{call PKG_ENERGY_AUDIT.INSERT_DTC_METER_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String UPDATE_DTC_METER_DETAILS = "{call PKG_ENERGY_AUDIT.UPDATE_DTC_METER_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}";

	public static final String GET_DTC_MTR_RDG_DETAILS = "{call PKG_ENERGY_AUDIT.GET_DTC_MTR_RDG_DETAILS(?,?,?,?,?,?)}";
	
	public static final String GET_FDR_ENRGY_AUDIT_DETAILS = "{call PKG_ENERGY_AUDIT.GET_FDR_ENRGY_AUDIT_DETAILS(?,?,?,?,?)}";
	public static final String UPSERT_FEEDER_UNITS = "{call PKG_ENERGY_AUDIT.UPSERT_FEEDER_UNITS(?,?,?,?,?,?,?,?)}";
	
	//Energy Audit Module Ends
	
	//User Module procedures 
	public static final String VERIFY_USER_ID = "{call PKG_USER.VERIFY_USER_ID(?,?)}";
	public static final String AUTHENTICATE_USER = "{call PKG_USER.AUTHENTICATE_USER(?,?,?)}";
	public static final String CHANGE_PASSWORD = "{call PKG_USER.CHANGE_USER_PASSWORD(?,?,?,?,?)}";
	
	public static final String GET_SAV_MENUS = "{call GET_MENUS_BY_USERID(?,?)}";
	
	public static final String GET_USER_ROLE_DETLS = "{call PKG_USER.GET_USER_ROLE_DETLS(?)}";
	public static final String UPSERT_USER_ROLE_DETAILS = "{call PKG_USER.UPSERT_USER_ROLE_DETAILS(?,?,?,?,?)}";
	
	public static final String GET_USER_MASTER_DETAILS = "{call PKG_USER.GET_USER_MASTER_DETAILS(?,?)}";
	public static final String UPSERT_USER_MASTER_DETAILS = "{call PKG_USER.UPSERT_USER_MASTER_DETAILS(?,?,?,?,?,?,?,?,?,?,?,?,?)}";
	public static final String GET_USER_SESSION_DETAILS = "{call PKG_USER.GET_USER_SESSION_DETAILS(?,?)}";
	public static final String UPDATE_USER_SESSION = "{call PKG_USER.UPDATE_USER_SESSION(?,?)}";
	public static final String GET_USER_DELIGATION_DETAILS = "{call PKG_USER.GET_USER_DELIGATION_DETAILS(?,?)}";
	public static final String UPSERT_USER_DELIGATION = "{call PKG_USER.UPSERT_USER_DELIGATION(?,?,?,?,?,?,?,?,?,?,?)}";
	
	
	//User Module Ends 
	
	
	// Report Modulae Starts
	public static final String REPORT_GET_BILLING_EFFICIENCY = "{call GET_BILLING_EFFICIENCY(?,?,?,?,?,?,?,?)}";
	
	
	/// Report Module Ends 
	
}
