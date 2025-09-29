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
            // Fixed: Use correct column names - subject, photo_path, no assigned_to
            String sql = "INSERT INTO complaints (user_id, category, subject, description, location, photo_path, status) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, complaint.getUserId());
            ps.setString(2, complaint.getCategory());
            ps.setString(3, complaint.getTitle()); // Maps to subject column
            ps.setString(4, complaint.getDescription());
            ps.setString(5, complaint.getLocation());
            ps.setString(6, complaint.getPhotoUrl()); // Maps to photo_path column
            ps.setString(7, "pending"); // Default status
            
            int result = ps.executeUpdate();
            System.out.println("🎯 Complaint submission result: " + (result > 0 ? "SUCCESS" : "FAILED"));
            return result > 0;
        } catch (SQLException e) {
            System.err.println("❌ Database error while submitting complaint: " + e.getMessage());
            e.printStackTrace();
        } catch (Exception e) {
            System.err.println("❌ Unexpected error while submitting complaint: " + e.getMessage());
            e.printStackTrace();
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

    public boolean updateComplaintStatus(int id, String status, String priority) {
        try (Connection conn = DBUtil.getConnection()) {
            // Fixed: Use priority instead of assigned_to, and CURRENT_TIMESTAMP for PostgreSQL
            String sql = "UPDATE complaints SET status = ?, priority = ?, updated_at = CURRENT_TIMESTAMP WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, status);
            ps.setString(2, priority);
            ps.setInt(3, id);
            int result = ps.executeUpdate();
            System.out.println("📝 Complaint status update result: " + (result > 0 ? "SUCCESS" : "FAILED"));
            return result > 0;
        } catch (SQLException e) {
            System.err.println("❌ Error updating complaint status: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    private Complaint extractComplaint(ResultSet rs) throws SQLException {
        return new Complaint(
            rs.getInt("id"),
            rs.getInt("user_id"),
            rs.getString("category"),
            rs.getString("subject"), // Fixed: use subject not title
            rs.getString("description"),
            rs.getString("location"),
            rs.getString("photo_path"), // Fixed: use photo_path not photo_url
            rs.getString("status"),
            rs.getString("priority"), // Use priority instead of assigned_to
            rs.getTimestamp("created_at"),
            rs.getTimestamp("updated_at")
        );
    }
}
