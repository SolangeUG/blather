package com.github.richardjwild.blather.application;

import com.github.richardjwild.blather.command.factory.*;
import com.github.richardjwild.blather.datatransfer.MessageRepository;
import com.github.richardjwild.blather.datatransfer.UserRepository;
import com.github.richardjwild.blather.io.Input;
import com.github.richardjwild.blather.io.Output;
import com.github.richardjwild.blather.messageformatting.TimestampFormatter;
import com.github.richardjwild.blather.parsing.CommandReader;
import com.github.richardjwild.blather.parsing.InputParser;
import com.github.richardjwild.blather.time.Clock;

public class ApplicationBuilder {

    public static Application build(Input input, Output output, Clock clock) {

        UserRepository userRepository = new UserRepository();
        MessageRepository messageRepository = new MessageRepository();

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