package com.mvs;

import java.sql.Date; // For SQL date handling in database operations
import java.util.ArrayList;
import java.util.List; // Import the List class
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

/**
 * MainFrame is the main window of the application, handling different user types and CRUD operations.
 * The user types include Admin, Patient, Doctor, and Manager.
 */
public class MainFrame extends JFrame {
  // Define tables and buttons for various entities
  private JTable tableUsers, tablePatients, tableDoctors, tableManagers, tableRecords, tableBranches, tableDepartments;
  private JButton btnLoadUsers, btnAddUser, btnUpdateUser, btnDeleteUser;
  private JButton btnLoadPatients, btnUpdatePatient, btnDeletePatient, btnLoadPatientProfiles;
  private JButton btnLoadDoctors, btnUpdateDoctor, btnDeleteDoctor, btnLoadDoctorInfo;
  private JButton btnLoadManagers, btnUpdateManager, btnDeleteManager, btnManagerOversight;
  private JButton btnLoadRecords, btnAddRecord, btnUpdateRecord, btnDeleteRecord;
  private JButton btnLoadBranches, btnAddBranch, btnUpdateBranch, btnDeleteBranch;
  private JButton btnLoadDepartments, btnAddDepartment, btnUpdateDepartment, btnDeleteDepartment;

  private JTabbedPane tabbedPane;
  private int userID; // Store the user's ID after login
  private MedicalRecordsModel recordsModel = new MedicalRecordsModel();
  private JTable tableMedications;
  private JButton btnLoadMedications, btnAddMedication, btnUpdateMedication, btnDeleteMedication;
  private MedicationModel medicationModel = new MedicationModel();

  private JTable tableAllergies;
  private JButton btnLoadAllergies, btnAddAllergy, btnUpdateAllergy, btnDeleteAllergy;
  private AllergyModel allergyModel = new AllergyModel();

  private JTable tableInsurance;
  private JButton btnLoadInsurance, btnAddInsurance, btnUpdateInsurance, btnDeleteInsurance;

  /**
   * Constructs a MainFrame based on user type and ID.
   * 
   * @param userTypeID the type of user (Admin, Patient, Doctor, Manager)
   * @param userID the ID of the user
   */
  public MainFrame(int userTypeID, int userID) {
    super("Database CRUD Operations");
    this.userID = userID;
    initializeComponents(userTypeID);
    setSize(1100, 600);
    setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
  }

  /**
   * Initializes UI components based on user type.
   * 
   * @param userTypeID the type of user
   */
  private void initializeComponents(int userTypeID) {
    tabbedPane = new JTabbedPane();

    if (userTypeID == 9) { // Admin
      tabbedPane.addTab("Admin Search", new AdminSearchPanel());
      setupManagersPanel();
      setupDoctorsPanel();
      setupPatientsPanel();
      setupUsersPanel();
      setupBranchPanel();  // Setup the new Branch panel
      setupDepartmentPanel();  // Setup the new Department panel
    } else if (userTypeID == 1) { // Patient
      setupPatientSpecificPanels();
      setupInsurancePanel(); // Ensure this is added before Medical Records
      setupMedicationPanel(); // Setup the new Medication panel
      setupAllergyPanel(); // Add this line to initialize the allergy panel
    } else if (userTypeID == 2) { // Doctor
      setupDoctorSpecificPanels(); // This method will handle doctor-specific UI components
    } else if (userTypeID == 3) { // Manager
      setupManagerUI();
    }

    add(tabbedPane, BorderLayout.CENTER);
  }

  /**
   * Sets up the manager's UI components.
   */
  private void setupManagerUI() {
    DepartmentManagerPanel managerPanel = new DepartmentManagerPanel(userID);
    tabbedPane.addTab("Department Manager", managerPanel);

    // Create and add the DoctorScheduleCalendar panel to the tabbedPane
    DoctorScheduleCalendar scheduleCalendar = new DoctorScheduleCalendar(userID); // Assuming it's been converted to JPanel
    tabbedPane.addTab("Doctor Scheduling", scheduleCalendar);
  }

  /**
   * Sets up the doctor's UI components.
   */
  private void setupDoctorSpecificPanels() {
    DoctorDashboardPanel doctorDashboardPanel = new DoctorDashboardPanel(this.userID);
    tabbedPane.addTab("Doctor Dashboard", doctorDashboardPanel);
  }

