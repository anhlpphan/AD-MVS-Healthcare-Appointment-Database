package com.mvs;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

/**
 * InsuranceModel provides methods to interact with the insurance records in the database.
 * It supports fetching, adding, updating, and deleting insurance records.
 */
public class InsuranceModel {

  /**
   * Establishes a connection to the database.
   * 
   * @return the database connection
   */
  private Connection connect() {
    Connection conn = null;
    try {
      conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mvs_final_project", "default", "Password123!");
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return conn;
  }

  /**
   * Retrieves a list of insurance records for a given user ID.
   * 
   * @param userID the ID of the user
   * @return a list of InsuranceRecord objects
   */
  public List<InsuranceRecord> getInsuranceRecords(int userID) {
    List<InsuranceRecord> records = new ArrayList<>();
    String sql = "SELECT Insurance_Card_No, Insurance_Company_Name, Insurance_Company_Phone_No, Primary_Doctor_User_ID, Exp_Date FROM INSURANCE WHERE User_ID = ?";
    try (Connection conn = this.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, userID);
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        records.add(new InsuranceRecord(
            rs.getString("Insurance_Card_No"), 
            userID,
            rs.getString("Insurance_Company_Name"), 
            rs.getString("Insurance_Company_Phone_No"),
            rs.getInt("Primary_Doctor_User_ID"), 
            rs.getDate("Exp_Date")));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return records;
  }

  /**
   * Adds a new insurance record to the database.
   * 
   * @param record the InsuranceRecord object to be added
   */
  public void addInsuranceRecord(InsuranceRecord record) {
    String sql = "INSERT INTO INSURANCE (Insurance_Card_No, User_ID, Insurance_Company_Name, Insurance_Company_Phone_No, Primary_Doctor_User_ID, Exp_Date) VALUES (?, ?, ?, ?, ?, ?)";
    try (Connection conn = this.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, record.getInsuranceCardNo());
      pstmt.setInt(2, record.getUserID());
      pstmt.setString(3, record.getInsuranceCompanyName());
      pstmt.setString(4, record.getInsuranceCompanyPhoneNo());
      pstmt.setInt(5, record.getPrimaryDoctorUserID());
      pstmt.setDate(6, new java.sql.Date(record.getExpDate().getTime()));
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  /**
   * Updates an existing insurance record in the database.
   * 
   * @param record the InsuranceRecord object with updated information
   */
  public void updateInsuranceRecord(InsuranceRecord record) {
    String sql = "UPDATE INSURANCE SET Insurance_Company_Name = ?, Insurance_Company_Phone_No = ?, Primary_Doctor_User_ID = ?, Exp_Date = ? WHERE Insurance_Card_No = ?";
    try (Connection conn = this.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, record.getInsuranceCompanyName());
      pstmt.setString(2, record.getInsuranceCompanyPhoneNo());
      pstmt.setInt(3, record.getPrimaryDoctorUserID());
      pstmt.setDate(4, new java.sql.Date(record.getExpDate().getTime()));
      pstmt.setString(5, record.getInsuranceCardNo());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println("Update failed: " + e.getMessage());
    }
  }

  /**
   * Deletes an insurance record from the database.
   * 
   * @param insuranceCardNo the insurance card number of the record to be deleted
   */
  public void deleteInsuranceRecord(String insuranceCardNo) {
    String sql = "DELETE FROM INSURANCE WHERE Insurance_Card_No = ?";
    try (Connection conn = this.connect();
         PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, insuranceCardNo);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
