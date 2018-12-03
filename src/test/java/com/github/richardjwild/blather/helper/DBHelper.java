package com.github.richardjwild.blather.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DBHelper {

    private static Connection connection;

    public static Connection getConnection() {
        String databaseUrl = "jdbc:postgresql://localhost:5432/test_blather";
        Properties properties = new Properties();
        properties.setProperty("user", "postgres");
        properties.setProperty("password", "postgres");

        try {
            connection = DriverManager.getConnection(databaseUrl, properties);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }

    public static void clearConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}