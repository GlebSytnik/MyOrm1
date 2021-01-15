package org.example.exception;

public class NotFieldException extends  RuntimeException {
    private String notFieldMessage;
    public NotFieldException(String message) {
        this.notFieldMessage = message;
    }
}
