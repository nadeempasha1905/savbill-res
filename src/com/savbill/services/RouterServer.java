package com.savbill.services;


import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;

import com.savbill.accounts.AccountManagementImpl;
import com.savbill.accounts.IAccountManagement;
import com.savbill.androidbilling.AndroidBillingMaster;
import com.savbill.androidbilling.IAndroidBillingMaster;
import com.savbill.cash.CashManagementImpl;
import com.savbill.cash.ICashManagement;
import com.savbill.customer.CustomerManagementImpl;
import com.savbill.customer.ICustomerManagement;
import com.savbill.deposits.DepositsManagementImpl;
import com.savbill.deposits.IDepositsManagement;
import com.savbill.energyaudit.EnergyAuditManagementImpl;
import com.savbill.energyaudit.IEnergyAuditManagement;
import com.savbill.infrastructure.IInfrastructureManagement;
import com.savbill.infrastructure.InfrastructureManagementImpl;
import com.savbill.master.IMasterManagement;
import com.savbill.master.MasterManagementImpl;
import com.savbill.preloadpicklist.IPreLoadPickList;
import com.savbill.preloadpicklist.PreloadPicklistImpl;
import com.savbill.reports.IOtherReports;
import com.savbill.reports.IReportGeneration;
import com.savbill.reports.OtherReportsImpl;
import com.savbill.reports.ReportGenerationImpl;
import com.savbill.serversidebilling.IServerSideBilling;
import com.savbill.serversidebilling.ServerSideBillingImpl;
import com.savbill.user.IUserManagement;
import com.savbill.user.UserManagementImpl;

@Path("/services")
public class RouterServer {
	
	IMasterManagement masterObj = new MasterManagementImpl();
	ICustomerManagement customerObj = new CustomerManagementImpl();
	IDepositsManagement depositsObj = new DepositsManagementImpl();
	ICashManagement cashObj = new CashManagementImpl();
	IAccountManagement accountsObj = new AccountManagementImpl();
	IInfrastructureManagement infrastructureObj = new InfrastructureManagementImpl();
	IEnergyAuditManagement energyauditObj = new EnergyAuditManagementImpl();
	IUserManagement userObj = new UserManagementImpl();
	IPreLoadPickList pickListObj = new PreloadPicklistImpl();
	IServerSideBilling serverSideBillObj = new ServerSideBillingImpl();
	IAndroidBillingMaster androidBilling = new AndroidBillingMaster();
	IReportGeneration reportObj = new ReportGenerationImpl();
	IOtherReports othersObj = new OtherReportsImpl();
	
	
/***************************************Master*************************************************/	
	
	//master module packages starts
	
	@POST
	@Path("/getcodetypesforcodedetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCodeTypesforCodeDetails(final JSONObject object){
		
		return masterObj.getCodeTypesforCodeDetails(object);
	}
	
	@POST
	@Path("/getcodedetailsfordropdowns")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCodeDetailsForDropdowns(final JSONObject object){
		
		return masterObj.getCodeDetailsForDropdowns(object);
	}
	
	@POST
	@Path("/getcodedetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCodeDetails(final JSONObject object){
		
		return masterObj.getCodeDetails(object);
	}
	
	@POST
	@Path("/upsertcodedetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertCodeDetails(final JSONObject object){
		
		return masterObj.upsertCodeDetails(object);
	}
	
	@POST
	@Path("/getconfigurationdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getConfigurationDetails(final JSONObject object){
		
		return masterObj.getConfigurationDetails(object);
	}
	
	@POST
	@Path("/upsertcloudbillconfig")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertCloudbillConfig(final JSONObject object){
		
		return masterObj.upsertCloudbillConfig(object);
	}
	
	@POST
	@Path("/getbillingparameters")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getBillingParameters(final JSONObject object){
		
		return masterObj.getBillingParameters(object);
	}
	
	@POST
	@Path("/upsertcloudbillparameter")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertCloudbillParameter(final JSONObject object){
		
		return masterObj.upsertCloudbillParameter(object);
	}
	
	@POST
	@Path("/getformdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getFormDetails(final JSONObject object){
		
		return masterObj.getFormDetails(object);
	}
	
	
	@POST
	@Path("/upsertformmaster")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertFormMaster(final JSONObject object){
		
		return masterObj.upsertFormMaster(object);
	}
	
	@POST
	@Path("/getforms")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getForms(final JSONObject object){
		
		return masterObj.getForms(object);
	} 
	
	@POST
	@Path("/getformprevilages")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getFormPrevilageDetails(final JSONObject object){
		
		return masterObj.getFormPrevilageDetails(object);
	} 
	
	@POST
	@Path("/upsertformprevilagedetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertFormPrevilageDetails(final JSONObject object){
		
		return masterObj.upsertFormPrevilageDetails(object);
	}
	
	@POST
	@Path("/getsubdivisiondetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getSubDivisionDetails(final JSONObject object){
		
		return masterObj.getSubDivisionDetails(object);
	}
	
	@POST
	@Path("/upsertsubdivisiondetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertSubDivisionDetails(final JSONObject object){
		
		return masterObj.upsertSubDivisionDetails(object);
	}
	
	@POST
	@Path("/getrebatemaster")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getRebateMasterDetails(final JSONObject object){
		
		return masterObj.getRebateMasterDetails(object);
	}
	
	

	@POST
	@Path("/upsertrebatemasterdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertRebateMasterDetails(final JSONObject object){
		
		return masterObj.upsertRebateMasterDetails(object);
	}
	
	@POST
	@Path("/getbankdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getBankDetails(final JSONObject object){
		
		return masterObj.getBankDetails(object);
	}
	
	@POST
	@Path("/getbankdetailsbymicrcode")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getBankDetailsbyMicrCode(final JSONObject object){
		
		return masterObj.getBankDetailsbyMicrCode(object);
	}
	
	@POST
	@Path("/upsertbankdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertBankDetails(final JSONObject object){
		
		return masterObj.upsertBankDetails(object);
	}
	
	@POST
	@Path("/gettarriffmaster")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getTariffMasterDetails(final JSONObject object){
		
		return masterObj.getTariffMasterDetails(object);
	}
	
	@POST
	@Path("/upserttariffmasterdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertTariffMasterDetails(final JSONObject object){
		
		return masterObj.upsertTariffMasterDetails(object);
	}
	
	@POST
	@Path("/getchequepenality")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getChequeDisPenaltyMasterDetails(final JSONObject object){
		
		return masterObj.getChequeDisPenaltyMasterDetails(object);
	}
	
