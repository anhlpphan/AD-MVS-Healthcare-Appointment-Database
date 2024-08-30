package com.mvs;

import javax.swing.*;
import java.awt.*;
import java.sql.Date;
import java.sql.Timestamp;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserFormDialog extends JDialog {
  // Form fields for user input
  private JTextField txtUsername, txtPassword, txtFirstName, txtMiddleName, txtLastName, txtEmail, txtPhoneNo,
      txtStreet, txtCity, txtState, txtZipCode, txtSSN;
  private JComboBox<String> comboSex;
  private JComboBox<UserType> comboUserType;
  private JSpinner dateOfBirthSpinner, dateRegisteredSpinner;
  private JButton btnSave, btnCancel;
  private int userId = -1; // Default -1, signifies a new user

  // Constructor to initialize the dialog
  public UserFormDialog(JFrame owner, User user) {
    super(owner, user == null ? "Add User" : "Edit User", true);
    setSize(500, 600);
    setLayout(new GridLayout(0, 2));

    // Initialize user types
    comboUserType = new JComboBox<>();
    loadUserTypes(); // Load user types from the database

    // Add form fields to the dialog
    add(new JLabel("User Type:"));
    add(comboUserType);

    add(new JLabel("Username:"));
    txtUsername = new JTextField(20);
    add(txtUsername);

    add(new JLabel("Password:"));
    txtPassword = new JTextField(20);
    add(txtPassword);

    add(new JLabel("First Name:"));
    txtFirstName = new JTextField(20);
    add(txtFirstName);

    add(new JLabel("Middle Name:"));
    txtMiddleName = new JTextField(20);
    add(txtMiddleName);

    add(new JLabel("Last Name:"));
    txtLastName = new JTextField(20);
    add(txtLastName);

    add(new JLabel("Email:"));
    txtEmail = new JTextField(20);
    add(txtEmail);

    add(new JLabel("Phone No:"));
    txtPhoneNo = new JTextField(20);
    add(txtPhoneNo);

    add(new JLabel("Street:"));
    txtStreet = new JTextField(20);
    add(txtStreet);

    add(new JLabel("City:"));
    txtCity = new JTextField(20);
    add(txtCity);

    add(new JLabel("State:"));
    txtState = new JTextField(20);
    add(txtState);

    add(new JLabel("Zip Code:"));
    txtZipCode = new JTextField(20);
    add(txtZipCode);

    add(new JLabel("SSN:"));
    txtSSN = new JTextField(20);
    add(txtSSN);

    add(new JLabel("Sex:"));
    comboSex = new JComboBox<>(new String[] { "M", "F", "Other" });
    add(comboSex);

    add(new JLabel("Date of Birth:"));
    dateOfBirthSpinner = new JSpinner(new SpinnerDateModel());
    add(dateOfBirthSpinner);

    add(new JLabel("Date Registered:"));
    dateRegisteredSpinner = new JSpinner(new SpinnerDateModel());
    add(dateRegisteredSpinner);

    // Save and Cancel buttons
    btnSave = new JButton("Save");
    btnSave.addActionListener(e -> saveUser()); // Add action listener to save user details
    add(btnSave);

    btnCancel = new JButton("Cancel");
    btnCancel.addActionListener(e -> setVisible(false)); // Add action listener to cancel the dialog
    add(btnCancel);

    // If user is not null, populate fields with user details
    if (user != null) {
      userId = user.getUserId();
      txtUsername.setText(user.getUsername());
      txtPassword.setText(user.getPassword());
      txtFirstName.setText(user.getFirstName());
      txtMiddleName.setText(user.getMiddleName());
      txtLastName.setText(user.getLastName());
      txtEmail.setText(user.getEmail());
      txtPhoneNo.setText(user.getPhoneNo());
      txtStreet.setText(user.getStreet());
      txtCity.setText(user.getCity());
      txtState.setText(user.getState());
      txtZipCode.setText(user.getZipCode());
      txtSSN.setText(user.getSSN());
      comboSex.setSelectedItem(user.getSex());
      dateOfBirthSpinner.setValue(user.getDateOfBirth());
      dateRegisteredSpinner.setValue(user.getDateRegistered());
      comboUserType.setSelectedItem(new UserType(user.getUserTypeID(), "")); // Assuming UserType has an appropriate constructor
    }
  }

  // Load user types from the database into the combo box
  private void loadUserTypes() {
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT User_Type_ID, Description FROM USER_TYPE");
        ResultSet rs = ps.executeQuery()) {
      while (rs.next()) {
        comboUserType.addItem(new UserType(rs.getInt("User_Type_ID"), rs.getString("Description")));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  // Save user details to the database
  private void saveUser() {
    String username = txtUsername.getText();
    String password = txtPassword.getText();
    String firstName = txtFirstName.getText();
    String middleName = txtMiddleName.getText();
    String lastName = txtLastName.getText();
    String email = txtEmail.getText();
    String phoneNo = txtPhoneNo.getText();
    String street = txtStreet.getText();
    String city = txtCity.getText();
    String state = txtState.getText();
    String zipCode = txtZipCode.getText();
    String ssn = txtSSN.getText();
    String sex = (String) comboSex.getSelectedItem();
    Date dateOfBirth = new Date(((java.util.Date) dateOfBirthSpinner.getValue()).getTime());
    Timestamp dateRegistered = new Timestamp(((java.util.Date) dateRegisteredSpinner.getValue()).getTime());
    UserType selectedType = (UserType) comboUserType.getSelectedItem();
    int userTypeID = selectedType.getId();

    // If userId is -1, add a new user, otherwise update the existing user
    if (userId == -1) { // Adding new user
      UserModel.addUser(username, password, firstName, middleName, lastName, dateOfBirth, sex, phoneNo, street, city,
          state, zipCode, dateRegistered, userTypeID, email, ssn);
    } else { // Updating existing user
      UserModel.updateUser(userId, username, password, firstName, middleName, lastName, dateOfBirth, sex, phoneNo,
          street, city, state, zipCode, dateRegistered, userTypeID, email, ssn);
    }
    setVisible(false); // Close the dialog
  }

  // Inner class to represent user types
  class UserType {
    private int id;
    private String description;

    public UserType(int id, String description) {
      this.id = id;
      this.description = description;
    }

    public int getId() {
      return id;
    }

    public String getDescription() {
      return description;
    }

    @Override
    public String toString() {
      return description;
    }
  }
}
