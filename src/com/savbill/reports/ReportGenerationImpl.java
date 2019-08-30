/**
 * 
 */
package com.savbill.reports;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.ResultSet;
import java.util.HashMap;
import java.util.ResourceBundle;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

import com.savbill.database.DBManagerIMPL;
import com.savbill.database.DBQueries;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.jasperreports.engine.query.JRQueryExecuterFactory;
import net.sf.jasperreports.engine.util.JRProperties;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

/**
 * @author DELL
 *
 */
public class ReportGenerationImpl implements IReportGeneration {
	
	// start of static block  
    static { 
       
        System.out.println("static block called "); 
        
        /* You may need to execute following line just once in your application.
        You can try to move it into initialization or static code block.
        Also you may notice that line below is marked as deprecated. 
        However in my case the export to pdf fails without this line being executed. */
    
      JRProperties.setProperty( JRQueryExecuterFactory.QUERY_EXECUTER_FACTORY_PREFIX+"plsql"
                              ,"com.jaspersoft.jrx.query.PlSqlQueryExecuterFactory");
    
      /* Prepare Jasper print and exporter objects in lines below */
    } 
    // end of static block  

	// database connection initialize
	DBManagerIMPL databaseObj = new DBManagerIMPL();
	Connection dbConnection;
	
	ResourceBundle propsBundle=ResourceBundle.getBundle("savbilldb");
	String REPORT_PATH = propsBundle.getString("REPORT_PATH");

