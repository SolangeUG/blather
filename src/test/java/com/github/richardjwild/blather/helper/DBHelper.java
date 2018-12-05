package com.github.richardjwild.blather.helper;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DBHelper {

    public static Connection getConnection() {
        return setUpConnection();
    }

    public static void insertTestData(Connection connection) {
        try {
            String sql = "INSERT INTO users(user_name) VALUES('Isimbi')";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearTestData(Connection connection) {
        try {
            String sql = "DELETE FROM messages";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = "DELETE FROM userFollowing";
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            sql = "DELETE FROM users";
            statement = connection.prepareStatement(sql);
            statement.executeUpdate();

            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public static void clearConnection(Connection connection) {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Connection setUpConnection() {
        String databaseUrl = "jdbc:postgresql://localhost:5432/blather";
        Properties properties = new Properties();
        properties.setProperty("user", "codurance");
        properties.setProperty("password", "1234");

        Connection connection = null;
        try {
            connection = DriverManager.getConnection(databaseUrl, properties);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}