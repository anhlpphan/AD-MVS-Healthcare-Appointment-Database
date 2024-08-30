package com.mvs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

/**
 * DoctorDashboardPanel is a JPanel that serves as a dashboard for doctors.
 * It allows doctors to view, search, update, and cancel their appointments.
 * Additionally, doctors can look up patient details and receive notifications.
 */
public class DoctorDashboardPanel extends JPanel {
  private int doctorUserId;
  private JTable tableAppointments;
  private JScrollPane scrollPaneAppointments;
  private JTextField startTimeField, endTimeField, patientIdField, appointmentIdField;
  private JButton patientLookupButton, openDatePickerButton, refreshButton, searchButton, updateButton, deleteButton;
  private JLabel selectedDateLabel;
  private LocalDate selectedDate;
  private JTabbedPane tabbedPane;

  /**
   * Constructs a DoctorDashboardPanel for the specified doctor user ID.
   * 
   * @param doctorUserId the ID of the doctor using the dashboard
   */
  public DoctorDashboardPanel(int doctorUserId) {
    this.doctorUserId = doctorUserId;
    initializeUI();
    loadAppointments();
  }

  /**
   * Initializes the user interface components.
   */
  private void initializeUI() {
    setLayout(new BorderLayout());

    tabbedPane = new JTabbedPane();

    JPanel dashboardPanel = new JPanel(new BorderLayout());
    JPanel topPanel = new JPanel(new FlowLayout());
    selectedDateLabel = new JLabel("No Date Selected");
    startTimeField = new JTextField(5);
    endTimeField = new JTextField(5);
    patientIdField = new JTextField(5);
    appointmentIdField = new JTextField(5);
    openDatePickerButton = new JButton("Select Date");
    refreshButton = new JButton("Refresh");
    searchButton = new JButton("Search");
    updateButton = new JButton("Update Appointment");
    deleteButton = new JButton("Cancel Appointment");
    patientLookupButton = new JButton("Patient Lookup");

    // Add action listeners
    openDatePickerButton.addActionListener(e -> onOpenDatePicker());
    refreshButton.addActionListener(e -> {
      resetSearch();
      loadAppointments();
    });
    searchButton.addActionListener(e -> searchAppointments());
    updateButton.addActionListener(e -> updateAppointment());
    deleteButton.addActionListener(e -> cancelAppointment());
    patientLookupButton.addActionListener(e -> openPatientDetails());

    // Add components to top panel
    topPanel.add(openDatePickerButton);
    topPanel.add(selectedDateLabel);
    topPanel.add(new JLabel("Patient ID:"));
    topPanel.add(patientIdField);
    topPanel.add(new JLabel("Appointment ID:"));
    topPanel.add(appointmentIdField);
    topPanel.add(new JLabel("Start Time:"));
    topPanel.add(startTimeField);
    topPanel.add(new JLabel("End Time:"));
    topPanel.add(endTimeField);
    topPanel.add(refreshButton);
    topPanel.add(searchButton);
    topPanel.add(updateButton);
    topPanel.add(deleteButton);
    topPanel.add(patientLookupButton);

    // Initialize table for appointments
    tableAppointments = new JTable(new DefaultTableModel(
        new Object[]{"Appointment ID", "Patient ID", "Date", "Start Time", "End Time", "Status"}, 0));
    scrollPaneAppointments = new JScrollPane(tableAppointments);
    dashboardPanel.add(topPanel, BorderLayout.NORTH);
    dashboardPanel.add(scrollPaneAppointments, BorderLayout.CENTER);

    // Add tabs to the tabbed pane
    tabbedPane.addTab("Dashboard", dashboardPanel);
    tabbedPane.addTab("Notifications", new NotificationsPanel()); // Assume NotificationsPanel is implemented elsewhere

    add(tabbedPane, BorderLayout.CENTER);
  }

