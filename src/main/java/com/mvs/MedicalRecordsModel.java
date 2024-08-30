package com.mvs;

// MedicalRecordsModel.java
import java.sql.*;
import java.util.ArrayList;

public class MedicalRecordsModel {

  // Method to establish a connection to the database
  private Connection connect() {
    Connection conn = null;
    try {
      // Adjust the connection details as per your database credentials
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mvs_final_project", "default", "Password123!");
    } catch (SQLException e) {
      // Print any SQL errors that occur
      System.out.println(e.getMessage());
    }
    return conn; // Return the established connection
  }

  // Method to fetch all medical records from the database
  public ArrayList<MedicalRecord> getMedicalRecords() {
    ArrayList<MedicalRecord> records = new ArrayList<>(); // List to store the fetched records
    String sql = "SELECT * FROM medical_records"; // SQL query to fetch all records
    try (Connection conn = this.connect(); // Establish a connection
         Statement stmt = conn.createStatement(); // Create a statement
         ResultSet rs = stmt.executeQuery(sql)) { // Execute the query
      while (rs.next()) { // Iterate through the result set
        // Add each record to the list
        records.add(new MedicalRecord(
            rs.getInt("Record_ID"), 
            rs.getInt("User_ID"), 
            rs.getString("Description"), 
            rs.getDate("Date")));
      }
    } catch (SQLException e) {
      // Print any SQL errors that occur
      System.out.println(e.getMessage());
    }
    return records; // Return the list of records
  }

  // Method to add a new medical record to the database
  public void addMedicalRecord(MedicalRecord record) {
    String sql = "INSERT INTO MEDICAL_RECORDS(User_ID, Description, Date) VALUES (?, ?, ?)"; // SQL query to insert a new record
    try (Connection conn = this.connect(); // Establish a connection
         PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare the SQL statement
      // Set the parameters for the prepared statement
      pstmt.setInt(1, record.getUserID());
      pstmt.setString(2, record.getDescription());
      pstmt.setDate(3, new java.sql.Date(record.getDate().getTime()));
      pstmt.executeUpdate(); // Execute the update
    } catch (SQLException e) {
      // Print any SQL errors that occur
      System.out.println(e.getMessage());
    }
  }

  // Method to update an existing medical record in the database
  public void updateMedicalRecord(MedicalRecord record) {
    String sql = "UPDATE MEDICAL_RECORDS SET Description = ?, Date = ? WHERE Record_ID = ?"; // SQL query to update a record
    try (Connection conn = this.connect(); // Establish a connection
         PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare the SQL statement
      // Set the parameters for the prepared statement
      pstmt.setString(1, record.getDescription());
      pstmt.setDate(2, new java.sql.Date(record.getDate().getTime()));
      pstmt.setInt(3, record.getRecordID());
      pstmt.executeUpdate(); // Execute the update
    } catch (SQLException e) {
      // Print any SQL errors that occur
      System.out.println("Update failed: " + e.getMessage());
    }
  }

  // Method to delete a medical record from the database
  public void deleteMedicalRecord(int recordID) {
    String sql = "DELETE FROM medical_records WHERE Record_ID = ?"; // SQL query to delete a record
    try (Connection conn = this.connect(); // Establish a connection
         PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare the SQL statement
      // Set the parameter for the prepared statement
      pstmt.setInt(1, recordID);
      pstmt.executeUpdate(); // Execute the update
    } catch (SQLException e) {
      // Print any SQL errors that occur
      System.out.println(e.getMessage());
    }
  }
}
