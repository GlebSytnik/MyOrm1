package org.example.exception;

public class NotListValueBaseException extends RuntimeException {
    private String notListValueBaseMessage;
    public NotListValueBaseException(String message) {
        this.notListValueBaseMessage = message;
    }

}
