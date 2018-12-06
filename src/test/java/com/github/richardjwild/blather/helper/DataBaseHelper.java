package com.github.richardjwild.blather.helper;

import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;

public class DataBaseHelper {

    private JdbcTemplate jdbcTemplate;

    public DataBaseHelper(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public void initializeTestData() {
        String sql = "INSERT INTO users(user_name) VALUES('Isimbi')";
        this.jdbcTemplate.update(sql);
    }

    public void clearTestData() {
        String sql = "DELETE FROM messages";
        this.jdbcTemplate.update(sql);

        sql = "DELETE FROM userFollowing";
        this.jdbcTemplate.update(sql);

        sql = "DELETE FROM users";
        this.jdbcTemplate.update(sql);
    }

}