	@POST
	@Path("/upsertchequedispenaltymasterDetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertChequeDisPenaltyMasterDetails(final JSONObject object){
		
		return masterObj.upsertChequeDisPenaltyMasterDetails(object);
	}
	
	@POST
	@Path("/getpaymentadjpriorty")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getPaymentAdjustmentPriortyDetails(final JSONObject object){
		
		return masterObj.getPaymentAdjustmentPriortyDetails(object);
	} 
	
	@POST
	@Path("/upsertpaymentadjustmentpriortydetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertPaymentAdjustmentPriortyDetails(final JSONObject object){
		
		return masterObj.upsertPaymentAdjustmentPriortyDetails(object);
	}
	
	@POST
	@Path("/getcontractormaster")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getContractorMasterDetails(final JSONObject object){
		
		return masterObj.getContractorMasterDetails(object);
	}
	
	@POST
	@Path("/upsertcontractormasterdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertContractorMasterDetails(final JSONObject object){
		
		return masterObj.upsertContractorMasterDetails(object);
	}
	
	@POST
	@Path("/getmeterreaderdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getMeterReaderDetails(final JSONObject object){
		
		return masterObj.getMeterReaderDetails(object);
	}
	
	@POST
	@Path("/upsertmeterreaderdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertMeterReaderDetails(final JSONObject object){
		
		return masterObj.upsertMeterReaderDetails(object);
	}
	
	@POST
	@Path("/getpredominantdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getPreDominantDetails(final JSONObject object){
		
		return masterObj.getPreDominantDetails(object);
	} 
	
	@POST
	@Path("/upsertpredominantdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertPreDominantDetails(final JSONObject object){
		
		return masterObj.upsertPreDominantDetails(object);
	}
	
	@POST
	@Path("/gethhdduploaddetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getHHDUploadDetails(final JSONObject object){
		
		return masterObj.getHHDUploadDetails(object);
	}
	
	@POST
	@Path("/gethhdddownloaddetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getHHDDownloadDetails(final JSONObject object){
		
		return masterObj.getHHDDownloadDetails(object);
	}
	
	@POST
	@Path("/getUserIdList")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getUserIdList(final JSONObject object){
		
		System.out.println("getuseridlist"+object);
		return pickListObj.getUserIdList(object);
	}
	
	@POST
	@Path("/getUserRolesList")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getUserRolesList(final JSONObject object){
		
		return pickListObj.getUserRolesList(object);
	}
	
	@POST
	@Path("/getUserDetailsByUserId")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getUserDetailsByUserId(final JSONObject object){
		
		return pickListObj.getUserDetailsByUserId(object);
	}
	
	@POST
	@Path("/getcustomerstatuslist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getcustomerstatuslist(final JSONObject object){
		
		return pickListObj.getcustomerstatuslist(object);
	}
	
	
	
	@POST
	@Path("/getappealamount")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getAppealAmountDetails(final JSONObject object){
		
		return masterObj.getAppealAmountDetails(object);
	}
	
	@POST
	@Path("/upsertapealamountdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertApealAmountDetails(final JSONObject object){
		
		return masterObj.upsertApealAmountDetails(object);
	}
	
	@POST
	@Path("/getdesignationdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getDesignationDetails(final JSONObject object){
		
		return masterObj.getDesignationDetails(object);
	} 
	
	@POST
	@Path("/upsertdesignationdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertDesignationDetails(final JSONObject object){
		
		return masterObj.upsertDesignationDetails(object);
	}
	
	//Master Module Services Ends
	
	
/***************************************Customer*************************************************/	
	
	//Customer Modules Services Starts
	
	@POST
	@Path("/validaterrno")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject validateRrNumber(final JSONObject object){
			
		return customerObj.validateRrNumber(object);
	}
	
	@POST
	@Path("/getconsumerrrnodetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getConsumerDetails(final JSONObject object){

		JSONObject json = new JSONObject();
		
		json = customerObj.GetConsumerRRNoDetails(object);
		
		return json;
	}
	
	@POST
	@Path("/cashcounterlist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject cashCounterList(final JSONObject object){
		
		return customerObj.cashCounterList(object);
	}
	
	@POST
	@Path("/getSideRRnumberValues")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject GetSideRRnumberValues(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = customerObj.GetSideRRnumberValues(object);
		
		return json;
	}
	
	@POST
	@Path("/getAppealAmount")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject GetAppealAmount(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = customerObj.GetAppealAmount(object);
		
		return json;
	}
	
	@POST
	@Path("/addupdateconsumerdeposits")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject addUpdateConsumerDeposits(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = customerObj.addOrUpdateConsumerDeposits(object);
		
		return json;
	}
	
	@POST
	@Path("/addupdateconsumerdocuments")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject addOrUpdateConsumerDocuments(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = customerObj.addOrUpdateConsumerDocuments(object);
		
		return json;
	}
	
	@POST
	@Path("/addupdateconsumerctptratio")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject addOrUpdateConsumerCT_PT_RATIO(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = customerObj.addOrUpdateConsumerCT_PT_RATIO(object);
		
		return json;
	}
	
	@POST
	@Path("/addupdateconsumerintimations")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject addOrUpdateConsumerIntimations(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = customerObj.addOrUpdateConsumerIntimations(object);
		
		return json;
	}
	
	@POST
	@Path("/getStationList")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getStationList(final JSONObject object){
		
		return customerObj.getStationList(object);
	} 
	
	@POST
	@Path("/getFeederList")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getFeederList(final JSONObject object){
		
		return customerObj.getFeederList(object);
	}
	
	@POST
	@Path("/getTransformerList")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getTransformerList(final JSONObject object){
		
		return customerObj.getTransformerList(object);
	}
	
	@POST
	@Path("/getMeterReaderCodesByOMUnit")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getMeterReaderCodesByOMUnit(final JSONObject object){
		
		return customerObj.getMeterReaderCodesByOMUnit(object);
	}
	
	@POST
	@Path("/getMeterReadingDayByMrCode")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getMeterReadingDayByMrCode(final JSONObject object){
		
		return customerObj.getMeterReadingDayByMrCode(object);
	}
	
	@POST
	@Path("/doAddOrUpdateConsumerMaster")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject doAddOrUpdateConsumerMaster(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = customerObj.doAddOrUpdateConsumerMaster(object);
		
		return json;
	}
	
	@POST
	@Path("/temporaryrenewal")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject temporaryRenewal(final JSONObject object){
			
		return customerObj.temporaryRenewal(object);
	}
	
	@POST
	@Path("/getcustomerhistory")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCustomerHistory(final JSONObject object){
		
		return customerObj.getCustomerHistory(object);
	}
	
