package com.github.richardjwild.blather.helper;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Properties;

public class DataBaseHelper {

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
        Connection connection = null;
        InputStream configuration = ClassLoader.getSystemResourceAsStream("application-test.properties");
        Properties properties = new Properties();
        try {
            properties.load(configuration);
            connection = DriverManager.getConnection(
                    properties.getProperty("database.url"),
                    properties.getProperty("database.user"),
                    properties.getProperty("database.password")
            );
        } catch (IOException | SQLException e) {
            e.printStackTrace();
        }
        return connection;
    }
}