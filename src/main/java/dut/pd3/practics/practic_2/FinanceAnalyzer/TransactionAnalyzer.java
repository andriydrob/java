package dut.pd3.practics.practic_2.FinanceAnalyzer;

import java.time.LocalDate;
import java.time.YearMonth;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

public abstract class TransactionAnalyzer {

    private static final DateTimeFormatter DATE_FORMATTER =
            DateTimeFormatter.ofPattern("dd-MM-yyyy");

    private static final DateTimeFormatter MONTH_FORMATTER =
            DateTimeFormatter.ofPattern("MM-yyyy");

    private TransactionAnalyzer() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static double calculateTotalBalance(List<Transaction> transactions) {
        double balance = 0;
        for (Transaction transaction : transactions) {
            balance += transaction.getAmount();
        }
        return balance;
    }

    public static int countTransactionsByMonth(List<Transaction> transactions,
                                               String monthYear) {
        int count = 0;
        for (Transaction transaction : transactions) {
            LocalDate date = LocalDate.parse(transaction.getDate(), DATE_FORMATTER);
            String txMonthYear = date.format(MONTH_FORMATTER);
            if (txMonthYear.equals(monthYear)) {
                count++;
            }
        }
        return count;
    }

    public static List<Transaction> findTopExpenses(List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .sorted(Comparator.comparing(Transaction::getAmount))
                .limit(10)
                .collect(Collectors.toList());
    }

    public static Optional<Transaction> findMaxExpenseByPeriod(
            List<Transaction> transactions, String monthYear) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> {
                    LocalDate date = LocalDate.parse(t.getDate(), DATE_FORMATTER);
                    return date.format(MONTH_FORMATTER).equals(monthYear);
                })
                .min(Comparator.comparingDouble(Transaction::getAmount));
    }

    public static Optional<Transaction> findMinExpenseByPeriod(
            List<Transaction> transactions, String monthYear) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .filter(t -> {
                    LocalDate date = LocalDate.parse(t.getDate(), DATE_FORMATTER);
                    return date.format(MONTH_FORMATTER).equals(monthYear);
                })
                .max(Comparator.comparingDouble(Transaction::getAmount));
    }

    public static Map<String, Double> getSpendingByCategory(
            List<Transaction> transactions) {
        return transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        Transaction::getDescription,
                        Collectors.summingDouble(t -> Math.abs(t.getAmount()))
                ));
    }

    public static Map<String, Double> getSpendingByMonth(
            List<Transaction> transactions) {
        Map<String, Double> rawMap = transactions.stream()
                .filter(t -> t.getAmount() < 0)
                .collect(Collectors.groupingBy(
                        t -> LocalDate.parse(t.getDate(), DATE_FORMATTER)
                                .format(MONTH_FORMATTER),
                        Collectors.summingDouble(t -> Math.abs(t.getAmount()))
                ));

        Map<String, Double> sortedMap = new LinkedHashMap<>();
        rawMap.entrySet().stream()
                .sorted(Comparator.comparing(e ->
                        YearMonth.parse(e.getKey(), MONTH_FORMATTER)))
                .forEach(e -> sortedMap.put(e.getKey(), e.getValue()));

        return sortedMap;
    }
}
