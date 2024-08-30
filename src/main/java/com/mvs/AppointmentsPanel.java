package com.mvs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

/**
 * AppointmentsPanel is a JPanel that allows patients to manage their appointments.
 * Patients can view, book, update, and cancel appointments. The panel also shows
 * patient information and allows filtering by location, department, doctor, and timeslot.
 */
public class AppointmentsPanel extends JPanel {
  private JComboBox<String> doctorDropdown, timeslotDropdown, departmentDropdown, locationDropdown;
  private JButton bookAppointmentButton, openDatePickerButton, refreshAppointmentsButton, updateAppointmentButton,
      deleteAppointmentButton;
  private JLabel selectedDateLabel;
  private int patientUserId;
  private int branchNo;
  private int selectedAvailabilityId = -1;
  private JTable tableAppointments;
  private JScrollPane scrollPaneAppointments;
  private JTabbedPane tabbedPane;

  /**
   * Constructs the AppointmentsPanel and initializes its components.
   * 
   * @param patientUserId the ID of the patient using this panel
   */
  public AppointmentsPanel(int patientUserId) {
    this.patientUserId = patientUserId;
    setLookAndFeel();
    initializeUI();
    loadLocations();
    setupActionListeners();
    loadUpcomingAppointments();
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
   * Initializes the user interface components.
   */
  private void initializeUI() {
    setLayout(new BorderLayout());

    tabbedPane = new JTabbedPane();

    JPanel filterPanel = createFilterPanel();
    JPanel appointmentPanel = new JPanel(new BorderLayout());
    appointmentPanel.add(filterPanel, BorderLayout.NORTH);

    tableAppointments = new JTable();
    tableAppointments.setModel(new DefaultTableModel(
        new Object[] { "Appointment ID", "Date", "Start Time", "End Time", "Doctor Name", "Location" }, 0));
    scrollPaneAppointments = new JScrollPane(tableAppointments);
    appointmentPanel.add(scrollPaneAppointments, BorderLayout.CENTER);

    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    bookAppointmentButton = new JButton("Book Appointment");
    bookAppointmentButton.addActionListener(e -> bookAppointment());

    refreshAppointmentsButton = new JButton("Refresh Appointments");
    refreshAppointmentsButton.addActionListener(e -> loadUpcomingAppointments());

    updateAppointmentButton = new JButton("Update Appointment");
    updateAppointmentButton.addActionListener(e -> openUpdateDialog());

    deleteAppointmentButton = new JButton("Delete Appointment");
    deleteAppointmentButton.addActionListener(e -> cancelAppointment());

    southPanel.add(bookAppointmentButton);
    southPanel.add(refreshAppointmentsButton);
    southPanel.add(updateAppointmentButton);
    southPanel.add(deleteAppointmentButton);
    appointmentPanel.add(southPanel, BorderLayout.SOUTH);

    tabbedPane.addTab("Appointments", appointmentPanel);

    PatientNotificationsPanel notificationsPanel = new PatientNotificationsPanel(patientUserId);
    tabbedPane.addTab("Notifications", notificationsPanel);

    add(tabbedPane, BorderLayout.CENTER);
    add(createPatientInfoPanel(), BorderLayout.EAST);
  }

  /**
   * Cancels the selected appointment.
   */
  private void cancelAppointment() {
    int selectedRow = tableAppointments.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(this, "Please select an appointment to cancel.", "Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    int appointmentId = (Integer) tableAppointments.getValueAt(selectedRow, 0);
    int selectedAvailabilityId = -1;

    // Retrieve the doctor user ID for the appointment
    int doctorUserId = getDoctorUserIdForAppointment(appointmentId);
    if (doctorUserId == -1) {
      JOptionPane.showMessageDialog(this, "Failed to retrieve doctor user ID for the appointment.", "Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    // Retrieve the availability ID for the doctor user ID
    selectedAvailabilityId = getAvailabilityIdForDoctorUserId(doctorUserId);
    if (selectedAvailabilityId == -1) {
      JOptionPane.showMessageDialog(this, "Failed to retrieve availability ID for the doctor user ID.", "Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to cancel this appointment?",
        "Confirm Cancellation", JOptionPane.YES_NO_OPTION);

    Connection conn = null;
    if (confirm == JOptionPane.YES_OPTION) {
      try {
        conn = DatabaseManager.getConnection();
        conn.setAutoCommit(false);

        String updateQuery = "UPDATE appointment SET Status = 'Cancelled' WHERE Appointment_ID = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
          pstmt.setInt(1, appointmentId);
          pstmt.executeUpdate();
        }

        // Set the corresponding availability back to available
        String updateAvailability = "UPDATE doctor_availability SET Is_Available = TRUE WHERE doctor_user_id = ?";
        try (PreparedStatement pstmtAvailability = conn.prepareStatement(updateAvailability)) {
          pstmtAvailability.setInt(1, doctorUserId);
          pstmtAvailability.executeUpdate();
        }

        conn.commit();
        JOptionPane.showMessageDialog(this, "Appointment cancelled successfully.", "Success",
            JOptionPane.INFORMATION_MESSAGE);
        loadUpcomingAppointments();
      } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Failed to cancel appointment: " + ex.getMessage(), "Error",
            JOptionPane.ERROR_MESSAGE);
        if (conn != null) {
          try {
            conn.rollback();
          } catch (SQLException exRollback) {
            JOptionPane.showMessageDialog(this, "Error during rollback: " + exRollback.getMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);
          }
        }
      } finally {
        if (conn != null) {
          try {
            conn.close();
          } catch (SQLException exClose) {
            JOptionPane.showMessageDialog(this, "Error closing database connection: " + exClose.getMessage(), "Error",
                JOptionPane.ERROR_MESSAGE);
          }
        }
      }
    }
  }

  /**
   * Retrieves the doctor user ID for the given appointment ID.
   * 
   * @param appointmentId the appointment ID
   * @return the doctor user ID or -1 if not found
   */
  private int getDoctorUserIdForAppointment(int appointmentId) {
    int doctorUserId = -1;
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn
            .prepareStatement("SELECT Doctor_User_ID FROM appointment WHERE Appointment_ID = ?")) {
      pstmt.setInt(1, appointmentId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        doctorUserId = rs.getInt("Doctor_User_ID");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return doctorUserId;
  }

  /**
   * Retrieves the availability ID for the given doctor user ID.
   * 
   * @param doctorUserId the doctor user ID
   * @return the availability ID or -1 if not found
   */
  private int getAvailabilityIdForDoctorUserId(int doctorUserId) {
    int availabilityId = -1;
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn
            .prepareStatement("SELECT availability_id FROM doctor_availability WHERE doctor_user_id = ?")) {
      pstmt.setInt(1, doctorUserId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        availabilityId = rs.getInt("availability_id");
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return availabilityId;
  }

  /**
   * Opens the dialog for updating the selected appointment.
   */
  private void openUpdateDialog() {
    int selectedRow = tableAppointments.getSelectedRow();
    if (selectedRow != -1) {
      int appointmentId = (Integer) tableAppointments.getValueAt(selectedRow, 0);
      try {
        Connection conn = DatabaseManager.getConnection();
        String query = "SELECT Date, Doctor_User_ID FROM appointment WHERE Appointment_ID = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, appointmentId);
        ResultSet rs = pstmt.executeQuery();
        if (rs.next()) {
          Date date = rs.getDate("Date");
          int doctorUserId = rs.getInt("Doctor_User_ID");
          loadTimeslots(String.valueOf(doctorUserId), new SimpleDateFormat("yyyy-MM-dd").format(date));
        }
        rs.close();
        pstmt.close();
        conn.close();
      } catch (SQLException e) {
        e.printStackTrace();
        JOptionPane.showMessageDialog(this, "Failed to load appointment details for updating.", "Error",
            JOptionPane.ERROR_MESSAGE);
      }

      PatientAppointmentUpdateDialog updateDialog = new PatientAppointmentUpdateDialog(null, patientUserId,
          appointmentId);
      updateDialog.setVisible(true);
      loadUpcomingAppointments(); // Reload all appointments after updating
    } else {
      JOptionPane.showMessageDialog(this, "Please select an appointment to update.", "Update Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Creates the panel with filter options for selecting location, department, doctor, and timeslot.
   * 
   * @return the created filter panel
   */
  private JPanel createFilterPanel() {
    JPanel filterPanel = new JPanel();
    filterPanel.setLayout(new BoxLayout(filterPanel, BoxLayout.LINE_AXIS));
    doctorDropdown = new JComboBox<>();
    timeslotDropdown = new JComboBox<>();
    timeslotDropdown.addItem("-- Select a Timeslot --"); // Placeholder added here
    departmentDropdown = new JComboBox<>();
    locationDropdown = new JComboBox<>();
    selectedDateLabel = new JLabel("-- Select a Date --");
    openDatePickerButton = new JButton("Pick Date");
    openDatePickerButton.addActionListener(e -> openDatePicker());

    filterPanel.add(new JLabel("Select Location:"));
    filterPanel.add(locationDropdown);
    filterPanel.add(new JLabel("Select Department:"));
    filterPanel.add(departmentDropdown);
    filterPanel.add(new JLabel("Select Doctor:"));
    filterPanel.add(doctorDropdown);
    filterPanel.add(selectedDateLabel);
    filterPanel.add(openDatePickerButton);
    filterPanel.add(new JLabel("Select Timeslot:"));
    filterPanel.add(timeslotDropdown);
    return filterPanel;
  }

  /**
   * Opens the date picker dialog.
   */
  private void openDatePicker() {
    CalendarDatePicker datePicker = new CalendarDatePicker(JFrame.getFrames()[0]);
    datePicker.setVisible(true);
    LocalDate date = datePicker.getSelectedDate();
    if (date != null) {
      String formattedDate = date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
      selectedDateLabel.setText(formattedDate);

      String selectedDoctor = (String) doctorDropdown.getSelectedItem();
      if (selectedDoctor != null && !selectedDoctor.equals("-- Select a Doctor --")) {
        String doctorId = selectedDoctor.split("-")[1].trim();
        loadTimeslots(doctorId, formattedDate);
      }
    }
  }

  /**
   * Sets up action listeners for the dropdowns and buttons.
   */
  private void setupActionListeners() {
    locationDropdown.addActionListener(e -> {
      String selectedLocation = (String) locationDropdown.getSelectedItem();
      if (selectedLocation != null && !selectedLocation.equals("-- Select a Location --")) {
        branchNo = Integer.parseInt(selectedLocation.split("-")[1].trim());
        loadDepartments();
      }
    });

    departmentDropdown.addActionListener(e -> {
      String selectedDepartment = (String) departmentDropdown.getSelectedItem();
      if (selectedDepartment != null && !selectedDepartment.equals("-- Select a Department --")) {
        int departmentNo = Integer.parseInt(selectedDepartment.split("-")[1].trim());
        loadDoctors(branchNo, departmentNo);
      }
    });

    doctorDropdown.addActionListener(e -> {
      String selectedDoctor = (String) doctorDropdown.getSelectedItem();
      String selectedDate = selectedDateLabel.getText();
      if (selectedDoctor != null && !selectedDoctor.equals("-- Select a Doctor --") && isValidDate(selectedDate)) {
        String doctorId = selectedDoctor.split("-")[1].trim();
        loadTimeslots(doctorId, selectedDate);
      }
    });

    timeslotDropdown.addActionListener(e -> {
      if (timeslotDropdown.getSelectedItem() != null) {
        String selectedTimeslot = timeslotDropdown.getSelectedItem().toString();
        try {
          String idStr = selectedTimeslot.substring(selectedTimeslot.lastIndexOf('[') + 1,
              selectedTimeslot.lastIndexOf(']'));
          selectedAvailabilityId = Integer.parseInt(idStr);
          System.out.println("Selected timeslot ID: " + selectedAvailabilityId);
        } catch (NumberFormatException | IndexOutOfBoundsException ex) {
          System.out.println("Failed to parse availability ID from timeslot selection: " + ex.getMessage());
        }
      }
    });
  }

  /**
   * Checks if the given date string is valid.
   * 
   * @param dateString the date string to validate
   * @return true if the date string is valid, false otherwise
   */
  private boolean isValidDate(String dateString) {
    return !dateString.equals("-- Select a Date --");
  }

  /**
   * Books a new appointment for the patient.
   */
  private void bookAppointment() {
    String selectedTimeslot = (String) timeslotDropdown.getSelectedItem();
    if (selectedTimeslot == null || selectedTimeslot.equals("-- Select a Timeslot --")) {
      JOptionPane.showMessageDialog(this, "Please select a timeslot to book an appointment.", "Error",
          JOptionPane.WARNING_MESSAGE);
      resetFields(); // Call reset fields if no timeslot is selected

      return;
    }

    String[] timesParts = selectedTimeslot.split(" - ");
    String startTimeStr = timesParts[0];
    String endTimeStr = timesParts[1].substring(0, timesParts[1].indexOf('[')).trim();

    Time startTime = Time.valueOf(startTimeStr + ":00");
    Time endTime = Time.valueOf(endTimeStr + ":00");
    int doctorId = Integer.parseInt(((String) doctorDropdown.getSelectedItem()).split("-")[1].trim());
    String date = selectedDateLabel.getText();
    String locationName = ((String) locationDropdown.getSelectedItem()).split(" - ")[0].trim();

    if (!checkTimeSlotAvailability(doctorId, date, startTime, endTime)) {
      JOptionPane.showMessageDialog(this, "This timeslot is already booked. Please choose another.", "Error",
          JOptionPane.ERROR_MESSAGE);
      return;
    }
    Connection conn = null;
    try {
      conn = DatabaseManager.getConnection();
      conn.setAutoCommit(false);

      String availabilityCheck = "SELECT Is_Available FROM doctor_availability WHERE availability_id = ?";
      try (PreparedStatement pstmtCheck = conn.prepareStatement(availabilityCheck)) {
        pstmtCheck.setInt(1, selectedAvailabilityId);
        ResultSet rs = pstmtCheck.executeQuery();
        if (rs.next() && !rs.getBoolean("Is_Available")) {
          JOptionPane.showMessageDialog(this, "This timeslot is no longer available. Please choose another.",
              "Booking Error", JOptionPane.ERROR_MESSAGE);
          return;
        }
      }

      String sqlInsertAppointment = "INSERT INTO APPOINTMENT (Patient_User_ID, Doctor_User_ID, Location, Start_Time, End_Time, Date) VALUES (?, ?, ?, ?, ?, ?)";
      try (PreparedStatement pstmt = conn.prepareStatement(sqlInsertAppointment)) {
        pstmt.setInt(1, patientUserId);
        pstmt.setInt(2, doctorId);
        pstmt.setString(3, locationName);
        pstmt.setTime(4, startTime);
        pstmt.setTime(5, endTime);
        pstmt.setDate(6, java.sql.Date.valueOf(date));
        pstmt.executeUpdate();
      }

      String sqlUpdateAvailability = "UPDATE doctor_availability SET Is_Available = FALSE WHERE availability_id = ?";
      try (PreparedStatement pstmtAvailability = conn.prepareStatement(sqlUpdateAvailability)) {
        pstmtAvailability.setInt(1, selectedAvailabilityId);
        pstmtAvailability.executeUpdate();
      }

      conn.commit();
      JOptionPane.showMessageDialog(this, "Appointment booked successfully!", "Success",
          JOptionPane.INFORMATION_MESSAGE);
      resetFields();

      loadUpcomingAppointments();

    } catch (SQLException ex) {
      JOptionPane.showMessageDialog(this, "Failed to book appointment: " + ex.getMessage(), "Error",
          JOptionPane.ERROR_MESSAGE);
      if (conn != null) {
        try {
          conn.rollback();
        } catch (SQLException exRollback) {
          JOptionPane.showMessageDialog(this, "Error during rollback: " + exRollback.getMessage(), "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    } finally {
      if (conn != null) {
        try {
          conn.close();
        } catch (SQLException exClose) {
          JOptionPane.showMessageDialog(this, "Error closing database connection: " + exClose.getMessage(), "Error",
              JOptionPane.ERROR_MESSAGE);
        }
      }
    }
  }

  /**
   * Checks if the specified timeslot is available for the doctor on the given date.
   * 
   * @param doctorId the doctor ID
   * @param date the date of the appointment
   * @param startTime the start time of the appointment
   * @param endTime the end time of the appointment
   * @return true if the timeslot is available, false otherwise
   */
  private boolean checkTimeSlotAvailability(int doctorId, String date, Time startTime, Time endTime) {
    java.sql.Date sqlDate = java.sql.Date.valueOf(date);
    String query = "SELECT COUNT(*) FROM appointment WHERE Doctor_User_ID = ? AND Date = ? AND NOT (Start_Time >= ? OR End_Time <= ?) AND Status != 'Cancelled'";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {
      pstmt.setInt(1, doctorId);
      pstmt.setDate(2, sqlDate);
      pstmt.setTime(3, endTime);
      pstmt.setTime(4, startTime);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        return rs.getInt(1) == 0;
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return false;
  }

  /**
   * Resets the fields in the filter panel to their default values.
   */
  public void resetFields() {
    // Reset all dropdowns to the first item, assuming the first item is the
    // placeholder.
    doctorDropdown.setSelectedIndex(0);
    departmentDropdown.setSelectedIndex(0);
    locationDropdown.setSelectedIndex(0);
    timeslotDropdown.removeAllItems(); // Ensure it is empty
    timeslotDropdown.addItem("-- Select a Timeslot --"); // Re-add the placeholder
    timeslotDropdown.setSelectedIndex(0); // Select the placeholder

    // Reset the selected date label and the availability ID
    selectedDateLabel.setText("-- Select a Date --");
    selectedAvailabilityId = -1; // Reset the availability ID

    // Log the state (optional, for debugging purposes)
    System.out.println("Fields reset: timeslots cleared and default items set.");
  }

  /**
   * Loads the available locations into the location dropdown.
   */
  private void loadLocations() {
    locationDropdown.removeAllItems();
    locationDropdown.addItem("-- Select a Location --");

    try (Connection conn = DatabaseManager.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT Location, Branch_No FROM MVS_BRANCH")) {

      while (rs.next()) {
        String locationName = rs.getString("Location");
        int branchNo = rs.getInt("Branch_No");
        locationDropdown.addItem(locationName + " - " + branchNo);
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads the departments for the selected location into the department dropdown.
   */
  private void loadDepartments() {
    departmentDropdown.removeAllItems();
    departmentDropdown.addItem("-- Select a Department --");

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn
            .prepareStatement("SELECT Department_No, Department_Name FROM DEPARTMENT WHERE Branch_No = ?")) {

      pstmt.setInt(1, branchNo);
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          int departmentNo = rs.getInt("Department_No");
          String departmentName = rs.getString("Department_Name");
          departmentDropdown.addItem(departmentName + " - " + departmentNo);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads the doctors for the selected department into the doctor dropdown.
   * 
   * @param branchNo the branch number
   * @param departmentNo the department number
   */
  private void loadDoctors(int branchNo, int departmentNo) {
    doctorDropdown.removeAllItems();
    doctorDropdown.addItem("-- Select a Doctor --");

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(
            "SELECT u.User_ID, u.First_Name, u.Last_Name FROM USER u JOIN DOCTOR d ON u.User_ID = d.User_ID WHERE d.Department_No = ?")) {

      pstmt.setInt(1, departmentNo);
      try (ResultSet rs = pstmt.executeQuery()) {
        while (rs.next()) {
          int userId = rs.getInt("User_ID");
          String firstName = rs.getString("First_Name");
          String lastName = rs.getString("Last_Name");
          doctorDropdown.addItem(firstName + " " + lastName + " - " + userId);
        }
      }

    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  /**
   * Loads the available timeslots for the selected doctor and date into the timeslot dropdown.
   * 
   * @param doctorId the doctor ID
   * @param dateString the date string
   */
  private void loadTimeslots(String doctorId, String dateString) {
    timeslotDropdown.removeAllItems();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
    try {
      java.util.Date dateUtil = dateFormat.parse(dateString);
      java.sql.Date dateSql = new java.sql.Date(dateUtil.getTime());

      String sql = "SELECT da.availability_id, da.start_time, da.end_time FROM doctor_availability da " +
          "WHERE da.doctor_user_id = ? AND da.date = ? AND da.Is_Available = TRUE AND " +
          "NOT EXISTS (SELECT 1 FROM appointment a WHERE a.Doctor_User_ID = da.doctor_user_id AND " +
          "a.Date = da.date AND NOT (a.Start_Time >= da.end_time OR a.End_Time <= da.start_time) AND " +
          "a.Status IN ('Booked', 'Rescheduled'))";
      try (Connection conn = DatabaseManager.getConnection();
          PreparedStatement pstmt = conn.prepareStatement(sql)) {
        pstmt.setInt(1, Integer.parseInt(doctorId));
        pstmt.setDate(2, dateSql);
        ResultSet rs = pstmt.executeQuery();
        while (rs.next()) {
          int id = rs.getInt("availability_id");
          Time startTime = rs.getTime("start_time");
          Time endTime = rs.getTime("end_time");
          String timeslot = new SimpleDateFormat("HH:mm").format(startTime) + " - " +
              new SimpleDateFormat("HH:mm").format(endTime) + " [" + id + "]";
          timeslotDropdown.addItem(timeslot);
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Failed to load timeslots: " + e.getMessage(), "Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Loads the upcoming appointments for the patient and displays them in the table.
   */
  public void loadUpcomingAppointments() {
    DefaultTableModel model = (DefaultTableModel) tableAppointments.getModel();
    model.setRowCount(0);
    model.setColumnIdentifiers(new Object[] { "Appointment ID", "Date", "Start Time", "End Time", "Doctor Name", "Location", "Branch" });

    String query = "SELECT a.Appointment_ID, a.Date, a.Start_Time, a.End_Time, "
                 + "CONCAT(u.First_Name, ' ', u.Last_Name) AS Doctor_Name, a.Location, b.Branch_Name "
                 + "FROM appointment a "
                 + "JOIN user u ON a.Doctor_User_ID = u.User_ID "
                 + "JOIN mvs_branch b ON a.Location = b.Location "
                 + "WHERE a.Patient_User_ID = ? AND (a.Status = 'Booked' OR a.Status = 'Rescheduled') "
                 + "ORDER BY a.Date, a.Start_Time";

    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement(query)) {
        pstmt.setInt(1, patientUserId);
        try (ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{
                    rs.getInt("Appointment_ID"),
                    rs.getDate("Date"),
                    rs.getTime("Start_Time"),
                    rs.getTime("End_Time"),
                    rs.getString("Doctor_Name"),
                    rs.getString("Location"),
                    rs.getString("Branch_Name")
                });
            }
        }
    } catch (SQLException ex) {
        JOptionPane.showMessageDialog(this, "Error loading upcoming appointments: " + ex.getMessage(), "Database Error",
            JOptionPane.ERROR_MESSAGE);
        ex.printStackTrace(); // Consider logging this error as well
    }
  }

  /**
   * Creates the panel displaying patient information.
   * 
   * @return the created patient information panel
   */
  private JPanel createPatientInfoPanel() {
    JPanel infoPanel = new JPanel();
    infoPanel.setLayout(new BoxLayout(infoPanel, BoxLayout.Y_AXIS));
    infoPanel.setBorder(BorderFactory.createTitledBorder("Patient Information"));

    JLabel lblFirstName = new JLabel("First Name: Loading...");
    JLabel lblLastName = new JLabel("Last Name: Loading...");
    JLabel lblDOB = new JLabel("Date of Birth: Loading...");
    JLabel lblSex = new JLabel("Sex: Loading...");
    JLabel lblPhone = new JLabel("Phone No: Loading...");
    JLabel lblEmail = new JLabel("Email: Loading...");
    JLabel txtAddress = new JLabel("Address: Loading...");

    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        try (Connection conn = DatabaseManager.getConnection();
            PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM patientprofile WHERE User_ID = ?")) {
          pstmt.setInt(1, patientUserId);
          ResultSet rs = pstmt.executeQuery();
          if (rs.next()) {
            lblFirstName.setText("First Name: " + rs.getString("First_Name"));
            lblLastName.setText("Last Name: " + rs.getString("Last_Name"));
            lblDOB.setText("Date of Birth: " + rs.getDate("Date_of_Birth").toString());
            lblSex.setText("Sex: " + rs.getString("Sex"));
            lblPhone.setText("Phone No: " + rs.getString("Phone_No"));
            lblEmail.setText("Email: " + rs.getString("Email"));
            String address = "Address: " + rs.getString("Street") + ", " +
                rs.getString("City") + ", " +
                rs.getString("State") + " " +
                rs.getString("Zip_Code");
            txtAddress.setText(address);
          } else {
            lblFirstName.setText("First Name: Not found");
            lblLastName.setText("Last Name: Not found");
            lblDOB.setText("Date of Birth: Not found");
            lblSex.setText("Sex: Not found");
            lblPhone.setText("Phone No: Not found");
            lblEmail.setText("Email: Not found");
            txtAddress.setText("Address: Not found");
          }
        } catch (SQLException ex) {
          System.out.println("SQL Error: " + ex.getMessage());
          lblFirstName.setText("First Name: Error");
          lblLastName.setText("Last Name: Error");
          lblDOB.setText("Date of Birth: Error");
          lblSex.setText("Sex: Error");
          lblPhone.setText("Phone No: Error");
          lblEmail.setText("Email: Error");
          txtAddress.setText("Address: Error loading.");
        }
        return null;
      }
    };
    worker.execute();

    infoPanel.add(lblFirstName);
    infoPanel.add(lblLastName);
    infoPanel.add(lblDOB);
    infoPanel.add(lblSex);
    infoPanel.add(lblPhone);
    infoPanel.add(lblEmail);
    infoPanel.add(txtAddress);

    return infoPanel;
  }
}
