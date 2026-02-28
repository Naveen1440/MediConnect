package com.edutech.progressive.config;


import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;
import java.io.InputStream;


import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;



import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnectionManager {

    private static final Properties properties = new Properties();

    static {
        loadProperties();
        loadDriver();
    }

    // Load database configuration from application.properties
    private static void loadProperties() {
        try (InputStream input = DatabaseConnectionManager.class
                .getClassLoader()
                .getResourceAsStream("application.properties")) {

            if (input == null) {
                throw new RuntimeException("application.properties not found in classpath!");
            }

            properties.load(input);

        } catch (IOException e) {
            throw new RuntimeException("Failed to load application.properties", e);
        }
    }

    // Load JDBC driver
    private static void loadDriver() {
        try {
            Class.forName(properties.getProperty("spring.datasource.driver-class-name"));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("Unable to load JDBC Driver", e);
        }
    }

    // Returns JDBC Connection using loaded properties
    public static Connection getConnection() throws SQLException {

        String url = properties.getProperty("spring.datasource.url");
        String username = properties.getProperty("spring.datasource.username");
        String password = properties.getProperty("spring.datasource.password");

        return DriverManager.getConnection(url, username, password);
    }
}