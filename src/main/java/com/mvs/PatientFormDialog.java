package com.mvs;

import javax.swing.*;
import java.awt.*;

public class PatientFormDialog extends JDialog {
  private JTextField txtHeight, txtWeight;
  private JButton btnSave, btnCancel;
  private int userId;

  // Constructor
  public PatientFormDialog(JFrame owner, Patient patient) {
    super(owner, patient == null ? "Add Patient" : "Edit Patient", true); // Set dialog title based on whether the patient is null
    setSize(300, 200);
    setLayout(new GridLayout(0, 2));

    // Initialize text fields and buttons
    txtHeight = new JTextField(10);
    txtWeight = new JTextField(10);
    btnSave = new JButton("Save");
    btnCancel = new JButton("Cancel");

    // Add components to the dialog
    add(new JLabel("Height (cm):"));
    add(txtHeight);
    add(new JLabel("Weight (kg):"));
    add(txtWeight);
    add(btnSave);
    add(btnCancel);

    // If editing an existing patient, populate the fields with existing data
    if (patient != null) {
      userId = patient.getUserId();
      txtHeight.setText(String.valueOf(patient.getHeight()));
      txtWeight.setText(String.valueOf(patient.getWeight()));
    }

    // Add action listeners for the buttons
    btnSave.addActionListener(e -> savePatient());
    btnCancel.addActionListener(e -> setVisible(false));
  }

  // Method to save patient data
  private void savePatient() {
    try {
      // Parse height and weight from the text fields
      double height = Double.parseDouble(txtHeight.getText());
      double weight = Double.parseDouble(txtWeight.getText());

      // Determine if adding a new patient or updating an existing one
      if (userId == 0) { // Assuming 0 means new patient
        PatientModel.addPatient(userId, height, weight);
      } else {
        PatientModel.updatePatient(userId, height, weight);
      }
      setVisible(false); // Close the dialog after saving
    } catch (NumberFormatException e) {
      JOptionPane.showMessageDialog(this, "Invalid input. Please enter valid numbers for height and weight.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}
