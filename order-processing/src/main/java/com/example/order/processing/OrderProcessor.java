package com.example.order.processing;

import com.example.order.processing.exception.OrderTypeMismatchException;
import com.example.order.processing.internal.PriceCalculator;
import com.example.order.processing.model.Order;
import com.example.order.processing.model.Product;

import java.math.BigDecimal;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

public class OrderProcessor<T extends Product> {
    private final Class<T> type;
    private final AtomicInteger processed = new AtomicInteger();

    public OrderProcessor(Class<T> type) {
        this.type = Objects.requireNonNull(type, "type");
    }

    public boolean supports(Order<?> order) {
        return type.isInstance(order.product());
    }

    @SuppressWarnings("unchecked") 
    public Order<T> accept(Order<?> order) {
        if (!supports(order)) {
            throw new OrderTypeMismatchException(type, order.product().getClass());
        }
        return (Order<T>) order;
    }

    public void processOrder(Order<? extends T> order) {
        BigDecimal total = PriceCalculator.total(order.product().getPrice(), order.quantity());
        processed.incrementAndGet();
        System.out.printf("[%s] %s: %s x%d = %s%n", Thread.currentThread().getName(),
                type.getSimpleName(), order.product().getName(), order.quantity(), total);
    }

    public int getProcessedCount() {
        return processed.get();
    }
}