  /**
   * Sets up the insurance panel for the patient.
   */
  private void setupInsurancePanel() {
    JPanel insurancePanel = new JPanel(new BorderLayout());
    tableInsurance = new JTable();
    loadInsurance(); // Load insurance records into the table
    JScrollPane scrollPane = new JScrollPane(tableInsurance);
    insurancePanel.add(scrollPane, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    btnLoadInsurance = new JButton("Load Insurance");
    btnAddInsurance = new JButton("Add Insurance");
    btnUpdateInsurance = new JButton("Update Insurance");
    btnDeleteInsurance = new JButton("Delete Insurance");

    buttonPanel.add(btnLoadInsurance);
    buttonPanel.add(btnAddInsurance);
    buttonPanel.add(btnUpdateInsurance);
    buttonPanel.add(btnDeleteInsurance);

    insurancePanel.add(buttonPanel, BorderLayout.SOUTH);
    tabbedPane.addTab("Insurance", insurancePanel);

    // Ensure action listeners are correctly set up
    btnLoadInsurance.addActionListener(e -> loadInsurance());
    btnAddInsurance.addActionListener(e -> addInsurance());
    btnUpdateInsurance.addActionListener(e -> updateInsurance());
    btnDeleteInsurance.addActionListener(e -> deleteInsurance());
  }

  /**
   * Loads insurance records into the table.
   */
  private void loadInsurance() {
    List<InsuranceRecord> records = new InsuranceModel().getInsuranceRecords(userID);
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("Insurance Card No");
    model.addColumn("Company Name");
    model.addColumn("Phone No");
    model.addColumn("Primary Doctor ID");
    model.addColumn("Expiration Date");

    for (InsuranceRecord record : records) {
      model.addRow(new Object[] {
          record.getInsuranceCardNo(),
          record.getInsuranceCompanyName(),
          record.getInsuranceCompanyPhoneNo(),
          record.getPrimaryDoctorUserID(),
          record.getExpDate()
      });
    }
    tableInsurance.setModel(model);
  }

  /**
   * Adds a new insurance record.
   */
  private void addInsurance() {
    JPanel panel = new JPanel(new GridLayout(0, 2));
    JTextField insuranceCardNoField = new JTextField();
    JTextField insuranceCompanyNameField = new JTextField();
    JTextField insuranceCompanyPhoneNoField = new JTextField();
    JTextField primaryDoctorUserIDField = new JTextField();
    JTextField expDateField = new JTextField();

    panel.add(new JLabel("Insurance Card No:"));
    panel.add(insuranceCardNoField);
    panel.add(new JLabel("Company Name:"));
    panel.add(insuranceCompanyNameField);
    panel.add(new JLabel("Company Phone No:"));
    panel.add(insuranceCompanyPhoneNoField);
    panel.add(new JLabel("Primary Doctor User ID:"));
    panel.add(primaryDoctorUserIDField);
    panel.add(new JLabel("Expiration Date (yyyy-mm-dd):"));
    panel.add(expDateField);

    int result = JOptionPane.showConfirmDialog(null, panel, "Add Insurance Record", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      try {
        InsuranceRecord record = new InsuranceRecord(
            insuranceCardNoField.getText(),
            userID, // Assumes userID is the ID of the patient
            insuranceCompanyNameField.getText(),
            insuranceCompanyPhoneNoField.getText(),
            Integer.parseInt(primaryDoctorUserIDField.getText()),
            java.sql.Date.valueOf(expDateField.getText()) // Explicit use of java.sql.Date
        );
        new InsuranceModel().addInsuranceRecord(record);
        loadInsurance(); // Refresh the insurance table
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error adding insurance record: " + e.getMessage());
      }
    }
  }

  /**
   * Updates an existing insurance record.
   */
  private void updateInsurance() {
    int selectedRow = tableInsurance.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(null, "Please select an insurance record to update.");
      return;
    }

    // Assuming model columns match the order they are added in loadInsurance()
    String insuranceCardNo = (String) tableInsurance.getValueAt(selectedRow, 0);
    String company = (String) tableInsurance.getValueAt(selectedRow, 1);
    String phone = (String) tableInsurance.getValueAt(selectedRow, 2);
    int primaryDoctorID = (Integer) tableInsurance.getValueAt(selectedRow, 3);
    Date expDate = (Date) tableInsurance.getValueAt(selectedRow, 4);

    JPanel panel = new JPanel(new GridLayout(0, 2));
    JTextField companyField = new JTextField(company);
    JTextField phoneField = new JTextField(phone);
    JTextField primaryDoctorIDField = new JTextField(String.valueOf(primaryDoctorID));
    JTextField expDateField = new JTextField(expDate.toString());

    panel.add(new JLabel("Company Name:"));
    panel.add(companyField);
    panel.add(new JLabel("Company Phone No:"));
    panel.add(phoneField);
    panel.add(new JLabel("Primary Doctor User ID:"));
    panel.add(primaryDoctorIDField);
    panel.add(new JLabel("Expiration Date (yyyy-mm-dd):"));
    panel.add(expDateField);

    int result = JOptionPane.showConfirmDialog(null, panel, "Update Insurance Record", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      try {
        InsuranceRecord record = new InsuranceRecord(
            insuranceCardNo,
            userID,
            companyField.getText(),
            phoneField.getText(),
            Integer.parseInt(primaryDoctorIDField.getText()),
            Date.valueOf(expDateField.getText()));
        new InsuranceModel().updateInsuranceRecord(record);
        loadInsurance(); // Refresh the insurance table
      } catch (Exception e) {
        JOptionPane.showMessageDialog(null, "Error updating insurance record: " + e.getMessage());
      }
    }
  }

  /**
   * Deletes an insurance record.
   */
  private void deleteInsurance() {
    int selectedRow = tableInsurance.getSelectedRow();
    if (selectedRow == -1) {
      JOptionPane.showMessageDialog(null, "Please select an insurance record to delete.");
      return;
    }

    String insuranceCardNo = (String) tableInsurance.getValueAt(selectedRow, 0);
    int confirm = JOptionPane.showConfirmDialog(null, "Are you sure you want to delete this record?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      new InsuranceModel().deleteInsuranceRecord(insuranceCardNo);
      loadInsurance(); // Refresh the insurance table
    }
  }

