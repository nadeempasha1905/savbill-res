package com.savbill.implementation;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import com.savbill.database.DBManagerResourceRelease;
import com.savbill.user.MenuStructureBO;
import com.savbill.util.DatabaseImpl;

import net.sf.json.JSONObject;

public class LoginMasterImpl {

	public static JSONObject GetLoginUserDetails() {
		// TODO Auto-generated method stub
		JSONObject json = new JSONObject();
		
		try {
			json.put("name", "Vanaj Nayak");
			json.put("role", "AAO");
			json.put("subDivision", "Udupi");
			json.put("division", "Mangalore");
			json.put("company", "MESCOM");
			
		} catch (Exception e) {
			// TODO: handle exception
		}
		return json;
	}

	public static JSONObject GetUserMenus() {
		// TODO Auto-generated method stub
		
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		PreparedStatement ps1 = null;
		ResultSet rs1 = null;
		
		List<MenuStructureBO> MenuList = new ArrayList<MenuStructureBO>();
		MenuStructureBO menu = null;
		JSONObject json = new JSONObject();
		
		
		
	
		
		try{
			
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			//con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.159:1521:cloudbill", "bill","bill");
			
			con = DatabaseImpl.GetLTSQLConnection();
			
			String query = " SELECT BFM_FORM_CD, DECODE (SUBSTR(BFM_FORM_CD, -1), '0', 'M', 'L') MNU," +
				       " BFM_FORM_NAME ,BFM_FORM_CD, BFP_PRIV " +
				       " from FORM_MASTER, FORM_PRIVILEGES " +
				       " WHERE BFM_FORM_CD = BFP_FORM_CD AND BFP_ROLE_CD ='SUP' AND BFP_PRIV !='0000' " +
				       " AND (BFM_FORM_CD like '_000000' or BFM_FORM_CD like '_000000') "+
				       " ORDER BY 1"; 
			
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			System.out.println("BFM_FORM_CD ***** MNU ******** BFM_FORM_NAME ****** BFP_PRIV ");
			while(rs.next()){
				System.out.println(rs.getString("BFM_FORM_CD") + "---" + rs.getString("MNU")+ "---" + rs.getString("BFM_FORM_NAME")+ "---" + rs.getString("BFP_PRIV"));
				
				menu = new MenuStructureBO();
				
					
					menu.setTitle(rs.getString("BFM_FORM_NAME"));
					menu.setName(rs.getString("BFM_FORM_CD"));
					menu.setPrevilage(rs.getString("BFP_PRIV"));

					menu.setSubMenuList(GetSubMenus(rs.getString("BFM_FORM_CD")));
					
					MenuList.add(menu);
			}
			json.put("MENU", MenuList);
			System.out.println("json : "+json);
			
		}catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Error Occured : " + e.getMessage());
		}
		
		DBManagerResourceRelease.close(rs, ps);
		DBManagerResourceRelease.close(rs1, ps1);
		return json;
	}

	private static List<MenuStructureBO> GetSubMenus(String FormCode) {
		// TODO Auto-generated method stub
		
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<MenuStructureBO> MenuList = new ArrayList<MenuStructureBO>();
		MenuStructureBO menu = null;
		
		try{
			
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			//con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.159:1521:cloudbill", "bill","bill");
			
			con = DatabaseImpl.GetLTSQLConnection();
			
			String query =" SELECT BFM_FORM_CD, DECODE (SUBSTR(BFM_FORM_CD, -1), '0', 'M', 'L') MNU," +
				       " BFM_FORM_NAME ,BFM_FORM_CD, BFP_PRIV" +
				       " from FORM_MASTER, FORM_PRIVILEGES " +
				       " WHERE BFM_FORM_CD = BFP_FORM_CD AND BFP_ROLE_CD ='SUP' AND BFP_PRIV !='0000' " +
				       " AND (BFM_FORM_CD like '"+FormCode.substring(0,6)+"%' or BFM_FORM_CD like '"+FormCode.substring(0,1)+"_00000')" +
				       		" AND BFM_FORM_CD <>  '"+FormCode+"' " +
				       " ORDER BY 1";  
			
			System.out.println("sub = "+query);
			
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			while(rs.next()){
				
				
				
				
				menu = new MenuStructureBO();
				
				menu.setTitle(rs.getString("BFM_FORM_NAME"));
				menu.setName(rs.getString("BFM_FORM_CD"));
				menu.setPrevilage(rs.getString("BFP_PRIV"));
				
				if(rs.getString("MNU").equals("M")){
					menu.setSubMenuList(GetMicroMenus(rs.getString("BFM_FORM_CD")));
				}
				MenuList.add(menu);
				
			}
			
		}catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Error Occured : " + e.getMessage());
		}
		//DatabaseImpl.CleanUp(con, ps, rs);
		DBManagerResourceRelease.close(rs, ps);
		return MenuList;
	}

	private static List<MenuStructureBO> GetMicroMenus(String FormCode) {
		// TODO Auto-generated method stub
		Connection con = null;
		PreparedStatement ps = null;
		ResultSet rs = null;
		
		List<MenuStructureBO> MenuList = new ArrayList<MenuStructureBO>();
		MenuStructureBO menu = null;
		
		try{
			
			//Class.forName("oracle.jdbc.driver.OracleDriver");
			//con = DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.159:1521:cloudbill", "bill","bill");
			
			con = DatabaseImpl.GetLTSQLConnection();
			
			String query =" SELECT BFM_FORM_CD, DECODE (SUBSTR(BFM_FORM_CD, -1), '0', 'M', 'L') MNU," +
				       " BFM_FORM_NAME ,BFM_FORM_CD, BFP_PRIV " +
				       " from FORM_MASTER, FORM_PRIVILEGES " +
				       " WHERE BFM_FORM_CD = BFP_FORM_CD AND BFP_ROLE_CD ='SUP' AND BFP_PRIV !='0000' " +
 					   " AND (BFM_FORM_CD like '"+FormCode.substring(0,6)+"%' or BFM_FORM_CD like '"+FormCode.substring(0,2)+"_0000') " +
 					   " AND BFM_FORM_CD <>  '"+FormCode+"' " +
				       " ORDER BY 1";  
			
			System.out.println("micro = "+query);
			
			ps = con.prepareStatement(query);
			rs = ps.executeQuery();
			
			
			while(rs.next()){
				menu = new MenuStructureBO();
				
				menu.setTitle(rs.getString("BFM_FORM_NAME"));
				menu.setName(rs.getString("BFM_FORM_CD"));
				menu.setPrevilage(rs.getString("BFP_PRIV"));

				MenuList.add(menu);
				
			}
			
		}catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Error Occured : " + e.getMessage());
		}
		//DatabaseImpl.CleanUp(con, ps, rs);
		DBManagerResourceRelease.close(rs, ps);
		return MenuList;
	}

	public static JSONObject validateUser() {
		// TODO Auto-generated method stub
		return null;
	}
	
}