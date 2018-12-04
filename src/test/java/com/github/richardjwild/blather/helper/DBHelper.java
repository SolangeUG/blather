package com.github.richardjwild.blather.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DBHelper {

    private static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            setUpConnection();
        }
        return connection;
    }

    public static void insertTestData() {
        if (connection != null) {
            try {
                String sql = "INSERT INTO users(user_name) VALUES('Isimbi')";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.executeUpdate();
                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }

    public static void clearTestData() {
        if (connection != null) {
            try {
                String sql = "DELETE FROM messages";
                PreparedStatement statement = connection.prepareStatement(sql);
                statement.executeUpdate();

                sql = "DELETE FROM users";
                statement = connection.prepareStatement(sql);
                statement.executeUpdate();

                statement.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
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

    private static void setUpConnection() {
        if (connection == null) {
            String databaseUrl = "jdbc:postgresql://localhost:5432/test_blather";
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
}