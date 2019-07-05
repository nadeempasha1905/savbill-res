package com.savbill.database;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;
/**
@author Madhu
 */
public final class DBManagerResourceRelease {
	/**
	   * Closes given ResultSet 
	   *@param ResultSet
	   */
	  public static void close(ResultSet rs) {
	    if (rs != null) {
	      try {
	        rs.close();
	      } catch (Exception excp) {
	        // Ignore, we don't care, we do care when we open it
	      }
	    }
	  }
	 
	 
	  /**
	   * Closes given PreparedStatement
	   * @param PreparedStatement 
	   */
	  public static void close(PreparedStatement ps) {
	    if (ps != null) {
	      try {
	        ps.close();
	      } catch (Exception excp) {
	        // Ignore, we don't care, we do care when we open it
	      }
	    }
	  }
	 
	 
	  /**
	   * Closes given Connection 
	   * @param Connection
	   */
	  public static void close(Connection cn) {
	    if (cn != null) {
	      try {
	        cn.close();
	      } catch (Exception excp) {
	        // Ignore, we don't care, we do care when we open it
	      }
	    }
	  }
	 
	 
	  /**
	   * Closes given Statement
	   * @param Statement
	   */
	  public static void close(Statement stmt) {
	    if (stmt != null) {
	      try {
	        stmt.close();
	      } catch (Exception excp) {
	        // Ignore, we don't care, we do care when we open it
	      }
	    }
	  }
	  /**
	   * Closes given Statement
	   * @param Statement
	   */
	  public static void close(CallableStatement cstmt) {
	    if (cstmt != null) {
	      try {
	    	  cstmt.close();
	      } catch (Exception excp) {
	        // Ignore, we don't care, we do care when we open it
	      }
	    }
	  }
	  /**
	   * Closes single set of ResultSet,PreparedStatement,Connection
	   * @param ResultSet,PreparedStatement,Connection
	   */
	 
	  public static void close(ResultSet rs, PreparedStatement ps, Connection conn) {
	    close(rs);
	    close(ps);
	    close(conn);
	  }
	  
	  public static void close(ResultSet rs, PreparedStatement ps) {
		    close(rs);
		    close(ps);
		  }
	  
	  public static void close(ResultSet rs, CallableStatement cs, Connection conn) {
		    close(rs);
		    close(cs);
		    close(conn);
		  }
	  
	  public static void close(ResultSet rs, CallableStatement cs) {
		    close(rs);
		    close(cs);
		  }
	  
	  public static void close( PreparedStatement ps,Connection conn) {
		    close(ps);
		    close(conn);
		  }
	}

