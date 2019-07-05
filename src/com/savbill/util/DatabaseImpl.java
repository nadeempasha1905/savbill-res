package com.savbill.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DatabaseImpl {
	
	public static Connection GetLTSQLConnection(){
		
		Connection con = null;
		
		try {
			
			/*Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cash","root","root");  */
			
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			con=DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.159:1521:cloudbill", "clb", "clb");
			
			/*Class.forName("oracle.jdbc.driver.OracleDriver");  
			con=DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.143:1521:cloudbill", "clb", "clb");*/
			
			
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Error Occured Oracle Connection : "+e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error Occured Mysql Connection : "+e.getMessage());
		}
		
		return con;
	}
	
public static Connection GetHTSQLConnection(){
		
		Connection con = null;
		
		try {
			
			/*Class.forName("com.mysql.jdbc.Driver");  
			con=DriverManager.getConnection("jdbc:mysql://localhost:3306/cash","root","root");  */
			
			Class.forName("oracle.jdbc.driver.OracleDriver");  
			con=DriverManager.getConnection("jdbc:oracle:thin:@192.168.0.159:1521:cloudbill", "htbill", "htbill");  
			
			
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Error Occured Oracle Connection : "+e.getMessage());
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			System.out.println("Error Occured Mysql Connection : "+e.getMessage());
		}
		
		return con;
	}
	
	public static void CleanUp(Connection con,PreparedStatement ps ,ResultSet rs){
		
		try {
			
			if(con != null){
				con.close();
			}
			
			if(rs != null){
				rs.close();
			}
			
			if(ps != null){
				ps.close();
			}
			
		} catch (SQLException e) {
			// TODO: handle exception
			System.out.println("Error Occured , CleanUp : "+e.getMessage());
		}
		
	}
	
	public static void PrintExceptionMessageOnConsole(String ClassName,String Method,String Message){
		
		System.out.println("Error Occured for Class : "+ClassName + ", Method : "+Method+" , Error : "+Message);
		return;
	}

}
