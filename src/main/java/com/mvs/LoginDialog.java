package com.mvs;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * LoginDialog is a JDialog that provides a simple login interface.
 * It allows users to enter their username and password, and attempts to authenticate them against the database.
 */
public class LoginDialog extends JDialog {
  private final JTextField txtUsername = new JTextField(15);
  private final JPasswordField txtPassword = new JPasswordField(15);
  private final JButton btnLogin = new JButton("Login");
  private boolean succeeded;
  private int userID = -1; // Store the user ID
  private int userTypeID = -1; // Default to -1, meaning no user type.

  /**
   * Returns the user ID of the authenticated user.
   * 
   * @return the user ID
   */
  public int getUserID() {
    return userID;
  }

  /**
   * Returns the user type ID of the authenticated user.
   * 
   * @return the user type ID
   */
  public int getUserTypeID() {
    return userTypeID;
  }

  /**
   * Constructs a LoginDialog.
   * 
   * @param parent the parent frame
   */
  public LoginDialog(JFrame parent) {
    super(parent, "Login", true);
    JPanel panel = new JPanel(new GridBagLayout());
    GridBagConstraints cs = new GridBagConstraints();

    cs.fill = GridBagConstraints.HORIZONTAL;

    // Add Username label and text field
    cs.gridx = 0;
    cs.gridy = 0;
    cs.gridwidth = 1;
    panel.add(new JLabel("Username:"), cs);

    cs.gridx = 1;
    cs.gridy = 0;
    cs.gridwidth = 2;
    panel.add(txtUsername, cs);

    // Add Password label and text field
    cs.gridx = 0;
    cs.gridy = 1;
    cs.gridwidth = 1;
    panel.add(new JLabel("Password:"), cs);

    cs.gridx = 1;
    cs.gridy = 1;
    cs.gridwidth = 2;
    panel.add(txtPassword, cs);

    // Add Login button
    panel.add(btnLogin, new GridBagConstraints(1, 2, 1, 1, 0, 0, GridBagConstraints.CENTER, GridBagConstraints.NONE,
        new Insets(10, 0, 10, 0), 0, 0));

    // Add action listener for login button
    btnLogin.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent e) {
        if (authenticate(getUsername(), getPassword())) {
          System.out.println("Authentication successful");
          JOptionPane.showMessageDialog(LoginDialog.this, "Hi " + getUsername() + "! You have successfully logged in.",
              "Login", JOptionPane.INFORMATION_MESSAGE);
          succeeded = true;
          dispose();
        } else {
          System.out.println("Authentication failed");
          JOptionPane.showMessageDialog(LoginDialog.this, "Invalid username or password", "Login",
              JOptionPane.ERROR_MESSAGE);
          txtPassword.setText("");
          succeeded = false;
        }
      }
    });

    getContentPane().add(panel, BorderLayout.CENTER);
    pack();
    setResizable(false);
    setLocationRelativeTo(parent);
  }

  /**
   * Returns the entered username.
   * 
   * @return the entered username
   */
  public String getUsername() {
    return txtUsername.getText().trim();
  }

  /**
   * Returns the entered password.
   * 
   * @return the entered password
   */
  public String getPassword() {
    return new String(txtPassword.getPassword());
  }

  /**
   * Returns whether the login was successful.
   * 
   * @return true if the login was successful, false otherwise
   */
  public boolean isSucceeded() {
    return succeeded;
  }

  /**
   * Authenticates the user by checking the username and password against the database.
   * 
   * @param username the entered username
   * @param password the entered password
   * @return true if authentication was successful, false otherwise
   */
  private boolean authenticate(String username, String password) {
    System.out.println("Authenticating user: " + username);
    try (Connection connection = DatabaseManager.getConnection();
         PreparedStatement ps = connection
            .prepareStatement("SELECT User_ID, User_Type_ID FROM USER WHERE Username = ? AND Password = ?")) {
      ps.setString(1, username);
      ps.setString(2, password);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        userID = rs.getInt("User_ID");
        userTypeID = rs.getInt("User_Type_ID");
        return true;
      }
    } catch (SQLException ex) {
      ex.printStackTrace();
    }
    return false;
  }
}
