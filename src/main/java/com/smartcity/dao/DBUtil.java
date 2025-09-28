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
            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            String driverClass = props.getProperty("db.driver");
            
            if (driverClass != null) {
                Class.forName(driverClass);
            }
        } catch (ClassNotFoundException | java.io.IOException e) {
            System.err.println("Database initialization error: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
