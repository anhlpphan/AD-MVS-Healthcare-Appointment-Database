package com.mvs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;

import java.awt.*;
import java.sql.*;

public class PatientNotificationsPanel extends JPanel {
  private JTable notificationsTable;
  private JScrollPane scrollPane;
  private int patientUserId; // Patient user ID to filter notifications
  private JButton refreshNotificationsButton;
  private JButton deleteNotificationButton;

  public PatientNotificationsPanel(int patientUserId) {
    this.patientUserId = patientUserId;
    initializeUI();
    loadNotifications();
  }

  private void initializeUI() {
    setLayout(new BorderLayout());
    notificationsTable = new JTable();
    notificationsTable.setModel(new DefaultTableModel(
        new Object[] { "Notification ID", "Date", "Message", "Status" }, 0));
    scrollPane = new JScrollPane(notificationsTable);
    add(scrollPane, BorderLayout.CENTER);

    // Panel for buttons
    JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    deleteNotificationButton = new JButton("Delete Notification");
    deleteNotificationButton.addActionListener(e -> deleteSelectedNotification());
    buttonPanel.add(deleteNotificationButton);

    // Refresh button
    refreshNotificationsButton = new JButton("Refresh Notifications");
    refreshNotificationsButton.addActionListener(e -> loadNotifications());
    buttonPanel.add(refreshNotificationsButton);

    add(buttonPanel, BorderLayout.SOUTH);
  }

  private void deleteSelectedNotification() {
    int selectedRow = notificationsTable.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Please select a notification to delete.", "Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    int notificationId = (Integer) notificationsTable.getValueAt(selectedRow, 0);
    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this notification?",
        "Confirm Deletion", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      try (Connection conn = DatabaseManager.getConnection()) {
        String deleteQuery = "DELETE FROM Notifications WHERE Notification_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(deleteQuery)) {
          pstmt.setInt(1, notificationId);
          pstmt.executeUpdate();
          JOptionPane.showMessageDialog(this, "Notification deleted successfully.", "Success",
              JOptionPane.INFORMATION_MESSAGE);
        }
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Failed to delete notification: " + ex.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
      }
      loadNotifications(); // Refresh the notifications display
    }
  }

  private void loadNotifications() {
    DefaultTableModel model = (DefaultTableModel) notificationsTable.getModel();
    model.setRowCount(0); // Clear existing data

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn
            .prepareStatement("SELECT * FROM Notifications WHERE Patient_User_ID = ? AND Status = 'Unread'")) {
      stmt.setInt(1, patientUserId);
      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("Notification_ID"),
            rs.getDate("Date"),
            rs.getString("Message"),
            rs.getString("Status")
        });
      }
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "Error loading notifications: " + ex.getMessage(), "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }
}
