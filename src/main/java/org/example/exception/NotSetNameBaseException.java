package org.example.exception;

public class NotSetNameBaseException extends RuntimeException {
    private String notSetNameBaseMessage;
    public NotSetNameBaseException(String message) {
        this.notSetNameBaseMessage = message;
    }
}
