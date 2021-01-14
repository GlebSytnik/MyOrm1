package org.example.exception;

public class NotNameAndTypeFieldException extends  RuntimeException {
    private String nameOrTypeFieldMessage;
    public NotNameAndTypeFieldException(String message) {
        this.nameOrTypeFieldMessage = message;
    }
}
