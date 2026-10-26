package dut.pd3.practics.practic_2.FinanceAnalyzer;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class Transaction {
    private String date;
    private double amount;
    private String description;
}
