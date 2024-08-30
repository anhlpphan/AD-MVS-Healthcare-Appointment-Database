package com.mvs;

import javax.swing.table.DefaultTableModel;
import java.sql.*;
import java.util.Date;

public class ManagerModel {

  /**
   * Retrieves a table model containing all manager records.
   * @return DefaultTableModel containing manager data.
   */
  public static DefaultTableModel getManagerTableModel() {
    try (Connection conn = DatabaseManager.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT User_ID, Start_Date, End_Date, Office_Number, Department_Supervised_ID, Salary FROM MANAGER")) {
      DefaultTableModel model = new DefaultTableModel(
          new Object[] { "User_ID", "Start_Date", "End_Date", "Office_Number", "Department_Supervised_ID", "Salary" }, 0);
      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("User_ID"),
            rs.getDate("Start_Date"),
            rs.getDate("End_Date"),
            rs.getString("Office_Number"),
            rs.getInt("Department_Supervised_ID"),
            rs.getDouble("Salary")
        });
      }
      return model;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Updates a manager record in the database.
   * @param userId The ID of the manager.
   * @param startDate The start date of the manager.
   * @param endDate The end date of the manager.
   * @param officeNo The office number of the manager.
   * @param departmentSupervisedID The department supervised by the manager.
   * @param salary The salary of the manager.
   * @return true if the update was successful, false otherwise.
   */
  public static boolean updateManager(int userId, Date startDate, Date endDate, String officeNo,
                                      int departmentSupervisedID, double salary) {
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement(
            "UPDATE MANAGER SET Start_Date = ?, End_Date = ?, Office_Number = ?, Department_Supervised_ID = ?, Salary = ? WHERE User_ID = ?")) {
      ps.setDate(1, new java.sql.Date(startDate.getTime()));
      ps.setDate(2, new java.sql.Date(endDate.getTime()));
      ps.setString(3, officeNo);
      ps.setInt(4, departmentSupervisedID);
      ps.setDouble(5, salary);
      ps.setInt(6, userId);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Deletes a manager record from the database.
   * @param userId The ID of the manager.
   * @return true if the deletion was successful, false otherwise.
   */
  public static boolean deleteManager(int userId) {
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement("DELETE FROM MANAGER WHERE User_ID = ?")) {
      ps.setInt(1, userId);
      return ps.executeUpdate() > 0;
    } catch (SQLException e) {
      e.printStackTrace();
      return false;
    }
  }

  /**
   * Retrieves a manager record by its ID.
   * @param userId The ID of the manager.
   * @return Manager object if found, null otherwise.
   */
  public static Manager getManagerByID(int userId) {
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement ps = conn.prepareStatement("SELECT * FROM MANAGER WHERE User_ID = ?")) {
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        return new Manager(
            rs.getInt("User_ID"),
            rs.getDate("Start_Date"),
            rs.getDate("End_Date"),
            rs.getString("Office_Number"),
            rs.getInt("Department_Supervised_ID"),
            rs.getDouble("Salary"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }

  /**
   * Retrieves a table model containing manager oversight information.
   * @return DefaultTableModel containing manager oversight data.
   */
  public static DefaultTableModel getManagerOversight() {
    try (Connection conn = DatabaseManager.getConnection();
         Statement stmt = conn.createStatement();
         ResultSet rs = stmt.executeQuery("SELECT * FROM ManagerOversight")) {
      DefaultTableModel model = new DefaultTableModel(
          new Object[] { "User_ID", "First_Name", "Last_Name", "Department_Supervised_ID", "Department_Name", "Branch_Name" }, 0);
      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("User_ID"),
            rs.getString("First_Name"),
            rs.getString("Last_Name"),
            rs.getInt("Department_Supervised_ID"),
            rs.getString("Department_Name"),
            rs.getString("Branch_Name")
        });
      }
      return model;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Retrieves a table model containing doctors by department ID.
   * @param departmentId The ID of the department.
   * @return DefaultTableModel containing doctors data.
   */
  public static DefaultTableModel getDoctorsByDepartment(int departmentId) {
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(
            "SELECT d.User_ID, CONCAT(u.First_Name, ' ', u.Last_Name) AS Name, d.Specialization FROM DOCTOR d JOIN USER u ON d.User_ID = u.User_ID WHERE d.Department_No = ?")) {
      stmt.setInt(1, departmentId);
      ResultSet rs = stmt.executeQuery();
      DefaultTableModel model = new DefaultTableModel(new Object[] { "User_ID", "Name", "Specialization" }, 0);
      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("User_ID"),
            rs.getString("Name"),
            rs.getString("Specialization")
        });
      }
      return model;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Searches employees based on the search query.
   * @param searchQuery The query to search for.
   * @return DefaultTableModel containing search results.
   */
  public static DefaultTableModel searchEmployees(String searchQuery) {
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT * FROM USER WHERE CONCAT(First_Name, ' ', Last_Name) LIKE ? OR User_ID LIKE ?")) {
      stmt.setString(1, "%" + searchQuery + "%");
      stmt.setString(2, "%" + searchQuery + "%");
      ResultSet rs = stmt.executeQuery();
      DefaultTableModel model = new DefaultTableModel(new Object[] { "User_ID", "First_Name", "Last_Name", "Email", "Salary" }, 0);
      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("User_ID"),
            rs.getString("First_Name"),
            rs.getString("Last_Name"),
            rs.getString("Email"),
            rs.getDouble("Salary") // Assuming Salary is a field you might want to see
        });
      }
      return model;
    } catch (SQLException e) {
      e.printStackTrace();
      return null;
    }
  }

  /**
   * Retrieves a doctor's information by their ID.
   * @param doctorId The ID of the doctor.
   * @return Doctor object if found, null otherwise.
   */
  public static Doctor getDoctorInformation(int doctorId) {
    String query = "SELECT d.User_ID, d.License_No, d.Specialization, d.Years_Of_Experience, d.Salary, d.Department_No " +
                   "FROM DOCTOR d " +
                   "JOIN USER u ON d.User_ID = u.User_ID " +
                   "WHERE d.User_ID = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setInt(1, doctorId);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        return new Doctor(
            rs.getInt("User_ID"),
            rs.getString("License_No"),
            rs.getString("Specialization"),
            rs.getInt("Years_Of_Experience"),
            rs.getDouble("Salary"),
            rs.getInt("Department_No")); // Including Department_No in the constructor
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null;
  }
}