  /**
   * Opens the patient details panel for the selected appointment.
   */
  private void openPatientDetails() {
    int selectedRow = tableAppointments.getSelectedRow();
    if (selectedRow != -1) {
      int appointmentId = (Integer) tableAppointments.getValueAt(selectedRow, 0);
      AppointmentDetailsPanel detailsPanel = new AppointmentDetailsPanel(appointmentId);
      tabbedPane.addTab("Patient Details", detailsPanel);
      tabbedPane.setSelectedIndex(tabbedPane.getTabCount() - 1);
    } else {
      JOptionPane.showMessageDialog(this, "Please select an appointment to view details.", "No Selection",
          JOptionPane.WARNING_MESSAGE);
    }
  }

  /**
   * Opens the date picker dialog and sets the selected date.
   */
  private void onOpenDatePicker() {
    CalendarDatePicker datePicker = new CalendarDatePicker(null);
    datePicker.setVisible(true);
    LocalDate date = datePicker.getSelectedDate();
    if (date != null) {
      selectedDate = date;
      selectedDateLabel.setText(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")));
      loadAppointments();
    }
  }

  /**
   * Loads the appointments for the doctor and displays them in the table.
   */
  private void loadAppointments() {
    DefaultTableModel model = (DefaultTableModel) tableAppointments.getModel();
    model.setRowCount(0);

    StringBuilder queryBuilder = new StringBuilder("SELECT Appointment_ID, Patient_User_ID, Date, Start_Time, End_Time, Status FROM appointment ");
    queryBuilder.append("WHERE Doctor_User_ID = ? AND Status IN ('Booked', 'Rescheduled', 'Confirmed')");

    ArrayList<Object> params = new ArrayList<>();
    params.add(doctorUserId);

    if (selectedDate != null) {
        queryBuilder.append(" AND Date = ?");
        params.add(java.sql.Date.valueOf(selectedDate));
    }

    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {
        for (int i = 0; i < params.size(); i++) {
            pstmt.setObject(i + 1, params.get(i));
        }
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
            model.addRow(new Object[]{
                rs.getInt("Appointment_ID"),
                rs.getInt("Patient_User_ID"),
                rs.getDate("Date"),
                rs.getTime("Start_Time"),
                rs.getTime("End_Time"),
                rs.getString("Status")
            });
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Failed to load appointments: " + ex.getMessage(), "Database Error",
            JOptionPane.ERROR_MESSAGE);
    }
}

  /**
   * Updates the selected appointment.
   */
  private void updateAppointment() {
    int selectedRow = tableAppointments.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Please select an appointment to update.", "Update Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    int appointmentId = (Integer) tableAppointments.getValueAt(selectedRow, 0);
    AppointmentUpdateDialog updateDialog = new AppointmentUpdateDialog(
        (Frame) SwingUtilities.getWindowAncestor(this), doctorUserId, appointmentId);
    updateDialog.setVisible(true);
    if (updateDialog.isUpdated()) {
      loadAppointments();
    }
  }

