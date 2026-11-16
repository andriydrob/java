package com.example.order.concurrency.app;

import com.example.order.concurrency.OrderExecutor;
import com.example.order.processing.OrderProcessor;
import com.example.order.processing.exception.InvalidOrderException;
import com.example.order.processing.exception.OrderTypeMismatchException;
import com.example.order.processing.model.Clothing;
import com.example.order.processing.model.Electronics;
import com.example.order.processing.model.Order;
import com.example.order.processing.model.Product;
import com.example.order.storage.OrderRepository;
import com.github.javafaker.Faker;

import java.math.BigDecimal;
import java.util.List;
import java.util.Locale;
import java.util.stream.Stream;


public class Main {
    private static final int ORDERS_PER_TYPE = 5;
    private static final Faker FAKER = new Faker(Locale.ENGLISH);

    public static void main(String[] args) throws InterruptedException {
        List<Order<Electronics>> electronics = generate(Main::randomElectronics);
        List<Order<Clothing>> clothing = generate(Main::randomClothing);

        OrderRepository<Product> repository = new OrderRepository<>();
        OrderProcessor<Electronics> electronicsProcessor = new OrderProcessor<>(Electronics.class);
        OrderProcessor<Clothing> clothingProcessor = new OrderProcessor<>(Clothing.class);

        try (OrderExecutor executor = new OrderExecutor(4)) {
            System.out.println("=== Saving orders concurrently ===");
            executor.processAll(electronics, repository::save);
            executor.processAll(clothing, repository::save);
            check(repository.size() == 2 * ORDERS_PER_TYPE, "all orders stored");

            System.out.println("=== Processing orders filtered by type ===");
            executor.processAll(repository.findByType(Electronics.class), electronicsProcessor::processOrder);
            executor.processAll(repository.findByType(Clothing.class), clothingProcessor::processOrder);

            System.out.println("=== Orders over 1000 (lambda filter) ===");
            repository.findAll().stream()
                    .filter(order -> order.product().getPrice().compareTo(BigDecimal.valueOf(1000)) > 0)
                    .map(order -> order.product().getName() + " - " + order.product().getPrice())
                    .forEach(System.out::println);

            Runnable report = () -> System.out.println("Report from thread " + Thread.currentThread().getName()
                    + ": " + repository.size() + " orders stored");
            Thread reportThread = new Thread(report, "report-thread");
            reportThread.start();
            reportThread.join();

            System.out.println("=== Generic exception handling ===");
            Order<?> unknown = clothing.get(0);
            try {
                electronicsProcessor.accept(unknown);
            } catch (OrderTypeMismatchException e) {
                System.out.println("Caught: " + e.getMessage());
            }
            try {
                Order.of(randomElectronics(), 0);
            } catch (InvalidOrderException e) {
                System.out.println("Caught: " + e.getMessage());
            }

            int failures = executor.processAll(repository.findAll(),
                    order -> electronicsProcessor.processOrder(electronicsProcessor.accept(order)));
            check(failures == ORDERS_PER_TYPE, "clothing orders rejected by electronics processor");
        }

        check(electronicsProcessor.getProcessedCount() == 2 * ORDERS_PER_TYPE, "electronics processed count");
        check(clothingProcessor.getProcessedCount() == ORDERS_PER_TYPE, "clothing processed count");
        System.out.println("All checks passed.");
    }

    private static <T extends Product> List<Order<T>> generate(java.util.function.Supplier<T> productSupplier) {
        return Stream.generate(productSupplier)
                .limit(ORDERS_PER_TYPE)
                .map(product -> Order.of(product, FAKER.number().numberBetween(1, 5)))
                .toList();
    }

    private static Electronics randomElectronics() {
        return Electronics.builder()
                .name(FAKER.commerce().productName())
                .price(randomPrice())
                .brand(FAKER.company().name())
                .warrantyMonths(FAKER.number().numberBetween(6, 36))
                .build();
    }

    private static Clothing randomClothing() {
        return Clothing.builder()
                .name(FAKER.commerce().productName())
                .price(randomPrice())
                .size(FAKER.options().option("XS", "S", "M", "L", "XL"))
                .material(FAKER.commerce().material())
                .color(FAKER.color().name())
                .build();
    }

    private static BigDecimal randomPrice() {
        return BigDecimal.valueOf(FAKER.number().randomDouble(2, 10, 3000));
    }

    private static void check(boolean condition, String what) {
        if (!condition) throw new IllegalStateException("Check failed: " + what);
    }
}
