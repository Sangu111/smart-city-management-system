package com.smartcity.dao;

import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBUtil {
    private static String url;
    private static String username;
    private static String password;

    static {
        try {
            Properties props = new Properties();
            try (InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties")) {
                if (in == null) {
                    throw new RuntimeException("Database configuration file 'db.properties' not found");
                }
                props.load(in);
            }
            // Support environment variables with fallback to properties
            String dbUrl = System.getenv("DATABASE_URL") != null ? 
                  System.getenv("DATABASE_URL") : 
                  (System.getenv("JDBC_DATABASE_URL") != null ? 
                   System.getenv("JDBC_DATABASE_URL") : props.getProperty("db.url"));
            
            // Convert postgresql:// to jdbc:postgresql:// for Render compatibility
            if (dbUrl != null && dbUrl.startsWith("postgresql://")) {
                dbUrl = "jdbc:" + dbUrl;
            }
            
            url = dbUrl;
            
            // If using DATABASE_URL (contains credentials), don't use separate username/password
            if (System.getenv("DATABASE_URL") != null) {
                username = null;  // Credentials are in the URL
                password = null;  // Credentials are in the URL
            } else {
                username = System.getenv("JDBC_DATABASE_USER") != null ? 
                          System.getenv("JDBC_DATABASE_USER") : props.getProperty("db.username");
                password = System.getenv("JDBC_DATABASE_PASSWORD") != null ? 
                          System.getenv("JDBC_DATABASE_PASSWORD") : props.getProperty("db.password");
            }
            
            // Auto-detect database driver based on URL
            String driverClass;
            if (dbUrl != null && dbUrl.startsWith("jdbc:postgresql")) {
                driverClass = "org.postgresql.Driver";
            } else if (dbUrl != null && dbUrl.startsWith("jdbc:mysql")) {
                driverClass = "com.mysql.cj.jdbc.Driver";
            } else {
                driverClass = props.getProperty("db.driver");
            }
            
            if (driverClass != null) {
                Class.forName(driverClass);
            }
        } catch (ClassNotFoundException | java.io.IOException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        if (username != null && password != null) {
            return DriverManager.getConnection(url, username, password);
        } else {
            // URL contains credentials (like Render DATABASE_URL)
            return DriverManager.getConnection(url);
        }
    }
}