	@POST
	@Path("/getcustomersearchdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCustomerSearchDetails(final JSONObject object){
		
		return customerObj.getCustomerSearchDetails(object);
	}
	
	@POST
	@Path("/getcustomerdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCustomerDetails(final JSONObject object){
		
		return customerObj.getCustomerDetails(object);
	}
	
	@POST
	@Path("/getcustomerregionrdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCustomerRegionDetails(final JSONObject object){
		
		return customerObj.getCustomerRegionDetails(object);
	}
	
	@POST
	@Path("/customerregionmapping")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject customerRegionMapping(final JSONObject object){
		
		return customerObj.customerRegionMapping(object);
	}
	
	@POST
	@Path("/getcustomerchangetypes")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCustomerChangeTypes(final JSONObject object){
		
		return customerObj.getCustomerChangeTypes(object);
	}
	
	@POST
	@Path("/getcustomerdetailsforupdate")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCustomerDetailsForUpdate(final JSONObject object){
			
		return customerObj.getCustomerDetailsForUpdate(object);
	}
	
	@POST
	@Path("/insertcustomerchangesinformation")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject insertCustomerChangesInformation(final JSONObject object){
			
		return customerObj.insertCustomerChangesInformation(object);
	} 
	
	@POST
	@Path("/getcustomersforverify")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCustomerDetailsForVerify(final JSONObject object){
		
		return customerObj.getCustomerDetailsForVerify(object);
	}
	
	@POST
	@Path("/verifyrejectcustomerchanges")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject verifyRejectCustomerChanges(final JSONObject object){
			
		return customerObj.verifyRejectCustomerChanges(object);
	}
	
	@POST
	@Path("/getcustomersforapprove")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCustomerDetailsForApprove(final JSONObject object){
		
		return customerObj.getCustomerDetailsForApprove(object);
	}
	
	@POST
	@Path("/approverejectcustomerchanges")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject approveRejectCustomerChanges(final JSONObject object){
			
		return customerObj.approveRejectCustomerChanges(object);
	}
	
	@POST
	@Path("/getsearchcriterialist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getSearchCriteriaList(final JSONObject object){
			
		return customerObj.getSearchCriteriaList(object);
	}
	
	@POST
	@Path("/fetchsearchtypevalues")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject fetchSearchtypeValues(final JSONObject object){
			
		return customerObj.fetchSearchtypeValues(object);
	}
	
	//Customer Modules Services Ends
	
	
/***************************************Deposits*************************************************/	
	
	//Customer Deposits Module Starts
	
	@POST
	@Path("/getcustomerdeposits")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCustomerDeposits(final JSONObject object){
		
		return depositsObj.getCustomerDeposits(object);
	}
	
	@POST
	@Path("/getcustomerdepositsdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCustomerDepositsDetails(final JSONObject object){
		
		return depositsObj.getCustomerDepositsDetails(object);
	}
	
	@POST
	@Path("/getdepositinstrestandnmmdparameters")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getDepositInstrestAndNmmdParameters(final JSONObject object){
		
		return depositsObj.getDepositInstrestAndNmmdParameters(object);
	} 
	
	@POST
	@Path("/calculateadditional_3mmd")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject calculateadditional_3mmd(final JSONObject object){
		
		return depositsObj.calculateadditional_3mmd(object);
	} 
	
	@POST
	@Path("/calculatesecuritydeposit")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject calculatesecuritydeposit(final JSONObject object){
		
		return depositsObj.calculatesecuritydeposit(object);
	} 
	
	@POST
	@Path("/getdepositinstrestyear")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getDepositInstrestYear(final JSONObject object){
		
		return depositsObj.getDepositInstrestYear(object);
	}
	
	
	@POST
	@Path("/getpendingdepositintrestdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getPendingDepositIntrestDetails(final JSONObject object){
		
		return depositsObj.getPendingDepositIntrestDetails(object);
	} 
	
	@POST
	@Path("/approvedepositsintrest")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject approveDepositsIntrest(final JSONObject object){
		
		return depositsObj.approveDepositsIntrest(object);
	}
	
	//Customer Deposits Module Ends
	
	
/***************************************Cash Section*************************************************/	
	
	//Cash Section Module Services Starts
	
	@POST
	@Path("/getreceiptdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getreceiptDetails(final JSONObject object){
		
		return cashObj.getreceiptDetails(object);
	}
	
	@POST
	@Path("/getreceiptposteddetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getreceiptPostedDetails(final JSONObject object){
		
		return cashObj.getreceiptPostedDetails(object);
	}
	
	@POST
	@Path("/getlistofpayments")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getListOfPayments(final JSONObject object){
		
		return cashObj.getListOfPayments(object);
	}
	
	@POST
	@Path("/getrecieptsforpost")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getRecieptsForPost(final JSONObject object){
		
		return cashObj.getRecieptsForPost(object);
	}
	
	//Cash Section Module Services Ends
	
	
/***************************************Accounts*************************************************/	
	
	//Accounts Module Services Starts
	
	@POST
	@Path("/getbills")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getBills(final JSONObject object){
		
		return accountsObj.getBills(object);
	}
	
	@POST
	@Path("/getbillbreakup")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getBillBreakup(final JSONObject object){
		
		return accountsObj.getBillBreakup(object);
	}
	
	@POST
	@Path("/getbillbreakupslabs")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getBillBreakupSlabs(final JSONObject object){
		
		return accountsObj.getBillBreakupSlabs(object);
	}
	
	@POST
	@Path("/getbillreceiptbreakupslabs")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getBillReceiptBreakupSlabs(final JSONObject object){
		
		return accountsObj.getBillReceiptBreakupSlabs(object);
	}
	
	@POST
	@Path("/getbillslist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getListBillDetails(final JSONObject object){
		
		return accountsObj.getListBillDetails(object);
	}
	
	@POST
	@Path("/getrrdetailsforbillcancel")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getRRDetailsForBillCancel(final JSONObject object){
		
		return accountsObj.getRRDetailsForBillCancel(object);
	}  
	
	@POST
	@Path("/cancelbill")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject cancelBill(final JSONObject object){
		
		return accountsObj.cancelBill(object);
	} 
	
	@POST
	@Path("/getspotfoliodetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getSpotFolioDetails(final JSONObject object){
		
		return accountsObj.getSpotFolioDetails(object);
	} 
	
	@POST
	@Path("/upsertspotfoliodetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertSpotFolioDetails(final JSONObject object){
		
		return accountsObj.upsertSpotFolioDetails(object);
	} 
	
	@POST
	@Path("/regeneratespotfoliodetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject regenerateSpotFolioDetails(final JSONObject object){
		
		return accountsObj.regenerateSpotFolioDetails(object);
	} 
	
