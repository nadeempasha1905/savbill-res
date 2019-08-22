/**
 * 
 */
package com.savbill.reports;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import net.sf.json.JSONObject;

/**
 * @author DELL
 *
 */
public interface IReportGeneration {
	
	JSONObject generateBillingEfficiency( HttpServletRequest request, HttpServletResponse response, JSONObject object);

	Response downloadreport(HttpServletRequest request, HttpServletResponse response);

	JSONObject dashboard_billingefficiency_comparision(HttpServletRequest request, HttpServletResponse response,
			JSONObject object);

	JSONObject getfinancialyears(HttpServletRequest request, HttpServletResponse response, JSONObject object);

	JSONObject maindashboard(HttpServletRequest request, HttpServletResponse response, JSONObject object);

	JSONObject getdcb(HttpServletRequest request, HttpServletResponse response, JSONObject object);

	JSONObject getsbdreports(HttpServletRequest request, HttpServletResponse response, JSONObject object);

	JSONObject generateCollectionEfficiency(HttpServletRequest request, HttpServletResponse response, JSONObject object);

	JSONObject getPaymentPurposewiseReport(HttpServletRequest request, HttpServletResponse response, JSONObject object);

}
