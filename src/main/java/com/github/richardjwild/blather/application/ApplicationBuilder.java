package com.github.richardjwild.blather.application;

import com.github.richardjwild.blather.command.factory.*;
import com.github.richardjwild.blather.helper.DatabaseConnection;
import com.github.richardjwild.blather.io.Input;
import com.github.richardjwild.blather.io.Output;
import com.github.richardjwild.blather.message.MessageRepository;
import com.github.richardjwild.blather.parsing.CommandReader;
import com.github.richardjwild.blather.parsing.InputParser;
import com.github.richardjwild.blather.persistence.JdbcMessageRepository;
import com.github.richardjwild.blather.persistence.JdbcUserRepository;
import com.github.richardjwild.blather.persistence.dao.MessageDAO;
import com.github.richardjwild.blather.persistence.dao.UserDAO;
import com.github.richardjwild.blather.time.Clock;
import com.github.richardjwild.blather.time.TimestampFormatter;
import com.github.richardjwild.blather.user.UserRepository;

public class ApplicationBuilder {

    public static Application build(Input input, Output output, Clock clock) {

        UserDAO userDAO = new UserDAO(new DatabaseConnection().getConnection());
        MessageDAO messageDAO = new MessageDAO(new DatabaseConnection().getConnection());

        UserRepository userRepository = new JdbcUserRepository(userDAO);
        // new InMemoryUserRepository();

        MessageRepository messageRepository = new JdbcMessageRepository(messageDAO);
        // new InMemoryMessageRepository();

        InputParser inputParser = new InputParser();
        Controller controller = new Controller();

        TimestampFormatter timestampFormatter = new TimestampFormatter(clock);

        CommandFactories commandFactories = new CommandFactories(
                new FollowCommandFactory(userRepository),
                new PostCommandFactory(messageRepository, userRepository, clock),
                new QuitCommandFactory(controller),
                new ReadCommandFactory(
                        messageRepository,
                        userRepository,
                        timestampFormatter,
                        output),
                new WallCommandFactory(
                        userRepository,
                        messageRepository,
                        timestampFormatter,
                        output));

        CommandReader commandReader = new CommandReader(
                input,
                inputParser,
                commandFactories);

        EventLoop eventLoop = new EventLoop(commandReader, controller);
        return new Application(eventLoop, output);
    }
}
