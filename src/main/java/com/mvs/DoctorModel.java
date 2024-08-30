package com.mvs;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

/**
 * DoctorModel provides methods to interact with the doctor-related tables in the database.
 * It supports fetching, updating, and deleting doctor records, as well as fetching doctor appointments.
 */
public class DoctorModel {

  /**
   * Returns a DefaultTableModel containing all doctors.
   * 
   * @return a DefaultTableModel with doctor data
   */
  public static DefaultTableModel getDoctorTableModel() {
    try (Connection conn = DatabaseManager.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT User_ID, License_No, Specialization, Years_Of_Experience, Salary, Department_No FROM DOCTOR")) {
      DefaultTableModel model = new DefaultTableModel(
          new Object[] {"User_ID", "License_No", "Specialization", "Years_Of_Experience", "Salary", "Department_No"}, 0);
      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("User_ID"),
            rs.getString("License_No"),
            rs.getString("Specialization"),
            rs.getInt("Years_Of_Experience"),
            rs.getDouble("Salary"),
            rs.getInt("Department_No")
        });
      }
      return model;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Updates the details of a doctor in the database.
   * 
   * @param userId the ID of the doctor
   * @param licenseNo the license number of the doctor
   * @param specialization the specialization of the doctor
   * @param yearsOfExperience the years of experience of the doctor
   * @param salary the salary of the doctor
   * @param departmentNo the department number of the doctor
   * @return true if the update was successful, false otherwise
   */
  public static boolean updateDoctor(int userId, String licenseNo, String specialization, int yearsOfExperience, double salary, int departmentNo) {
    String sql = "UPDATE DOCTOR SET License_No = ?, Specialization = ?, Years_Of_Experience = ?, Salary = ?, Department_No = ? WHERE User_ID = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(sql)) {
      ps.setString(1, licenseNo);
      ps.setString(2, specialization);
      ps.setInt(3, yearsOfExperience);
      ps.setDouble(4, salary);
      ps.setInt(5, departmentNo);
      ps.setInt(6, userId);
      int affectedRows = ps.executeUpdate();
      return affectedRows > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Deletes a doctor from the database.
   * 
   * @param userId the ID of the doctor
   * @return true if the deletion was successful, false otherwise
   */
  public static boolean deleteDoctor(int userId) {
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement("DELETE FROM DOCTOR WHERE User_ID = ?")) {
      ps.setInt(1, userId);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Retrieves a doctor by their ID.
   * 
   * @param userId the ID of the doctor
   * @return a Doctor object if found, null otherwise
   */
  public static Doctor getDoctorByID(int userId) {
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT * FROM DOCTOR WHERE User_ID = ?")) {
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return new Doctor(
            rs.getInt("User_ID"),
            rs.getString("License_No"),
            rs.getString("Specialization"),
            rs.getInt("Years_Of_Experience"),
            rs.getDouble("Salary"),
            rs.getInt("Department_No"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Returns a DefaultTableModel containing detailed information about all doctors.
   * 
   * @return a DefaultTableModel with doctor information
   */
  public static DefaultTableModel getDoctorInformation() {
    try (Connection conn = DatabaseManager.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM DoctorInformation")) {
      DefaultTableModel model = new DefaultTableModel(
          new Object[] { "User_ID", "First_Name", "Last_Name", "Specialization", "Years_Of_Experience", "Phone_No", "Email" }, 0);
      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("User_ID"),
            rs.getString("First_Name"),
            rs.getString("Last_Name"),
            rs.getString("Specialization"),
            rs.getInt("Years_Of_Experience"),
            rs.getString("Phone_No"),
            rs.getString("Email")
        });
      }
      return model;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Returns a DefaultTableModel containing upcoming appointments for a specific doctor.
   * 
   * @param doctorUserId the ID of the doctor
   * @return a DefaultTableModel with appointment data
   */
  public static DefaultTableModel getDoctorAppointments(int doctorUserId) {
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(
             "SELECT * FROM APPOINTMENT WHERE Doctor_User_ID = ? AND Date >= CURDATE() ORDER BY Date, Start_Time")) {
      stmt.setInt(1, doctorUserId);
      ResultSet rs = stmt.executeQuery();
      DefaultTableModel model = new DefaultTableModel(
          new Object[] { "Appointment_ID", "Patient_User_ID", "Doctor_User_ID", "Location", "Start_Time", "End_Time", "Date" }, 0);
      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("Appointment_ID"),
            rs.getInt("Patient_User_ID"),
            rs.getInt("Doctor_User_ID"),
            rs.getString("Location"),
            rs.getTime("Start_Time"),
            rs.getTime("End_Time"),
            rs.getDate("Date")
        });
      }
      return model;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Returns a DefaultTableModel containing detailed information about a specific doctor.
   * 
   * @param doctorUserId the ID of the doctor
   * @return a DefaultTableModel with doctor information
   */
  public static DefaultTableModel getDoctorInformation(int doctorUserId) {
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(
             "SELECT User_ID, First_Name, Last_Name, Specialization, Years_Of_Experience, Phone_No, Email FROM doctorinformation WHERE User_ID = ?")) {
      stmt.setInt(1, doctorUserId);
      ResultSet rs = stmt.executeQuery();
      DefaultTableModel model = new DefaultTableModel(
          new Object[] { "User ID", "First Name", "Last Name", "Specialization", "Years Of Experience", "Phone No", "Email" }, 0);
      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("User_ID"),
            rs.getString("First_Name"),
            rs.getString("Last_Name"),
            rs.getString("Specialization"),
            rs.getInt("Years_Of_Experience"),
            rs.getString("Phone_No"),
            rs.getString("Email")
        });
      }
      return model;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }
}
