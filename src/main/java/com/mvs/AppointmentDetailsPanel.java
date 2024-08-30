package com.mvs;

import javax.swing.*;
import java.awt.*;
import java.sql.*;
import java.util.ArrayList;

/**
 * AppointmentDetailsPanel is a JPanel that displays detailed information about an appointment.
 * It allows viewing initial appointment details as well as medical history, allergies, insurance, and medications of the patient.
 */
public class AppointmentDetailsPanel extends JPanel {
  private int appointmentId;
  private JTextArea detailsTextArea;
  private JScrollPane scrollPane;
  private JButton backButton; // Button to return to the initial details view

  /**
   * Constructs the AppointmentDetailsPanel and initializes its components.
   * 
   * @param appointmentId the ID of the appointment to display details for
   */
  public AppointmentDetailsPanel(int appointmentId) {
    this.appointmentId = appointmentId;
    setLayout(new BorderLayout());

    // Text area for displaying details
    detailsTextArea = new JTextArea(10, 30);
    detailsTextArea.setEditable(false);
    detailsTextArea.setFont(new Font("Monospaced", Font.PLAIN, 12)); // Set a monospaced font for better alignment
    scrollPane = new JScrollPane(detailsTextArea);

    // Buttons for various actions
    JButton medicalHistoryButton = new JButton("View Medical History");
    JButton allergiesButton = new JButton("View Allergies");
    JButton insuranceButton = new JButton("View Insurance");
    JButton medicationsButton = new JButton("View Medications"); // Added button for viewing medications
    JButton closeButton = new JButton("Close");
    backButton = new JButton("Return to Details");
    backButton.addActionListener(e -> fetchAppointmentDetails(appointmentId)); // Fetch initial details
    backButton.setVisible(false); // Initially hide the back button

    // Set action listeners for buttons
    medicalHistoryButton.addActionListener(e -> fetchDetails("Medical"));
    allergiesButton.addActionListener(e -> fetchDetails("Allergies"));
    insuranceButton.addActionListener(e -> fetchDetails("Insurance"));
    medicationsButton.addActionListener(e -> fetchDetails("Medications")); // Listener for medications
    closeButton.addActionListener(e -> closeTab());

    // Panel for buttons
    JPanel buttonsPanel = new JPanel(new GridLayout(1, 6, 5, 5)); // Adjusted for additional button
    buttonsPanel.add(medicalHistoryButton);
    buttonsPanel.add(allergiesButton);
    buttonsPanel.add(insuranceButton);
    buttonsPanel.add(medicationsButton); // Add to panel
    buttonsPanel.add(backButton);
    buttonsPanel.add(closeButton);

    add(buttonsPanel, BorderLayout.NORTH);
    add(scrollPane, BorderLayout.CENTER);

    // Fetch initial appointment details
    fetchAppointmentDetails(appointmentId);
  }

  /**
   * Fetches and displays the initial details of the appointment.
   * 
   * @param appointmentId the ID of the appointment to fetch details for
   */
  private void fetchAppointmentDetails(int appointmentId) {
    String query = """
        SELECT a.Appointment_ID, a.Date, a.Start_Time, a.End_Time, a.Location,
               u.First_Name, u.Last_Name, u.Date_of_Birth, u.Phone_No, u.Email,
               p.Height, p.Weight
        FROM APPOINTMENT a
        JOIN PATIENT p ON a.Patient_User_ID = p.User_ID
        JOIN USER u ON p.User_ID = u.User_ID
        WHERE a.Appointment_ID = ?
        """;

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(query)) {
      pstmt.setInt(1, appointmentId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        String details = String.format(
            "Appointment ID: %d\nDate: %s\nStart Time: %s\nEnd Time: %s\nLocation: %s\n\n--- Patient Information ---\nName: %s %s\nDate of Birth: %s\nPhone No: %s\nEmail: %s\nHeight: %.1f m\nWeight: %.1f kg",
            rs.getInt("Appointment_ID"), rs.getDate("Date"), rs.getTime("Start_Time"),
            rs.getTime("End_Time"), rs.getString("Location"), rs.getString("First_Name"),
            rs.getString("Last_Name"), rs.getDate("Date_of_Birth"), rs.getString("Phone_No"),
            rs.getString("Email"), rs.getDouble("Height"), rs.getDouble("Weight"));
        detailsTextArea.setText(details);
        backButton.setVisible(false); // Hide the back button when showing initial details
      } else {
        detailsTextArea.setText("No details found for this appointment.");
      }
    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error retrieving appointment details: " + e.getMessage(), "Database Error",
          JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Fetches and displays specific details of the patient based on the provided type.
   * 
   * @param type the type of details to fetch (e.g., Medical, Allergies, Insurance, Medications)
   */
  private void fetchDetails(String type) {
    String sql = "";
    String label = "";

    switch (type) {
      case "Medical":
        sql = "SELECT Description, Date FROM MEDICAL_RECORDS WHERE User_ID = (SELECT Patient_User_ID FROM APPOINTMENT WHERE Appointment_ID = ?)";
        label = "Medical Records";
        break;
      case "Allergies":
        sql = "SELECT Allergy_Description FROM ALLERGY WHERE User_ID = (SELECT Patient_User_ID FROM APPOINTMENT WHERE Appointment_ID = ?)";
        label = "Allergies";
        break;
      case "Insurance":
        sql = """
            SELECT Insurance_Company_Name, Insurance_Company_Phone_No, Exp_Date
            FROM INSURANCE
            WHERE User_ID = (SELECT Patient_User_ID FROM APPOINTMENT WHERE Appointment_ID = ?)
            """;
        label = "Insurance";
        break;
      case "Medications":
        sql = "SELECT Medication_Description FROM MEDICATION WHERE User_ID = (SELECT Patient_User_ID FROM APPOINTMENT WHERE Appointment_ID = ?)";
        label = "Medications";
        break;
    }

    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, appointmentId);
      ResultSet rs = pstmt.executeQuery();

      StringBuilder details = new StringBuilder();
      details.append("--- ").append(label).append(" ---\n");

      ArrayList<String> records = new ArrayList<>();

      if (rs.next()) {
        switch (type) {
          case "Medical":
            do {
              records.add("Date: " + rs.getDate("Date") + "\nDescription: " + rs.getString("Description"));
            } while (rs.next());
            break;
          case "Allergies":
            do {
              records.add("Allergy: " + rs.getString("Allergy_Description"));
            } while (rs.next());
            break;
          case "Insurance":
            records.add("Insurance Company: " + rs.getString("Insurance_Company_Name") +
                "\nPhone: " + rs.getString("Insurance_Company_Phone_No") +
                "\nExpiry Date: " + rs.getDate("Exp_Date"));
            break;
          case "Medications":
            do {
              records.add("Medication: " + rs.getString("Medication_Description"));
            } while (rs.next());
            break;
        }

        details.append(String.join("\n\n", records));
      } else {
        details.append("No ").append(type.toLowerCase()).append(" records found.");
      }

      detailsTextArea.setText(details.toString());
      backButton.setVisible(true); // Show the back button when viewing specific details

    } catch (SQLException e) {
      e.printStackTrace();
      JOptionPane.showMessageDialog(this, "Error retrieving " + type.toLowerCase() + " details: " + e.getMessage(),
          "Database Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Closes the current tab containing this panel.
   */
  private void closeTab() {
    Container parent = this.getParent();
    if (parent instanceof JTabbedPane) {
      JTabbedPane tabPane = (JTabbedPane) parent;
      tabPane.remove(this);
    }
  }
}
