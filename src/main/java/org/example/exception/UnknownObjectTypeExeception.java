package org.example.exception;

public class UnknownObjectTypeExeception extends  RuntimeException {
    private String unknownObjectMessage;
    public UnknownObjectTypeExeception(String message) {
        this.unknownObjectMessage = message;
    }
}
