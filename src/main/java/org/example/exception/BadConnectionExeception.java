package org.example.exception;



public class BadConnectionExeception extends RuntimeException {
    private String badConnection;
    public BadConnectionExeception(String message) {
       this.badConnection = message;
    }
}
