package com.example.exceptions;

public class ServerCommunicationError extends RuntimeException {
    public ServerCommunicationError(String message) {
        super(message);
    }
}
