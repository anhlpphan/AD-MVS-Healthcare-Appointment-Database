package com.mvs;

import javax.swing.*;
import java.awt.*;

/**
 * DoctorFormDialog is a JDialog that allows for adding or editing a doctor's details.
 * It provides fields for license number, specialization, years of experience, salary, and department number.
 */
public class DoctorFormDialog extends JDialog {
  private JTextField txtLicense_No, txtSpecialization, txtYears_Of_Experience, txtSalary, txtDepartmentNo;
  private JButton btnSave, btnCancel;
  private int userId;

  /**
   * Constructs a DoctorFormDialog for adding or editing a doctor's details.
   * 
   * @param owner the parent frame
   * @param doctor the doctor object to edit, or null to add a new doctor
   */
  public DoctorFormDialog(JFrame owner, Doctor doctor) {
    super(owner, doctor == null ? "Add Doctor" : "Edit Doctor", true);
    setSize(300, 200);
    setLayout(new GridLayout(0, 2));

    // Initialize text fields and buttons
    txtLicense_No = new JTextField(10);
    txtSpecialization = new JTextField(10);
    txtYears_Of_Experience = new JTextField(10);
    txtSalary = new JTextField(10);
    txtDepartmentNo = new JTextField(10);  // New text field for department number
    btnSave = new JButton("Save");
    btnCancel = new JButton("Cancel");

    // Add components to the dialog
    add(new JLabel("License No:"));
    add(txtLicense_No);
    add(new JLabel("Specialization:"));
    add(txtSpecialization);
    add(new JLabel("Years Of Experience:"));
    add(txtYears_Of_Experience);
    add(new JLabel("Salary:"));
    add(txtSalary);
    add(new JLabel("Department No:"));  // Label for the new field
    add(txtDepartmentNo);
    add(btnSave);
    add(btnCancel);

    // If editing an existing doctor, populate fields with doctor's current details
    if (doctor != null) {
      userId = doctor.getUserId();
      txtLicense_No.setText(doctor.getLicenseNo());
      txtSpecialization.setText(doctor.getSpecialization());
      txtYears_Of_Experience.setText(String.valueOf(doctor.getYearsOfExperience()));
      txtSalary.setText(String.valueOf(doctor.getSalary()));
      txtDepartmentNo.setText(String.valueOf(doctor.getDepartmentNo()));  // Set department number if editing
    }

    // Add action listeners for save and cancel buttons
    btnSave.addActionListener(e -> saveDoctor());
    btnCancel.addActionListener(e -> setVisible(false));
  }

  /**
   * Saves the doctor's details by updating the database.
   */
  private void saveDoctor() {
    String licenseNo = txtLicense_No.getText();
    String specialization = txtSpecialization.getText();
    int yearsExperience;
    double salary;
    int departmentNo;

    try {
      // Parse input values
      yearsExperience = Integer.parseInt(txtYears_Of_Experience.getText());
      salary = Double.parseDouble(txtSalary.getText());
      departmentNo = Integer.parseInt(txtDepartmentNo.getText());

      // Call the updateDoctor method with individual parameters
      boolean result = DoctorModel.updateDoctor(userId, licenseNo, specialization, yearsExperience, salary, departmentNo);
      if (result) {
        JOptionPane.showMessageDialog(this, "Doctor updated successfully");
        setVisible(false);  // Close the dialog if update is successful
      } else {
        JOptionPane.showMessageDialog(this, "Update failed", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } catch (NumberFormatException e) {
      // Handle input errors
      JOptionPane.showMessageDialog(this, "Please check your input values", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
  }
}
