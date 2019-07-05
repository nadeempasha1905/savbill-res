package com.savbill.database;

import java.sql.Connection;

/**
@author Madhu
 */
public interface IDBManger {
	/**
	 * getDatabaseConnection() to obtain connection object with specific database 
	 * along with  parameters specified
	 * If connection exits returns the instance of existing connection
	 * @return Connection
	 */
	public Connection getDatabaseConnection() throws Exception;
	
	public Connection getHTDatabaseConnection() throws Exception;
}
