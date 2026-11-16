package com.example.order.processing.exception;

public class OrderTypeMismatchException extends OrderProcessingException {
    public OrderTypeMismatchException(Class<?> expected, Class<?> actual) {
        super("Expected " + expected.getSimpleName() + " order, got " + actual.getSimpleName());
    }
}
