package com.smartcity.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.smartcity.dao.DBUtil;

@WebServlet("/check-connection")
public class ConnectionCheckServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html><html><head><title>Database Connection Check</title></head><body>");
        out.println("<h2>🔍 Database Connection Status</h2>");
        
        try (Connection conn = DBUtil.getConnection()) {
            DatabaseMetaData metaData = conn.getMetaData();
            
            out.println("<div style='background: #d4edda; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
            out.println("<h3>✅ Connection Successful!</h3>");
            out.println("<p><strong>Database URL:</strong> " + metaData.getURL() + "</p>");
            out.println("<p><strong>Database Product:</strong> " + metaData.getDatabaseProductName() + "</p>");
            out.println("<p><strong>Database Version:</strong> " + metaData.getDatabaseProductVersion() + "</p>");
            out.println("<p><strong>Driver:</strong> " + metaData.getDriverName() + "</p>");
            out.println("<p><strong>Username:</strong> " + metaData.getUserName() + "</p>");
            out.println("</div>");
            
            // Check if tables exist
            ResultSet tables = metaData.getTables(null, null, "%", new String[]{"TABLE"});
            out.println("<h3>📋 Existing Tables:</h3>");
            out.println("<ul>");
            boolean hasUsers = false;
            boolean hasComplaints = false;
            while (tables.next()) {
                String tableName = tables.getString("TABLE_NAME");
                out.println("<li>" + tableName + "</li>");
                if ("users".equals(tableName)) hasUsers = true;
                if ("complaints".equals(tableName)) hasComplaints = true;
            }
            out.println("</ul>");
            
            if (!hasUsers || !hasComplaints) {
                out.println("<div style='background: #fff3cd; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
                out.println("<h3>⚠️ Tables Missing</h3>");
                out.println("<p>Your database is connected but missing some tables.</p>");
                out.println("<p><a href='setup-db' style='background: #007bff; color: white; padding: 10px 15px; text-decoration: none; border-radius: 5px;'>Setup Database Now</a></p>");
                out.println("</div>");
            } else {
                out.println("<div style='background: #d4edda; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
                out.println("<h3>🎉 Database Ready!</h3>");
                out.println("<p>All required tables exist. Your Smart City app is ready to use.</p>");
                out.println("<p><a href='/' style='background: #28a745; color: white; padding: 10px 15px; text-decoration: none; border-radius: 5px;'>Go to App</a></p>");
                out.println("</div>");
            }
            
        } catch (Exception e) {
            out.println("<div style='background: #f8d7da; padding: 15px; border-radius: 5px; margin: 10px 0;'>");
            out.println("<h3 style='color: #721c24;'>❌ Connection Failed</h3>");
            out.println("<p><strong>Error:</strong> " + e.getMessage() + "</p>");
            out.println("<p><strong>Your Connection String Should Be:</strong></p>");
            out.println("<code>postgresql://smartcity_user:p4DMwDlKZh7rIoWdB7zTxvy3gPIa0Fly@dpg-d3d451ggjchc739mojhg-a.singapore-postgres.render.com/smartcity_wb0d</code>");
            out.println("</div>");
        }
        
        out.println("<h3>🛠️ Quick Actions:</h3>");
        out.println("<p><a href='database-manager'>View Database Manager</a> | ");
        out.println("<a href='debug-login'>Debug Login</a> | ");
        out.println("<a href='setup-db'>Setup Database</a></p>");
        
        out.println("</body></html>");
    }
}