package com.smartcity.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

import com.smartcity.model.Complaint;

public class ComplaintDAO {
    public boolean submitComplaint(Complaint complaint) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO complaints (user_id, category, title, description, location, photo_url, status, assigned_to, created_at, updated_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, complaint.getUserId());
            ps.setString(2, complaint.getCategory());
            ps.setString(3, complaint.getTitle());
            ps.setString(4, complaint.getDescription());
            ps.setString(5, complaint.getLocation());
            ps.setString(6, complaint.getPhotoUrl());
            ps.setString(7, complaint.getStatus());
            ps.setString(8, complaint.getAssignedTo());
            ps.setTimestamp(9, complaint.getCreatedAt());
            ps.setTimestamp(10, complaint.getUpdatedAt());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Database error while submitting complaint: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Unexpected error while submitting complaint: " + e.getMessage());
        }
        return false;
    }

    public List<Complaint> getComplaintsByUser(int userId) {
        List<Complaint> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM complaints WHERE user_id = ? ORDER BY created_at DESC";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, userId);
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                list.add(extractComplaint(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving complaints by user: " + e.getMessage());
        }
        return list;
    }

    public List<Complaint> getAllComplaints() {
        List<Complaint> list = new ArrayList<>();
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM complaints ORDER BY created_at DESC";
            Statement st = conn.createStatement();
            ResultSet rs = st.executeQuery(sql);
            while (rs.next()) {
                list.add(extractComplaint(rs));
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving all complaints: " + e.getMessage());
        }
        return list;
    }

    public boolean updateComplaintStatus(int id, String status, String assignedTo) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "UPDATE complaints SET status = ?, assigned_to = ?, updated_at = NOW() WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, assignedTo);
            ps.setInt(3, id);
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error updating complaint status: " + e.getMessage());
        }
        return false;
    }

    private Complaint extractComplaint(ResultSet rs) throws SQLException {
        return new Complaint(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getString("category"),
            rs.getString("title"),
            rs.getString("description"),
            rs.getString("location"),
            rs.getString("photo_url"),
            rs.getString("status"),
            rs.getString("assigned_to"),
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}
