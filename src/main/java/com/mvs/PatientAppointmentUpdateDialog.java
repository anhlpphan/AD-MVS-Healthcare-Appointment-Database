package com.mvs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

public class PatientAppointmentUpdateDialog extends JDialog {
  private final int patientUserId;
  private final int appointmentId;
  private LocalDate newDate;
  private Time newStartTime;
  private Time newEndTime;
  private JComboBox<String> timeslotComboBox;
  private CalendarDatePicker datePicker;
  private boolean updated = false;
  private JLabel selectedDateLabel;

  private LocalDate oldDate;
  private Time oldStartTime;
  private Time oldEndTime;

  // Constructor
  public PatientAppointmentUpdateDialog(Frame parent, int patientUserId, int appointmentId) {
    super(parent, "Update Appointment", true);
    this.patientUserId = patientUserId;
    this.appointmentId = appointmentId;
    fetchCurrentAppointmentDetails(); // Fetch current appointment details
    setupUI(); // Set up the UI
    setLocationRelativeTo(parent); // Center the dialog
  }

  // Fetch current appointment details from the database
  private void fetchCurrentAppointmentDetails() {
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement("SELECT Date, Start_Time, End_Time FROM appointment WHERE Appointment_ID = ?")) {
      stmt.setInt(1, appointmentId);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        oldDate = rs.getDate("Date").toLocalDate();
        oldStartTime = rs.getTime("Start_Time");
        oldEndTime = rs.getTime("End_Time");
      }
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "Failed to fetch appointment details: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  // Set up the UI components
  private void setupUI() {
    setLayout(new BorderLayout());
    JPanel panel = new JPanel(new GridLayout(0, 1));

    datePicker = new CalendarDatePicker(null);
    JButton openDatePickerButton = new JButton("Pick Date");
    selectedDateLabel = new JLabel("-- No Date Selected --");

    openDatePickerButton.addActionListener(e -> {
      datePicker.setVisible(true);
      newDate = datePicker.getSelectedDate();
      if (newDate != null) {
        selectedDateLabel.setText("Selected Date: " + newDate.toString());
        loadAvailableTimeslots(); // Load available timeslots based on the selected date
      }
    });

    timeslotComboBox = new JComboBox<>();
    JButton updateButton = new JButton("Update");
    updateButton.addActionListener(this::performUpdate);

    panel.add(new JLabel("Select New Date:"));
    panel.add(openDatePickerButton);
    panel.add(selectedDateLabel);

    panel.add(new JLabel("Select New Timeslot:"));
    panel.add(timeslotComboBox);
    panel.add(updateButton);

    add(panel, BorderLayout.CENTER);
    setSize(300, 200);
  }

  // Load available timeslots for the selected date
  private void loadAvailableTimeslots() {
    timeslotComboBox.removeAllItems();
    String query = "SELECT da.start_time, da.end_time " +
                   "FROM doctor_availability da " +
                   "WHERE da.doctor_user_id = (SELECT Doctor_User_ID FROM appointment WHERE Appointment_ID = ?) " +
                   "AND da.date = ? AND da.is_available = TRUE " +
                   "AND NOT EXISTS (" +
                   "    SELECT 1 FROM appointment a " +
                   "    WHERE a.Doctor_User_ID = da.doctor_user_id AND a.Date = da.date " +
                   "    AND a.Appointment_ID <> ? AND " +
                   "    NOT (a.Start_Time >= da.end_time OR a.End_Time <= da.start_time) " +
                   "    AND a.Status IN ('Booked', 'Rescheduled')" +
                   ")";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setInt(1, appointmentId);
      stmt.setDate(2, java.sql.Date.valueOf(newDate));
      stmt.setInt(3, appointmentId);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Time startTime = rs.getTime("start_time");
        Time endTime = rs.getTime("end_time");
        String timeSlot = startTime.toString() + " - " + endTime.toString();
        timeslotComboBox.addItem(timeSlot);
      }
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "Failed to load timeslots: " + ex.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  // Perform the update action
  private void performUpdate(ActionEvent e) {
    if (newDate == null || timeslotComboBox.getSelectedItem() == null) {
      JOptionPane.showMessageDialog(this, "Please select a date and time.", "Update Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    String timeslot = (String) timeslotComboBox.getSelectedItem();
    String[] parts = timeslot.split(" - ");
    newStartTime = Time.valueOf(parts[0].trim());
    newEndTime = Time.valueOf(parts[1].trim());

    if (newDate.equals(oldDate) && newStartTime.equals(oldStartTime) && newEndTime.equals(oldEndTime)) {
      JOptionPane.showMessageDialog(this, "You are trying to update to the same date, start time, and end time. Please select a different date or time.", "Update Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (updateAppointmentInDatabase()) {
      JOptionPane.showMessageDialog(this, "Appointment updated successfully.", "Success", JOptionPane.INFORMATION_MESSAGE);
      updated = true;
      dispose(); // Close the update dialog
      AppointmentsPanel parentPanel = (AppointmentsPanel) getParent();
      parentPanel.resetFields(); // Reset fields in the main panel
      parentPanel.loadUpcomingAppointments(); // Refresh the appointments list
    } else {
      JOptionPane.showMessageDialog(this, "Failed to update appointment.", "Update Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  // Update the appointment in the database
  private boolean updateAppointmentInDatabase() {
    String updateQuery = "UPDATE appointment SET date = ?, start_time = ?, end_time = ?, status = 'Rescheduled' WHERE appointment_id = ?";
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement stmt = conn.prepareStatement(updateQuery)) {
      stmt.setDate(1, java.sql.Date.valueOf(newDate));
      stmt.setTime(2, newStartTime);
      stmt.setTime(3, newEndTime);
      stmt.setInt(4, appointmentId);

      int affectedRows = stmt.executeUpdate();
      return affectedRows > 0;
    } catch (SQLException ex) {
      System.err.println("SQL Error during update: " + ex.getMessage());
      return false;
    }
  }

  // Check if the appointment was updated successfully
  public boolean isUpdated() {
    return updated;
  }
}
