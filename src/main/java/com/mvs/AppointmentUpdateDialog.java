package com.mvs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.time.LocalDate;

/**
 * AppointmentUpdateDialog is a JDialog that allows users to update an existing appointment.
 * Users can select a new date and timeslot for the appointment, and the dialog ensures
 * that the selected timeslot is available before updating the appointment in the database.
 */
public class AppointmentUpdateDialog extends JDialog {
  private final int doctorUserId;
  private final int appointmentId;
  private LocalDate newDate;
  private Time newStartTime;
  private Time newEndTime;
  private JComboBox<String> timeslotComboBox;
  private CalendarDatePicker datePicker;
  private boolean updated = false;
  private JLabel selectedDateLabel; // Label to display the selected date

  private LocalDate oldDate;
  private Time oldStartTime;
  private Time oldEndTime;

  /**
   * Constructs an AppointmentUpdateDialog with the specified parent frame, doctor user ID, and appointment ID.
   * 
   * @param parent the parent frame
   * @param doctorUserId the ID of the doctor for the appointment
   * @param appointmentId the ID of the appointment to update
   */
  public AppointmentUpdateDialog(Frame parent, int doctorUserId, int appointmentId) {
    super(parent, "Update Appointment", true);
    this.doctorUserId = doctorUserId;
    this.appointmentId = appointmentId;
    fetchCurrentAppointmentDetails();
    setupUI();
    setLocationRelativeTo(parent);
  }

