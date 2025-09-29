package com.smartcity.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;

import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.smartcity.dao.DBUtil;

@WebServlet("/init-db")
public class InitDbServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try (Connection conn = DBUtil.getConnection()) {
            
            // Check if users table exists
            boolean tablesExist = false;
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM users");
                ps.executeQuery();
                tablesExist = true;
            } catch (Exception e) {
                // Tables don't exist
            }
            
            if (!tablesExist) {
                // Create tables and insert data
                Statement stmt = conn.createStatement();
                
                // Create users table
                stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                    "id SERIAL PRIMARY KEY, " +
                    "username VARCHAR(50) UNIQUE NOT NULL, " +
                    "password VARCHAR(255) NOT NULL, " +
                    "email VARCHAR(100) UNIQUE NOT NULL, " +
                    "role VARCHAR(20) DEFAULT 'citizen', " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
                
                // Create complaints table
                stmt.execute("CREATE TABLE IF NOT EXISTS complaints (" +
                    "id SERIAL PRIMARY KEY, " +
                    "user_id INTEGER NOT NULL, " +
                    "category VARCHAR(50) NOT NULL, " +
                    "subject VARCHAR(200) NOT NULL, " +
                    "description TEXT NOT NULL, " +
                    "location VARCHAR(200), " +
                    "status VARCHAR(50) DEFAULT 'pending', " +
                    "priority VARCHAR(20) DEFAULT 'medium', " +
                    "photo_path VARCHAR(500), " +
                    "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP, " +
                    "FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE)");
                
                // Insert admin user (password: admin123)
                stmt.execute("INSERT INTO users (username, password, email, role) VALUES " +
                    "('admin', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'admin@smartcity.com', 'admin')");
                
                // Insert citizen user (password: test)
                stmt.execute("INSERT INTO users (username, password, email, role) VALUES " +
                    "('citizen1', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'citizen1@example.com', 'citizen')");
                
                // Insert sample complaints
                stmt.execute("INSERT INTO complaints (user_id, category, subject, description, location) VALUES " +
                    "(2, 'Roads', 'Pothole on Main Street', 'Large pothole causing traffic issues', 'Main Street')");
                
                out.println("✅ Database initialized successfully!<br>");
                out.println("Admin Login: admin / admin123<br>");
                out.println("Citizen Login: citizen1 / test<br>");
                out.println("<a href='/'>Go to Login</a>");
            } else {
                out.println("✅ Database already initialized!<br>");
                out.println("<a href='/'>Go to Login</a>");
            }
            
        } catch (Exception e) {
            out.println("❌ Error: " + e.getMessage());
        }
    }
}