  /**
   * Sets up panels specific to patients.
   */
  private void setupPatientSpecificPanels() {
    tabbedPane = new JTabbedPane();

    // Panel for Current Appointments
    AppointmentsPanel appointmentsPanel = new AppointmentsPanel(this.userID);
    tabbedPane.addTab("Dashboard", appointmentsPanel);

    // Panel for Medical Records with CRUD operations
    JPanel recordsPanel = new JPanel(new BorderLayout());
    tableRecords = new JTable();
    loadMedicalRecords(); // Loads medical records into the table
    JScrollPane scrollPaneRecords = new JScrollPane(tableRecords);
    recordsPanel.add(scrollPaneRecords, BorderLayout.CENTER);

    JPanel recordButtonPanel = new JPanel();
    btnLoadRecords = new JButton("Refresh Records");
    btnAddRecord = new JButton("Add Record");
    btnUpdateRecord = new JButton("Update Record");
    btnDeleteRecord = new JButton("Delete Record");

    recordButtonPanel.add(btnLoadRecords);
    recordButtonPanel.add(btnAddRecord);
    recordButtonPanel.add(btnUpdateRecord);
    recordButtonPanel.add(btnDeleteRecord);

    btnLoadRecords.addActionListener(e -> loadMedicalRecords());
    btnAddRecord.addActionListener(e -> addMedicalRecord());
    btnUpdateRecord.addActionListener(e -> updateSelectedMedicalRecord());
    btnDeleteRecord.addActionListener(e -> deleteSelectedMedicalRecord());

    recordsPanel.add(recordButtonPanel, BorderLayout.SOUTH);
    tabbedPane.addTab("Medical Records", recordsPanel);
  }

  /**
   * Loads medical records into the table.
   */
  private void loadMedicalRecords() {
    ArrayList<MedicalRecord> records = recordsModel.getMedicalRecords();
    DefaultTableModel tableModel = new DefaultTableModel(new Object[] { "Record ID", "User ID", "Description", "Date" }, 0);
    for (MedicalRecord record : records) {
      tableModel.addRow(new Object[] { record.getRecordID(), record.getUserID(), record.getDescription(), record.getDate() });
    }
    tableRecords.setModel(tableModel);
  }

  /**
   * Adds a new medical record.
   */
  private void addMedicalRecord() {
    // Assuming record details are hardcoded for demonstration; replace with a dialog input form in real application
    MedicalRecord record = new MedicalRecord(-1, this.userID, "Example Description", new java.util.Date());
    recordsModel.addMedicalRecord(record);
    loadMedicalRecords(); // Refresh the table to show the new record
  }

  /**
   * Updates the selected medical record.
   */
  private void updateSelectedMedicalRecord() {
    int row = tableRecords.getSelectedRow();
    if (row >= 0) {
      int recordID = Integer.parseInt(tableRecords.getValueAt(row, 0).toString());
      String description = JOptionPane.showInputDialog("Enter new description:");
      java.util.Date date = new java.util.Date(); // Using current date for simplicity

      System.out.println("Updating record: " + recordID + " with new description: " + description);

      if (description != null && !description.isEmpty()) {
        MedicalRecord record = new MedicalRecord(recordID, userID, description, date);
        recordsModel.updateMedicalRecord(record);
        loadMedicalRecords(); // Refresh the table to show updated data
      } else {
        JOptionPane.showMessageDialog(this, "Description cannot be empty.");
      }
    } else {
      JOptionPane.showMessageDialog(this, "Select a record to update.");
    }
  }

  /**
   * Deletes the selected medical record.
   */
  private void deleteSelectedMedicalRecord() {
    int row = tableRecords.getSelectedRow();
    if (row >= 0) {
      int recordID = (int) tableRecords.getValueAt(row, 0);
      recordsModel.deleteMedicalRecord(recordID);
      loadMedicalRecords(); // Refresh list after delete
    } else {
      JOptionPane.showMessageDialog(this, "Select a record to delete.");
    }
  }

  /**
   * Sets up the managers panel.
   */
  private void setupManagersPanel() {
    JPanel managersPanel = new JPanel(new BorderLayout());
    tableManagers = new JTable();
    JScrollPane scrollPaneUsers = new JScrollPane(tableManagers);
    managersPanel.add(scrollPaneUsers, BorderLayout.CENTER);

    JPanel managerButtonPanel = new JPanel();
    btnLoadManagers = new JButton("Load Manager");
    btnUpdateManager = new JButton("Update Manager");
    btnDeleteManager = new JButton("Delete Manager");
    btnManagerOversight = new JButton("Manager Oversight");

    btnLoadManagers.addActionListener(e -> loadManagers());
    btnUpdateManager.addActionListener(e -> updateManager());
    btnDeleteManager.addActionListener(e -> deleteManager());
    btnManagerOversight.addActionListener(e -> managerOversight());

    managerButtonPanel.add(btnLoadManagers);
    managerButtonPanel.add(btnUpdateManager);
    managerButtonPanel.add(btnDeleteManager);
    managerButtonPanel.add(btnManagerOversight);
    managersPanel.add(managerButtonPanel, BorderLayout.SOUTH);

    tabbedPane.addTab("Manager", managersPanel);
  }

