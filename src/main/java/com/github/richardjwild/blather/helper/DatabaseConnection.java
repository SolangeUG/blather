package com.github.richardjwild.blather.helper;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

public class DatabaseConnection {

    private Connection connection;

    public Connection getConnection() {
        if (connection == null) {
            setUpConnection();
        }
        return connection;
    }

    private void setUpConnection() {
        if (connection == null) {
            InputStream configuration = ClassLoader.getSystemResourceAsStream("application.properties");
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
        }
    }

    public void closeConnection() {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
