package com.mvs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.swing.border.TitledBorder;

public class TimeSettingDialog extends JDialog {
  private final List<LocalDate> selectedDates;
  private final int doctorUserId;
  private JTextField globalStartTimeField, globalEndTimeField;
  private JButton autofillButton;
  private Map<Component, TimeSlot> initialTimeSlots = new HashMap<>(); // Store initial values

  public TimeSettingDialog(Frame owner, String title, boolean modal, List<LocalDate> selectedDates, int doctorUserId) {
    super(owner, title, modal);
    this.selectedDates = selectedDates;
    this.doctorUserId = doctorUserId;
    setSize(500, 700);
    setLocationRelativeTo(owner);
    try {
      UIManager.setLookAndFeel(new javax.swing.plaf.nimbus.NimbusLookAndFeel());
    } catch (UnsupportedLookAndFeelException e) {
      e.printStackTrace();
    }
    initializeUI();
  }

  private void initializeUI() {
    setLayout(new BorderLayout());

    JPanel autofillPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    globalStartTimeField = new JTextField(5);
    globalEndTimeField = new JTextField(5);
    autofillButton = new JButton("Autofill Unfilled Slots");
    autofillButton.addActionListener(this::autofillAllSlots);

    autofillPanel.add(new JLabel("Global Start Time (HH:mm):"));
    autofillPanel.add(globalStartTimeField);
    autofillPanel.add(new JLabel("Global End Time (HH:mm):"));
    autofillPanel.add(globalEndTimeField);
    autofillPanel.add(autofillButton);

    JPanel datesPanel = new JPanel(new GridLayout(0, 1));
    JScrollPane scrollPane = new JScrollPane(datesPanel);
    scrollPane.setVerticalScrollBarPolicy(ScrollPaneConstants.VERTICAL_SCROLLBAR_ALWAYS);

    for (LocalDate date : selectedDates) {
      JPanel datePanel = new JPanel(new GridBagLayout());
      datePanel.setBorder(BorderFactory.createTitledBorder(date.toString()));

      GridBagConstraints gbc = new GridBagConstraints();
      gbc.gridwidth = GridBagConstraints.REMAINDER;
      gbc.weightx = 1;
      gbc.fill = GridBagConstraints.HORIZONTAL;

      JPanel timeSlotsPanel = new JPanel();
      timeSlotsPanel.setLayout(new BoxLayout(timeSlotsPanel, BoxLayout.Y_AXIS));
      addTimeSlot(timeSlotsPanel);

      JButton addButton = new JButton("Add Timeslot");
      addButton.addActionListener(e -> addTimeSlot(timeSlotsPanel));

      datePanel.add(timeSlotsPanel, gbc);
      datePanel.add(addButton, gbc);
      datesPanel.add(datePanel);
    }

    JButton confirmButton = new JButton("Confirm Schedule");
    confirmButton.addActionListener(this::handleScheduleConfirmation);

    add(autofillPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);
    add(confirmButton, BorderLayout.SOUTH);
  }

  private void addTimeSlot(JPanel panel) {
    JPanel timeSlotPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
    JTextField startTimeField = new JTextField(5);
    JTextField endTimeField = new JTextField(5);
    JButton removeButton = new JButton("Remove");
    removeButton.addActionListener(e -> {
      panel.remove(timeSlotPanel);
      panel.revalidate();
      panel.repaint();
    });

    timeSlotPanel.add(new JLabel("Start Time (HH:mm):"));
    timeSlotPanel.add(startTimeField);
    timeSlotPanel.add(new JLabel("End Time (HH:mm):"));
    timeSlotPanel.add(endTimeField);
    timeSlotPanel.add(removeButton);
    panel.add(timeSlotPanel);
    panel.revalidate();
    panel.repaint();

    // Store initial values when adding time slot
    initialTimeSlots.put(timeSlotPanel, new TimeSlot(startTimeField.getText(), endTimeField.getText()));
  }

