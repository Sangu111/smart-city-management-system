package com.smartcity.servlet;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.Statement;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import com.smartcity.dao.DBUtil;

@WebServlet("/setup")
public class DatabaseSetupServlet extends HttpServlet {
    
    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) 
            throws ServletException, IOException {
        
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();
        
        try (Connection conn = DBUtil.getConnection();
             Statement stmt = conn.createStatement()) {
            
            // Drop existing tables
            try {
                stmt.execute("DROP TABLE IF EXISTS complaints");
                stmt.execute("DROP TABLE IF EXISTS users");
            } catch (Exception e) {
                // Ignore if tables don't exist
            }
            
            // Create users table
            stmt.execute("CREATE TABLE users (" +
                "id SERIAL PRIMARY KEY, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "role VARCHAR(20) DEFAULT 'citizen', " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            
            // Create complaints table
            stmt.execute("CREATE TABLE complaints (" +
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
            
            // Insert sample citizen (password: test)
            stmt.execute("INSERT INTO users (username, password, email, role) VALUES " +
                "('citizen1', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'citizen1@example.com', 'citizen')");
            
            // Insert sample complaints
            stmt.execute("INSERT INTO complaints (user_id, category, subject, description, location) VALUES " +
                "(2, 'Roads', 'Pothole on Main Street', 'Large pothole causing traffic issues', 'Main Street')");
            
            out.println("<html><body>");
            out.println("<h1 style='color: green;'>✅ Database Setup Complete!</h1>");
            out.println("<p>Tables created and sample data inserted successfully.</p>");
            out.println("<h2>Test Credentials:</h2>");
            out.println("<ul>");
            out.println("<li><strong>Admin:</strong> admin / admin123</li>");
            out.println("<li><strong>Citizen:</strong> citizen1 / test</li>");
            out.println("</ul>");
            out.println("<a href='/' style='background: #007bff; color: white; padding: 10px; text-decoration: none; border-radius: 5px;'>Go to Login</a>");
            out.println("</body></html>");
            
        } catch (Exception e) {
            out.println("<html><body>");
            out.println("<h1 style='color: red;'>❌ Database Setup Failed</h1>");
            out.println("<p>Error: " + e.getMessage() + "</p>");
            out.println("<pre>" + e.getStackTrace() + "</pre>");
            out.println("</body></html>");
            e.printStackTrace();
        }
    }
}