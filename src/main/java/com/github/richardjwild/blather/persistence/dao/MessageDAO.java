package com.github.richardjwild.blather.persistence.dao;

import com.github.richardjwild.blather.helper.DatabaseConnection;
import com.github.richardjwild.blather.message.Message;
import com.github.richardjwild.blather.user.User;
import org.springframework.jdbc.core.JdbcTemplate;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    private Connection connection;
    private JdbcTemplate jdbcTemplate;

    public MessageDAO(Connection connection) {
        this.connection = connection;
        this.jdbcTemplate = new JdbcTemplate(DataSourceHelper.getDataSource());
    }

    public List<Message> findBy(String userName) throws SQLException {
        String sql = "SELECT mess.user_name, mess.message_text, mess.message_date, " +
                    "        us.user_name " +
                    " FROM messages mess " +
                    " LEFT OUTER JOIN users us ON mess.user_name = us.user_name " +
                    " WHERE us.user_name = ?";

        User user = new User(userName);

        return this.jdbcTemplate.query(sql,
                                    new Object[] {userName},
                                    (rs, rowNum) -> new Message(user,
                                                    rs.getString("message_text"),
                                                    rs.getTimestamp("message_date").toInstant()));
    }

    private Message getMessage(ResultSet resultSet, User user) throws SQLException {
        return new Message(user,
                        resultSet.getString("message_text"),
                        resultSet.getTimestamp("message_date").toInstant());
    }

    public void save(Message message) throws SQLException {
        String sql = "INSERT INTO messages(user_name, message_text, message_date)" +
                    " VALUES(?, ?, ?)";
        PreparedStatement insertStatement = connection.prepareStatement(sql);
        insertStatement.setString(1, message.recipient().name());
        insertStatement.setString(2, message.text());
        insertStatement.setObject(3, Timestamp.from(message.timestamp()));

        insertStatement.executeUpdate();
        insertStatement.close();
    }
}
