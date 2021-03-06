package com.savbill.cash;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.sf.json.JSONObject;

public interface ICashManagement {
	
	JSONObject getreceiptDetails(JSONObject object);
	JSONObject getreceiptPostedDetails(JSONObject object);
	JSONObject getListOfPayments(JSONObject object);
	JSONObject getRecieptsForPost(JSONObject object);
	JSONObject getbilldetailsinreceiptgen(JSONObject object);
	JSONObject getreceiptsummarydetails(JSONObject object);
	JSONObject savereceiptdetails(JSONObject object, HttpServletRequest request, HttpServletResponse response);
	JSONObject dorecepitposting(JSONObject object, HttpServletRequest request);
	JSONObject doreconcilation(JSONObject object, HttpServletRequest request);
	JSONObject doreceiptreposting(JSONObject object, HttpServletRequest request);
	JSONObject verifyreceiptnumber(JSONObject object, HttpServletRequest request);
	JSONObject savereceiptdetailsManaualReceipts(JSONObject object, HttpServletRequest request,
			HttpServletResponse response);
	JSONObject getfirstreceiptnumber(JSONObject object, HttpServletRequest request);
	JSONObject getreceiptdetailstocancel(JSONObject object);
	JSONObject getchequedetailstocancel(JSONObject object);
	JSONObject docancelreceipts(JSONObject object);
	JSONObject docancelcheques(JSONObject object);
	JSONObject uploadmanualreceipts(JSONObject object);
	JSONObject getprocessdetails(JSONObject object);
	JSONObject getrrnumberdetails(JSONObject object);
	JSONObject doprocessmrreset(JSONObject object);
	JSONObject doprocessrrreset(JSONObject object);
	JSONObject getreceiptsummarydetailshrt(JSONObject object);
	JSONObject getreceiptdetailstocancel_hrt(JSONObject object);
	JSONObject getchequedetailstocancel_hrt(JSONObject object);
	JSONObject docancelreceipts_hrt(JSONObject object);
	JSONObject docancelcheques_hrt(JSONObject object);
}
