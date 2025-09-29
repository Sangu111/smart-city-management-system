package com.smartcity.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smartcity.dao.DBUtil;

@WebServlet("/fix-users")
public class FixUsersServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html><html><head><title>Fix Users</title></head><body>");
        out.println("<h2>🔧 Fixing User Credentials</h2>");
        
        try (Connection conn = DBUtil.getConnection()) {
            // Delete existing users
            PreparedStatement deleteStmt = conn.prepareStatement("DELETE FROM users WHERE username IN ('admin', 'citizen1')");
            deleteStmt.executeUpdate();
            out.println("<p>🗑️ Deleted existing users</p>");
            
            // Insert admin with correct hash (admin123)
            PreparedStatement adminStmt = conn.prepareStatement(
                "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)");
            adminStmt.setString(1, "admin");
            adminStmt.setString(2, "240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9"); // admin123
            adminStmt.setString(3, "admin@smartcity.com");
            adminStmt.setString(4, "admin");
            adminStmt.executeUpdate();
            out.println("<p>✅ Admin user created: admin / admin123</p>");
            
            // Insert citizen with correct hash (test)
            PreparedStatement citizenStmt = conn.prepareStatement(
                "INSERT INTO users (username, password, email, role) VALUES (?, ?, ?, ?)");
            citizenStmt.setString(1, "citizen1");
            citizenStmt.setString(2, "9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08"); // test
            citizenStmt.setString(3, "citizen1@example.com");
            citizenStmt.setString(4, "citizen");
            citizenStmt.executeUpdate();
            out.println("<p>✅ Citizen user created: citizen1 / test</p>");
            
            out.println("<h3>🎉 Users Fixed Successfully!</h3>");
            out.println("<p><strong>Try these credentials:</strong></p>");
            out.println("<ul>");
            out.println("<li>Admin: <code>admin</code> / <code>admin123</code></li>");
            out.println("<li>Citizen: <code>citizen1</code> / <code>test</code></li>");
            out.println("</ul>");
            out.println("<p><a href='/'>Go to Login Page</a></p>");
            out.println("<p><a href='debug-login'>Check Debug Page</a></p>");
            
        } catch (Exception e) {
            out.println("<h3 style='color:red;'>❌ Error:</h3>");
            out.println("<p>" + e.getMessage() + "</p>");
            e.printStackTrace(out);
        }
        
        out.println("</body></html>");
    }
}