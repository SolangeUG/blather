package com.github.richardjwild.blather.persistence.dao;

import com.github.richardjwild.blather.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public User findBy(String name) throws SQLException {
        String sql = "SELECT * FROM users WHERE name = ?";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, name);

        ResultSet results = statement.executeQuery();

        User user = null;
        while(results.next()) {
            user = new User(results.getString("name"));
            results.close();
        }
        return user;
    }

    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users(name) VALUES(?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, user.name());
        statement.execute();
    }
}
