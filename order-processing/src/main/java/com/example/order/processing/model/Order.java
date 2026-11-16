package com.example.order.processing.model;

import com.example.order.processing.exception.InvalidOrderException;

import java.util.UUID;

public record Order<T extends Product>(UUID id, T product, int quantity) {

    public Order {
        if (id == null) throw new InvalidOrderException("Order id must not be null");
        if (product == null) throw new InvalidOrderException("Order must contain a product");
        if (quantity <= 0) throw new InvalidOrderException("Quantity must be positive, got " + quantity);
    }

    public static <T extends Product> Order<T> of(T product, int quantity) {
        return new Order<>(UUID.randomUUID(), product, quantity);
    }
}