  /**
   * Fetches the current details of the appointment from the database.
   */
  private void fetchCurrentAppointmentDetails() {
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn
            .prepareStatement("SELECT Date, Start_Time, End_Time FROM appointment WHERE Appointment_ID = ?")) {
      stmt.setInt(1, appointmentId);
      ResultSet rs = stmt.executeQuery();
      if (rs.next()) {
        oldDate = rs.getDate("Date").toLocalDate();
        oldStartTime = rs.getTime("Start_Time");
        oldEndTime = rs.getTime("End_Time");
      }
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "Failed to fetch appointment details: " + ex.getMessage(), "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Sets up the user interface components for the dialog.
   */
  private void setupUI() {
    setLayout(new BorderLayout());
    JPanel panel = new JPanel(new GridLayout(0, 1));

    datePicker = new CalendarDatePicker(null);
    JButton openDatePickerButton = new JButton("Pick Date");
    selectedDateLabel = new JLabel("-- No Date Selected --");

    // Add action listener to the date picker button
    openDatePickerButton.addActionListener(e -> {
      datePicker.setVisible(true);
      newDate = datePicker.getSelectedDate();
      if (newDate != null) {
        selectedDateLabel.setText("Selected Date: " + newDate.toString());
        loadAvailableTimeslots();
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

  /**
   * Loads the available timeslots for the selected doctor and date into the timeslot combo box.
   */
  private void loadAvailableTimeslots() {
    timeslotComboBox.removeAllItems();
    String query = """
        SELECT start_time, end_time
        FROM doctor_availability
        WHERE doctor_user_id = ?
          AND date = ?
          AND is_available = TRUE
          AND NOT EXISTS (
            SELECT 1
            FROM appointment
            WHERE Doctor_User_ID = ?
              AND Date = ?
              AND ((Start_Time <= ? AND End_Time > ?) OR (Start_Time < ? AND End_Time >= ?))
              AND Appointment_ID != ?
              AND Status IN ('Booked', 'Rescheduled')
          )
        """;

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setInt(1, doctorUserId);
      stmt.setDate(2, java.sql.Date.valueOf(newDate));
      stmt.setInt(3, doctorUserId);
      stmt.setDate(4, java.sql.Date.valueOf(newDate));
      stmt.setTime(5, oldStartTime);
      stmt.setTime(6, oldStartTime);
      stmt.setTime(7, oldEndTime);
      stmt.setTime(8, oldEndTime);
      stmt.setInt(9, appointmentId);

      ResultSet rs = stmt.executeQuery();
      while (rs.next()) {
        Time startTime = rs.getTime("start_time");
        Time endTime = rs.getTime("end_time");
        String timeSlot = startTime.toString() + " - " + endTime.toString();
        timeslotComboBox.addItem(timeSlot);
      }
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "Failed to load timeslots: " + ex.getMessage(), "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Performs the update of the appointment when the update button is clicked.
   * 
   * @param e the action event
   */
  private void performUpdate(ActionEvent e) {
    if (newDate == null || timeslotComboBox.getSelectedItem() == null) {
      JOptionPane.showMessageDialog(this, "Please select a date and time.", "Update Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    String selectedTimeslot = (String) timeslotComboBox.getSelectedItem();
    String[] parts = selectedTimeslot.split(" - ");
    newStartTime = Time.valueOf(parts[0].trim());
    newEndTime = Time.valueOf(parts[1].trim());

    // Check if the new date and time are different from the old ones
    if (!newDate.equals(oldDate) || !newStartTime.equals(oldStartTime) || !newEndTime.equals(oldEndTime)) {
      if (!isTimeSlotAvailable(newDate, newStartTime, newEndTime)) {
        JOptionPane.showMessageDialog(this, "This timeslot is already booked. Please select another timeslot.",
            "Booking Error", JOptionPane.ERROR_MESSAGE);
        return;
      }
    }

    if (updateAppointmentInDatabase()) {
      updateAvailability(oldDate, oldStartTime, oldEndTime, newDate, newStartTime, newEndTime);
      JOptionPane.showMessageDialog(this, "Appointment updated successfully.", "Success",
          JOptionPane.INFORMATION_MESSAGE);
      updated = true;
      dispose();
    } else {
      JOptionPane.showMessageDialog(this, "Failed to update appointment.", "Update Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Checks if the selected timeslot is available for the new appointment date and time.
   * 
   * @param date the new date for the appointment
   * @param startTime the new start time for the appointment
   * @param endTime the new end time for the appointment
   * @return true if the timeslot is available, false otherwise
   */
  private boolean isTimeSlotAvailable(LocalDate date, Time startTime, Time endTime) {
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "SELECT COUNT(*) FROM appointment WHERE Doctor_User_ID = ? AND Date = ? AND NOT (Start_Time >= ? OR End_Time <= ?) AND Status NOT IN ('Cancelled')")) {
      pstmt.setInt(1, doctorUserId);
      pstmt.setDate(2, java.sql.Date.valueOf(date));
      pstmt.setTime(3, endTime);
      pstmt.setTime(4, startTime);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next() && rs.getInt(1) == 0) {
        return true;
      }
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "Error checking time slot availability: " + ex.getMessage(), "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
    return false;
  }

  /**
   * Updates the appointment in the database with the new date and time.
   * 
   * @return true if the update was successful, false otherwise
   */
  private boolean updateAppointmentInDatabase() {
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(
            "UPDATE appointment SET Date = ?, Start_Time = ?, End_Time = ?, Status = 'Rescheduled' WHERE Appointment_ID = ?")) {
      stmt.setDate(1, java.sql.Date.valueOf(newDate));
      stmt.setTime(2, newStartTime);
      stmt.setTime(3, newEndTime);
      stmt.setInt(4, appointmentId);
      int affectedRows = stmt.executeUpdate();
      return affectedRows > 0;
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "SQL Error during update: " + ex.getMessage(), "Database Error",
          JOptionPane.ERROR_MESSAGE);
      return false;
    }
  }

  /**
   * Updates the availability status of the old and new timeslots in the database.
   * 
   * @param oldDate the old date of the appointment
   * @param oldStartTime the old start time of the appointment
   * @param oldEndTime the old end time of the appointment
   * @param newDate the new date of the appointment
   * @param newStartTime the new start time of the appointment
   * @param newEndTime the new end time of the appointment
   */
  private void updateAvailability(LocalDate oldDate, Time oldStartTime, Time oldEndTime, LocalDate newDate,
      Time newStartTime, Time newEndTime) {
    try (Connection conn = DatabaseManager.getConnection()) {
      conn.setAutoCommit(false);
      // Revert old timeslot to available if the appointment date or time has changed
      if (!oldDate.equals(newDate) || !oldStartTime.equals(newStartTime) || !oldEndTime.equals(newEndTime)) {
        updateTimeslotAvailability(conn, oldDate, oldStartTime, oldEndTime, true);
      }
      // Mark new timeslot as unavailable
      updateTimeslotAvailability(conn, newDate, newStartTime, newEndTime, false);
      conn.commit();
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "Failed to update availability: " + ex.getMessage(), "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Updates the availability status of a specific timeslot in the database.
   * 
   * @param conn the database connection
   * @param date the date of the timeslot
   * @param startTime the start time of the timeslot
   * @param endTime the end time of the timeslot
   * @param isAvailable the new availability status of the timeslot
   * @throws SQLException if a database error occurs
   */
  private void updateTimeslotAvailability(Connection conn, LocalDate date, Time startTime, Time endTime,
      boolean isAvailable) throws SQLException {
    try (PreparedStatement stmt = conn.prepareStatement(
        "UPDATE doctor_availability SET Is_Available = ? WHERE Doctor_User_ID = ? AND Date = ? AND Start_Time = ? AND End_Time = ?")) {
      stmt.setBoolean(1, isAvailable);
      stmt.setInt(2, doctorUserId);
      stmt.setDate(3, java.sql.Date.valueOf(date));
      stmt.setTime(4, startTime);
      stmt.setTime(5, endTime);
      int affectedRows = stmt.executeUpdate();
      if (affectedRows == 0) {
        throw new SQLException("No availability record updated.");
      }
    }
  }

  /**
   * Returns whether the appointment was successfully updated.
   * 
   * @return true if the appointment was updated, false otherwise
   */
  public boolean isUpdated() {
    return updated;
  }
}
