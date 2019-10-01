package com.savbill.user;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import oracle.jdbc.OracleTypes;

import com.savbill.database.DBManagerIMPL;
import com.savbill.database.DBManagerResourceRelease;
import com.savbill.database.DBQueries;
import com.savbill.util.EncriptAndDecript;

public class UserManagementImpl implements IUserManagement{
	
	// database connection init
	static DBManagerIMPL databaseObj = new DBManagerIMPL();
	static Connection dbConnection;
	
	@Override
	public JSONObject validateUserID(JSONObject object) {
		
		CallableStatement validateUserIDCS = null;
		ResultSet validateUserIDRS = null;
		
		JSONObject AckObj = new JSONObject();
		
		String user_id = object.getString("userID");
		System.out.println(user_id);
		
		try {
			if(user_id.isEmpty()) {
				AckObj.put("status", "error");					
				AckObj.put("message", "Please enter User ID");	
			}else{
							
				dbConnection=databaseObj.getDatabaseConnection();
				
				//1.check for user ID
				validateUserIDCS = dbConnection.prepareCall(DBQueries.VERIFY_USER_ID);
				validateUserIDCS.setString(1,user_id);
				validateUserIDCS.registerOutParameter(2, OracleTypes.CURSOR);
				validateUserIDCS.executeUpdate();
				validateUserIDRS = (ResultSet) validateUserIDCS.getObject(2);
				
				while(validateUserIDRS.next()){
					JSONObject json = new JSONObject();
					String resp = validateUserIDRS.getString("resp");
					
					if(resp.equalsIgnoreCase("success")){
						json.put("user_id", validateUserIDRS.getString("user_id"));
						json.put("name", validateUserIDRS.getString("user_name"));
						json.put("actual_role", validateUserIDRS.getString("user_actual_role"));
						json.put("role", validateUserIDRS.getString("user_role"));
						json.put("location_code", validateUserIDRS.getString("user_location_code"));
						json.put("subDivision", validateUserIDRS.getString("user_sub_division"));
						json.put("division", validateUserIDRS.getString("user_division"));
						json.put("circle", validateUserIDRS.getString("user_circle"));
						json.put("zone", validateUserIDRS.getString("user_zone"));
						json.put("company", validateUserIDRS.getString("user_company"));
						json.put("delegated_user_id", validateUserIDRS.getString("delegate_from_user_id"));
						json.put("delegated_user_name", validateUserIDRS.getString("delegate_from_user_name"));
						json.put("delegate_from_date", validateUserIDRS.getString("delegate_from_date"));
						json.put("delegate_to_date", validateUserIDRS.getString("delegate_to_date"));
						json.put("connection_type", validateUserIDRS.getString("setup"));
						json.put("delegated_to_user", validateUserIDRS.getString("delegated_to_user"));
						AckObj.put("status", "success");
						AckObj.put("user_details", json);
						AckObj.put("message", "UserId exists, proceed to further steps");
					}
					else if(resp.equalsIgnoreCase("success_delegated")) {
						json.put("user_id", validateUserIDRS.getString("user_id"));
						json.put("name", validateUserIDRS.getString("user_name"));
						json.put("actual_role", validateUserIDRS.getString("user_actual_role"));
						json.put("role", validateUserIDRS.getString("user_role"));
						json.put("location_code", validateUserIDRS.getString("user_location_code"));
						json.put("subDivision", validateUserIDRS.getString("user_sub_division"));
						json.put("division", validateUserIDRS.getString("user_division"));
						json.put("circle", validateUserIDRS.getString("user_circle"));
						json.put("zone", validateUserIDRS.getString("user_zone"));
						json.put("company", validateUserIDRS.getString("user_company"));
						json.put("delegated_user_id", validateUserIDRS.getString("delegate_from_user_id"));
						json.put("delegated_user_name", validateUserIDRS.getString("delegate_from_user_name"));
						json.put("delegate_from_date", validateUserIDRS.getString("delegate_from_date"));
						json.put("delegate_to_date", validateUserIDRS.getString("delegate_to_date"));
						json.put("connection_type", validateUserIDRS.getString("setup"));
						json.put("delegated_to_user", validateUserIDRS.getString("delegated_to_user"));
						AckObj.put("status", "error");
						AckObj.put("user_details", json);
						AckObj.put("message", "Cannot Proceed. User Role is Delegated to "+validateUserIDRS.getString("delegated_to_user"));
					}
					else{
						AckObj.put("status", "error");
						AckObj.put("message", "Invalid User ID, Please enter a valid UserId.");
					}
				}
			}
			} catch (Exception e) {
				AckObj.put("status", "failure");
				e.printStackTrace();
				AckObj.put("message", "database not connected");
			}finally{
				
				DBManagerResourceRelease.close(validateUserIDRS, validateUserIDCS);
			}
		
		return AckObj;
				
	}
	
