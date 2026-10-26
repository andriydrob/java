package dut.pd3.practics.practic_2.FinanceAnalyzer;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

class TransactionAnalyzerTest {

    private List<Transaction> testTransactions;

    @BeforeEach
    void setUp() {
        testTransactions = Arrays.asList(
                new Transaction("01-02-2023",  100.0, "Зарплата"),
                new Transaction("02-02-2023",  -50.0, "Кафе"),
                new Transaction("03-02-2023",  150.0, "Фріланс"),
                new Transaction("01-03-2023", -200.0, "Оренда"),
                new Transaction("15-03-2023", -300.0, "Продукти"),
                new Transaction("20-03-2023", -100.0, "Транспорт"),
                new Transaction("01-04-2023", -500.0, "Ресторан"),
                new Transaction("02-04-2023", -400.0, "Одяг"),
                new Transaction("03-04-2023", -350.0, "Електроніка"),
                new Transaction("04-04-2023", -250.0, "Спортзал"),
                new Transaction("05-04-2023", -150.0, "Кіно"),
                new Transaction("06-04-2023", -120.0, "Книги"),
                new Transaction("07-04-2023",  -90.0, "Паркінг"),
                new Transaction("08-04-2023",  -80.0, "Аптека"),
                new Transaction("09-04-2023",  -60.0, "Кав'ярня"),
                new Transaction("10-04-2023",  -40.0, "Газета")
        );
    }

    @Test
    void testCalculateTotalBalance() {
        double result = TransactionAnalyzer.calculateTotalBalance(testTransactions);
        Assertions.assertEquals(-2440.0, result, 0.001,
                "Розрахунок загального балансу неправильний");
    }

    @Test
    void testCalculateTotalBalanceEmpty() {
        double result = TransactionAnalyzer.calculateTotalBalance(List.of());
        Assertions.assertEquals(0.0, result, 0.001,
                "Баланс порожнього списку має бути 0");
    }

    @Test
    void testCountTransactionsByMonth() {
        int countFeb = TransactionAnalyzer.countTransactionsByMonth(
                testTransactions, "02-2023");
        int countMar = TransactionAnalyzer.countTransactionsByMonth(
                testTransactions, "03-2023");

        Assertions.assertEquals(3, countFeb,
                "Кількість транзакцій за лютий неправильна");
        Assertions.assertEquals(3, countMar,
                "Кількість транзакцій за березень неправильна");
    }

    @Test
    void testCountTransactionsByMonthNoResults() {
        int count = TransactionAnalyzer.countTransactionsByMonth(
                testTransactions, "06-2023");
        Assertions.assertEquals(0, count,
                "Місяць без транзакцій має повертати 0");
    }

    @Test
    void testFindTopExpenses() {
        List<Transaction> top = TransactionAnalyzer.findTopExpenses(testTransactions);

        Assertions.assertNotNull(top, "Список топ-витрат не повинен бути null");
        Assertions.assertTrue(top.size() <= 10,
                "Список повинен містити не більше 10 елементів");
        Assertions.assertTrue(top.stream().allMatch(t -> t.getAmount() < 0),
                "Усі елементи мають бути витратами (від'ємні значення)");
        // Найбільша витрата перша
        Assertions.assertEquals(-500.0, top.get(0).getAmount(), 0.001,
                "Перша витрата повинна бути найбільшою (-500)");
        // Відсортовано за зростанням
        for (int i = 0; i < top.size() - 1; i++) {
            Assertions.assertTrue(
                    top.get(i).getAmount() <= top.get(i + 1).getAmount(),
                    "Витрати мають бути відсортовані за зростанням суми");
        }
    }

    @Test
    void testFindTopExpensesLimitedToTen() {
        List<Transaction> top = TransactionAnalyzer.findTopExpenses(testTransactions);
        Assertions.assertEquals(10, top.size(),
                "Метод повинен повертати рівно 10 елементів, якщо витрат більше 10");
    }

    @Test
    void testFindMaxExpenseByPeriod() {
        Optional<Transaction> max =
                TransactionAnalyzer.findMaxExpenseByPeriod(testTransactions, "03-2023");

        Assertions.assertTrue(max.isPresent(), "Найбільша витрата повинна бути знайдена");
        Assertions.assertEquals(-300.0, max.get().getAmount(), 0.001,
                "Найбільша витрата за березень — -300 грн");
        Assertions.assertEquals("Продукти", max.get().getDescription());
    }

    @Test
    void testFindMinExpenseByPeriod() {
        Optional<Transaction> min =
                TransactionAnalyzer.findMinExpenseByPeriod(testTransactions, "03-2023");

        Assertions.assertTrue(min.isPresent(), "Найменша витрата повинна бути знайдена");
        Assertions.assertEquals(-100.0, min.get().getAmount(), 0.001,
                "Найменша витрата за березень — -100 грн");
        Assertions.assertEquals("Транспорт", min.get().getDescription());
    }

    @Test
    void testFindExpenseByPeriodEmpty() {
        Optional<Transaction> max =
                TransactionAnalyzer.findMaxExpenseByPeriod(testTransactions, "07-2023");
        Assertions.assertFalse(max.isPresent(),
                "Для місяця без витрат має повертати Optional.empty()");
    }

    @Test
    void testGetSpendingByCategory() {
        Map<String, Double> spending =
                TransactionAnalyzer.getSpendingByCategory(testTransactions);

        Assertions.assertTrue(spending.containsKey("Кафе"),
                "Категорія 'Кафе' повинна бути присутня");
        Assertions.assertEquals(50.0, spending.get("Кафе"), 0.001,
                "Сума витрат по категорії 'Кафе' неправильна");
        Assertions.assertFalse(spending.containsKey("Зарплата"),
                "Доходи не повинні бути в звіті витрат");
    }

    @Test
    @Disabled("Інтеграційний тест — потребує доступу до informer.com.ua")
    void testReadTransactionsFromUrl() {
        List<Transaction> transactions = TransactionCSVReader.readTransactions(
                "https://informer.com.ua/dut/java/pr2.csv");

        Assertions.assertNotNull(transactions, "Список транзакцій не повинен бути null");
        Assertions.assertFalse(transactions.isEmpty(),
                "CSV-файл повинен містити транзакції");
        transactions.forEach(t -> {
            Assertions.assertNotNull(t.getDate(), "Дата не може бути null");
            Assertions.assertNotNull(t.getDescription(), "Опис не може бути null");
        });
    }

    @Test
    void testParseTransactionsFromCsvLines() {
        List<String> lines = Arrays.asList(
                "05-12-2023,-450,Сільпо",
                "07-12-2023,-120,Аптека",
                "10-12-2023,8000,Зарплата",
                "Дата,Сума,Категорія"
        );

        List<Transaction> transactions =
                TransactionCSVReader.parseTransactions(lines);

        Assertions.assertEquals(3, transactions.size(),
                "Рядок-заголовок повинен бути пропущений");
        Assertions.assertEquals("05-12-2023", transactions.get(0).getDate());
        Assertions.assertEquals(-450.0, transactions.get(0).getAmount(), 0.001);
        Assertions.assertEquals("Сільпо", transactions.get(0).getDescription());
        Assertions.assertEquals(8000.0, transactions.get(2).getAmount(), 0.001);
    }
}
