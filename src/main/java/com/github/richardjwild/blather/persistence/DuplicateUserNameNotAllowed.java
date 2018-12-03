package com.github.richardjwild.blather.persistence;

class DuplicateUserNameNotAllowed extends RuntimeException {
    private String message;

    DuplicateUserNameNotAllowed(String message) {
        this.message = message;
    }
}
