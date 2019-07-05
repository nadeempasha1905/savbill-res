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
}
