package com.github.richardjwild.blather.persistence;

public class DuplicateUserNameNotAllowed extends RuntimeException {
    private String message;

    public DuplicateUserNameNotAllowed(String message) {

        this.message = message;
    }
}
