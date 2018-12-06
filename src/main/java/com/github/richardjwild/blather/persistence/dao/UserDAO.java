package com.github.richardjwild.blather.persistence.dao;

import com.github.richardjwild.blather.user.User;
import org.springframework.jdbc.core.BatchPreparedStatementSetter;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserDAO {

    private JdbcTemplate jdbcTemplate;

    public UserDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public User findBy(String name) {
        List<User> users = this.jdbcTemplate.query("SELECT * FROM users WHERE user_name = ?",
                new Object[]{name},
                (rs, rowNum) -> new User(rs.getString("user_name")));

        User user = null;
        if (!users.isEmpty()) {
            user = users.get(0);
            String sql = "SELECT * FROM userFollowing WHERE user_name = ?";
            List<User> usersFollowing = this.jdbcTemplate.query(sql,
                    new Object[]{name},
                    (rs, rowNum) -> new User(rs.getString("follows_name")));

            usersFollowing.forEach(user::follow);
        }

        return user;
    }

    public void save(User user) {
        this.jdbcTemplate.update("INSERT INTO users(user_name) VALUES(?)",user.name());
        updateFollowing(user);
    }

    public void updateFollowing(User user) {
        List<User> newUsersFollowing = new ArrayList<>(user.getUsersFollowing());

        User retrievedUser = this.findBy(user.name());
        List<User> usersFollowing = new ArrayList<>(retrievedUser.getUsersFollowing());

        List<User> result = newUsersFollowing
                                .stream()
                                .filter(elem -> ! usersFollowing.contains(elem))
                                .collect(Collectors.toList());

        String sql = "INSERT INTO userFollowing(user_name, follows_name) VALUES(?, ?)";

        this.jdbcTemplate.batchUpdate(sql,
                new BatchPreparedStatementSetter() {
                    public void setValues(PreparedStatement ps, int i) throws SQLException {
                        ps.setString(1, user.name());
                        ps.setString(2, result.get(i).name());
                    }
                    public int getBatchSize() {
                        return result.size();
                    }
                }
        );
    }
}
