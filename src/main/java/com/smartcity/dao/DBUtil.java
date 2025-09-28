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
    private static String driver;

    static {
        try {
            Properties props = new Properties();
            InputStream in = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
            if (in == null) {
                throw new RuntimeException("Database configuration file 'db.properties' not found");
            }
            props.load(in);
            url = props.getProperty("db.url");
            username = props.getProperty("db.username");
            password = props.getProperty("db.password");
            driver = props.getProperty("db.driver");
            Class.forName(driver);
            in.close();
        } catch (ClassNotFoundException e) {
            System.err.println("MySQL JDBC driver not found: " + e.getMessage());
            throw new RuntimeException("Database driver initialization failed", e);
        } catch (Exception e) {
            System.err.println("Failed to initialize database configuration: " + e.getMessage());
            throw new RuntimeException("Database initialization failed", e);
        }
    }

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
