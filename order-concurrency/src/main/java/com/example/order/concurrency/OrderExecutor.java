package com.example.order.concurrency;

import com.example.order.processing.model.Order;
import com.example.order.processing.model.Product;

import java.util.Collection;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.Consumer;

public class OrderExecutor implements AutoCloseable {
    private final ExecutorService pool;

    public OrderExecutor(int threads) {
        AtomicInteger counter = new AtomicInteger();
        pool = Executors.newFixedThreadPool(threads, r -> new Thread(r, "order-worker-" + counter.incrementAndGet()));
    }

    public <O extends Order<? extends Product>> int processAll(Collection<? extends O> orders, Consumer<? super O> action)
            throws InterruptedException {
        List<Callable<Void>> tasks = orders.stream()
                .<Callable<Void>>map(order -> () -> {
                    action.accept(order);
                    return null;
                })
                .toList();

        int failures = 0;
        for (Future<Void> future : pool.invokeAll(tasks)) {
            try {
                future.get();
            } catch (ExecutionException e) {
                failures++;
                System.err.println("Order failed: " + e.getCause().getMessage());
            }
        }
        return failures;
    }

    @Override
    public void close() {
        pool.shutdown();
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            pool.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }
}