  private void autofillAllSlots(ActionEvent e) {
    String startTime = globalStartTimeField.getText();
    String endTime = globalEndTimeField.getText();
    if (!isValidTime(startTime) || !isValidTime(endTime)) {
      JOptionPane.showMessageDialog(this, "Please enter valid global start and end times.", "Invalid Time",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    LocalTime start = LocalTime.parse(startTime, DateTimeFormatter.ofPattern("H:mm"));
    LocalTime end = LocalTime.parse(endTime, DateTimeFormatter.ofPattern("H:mm"));

    if (end.isBefore(start)) {
      JOptionPane.showMessageDialog(this, "End time must be after start time.", "Time Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    // Clear the map to store fresh initial values
    initialTimeSlots.clear();

    // Capture the initial values before autofilling
    Component[] datePanels = ((JPanel) ((JScrollPane) getContentPane().getComponent(1)).getViewport().getView())
        .getComponents();
    for (Component comp : datePanels) {
      JPanel datePanel = (JPanel) comp;
      JPanel timeSlotsPanel = (JPanel) datePanel.getComponent(0);
      for (Component timeComp : timeSlotsPanel.getComponents()) {
        JPanel timeSlotPanel = (JPanel) timeComp;
        JTextField startTimeField = (JTextField) timeSlotPanel.getComponent(1);
        JTextField endTimeField = (JTextField) timeSlotPanel.getComponent(3);
        initialTimeSlots.put(timeSlotPanel, new TimeSlot(startTimeField.getText(), endTimeField.getText()));
      }
    }

    // Proceed with autofill as before
    Map<LocalDate, String> autofillResults = new HashMap<>();
    for (Component comp : datePanels) {
      JPanel datePanel = (JPanel) comp;
      LocalDate date = LocalDate.parse(((TitledBorder) datePanel.getBorder()).getTitle());
      JPanel timeSlotsPanel = (JPanel) datePanel.getComponent(0);
      if (checkAndFillTimeSlots(timeSlotsPanel, start, end)) {
        autofillResults.put(date, "Autofilled without overlap.");
      } else {
        autofillResults.put(date, "Cannot autofill due to overlap.");
      }
    }

    displayAutofillResults(autofillResults);
  }

  private boolean checkAndFillTimeSlots(JPanel timeSlotsPanel, LocalTime start, LocalTime end) {
    boolean canAutofill = true;
    for (Component comp : timeSlotsPanel.getComponents()) {
      JTextField existingStartTime = (JTextField) ((JPanel) comp).getComponent(1);
      JTextField existingEndTime = (JTextField) ((JPanel) comp).getComponent(3);
      if (!existingStartTime.getText().isEmpty() && !existingEndTime.getText().isEmpty()) {
        LocalTime existingStart = LocalTime.parse(existingStartTime.getText(), DateTimeFormatter.ofPattern("H:mm"));
        LocalTime existingEnd = LocalTime.parse(existingEndTime.getText(), DateTimeFormatter.ofPattern("H:mm"));
        // Check for overlap
        if ((start.isBefore(existingEnd) && start.isAfter(existingStart)) ||
            (end.isAfter(existingStart) && end.isBefore(existingEnd))) {
          canAutofill = false; // Overlap detected
          break;
        }
        // Check if start time or end time is the same
        if (start.equals(existingStart) || end.equals(existingEnd) || start.equals(existingEnd)
            || end.equals(existingStart)) {
          canAutofill = false; // Overlap detected
          break;
        }
      }
    }

    if (canAutofill) {
      // If no overlaps, fill the first empty slot
      for (Component comp : timeSlotsPanel.getComponents()) {
        JTextField startTimeField = (JTextField) ((JPanel) comp).getComponent(1);
        JTextField endTimeField = (JTextField) ((JPanel) comp).getComponent(3);
        if (startTimeField.getText().isEmpty() && endTimeField.getText().isEmpty()) {
          startTimeField.setText(start.toString());
          endTimeField.setText(end.toString());
          return true; // Filled successfully
        }
      }
    }

    return false; // No empty slots to fill or overlap detected
  }

  private boolean hasAdditionalTimeSlots(LocalDate date) {
    for (Component comp : ((JPanel) ((JScrollPane) getContentPane().getComponent(1)).getViewport().getView())
        .getComponents()) {
      JPanel datePanel = (JPanel) comp;
      LocalDate panelDate = LocalDate.parse(((TitledBorder) datePanel.getBorder()).getTitle());
      if (panelDate.equals(date)) {
        JPanel timeSlotsPanel = (JPanel) datePanel.getComponent(0);
        // Check if there are additional time slots other than the first one
        return timeSlotsPanel.getComponentCount() > 1;
      }
    }
    return false; // No additional time slots found
  }

  private void displayAutofillResults(Map<LocalDate, String> results) {
    StringBuilder message = new StringBuilder("<html>Autofill Results:<br>");
    boolean overlapDetected = false;
    for (Map.Entry<LocalDate, String> entry : results.entrySet()) {
      LocalDate date = entry.getKey();
      String result = entry.getValue();
      if (result.equals("Overlap detected, not autofilled.") || result.equals("Conflicting start/end times.")) {
        if (hasAdditionalTimeSlots(date)) {
          overlapDetected = true;
          message.append(date.toString()).append(": ").append(result).append("<br>");
        } else {
          message.append(date.toString()).append(": No additional time slots available to autofill.<br>");
        }
      } else {
        message.append(date.toString()).append(": ").append(result).append("<br>");
      }
    }

    message.append("</html>");

    if (overlapDetected) {
      JOptionPane.showMessageDialog(this, message.toString(), "Overlap Detected", JOptionPane.ERROR_MESSAGE);
    } else {
      int response = JOptionPane.showConfirmDialog(this, message.toString(), "Confirm Autofill",
          JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
      if (response == JOptionPane.YES_OPTION) {
        JOptionPane.showMessageDialog(this, "Changes accepted.", "Success", JOptionPane.INFORMATION_MESSAGE);
      } else {
        resetFields(); // Reset fields if they do not accept
      }
    }
  }

  private void resetFields() {
    // Reset fields using stored initial values
    for (Map.Entry<Component, TimeSlot> entry : initialTimeSlots.entrySet()) {
      JPanel timeSlotPanel = (JPanel) entry.getKey();
      JTextField startTimeField = (JTextField) timeSlotPanel.getComponent(1);
      JTextField endTimeField = (JTextField) timeSlotPanel.getComponent(3);
      TimeSlot initialTimeSlot = entry.getValue();
      startTimeField.setText(initialTimeSlot.startTime);
      endTimeField.setText(initialTimeSlot.endTime);
    }
  }

  private void handleScheduleConfirmation(ActionEvent e) {
    Connection conn = null; // Declare the connection object outside the try block to ensure its scope
                            // covers the entire method.
    try {
      conn = DatabaseManager.getConnection(); // Initialize the connection.
      conn.setAutoCommit(false); // Start transaction control.

      for (Component comp : ((JPanel) ((JScrollPane) getContentPane().getComponent(1)).getViewport().getView())
          .getComponents()) {
        JPanel datePanel = (JPanel) comp;
        LocalDate date = LocalDate.parse(((TitledBorder) datePanel.getBorder()).getTitle());
        JPanel timeSlotsPanel = (JPanel) datePanel.getComponent(0);

        for (Component timeComp : timeSlotsPanel.getComponents()) {
          JTextField startTimeField = (JTextField) ((JPanel) timeComp).getComponent(1);
          JTextField endTimeField = (JTextField) ((JPanel) timeComp).getComponent(3);
          if (isValidTime(startTimeField.getText()) && isValidTime(endTimeField.getText())) {
            updateDoctorAvailability(conn, doctorUserId, date, startTimeField.getText(), endTimeField.getText());
          } else {
            JOptionPane.showMessageDialog(this, "Invalid time format.", "Error", JOptionPane.ERROR_MESSAGE);
            return; // Stop execution and report error
          }
        }
      }
      conn.commit(); // Commit the transaction
      JOptionPane.showMessageDialog(this, "Schedule updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
      dispose(); // Close dialog after successful update
    } catch (SQLException ex) {
      if (conn != null) {
        try {
          conn.rollback(); // Rollback on error
        } catch (SQLException exRollback) {
          JOptionPane.showMessageDialog(this, "Error during rollback: " + exRollback.getMessage(), "Database Error",
              JOptionPane.ERROR_MESSAGE);
        }
      }
      JOptionPane.showMessageDialog(this, "Failed to update schedule: " + ex.getMessage(), "Database Error",
          JOptionPane.ERROR_MESSAGE);
    } finally {
      if (conn != null) {
        try {
          conn.close(); // Always close the connection in the finally block to ensure it's closed even
                        // if an error occurs.
        } catch (SQLException exClose) {
          JOptionPane.showMessageDialog(this, "Error closing database connection: " + exClose.getMessage(),
              "Database Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    }
  }

  private boolean isValidTime(String time) {
    try {
      LocalTime.parse(time, DateTimeFormatter.ofPattern("H:mm"));
      return true;
    } catch (DateTimeParseException ex) {
      return false;
    }
  }

  private void updateDoctorAvailability(Connection conn, int userId, LocalDate date, String startTime, String endTime)
      throws SQLException {
    if (startTime.equals(endTime)) {
      JOptionPane.showMessageDialog(this, "Appointments cannot start and end at the same minute.", "Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    String query = "INSERT INTO doctor_availability (doctor_user_id, date, start_time, end_time) VALUES (?, ?, ?, ?) " +
        "ON DUPLICATE KEY UPDATE start_time = VALUES(start_time), end_time = VALUES(end_time)";
    try (PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setInt(1, userId);
      stmt.setDate(2, java.sql.Date.valueOf(date));
      stmt.setString(3, startTime);
      stmt.setString(4, endTime);
      stmt.executeUpdate();
    }
  }

  private static class TimeSlot {
    String startTime;
    String endTime;

    TimeSlot(String startTime, String endTime) {
      this.startTime = startTime;
      this.endTime = endTime;
    }
  }
}
