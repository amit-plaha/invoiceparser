package com.diderointerview.exception;

public class PersistenceException extends Exception {

    public PersistenceException(String message) {
        super(message);
    }

    public PersistenceException(String message, Throwable throwable) {
        super(message, throwable);
    }
}
