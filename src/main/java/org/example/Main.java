package org.example;

import org.example.exception.InvalidInputException;

import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Calculator calculator = new Calculator();

        System.out.println("=== Калькулятор ===");
        System.out.println("Доступні операції: +, -, *, /, sqrt");

        double a = 0, b = 0;
        String operation = "";

        try {
            System.out.print("Введіть операцію: ");
            operation = scanner.nextLine().trim();

            if (operation.equals("sqrt")) {
                System.out.print("Введіть число: ");
                a = Double.parseDouble(scanner.nextLine().trim());
            } else {
                System.out.print("Введіть перше число: ");
                a = Double.parseDouble(scanner.nextLine().trim());

                System.out.print("Введіть друге число: ");
                b = Double.parseDouble(scanner.nextLine().trim());
            }

            double result = switch (operation) {
                case "+"    -> calculator.add(a, b);
                case "-"    -> calculator.subtract(a, b);
                case "*"    -> calculator.multiply(a, b);
                case "/"    -> calculator.divide(a, b);
                case "sqrt" -> calculator.sqrt(a);
                default     -> throw new InvalidInputException(
                    "Невідома операція: \"" + operation + "\". Дозволені: +, -, *, /, sqrt"
                );
            };

            if (operation.equals("sqrt")) {
                System.out.printf("Результат: √%.4f = %.4f%n", a, result);
            } else {
                System.out.printf("Результат: %.4f %s %.4f = %.4f%n", a, operation, b, result);
            }

        } catch (NumberFormatException e) {
            System.out.println("[Помилка] Невірний формат числа. Будь ласка, вводьте числові значення.");
        } catch (ArithmeticException e) {
            System.out.println("[Помилка] Арифметична помилка: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.out.println("[Помилка] Некоректні вхідні дані: " + e.getMessage());
        } finally {
            System.out.println("--- Обробку запиту завершено ---");
            scanner.close();
        }
    }
}