  /**
   * Sets up the users panel.
   */
  private void setupUsersPanel() {
    JPanel usersPanel = new JPanel(new BorderLayout());
    tableUsers = new JTable();
    JScrollPane scrollPaneUsers = new JScrollPane(tableUsers);
    usersPanel.add(scrollPaneUsers, BorderLayout.CENTER);

    JPanel userButtonPanel = new JPanel();
    btnLoadUsers = new JButton("Load Users");
    btnAddUser = new JButton("Add User");
    btnUpdateUser = new JButton("Update User");
    btnDeleteUser = new JButton("Delete User");

    btnLoadUsers.addActionListener(e -> loadUsers());
    btnAddUser.addActionListener(e -> addUser());
    btnUpdateUser.addActionListener(e -> updateUser());
    btnDeleteUser.addActionListener(e -> deleteUser());

    userButtonPanel.add(btnLoadUsers);
    userButtonPanel.add(btnAddUser);
    userButtonPanel.add(btnUpdateUser);
    userButtonPanel.add(btnDeleteUser);
    usersPanel.add(userButtonPanel, BorderLayout.SOUTH);

    tabbedPane.addTab("Users", usersPanel);
  }

  /**
   * Sets up the patients panel.
   */
  private void setupPatientsPanel() {
    JPanel patientsPanel = new JPanel(new BorderLayout());
    tablePatients = new JTable();
    JScrollPane scrollPanePatients = new JScrollPane(tablePatients);
    patientsPanel.add(scrollPanePatients, BorderLayout.CENTER);

    JPanel patientButtonPanel = new JPanel();
    btnLoadPatients = new JButton("Load Patients");
    btnUpdatePatient = new JButton("Update Patient");
    btnDeletePatient = new JButton("Delete Patient");
    btnLoadPatientProfiles = new JButton("Load Patient Profiles");

    btnLoadPatients.addActionListener(e -> loadPatients());
    btnUpdatePatient.addActionListener(e -> updatePatient());
    btnDeletePatient.addActionListener(e -> deletePatient());
    btnLoadPatientProfiles.addActionListener(e -> loadPatientProfiles());

    patientButtonPanel.add(btnLoadPatients);
    patientButtonPanel.add(btnUpdatePatient);
    patientButtonPanel.add(btnDeletePatient);
    patientButtonPanel.add(btnLoadPatientProfiles);
    patientsPanel.add(patientButtonPanel, BorderLayout.SOUTH);

    tabbedPane.addTab("Patients", patientsPanel);
  }

  /**
   * Sets up the doctors panel.
   */
  private void setupDoctorsPanel() {
    JPanel doctorsPanel = new JPanel(new BorderLayout());
    tableDoctors = new JTable();
    JScrollPane scrollPaneDoctors = new JScrollPane(tableDoctors);
    doctorsPanel.add(scrollPaneDoctors, BorderLayout.CENTER);

    JPanel doctorButtonPanel = new JPanel();
    btnLoadDoctors = new JButton("Load Doctors");
    btnUpdateDoctor = new JButton("Update Doctor");
    btnDeleteDoctor = new JButton("Delete Doctor");
    btnLoadDoctorInfo = new JButton("Doctor Info");

    btnLoadDoctors.addActionListener(e -> loadDoctors());
    btnUpdateDoctor.addActionListener(e -> updateDoctor());
    btnDeleteDoctor.addActionListener(e -> deleteDoctor());
    btnLoadDoctorInfo.addActionListener(e -> loadDoctorInfo());

    doctorButtonPanel.add(btnLoadDoctors);
    doctorButtonPanel.add(btnUpdateDoctor);
    doctorButtonPanel.add(btnDeleteDoctor);
    doctorButtonPanel.add(btnLoadDoctorInfo);
    doctorsPanel.add(doctorButtonPanel, BorderLayout.SOUTH);

    tabbedPane.addTab("Doctors", doctorsPanel);
  }

  /**
   * Loads user data into the table.
   */
  private void loadUsers() {
    tableUsers.setModel(UserModel.getUsersTableModel());
  }

  /**
   * Opens the dialog to add a new user.
   */
  private void addUser() {
    UserFormDialog addDialog = new UserFormDialog(this, null);
    addDialog.setVisible(true);
    loadUsers();
  }

