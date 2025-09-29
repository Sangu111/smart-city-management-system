package com.smartcity.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.smartcity.dao.ComplaintDAO;
import com.smartcity.dao.DBUtil;
import com.smartcity.model.Complaint;
import com.smartcity.model.User;

@WebServlet("/test-complaint")
public class TestComplaintServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html><html><head><title>Test Complaint System</title></head><body>");
        out.println("<h2>🧪 Complaint System Test & Debug</h2>");
        
        // Check login status
        HttpSession session = request.getSession(false);
        User currentUser = null;
        if (session != null) {
            currentUser = (User) session.getAttribute("user");
        }
        
        if (currentUser == null) {
            out.println("<div style='background: #fff3cd; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
            out.println("<h3>⚠️ Not Logged In</h3>");
            out.println("<p>You need to be logged in to test complaint submission.</p>");
            out.println("<p><a href='test-login'>Quick Login</a> | <a href='jsp/login.jsp'>Login Page</a></p>");
            out.println("</div>");
        } else {
            out.println("<div style='background: #d4edda; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
            out.println("<h3>✅ Logged in as: " + currentUser.getUsername() + "</h3>");
            out.println("<p>User ID: " + currentUser.getId() + ", Role: " + currentUser.getRole() + "</p>");
            out.println("</div>");
            
            // Show test complaint form
            showTestForm(out, currentUser);
        }
        
        // Show current complaints
        showCurrentComplaints(out);
        
        out.println("<p><a href='complaint'>Go to Dashboard</a> | ");
        out.println("<a href='database-manager'>Database Manager</a></p>");
        
        out.println("</body></html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        HttpSession session = request.getSession(false);
        User currentUser = (User) session.getAttribute("user");
        
        if (currentUser == null) {
            out.println("Error: Not logged in");
            return;
        }
        
        String category = request.getParameter("category");
        String title = request.getParameter("title");
        String description = request.getParameter("description");
        String location = request.getParameter("location");
        
        out.println("<!DOCTYPE html><html><head><title>Test Complaint Result</title></head><body>");
        out.println("<h2>🧪 Complaint Submission Test Result</h2>");
        
        out.println("<div style='background: #e7f3ff; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
        out.println("<h3>📋 Submission Data:</h3>");
        out.println("<p><strong>User ID:</strong> " + currentUser.getId() + "</p>");
        out.println("<p><strong>Category:</strong> " + category + "</p>");
        out.println("<p><strong>Title:</strong> " + title + "</p>");
        out.println("<p><strong>Description:</strong> " + description + "</p>");
        out.println("<p><strong>Location:</strong> " + location + "</p>");
        out.println("</div>");
        
        try {
            // Test direct database insertion
            out.println("<div style='background: #f8f9fa; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
            out.println("<h3>🔍 Database Test:</h3>");
            
            try (Connection conn = DBUtil.getConnection()) {
                // Test the exact SQL that should work
                String sql = "INSERT INTO complaints (user_id, category, subject, description, location, status) VALUES (?, ?, ?, ?, ?, ?) RETURNING id";
                PreparedStatement ps = conn.prepareStatement(sql);
                ps.setInt(1, currentUser.getId());
                ps.setString(2, category);
                ps.setString(3, title);
                ps.setString(4, description);
                ps.setString(5, location);
                ps.setString(6, "pending");
                
                ResultSet rs = ps.executeQuery();
                if (rs.next()) {
                    int newId = rs.getInt("id");
                    out.println("<p>✅ <strong>Direct DB Insert SUCCESS!</strong> New complaint ID: " + newId + "</p>");
                } else {
                    out.println("<p>❌ <strong>Direct DB Insert FAILED!</strong></p>");
                }
            }
            out.println("</div>");
            
            // Test using ComplaintDAO
            out.println("<div style='background: #f8f9fa; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
            out.println("<h3>🔧 DAO Test:</h3>");
            
            Complaint complaint = new Complaint(0, currentUser.getId(), category, title, description, location, null, "pending", "", new Timestamp(System.currentTimeMillis()), new Timestamp(System.currentTimeMillis()));
            ComplaintDAO dao = new ComplaintDAO();
            boolean success = dao.submitComplaint(complaint);
            
            if (success) {
                out.println("<p>✅ <strong>DAO Submission SUCCESS!</strong></p>");
            } else {
                out.println("<p>❌ <strong>DAO Submission FAILED!</strong></p>");
            }
            out.println("</div>");
            
        } catch (Exception e) {
            out.println("<div style='background: #f8d7da; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
            out.println("<h3>❌ Error During Test</h3>");
            out.println("<p><strong>Error:</strong> " + e.getMessage() + "</p>");
            out.println("</div>");
        }
        
        out.println("<p><a href='test-complaint'>Test Again</a> | ");
        out.println("<a href='complaint'>Go to Dashboard</a></p>");
        
        out.println("</body></html>");
    }
    
    private void showTestForm(PrintWriter out, User user) {
        out.println("<div style='background: #f8f9fa; padding: 15px; border-radius: 5px; margin: 20px 0;'>");
        out.println("<h3>🆕 Test Complaint Submission</h3>");
        out.println("<form method='post'>");
        out.println("<div style='margin: 10px 0;'>");
        out.println("<label>Category:</label><br>");
        out.println("<select name='category' style='padding: 8px; width: 200px;'>");
        out.println("<option value='Roads'>Roads</option>");
        out.println("<option value='Water Supply'>Water Supply</option>");
        out.println("<option value='Electricity'>Electricity</option>");
        out.println("<option value='Sanitation'>Sanitation</option>");
        out.println("</select>");
        out.println("</div>");
        out.println("<div style='margin: 10px 0;'>");
        out.println("<label>Title/Subject:</label><br>");
        out.println("<input type='text' name='title' value='Test complaint " + System.currentTimeMillis() % 1000 + "' style='padding: 8px; width: 300px;'>");
        out.println("</div>");
        out.println("<div style='margin: 10px 0;'>");
        out.println("<label>Description:</label><br>");
        out.println("<textarea name='description' style='padding: 8px; width: 300px; height: 80px;'>This is a test complaint to verify the submission system is working correctly.</textarea>");
        out.println("</div>");
        out.println("<div style='margin: 10px 0;'>");
        out.println("<label>Location:</label><br>");
        out.println("<input type='text' name='location' value='Test Location - Main Street' style='padding: 8px; width: 300px;'>");
        out.println("</div>");
        out.println("<button type='submit' style='background: #007bff; color: white; padding: 10px 20px; border: none; border-radius: 5px;'>Submit Test Complaint</button>");
        out.println("</form>");
        out.println("</div>");
    }
    
    private void showCurrentComplaints(PrintWriter out) {
        out.println("<div style='background: #f8f9fa; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
        out.println("<h3>📋 Current Complaints in Database:</h3>");
        
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT c.id, u.username, c.category, c.subject, c.status, c.created_at FROM complaints c LEFT JOIN users u ON c.user_id = u.id ORDER BY c.created_at DESC LIMIT 10");
            ResultSet rs = ps.executeQuery();
            
            out.println("<table border='1' style='border-collapse: collapse; width: 100%;'>");
            out.println("<tr style='background: #e9ecef;'>");
            out.println("<th style='padding: 8px;'>ID</th>");
            out.println("<th style='padding: 8px;'>User</th>");
            out.println("<th style='padding: 8px;'>Category</th>");
            out.println("<th style='padding: 8px;'>Subject</th>");
            out.println("<th style='padding: 8px;'>Status</th>");
            out.println("<th style='padding: 8px;'>Created</th>");
            out.println("</tr>");
            
            boolean hasComplaints = false;
            while (rs.next()) {
                hasComplaints = true;
                out.println("<tr>");
                out.println("<td style='padding: 8px;'>" + rs.getInt("id") + "</td>");
                out.println("<td style='padding: 8px;'>" + rs.getString("username") + "</td>");
                out.println("<td style='padding: 8px;'>" + rs.getString("category") + "</td>");
                out.println("<td style='padding: 8px;'>" + rs.getString("subject") + "</td>");
                out.println("<td style='padding: 8px;'>" + rs.getString("status") + "</td>");
                out.println("<td style='padding: 8px;'>" + rs.getTimestamp("created_at") + "</td>");
                out.println("</tr>");
            }
            
            if (!hasComplaints) {
                out.println("<tr><td colspan='6' style='padding: 8px; text-align: center;'>No complaints found</td></tr>");
            }
            
            out.println("</table>");
            
        } catch (Exception e) {
            out.println("<p style='color: red;'>Error loading complaints: " + e.getMessage() + "</p>");
        }
        
        out.println("</div>");
    }
}