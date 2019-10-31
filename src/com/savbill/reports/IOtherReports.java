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
public interface IOtherReports {

	Response generate_other_reports(HttpServletRequest request, HttpServletResponse response);

	Response generate_bill_cancel_report(HttpServletRequest request, HttpServletResponse response);

	Response generate_credit_report(HttpServletRequest request, HttpServletResponse response);

	Response generate_debit_withdrawal_report(HttpServletRequest request, HttpServletResponse response);

}
