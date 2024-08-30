package com.mvs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;

/**
 * AdminSearchPanel is a JPanel that allows admin users to search for users in the database 
 * by first name and last name. The search results are displayed in a JTable.
 */
public class AdminSearchPanel extends JPanel {
  private JTextField txtFirstName, txtLastName;
  private JButton btnSearch;
  private JTable resultTable;
  private DefaultTableModel tableModel;

  /**
   * Constructs the AdminSearchPanel and initializes its components.
   */
  public AdminSearchPanel() {
    setLayout(new BorderLayout());
    
    // Create the search panel with text fields for first and last name
    JPanel searchPanel = new JPanel(new GridLayout(2, 2));
    txtFirstName = new JTextField();
    txtLastName = new JTextField();
    btnSearch = new JButton("Search");

    // Add components to the search panel
    searchPanel.add(new JLabel("First Name:"));
    searchPanel.add(txtFirstName);
    searchPanel.add(new JLabel("Last Name:"));
    searchPanel.add(txtLastName);
    add(searchPanel, BorderLayout.NORTH);

    // Add an action listener to the search button to perform the search
    btnSearch.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        performSearch(txtFirstName.getText().trim(), txtLastName.getText().trim());
      }
    });
    add(btnSearch, BorderLayout.SOUTH);

    // Set up the table model and result table to display search results
    tableModel = new DefaultTableModel();
    tableModel.setColumnIdentifiers(new Object[] { "User ID", "First Name", "Last Name", "Username", "Email", "Phone No" });
    resultTable = new JTable(tableModel);
    add(new JScrollPane(resultTable), BorderLayout.CENTER);
  }

  /**
   * Performs a search for users based on the given first name and last name.
   * The results are displayed in the result table.
   * 
   * @param firstName the first name to search for
   * @param lastName the last name to search for
   */
  private void performSearch(String firstName, String lastName) {
    // Clear previous results
    tableModel.setRowCount(0);

    String query = "SELECT User_ID, First_Name, Last_Name, Username, Email, Phone_No FROM USER WHERE First_Name LIKE ? AND Last_Name LIKE ?";
    try (
        Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/mvs_final_project", "default", "Password123!");
        PreparedStatement stmt = conn.prepareStatement(query)) {

      // Set parameters for the query
      stmt.setString(1, "%" + firstName + "%");
      stmt.setString(2, "%" + lastName + "%");

      // Execute the query and process the results
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        int userId = rs.getInt("User_ID");
        String fName = rs.getString("First_Name");
        String lName = rs.getString("Last_Name");
        String username = rs.getString("Username");
        String email = rs.getString("Email");
        String phone = rs.getString("Phone_No");

        // Add the result row to the table model
        tableModel.addRow(new Object[] { userId, fName, lName, username, email, phone });
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
  }
}
