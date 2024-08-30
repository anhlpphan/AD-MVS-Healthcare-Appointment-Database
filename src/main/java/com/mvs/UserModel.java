package com.mvs;

import javax.swing.table.DefaultTableModel;
import java.sql.*;

public class UserModel {

  // Method to get a table model with user data
  public static DefaultTableModel getUsersTableModel() {
    try (Connection conn = DatabaseManager.getConnection();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery("SELECT User_ID, Username, First_Name, Last_Name, Email, User_Type_ID FROM USER")) {

      // Create a table model with the specified column names
      DefaultTableModel model = new DefaultTableModel(
          new Object[] { "User ID", "Username", "First Name", "Last Name", "Email", "User Type ID" }, 0);

      // Populate the table model with data from the ResultSet
      while (rs.next()) {
        model.addRow(new Object[] {
            rs.getInt("User_ID"),
            rs.getString("Username"),
            rs.getString("First_Name"),
            rs.getString("Last_Name"),
            rs.getString("Email"),
            rs.getInt("User_Type_ID")
        });
      }
      return model; // Return the populated table model
    } catch (SQLException e) {
      e.printStackTrace();
      return null; // Return null if an exception occurs
    }
  }

  // Method to get a user by ID
  public static User getUserById(int userId) {
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("SELECT * FROM USER WHERE User_ID = ?")) {
      ps.setInt(1, userId);
      ResultSet rs = ps.executeQuery();
      if (rs.next()) {
        // Create and return a User object with data from the ResultSet
        return new User(
            rs.getInt("User_ID"),
            rs.getString("Username"),
            rs.getString("Password"),
            rs.getString("First_Name"),
            rs.getString("Middle_Name"),
            rs.getString("Last_Name"),
            rs.getDate("Date_of_Birth"),
            rs.getString("Sex"),
            rs.getString("Phone_No"),
            rs.getString("Street"),
            rs.getString("City"),
            rs.getString("State"),
            rs.getString("Zip_Code"),
            rs.getTimestamp("Date_Registered"),
            rs.getInt("User_Type_ID"),
            rs.getString("Email"),
            rs.getString("SSN"));
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return null; // Return null if no user is found or an exception occurs
  }

  // Method to add a new user
  public static boolean addUser(String username, String password, String firstName, String middleName, String lastName,
      java.sql.Date dateOfBirth, String sex, String phoneNo, String street, String city, String state,
      String zipCode, java.sql.Timestamp dateRegistered, int userTypeID, String email, String ssn) {
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "INSERT INTO USER (Username, Password, First_Name, Middle_Name, Last_Name, Date_of_Birth, Sex, Phone_No, Street, City, State, Zip_Code, Date_Registered, User_Type_ID, Email, SSN) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)")) {
      // Set the parameters for the PreparedStatement
      ps.setString(1, username);
      ps.setString(2, password);
      ps.setString(3, firstName);
      ps.setString(4, middleName);
      ps.setString(5, lastName);
      ps.setDate(6, dateOfBirth);
      ps.setString(7, sex);
      ps.setString(8, phoneNo);
      ps.setString(9, street);
      ps.setString(10, city);
      ps.setString(11, state);
      ps.setString(12, zipCode);
      ps.setTimestamp(13, dateRegistered);
      ps.setInt(14, userTypeID);
      ps.setString(15, email);
      ps.setString(16, ssn);
      ps.executeUpdate(); // Execute the update
      return true; // Return true if the update is successful
    } catch (SQLException e) {
      e.printStackTrace();
      return false; // Return false if an exception occurs
    }
  }

  // Method to update an existing user
  public static boolean updateUser(int userId, String username, String password, String firstName, String middleName,
      String lastName, Date dateOfBirth, String sex, String phoneNo, String street,
      String city, String state, String zipCode, Timestamp dateRegistered,
      int userTypeID, String email, String ssn) {
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement(
            "UPDATE USER SET Username = ?, Password = ?, First_Name = ?, Middle_Name = ?, Last_Name = ?, Date_of_Birth = ?, Sex = ?, Phone_No = ?, Street = ?, City = ?, State = ?, Zip_Code = ?, Date_Registered = ?, User_Type_ID = ?, Email = ?, SSN = ? WHERE User_ID = ?")) {
      // Set the parameters for the PreparedStatement
      ps.setString(1, username);
      ps.setString(2, password);
      ps.setString(3, firstName);
      ps.setString(4, middleName);
      ps.setString(5, lastName);
      ps.setDate(6, dateOfBirth);
      ps.setString(7, sex);
      ps.setString(8, phoneNo);
      ps.setString(9, street);
      ps.setString(10, city);
      ps.setString(11, state);
      ps.setString(12, zipCode);
      ps.setTimestamp(13, dateRegistered);
      ps.setInt(14, userTypeID);
      ps.setString(15, email);
      ps.setString(16, ssn);
      ps.setInt(17, userId);
      int affectedRows = ps.executeUpdate(); // Execute the update
      return affectedRows > 0; // Return true if rows are affected
    } catch (SQLException e) {
      e.printStackTrace();
      return false; // Return false if an exception occurs
    }
  }

  // Method to delete a user by ID
  public static boolean deleteUser(int userId) {
    try (Connection conn = DatabaseManager.getConnection();
        PreparedStatement ps = conn.prepareStatement("DELETE FROM USER WHERE User_ID = ?")) {
      ps.setInt(1, userId);
      int affectedRows = ps.executeUpdate(); // Execute the update
      return affectedRows > 0; // Return true if rows are affected
    } catch (SQLException e) {
      e.printStackTrace();
      return false; // Return false if an exception occurs
    }
  }
}
