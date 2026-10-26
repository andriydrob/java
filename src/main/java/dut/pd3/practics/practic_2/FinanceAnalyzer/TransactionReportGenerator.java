package dut.pd3.practics.practic_2.FinanceAnalyzer;

import java.util.List;
import java.util.Map;
import java.util.Optional;


public abstract class TransactionReportGenerator {

    private TransactionReportGenerator() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static void printBalanceReport(double totalBalance) {
        System.out.printf("Загальний баланс: %.2f грн%n", totalBalance);
    }


    public static void printTransactionsCountByMonth(String monthYear, int count) {
        System.out.printf("Кількість транзакцій за %s: %d%n", monthYear, count);
    }

    public static void printTopExpensesReport(List<Transaction> topExpenses) {
        System.out.println("\n=== 10 найбільших витрат ===");
        int rank = 1;
        for (Transaction expense : topExpenses) {
            System.out.printf("%2d. %-30s %.2f грн%n",
                    rank++,
                    expense.getDescription(),
                    expense.getAmount());
        }
    }

    public static void printPeriodExpensesReport(Optional<Transaction> maxExpense,
                                                 Optional<Transaction> minExpense,
                                                 String monthYear) {
        System.out.printf("%n=== Аналіз витрат за %s ===%n", monthYear);
        maxExpense.ifPresentOrElse(
                t -> System.out.printf("Найбільша витрата: %-30s %.2f грн%n",
                        t.getDescription(), Math.abs(t.getAmount())),
                () -> System.out.println("Найбільша витрата: немає даних")
        );
        minExpense.ifPresentOrElse(
                t -> System.out.printf("Найменша витрата:  %-30s %.2f грн%n",
                        t.getDescription(), Math.abs(t.getAmount())),
                () -> System.out.println("Найменша витрата:  немає даних")
        );
    }

    public static void printCategoryExpensesReport(
            Map<String, Double> categoryExpenses) {
        System.out.println("\n=== Витрати по категоріях (1 * = 1 000 грн) ===");
        categoryExpenses.entrySet().stream()
                .sorted(Map.Entry.<String, Double>comparingByValue().reversed())
                .forEach(entry -> {
                    int stars = (int) (entry.getValue() / 1000);
                    System.out.printf("%-30s | %-15s %.0f грн%n",
                            entry.getKey(),
                            "*".repeat(Math.max(stars, 1)),
                            entry.getValue());
                });
    }

    public static void printMonthlyExpensesReport(
            Map<String, Double> monthlyExpenses) {
        System.out.println("\n=== Витрати по місяцях (1 * = 1 000 грн) ===");
        monthlyExpenses.forEach((month, total) -> {
            int stars = (int) (total / 1000);
            System.out.printf("%s | %-20s %.0f грн%n",
                    month,
                    "*".repeat(Math.max(stars, 1)),
                    total);
        });
    }
}
