package com.github.richardjwild.blather.persistence;

import com.github.richardjwild.blather.message.Message;
import com.github.richardjwild.blather.message.MessageRepository;
import com.github.richardjwild.blather.persistence.dao.MessageDAO;
import com.github.richardjwild.blather.user.User;

import java.util.stream.Stream;

public class JdbcMessageRepository implements MessageRepository {
    private MessageDAO messageDAO;

    public JdbcMessageRepository(MessageDAO messageDAO) {
        this.messageDAO = messageDAO;
    }

    @Override
    public void postMessage(User recipient, Message message) {
        this.messageDAO.save(message);
    }

    @Override
    public Stream<Message> allMessagesPostedTo(User recipient) {
        return messageDAO.findBy(recipient.name()).stream();
    }
}
