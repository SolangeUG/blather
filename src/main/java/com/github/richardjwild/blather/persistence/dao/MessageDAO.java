package com.github.richardjwild.blather.persistence.dao;

import com.github.richardjwild.blather.message.Message;
import com.github.richardjwild.blather.user.User;
import org.springframework.jdbc.core.JdbcTemplate;

import javax.sql.DataSource;
import java.sql.Timestamp;
import java.util.List;

public class MessageDAO {

    private JdbcTemplate jdbcTemplate;

    public MessageDAO(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public List<Message> findBy(String userName) {
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

    public void save(Message message) {
        String sql = "INSERT INTO messages(user_name, message_text, message_date)" +
                    " VALUES(?, ?, ?)";

        this.jdbcTemplate.update(sql,
                message.recipient().name(), message.text(), Timestamp.from(message.timestamp()));
    }
}
