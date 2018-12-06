package com.github.richardjwild.blather.application;

import com.github.richardjwild.blather.io.Output;

public class Application {

    private static final String WELCOME_MESSAGE = "Welcome to Blather";
    private static final String GOODBYE_MESSAGE = "Bye!";

    private final EventLoop eventLoop;
    private final Output output;

    public Application(EventLoop eventLoop, Output output) {
        this.eventLoop = eventLoop;
        this.output = output;
    }

    public void run() {
        output.writeLine(WELCOME_MESSAGE);
        eventLoop.start();
        output.writeLine(GOODBYE_MESSAGE);
    }
}
