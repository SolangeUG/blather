package com.github.richardjwild.blather.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            setUpConnection();
        }
        return connection;
    }

    private static void setUpConnection() {
        if (connection == null) {
            String databaseUrl = "jdbc:postgresql://localhost:5432/blather";
            Properties properties = new Properties();
            properties.setProperty("user", "postgres");
            properties.setProperty("password", "postgres");

            try {
                connection = DriverManager.getConnection(databaseUrl, properties);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
