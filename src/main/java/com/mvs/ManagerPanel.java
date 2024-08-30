package com.mvs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;

public class ManagerPanel extends JPanel {
  private JTable table;
  private DefaultTableModel model;
  private JButton btnLoad, btnSearch;
  private JTextField searchField;
  private int managerUserId;

  public ManagerPanel(int managerUserId) {
    this.managerUserId = managerUserId;
    setLayout(new BorderLayout());
    initializeUI();
  }

  private void initializeUI() {
    // Create a table model with columns: Doctor ID, First Name, Last Name, Specialization
    model = new DefaultTableModel(new Object[] { "Doctor ID", "First Name", "Last Name", "Specialization" }, 0);
    table = new JTable(model);
    add(new JScrollPane(table), BorderLayout.CENTER);

    // Create a panel for buttons and search field
    JPanel northPanel = new JPanel(new FlowLayout());

    // Load Doctors button to load all doctors supervised by the manager
    btnLoad = new JButton("Load Doctors");
    btnLoad.addActionListener(e -> loadDoctors());
    northPanel.add(btnLoad);

    // Search field for entering search text
    searchField = new JTextField(20);
    btnSearch = new JButton("Search");
    btnSearch.addActionListener(e -> searchEmployees(searchField.getText()));
    northPanel.add(searchField);
    northPanel.add(btnSearch);

    add(northPanel, BorderLayout.NORTH);
  }

  private void loadDoctors() {
    model.setRowCount(0); // Clear existing data in the table
    String query = "SELECT d.User_ID, u.First_Name, u.Last_Name, d.Specialization " +
        "FROM DOCTOR d " +
        "JOIN USER u ON d.User_ID = u.User_ID " +
        "WHERE d.Department_No IN (SELECT Department_Supervised_ID FROM MANAGER WHERE User_ID = ?)";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setInt(1, managerUserId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int userId = rs.getInt("User_ID");
        String firstName = rs.getString("First_Name");
        String lastName = rs.getString("Last_Name");
        String specialization = rs.getString("Specialization");
        model.addRow(new Object[] { userId, firstName, lastName, specialization });
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error loading doctors: " + ex.getMessage(), "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  private void searchEmployees(String searchText) {
    model.setRowCount(0); // Clear existing data in the table
    String query = "SELECT d.User_ID, u.First_Name, u.Last_Name, d.Specialization " +
        "FROM DOCTOR d " +
        "JOIN USER u ON d.User_ID = u.User_ID " +
        "WHERE (u.First_Name = ? OR u.Last_Name = ? OR d.Specialization = ? OR d.User_ID = ?) AND " +
        "d.Department_No IN (SELECT Department_Supervised_ID FROM MANAGER WHERE User_ID = ?)";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setString(1, searchText);
      stmt.setString(2, searchText);
      stmt.setString(3, searchText);
      try {
        int userId = Integer.parseInt(searchText);
        stmt.setInt(4, userId);
      } catch (NumberFormatException e) {
        stmt.setInt(4, -1); // Set an impossible ID if not a valid number
      }
      stmt.setInt(5, managerUserId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int userId = rs.getInt("User_ID");
        String firstName = rs.getString("First_Name");
        String lastName = rs.getString("Last_Name");
        String specialization = rs.getString("Specialization");
        model.addRow(new Object[] { userId, firstName, lastName, specialization });
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error searching doctors: " + ex.getMessage(), "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
