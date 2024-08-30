package com.mvs;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DepartmentModel provides methods to interact with the department table in the database.
 * It supports fetching, adding, updating, and deleting department records.
 */
public class DepartmentModel {

    /**
     * Returns a DefaultTableModel containing all departments.
     * 
     * @return a DefaultTableModel with department data
     */
    public static DefaultTableModel getDepartmentsTableModel() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Department No", "Department Name", "Branch No"}, 0);
        String query = "SELECT Department_No, Department_Name, Branch_No FROM department";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("Department_No"), rs.getString("Department_Name"), rs.getInt("Branch_No")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    /**
     * Adds a new department to the database.
     * 
     * @param departmentName the name of the new department
     * @param branchNo the branch number associated with the department
     */
    public static void addDepartment(String departmentName, int branchNo) {
        String query = "INSERT INTO department (Department_Name, Branch_No) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, departmentName);
            stmt.setInt(2, branchNo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Updates an existing department in the database.
     * 
     * @param departmentNo the department number to update
     * @param departmentName the new name for the department
     * @param branchNo the new branch number for the department
     */
    public static void updateDepartment(int departmentNo, String departmentName, int branchNo) {
        String query = "UPDATE department SET Department_Name = ?, Branch_No = ? WHERE Department_No = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, departmentName);
            stmt.setInt(2, branchNo);
            stmt.setInt(3, departmentNo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Deletes a department from the database.
     * 
     * @param departmentNo the department number to delete
     */
    public static void deleteDepartment(int departmentNo) {
        String query = "DELETE FROM department WHERE Department_No = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, departmentNo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