	@POST
	@Path("/getfldetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getFLDetails(final JSONObject object){
		
		return accountsObj.getFLDetails(object);
	}
	
	@POST
	@Path("/doaddorupdatedfldetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject doAddOrUpdateFLDetails(final JSONObject object){
		
		return accountsObj.doAddOrUpdateFLDetails(object);
	}
	
	@POST
	@Path("/getchargecodedetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getChargeCodeDetails(final JSONObject object){
		
		return accountsObj.getChargeCodeDetails(object);
	}
	
	@POST
	@Path("/getchargecodedetailsforrebatetype")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getChargeCodeDetailsForRebateType(final JSONObject object){
		
		return accountsObj.getChargeCodeDetailsForRebateType(object);
	}
	
	@POST
	@Path("/getrebatedetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getRebateDetails(final JSONObject object){
		
		return accountsObj.getRebateDetails(object);
	}
	
	@POST
	@Path("/doaddorupdaterebatedetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject doAddOrUpdateRebateDetails(final JSONObject object){
		
		return accountsObj.doAddOrUpdateRebateDetails(object);
	}
	
	@POST
	@Path("/getregularpenality")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getRegularPenaltyDetails(final JSONObject object){
		
		return accountsObj.getRegularPenaltyDetails(object);
	}
	
	@POST
	@Path("/upsertregularpenaltydetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertRegularPenaltyDetails(final JSONObject object){
		
		return accountsObj.upsertRegularPenaltyDetails(object);
	}
	
	@POST
	@Path("/getecsbankaccounttype")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getEcsBankAccountType(final JSONObject object){
		
		return accountsObj.getEcsBankAccountType(object);
	}
	
	@POST
	@Path("/getcustomername")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCustomerName(final JSONObject object){
		
		return accountsObj.getCustomerName(object);
	}
	
	@POST
	@Path("/getecsdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getECSDetails(final JSONObject object){
		
		return accountsObj.getECSDetails(object);
	} 
	
	@POST
	@Path("/upsertecsdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertEcsDetails(final JSONObject object){
		
		return accountsObj.upsertEcsDetails(object);
	}
	
	@POST
	@Path("/getchequedetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getChequeDetails(final JSONObject object){
		
		return accountsObj.getChequeDetails(object);
	}
	
	@POST
	@Path("/dochequedishonour")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject dochequedishonour(final JSONObject object){
		
		return accountsObj.dochequedishonour(object);
	}
	
	@POST
	@Path("/getreceiptdetailsforchequedisno")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getreceiptdetailsforchequedisno(final JSONObject object){
		
		return accountsObj.getreceiptdetailsforchequedisno(object);
	}
	
	@POST
	@Path("/dovalidaterrnumber")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject dovalidaterrnumber(final JSONObject object){
		
		return accountsObj.dovalidaterrnumber(object);
	}
	
	@POST
	@Path("/saveadjustmentrecord")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject saveadjustmentrecord(final JSONObject object,@Context HttpServletRequest request){
		
		return accountsObj.saveadjustmentrecord(object,request);
	}
	
	
	
	@POST
	@Path("/getotherchequedetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getOtherChequeDetails(final JSONObject object){
		
		return accountsObj.getOtherChequeDetails(object);
	}
	
	@POST
	@Path("/getbilledavgunits")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getBilledAvgUnits(final JSONObject object){
		
		return accountsObj.getBilledAvgUnits(object);
	}
	
	@POST
	@Path("/getfixedavgdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getFixedAvgUnits(final JSONObject object){
		
		return accountsObj.getFixedAvgUnits(object);
	}
	
	@POST
	@Path("/upsertmanualaverageunits")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertFixedAvgUnits(final JSONObject object){
		
		return accountsObj.upsertFixedAvgUnits(object);
	}
	
	@POST
	@Path("/getadjustmentdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getAdjustmentDetails(final JSONObject object){
		
		return accountsObj.getAdjustmentDetails(object);
	}
	
	@POST
	@Path("/getreceiptdetailstoadjust")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getreceiptdetailstoadjust(final JSONObject object){
		
		return accountsObj.getreceiptdetailstoadjust(object);
	}
	
	@POST
	@Path("/getdisconnectiondetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getDisconnectionDetails(final JSONObject object){
		
		return accountsObj.getDisconnectionDetails(object);
	}
	
	@POST
	@Path("/getreconnectiondetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getReconnectionDetails(final JSONObject object){
		
		return accountsObj.getReconnectionDetails(object);
	}
	
	@POST
	@Path("/getmrwiseprocessdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getMRWiseProcessDetails(final JSONObject object){
		
		return accountsObj.getMRWiseProcessDetails(object);
	}
	
	@POST
	@Path("/getrrnumberwiseprocessdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getRRNumberWiseProcessDetails(final JSONObject object){
		
		return accountsObj.getRRNumberWiseProcessDetails(object);
	}
	
	@POST
	@Path("/getreadingentrydata")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getReadingEntryData(final JSONObject object){
		
		return accountsObj.getReadingEntryData(object);
	}
	
	@POST
	@Path("/upsertreadingentrydata")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertReadingEntryData(final JSONObject object){
		
		return accountsObj.upsertReadingEntryData(object);
	} 
	
	@POST
	@Path("/getmeterdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getMeterDetails(final JSONObject object){
		
		return accountsObj.getMeterDetails(object);
	} 
	
	@POST
	@Path("/saveremoveassignmeterdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject saveremoveassignmeterdetails(final JSONObject object){
		
		return accountsObj.saveremoveassignmeterdetails(object);
	} 
	
	@POST
	@Path("/getdesignations")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getDesignations(final JSONObject object){
		
		return accountsObj.getDesignations(object);
	}
	
	@POST
	@Path("/getratingdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getRatingDetails(final JSONObject object){
		
		return accountsObj.getRatingDetails(object);
	}
	
	@POST
	@Path("/upsertratingdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertRatingDetails(final JSONObject object){
		
		return accountsObj.upsertRatingDetails(object);
	}
	
	@POST
	@Path("/getdebitdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getDebitDetails(final JSONObject object){
		
		return accountsObj.getDebitDetails(object);
	}
	
	@POST
	@Path("/insertdebit")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject insertDebit(final JSONObject object){
		
		return accountsObj.insertDebit(object);
	}
	
	@POST
	@Path("/getpendingdebits")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getPendingDebits(final JSONObject object){
		
		return accountsObj.getPendingDebits(object);
	}
	
