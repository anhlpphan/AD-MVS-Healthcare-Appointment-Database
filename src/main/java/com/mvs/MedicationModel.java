package com.mvs;

import java.sql.*;
import java.util.ArrayList;

public class MedicationModel {
  
  // Method to establish a connection to the database
  private Connection connect() {
    String url = "jdbc:mysql://localhost:3306/mvs_final_project"; // Database URL
    String user = "default"; // Database username
    String password = "Password123!"; // Database password
    try {
      // Try to establish a connection and return it
      return DriverManager.getConnection(url, user, password);
    } catch (SQLException e) {
      // Print any SQL errors that occur
      System.out.println(e.getMessage());
      return null;
    }
  }

  // Method to fetch all medications from the database
  public ArrayList<Medication> getAllMedications() {
    String sql = "SELECT * FROM MEDICATION"; // SQL query to fetch all medications
    ArrayList<Medication> medications = new ArrayList<>(); // List to store the fetched medications
    try (Connection conn = this.connect(); // Establish a connection
         Statement stmt = conn.createStatement(); // Create a statement
         ResultSet rs = stmt.executeQuery(sql)) { // Execute the query
      while (rs.next()) { // Iterate through the result set
        // Add each medication to the list
        medications.add(new Medication(
            rs.getInt("Medication_ID"), 
            rs.getInt("User_ID"), 
            rs.getString("Medication_Description")));
      }
    } catch (SQLException e) {
      // Print any SQL errors that occur
      System.out.println(e.getMessage());
    }
    return medications; // Return the list of medications
  }

  // Method to add a new medication to the database
  public void addMedication(Medication medication) {
    String sql = "INSERT INTO MEDICATION (User_ID, Medication_Description) VALUES (?, ?)"; // SQL query to insert a new medication
    try (Connection conn = this.connect(); // Establish a connection
         PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare the SQL statement
      // Set the parameters for the prepared statement
      pstmt.setInt(1, medication.getUserId());
      pstmt.setString(2, medication.getDescription());
      pstmt.executeUpdate(); // Execute the update
    } catch (SQLException e) {
      // Print any SQL errors that occur
      System.out.println(e.getMessage());
    }
  }

  // Method to update an existing medication in the database
  public void updateMedication(Medication medication) {
    String sql = "UPDATE MEDICATION SET Medication_Description = ? WHERE Medication_ID = ?"; // SQL query to update a medication
    try (Connection conn = this.connect(); // Establish a connection
         PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare the SQL statement
      // Set the parameters for the prepared statement
      pstmt.setString(1, medication.getDescription());
      pstmt.setInt(2, medication.getMedicationId());
      pstmt.executeUpdate(); // Execute the update
    } catch (SQLException e) {
      // Print any SQL errors that occur
      System.out.println(e.getMessage());
    }
  }

  // Method to delete a medication from the database
  public void deleteMedication(int medicationId) {
    String sql = "DELETE FROM MEDICATION WHERE Medication_ID = ?"; // SQL query to delete a medication
    try (Connection conn = this.connect(); // Establish a connection
         PreparedStatement pstmt = conn.prepareStatement(sql)) { // Prepare the SQL statement
      // Set the parameter for the prepared statement
      pstmt.setInt(1, medicationId);
      pstmt.executeUpdate(); // Execute the update
    } catch (SQLException e) {
      // Print any SQL errors that occur
      System.out.println(e.getMessage());
    }
  }
}
