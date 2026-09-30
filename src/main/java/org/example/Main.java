package org.example;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {

    private static final Scanner scanner = new Scanner(System.in);

    public static void main(String[] args) {
        Category electronics = new Category(1, "Електроніка");
        Category smartphones = new Category(2, "Смартфони");
        Category accessories = new Category(3, "Аксесуари");

        List<Product> catalog = new ArrayList<>();
        catalog.add(new Product(1, "Ноутбук", 19999.99, "Високопродуктивний ноутбук для роботи та ігор", electronics));
        catalog.add(new Product(2, "Смартфон", 12999.50, "Смартфон з великим екраном та високою автономністю", smartphones));
        catalog.add(new Product(3, "Навушники", 2499.00, "Бездротові навушники з шумозаглушенням", accessories));

        Cart cart = new Cart();
        List<Order> orderHistory = new ArrayList<>();

        while (true) {
            printMenu();
            int choice = readInt("Ваш вибір: ");

            switch (choice) {
                case 1:
                    printProducts(catalog);
                    break;
                case 2:
                    addToCart(catalog, cart);
                    break;
                case 3:
                    System.out.println(cart);
                    break;
                case 4:
                    removeFromCart(cart);
                    break;
                case 5:
                    makeOrder(cart, orderHistory);
                    break;
                case 6:
                    printOrderHistory(orderHistory);
                    break;
                case 7:
                    searchProducts(catalog);
                    break;
                case 0:
                    System.out.println("Дякуємо, що використовували наш магазин!");
                    return;
                default:
                    System.out.println("Невідома опція. Спробуйте ще раз.");
            }
        }
    }

    private static void printMenu() {
        System.out.println("\nВиберіть опцію:");
        System.out.println("1 - Переглянути список товарів");
        System.out.println("2 - Додати товар до кошика");
        System.out.println("3 - Переглянути кошик");
        System.out.println("4 - Видалити товар з кошика");
        System.out.println("5 - Зробити замовлення");
        System.out.println("6 - Історія замовлень");
        System.out.println("7 - Пошук товарів");
        System.out.println("0 - Вийти");
    }

    private static void printProducts(List<Product> products) {
        if (products.isEmpty()) {
            System.out.println("Товарів не знайдено.");
            return;
        }
        for (Product product : products) {
            System.out.println(product);
        }
    }

    private static void addToCart(List<Product> catalog, Cart cart) {
        int id = readInt("Введіть ID товару для додавання до кошика: ");
        Product product = findById(catalog, id);
        if (product == null) {
            System.out.println("Товар з таким ID не знайдено.");
        } else {
            cart.addProduct(product);
            System.out.println("Додано до кошика: " + product.getName());
        }
    }

    private static void removeFromCart(Cart cart) {
        if (cart.isEmpty()) {
            System.out.println("Кошик порожній.");
            return;
        }
        int id = readInt("Введіть ID товару для видалення з кошика: ");
        Product product = findById(cart.getProducts(), id);
        if (product != null && cart.removeProduct(product)) {
            System.out.println("Видалено з кошика: " + product.getName());
        } else {
            System.out.println("Такого товару немає в кошику.");
        }
    }

    private static void makeOrder(Cart cart, List<Order> orderHistory) {
        if (cart.isEmpty()) {
            System.out.println("Кошик порожній. Додайте товари перед оформленням замовлення.");
            return;
        }
        Order order = new Order(cart);
        orderHistory.add(order);
        System.out.println("Замовлення оформлено:");
        System.out.println(order);
        cart.clear();
    }

    private static void printOrderHistory(List<Order> orderHistory) {
        if (orderHistory.isEmpty()) {
            System.out.println("Історія замовлень порожня.");
            return;
        }
        for (int i = 0; i < orderHistory.size(); i++) {
            System.out.println("--- Замовлення №" + (i + 1) + " ---");
            System.out.println(orderHistory.get(i));
        }
    }

    private static void searchProducts(List<Product> catalog) {
        System.out.print("Введіть назву товару або категорії: ");
        String query = scanner.nextLine().trim().toLowerCase();

        List<Product> found = new ArrayList<>();
        for (Product product : catalog) {
            boolean byName = product.getName().toLowerCase().contains(query);
            boolean byCategory = product.getCategory().getName().toLowerCase().contains(query);
            if (byName || byCategory) {
                found.add(product);
            }
        }
        printProducts(found);
    }

    private static Product findById(List<Product> products, int id) {
        for (Product product : products) {
            if (product.getId() == id) {
                return product;
            }
        }
        return null;
    }

    private static int readInt(String message) {
        while (true) {
            System.out.print(message);
            String line = scanner.nextLine().trim();
            try {
                return Integer.parseInt(line);
            } catch (NumberFormatException e) {
                System.out.println("Будь ласка, введіть число.");
            }
        }
    }
}