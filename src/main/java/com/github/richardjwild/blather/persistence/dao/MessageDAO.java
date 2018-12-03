package com.github.richardjwild.blather.persistence.dao;

import com.github.richardjwild.blather.message.Message;
import com.github.richardjwild.blather.user.User;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class MessageDAO {
    private Connection connection;

    public MessageDAO(Connection connection) {
        this.connection = connection;
    }

    public List<Message> findBy(String userName) throws SQLException {
        String sql = "SELECT m.user_id, m.message_text, m.message_date, u.user_id, u.name" +
                    " FROM messages m LEFT OUTER JOIN users u " +
                    " ON m.user_id = u.user_id " +
                    " WHERE u.name = ?";

        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setString(1, userName);
        ResultSet resultSet = statement.executeQuery();

        List<Message> messages = new ArrayList<>();

        while(resultSet.next()) {
            User user = new User(userName);
            Message message = getMessage(resultSet, user);
            messages.add(message);
        }

        return messages;
    }

    private Message getMessage(ResultSet resultSet, User user) throws SQLException {
        return new Message(user,
                        resultSet.getString("message_text"),
                        resultSet.getTimestamp("message_date").toInstant());
    }

    public void save(Message message) throws SQLException {
        PreparedStatement userStatement = connection.prepareStatement(
                "SELECT user_id FROM users WHERE name = ?"
        );
        userStatement.setString(1, message.recipient().name());
        ResultSet resultSet = userStatement.executeQuery();
        int userId = 0;
        while(resultSet.next()) {
            userId = resultSet.getInt("user_id");
        }

        String sql = "INSERT INTO messages(user_id, message_text, message_date)" +
                " VALUES(?, ?, ?)";
        PreparedStatement statement = connection.prepareStatement(sql);
        statement.setInt(1, userId);
        statement.setString(2, message.text());
        statement.setObject(3, Timestamp.from(message.timestamp()));

        statement.execute();
        statement.close();
    }
}
