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
        String sql = "SELECT mess.user_name, mess.message_text, mess.message_date, " +
                    "        us.user_name " +
                    " FROM messages mess " +
                    " LEFT OUTER JOIN users us ON mess.user_name = us.user_name " +
                    " WHERE us.user_name = ?";

        PreparedStatement selectStatement = connection.prepareStatement(sql);
        selectStatement.setString(1, userName);
        ResultSet resultSet = selectStatement.executeQuery();

        List<Message> messages = new ArrayList<>();

        while(resultSet.next()) {
            User user = new User(userName);
            Message message = getMessage(resultSet, user);
            messages.add(message);
        }

        resultSet.close();
        return messages;
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
