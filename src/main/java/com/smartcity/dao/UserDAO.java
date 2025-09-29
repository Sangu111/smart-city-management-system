package com.smartcity.dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import com.smartcity.model.User;

public class UserDAO {
    
    static {
        // Auto-initialize database on first access
        initializeDatabase();
    }
    
    private static void initializeDatabase() {
        try (Connection conn = DBUtil.getConnection()) {
            // Check if users table exists
            try {
                PreparedStatement ps = conn.prepareStatement("SELECT COUNT(*) FROM users LIMIT 1");
                ps.executeQuery();
                // Table exists, do nothing
            } catch (SQLException e) {
                // Table doesn't exist, create it
                createTables(conn);
            }
        } catch (Exception e) {
            System.err.println("Database initialization error: " + e.getMessage());
        }
    }
    
    private static void createTables(Connection conn) throws SQLException {
        try (java.sql.Statement stmt = conn.createStatement()) {
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
                "('admin', 'ef92b778bafe771e89245b89ecbc08a44a4e166c06659911881f383d4473e94f', 'admin@smartcity.com', 'admin') " +
                "ON CONFLICT (username) DO NOTHING");
            
            // Insert citizen user (password: test)  
            stmt.execute("INSERT INTO users (username, password, email, role) VALUES " +
                "('citizen1', '9f86d081884c7d659a2feaa0c55ad015a3bf4f1b2b0b822cd15d6c15b0f00a08', 'citizen1@example.com', 'citizen') " +
                "ON CONFLICT (username) DO NOTHING");
            
            System.out.println("✅ Database tables created and initialized successfully!");
        }
    }
    
    public User getUserByUsername(String username) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM users WHERE username = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, username);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by username: " + e.getMessage());
        }
        return null;
    }

    public User getUserById(int id) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "SELECT * FROM users WHERE id = ?";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                return extractUser(rs);
            }
        } catch (SQLException e) {
            System.err.println("Error retrieving user by ID: " + e.getMessage());
        }
        return null;
    }

    public boolean registerUser(User user) {
        try (Connection conn = DBUtil.getConnection()) {
            String sql = "INSERT INTO users (username, email, password_hash, role) VALUES (?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(sql);
            ps.setString(1, user.getUsername());
            ps.setString(2, user.getEmail());
            ps.setString(3, user.getPasswordHash());
            ps.setString(4, user.getRole());
            return ps.executeUpdate() > 0;
        } catch (SQLException e) {
            System.err.println("Error registering user: " + e.getMessage());
        }
        return false;
    }

    private User extractUser(ResultSet rs) throws SQLException {
        return new User(
            rs.getInt("id"),
            rs.getString("username"),
            rs.getString("email"),
            rs.getString("password_hash"),
            rs.getString("role")
        );
    }
}