	@POST
	@Path("/approvedebits")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject approveDebits(final JSONObject object){
		
		return accountsObj.approveDebits(object);
	}
	
	@POST
	@Path("/rejectdebits")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject rejectDebits(final JSONObject object){
		
		return accountsObj.rejectDebits(object);
	}
	
	@POST
	@Path("/getcustomercredits")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCustomerCredits(final JSONObject object){
		
		return accountsObj.getCustomerCredits(object);
	}
	
	@POST
	@Path("/getcreditdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCreditDetails(final JSONObject object){
		
		return accountsObj.getCreditDetails(object);
	}
	
	@POST
	@Path("/insertcredit")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject insertCredit(final JSONObject object){
		
		return accountsObj.insertCredit(object);
	}
	
	@POST
	@Path("/getpendingcredits")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getPendingCredits(final JSONObject object){
		
		return accountsObj.getPendingCredits(object);
	}
	
	@POST
	@Path("/getbulkcreditdetailsforapprove")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getBulkCreditDetailsForApprove(final JSONObject object){
		
		return accountsObj.getBulkCreditDetailsForApprove(object);
	}
	
	@POST
	@Path("/approvecredits")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject approveCredits(final JSONObject object,@Context HttpServletRequest request){
		
		return accountsObj.approveCredits(object,request);
	}
	
	@POST
	@Path("/rejectcredits")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject rejectCredits(final JSONObject object,@Context HttpServletRequest request){
		
		return accountsObj.rejectCredits(object,request);
	}
	
	@POST
	@Path("/getpendingdeposits")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getPendingDeposits(final JSONObject object){
		
		return accountsObj.getPendingDeposits(object);
	}
			
	@POST
	@Path("/getwithdrawldetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getWithdrawlDetails(final JSONObject object){
		
		return accountsObj.getWithdrawlDetails(object);
	}
	
	@POST
	@Path("/getwithdrawldetailsfornewentry")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getWithdrawlDetailsForNewEntry(final JSONObject object){
		
		return accountsObj.getWithdrawlDetailsForNewEntry(object);
	}
	
	@POST
	@Path("/insertwithdrawldetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject insertWithdrawlDetails(final JSONObject object){
		
		return accountsObj.insertWithdrawlDetails(object);
	}
	
	@POST
	@Path("/getpendingwithdrawls")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getWithdrawlDetailsForApproval(final JSONObject object){
		
		return accountsObj.getWithdrawlDetailsForApproval(object);
	}
	
	@POST
	@Path("/approverejectwithdrawals")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject approveRejectWithdrawals(final JSONObject object){
		
		return accountsObj.approveRejectWithdrawals(object);
	}
	
	//Accounts Module Services Ends
	

/***************************************Main*************************************************/	
	
	//Main Module Services Starts
	
	@POST
	@Path("/generateserversidebill")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject generateServerSideBill(final JSONObject object, @Context HttpServletRequest request){
		
		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = serverSideBillObj.generateServerSideBill(object,request);
		
		return json;
	}
	
	@POST
	@Path("/mrtomrrransfer")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject mrToMrTransfer(final JSONObject object){
		
		return serverSideBillObj.mrToMrTransfer(object);
	}
	
	//Main Module Services Ends
	
	
/***************************************Infrastructure*************************************************/
	
	//Infrastructure Module Services Starts
	
	@POST
	@Path("/getlocationdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getLocationDetails(final JSONObject object){
		
		return infrastructureObj.getLocationDetails(object);
	}
	
	@POST
	@Path("/getstationdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getStationDetails(final JSONObject object){
		
		return infrastructureObj.getStationDetails(object);
	}
	
	@POST
	@Path("/upsertstationdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertStationDetails(final JSONObject object){
		
		return infrastructureObj.upsertStationDetails(object);
	}
	
	@POST
	@Path("/getfeederdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getFeederDetails(final JSONObject object){
		
		return infrastructureObj.getFeederDetails(object);
	}
	
	@POST
	@Path("/upsertfeederdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertFeederDetails(final JSONObject object){
		
		return infrastructureObj.upsertFeederDetails(object);
	}
	
	@POST
	@Path("/getdtcdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getDTCDetails(final JSONObject object){
		
		return infrastructureObj.getDTCDetails(object);
	}
	
	@POST
	@Path("/upsertdtcdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertDTCDetails(final JSONObject object){
		
		return infrastructureObj.upsertDTCDetails(object);
	}
	
	@POST
	@Path("/getomdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getOMDetails(final JSONObject object){
		
		return infrastructureObj.getOMDetails(object);
	}
	
	@POST
	@Path("/upsertomdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertOMDetails(final JSONObject object){
		
		return infrastructureObj.upsertOMDetails(object);
	}
	
	@POST
	@Path("/gettowndetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getTownDetails(final JSONObject object){
		
		return infrastructureObj.getTownDetails(object);
	}
	
	@POST
	@Path("/getdtctmapdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getDTCTownMapDetails(final JSONObject object){
		
		return infrastructureObj.getDTCTownMapDetails(object);
	}
	
	@POST
	@Path("/mapdtctown")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject mapDTCTown(final JSONObject object){
		
		return infrastructureObj.mapDTCTown(object);
	}
	
	@POST
	@Path("/getdtcdetailsfortransfer")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getDTCDetailsForTransfer(final JSONObject object){
		
		return infrastructureObj.getDTCDetailsForTransfer(object);
	}
	
	@POST
	@Path("/dtctransfer")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject dtcTransfer(final JSONObject object){
		
		return infrastructureObj.dtcTransfer(object);
	} 
	
	//Infrastructure Module Services Ends
	

	
/***************************************Energy Audit*************************************************/
	
	//Energy Audit Module Services Ends
	
	@POST
	@Path("/getdtcmetermaster")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getDTCMeterMasterDetails(final JSONObject object){
		
		return energyauditObj.getDTCMeterMasterDetails(object);
	}
	
	@POST
	@Path("/upsertdtcmetermasterdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertDTCMeterMasterDetails(final JSONObject object){
		
		return energyauditObj.upsertDTCMeterMasterDetails(object);
	}
	
	@POST
	@Path("/getdtcmeterreading")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getDTCMeterReadingDetails(final JSONObject object){
		
		return energyauditObj.getDTCMeterReadingDetails(object);
	}
	
	@POST
	@Path("/getfeederenergyaudit")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getFeederEnergyAuditDetails(final JSONObject object){
		
		return energyauditObj.getFeederEnergyAuditDetails(object);
	} 
	
	@POST
	@Path("/upsertfeederenergyauditdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertFeederEnergyAuditDetails(final JSONObject object){
		
		return energyauditObj.upsertFeederEnergyAuditDetails(object);
	}
	
