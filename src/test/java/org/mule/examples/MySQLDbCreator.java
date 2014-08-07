/**
 * Mule Anypoint Template
 * Copyright (c) MuleSoft, Inc.
 * All rights reserved.  http://www.mulesoft.com
 */

package org.mule.examples;

import java.io.FileInputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import org.apache.log4j.Logger;

public class MySQLDbCreator {
	private static final Logger log = Logger.getLogger(MySQLDbCreator.class);
	private String databaseName;
	private String databaseUrl;
	private String databaseWithNameUrl;
	
	public MySQLDbCreator(String databaseName, String pathToProperties){
		final Properties props = new Properties();
		try {
			props.load(new FileInputStream(pathToProperties));
		} catch (Exception e) {
			log.error("Error occured while reading mule.test.properties", e);
		}
		final String user = props.getProperty("database.user");
		final String password = props.getProperty("database.password");
		final String dbUrl = props.getProperty("database.url");
		
		this.databaseName = databaseName;
		this.databaseUrl = dbUrl+"?user="+user+"&password="+password;
		this.databaseWithNameUrl = dbUrl+databaseName+"?rewriteBatchedStatements=true&user="+user+"&password="+password;
	}
	
	public String getDatabaseUrlWithName(){
		return databaseWithNameUrl;
	}
	
	public void setUpDatabase() {
		
		System.out.println("******************************** Populate MySQL DB **************************");
		Connection conn = null;
		
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			
			// Get a connection
			conn = DriverManager.getConnection(databaseUrl);
			Statement stmt = conn.createStatement();
			
			stmt.addBatch("CREATE DATABASE "+databaseName);
			stmt.addBatch("USE "+databaseName);
			stmt.addBatch("CREATE USER 'generatedata'@'localhost' IDENTIFIED BY 'generatedata';");
			stmt.addBatch("GRANT ALL PRIVILEGES ON company.* TO generatedata@localhost IDENTIFIED BY 'generatedata';");
			stmt.addBatch("FLUSH PRIVILEGES;");
			stmt.addBatch("CREATE TABLE `orders` (\n  `id` int(11) NOT NULL AUTO_INCREMENT,\n  `item_id` int(11) NOT NULL,\n  `item_units` int(11) NOT NULL,\n  `customer_id` int(11) NOT NULL,\n  PRIMARY KEY (`id`)\n) ENGINE=InnoDB AUTO_INCREMENT=109 DEFAULT CHARSET=utf8;");
			stmt.addBatch("INSERT INTO orders (item_id,item_units,customer_id) VALUES (1, 2, 3);");
			stmt.executeBatch();
			System.out.println("Success");
			
		} catch (SQLException ex) {
		    // handle any errors
		    log.error("SQLException: " + ex.getMessage());
		    log.error("SQLState: " + ex.getSQLState());
		    log.error("VendorError: " + ex.getErrorCode());
		} catch (Exception except) {
			except.printStackTrace();
		}
	}
	
	public void tearDownDataBase() {
		System.out.println("******************************** Delete Tables from MySQL DB **************************");
		Connection conn = null;
		try {
			Class.forName("com.mysql.jdbc.Driver").newInstance();
			conn = DriverManager.getConnection(databaseUrl);
		
			Statement stmt = conn.createStatement();
			stmt.executeUpdate("DROP SCHEMA "+databaseName);
			stmt.executeUpdate("drop user 'generatedata'@'localhost'");
		} catch (Exception except) {
			except.printStackTrace();
		}
	}
}
