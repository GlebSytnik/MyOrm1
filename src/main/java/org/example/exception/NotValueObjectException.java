package org.example.exception;

public class NotValueObjectException extends RuntimeException {
    private String valueException;
    public NotValueObjectException(String message) {
        this.valueException = message;
    }
}