	@POST
	@Path("/getentryrecordsforassessed")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getentryrecordsforassessed(final JSONObject object){
		
		return energyauditObj.getentryrecordsforassessed(object);
	}
	
	@POST
	@Path("/save_assessed_consumption_details")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject save_assessed_consumption_details(final JSONObject object){
		
		return energyauditObj.save_assessed_consumption_details(object);
	}
	
	
	//Energy Audit Module Services Ends
	
/***************************************User*************************************************/	
	
	//User Module Services starts
	
	@POST
	@Path("/validateuserid")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject validateUserID(final JSONObject object){
			
		return userObj.validateUserID(object);
	}
	
	@POST
	@Path("/validateuser")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject authenticateUser(final JSONObject object, @Context HttpServletRequest request){
		String ipAdress = request.getRemoteHost();
		return userObj.authenticateUser(object, ipAdress);
	}
	
	@POST
	@Path("/validateuserRefresh")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject authenticateUserRefresh(final JSONObject object){
		System.out.println(object);
		return userObj.authenticateUserRefresh(object);
	}
		
	@POST
	@Path("/logout")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject logOutSessionData(final JSONObject object){
		
		return userObj.logOutSessionData(object);
	}
	
	@POST
	@Path("/getuserroles")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getUserRoles(final JSONObject object){
		
		return userObj.getUserRoles(object);
	}
	
	@POST
	@Path("/upsertuserroles")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertUserRoles(final JSONObject object){
		
		return userObj.upsertUserRoles(object);
	}
	
	@POST
	@Path("/getusermasterdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getUserMasterDetails(final JSONObject object){
		
		return userObj.getUserMasterDetails(object);
	}
	
	@POST
	@Path("/upsertusermaster")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertUserMaster(final JSONObject object){
		
		return userObj.upsertUserMaster(object);
	}
	
	@POST
	@Path("/deleteusermaster")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject deleteUserMaster(final JSONObject object){
		
		return userObj.deleteUserMaster(object);
	}
	
	@POST
	@Path("/getusersessiondetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getUserSessionDetails(final JSONObject object){
		
		return userObj.getUserSessionDetails(object);
	}
	
	@POST
	@Path("/updateusersessions")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject updateUserSessions(final JSONObject object){
		
		return userObj.updateUserSessions(object);
	}
	
	@POST
	@Path("/getuserdeligationdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getUserDeligationDetails(final JSONObject object){
		
		return userObj.getUserDeligationDetails(object);
	}
	
	@POST
	@Path("/upsertuserdeligation")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject upsertUserDeligation(final JSONObject object){
		
		return userObj.upsertUserDeligation(object);
	}
	
	@POST
	@Path("/changepassword")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject changepassword(final JSONObject object){
		
		return userObj.changepassword(object);
	}
	
	
	//User Module Services Ends
	
	
/***************************************Preload Picklists*************************************************/	
	
	//Preload Picklist Services Starts
	
	@POST
	@Path("/getcamplist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCampList(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getCampList(object);
		
		return json;
	}
	
	@POST
	@Path("/getzonelist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getZoneList(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getZoneList(object);
		
		return json;
	}
	
	@POST
	@Path("/getdivisionlist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getDivisionList(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getDivisionList(object);
		
		return json;
	}
	
	@POST
	@Path("/getcirclelist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCircleList(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getCircleList(object);
		
		return json;
	}
	
	
	@POST
	@Path("/getlocationlist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getLocationList(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getLocationList(object);
		
		return json;
	}
	
	@POST
	@Path("/getomunitlist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getOMUnitList(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getOMUnitList(object);
		
		return json;
	}
	
	@POST
	@Path("/gettarifflist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getTariffList(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getTariffList(object);
		
		return json;
	}
	
	@POST
	@Path("/getchargedescriptionlist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getChargeDescriptionList(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getChargeDescriptionList(object);
		
		return json;
	}

	@POST
	@Path("/getpaymentpurposelist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getPaymentPurposeList(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getPaymentPurposeList(object);
		
		return json;
	}

	@POST
	@Path("/gethrtlocationcounterdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getLocationCounterDetails(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getLocationCounterDetails(object);
		
		return json;
	}
	
	@POST
	@Path("/getcashcounternumber")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCashCounterNoFromInitialReceiptPayment(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getCashCounterNoFromInitialReceiptPayment(object);
		
		return json;
	}
	
	@POST
	@Path("/getCodeDescrByCodeValue")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCodeDescrByCodeValue(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getCodeDescrByCodeValue(object);
		
		return json;
	}
	
	@POST
	@Path("/getCodeValueByCodeDescription")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCodeValueByCodeDescription(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getCodeValueByCodeDescription(object);
		
		return json;
	}
	
	@POST
	@Path("/getConsumerMasterPickListValues")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getConsumerMasterPickListValues(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getConsumerMasterPickListValues(object);
		
		return json;
	}
	
	@POST
	@Path("/getrebatetypelist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getrebatetypelist(final JSONObject object){
		
		return pickListObj.getrebatetypelist(object);
	}
	
	@POST
	@Path("/getapealtype")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getApealType(final JSONObject object){
		
		return pickListObj.getApealType(object);
	}
	
	@POST
	@Path("/getmrcodelist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCustomerMeterReaderCodeList(final JSONObject object){
		
		return pickListObj.getCustomerMeterReaderCodeList(object);
	}
	
	@POST
	@Path("/getmrdaylist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getCustomerMeterReadingDayList(final JSONObject object){
		
		return pickListObj.getCustomerMeterReadingDayList(object);
	}
	
	@POST
	@Path("/getmeterreadercodelist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getMeterReaderCodeList(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getMeterReaderCodeList(object);
		
		return json;
	}
	
	@POST
	@Path("/getmeterreadingdaylist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getMeterReadingDayList(final JSONObject object){

		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = pickListObj.getMeterReadingDayList(object);
		
		return json;
	}
	
	//Preload Picklist Services Ends
	
	//Android Billing starts here

	
	@POST
	@Path("/handshake")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject HandShake(final JSONObject object, @Context HttpServletRequest request){

		System.out.println("Incoming Connection.....IMEI_NO : "+(String)object.get("imei_no") + " USER : "+(String)object.get("user_id"));
		object.put("status", "success");
		
		return object;
		
		
	}
	
	@POST
	@Path("/getRecordCount")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getRecordCount(final JSONObject object){

		return  androidBilling.getRecordCountForTodays(object);
		
	}
	
	@POST
	@Path("/getBillingDataFromTable")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getBillingData(final JSONObject object){

		return  androidBilling.getBillingDataFromTable(object);
		
	}
	
