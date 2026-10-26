package dut.pd3.practics.practic_2.FinanceAnalyzer;

import java.util.List;
import java.util.Map;
import java.util.Optional;

public class Main {

    private static final String CSV_URL =
            "https://informer.com.ua/dut/java/pr2.csv";

    public static void main(String[] args) {

        //Задача 1: Читання даних з CSV файлу
        List<Transaction> transactions =
                TransactionCSVReader.readTransactions(CSV_URL);

        System.out.println("=== Список транзакцій ===");
        transactions.forEach(System.out::println);
        System.out.println();

        // Задача 2: Загальний баланс
        double totalBalance = TransactionAnalyzer.calculateTotalBalance(transactions);
        TransactionReportGenerator.printBalanceReport(totalBalance);

        //Задача 3: Підрахунок транзакцій за місяць
        String monthYear = "01-2024";
        int count = TransactionAnalyzer.countTransactionsByMonth(transactions, monthYear);
        TransactionReportGenerator.printTransactionsCountByMonth(monthYear, count);

        // Задача 5: 10 найбільших витрат
        List<Transaction> topExpenses = TransactionAnalyzer.findTopExpenses(transactions);
        TransactionReportGenerator.printTopExpensesReport(topExpenses);

        // Самостійна: Найбільша/найменша витрата за грудень 2023
        String analysisPeriod = "12-2023";
        Optional<Transaction> maxExpense =
                TransactionAnalyzer.findMaxExpenseByPeriod(transactions, analysisPeriod);
        Optional<Transaction> minExpense =
                TransactionAnalyzer.findMinExpenseByPeriod(transactions, analysisPeriod);
        TransactionReportGenerator.printPeriodExpensesReport(
                maxExpense, minExpense, analysisPeriod);

        // Самостійна: Витрати по категоріях
        Map<String, Double> categoryExpenses =
                TransactionAnalyzer.getSpendingByCategory(transactions);
        TransactionReportGenerator.printCategoryExpensesReport(categoryExpenses);

        //Самостійна: Витрати по місяцях
        Map<String, Double> monthlyExpenses =
                TransactionAnalyzer.getSpendingByMonth(transactions);
        TransactionReportGenerator.printMonthlyExpensesReport(monthlyExpenses);
    }
}
