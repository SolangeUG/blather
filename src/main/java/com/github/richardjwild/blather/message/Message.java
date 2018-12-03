package com.github.richardjwild.blather.message;

import com.github.richardjwild.blather.time.TimestampFormatter;
import com.github.richardjwild.blather.user.User;

import java.time.Instant;
import java.util.StringJoiner;

import static org.apache.commons.lang3.builder.EqualsBuilder.reflectionEquals;
import static org.apache.commons.lang3.builder.HashCodeBuilder.reflectionHashCode;

public class Message implements Comparable<Message> {

    private final User recipient;
    private final String text;
    private final Instant timestamp;

    public Message(User recipient, String text, Instant timestamp) {
        this.recipient = recipient;
        this.text = text;
        this.timestamp = timestamp;
    }

    public String formatRead(TimestampFormatter timestampFormatter) {
        return new StringJoiner(" ")
                .add(text)
                .add(timestampFormatter.format(timestamp))
                .toString();
    }

    public String formatWall(TimestampFormatter timestampFormatter) {
        return new StringJoiner(" - ")
                .add(recipient.name())
                .add(formatRead(timestampFormatter))
                .toString();
    }

    @Override
    public int compareTo(Message other) {
        return timestamp.compareTo(other.timestamp);
    }

    @Override
    public boolean equals(Object o) {
        return reflectionEquals(this, o);
    }

    @Override
    public int hashCode() {
        return reflectionHashCode(this);
    }

    public User recipient() {
        return recipient;
    }

    public String text() {
        return text;
    }

    public Instant timestamp() {
        return timestamp;
    }
}
