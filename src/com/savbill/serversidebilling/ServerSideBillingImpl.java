package com.savbill.serversidebilling;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import javax.servlet.http.HttpServletRequest;

import oracle.jdbc.OracleTypes;

import com.savbill.database.DBManagerIMPL;
import com.savbill.database.DBManagerResourceRelease;
import com.savbill.database.DBQueries;
import com.savbill.util.ReferenceUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class ServerSideBillingImpl implements IServerSideBilling {

	static DBManagerIMPL databaseObj = new DBManagerIMPL();
	static Connection dbConnection;
	
	
	@Override
	public JSONObject generateServerSideBill(JSONObject object, HttpServletRequest request) {
		// TODO Auto-generated method stub
		
		JSONObject JsonResponse = new  JSONObject();
		
		try{
			
			if(object != null){
				
				String conn_type    = (String)object.get("conn_type");
				String BillingType = (String)object.get("Billing_Type");
				String RrNumber    = (String)object.get("RR_Number");
				String BillDate    = (String)object.get("Bill_Date");
				String MeterCode   = (String)object.get("Meter_Code"); 
				String Location    = (String)object.get("Location");
				String UserId      = (String)object.get("UserId");
				
				String bill_contextPath = request.getServletContext().getRealPath("BillingProgram.jar");
				String recon_contextPath = request.getServletContext().getRealPath("Reconcilation.jar");
				System.out.println("bill_contextPath : "+bill_contextPath);
				System.out.println("recon_contextPath : "+recon_contextPath);
				
				if(!conn_type.isEmpty()){System.out.println("1");
					
					if(!BillingType.isEmpty() && BillingType != null){System.out.println("2");
						
						//if(BillingType.equals("RR_NUMBER_WISE")){System.out.println("3");
						if(BillingType.equals("rr_no_wise")){System.out.println("3");

							if(!RrNumber.isEmpty() && RrNumber != null){System.out.println("4");
								
								System.out.println("5");
								
								if(!BillDate.isEmpty() && BillDate != null){System.out.println("6");
									
									if(conn_type.equals("LT")){
										dbConnection = databaseObj.getDatabaseConnection();
									}else if(conn_type.equals("HT")){
										dbConnection = databaseObj.getHTDatabaseConnection();
									}
									
									if(CompareBillDateAndServerDate(BillDate)){System.out.println("7");
										
										if(ReferenceUtil.validateRRStatus(RrNumber, dbConnection)){System.out.println("8");
											
											if((ReferenceUtil.CheckRRNumberTariffIsLT7(RrNumber, dbConnection))){System.out.println("9");
												
												//Here Code Goes for LT7 Billing.
												
												//Implementation Pending
												
												
											}else{
												
												//Here Code Goes Other than LT7
												
												if(MoveToHHDProcedure(RrNumber, BillDate, Location, "S", UserId)){System.out.println("10");
													
													String InstallationType = ReferenceUtil.GetInstallationTypeByRRno(RrNumber, dbConnection);
													
													if(CallBillingProgram(RrNumber, BillDate, InstallationType, Location, UserId,bill_contextPath,recon_contextPath)){System.out.println("11");
														
														JsonResponse.put("status", "success");
														JsonResponse.put("message", " Bill Generated Successfully For RR_Number : "+RrNumber + " , Bill_Date : "+BillDate);
													}else{
														
														JsonResponse.put("status", "error");
														JsonResponse.put("message", " Cannot Generate Bill For  RR_Number : "+RrNumber + " , Bill_Date : "+BillDate);
													}
													
												}else{System.out.println("12");
													
													JsonResponse.put("status", "error");
													JsonResponse.put("message"," Runtime Error : Cannot Download Data. ");
													
												}
												
											}
											
										}else{System.out.println("13");
											JsonResponse.put("status", "error");
											JsonResponse.put("message","Processing Is Being Done For This RR Number . ");
										}
									}else{System.out.println("14");
										JsonResponse.put("status", "error");
										JsonResponse.put("message","Bill Date And Server Date Are Not Equal.Cannot Continue. ");
									}
								}else{System.out.println("15");
									JsonResponse.put("status", "error");
									JsonResponse.put("message","RR Number And Bill Date Are Empty . ");
								}
							}else{System.out.println("16");
								JsonResponse.put("status", "error");
								JsonResponse.put("message","RR Number Is Empty . ");
							}
						//}else if(BillingType.equals("METER_CODE_WISE")){
						}else if(BillingType.equals("mr_code_wise")){	
							if(conn_type.equals("LT")){
								dbConnection = databaseObj.getDatabaseConnection();
							}else if(conn_type.equals("HT")){
								dbConnection = databaseObj.getHTDatabaseConnection();
							}
							
							System.out.println(MeterCode+","+ BillDate+","+ Location+"," +"S"+"," +UserId);
							if(CompareBillDateAndServerDate(BillDate)){
								
								if(MoveToHHDMrCodeProcedure(MeterCode, BillDate, Location, "S", UserId)){
									
									if(CallBillingProgramForMrCode(MeterCode, BillDate, Location, UserId)){
										
										JsonResponse.put("status", "success");
										JsonResponse.put("message", " Bill Generated Successfully For MR_Code : "+MeterCode + " , Bill_Date : "+BillDate);
										JsonResponse.put("remarks", SetRemarks(MeterCode, BillDate, Location,conn_type));
									}else{
										JsonResponse.put("status", "error");
										JsonResponse.put("message", " Cannot Generate Bill For MR_Code : "+MeterCode + " , Bill_Date : "+BillDate);
										JsonResponse.put("remarks", SetRemarks(MeterCode, BillDate, Location,conn_type));
									}
								}
								
							}else{
								JsonResponse.put("status", "error");
								JsonResponse.put("message","Bill Date And Server Date Are Not Equal.Cannot Continue. ");
							}
							
						}else{
							JsonResponse.put("status", "error");
							JsonResponse.put("message", "Invalid Billing Type.Try Again");
						}
						
					}else{
						JsonResponse.put("status", "error");
						JsonResponse.put("message", "Invalid Billing Type.Try Again");
					}
					
				}else{
					
					JsonResponse.put("status", "fail");
					JsonResponse.put("message", "Database Connection Not Configured ...!");
				}
				
				
			}else{
				JsonResponse.put("status", "error");
				JsonResponse.put("status", "Invalid Parameters.Try Again.");
			}
			
		}catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			JsonResponse.put("status", "fail");
			JsonResponse.put("message", "Database Connection Error ...!");
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			JsonResponse.put("status", "fail");
			JsonResponse.put("message", " Runtime Error . Could not Continue  ...!");
		}
		
		
		
		
		System.out.println("Bill Generate : "+JsonResponse);
		
		return JsonResponse;
	}


	private JSONArray SetRemarks(String meterCode, String billDate, String location, String conn_type) {
		// TODO Auto-generated method stub
		
		
		JSONArray json_array = new JSONArray();
		JSONObject json ;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String[] date = billDate.split("/");
		String ReadingDay = date[0].toString().trim();
		
		String qry = " SELECT ROWNUM ROW_NUM,SUBSTR(CM_RR_NO,8) RR_NO,CM_MTR_RDR_CD MR_CD,'"+billDate+"' BILL_DT,'Reading Not Found' MSG"
				   + " FROM CUST_MASTER "
				   + " WHERE SUBSTR(CM_RR_NO, 1, 7) = '"+location+"' "
				   + " AND CM_CONSMR_STS IN ('1','9','10') "
				   + " AND CM_MTR_RDR_CD = '"+meterCode+"' AND CM_MTR_RDG_DAY = '"+ReadingDay+"' "
				   + " AND NVL(CM_FIRST_BILL_DC_FLG,'N') = 'N' "
				   + " AND CM_RR_NO NOT IN "
				   + " (SELECT CM_RR_NO "
				   + "	FROM CUST_MASTER,MTR_RDG "
				   + "  WHERE SUBSTR(CM_RR_NO, 1, 7) = '"+location+"' "
				   + "  AND CM_CONSMR_STS IN ('1','9','10')  "
				   + "  AND CM_MTR_RDR_CD = '"+meterCode+"2' AND CM_MTR_RDG_DAY = '"+ReadingDay+"' "
				   + "  AND CM_RR_NO = MR_BCN_RR_NO AND MR_RDG_DT = to_date('"+billDate+"','dd/mm/yyyy')) ";
		
		try {
			try {
				if (conn_type.equals("LT")) {
					dbConnection = databaseObj.getDatabaseConnection();
				} else if (conn_type.equals("HT")) {
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			System.out.println("SetRemarks qry " + qry);
			ps = dbConnection.prepareStatement(qry);
			rs = ps.executeQuery();

			while(rs.next()){
				
				json = new JSONObject();
				
				json.put("row_num", rs.getString("ROW_NUM"));
				json.put("rr_no", rs.getString("RR_NO"));
				json.put("mr_cd", rs.getString("MR_CD"));
				json.put("bill_dt", rs.getString("BILL_DT"));
				json.put("msg", rs.getString("MSG"));
				
				json_array.add(json);
				
			}
			
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}
		return json_array;
	}


	@Override
	public boolean CompareBillDateAndServerDate(String BillDate) {
		// TODO Auto-generated method stub

		PreparedStatement ps = null;
		ResultSet rs = null;

		try{
			
			System.out.println("select trunc(sysdate,'dd')-to_date('"+BillDate+"','dd/mm/yyyy') count from dual ");
			ps = dbConnection.prepareStatement("  select trunc(sysdate,'dd')-to_date('"+BillDate+"','dd/mm/yyyy') count from dual  ");
			rs = ps.executeQuery();
			
			if(rs.next()){
				System.out.println("rs.getInt('count')"+rs.getInt("count"));
				if(rs.getInt("count") >= 0 ){
					System.out.println("rs.getInt('count')"+rs.getInt("count"));
					return true;
				}else{
					System.out.println("rs.getInt('count')"+rs.getInt("count"));
					return false;
				}
			}
		}catch (Exception e) {
			// TODO: handle exception\
			return false;
		}
		return false;
	}


	@Override
	public boolean MoveToHHDProcedure(String Rrnumber, String BillDate,
			String Location, String BillingMode, String UserId) {
		// TODO Auto-generated method stub
		
		CallableStatement cstmt = null;
		
		try {
			
			cstmt = dbConnection.prepareCall(" { call PKG_HHD.MOVE_TO_HHD_RRNO('"+Rrnumber+"','"+BillDate+"','"+UserId+"','"+BillingMode+"','"+Location+"',?) } ");
			
			
			System.out.println(" { call PKG_HHD.MOVE_TO_HHD_RRNO('"+Rrnumber+"','"+BillDate+"','"+UserId+"','"+BillingMode+"','"+Location+"',?) } ");
			cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
			int i = cstmt.executeUpdate();
			
			System.out.println("MoveToHHDProcedure  result : "+i);

			if(i > 0){
				String msg = cstmt.getString(1);
				System.out.println("MoveToHHDProcedure  msg : "+msg);
				if (msg.equalsIgnoreCase("TRUE")) {
					return true;
				} else {
					return false;
				}
			}else{
				return false;
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}finally{
			DBManagerResourceRelease.close(cstmt);
		}
	}


	@Override
	public boolean CallBillingProgram(String RrNumber, String BillDate,
			String InstalType, String Location,String UserID, String bill_contextPath, String recon_contextPath) {
		// TODO Auto-generated method stub
		
		CallableStatement cstmt = null;
		ResultSet rs = null;
		String ip = "";
		String database = "";
		String user = "";
		String pass = "";
		String PATH = "";
		
		try{
			
			//cstmt = dbConnection.prepareCall("");
			
			try{
		    	//resouce bundle to read string's specified in properties file
		    	ResourceBundle propsBundle=ResourceBundle.getBundle("savbilldb");
		        
		    	ip = propsBundle.getString("IP");
		        database = propsBundle.getString("DATABASE");
		        user = propsBundle.getString("USER");
		        pass = propsBundle.getString("PASS");
		        PATH = propsBundle.getString("PATH");
		       
		       } 
		    catch(Exception e){
		        System.out.println("error" + e);
		       }	 
			
			System.out.println("Billing Program Starts Here...!");
			
			//Call The Respective Child Process...!
			System.out.println("java -jar "+PATH+"BillingProgram.jar "+ip+" "+user+"/"+pass+" "+database+" -n="+RrNumber+"  -d="+BillDate + " -l="+Location + " -u="+UserID + " -p="+PATH);
			
			
			Process p = Runtime.getRuntime().exec("java -jar "+PATH+"BillingProgram.jar "+ip+" "+user+"/"+pass+" "+database+" -n="+RrNumber+"  -d="+BillDate + " -l="+Location + " -u="+UserID + " -p="+PATH);
			 
	        final InputStream is = p.getInputStream();
	        Thread t = new Thread(new Runnable() {
	            public void run() {
	                InputStreamReader isr = new InputStreamReader(is);
	                int ch;
	                try {
	                    while ((ch = isr.read()) != -1) {
	                        System.out.print((char) ch);
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	        t.start();
	        p.waitFor();
	        t.join();
	        System.out.println("Billing Program Ends Here");
			
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}finally{
			
		}
		return true;
	}


	@Override
	public boolean MoveToHHDMrCodeProcedure(String MeterCode, String BillDate,
			String Location, String BillingMode, String UserId) {
		// TODO Auto-generated method stub
		CallableStatement cstmt = null;
		
		try {
			cstmt = dbConnection.prepareCall(" { call PKG_HHD.MOVE_TO_HHD('"+MeterCode+"','"+UserId+"','"+BillDate +"','"+BillingMode+"','"+Location+"',?) } ");
			
			cstmt.registerOutParameter(1, java.sql.Types.VARCHAR);
			int i = cstmt.executeUpdate();

			if(i > 0){
				String msg = cstmt.getString(1);
				System.out.println("msg : "+msg);
				if (msg.equalsIgnoreCase("TRUE")) {
					return true;
				} else {
					return false;
				}
			}else{
				return false;
			}
		} catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			return false;
		}finally{
			DBManagerResourceRelease.close(cstmt);
		}
	}


	@Override
	public boolean CallBillingProgramForMrCode(String meterCode,
			String BillDate, String Location, String UserID) {
		// TODO Auto-generated method stub
		try {
			
			
/*			CallableStatement calSt = dbConnection.prepareCall("{ call CLOUDBILLEXEC(?,?,?,?) }");


			calSt.setString(1, "CLB");
			calSt.setString(2, billPath);
			String installationType = "";

			String dbBillGenMonth = CommonUtill
					.getDayMonthYear(billGenDt, "MM");

			installationType = "clb/clb -r=" + mrCode + " -d=" + billGenDt 

					+ " -m=" + dbBillGenMonth + " -u=" + uid+" -l="+loc;
			calSt.setString(3, installationType);

			calSt.registerOutParameter(4, java.sql.Types.NUMERIC);
			int i = calSt.executeUpdate();

			int returnValue = calSt.getInt(4);

			System.out.println("CLOUDBILLEXEC MRwise return value " + returnValue);

			if (returnValue > 0) {
				status = "";

			} else {
				status = "success";
			}*/
			
			if(BillDate.contains("-")){
				BillDate.replace("-","/");
			}
			String[] S_array = BillDate.split("/");
			String   Bill_Month = S_array[1];

			
			
			//cstmt = dbConnection.prepareCall("");
			
			System.out.println("Billing Program Starts Here...!");
			
			//Call The Respective Child Process...!
			System.out.println("java -jar D:/SCHEDULERS/BillingProgram.jar 192.168.0.159  clb/clb -m="+Bill_Month+" -r="+meterCode+"  -d="+BillDate + " -l="+Location + " -u="+UserID);
			
			
			Process p = Runtime.getRuntime().exec("java -jar D:/SCHEDULERS/BillingProgram.jar 192.168.0.159  clb/clb -m="+Bill_Month+" -r="+meterCode+" -d="+BillDate + " -l="+Location + " -u="+UserID);
			 
	        final InputStream is = p.getInputStream();
	        Thread t = new Thread(new Runnable() {
	            public void run() {
	                InputStreamReader isr = new InputStreamReader(is);
	                int ch;
	                try {
	                    while ((ch = isr.read()) != -1) {
	                        System.out.print((char) ch);
	                    }
	                } catch (IOException e) {
	                    e.printStackTrace();
	                }
	            }
	        });
	        t.start();
	        p.waitFor();
	        t.join();
	        System.out.println("Billing Program Ends Here");
	        
	        return true;
			
		}/* catch (SQLException e) {
			// TODO: handle exception
			e.printStackTrace();
		}*/catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return false;
	}
	
	
	public static String GetInstallationTypeByRRno(String rrNumber, Connection dbConnection) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		String Inst_Type = "";
		
		String qry = "SELECT CM_INSTALL_TYP FROM CUST_MASTER WHERE cm_rr_no = '" + rrNumber + "' ";
		
		try {
			System.out.println("InstallationTypeByRRNO qry " + qry);
			ps = dbConnection.prepareStatement(qry);
			rs = ps.executeQuery();

			if(rs.next()) {
				String s = rs.getString("CM_INSTALL_TYP");
				Inst_Type = s;
			}
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally {
			DBManagerResourceRelease.close(ps);
			DBManagerResourceRelease.close(rs);
		}
		return Inst_Type;
	}
	
	@Override
	public JSONObject mrToMrTransfer(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String conn_type = (String) object.get("conn_type");

		String from_mr_code = (String) object.get("from_mr_code");
		String to_mr_code = (String) object.get("to_mr_code");
		String from_reading_day = (String) object.get("from_reading_day");
		String to_reading_day = (String) object.get("to_reading_day");
		String from_folio_no = (String) object.get("from_folio_no");
		String to_folio_no = (String) object.get("to_folio_no");
		
		try {
			if(!from_mr_code.isEmpty() && !to_mr_code.isEmpty() && !from_reading_day.isEmpty() && !to_reading_day.isEmpty() && !from_folio_no.isEmpty() && !to_folio_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				accountsCS=dbConnection.prepareCall(DBQueries.TRANSFER_RR_NO_MR_TO_MR);
				accountsCS.setString(1, from_mr_code);
				accountsCS.setString(2, from_reading_day);
				accountsCS.setString(3, from_folio_no);
				accountsCS.setString(4, to_folio_no);
				accountsCS.setString(5, to_mr_code);
				accountsCS.setString(6, to_reading_day);
				accountsCS.setString(7, (String) object.get("userid"));
				accountsCS.setString(8, (String) object.get("location_code"));
				
				accountsCS.registerOutParameter(9, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(9);
				
				if(accountsRS.next()){
					String RESP = accountsRS.getString("RESP");
					System.out.println("RESP : "+RESP);
					
					if(RESP.equalsIgnoreCase("success")){
						obj.put("status", "success");
						obj.put("message", " Transfer Successful from" + from_mr_code + " : " +from_reading_day + "  to  " + to_mr_code + " : " + to_reading_day ) ;
					}else {
						obj.put("status", "error");
						obj.put("message", RESP ) ;
					}
				}
						
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid RR Number");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(accountsRS, accountsCS);
		}
		
		return obj;
	}
	
}
