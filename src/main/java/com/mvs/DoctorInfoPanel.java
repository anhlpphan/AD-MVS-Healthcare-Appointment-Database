package com.mvs;

import javax.swing.*;
import java.awt.*;
import java.sql.*;

/**
 * DoctorInfoPanel is a JPanel that displays detailed information about a doctor.
 * The information includes first name, last name, specialization, years of experience, phone number, and email.
 * The data is loaded asynchronously from the database.
 */
public class DoctorInfoPanel extends JPanel {
  private int doctorUserId;

  /**
   * Constructs a DoctorInfoPanel with the specified doctor user ID.
   * 
   * @param doctorUserId the ID of the doctor whose information will be displayed
   */
  public DoctorInfoPanel(int doctorUserId) {
    this.doctorUserId = doctorUserId;
    setLayout(new BoxLayout(this, BoxLayout.Y_AXIS));
    setBorder(BorderFactory.createTitledBorder("Doctor Information"));
    setBackground(new Color(245, 245, 245)); // Optional: Set background color
    initializeUI();
  }

  /**
   * Initializes the user interface components.
   */
  private void initializeUI() {
    JLabel lblFirstName = new JLabel("First Name: Loading...");
    JLabel lblLastName = new JLabel("Last Name: Loading...");
    JLabel lblSpecialization = new JLabel("Specialization: Loading...");
    JLabel lblExperience = new JLabel("Years of Experience: Loading...");
    JLabel lblPhone = new JLabel("Phone No: Loading...");
    JLabel lblEmail = new JLabel("Email: Loading...");

    // Customizing labels for better appearance
    customizeLabel(lblFirstName);
    customizeLabel(lblLastName);
    customizeLabel(lblSpecialization);
    customizeLabel(lblExperience);
    customizeLabel(lblPhone);
    customizeLabel(lblEmail);

    // Load doctor data asynchronously
    SwingWorker<Void, Void> worker = new SwingWorker<Void, Void>() {
      @Override
      protected Void doInBackground() throws Exception {
        loadDoctorInformation(lblFirstName, lblLastName, lblSpecialization, lblExperience, lblPhone, lblEmail);
        return null;
      }
    };
    worker.execute();

    add(lblFirstName);
    add(lblLastName);
    add(lblSpecialization);
    add(lblExperience);
    add(lblPhone);
    add(lblEmail);
  }

  /**
   * Customizes the appearance of a JLabel.
   * 
   * @param label the JLabel to customize
   */
  private void customizeLabel(JLabel label) {
    label.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10)); // Add padding
    label.setFont(new Font("Arial", Font.PLAIN, 12)); // Set font style and size
  }

  /**
   * Loads doctor information from the database and updates the given labels.
   * 
   * @param labels the JLabels to update with doctor information
   */
  private void loadDoctorInformation(JLabel... labels) {
    try (Connection conn = DatabaseManager.getConnection();
         PreparedStatement pstmt = conn.prepareStatement("SELECT * FROM doctorinformation WHERE User_ID = ?")) {
      pstmt.setInt(1, doctorUserId);
      ResultSet rs = pstmt.executeQuery();
      if (rs.next()) {
        labels[0].setText("First Name: " + rs.getString("First_Name"));
        labels[1].setText("Last Name: " + rs.getString("Last_Name"));
        labels[2].setText("Specialization: " + rs.getString("Specialization"));
        labels[3].setText("Years of Experience: " + rs.getInt("Years_Of_Experience"));
        labels[4].setText("Phone No: " + rs.getString("Phone_No"));
        labels[5].setText("Email: " + rs.getString("Email"));
      } else {
        for (JLabel label : labels) {
          label.setText(label.getText().split(":")[0] + ": Not found");
        }
      }
    } catch (SQLException ex) {
      System.err.println("SQL Error: " + ex.getMessage());
      for (JLabel label : labels) {
        label.setText(label.getText().split(":")[0] + ": Error");
      }
    }
  }
}
