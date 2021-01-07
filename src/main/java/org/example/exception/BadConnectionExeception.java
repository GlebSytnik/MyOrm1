package org.example.exception;



public class BadConnectionExeception extends Exception {
    private String badConnection;
    public BadConnectionExeception(String message) {
       this.badConnection = message;
    }
}
