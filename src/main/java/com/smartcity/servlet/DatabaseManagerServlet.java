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

@WebServlet("/database-manager")
public class DatabaseManagerServlet extends HttpServlet {
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        out.println("<!DOCTYPE html><html><head><title>Database Manager</title>");
        out.println("<style>");
        out.println("body { font-family: Arial; margin: 20px; }");
        out.println("table { border-collapse: collapse; width: 100%; margin: 10px 0; }");
        out.println("th, td { border: 1px solid #ddd; padding: 8px; text-align: left; }");
        out.println("th { background-color: #f2f2f2; }");
        out.println("pre { background: #f8f9fa; padding: 15px; border-radius: 5px; overflow-x: auto; }");
        out.println(".section { margin: 20px 0; padding: 15px; border: 1px solid #ddd; border-radius: 5px; }");
        out.println("</style>");
        out.println("</head><body>");
        
        out.println("<h1>🗄️ Smart City Database Manager</h1>");
        
        try (Connection conn = DBUtil.getConnection()) {
            out.println("<div class='section'>");
            out.println("<h2>✅ Database Connection: SUCCESS</h2>");
            out.println("<p>Database URL: " + conn.getMetaData().getURL() + "</p>");
            out.println("</div>");
            
            // Show all users
            showUsersTable(out, conn);
            
            // Show all complaints
            showComplaintsTable(out, conn);
            
            // Show SQL queries for vendor database
            showSQLQueries(out);
            
            // Show database schema
            showDatabaseSchema(out);
            
        } catch (Exception e) {
            out.println("<div class='section' style='background: #fee;'>");
            out.println("<h3 style='color: red;'>❌ Database Error:</h3>");
            out.println("<p>" + e.getMessage() + "</p>");
            out.println("</div>");
        }
        
