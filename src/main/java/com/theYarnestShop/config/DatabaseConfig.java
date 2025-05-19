package com.theYarnestShop.config;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConfig {
	// Database name
	private static final String DB_NAME = "theyarnestshop";
	
	// Database connection URL
	private static final String URL = "jdbc:mysql://localhost:3306/" + DB_NAME;
	
	// Database credentials
	private static final String USERNAME = "root";
	private static final String PASSWORD = "";

	/**
	 * Establishes a connection to the MySQL database using the configured
	 * URL, username, and password. Loads the JDBC driver and returns
	 * a Connection object if successful.
	 * 
	 * @return Connection object to the database
	 * @throws SQLException if a database access error occurs
	 * @throws ClassNotFoundException if the JDBC driver class is not found
	 */
	public static Connection getDatabaseConnection() throws SQLException, ClassNotFoundException {
		Class.forName("com.mysql.cj.jdbc.Driver");
		return DriverManager.getConnection(URL, USERNAME, PASSWORD);
	}

	/**
	 * Returns the name of the database.
	 * 
	 * @return Database name as a String
	 */
	public static String getDbName() {
		return DB_NAME;
	}

	/**
	 * Placeholder method. Currently returns null.
	 * This method is not implemented and may be removed or completed later.
	 * 
	 * @return null
	 */
	public static Connection getConnection() {
		// TODO Auto-generated method stub
		return null;
	}		
}
