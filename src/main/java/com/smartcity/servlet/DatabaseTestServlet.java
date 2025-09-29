package com.smartcity.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.smartcity.dao.DBUtil;

@WebServlet("/dbtest")
public class DatabaseTestServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<html><body>");
        out.println("<h1>Database Connection Test</h1>");
        
        try (Connection conn = DBUtil.getConnection()) {
            out.println("<p style='color: green;'>✅ Database connection successful!</p>");
            
            DatabaseMetaData metaData = conn.getMetaData();
            out.println("<p><strong>Database:</strong> " + metaData.getDatabaseProductName() + "</p>");
            out.println("<p><strong>URL:</strong> " + metaData.getURL() + "</p>");
            
            // Check if tables exist
            ResultSet rs = metaData.getTables(null, null, "users", null);
            if (rs.next()) {
                out.println("<p style='color: blue;'>📋 Table 'users' exists</p>");
                
                // Count users
                Statement stmt = conn.createStatement();
                ResultSet countRs = stmt.executeQuery("SELECT COUNT(*) as count FROM users");
                if (countRs.next()) {
                    out.println("<p>👥 Number of users: " + countRs.getInt("count") + "</p>");
                }
            } else {
                out.println("<p style='color: orange;'>⚠️ Table 'users' does not exist</p>");
                out.println("<a href='/setup' style='background: #28a745; color: white; padding: 10px; text-decoration: none;'>Create Tables</a>");
            }
            
        } catch (Exception e) {
            out.println("<p style='color: red;'>❌ Database error: " + e.getMessage() + "</p>");
            out.println("<pre>" + e.toString() + "</pre>");
        }
        
        out.println("<br><br>");
        out.println("<a href='/' style='background: #007bff; color: white; padding: 10px; text-decoration: none;'>Back to App</a>");
        out.println("</body></html>");
    }
}