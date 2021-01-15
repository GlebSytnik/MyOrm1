package org.example.exception;

public class UnknownClassException extends  RuntimeException{
    private String unknownClassException;
    public UnknownClassException(String message) {
        this.unknownClassException = message;
    }
}
