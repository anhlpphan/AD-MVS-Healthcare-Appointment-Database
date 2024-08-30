package com.mvs;

import java.sql.*;
import java.util.ArrayList;

public class AllergyModel {
  private Connection connect() {
    // Replace these with your database credentials
    String url = "jdbc:mysql://localhost:3306/mvs_final_project";
    String user = "default";
    String password = "Password123!";
    try {
      return DriverManager.getConnection(url, user, password);
    } catch (SQLException e) {
      System.out.println(e.getMessage());
      return null;
    }
  }

  public ArrayList<Allergy> getAllergies() {
    ArrayList<Allergy> allergies = new ArrayList<>();
    String sql = "SELECT * FROM ALLERGY";
    try (Connection conn = this.connect();
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(sql)) {
      while (rs.next()) {
        allergies.add(new Allergy(rs.getInt("Allergy_ID"), rs.getInt("User_ID"), rs.getString("Allergy_Description")));
      }
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
    return allergies;
  }

  public void addAllergy(Allergy allergy) {
    String sql = "INSERT INTO ALLERGY (User_ID, Allergy_Description) VALUES (?, ?)";
    try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, allergy.getUserId());
      pstmt.setString(2, allergy.getDescription());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void updateAllergy(Allergy allergy) {
    String sql = "UPDATE ALLERGY SET Allergy_Description = ? WHERE Allergy_ID = ?";
    try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setString(1, allergy.getDescription());
      pstmt.setInt(2, allergy.getAllergyId());
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }

  public void deleteAllergy(int allergyId) {
    String sql = "DELETE FROM ALLERGY WHERE Allergy_ID = ?";
    try (Connection conn = this.connect();
        PreparedStatement pstmt = conn.prepareStatement(sql)) {
      pstmt.setInt(1, allergyId);
      pstmt.executeUpdate();
    } catch (SQLException e) {
      System.out.println(e.getMessage());
    }
  }
}
