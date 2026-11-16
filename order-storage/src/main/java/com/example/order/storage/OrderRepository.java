package com.example.order.storage;

import com.example.order.processing.model.Order;
import com.example.order.processing.model.Product;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

public class OrderRepository<T extends Product> {
    private final Map<UUID, Order<? extends T>> orders = new ConcurrentHashMap<>();

    public void save(Order<? extends T> order) {
        orders.put(order.id(), order);
    }

    public Optional<Order<? extends T>> findById(UUID id) {
        return Optional.ofNullable(orders.get(id));
    }

    public List<Order<? extends T>> findAll() {
        return List.copyOf(orders.values());
    }

    @SuppressWarnings("unchecked")
    public <S extends T> List<Order<S>> findByType(Class<S> type) {
        return orders.values().stream()
                .filter(order -> type.isInstance(order.product()))
                .map(order -> (Order<S>) order)
                .toList();
    }

    public int size() {
        return orders.size();
    }
}
