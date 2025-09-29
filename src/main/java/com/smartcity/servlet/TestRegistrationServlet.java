package com.smartcity.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smartcity.dao.DBUtil;
import com.smartcity.dao.PasswordUtil;
import com.smartcity.dao.UserDAO;
import com.smartcity.model.User;

@WebServlet("/test-registration")
public class TestRegistrationServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html><html><head><title>Test Registration</title></head><body>");
        out.println("<h2>🧪 Registration Test & Debug</h2>");
        
        // Show current users
        showCurrentUsers(out);
        
        // Test registration form
        out.println("<div style='background: #f8f9fa; padding: 15px; border-radius: 5px; margin: 20px 0;'>");
        out.println("<h3>🆕 Test New User Registration</h3>");
        out.println("<form method='post'>");
        out.println("<div style='margin: 10px 0;'>");
        out.println("<label>Username:</label><br>");
        out.println("<input type='text' name='username' value='testuser" + System.currentTimeMillis() % 1000 + "' style='padding: 8px; width: 200px;'>");
        out.println("</div>");
        out.println("<div style='margin: 10px 0;'>");
        out.println("<label>Email:</label><br>");
        out.println("<input type='email' name='email' value='test" + System.currentTimeMillis() % 1000 + "@example.com' style='padding: 8px; width: 200px;'>");
        out.println("</div>");
        out.println("<div style='margin: 10px 0;'>");
        out.println("<label>Password:</label><br>");
        out.println("<input type='password' name='password' value='test123' style='padding: 8px; width: 200px;'>");
        out.println("</div>");
        out.println("<button type='submit' style='background: #28a745; color: white; padding: 10px 20px; border: none; border-radius: 5px;'>Test Registration</button>");
        out.println("</form>");
        out.println("</div>");
        
        out.println("<p><a href='jsp/register.jsp'>Go to Real Registration Page</a> | ");
        out.println("<a href='database-manager'>Database Manager</a></p>");
        
        out.println("</body></html>");
    }
    
    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        String username = request.getParameter("username");
        String email = request.getParameter("email");
        String password = request.getParameter("password");
        
        out.println("<!DOCTYPE html><html><head><title>Registration Test Result</title></head><body>");
        out.println("<h2>🧪 Registration Test Result</h2>");
        
        out.println("<div style='background: #e7f3ff; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
        out.println("<h3>📋 Input Data:</h3>");
        out.println("<p><strong>Username:</strong> " + username + "</p>");
        out.println("<p><strong>Email:</strong> " + email + "</p>");
        out.println("<p><strong>Password:</strong> " + password + "</p>");
        out.println("</div>");
        
        try {
            // Check if user already exists
            UserDAO userDAO = new UserDAO();
            User existingUser = userDAO.getUserByUsername(username);
            
            if (existingUser != null) {
                out.println("<div style='background: #fff3cd; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
                out.println("<h3>⚠️ Username Already Exists</h3>");
                out.println("<p>User '" + username + "' already exists in database.</p>");
                out.println("</div>");
            }
            
            // Check email in database
            try (Connection conn = DBUtil.getConnection()) {
                PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM users WHERE email = ?");
                ps.setString(1, email);
                ResultSet rs = ps.executeQuery();
                if (rs.next() && rs.getInt(1) > 0) {
                    out.println("<div style='background: #fff3cd; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
                    out.println("<h3>⚠️ Email Already Exists</h3>");
                    out.println("<p>Email '" + email + "' already exists in database.</p>");
                    out.println("</div>");
                }
            }
            
            // Try registration
            String passwordHash = PasswordUtil.hashPassword(password);
            User newUser = new User(0, username, email, passwordHash, "citizen");
            
            out.println("<div style='background: #e7f3ff; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
            out.println("<h3>🔐 Password Hash Generated:</h3>");
            out.println("<p>" + passwordHash + "</p>");
            out.println("</div>");
            
            boolean success = userDAO.registerUser(newUser);
            
            if (success) {
                out.println("<div style='background: #d4edda; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
                out.println("<h3>✅ Registration Successful!</h3>");
                out.println("<p>User '" + username + "' has been registered successfully.</p>");
                out.println("<p><strong>Login Credentials:</strong></p>");
                out.println("<p>Username: <code>" + username + "</code></p>");
                out.println("<p>Password: <code>" + password + "</code></p>");
                out.println("</div>");
            } else {
                out.println("<div style='background: #f8d7da; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
                out.println("<h3>❌ Registration Failed</h3>");
                out.println("<p>Could not register user '" + username + "'. This usually means the username or email already exists.</p>");
                out.println("</div>");
            }
            
        } catch (Exception e) {
            out.println("<div style='background: #f8d7da; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
            out.println("<h3>❌ Registration Error</h3>");
            out.println("<p><strong>Error:</strong> " + e.getMessage() + "</p>");
            out.println("</div>");
        }
        
        out.println("<p><a href='test-registration'>Try Another Registration</a> | ");
        out.println("<a href='debug-login'>Check Users</a></p>");
        
        out.println("</body></html>");
    }
    
    private void showCurrentUsers(PrintWriter out) {
        out.println("<div style='background: #f8f9fa; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
        out.println("<h3>👥 Current Users in Database:</h3>");
        
        try (Connection conn = DBUtil.getConnection()) {
            PreparedStatement ps = conn.prepareStatement("SELECT id, username, email, role FROM users ORDER BY id");
            ResultSet rs = ps.executeQuery();
            
            out.println("<table border='1' style='border-collapse: collapse; width: 100%;'>");
            out.println("<tr style='background: #e9ecef;'>");
            out.println("<th style='padding: 8px;'>ID</th>");
            out.println("<th style='padding: 8px;'>Username</th>");
            out.println("<th style='padding: 8px;'>Email</th>");
            out.println("<th style='padding: 8px;'>Role</th>");
            out.println("</tr>");
            
            while (rs.next()) {
                out.println("<tr>");
                out.println("<td style='padding: 8px;'>" + rs.getInt("id") + "</td>");
                out.println("<td style='padding: 8px;'>" + rs.getString("username") + "</td>");
                out.println("<td style='padding: 8px;'>" + rs.getString("email") + "</td>");
                out.println("<td style='padding: 8px;'>" + rs.getString("role") + "</td>");
                out.println("</tr>");
            }
            out.println("</table>");
            
        } catch (Exception e) {
            out.println("<p style='color: red;'>Error loading users: " + e.getMessage() + "</p>");
        }
        
        out.println("</div>");
    }
}