	@POST
	@Path("/uploadsinglerecordfromjobscheduler")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject uploadsinglerecordfromjobscheduler(final JSONObject object){

		return  androidBilling.uploadsinglerecordfromjobscheduler(object);
		
	}
	//Android Billing Ends
	
	//Reports Implementation
	
	@POST
	@Path("/generateBillingEfficiency")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject generateBillingEfficiency(final JSONObject object, @Context HttpServletRequest request, @Context HttpServletResponse response){

		return  reportObj.generateBillingEfficiency(request, response,object);
		
	}
	
	@POST
	@Path("/generateCollectionEfficiency")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject generateCollectionEfficiency(final JSONObject object, @Context HttpServletRequest request, @Context HttpServletResponse response){

		return  reportObj.generateCollectionEfficiency(request, response,object);
		
	}
	
	@POST
	@Path("/getPaymentPurposewiseReport")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getPaymentPurposewiseReport(final JSONObject object, @Context HttpServletRequest request, @Context HttpServletResponse response){

		return  reportObj.getPaymentPurposewiseReport(request, response,object);
		
	}
	
	@GET
	@Path("/downloadreport")
	/*@Consumes({MediaType.APPLICATION_JSON})*/
	/*@Produces({MediaType.APPLICATION_OCTET_STREAM} )*/
	@Produces("application/pdf")
	public Response downloadreport(@Context HttpServletRequest request, @Context HttpServletResponse response){
		System.out.println("downloading report......."+request.getParameter("conn_type"));
		
		return reportObj.downloadreport(request,response);
	}  
	
	/*
	 * @POST
	 * 
	 * @Path("/generate_other_reports")
	 * 
	 * @Consumes({MediaType.APPLICATION_JSON})
	 * 
	 * @Produces({MediaType.APPLICATION_JSON}) public JSONObject
	 * generate_other_reports(final JSONObject object, @Context HttpServletRequest
	 * request, @Context HttpServletResponse response){
	 * 
	 * return othersObj.generate_other_reports(request, response,object);
	 * 
	 * }
	 */
	
	@GET
	@Path("/generate_other_reports")
	@Produces("application/pdf")
	public Response generate_other_reports(@Context HttpServletRequest request, @Context HttpServletResponse response){
		System.out.println("downloading report......."+request.getParameter("conn_type"));
		
		return othersObj.generate_other_reports(request,response);
	}  
	
	@GET
	@Path("/generate_bill_cancel_report")
	@Produces("application/pdf")
	public Response generate_bill_cancel_report(@Context HttpServletRequest request, @Context HttpServletResponse response){
		System.out.println("downloading report......."+request.getParameter("conn_type"));
		
		return othersObj.generate_bill_cancel_report(request,response);
	} 
	
	@GET
	@Path("/generate_credit_report")
	@Produces("application/pdf")
	public Response generate_credit_report(@Context HttpServletRequest request, @Context HttpServletResponse response){
		System.out.println("downloading report......."+request.getParameter("conn_type"));
		
		return othersObj.generate_credit_report(request,response);
	} 
	
	@GET
	@Path("/generate_debit_withdrawal_report")
	@Produces("application/pdf")
	public Response generate_debit_withdrawal_report(@Context HttpServletRequest request, @Context HttpServletResponse response){
		System.out.println("downloading report......."+request.getParameter("conn_type"));
		
		return othersObj.generate_debit_withdrawal_report(request,response);
	} 
	
	@GET
	@Path("/generate_adjustment_report")
	@Produces("application/pdf")
	public Response generate_adjustment_report(@Context HttpServletRequest request, @Context HttpServletResponse response){
		System.out.println("downloading report......."+request.getParameter("conn_type"));
		
		return othersObj.generate_adjustment_report(request,response);
	} 
	
	@GET
	@Path("/generate_dlrmnr_report")
	@Produces("application/pdf")
	public Response generate_dlrmnr_report(@Context HttpServletRequest request, @Context HttpServletResponse response){
		System.out.println("downloading report......."+request.getParameter("conn_type"));
		
		return othersObj.generate_dlrmnr_report(request,response);
	} 
	
	@GET
	@Path("/generate_present_reading_less_report")
	@Produces("application/pdf")
	public Response generate_present_reading_less_report(@Context HttpServletRequest request, @Context HttpServletResponse response){
		System.out.println("downloading report......."+request.getParameter("conn_type"));
		
		return othersObj.generate_present_reading_less_report(request,response);
	} 
	
	@GET
	@Path("/generate_abnormal_subnormal_report")
	@Produces("application/pdf")
	public Response generate_abnormal_subnormal_report(@Context HttpServletRequest request, @Context HttpServletResponse response){
		System.out.println("downloading report......."+request.getParameter("conn_type"));
		
		return othersObj.generate_abnormal_subnormal_report(request,response);
	} 
	
	@GET
	@Path("/generate_zero_consumption_report")
	@Produces("application/pdf")
	public Response generate_zero_consumption_report(@Context HttpServletRequest request, @Context HttpServletResponse response){
		System.out.println("downloading report......."+request.getParameter("conn_type"));
		
		return othersObj.generate_zero_consumption_report(request,response);
	}
	
	@GET
	@Path("/generate_high_value_report")
	@Produces("application/pdf")
	public Response generate_high_value_report(@Context HttpServletRequest request, @Context HttpServletResponse response){
		System.out.println("downloading report......."+request.getParameter("conn_type"));
		
		return othersObj.generate_high_value_report(request,response);
	}
	
	
	@POST
	@Path("/dashboard_billingefficiency_comparision")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject dashboard_billingefficiency_comparision(final JSONObject object, @Context HttpServletRequest request, @Context HttpServletResponse response){

		return  reportObj.dashboard_billingefficiency_comparision(request, response,object);
		
	}
	
	
	@POST
	@Path("/maindashboard")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject maindashboard(final JSONObject object, @Context HttpServletRequest request, @Context HttpServletResponse response){

		return  reportObj.maindashboard(request, response,object);
		
	}
	
	@POST
	@Path("/getfinancialyears")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getfinancialyears(final JSONObject object, @Context HttpServletRequest request, @Context HttpServletResponse response){

		return  reportObj.getfinancialyears(request, response,object);
		
	}
	
	
	@POST
	@Path("/getdcb")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getdcb(final JSONObject object, @Context HttpServletRequest request, @Context HttpServletResponse response){

		return  reportObj.getdcb(request, response,object);
		
	}
	
	@POST
	@Path("/getsbdreports")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getsbdreports(final JSONObject object, @Context HttpServletRequest request, @Context HttpServletResponse response){

		return  reportObj.getsbdreports(request, response,object);
		
	}
	