  /**
   * Opens the dialog to update an existing user.
   */
  private void updateUser() {
    int selectedRow = tableUsers.getSelectedRow();
    if (selectedRow >= 0) {
      int userId = (int) tableUsers.getValueAt(selectedRow, 0);
      User user = UserModel.getUserById(userId);
      if (user != null) {
        UserFormDialog updateDialog = new UserFormDialog(this, user);
        updateDialog.setVisible(true);
        loadUsers();
      } else {
        JOptionPane.showMessageDialog(this, "Failed to fetch user details.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(this, "Select a user to update", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Sets up the branch panel.
   */
  private void setupBranchPanel() {
    JPanel branchPanel = new JPanel(new BorderLayout());
    tableBranches = new JTable();
    JScrollPane scrollPaneBranches = new JScrollPane(tableBranches);
    branchPanel.add(scrollPaneBranches, BorderLayout.CENTER);

    JPanel branchButtonPanel = new JPanel();
    btnLoadBranches = new JButton("Load Branches");
    btnAddBranch = new JButton("Add Branch");
    btnUpdateBranch = new JButton("Update Branch");
    btnDeleteBranch = new JButton("Delete Branch");

    branchButtonPanel.add(btnLoadBranches);
    branchButtonPanel.add(btnAddBranch);
    branchButtonPanel.add(btnUpdateBranch);
    branchButtonPanel.add(btnDeleteBranch);

    branchPanel.add(branchButtonPanel, BorderLayout.SOUTH);

    btnLoadBranches.addActionListener(e -> loadBranches());
    btnAddBranch.addActionListener(e -> addBranch());
    btnUpdateBranch.addActionListener(e -> updateBranch());
    btnDeleteBranch.addActionListener(e -> deleteBranch());

    tabbedPane.addTab("Branches", branchPanel);
  }

  /**
   * Loads branch data into the table.
   */
  private void loadBranches() {
    try {
      tableBranches.setModel(BranchModel.getBranchesTableModel());
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Error loading branches: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Opens the dialog to add a new branch.
   */
  private void addBranch() {
    JTextField locationField = new JTextField();
    JTextField branchNameField = new JTextField();
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Location:"));
    panel.add(locationField);
    panel.add(new JLabel("Branch Name:"));
    panel.add(branchNameField);

    int result = JOptionPane.showConfirmDialog(null, panel, "Add New Branch", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      String location = locationField.getText();
      String branchName = branchNameField.getText();
      if (!location.isEmpty() && !branchName.isEmpty()) {
        BranchModel.addBranch(location, branchName);
        loadBranches();
      } else {
        JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * Opens the dialog to update an existing branch.
   */
  private void updateBranch() {
    int row = tableBranches.getSelectedRow();
    if (row < 0) {
      JOptionPane.showMessageDialog(this, "Please select a branch to update", "Selection Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    int branchNo = Integer.parseInt(tableBranches.getValueAt(row, 0).toString());
    String currentLocation = tableBranches.getValueAt(row, 1).toString();
    String currentBranchName = tableBranches.getValueAt(row, 2).toString();

    JTextField locationField = new JTextField(currentLocation);
    JTextField branchNameField = new JTextField(currentBranchName);
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Location:"));
    panel.add(locationField);
    panel.add(new JLabel("Branch Name:"));
    panel.add(branchNameField);

    int result = JOptionPane.showConfirmDialog(null, panel, "Update Branch", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      String location = locationField.getText();
      String branchName = branchNameField.getText();
      if (!location.isEmpty() && !branchName.isEmpty()) {
        BranchModel.updateBranch(branchNo, location, branchName);
        loadBranches();
      } else {
        JOptionPane.showMessageDialog(this, "Please fill in all fields", "Input Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * Deletes an existing branch.
   */
  private void deleteBranch() {
    int row = tableBranches.getSelectedRow();
    if (row < 0) {
      JOptionPane.showMessageDialog(this, "Please select a branch to delete", "Selection Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    int branchNo = Integer.parseInt(tableBranches.getValueAt(row, 0).toString());
    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this branch?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      BranchModel.deleteBranch(branchNo);
      loadBranches();
    }
  }

  /**
   * Sets up the department panel.
   */
  private void setupDepartmentPanel() {
    JPanel departmentPanel = new JPanel(new BorderLayout());
    tableDepartments = new JTable();
    JScrollPane scrollPaneDepartments = new JScrollPane(tableDepartments);
    departmentPanel.add(scrollPaneDepartments, BorderLayout.CENTER);

    JPanel departmentButtonPanel = new JPanel();
    btnLoadDepartments = new JButton("Load Departments");
    btnAddDepartment = new JButton("Add Department");
    btnUpdateDepartment = new JButton("Update Department");
    btnDeleteDepartment = new JButton("Delete Department");

    departmentButtonPanel.add(btnLoadDepartments);
    departmentButtonPanel.add(btnAddDepartment);
    departmentButtonPanel.add(btnUpdateDepartment);
    departmentButtonPanel.add(btnDeleteDepartment);

    departmentPanel.add(departmentButtonPanel, BorderLayout.SOUTH);

    btnLoadDepartments.addActionListener(e -> loadDepartments());
    btnAddDepartment.addActionListener(e -> addDepartment());
    btnUpdateDepartment.addActionListener(e -> updateDepartment());
    btnDeleteDepartment.addActionListener(e -> deleteDepartment());

    tabbedPane.addTab("Departments", departmentPanel);
  }

  /**
   * Loads department data into the table.
   */
  private void loadDepartments() {
    try {
      tableDepartments.setModel(DepartmentModel.getDepartmentsTableModel());
    } catch (Exception e) {
      JOptionPane.showMessageDialog(this, "Error loading departments: " + e.getMessage(), "Database Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Opens the dialog to add a new department.
   */
  private void addDepartment() {
    JTextField departmentNameField = new JTextField();
    JTextField branchNoField = new JTextField();
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Department Name:"));
    panel.add(departmentNameField);
    panel.add(new JLabel("Branch No:"));
    panel.add(branchNoField);

    int result = JOptionPane.showConfirmDialog(null, panel, "Add New Department", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      String departmentName = departmentNameField.getText();
      int branchNo;
      try {
        branchNo = Integer.parseInt(branchNoField.getText());
        DepartmentModel.addDepartment(departmentName, branchNo);
        loadDepartments();  // Refresh the department list after adding a new one
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid input for branch number", "Input Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * Opens the dialog to update an existing department.
   */
  private void updateDepartment() {
    int row = tableDepartments.getSelectedRow();
    if (row < 0) {
      JOptionPane.showMessageDialog(this, "Please select a department to update", "Selection Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    int departmentNo = Integer.parseInt(tableDepartments.getValueAt(row, 0).toString());
    String currentDepartmentName = tableDepartments.getValueAt(row, 1).toString();
    int currentBranchNo = Integer.parseInt(tableDepartments.getValueAt(row, 2).toString());

    JTextField departmentNameField = new JTextField(currentDepartmentName);
    JTextField branchNoField = new JTextField(String.valueOf(currentBranchNo));
    JPanel panel = new JPanel(new GridLayout(0, 1));
    panel.add(new JLabel("Department Name:"));
    panel.add(departmentNameField);
    panel.add(new JLabel("Branch No:"));
    panel.add(branchNoField);

    int result = JOptionPane.showConfirmDialog(null, panel, "Update Department", JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);
    if (result == JOptionPane.OK_OPTION) {
      String departmentName = departmentNameField.getText();
      int branchNo;
      try {
        branchNo = Integer.parseInt(branchNoField.getText());
        DepartmentModel.updateDepartment(departmentNo, departmentName, branchNo);
        loadDepartments();  // Refresh the list after updating
      } catch (NumberFormatException e) {
        JOptionPane.showMessageDialog(this, "Invalid input for branch number", "Input Error", JOptionPane.ERROR_MESSAGE);
      }
    }
  }

  /**
   * Deletes an existing department.
   */
  private void deleteDepartment() {
    int row = tableDepartments.getSelectedRow();
    if (row < 0) {
      JOptionPane.showMessageDialog(this, "Please select a department to delete", "Selection Error", JOptionPane.ERROR_MESSAGE);
      return;
    }

    int departmentNo = Integer.parseInt(tableDepartments.getValueAt(row, 0).toString());
    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this department?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      DepartmentModel.deleteDepartment(departmentNo);
      loadDepartments();  // Refresh the list after deletion
    }
  }

  /**
   * Deletes the selected user.
   */
  private void deleteUser() {
    int selectedRow = tableUsers.getSelectedRow();
    if (selectedRow >= 0) {
      int userId = (int) tableUsers.getValueAt(selectedRow, 0);
      if (UserModel.deleteUser(userId)) {
        JOptionPane.showMessageDialog(this, "User deleted successfully.");
        loadUsers();
      } else {
        JOptionPane.showMessageDialog(this, "Failed to delete user.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(this, "Select a user to delete", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Loads patient data into the table.
   */
  private void loadPatients() {
    tablePatients.setModel(PatientModel.getPatientsTableModel());
  }

  /**
   * Opens the dialog to update an existing patient.
   */
  private void updatePatient() {
    int selectedRow = tablePatients.getSelectedRow();
    if (selectedRow >= 0) {
      int userId = (int) tablePatients.getValueAt(selectedRow, 0);
      // Assuming that a method to fetch patient by userId exists
      Patient patient = PatientModel.getPatientById(userId);
      if (patient != null) {
        PatientFormDialog updateDialog = new PatientFormDialog(this, patient);
        updateDialog.setVisible(true);
        loadPatients();
      } else {
        JOptionPane.showMessageDialog(this, "Failed to fetch patient details.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(this, "Select a patient to update", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Deletes the selected patient.
   */
  private void deletePatient() {
    int selectedRow = tablePatients.getSelectedRow();
    if (selectedRow >= 0) {
      int userId = (int) tablePatients.getValueAt(selectedRow, 0);
      if (PatientModel.deletePatient(userId)) {
        JOptionPane.showMessageDialog(this, "Patient deleted successfully.");
        loadPatients();
      } else {
        JOptionPane.showMessageDialog(this, "Failed to delete patient.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(this, "Select a patient to delete", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Loads patient profiles into the table.
   */
  private void loadPatientProfiles() {
    tablePatients.setModel(PatientModel.getPatientProfiles());
  }

  /**
   * Main method to launch the application.
   */
  public static void main(String[] args) {
    SwingUtilities.invokeLater(() -> {
      LoginDialog loginDlg = new LoginDialog(null);
      loginDlg.setVisible(true);
      if (loginDlg.isSucceeded()) {
        // Pass both userTypeID and userID from the login dialog
        MainFrame frame = new MainFrame(loginDlg.getUserTypeID(), loginDlg.getUserID());
        frame.setVisible(true);
      } else {
        System.exit(0);
      }
    });
  }

  /**
   * Loads doctor data into the table.
   */
  private void loadDoctors() {
    tableDoctors.setModel(DoctorModel.getDoctorTableModel());
  }

  /**
   * Opens the dialog to update an existing doctor.
   */
  private void updateDoctor() {
    int selectedRow = tableDoctors.getSelectedRow();
    if (selectedRow >= 0) {
      int userId = (int) tableDoctors.getValueAt(selectedRow, 0);
      Doctor doctor = DoctorModel.getDoctorByID(userId);
      if (doctor != null) {
        DoctorFormDialog updateDialog = new DoctorFormDialog(this, doctor);
        updateDialog.setVisible(true);
        loadDoctors();
      } else {
        JOptionPane.showMessageDialog(this, "Failed to fetch doctor details.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(this, "Select a doctor to update", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Deletes the selected doctor.
   */
  private void deleteDoctor() {
    int selectedRow = tableDoctors.getSelectedRow();
    if (selectedRow >= 0) {
      int userId = (int) tableDoctors.getValueAt(selectedRow, 0);
      int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this doctor?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
      if (confirm == JOptionPane.YES_OPTION) {
        if (DoctorModel.deleteDoctor(userId)) {
          JOptionPane.showMessageDialog(this, "Doctor deleted successfully.");
          loadDoctors(); // Refresh the doctors list
        } else {
          JOptionPane.showMessageDialog(this, "Failed to delete doctor.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    } else {
      JOptionPane.showMessageDialog(this, "Select a doctor to delete", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Loads doctor information into the table.
   */
  private void loadDoctorInfo() {
    tableDoctors.setModel(DoctorModel.getDoctorInformation());
  }

  /**
   * Loads manager data into the table.
   */
  private void loadManagers() {
    tableManagers.setModel(ManagerModel.getManagerTableModel());
  }

  /**
   * Opens the dialog to update an existing manager.
   */
  private void updateManager() {
    int selectedRow = tableManagers.getSelectedRow();
    if (selectedRow >= 0) {
      int userId = (int) tableManagers.getValueAt(selectedRow, 0);
      Manager manager = ManagerModel.getManagerByID(userId);
      if (manager != null) {
        ManagerFormDialog updateDialog = new ManagerFormDialog(this, manager);
        updateDialog.setVisible(true);
        loadManagers();
      } else {
        JOptionPane.showMessageDialog(this, "Failed to fetch manager details.", "Error", JOptionPane.ERROR_MESSAGE);
      }
    } else {
      JOptionPane.showMessageDialog(this, "Select a manager to update", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Deletes the selected manager.
   */
  private void deleteManager() {
    int selectedRow = tableManagers.getSelectedRow();
    if (selectedRow >= 0) {
      int userId = (int) tableManagers.getValueAt(selectedRow, 0);
      int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this manager?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
      if (confirm == JOptionPane.YES_OPTION) {
        if (ManagerModel.deleteManager(userId)) {
          JOptionPane.showMessageDialog(this, "Manager deleted successfully.");
          loadManagers(); // Refresh the doctors list
        } else {
          JOptionPane.showMessageDialog(this, "Failed to delete manager.", "Error", JOptionPane.ERROR_MESSAGE);
        }
      }
    } else {
      JOptionPane.showMessageDialog(this, "Select a manager to delete", "Error", JOptionPane.ERROR_MESSAGE);
    }
  }

  /**
   * Loads manager oversight data into the table.
   */
  private void managerOversight() {
    tableManagers.setModel(ManagerModel.getManagerOversight());
  }

  /**
   * Sets up the medication panel.
   */
  private void setupMedicationPanel() {
    JPanel medicationPanel = new JPanel(new BorderLayout());
    tableMedications = new JTable();
    loadMedications(); // Load medications into the table
    JScrollPane scrollPane = new JScrollPane(tableMedications);
    medicationPanel.add(scrollPane, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    btnLoadMedications = new JButton("Load Medications");
    btnAddMedication = new JButton("Add Medication");
    btnUpdateMedication = new JButton("Update Medication");
    btnDeleteMedication = new JButton("Delete Medication");

    buttonPanel.add(btnLoadMedications);
    buttonPanel.add(btnAddMedication);
    buttonPanel.add(btnUpdateMedication);
    buttonPanel.add(btnDeleteMedication);

    btnLoadMedications.addActionListener(e -> loadMedications());
    btnAddMedication.addActionListener(e -> addMedication());
    btnUpdateMedication.addActionListener(e -> updateMedication());
    btnDeleteMedication.addActionListener(e -> deleteMedication());

    medicationPanel.add(buttonPanel, BorderLayout.SOUTH);
    tabbedPane.addTab("Medications", medicationPanel);
  }

  /**
   * Loads medication data into the table.
   */
  private void loadMedications() {
    ArrayList<Medication> medications = medicationModel.getAllMedications();
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("Medication ID");
    model.addColumn("User ID");
    model.addColumn("Description");

    for (Medication medication : medications) {
      model.addRow(new Object[] { medication.getMedicationId(), medication.getUserId(), medication.getDescription() });
    }
    tableMedications.setModel(model);
  }

  /**
   * Opens the dialog to add a new medication.
   */
  private void addMedication() {
    // This could open a dialog to input new medication details or use placeholders for simplicity
    String description = JOptionPane.showInputDialog(this, "Enter medication description:");
    if (description != null && !description.trim().isEmpty()) {
      Medication newMedication = new Medication(0, userID, description);
      medicationModel.addMedication(newMedication);
      loadMedications(); // Refresh the table
    }
  }

  /**
   * Opens the dialog to update an existing medication.
   */
  private void updateMedication() {
    int row = tableMedications.getSelectedRow();
    if (row == -1) {
      JOptionPane.showMessageDialog(this, "Please select a medication to update.");
      return;
    }

    int medicationId = Integer.parseInt(tableMedications.getValueAt(row, 0).toString());
    String description = JOptionPane.showInputDialog(this, "Enter new medication description:");
    if (description != null && !description.trim().isEmpty()) {
      Medication updatedMedication = new Medication(medicationId, userID, description);
      medicationModel.updateMedication(updatedMedication);
      loadMedications(); // Refresh the table
    }
  }

  /**
   * Deletes the selected medication.
   */
  private void deleteMedication() {
    int row = tableMedications.getSelectedRow();
    if (row == -1) {
      JOptionPane.showMessageDialog(this, "Please select a medication to delete.");
      return;
    }

    int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this medication?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
    if (confirm == JOptionPane.YES_OPTION) {
      int medicationId = Integer.parseInt(tableMedications.getValueAt(row, 0).toString());
      medicationModel.deleteMedication(medicationId);
      loadMedications(); // Refresh the table
    }
  }

  /**
   * Sets up the allergy panel.
   */
  private void setupAllergyPanel() {
    JPanel allergyPanel = new JPanel(new BorderLayout());
    tableAllergies = new JTable();
    loadAllergies(); // Load allergies into the table
    JScrollPane scrollPane = new JScrollPane(tableAllergies);
    allergyPanel.add(scrollPane, BorderLayout.CENTER);

    JPanel buttonPanel = new JPanel();
    btnLoadAllergies = new JButton("Load Allergies");
    btnAddAllergy = new JButton("Add Allergy");
    btnUpdateAllergy = new JButton("Update Allergy");
    btnDeleteAllergy = new JButton("Delete Allergy");

    buttonPanel.add(btnLoadAllergies);
    buttonPanel.add(btnAddAllergy);
    buttonPanel.add(btnUpdateAllergy);
    buttonPanel.add(btnDeleteAllergy);

    btnLoadAllergies.addActionListener(e -> loadAllergies());
    btnAddAllergy.addActionListener(e -> addAllergy());
    btnUpdateAllergy.addActionListener(e -> updateAllergy());
    btnDeleteAllergy.addActionListener(e -> deleteAllergy());

    allergyPanel.add(buttonPanel, BorderLayout.SOUTH);
    tabbedPane.addTab("Allergies", allergyPanel);
  }

  /**
   * Loads allergy data into the table.
   */
  private void loadAllergies() {
    ArrayList<Allergy> allergies = allergyModel.getAllergies();
    DefaultTableModel model = new DefaultTableModel();
    model.addColumn("Allergy ID");
    model.addColumn("User ID");
    model.addColumn("Description");

    for (Allergy allergy : allergies) {
      model.addRow(new Object[] { allergy.getAllergyId(), allergy.getUserId(), allergy.getDescription() });
    }
    tableAllergies.setModel(model);
  }

  /**
   * Opens the dialog to add a new allergy.
   */
  private void addAllergy() {
    String description = JOptionPane.showInputDialog(this, "Enter allergy description:");
    if (description != null && !description.trim().isEmpty()) {
      Allergy newAllergy = new Allergy(0, userID, description); // Assuming userID is the ID of the patient
      allergyModel.addAllergy(newAllergy);
      loadAllergies(); // Refresh the table
    }
  }

  /**
   * Opens the dialog to update an existing allergy.
   */
  private void updateAllergy() {
    int row = tableAllergies.getSelectedRow();
    if (row != -1) {
      int allergyId = (int) tableAllergies.getValueAt(row, 0);
      String description = JOptionPane.showInputDialog(this, "Enter new allergy description:", tableAllergies.getValueAt(row, 2).toString());
      if (description != null && !description.trim().isEmpty()) {
        Allergy updatedAllergy = new Allergy(allergyId, userID, description);
        allergyModel.updateAllergy(updatedAllergy);
        loadAllergies(); // Refresh the table
      }
    } else {
      JOptionPane.showMessageDialog(this, "Please select an allergy to update.");
    }
  }

  /**
   * Deletes the selected allergy.
   */
  private void deleteAllergy() {
    int row = tableAllergies.getSelectedRow();
    if (row != -1) {
      int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this allergy?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
      if (confirm == JOptionPane.YES_OPTION) {
        int allergyId = (int) tableAllergies.getValueAt(row, 0);
        allergyModel.deleteAllergy(allergyId);
        loadAllergies(); // Refresh the table
      }
    } else {
      JOptionPane.showMessageDialog(this, "Please select an allergy to delete.");
    }
  }

}
