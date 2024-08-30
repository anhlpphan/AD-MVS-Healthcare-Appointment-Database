package com.mvs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * DoctorScheduleCalendar is a JPanel that allows a manager to view and manage
 * a doctor's schedule.
 * The panel includes a calendar for selecting dates and a button to set
 * available times for the selected dates.
 */
public class DoctorScheduleCalendar extends JPanel {
  private LocalDate currentDate;
  private JPanel calendarPanel;
  private JLabel monthLabel;
  private List<LocalDate> selectedDates = new ArrayList<>();
  private Map<LocalDate, JButton> dateButtons = new HashMap<>();
  private JTextField userIdField;
  private int doctorUserId;
  private JButton confirmDatesButton, selectButton;
  private int managerUserId; // The manager's user ID

  /**
   * Constructs a DoctorScheduleCalendar for the specified manager user ID.
   * 
   * @param managerUserId the ID of the manager
   */
  public DoctorScheduleCalendar(int managerUserId) {
    this.managerUserId = managerUserId;
    setLookAndFeel();

    currentDate = LocalDate.now();
    setLayout(new BorderLayout());

    initializeUI();
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
    JPanel headerPanel = new JPanel(new BorderLayout());
    monthLabel = new JLabel("", JLabel.CENTER);
    JButton prevButton = new JButton("<");
    JButton nextButton = new JButton(">");
    confirmDatesButton = new JButton("Confirm Dates");
    confirmDatesButton.setEnabled(false); // Disabled until a valid doctor is selected

    userIdField = new JTextField(10);
    selectButton = new JButton("Select Doctor");
    JPanel userIdPanel = new JPanel(new FlowLayout());
    userIdPanel.add(new JLabel("Doctor User ID:"));
    userIdPanel.add(userIdField);
    userIdPanel.add(selectButton);
    selectButton.addActionListener(this::confirmDoctorId);

    prevButton.addActionListener(e -> navigateMonths(-1));
    nextButton.addActionListener(e -> navigateMonths(1));
    headerPanel.add(userIdPanel, BorderLayout.NORTH);
    headerPanel.add(prevButton, BorderLayout.WEST);
    headerPanel.add(monthLabel, BorderLayout.CENTER);
    headerPanel.add(nextButton, BorderLayout.EAST);
    add(headerPanel, BorderLayout.NORTH);

    calendarPanel = new JPanel(new GridLayout(0, 7)); // 7 days of the week
    updateCalendar();
    add(calendarPanel, BorderLayout.CENTER);

    JPanel confirmPanel = new JPanel();
    confirmPanel.add(confirmDatesButton);
    confirmDatesButton.addActionListener(this::openTimeSettingDialog);
    add(confirmPanel, BorderLayout.SOUTH);
  }

  /**
   * Confirms the doctor ID entered and validates if the doctor belongs to the
   * manager's department.
   * 
   * @param e the action event
   */
  private void confirmDoctorId(ActionEvent e) {
    int userId = 0;
    try {
      userId = Integer.parseInt(userIdField.getText());
    } catch (NumberFormatException ex) {
      JOptionPane.showMessageDialog(this, "Invalid Doctor ID", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    if (validateDoctor(userId)) {
      doctorUserId = userId;
      JOptionPane.showMessageDialog(this, "Doctor selected: User ID " + doctorUserId, "Doctor Selected",
          JOptionPane.INFORMATION_MESSAGE);
      confirmDatesButton.setEnabled(true);
    } else {
      JOptionPane.showMessageDialog(this, "Doctor ID is not valid or does not belong to your department.",
          "Invalid Doctor", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Validates if the doctor ID belongs to a doctor in the manager's department.
   * 
   * @param userId the doctor user ID
   * @return true if the doctor is valid, false otherwise
   */
  private boolean validateDoctor(int userId) {
    String query = "SELECT d.User_ID FROM doctor d " +
        "JOIN user u ON d.User_ID = u.User_ID " +
        "JOIN department dep ON d.Department_No = dep.Department_No " +
        "WHERE u.User_Type_ID = 2 AND d.User_ID = ? AND dep.Manager_ID = ?";
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement stmt = conn.prepareStatement(query)) {
      stmt.setInt(1, userId);
      stmt.setInt(2, managerUserId);
      ResultSet rs = stmt.executeQuery();
      return rs.next();
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return false;
  }

  /**
   * Navigates the calendar by the specified number of months.
   * 
   * @param delta the number of months to navigate
   */
  private void navigateMonths(int delta) {
    currentDate = currentDate.plusMonths(delta);
    updateCalendar();
  }

  /**
   * Updates the calendar display for the current month.
   */
  private void updateCalendar() {
    calendarPanel.removeAll();
    LocalDate firstOfMonth = currentDate.withDayOfMonth(1);
    int daysInMonth = firstOfMonth.lengthOfMonth();
    int dayOfWeek = firstOfMonth.getDayOfWeek().getValue();

    monthLabel.setText(firstOfMonth.format(DateTimeFormatter.ofPattern("MMMM yyyy")));
    String[] dayNames = { "Su", "Mo", "Tu", "We", "Th", "Fr", "Sa" };
    for (String dayName : dayNames) {
      calendarPanel.add(new JLabel(dayName, JLabel.CENTER));
    }

    // Adjust dayOfWeek to suit the calendar starting with Sunday
    int shift = dayOfWeek % 7; // Adjust to 0-indexed for Sunday start

    // Adding empty labels for empty days at the start
    for (int i = 0; i < shift; i++) {
      calendarPanel.add(new JLabel(""));
    }

    // Adding day buttons
    for (int i = 1; i <= daysInMonth; i++) {
      final LocalDate date = LocalDate.of(currentDate.getYear(), currentDate.getMonth(), i);
      JButton dayButton = new JButton(String.valueOf(i));
      dayButton.setBackground(selectedDates.contains(date) ? Color.BLUE : Color.LIGHT_GRAY);
      dayButton.addActionListener(e -> toggleDateSelection(date));
      calendarPanel.add(dayButton);
      dateButtons.put(date, dayButton);
    }

    calendarPanel.revalidate();
    calendarPanel.repaint();
  }

  /**
   * Toggles the selection of a date in the calendar.
   * 
   * @param date the date to toggle
   */
  private void toggleDateSelection(LocalDate date) {
    if (selectedDates.contains(date)) {
      selectedDates.remove(date);
      dateButtons.get(date).setBackground(Color.LIGHT_GRAY);
    } else {
      selectedDates.add(date);
      dateButtons.get(date).setBackground(Color.BLUE);
    }
  }

  /**
   * Opens the time setting dialog for the selected dates.
   * 
   * @param e the action event
   */
  private void openTimeSettingDialog(ActionEvent e) {
    if (selectedDates.isEmpty()) {
      JOptionPane.showMessageDialog(this, "No dates selected", "Error", JOptionPane.ERROR_MESSAGE);
      return;
    }
    TimeSettingDialog dialog = new TimeSettingDialog(null, "Set Times", true, selectedDates, doctorUserId);
    dialog.setVisible(true);
  }
}
