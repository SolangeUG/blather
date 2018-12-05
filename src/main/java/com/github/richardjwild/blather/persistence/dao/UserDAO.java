package com.github.richardjwild.blather.persistence.dao;

import com.github.richardjwild.blather.user.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class UserDAO {

    private Connection connection;
    private JdbcTemplate jdbcTemplate;

    public UserDAO(Connection connection) {
        this.connection = connection;


        this.jdbcTemplate = new JdbcTemplate(DataSourceHelper.getDataSource());
    }

    public User findBy(String name) throws SQLException {
        List<User> users = this.jdbcTemplate.query("SELECT * FROM users WHERE user_name = ?",
                new Object[]{name},
                (rs, rowNum) -> {
                    User user1 = new User(rs.getString("user_name"));
                    return user1;
                });

        User user = null;
        if (!users.isEmpty()) {
            user = users.get(0);
            String sql = "SELECT * FROM userFollowing WHERE user_name = ?";
            List<User> usersFollowing = this.jdbcTemplate.query(sql,
                    new Object[]{name},
                    (rs, rowNum) -> {
                        User user1 = new User(rs.getString("follows_name"));
                        return user1;
                    });

            usersFollowing.forEach(user::follow);
        }

        return user;
    }

    public void save(User user) throws SQLException {
        this.jdbcTemplate.update("INSERT INTO users(user_name) VALUES(?)",user.name());
        updateFollowing(user);
    }

    public void updateFollowing(User user) throws SQLException {
        List<User> newUsersFollowing = new ArrayList<>(user.getUsersFollowing());

        User retrievedUser = this.findBy(user.name());
        List<User> usersFollowing = new ArrayList<>(retrievedUser.getUsersFollowing());

        for (User following : newUsersFollowing) {

            if (! usersFollowing.contains(following)) {
                String sql = "INSERT INTO userFollowing(user_name, follows_name) VALUES(?, ?)";

                PreparedStatement insertStatement = connection.prepareStatement(sql);
                insertStatement.setString(1, user.name());
                insertStatement.setString(2, following.name());
                insertStatement.executeUpdate();
            }
        }
    }
}