	@Override
	public JSONObject authenticateUser(JSONObject object, String ipAdress) {
		
		CallableStatement chkLoginUsersCS = null;
		ResultSet chkLoginUsersRS = null;
		JSONObject AckObj = new JSONObject();
				
		String encryptedpassword = null;
		
		JSONObject jsonLogin = new  JSONObject();
		
		String username = (String) object.get("userID");
		String password = (String) object.get("password");
		String ipAddress = ipAdress;
		String locationCode = (String) object.get("locationCode");
		String userRole = (String) object.get("userRole");
		String connType = (String) object.get("connType");
		String macAddress = ipAdress;
		
		try {
			if(username.isEmpty() ||password.isEmpty()){
				AckObj.put("status", "error");					
				AckObj.put("message", "Please enter Username and Password");	
			}else{
				encryptedpassword = EncriptAndDecript.encrypt(password);
				
				//dbConnection = DatabaseImpl.GetLTSQLConnection();
				if(connType.equals("LT")){
					dbConnection=databaseObj.getDatabaseConnection();
				}else if(connType.equals("HT")){
					dbConnection=databaseObj.getHTDatabaseConnection();
				}
				
				chkLoginUsersCS = dbConnection.prepareCall(DBQueries.AUTHENTICATE_USER);
				chkLoginUsersCS.setString(1,username);
				chkLoginUsersCS.setString(2,encryptedpassword);
				chkLoginUsersCS.registerOutParameter(3, OracleTypes.CURSOR);
				chkLoginUsersCS.executeUpdate();
				chkLoginUsersRS = (ResultSet) chkLoginUsersCS.getObject(3);
				if(chkLoginUsersRS.next()) {
					String resp = chkLoginUsersRS.getString("resp");
					
					if(resp.equalsIgnoreCase("success")){
						AckObj.put("Username", username);
						AckObj.put("status", "success");
						AckObj.put("menus", getUserMenus(userRole,connType));
						AckObj.put("sav_menus", getSAV_menus(userRole,connType));
						AckObj.put("message", "User Authentication Succes.");
						
						jsonLogin.put("Username", username);
						jsonLogin.put("LogoutStatus", "N");
						jsonLogin.put("IPaddress", ipAddress);
						jsonLogin.put("LocationCode", locationCode);
						jsonLogin.put("MAC_Address", macAddress);
						
						AckObj.put("session_details",logUserSessionData(jsonLogin));
									
						return AckObj;
					} else {
						AckObj.put("status", "error");					
						AckObj.put("Username", username);
						AckObj.put("message", "Invalid Login Details");	
						return AckObj;
					}
				} else {
					AckObj.put("status", "error");					
					AckObj.put("Username", username);
					AckObj.put("message", "Invalid Login Details");	
					return AckObj;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AckObj.put("status", "failure");
			
			AckObj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(chkLoginUsersRS, chkLoginUsersCS);
		}
		
		return AckObj;
				
	}
	
	JSONArray getSAV_menus(String userrole,String conn_type) {	
		
	CallableStatement userCS = null;
	ResultSet userRS = null;
	JSONArray array = new JSONArray();
	JSONObject obj = new JSONObject();
	
	try {
		if(conn_type.equalsIgnoreCase("LT")){
			dbConnection = databaseObj.getDatabaseConnection();
		}else if(conn_type.equals("HT")){
			dbConnection = databaseObj.getHTDatabaseConnection();
		}
		userCS=dbConnection.prepareCall(DBQueries.GET_SAV_MENUS);
		userCS.registerOutParameter(1, OracleTypes.CURSOR);
		userCS.setString(2, userrole);
		userCS.executeUpdate();
		userRS = (ResultSet) userCS.getObject(1);
		
		while(userRS.next()){
			
			JSONObject ackobj=new JSONObject();
			
			ackobj.put("form_code", ConvertIFNullToString(userRS.getString("form_code")));
			ackobj.put("form_id", ConvertIFNullToString(userRS.getString("form_id")));
			ackobj.put("form_name", ConvertIFNullToString(userRS.getString("form_name")));
			ackobj.put("form_description", ConvertIFNullToString(userRS.getString("form_description")));
			ackobj.put("group_name", ConvertIFNullToString(userRS.getString("group_name")));
			ackobj.put("menu_type", ConvertIFNullToString(userRS.getString("menu_type")));
			ackobj.put("form_privileges", ConvertIFNullToString(userRS.getString("form_privileges")));

			array.add(ackobj);
			
		}
	} catch (Exception e) {
		obj.put("status", "fail");
		e.printStackTrace();
		obj.put("message", "database not connected");
	}finally
	{
		DBManagerResourceRelease.close(userRS, userCS);
	}
	
	return array;
	
	}
	
public static String ConvertIFNullToString(String value){
		
		return ((value == null || value == "") ? "" : value);
		
	}


	@Override
	public JSONObject getUserMenus(String userRole, String connType) {

		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<MenuStructureBO> MenuList = new ArrayList<MenuStructureBO>();
		MenuStructureBO menu = null;
		JSONObject json = new JSONObject();
		
		try{
			
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			//con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.159:1521:cloudbill", "bill","bill");
			
			//con = DatabaseImpl.GetLTSQLConnection();
			if(connType.equals("LT")){
				con = databaseObj.getDatabaseConnection();
			}else if(connType.equals("HT")){
				con = databaseObj.getHTDatabaseConnection();
			}
			
			//dbConnection=databaseObj.getDatabaseConnection();
			
			String query = " SELECT BFM_FORM_CD, DECODE (SUBSTR(BFM_FORM_CD, -1), '0', 'M', 'L') MNU," +
				       " BFM_FORM_NAME ,BFM_FORM_CD, BFP_PRIV " +
				       " from FORM_MASTER, FORM_PRIVILEGES " +
				       " WHERE BFM_FORM_CD = BFP_FORM_CD AND BFP_ROLE_CD ='"+userRole+"' AND BFP_PRIV !='0000' " +
				       " AND (BFM_FORM_CD like '_000000' or BFM_FORM_CD like '_000000') "
				       + "AND NVL(BFM_FORM_STS,'N') = 'Y' "+
				       " ORDER BY 1"; 
			
			//System.out.println(con);
			//System.out.println(query);
			
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			while(rs.next()){
				
				menu = new MenuStructureBO();
				
				String Form_code = rs.getString("BFM_FORM_CD");
				
				//System.out.println("main : "+Form_code);
				
				menu.setTitle(rs.getString("BFM_FORM_NAME"));
				menu.setName(rs.getString("BFM_FORM_CD"));
				menu.setPrevilage(rs.getString("BFP_PRIV"));

				menu.setSubMenuList(getSubMenus(Form_code,userRole,connType));
				
				MenuList.add(menu);
			}
			json.put("MENU", MenuList);
			
		}catch (Exception e) {
			e.printStackTrace();
		}finally{
			//DBManagerResourceRelease.close(rs);
			DBManagerResourceRelease.close(ps);
			//DatabaseImpl.CleanUp(con, ps1, rs1);
		}
		
		return json;
	}

	private static List<MenuStructureBO> getSubMenus(String FormCode, String userRole, String connType) {
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<MenuStructureBO> MenuList = new ArrayList<MenuStructureBO>();
		MenuStructureBO menu = null;
		
		try{
			
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			//con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.159:1521:cloudbill", "bill","bill");
			
			//con = DatabaseImpl.GetLTSQLConnection();
			//dbConnection=databaseObj.getDatabaseConnection();
			
/*			String query =" SELECT BFM_FORM_CD, DECODE (SUBSTR(BFM_FORM_CD, -1), '0', 'M', 'L') MNU," +
				       " BFM_FORM_NAME ,BFM_FORM_CD, BFP_PRIV" +
				       " from FORM_MASTER, FORM_PRIVILEGES " +
				       " WHERE BFM_FORM_CD = BFP_FORM_CD AND BFP_ROLE_CD ='"+userRole+"' AND BFP_PRIV !='0000' " +
				       " AND (BFM_FORM_CD like '"+FormCode.substring(0,6)+"%' or BFM_FORM_CD like '"+FormCode.substring(0,1)+"_00000')" +
				       		" AND BFM_FORM_CD <>  '"+FormCode+"' " +
				       " ORDER BY 1";  */
			
			if(connType.equals("LT")){
				con = databaseObj.getDatabaseConnection();
			}else if(connType.equals("HT")){
				con = databaseObj.getHTDatabaseConnection();
			}
			
			
			
			// query Changed by ravi on 22/12/2015
			String query =" SELECT BFM_FORM_CD, DECODE (SUBSTR(BFM_FORM_CD,2,1), '0', 'L', 'M') MNU," +
				       " BFM_FORM_NAME ,BFM_FORM_CD, BFP_PRIV" +
				       " from FORM_MASTER, FORM_PRIVILEGES " +
				       " WHERE BFM_FORM_CD = BFP_FORM_CD AND BFP_ROLE_CD ='"+userRole+"' AND BFP_PRIV !='0000' " +
				       " AND (BFM_FORM_CD like '"+FormCode.substring(0,5)+"%' or BFM_FORM_CD like '"+FormCode.substring(0,1)+"_00000')" +
				       " AND NVL(BFM_FORM_STS,'N') = 'Y' " +
				       		" AND BFM_FORM_CD <>  '"+FormCode+"' " +
				       " ORDER BY 1";  

			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			while(rs.next()){
				
				menu = new MenuStructureBO();
				
				String Form_code= rs.getString("BFM_FORM_CD");
				//System.out.println("sub : "+Form_code);
				menu.setTitle(rs.getString("BFM_FORM_NAME"));
				menu.setName(rs.getString("BFM_FORM_CD"));
				menu.setPrevilage(rs.getString("BFP_PRIV"));
				
				if(rs.getString("MNU").equals("M")){
					menu.setSubMenuList(GetMicroMenus(Form_code,userRole,connType));
				}
				MenuList.add(menu);
				
			}
			
		}catch (Exception e) {
			System.out.println("Error Occured : " + e.getMessage());
		}
		//DBManagerResourceRelease.close(rs);
		DBManagerResourceRelease.close(ps);
		//DatabaseImpl.CleanUp(con, ps, rs);
		return MenuList;
	}

	private static List<MenuStructureBO> GetMicroMenus(String FormCode, String userRole, String connType) {
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<MenuStructureBO> MenuList = new ArrayList<MenuStructureBO>();
		MenuStructureBO menu = null;
		
		try{
			
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			//con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.159:1521:cloudbill", "bill","bill");
			
			//con = DatabaseImpl.GetLTSQLConnection();
			//dbConnection=databaseObj.getDatabaseConnection();
			
/*			String query =" SELECT BFM_FORM_CD, DECODE (SUBSTR(BFM_FORM_CD, -1), '0', 'M', 'L') MNU," +
				       " BFM_FORM_NAME ,BFM_FORM_CD, BFP_PRIV " +
				       " from FORM_MASTER, FORM_PRIVILEGES " +
				       " WHERE BFM_FORM_CD = BFP_FORM_CD AND BFP_ROLE_CD ='"+userRole+"' AND BFP_PRIV !='0000' " +
 					   " AND (BFM_FORM_CD like '"+FormCode.substring(0,5)+"%' or BFM_FORM_CD like '"+FormCode.substring(0,2)+"_0000') " +
 					   " AND BFM_FORM_CD <>  '"+FormCode+"' " +
				       " ORDER BY 1"; */ 
			
			if(connType.equals("LT")){
				con = databaseObj.getDatabaseConnection();
			}else if(connType.equals("HT")){
				con = databaseObj.getHTDatabaseConnection();
			}
			
			// query Changed by ravi on 22/12/2015
			String query =" SELECT BFM_FORM_CD, DECODE (SUBSTR(BFM_FORM_CD,3,1), '0', 'L', 'M') MNU," +
				       " BFM_FORM_NAME ,BFM_FORM_CD, BFP_PRIV " +
				       " from FORM_MASTER, FORM_PRIVILEGES " +
				       " WHERE BFM_FORM_CD = BFP_FORM_CD AND BFP_ROLE_CD ='"+userRole+"' AND BFP_PRIV !='0000' " +
				       " AND NVL(BFM_FORM_STS,'N') = 'Y' " +
					   " AND (BFM_FORM_CD like '"+FormCode.substring(0,5)+"%' or BFM_FORM_CD like '"+FormCode.substring(0,2)+"_0000') " +
					   " AND BFM_FORM_CD <>  '"+FormCode+"' " +
				       " ORDER BY 1"; 
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			
			while(rs.next()){
				menu = new MenuStructureBO();
				
				String Form_code= rs.getString("BFM_FORM_CD");
				//System.out.println("micro : "+Form_code);
				
				menu.setTitle(rs.getString("BFM_FORM_NAME"));
				menu.setName(rs.getString("BFM_FORM_CD"));
				menu.setPrevilage(rs.getString("BFP_PRIV"));

				MenuList.add(menu);
				
			}
			
		}catch (Exception e) {
			e.printStackTrace();
			System.out.println("Error Occured : " + e.getMessage());
		}
		//DatabaseImpl.CleanUp(con, ps, rs);
		//DBManagerResourceRelease.close(rs);
		DBManagerResourceRelease.close(ps);
		return MenuList;
	}

	@Override
	public JSONObject logUserSessionData(JSONObject object) {

		PreparedStatement pr = null;
		PreparedStatement ps = null;
		JSONObject json = new JSONObject(); 
		ResultSet rs = null;
		int Session_Id = 0 ; 
		try{
			
			dbConnection = databaseObj.getDatabaseConnection();
			
			ps = dbConnection.prepareStatement(" select sq_sessionID.nextval session_id from dual");
			rs = ps.executeQuery();
			
			if(rs.next()){
				Session_Id = rs.getInt("session_id");
			}

			pr = dbConnection.prepareStatement("insert into CLOUDBILL_USR_SESSION   values("+Session_Id+",?,SYSDATE,SYSDATE,?,?,?,?)");
					 pr.setString(1,(String)object.get("Username"));
					 pr.setString(2,(String)object.get("IPaddress"));
					 pr.setString(3,(String)object.get("LogoutStatus"));
					 pr.setString(4,(String)object.get("LocationCode"));
					 pr.setString(5,(String)object.get("MAC_Address"));//CUS_MAC_ADDRESS
					 int i = pr.executeUpdate();
					 
					 if(i > 0){
						System.out.println("User Logged Succesfully");
						json.put("SESSION_ID", Session_Id);
						json.put("IP_ADDRESS", (String)object.get("IPaddress"));
						json.put("MAC_ADDRESS", (String)object.get("MAC_Address"));
					 }
					 
			 }catch(Exception e){
				 System.out.println("Exception thrown from logsessionUserData " +e); 
				 	json.put("SESSION_ID", "");
					json.put("IP_ADDRESS", (String)object.get("IPaddress"));
					json.put("MAC_ADDRESS", (String)object.get("MAC_Address"));
				 
			 }finally{
				 DBManagerResourceRelease.close(pr);
				 DBManagerResourceRelease.close(rs , ps);
				 
			 } 
			 
		return json;
	}
	
	@Override
	public JSONObject logOutSessionData(JSONObject object) {

		JSONObject jsonResponse = new JSONObject();
		
		String session_id = (String)object.get("SESSION_ID");
		PreparedStatement pr = null; 
		try{
			
			dbConnection = databaseObj.getDatabaseConnection();
			
			pr = dbConnection.prepareStatement("UPDATE CLOUDBILL_USR_SESSION " +
											   " set CUS_LOGOUT_STS = 'Y',CUS_LOGOUT_TMPSTP = SYSDATE  " +
											   " WHERE   CUS_SESN_ID = '"+session_id+"' ");
			int i =  pr.executeUpdate();
			if(i > 0){
				
				jsonResponse.put("status", "success");
				jsonResponse.put("message", "User Logged Out .");
				
			}
			 }catch(Exception e){
				 System.out.println("Exception thrown from logsessionUserData " +e); 
				 jsonResponse.put("status", "fail");
					jsonResponse.put("message", "Logout Unsuccesful .");
			 }finally{
				 DBManagerResourceRelease.close(pr);
			 } 
			 
		return jsonResponse;
	}

	@Override
	public JSONObject authenticateUserRefresh(JSONObject object) {

		PreparedStatement ps = null;
		ResultSet chkLoginUsersRS = null;
		JSONObject AckObj = new JSONObject();
		
		String username = (String) object.get("userID");
		String userRole = (String) object.get("userRole");
		String connType = (String) object.get("connType");
		String sessionID = (String) object.get("sessionId");
		
		try {
			if(username.isEmpty()){
				AckObj.put("status", "error");					
				AckObj.put("message", "UserId Is Empty.");	
			}else if(userRole.isEmpty()){
				AckObj.put("status", "error");					
				AckObj.put("message", "User Role IS Empty.");	
			}else if(sessionID.isEmpty()){
				AckObj.put("status", "error");					
				AckObj.put("message", "Sorry ! Session Terminated. Login Again .");	
			}		
			else{
				if(connType.equals("LT")){
					dbConnection=databaseObj.getDatabaseConnection();
				}else if(connType.equals("HT")){
					dbConnection=databaseObj.getHTDatabaseConnection();
				}
				
				
				ps = dbConnection.prepareStatement("SELECT COUNT(*) CNT FROM CLOUDBILL_USR_SESSION WHERE  CUS_SESN_ID = '"+sessionID+"'  "
						+ " AND CUS_USR_ID = '"+username+"' AND CUS_LOGOUT_STS = 'N'  "); 
				
				chkLoginUsersRS = ps.executeQuery();
				
				if(chkLoginUsersRS.next()){
					
					if(chkLoginUsersRS.getInt("CNT") > 0){
						
						AckObj.put("Username", username);
						AckObj.put("SESSION_ID", sessionID);
						AckObj.put("status", "success");
						AckObj.put("menus", getUserMenus(userRole,connType));
						AckObj.put("sav_menus", getSAV_menus(userRole,connType));
						AckObj.put("message", "User Authentication Succes.");
						
						return AckObj;
					}else {
						AckObj.put("status", "error");					
						AckObj.put("Username", username);
						AckObj.put("message", "Sorry ! Session Terminated. Login Again .");	
						return AckObj;
					}
				}else {
					AckObj.put("status", "error");					
					AckObj.put("Username", username);
					AckObj.put("message", "Sorry ! Session Terminated. Login Again .");	
					return AckObj;
				}
			}
			
		} catch (Exception e) {
			e.printStackTrace();
			AckObj.put("status", "failure");
			
			AckObj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(chkLoginUsersRS, ps);
		}
		
		return AckObj;
	}
	
	@Override
	public JSONObject getUserRoles(JSONObject object) {
		
		CallableStatement userCS = null;
		ResultSet userRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		String conn_type = object.getString("conn_type");
		
		try {
			if(conn_type.equalsIgnoreCase("LT")){
				dbConnection = databaseObj.getDatabaseConnection();
			}else if(conn_type.equals("HT")){
				dbConnection = databaseObj.getHTDatabaseConnection();
			}
			userCS=dbConnection.prepareCall(DBQueries.GET_USER_ROLE_DETLS);
			userCS.registerOutParameter(1, OracleTypes.CURSOR);
			userCS.executeUpdate();
			userRS = (ResultSet) userCS.getObject(1);
			
			while(userRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", userRS.getString("row_num"));
				ackobj.put("key", userRS.getString("role_cd"));
				ackobj.put("value", userRS.getString("role_description"));
				ackobj.put("role_cd", userRS.getString("role_cd"));
				ackobj.put("role_description", userRS.getString("role_description"));
				ackobj.put("role_sts", userRS.getString("role_sts"));
				ackobj.put("role_sts_description", userRS.getString("role_sts_description"));
				ackobj.put("userid", userRS.getString("userid"));
				ackobj.put("tmpstp", userRS.getString("tmpstp"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "User Roles Arrived !!!");
				obj.put("user_roles_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(userRS, userCS);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject upsertUserRoles(JSONObject object) {
		CallableStatement userCS = null;
		ResultSet userRS = null;
		JSONObject obj = new JSONObject();
		
		String conn_type = (String) object.get("conn_type");
		String db_option = (String) object.get("option");
		
		System.out.println(object);
		
		try {
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					userCS=dbConnection.prepareCall(DBQueries.UPSERT_USER_ROLE_DETAILS);
					
					userCS.setString(1, (String) object.get("role_cd"));
					userCS.setString(2, (String) object.get("role_description"));
					userCS.setString(3, (String) object.get("role_sts"));
					userCS.setString(4, (String) object.get("userid"));
					
					userCS.registerOutParameter(5, OracleTypes.CURSOR);
					userCS.executeUpdate();
					
					userRS = (ResultSet) userCS.getObject(5);
					
					if(userRS.next()){
						String RESP = userRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " User Role Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " User Role Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
					}
														
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid Inputs");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(userRS, userCS);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getUserMasterDetails(JSONObject object) {
		
		CallableStatement userCS = null;
		ResultSet userRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			userCS=dbConnection.prepareCall(DBQueries.GET_USER_MASTER_DETAILS);
			userCS.setString(1, object.getString("location_code"));
			userCS.registerOutParameter(2, OracleTypes.CURSOR);
			userCS.executeUpdate();
			userRS = (ResultSet) userCS.getObject(2);
			
			while(userRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", userRS.getString("row_num"));
				ackobj.put("user_id", userRS.getString("user_id"));
				ackobj.put("key", userRS.getString("user_id"));
				//ackobj.put("value", userRS.getString("user_id_descr"));
				ackobj.put("user_name", userRS.getString("user_name"));
				ackobj.put("user_role", userRS.getString("user_role"));
				ackobj.put("user_role_description", userRS.getString("user_role_description"));
				ackobj.put("user_email", userRS.getString("user_email"));
				ackobj.put("user_valid_from", userRS.getString("user_valid_from"));
				ackobj.put("user_valid_upto", userRS.getString("user_valid_upto"));
				ackobj.put("user_db_setup", userRS.getString("user_db_setup"));
				ackobj.put("user_db_setup_description", userRS.getString("user_db_setup_description"));
				ackobj.put("user_status", userRS.getString("user_status"));
				ackobj.put("user_status_description", userRS.getString("user_status_description"));
				ackobj.put("userid", userRS.getString("userid"));
				ackobj.put("tmpstp", userRS.getString("tmpstp"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "User Details Arrived !!!");
				obj.put("user_master_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(userRS, userCS);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject upsertUserMaster(JSONObject object) {
		CallableStatement userCS = null;
		ResultSet userRS = null;
		JSONObject obj = new JSONObject();
		
		String location_code = (String) object.get("location_code");
		String conn_type = (String) object.get("conn_type");
		String db_option = (String) object.get("option");
		
		System.out.println(object);
		
		try {
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					userCS=dbConnection.prepareCall(DBQueries.UPSERT_USER_MASTER_DETAILS);
					
					userCS.setString(1, (String) object.get("option"));
					userCS.setString(2, location_code);
					userCS.setString(3, (String) object.get("user_id"));
					userCS.setString(4, EncriptAndDecript.encrypt((String) object.get("password")));
					userCS.setString(5, (String) object.get("user_name"));
					userCS.setString(6, (String) object.get("user_role"));
					userCS.setString(7, (String) object.get("user_email"));
					userCS.setString(8, (String) object.get("user_valid_from"));
					userCS.setString(9, (String) object.get("user_valid_upto"));
					userCS.setString(10, (String) object.get("user_db_setup"));
					userCS.setString(11, (String) object.get("user_status"));
					
					userCS.setString(12, (String) object.get("userid"));
					
					userCS.registerOutParameter(13, OracleTypes.CURSOR);
					userCS.executeUpdate();
					
					userRS = (ResultSet) userCS.getObject(13);
					
					if(userRS.next()){
						String RESP = userRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " User Master Details "+(db_option.equals("ADD") ? "  Inserted " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " User Master Details  "+ (db_option.equals("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else {
							obj.put("status", "error");
							obj.put("message", userRS.getString("RESP"));
						}
							
														
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid Inputs");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(userRS, userCS);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject deleteUserMaster(JSONObject object) {
		CallableStatement userCS = null;
		ResultSet userRS = null;
		JSONObject obj = new JSONObject();
		
		String location_code = (String) object.get("location_code");
		String conn_type = (String) object.get("conn_type");
		String db_option = (String) object.get("option");
		
		System.out.println(object);
		
		try {
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					userCS=dbConnection.prepareCall(DBQueries.UPSERT_USER_MASTER_DETAILS);
					
					userCS.setString(1, (String) object.get("option"));
					userCS.setString(2, location_code);
					userCS.setString(3, (String) object.get("user_id"));
					userCS.setString(4, (String) object.get(""));
					userCS.setString(5, (String) object.get(""));
					userCS.setString(6, (String) object.get(""));
					userCS.setString(7, (String) object.get(""));
					userCS.setString(8, (String) object.get(""));
					userCS.setString(9, (String) object.get(""));
					userCS.setString(10, (String) object.get("user_db_setup"));
					userCS.setString(11, (String) object.get(""));
					
					userCS.setString(12, (String) object.get("userid"));
					
					userCS.registerOutParameter(13, OracleTypes.CURSOR);
					userCS.executeUpdate();
					
					userRS = (ResultSet) userCS.getObject(13);
					
					if(userRS.next()){
						String RESP = userRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " User Master Details Deleted successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " User Master Details  Deletion failed.");
						}else {
							obj.put("status", "error");
							obj.put("message", userRS.getString("RESP"));
						}
							
														
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid Inputs");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(userRS, userCS);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getUserSessionDetails(JSONObject object) {
		
		CallableStatement userCS = null;
		ResultSet userRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			userCS=dbConnection.prepareCall(DBQueries.GET_USER_SESSION_DETAILS);
			userCS.setString(1, object.getString("location_code"));
			userCS.registerOutParameter(2, OracleTypes.CURSOR);
			userCS.executeUpdate();
			userRS = (ResultSet) userCS.getObject(2);
			
			while(userRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", userRS.getString("row_num"));
				ackobj.put("session_id", userRS.getString("session_id"));
				ackobj.put("user_id", userRS.getString("user_id"));
				ackobj.put("login_tmpstp", userRS.getString("login_tmpstp"));
				ackobj.put("logout_tmpstp", userRS.getString("logout_tmpstp"));
				ackobj.put("login_ip_addrs", userRS.getString("login_ip_addrs"));
				ackobj.put("mac_addrs", userRS.getString("mac_addrs"));
				ackobj.put("logout_sts", userRS.getString("logout_sts"));

				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "User Session Details Arrived !!!");
				obj.put("user_session_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(userRS, userCS);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject updateUserSessions(JSONObject data) {
		
		String session_id = (String) data.get("session_id");
		String conn_type = (String) data.get("conn_type");
		
		JSONObject AckObj=new JSONObject();
		try {
			if(data!=null){
				
		    	JSONArray getArray = data.getJSONArray("data");
		    	
		    	for(int i = 0; i < getArray.size(); i++) {
		    		JSONObject objects = getArray.getJSONObject(i);
		    		AckObj = updateIndividualUserSession(objects, conn_type);
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
	
	public JSONObject updateIndividualUserSession(JSONObject object, String conn_type) {
		CallableStatement userCS = null;
		ResultSet userRS = null;
		JSONObject obj = new JSONObject();
		
		String session_id = (String) object.get("session_id");
		
		System.out.println("DATA : "+object+" session_id:"+session_id);
		
	
		try {
			if(!session_id.isEmpty() ){
				
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				userCS=dbConnection.prepareCall(DBQueries.UPDATE_USER_SESSION);
				userCS.setString(1, session_id);
				
				userCS.registerOutParameter(2, OracleTypes.CURSOR);
				userCS.executeUpdate();
				
				userRS = (ResultSet) userCS.getObject(2);
					
					if(userRS.next()){
						String RESP = userRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " Session Updated successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " Session Updation  Failed.");
						}else  {
							obj.put("status", "error");
							obj.put("message", RESP);
						}						
					}
														
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid Session ID");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(userRS, userCS);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject getUserDeligationDetails(JSONObject object) {
		
		CallableStatement userCS = null;
		ResultSet userRS = null;
		JSONArray array = new JSONArray();
		JSONObject obj = new JSONObject();
		
		try {
			dbConnection = databaseObj.getDatabaseConnection();
			userCS=dbConnection.prepareCall(DBQueries.GET_USER_DELIGATION_DETAILS);
			userCS.setString(1, object.getString("location_code"));
			userCS.registerOutParameter(2, OracleTypes.CURSOR);
			userCS.executeUpdate();
			userRS = (ResultSet) userCS.getObject(2);
			
			while(userRS.next()){
				
				JSONObject ackobj=new JSONObject();
				
				ackobj.put("row_num", userRS.getString("row_num"));
				ackobj.put("user_id", userRS.getString("user_id"));
				ackobj.put("user_name", userRS.getString("user_name"));
				ackobj.put("user_desgn", userRS.getString("user_desgn"));
				ackobj.put("deligated_by", userRS.getString("deligated_by"));
				ackobj.put("deligated_user_name", userRS.getString("deligated_user_name"));
				ackobj.put("deligated_role", userRS.getString("deligated_role"));
				ackobj.put("from_dt", userRS.getString("from_dt"));
				ackobj.put("to_dt", userRS.getString("to_dt"));
				ackobj.put("deligated_on", userRS.getString("deligated_on"));
				ackobj.put("deligation_updated_on", userRS.getString("deligation_updated_on"));
				
				array.add(ackobj);
				
			}
			if(array.isEmpty()) {
				//no tasks for user
				obj.put("status", "success");
				obj.put("message", "No Records Found !!!");
			} else{
				obj.put("status", "success");
				obj.put("message", "User Deligation Details Arrived !!!");
				obj.put("user_deligation_details", array);
			}
			
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(userRS, userCS);
		}
		
		return obj;
	}
	
	@Override
	public JSONObject upsertUserDeligation(JSONObject object) {
		CallableStatement userCS = null;
		ResultSet userRS = null;
		JSONObject obj = new JSONObject();
		
		String location_code = (String) object.get("location_code");
		String conn_type = (String) object.get("conn_type");
		String db_option = (String) object.get("option");
		
		System.out.println(object);
		
		try {
				if(conn_type.equalsIgnoreCase("LT")){
					dbConnection = databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection = databaseObj.getHTDatabaseConnection();
				}
				
					userCS=dbConnection.prepareCall(DBQueries.UPSERT_USER_DELIGATION);
					
					userCS.setString(1, (String) object.get("option"));
					userCS.setString(2, location_code);
					userCS.setString(3, (String) object.get("to_userid"));
					userCS.setString(4, (String) object.get("to_username"));
					userCS.setString(5, (String) object.get("to_userrole"));
					userCS.setString(6, (String) object.get("delegated_userid"));
					userCS.setString(7, (String) object.get("delegated_username"));
					userCS.setString(8, (String) object.get("delegated_userrole"));
					userCS.setString(9, (String) object.get("fromdate"));
					userCS.setString(10, (String) object.get("todate"));
					
					userCS.registerOutParameter(11, OracleTypes.CURSOR);
					userCS.executeUpdate();
					
					userRS = (ResultSet) userCS.getObject(11);
					
					if(userRS.next()){
						String RESP = userRS.getString("RESP");
						System.out.println("RESP : "+RESP);
						
						if(RESP.equalsIgnoreCase("success")){
							obj.put("status", "success");
							obj.put("message", " User Deligation "+(db_option.equalsIgnoreCase("ADD") ? "  Added " : "  Updated")+" successfully");
						}else if (RESP.equalsIgnoreCase("fail")) {
							obj.put("status", "error");
							obj.put("message", " User Deligation  "+ (db_option.equalsIgnoreCase("ADD") ? " Insertion " : "Updation") + "  failed.");
						}else {
							obj.put("status", "error");
							obj.put("message", userRS.getString("RESP"));
						}
							
														
			}else{
				obj.put("status", "error");
				obj.put("message", "Please Enter a valid Inputs");
			}
		} catch (Exception e) {
			obj.put("status", "fail");
			e.printStackTrace();
			obj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(userRS, userCS);
		}
		
		return obj;
	}

	@Override
	public JSONObject changepassword(JSONObject object) {
		// TODO Auto-generated method stub
		CallableStatement chkLoginUsersCS = null;
		ResultSet chkLoginUsersRS = null;
		JSONObject AckObj = new JSONObject();
				
		String old_encryptedpassword = null;
		String new_encryptedpassword = null;
		
		String conn_type = (String) object.get("conn_type");
		
		try {
				old_encryptedpassword = EncriptAndDecript.encrypt((String) object.get("old_password"));
				new_encryptedpassword = EncriptAndDecript.encrypt((String) object.get("new_password"));
				
				System.out.println(old_encryptedpassword);
				System.out.println(new_encryptedpassword);
				
				//dbConnection = DatabaseImpl.GetLTSQLConnection();
				if(conn_type.equals("LT")){
					dbConnection=databaseObj.getDatabaseConnection();
				}else if(conn_type.equals("HT")){
					dbConnection=databaseObj.getHTDatabaseConnection();
				}
				
				chkLoginUsersCS = dbConnection.prepareCall(DBQueries.CHANGE_PASSWORD);
				chkLoginUsersCS.registerOutParameter(1, OracleTypes.CURSOR);
				chkLoginUsersCS.setString(2,(String) object.get("userid"));
				chkLoginUsersCS.setString(3,old_encryptedpassword);
				chkLoginUsersCS.setString(4,new_encryptedpassword);
				chkLoginUsersCS.setString(5,(String) object.get("userid"));
				chkLoginUsersCS.executeUpdate();
				chkLoginUsersRS = (ResultSet) chkLoginUsersCS.getObject(1);
				
				if(chkLoginUsersRS.next()) {
					String resp = chkLoginUsersRS.getString("RESP");
					
					if(resp.equalsIgnoreCase("success")){
						AckObj.put("status", "success");
						AckObj.put("message", chkLoginUsersRS.getString("MESSAGE"));
					} else {
						AckObj.put("status", "error");					
						AckObj.put("message", chkLoginUsersRS.getString("MESSAGE"));	
					}
				} else {
					AckObj.put("status", "error");					
					AckObj.put("message", "Password Change Unscuccessful.");	
				}
			
		} catch (Exception e) {
			e.printStackTrace();
			AckObj.put("status", "failure");
			
			AckObj.put("message", "database not connected");
		}finally
		{
			DBManagerResourceRelease.close(chkLoginUsersRS, chkLoginUsersCS);
		}
		
		return AckObj;
				
	}

}
