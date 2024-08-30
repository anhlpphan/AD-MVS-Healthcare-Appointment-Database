package com.mvs;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class PatientModel {
  
  // Retrieve all patient records
  public static DefaultTableModel getPatientsTableModel() {
    try (Connection conn = DatabaseManager.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT User_ID, Height, Weight FROM PATIENT")) {
      DefaultTableModel model = new DefaultTableModel(new Object[] { "User ID", "Height", "Weight" }, 0);
      while (rs.next()) {
        model.addRow(new Object[] { rs.getInt("User_ID"), rs.getDouble("Height"), rs.getDouble("Weight") });
      }
      return model;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  // Add a new patient
  public static boolean addPatient(int userId, double height, double weight) {
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn
            .prepareStatement("INSERT INTO PATIENT (User_ID, Height, Weight) VALUES (?, ?, ?)")) {
      ps.setInt(1, userId);
      ps.setDouble(2, height);
      ps.setDouble(3, weight);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  // Update an existing patient
  public static boolean updatePatient(int userId, double height, double weight) {
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("UPDATE PATIENT SET Height = ?, Weight = ? WHERE User_ID = ?")) {
      ps.setDouble(1, height);
      ps.setDouble(2, weight);
      ps.setInt(3, userId);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  // Delete a patient
  public static boolean deletePatient(int userId) {
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM PATIENT WHERE User_ID = ?")) {
      ps.setInt(1, userId);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  // Retrieve a patient by ID
  public static Patient getPatientById(int userId) {
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM PATIENT WHERE User_ID = ?")) {
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return new Patient(rs.getInt("User_ID"), rs.getDouble("Height"), rs.getDouble("Weight"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  // Retrieve detailed patient profiles
  public static DefaultTableModel getPatientProfiles() {
    try (Connection conn = DatabaseManager.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT * FROM PatientProfile")) {
      DefaultTableModel model = new DefaultTableModel(
          new Object[] { "User ID", "First Name", "Last Name", "DOB", "Sex", "Phone", "Email", "Street", "City",
              "State", "Zip", "Height", "Weight" },
          0);
      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("User_ID"),
            rs.getString("First_Name"),
            rs.getString("Last_Name"),
            rs.getDate("Date_of_Birth"),
            rs.getString("Sex"),
            rs.getString("Phone_No"),
            rs.getString("Email"),
            rs.getString("Street"),
            rs.getString("City"),
            rs.getString("State"),
            rs.getString("Zip_Code"),
            rs.getDouble("Height"),
            rs.getDouble("Weight")
        });
      }
      return model;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
