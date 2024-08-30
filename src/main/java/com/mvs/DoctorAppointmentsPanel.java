package com.mvs;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.BorderLayout;

/**
 * DoctorAppointmentsPanel is a JPanel that displays the appointments for a specific doctor.
 * It loads the appointments from the database and displays them in a JTable.
 */
public class DoctorAppointmentsPanel extends JPanel {
  private JTable appointmentsTable;

  /**
   * Constructs a DoctorAppointmentsPanel for the specified doctor user ID.
   * 
   * @param doctorUserId the ID of the doctor whose appointments will be displayed
   */
  public DoctorAppointmentsPanel(int doctorUserId) {
    setLayout(new BorderLayout());
    appointmentsTable = new JTable();
    add(new JScrollPane(appointmentsTable), BorderLayout.CENTER);
    loadAppointments(doctorUserId);
  }

  /**
   * Loads the appointments for the specified doctor and sets the table model.
   * 
   * @param doctorUserId the ID of the doctor whose appointments will be loaded
   */
  private void loadAppointments(int doctorUserId) {
    DefaultTableModel model = DoctorModel.getDoctorAppointments(doctorUserId);
    appointmentsTable.setModel(model);
  }
}