        out.println("</body></html>");
    }
    
    private void showUsersTable(PrintWriter out, Connection conn) throws Exception {
        out.println("<div class='section'>");
        out.println("<h2>👥 Users Table</h2>");
        
        PreparedStatement ps = conn.prepareStatement("SELECT id, username, email, role, created_at FROM users ORDER BY id");
        ResultSet rs = ps.executeQuery();
        
        out.println("<table>");
        out.println("<tr><th>ID</th><th>Username</th><th>Email</th><th>Role</th><th>Created At</th></tr>");
        
        while (rs.next()) {
            out.println("<tr>");
            out.println("<td>" + rs.getInt("id") + "</td>");
            out.println("<td>" + rs.getString("username") + "</td>");
            out.println("<td>" + rs.getString("email") + "</td>");
            out.println("<td>" + rs.getString("role") + "</td>");
            out.println("<td>" + rs.getTimestamp("created_at") + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</div>");
    }
    
    private void showComplaintsTable(PrintWriter out, Connection conn) throws Exception {
        out.println("<div class='section'>");
        out.println("<h2>📋 Complaints Table</h2>");
        
        PreparedStatement ps = conn.prepareStatement("SELECT c.id, u.username, c.category, c.subject, c.status, c.created_at FROM complaints c LEFT JOIN users u ON c.user_id = u.id ORDER BY c.id");
        ResultSet rs = ps.executeQuery();
        
        out.println("<table>");
        out.println("<tr><th>ID</th><th>User</th><th>Category</th><th>Subject</th><th>Status</th><th>Created At</th></tr>");
        
        while (rs.next()) {
            out.println("<tr>");
            out.println("<td>" + rs.getInt("id") + "</td>");
            out.println("<td>" + rs.getString("username") + "</td>");
            out.println("<td>" + rs.getString("category") + "</td>");
            out.println("<td>" + rs.getString("subject") + "</td>");
            out.println("<td>" + rs.getString("status") + "</td>");
            out.println("<td>" + rs.getTimestamp("created_at") + "</td>");
            out.println("</tr>");
        }
        out.println("</table>");
        out.println("</div>");
    }
    
    private void showSQLQueries(PrintWriter out) {
        out.println("<div class='section'>");
        out.println("<h2>🔧 SQL Queries for Your Vendor Database</h2>");
        
        out.println("<h3>1. Create Users Table</h3>");
        out.println("<pre>");
        out.println("CREATE TABLE users (");
        out.println("    id SERIAL PRIMARY KEY,");
        out.println("    username VARCHAR(50) UNIQUE NOT NULL,");
        out.println("    password VARCHAR(255) NOT NULL,");
        out.println("    email VARCHAR(100) UNIQUE NOT NULL,");
        out.println("    role VARCHAR(20) DEFAULT 'citizen',");
        out.println("    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
        out.println(");");
        out.println("</pre>");
        
        out.println("<h3>2. Create Complaints Table</h3>");
        out.println("<pre>");
        out.println("CREATE TABLE complaints (");
        out.println("    id SERIAL PRIMARY KEY,");
        out.println("    user_id INTEGER NOT NULL,");
        out.println("    category VARCHAR(50) NOT NULL,");
        out.println("    subject VARCHAR(200) NOT NULL,");
        out.println("    description TEXT NOT NULL,");
        out.println("    location VARCHAR(200),");
        out.println("    status VARCHAR(50) DEFAULT 'pending',");
        out.println("    priority VARCHAR(20) DEFAULT 'medium',");
        out.println("    photo_path VARCHAR(500),");
        out.println("    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
        out.println("    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
        out.println("    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE");
        out.println(");");
        out.println("</pre>");
        
        out.println("<h3>3. Insert Sample Data</h3>");
        out.println("<pre>");
        out.println("-- Admin user (password: admin123)");
        out.println("INSERT INTO users (username, password, email, role) VALUES ");
        out.println("('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin@smartcity.com', 'admin');");
        out.println("");
        out.println("-- Citizen user (password: test)");
        out.println("INSERT INTO users (username, password, email, role) VALUES ");
        out.println("('citizen1', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'citizen1@example.com', 'citizen');");
        out.println("");
        out.println("-- Sample complaint");
        out.println("INSERT INTO complaints (user_id, category, subject, description, location) VALUES ");
        out.println("(2, 'Roads', 'Pothole on Main Street', 'Large pothole causing traffic issues', 'Main Street');");
        out.println("</pre>");
        
        out.println("<h3>4. Clear Existing Users (if needed)</h3>");
        out.println("<pre>");
        out.println("-- Delete all users and complaints");
        out.println("DELETE FROM complaints;");
        out.println("DELETE FROM users;");
        out.println("");
        out.println("-- Reset auto-increment counters");
        out.println("ALTER SEQUENCE users_id_seq RESTART WITH 1;");
        out.println("ALTER SEQUENCE complaints_id_seq RESTART WITH 1;");
        out.println("</pre>");
        
        out.println("</div>");
    }
    
    private void showDatabaseSchema(PrintWriter out) {
        out.println("<div class='section'>");
        out.println("<h2>📊 Complete Database Schema</h2>");
        out.println("<pre>");
        out.println("-- Smart City Management System Database Schema");
        out.println("-- Compatible with PostgreSQL and MySQL");
        out.println("");
        out.println("-- Drop existing tables");
        out.println("DROP TABLE IF EXISTS complaints CASCADE;");
        out.println("DROP TABLE IF EXISTS users CASCADE;");
        out.println("");
        out.println("-- Create users table");
        out.println("CREATE TABLE users (");
        out.println("    id SERIAL PRIMARY KEY,");
        out.println("    username VARCHAR(50) UNIQUE NOT NULL,");
        out.println("    password VARCHAR(255) NOT NULL,");
        out.println("    email VARCHAR(100) UNIQUE NOT NULL,");
        out.println("    role VARCHAR(20) DEFAULT 'citizen',");
        out.println("    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP");
        out.println(");");
        out.println("");
        out.println("-- Create complaints table");
        out.println("CREATE TABLE complaints (");
        out.println("    id SERIAL PRIMARY KEY,");
        out.println("    user_id INTEGER NOT NULL,");
        out.println("    category VARCHAR(50) NOT NULL,");
        out.println("    subject VARCHAR(200) NOT NULL,");
        out.println("    description TEXT NOT NULL,");
        out.println("    location VARCHAR(200),");
        out.println("    status VARCHAR(50) DEFAULT 'pending',");
        out.println("    priority VARCHAR(20) DEFAULT 'medium',");
        out.println("    photo_path VARCHAR(500),");
        out.println("    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
        out.println("    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,");
        out.println("    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE");
        out.println(");");
        out.println("");
        out.println("-- Insert initial data");
        out.println("INSERT INTO users (username, password, email, role) VALUES");
        out.println("('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin@smartcity.com', 'admin'),");
        out.println("('citizen1', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'citizen1@example.com', 'citizen');");
        out.println("");
        out.println("INSERT INTO complaints (user_id, category, subject, description, location) VALUES");
        out.println("(2, 'Roads', 'Pothole on Main Street', 'Large pothole causing traffic issues', 'Main Street');");
        out.println("</pre>");
        out.println("</div>");
    }
}