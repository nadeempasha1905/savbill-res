/**
 * 
 */
package com.savbill.customer;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import oracle.jdbc.OracleTypes;
import net.sf.json.JSON;
import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;

import com.savbill.database.DBManagerIMPL;
import com.savbill.database.DBManagerResourceRelease;
import com.savbill.database.DBQueries;
import com.savbill.implementation.ConsumerMasterImpl;
import com.savbill.logging.LogWrapper;
import com.savbill.preloadpicklist.IPreLoadPickList;
import com.savbill.preloadpicklist.PreloadPicklistImpl;
import com.savbill.util.DatabaseImpl;
import com.savbill.util.ReferenceUtil;
import com.sun.corba.se.impl.protocol.SpecialMethod;

/**
 * @author Nadeem
 *
 */
public class CustomerManagementImpl implements ICustomerManagement {
	
	static DBManagerIMPL databaseObj = new DBManagerIMPL();
	static Connection    dbConnection;

	LogWrapper Log=new LogWrapper(this.getClass().getSimpleName());
	
	@Override
	public JSONObject validateRrNumber(JSONObject object) {
		// TODO Auto-generated method stub
		JSONObject jsonResponse = new JSONObject();
		
		String rrNumber = (String)object.get("rr_no");
		
		if(!rrNumber.isEmpty()){
			if(ReferenceUtil.CheckRrNumberExists(rrNumber)){
				jsonResponse.put("status", "success");
			}else {
				jsonResponse.put("status", "error");
			}
		}else{
			jsonResponse.put("status", "error");
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject GetConsumerRRNoDetails(JSONObject object) {
		System.out.println(object);
		// TODO Auto-generated method stub
		JSONObject jsonResponse = new JSONObject();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
			if(object != null && object.size() > 0 && !object.isEmpty()){
			
				String RRNo = object.get("rrNumber").toString().toUpperCase();
				String Location = object.get("location").toString().toUpperCase();
				String ConnType = object.get("conntype").toString().toUpperCase();
				
				if((RRNo !=  null && RRNo.length() > 0) && 
						(Location != null && Location.length() > 0) && 
							(ConnType != null && ConnType.length() > 0)){
					
					
					
					//Validate RR Number Is in Processing State or not.
					jsonResponse.put("process_status", ReferenceUtil.validateRRStatus(RRNo,Location,ConnType));
					
					//retrieve rr_number details.
					
					CustomerMasterBO custMstr = getConsumerMaster(RRNo,Location,ConnType);
					
					if(custMstr != null){
						jsonResponse.put("status", "success");
						jsonResponse.put("message", "Customer Details Retrieved !!!");
						jsonResponse.put("rrno_detail", custMstr);
						
						//retrieve consumer deposits
						List<CustomerDepositsBO> consumerDepositList = getConsumerDeposits(RRNo,Location,ConnType); 
						jsonResponse.put("rrno_deposits", consumerDepositList);
						
						Iterator<CustomerDepositsBO> itd = consumerDepositList.iterator();
						double sum = 0.0;
						double msdt=0.0;
						double mmd3total = 0.0;
						
						while (itd.hasNext()) {
							CustomerDepositsBO cdp = (CustomerDepositsBO) itd.next();
							
							double big = 0.0;
							
							big = Double.parseDouble(cdp.getCdpAmtPaid());
							
							sum = sum + big ;
							
							//sum = sum.add(cdp.getCdpAmtPaid().toString());

							String purp = cdp.getCdpPymntPurpose();
							//System.out.println("cdp.getCdpPymntPurpose() = "+cdp.getCdpPymntPurpose());
							
							if(cdp.getCdpAmtPaid()!=null ){
								if(purp!=null && purp.length()>0){
									if(purp.equals("2") || purp.equals("6") || purp.equals("74") || purp.equals("75")){
										
										double big1 = 0.0;
										
										big1 = Double.parseDouble(cdp.getCdpAmtPaid());
										
										mmd3total = mmd3total + big1 ;
										
										//mmd3total = mmd3total.add(cdp.getCdpAmtPaid().toString());
									}else if(purp.equals("3") || purp.equals("15") ){
										
										double big2 = 0.0;
										
										big2 = Double.parseDouble(cdp.getCdpAmtPaid());
										
										msdt = msdt + big2 ;
										
										//msdt = msdt.add(cdp.getCdpAmtPaid().toString());
									}
								}
							}

						}
						System.out.println("MSDTOTAL"+ msdt);
						System.out.println("MMDTOTAL"+ mmd3total);
						
						jsonResponse.put("rrno_deposits_MSDTOTAL", msdt);
						jsonResponse.put("rrno_deposits_MMDTOTAL", mmd3total);
						
						//customer deposits ends here
						
						//retrieve customer description.
						
						CustomerDescriptionBO customerDescr = getCustomerDescriptionBO(RRNo,Location,ConnType);System.out.println("customerDescr : "+customerDescr);
						jsonResponse.put("rrno_customerDescription", customerDescr);
						
						
						
						//customer description ends here
						
						//retrieve customer documents.
						
						List<CustomerDocumentBO> customerDocuments = getCustomerDocuments(RRNo,Location,ConnType);System.out.println("customerDocuments : "+customerDocuments);
						jsonResponse.put("rrno_customerDocuments", customerDocuments);
						
						//customer documents ends here
						
						//retrieve customer CTPT-ratio documents.
						
						List<CustomerCTPTratioBO> customerCTPTratio = getCustomerCTPTRation(RRNo,Location,ConnType);System.out.println("ctpt : : "+customerCTPTratio);
						jsonResponse.put("rrno_customerCTPTratio", customerCTPTratio);
						
						//customer ctpt-ratio documents ends here
						
						
						//retrieve customer intimations
						
						List<CustomerIntimationsBO> customerIntimations = getCustomerIntimations(RRNo,Location,ConnType);System.out.println("init : "+customerIntimations);
						jsonResponse.put("rrno_customerIntimations", customerIntimations);
						
						//customer documents ends here
						
						//retrieve Spot Folio.
						
						CustomerSpotFolioBO customerSpotFolio = getCustomerSpotFolioBO(RRNo,Location,ConnType);System.out.println("spot : "+customerSpotFolio);
						jsonResponse.put("rrno_customerSpotFolio", customerSpotFolio);
						
						//customer spot folio ends here
						
						//customer bills
						CustomerBillsBO customerBills = getCustomerBillsBO(RRNo,Location,ConnType);
						jsonResponse.put("rrno_customerBills", customerBills);
						
						//customer bills ends here ...
						
						
						jsonResponse.put("rrno_MMDSERVER", "S");
						jsonResponse.put("rrno_IVRS_ID", ReferenceUtil.getIvrsID(RRNo, ConnType));
						jsonResponse.put("METERCODE", ReferenceUtil.getMeterCodeList(ReferenceUtil.getOMUnitCode(custMstr.getCmOmUnitCd(),ConnType), ConnType));
					}else{
						jsonResponse.put("status", "error");
						jsonResponse.put("message", "RR Number Entered Does Not Exists.Try Again.");
					}
				}else{
					jsonResponse.put("status", "error");
					jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
				}
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Please Enter Valid RR Number And Try Again.");
			}
		} catch (Exception e) {
			// TODO: handle exception
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Database Error . Try Again .");
			System.out.println("Error Occured , getconsumerDetails : "+e.getMessage());
		}
		
		
		return jsonResponse;
	}

	private List<CustomerBillsBO> getCustomerBills(String rRNo,
			String location, String ConnType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		CustomerBillsBO bill= null;
		List<CustomerBillsBO> list = new ArrayList<CustomerBillsBO>();

		try {
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			String qry = " SELECT CB_BILL_DT, CB_BILL_NO FROM CUST_BILL WHERE CB_RR_NO = ? AND substr(CB_RR_NO,1,7) = '"+location+"' ";
			pr = dbConnection.prepareStatement(qry);
			pr.setString(1, rRNo);
			rs = pr.executeQuery();

			while (rs.next()) {
				bill = new CustomerBillsBO();
				bill.setCbBillDt(ReferenceUtil.reverseConvert(rs.getString(1)));
				bill.setCbBillNo(rs.getString(2));
				
				list.add(bill);

			}

		} catch (Exception e) {
			System.out.println("Exception thrown , getCustomerBills" + e);
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}

		return list;
	}
	
	
	private CustomerBillsBO getCustomerBillsBO(String rRNo,
			String location, String ConnType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		CustomerBillsBO bill= new CustomerBillsBO();

		try {
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			String qry = " SELECT CB_BILL_DT, CB_BILL_NO FROM CUST_BILL WHERE CB_RR_NO = ? AND substr(CB_RR_NO,1,7) = '"+location+"' ";
			pr = dbConnection.prepareStatement(qry);
			pr.setString(1, rRNo);
			rs = pr.executeQuery();

			while (rs.next()) {
				bill = new CustomerBillsBO();
				bill.setCbBillDt(ReferenceUtil.reverseConvert(rs.getString(1)));
				bill.setCbBillNo(rs.getString(2));
				
			}

		} catch (Exception e) {
			System.out.println("Exception thrown , getCustomerBills" + e);
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}

		return bill;
	}

	private List<CustomerSpotFolioBO> getCustomerSpotFolio(String rRNo,
			String location, String ConnType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		CustomerSpotFolioBO cmr = null;
		List<CustomerSpotFolioBO> list = new ArrayList<CustomerSpotFolioBO>();

		try {
			 
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			pr = dbConnection.prepareStatement("select * from SPOT_FOLIO where SF_RR_NO=? AND  substr(SF_RR_NO,1,7) = '"+location+"' ");
			pr.setString(1, rRNo);
			rs = pr.executeQuery();
			while (rs.next()) {
				cmr = new CustomerSpotFolioBO();
				cmr.setSpFolioNo(rs.getString("SF_FOLIO_NO"));
				cmr.setSpMrCode(rs.getString("SF_MR_CODE"));
				cmr.setSpMrRdgDay(rs.getString("SF_MR_RDG_DAY"));
				cmr.setSpRRNo(rs.getString("SF_RR_NO").substring(7));
				cmr.setSpUser(rs.getString("SF_USER"));
				cmr.setSpTmpstp(rs.getTimestamp("SF_TMPSTP").toString());

				list.add(cmr);
			}
		} catch (SQLException e) {
			System.out.println("Exception thrown , getCustomerIntimations" + e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		return list;
	}
	
	
	private CustomerSpotFolioBO getCustomerSpotFolioBO(String rRNo,
			String location, String ConnType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		CustomerSpotFolioBO cmr = new CustomerSpotFolioBO();

		try {
			 
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			pr = dbConnection.prepareStatement("select * from SPOT_FOLIO where SF_RR_NO=? AND  substr(SF_RR_NO,1,7) = '"+location+"' ");
			pr.setString(1, rRNo);
			rs = pr.executeQuery();
			while (rs.next()) {
				cmr = new CustomerSpotFolioBO();
				cmr.setSpFolioNo(rs.getString("SF_FOLIO_NO"));
				cmr.setSpMrCode(rs.getString("SF_MR_CODE"));
				cmr.setSpMrRdgDay(rs.getString("SF_MR_RDG_DAY"));
				cmr.setSpRRNo(rs.getString("SF_RR_NO").substring(7));
				cmr.setSpUser(rs.getString("SF_USER"));
				cmr.setSpTmpstp(rs.getTimestamp("SF_TMPSTP").toString());

			}
		} catch (SQLException e) {
			System.out.println("Exception thrown , getCustomerIntimations" + e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		return cmr;
	}

	private List<CustomerIntimationsBO> getCustomerIntimations(String rRNo,
			String location, String ConnType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		CustomerIntimationsBO cmr = null;
		List<CustomerIntimationsBO> list = new ArrayList<CustomerIntimationsBO>();
		try {
			 
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			
			String qry = "SELECT  CUST_INTIMATIONS.rowid as ROW_ID,ROWNUM ROW_NUM,SUBSTR(CI_RR_NO,8) CI_RR_NO , CI_INTM_DT,CI_INTM_TYP,CI_LETR_NO,CI_USER,CI_TMPSTP,CI_RMKS,CCD_DESCR"
					+ " FROM CUST_INTIMATIONS  ,CODE_DETL"
					+ " WHERE CI_RR_NO=? AND substr(CI_RR_NO,1,7) = '"+location+"' AND CCD_CCM_CD_TYP='CONSMR_INT' AND CI_INTM_TYP=CCD_CD_VAL";

			pr = dbConnection.prepareStatement(qry);
			
			pr.setString(1, rRNo);
			rs = pr.executeQuery();
			while (rs.next()) {
				cmr = new CustomerIntimationsBO();
				cmr.setCiRrNo(rs.getString("CI_RR_NO"));
				cmr.setCiIntmDt(ReferenceUtil.reverseConvert(rs.getString("CI_INTM_DT")));
				cmr.setCiIntmTyp(rs.getString("CI_INTM_TYP"));
				cmr.setCiLetrNo(rs.getString("CI_LETR_NO"));
				cmr.setCiUser(rs.getString("CI_USER"));
				cmr.setCiRmks(rs.getString("CI_RMKS"));
				cmr.setCiTmpstp(rs.getTimestamp("CI_TMPSTP").toString());
				cmr.setCodeDescription(rs.getString("CCD_DESCR"));
				cmr.setRowID(rs.getString("ROW_ID"));
				cmr.setRowNUM(rs.getString("ROW_NUM"));
				
				list.add(cmr);

			}

		} catch (SQLException e) {
			System.out.println("Exception thrown  , getCustomerIntimations" + e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
			}

		return list;
	}

	private List<CustomerCTPTratioBO> getCustomerCTPTRation(String rRNo,
			String location, String ConnType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		CustomerCTPTratioBO cmr = null;
		List<CustomerCTPTratioBO> list = new ArrayList<CustomerCTPTratioBO>();

		try {
			 
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			


			pr = dbConnection.prepareStatement("select  CT_PT_RATIO.rowid as ROW_ID,ROWNUM ROW_NUM,CPR_RR_NO,CPR_FROM_DT,CPR_TO_DT,CPR_CT_RATIO_AVAIL,"
					+ "	CPR_PT_RATIO_AVAIL,	CPR_CT_RATIO_NUM, CPR_CT_RATIO_DEN,	CPR_PT_RATIO_NUM,	CPR_PT_RATIO_DEN,"
					+ " CPR_MULT_CONST,	CPR_RMKS, CPR_USER,	CPR_TMPSTP "
					+ "  from CT_PT_RATIO where CPR_RR_NO=? AND substr(CPR_RR_NO,1,7) = '"+location+"' ");
			pr.setString(1, rRNo);
			rs = pr.executeQuery();
			while (rs.next()) {
				cmr = new CustomerCTPTratioBO();
				cmr.setCprRrNo(rs.getString("CPR_RR_NO").substring(7));//System.out.println("1");
				cmr.setCprFromDt(ReferenceUtil.reverseConvert(rs.getString("CPR_FROM_DT")));//System.out.println("2");
				cmr.setCprToDt(ReferenceUtil.reverseConvert(rs.getString("CPR_TO_DT")));//System.out.println("1");
				cmr.setCprCtRatioAvail(rs.getString("CPR_CT_RATIO_AVAIL"));//System.out.println("1");
				cmr.setCprPtRatioAvail(rs.getString("CPR_PT_RATIO_AVAIL"));//System.out.println("1");
				cmr.setCprCtRatioNum(rs.getString("CPR_CT_RATIO_NUM"));//System.out.println("1");
				cmr.setCprCtRatioDen(rs.getString("CPR_CT_RATIO_DEN"));//System.out.println("1");
				cmr.setCprPtRatioNum(rs.getString("CPR_PT_RATIO_NUM"));//System.out.println("1");
				cmr.setCprPtRatioDen(rs.getString("CPR_PT_RATIO_DEN"));//System.out.println("1");
				cmr.setCprMultConst(rs.getString("CPR_MULT_CONST"));//System.out.println("1");
				cmr.setCprRmks(rs.getString("CPR_RMKS"));//ystem.out.println("1");
				cmr.setCprUser(rs.getString("CPR_USER"));//System.out.println("1");
				cmr.setCprTmpstp(rs.getTimestamp("CPR_TMPSTP").toString());//System.out.println("1");
				cmr.setRowID(rs.getString("ROW_ID"));//System.out.println("1");
				cmr.setRowNUM(rs.getString("ROW_NUM"));
				
				list.add(cmr);

			}

		} catch (SQLException e) {
			System.out.println("Exception thrown , getCustomerCTPTRation" + e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}

		return list;
	}

	private List<CustomerDocumentBO> getCustomerDocuments(String rRNo,
			String location, String ConnType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		CustomerDocumentBO cmr = null;
		List<CustomerDocumentBO> list = new ArrayList<CustomerDocumentBO>();
		try {
			 
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			/*String qry = "SELECT CDOC_RR_NO,CDOC_DOC_TYP,CDOC_SL_NO,CDOC_DOC_SUBM_DT,"
					+ " CDOC_USER ,CDOC_TMPSTP,CCD_DESCR FROM CONSUMER_DOCUMENTS,CODE_DETL"
					+ " WHERE CDOC_RR_NO=? AND CDOC_RR_NO "+setup+" AND CCD_CCM_CD_TYP='DOC_TYP' AND CDOC_DOC_TYP=CCD_CD_VAL";*/
			//umesh
			String qry = "SELECT CUST_DOCUMENTS.rowid as ROW_ID,ROWNUM ROW_NUM,CDOC_RR_NO,CDOC_DOC_TYP,CDOC_SL_NO,CDOC_DOC_SUBM_DT,"
					+ " CDOC_USER ,CDOC_TMPSTP,CCD_DESCR FROM CUST_DOCUMENTS,CODE_DETL"
					+ " WHERE CDOC_RR_NO=? AND   substr(CDOC_RR_NO,1,7) = '"+location+"' AND CCD_CCM_CD_TYP='DOC_TYP' AND CDOC_DOC_TYP=CCD_CD_VAL";

			pr = dbConnection.prepareStatement(qry);
			pr.setString(1, rRNo);
			rs = pr.executeQuery();
			while (rs.next()) {
				cmr = new CustomerDocumentBO();
				cmr.setCdocRrno(rs.getString("CDOC_RR_NO").substring(7));
				cmr.setCdocDocTyp(rs.getString("CDOC_DOC_TYP"));
				cmr.setCdocSlNo(rs.getString("CDOC_SL_NO"));
				cmr.setCdocDocSubmDt(ReferenceUtil.reverseConvert(rs.getString("CDOC_DOC_SUBM_DT")));
				cmr.setCdocUser(rs.getString("CDOC_USER"));
				if(rs.getTimestamp("CDOC_TMPSTP")!=null){
				cmr.setCdocTmpstp(rs.getTimestamp("CDOC_TMPSTP").toString());
				}
				cmr.setDocDescription(rs.getString("CCD_DESCR"));
				cmr.setRowID(rs.getString("ROW_ID"));
				cmr.setRowNUM(rs.getString("ROW_NUM"));
				
				list.add(cmr);

			}

		} catch (SQLException e) {
			System.out.println("Exception thrown , getCustomerDocuments :" + e);
		} catch (Exception e) {
			System.out.println("Exception thrown , getCustomerDocuments :" + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}

		return list;
	}

	private List<CustomerDescriptionBO> getCustomerDescription(String rRNo,
			String location, String ConnType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		CustomerDescriptionBO cmr = null;
		List<CustomerDescriptionBO> list = new ArrayList<CustomerDescriptionBO>();

		try {
			
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			
			pr = dbConnection.prepareStatement("select * from CUST_DESCR where CD_RR_NO='"+rRNo+"' AND  substr(CD_RR_NO,1,7) = '"+location+"' ");
			
			
			System.out.println("select * from CUST_DESCR where CD_RR_NO='"+rRNo+"' AND  substr(CD_RR_NO,1,7) = '"+location+"'");
			//pr.setString(1, rRNo);
			rs = pr.executeQuery();
			while (rs.next()) {
				cmr = new CustomerDescriptionBO();
				cmr.setCdRrNo(rs.getString("CD_RR_NO").substring(7));
				cmr.setCdLangCd(rs.getString("CD_LANG_CD"));
				cmr.setCdConsmrName(rs.getString("CD_CONSMR_NAME"));
				cmr.setCdPremiseAddr1(rs.getString("CD_PREMISE_ADDR1"));
				cmr.setCdPremiseAddr2(rs.getString("CD_PREMISE_ADDR2"));
				cmr.setCdPremiseAddr3(rs.getString("CD_PREMISE_ADDR3"));
				cmr.setCdPremiseAddr4(rs.getString("CD_PREMISE_ADDR4"));
				cmr.setCdCorresAddr1(rs.getString("CD_CORRES_ADDR1"));
				cmr.setCdCorresAddr2(rs.getString("CD_CORRES_ADDR2"));
				cmr.setCdCorresAddr3(rs.getString("CD_CORRES_ADDR3"));
				cmr.setCdCorresAddr4(rs.getString("CD_CORRES_ADDR4"));
				cmr.setCdPlaceName(rs.getString("CD_PLACE_NAME"));
				cmr.setCdTmpstp(rs.getString("CD_TMPSTP"));
				

				list.add(cmr);
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown , getCustomerDescription :" + e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception thrown , getCustomerDescription :" + e);
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}

		return list;
	}
	
	
	private CustomerDescriptionBO getCustomerDescriptionBO(String rRNo,
			String location, String ConnType) {
		// TODO Auto-generated method stub
		PreparedStatement pr = null;
		ResultSet rs = null;
		CustomerDescriptionBO cmr = null;

		try {
			
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			
			pr = dbConnection.prepareStatement("select * from CUST_DESCR where CD_RR_NO='"+rRNo+"' AND  substr(CD_RR_NO,1,7) = '"+location+"' ");
			
			
			System.out.println("select * from CUST_DESCR where CD_RR_NO='"+rRNo+"' AND  substr(CD_RR_NO,1,7) = '"+location+"'");
			//pr.setString(1, rRNo);
			rs = pr.executeQuery();
			while (rs.next()) {
				cmr = new CustomerDescriptionBO();
				cmr.setCdRrNo(rs.getString("CD_RR_NO").substring(7));
				cmr.setCdLangCd(rs.getString("CD_LANG_CD"));
				cmr.setCdConsmrName(rs.getString("CD_CONSMR_NAME"));
				cmr.setCdPremiseAddr1(rs.getString("CD_PREMISE_ADDR1"));
				cmr.setCdPremiseAddr2(rs.getString("CD_PREMISE_ADDR2"));
				cmr.setCdPremiseAddr3(rs.getString("CD_PREMISE_ADDR3"));
				cmr.setCdPremiseAddr4(rs.getString("CD_PREMISE_ADDR4"));
				cmr.setCdCorresAddr1(rs.getString("CD_CORRES_ADDR1"));
				cmr.setCdCorresAddr2(rs.getString("CD_CORRES_ADDR2"));
				cmr.setCdCorresAddr3(rs.getString("CD_CORRES_ADDR3"));
				cmr.setCdCorresAddr4(rs.getString("CD_CORRES_ADDR4"));
				cmr.setCdPlaceName(rs.getString("CD_PLACE_NAME"));
				cmr.setCdTmpstp(rs.getString("CD_TMPSTP"));
				

			}

		} catch (SQLException e) {
			System.out.println("Exception thrown , getCustomerDescription :" + e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			System.out.println("Exception thrown , getCustomerDescription :" + e);
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}

		return cmr;
	}
	

	private List<CustomerDepositsBO> getConsumerDeposits(String rRNo, String location,
			String ConnType) {
		// TODO Auto-generated method stub
		
		PreparedStatement pr = null;
		ResultSet rs = null;
		CustomerDepositsBO cmr = null;
		int amt=0; double msdTot=0; double mmdTot=0;
		List<CustomerDepositsBO> list = new ArrayList<CustomerDepositsBO>();
		try {
			 
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			
			String qry = "SELECT  CUST_DEPOSITS.rowid as ROW_ID,ROWNUM ROW_NUM,CDP_RCPT_NO,CDP_RCPT_DT ,"
					+ " NVL((SELECT LC_CNTR_DESC||'('||SUBSTR(LC_CNTR_NO,8)||')' FROM LOC_COUNTERS  WHERE LC_CNTR_NO = CDP_CASH_COUNTR ),'NO NAME')  CDP_CASH_COUNTR_NAME ,"
					+ " CDP_CASH_COUNTR ,CDP_PYMNT_PURPOSE ,"
					+ " CDP_AMT_PAID ,CDP_BILL_NO ,CDP_BILL_DT,CDP_RR_NO,"
					+ " CDP_VALID_TO_DT,CDP_DR_ACCT_CD ,CDP_CR_ACCT_CD ,"
					+ " CDP_USER  ,	CDP_TMPSTP  ,CDP_REMARKS ,NVL(CDP_ADJ_STS,'N') CDP_ADJ_STS,DECODE(NVL(CDP_ADJ_STS,'N'),'Y','Yes','No') CDP_ADJ_STS_DESCR,CCD_DESCR "
					+ " FROM CUST_DEPOSITS,CODE_DETL "
					+ " WHERE CDP_RR_NO= ? AND  substr(CDP_RR_NO,1,7) = '"+location+"'  " 
					+ " AND CCD_CCM_CD_TYP='PYMNT_PURP' AND CDP_PYMNT_PURPOSE=CCD_CD_VAL";

			pr = dbConnection.prepareStatement(qry);
			pr.setString(1, rRNo);
			rs = pr.executeQuery();
			while (rs.next()) {
				cmr = new CustomerDepositsBO();
				cmr.setCdpRcptNo(rs.getString("CDP_RCPT_NO"));
				cmr.setCdpRcptDt(ReferenceUtil.reverseConvert(rs.getString("CDP_RCPT_DT")));
				cmr.setCdpCashCountr(rs.getString("CDP_CASH_COUNTR"));
				cmr.setCdpPymntPurpose(rs.getString("CDP_PYMNT_PURPOSE"));
				cmr.setCdpAmtPaid(rs.getString("CDP_AMT_PAID"));
				cmr.setCdpBillNo(rs.getString("CDP_BILL_NO"));
				cmr.setCdpBillDt(ReferenceUtil.reverseConvert(rs.getString("CDP_BILL_DT")));
				cmr.setCdpRRNO(rs.getString("CDP_RR_NO").substring(7));
				cmr.setCdpValidToDt(ReferenceUtil.reverseConvert(rs.getString("CDP_VALID_TO_DT")));
				cmr.setCdpDrAcctCd(rs.getString("CDP_DR_ACCT_CD"));
				cmr.setCdpCrAcctCd(rs.getString("CDP_CR_ACCT_CD"));
				cmr.setCdpUser(rs.getString("CDP_USER"));
				if(rs.getTimestamp("CDP_TMPSTP")!=null){
					cmr.setCdpTmpstp(rs.getTimestamp("CDP_TMPSTP").toString());
				}
				cmr.setCdpRemarks(rs.getString("CDP_REMARKS"));
				cmr.setCdpAdjSts(rs.getString("CDP_ADJ_STS"));
				cmr.setCdpAdjStsDescription(rs.getString("CDP_ADJ_STS_DESCR"));
				
				
				if(rs.getString("CDP_ADJ_STS").equals("Y")){
					amt=0;
				}else{
					amt=rs.getInt("CDP_AMT_PAID");
				}
				if(rs.getString("CDP_AMT_PAID")!=null && rs.getString("CDP_AMT_PAID").length()>0){
					/*String val=ReferenceUtil.GfnReturnCode(rs.getString("CDP_PYMNT_PURPOSE"),ConnType);*/
					String val= rs.getString("CDP_PYMNT_PURPOSE") ;
					//System.out.println("val = "+val); 
					if(val!=null && val.length()>0){
						if(val.equals("2") || val.equals("6") || val.equals("74") || val.equals("75")){
							msdTot+=amt;
						}else if(val.equals("3") || val.equals("15") ){
							mmdTot+=amt;
						}
					}
				}
				//cmr.setMsdTotal(msdTot);
				//cmr.setMmd3Total(mmdTot);
				cmr.setDepositDescription(rs.getString("CCD_DESCR"));
				cmr.setCashCounterDescr(rs.getString("CDP_CASH_COUNTR_NAME"));
				cmr.setRowID(rs.getString("ROW_ID"));
				cmr.setRowNUM(rs.getString("ROW_NUM"));
				
				list.add(cmr);
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown , getConsumerDeposits :" + e);
		} catch (Exception e) {
			System.out.println("Exception thrown , getConsumerDeposits :" + e);
			// TODO Auto-generated catch block
			e.printStackTrace();
		
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}

		return list;
	}

	private static CustomerMasterBO getConsumerMaster(String rRNo, String location, String ConnType) {
		// TODO Auto-generated method stub
		
		
		PreparedStatement pr = null;
		ResultSet rs = null; 
		CustomerMasterBO consumerBO = null;
		
		try {
			if(ConnType.equals("LT") || ConnType == "LT"){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(ConnType.equals("HT") || ConnType == "HT"){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			String qry = " SELECT C.*,FN_GET_DTC_CAPACITY(C.CM_STATION_NO,C.CM_FDR_NO,C.CM_OM_UNIT_CD,C.CM_TRSFMR_NO,'CAP') DTC_CAP,"
						  + " FN_GET_DTC_CAPACITY(C.CM_STATION_NO,C.CM_FDR_NO,C.CM_OM_UNIT_CD,C.CM_TRSFMR_NO,'DTC_CONN_LD') DTC_CONN_LD,"
						  + " FN_GET_DTC_CAPACITY(C.CM_STATION_NO,C.CM_FDR_NO,C.CM_OM_UNIT_CD,C.CM_TRSFMR_NO,'PER') DTC_CONN_PER "
						  + " FROM CUST_MASTER C where C.CM_RR_NO= ? ";
			pr = dbConnection.prepareStatement(qry);
			pr.setString(1, rRNo);

			rs = pr.executeQuery();
			while (rs.next()) {
				consumerBO = new CustomerMasterBO();
				
				if (rs.getString("CM_RR_NO") != null && rs.getString("CM_RR_NO").length()>10) {
					consumerBO.setCmRrNo(rs.getString("CM_RR_NO").substring(7));
				} 
				
				consumerBO.setCmServiceDt(ReferenceUtil.reverseConvert(rs.getString("CM_SERVICE_DT")));
				
				if (rs.getString("CM_OLD_RR_NO") != null && rs.getString("CM_OLD_RR_NO").length()>10) {
					consumerBO.setCmOldRrNo(rs.getString("CM_OLD_RR_NO").substring(7));
				} 
				consumerBO.setCmKebOfficeFlg(rs.getString("CM_KEB_OFFICE_FLG"));
				consumerBO.setCmPinCd(rs.getString("CM_PIN_CD"));
				consumerBO.setCmTelephoneNo(rs.getString("CM_TELEPHONE_NO"));
				consumerBO.setCmTalukCd(rs.getString("CM_TALUK_CD"));
				consumerBO.setCmTalukDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_TALUK_CD"), "TALUK",ConnType));
				if(rs.getString("CM_SERVICE_DT")!=null && rs.getString("CM_TMP_DURN")!=null ){
					consumerBO.setCmRenewdate(ReferenceUtil.getRenewDate(ReferenceUtil.reverseConvert(rs.getString("CM_SERVICE_DT")), rs.getString("CM_TMP_DURN"))); 
				}
				
				consumerBO.setCmDistrictCd(rs.getString("CM_DISTRICT_CD"));
				consumerBO.setCmDistrictDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_DISTRICT_CD"), "DISTRICT",ConnType));
				consumerBO.setCmStateConstituencyCd(rs.getString("CM_STATE_CONSTITUENCY_CD"));
				consumerBO.setCmStateConstituencyDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_STATE_CONSTITUENCY_CD"), "S_CONSTCY",ConnType));
				consumerBO.setCmCentralConstituencyCd(rs.getString("CM_CENTRAL_CONSTITUENCY_CD"));
				consumerBO.setCmCentralConstituencyDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_CENTRAL_CONSTITUENCY_CD"),"C_CONSTCY",ConnType));
				consumerBO.setCmRegionCd(rs.getString("CM_REGION_CD"));
				consumerBO.setCmRegionDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_REGION_CD"), "REG_TYP",ConnType));
				/*consumerBO.setCmPwrPurpose(ReferenceUtil.getPowerPurposeCodeTOName(rs.getString("CM_PWR_PURPOSE"),ConnType));
				consumerBO.setCmIndustryCd(ReferenceUtil.getIndustrialCodeTOName(rs.getString("CM_INDUSTRY_CD"),ConnType));*/
				consumerBO.setCmPwrPurpose(rs.getString("CM_PWR_PURPOSE"));
				consumerBO.setCmIndustryCd(rs.getString("CM_INDUSTRY_CD"));
				consumerBO.setCmIndustryDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_INDUSTRY_CD"), "IND_PWR_CD",ConnType));
				consumerBO.setCmApplntTyp(rs.getString("CM_APPLNT_TYP"));
				consumerBO.setCmApplntTypDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_APPLNT_TYP"), "APLNT_TYP",ConnType));
				consumerBO.setCmPowerSanctNo(rs.getString("CM_POWER_SANCT_NO"));
				consumerBO.setCmPowerSanctDt(ReferenceUtil.reverseConvert(rs.getString("CM_POWER_SANCT_DT")));
				/*consumerBO.setCmPowerSanctAuth(ReferenceUtil.getPwrSanctionedCodeTOName(rs.getString("CM_POWER_SANCT_AUTH"),ConnType));*/
				consumerBO.setCmPowerSanctAuth(rs.getString("CM_POWER_SANCT_AUTH"));
				
				if (rs.getString("CM_NXT_RR_NO") != null && rs.getString("CM_NXT_RR_NO").length()>10) {
					consumerBO.setCmNxtRrNo(rs.getString("CM_NXT_RR_NO").substring(7));
				} 
				consumerBO.setCmLdgrNo(rs.getString("CM_LDGR_NO"));
				consumerBO.setCmLdgrOpenedDt(ReferenceUtil.reverseConvert(rs.getString("CM_LDGR_OPENED_DT")));
				consumerBO.setCmFolioNo(rs.getString("CM_FOLIO_NO"));
				consumerBO.setCmMtrRdgCycle(rs.getString("CM_MTR_RDG_CYCLE"));
				consumerBO.setCmMtrRdgDay(rs.getString("CM_MTR_RDG_DAY"));
				consumerBO.setCmMtrRdrCd(rs.getString("CM_MTR_RDR_CD"));
				consumerBO.setCmMtrRdrDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_MTR_RDR_CD"), "IND_PWR_CD",ConnType));
				consumerBO.setCmTariffcatogory(ReferenceUtil.getTariffDescByTariffCode(rs.getString("CM_TRF_CATG"),ConnType));
				consumerBO.setCmStationNo(rs.getString("CM_STATION_NO"));
				consumerBO.setCmStationName(ReferenceUtil.getStationName(rs.getString("CM_STATION_NO"),ConnType));
				consumerBO.setCmFdrNo(rs.getString("CM_FDR_NO"));
				consumerBO.setCmFdrName(ReferenceUtil.getFeederName(rs.getString("CM_FDR_NO"),rs.getString("CM_STATION_NO"),ConnType));
				consumerBO.setCmLineMin(rs.getString("CM_LINE_MIN"));
				consumerBO.setCmOmUnitCdName(ReferenceUtil.getOMUnitCodeName(rs.getString("CM_OM_UNIT_CD"),ConnType));
				consumerBO.setCmOmUnitCd(rs.getString("CM_OM_UNIT_CD"));
				consumerBO.setCmTrsfmrNo(rs.getString("CM_TRSFMR_NO"));
				consumerBO.setCmTrsfmrName(ReferenceUtil.getTransformerName(rs.getString("CM_TRSFMR_NO"),rs.getString("CM_FDR_NO"),rs.getString("CM_STATION_NO"),ConnType));
				consumerBO.setCmAvgConsmp(rs.getString("CM_AVG_CONSMP"));
				consumerBO.setCmFlConsumer(rs.getString("CM_FL_CONSUMER"));
				consumerBO.setCmPwrCutExemptFlg(rs.getString("CM_PWR_CUT_EXEMPT_FLG"));
				consumerBO.setCmConsmrSts(rs.getString("CM_CONSMR_STS"));
				consumerBO.setCmConsmrStsDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_CONSMR_STS"), "CONN_STS",ConnType));
				consumerBO.setCmChqBounceFlg(rs.getString("CM_CHQ_BOUNCE_FLG"));
				consumerBO.setCmDlCount(rs.getString("CM_DL_COUNT"));
				consumerBO.setCmDlSent(rs.getString("CM_DL_SENT"));
				consumerBO.setCmTotMmd(rs.getString("CM_TOT_MMD"));
				consumerBO.setCmLastMmdDt(rs.getString("CM_LAST_MMD_DT"));
				consumerBO.setCm100DmdEntlmnt(rs.getString("CM_100_ENERGY_ENTLMNT"));
				consumerBO.setCmTmpFolioNo(rs.getString("CM_TMP_FOLIO_NO"));
				consumerBO.setCm100DmdEntlmnt(rs.getString("CM_100_DMD_ENTLMNT"));
				consumerBO.setCmTrfCatg(rs.getString("CM_TRF_CATG"));
				consumerBO.setCmTrfCatgDescription(ReferenceUtil.getTariffDescByTariffCode(rs.getString("CM_TRF_CATG"),ConnType));
				consumerBO.setCmTrfEffectFrmDt(ReferenceUtil.reverseConvert(rs.getString("CM_TRF_EFFECT_FRM_DT")));
				consumerBO.setCmLdEffectFrmDt(ReferenceUtil.reverseConvert(rs.getString("CM_LD_EFFECT_FRM_DT")));
				consumerBO.setCmLdSanctHp(rs.getString("CM_LD_SANCT_HP"));
				consumerBO.setCmLdSanctKva(rs.getString("CM_LD_SANCT_KVA"));
				consumerBO.setCmLdSanctKw(rs.getString("CM_LD_SANCT_KW"));
				consumerBO.setCmLightLoad(rs.getString("CM_LIGHT_LOAD"));
				consumerBO.setCmHeatLoad(rs.getString("CM_HEAT_LOAD"));
				consumerBO.setCmMotivePower(rs.getString("CM_MOTIVE_POWER"));
				consumerBO.setCmSupplyVolt(rs.getString("CM_SUPPLY_VOLT"));
				consumerBO.setCmPurgeFlg(rs.getString("CM_PURGE_FLG"));
				consumerBO.setCmInstallTyp(rs.getString("CM_INSTALL_TYP"));

				consumerBO.setCmInstallTypDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_INSTALL_TYP"), "INSTL_TYP",ConnType));
				consumerBO.setCmMeteredFlg(rs.getString("CM_METERED_FLG"));
				consumerBO.setCmTlNo(rs.getString("CM_TL_NO"));
				consumerBO.setCmCapacitorCap(rs.getString("CM_CAPACITOR_CAP"));
				/*consumerBO.setCmStarterTyp(ReferenceUtil.getStarterTypeCodeTOName(rs.getString("CM_STARTER_TYP"),ConnType));*/
				consumerBO.setCmStarterTyp(rs.getString("CM_STARTER_TYP"));
				/*consumerBO.setCmPremisJuris(ReferenceUtil.getJurisdictionTypeCodeTOName(rs.getString("CM_PREMIS_JURIS"),ConnType));*/
				consumerBO.setCmPremisJuris(rs.getString("CM_PREMIS_JURIS"));
				/*consumerBO.setCmWellTyp(ReferenceUtil.getWellTypeCodeTOName(rs.getString("CM_WELL_TYP"),ConnType));*/
				consumerBO.setCmWellTyp(rs.getString("CM_WELL_TYP"));
				/*consumerBO.setCmLightingTyp(ReferenceUtil.getLightingCodeTOName(rs.getString("CM_LIGHTING_TYP"),ConnType));*/
				consumerBO.setCmLightingTyp(rs.getString("CM_LIGHTING_TYP"));
				consumerBO.setCmConnLdHp(rs.getString("CM_CONN_LD_HP"));
				consumerBO.setCmConnLdKw(rs.getString("CM_CONN_LD_KW"));
				consumerBO.setCmSupplyEffFrmDt(ReferenceUtil.reverseConvert(rs.getString("CM_SUPPLY_EFF_FRM_DT")));
				consumerBO.setCmPhaseOfInstln(rs.getString("CM_PHASE_OF_INSTLN"));
				consumerBO.setCmTaxExemptFlg(rs.getString("CM_TAX_EXEMPT_FLG"));
				consumerBO.setCmUnauthFlg(rs.getString("CM_UNAUTH_FLG"));
				consumerBO.setCmPwrPurEffFrmDt(ReferenceUtil.reverseConvert(rs.getString("CM_PWR_PUR_EFF_FRM_DT")));
				consumerBO.setCmIndEffFrmDt(ReferenceUtil.reverseConvert(rs.getString("CM_IND_EFF_FRM_DT")));
				/*consumerBO.setCmConnTyp(ReferenceUtil.getConnectionTypeCodeTOName(rs.getString("CM_CONN_TYP"),ConnType));*/
				consumerBO.setCmConnTyp(rs.getString("CM_CONN_TYP"));
				consumerBO.setCmNtrLocCd(rs.getString("CM_NTR_LOC_CD"));
				consumerBO.setCmBjkjOutlet(rs.getString("CM_BJKJ_OUTLET"));
				consumerBO.setCmMinDemandEntl(rs.getString("CM_MIN_DEMAND_ENTL"));
				consumerBO.setCmBulkNoOfHouses(rs.getString("CM_BULK_NO_OF_HOUSES"));
				consumerBO.setCmIvrsId(rs.getString("CM_IVRS_ID"));
				consumerBO.setCmUser(rs.getString("CM_USER"));
				consumerBO.setCmTmpstp(ReferenceUtil.getTimestampe(rRNo,ConnType)); 
				consumerBO.setCmPoleNo(rs.getString("CM_POLE_NO"));
				consumerBO.setCmBegMnth(rs.getString("CM_BEG_MNTH"));
				consumerBO.setCmTmpDurn(rs.getString("CM_TMP_DURN"));
				consumerBO.setCmFirstBillDcFlg(rs.getString("CM_FIRST_BILL_DC_FLG"));
				consumerBO.setCmRmks(rs.getString("CM_RMKS"));
				consumerBO.setCmSubmeterFlg(rs.getString("CM_SUBMETER_FLG"));
				consumerBO.setCmDblMtrFlg(rs.getString("CM_DBL_MTR_FLG"));
				consumerBO.setCmSlumFlg(rs.getString("CM_SLUM_FLG"));
				consumerBO.setCmEmailId(rs.getString("CM_EMAIL_ID"));
				consumerBO.setCmCstNo(rs.getString("CM_CST_NO"));
				consumerBO.setCmKstNo(rs.getString("CM_KST_NO"));
				consumerBO.setCmTinNo(rs.getString("CM_TIN_NO"));
				consumerBO.setCmTodMeterFlag(rs.getString("CM_TOD_METER"));
				consumerBO.setCmTalukDescription(ReferenceUtil.getCodeDesc(rs.getString("CM_TALUK_CD"), "TALUK",ConnType));
				consumerBO.setCmRenewdateExtends(ReferenceUtil.getRenewDateExtends());
				consumerBO.setDTC_CAP(rs.getString("DTC_CAP"));
				consumerBO.setDTC_CONN_LD(rs.getString("DTC_CONN_LD"));
				consumerBO.setDTC_CONN_PER(rs.getString("DTC_CONN_PER"));
				
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown ,getConsumerMaster" + e);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} finally {
			DBManagerResourceRelease.close(pr);
			DBManagerResourceRelease.close(rs);
		}
		return consumerBO;
	}
	
	@Override
	public JSONObject cashCounterList(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String location_code = (String) object.get("location_code");
		String conn_type = (String) object.get("conn_type");
		
		try {
			if(!location_code.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					accountsCS=dbConnection.prepareCall(DBQueries.GET_CASH_COUNTER_LIST);
					accountsCS.setString(1, location_code);
					
					accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(2);
					
					while(accountsRS.next())
					{
						JSONObject ackobj=new JSONObject();
						
						ackobj.put("row_num", accountsRS.getString("row_num"));
						ackobj.put("key", accountsRS.getString("cntr_no"));
						ackobj.put("value", accountsRS.getString("cntr_name"));
		
						array.add(ackobj);
						
					}
					if(array.isEmpty()) {
						obj.put("status", "success");
						obj.put("message", "No Records Found !!!");
					} else{
						obj.put("status", "success");
						obj.put("cash_counters_list", array);
					}
				}else{
					obj.put("status", "error");
					obj.put("message", "Please Enter Proper Input");
				}
			} catch (Exception e) {
				obj.put("status", "fail");
				e.printStackTrace();
				obj.put("message", "database not connected");
			}finally
			{
				DBManagerResourceRelease.close(accountsRS, accountsCS, dbConnection);
			}
			
			return obj;
	}
	
	@Override
	public JSONObject GetSideRRnumberValues(JSONObject object) {
		
		JSONObject jsonResponse = new JSONObject();
		JSONArray jsonarray = new JSONArray();
		JSONObject jsonlist = new JSONObject();
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		try {
		
			if(object != null && object.size() > 0 && !object.isEmpty()){
				
				String RRNo = object.get("rrNumber").toString().toUpperCase();
				String Location = object.get("location").toString().toUpperCase();
				String ConnType = object.get("conntype").toString().toUpperCase();
				
				if((RRNo !=  null && RRNo.length() > 0) && 
						(Location != null && Location.length() > 0) && 
							(ConnType != null && ConnType.length() > 0)){
					

					if(ConnType.equals("LT") || ConnType == "LT"){
						dbConnection = databaseObj.getDatabaseConnection();
					}else if(ConnType.equals("HT") || ConnType == "HT"){
						dbConnection = databaseObj.getHTDatabaseConnection();
					}
						
					/*String Query = "  SELECT  CM_OM_UNIT_CD,Pkg_Tc_Wise_Count.FN_GET_OM_NAME(CM_OM_UNIT_CD) CM_OM_UNIT_NAME, CM_PIN_CD,"
							+ " NVL(CD_CORRES_ADDR2,'') CD_CORRES_ADDR2, NVL(CD_CORRES_ADDR3,'') CD_CORRES_ADDR3, "
							+ " NVL(CD_CORRES_ADDR4,'')  CD_CORRES_ADDR4, " +
					" TO_CHAR(CM_LDGR_OPENED_DT,'DD/MM/YYYY') CM_LDGR_OPENED_DT, NVL(SF_FOLIO_NO,'0')||'A'  SF_FOLIO_NO ,CM_MTR_RDR_CD,CM_MTR_RDG_DAY, " +
					" CD_PLACE_NAME, (SELECT CCD_DESCR FROM CODE_DETL WHERE CCD_CD_VAL=CM_PREMIS_JURIS AND CCD_CCM_CD_TYP='JURD') JURIDICTION , " +
					" NVL(CD_PREMISE_ADDR2,'') CD_PREMISE_ADDR2, NVL(CD_PREMISE_ADDR3,'') CD_PREMISE_ADDR3, " +
					" (SELECT CCD_DESCR FROM CODE_DETL WHERE CCD_CD_VAL=CM_TALUK_CD AND CCD_CCM_CD_TYP='TALUK') TALUK, " +
					" (SELECT CCD_DESCR FROM CODE_DETL WHERE CCD_CD_VAL=CM_DISTRICT_CD AND CCD_CCM_CD_TYP='DISTRICT') DISTRICT, " +
					" (SELECT CCD_DESCR FROM CODE_DETL WHERE CCD_CD_VAL=CM_STATE_CONSTITUENCY_CD AND CCD_CCM_CD_TYP='S_CONSTCY') S_CONSTCY, " +
					" (SELECT CCD_DESCR FROM CODE_DETL WHERE CCD_CD_VAL=CM_CENTRAL_CONSTITUENCY_CD AND CCD_CCM_CD_TYP='C_CONSTCY') C_CONSTCY, " +
					" (SELECT CCD_DESCR FROM CODE_DETL WHERE CCD_CD_VAL=CM_REGION_CD AND CCD_CCM_CD_TYP='REG_TYP') REG_TYP, " +
					" CM_STATION_NO,NVL(Pkg_Tc_Wise_Count.FN_GET_STATION_NAME(CM_STATION_NO),'') STATION_NAME, " +
					" CM_FDR_NO,NVL(Pkg_Tc_Wise_Count.FN_GET_FEEDER_NAME(CM_STATION_NO,CM_FDR_NO),'') FEEDER_NAME, " +
					" CM_TRSFMR_NO, NVL(Pkg_Tc_Wise_Count.FN_GET_TRNFMR_NAME(CM_STATION_NO,CM_FDR_NO,CM_OM_UNIT_CD,CM_TRSFMR_NO),'') TRSFRM_NAME, " +
					" CM_POLE_NO " +
					" FROM CUST_MASTER,CUST_DESCR,SPOT_FOLIO " +
					" WHERE CM_RR_NO=CD_RR_NO AND SF_RR_NO(+)=CM_RR_NO   AND CM_RR_NO='"+RRNo+"'  " ;*/
					
					String Query = "  SELECT  CM_OM_UNIT_CD,Pkg_Tc_Wise_Count.FN_GET_OM_NAME(CM_OM_UNIT_CD) CM_OM_UNIT_NAME, CM_PIN_CD,"
							+ " NVL(CD_LANG_CD,'1') CD_LANG_CD,NVL(CD_CORRES_ADDR2,'') CD_CORRES_ADDR2, NVL(CD_CORRES_ADDR3,'') CD_CORRES_ADDR3, "
							+ " NVL(CD_CORRES_ADDR4,'')  CD_CORRES_ADDR4, " +
					" TO_CHAR(CM_LDGR_OPENED_DT,'DD/MM/YYYY') CM_LDGR_OPENED_DT, NVL(SF_FOLIO_NO,'0')||'A'  SF_FOLIO_NO ,CM_MTR_RDR_CD,CM_MTR_RDG_DAY, " +
					" CD_PLACE_NAME, CM_PREMIS_JURIS JURIDICTION , " +
					" NVL(CD_PREMISE_ADDR2,'') CD_PREMISE_ADDR2, NVL(CD_PREMISE_ADDR3,'') CD_PREMISE_ADDR3,  " +
					" CM_TALUK_CD  TALUK, " +
					" CM_DISTRICT_CD  DISTRICT, " +
					" CM_STATE_CONSTITUENCY_CD  S_CONSTCY, " +
					" CM_CENTRAL_CONSTITUENCY_CD  C_CONSTCY, " +
					 "CM_REGION_CD  REG_TYP, " +
					" CM_STATION_NO,NVL(Pkg_Tc_Wise_Count.FN_GET_STATION_NAME(CM_STATION_NO),'') STATION_NAME, " +
					" CM_FDR_NO,NVL(Pkg_Tc_Wise_Count.FN_GET_FEEDER_NAME(CM_STATION_NO,CM_FDR_NO),'') FEEDER_NAME, " +
					" CM_TRSFMR_NO, NVL(Pkg_Tc_Wise_Count.FN_GET_TRNFMR_NAME(CM_STATION_NO,CM_FDR_NO,CM_OM_UNIT_CD,CM_TRSFMR_NO),'') TRSFRM_NAME, " +
					" CM_POLE_NO " +
					" FROM CUST_MASTER,CUST_DESCR,SPOT_FOLIO " +
					" WHERE CM_RR_NO=CD_RR_NO AND SF_RR_NO(+)=CM_RR_NO   AND CM_RR_NO='"+RRNo+"'  " ;
					
					//pincode,
					
					ps = dbConnection.prepareStatement(Query);
					rs = ps.executeQuery();
					
					if(rs.next()){
						
						jsonResponse.put("status", "success");
						
						//CustomerMasterBO custMstr = getConsumerMaster(RRNo,Location,ConnType);
						
						
						
						
						jsonlist.put("cmOmUnitCd", (rs.getString("CM_OM_UNIT_CD") == null || rs.getString("CM_OM_UNIT_CD") == "") ? "" : rs.getString("CM_OM_UNIT_CD") );
						jsonlist.put("cmOmUnitCdName", (rs.getString("CM_OM_UNIT_NAME") == null || rs.getString("CM_OM_UNIT_NAME") == "") ? "" : rs.getString("CM_OM_UNIT_NAME"));
						jsonlist.put("cmPinCd", (rs.getString("CM_PIN_CD") == null || rs.getString("CM_PIN_CD") == "") ? "" : rs.getString("CM_PIN_CD"));
						/*jsonlist.put("cdCorresAddr2", (rs.getString("CD_CORRES_ADDR2") == null || rs.getString("CD_CORRES_ADDR2") == "") ? "" : rs.getString("CD_CORRES_ADDR2"));
						jsonlist.put("cdCorresAddr3", (rs.getString("CD_CORRES_ADDR3") == null || rs.getString("CD_CORRES_ADDR3") == "") ? "" : rs.getString("CD_CORRES_ADDR3"));
						jsonlist.put("cdCorresAddr4", (rs.getString("CD_CORRES_ADDR4") == null || rs.getString("CD_CORRES_ADDR4") == "") ? "" : rs.getString("CD_CORRES_ADDR4"));
						*/jsonlist.put("cmLdgrOpenedDt", (rs.getString("CM_LDGR_OPENED_DT") == null || rs.getString("CM_LDGR_OPENED_DT") == "") ? "" : rs.getString("CM_LDGR_OPENED_DT"));
						/*jsonlist.put("spFolioNo", (rs.getString("SF_FOLIO_NO") == null || rs.getString("SF_FOLIO_NO") == "") ? "" : rs.getString("SF_FOLIO_NO"));*/
						jsonlist.put("cmMtrRdrCd", (rs.getString("CM_MTR_RDR_CD") == null || rs.getString("CM_MTR_RDR_CD") == "") ? "" : rs.getString("CM_MTR_RDR_CD"));
						jsonlist.put("cmMtrRdgDay", (rs.getString("CM_MTR_RDG_DAY") == null || rs.getString("CM_MTR_RDG_DAY") == "") ? "" : rs.getString("CM_MTR_RDG_DAY"));
						/*jsonlist.put("cdPlaceName", (rs.getString("CD_PLACE_NAME") == null || rs.getString("CD_PLACE_NAME") == "") ? "" : rs.getString("CD_PLACE_NAME"));*/
						jsonlist.put("cmPremisJuris", (rs.getString("JURIDICTION") == null || rs.getString("JURIDICTION") == "") ? "" : rs.getString("JURIDICTION"));
						jsonlist.put("cmTalukCd", (rs.getString("TALUK") == null || rs.getString("TALUK") == "") ? "" : rs.getString("TALUK"));
						jsonlist.put("cmDistrictCd", (rs.getString("DISTRICT") == null || rs.getString("DISTRICT") == "") ? "" : rs.getString("DISTRICT"));
						jsonlist.put("cmStateConstituencyCd", (rs.getString("S_CONSTCY") == null || rs.getString("S_CONSTCY") == "") ? "" : rs.getString("S_CONSTCY"));
						jsonlist.put("cmCentralConstituencyCd", (rs.getString("C_CONSTCY") == null || rs.getString("C_CONSTCY") == "") ? "" : rs.getString("C_CONSTCY"));
						jsonlist.put("cmRegionCd", (rs.getString("REG_TYP") == null || rs.getString("REG_TYP") == "") ? "" : rs.getString("REG_TYP"));
						jsonlist.put("cmStationNo", (rs.getString("CM_STATION_NO") == null || rs.getString("CM_STATION_NO") == "") ? "" : rs.getString("CM_STATION_NO"));
						jsonlist.put("cmStationName", (rs.getString("STATION_NAME") == null || rs.getString("STATION_NAME") == "") ? "" : rs.getString("STATION_NAME"));
						jsonlist.put("cmFdrNo", (rs.getString("CM_FDR_NO") == null || rs.getString("CM_FDR_NO") == "") ? "" : rs.getString("CM_FDR_NO"));
						jsonlist.put("cmFdrName", (rs.getString("FEEDER_NAME") == null || rs.getString("FEEDER_NAME") == "") ? "" : rs.getString("FEEDER_NAME"));
						jsonlist.put("cmTrsfmrNo", (rs.getString("CM_TRSFMR_NO") == null || rs.getString("CM_TRSFMR_NO") == "") ? "" : rs.getString("CM_TRSFMR_NO"));
						jsonlist.put("cmTrsfmrName", (rs.getString("TRSFRM_NAME") == null || rs.getString("TRSFRM_NAME") == "") ? "" : rs.getString("TRSFRM_NAME"));
						jsonlist.put("cmPoleNo", (rs.getString("CM_POLE_NO") == null || rs.getString("CM_POLE_NO") == "") ? "" : rs.getString("CM_POLE_NO"));
						/*jsonlist.put("cdPremiseAddr2", (rs.getString("CD_PREMISE_ADDR2") == null || rs.getString("CD_PREMISE_ADDR2") == "") ? "" : rs.getString("CD_PREMISE_ADDR2"));
						jsonlist.put("cdPremiseAddr3", (rs.getString("CD_PREMISE_ADDR3") == null || rs.getString("CD_PREMISE_ADDR3") == "") ? "" : rs.getString("CD_PREMISE_ADDR3"));
						*/
						
						jsonResponse.put("rrno_detail", jsonlist);
						
						
						jsonlist = new JSONObject();
						
						//jsonlist.put("cdRrNo", "");
						jsonlist.put("cdLangCd", (rs.getString("CD_LANG_CD") == null || rs.getString("CD_LANG_CD") == "") ? "1" : rs.getString("CD_LANG_CD"));
						//jsonlist.put("cdConsmrName", "");
						//jsonlist.put("cdPremiseAddr1","");
						jsonlist.put("cdPremiseAddr2", (rs.getString("CD_PREMISE_ADDR2") == null || rs.getString("CD_PREMISE_ADDR2") == "") ? "" : rs.getString("CD_PREMISE_ADDR2"));
						jsonlist.put("cdPremiseAddr3", (rs.getString("CD_PREMISE_ADDR3") == null || rs.getString("CD_PREMISE_ADDR3") == "") ? "" : rs.getString("CD_PREMISE_ADDR3"));
						//jsonlist.put("cdPremiseAddr4", (rs.getString("CD_PREMISE_ADDR4") == null || rs.getString("CD_PREMISE_ADDR4") == "") ? "" : rs.getString("CD_PREMISE_ADDR4"));
						jsonlist.put("cdCorresAddr2", (rs.getString("CD_CORRES_ADDR2") == null || rs.getString("CD_CORRES_ADDR2") == "") ? "" : rs.getString("CD_CORRES_ADDR2"));
						jsonlist.put("cdCorresAddr3", (rs.getString("CD_CORRES_ADDR3") == null || rs.getString("CD_CORRES_ADDR3") == "") ? "" : rs.getString("CD_CORRES_ADDR3"));
						jsonlist.put("cdCorresAddr4", (rs.getString("CD_CORRES_ADDR4") == null || rs.getString("CD_CORRES_ADDR4") == "") ? "" : rs.getString("CD_CORRES_ADDR4"));
						jsonlist.put("cdPlaceName", (rs.getString("CD_PLACE_NAME") == null || rs.getString("CD_PLACE_NAME") == "") ? "" : rs.getString("CD_PLACE_NAME"));
						//jsonlist.put("cdUsrid", "");
						
						jsonResponse.put("rrno_customerDescription", jsonlist);
						
						jsonlist = new JSONObject();
						
						//jsonlist.put("spRRNo","");
						//jsonlist.put("spMrCode", "");
						//jsonlist.put("spMrRdgDay", "");
						jsonlist.put("spFolioNo", (rs.getString("SF_FOLIO_NO") == null || rs.getString("SF_FOLIO_NO") == "") ? "" : rs.getString("SF_FOLIO_NO"));
						//jsonlist.put("spUser", "");
						
						jsonResponse.put("rrno_customerSpotFolio", jsonlist);
						
						
						
						
						
					}else{
						jsonResponse.put("status", "error");
						jsonResponse.put("status", "No Side RRNo Details Found...! ");
					}
					
				}
				
				
			/*	if(!jsonlist.isEmpty() && jsonlist.size() > 0){
					jsonResponse.put("rrno_detail", jsonlist);
				}else{
					jsonResponse.put("status", "error");
					jsonResponse.put("message", "No Side RRno Details Found.");
				}*/
		}else{
			jsonResponse.put("status", "error");
			jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
		}
			
	} catch (Exception e) {
		jsonResponse.put("status", "error");
		jsonResponse.put("message", "Error In Retrieving Side RRno Details.");
		// TODO: handle exception
	}finally
	{
		DBManagerResourceRelease.close(rs, ps);
	}
	
	return jsonResponse;
	
	
	}
	
	@Override
	public JSONObject GetAppealAmount(JSONObject object) {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;

		JSONObject jsonResponse = new JSONObject();
		
		try {
			if(!object.isEmpty() || !object.isNullObject()){
				
				
					
					String connType = (String) object.get("conn_type").toString().trim();
					String receiptNo = (String) object.get("receipt_no").toString().trim();
					String receiptdate = (String) object.get("receipt_date").toString().trim();
					String cashCounterNo = (String) object.get("cash_counter_no").toString().trim();
					
					if( (connType.length() > 0 && connType != null)
							&& (receiptNo.length() > 0 && receiptNo != null)
							&& (receiptdate.length() > 0 && receiptdate != null)
							&& (cashCounterNo.length() > 0 && cashCounterNo != null)
							){
						
						if(connType.equals("LT") || connType == "LT"){
							dbConnection = databaseObj.getDatabaseConnection();
						}else if(connType.equals("HT") || connType == "HT"){
							dbConnection = databaseObj.getHTDatabaseConnection();
						}
						
						if(dbConnection != null){
							System.out.println("6");
							ps = dbConnection.prepareStatement(" SELECT IRP_PURPOSE,CCD_DESCR, IRP_AMT_PAID, IRP_PAYEE_NAME "
									+ "  FROM CODE_DETL,INITIAL_RCPT_PYMNT "
									+ "  WHERE CCD_CCM_CD_TYP='PYMNT_PURP' AND CCD_CD_VAL=IRP_PURPOSE  "
									+ "  AND IRP_RCPT_NO=  '"+receiptNo+"' "
									+ "  AND IRP_RCPT_DT=TO_DATE('"+receiptdate+"','DD/MM/YYYY') "
									+ "  AND IRP_CASH_COUNTR_NO='"+cashCounterNo+"' ");
							
							
							rs = ps.executeQuery();
							if (rs.next()) {

								jsonResponse.put("status", "success");
								jsonResponse.put("PURPOSE_CODE",rs.getString("IRP_PURPOSE"));
								jsonResponse.put("PURPOSE_DESCR", rs.getString("CCD_DESCR"));
								jsonResponse.put("AMOUNT_PAID", rs.getString("IRP_AMT_PAID"));
								jsonResponse.put("PAYEE_NAME", rs.getString("IRP_PAYEE_NAME"));
							}else{
								jsonResponse.put("status", "error");
								jsonResponse.put("message", " Receipt Details Not Found.");
							}
							
						}else{
							jsonResponse.put("status", "fail");
							jsonResponse.put("message", "Error : Database Not Connected.Contact Admin.");
						}
						
						
					}else{
						jsonResponse.put("status", "error");
						jsonResponse.put("message", "Please Input Mandatory Fields.Try Again.");
					}
				
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		} catch (Exception e) {
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error In Retrieving Receipt Details.");
			// TODO: handle exception
		}finally
		{
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject addOrUpdateConsumerDeposits(JSONObject object) {
		// TODO Auto-generated method stub
		
		JSONObject JsonResponse = new JSONObject();
		PreparedStatement ps =null;
		
		
		try {
			
			if(!object.isEmpty()){
				
/*				String OPTION = (String)object.get("DB_OPTION");
				String CONN_TYPE = (String)object.get("CONN_TYPE");
				String LOCATION = (String)object.get("LOCATION");
				String RR_NO = (String)object.get("DEP_RR_NO");
				String RCPT_NO = (String)object.get("DEP_RCPT_NO");
				String RCPT_DATE = (String)object.get("DEP_RCPT_DT");
				String CASH_CNTR = (String)object.get("DEP_CASH_CNTR");
				String PYMNT_PURP = (String)object.get("DEP_PAYMENT_PURPOSE");
				String AMOUNT_PAID = (String)object.get("DEP_AMOUNT_PAID");
				String REMARKS = (String)object.get("DEP_REMARKS");
				String ADJ_STS = (String)object.get("DEP_ADJUSTMENT_STS");
				String DEBIT_AC_CODE = (String)object.get("DEP_DEBIT_AC_CODE");
				String CREDIT_AC_CODE = (String)object.get("DEP_CREDIT_AC_CODE");
				String USER_ID = (String)object.get("DEP_USER_ID");*/
				
				String OPTION = (String)object.get("option");
				String CONN_TYPE = (String)object.get("conn_type");
				String RR_NO = (String)object.get("cdpRRNO").toString().toUpperCase();
				String PYMNT_PURP = (String)object.get("cdpPymntPurpose");
				String AMOUNT_PAID = (String)object.get("cdpAmtPaid");
				String cdpBillNo = (String)object.get("cdpBillNo");
				String cdpBillDt = (String)object.get("cdpBillDt");
				String cdpValidToDt = (String)object.get("cdpValidToDt");
				String DEBIT_AC_CODE = (String)object.get("cdpDrAcctCd");
				String CREDIT_AC_CODE = (String)object.get("cdpCrAcctCd");
				String USER_ID = (String)object.get("cdpUser");
				String cdpTmpstp = (String)object.get("cdpTmpstp");
				String REMARKS = (String)object.get("cdpRemarks");
				String ADJ_STS = (String)object.get("cdpAdjSts");
				String RCPT_NO = (String)object.get("cdpRcptNo");
				String RCPT_DATE = (String)object.get("cdpRcptDt");
				String CASH_CNTR = (String)object.get("cdpCashCountr");
				String cashCounterDescr = (String)object.get("cashCounterDescr");
				String depositDescription = (String)object.get("depositDescription");
				String rowID = (String)object.get("rowID");
				String cdpAmtPaids = (String)object.get("cdpAmtPaids");
				String msdTotal = (String)object.get("msdTotal");
				String mmd3Total = (String)object.get("mmd3Total");
				
				RR_NO          = ReferenceUtil.ConvertIFNullToString(RR_NO);
				PYMNT_PURP          = ReferenceUtil.ConvertIFNullToString(PYMNT_PURP);
				AMOUNT_PAID          = ReferenceUtil.ConvertIFNullToString(AMOUNT_PAID);
				cdpBillNo          = ReferenceUtil.ConvertIFNullToString(cdpBillNo);
				cdpBillDt          = ReferenceUtil.ConvertIFNullToString(cdpBillDt);
				cdpValidToDt          = ReferenceUtil.ConvertIFNullToString(cdpValidToDt);
				DEBIT_AC_CODE          = ReferenceUtil.ConvertIFNullToString(DEBIT_AC_CODE);
				CREDIT_AC_CODE          = ReferenceUtil.ConvertIFNullToString(CREDIT_AC_CODE);
				USER_ID          = ReferenceUtil.ConvertIFNullToString(USER_ID);
				cdpTmpstp          = ReferenceUtil.ConvertIFNullToString(cdpTmpstp);
				REMARKS          = ReferenceUtil.ConvertIFNullToString(REMARKS);
				ADJ_STS          = ReferenceUtil.ConvertIFNullToString(ADJ_STS);
				RCPT_NO          = ReferenceUtil.ConvertIFNullToString(RCPT_NO);
				RCPT_DATE          = ReferenceUtil.ConvertIFNullToString(RCPT_DATE);
				CASH_CNTR          = ReferenceUtil.ConvertIFNullToString(CASH_CNTR);
				cashCounterDescr          = ReferenceUtil.ConvertIFNullToString(cashCounterDescr);
				
				depositDescription          = ReferenceUtil.ConvertIFNullToString(depositDescription);
				rowID          = ReferenceUtil.ConvertIFNullToString(rowID);
				cdpAmtPaids          = ReferenceUtil.ConvertIFNullToString(cdpAmtPaids);
				msdTotal          = ReferenceUtil.ConvertIFNullToString(msdTotal);
				mmd3Total          = ReferenceUtil.ConvertIFNullToString(mmd3Total);
				
				int i = 0;
				String update_Query,insert_Query = "" ;
				
				if(CONN_TYPE.equals("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(CONN_TYPE.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				dbConnection.setAutoCommit(false);
				
				if(OPTION.equals("ADD")){
					
					
					
					insert_Query = " INSERT INTO CUST_DEPOSITS (CDP_RR_NO, CDP_RCPT_NO, CDP_RCPT_DT, CDP_CASH_COUNTR, CDP_PYMNT_PURPOSE,"
							+ " CDP_AMT_PAID, CDP_DR_ACCT_CD,CDP_CR_ACCT_CD, CDP_USER, CDP_TMPSTP,CDP_REMARKS,CDP_ADJ_STS ) VALUES"
							+ "('"+RR_NO+"','"+RCPT_NO+"',TO_DATE('"+RCPT_DATE+"','DD/MM/YYYY'),'"+CASH_CNTR+"','"+PYMNT_PURP+"','"+AMOUNT_PAID+"','"+DEBIT_AC_CODE+"',"
							+ "'"+CREDIT_AC_CODE+"','"+USER_ID+"',sysdate,'"+REMARKS+"','"+ADJ_STS+"') ";
					
					ps = dbConnection.prepareStatement(insert_Query);
					
					i = ps.executeUpdate();
					
					if(i > 0){
						JsonResponse.put("status","success");
						JsonResponse.put("message", "Deposit Inserted Successfully.");
						
						dbConnection.commit();
					}else{
						JsonResponse.put("status","error");
						JsonResponse.put("message", "Deposit Not Inserted.");
						
						dbConnection.rollback();
					}
					
				}else if(OPTION.equals("UPDATE")){
					
					update_Query = "  UPDATE CUST_DEPOSITS SET CDP_RCPT_NO = '"+RCPT_NO+"' , CDP_RCPT_DT = TO_DATE('"+RCPT_DATE+"','DD/MM/YYYY') , "
							+ "  CDP_CASH_COUNTR = '"+CASH_CNTR+"', CDP_PYMNT_PURPOSE = '"+PYMNT_PURP+"', CDP_AMT_PAID = '"+AMOUNT_PAID+"' ,"
							+ "  CDP_DR_ACCT_CD  = '"+DEBIT_AC_CODE+"', CDP_CR_ACCT_CD = '"+CREDIT_AC_CODE+"', CDP_USER = '"+USER_ID+"', "
						    + "  CDP_TMPSTP = SYSDATE ,CDP_REMARKS = '"+REMARKS+"',CDP_ADJ_STS = '"+ADJ_STS+"'  "
						    + "  WHERE  CDP_RR_NO = '"+RR_NO+"' AND  CDP_RCPT_NO = '"+RCPT_NO+"' AND  CDP_RCPT_DT = TO_DATE('"+RCPT_DATE+"','DD/MM/YYYY') "
						    + "  AND CDP_CASH_COUNTR = '"+CASH_CNTR+"'  ";
					System.out.println(update_Query);
					ps = dbConnection.prepareStatement(update_Query);
					
					i = ps.executeUpdate();System.out.println("1");
					
				}else{
					JsonResponse.put("status","error");
					JsonResponse.put("message", "Sorry ! Not a Valid Option.");
				}
				
				
				if(i > 0){System.out.println("2");
				
				update_Query = "";
				update_Query = "UPDATE CUST_MASTER SET CM_USER = '"+USER_ID+"',   "
						+ "  CM_TMPSTP = SYSDATE "
						+ " WHERE CM_RR_NO = '"+RR_NO+"' " ;
				System.out.println("3");
				ps = dbConnection.prepareStatement(update_Query);System.out.println("4 : "+update_Query);
				int j = ps.executeUpdate();System.out.println("5");
				
				if(j > 0){
					JsonResponse.put("status","success");
					JsonResponse.put("message", "Deposit Updated Successfully.");
					
					dbConnection.commit();System.out.println("6");
				}else{
					JsonResponse.put("status","error");
					JsonResponse.put("message", "Deposit Not Updated.");
					
					dbConnection.rollback();
				}
			}else{
				JsonResponse.put("status","error");
				JsonResponse.put("message", "Deposit Not Updated.");
				
				dbConnection.rollback();
			}
				
				

				/*List<CustomerDepositsBO> consumerDepositList = getConsumerDeposits(RR_NO,LOCATION,CONN_TYPE); 
				JsonResponse.put("rrno_deposits", consumerDepositList);*/
				
			}else{
				JsonResponse.put("status", "error");
				JsonResponse.put("message", "No Deposit Record Found To Insert.");
			}
		} catch(SQLException e){
			e.printStackTrace();
			System.out.println("Error Occured , Insert Deposits : "+e.getMessage());
			JsonResponse.put("status", "fail");
			JsonResponse.put("message", "Error : Deposit Not Inserted.");
			
			try {
				dbConnection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error Occured , Insert Deposits : "+e.getMessage());
			JsonResponse.put("status", "fail");
			JsonResponse.put("message", "Error : Deposit Not Inserted.");
			
			try {
				dbConnection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally {
			DBManagerResourceRelease.close(ps);
			
			try {
				dbConnection.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return JsonResponse;
	}
	
	@Override
	public JSONObject addOrUpdateConsumerDocuments(JSONObject object) {
		// TODO Auto-generated method stub
		JSONObject JsonResponse = new JSONObject();
		PreparedStatement ps =null;
		
		
		try {
			
			if(!object.isEmpty()){
				
				
				String OPTION = (String)object.get("option");
				String CONN_TYPE = (String)object.get("conn_type");
				String cdocRrno = (String)object.get("cdocRrno").toString().toUpperCase();
				String cdocDocSubmDt = (String)object.get("cdocDocSubmDt");
				String cdocUser = (String)object.get("cdocUser");
				String cdocTmpstp = (String)object.get("cdocTmpstp");
				String cdocDocTyp = (String)object.get("cdocDocTyp");
				String cdocSlNo = (String)object.get("cdocSlNo");
				String docDescription = (String)object.get("docDescription");
				String rowID = (String)object.get("rowID");
				int cdocSlNo_int = 0;
				
				cdocRrno              = ReferenceUtil.ConvertIFNullToString(cdocRrno);
				cdocDocSubmDt              = ReferenceUtil.ConvertIFNullToString(cdocDocSubmDt);
				cdocUser              = ReferenceUtil.ConvertIFNullToString(cdocUser);
				cdocTmpstp              = ReferenceUtil.ConvertIFNullToString(cdocTmpstp);
				cdocDocTyp              = ReferenceUtil.ConvertIFNullToString(cdocDocTyp);
				cdocSlNo              = ReferenceUtil.ConvertIFNullToString(cdocSlNo);
				docDescription              = ReferenceUtil.ConvertIFNullToString(docDescription);
				
				int i = 0;
				String  strDocumentsDesc = ""; 
				String update_Query = "";
				
				if(CONN_TYPE.equals("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(CONN_TYPE.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				dbConnection.setAutoCommit(false);
				
				
				if(OPTION.equals("ADD")){
					
					cdocSlNo_int = ReferenceUtil.getMaxSLNO(cdocRrno, "CUST_DOCUMENTS", "CDOC_SL_NO", CONN_TYPE,"CDOC_RR_NO") ;
					
	                strDocumentsDesc = strDocumentsDesc + "INSERT INTO CUST_DOCUMENTS ";
	                strDocumentsDesc = strDocumentsDesc + "(CDOC_RR_NO,CDOC_SL_NO,CDOC_DOC_TYP,CDOC_DOC_SUBM_DT,CDOC_USER,CDOC_TMPSTP) ";
	                strDocumentsDesc = strDocumentsDesc + " VALUES ";

	                strDocumentsDesc = strDocumentsDesc + "('" + cdocRrno + "', ";
	                strDocumentsDesc = strDocumentsDesc + " '" + cdocSlNo_int + "' , '"
						/*+ ReferenceUtil.getNameToCodeCommon(docDescription, "DOC_TYP", CONN_TYPE) + "', ";*/
	                		+ cdocDocTyp + "' ,  " ;
	                strDocumentsDesc = strDocumentsDesc + "  TO_DATE('" + cdocDocSubmDt + "','dd/MM/yyyy'), ";
	                strDocumentsDesc = strDocumentsDesc + "  '" + cdocUser
						+ "', TO_DATE(SYSDATE, 'dd/MM/yyyy') )";  
	                
	                System.out.println(strDocumentsDesc);
					
					ps = dbConnection.prepareStatement(strDocumentsDesc);
					
					 i = ps.executeUpdate();
					
					/*if(i > 0){
						JsonResponse.put("status","success");
						JsonResponse.put("message", "Documents Inserted Successfully.");
						
						dbConnection.commit();
					}else{
						JsonResponse.put("status","error");
						JsonResponse.put("message", "Documents Not Inserted.");
						
						dbConnection.rollback();
					}*/
					
				}else if(OPTION.equals("UPDATE")){
					
					update_Query = "  UPDATE CUST_DOCUMENTS SET  "
							+ "  CDOC_RR_NO = '"+cdocRrno+"' ,CDOC_SL_NO = '"+cdocSlNo+"' ,CDOC_DOC_TYP = '"+cdocDocTyp+"', "
							+ "  CDOC_DOC_SUBM_DT = TO_DATE('"+cdocDocSubmDt+"','DD/MM/YYYY') ,CDOC_USER = '"+cdocUser+"' ,CDOC_TMPSTP = SYSDATE  "
						    + "  WHERE  CDOC_RR_NO = '"+cdocRrno+"'  "
						    + "  AND    CDOC_SL_NO = '"+cdocSlNo+"' "
						    //+ "  AND    CDOC_DOC_SUBM_DT = TO_DATE('"+cdocDocSubmDt+"','DD/MM/YYYY') "
						    ; 
						   // + "  AND    CDOC_DOC_TYP = '"+cdocDocTyp+"' ";
					
					ps = dbConnection.prepareStatement(update_Query);
					
					System.out.println(update_Query);
					
					i = ps.executeUpdate();

				}else{
					JsonResponse.put("status","error");
					JsonResponse.put("message", "Sorry ! Not a Valid Option.");
				}
				
				
				if(i > 0){
					
					update_Query = "";
					update_Query = "UPDATE CUST_MASTER SET CM_USER = '"+cdocUser+"',   "
							+ "  CM_TMPSTP = SYSDATE "
							+ " WHERE CM_RR_NO = '"+cdocRrno+"' " ;
					
					ps = dbConnection.prepareStatement(update_Query);
					int j = ps.executeUpdate();
					
					if(j > 0){
						JsonResponse.put("status","success");
						JsonResponse.put("cdocSlNo",cdocSlNo_int+"");
						JsonResponse.put("message", "Documents Updated Successfully.");
						
						dbConnection.commit();
						
					}else{
						JsonResponse.put("status","error");
						JsonResponse.put("message", "Documents Not Updated.");
						
						dbConnection.rollback();
					}
				}else{
					JsonResponse.put("status","error");
					JsonResponse.put("message", "Documents Not Updated.");
					
					dbConnection.rollback();
				}

			}else{
				JsonResponse.put("status", "error");
				JsonResponse.put("message", "No Documents Record Found To Insert/Update.");
			}
		} catch(SQLException e){
			e.printStackTrace();
			System.out.println("Error Occured , Insert Documents : "+e.getMessage());
			JsonResponse.put("status", "error");
			JsonResponse.put("message", "Error : Documents Not Inserted/Updated.");
			
			try {
				dbConnection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error Occured , Insert Documents : "+e.getMessage());
			JsonResponse.put("status", "error");
			JsonResponse.put("message", "Error : Documents Not Inserted/Updated.");
			
			try {
				dbConnection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally {
			DBManagerResourceRelease.close(ps);
			
			try {
				dbConnection.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return JsonResponse;
	}

	@Override
	public JSONObject addOrUpdateConsumerCT_PT_RATIO(JSONObject object) {
		// TODO Auto-generated method stub
		JSONObject JsonResponse = new JSONObject();
		PreparedStatement ps =null;
		
		
		try {
			
			if(!object.isEmpty()){
				
				
				String OPTION = (String)object.get("option");
				String CONN_TYPE = (String)object.get("conn_type");
				String cprRrNo = (String)object.get("cprRrNo").toString().toUpperCase();
				String cprFromDt = (String)object.get("cprFromDt");
				String cprToDt = (String)object.get("cprToDt");
				String cprCtRatioAvail = (String)object.get("cprCtRatioAvail");
				String cprPtRatioAvail = (String)object.get("cprPtRatioAvail");
				String cprCtRatioNum = (String)object.get("cprCtRatioNum");
				String cprCtRatioDen = (String)object.get("cprCtRatioDen");
				String cprPtRatioNum = (String)object.get("cprPtRatioNum");
				String cprPtRatioDen = (String)object.get("cprPtRatioDen");
				String cprMultConst = (String)object.get("cprMultConst");
				String cprRmks = (String)object.get("cprRmks");
				String cprUser = (String)object.get("cprUser");
				String cprTmpstp = (String)object.get("cprTmpstp");
				String rowID = (String)object.get("rowID");
				
				
				cprFromDt              = ReferenceUtil.ConvertIFNullToString(cprFromDt);
				cprToDt                = ReferenceUtil.ConvertIFNullToString(cprToDt);
				cprCtRatioAvail        = ReferenceUtil.ConvertIFNullToString(cprCtRatioAvail);
				cprPtRatioAvail        = ReferenceUtil.ConvertIFNullToString(cprPtRatioAvail);
				cprCtRatioNum          = ReferenceUtil.ConvertIFNullToString(cprCtRatioNum);
				cprCtRatioDen          = ReferenceUtil.ConvertIFNullToString(cprCtRatioDen);
				cprPtRatioNum          = ReferenceUtil.ConvertIFNullToString(cprPtRatioNum);
				cprPtRatioDen          = ReferenceUtil.ConvertIFNullToString(cprPtRatioDen);
				cprMultConst           = ReferenceUtil.ConvertIFNullToString(cprMultConst);
				cprRmks                = ReferenceUtil.ConvertIFNullToString(cprRmks);
				cprUser                = ReferenceUtil.ConvertIFNullToString(cprUser);
				cprTmpstp              = ReferenceUtil.ConvertIFNullToString(cprTmpstp);
				rowID                  = ReferenceUtil.ConvertIFNullToString(rowID);
				
				int i = 0; 
				String  strCTPTDesc = "";
				String update_Query = "";
				if(CONN_TYPE.equals("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(CONN_TYPE.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				dbConnection.setAutoCommit(false);
				
				
				
				
				if(OPTION.equals("ADD")){
					
					
					
	                strCTPTDesc = strCTPTDesc + "INSERT INTO CT_PT_RATIO ";
	                strCTPTDesc = strCTPTDesc + "(CPR_RR_NO,CPR_FROM_DT,CPR_TO_DT,CPR_CT_RATIO_AVAIL,CPR_PT_RATIO_AVAIL,CPR_CT_RATIO_NUM, ";
	                strCTPTDesc = strCTPTDesc + "CPR_CT_RATIO_DEN,CPR_PT_RATIO_NUM,CPR_PT_RATIO_DEN,CPR_MULT_CONST,CPR_RMKS,CPR_USER,CPR_TMPSTP)";
	                strCTPTDesc = strCTPTDesc + " VALUES "  ;
							
	                strCTPTDesc =  strCTPTDesc + "('"+cprRrNo+"', " ;
	                strCTPTDesc = strCTPTDesc + " TO_DATE('"+cprFromDt+"','dd/mm/yyyy') ,";
	                strCTPTDesc = strCTPTDesc + " TO_DATE('"+cprToDt+"','dd/mm/yyyy') ,";
	                strCTPTDesc = strCTPTDesc + " '"+cprCtRatioAvail+"', " ;
	                strCTPTDesc = strCTPTDesc + " '"+cprPtRatioAvail+"', ";
	                strCTPTDesc = strCTPTDesc + "  "+cprCtRatioNum+",";
	                strCTPTDesc = strCTPTDesc + "  "+cprCtRatioDen+",";
	                strCTPTDesc = strCTPTDesc + "  "+cprPtRatioNum+",";
	                strCTPTDesc = strCTPTDesc + "  "+cprPtRatioDen+",";
	                strCTPTDesc = strCTPTDesc + "  "+Math.round(Double.parseDouble(cprMultConst))+",";
	                strCTPTDesc = strCTPTDesc + "  '"+cprRmks+"',";
	                strCTPTDesc = strCTPTDesc + "  '"+cprUser+"' ,TO_DATE(SYSDATE, 'dd/MM/yyyy') ) "; 
					
					ps = dbConnection.prepareStatement(strCTPTDesc);
					
					 i = ps.executeUpdate();
					
					/*if(i > 0){
						JsonResponse.put("status","error");
						JsonResponse.put("message", "CT_PT_RATIO Inserted Successfully.");
						dbConnection.commit();
					}else{
						JsonResponse.put("status","error");
						JsonResponse.put("message", "CT_PT_RATIO Not Inserted.");
						
						dbConnection.rollback();
					}*/
					
				}else if(OPTION.equals("UPDATE")){
					
					update_Query = "  UPDATE CT_PT_RATIO SET  "
							+ "  CPR_RR_NO= '"+cprRrNo+"' ,CPR_FROM_DT =  TO_DATE('"+cprFromDt+"','DD/MM/YYYY')  , "
							+ "  CPR_TO_DT= TO_DATE('"+cprToDt+"','DD/MM/YYYY') ,"
							+ "  CPR_CT_RATIO_AVAIL= '"+cprCtRatioAvail+"' ,CPR_PT_RATIO_AVAIL= '"+cprPtRatioAvail+"' ,"
							+ "  CPR_CT_RATIO_NUM= '"+cprCtRatioNum+"' ,"
							+ "  CPR_CT_RATIO_DEN= '"+cprCtRatioDen+"' ,CPR_PT_RATIO_NUM= '"+cprPtRatioNum+"' ,CPR_PT_RATIO_DEN= '"+cprPtRatioDen+"' ,"
							+ "  CPR_MULT_CONST= '"+cprMultConst+"' ,CPR_RMKS= '"+cprRmks+"' ,CPR_USER = '"+cprUser+"' ,CPR_TMPSTP= SYSDATE  "
						    + "  WHERE  CPR_RR_NO = '"+cprRrNo+"'  "
						    + "  AND    CPR_FROM_DT = TO_DATE('"+cprFromDt+"','DD/MM/YYYY') ";
					
					ps = dbConnection.prepareStatement(update_Query);
					
					i = ps.executeUpdate();

				}else{
					JsonResponse.put("status","error");
					JsonResponse.put("message", "Sorry ! Not a Valid Option.");
				}
				
				
				if(i > 0){
					update_Query = "";
					update_Query = "UPDATE CUST_MASTER SET CM_USER = '"+cprUser+"' ,  "
							+ "  CM_TMPSTP = SYSDATE "
							+ " WHERE CM_RR_NO = '"+cprRrNo+"' " ;
					
					ps = dbConnection.prepareStatement(update_Query);
					int j = ps.executeUpdate();
					
					if(j > 0){
						JsonResponse.put("status","success");
						JsonResponse.put("message", "CT_PT_RATIO Updated Successfully.");
						
						dbConnection.commit();
					}else{
						JsonResponse.put("status","error");
						JsonResponse.put("message", "CT_PT_RATIO Not Updated.");
						
						dbConnection.rollback();
					}
				}else{
					JsonResponse.put("status","error");
					JsonResponse.put("message", "CT_PT_RATIO Not Updated.");
					
					dbConnection.rollback();
				}
				
				

			}else{
				JsonResponse.put("status", "error");
				JsonResponse.put("message", "No CT_PT_RATIO Record Found To Insert/Update.");
			}
		} catch(SQLException e){
			e.printStackTrace();
			System.out.println("Error Occured , Insert CT_PT_RATIO : "+e.getMessage());
			JsonResponse.put("status", "error");
			JsonResponse.put("message", "Error : CT_PT_RATIO Not Inserted/Updated.");
			
			try {
				dbConnection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error Occured , Insert CT_PT_RATIO : "+e.getMessage());
			JsonResponse.put("status", "error");
			JsonResponse.put("message", "Error : CT_PT_RATIO Not Inserted/Updated.");
			
			try {
				dbConnection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}finally {
			DBManagerResourceRelease.close(ps);
			
			try {
				dbConnection.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			};
		}
		return JsonResponse;
	}

	@Override
	public JSONObject addOrUpdateConsumerIntimations(JSONObject object) {
		// TODO Auto-generated method stub
		JSONObject JsonResponse = new JSONObject();
		PreparedStatement ps =null;
		
		
		try {
			
			if(!object.isEmpty()){
				
				
				String OPTION = (String)object.get("option");
				String CONN_TYPE = (String)object.get("conn_type");
				String ciRrNo = (String)object.get("ciRrNo").toString().toUpperCase();
				String ciIntmTyp = (String)object.get("ciIntmTyp");
				String codeDescription = (String)object.get("codeDescription");
				String ciIntmDt = (String)object.get("ciIntmDt");
				String ciLetrNo = (String)object.get("ciLetrNo");
				String ciUser = (String)object.get("ciUser");
				String ciTmpstp = (String)object.get("ciTmpstp");
				String ciRmks = (String)object.get("ciRmks");
				String rowID = (String)object.get("rowID");
				
				ciRrNo          = ReferenceUtil.ConvertIFNullToString(ciRrNo);
				ciIntmTyp       = ReferenceUtil.ConvertIFNullToString(ciIntmTyp);
				codeDescription = ReferenceUtil.ConvertIFNullToString(codeDescription);
				ciIntmDt        = ReferenceUtil.ConvertIFNullToString(ciIntmDt);
				ciLetrNo        = ReferenceUtil.ConvertIFNullToString(ciLetrNo);
				ciUser          = ReferenceUtil.ConvertIFNullToString(ciUser);
				ciTmpstp        = ReferenceUtil.ConvertIFNullToString(ciTmpstp);
				ciRmks          = ReferenceUtil.ConvertIFNullToString(ciRmks);
				rowID           = ReferenceUtil.ConvertIFNullToString(rowID);
				
				
				int i = 0 ;
				
				String  strIntimationDesc = "" , update_Query= "";
				
				if(CONN_TYPE.equals("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(CONN_TYPE.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				
				dbConnection.setAutoCommit(false);
				
				
				if(OPTION.equals("ADD")){
					
					
	                strIntimationDesc = strIntimationDesc + "INSERT INTO CUST_INTIMATIONS ";
	                strIntimationDesc = strIntimationDesc + "(CI_RR_NO,CI_INTM_DT,CI_INTM_TYP,CI_LETR_NO,CI_RMKS,CI_USER,CI_TMPSTP ) ";
	                strIntimationDesc = strIntimationDesc + " VALUES ";

	               
	                strIntimationDesc =  strIntimationDesc + "('"+ciRrNo+"', " ;
	                strIntimationDesc = strIntimationDesc + "  TO_DATE('"+ciIntmDt+"','dd/MM/yyyy'), " ;
	               // strIntimationDesc = strIntimationDesc + " '"+ReferenceUtil.getNameToCodeCommon(codeDescription,"CONSMR_INT",CONN_TYPE)+"', " ;
	                strIntimationDesc = strIntimationDesc + " '"+ciIntmTyp+"', " ;
	                strIntimationDesc = strIntimationDesc + " '"+ciLetrNo+"','"+ciRmks+"', ";
	                strIntimationDesc = strIntimationDesc + " '"+ciUser+"', SYSDATE )" ;
	                
	                
	                System.out.println(strIntimationDesc);
	                
					
					ps = dbConnection.prepareStatement(strIntimationDesc);
					
					i = ps.executeUpdate();
					
					/*if(i > 0){
						JsonResponse.put("status","success");
						JsonResponse.put("message", "INTIMATIONS Inserted Successfully.");
						
						dbConnection.commit();
					}else{
						JsonResponse.put("status","error");
						JsonResponse.put("message", "INTIMATIONS Not Inserted.");
						
						dbConnection.rollback();
					}*/
					
				}else if(OPTION.equals("UPDATE")){
					
					 update_Query = "  UPDATE CUST_INTIMATIONS SET"
							+ "  CI_RR_NO = '"+ciRrNo+"',CI_INTM_DT = TO_DATE('"+ciIntmDt+"','DD/MM/YYYY'),CI_INTM_TYP = '"+ciIntmTyp+"' ,"
						    + "  CI_LETR_NO  = '"+ciLetrNo+"',CI_RMKS = '"+ciRmks+"',CI_USER = '"+ciUser+"',"
						    + "  CI_TMPSTP  = SYSDATE  "
						    + "  WHERE  CI_RR_NO = '"+ciRrNo+"'  "
						    + "  AND  CI_INTM_DT = TO_DATE('"+ciIntmDt+"','DD/MM/YYYY') "
						    + "  AND  CI_LETR_NO  = '"+ciLetrNo+"' ";
					
					
					
					ps = dbConnection.prepareStatement(update_Query);
					
					i = ps.executeUpdate();
					
				}else{
					JsonResponse.put("status","error");
					JsonResponse.put("message", "Sorry ! Not a Valid Option.");
				}
				
				
				if(i > 0){
					update_Query = "";
					update_Query = "UPDATE CUST_MASTER SET CM_USER = '"+ciUser+"' ,  "
							+ "  CM_TMPSTP = SYSDATE "
							+ " WHERE CM_RR_NO = '"+ciRrNo+"' " ;
					
					ps = dbConnection.prepareStatement(update_Query);
					int j = ps.executeUpdate();
					
					if(j > 0){
						JsonResponse.put("status","success");
						JsonResponse.put("message", "INTIMATIONS Updated Successfully.");
						
						dbConnection.commit();
					}else{
						JsonResponse.put("status","error");
						JsonResponse.put("message", "INTIMATIONS Not Updated.");
						
						dbConnection.rollback();
					}
				}else{
					JsonResponse.put("status","error");
					JsonResponse.put("message", "INTIMATIONS Not Updated.");
					dbConnection.rollback();
				}

			}else{
				JsonResponse.put("status", "error");
				JsonResponse.put("message", "No INTIMATIONS Record Found To Insert/Update.");
			}
		} catch(SQLException e){
			e.printStackTrace();
			System.out.println("Error Occured , Insert INTIMATIONS : "+e.getMessage());
			JsonResponse.put("status", "error");
			JsonResponse.put("message", "Error : INTIMATIONS Not Inserted/Updated.");
			
			try {
				dbConnection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		}
		catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("Error Occured , Insert INTIMATIONS : "+e.getMessage());
			JsonResponse.put("status", "error");
			JsonResponse.put("message", "Error : INTIMATIONS Not Inserted/Updated.");
			
			try {
				dbConnection.rollback();
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
		}finally {
			DBManagerResourceRelease.close(ps);
			
			try {
				dbConnection.setAutoCommit(true);
			} catch (SQLException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		return JsonResponse;
	}
	
	@Override
	public JSONObject getStationList(JSONObject object) {
		// TODO Auto-generated method stub
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = "";
		Map<String, String> station = null;
		JSONObject jsonResponse = new JSONObject();
		JSONArray jsonlist = new JSONArray();
		
		try {
			
			if(!object.isEmpty()){
				
				String LOCATION = (String)object.get("location");
				String CONN_TYPE = (String)object.get("conn_type");
				
				if(CONN_TYPE.equals("LT") || CONN_TYPE == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(CONN_TYPE.equals("HT") || CONN_TYPE == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				
				String qyr = " SELECT  SM_STN_CD,SM_STN_NAME FROM STATION_MASTER WHERE SM_LOC_CD LIKE '"+LOCATION+"%' ORDER BY 1  ";
				
				ps = dbConnection.prepareStatement(qyr);
				rs = ps.executeQuery();

				while (rs.next()) {
					station = new HashMap<String, String>();
					
					station.put("key", rs.getString("SM_STN_CD"));
					station.put("value", rs.getString("SM_STN_NAME") + " (" + rs.getString("SM_STN_CD")+")");

					jsonlist.add(station);
				}
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "No Station Detials");
			}
			
			
			if(!jsonlist.isEmpty()){
				jsonResponse.put("status", "success");
				jsonResponse.put("StationCodeList", jsonlist);
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "No Station Detials");
			}


		} catch (SQLException e) {
			System.out.println("Exception thrown " + e);
			jsonResponse.put("status", "error");
			jsonResponse.put("message", "Query Error . Station Detials .");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonResponse.put("status", "error");
			jsonResponse.put("message", " Database Error.  Station Detials . ");
		} finally {
			//DatabaseImpl.CleanUp(con, ps, rs);
			DBManagerResourceRelease.close(rs);
			DBManagerResourceRelease.close(ps);
		}
		
		return jsonResponse;
	}
	
	@Override
	public JSONObject getFeederList(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = "";
		Map<String, String> station = null;
		JSONObject jsonResponse = new JSONObject();
		JSONArray jsonlist = new JSONArray();
		
		try {
			
			if(!object.isEmpty()){
				
				String LOCATION = (String)object.get("location");
				String CONN_TYPE = (String)object.get("conn_type");
				String stationCode = (String)object.get("stationCode");
				
				if(CONN_TYPE.equals("LT") || CONN_TYPE == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(CONN_TYPE.equals("HT") || CONN_TYPE == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				
				String qyr = " SELECT  NFM_FDR_NO,NFM_FDR_DESCR FROM FEEDER_MASTER "
					+ " WHERE NFM_NST_STN_CD  like '"+stationCode+"%' AND  NFM_NST_LOC_CD LIKE '"+LOCATION+"%' ORDER BY 1  ";
				
				ps = dbConnection.prepareStatement(qyr);
				rs = ps.executeQuery();

				while (rs.next()) {
					station = new HashMap<String, String>();
					
					station.put("key", rs.getString("NFM_FDR_NO"));
					station.put("value", rs.getString("NFM_FDR_DESCR") + " (" + rs.getString("NFM_FDR_NO")+")");

					jsonlist.add(station);
				}
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "No Feeder Detials");
			}
			
			
			if(!jsonlist.isEmpty()){
				jsonResponse.put("status", "success");
				jsonResponse.put("FeederCodeList", jsonlist);
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "No Feeder Detials");
			}


		} catch (SQLException e) {
			System.out.println("Exception thrown " + e);
			jsonResponse.put("status", "error");
			jsonResponse.put("message", "Query Error . Feeder Detials .");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonResponse.put("status", "error");
			jsonResponse.put("message", " Database Error. Feeder Detials");
		} finally {
			//DatabaseImpl.CleanUp(con, ps, rs);
			DBManagerResourceRelease.close(rs);
			DBManagerResourceRelease.close(ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getTransformerList(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		String result = "";
		Map<String, String> station = null;
		JSONObject jsonResponse = new JSONObject();
		JSONArray jsonlist = new JSONArray();
		
		try {
			
			if(!object.isEmpty()){
				
				String LOCATION = (String)object.get("location");
				String CONN_TYPE = (String)object.get("conn_type");
				String stationCode = (String)object.get("stationCode");
				String feederCode = (String)object.get("feederCode");
				
				if(CONN_TYPE.equals("LT") || CONN_TYPE == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(CONN_TYPE.equals("HT") || CONN_TYPE == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				System.out.println(dbConnection);
				String qyr = " SELECT DISTINCT(TM_TRSFMR_CENTER_NO),TM_TRSFMR_CENTER_NAME "
					+ "  FROM TRSFMR_MASTR   WHERE "
					+ "  TM_NFM_NST_STN_CD = '"+stationCode+"'  AND "
							+ " TM_NFM_FDR_NO IN( '"+ feederCode + "')  AND "
									+ " TM_LOC_CD LIKE '"+LOCATION+"%' order by  1     ";
				
				ps = dbConnection.prepareStatement(qyr);
				rs = ps.executeQuery();

				//System.out.println("Rs : "+rs.getFetchSize());
				System.out.println(qyr);	
				while (rs.next()) {
					System.out.println("kcmkalsmcdlkmax");
					station = new HashMap<String, String>();
					
					station.put("key", rs.getString("TM_TRSFMR_CENTER_NO"));
					station.put("value", rs.getString("TM_TRSFMR_CENTER_NAME") + " (" + rs.getString("TM_TRSFMR_CENTER_NO")+")");

					jsonlist.add(station);
					
					System.out.println(station);
				}
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "No Transformer Detials");
			}
			
			
			if(!jsonlist.isEmpty()){
				jsonResponse.put("status", "success");
				jsonResponse.put("TransformerCodeList", jsonlist);
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "No Transformer Detials");
			}


		} catch (SQLException e) {
			System.out.println("Exception thrown " + e);
			jsonResponse.put("status", "error");
			jsonResponse.put("message", "Query Error .  Transformer Detials . ");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonResponse.put("status", "error");
			jsonResponse.put("message", " Database Error.  Transformer Detials . ");
		} finally {
			//DatabaseImpl.CleanUp(con, ps, rs);
			DBManagerResourceRelease.close(rs);
			DBManagerResourceRelease.close(ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getMeterReaderCodesByOMUnit(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		JSONObject jsonResponse = new JSONObject();
		JSONArray MeterCodeList = new JSONArray();
		
		try {
			
			if(!object.isEmpty()){
				
				String LOCATION  = (String)object.get("location");
				String CONN_TYPE = (String)object.get("conn_type");
				String om_code   = (String)object.get("om_code");
				
				if(CONN_TYPE.equals("LT") || CONN_TYPE == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(CONN_TYPE.equals("HT") || CONN_TYPE == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				/*jsonResponse.put("status", "success");
				jsonResponse.put("METERCODE", ReferenceUtil.getMeterCodeList(om_code, CONN_TYPE));*/
				
				if(dbConnection != null){
					ps = dbConnection.prepareStatement(" SELECT DISTINCT MRM_MTR_RDR_CD FROM MTR_RDR_MASTR " +
							  " WHERE MRM_MTR_RDR_STS ='Y' AND " +
							  " MRM_OM_UNIT_CD LIKE '" + om_code + "%' ORDER BY 1  ") ; 
					rs = ps.executeQuery();
					while (rs.next()) {
						JSONObject meterCode = new JSONObject();
						
						meterCode.put("key", rs.getString("MRM_MTR_RDR_CD"));
						meterCode.put("value", rs.getString("MRM_MTR_RDR_CD"));
						MeterCodeList.add(meterCode);
					}
				}
				
				jsonResponse.put("status", "success");
				jsonResponse.put("MeterReaderCode",MeterCodeList);
			
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Invalid Inputs / No Meter Reader Detials");
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown " + e);
			jsonResponse.put("status", "error");
			jsonResponse.put("message", "Database Query / Runtime Error .");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonResponse.put("status", "error");
			jsonResponse.put("message", " Database Query / Runtime Error.");
		} finally {
			//DatabaseImpl.CleanUp(con, ps, rs);
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	@Override
	public JSONObject getMeterReadingDayByMrCode(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs = null;
		Map<String, String> mtr_day = null;
		JSONObject jsonResponse = new JSONObject();
		JSONArray MeterDayList = new JSONArray();
		
		try {
			
			if(!object.isEmpty()){
				
				String LOCATION  = (String)object.get("location");
				String CONN_TYPE = (String)object.get("conn_type");
				String om_code   = (String)object.get("om_code");
				String mr_code   = (String)object.get("mr_code");
				
				if(CONN_TYPE.equals("LT") || CONN_TYPE == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(CONN_TYPE.equals("HT") || CONN_TYPE == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					String qyr = " select distinct mrm_mtr_rdg_day from mtr_rdr_mastr "
							   + " where mrm_mtr_rdr_cd like '%"+mr_code+"%' AND MRM_OM_UNIT_CD like '"+om_code+"%' order by 1  ";

				ps = dbConnection.prepareStatement(qyr);
				rs = ps.executeQuery();
				
				/*while(rs.next()){
					
					mtr_day = new HashMap<String, String>();
					
					mtr_day.put("MTR_DAY", rs.getString("mrm_mtr_rdg_day"));
					
					jsonArray.add(mtr_day);
				}*/
				while (rs.next()) {
					JSONObject meterCode = new JSONObject();
					
					meterCode.put("key", rs.getString("mrm_mtr_rdg_day"));
					meterCode.put("value", rs.getString("mrm_mtr_rdg_day"));
					MeterDayList.add(meterCode);
				}
				
				jsonResponse.put("status", "success");
				jsonResponse.put("MeterReadingDay", MeterDayList);
			
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Invalid Inputs / No Meter Reading Day Detials");
			}

		} catch (SQLException e) {
			System.out.println("Exception thrown " + e);
			jsonResponse.put("status", "error");
			jsonResponse.put("message", "Database Query / Runtime Error .");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			jsonResponse.put("status", "error");
			jsonResponse.put("message", " Database Query / Runtime Error.");
		} finally {
			//DatabaseImpl.CleanUp(con, ps, rs);
			DBManagerResourceRelease.close(rs, ps);
		}
		
		return jsonResponse;
	}

	
	@SuppressWarnings("rawtypes")
	@Override
	public JSONObject doAddOrUpdateConsumerMaster(JSONObject object) {
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean InsertUpdateStatus = false; 

		JSONObject jsonResponse = new JSONObject();
		
		try {
			System.out.println(object);
			if(!object.isEmpty() || !object.isNullObject()){
				
				
				String ConnType = (String) object.get("conn_type").toString().trim();
				String OPTION   = (String) object.get("option").toString().trim();
				String LOCATION   = (String) object.get("location_code").toString().trim();
				String LogIn_User   = (String) object.get("user").toString().trim();
				
				
				if(ConnType.equals("LT") || ConnType == "LT"){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(ConnType.equals("HT") || ConnType == "HT"){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				dbConnection.setAutoCommit(false);
				
				
				Iterator allkey = object.keys();
				System.out.println(allkey);
				while(allkey.hasNext()){
					Object Obj = allkey.next();
					
					String Keyname  = Obj.toString();
					System.out.println("Keyname : "+Keyname);
				    
					
					if(Keyname.equals("rrno_detail")){
						List<?> list = (List<?>)object.get(Keyname);
						System.out.println("rrno_detail : "+OPTION);
						
						if(InsertCustomerDetails(list,OPTION,ConnType,LOCATION)){
							InsertUpdateStatus = true;
						}else{
							InsertUpdateStatus = false;
						}
					}
					else if(Keyname.equals("rrno_deposits") && InsertUpdateStatus){
						List<?> list = (List<?>)object.get(Keyname);
						System.out.println("rrno_deposits : "+OPTION);
						
						if(InsertConsumerDeposits(list,OPTION,ConnType,LOCATION)){
							InsertUpdateStatus = true;
						}else{
							InsertUpdateStatus = false;
						}
					}
					else if(Keyname.equals("rrno_customerDescription") && InsertUpdateStatus){
						List<?> list = (List<?>)object.get(Keyname);
						System.out.println("rrno_customerDescription : "+OPTION);
						
						if(InsertConsumerDescriptions(list,OPTION,ConnType,LOCATION,LogIn_User)){
							InsertUpdateStatus = true;
						}else{
							InsertUpdateStatus = false;
						}
					}
					else if(Keyname.equals("rrno_customerDocuments") && InsertUpdateStatus){
						List<?> list = (List<?>)object.get(Keyname);
						System.out.println("rrno_customerDocuments : "+OPTION);
						
						if(InsertConsumerDocuments(list,OPTION,ConnType,LOCATION)){
							InsertUpdateStatus = true;
						}else{
							InsertUpdateStatus = false;
						}
					}
					else if(Keyname.equals("rrno_customerCTPTratio") && InsertUpdateStatus){
						List<?> list = (List<?>)object.get(Keyname);
						System.out.println("rrno_customerCTPTratio : "+OPTION);
						
						if(InsertConsumerCT_PT_Ratio(list,OPTION,ConnType,LOCATION)){
							InsertUpdateStatus = true;
						}else{
							InsertUpdateStatus = false;
						}
					}
					else if(Keyname.equals("rrno_customerIntimations") && InsertUpdateStatus){
						List<?> list = (List<?>)object.get(Keyname);
						System.out.println("rrno_customerIntimations : "+OPTION);
						
						if(InsertConsumerIntimations(list,OPTION,ConnType,LOCATION)){
							InsertUpdateStatus = true;
						}else{
							InsertUpdateStatus = false;
						}
					}
					else if(Keyname.equals("rrno_customerSpotFolio") && InsertUpdateStatus){
						List<?> list = (List<?>)object.get(Keyname);
						System.out.println("rrno_customerSpotFolio : "+OPTION);
						
						if(InsertConsumerSpotFolio(list,OPTION,ConnType,LOCATION)){
							InsertUpdateStatus = true;
						}else{
							InsertUpdateStatus = false;
						}
					}
				}
				
				if(InsertUpdateStatus){
					jsonResponse.put("status", "success");
					jsonResponse.put("message", "Records Inserted/Updated Successfully...!");
					
					dbConnection.commit();
					
					
				}else{
					
					dbConnection.rollback();
					
					jsonResponse.put("status", "error");
					jsonResponse.put("message", "Insertion/Updation Failure!");
				}
				
			}else{
				jsonResponse.put("status", "error");
				jsonResponse.put("message", "Inputs Are Invalid.Try Again.");
			}
				
		}catch(SQLException e){
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Database Connection Error . ");
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			Log.info("[" + this.getClass().getSimpleName() + "]-->" + "Exception Occured : " + e.getMessage());
			e.printStackTrace();
			
		}
		catch (JSONException e) {
			
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				e1.printStackTrace();
			}
			Log.info("[" + this.getClass().getSimpleName() + "]-->" + "Exception Occured : " + e.getMessage());
			e.printStackTrace();
		}catch (Exception e) {
			Log.info("["+this.getClass().getSimpleName()+"]-->"+"Exception Occured : "+e.getMessage());
			jsonResponse.put("status", "fail");
			jsonResponse.put("message", "Error Inserting/Updating Consumer Master Detsils.");
			// TODO: handle exception
			
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			
			
		}finally
		{
			try {
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			DBManagerResourceRelease.close(rs, ps);
		}
		
		System.out.println(jsonResponse);
		
		return jsonResponse;
		
		
		
	}

	private boolean InsertConsumerSpotFolio(List<?> list, String OPTION , String connType, String lOCATION) {
		// TODO Auto-generated method stub
		
		 /* '=====Begin============To Update the Spot Folio Details ====================*/               
		String strSF = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean Result = false;
		
		try {
			
			
			Iterator itr = list.iterator();
			while(itr.hasNext()){
				Object element = itr.next();
				Map object = (Map)element;
				
				String spRRNo = (String)object.get("spRRNo").toString().toUpperCase();
				String spFolioNo = (String)object.get("spFolioNo");
				String spMrRdgDay = (String)object.get("spMrRdgDay");
				String spMrCode = (String)object.get("spMrCode");
				String spTmpstp = (String)object.get("spTmpstp");
				String spUser = (String)object.get("spUser");
				
				spRRNo = lOCATION+spRRNo;
				
				spRRNo          = ReferenceUtil.ConvertIFNullToString(spRRNo);
				spFolioNo       = ReferenceUtil.ConvertIFNullToString(spFolioNo);
				spMrRdgDay = ReferenceUtil.ConvertIFNullToString(spMrRdgDay);
				spMrCode        = ReferenceUtil.ConvertIFNullToString(spMrCode);
				spTmpstp        = ReferenceUtil.ConvertIFNullToString(spTmpstp);
				spUser          = ReferenceUtil.ConvertIFNullToString(spUser);
				
				if(OPTION.equals("ADD")){

					strSF = "" ;
	                strSF = strSF + " INSERT INTO SPOT_FOLIO " ;
	                strSF = strSF + " (SF_MR_CODE,SF_MR_RDG_DAY,SF_FOLIO_NO,SF_RR_NO,SF_USER,SF_TMPSTP)" ;
	                strSF = strSF + "	VALUES " ;
	                strSF = strSF + " ('"+spMrCode+"', " ;
	                strSF = strSF + "  "+spMrRdgDay+" , " ;
	                strSF = strSF + "  '"+spFolioNo+"' ," ;
	                strSF = strSF + "  '"+spRRNo+"', '"+spUser+"' , SYSDATE  ) ";
			        
				}else {
					if(OPTION.equals("UPDATE")){
						
						strSF = "";
	                    strSF = strSF + " UPDATE SPOT_FOLIO SET ";
	                    //strSF = strSF + " SF_MR_CODE ='"+spMrCode+"' ,";
	                    //strSF = strSF + " SF_MR_RDG_DAY ="+spMrRdgDay+" ,";
	                    strSF = strSF + " SF_FOLIO_NO ='"+spFolioNo+"' , ";
	                    strSF = strSF + " SF_USER ='"+spUser+"' , ";
	                    strSF = strSF + " SF_TMPSTP = SYSDATE ";
	                    strSF = strSF + " WHERE SF_RR_NO ='"+spRRNo+"' ";
	                    
					}
				}
				System.out.println(strSF);
				ps = dbConnection.prepareStatement(strSF);
                int v = ps.executeUpdate();
                
                System.out.println(strSF);
                
                /*if(v > 0){
                	Result = true;
                }*/
                System.out.println("Row Updated...!"+Result);
			}	
			
			Result = true;
			
		} catch (JSONException e) {
			// TODO: handle exception
			Result = false;
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.info("[" + this.getClass().getSimpleName() + "]-->" + "Exception Occured : " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			Result = false;
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.info("["+this.getClass().getSimpleName()+"]-->"+"Exception Occured : "+e.getMessage());
			e.printStackTrace();
		}finally {
			DBManagerResourceRelease.close(rs);
			DBManagerResourceRelease.close(ps);
		}
      /*  '=====End============To Update the Spot Folio Details ====================*/	
		
		return Result;
	}

	private boolean InsertConsumerIntimations(List<?> list, String OPTION, String connType, String lOCATION) {
		// TODO Auto-generated method stub
		
		String strIntimationDel,strIntimationDesc,strIntimationValues = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		int  i = 0;
		boolean Result = false;
		
		try {
			
			
			Iterator itr = list.iterator();
			while(itr.hasNext()){
				Object element = itr.next();
				Map object = (Map)element;
				
				
				String ciRrNo = (String)object.get("ciRrNo").toString().toUpperCase();
				String ciIntmTyp = (String)object.get("ciIntmTyp");
				String codeDescription = (String)object.get("codeDescription");
				String ciIntmDt = (String)object.get("ciIntmDt");
				String ciLetrNo = (String)object.get("ciLetrNo");
				String ciUser = (String)object.get("ciUser");
				String ciTmpstp = (String)object.get("ciTmpstp");
				String ciRmks = (String)object.get("ciRmks");
				String rowID = (String)object.get("rowID");
				
				ciRrNo = lOCATION+ciRrNo;
				
				ciRrNo          = ReferenceUtil.ConvertIFNullToString(ciRrNo);
				ciIntmTyp       = ReferenceUtil.ConvertIFNullToString(ciIntmTyp);
				codeDescription = ReferenceUtil.ConvertIFNullToString(codeDescription);
				ciIntmDt        = ReferenceUtil.ConvertIFNullToString(ciIntmDt);
				ciLetrNo        = ReferenceUtil.ConvertIFNullToString(ciLetrNo);
				ciUser          = ReferenceUtil.ConvertIFNullToString(ciUser);
				ciTmpstp        = ReferenceUtil.ConvertIFNullToString(ciTmpstp);
				ciRmks          = ReferenceUtil.ConvertIFNullToString(ciRmks);
				rowID           = ReferenceUtil.ConvertIFNullToString(rowID);
				
				if(i == 0){
					strIntimationDel = "" ;
	                strIntimationDel = "DELETE FROM CUST_INTIMATIONS WHERE CI_RR_NO = '"+ciRrNo+"' " ;
	                ps = dbConnection.prepareStatement(strIntimationDel);
	                ps.executeQuery();
				}

	               /* '========Now add the records in the database one by one in the CONSUMER_INTIMATIONS  ==============*/
	                strIntimationDesc = "";
	                strIntimationDesc = strIntimationDesc + "INSERT INTO CUST_INTIMATIONS ";
	                strIntimationDesc = strIntimationDesc + "(CI_RR_NO,CI_INTM_DT,CI_INTM_TYP,CI_LETR_NO,CI_RMKS,CI_USER,CI_TMPSTP ) ";
	                strIntimationDesc = strIntimationDesc + " VALUES ";

							strIntimationValues = "" ;
		                    strIntimationValues = strIntimationDesc   + strIntimationValues + "('"+ciRrNo+"', " ;
		                    strIntimationValues = strIntimationValues + "  TO_DATE('"+ciIntmDt+"','dd/MM/yyyy'), " ;
		                    //strIntimationValues = strIntimationValues + " '"+ReferenceUtil.getNameToCodeCommon(codeDescription,"CONSMR_INT",connType)+"', " ;
		                    strIntimationValues = strIntimationValues + " '"+ciIntmTyp+"', " ;
		                    strIntimationValues = strIntimationValues + " '"+ciLetrNo+"','"+ciRmks+"', ";
		                    strIntimationValues = strIntimationValues + " '"+ciUser+"', TO_DATE(SYSDATE,'dd/mm/yyyy') )" ;
				
		                    ps = dbConnection.prepareStatement(strIntimationValues);
			                 int v = ps.executeUpdate();
			                 
			                 System.out.println(strIntimationValues);
			                 
			                 if(v > 0){
			                 	Result = true;
			                 }
			                 System.out.println("Row Updated...!"+Result);
		        
		        i++;
			}	
			
			Result = true;
			
			
		}catch (JSONException e) {
			// TODO: handle exception
			Result = false;
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.info("[" + this.getClass().getSimpleName() + "]-->" + "Exception Occured : " + e.getMessage());
			e.printStackTrace();
		}  catch (Exception e) {
			// TODO: handle exception
			Result = false;
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.info("["+this.getClass().getSimpleName()+"]-->"+"Exception Occured : "+e.getMessage());
			e.printStackTrace();
		}finally {
			DBManagerResourceRelease.close(rs);
			DBManagerResourceRelease.close(ps);
		}
		
		return Result;
	}

	private boolean InsertConsumerCT_PT_Ratio(List<?> list, String OPTION, String connType, String lOCATION) {
		// TODO Auto-generated method stub
		
		String strCTPTDel,strCTPTDesc,strCTPTValues = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		int  i = 0;
		boolean Result = false;
		
		try {
			
			
			Iterator itr = list.iterator();
			while(itr.hasNext()){
				Object element = itr.next();
				Map object = (Map)element;
				
				
				String cprRrNo = (String)object.get("cprRrNo").toString().toUpperCase();
				String cprFromDt = (String)object.get("cprFromDt");
				String cprToDt = (String)object.get("cprToDt");
				String cprCtRatioAvail = (String)object.get("cprCtRatioAvail");
				String cprPtRatioAvail = (String)object.get("cprPtRatioAvail");
				String cprCtRatioNum = (String)object.get("cprCtRatioNum");
				String cprCtRatioDen = (String)object.get("cprCtRatioDen");
				String cprPtRatioNum = (String)object.get("cprPtRatioNum");
				String cprPtRatioDen = (String)object.get("cprPtRatioDen");
				String cprMultConst = (String)object.get("cprMultConst");
				String cprRmks = (String)object.get("cprRmks");
				String cprUser = (String)object.get("cprUser");
				String cprTmpstp = (String)object.get("cprTmpstp");
				String rowID = (String)object.get("rowID");
				
				String fmDate	= (cprFromDt	=="") ? "" : cprFromDt;
				String toDate	= (cprToDt 	=="") ? "" : cprToDt;
				
				cprRrNo = lOCATION+cprRrNo;
				
				cprFromDt              = ReferenceUtil.ConvertIFNullToString(cprFromDt);
				cprToDt                = ReferenceUtil.ConvertIFNullToString(cprToDt);
				cprCtRatioAvail        = ReferenceUtil.ConvertIFNullToString(cprCtRatioAvail);
				cprPtRatioAvail        = ReferenceUtil.ConvertIFNullToString(cprPtRatioAvail);
				cprCtRatioNum          = ReferenceUtil.ConvertIFNullToString(cprCtRatioNum);
				cprCtRatioDen          = ReferenceUtil.ConvertIFNullToString(cprCtRatioDen);
				cprPtRatioNum          = ReferenceUtil.ConvertIFNullToString(cprPtRatioNum);
				cprPtRatioDen          = ReferenceUtil.ConvertIFNullToString(cprPtRatioDen);
				cprMultConst           = ReferenceUtil.ConvertIFNullToString(cprMultConst);
				cprRmks                = ReferenceUtil.ConvertIFNullToString(cprRmks);
				cprUser                = ReferenceUtil.ConvertIFNullToString(cprUser);
				cprTmpstp              = ReferenceUtil.ConvertIFNullToString(cprTmpstp);
				rowID                  = ReferenceUtil.ConvertIFNullToString(rowID);

				
				if(i ==0 ){
					strCTPTDel = "";
	                strCTPTDel = "DELETE FROM CT_PT_RATIO WHERE CPR_RR_NO = '"+cprRrNo+"' ";
	                ps = dbConnection.prepareStatement(strCTPTDel);
	                ps.executeQuery();
				}
                
                strCTPTDesc = "";
                strCTPTDesc = strCTPTDesc + "INSERT INTO CT_PT_RATIO ";
                strCTPTDesc = strCTPTDesc + "(CPR_RR_NO,CPR_FROM_DT,CPR_TO_DT,CPR_CT_RATIO_AVAIL,CPR_PT_RATIO_AVAIL,CPR_CT_RATIO_NUM, ";
                strCTPTDesc = strCTPTDesc + "CPR_CT_RATIO_DEN,CPR_PT_RATIO_NUM,CPR_PT_RATIO_DEN,CPR_MULT_CONST,CPR_RMKS,CPR_USER,CPR_TMPSTP)";
                strCTPTDesc = strCTPTDesc + " VALUES "  ;
						
						strCTPTValues = "" ;
		                strCTPTValues = strCTPTDesc   + strCTPTValues + "('"+cprRrNo+"', " ;
		                strCTPTValues = strCTPTValues + " TO_DATE('"+fmDate+"','dd/mm/yyyy') ,";
		                strCTPTValues = strCTPTValues + " TO_DATE('"+toDate+"','dd/mm/yyyy') ,";
		                strCTPTValues = strCTPTValues + " '"+cprCtRatioAvail+"', " ;
		                strCTPTValues = strCTPTValues + " '"+cprPtRatioAvail+"', ";
		                strCTPTValues = strCTPTValues + "  "+cprCtRatioNum+",";
		                strCTPTValues = strCTPTValues + "  "+cprCtRatioDen+",";
		                strCTPTValues = strCTPTValues + "  "+cprPtRatioNum+",";
		                strCTPTValues = strCTPTValues + "  "+cprPtRatioDen+",";
		                strCTPTValues = strCTPTValues + "  "+cprMultConst+",";
		                strCTPTValues = strCTPTValues + "  '"+cprRmks+"',";
		                strCTPTValues = strCTPTValues + "  '"+cprUser+"' ,TO_DATE(SYSDATE, 'dd/MM/yyyy') ) ";
		                
		                System.out.println(strCTPTValues);
				
		                ps = dbConnection.prepareStatement(strCTPTValues);
		                 int v = ps.executeUpdate();
		                 
		                 if(v > 0){
		                 	Result = true;
		                 }
		                 System.out.println("Row Updated...!"+Result);
		        
		        i++;
		        
			}	
			
			Result = true;
			
		} catch (JSONException e) {
			// TODO: handle exception
			Result = false;
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.info("[" + this.getClass().getSimpleName() + "]-->" + "Exception Occured : " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			Result = false;
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.info("["+this.getClass().getSimpleName()+"]-->"+"Exception Occured : "+e.getMessage());
			e.printStackTrace();
		}finally {
			DBManagerResourceRelease.close(rs);
			DBManagerResourceRelease.close(ps);
		}
		
		return Result;
		
	}

	private boolean InsertConsumerDocuments(List<?> list, String OPTION,	String connType, String lOCATION) {
		// TODO Auto-generated method stub
		
		String strDocumentsDel,strDocumentsDesc,strDocumentsValues = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		int  i = 0;
		boolean Result = false;
		
		try {
			
			
			Iterator itr = list.iterator();
			while(itr.hasNext()){
				Object element = itr.next();
				Map object = (Map)element;
				
				
				
				String cdocRrno = (String)object.get("cdocRrno").toString().toUpperCase();
				String cdocDocSubmDt = (String)object.get("cdocDocSubmDt");
				String cdocUser = (String)object.get("cdocUser");
				String cdocTmpstp = (String)object.get("cdocTmpstp");
				String cdocDocTyp = (String)object.get("cdocDocTyp");
				String cdocSlNo = (String)object.get("cdocSlNo");
				String docDescription = (String)object.get("docDescription");
				String rowID = (String)object.get("rowID");
				
				cdocRrno = lOCATION+cdocRrno;

				cdocDocSubmDt              = ReferenceUtil.ConvertIFNullToString(cdocDocSubmDt);
				cdocUser              = ReferenceUtil.ConvertIFNullToString(cdocUser);
				cdocTmpstp              = ReferenceUtil.ConvertIFNullToString(cdocTmpstp);
				cdocDocTyp              = ReferenceUtil.ConvertIFNullToString(cdocDocTyp);
				cdocSlNo              = ReferenceUtil.ConvertIFNullToString(cdocSlNo);
				docDescription              = ReferenceUtil.ConvertIFNullToString(docDescription);
				rowID              = ReferenceUtil.ConvertIFNullToString(rowID);
				
				
				
				if(i == 0){
					strDocumentsDel = "" ;
	                strDocumentsDel = "DELETE FROM CUST_DOCUMENTS WHERE CDOC_RR_NO = '"+cdocRrno+"' ";
	                ps = dbConnection.prepareStatement(strDocumentsDel);
	                ps.executeQuery();
				}
				    
	                
	               /* 'Now insert all the records for this RRNo for the Documents*/
	                strDocumentsDesc = "";
	                strDocumentsDesc = strDocumentsDesc + "INSERT INTO CUST_DOCUMENTS ";
	                strDocumentsDesc = strDocumentsDesc + "(CDOC_RR_NO,CDOC_SL_NO,CDOC_DOC_TYP,CDOC_DOC_SUBM_DT,CDOC_USER,CDOC_TMPSTP) ";
	                strDocumentsDesc = strDocumentsDesc + " VALUES ";

	                strDocumentsValues = "";
	                strDocumentsValues = strDocumentsDesc + strDocumentsValues + "('" + cdocRrno + "', ";
	                strDocumentsValues = strDocumentsValues + " '" + (i) + "' , '"
						/*+ ReferenceUtil.getNameToCodeCommon(docDescription, "DOC_TYP", connType) + "', ";*/
	                		+ ReferenceUtil.getMaxSLNO(cdocRrno, "CUST_DOCUMENTS", "CDOC_SL_NO", connType,"CDOC_RR_NO") + "', ";
	                strDocumentsValues = strDocumentsValues + "  TO_DATE('" + cdocDocSubmDt + "','dd/MM/yyyy'), ";
	                strDocumentsValues = strDocumentsValues + "  '" + cdocUser
						+ "', TO_DATE(SYSDATE, 'dd/MM/yyyy') )";          
		                        
				
	                System.out.println(strDocumentsValues);
	                
	                ps = dbConnection.prepareStatement(strDocumentsValues);
	                 int v = ps.executeUpdate();
	                 
	                 if(v > 0){
	                 	Result = true;
	                 }
	                 System.out.println("Row Updated...!"+Result);
		        i++;
			}	
			
			Result = true;
			
		}catch (JSONException e) {
			// TODO: handle exception
			Result = false;
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.info("[" + this.getClass().getSimpleName() + "]-->" + "Exception Occured : " + e.getMessage());
			e.printStackTrace();
		}  catch (Exception e) {
			// TODO: handle exception
			Result = false;
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.info("["+this.getClass().getSimpleName()+"]-->"+"Exception Occured : "+e.getMessage());
			e.printStackTrace();
		}finally {
			DBManagerResourceRelease.close(rs);
			DBManagerResourceRelease.close(ps);
		}
		
		return Result;
	}

	private boolean InsertConsumerDescriptions(List<?> list,String OPTION, String connType, String lOCATION, String logIn_User) {
		// TODO Auto-generated method stub
		 /*  =====Begin============To insert into the Consumer Address Details ====================*/
		String strCD = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean Result = false;
		
		try {
			
			
			Iterator itr = list.iterator();
			while(itr.hasNext()){
				Object element = itr.next();
				Map object = (Map)element;
				
				
				String cdRrNo = (String)object.get("cdRrNo").toString().toUpperCase();
				String cdLangCd = (String)object.get("cdLangCd");
				String cdConsmrName = (String)object.get("cdConsmrName");
				String cdPremiseAddr1 = (String)object.get("cdPremiseAddr1");
				String cdPremiseAddr2 = (String)object.get("cdPremiseAddr2");
				String cdPremiseAddr3 = (String)object.get("cdPremiseAddr3");
				String cdPremiseAddr4 = (String)object.get("cdPremiseAddr4");
				String cdCorresAddr1 = (String)object.get("cdCorresAddr1");
				String cdCorresAddr2 = (String)object.get("cdCorresAddr2");
				String cdCorresAddr3 = (String)object.get("cdCorresAddr3");
				String cdCorresAddr4 = (String)object.get("cdCorresAddr4");
				String cdPlaceName = (String)object.get("cdPlaceName");
				String cdUsrid = (String)object.get("cdUsrid");
				String cdTmpstp = (String)object.get("cdTmpstp");
				String CD_VERSION = (String)object.get("CD_VERSION");
				
				cdRrNo = lOCATION+cdRrNo;
				
				cdLangCd              = ReferenceUtil.ConvertIFNullToString(cdLangCd);
				cdConsmrName              = ReferenceUtil.ConvertIFNullToString(cdConsmrName);
				cdPremiseAddr1              = ReferenceUtil.ConvertIFNullToString(cdPremiseAddr1);
				cdPremiseAddr2              = ReferenceUtil.ConvertIFNullToString(cdPremiseAddr2);
				cdPremiseAddr3              = ReferenceUtil.ConvertIFNullToString(cdPremiseAddr3);
				cdPremiseAddr4              = ReferenceUtil.ConvertIFNullToString(cdPremiseAddr4);
				cdCorresAddr1              = ReferenceUtil.ConvertIFNullToString(cdCorresAddr1);
				cdCorresAddr2              = ReferenceUtil.ConvertIFNullToString(cdCorresAddr2);
				cdCorresAddr3              = ReferenceUtil.ConvertIFNullToString(cdCorresAddr3);
				cdCorresAddr4              = ReferenceUtil.ConvertIFNullToString(cdCorresAddr4);
				cdPlaceName              = ReferenceUtil.ConvertIFNullToString(cdPlaceName);
				cdUsrid              = ReferenceUtil.ConvertIFNullToString(cdUsrid);
				cdTmpstp              = ReferenceUtil.ConvertIFNullToString(cdTmpstp);
				CD_VERSION              = ReferenceUtil.ConvertIFNullToString(CD_VERSION);
				
				
				if(OPTION.equals("ADD")){

					strCD = "";
			        strCD = strCD + " INSERT INTO CUST_DESCR " ;
			        strCD = strCD + " (CD_RR_NO,CD_LANG_CD,CD_CONSMR_NAME,CD_PREMISE_ADDR1,CD_PREMISE_ADDR2,";
			        strCD = strCD + " CD_PREMISE_ADDR3,CD_PREMISE_ADDR4,CD_CORRES_ADDR1,CD_CORRES_ADDR2,";
			        strCD = strCD + " CD_CORRES_ADDR3,CD_CORRES_ADDR4,CD_PLACE_NAME,CD_USRID,CD_TMPSTP) ";
			        strCD = strCD + " VALUES " ;
			        strCD = strCD + " ('"+cdRrNo+ "', '"+cdLangCd+"', '"+cdConsmrName+ "', '"+cdPremiseAddr1+ "', '"+cdPremiseAddr2+"', ";
			        strCD = strCD + "  '"+cdPremiseAddr3+"','"+cdPremiseAddr4+"','"+cdCorresAddr1+"','"+cdCorresAddr2+"'," ;
			        strCD = strCD + "  '"+cdCorresAddr3+"', '"+cdCorresAddr4+"','"+cdPlaceName+"','"+logIn_User+"',SYSDATE )" ;
			        
				}else {
					if(OPTION.equals("UPDATE")){
						
						strCD = "" ;
	                    strCD = strCD + " UPDATE CUST_DESCR SET ";
	                    strCD = strCD + " CD_CONSMR_NAME ='"+cdConsmrName+"' , ";
	                    strCD = strCD + " CD_PREMISE_ADDR1 ='"+cdPremiseAddr1+"' , ";
	                    strCD = strCD + " CD_PREMISE_ADDR2 ='"+cdPremiseAddr2+"' , ";
	                    strCD = strCD + " CD_PREMISE_ADDR3 ='"+cdPremiseAddr3+"' , ";
	                    strCD = strCD + " CD_PREMISE_ADDR4 ='"+cdPremiseAddr4+"' , ";
	                    strCD = strCD + " CD_CORRES_ADDR1 ='"+cdCorresAddr1+"' , ";
	                    strCD = strCD + " CD_CORRES_ADDR2 ='"+cdCorresAddr2+"' , ";
	                    strCD = strCD + " CD_CORRES_ADDR3 ='"+cdCorresAddr3+"' , ";
	                    strCD = strCD + " CD_CORRES_ADDR4 ='"+cdCorresAddr4+"' , ";
	                    strCD = strCD + " CD_PLACE_NAME ='"+cdPlaceName+"' , ";
	                   /* strCD = strCD + " CD_USRID ='"+cdUsrid+"' , ";*/
	                    strCD = strCD + " CD_USRID ='"+logIn_User+"' , ";
	                    strCD = strCD + " CD_TMPSTP =TO_DATE(SYSDATE ,'dd/mm/yyyy')  ";
	                    strCD = strCD + " WHERE CD_RR_NO ='"+cdRrNo+"' ";
	                    
					}
				}
				
				System.out.println(strCD);
				
				
				 ps = dbConnection.prepareStatement(strCD);
                 int v = ps.executeUpdate();
                 
                 if(v > 0){
                 	Result = true;
                 }
                 System.out.println("Row Updated...!"+Result);
			}	
			
			Result = true;
			
		} catch (JSONException e) {
			// TODO: handle exception
			Result = false;
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.info("[" + this.getClass().getSimpleName() + "]-->" + "Exception Occured : " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			Result = false;
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.info("["+this.getClass().getSimpleName()+"]-->"+"Exception Occured : "+e.getMessage());
			e.printStackTrace();
		}finally {
			DBManagerResourceRelease.close(rs);
			DBManagerResourceRelease.close(ps);
		}
			
        
      /*   =====End============To insert into the Consumer Address Details  ====================*/
		return Result;
	}

	private boolean InsertConsumerDeposits(List<?> list, String OPTION ,String connType, String lOCATION) {
		// TODO Auto-generated method stub
		
		String strDepositDel,strDepositDesc,strDepositValues = "";
		PreparedStatement ps = null;
		ResultSet rs = null;
		int  i = 0;
		boolean Result = false;
		
		try {
			
			
			Iterator itr = list.iterator();
			while(itr.hasNext()){
				Object element = itr.next();
				Map object = (Map)element;
				
				String cdpRRNO = (String)object.get("cdpRRNO").toString().toUpperCase();
				String cdpPymntPurpose = (String)object.get("cdpPymntPurpose");
				String cdpAmtPaid = (String)object.get("cdpAmtPaid");
				String cdpBillNo = (String)object.get("cdpBillNo");
				String cdpBillDt = (String)object.get("cdpBillDt");
				String cdpValidToDt = (String)object.get("cdpValidToDt");
				String cdpDrAcctCd = (String)object.get("cdpDrAcctCd");
				String cdpCrAcctCd = (String)object.get("cdpCrAcctCd");
				String cdpUser = (String)object.get("cdpUser");
				String cdpTmpstp = (String)object.get("cdpTmpstp");
				String cdpRemarks = (String)object.get("cdpRemarks");
				String cdpAdjSts = (String)object.get("cdpAdjSts");
				String cdpRcptNo = (String)object.get("cdpRcptNo");
				String cdpRcptDt = (String)object.get("cdpRcptDt");
				String cdpCashCountr = (String)object.get("cdpCashCountr");
				String cashCounterDescr = (String)object.get("cashCounterDescr");
				String depositDescription = (String)object.get("depositDescription");
				String rowID = (String)object.get("rowID");
				String cdpAmtPaids = (String)object.get("cdpAmtPaids");
				String msdTotal = (String)object.get("msdTotal");
				String mmd3Total = (String)object.get("mmd3Total");
				
				cdpRRNO = lOCATION+cdpRRNO;
				
				cdpPymntPurpose              = ReferenceUtil.ConvertIFNullToString(cdpPymntPurpose);
				cdpAmtPaid              = ReferenceUtil.ConvertIFNullToString(cdpAmtPaid);
				cdpBillNo              = ReferenceUtil.ConvertIFNullToString(cdpBillNo);
				cdpBillDt              = ReferenceUtil.ConvertIFNullToString(cdpBillDt);
				cdpValidToDt              = ReferenceUtil.ConvertIFNullToString(cdpValidToDt);
				cdpDrAcctCd              = ReferenceUtil.ConvertIFNullToString(cdpDrAcctCd);
				cdpCrAcctCd              = ReferenceUtil.ConvertIFNullToString(cdpCrAcctCd);
				cdpUser              = ReferenceUtil.ConvertIFNullToString(cdpUser);
				cdpTmpstp              = ReferenceUtil.ConvertIFNullToString(cdpTmpstp);
				cdpRemarks              = ReferenceUtil.ConvertIFNullToString(cdpRemarks);
				cdpAdjSts              = ReferenceUtil.ConvertIFNullToString(cdpAdjSts);
				cdpRcptNo              = ReferenceUtil.ConvertIFNullToString(cdpRcptNo);
				cdpRcptDt              = ReferenceUtil.ConvertIFNullToString(cdpRcptDt);
				cdpCashCountr              = ReferenceUtil.ConvertIFNullToString(cdpCashCountr);
				cashCounterDescr              = ReferenceUtil.ConvertIFNullToString(cashCounterDescr);
				depositDescription              = ReferenceUtil.ConvertIFNullToString(depositDescription);
				cdpAmtPaids              = ReferenceUtil.ConvertIFNullToString(cdpAmtPaids);
				msdTotal              = ReferenceUtil.ConvertIFNullToString(msdTotal);
				mmd3Total              = ReferenceUtil.ConvertIFNullToString(mmd3Total);
				rowID              = ReferenceUtil.ConvertIFNullToString(rowID);
				
				if(i == 0){
					strDepositDel = "";
	                strDepositDel = "DELETE FROM CUST_DEPOSITS WHERE CDP_RR_NO = '"+cdpRRNO+"' ";
	                ps = dbConnection.prepareStatement(strDepositDel);
	                ps.executeQuery();
				}

	               /* '========Now add the records in the database one by one in the CONSUMER_INTIMATIONS  ==============*/
				strDepositDesc = "";
                strDepositDesc = strDepositDesc + "INSERT INTO CUST_DEPOSITS ";
                strDepositDesc = strDepositDesc + "(CDP_RR_NO, CDP_RCPT_NO, CDP_RCPT_DT, CDP_CASH_COUNTR, CDP_PYMNT_PURPOSE,  ";
                strDepositDesc = strDepositDesc + "CDP_AMT_PAID, CDP_DR_ACCT_CD, ";
                strDepositDesc = strDepositDesc + "CDP_CR_ACCT_CD, CDP_USER, CDP_TMPSTP,CDP_REMARKS,CDP_ADJ_STS) ";
                strDepositDesc = strDepositDesc + " VALUES "  ;
                
						strDepositValues = "";
	                    strDepositValues = strDepositDesc + strDepositValues + "('"+cdpRRNO+"', " ;
	                    strDepositValues = strDepositValues + " '"+cdpRcptNo+"', ";
	                    strDepositValues = strDepositValues + "  TO_DATE('"+cdpRcptDt+"','dd/MM/yyyy'), ";
	                    strDepositValues = strDepositValues + " '"+cdpCashCountr+"', ";
	                    strDepositValues = strDepositValues + " '"+ReferenceUtil.getNameToCodeCommon(depositDescription, "PYMNT_PURP",connType)+ "', ";
	                    strDepositValues = strDepositValues + " '"+cdpAmtPaid+"', ";
	                    strDepositValues = strDepositValues + " '"+cdpDrAcctCd+"', ";
	                    strDepositValues = strDepositValues + " '"+cdpCrAcctCd+"', ";
	
	                    strDepositValues = strDepositValues + " '"+cdpUser+"', TO_DATE(SYSDATE,'dd/mm/yyyy'),";
	                    strDepositValues = strDepositValues + " '"+cdpRemarks+"',";
	                    strDepositValues = strDepositValues + " '"+cdpAdjSts+ "') ";
	                    
		        /*ps=dbConnection.prepareStatement(strDepositValues);
		        ps.executeQuery();*/
	                    
	                    System.out.println("Deposit : "+strDepositValues);
	                    
	                    ps = dbConnection.prepareStatement(strDepositValues);
	                    int v = ps.executeUpdate();
	                    
	                    if(v > 0){
	                    	Result = true;
	                    }
	                    System.out.println("Row Updated...!"+Result);
	                	
		        
		        i++;
			}	
			
			
			Result = true;
			
			
		} catch (JSONException e) {
			// TODO: handle exception
			Result = false;
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.info("[" + this.getClass().getSimpleName() + "]-->" + "Exception Occured : " + e.getMessage());
			e.printStackTrace();
		} catch (Exception e) {
			// TODO: handle exception
			Result = false;
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.info("["+this.getClass().getSimpleName()+"]-->"+"Exception Occured : "+e.getMessage());
			e.printStackTrace();
		}finally {
			DBManagerResourceRelease.close(rs);
			DBManagerResourceRelease.close(ps);
		}
		
		return Result;
	}

	private boolean InsertCustomerDetails(List<?> list, String OPTION, String ConnType, String lOCATION) {
		// TODO Auto-generated method stub
		
		String tempDuration=null,beginningMonth1=null;
		
		PreparedStatement ps = null;
		ResultSet rs = null;
		boolean Result = false;
		
		try {
			
		Iterator itr = list.iterator();
		while(itr.hasNext()){
			Object element = itr.next();
			Map object = (Map)element;
			
			System.out.println("cmRrNo : "+object.get("cmRrNo"));
			System.out.println("cmServiceDt : "+object.get("cmServiceDt"));
			
			  //String ConnType = (String)object.get("ConnType");
			
			  String location  =(String)object.get("location");
			  location              = ReferenceUtil.ConvertIFNullToString(location);
			  String cmRrNo  = (String)object.get("cmRrNo").toString().trim().toUpperCase();
			  cmRrNo              = ReferenceUtil.ConvertIFNullToString(cmRrNo);
			  cmRrNo = lOCATION+cmRrNo;
			  String cmServiceDt  =(String)object.get("cmServiceDt");
			  cmServiceDt              = ReferenceUtil.ConvertIFNullToString(cmServiceDt);
			  String cmOldRrNo  = (String)object.get("cmOldRrNo").toString().trim().toUpperCase();
			  cmOldRrNo              = ReferenceUtil.ConvertIFNullToString(cmOldRrNo);
			  cmOldRrNo = lOCATION+cmOldRrNo;
			  String cmKebOfficeFlg  =(String)object.get("cmKebOfficeFlg");
			  cmKebOfficeFlg              = ReferenceUtil.ConvertIFNullToString(cmKebOfficeFlg);
			  String cmPinCd  =(String)object.get("cmPinCd");
			  cmPinCd              = ReferenceUtil.ConvertIFNullToString(cmPinCd);
			  String cmTelephoneNo  =(String)object.get("cmTelephoneNo");
			  cmTelephoneNo              = ReferenceUtil.ConvertIFNullToString(cmTelephoneNo);
			  String cmTalukCd  =(String)object.get("cmTalukCd");
			  cmTalukCd              = ReferenceUtil.ConvertIFNullToString(cmTalukCd);
			  String cmTalukDescription  =(String)object.get("cmTalukDescription");
			  cmTalukDescription              = ReferenceUtil.ConvertIFNullToString(cmTalukDescription);
			  String cmTariffcatogory  =(String)object.get("cmTariffcatogory");
			  cmTariffcatogory              = ReferenceUtil.ConvertIFNullToString(cmTariffcatogory);
			  String cmDistrictCd  =(String)object.get("cmDistrictCd");
			  cmDistrictCd              = ReferenceUtil.ConvertIFNullToString(cmDistrictCd);
			  String cmDistrictDescription  =(String)object.get("cmDistrictDescription");
			  cmDistrictDescription              = ReferenceUtil.ConvertIFNullToString(cmDistrictDescription);
			  String cmLoanAmount  =(String)object.get("cmLoanAmount");
			  cmLoanAmount              = ReferenceUtil.ConvertIFNullToString(cmLoanAmount);
			  String cmStateConstituencyCd  =(String)object.get("cmStateConstituencyCd");
			  cmStateConstituencyCd              = ReferenceUtil.ConvertIFNullToString(cmStateConstituencyCd);
			  String cmStateConstituencyDescription  =(String)object.get("cmStateConstituencyDescription");
			  cmStateConstituencyDescription              = ReferenceUtil.ConvertIFNullToString(cmStateConstituencyDescription);
			  String cmRenewdate  =(String)object.get("cmRenewdate");
			  cmRenewdate              = ReferenceUtil.ConvertIFNullToString(cmRenewdate);
			  String cmRenewdateExtends  =(String)object.get("cmRenewdateExtends");
			  cmRenewdateExtends              = ReferenceUtil.ConvertIFNullToString(cmRenewdateExtends);
			  String cmCentralConstituencyCd  =(String)object.get("cmCentralConstituencyCd");
			  cmCentralConstituencyCd              = ReferenceUtil.ConvertIFNullToString(cmCentralConstituencyCd);
			  String cmCentralConstituencyDescription  =(String)object.get("cmCentralConstituencyDescription");
			  cmCentralConstituencyDescription              = ReferenceUtil.ConvertIFNullToString(cmCentralConstituencyDescription);
			  String cmTodMeterFlag  =(String)object.get("cmTodMeterFlag");
			  cmTodMeterFlag              = ReferenceUtil.ConvertIFNullToString(cmTodMeterFlag);
			  String cmRegionCd  =(String)object.get("cmRegionCd");
			  cmRegionCd              = ReferenceUtil.ConvertIFNullToString(cmRegionCd);
			  String cmRegionDescription  =(String)object.get("cmRegionDescription");
			  cmRegionDescription              = ReferenceUtil.ConvertIFNullToString(cmRegionDescription);
			  String cmPwrPurpose  =(String)object.get("cmPwrPurpose");
			  cmPwrPurpose              = ReferenceUtil.ConvertIFNullToString(cmPwrPurpose);
			  String cmIndustryCd  =(String)object.get("cmIndustryCd");
			  cmIndustryCd              = ReferenceUtil.ConvertIFNullToString(cmIndustryCd);
			  String cmIndustryDescription  =(String)object.get("cmIndustryDescription");
			  cmIndustryDescription              = ReferenceUtil.ConvertIFNullToString(cmIndustryDescription);
			  String cmApplntTyp  =(String)object.get("cmApplntTyp");
			  cmApplntTyp              = ReferenceUtil.ConvertIFNullToString(cmApplntTyp);
			  String cmApplntTypDescription  =(String)object.get("cmApplntTypDescription");
			  cmApplntTypDescription              = ReferenceUtil.ConvertIFNullToString(cmApplntTypDescription);
			  String cmPowerSanctNo  =(String)object.get("cmPowerSanctNo");
			  cmPowerSanctNo              = ReferenceUtil.ConvertIFNullToString(cmPowerSanctNo);
			  String cmPowerSanctDt  =(String)object.get("cmPowerSanctDt");
			  cmPowerSanctDt              = ReferenceUtil.ConvertIFNullToString(cmPowerSanctDt);
			  String cmPowerSanctAuth  =(String)object.get("cmPowerSanctAuth");
			  cmPowerSanctAuth              = ReferenceUtil.ConvertIFNullToString(cmPowerSanctAuth);
			  String cmNxtRrNo  =(String)object.get("cmNxtRrNo");
			  cmNxtRrNo              = ReferenceUtil.ConvertIFNullToString(cmNxtRrNo);
			  cmNxtRrNo = lOCATION+cmNxtRrNo;
			  String cmLdgrNo  =(String)object.get("cmLdgrNo");
			  cmLdgrNo              = ReferenceUtil.ConvertIFNullToString(cmLdgrNo);
			  String cmLdgrOpenedDt  =(String)object.get("cmLdgrOpenedDt");
			  cmLdgrOpenedDt              = ReferenceUtil.ConvertIFNullToString(cmLdgrOpenedDt);
			  String cmFolioNo  =(String)object.get("cmFolioNo");
			  cmFolioNo              = ReferenceUtil.ConvertIFNullToString(cmFolioNo);
			  String cmMtrRdgCycle  =(String)object.get("cmMtrRdgCycle");
			  cmMtrRdgCycle              = ReferenceUtil.ConvertIFNullToString(cmMtrRdgCycle);
			  String cmMtrRdgDay  =  (String)object.get("cmMtrRdgDay");
			  cmMtrRdgDay              = ReferenceUtil.ConvertIFNullToString(cmMtrRdgDay);
			  String cmNewMtrRdgDay  =(String)object.get("cmNewMtrRdgDay");
			  cmNewMtrRdgDay              = ReferenceUtil.ConvertIFNullToString(cmNewMtrRdgDay);
			  String cmMtrRdrCd  =(String)object.get("cmMtrRdrCd");
			  cmMtrRdrCd              = ReferenceUtil.ConvertIFNullToString(cmMtrRdrCd);
			  String cmNewMtrRdrCd  =(String)object.get("cmNewMtrRdrCd");
			  cmNewMtrRdrCd              = ReferenceUtil.ConvertIFNullToString(cmNewMtrRdrCd);
			  String cmMtrRdrDescription  =(String)object.get("cmMtrRdrDescription");
			  cmMtrRdrDescription              = ReferenceUtil.ConvertIFNullToString(cmMtrRdrDescription);
			  String cmStationNo  =(String)object.get("cmStationNo");
			  cmStationNo              = ReferenceUtil.ConvertIFNullToString(cmStationNo);
			  String cmStationName  =(String)object.get("cmStationName");
			  cmStationName              = ReferenceUtil.ConvertIFNullToString(cmStationName);
			  String cmFdrNo  =(String)object.get("cmFdrNo");
			  cmFdrNo              = ReferenceUtil.ConvertIFNullToString(cmFdrNo);
			  String cmFdrName  =(String)object.get("cmFdrName");
			  cmFdrName              = ReferenceUtil.ConvertIFNullToString(cmFdrName);
			  String cmLineMin  =(String)object.get("cmLineMin");
			  cmLineMin              = ReferenceUtil.ConvertIFNullToString(cmLineMin);
			  String cmOmUnitCd  =(String)object.get("cmOmUnitCd");
			  cmOmUnitCd              = ReferenceUtil.ConvertIFNullToString(cmOmUnitCd);
			  String cmOmUnitCdName  =(String)object.get("cmOmUnitCdName");
			  cmOmUnitCdName              = ReferenceUtil.ConvertIFNullToString(cmOmUnitCdName);
			  String cmTrsfmrNo  =(String)object.get("cmTrsfmrNo");
			  cmTrsfmrNo              = ReferenceUtil.ConvertIFNullToString(cmTrsfmrNo);
			  String cmTrsfmrName  =(String)object.get("cmTrsfmrName");
			  cmTrsfmrName              = ReferenceUtil.ConvertIFNullToString(cmTrsfmrName);
			  String cmAvgConsmp  =(String)object.get("cmAvgConsmp");
			  cmAvgConsmp              = ReferenceUtil.ConvertIFNullToString(cmAvgConsmp);
			  String cmFlConsumer  =(String)object.get("cmFlConsumer");
			  cmFlConsumer              = ReferenceUtil.ConvertIFNullToString(cmFlConsumer);
			  String cmPwrCutExemptFlg  =(String)object.get("cmPwrCutExemptFlg");
			  cmPwrCutExemptFlg              = ReferenceUtil.ConvertIFNullToString(cmPwrCutExemptFlg);
			  String cmConsmrSts  =(String)object.get("cmConsmrSts");
			  cmConsmrSts              = ReferenceUtil.ConvertIFNullToString(cmConsmrSts);
			  String cmConsmrStsDescription  =(String)object.get("cmConsmrStsDescription");
			  cmConsmrStsDescription              = ReferenceUtil.ConvertIFNullToString(cmConsmrStsDescription);
			  String cmChqBounceFlg  =(String)object.get("cmChqBounceFlg");
			  cmChqBounceFlg              = ReferenceUtil.ConvertIFNullToString(cmChqBounceFlg);
			  String cmDlCount  =(String)object.get("cmDlCount");
			  cmDlCount              = ReferenceUtil.ConvertIFNullToString(cmDlCount);
			  String cmDlSent  =(String)object.get("cmDlSent");
			  cmDlSent              = ReferenceUtil.ConvertIFNullToString(cmDlSent);
			  String cmTotMmd  =(String)object.get("cmTotMmd");
			  cmTotMmd              = ReferenceUtil.ConvertIFNullToString(cmTotMmd);
			  String cmLastMmdDt  =(String)object.get("cmLastMmdDt");
			  cmLastMmdDt              = ReferenceUtil.ConvertIFNullToString(cmLastMmdDt);
			  String cm100EnergyEntlmnt  =(String)object.get("cm100EnergyEntlmnt");
			  cm100EnergyEntlmnt              = ReferenceUtil.ConvertIFNullToString(cm100EnergyEntlmnt);
			  String cmTmpFolioNo  =(String)object.get("cmTmpFolioNo");
			  cmTmpFolioNo              = ReferenceUtil.ConvertIFNullToString(cmTmpFolioNo);
			  String cm100DmdEntlmnt  =(String)object.get("cm100DmdEntlmnt");
			  cm100DmdEntlmnt              = ReferenceUtil.ConvertIFNullToString(cm100DmdEntlmnt);
			  String cmTrfCatg  =(String)object.get("cmTrfCatg");
			  cmTrfCatg              = ReferenceUtil.ConvertIFNullToString(cmTrfCatg);
			  String cmTrfCatgDescription  =(String)object.get("cmTrfCatgDescription");
			  cmTrfCatgDescription              = ReferenceUtil.ConvertIFNullToString(cmTrfCatgDescription);
			  String cmTrfEffectFrmDt  =(String)object.get("cmTrfEffectFrmDt");
			  cmTrfEffectFrmDt              = ReferenceUtil.ConvertIFNullToString(cmTrfEffectFrmDt);
			  String cmLdEffectFrmDt  =(String)object.get("cmLdEffectFrmDt");
			  cmLdEffectFrmDt              = ReferenceUtil.ConvertIFNullToString(cmLdEffectFrmDt);
			  String cmLdSanctKw  =(String)object.get("cmLdSanctKw");
			  cmLdSanctKw              = ReferenceUtil.ConvertIFNullToString(cmLdSanctKw);
			  String cmLdSanctHp  =(String)object.get("cmLdSanctHp");
			  cmLdSanctHp              = ReferenceUtil.ConvertIFNullToString(cmLdSanctHp);
			  String cmLdSanctKva  =(String)object.get("cmLdSanctKva");
			  cmLdSanctKva              = ReferenceUtil.ConvertIFNullToString(cmLdSanctKva);
			  String cmLightLoad  =(String)object.get("cmLightLoad");
			  cmLightLoad              = ReferenceUtil.ConvertIFNullToString(cmLightLoad);
			  String cmHeatLoad  =(String)object.get("cmHeatLoad");
			  cmHeatLoad              = ReferenceUtil.ConvertIFNullToString(cmHeatLoad);
			  String cmMotivePower  =(String)object.get("cmMotivePower");
			  cmMotivePower              = ReferenceUtil.ConvertIFNullToString(cmMotivePower);
			  String cmSupplyVolt  =(String)object.get("cmSupplyVolt");
			  cmSupplyVolt              = ReferenceUtil.ConvertIFNullToString(cmSupplyVolt);
			  String cmPurgeFlg  =(String)object.get("cmPurgeFlg");
			  cmPurgeFlg              = ReferenceUtil.ConvertIFNullToString(cmPurgeFlg);
			  String cmInstallTyp  =(String)object.get("cmInstallTyp");
			  cmInstallTyp              = ReferenceUtil.ConvertIFNullToString(cmInstallTyp);
			  String cmInstallTypDescription  =(String)object.get("cmInstallTypDescription");
			  cmInstallTypDescription              = ReferenceUtil.ConvertIFNullToString(cmInstallTypDescription);
			  String cmMeteredFlg  =(String)object.get("cmMeteredFlg");
			  cmMeteredFlg              = ReferenceUtil.ConvertIFNullToString(cmMeteredFlg);
			  String cmTlNo  =(String)object.get("cmTlNo");
			  cmTlNo              = ReferenceUtil.ConvertIFNullToString(cmTlNo);
			  String cmCapacitorCap  =(String)object.get("cmCapacitorCap");
			  cmCapacitorCap              = ReferenceUtil.ConvertIFNullToString(cmCapacitorCap);
			  String cmStarterTyp  =(String)object.get("cmStarterTyp");
			  cmStarterTyp              = ReferenceUtil.ConvertIFNullToString(cmStarterTyp);
			  String cmPremisJuris  =(String)object.get("cmPremisJuris");
			  cmPremisJuris              = ReferenceUtil.ConvertIFNullToString(cmPremisJuris);
			  String cmWellTyp  =(String)object.get("cmWellTyp");
			  cmWellTyp              = ReferenceUtil.ConvertIFNullToString(cmWellTyp);
			  String cmLightingTyp  =(String)object.get("cmLightingTyp");
			  cmLightingTyp              = ReferenceUtil.ConvertIFNullToString(cmLightingTyp);
			  String cmConnLdHp  =(String)object.get("cmConnLdHp");
			  cmConnLdHp              = ReferenceUtil.ConvertIFNullToString(cmConnLdHp);
			  String cmConnLdKw  =(String)object.get("cmConnLdKw");
			  cmConnLdKw              = ReferenceUtil.ConvertIFNullToString(cmConnLdKw);
			  String cmSupplyEffFrmDt  =(String)object.get("cmSupplyEffFrmDt");
			  cmSupplyEffFrmDt              = ReferenceUtil.ConvertIFNullToString(cmSupplyEffFrmDt);
			  String cmTaxExemptFlg  =(String)object.get("cmTaxExemptFlg");
			  cmTaxExemptFlg              = ReferenceUtil.ConvertIFNullToString(cmTaxExemptFlg);
			  String cmUnauthFlg  =(String)object.get("cmUnauthFlg");
			  cmUnauthFlg              = ReferenceUtil.ConvertIFNullToString(cmUnauthFlg);
			  String cmPwrPurEffFrmDt  =(String)object.get("cmPwrPurEffFrmDt");
			  cmPwrPurEffFrmDt              = ReferenceUtil.ConvertIFNullToString(cmPwrPurEffFrmDt);
			  String cmIndEffFrmDt  =(String)object.get("cmIndEffFrmDt");
			  cmIndEffFrmDt              = ReferenceUtil.ConvertIFNullToString(cmIndEffFrmDt);
			  String cmConnTyp  =(String)object.get("cmConnTyp");
			  cmConnTyp              = ReferenceUtil.ConvertIFNullToString(cmConnTyp);
			  String cmNtrLocCd  =(String)object.get("cmNtrLocCd");
			  cmNtrLocCd              = ReferenceUtil.ConvertIFNullToString(cmNtrLocCd);
			  String cmBjkjOutlet  =(String)object.get("cmBjkjOutlet");
			  cmBjkjOutlet              = ReferenceUtil.ConvertIFNullToString(cmBjkjOutlet);
			  String cmMinDemandEntl  =(String)object.get("cmMinDemandEntl");
			  cmMinDemandEntl              = ReferenceUtil.ConvertIFNullToString(cmMinDemandEntl);
			  String cmBulkNoOfHouses  =(String)object.get("cmBulkNoOfHouses");
			  cmBulkNoOfHouses              = ReferenceUtil.ConvertIFNullToString(cmBulkNoOfHouses);
			  String cmIvrsId  =(String)object.get("cmIvrsId");
			  cmIvrsId              = ReferenceUtil.ConvertIFNullToString(cmIvrsId);
			  
			  String cmUser  =(String)object.get("cmUser");
			  cmUser              = ReferenceUtil.ConvertIFNullToString(cmUser);
			  String cmTmpstp  =(String)object.get("cmTmpstp");
			  cmTmpstp              = ReferenceUtil.ConvertIFNullToString(cmTmpstp);
			  String cmPoleNo  =(String)object.get("cmPoleNo");
			  cmPoleNo              = ReferenceUtil.ConvertIFNullToString(cmPoleNo);
			  String cmBegMnth  =(String)object.get("cmBegMnth");
			  cmBegMnth              = ReferenceUtil.ConvertIFNullToString(cmBegMnth);
			  String cmTmpDurn  =(String)object.get("cmTmpDurn");
			  cmTmpDurn              = ReferenceUtil.ConvertIFNullToString(cmTmpDurn);
			  String cmFirstBillDcFlg  =(String)object.get("cmFirstBillDcFlg");
			  cmFirstBillDcFlg              = ReferenceUtil.ConvertIFNullToString(cmFirstBillDcFlg);
			  String cmRmks  =(String)object.get("cmRmks");
			  cmRmks              = ReferenceUtil.ConvertIFNullToString(cmRmks);
			  String cmSubmeterFlg  =(String)object.get("cmSubmeterFlg");
			  cmSubmeterFlg              = ReferenceUtil.ConvertIFNullToString(cmSubmeterFlg);
			  String cmDblMtrFlg  =(String)object.get("cmDblMtrFlg");
			  cmDblMtrFlg              = ReferenceUtil.ConvertIFNullToString(cmDblMtrFlg);
			  String cmSlumFlg  =(String)object.get("cmSlumFlg");
			  cmSlumFlg              = ReferenceUtil.ConvertIFNullToString(cmSlumFlg);
			  String cmEmailId  =(String)object.get("cmEmailId");
			  cmEmailId              = ReferenceUtil.ConvertIFNullToString(cmEmailId);
			  String cmCstNo  =(String)object.get("cmCstNo");
			  cmCstNo              = ReferenceUtil.ConvertIFNullToString(cmCstNo);
			  String cmKstNo  =(String)object.get("cmKstNo");
			  cmKstNo              = ReferenceUtil.ConvertIFNullToString(cmKstNo);
			  String cmTinNo  =(String)object.get("cmTinNo");
			  cmTinNo              = ReferenceUtil.ConvertIFNullToString(cmTinNo);
			  String cmTransmissionLineNo  =(String)object.get("cmTransmissionLineNo");
			  cmTransmissionLineNo              = ReferenceUtil.ConvertIFNullToString(cmTransmissionLineNo);
			  String spotFolioNo  =(String)object.get("spotFolioNo");
			  spotFolioNo              = ReferenceUtil.ConvertIFNullToString(spotFolioNo);
			  String toFolioNo  =(String)object.get("toFolioNo");
			  toFolioNo              = ReferenceUtil.ConvertIFNullToString(toFolioNo);
			  String dtcConnLoad  =(String)object.get("dtcConnLoad");
			  dtcConnLoad              = ReferenceUtil.ConvertIFNullToString(dtcConnLoad);
			  String dtcCapacity   =(String)object.get("dtcCapacity");
			  dtcCapacity              = ReferenceUtil.ConvertIFNullToString(dtcCapacity);
			  String dtc  =(String)object.get("dtc");
			  dtc              = ReferenceUtil.ConvertIFNullToString(dtc);
			  String dtcLoad  = (String)object.get("dtcLoad");
			  dtcLoad              = ReferenceUtil.ConvertIFNullToString(dtcLoad);
			  String noOfdays  =(String)object.get("noOfdays");
			  noOfdays              = ReferenceUtil.ConvertIFNullToString(noOfdays);
			  String cmFullRRno  =(String)object.get("cmFullRRno");
			  cmFullRRno              = ReferenceUtil.ConvertIFNullToString(cmFullRRno);
			  String cmRowNum  =(String)object.get("cmRowNum");
			  cmRowNum              = ReferenceUtil.ConvertIFNullToString(cmRowNum);
			  String CM_VERSION  =(String)object.get("CM_VERSION");
			  CM_VERSION              = ReferenceUtil.ConvertIFNullToString(CM_VERSION);
			  String cmPhaseOfInstln  =(String)object.get("cmPhaseOfInstln");

			/*
			 * 
			 * 
			*/
			
			
			
			
			String	strCMDesc = "", strCMValues = "",  strCM = "",strCD="",strSF="",
					strCTPTDesc="",strDocumentsDesc="", strCTPTValues="",strDocumentsValues="",
					strIntimationDesc="",strIntimationValues="",strDepositDesc="",strDepositValues="";

			cmKebOfficeFlg    = (cmKebOfficeFlg == "") 		? "N"  : cmKebOfficeFlg ;
			cmPurgeFlg 	      = (cmPurgeFlg == "") 			? "N"  : cmPurgeFlg ;
			cmTaxExemptFlg	  = (cmTaxExemptFlg == "") 		? "N"  : cmTaxExemptFlg ;
			cmUnauthFlg	      = (cmUnauthFlg == "") 		? "N"  : cmUnauthFlg ;
			cmPoleNo		  = (cmPoleNo == "") 			? "0"  : cmPoleNo ;
			
			String beginMonth	= (beginningMonth1 == null) 			? ""  : beginningMonth1 ;
			// return 0 values
			cmPinCd        = ReferenceUtil.validateTernaryCondition(cmPinCd) ;
			//cmTelephoneNo  = ReferenceUtil.validateTernaryCondition(cmTelephoneNo) ;
			cmPowerSanctNo = ReferenceUtil.validateTernaryCondition(cmPowerSanctNo) ;
			cmLdgrNo       = ReferenceUtil.validateTernaryCondition(cmLdgrNo) ;
			cmFolioNo      = ReferenceUtil.validateTernaryCondition(cmFolioNo);
			cmMtrRdgCycle  = ReferenceUtil.validateTernaryCondition(cmMtrRdgCycle);
			cmMtrRdrCd     = ReferenceUtil.validateTernaryCondition(cmMtrRdrCd) ;
			
			cmMtrRdgDay       		= (cmMtrRdgDay == "" || cmMtrRdgDay == null) 		 ? "0" : cmMtrRdgDay;
			cmLineMin				= (cmLineMin == "" || cmLineMin == null) 	  	 ? "0" : cmLineMin ;
			cmMinDemandEntl		 	= (cmMinDemandEntl == "" || cmMinDemandEntl == null) 	 ? "0" : cmMinDemandEntl ;
			cmLdSanctKw			    = (cmLdSanctKw == "" || cmLdSanctKw == null)		 ? "0" : cmLdSanctKw ;
			cmLdSanctHp			    = (cmLdSanctHp == "" || cmLdSanctHp == null)	     ? "0" : cmLdSanctHp ;
			cmLdSanctKva			= (cmLdSanctKva  == "" || cmLdSanctKva == null)		 ? "0" : cmLdSanctKva ;
			cmLightLoad			    = (cmLightLoad== "" || cmLightLoad == null) 		 ? "0" : cmLightLoad ;
			cmHeatLoad			    = (cmHeatLoad== "" || cmHeatLoad == null) 		 ? "0" : cmHeatLoad ;
			cmMotivePower			= (cmMotivePower== "" || cmMotivePower == null) 		 ? "0" : cmMotivePower ;
			cmSupplyVolt			= (cmSupplyVolt== "" || cmSupplyVolt == null) 		 ? "230" : cmSupplyVolt ;
		    cmConnLdHp				= (cmConnLdHp== "" || cmConnLdHp == null) 		 ? "0" : cmConnLdHp ;
			cmConnLdKw				= (cmConnLdKw == "" || cmConnLdKw == null) 		 ? "0" : cmConnLdKw;
			cmPhaseOfInstln	        = (cmPhaseOfInstln == "" || cmPhaseOfInstln == null) 	 ? "0" : cmPhaseOfInstln ;
			cmBjkjOutlet			= (cmBjkjOutlet == "" || cmBjkjOutlet == null) 	 	 ? "0" : cmBjkjOutlet ;
			cmBulkNoOfHouses        = (cmBulkNoOfHouses == "" || cmBulkNoOfHouses == null) 	 ? "1" : cmBulkNoOfHouses ;
			cmTmpDurn				= (cmTmpDurn == "" || cmTmpDurn == null) 	 	 ? "0" : cmTmpDurn ;
			
			cmSupplyEffFrmDt		= (cmSupplyEffFrmDt =="" || cmSupplyEffFrmDt == null)	 ? "" : cmSupplyEffFrmDt ;
			cmPwrPurEffFrmDt		= (cmPwrPurEffFrmDt=="" || cmPwrPurEffFrmDt == null)	 ? "" : cmPwrPurEffFrmDt ;
			cmIndustryCd		    = (cmIndustryCd =="" || cmIndustryCd == null)	 	 ? "" : cmIndustryCd ;
			cmFirstBillDcFlg        = (cmFirstBillDcFlg == "" || cmFirstBillDcFlg == null)   ? "" : cmFirstBillDcFlg ;
			cmRmks				    = (cmRmks == "" || cmRmks == null) 			 ? "" : cmRmks ;
			cmSlumFlg				= (cmSlumFlg == "" || cmSlumFlg == null) 		 ? "" : cmSlumFlg ;
			cmEmailId				= (cmEmailId == "" || cmEmailId == null) 		 ? "" : cmEmailId ;
			cmKstNo				    = (cmKstNo   == "" || cmKstNo == null) 		 ? "" : cmKstNo ;
			cmCstNo				    = (cmCstNo   == "" || cmCstNo == null) 		 ? "" : cmCstNo ;
			cmTodMeterFlag			= (cmTodMeterFlag == "Y")  ? "Y": "N";
			cmTinNo				    = (cmTinNo   == "" || cmTinNo == null) 		 ? "" : cmTinNo ;
			cmFlConsumer		    = (cmFlConsumer == "" || cmFlConsumer == null) 	     ? "N": "Y";
			
			/*'=====Begin============To insert in the Consumer Master ====================*/
			
			if(OPTION.equals("ADD")){
				
				strCMDesc = strCMDesc + "INSERT INTO CUST_MASTER " ;
	            
	            strCMDesc 	= strCMDesc + "(CM_RR_NO,CM_SERVICE_DT," ;
	            strCMValues = strCMValues + "('" + cmRrNo+ "', TO_DATE('"+cmServiceDt+"', 'dd/MM/yyyy')," ;
	           
	            
	            strCMDesc	= strCMDesc + "CM_OLD_RR_NO,CM_KEB_OFFICE_FLG,";
	            strCMValues = strCMValues + " '"+cmOldRrNo+ "'  ,  '"+cmKebOfficeFlg+"' ,";
	          
	            strCMDesc 	= strCMDesc + "CM_PIN_CD,CM_TELEPHONE_NO,";
	            strCMValues = strCMValues + " '"+cmPinCd+"' , '"+cmTelephoneNo+"' ,";
	                                                       
	            strCMDesc 	= strCMDesc + "CM_TALUK_CD,CM_DISTRICT_CD,";
	           /* strCMValues = strCMValues + " '"+ReferenceUtil.getTalukTypeNameTOCode(cmTalukDescription,ConnType )+"' , '"
	            						  + ReferenceUtil.getDistrictTypeNameTOCode(cmDistrictDescription,ConnType)+"', ";*/
	            strCMValues = strCMValues + " '"+cmTalukCd+"' , '"
						  + cmDistrictCd+"', ";
	          
	            strCMDesc	= strCMDesc + "CM_STATE_CONSTITUENCY_CD,CM_CENTRAL_CONSTITUENCY_CD,";
	           /* strCMValues = strCMValues + " '"+ReferenceUtil.getStateConstencyNameTOCode(cmStateConstituencyDescription,ConnType)+"' ,'"
	            			              +ReferenceUtil.getCentralConstencyNameTOCode(cmCentralConstituencyDescription,ConnType)+"' ,";*/
	            strCMValues = strCMValues + " '"+cmStateConstituencyCd+"' ,'"
			              +cmCentralConstituencyCd+"' ,";
	          
	            strCMDesc 	= strCMDesc + "CM_REGION_CD,CM_PWR_PURPOSE,";
	            /*strCMValues = strCMValues + " '"+ReferenceUtil.getRegionConstencyNameTOCode(cmRegionDescription,ConnType)+ "' ,'"
	                                      +ReferenceUtil.getPowerPurposeNameTOCode(cmPwrPurpose,ConnType)+"', ";*/
	            strCMValues = strCMValues + " '"+cmRegionCd+ "' ,'"
                        +cmPwrPurpose+"', ";
	          
	            strCMDesc 	= strCMDesc + "CM_INDUSTRY_CD,CM_APPLNT_TYP,";
	           /* strCMValues = strCMValues + " '"+ReferenceUtil.getIndustrialNameTOCode(cmIndustryDescription,ConnType)+"' , '"
	                                      +ReferenceUtil.getConsumerCodeType(cmApplntTypDescription,ConnType)+"' ,";*/
	            strCMValues = strCMValues + " '"+cmIndustryCd+"' , '"
                        +cmApplntTyp+"' ,";
	          
	            strCMDesc   = strCMDesc + "CM_POWER_SANCT_NO,CM_POWER_SANCT_DT,";
	            strCMValues = strCMValues + " '"+cmPowerSanctNo+"' , TO_DATE('"+cmPwrPurEffFrmDt+"', 'dd/MM/yyyy') ,";
	          
	           
	            strCMDesc   = strCMDesc + "CM_POWER_SANCT_AUTH,CM_NXT_RR_NO,";
	           /* strCMValues = strCMValues + " '"+ReferenceUtil.getPwrSanctionedNameTOCode(cmPowerSanctAuth,ConnType)+"' ,'"
	                                      +cmNxtRrNo+"' ,";*/
	            strCMValues = strCMValues + " '"+cmPowerSanctAuth+"' ,'"
                        +cmNxtRrNo+"' ,";
	           
	            strCMDesc	= strCMDesc + "CM_LDGR_NO,CM_LDGR_OPENED_DT,";
	            strCMValues = strCMValues + " '"+cmLdgrNo+ "' , TO_DATE('"+cmLdgrOpenedDt+"', 'dd/MM/yyyy')  , ";
	           
	            strCMDesc 	= strCMDesc + "CM_FOLIO_NO,CM_MTR_RDG_CYCLE,";
	            strCMValues = strCMValues + " '"+cmFolioNo+"' ,'"+cmMtrRdgCycle+"' ,";
	          
	            strCMDesc 	= strCMDesc + "CM_MTR_RDG_DAY,CM_MTR_RDR_CD,";
	            strCMValues = strCMValues + " "+Integer.parseInt(cmMtrRdgDay)+" , '"+cmMtrRdrCd+"',";
	          
	            strCMDesc 	= strCMDesc + "CM_STATION_NO,CM_FDR_NO,";
	            strCMValues = strCMValues + " '"+ReferenceUtil.validateTernaryCondition(cmStationNo)+"' ,'"
	                                            +ReferenceUtil.validateTernaryCondition(cmFdrNo)+"' ,";
	           
	            strCMDesc 	= strCMDesc + "CM_LINE_MIN,CM_OM_UNIT_CD,";
	            /*strCMValues = strCMValues + " "+cmLineMin+" , '"+ReferenceUtil.getOMUnitCodes(cmOmUnitCd,ConnType)+"' ,";*/
	            strCMValues = strCMValues + " "+Integer.parseInt(cmLineMin)+" , '"+cmOmUnitCd+"' ,";

	            strCMDesc 	= strCMDesc + "CM_TRSFMR_NO,CM_AVG_CONSMP,";
	            strCMValues = strCMValues + " '"+Integer.parseInt(ReferenceUtil.validateTernaryCondition(cmTrsfmrNo))+ "' ,NULL,"; 
	         
	            strCMDesc	= strCMDesc + "CM_FL_CONSUMER,CM_PWR_CUT_EXEMPT_FLG,";
	            //strCMValues = strCMValues + " '"+cmFlConsumer+"', '"+ReferenceUtil.validateTernaryCondition(cmPwrCutExemptFlg)+"' ,";
	            strCMValues = strCMValues + " '"+cmFlConsumer+"', '"+cmPwrCutExemptFlg+"' ,";
	         
	            strCMDesc 	= strCMDesc + "CM_CONSMR_STS,CM_CHQ_BOUNCE_FLG,";
	            //strCMValues = strCMValues + " '"+ReferenceUtil.getConsumerStatusNameTOCode(cmConsmrStsDescription,ConnType) + "' ,NULL,";
	            strCMValues = strCMValues + " '"+cmConsmrSts + "' ,'"+cmChqBounceFlg+"',";
	          
	            strCMDesc 	= strCMDesc + "CM_DL_COUNT,CM_DL_SENT,";
	            strCMValues = strCMValues + " NULL, NULL, ";
	        
	            strCMDesc 	= strCMDesc + "CM_TOT_MMD,CM_LAST_MMD_DT,";
	            strCMValues = strCMValues + " NULL, NULL,";
	          
	            strCMDesc 	= strCMDesc + "CM_100_ENERGY_ENTLMNT,CM_TMP_FOLIO_NO,";
	            strCMValues = strCMValues + "NULL , NULL,";
	           
	            strCMDesc 	= strCMDesc + "CM_100_DMD_ENTLMNT,CM_TRF_CATG,";
	            //strCMValues = strCMValues + " "+cmMinDemandEntl+" , '"+ ReferenceUtil.getTariffCodeByName(cmTrfCatgDescription,ConnType)+ "' ,";
	            strCMValues = strCMValues + " '"+Integer.parseInt(cmMinDemandEntl)+"' , '"+ cmTrfCatg+ "' ,";
	           
	            strCMDesc 	= strCMDesc + "CM_TRF_EFFECT_FRM_DT, CM_LD_EFFECT_FRM_DT, ";
	            strCMValues = strCMValues + " TO_DATE('"+cmTrfEffectFrmDt+"', 'dd/MM/yyyy'), TO_DATE('"+cmLdgrOpenedDt+"', 'dd/MM/yyyy') ,";
	          
	            strCMDesc 	= strCMDesc + "CM_LD_SANCT_KW,CM_LD_SANCT_HP,";
	            strCMValues = strCMValues + " "+Double.parseDouble(cmLdSanctKw)+" , "+Double.parseDouble(cmLdSanctHp)+" ,";
	         
	            strCMDesc 	= strCMDesc + "CM_LD_SANCT_KVA,CM_LIGHT_LOAD,";
	            strCMValues = strCMValues + " "+Double.parseDouble(cmLdSanctKva)+" ,  "+Double.parseDouble(cmLightLoad)+" ,";
	          
	            strCMDesc 	= strCMDesc + "CM_HEAT_LOAD,CM_MOTIVE_POWER,";
	            strCMValues = strCMValues + " "+Double.parseDouble(cmHeatLoad)+" , "+Double.parseDouble(cmMotivePower)+" ,";
	   
	            strCMDesc 	= strCMDesc + "CM_SUPPLY_VOLT,CM_PURGE_FLG,";
	            strCMValues = strCMValues + " "+Double.parseDouble(cmSupplyVolt)+" , '"+cmPurgeFlg+"' ,";
	         
	            strCMDesc 	= strCMDesc + "CM_INSTALL_TYP,CM_METERED_FLG,";
	            //strCMValues = strCMValues + " '"+ReferenceUtil.getInstallationTypeCodes(cmInstallTyp,ConnType)+"' , NULL,";
	            strCMValues = strCMValues + " '"+cmInstallTyp+"' , '"+cmMeteredFlg+"',";
	        
	            strCMDesc 	= strCMDesc + "CM_TL_NO,CM_CAPACITOR_CAP,";
	            strCMValues = strCMValues + "'"+cmTransmissionLineNo+"', '"+cmCapacitorCap+"',";
	          
	            strCMDesc 	= strCMDesc + "CM_STARTER_TYP,CM_PREMIS_JURIS,";
	            //strCMValues = strCMValues + " '"+ReferenceUtil.getStarterTypeNameTOCode(cmStarterTyp,ConnType)+"' , '"
	              //                        +ReferenceUtil.getJurisdictionTypeNameTOCode(cmPremisJuris,ConnType)+"' ,";
	            strCMValues = strCMValues + " '"+cmStarterTyp+"' , '"
                        +cmPremisJuris+"' ,";

	         
	            strCMDesc 	= strCMDesc + "CM_WELL_TYP,CM_LIGHTING_TYP,";
	            //strCMValues = strCMValues + " '"+ReferenceUtil.getWellTypeNameTOCode(cmWellTyp,ConnType)+"' , '"
	            //                          +ReferenceUtil.getLightingNameTOCode(cmLightingTyp,ConnType)+"' ,";
	            strCMValues = strCMValues + " '"+cmWellTyp+"' , '"
                        +cmLightingTyp+"' ,";

	         
	            strCMDesc 	= strCMDesc + "CM_CONN_LD_HP,CM_CONN_LD_KW,";
	            strCMValues = strCMValues + " "+Double.parseDouble(cmConnLdHp)+ " , "+Double.parseDouble(cmConnLdKw)+ " ,";
	          
	            strCMDesc 	= strCMDesc + "CM_SUPPLY_EFF_FRM_DT,CM_PHASE_OF_INSTLN,";
	            strCMValues = strCMValues + " TO_DATE('"+cmSupplyEffFrmDt+"','dd/mm/yyyy') , "+cmPhaseOfInstln+" ,";
	          
	            strCMDesc 	= strCMDesc + " CM_TAX_EXEMPT_FLG, CM_UNAUTH_FLG,";
	            strCMValues = strCMValues + " '"+cmTaxExemptFlg+"' , '"+cmUnauthFlg+"' ,";
	          
	            strCMDesc 	= strCMDesc + "CM_PWR_PUR_EFF_FRM_DT,CM_IND_EFF_FRM_DT,";
	            strCMValues = strCMValues + " TO_DATE('"+cmPowerSanctDt+"' ,'dd/mm/yyyy') , TO_DATE('"+cmIndEffFrmDt+"','dd/mm/yyyy') ,";
	           
	            strCMDesc 	= strCMDesc + "CM_CONN_TYP,CM_NTR_LOC_CD,";
	            //strCMValues = strCMValues + " '"+ReferenceUtil.getConnectionTypeNameTOCode(cmConnTyp,ConnType)+"' , '"
	             //                               +ReferenceUtil.validateTernaryCondition(location)+ "' ," ;
	            strCMValues = strCMValues + " '"+cmConnTyp+"' , '"
                        +ReferenceUtil.validateTernaryCondition(cmNtrLocCd)+ "' ," ;

	        
	            strCMDesc 	= strCMDesc + "CM_BJKJ_OUTLET,CM_MIN_DEMAND_ENTL,";
	            strCMValues = strCMValues + " "+cmBjkjOutlet+" ,NULL,";
	           
	            strCMDesc 	= strCMDesc + "CM_BULK_NO_OF_HOUSES,CM_IVRS_ID,";
	            strCMValues = strCMValues + "  '"+cmBulkNoOfHouses+"' ,NULL,";
	           
	            strCMDesc 	= strCMDesc + "CM_POLE_NO,";
	            strCMValues = strCMValues + " '"+cmPoleNo+"' ,";

	            strCMDesc 	= strCMDesc + "CM_TMP_DURN,";
	            strCMValues = strCMValues + " "+cmTmpDurn+" ,";

	            strCMDesc 	= strCMDesc + "CM_RMKS,";
	            strCMValues = strCMValues + " '"+cmRmks+ "' ,";
	           
	            strCMDesc 	= strCMDesc + "CM_BEG_MNTH,";
	        	strCMValues = strCMValues + " '"+beginMonth+"' ,";

	            strCMDesc 	= strCMDesc + "CM_FIRST_BILL_DC_FLG,";
	            strCMValues = strCMValues + " '"+cmFirstBillDcFlg+"' ,";

	           
	            strCMDesc = strCMDesc + "CM_USER ,CM_TMPSTP, CM_SUBMETER_FLG,CM_SLUM_FLG,";
	           
	            strCMDesc = strCMDesc + "CM_EMAIL_ID,CM_KST_NO,CM_CST_NO, CM_tod_meter, CM_TIN_NO ) ";

	            strCMValues = strCMValues + " '"+cmUser+"' ,SYSDATE, '"+cmSubmeterFlg+"',";
	            strCMValues = strCMValues + " '"+cmSlumFlg+"' ,";
	            strCMValues = strCMValues + " '"+cmEmailId+"',";
	            strCMValues = strCMValues + " '"+cmKstNo+"',";
	            strCMValues = strCMValues + " '"+cmCstNo+"',";
	            strCMValues = strCMValues + " '"+cmTodMeterFlag+"',";
	            strCMValues = strCMValues + " '"+cmTinNo+"')";

	            strCM = strCMDesc + "VALUES" + strCMValues ;
	            
			}else{
				if(OPTION.equals("UPDATE")){
					
					strCM = "" ;
		            strCM = strCM + " UPDATE CUST_MASTER SET " ;
		           
		            strCM = strCM + " CM_SERVICE_DT = TO_DATE('"+cmServiceDt+"', 'dd/MM/yyyy') ,";
		            strCM = strCM + " CM_OLD_RR_NO =  '"+cmOldRrNo+"' ,";
		            strCM = strCM + " CM_KEB_OFFICE_FLG = '"+cmKebOfficeFlg+"' ,";
		            strCM = strCM + " CM_PIN_CD = '"+cmPinCd+"'  , ";
		            strCM = strCM + " CM_TELEPHONE_NO = '"+cmTelephoneNo+"' ,";

		            strCM = strCM + " CM_TALUK_CD = '"+cmTalukCd+"' ," ;
	                strCM = strCM + " CM_DISTRICT_CD = '"+cmDistrictCd+"' ,";
	                strCM = strCM + " CM_STATE_CONSTITUENCY_CD = '"+cmStateConstituencyCd+ "' ,";
	                strCM = strCM + " CM_CENTRAL_CONSTITUENCY_CD = '"+cmCentralConstituencyCd+"' ,";

	                strCM = strCM + " CM_REGION_CD =  '"+cmRegionCd+ "' ,";
	                strCM = strCM + " CM_PWR_PURPOSE =  '"+cmPwrPurpose+"' ,";
	                strCM = strCM + " CM_INDUSTRY_CD =  '"+cmIndustryCd+"' ,";
	                strCM = strCM + " CM_APPLNT_TYP =  '"+cmApplntTyp+"' ,";
	                strCM = strCM + " CM_POWER_SANCT_NO =  '"+cmPowerSanctNo+ "' ,";
	                
	                strCM = strCM + " CM_CAPACITOR_CAP =  '"+cmCapacitorCap+"' ,";
	                strCM = strCM + " CM_MIN_DEMAND_ENTL =  '"+cmMinDemandEntl+ "' ,";
	                strCM = strCM + " CM_TL_NO =  '"+cmTransmissionLineNo+ "' ,";
	                strCM = strCM + " CM_NTR_LOC_CD =  '"+cmNtrLocCd+ "' ,";
	                //

	                strCM = strCM + " CM_POWER_SANCT_DT = TO_DATE('"+cmPowerSanctDt+"', 'dd/MM/yyyy') ,";
	                strCM = strCM + " CM_POWER_SANCT_AUTH = '"+cmPowerSanctAuth+"' ,";
	                strCM = strCM + " CM_NXT_RR_NO = '"+cmNxtRrNo+"' ,";
	                strCM = strCM + " CM_LDGR_NO =  '"+cmLdgrNo+"' ,";
	                strCM = strCM + " CM_LDGR_OPENED_DT =  TO_DATE('"+cmLdgrOpenedDt+"', 'dd/MM/yyyy')   , ";

	                strCM = strCM + " CM_FOLIO_NO =  '"+cmFolioNo+"' ,";
	                strCM = strCM + " CM_MTR_RDG_CYCLE =  '"+cmMtrRdgCycle+"' ,";
	                strCM = strCM + " CM_MTR_RDG_DAY = "+cmMtrRdgDay+" ,";
	                strCM = strCM + " CM_MTR_RDR_CD =  '"+cmMtrRdrCd+"' ,";
	                strCM = strCM + " CM_STATION_NO =  '"+ReferenceUtil.validateTernaryCondition(cmStationNo)+"' ,";
	                strCM = strCM + " CM_FDR_NO =  '"+ReferenceUtil.validateTernaryCondition(cmFdrNo)+"' ,";
	                
	                strCM = strCM + " CM_LINE_MIN =  "+cmLineMin+" ,";
	                strCM = strCM + " CM_OM_UNIT_CD =  '"+cmOmUnitCd+"' ,";
	                strCM = strCM + " CM_TRSFMR_NO =  '"+ReferenceUtil.validateTernaryCondition(cmTrsfmrNo)+"' ,";
	               
	                strCM = strCM + " CM_PWR_CUT_EXEMPT_FLG =  '"+ReferenceUtil.validateTernaryCondition(cmPwrCutExemptFlg)+"' ,";
	                strCM = strCM + " CM_POLE_NO =  '"+cmPoleNo+"' ,";

	               /* strCM = strCM + " CM_CONSMR_STS =  '"+ReferenceUtill.getConsumerStatusNameTOCode(mst.getCmConsmrSts()) +"' ,";*/
	                strCM = strCM + " CM_100_DMD_ENTLMNT =  "+cm100DmdEntlmnt+" ,";
	                /*strCM = strCM + " CM_TRF_CATG =  '"+ ReferenceUtill.getTariffCodeByName(mst.getCmTariffcatogory())+"' ,";*/
	                strCM = strCM + " CM_TRF_EFFECT_FRM_DT = TO_DATE('"+cmTrfEffectFrmDt+"', 'dd/MM/yyyy') ,";
	                strCM = strCM + " CM_LD_EFFECT_FRM_DT = TO_DATE('"+cmLdgrOpenedDt+"', 'dd/MM/yyyy') ,";

	        		strCM = strCM + " CM_LD_SANCT_KW = "+cmLdSanctKw+ " ,";
	                strCM = strCM + " CM_LD_SANCT_HP = "+cmLdSanctHp+" ,";
	                strCM = strCM + " CM_LD_SANCT_KVA = "+cmLdSanctKva+" ,";
	                strCM = strCM + " CM_LIGHT_LOAD = "+cmLightLoad+" ,";
	                strCM = strCM + " CM_HEAT_LOAD =  "+cmHeatLoad+" ,";
	                strCM = strCM + " CM_MOTIVE_POWER = "+cmMotivePower+" ,";

	                strCM = strCM + " CM_SUPPLY_VOLT = "+cmSupplyVolt+" ,";
	                strCM = strCM + " CM_PURGE_FLG = '"+cmPurgeFlg+"' ,";
	                strCM = strCM + " CM_INSTALL_TYP = '"+cmInstallTyp+"' ,";
	                       
	        		strCM = strCM + " CM_STARTER_TYP =  '"+cmStarterTyp+"' ,";
	                strCM = strCM + " CM_PREMIS_JURIS =  '"+cmPremisJuris+"' ,";
	                strCM = strCM + " CM_WELL_TYP =  '"+cmWellTyp+"' ,";
	                strCM = strCM + " CM_LIGHTING_TYP = '"+cmLightingTyp+"' ,";
	                strCM = strCM + " CM_CONN_LD_HP = "+cmConnLdHp+" ,";
	                
	                strCM = strCM + " CM_CONN_LD_KW =  "+cmConnLdKw+" ,";
	                strCM = strCM + " CM_SUPPLY_EFF_FRM_DT =  TO_DATE('"+cmSupplyEffFrmDt+"','dd/mm/yyyy') ,";
	                strCM = strCM + " CM_PHASE_OF_INSTLN =  " +cmPhaseOfInstln+" ,";
	                strCM = strCM + " CM_TAX_EXEMPT_FLG = '"+cmTaxExemptFlg+"' ,";
	                strCM = strCM + " CM_UNAUTH_FLG = '"+cmUnauthFlg+"' ,";
	                
		    		strCM = strCM + " CM_PWR_PUR_EFF_FRM_DT = TO_DATE('"+cmPwrPurEffFrmDt+"' ,'dd/mm/yyyy'),";
		            strCM = strCM + " CM_IND_EFF_FRM_DT = TO_DATE('"+cmIndEffFrmDt+"','dd/mm/yyyy') ,";
		            strCM = strCM + " CM_CONN_TYP = '"+cmConnTyp+"' ,";
		            //strCM = strCM + " CM_NTR_LOC_CD = '"+ReferenceUtil.validateTernaryCondition(location)+"' ,";
		            strCM = strCM + " CM_BJKJ_OUTLET = "+cmBjkjOutlet+" ," ;
		    		strCM = strCM + " CM_BULK_NO_OF_HOUSES = "+cmBulkNoOfHouses+" ,";
		            strCM = strCM + " CM_RMKS = '" +cmRmks+ "' ,";
		            strCM = strCM + " CM_TMP_DURN = "+cmTmpDurn+" ,";
		            
		            
		            strCM = strCM + " CM_BEG_MNTH = '"+cmBegMnth+"' ,";
	                strCM = strCM + " CM_SUBMETER_FLG = '"+cmSubmeterFlg+"' ,";
	                strCM = strCM + " CM_FIRST_BILL_DC_FLG = '"+cmFirstBillDcFlg+"' ,";
	                strCM = strCM + " CM_SLUM_FLG = '"+cmSlumFlg+"',";
	                strCM = strCM + " CM_EMAIL_ID = '"+cmEmailId+"',";
	                strCM = strCM + " CM_KST_NO = '"+cmKstNo+"',";
	                strCM = strCM + " CM_CST_NO = '" +cmCstNo+"',";
	                strCM = strCM + " CM_TIN_NO = '"+cmTinNo+"',";
	                strCM = strCM + " CM_USER = '"+cmUser+"' ,";
	                strCM = strCM + " CM_TMPSTP = SYSDATE , ";
	                strCM = strCM + " CM_TOD_METER = '"+cmTodMeterFlag+"' ";
	                strCM = strCM + " WHERE CM_RR_NO = '"+cmRrNo+"' ";
					
				}
			}
			
			
			
            System.out.println("Query = "+strCM);
           
            ps = dbConnection.prepareStatement(strCM);
            int v = ps.executeUpdate();
            
            if(v > 0){
            	Result = true;
            }else{
            	Result = false;
            }
            System.out.println("Row Updated...!"+Result);
            
          /*  =====End============To insert in the Consumer Master ====================*/
		}
            
		} 
		 catch (JSONException e) {
					// TODO: handle exception
					Result = false;
					try {
						dbConnection.rollback();
						dbConnection.setAutoCommit(true);
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
					Log.info("[" + this.getClass().getSimpleName() + "]-->" + "Exception Occured : " + e.getMessage());
					e.printStackTrace();
				}
		
		catch (Exception e) {
			// TODO: handle exception
			Result = false;
			try {
				dbConnection.rollback();
				dbConnection.setAutoCommit(true);
			} catch (SQLException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
			Log.info("["+this.getClass().getSimpleName()+"]-->"+"Exception Occured : "+e.getMessage());
			e.printStackTrace();
			
			
		}
		return Result;
	}
	
	@Override
	public JSONObject temporaryRenewal(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_no");
		String conn_type = (String) object.get("conn_type");
		
		try {
			if(!rr_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				accountsCS=dbConnection.prepareCall(DBQueries.TEMPORARY_RENEWAL);
				accountsCS.setString(1, rr_no);
				accountsCS.setString(2, (String) object.get("service_date"));
				accountsCS.setString(3, (String) object.get("old_duration"));
				accountsCS.setString(4, (String) object.get("new_duration"));
				accountsCS.setString(5, (String) object.get("renewal_remarks"));
				accountsCS.setString(6, (String) object.get("userid"));
				
				accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				
				accountsRS = (ResultSet) accountsCS.getObject(7);
				
				if(accountsRS.next()){
					String RESP = accountsRS.getString("RESP");
					System.out.println("RESP : "+RESP);
					
					if(RESP.equalsIgnoreCase("success")){
						obj.put("status", "success");
						obj.put("message", " Temporary Renewal Successful for the given RR Number "+rr_no.substring(7)+" For "+ (String) object.get("new_duration")+" Days");
						obj.put("cmUser", accountsRS.getString("USER_ID"));
						obj.put("cmTmpstp", accountsRS.getString("TMPSTP"));
						obj.put("cmTmpDurn", accountsRS.getString("TMP_DURATION"));
						obj.put("renewalDate", accountsRS.getString("NEXT_RENWAL_DT"));
					}else if (RESP.equalsIgnoreCase("fail")) {
						obj.put("status", "error");
						obj.put("message", " Temporary Renewal Failed for the given RR Number "+rr_no.substring(7)+" For "+ (String) object.get("new_duration")+" Days");
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
	
	@Override
	public JSONObject getCustomerHistory(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("rr_number");
		
		System.out.println(rr_no);
		
		try {
			if(!rr_no.isEmpty()){
				//dbConnection = DatabaseImpl.GetLTSQLConnection();
				dbConnection = databaseObj.getDatabaseConnection();
				accountsCS=dbConnection.prepareCall(DBQueries.GET_CUSTOMER_HISTORY_DETAILS);
				accountsCS.setString(1, rr_no);
				accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
				accountsCS.executeUpdate();
				accountsRS = (ResultSet) accountsCS.getObject(2);
				while(accountsRS.next())
				{
					JSONObject ackobj=new JSONObject();
					
					ackobj.put("row_num", accountsRS.getString("row_num"));
					ackobj.put("cm_rr_no", accountsRS.getString("cm_rr_no"));
					ackobj.put("cm_service_dt", accountsRS.getString("cm_service_dt"));
					ackobj.put("cm_old_rr_no", accountsRS.getString("cm_old_rr_no"));
					ackobj.put("cm_keb_office_flg", accountsRS.getString("cm_keb_office_flg"));
					ackobj.put("cm_pin_cd", accountsRS.getString("cm_pin_cd"));
					ackobj.put("cm_telephone_no", accountsRS.getString("cm_telephone_no"));
					ackobj.put("cm_taluk_cd", accountsRS.getString("cm_taluk_cd"));
					ackobj.put("cm_taluk_cd_descr", accountsRS.getString("cm_taluk_cd_descr"));
					ackobj.put("cm_district_cd", accountsRS.getString("cm_district_cd"));
					ackobj.put("cm_district_cd_descr", accountsRS.getString("cm_district_cd_descr"));
					ackobj.put("cm_state_constituency_cd", accountsRS.getString("cm_state_constituency_cd"));
					ackobj.put("cm_state_constituency_cd_descr", accountsRS.getString("cm_state_constituency_cd_descr"));
					ackobj.put("cm_central_constituency_cd", accountsRS.getString("cm_central_constituency_cd"));
					ackobj.put("cm_central_constit_cd_descr", accountsRS.getString("cm_central_constit_cd_descr"));
					ackobj.put("cm_region_cd", accountsRS.getString("cm_region_cd"));
					ackobj.put("cm_region_cd_descr", accountsRS.getString("cm_region_cd_descr"));
					ackobj.put("cm_pwr_purpose", accountsRS.getString("cm_pwr_purpose"));
					ackobj.put("cm_pwr_purpose_descr", accountsRS.getString("cm_pwr_purpose_descr"));
					ackobj.put("cm_industry_cd", accountsRS.getString("cm_industry_cd"));
					ackobj.put("cm_industry_cd_descr", accountsRS.getString("cm_industry_cd_descr"));
					ackobj.put("cm_applnt_typ", accountsRS.getString("cm_applnt_typ"));
					ackobj.put("applnt_typ_descr", accountsRS.getString("applnt_typ_descr"));
					ackobj.put("cm_power_sanct_no", accountsRS.getString("cm_power_sanct_no"));
					ackobj.put("cm_power_sanct_dt", accountsRS.getString("cm_power_sanct_dt"));
					ackobj.put("cm_power_sanct_auth", accountsRS.getString("cm_power_sanct_auth"));
					ackobj.put("cm_power_sanct_auth_descr", accountsRS.getString("cm_power_sanct_auth_descr"));
					ackobj.put("cm_nxt_rr_no", accountsRS.getString("cm_nxt_rr_no"));
					ackobj.put("cm_ldgr_no", accountsRS.getString("cm_ldgr_no"));
					ackobj.put("cm_ldgr_opened_dt", accountsRS.getString("cm_ldgr_opened_dt"));
					ackobj.put("cm_folio_no", accountsRS.getString("cm_folio_no"));
					ackobj.put("cm_mtr_rdg_cycle", accountsRS.getString("cm_mtr_rdg_cycle"));
					ackobj.put("cm_mtr_rdg_day", accountsRS.getString("cm_mtr_rdg_day"));
					ackobj.put("cm_mtr_rdr_cd", accountsRS.getString("cm_mtr_rdr_cd"));
					ackobj.put("cm_station_no", accountsRS.getString("cm_station_no"));
					ackobj.put("cm_station_no_descr", accountsRS.getString("cm_station_no_descr"));
					ackobj.put("cm_fdr_no", accountsRS.getString("cm_fdr_no"));
					ackobj.put("cm_fdr_no_descr", accountsRS.getString("cm_fdr_no_descr"));
					ackobj.put("cm_line_min", accountsRS.getString("cm_line_min"));
					ackobj.put("cm_om_unit_cd", accountsRS.getString("cm_om_unit_cd"));
					ackobj.put("cm_om_unit_cd_descr", accountsRS.getString("cm_om_unit_cd_descr"));
					ackobj.put("cm_trsfmr_no", accountsRS.getString("cm_trsfmr_no"));
					ackobj.put("cm_trsfmr_no_descr", accountsRS.getString("cm_trsfmr_no_descr"));
					ackobj.put("cm_avg_consmp", accountsRS.getString("cm_avg_consmp"));
					ackobj.put("cm_fl_consumer", accountsRS.getString("cm_fl_consumer"));
					ackobj.put("cm_pwr_cut_exempt_flg", accountsRS.getString("cm_pwr_cut_exempt_flg"));
					ackobj.put("cm_consmr_sts", accountsRS.getString("cm_consmr_sts"));
					ackobj.put("cm_consmr_sts_descr", accountsRS.getString("cm_consmr_sts_descr"));
					ackobj.put("cm_chq_bounce_flg", accountsRS.getString("cm_chq_bounce_flg"));
					ackobj.put("cm_dl_count", accountsRS.getString("cm_dl_count"));
					ackobj.put("cm_dl_sent", accountsRS.getString("cm_dl_sent"));
					ackobj.put("cm_tot_mmd", accountsRS.getString("cm_tot_mmd"));
					ackobj.put("cm_last_mmd_dt", accountsRS.getString("cm_last_mmd_dt"));
					ackobj.put("cm_100_energy_entlmnt", accountsRS.getString("cm_100_energy_entlmnt"));
					ackobj.put("cm_tmp_folio_no", accountsRS.getString("cm_tmp_folio_no"));
					ackobj.put("cm_100_dmd_entlmnt", accountsRS.getString("cm_100_dmd_entlmnt"));
					ackobj.put("cm_trf_catg", accountsRS.getString("cm_trf_catg"));
					ackobj.put("trf_catg_descr", accountsRS.getString("trf_catg_descr"));
					ackobj.put("cm_trf_effect_frm_dt", accountsRS.getString("cm_trf_effect_frm_dt"));
					ackobj.put("cm_ld_effect_frm_dt", accountsRS.getString("cm_ld_effect_frm_dt"));
					ackobj.put("cm_ld_sanct_kw", accountsRS.getString("cm_ld_sanct_kw"));
					ackobj.put("cm_ld_sanct_hp", accountsRS.getString("cm_ld_sanct_hp"));
					ackobj.put("cm_ld_sanct_kva", accountsRS.getString("cm_ld_sanct_kva"));
					ackobj.put("cm_light_load", accountsRS.getString("cm_light_load"));
					ackobj.put("cm_heat_load", accountsRS.getString("cm_heat_load"));
					ackobj.put("cm_motive_power", accountsRS.getString("cm_motive_power"));
					ackobj.put("cm_supply_volt", accountsRS.getString("cm_supply_volt"));
					ackobj.put("cm_purge_flg", accountsRS.getString("cm_purge_flg"));
					ackobj.put("cm_install_typ", accountsRS.getString("cm_install_typ"));
					ackobj.put("cm_install_typ_descr", accountsRS.getString("cm_install_typ_descr"));
					ackobj.put("cm_metered_flg", accountsRS.getString("cm_metered_flg"));
					ackobj.put("cm_tl_no", accountsRS.getString("cm_tl_no"));
					ackobj.put("cm_capacitor_cap", accountsRS.getString("cm_capacitor_cap"));
					ackobj.put("cm_starter_typ", accountsRS.getString("cm_starter_typ"));
					ackobj.put("cm_starter_typ_descr", accountsRS.getString("cm_starter_typ_descr"));
					ackobj.put("cm_premis_juris", accountsRS.getString("cm_premis_juris"));
					ackobj.put("cm_premis_juris_descr", accountsRS.getString("cm_premis_juris_descr"));
					ackobj.put("cm_well_typ", accountsRS.getString("cm_well_typ"));
					ackobj.put("cm_well_typ_descr", accountsRS.getString("cm_well_typ_descr"));
					ackobj.put("cm_lighting_typ", accountsRS.getString("cm_lighting_typ"));
					ackobj.put("cm_lighting_typ_descr", accountsRS.getString("cm_lighting_typ_descr"));
					ackobj.put("cm_conn_ld_hp", accountsRS.getString("cm_conn_ld_hp"));
					ackobj.put("cm_conn_ld_kw", accountsRS.getString("cm_conn_ld_kw"));
					ackobj.put("cm_supply_eff_frm_dt", accountsRS.getString("cm_supply_eff_frm_dt"));
					ackobj.put("cm_phase_of_instln", accountsRS.getString("cm_phase_of_instln"));
					ackobj.put("cm_phase_of_instln_descr", accountsRS.getString("cm_phase_of_instln_descr"));
					ackobj.put("cm_tax_exempt_flg", accountsRS.getString("cm_tax_exempt_flg"));
					ackobj.put("cm_unauth_flg", accountsRS.getString("cm_unauth_flg"));
					ackobj.put("cm_pwr_pur_eff_frm_dt", accountsRS.getString("cm_pwr_pur_eff_frm_dt"));
					ackobj.put("cm_ind_eff_frm_dt", accountsRS.getString("cm_ind_eff_frm_dt"));
					ackobj.put("cm_conn_typ", accountsRS.getString("cm_conn_typ"));
					ackobj.put("cm_conn_typ_descr", accountsRS.getString("cm_conn_typ_descr"));
					ackobj.put("cm_ntr_loc_cd", accountsRS.getString("cm_ntr_loc_cd"));
					ackobj.put("cm_bjkj_outlet", accountsRS.getString("cm_bjkj_outlet"));
					ackobj.put("cm_min_demand_entl", accountsRS.getString("cm_min_demand_entl"));
					ackobj.put("cm_bulk_no_of_houses", accountsRS.getString("cm_bulk_no_of_houses"));
					ackobj.put("cm_ivrs_id", accountsRS.getString("cm_ivrs_id"));
					ackobj.put("cm_user", accountsRS.getString("cm_user"));
					ackobj.put("cm_tmpstp", accountsRS.getString("cm_tmpstp"));
					ackobj.put("cm_version", accountsRS.getString("cm_version"));
					ackobj.put("cd_rr_no", accountsRS.getString("cd_rr_no"));
					ackobj.put("cd_lang_cd", accountsRS.getString("cd_lang_cd"));
					ackobj.put("cd_consmr_name", accountsRS.getString("cd_consmr_name"));
					ackobj.put("cd_premise_addr1", accountsRS.getString("cd_premise_addr1"));
					ackobj.put("cd_premise_addr2", accountsRS.getString("cd_premise_addr2"));
					ackobj.put("cd_premise_addr3", accountsRS.getString("cd_premise_addr3"));
					//ackobj.put("cd_premise_addr4", accountsRS.getString("cd_premise_addr4"));
					ackobj.put("cd_corres_addr1", accountsRS.getString("cd_corres_addr1"));
					ackobj.put("cd_corres_addr2", accountsRS.getString("cd_corres_addr2"));
					ackobj.put("cd_corres_addr3", accountsRS.getString("cd_corres_addr3"));
					ackobj.put("cd_corres_addr4", accountsRS.getString("cd_corres_addr4"));
					ackobj.put("cd_place_name", accountsRS.getString("cd_place_name"));
					//ackobj.put("cd_user", accountsRS.getString("cd_user"));
					//ackobj.put("cd_tmpstp", accountsRS.getString("cd_tmpstp"));
					ackobj.put("cd_version", accountsRS.getString("cd_version"));
					ackobj.put("cm_submeter_flg", accountsRS.getString("cm_submeter_flg"));
					ackobj.put("cm_email_id", accountsRS.getString("cm_email_id"));
					ackobj.put("cm_cst_no", accountsRS.getString("cm_cst_no"));
					ackobj.put("cm_tin_no", accountsRS.getString("cm_tin_no"));
					ackobj.put("cm_kst_no", accountsRS.getString("cm_kst_no"));
					ackobj.put("cm_slum_flg", accountsRS.getString("cm_slum_flg"));
					ackobj.put("sf_folio_no", accountsRS.getString("sf_folio_no"));
					ackobj.put("cm_first_bill_dc_flg", accountsRS.getString("cm_first_bill_dc_flg"));
					ackobj.put("cm_beg_mnth", accountsRS.getString("cm_beg_mnth"));
					ackobj.put("cm_beg_mnth_descr", accountsRS.getString("cm_beg_mnth_descr"));
					ackobj.put("cb_bill_dt", accountsRS.getString("cb_bill_dt"));
					ackobj.put("cb_bill_no", accountsRS.getString("cb_bill_no"));

					array.add(ackobj);
					
				}
				if(array.isEmpty()) {
					//no tasks for user
					obj.put("status", "success");
					obj.put("message", "No Records Found !!!");
				} else{
					obj.put("status", "success");
					obj.put("message", "Customer History Details Retrieved !!!");
					obj.put("customer_history", array);
				}
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter atleast one of RR Number OR Bill Number or Bill Date");
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
	
	@Override
	public JSONObject getCustomerSearchDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = object.getString("conn_type");
		
		try {
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			accountsCS = dbConnection.prepareCall(DBQueries.GET_CUSTOMER_SEARCH_DETAILS);
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("cs_id", accountsRS.getString("cs_id"));
				ackobj.put("cs_descr", accountsRS.getString("cs_descr"));
				ackobj.put("cs_type", accountsRS.getString("cs_type"));
				ackobj.put("key", accountsRS.getString("cs_id"));
				ackobj.put("value", accountsRS.getString("cs_descr"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("custimer_search_list", array);
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

	@Override
	public JSONObject getCustomerDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			//dbConnection = DatabaseImpl.GetLTSQLConnection();
			dbConnection = databaseObj.getDatabaseConnection();
			accountsCS=dbConnection.prepareCall(DBQueries.GET_CUSTOMER_DETIALS);
			accountsCS.setString(1, object.getString("type"));
			accountsCS.setString(2, object.getString("location_code"));
			accountsCS.setString(3, object.getString("rr_number"));
			accountsCS.setString(4, object.getString("mr_code"));
			accountsCS.setString(5, object.getString("reading_day"));
			accountsCS.setString(6, object.getString("spot_folio_number"));
			accountsCS.setString(7, object.getString("station_code"));
			accountsCS.setString(8, object.getString("feeder_code"));
			accountsCS.setString(9, object.getString("om_code"));
			accountsCS.setString(10, object.getString("dtc_code"));
			accountsCS.registerOutParameter(11, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(11);
			if(object.getString("type").equalsIgnoreCase("CNT")){
				if(accountsRS.next()){
					obj.put("count", accountsRS.getString("cnt"));
					obj.put("message","Count : "+ accountsRS.getString("cnt"));
					obj.put("status", "success");
				}else{
					obj.put("status", "error");
					obj.put("message", "Not able to find count.");
				}
			}else{
				while(accountsRS.next()){
					JSONObject ackobj=new JSONObject();
					ackobj.put("row_num", accountsRS.getString("row_num"));
					ackobj.put("rr_no", accountsRS.getString("rr_no"));
					ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
					ackobj.put("om_cd", accountsRS.getString("om_cd"));
					ackobj.put("mr_cd", accountsRS.getString("mr_cd"));
					ackobj.put("rdg_day", accountsRS.getString("rdg_day"));
					ackobj.put("sf_no", accountsRS.getString("sf_no"));
					ackobj.put("stn_no", accountsRS.getString("stn_no"));
					ackobj.put("fdr_no", accountsRS.getString("fdr_no"));
					ackobj.put("dtc_no", accountsRS.getString("dtc_no"));
					ackobj.put("pole_no", accountsRS.getString("pole_no"));
					ackobj.put("ld_sanct_kw", accountsRS.getString("ld_sanct_kw"));
					ackobj.put("ld_sanct_hp", accountsRS.getString("ld_sanct_hp"));
					ackobj.put("rr_sts", accountsRS.getString("rr_sts"));
					ackobj.put("userid", accountsRS.getString("userid"));
					ackobj.put("tmpstp", accountsRS.getString("tmpstp"));
	
					array.add(ackobj);
					
				}
				if(array.isEmpty()) {
					//no tasks for user
					obj.put("status", "success");
					obj.put("message", "No Records Found !!!");
				} else{
					obj.put("status", "success");
					obj.put("customer_details", array);
				}
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
	
	@Override
	public JSONObject getCustomerRegionDetails(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type =object.getString("conn_type");
		
		try {

			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			accountsCS=dbConnection.prepareCall(DBQueries.GET_CUSTOMER_REGION_DETAILS);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("om_code"));
			accountsCS.setString(3, object.getString("mr_code"));
			accountsCS.setString(4, object.getString("rgd_day"));
			accountsCS.registerOutParameter(5, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(5);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("om_cd", accountsRS.getString("om_cd"));
				ackobj.put("om_name", accountsRS.getString("om_name"));
				ackobj.put("mr_cd", accountsRS.getString("mr_cd"));
				ackobj.put("rdg_day", accountsRS.getString("rdg_day"));
				ackobj.put("sp_folio", accountsRS.getString("sp_folio"));
				ackobj.put("region_cd", accountsRS.getString("region_cd"));
				ackobj.put("region_cd_description", accountsRS.getString("region_cd_description"));


				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Customer Region Details Retrieved !!!");
				obj.put("customer_region_details", array);
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
	
	@Override
	public JSONObject customerRegionMapping(JSONObject data) {
		
		String userid = (String) data.get("userid");
		
		String conn_type = (String) data.get("conn_type");
		
		String region_code = (String) data.get("region_code");
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("data");
		    	
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		AckObj = customerIndividualRegionMapping(objects, userid, conn_type, region_code);
			    }
		    	return AckObj;
			}
		} catch (Exception e) {
			AckObj.put("status", "error");
			e.printStackTrace();
			AckObj.put("message", "Insert failed");
		}
		
		return AckObj;
	}
	
	
	public JSONObject customerIndividualRegionMapping(JSONObject object, String userid, String conn_type, String region_code) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		try {
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			accountsCS=dbConnection.prepareCall(DBQueries.CUSTOMER_REGION_MAPPING);
			accountsCS.setString(1, object.getString("full_rr_no"));
			accountsCS.setString(2, region_code);
			accountsCS.setString(3, userid);
			
			accountsCS.registerOutParameter(4, OracleTypes.CURSOR);
			
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(4);
			
			if(accountsRS.next()){
				String RESP = accountsRS.getString("RESP");
				System.out.println("RESP : "+RESP);
				
				if(RESP.equalsIgnoreCase("success")){
					obj.put("status", "success");
					obj.put("message", object.getString("region_cd_description") +" Region Mapped Successfully for the RR Number "+object.getString("rr_no"));
				}else if (RESP.equalsIgnoreCase("fail")) {
					obj.put("status", "error");
					obj.put("message",object.getString("region_cd_description") +" Region Mapping failed for the RR Number "+object.getString("rr_no"));
				}
				
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
	
	@Override
	public JSONObject getCustomerChangeTypes(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = object.getString("conn_type");
		
		try {
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			accountsCS=dbConnection.prepareCall(DBQueries.GET_CUSTOMER_CHANGE_TYPES);
			accountsCS.registerOutParameter(1, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(1);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("key", accountsRS.getString("change_id"));
				ackobj.put("value", accountsRS.getString("change_name"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("customer_change_types", array);
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
	
	@Override
	public JSONObject getCustomerDetailsForUpdate(JSONObject object) {
			
			CallableStatement accountsCS = null;
			ResultSet accountsRS = null;
			JSONObject obj = new JSONObject();
			
			String conn_type = (String) object.get("conn_type");
			String change_type = object.getString("changeType");
			
			try {
				if(!change_type.isEmpty() ){
					
					if(conn_type.equalsIgnoreCase("LT")){
						dbConnection = databaseObj.getDatabaseConnection();
					}else if(conn_type.equals("HT")){
						dbConnection = databaseObj.getHTDatabaseConnection();
					}
					accountsCS=dbConnection.prepareCall(DBQueries.GET_CUSTOMER_DETLS_FOR_UPDATE);
					accountsCS.setString(1, object.getString("rr_number"));
					accountsCS.registerOutParameter(2, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					accountsRS = (ResultSet) accountsCS.getObject(2);
					
					JSONObject ackobj=new JSONObject();
					if(accountsRS.next()){
						
						if (change_type.equals("1") ){
							ackobj.put("row_num", accountsRS.getString("row_num"));
							ackobj.put("rr_no", accountsRS.getString("rr_no"));
							ackobj.put("ld_sanct_hp", accountsRS.getString("ld_sanct_hp"));
							ackobj.put("ld_sanct_kw", accountsRS.getString("ld_sanct_kw"));
							ackobj.put("ld_sanct_kva", accountsRS.getString("ld_sanct_kva"));
						}else if (change_type.equals("2") ){
							ackobj.put("row_num", accountsRS.getString("row_num"));
							ackobj.put("rr_no", accountsRS.getString("rr_no"));
							ackobj.put("trf_catg", accountsRS.getString("trf_catg"));
							ackobj.put("trf_catg_description", accountsRS.getString("trf_catg_description"));
						}else if (change_type.equals("3") ){
							ackobj.put("row_num", accountsRS.getString("row_num"));
							ackobj.put("rr_no", accountsRS.getString("rr_no"));
							ackobj.put("customer_name", accountsRS.getString("customer_name"));
						}else if (change_type.equals("4") ){
							ackobj.put("row_num", accountsRS.getString("row_num"));
							ackobj.put("rr_no", accountsRS.getString("rr_no"));
							ackobj.put("customer_sts", accountsRS.getString("customer_sts"));
							ackobj.put("customer_sts_description", accountsRS.getString("customer_sts_description"));
						}else if (change_type.equals("5") ){
							ackobj.put("row_num", accountsRS.getString("row_num"));
							ackobj.put("rr_no", accountsRS.getString("rr_no"));
							ackobj.put("premise_addr1", accountsRS.getString("premise_addr1"));
							ackobj.put("premise_addr2", accountsRS.getString("premise_addr2"));
							ackobj.put("premise_addr3", accountsRS.getString("premise_addr3"));
						}else if (change_type.equals("6") ){
							ackobj.put("row_num", accountsRS.getString("row_num"));
							ackobj.put("rr_no", accountsRS.getString("rr_no"));
							ackobj.put("corres_addr1", accountsRS.getString("corres_addr1"));
							ackobj.put("corres_addr2", accountsRS.getString("corres_addr2"));
							ackobj.put("corres_addr3", accountsRS.getString("corres_addr3"));
						}
	
					}if(ackobj.isEmpty()) {
						//no tasks for user
						obj.put("status", "success");
						obj.put("message","RR Number "+ object.getString("rr_number").substring(7)+"  Not Found in Database!!!");
					} else{
						obj.put("status", "success");
						obj.put("customer_information", ackobj);
					}
				}else{
					obj.put("status", "error");
					obj.put("message", "Please Enter Proper Input");
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
	
	@Override
	public JSONObject insertCustomerChangesInformation(JSONObject object) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		JSONObject dataObj = new JSONObject();
		
		dataObj=(JSONObject) object.get("data");
		
		String rr_no = (String) dataObj.get("rr_no");
		String conn_type = (String) object.get("conn_type");
		String change_type = (String) object.get("changeType");
		
		System.out.println(object.get("data"));
		
		try {
			if(!rr_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
					accountsCS=dbConnection.prepareCall(DBQueries.INSERT_CUSTOMER_CHANGES);
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, change_type);
					
					if (change_type.equals("1")){
						
						accountsCS.setString(3, (String) dataObj.get("ld_sanct_hp"));
						accountsCS.setString(4, (String) dataObj.get("ld_sanct_kw"));
						accountsCS.setString(5, (String) dataObj.get("ld_sanct_kva"));
						accountsCS.setString(6, (String) dataObj.get("LoadHP"));
						accountsCS.setString(7, (String) dataObj.get("LoadKW"));
						accountsCS.setString(8, (String) dataObj.get("LoadKVA"));
					}else if (change_type.equals("2")){
						
						accountsCS.setString(3, (String) dataObj.get("trf_catg"));
						accountsCS.setString(4, "");
						accountsCS.setString(5, "");
						accountsCS.setString(6, (String) dataObj.get("trfCatg"));
						accountsCS.setString(7, "");
						accountsCS.setString(8, "");
						
					}else if (change_type.equals("3")){
						
						accountsCS.setString(3, (String) dataObj.get("customer_name"));
						accountsCS.setString(4, "");
						accountsCS.setString(5, "");
						accountsCS.setString(6, (String) dataObj.get("customerName"));
						accountsCS.setString(7, "");
						accountsCS.setString(8, "");
						
					}else if (change_type.equals("4")){
						
						accountsCS.setString(3, (String) dataObj.get("customer_sts"));
						accountsCS.setString(4, "");
						accountsCS.setString(5, "");
						accountsCS.setString(6, (String) dataObj.get("customerStatus"));
						accountsCS.setString(7, "");
						accountsCS.setString(8, "");
						
					}else if (change_type.equals("5")){
						
						accountsCS.setString(3, (String) dataObj.get("premise_addr1"));
						accountsCS.setString(4, (String) dataObj.get("premise_addr2"));
						accountsCS.setString(5, (String) dataObj.get("premise_addr3"));
						accountsCS.setString(6, (String) dataObj.get("pemisesAddess"));
						accountsCS.setString(7, (String) dataObj.get("pemisesAddess1"));
						accountsCS.setString(8, (String) dataObj.get("pemisesAddess2"));
						
					}else if (change_type.equals("6")){
						
						accountsCS.setString(3, (String) dataObj.get("corres_addr1"));
						accountsCS.setString(4, (String) dataObj.get("corres_addr2"));
						accountsCS.setString(5, (String) dataObj.get("corres_addr3"));
						accountsCS.setString(6, (String) dataObj.get("correspondingAddress"));
						accountsCS.setString(7, (String) dataObj.get("correspondingAddress1"));
						accountsCS.setString(8, (String) dataObj.get("correspondingAddress2"));
					}
					
					accountsCS.setString(9, (String) dataObj.get("remarks"));
					accountsCS.setString(10, (String) object.get("userid"));
					
					accountsCS.registerOutParameter(11, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					
					accountsRS = (ResultSet) accountsCS.getObject(11);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("PROCESS_STS")){
							obj.put("status", "error");
							obj.put("message", "Enter RR Number is Under "+accountsRS.getString("BILL_STS")+ " Process..!");
						}else if(RESP.equalsIgnoreCase("VERIFY_STS")){
							obj.put("status", "error");
							obj.put("message", "Enter Details is Alreday Under "+accountsRS.getString("CHANGE_STS")+ " for this RR Number "+rr_no.substring(7));
						}else if(RESP.equalsIgnoreCase("APPROVE_STS")){
							obj.put("status", "error");
							obj.put("message", "Enter Details is Alreday Under "+accountsRS.getString("CHANGE_STS")+ " for this RR Number "+rr_no.substring(7));
						}else if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Customer Changes Inserted Successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Customer Changes Insertion Faild....!");
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
	
	@Override
	public JSONObject getCustomerDetailsForVerify(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type =object.getString("conn_type");
		
		try {
			
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			
			accountsCS=dbConnection.prepareCall(DBQueries.GET_CUSTOMER_DETLS_FOR_VERIFY);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("change_type"));
			accountsCS.registerOutParameter(3, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(3);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("change_type", accountsRS.getString("change_type"));
				ackobj.put("old_value", accountsRS.getString("old_value"));
				ackobj.put("old_value_description", accountsRS.getString("old_value_description"));
				ackobj.put("new_value", accountsRS.getString("new_value"));
				ackobj.put("new_value_description", accountsRS.getString("new_value_description"));
				ackobj.put("change_id", accountsRS.getString("change_id"));
				ackobj.put("change_no", accountsRS.getString("change_no"));
				ackobj.put("remarks", accountsRS.getString("remarks"));
				ackobj.put("inserted_userid", accountsRS.getString("inserted_userid"));
				ackobj.put("inserted_tmpstp", accountsRS.getString("inserted_tmpstp"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Customer Updation Details Retrieved for Verify!!!");
				obj.put("customer_details_verify", array);
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
	
	@Override
	public JSONObject verifyRejectCustomerChanges(JSONObject data) {
		
		String verifierid = (String) data.get("verifierid");
		
		String conn_type = (String) data.get("conn_type");
		
		String option = (String) data.get("option");
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("data");
		    	
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		AckObj = verifyRejectIndividualCustomerChanges(objects, verifierid, conn_type, option);
			    }
		    	return AckObj;
			}
		} catch (Exception e) {
			AckObj.put("status", "error");
			e.printStackTrace();
			AckObj.put("message", "Insert failed");
		}
		
		return AckObj;
	}
	
	
	public JSONObject verifyRejectIndividualCustomerChanges(JSONObject object, String verifierid, String conn_type, String option) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("full_rr_no");
		
		try {
			if(!rr_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					accountsCS=dbConnection.prepareCall(DBQueries.VERIFY_REJECT_CUSTOMER_CHANGES);
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, option);
					accountsCS.setString(3, object.getString("change_id"));
					accountsCS.setString(4, object.getString("change_no"));
					accountsCS.setString(5, object.getString("new_value"));
					accountsCS.setString(6, verifierid);
					
					accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					accountsRS = (ResultSet) accountsCS.getObject(7);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Customer Details "+(option.equals("VERIFY") ? "  Verified " : "  Rejected")+" Successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Customer Details "+(option.equals("VERIFY") ? "  Verified " : "  Rejected")+" failed..");
						}
						
					}
														
			}else{
				obj.put("status", "error");
				obj.put("message", "Invalid input...!");
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
	
	@Override
	public JSONObject getCustomerDetailsForApprove(JSONObject object) {
		
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type =object.getString("conn_type");

		try {

			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			accountsCS=dbConnection.prepareCall(DBQueries.GET_CUSTOMER_DETLS_FOR_APPROVE);
			accountsCS.setString(1, object.getString("location_code"));
			accountsCS.setString(2, object.getString("change_type"));
			accountsCS.registerOutParameter(3, OracleTypes.CURSOR);
			accountsCS.executeUpdate();
			accountsRS = (ResultSet) accountsCS.getObject(3);
			
			while(accountsRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", accountsRS.getString("row_num"));
				ackobj.put("rr_no", accountsRS.getString("rr_no"));
				ackobj.put("full_rr_no", accountsRS.getString("full_rr_no"));
				ackobj.put("change_type", accountsRS.getString("change_type"));
				ackobj.put("old_value", accountsRS.getString("old_value"));
				ackobj.put("old_value_description", accountsRS.getString("old_value_description"));
				ackobj.put("new_value", accountsRS.getString("new_value"));
				ackobj.put("new_value_description", accountsRS.getString("new_value_description"));
				ackobj.put("change_id", accountsRS.getString("change_id"));
				ackobj.put("change_no", accountsRS.getString("change_no"));
				ackobj.put("remarks", accountsRS.getString("remarks"));
				ackobj.put("inserted_userid", accountsRS.getString("inserted_userid"));
				ackobj.put("inserted_tmpstp", accountsRS.getString("inserted_tmpstp"));
				ackobj.put("verified_userid", accountsRS.getString("verified_userid"));
				ackobj.put("verified_tmpstp", accountsRS.getString("verified_tmpstp"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "Customer Verified Details Retrieved for Approval!!!");
				obj.put("customer_details_approve", array);
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
	
	@Override
	public JSONObject approveRejectCustomerChanges(JSONObject data) {
		
		String approverid = (String) data.get("approverid");
		
		String conn_type = (String) data.get("conn_type");
		
		String option = (String) data.get("option");
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("data");
		    	
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		AckObj = approveRejectIndividualCustomerChanges(objects, approverid, conn_type, option);
			    }
		    	return AckObj;
			}
		} catch (Exception e) {
			AckObj.put("status", "error");
			e.printStackTrace();
			AckObj.put("message", "Insert failed");
		}
		
		return AckObj;
	}
	
	
	public JSONObject approveRejectIndividualCustomerChanges(JSONObject object,String approverid,String conn_type,String option) {
		CallableStatement accountsCS = null;
		ResultSet accountsRS = null;
		JSONObject obj = new JSONObject();
		
		String rr_no = (String) object.get("full_rr_no");
		
		System.out.println(object);
		
		try {
			if(!rr_no.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					accountsCS=dbConnection.prepareCall(DBQueries.APPROVE_REJECT_CUSTOMER_CHANGE);
					accountsCS.setString(1, rr_no);
					accountsCS.setString(2, option);
					accountsCS.setString(3, object.getString("change_id"));
					accountsCS.setString(4, object.getString("change_no"));
					accountsCS.setString(5, object.getString("new_value"));
					accountsCS.setString(6, approverid);
					
					accountsCS.registerOutParameter(7, OracleTypes.CURSOR);
					accountsCS.executeUpdate();
					accountsRS = (ResultSet) accountsCS.getObject(7);
					
					if(accountsRS.next()){
						String RESP = accountsRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Customer Details "+(option.equals("APPROVE") ? "  Approved " : "  Rejected")+" Successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Customer Details "+(option.equals("APPROVE") ? "  Approved " : "  Rejected")+" failed..");
						}
						
					}
														
			}else{
				obj.put("status", "error");
				obj.put("message", "Invalid input...!");
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

	@Override
	public JSONObject getSearchCriteriaList(JSONObject object) {
		// TODO Auto-generated method stub

		PreparedStatement ps = null;
		ResultSet rs =  null;
		JSONObject json = null;
		JSONArray array = new JSONArray();
		JSONObject JSON_RESPONSE = new JSONObject();
		
		try {
			
			if(!object.isEmpty()){
				
				String Conn_Type = (String)object.get("Conn_Type");
				
				if(Conn_Type.equals("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(Conn_Type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				String Query = "SELECT CS_COL_NAME, CS_DESCR FROM CUST_SEARCH "
						+ " WHERE NVL(CS_STATUS,'Y') = 'Y'"
						+ " ORDER BY CS_ID";
				
				ps = dbConnection.prepareStatement(Query);
				rs = ps.executeQuery();
				
				while(rs.next()) {
					
					json = new JSONObject();
					
					json.put("key", rs.getString("CS_COL_NAME"));
					json.put("value", rs.getString("CS_DESCR"));
					
					array.add(json);
					
					//System.out.println(array);
				}
				
				if(array.isEmpty()) {
					JSON_RESPONSE.put("status", "error");
					JSON_RESPONSE.put("message", "No Search Criteria List Exists !!!");
				}else {
					JSON_RESPONSE.put("status", "success");
					JSON_RESPONSE.put("SEARCH_CRITERIA_LIST", array);
				}
			}else{
				JSON_RESPONSE.put("status", "error");
				JSON_RESPONSE.put("message", "Sorry ! Invalid Inputs.");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			JSON_RESPONSE.put("status", "fail");
			JSON_RESPONSE.put("message", "Database Error .");
		}finally {
			DBManagerResourceRelease.close(rs, ps);
		}
		return JSON_RESPONSE;
	}

	@Override
	public JSONObject fetchSearchtypeValues(JSONObject object) {
		// TODO Auto-generated method stub
		PreparedStatement ps = null;
		ResultSet rs =  null;
		JSONObject json = null;
		JSONArray array = new JSONArray();
		JSONObject JSON_RESPONSE = new JSONObject();
		IPreLoadPickList pickListObj = new PreloadPicklistImpl();
		
		try {
			
			if(!object.isEmpty()){
				
				String location_code = (String) object.get("LOCATION_CODE").toString().trim();
				String Conn_Type = (String) object.get("CONN_TYPE").toString().trim();
				String key = (String)object.get("key");
				
				//key = "CM_MTR_RDR_CD";
				
				object.put("Location_Code", location_code);
				object.put("Conn_Type", Conn_Type);
				
				if(Conn_Type.equals("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(Conn_Type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
				String Query = "  SELECT CS_TYPE FROM CUST_SEARCH  "
						+ " WHERE CS_COL_NAME =  '"+key+"'" ;
				
				System.out.println(Query);
				
				ps = dbConnection.prepareStatement(Query);
				rs = ps.executeQuery();
				
				if(rs.next()) {
					
					if(rs.getString("CS_TYPE").equals("STRING")) {
						JSON_RESPONSE.put("type",rs.getString("CS_TYPE"));
						
					}else if(rs.getString("CS_TYPE").equals("OPTIONS")) {
						JSON_RESPONSE.put("type",rs.getString("CS_TYPE"));
						
						if(key.equals("CM_MTR_RDR_CD")) {
							JSON_RESPONSE.put("list",pickListObj.getMeterReaderCodeList(object).getJSONArray("METER_READER_CODE"));
						}
						
						if(key.equals("CM_OM_UNIT_CD")) {
							JSON_RESPONSE.put("list",pickListObj.getOMCode(object));
						}
						
						if(key.equals("CM_STATION_NO")) {
							JSON_RESPONSE.put("list",pickListObj.getStationList(object));
						}
						
						if(key.equals("CM_TRSFMR_NO")) {
							JSON_RESPONSE.put("list",pickListObj.getTransformerCodeList(object).getJSONArray("TRANSFORMER_LIST"));
						}
						
						if(key.equals("CM_CONSMR_STS")) {
							JSON_RESPONSE.put("list",pickListObj.getCustomerStatus(object));
						}
						
						if(key.equals("CM_MTR_RDG_DAY")) {
							JSON_RESPONSE.put("list",pickListObj.getMeterReadingDayList(object).getJSONArray("METER_READING_DAY"));
						}
						
						if(key.equals("CM_TRF_CATG")) {
							JSON_RESPONSE.put("list",pickListObj.getTariffList(object).getJSONArray("TARIFF_DESCR_LIST"));
						}
						
						if(key.equals("CM_FDR_NO")) {
							JSON_RESPONSE.put("list",pickListObj.getFeederList(object).getJSONArray("FEEDER_LIST"));
						}
						
						
					}else if(rs.getString("CS_TYPE").equals("DATE")) {
						JSON_RESPONSE.put("type",rs.getString("CS_TYPE"));
						
					}
					
					JSON_RESPONSE.put("status","success");
				}else{
					JSON_RESPONSE.put("status", "error");
					JSON_RESPONSE.put("message", "No Details Found.");
				}
				
			}else{
				JSON_RESPONSE.put("status", "error");
				JSON_RESPONSE.put("message", "Sorry ! Invalid Inputs.");
			}
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
			JSON_RESPONSE.put("status", "fail");
			JSON_RESPONSE.put("message", "Database Error .");
		}finally {
			DBManagerResourceRelease.close(rs, ps);
		}
		return JSON_RESPONSE;
	}
	

}
