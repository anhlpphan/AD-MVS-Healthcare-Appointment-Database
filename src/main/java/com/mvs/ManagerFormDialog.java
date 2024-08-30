package com.mvs;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;

public class ManagerFormDialog extends JDialog {
  private JTextField txtStartDate, txtEndDate, txtOfficeNumber, txtDepartmentSupervisedId, txtSalary;
  private JButton btnSave, btnCancel;
  private Manager manager;

  /**
   * Constructor for the ManagerFormDialog.
   * @param owner The parent frame of the dialog.
   * @param manager The manager object to edit, or null if adding a new manager.
   */
  public ManagerFormDialog(JFrame owner, Manager manager) {
    super(owner, manager == null ? "Add Manager" : "Edit Manager", true);
    setSize(400, 300);
    setLayout(new GridLayout(0, 2));

    // Initialize text fields
    txtStartDate = new JTextField(10);
    txtEndDate = new JTextField(10);
    txtOfficeNumber = new JTextField(10);
    txtDepartmentSupervisedId = new JTextField(10);
    txtSalary = new JTextField(10);
    
    // Initialize buttons
    btnSave = new JButton("Save");
    btnCancel = new JButton("Cancel");

    // Add labels and text fields to the dialog
    add(new JLabel("Start Date:"));
    add(txtStartDate);
    add(new JLabel("End Date:"));
    add(txtEndDate);
    add(new JLabel("Office Number:"));
    add(txtOfficeNumber);
    add(new JLabel("Dept Supervised ID:"));
    add(txtDepartmentSupervisedId);
    add(new JLabel("Salary:"));
    add(txtSalary);
    add(btnSave);
    add(btnCancel);

    // If a manager is being edited, populate the fields with the manager's current data
    if (manager != null) {
      this.manager = manager;
      txtStartDate.setText(manager.getStartDate() != null ? manager.getStartDate().toString() : "");
      txtEndDate.setText(manager.getEndDate() != null ? manager.getEndDate().toString() : "");
      txtOfficeNumber.setText(manager.getOfficeNumber());
      txtDepartmentSupervisedId.setText(Integer.toString(manager.getDepartmentSupervisedId()));
      txtSalary.setText(Double.toString(manager.getSalary()));
    }

    // Add action listeners to buttons
    btnSave.addActionListener(e -> saveManager());
    btnCancel.addActionListener(e -> setVisible(false));
  }

  /**
   * Saves the manager information to the database.
   */
  private void saveManager() {
    try {
      // Parse input values
      Date startDate = Date.valueOf(txtStartDate.getText());
      Date endDate = Date.valueOf(txtEndDate.getText());
      String officeNumber = txtOfficeNumber.getText();
      int departmentSupervisedId = Integer.parseInt(txtDepartmentSupervisedId.getText());
      double salary = Double.parseDouble(txtSalary.getText());

      // Update the manager information in the database
      boolean updated = ManagerModel.updateManager(manager.getUserId(), startDate, endDate, officeNumber, departmentSupervisedId, salary);
      if (updated) {
        JOptionPane.showMessageDialog(this, "Manager updated successfully.");
      } else {
        JOptionPane.showMessageDialog(this, "Failed to update manager.");
      }
    } catch (IllegalArgumentException e) {
      // Show an error message if input values are invalid
      JOptionPane.showMessageDialog(this, "Please check your input values.", "Input Error", JOptionPane.ERROR_MESSAGE);
    }
    // Hide the dialog
    setVisible(false);
  }
}