  /**
   * Cancels the selected appointment.
   */
  private void cancelAppointment() {
    int selectedRow = tableAppointments.getSelectedRow();
    if (selectedRow != -1) {
      int appointmentId = (Integer) tableAppointments.getValueAt(selectedRow, 0);
      try (Connection conn = DatabaseManager.getConnection();
          PreparedStatement pstmt = conn
              .prepareStatement("UPDATE appointment SET Status = 'Cancelled' WHERE Appointment_ID = ?")) {
        pstmt.setInt(1, appointmentId);
        pstmt.executeUpdate();
        JOptionPane.showMessageDialog(this, "Appointment cancelled successfully.", "Cancellation Successful",
            JOptionPane.INFORMATION_MESSAGE);
        loadAppointments();
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Failed to cancel appointment: " + ex.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(this, "Please select an appointment to cancel.", "Cancellation Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Searches for appointments based on the search criteria and displays them in the table.
   */
  private void searchAppointments() {
    DefaultTableModel model = (DefaultTableModel) tableAppointments.getModel();
    model.setRowCount(0);

    StringBuilder queryBuilder = new StringBuilder(
        "SELECT Appointment_ID, Patient_User_ID, Date, Start_Time, End_Time, Status FROM appointment WHERE Doctor_User_ID = ? AND Status IN ('Booked', 'Rescheduled')");
    ArrayList<Object> params = new ArrayList<>();
    params.add(doctorUserId);

    if (selectedDate != null) {
      queryBuilder.append(" AND Date = ?");
      params.add(java.sql.Date.valueOf(selectedDate));
    }
    if (!patientIdField.getText().isEmpty()) {
      queryBuilder.append(" AND Patient_User_ID = ?");
      params.add(Integer.parseInt(patientIdField.getText().trim()));
    }
    if (!appointmentIdField.getText().isEmpty()) {
      queryBuilder.append(" AND Appointment_ID = ?");
      params.add(Integer.parseInt(appointmentIdField.getText().trim()));
    }
    if (!startTimeField.getText().isEmpty() || !endTimeField.getText().isEmpty()) {
      addTimeConditions(queryBuilder, params);
    }

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(queryBuilder.toString())) {
      for (int i = 0; i < params.size(); i++) {
        pstmt.setObject(i + 1, params.get(i));
      }
      ResultSet rs = pstmt.executeQuery();
      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("Appointment_ID"),
            rs.getInt("Patient_User_ID"),
            rs.getDate("Date"),
            rs.getTime("Start_Time"),
            rs.getTime("End_Time"),
            rs.getString("Status")
        });
      }
    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "Failed to load appointments: " + ex.getMessage(), "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Adds time conditions to the search query based on the input fields.
   * 
   * @param queryBuilder the StringBuilder for the SQL query
   * @param params the list of parameters for the prepared statement
   */
  private void addTimeConditions(StringBuilder queryBuilder, ArrayList<Object> params) {
    String[] startTimeRange = parseTimeRange(startTimeField.getText().trim());
    String[] endTimeRange = parseTimeRange(endTimeField.getText().trim());

    if (startTimeRange != null) {
      queryBuilder.append(" AND Start_Time BETWEEN ? AND ?");
      params.add(Time.valueOf(startTimeRange[0]));
      params.add(Time.valueOf(startTimeRange[1]));
    }
    if (endTimeRange != null) {
      queryBuilder.append(" AND End_Time BETWEEN ? AND ?");
      params.add(Time.valueOf(endTimeRange[0]));
      params.add(Time.valueOf(endTimeRange[1]));
    }
  }

  /**
   * Parses the time range input and returns an array with the start and end time.
   * 
   * @param input the time range input
   * @return an array with the start and end time, or null if the input is invalid
   */
  private String[] parseTimeRange(String input) {
    if (input == null || input.isEmpty()) {
      return null;
    }

    try {
      if (input.contains(":")) {
        String[] parts = input.split(":");

        if (parts.length == 2) {
          int hour = Integer.parseInt(parts[0]);
          int minute = Integer.parseInt(parts[1]);

          String startTime = String.format("%02d:%02d:00", hour, minute);
          String endTime = String.format("%02d:%02d:59", hour, minute);
          return new String[] { startTime, endTime };
        } else {
          return new String[] { input + ":00", input + ":59" };
        }
      } else {
        int hour = Integer.parseInt(input);
        String startTime = String.format("%02d:00:00", hour);
        String endTime = String.format("%02d:59:59", hour);
        return new String[] { startTime, endTime };
      }
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(null, "Invalid time format: " + input, "Input Error", JOptionPane.ERROR_MESSAGE);
      return null;
    }
  }

  /**
   * Resets the search fields to their default values.
   */
  private void resetSearch() {
    selectedDate = null;
    selectedDateLabel.setText("No Date Selected");
    startTimeField.setText("");
    endTimeField.setText("");
    patientIdField.setText("");
    appointmentIdField.setText("");
  }
}
