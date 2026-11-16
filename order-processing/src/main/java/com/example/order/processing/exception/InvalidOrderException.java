package com.example.order.processing.exception;

public class InvalidOrderException extends OrderProcessingException {
    public InvalidOrderException(String message) {
        super(message);
    }
}
