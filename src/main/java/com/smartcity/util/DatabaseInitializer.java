package com.smartcity.util;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import com.smartcity.dao.DBUtil;

@WebListener
public class DatabaseInitializer implements ServletContextListener {

    @Override
    public void contextInitialized(ServletContextEvent sce) {
        System.out.println("🚀 Smart City Management System - Starting up...");
        initializeDatabase();
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        System.out.println("🛑 Smart City Management System - Shutting down...");
    }

    private void initializeDatabase() {
        System.out.println("🔄 Initializing database...");
        try (Connection conn = DBUtil.getConnection()) {
            System.out.println("✅ Database connection established");
            
            // Check if users table exists
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM users LIMIT 1");
                ps.executeQuery();
                System.out.println("📋 Tables already exist");
            } catch (SQLException e) {
                // Tables don't exist, create them
                System.out.println("📋 Tables don't exist, creating them...");
                createTables(conn);
            }
            
        } catch (Exception e) {
            System.err.println("❌ Database initialization error: " + e.getMessage());
            e.printStackTrace();
        }
    }
    
    private void createTables(Connection conn) throws SQLException {
        try (java.sql.Statement stmt = conn.createStatement()) {
            System.out.println("🔨 Creating users table...");
            
            // Create users table
            stmt.execute("CREATE TABLE IF NOT EXISTS users (" +
                "id SERIAL PRIMARY KEY, " +
                "username VARCHAR(50) UNIQUE NOT NULL, " +
                "password VARCHAR(255) NOT NULL, " +
                "email VARCHAR(100) UNIQUE NOT NULL, " +
                "role VARCHAR(20) DEFAULT 'citizen', " +
                "created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP)");
            
            System.out.println("🔨 Creating complaints table...");
            
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
            
            System.out.println("👤 Inserting admin user...");
            
            // Insert admin user (password: admin123)
            try {
                // First delete existing admin user if any
                stmt.execute("DELETE FROM users WHERE username = 'admin'");
                stmt.execute("INSERT INTO users (username, password, email, role) VALUES " +
                    "('admin', '240be518fabd2724ddb6f04eeb1da5967448d7e831c08c8fa822809f74c720a9', 'admin@smartcity.com', 'admin')");
                System.out.println("✅ Admin user created: admin / admin123");
            } catch (SQLException e) {
                System.out.println("ℹ️ Admin user setup error: " + e.getMessage());
            }
            
            // Insert citizen user (password: test)
            try {
                // First delete existing citizen user if any
                stmt.execute("DELETE FROM users WHERE username = 'citizen1'");
                stmt.execute("INSERT INTO users (username, password, email, role) VALUES " +
                    "('citizen1', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'citizen1@example.com', 'citizen')");
                System.out.println("✅ Citizen user created: citizen1 / test");
            } catch (SQLException e) {
                System.out.println("ℹ️ Citizen user setup error: " + e.getMessage());
            }
            
            // Insert sample complaint
            try {
                stmt.execute("INSERT INTO complaints (user_id, category, subject, description, location) VALUES " +
                    "(2, 'Roads', 'Pothole on Main Street', 'Large pothole causing traffic issues', 'Main Street')");
                System.out.println("✅ Sample complaint added");
            } catch (SQLException e) {
                System.out.println("ℹ️ Sample data already exists");
            }
            
            System.out.println("🎉 Database initialization completed successfully!");
            System.out.println("🔑 Login credentials:");
            System.out.println("   Admin: admin / admin123");
            System.out.println("   Citizen: citizen1 / test");
        }
    }
}