	@Override
	public JSONObject generateBillingEfficiency(HttpServletRequest request, HttpServletResponse response,
			JSONObject object) {
		// TODO Auto-generated method stub

		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();

		String conn_type = object.getString("conn_type");
		String report_wise = object.getString("report_wise");
		String selected_location = object.getString("selected_location");
		String metercode = object.getString("metercode");
		String tariffcodes = object.getString("tariffcodes");
		String fromdate = object.getString("fromdate");
		String todate = object.getString("todate");
		Boolean header = object.getBoolean("header");
		
		System.out.println(object);

		try {

			if (conn_type.equalsIgnoreCase("LT")) {

				dbConnection = databaseObj.getDatabaseConnection();

			} else if (conn_type.equals("HT")) {
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			//accountsCS = dbConnection.prepareCall(DBQueries.REPORT_GET_BILLING_EFFICIENCY);
			accountsCS = dbConnection.prepareCall("{ call  GET_BILLING_EFFICIENCY "
					+ " (?, '"+report_wise+"', '"+selected_location+"', '"+metercode+"', "
							+ "'"+tariffcodes+"', '"+fromdate+"', '"+todate+"', "+header+")}");
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			
			/*
			 * accountsCS.setString(2, report_wise); accountsCS.setString(3,
			 * selected_location); accountsCS.setString(4, metercode);
			 * accountsCS.setString(5, tariffcodes); accountsCS.setString(6, fromdate);
			 * accountsCS.setString(7, todate); accountsCS.setBoolean(8, true);
			 */
			 
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			System.out.println(accountsRS);
			while (accountsRS.next()) {
				JSONObject ackobj = new JSONObject();

				System.out.println(accountsRS.getString("row_no"));
				ackobj.put("row_no", accountsRS.getString("row_no"));
				ackobj.put("trf", accountsRS.getString("trf"));
				ackobj.put("tobebilled", accountsRS.getString("tobebilled"));
				ackobj.put("billed", accountsRS.getString("billed"));
				ackobj.put("notbilled", accountsRS.getString("notbilled"));
				ackobj.put("billeff", accountsRS.getString("billeff"));
				ackobj.put("nor", accountsRS.getString("nor"));
				ackobj.put("dl", accountsRS.getString("dl"));
				ackobj.put("mnr", accountsRS.getString("mnr"));
				ackobj.put("dc", accountsRS.getString("dc"));
				ackobj.put("vacant", accountsRS.getString("vacant"));
				array.add(ackobj);

			}
			if (array.isEmpty()) {
				// no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else {
				obj.put("status", "success");
				obj.put("message", "Billing Efficiency Details Retrieved !!!");
				obj.put("BILLING_EFFICIENCY", array);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}

		return obj;
	}

	@Override
	public Response downloadreport(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		HashMap parameterMap = new HashMap();
		String path = null ;
		
		String report_type = request.getParameter("report_type").trim();
		String fileName = "";
		String report = "";
		String report_title = "";
		
		String conn_type = request.getParameter("conn_type");
		String report_wise = request.getParameter("report_wise");
		String selected_location = request.getParameter("selected_location");
		String metercode = request.getParameter("metercode");
		String tariffcodes = request.getParameter("tariffcodes");
		String fromdate = request.getParameter("fromdate");
		String todate = request.getParameter("todate");
		String month_year = request.getParameter("month_year");
		String header = request.getParameter("header");
		String username = request.getParameter("username");
		String companyname = "Mescom Electricity Company Limited";
		
		String station_code = request.getParameter("station_code");
		String feeder_code = request.getParameter("feeder_code");
		String transformer_code = request.getParameter("transformer_code");
		String gps = request.getParameter("gps");
		
		String rr_number = request.getParameter("rr_number");
		String no_of_months = request.getParameter("no_of_months");
		String om_code = request.getParameter("om_code");
		String reading_day = request.getParameter("reading_day");
		
		
		
		
		try {
		if (conn_type.equalsIgnoreCase("LT")) {
				dbConnection = databaseObj.getDatabaseConnection();
			
			} else if (conn_type.equals("HT")) {
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		if(report_type.length() > 0){
			
			if(report_type.equals("BILLING_EFF")){
				fileName = "SAVBillingEfficiencyReport";
				report_title = "Billing Efficiency Report";
			}else if(report_type.equals("MAIN_DCB")){
				fileName = "DCBReport";
				report_title = "DCB for the month of "+month_year;
			}else if(report_type.equals("LIST_BILLS")){
				fileName = "ListBillReport";
				report_title = "List of Bill Details";
			}

	        try {
	        	String contextPath = request.getServletContext().getRealPath("ReportGenerationImpl.java");
	        	File contextfile = new File(contextPath);
	        	String parentfile = contextfile.getParent();
	        	
	        	/*File f = new File(parentfile+"/Jaspers/" + fileName );*/
	        	File f = new File(REPORT_PATH+ fileName );
				path = f.getPath();
				
				/*String imgPath = "";
					imgPath = request.getServletContext().getRealPath("app/images/mescom_logo.jpg");*/
				
				String imgPath = REPORT_PATH + "mescom_logo.jpg";
				//imgPath = request.getServletContext().getRealPath("app/images/mescom_logo.jpg");
				
					
				/*	if(om_code != null && om_code.length()>0){
						subdivision = om_code;
					}*/
	            
	            parameterMap.put("REPORT_TITLE", report_title);
	            parameterMap.put("REPORT_TYPE", report_type);
	            parameterMap.put("report_type", report_type);
	            parameterMap.put("locationcode", selected_location);
	            parameterMap.put("subdivision", selected_location);
	            parameterMap.put("fromDate", fromdate);
	            parameterMap.put("toDate", todate);
	            parameterMap.put("month_year", month_year);
	            parameterMap.put("username", username);
	            parameterMap.put("reporttitle", report_title);
	            parameterMap.put("companyname", companyname);
	            parameterMap.put("grouptype", report_wise);
	            parameterMap.put("mrcode", metercode);
	            parameterMap.put("tariffs", tariffcodes);
	            
	            parameterMap.put("station_code", station_code);
	            parameterMap.put("feeder_code", feeder_code);
	            parameterMap.put("transformer_code", transformer_code);
	            parameterMap.put("gps", gps);
	            
	            parameterMap.put("rr_number", rr_number);
	            parameterMap.put("no_of_months", no_of_months);
	            parameterMap.put("om_code", om_code);
	            parameterMap.put("reading_day", reading_day);
	            
	            System.out.println(parameterMap);
	            
	        } catch (Exception e) {
	            e.printStackTrace();
	        } finally {
	            try {
	            	//dbConn.close();
	            } catch (Exception e) {
	                e.printStackTrace();
	            }
	        }
	        
	        response.setContentType("application/pdf");
	        
	        
	        
			JasperPrint jasperPrint = null;
			InputStream input = null;
			ServletOutputStream outStream = null;
			FileInputStream fin = null;
			try {

				input = new FileInputStream(new File(path+ ".jasper"));
				//System.out.println("before going to conn");
				
				jasperPrint = JasperFillManager.fillReport(input,parameterMap,dbConnection);
				//System.out.println("after going to conn");

				JRPdfExporter pdfExporter = new JRPdfExporter();
	
				pdfExporter.setParameter(JRExporterParameter.JASPER_PRINT,jasperPrint);
				//System.out.println("dir abspath .pdf " + dir.getPath()+ "/" + fname + ".pdf");
				//pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, dir.getPath()+ "/" + PDFname + ".pdf");
				
				//System.out.println("dir abspath .pdf " + path);
				pdfExporter.setParameter(JRExporterParameter.OUTPUT_FILE_NAME, path+ ".pdf");
				
				pdfExporter.exportReport();
				
				JasperExportManager.exportReportToPdfFile(jasperPrint,path+ ".pdf");
				
				fin = new FileInputStream(new File(path+ ".pdf"));
				outStream = response.getOutputStream();
				// SET THE MIME TYPE.
				response.setContentType("application/pdf");
				// set content dispostion to attachment in with file name.
				// case the open/save dialog needs to appear.
				response.setHeader("Content-Disposition", "attachment;filename="+fileName+".pdf");
				

				byte[] buffer = new byte[1024];
				int n = 0;
				while ((n = fin.read(buffer)) != -1) {
				outStream.write(buffer, 0, n);
				}
				

				outStream.flush();
				fin.close();
				outStream.close();
				
			} catch (JRException e) {
				System.out.println("ERRR IN iREPORT GENERATION FROM SERVER");
				//rbo.setReport_status("E");
				//ReferenceUtill.updateReportStatus(rbo);
				e.printStackTrace();
			} catch (FileNotFoundException e) {
				System.out.println("ERRR IN iREPORT GENERATION FROM SERVER");
				e.printStackTrace();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
		byte[] buffer2 = null;
		  try {
		   File file = new File(path+ ".pdf");
		   FileInputStream fis = new FileInputStream(file);
		   buffer2 = IOUtils.toByteArray(fis);
		   fis.close();
		  } catch (Exception e) {
		   e.printStackTrace();
		  }
		   System.out.println(buffer2);
		  return Response.ok(buffer2)
		    .header("Content-Length", buffer2.length)
		    .header("Content-Disposition","attachment; filename="+fileName+".pdf").build();
	}

	@Override
	public JSONObject dashboard_billingefficiency_comparision(HttpServletRequest request, HttpServletResponse response,
			JSONObject object) {
		// TODO Auto-generated method stub
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();

		String Conn_Type = object.getString("Conn_Type");
		String Location_Code = object.getString("Location_Code");
		String financial_year = object.getString("financial_year");
		String selected_location = object.getString("search_location");
		String inputtype = object.getString("inputtype");
		String displaywise = object.getString("displaywise");
		
		System.out.println(object);

		try {

			if (Conn_Type.equalsIgnoreCase("LT")) {

				dbConnection = databaseObj.getDatabaseConnection();

			} else if (Conn_Type.equals("HT")) {
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			accountsCS = dbConnection.prepareCall("{ call  GET_DASHBOARD_BILLINGEFF (?, '"+selected_location+"','"+inputtype+"','"+displaywise+"' ,'"+financial_year+"')}");
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			System.out.println(accountsRS);
			while (accountsRS.next()) {
				JSONObject ackobj = new JSONObject();
				
				if(inputtype.equals("LINE")) {
					ackobj.put("charttype", accountsRS.getString("charttype"));
					ackobj.put("trf", accountsRS.getString("trf"));
					ackobj.put("dates", accountsRS.getString("dates"));
					ackobj.put("billeff", accountsRS.getString("billeff"));
					array.add(ackobj);
				}else {
					ackobj.put("row_no", accountsRS.getString("row_no"));
					ackobj.put("trf", accountsRS.getString("trf"));
					ackobj.put("tobebilled", accountsRS.getString("tobebilled"));
					ackobj.put("billed", accountsRS.getString("billed"));
					ackobj.put("notbilled", accountsRS.getString("notbilled"));
					ackobj.put("billeff", accountsRS.getString("billeff"));
					ackobj.put("nor", accountsRS.getString("nor"));
					ackobj.put("dl", accountsRS.getString("dl"));
					ackobj.put("mnr", accountsRS.getString("mnr"));
					ackobj.put("dc", accountsRS.getString("dc"));
					ackobj.put("vacant", accountsRS.getString("vacant"));
					array.add(ackobj);
				}



			}
			if (array.isEmpty()) {
				// no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else {
				obj.put("status", "success");
				obj.put("message", "Billing Efficiency Details Retrieved !!!");
				obj.put("BILLING_EFFICIENCY_DASHBOARD", array);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}

		return obj;
	}

	@Override
	public JSONObject getfinancialyears(HttpServletRequest request, HttpServletResponse response, JSONObject object) {
		// TODO Auto-generated method stub
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();

		String Conn_Type = object.getString("Conn_Type");
		
		System.out.println(object);

		try {

			if (Conn_Type.equalsIgnoreCase("LT")) {

				dbConnection = databaseObj.getDatabaseConnection();

			} else if (Conn_Type.equals("HT")) {
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			//accountsCS = dbConnection.prepareCall(DBQueries.REPORT_GET_BILLING_EFFICIENCY);
			accountsCS = dbConnection.prepareCall("{ call  GET_FINANCIAL_YEARS (?)}");
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			System.out.println(accountsRS);
			while (accountsRS.next()) {
				JSONObject ackobj = new JSONObject();

				//System.out.println(accountsRS.getString("fy"));
				ackobj.put("year", accountsRS.getString("fy"));
				array.add(ackobj);

			}
			if (array.isEmpty()) {
				// no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else {
				obj.put("status", "success");
				obj.put("message", "Financial Years Retrieved !!!");
				obj.put("FINANCIAL_YEARS", array);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}

		return obj;
	}

	@Override
	public JSONObject maindashboard(HttpServletRequest request, HttpServletResponse response, JSONObject object) {
		// TODO Auto-generated method stub


		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();

		String Conn_Type = object.getString("Conn_Type");
		String Location_Code = object.getString("Location_Code");
		String month_year = object.getString("month_year");
		
		System.out.println(object);

		try {

			if (Conn_Type.equalsIgnoreCase("LT")) {

				dbConnection = databaseObj.getDatabaseConnection();

			} else if (Conn_Type.equals("HT")) {
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			//accountsCS = dbConnection.prepareCall(DBQueries.REPORT_GET_BILLING_EFFICIENCY);
			accountsCS = dbConnection.prepareCall("{ call  GET_DASHBOARD_DATA (?,'"+Location_Code+"','"+month_year+"')}");
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			System.out.println(accountsRS);
			while (accountsRS.next()) {
				JSONObject ackobj = new JSONObject();

				ackobj.put("tobebilled", accountsRS.getString("tobebilled"));
				ackobj.put("billed", accountsRS.getString("billed"));
				ackobj.put("notbilled", accountsRS.getString("notbilled"));
				ackobj.put("bill_eff", accountsRS.getString("bill_eff"));
				ackobj.put("consmp", accountsRS.getString("consmp"));
				ackobj.put("dmd", accountsRS.getString("dmd"));
				ackobj.put("col", accountsRS.getString("col"));
				ackobj.put("crdt", accountsRS.getString("crdt"));
				ackobj.put("totcol", accountsRS.getString("totcol"));
				ackobj.put("coll_eff", accountsRS.getString("coll_eff"));
				ackobj.put("dmdarr", accountsRS.getString("dmdarr"));
				ackobj.put("colarr", accountsRS.getString("colarr"));
				array.add(ackobj);

			}
			if (array.isEmpty()) {
				// no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else {
				obj.put("status", "success");
				obj.put("message", "Main-Dashboard Retrieved !!!");
				obj.put("MAINDASHBOARD", array);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}

		return obj;
	}

	@Override
	public JSONObject getdcb(HttpServletRequest request, HttpServletResponse response, JSONObject object) {
		// TODO Auto-generated method stub
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();

		String conn_type = object.getString("conn_type");
		String report_type = object.getString("report_type");
		String Location_Code = object.getString("selected_location");
		String month_year = object.getString("month_year");
		String tariffcodes = object.getString("tariffcodes");
		String station_code = object.getString("station_code");
		String feeder_code = object.getString("feeder_code");
		String transformer_code = object.getString("transformer_code");
		String metercode = object.getString("metercode");
		String gps = object.getString("gps");
		Boolean header = object.getBoolean("header");
		
		System.out.println(object);

		try {

			if (conn_type.equalsIgnoreCase("LT")) {

				dbConnection = databaseObj.getDatabaseConnection();

			} else if (conn_type.equals("HT")) {
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			//accountsCS = dbConnection.prepareCall(DBQueries.REPORT_GET_BILLING_EFFICIENCY);
			accountsCS = dbConnection.prepareCall("{ call  PKG_DCB_REPORTS.GET_DCB (?,'"+report_type+"','"+Location_Code+"','"+month_year+"',"
					                                                               + "'"+tariffcodes+"','"+station_code+"','"+feeder_code+"',"
					                                                               + "'"+transformer_code+"','"+metercode+"','"+gps+"',"+header+")}");
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			System.out.println(accountsRS);
			while (accountsRS.next()) {
				JSONObject ackobj = new JSONObject();

				ackobj.put("row_no", accountsRS.getString("row_no"));
				ackobj.put("trfgrp", accountsRS.getString("trfgrp"));
				ackobj.put("trforder", accountsRS.getString("trforder"));
				ackobj.put("acccd_dmd", accountsRS.getString("acccd_dmd"));
				ackobj.put("acccd_bal", accountsRS.getString("acccd_bal"));
				ackobj.put("vc", accountsRS.getString("vc"));
				ackobj.put("trf_desc", accountsRS.getString("trf_desc"));
				ackobj.put("trf", accountsRS.getString("trf"));
				ackobj.put("kw", accountsRS.getString("kw"));
				ackobj.put("hp", accountsRS.getString("hp"));
				ackobj.put("kva", accountsRS.getString("kva"));
				ackobj.put("tot_insts", accountsRS.getString("tot_insts"));
				ackobj.put("active_insts", accountsRS.getString("active_insts"));
				ackobj.put("tobe_billed", accountsRS.getString("tobe_billed"));
				ackobj.put("billed_insts", accountsRS.getString("billed_insts"));
				ackobj.put("mtr_units", accountsRS.getString("mtr_units"));
				ackobj.put("assd_units", accountsRS.getString("assd_units"));
				ackobj.put("wheeled_units", accountsRS.getString("wheeled_units"));
				ackobj.put("consmptot", accountsRS.getString("consmptot"));
				ackobj.put("rev_ob", accountsRS.getString("rev_ob"));
				ackobj.put("int_ob", accountsRS.getString("int_ob"));
				ackobj.put("tax_ob", accountsRS.getString("tax_ob"));
				ackobj.put("obtot", accountsRS.getString("obtot"));
				ackobj.put("rev_dmd", accountsRS.getString("rev_dmd"));
				ackobj.put("int_dmd", accountsRS.getString("int_dmd"));
				ackobj.put("tax_dmd", accountsRS.getString("tax_dmd"));
				ackobj.put("dmdtot", accountsRS.getString("dmdtot"));
				ackobj.put("obdmdrev", accountsRS.getString("obdmdrev"));
				ackobj.put("obdmdint", accountsRS.getString("obdmdint"));
				ackobj.put("obdmdtax", accountsRS.getString("obdmdtax"));
				ackobj.put("obdmdtot", accountsRS.getString("obdmdtot"));
				ackobj.put("ccrev_coll", accountsRS.getString("ccrev_coll"));
				ackobj.put("adjrev_coll", accountsRS.getString("adjrev_coll"));
				ackobj.put("collrevtot", accountsRS.getString("collrevtot"));
				ackobj.put("ccint_coll", accountsRS.getString("ccint_coll"));
				ackobj.put("adjint_coll", accountsRS.getString("adjint_coll"));
				ackobj.put("collinttot", accountsRS.getString("collinttot"));
				ackobj.put("cctax_coll", accountsRS.getString("cctax_coll"));
				ackobj.put("adjtax_coll", accountsRS.getString("adjtax_coll"));
				ackobj.put("colltaxtot", accountsRS.getString("colltaxtot"));
				ackobj.put("colltot", accountsRS.getString("colltot"));
				ackobj.put("rev_cb", accountsRS.getString("rev_cb"));
				ackobj.put("int_cb", accountsRS.getString("int_cb"));
				ackobj.put("tax_cb", accountsRS.getString("tax_cb"));
				ackobj.put("cb_total", accountsRS.getString("cb_total"));
				array.add(ackobj);

			}
			if (array.isEmpty()) {
				// no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else {
				obj.put("status", "success");
				obj.put("message", "DCB Data Retrieved !!!");
				obj.put("DCBDATA", array);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}

		return obj;
	}

	@Override
	public JSONObject getsbdreports(HttpServletRequest request, HttpServletResponse response, JSONObject object) {

		// TODO Auto-generated method stub
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();

		String conn_type = object.getString("conn_type");
		String report_type = object.getString("report_type");
		String Location_Code = object.getString("selected_location");
		String metercode = object.getString("metercode");
		String tariffcodes = object.getString("tariffcodes");
		String fromdate = object.getString("fromdate");
		String todate = object.getString("todate");
		Boolean header = object.getBoolean("header");
		
		System.out.println(object);

		try {

			if (conn_type.equalsIgnoreCase("LT")) {

				dbConnection = databaseObj.getDatabaseConnection();

			} else if (conn_type.equals("HT")) {
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			//accountsCS = dbConnection.prepareCall(DBQueries.REPORT_GET_BILLING_EFFICIENCY);
			accountsCS = dbConnection.prepareCall("{ call  GET_SBD_BILLING_EFFICIENCY (?,'"+report_type+"','"+Location_Code+"','"+metercode+"',"
					                                                               + "'"+tariffcodes+"','"+fromdate+"','"+todate+"',"+header+")}");
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			System.out.println(accountsRS);
			while (accountsRS.next()) {
				JSONObject ackobj = new JSONObject();

				ackobj.put("row_no", accountsRS.getString("row_no"));
				ackobj.put("mrcode", accountsRS.getString("mrcode"));
				ackobj.put("trf", accountsRS.getString("trf"));
				ackobj.put("tobebilled", accountsRS.getString("tobebilled"));
				ackobj.put("billed", accountsRS.getString("billed"));
				ackobj.put("notbilled", accountsRS.getString("notbilled"));
				ackobj.put("bill_eff", accountsRS.getString("bill_eff"));
				ackobj.put("nor", accountsRS.getString("nor"));
				ackobj.put("dl", accountsRS.getString("dl"));
				ackobj.put("mnr", accountsRS.getString("mnr"));
				ackobj.put("vacant", accountsRS.getString("vacant"));
				ackobj.put("dc", accountsRS.getString("dc"));

				array.add(ackobj);

			}
			if (array.isEmpty()) {
				// no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else {
				obj.put("status", "success");
				obj.put("message", "SBD Data Retrieved !!!");
				obj.put("SBDDATA", array);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}

		return obj;
	
	}

	@Override
	public JSONObject generateCollectionEfficiency(HttpServletRequest request, HttpServletResponse response,
			JSONObject object) {

		// TODO Auto-generated method stub
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();

		String conn_type = object.getString("conn_type");
		String report_type = object.getString("report_type");
		String Location_Code = object.getString("selected_location");
		String metercode = object.getString("metercode");
		String tariffcodes = object.getString("tariffcodes");
		String fromdate = object.getString("fromdate");
		String todate = object.getString("todate");
		Boolean header = object.getBoolean("header");
		
		System.out.println(object);

		try {

			if (conn_type.equalsIgnoreCase("LT")) {

				dbConnection = databaseObj.getDatabaseConnection();

			} else if (conn_type.equals("HT")) {
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			accountsCS = dbConnection.prepareCall("{ call GET_COLLECTION_EFFICIENCY (?,'"+report_type+"','"+Location_Code+"','"+metercode+"',"
					                                                               + "'"+tariffcodes+"','"+fromdate+"','"+todate+"',"+header+")}");
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			System.out.println(accountsRS);
			while (accountsRS.next()) {
				JSONObject ackobj = new JSONObject();

				ackobj.put("row_no", accountsRS.getString("row_no"));
				ackobj.put("mrcode", accountsRS.getString("mrcode"));
				ackobj.put("trf", accountsRS.getString("trf"));
				ackobj.put("consmp", accountsRS.getString("consmp"));
				ackobj.put("dmd", accountsRS.getString("dmd"));
				ackobj.put("col", accountsRS.getString("col"));
				ackobj.put("crdt", accountsRS.getString("crdt"));
				ackobj.put("col_eff", accountsRS.getString("col_eff"));
				ackobj.put("dmdarr", accountsRS.getString("dmdarr"));
				ackobj.put("colarr", accountsRS.getString("colarr"));

				array.add(ackobj);

			}
			if (array.isEmpty()) {
				// no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else {
				obj.put("status", "success");
				obj.put("message", "Collection Efficiency Data Retrieved !!!");
				obj.put("COLLECTION_EFFICIENCY", array);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}

		return obj;
	
	}

	@Override
	public JSONObject getPaymentPurposewiseReport(HttpServletRequest request, HttpServletResponse response,
			JSONObject object) {

		// TODO Auto-generated method stub
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();

		String conn_type = object.getString("conn_type");
		String Location_Code = object.getString("selected_location");
		String cashcounternumber = object.getString("cashcounternumber");
		String fromdate = object.getString("fromdate");
		String todate = object.getString("todate"); 
		Boolean header = object.getBoolean("header");
		
		System.out.println(object);

		try {

			if (conn_type.equalsIgnoreCase("LT")) {

				dbConnection = databaseObj.getDatabaseConnection();

			} else if (conn_type.equals("HT")) {
				dbConnection = databaseObj.getHTDatabaseConnection();
			}

			accountsCS = dbConnection.prepareCall("{ call PKG_CASH.GET_PURPOSEWISE_REPORTS (?,'"+Location_Code+"','"+cashcounternumber+"',"
					                                                                        + "'"+fromdate+"','"+todate+"',"+header+")}");
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			
			System.out.println(accountsCS);
			
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			System.out.println(accountsRS);
			while (accountsRS.next()) {
				JSONObject ackobj = new JSONObject();

				ackobj.put("row_no", accountsRS.getString("row_no"));
				ackobj.put("account_code", accountsRS.getString("account_code"));
				ackobj.put("pymnt_purpose", accountsRS.getString("pymnt_purpose"));
				ackobj.put("amount", accountsRS.getString("amount"));

				array.add(ackobj);

			}
			if (array.isEmpty()) {
				// no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else {
				obj.put("status", "success");
				obj.put("message", "Payment Purpose Wise Data Retrieved !!!");
				obj.put("PURPOSEWISE", array);
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}

		return obj;
	
	}

}
