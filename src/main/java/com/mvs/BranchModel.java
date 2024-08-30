package com.mvs;

import javax.swing.table.DefaultTableModel;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class BranchModel {

    public static DefaultTableModel getBranchesTableModel() {
        DefaultTableModel model = new DefaultTableModel(new Object[]{"Branch No", "Location", "Branch Name"}, 0);
        String query = "SELECT Branch_No, Location, Branch_Name FROM mvs_branch";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                model.addRow(new Object[]{rs.getInt("Branch_No"), rs.getString("Location"), rs.getString("Branch_Name")});
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return model;
    }

    public static void addBranch(String location, String branchName) {
        String query = "INSERT INTO mvs_branch (Location, Branch_Name) VALUES (?, ?)";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, location);
            stmt.setString(2, branchName);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void updateBranch(int branchNo, String location, String branchName) {
        String query = "UPDATE mvs_branch SET Location = ?, Branch_Name = ? WHERE Branch_No = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setString(1, location);
            stmt.setString(2, branchName);
            stmt.setInt(3, branchNo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void deleteBranch(int branchNo) {
        String query = "DELETE FROM mvs_branch WHERE Branch_No = ?";
        try (Connection conn = DatabaseManager.getConnection();
             PreparedStatement stmt = conn.prepareStatement(query)) {
            stmt.setInt(1, branchNo);
            stmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
