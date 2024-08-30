package com.mvs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

/**
 * DepartmentManagerPanel is a JPanel that allows department managers to manage doctors in their department.
 * It provides functionality to load, search, and update the salary of doctors.
 */
public class DepartmentManagerPanel extends JPanel {
  private JTable table;
  private DefaultTableModel model;
  private JButton btnLoad, btnSearch, btnUpdateSalary;
  private JTextField searchField;
  private int managerUserId;

  /**
   * Constructs a DepartmentManagerPanel with the specified manager user ID.
   * 
   * @param managerUserId the ID of the department manager
   */
  public DepartmentManagerPanel(int managerUserId) {
    this.managerUserId = managerUserId;
    setLayout(new BorderLayout());
    setLookAndFeel();
    initializeUI();
  }

  /**
   * Initializes the user interface components.
   */
  private void initializeUI() {
    model = new DefaultTableModel(new Object[] { "Doctor ID", "First Name", "Last Name", "Specialization", "Salary" }, 0);
    table = new JTable(model);
    add(new JScrollPane(table), BorderLayout.CENTER);

    JPanel northPanel = new JPanel(new FlowLayout());
    btnLoad = new JButton("Load Doctors");
    btnLoad.addActionListener(e -> loadDoctors());
    northPanel.add(btnLoad);

    searchField = new JTextField(20);
    btnSearch = new JButton("Search");
    btnSearch.addActionListener(e -> searchDoctors(searchField.getText().trim()));
    northPanel.add(searchField);
    northPanel.add(btnSearch);

    btnUpdateSalary = new JButton("Update Salary");
    btnUpdateSalary.addActionListener(e -> showSalaryUpdateDialog());
    northPanel.add(btnUpdateSalary);

    add(northPanel, BorderLayout.NORTH);
  }

  /**
   * Sets the look and feel of the UI to Nimbus.
   */
  private void setLookAndFeel() {
    try {
      UIManager.setLookAndFeel(new javax.swing.plaf.nimbus.NimbusLookAndFeel());
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads the doctors managed by the department manager and displays them in the table.
   */
  private void loadDoctors() {
    model.setRowCount(0);
    String query = "SELECT d.User_ID, u.First_Name, u.Last_Name, d.Specialization, d.Salary " +
        "FROM DOCTOR d " +
        "JOIN USER u ON d.User_ID = u.User_ID " +
        "WHERE d.Department_No IN (SELECT Department_No FROM DEPARTMENT WHERE Manager_ID = ?)";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setInt(1, managerUserId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        model.addRow(new Object[] { rs.getInt("User_ID"), rs.getString("First_Name"),
            rs.getString("Last_Name"), rs.getString("Specialization"), rs.getDouble("Salary") });
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error loading doctors: " + ex.getMessage(), "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Searches for doctors based on the search text and displays the results in the table.
   * 
   * @param searchText the text to search for
   */
  private void searchDoctors(String searchText) {
    model.setRowCount(0);
    String query = "SELECT d.User_ID, u.First_Name, u.Last_Name, d.Specialization, d.Salary " +
        "FROM DOCTOR d " +
        "JOIN USER u ON d.User_ID = u.User_ID " +
        "WHERE (u.First_Name = ? OR u.Last_Name = ? OR CONCAT(u.First_Name, ' ', u.Last_Name) = ? OR d.Specialization = ?) AND "
        +
        "d.Department_No IN (SELECT Department_No FROM DEPARTMENT WHERE Manager_ID = ?)";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, searchText);
      stmt.setString(2, searchText);
      stmt.setString(3, searchText);
      stmt.setString(4, searchText);
      stmt.setInt(5, managerUserId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        model.addRow(new Object[] { rs.getInt("User_ID"), rs.getString("First_Name"),
            rs.getString("Last_Name"), rs.getString("Specialization"), rs.getDouble("Salary") });
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error searching doctors: " + ex.getMessage(), "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Shows a dialog to update the salary of the selected doctor.
   */
  private void showSalaryUpdateDialog() {
    int selectedRow = table.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Please select a doctor to update.", "Update Error",
          JOptionPane.WARNING_MESSAGE);
      return;
    }

    int userId = (int) model.getValueAt(selectedRow, 0);
    double currentSalary = (double) model.getValueAt(selectedRow, 4);

    String newSalaryStr = JOptionPane.showInputDialog(this, "Enter new salary:", currentSalary);
    if (newSalaryStr != null && !newSalaryStr.isEmpty()) {
      try {
        double newSalary = Double.parseDouble(newSalaryStr);
        updateDoctorSalary(userId, newSalary);
      } catch (NumberFormatException ex) {
        JOptionPane.showMessageDialog(this, "Invalid salary format.", "Input Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * Updates the salary of the specified doctor in the database.
   * 
   * @param userId the ID of the doctor
   * @param newSalary the new salary of the doctor
   */
  private void updateDoctorSalary(int userId, double newSalary) {
    String query = "UPDATE DOCTOR SET Salary = ? WHERE User_ID = ?";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setDouble(1, newSalary);
      stmt.setInt(2, userId);
      int affectedRows = stmt.executeUpdate();
      if (affectedRows > 0) {
        JOptionPane.showMessageDialog(this, "Doctor's salary updated successfully.", "Update Successful",
            JOptionPane.INFORMATION_MESSAGE);
        loadDoctors(); // Refresh the list to show the updated salary
      } else {
        JOptionPane.showMessageDialog(this, "No rows affected. Please try again.", "Update Failed",
            JOptionPane.ERROR_MESSAGE);
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error updating doctor's salary: " + ex.getMessage(), "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
