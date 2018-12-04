package com.github.richardjwild.blather.persistence.dao;

import com.github.richardjwild.blather.user.User;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDAO {

    private Connection connection;

    public UserDAO(Connection connection) {
        this.connection = connection;
    }

    public User findBy(String name) throws SQLException {
        String sql = "SELECT * FROM users WHERE user_name = ?";
        PreparedStatement selectStatement = connection.prepareStatement(sql);
        selectStatement.setString(1, name);

        ResultSet results = selectStatement.executeQuery();

        User user = null;
        while(results.next()) {
            user = new User(results.getString("user_name"));
        }

        if (user != null) {
            sql = "SELECT * FROM userFollowing WHERE user_name = ?";
            selectStatement = connection.prepareStatement(sql);
            selectStatement.setString(1, name);

            ResultSet followings = selectStatement.executeQuery();
            while(followings.next()) {
                User following = new User(followings.getString("follows_name"));
                user.follow(following);
            }

            followings.close();
        }

        results.close();
        selectStatement.close();
        return user;
    }

    public void save(User user) throws SQLException {
        String sql = "INSERT INTO users(user_name) VALUES(?)";
        PreparedStatement insertStatement = connection.prepareStatement(sql);
        insertStatement.setString(1, user.name());

        insertStatement.executeUpdate();
        List<User> usersFollowing = new ArrayList<>(user.getUsersFollowing());

        for (User following : usersFollowing) {
            sql = "INSERT INTO userFollowing(user_name, follows_name) VALUES(?, ?)";
            insertStatement = connection.prepareStatement(sql);
            insertStatement.setString(1, user.name());
            insertStatement.setString(2, following.name());
            insertStatement.executeUpdate();
        }

        insertStatement.close();
    }
}
