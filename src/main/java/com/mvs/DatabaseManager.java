package com.mvs;

import java.sql.*;

/**
 * DatabaseManager is a utility class that manages the database connection for the application.
 * It provides a method to get a connection to the database, ensuring a single connection instance is used.
 */
public class DatabaseManager {
  private static Connection connection;

  /**
   * Returns a connection to the database. If a connection does not already exist or is closed, a new one is created.
   * 
   * @return the database connection
   * @throws SQLException if a database access error occurs
   */
  public static Connection getConnection() throws SQLException {
    if (connection == null || connection.isClosed()) {
      String url = "jdbc:mysql://localhost:3306/mvs_final_project";
      String user = "default"; 
      String password = "Password123!"; 
      connection = DriverManager.getConnection(url, user, password);
    }
    return connection;
  }
}
