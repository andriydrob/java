package dut.pd3.practics.practic_2.FinanceAnalyzer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URI;
import java.util.ArrayList;
import java.util.List;

public abstract class TransactionCSVReader {

    private TransactionCSVReader() {
        throw new UnsupportedOperationException("Utility class");
    }

    public static List<String> readRawLines(String filePath) {
        List<String> lines = new ArrayList<>();
        try {
            try (BufferedReader br = new BufferedReader(
                    new InputStreamReader(
                            URI.create(filePath).toURL().openStream(), "UTF-8"))) {
                String line;
                while ((line = br.readLine()) != null) {
                    if (!line.isBlank()) {
                        lines.add(line);
                    }
                }
            }
        } catch (IOException e) {
            System.err.println("Помилка читання файлу: " + e.getMessage());
        }
        return lines;
    }

    public static List<Transaction> parseTransactions(List<String> lines) {
        List<Transaction> transactions = new ArrayList<>();
        for (String line : lines) {
            String[] values = line.split(",", 3);
            if (values.length < 3) continue;
            try {
                Transaction transaction = new Transaction(
                        values[0].trim(),
                        Double.parseDouble(values[1].trim()),
                        values[2].trim()
                );
                transactions.add(transaction);
            } catch (NumberFormatException e) {
            }
        }
        return transactions;
    }

    public static List<Transaction> readTransactions(String filePath) {
        List<String> rawLines = readRawLines(filePath);
        return parseTransactions(rawLines);
    }
}
