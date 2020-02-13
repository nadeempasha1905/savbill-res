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
import com.savbill.database.DBManagerResourceRelease;
import com.savbill.database.DBQueries;

import net.sf.jasperreports.engine.JRException;
import net.sf.jasperreports.engine.JRExporterParameter;
import net.sf.jasperreports.engine.JasperExportManager;
import net.sf.jasperreports.engine.JasperFillManager;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.engine.export.JRPdfExporter;
import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

/**
 * @author DELL
 *
 */
public class OtherReportsImpl implements IOtherReports {

	// database connection init
		static DBManagerIMPL databaseObj = new DBManagerIMPL();
		static Connection dbConnection;
		
		ResourceBundle propsBundle=ResourceBundle.getBundle("savbilldb");
		String REPORT_PATH = propsBundle.getString("REPORT_PATH");
		String REPORT_IMAGE_LOGO_NAME = propsBundle.getString("REPORT_IMAGE_LOGO_NAME");
		
		
	@Override
	public Response generate_other_reports(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		
		
		HashMap parameterMap = new HashMap();
		String path = null ;
		
		String conn_type = request.getParameter("conn_type").trim();
		String fileName =  request.getParameter("filename").trim();
		
	        try {
	        	
				if (conn_type.equalsIgnoreCase("LT")) {
					dbConnection = databaseObj.getDatabaseConnection();
				} else if (conn_type.equals("HT")) {
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
	        	
	        	String contextPath = request.getServletContext().getRealPath("OtherReportsImpl.java");
	        	File contextfile = new File(contextPath);
	        	String parentfile = contextfile.getParent();
	        	
	        	File f = new File(REPORT_PATH+ fileName );
				path = f.getPath();
				
				String imgPath = REPORT_PATH +REPORT_IMAGE_LOGO_NAME;
				
				parameterMap.put("company_name", request.getParameter("company")); 
				parameterMap.put("subdivision_name",request.getParameter("subdivision_name"));
				parameterMap.put("location_code",  request.getParameter("location_code"));
	            parameterMap.put("imgPath", imgPath);
	            
	            parameterMap.put("report_id", request.getParameter("report_id"));
	            parameterMap.put("rebate_code", request.getParameter("rebate_code"));
	            parameterMap.put("report_view_selected", request.getParameter("report_view_selected"));
	            parameterMap.put("om_section", request.getParameter("om_section"));
	            parameterMap.put("meter_code", request.getParameter("meter_code"));
	            parameterMap.put("reading_day", request.getParameter("reading_day"));
	            parameterMap.put("load_type_selected", request.getParameter("load_type_selected"));
	            parameterMap.put("from_load_hp_kw", request.getParameter("from_load_hp_kw"));
	            parameterMap.put("to_load_hp_kw", request.getParameter("to_load_hp_kw"));
	            parameterMap.put("dmd_consmp_selected", request.getParameter("dmd_consmp_selected"));
	            parameterMap.put("dmd_consmp_from_value", request.getParameter("dmd_consmp_from_value"));
	            parameterMap.put("dmd_consmp_to_value", request.getParameter("dmd_consmp_to_value"));
	            parameterMap.put("dateoption_selected", request.getParameter("dateoption_selected"));
	            parameterMap.put("from_date", request.getParameter("from_date"));
	            parameterMap.put("to_date", request.getParameter("to_date"));
	            parameterMap.put("month_year", request.getParameter("month_year"));
	            parameterMap.put("no_of_months", request.getParameter("no_of_months"));
	            parameterMap.put("rrnumber", request.getParameter("rrnumber"));
	            parameterMap.put("rrnumber_expressions", request.getParameter("rrnumber_expressions"));
	            parameterMap.put("tariffs", request.getParameter("tariffs"));
	            parameterMap.put("customer_status", request.getParameter("customer_status"));
	            parameterMap.put("username", request.getParameter("username"));
	            
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
	        
	        byte[] buffer2 = create_report(path,parameterMap,fileName,request,response);
	        return Response.ok(buffer2)
				    .header("Content-Length", buffer2.length)
				    .header("Content-Disposition","attachment; filename="+fileName+".pdf").build();
	}

	@Override
	public Response generate_bill_cancel_report(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

		HashMap parameterMap = new HashMap();
		String path = null ;
		
		String conn_type = request.getParameter("conn_type").trim();
		String fileName =  request.getParameter("filename").trim();
		
	        try {
	        	
				if (conn_type.equalsIgnoreCase("LT")) {
					dbConnection = databaseObj.getDatabaseConnection();
				} else if (conn_type.equals("HT")) {
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
	        	
	        	String contextPath = request.getServletContext().getRealPath("OtherReportsImpl.java");
	        	File contextfile = new File(contextPath);
	        	String parentfile = contextfile.getParent();
	        	
	        	File f = new File(REPORT_PATH+ fileName );
				path = f.getPath();
				
				String imgPath = REPORT_PATH +REPORT_IMAGE_LOGO_NAME;
				
				parameterMap.put("company_name", request.getParameter("company")); 
				parameterMap.put("subdivision_name",request.getParameter("subdivision_name"));
				parameterMap.put("location_code",  request.getParameter("location_code"));
	            parameterMap.put("imgPath", imgPath);
	            
	           // +"&billcancel_selected="+($scope.billcancel_selected ? $scope.billcancel_selected : '')
				
	            parameterMap.put("rrnumber", request.getParameter("billcancel_rrnumber"));
	            parameterMap.put("selected_username", request.getParameter("billcancel_userid"));
	            parameterMap.put("om_section", request.getParameter("om_section"));
	            parameterMap.put("meter_code", request.getParameter("meter_code"));
	            parameterMap.put("reading_day", request.getParameter("reading_day"));
	            parameterMap.put("dateoption_selected", request.getParameter("dateoption_selected"));
	            parameterMap.put("from_date", request.getParameter("from_date"));
	            parameterMap.put("to_date", request.getParameter("to_date"));
	            parameterMap.put("month_year", request.getParameter("month_year"));
	            parameterMap.put("tariffs", request.getParameter("tariffs"));
	            parameterMap.put("username", request.getParameter("username"));
	            
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
	        
	        byte[] buffer2 = create_report(path,parameterMap,fileName,request,response);
	        return Response.ok(buffer2)
				    .header("Content-Length", buffer2.length)
				    .header("Content-Disposition","attachment; filename="+fileName+".pdf").build();
	}

	@Override
	public Response generate_credit_report(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		HashMap parameterMap = new HashMap();
		String path = null ;
		
		String conn_type = request.getParameter("conn_type").trim();
		String fileName =  request.getParameter("filename").trim();
		
	        try {
	        	
				if (conn_type.equalsIgnoreCase("LT")) {
					dbConnection = databaseObj.getDatabaseConnection();
				} else if (conn_type.equals("HT")) {
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
	        	
	        	String contextPath = request.getServletContext().getRealPath("OtherReportsImpl.java");
	        	File contextfile = new File(contextPath);
	        	String parentfile = contextfile.getParent();
	        	
	        	File f = new File(REPORT_PATH+ fileName );
				path = f.getPath();
				
				String imgPath = REPORT_PATH +REPORT_IMAGE_LOGO_NAME;
				
				parameterMap.put("company_name", request.getParameter("company")); 
				parameterMap.put("subdivision_name",request.getParameter("subdivision_name"));
				parameterMap.put("location_code",  request.getParameter("location_code"));
	            parameterMap.put("imgPath", imgPath);
	            
	           // +"&billcancel_selected="+($scope.billcancel_selected ? $scope.billcancel_selected : '')
	            
	            parameterMap.put("reportwise", request.getParameter("reportwise_selected"));
	            parameterMap.put("approvedstatus", request.getParameter("credit_status"));
	            parameterMap.put("credittype", request.getParameter("credit_type"));
	            parameterMap.put("report_view_selected", request.getParameter("reporttype_selected"));
	            parameterMap.put("om_section", request.getParameter("om_section"));
	            parameterMap.put("meter_code", request.getParameter("meter_code"));
	            parameterMap.put("reading_day", request.getParameter("reading_day"));
	            parameterMap.put("dateoption_selected", request.getParameter("dateoption_selected"));
	            parameterMap.put("from_date", request.getParameter("from_date"));
	            parameterMap.put("to_date", request.getParameter("to_date"));
	            parameterMap.put("month_year", request.getParameter("month_year"));
	            parameterMap.put("tariffs", request.getParameter("tariffs"));
	            parameterMap.put("username", request.getParameter("username"));
	            
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
	        byte[] buffer2 = create_report(path,parameterMap,fileName,request,response);
	        return Response.ok(buffer2)
				    .header("Content-Length", buffer2.length)
				    .header("Content-Disposition","attachment; filename="+fileName+".pdf").build();
	}
	
	private byte[] create_report(String path, HashMap parameterMap,String fileName,HttpServletRequest request,HttpServletResponse response) {
		
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
			
			System.out.println("dir abspath .pdf " + path);
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
	
	byte[] buffer2 = null;
	  try {
	   File file = new File(path+ ".pdf");
	   FileInputStream fis = new FileInputStream(file);
	   buffer2 = IOUtils.toByteArray(fis);
	   fis.close();
	  } catch (Exception e) {
	   e.printStackTrace();
	  }
	   System.out.println("buffer2 : "+buffer2);
	   
	   return buffer2;
		/*
		 * return Response.ok(buffer2) .header("Content-Length", buffer2.length)
		 * .header("Content-Disposition","attachment; filename="+fileName+".pdf").build(
		 * );
		 */
		
	}

	@Override
	public Response generate_debit_withdrawal_report(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		HashMap parameterMap = new HashMap();
		String path = null ;
		
		String conn_type = request.getParameter("conn_type").trim();
		String fileName =  request.getParameter("filename").trim();
		
	        try {
	        	
				if (conn_type.equalsIgnoreCase("LT")) {
					dbConnection = databaseObj.getDatabaseConnection();
				} else if (conn_type.equals("HT")) {
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
	        	
	        	String contextPath = request.getServletContext().getRealPath("OtherReportsImpl.java");
	        	File contextfile = new File(contextPath);
	        	String parentfile = contextfile.getParent();
	        	
	        	File f = new File(REPORT_PATH+ fileName );
				path = f.getPath();
				
				String imgPath = REPORT_PATH +REPORT_IMAGE_LOGO_NAME;
				
				parameterMap.put("company_name", request.getParameter("company")); 
				parameterMap.put("subdivision_name",request.getParameter("subdivision_name"));
				parameterMap.put("location_code",  request.getParameter("location_code"));
	            parameterMap.put("imgPath", imgPath);
	            
	           // +"&billcancel_selected="+($scope.billcancel_selected ? $scope.billcancel_selected : '')
	            
	            parameterMap.put("report_view_selected", request.getParameter("reportwise_selected"));
	            parameterMap.put("reportwise", request.getParameter("datetype_selected"));
	            parameterMap.put("credittype", request.getParameter("charge_description"));
	            parameterMap.put("report_id", request.getParameter("reporttype_selected"));
	            parameterMap.put("rrnumber", request.getParameter("rrnumber"));
	            parameterMap.put("approvedstatus", request.getParameter("debit_status"));
	            parameterMap.put("om_section", request.getParameter("om_section"));
	            parameterMap.put("meter_code", request.getParameter("meter_code"));
	            parameterMap.put("reading_day", request.getParameter("reading_day"));
	            parameterMap.put("dateoption_selected", request.getParameter("dateoption_selected"));
	            parameterMap.put("from_date", request.getParameter("from_date"));
	            parameterMap.put("to_date", request.getParameter("to_date"));
	            parameterMap.put("month_year", request.getParameter("month_year"));
	            parameterMap.put("tariffs", request.getParameter("tariffs"));
	            parameterMap.put("username", request.getParameter("username"));
	            
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
	        byte[] buffer2 = create_report(path,parameterMap,fileName,request,response);
	        return Response.ok(buffer2)
				    .header("Content-Length", buffer2.length)
				    .header("Content-Disposition","attachment; filename="+fileName+".pdf").build();
	}

	@Override
	public Response generate_adjustment_report(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		HashMap parameterMap = new HashMap();
		String path = null ;
		
		String conn_type = request.getParameter("conn_type").trim();
		String fileName =  request.getParameter("filename").trim();
		
	        try {
	        	
				if (conn_type.equalsIgnoreCase("LT")) {
					dbConnection = databaseObj.getDatabaseConnection();
				} else if (conn_type.equals("HT")) {
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
	        	
	        	String contextPath = request.getServletContext().getRealPath("OtherReportsImpl.java");
	        	File contextfile = new File(contextPath);
	        	String parentfile = contextfile.getParent();
	        	
	        	File f = new File(REPORT_PATH+ fileName );
				path = f.getPath();
				
				String imgPath = REPORT_PATH +REPORT_IMAGE_LOGO_NAME;
				
				parameterMap.put("company_name", request.getParameter("company")); 
				parameterMap.put("subdivision_name",request.getParameter("subdivision_name"));
				parameterMap.put("location_code",  request.getParameter("location_code"));
	            parameterMap.put("imgPath", imgPath);
	            
	           // +"&billcancel_selected="+($scope.billcancel_selected ? $scope.billcancel_selected : '')
	            
	            parameterMap.put("report_view_selected", request.getParameter("reporttype_selected"));
	            parameterMap.put("fromtype", request.getParameter("fromtype"));
	            parameterMap.put("totype", request.getParameter("totype"));
	            parameterMap.put("om_section", request.getParameter("om_section"));
	            parameterMap.put("meter_code", request.getParameter("meter_code"));
	            parameterMap.put("reading_day", request.getParameter("reading_day"));
	            parameterMap.put("dateoption_selected", request.getParameter("dateoption_selected"));
	            parameterMap.put("from_date", request.getParameter("from_date"));
	            parameterMap.put("to_date", request.getParameter("to_date"));
	            parameterMap.put("month_year", request.getParameter("month_year"));
	            parameterMap.put("tariffs", request.getParameter("tariffs"));
	            parameterMap.put("username", request.getParameter("username"));
	            
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
	        byte[] buffer2 = create_report(path,parameterMap,fileName,request,response);
	        return Response.ok(buffer2)
				    .header("Content-Length", buffer2.length)
				    .header("Content-Disposition","attachment; filename="+fileName+".pdf").build();
	}

	@Override
	public Response generate_dlrmnr_report(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		HashMap parameterMap = new HashMap();
		String path = null ;
		
		String conn_type = request.getParameter("conn_type").trim();
		String fileName =  request.getParameter("filename").trim();
		
	        try {
	        	
				if (conn_type.equalsIgnoreCase("LT")) {
					dbConnection = databaseObj.getDatabaseConnection();
				} else if (conn_type.equals("HT")) {
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
	        	
	        	String contextPath = request.getServletContext().getRealPath("OtherReportsImpl.java");
	        	File contextfile = new File(contextPath);
	        	String parentfile = contextfile.getParent();
	        	
	        	File f = new File(REPORT_PATH+ fileName );
				path = f.getPath();
				
				String imgPath = REPORT_PATH +REPORT_IMAGE_LOGO_NAME;
				
				parameterMap.put("company_name", request.getParameter("company")); 
				parameterMap.put("subdivision_name",request.getParameter("subdivision_name"));
				parameterMap.put("location_code",  request.getParameter("location_code"));
	            parameterMap.put("imgPath", imgPath);
	            
	           // +"&billcancel_selected="+($scope.billcancel_selected ? $scope.billcancel_selected : '')
	            
			/*
			 * +"&reporttype_selected="+($scope.doorlockmnr_reporttype_selected ? $scope.doorlockmnr_reporttype_selected : '')
					+"&doorlockmnr_type_selected="+($scope.doorlockmnr_type_selected ? $scope.doorlockmnr_type_selected : '')
					+"&reportwise_selected="+($scope.reportwise_selected ? $scope.reportwise_selected : '')
					+"&monthstype="+($scope.doorlockmnr.monthstype ? $scope.doorlockmnr.monthstype.key : '')
					+"&from_months="+($scope.doorlockmnr.monthtype_from_value ? $scope.doorlockmnr.monthtype_from_value : '')
					+"&to_months="+($scope.doorlockmnr.monthtype_to_value ? $scope.doorlockmnr.monthtype_to_value : '')
			*/
	            parameterMap.put("report_view_selected", request.getParameter("reporttype_selected"));
	            parameterMap.put("monthtype", request.getParameter("monthstype"));
	            parameterMap.put("monthsfrom", request.getParameter("from_months"));
	            parameterMap.put("monthsto", request.getParameter("to_months"));
	            parameterMap.put("report_id", request.getParameter("doorlockmnr_type_selected"));
	            parameterMap.put("grouptype", request.getParameter("reportwise_selected"));
	            parameterMap.put("om_section", request.getParameter("om_section"));
	            parameterMap.put("meter_code", request.getParameter("meter_code"));
	            parameterMap.put("reading_day", request.getParameter("reading_day"));
	            parameterMap.put("dateoption_selected", request.getParameter("dateoption_selected"));
	            parameterMap.put("from_date", request.getParameter("from_date"));
	            parameterMap.put("to_date", request.getParameter("to_date"));
	            parameterMap.put("month_year", request.getParameter("month_year"));
	            parameterMap.put("tariffs", request.getParameter("tariffs"));
	            parameterMap.put("username", request.getParameter("username"));
	            
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
	        byte[] buffer2 = create_report(path,parameterMap,fileName,request,response);
	        return Response.ok(buffer2)
				    .header("Content-Length", buffer2.length)
				    .header("Content-Disposition","attachment; filename="+fileName+".pdf").build();
	}

	@Override
	public Response generate_present_reading_less_report(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub
		HashMap parameterMap = new HashMap();
		String path = null ;
		
		String conn_type = request.getParameter("conn_type").trim();
		String fileName =  request.getParameter("filename").trim();
		
	        try {
	        	
				if (conn_type.equalsIgnoreCase("LT")) {
					dbConnection = databaseObj.getDatabaseConnection();
				} else if (conn_type.equals("HT")) {
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
	        	
	        	String contextPath = request.getServletContext().getRealPath("OtherReportsImpl.java");
	        	File contextfile = new File(contextPath);
	        	String parentfile = contextfile.getParent();
	        	
	        	File f = new File(REPORT_PATH+ fileName );
				path = f.getPath();
				
				String imgPath = REPORT_PATH +REPORT_IMAGE_LOGO_NAME;
				
				parameterMap.put("company_name", request.getParameter("company")); 
				parameterMap.put("subdivision_name",request.getParameter("subdivision_name"));
				parameterMap.put("location_code",  request.getParameter("location_code"));
	            parameterMap.put("imgPath", imgPath);
	            
	            parameterMap.put("om_section", request.getParameter("om_section"));
	            parameterMap.put("meter_code", request.getParameter("meter_code"));
	            parameterMap.put("reading_day", request.getParameter("reading_day"));
	            parameterMap.put("dateoption_selected", request.getParameter("dateoption_selected"));
	            parameterMap.put("from_date", request.getParameter("from_date"));
	            parameterMap.put("to_date", request.getParameter("to_date"));
	            parameterMap.put("month_year", request.getParameter("month_year"));
	            parameterMap.put("tariffs", request.getParameter("tariffs"));
	            parameterMap.put("username", request.getParameter("username"));
	            
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
	        byte[] buffer2 = create_report(path,parameterMap,fileName,request,response);
	        return Response.ok(buffer2)
				    .header("Content-Length", buffer2.length)
				    .header("Content-Disposition","attachment; filename="+fileName+".pdf").build();
	}

	@Override
	public Response generate_abnormal_subnormal_report(HttpServletRequest request, HttpServletResponse response) {
		// TODO Auto-generated method stub

		HashMap parameterMap = new HashMap();
		String path = null ;
		
		String conn_type = request.getParameter("conn_type").trim();
		String fileName =  request.getParameter("filename").trim();
		
	        try {
	        	
				if (conn_type.equalsIgnoreCase("LT")) {
					dbConnection = databaseObj.getDatabaseConnection();
				} else if (conn_type.equals("HT")) {
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
	        	
	        	String contextPath = request.getServletContext().getRealPath("OtherReportsImpl.java");
	        	File contextfile = new File(contextPath);
	        	String parentfile = contextfile.getParent();
	        	
	        	File f = new File(REPORT_PATH+ fileName );
				path = f.getPath();
				
				String imgPath = REPORT_PATH +REPORT_IMAGE_LOGO_NAME;
				
				parameterMap.put("company_name", request.getParameter("company")); 
				parameterMap.put("subdivision_name",request.getParameter("subdivision_name"));
				parameterMap.put("location_code",  request.getParameter("location_code"));
	            parameterMap.put("imgPath", imgPath);
	            
	            parameterMap.put("report_id", request.getParameter("reporttype_selected"));
	            parameterMap.put("grouptype", request.getParameter("reportwise_selected"));
	            parameterMap.put("onloadpercentagebasis", request.getParameter("consumption_selected"));
	            parameterMap.put("loadpercentage", request.getParameter("consumption_percentage"));
	            parameterMap.put("report_view_selected", request.getParameter("abnormal_reporttype_selected"));
	            parameterMap.put("om_section", request.getParameter("om_section"));
	            parameterMap.put("meter_code", request.getParameter("meter_code"));
	            parameterMap.put("reading_day", request.getParameter("reading_day"));
	            parameterMap.put("dateoption_selected", request.getParameter("dateoption_selected"));
	            parameterMap.put("from_date", request.getParameter("from_date"));
	            parameterMap.put("to_date", request.getParameter("to_date"));
	            parameterMap.put("month_year", request.getParameter("month_year"));
	            parameterMap.put("tariffs", request.getParameter("tariffs"));
	            parameterMap.put("username", request.getParameter("username"));
	            
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
	        byte[] buffer2 = create_report(path,parameterMap,fileName,request,response);
	        return Response.ok(buffer2)
				    .header("Content-Length", buffer2.length)
				    .header("Content-Disposition","attachment; filename="+fileName+".pdf").build();
	
	}

	@Override
	public Response generate_zero_consumption_report(HttpServletRequest request, HttpServletResponse response) {

		// TODO Auto-generated method stub
		HashMap parameterMap = new HashMap();
		String path = null ;
		
		String conn_type = request.getParameter("conn_type").trim();
		String fileName =  request.getParameter("filename").trim();
		
	        try {
	        	
				if (conn_type.equalsIgnoreCase("LT")) {
					dbConnection = databaseObj.getDatabaseConnection();
				} else if (conn_type.equals("HT")) {
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
	        	
	        	String contextPath = request.getServletContext().getRealPath("OtherReportsImpl.java");
	        	File contextfile = new File(contextPath);
	        	String parentfile = contextfile.getParent();
	        	
	        	File f = new File(REPORT_PATH+ fileName );
				path = f.getPath();
				
				String imgPath = REPORT_PATH +REPORT_IMAGE_LOGO_NAME;
				
				parameterMap.put("company_name", request.getParameter("company")); 
				parameterMap.put("subdivision_name",request.getParameter("subdivision_name"));
				parameterMap.put("location_code",  request.getParameter("location_code"));
	            parameterMap.put("imgPath", imgPath);
	            
	            parameterMap.put("report_view_selected", request.getParameter("reporttype_selected"));
	            parameterMap.put("monthtype", request.getParameter("monthstype"));
	            parameterMap.put("monthsfrom", request.getParameter("from_months"));
	            parameterMap.put("monthsto", request.getParameter("to_months"));
	            parameterMap.put("grouptype", request.getParameter("reportwise_selected"));
	            parameterMap.put("om_section", request.getParameter("om_section"));
	            parameterMap.put("meter_code", request.getParameter("meter_code"));
	            parameterMap.put("reading_day", request.getParameter("reading_day"));
	            parameterMap.put("dateoption_selected", request.getParameter("dateoption_selected"));
	            parameterMap.put("from_date", request.getParameter("from_date"));
	            parameterMap.put("to_date", request.getParameter("to_date"));
	            parameterMap.put("month_year", request.getParameter("month_year"));
	            parameterMap.put("tariffs", request.getParameter("tariffs"));
	            parameterMap.put("username", request.getParameter("username"));
	            
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
	        byte[] buffer2 = create_report(path,parameterMap,fileName,request,response);
	        return Response.ok(buffer2)
				    .header("Content-Length", buffer2.length)
				    .header("Content-Disposition","attachment; filename="+fileName+".pdf").build();
	
	}

	@Override
	public Response generate_high_value_report(HttpServletRequest request, HttpServletResponse response) {

		// TODO Auto-generated method stub
		HashMap parameterMap = new HashMap();
		String path = null ;
		
		String conn_type = request.getParameter("conn_type").trim();
		String fileName =  request.getParameter("filename").trim();
		
	        try {
	        	
				if (conn_type.equalsIgnoreCase("LT")) {
					dbConnection = databaseObj.getDatabaseConnection();
				} else if (conn_type.equals("HT")) {
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
	        	
	        	String contextPath = request.getServletContext().getRealPath("OtherReportsImpl.java");
	        	File contextfile = new File(contextPath);
	        	String parentfile = contextfile.getParent();
	        	
	        	File f = new File(REPORT_PATH+ fileName );
				path = f.getPath();
				
				String imgPath = REPORT_PATH +REPORT_IMAGE_LOGO_NAME;
				
				parameterMap.put("company_name", request.getParameter("company")); 
				parameterMap.put("subdivision_name",request.getParameter("subdivision_name"));
				parameterMap.put("location_code",  request.getParameter("location_code"));
	            parameterMap.put("imgPath", imgPath);
	            
	            parameterMap.put("report_view_selected", request.getParameter("reporttype_selected"));
	            parameterMap.put("from_amount", request.getParameter("cheque_from_amount"));
	            parameterMap.put("to_amount", request.getParameter("cheque_to_amount"));
	            parameterMap.put("om_section", request.getParameter("om_section"));
	            parameterMap.put("meter_code", request.getParameter("meter_code"));
	            parameterMap.put("reading_day", request.getParameter("reading_day"));
	            parameterMap.put("dateoption_selected", request.getParameter("dateoption_selected"));
	            parameterMap.put("from_date", request.getParameter("from_date"));
	            parameterMap.put("to_date", request.getParameter("to_date"));
	            parameterMap.put("month_year", request.getParameter("month_year"));
	            parameterMap.put("tariffs", request.getParameter("tariffs"));
	            parameterMap.put("username", request.getParameter("username"));
	            
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
	        byte[] buffer2 = create_report(path,parameterMap,fileName,request,response);
	        return Response.ok(buffer2)
				    .header("Content-Length", buffer2.length)
				    .header("Content-Disposition","attachment; filename="+fileName+".pdf").build();
	
	}

}