	@POST
	@Path("/getotherreportstypelist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getotherreportstypelist(final JSONObject object, @Context HttpServletRequest request, @Context HttpServletResponse response){

		return  reportObj.getotherreportstypelist(request, response,object);
		
	}
	
	@POST
	@Path("/getledgernolist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getledgernolist(final JSONObject object, @Context HttpServletRequest request, @Context HttpServletResponse response){

		return  reportObj.getledgernolist(request, response,object);
		
	}
	
	
	@POST
	@Path("/getexceptionreportstypelist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getexceptionreportstypelist(final JSONObject object, @Context HttpServletRequest request, @Context HttpServletResponse response){

		return  reportObj.getexceptionreportstypelist(request, response,object);
		
	}
	
	@POST
	@Path("/getexceptionreports_camplist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getexceptionreports_camplist(final JSONObject object){

		return  reportObj.getexceptionreports_camplist(object);
		
	}
	
	@POST
	@Path("/getuseridlist_billcancelreport")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getuseridlist_billcancelreport(final JSONObject object){

		return  reportObj.getuseridlist_billcancelreport(object);
		
	}
	
	@POST
	@Path("/getcredittypelist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getcredittypelist(final JSONObject object){

		return  reportObj.getcredittypelist(object);
		
	}
	
	@POST
	@Path("/getchargedescriptionreportlist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getchargedescriptionlist(final JSONObject object){

		return  reportObj.getchargedescriptionlist(object);
		
	}
	
	@POST
	@Path("/getgramapanchayathlist")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getgramapanchayathlist(final JSONObject object){

		return  reportObj.getgramapanchayathlist(object);
		
	}
	
	///////////////////////////
	
	//Receipt Generation
	
	@POST
	@Path("/getbilldetailsinreceiptgen")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getbilldetailsinreceiptgen(final JSONObject object){
		
		return cashObj.getbilldetailsinreceiptgen(object);
	}
	
	
	@POST
	@Path("/getreceiptsummarydetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getreceiptsummarydetails(final JSONObject object){
		
		return cashObj.getreceiptsummarydetails(object);
	}
	
	@POST
	@Path("/getreceiptsummarydetailshrt")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getreceiptsummarydetailshrt(final JSONObject object){
		
		return cashObj.getreceiptsummarydetailshrt(object);
	}
	
	@POST
	@Path("/savereceiptdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject savereceiptdetails(final JSONObject object, @Context HttpServletRequest request,@Context HttpServletResponse response){
		
		JSONObject json = new JSONObject();
		
		System.out.println(object);
		
		json = cashObj.savereceiptdetails(object,request,response);
		
		return json;
	}
	
	
	@POST
	@Path("/dorecepitposting")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject dorecepitposting(final JSONObject object,@Context HttpServletRequest request){
		
		return cashObj.dorecepitposting(object,request);
	}
	
	@POST
	@Path("/doreceiptreposting")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject doreceiptreposting(final JSONObject object,@Context HttpServletRequest request){
		
		return cashObj.doreceiptreposting(object,request);
	}
	
	@POST
	@Path("/doreconcilation")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject doreconcilation(final JSONObject object,@Context HttpServletRequest request){
		
		return cashObj.doreconcilation(object,request);
	}
	
	
	@POST
	@Path("/verifyreceiptnumber")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject verifyreceiptnumber(final JSONObject object, @Context HttpServletRequest request){
		
		System.out.println(object);
		return cashObj.verifyreceiptnumber(object,request);
	}
	
	@POST
	@Path("/getfirstreceiptnumber")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getfirstreceiptnumber(final JSONObject object, @Context HttpServletRequest request){
		
		System.out.println(object);
		return cashObj.getfirstreceiptnumber(object,request);
	}
	
	@POST
	@Path("/savereceiptdetailsmanualreceipts")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject savereceiptdetailsmanualreceipts(final JSONObject object, @Context HttpServletRequest request,@Context HttpServletResponse response){
		
		System.out.println(object);
		return cashObj.savereceiptdetailsManaualReceipts(object,request,response);
		
	}
	
	@POST
	@Path("/getreceiptdetailstocancel")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getreceiptdetailstocancel(final JSONObject object){
		
		return cashObj.getreceiptdetailstocancel(object);
	}
	
	@POST
	@Path("/getreceiptdetailstocancel_hrt")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getreceiptdetailstocancel_hrt(final JSONObject object){
		
		return cashObj.getreceiptdetailstocancel_hrt(object);
	}
	
	@POST
	@Path("/getchequedetailstocancel")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getchequedetailstocancel(final JSONObject object){
		
		return cashObj.getchequedetailstocancel(object);
	}
	
	@POST
	@Path("/getchequedetailstocancel_hrt")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getchequedetailstocancel_hrt(final JSONObject object){
		
		return cashObj.getchequedetailstocancel_hrt(object);
	}
	
	@POST
	@Path("/docancelreceipts")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject docancelreceipts(final JSONObject object){
		
		return cashObj.docancelreceipts(object);
	}
	
	@POST
	@Path("/docancelreceipts_hrt")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject docancelreceipts_hrt(final JSONObject object){
		
		return cashObj.docancelreceipts_hrt(object);
	}
	
	@POST
	@Path("/docancelcheques")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject docancelcheques(final JSONObject object){
		
		return cashObj.docancelcheques(object);
	}
	
	@POST
	@Path("/docancelcheques_hrt")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject docancelcheques_hrt(final JSONObject object){
		
		return cashObj.docancelcheques_hrt(object);
	}
	
	@POST
	@Path("/uploadmanualreceipts")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject uploadmanualreceipts(final JSONObject object){
		
		return cashObj.uploadmanualreceipts(object);
	}
	
	@POST
	@Path("/getprocessdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getprocessdetails(final JSONObject object){
		
		return cashObj.getprocessdetails(object);
	}
	
	@POST
	@Path("/getrrnumberdetails")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject getrrnumberdetails(final JSONObject object){
		
		return cashObj.getrrnumberdetails(object);
	}
	
	@POST
	@Path("/doprocessmrreset")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject doprocessmrreset(final JSONObject object){
		
		return cashObj.doprocessmrreset(object);
	}
	
	@POST
	@Path("/doprocessrrreset")
	@Consumes({MediaType.APPLICATION_JSON})
	@Produces({MediaType.APPLICATION_JSON})
	public JSONObject doprocessrrreset(final JSONObject object){
		
		return cashObj.doprocessrrreset(object);
	}
	
	
	////////////////
	
}
