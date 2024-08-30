package com.mvs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.Vector;

public class NotificationsPanel extends JPanel {
  private JTable notificationsTable; // Table to display notifications
  private JScrollPane scrollPane; // Scroll pane to hold the table
  private JButton deleteButton; // Button to mark notifications as read (delete)

  // Constructor to initialize the UI and load notifications
  public NotificationsPanel() {
    initializeUI(); // Set up the UI components
    loadNotifications(); // Load notifications from the database
  }

  // Method to initialize the UI components
  private void initializeUI() {
    setLayout(new BorderLayout()); // Set the layout manager
    notificationsTable = new JTable(); // Create the table
    notificationsTable.setModel(new DefaultTableModel(
        new Object[] { "Notification ID", "Date", "Start Time", "End Time", "Message", "Status" }, 0)); // Set table model
    scrollPane = new JScrollPane(notificationsTable); // Create scroll pane for the table
    add(scrollPane, BorderLayout.CENTER); // Add scroll pane to the center of the panel

    deleteButton = new JButton("Mark as Read"); // Create button to mark as read
    deleteButton.addActionListener(this::deleteNotification); // Add action listener for the button

    JButton refreshButton = new JButton("Refresh"); // Create button to refresh notifications
    refreshButton.addActionListener(e -> loadNotifications()); // Add action listener to refresh notifications

    JPanel buttonPanel = new JPanel(); // Create a panel for the buttons
    buttonPanel.add(deleteButton); // Add delete button to the panel
    buttonPanel.add(refreshButton); // Add refresh button to the panel
    add(buttonPanel, BorderLayout.SOUTH); // Add button panel to the bottom of the main panel
  }

  // Method to load notifications from the database
  private void loadNotifications() {
    DefaultTableModel model = (DefaultTableModel) notificationsTable.getModel(); // Get table model
    model.setRowCount(0); // Clear existing data

    try (Connection conn = DatabaseManager.getConnection(); // Get database connection
        PreparedStatement stmt = conn.prepareStatement("SELECT * FROM Notifications WHERE Status = 'Unread'")) { // SQL query to fetch unread notifications
      ResultSet rs = stmt.executeQuery(); // Execute query
      while (rs.next()) { // Iterate through the result set
        Vector<Object> row = new Vector<>(); // Create a new row
        row.add(rs.getInt("Notification_ID")); // Add notification ID
        row.add(rs.getDate("Date")); // Add date
        row.add(rs.getTime("Start_Time")); // Add start time
        row.add(rs.getTime("End_Time")); // Add end time
        row.add(rs.getString("Message")); // Add message
        row.add(rs.getString("Status")); // Add status
        model.addRow(row); // Add row to the table model
      }
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "Error loading notifications: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE); // Show error message
    }
  }

  // Method to mark a notification as read (delete it from the database)
  private void deleteNotification(ActionEvent e) {
    int selectedRow = notificationsTable.getSelectedRow(); // Get the selected row
    if (selectedRow >= 0) { // Check if a row is selected
      int notificationId = (int) notificationsTable.getValueAt(selectedRow, 0); // Get notification ID from the selected row
      String deleteQuery = "DELETE FROM Notifications WHERE Notification_ID = ?"; // SQL query to delete the notification
      try (Connection conn = DatabaseManager.getConnection(); // Get database connection
          PreparedStatement stmt = conn.prepareStatement(deleteQuery)) { // Prepare the SQL statement
        stmt.setInt(1, notificationId); // Set the notification ID parameter
        stmt.executeUpdate(); // Execute the delete statement
        ((DefaultTableModel) notificationsTable.getModel()).removeRow(selectedRow); // Remove the row from the table model
        JOptionPane.showMessageDialog(this, "Notification marked as read (deleted).", "Success", JOptionPane.INFORMATION_MESSAGE); // Show success message
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error deleting notification: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE); // Show error message
      }
    } else {
      JOptionPane.showMessageDialog(this, "Please select a notification to mark as read.", "Selection Error", JOptionPane.ERROR_MESSAGE); // Show error message if no row is selected
    }
  }
}
