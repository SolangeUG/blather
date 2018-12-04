package com.github.richardjwild.blather.message;

import com.github.richardjwild.blather.helper.DBHelper;
import com.github.richardjwild.blather.persistence.JdbcMessageRepository;
import com.github.richardjwild.blather.persistence.dao.MessageDAO;
import com.github.richardjwild.blather.user.User;
import org.junit.After;
import org.junit.Test;

import java.sql.SQLException;
import java.time.Instant;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.stream.Stream;

import static java.util.stream.Collectors.toList;
import static org.fest.assertions.Assertions.assertThat;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;

public class JdbcMessageRepositoryShould {

    private static final User RECIPIENT_1 = new User("Isimbi");
    private static final User RECIPIENT_2 = new User("Benjamin");
    private static final String TEXT_1 = "message text 1";
    private static final String TEXT_2 = "message text 2";
    private static final String TEXT_3 = "message text 3";
    private static final Instant TIMESTAMP_1 = Instant.now();
    private static final Instant TIMESTAMP_2 = TIMESTAMP_1.plusMillis(1);
    private static final Instant TIMESTAMP_3 = TIMESTAMP_2.plusMillis(1);
    private static final Message MESSAGE_1_FOR_RECIPIENT_1 = new Message(RECIPIENT_1, TEXT_1, TIMESTAMP_1);
    private static final Message MESSAGE_2_FOR_RECIPIENT_1 = new Message(RECIPIENT_1, TEXT_2, TIMESTAMP_2);
    private static final Message MESSAGE_1_FOR_RECIPIENT_2 = new Message(RECIPIENT_2, TEXT_3, TIMESTAMP_3);

    private MessageDAO messageDAO = mock(MessageDAO.class);
    private MessageRepository messageRepository = new JdbcMessageRepository(messageDAO);

    @Test
    public void store_all_messages_posted_to_a_recipient() throws SQLException {
        given(messageDAO.findBy(RECIPIENT_1.name())).willReturn(
                Arrays.asList(MESSAGE_1_FOR_RECIPIENT_1, MESSAGE_2_FOR_RECIPIENT_1));

        messageRepository.postMessage(RECIPIENT_1, MESSAGE_1_FOR_RECIPIENT_1);
        messageRepository.postMessage(RECIPIENT_1, MESSAGE_2_FOR_RECIPIENT_1);

        Stream<Message> actualMessages = messageRepository.allMessagesPostedTo(RECIPIENT_1);

        assertThat(list(actualMessages)).containsExactly(MESSAGE_1_FOR_RECIPIENT_1, MESSAGE_2_FOR_RECIPIENT_1);
    }

    @Test
    public void retrieve_messages_posted_to_a_recipient() throws SQLException {
        given(messageDAO.findBy(RECIPIENT_1.name())).willReturn(
                Collections.singletonList(MESSAGE_1_FOR_RECIPIENT_1));
        messageRepository.postMessage(RECIPIENT_1, MESSAGE_1_FOR_RECIPIENT_1);

        given(messageDAO.findBy(RECIPIENT_2.name())).willReturn(
                Collections.singletonList(MESSAGE_1_FOR_RECIPIENT_2));
        messageRepository.postMessage(RECIPIENT_2, MESSAGE_1_FOR_RECIPIENT_2);

        Stream<Message> messagesForRecipient1 = messageRepository.allMessagesPostedTo(RECIPIENT_1);
        Stream<Message> messagesForRecipient2 = messageRepository.allMessagesPostedTo(RECIPIENT_2);

        verify(messageDAO).save(MESSAGE_1_FOR_RECIPIENT_1);
        verify(messageDAO).save(MESSAGE_1_FOR_RECIPIENT_2);

        assertThat(list(messagesForRecipient1)).containsExactly(MESSAGE_1_FOR_RECIPIENT_1);
        assertThat(list(messagesForRecipient2)).containsExactly(MESSAGE_1_FOR_RECIPIENT_2);
    }

    @Test
    public void return_empty_collection_when_no_messages_posted_to_recipient() {
        messageDAO = new MessageDAO(DBHelper.getConnection());
        messageRepository = new JdbcMessageRepository(messageDAO);

        Stream<Message> actualMessages = messageRepository.allMessagesPostedTo(RECIPIENT_2);

        assertThat(list(actualMessages)).isEmpty();
    }

    @Test
    public void retrieve_message_posted_to_recipient() {
        messageDAO = new MessageDAO(DBHelper.getConnection());
        messageRepository = new JdbcMessageRepository(messageDAO);
        messageRepository.postMessage(RECIPIENT_1, MESSAGE_1_FOR_RECIPIENT_1);
        
        Stream<Message> actualMesasges = messageRepository.allMessagesPostedTo(RECIPIENT_1);

        assertThat(list(actualMesasges)).contains(MESSAGE_1_FOR_RECIPIENT_1);
    }

    @After
    public void tearDown() {
        DBHelper.clearConnection();
    }

    private List<Message> list(Stream<Message> messageStream) {
        return messageStream.collect(toList());
    }
}
