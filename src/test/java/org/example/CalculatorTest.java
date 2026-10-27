package org.example;

import org.example.exception.InvalidInputException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CalculatorTest {

    private Calculator calculator;

    @BeforeEach
    void setUp() {
        calculator = new Calculator();
    }

    @Test
    @DisplayName("Додавання двох додатніх чисел")
    void testAdd_positiveNumbers() {
        assertEquals(7.0, calculator.add(3.0, 4.0), 1e-9);
    }

    @Test
    @DisplayName("Додавання від'ємних чисел")
    void testAdd_negativeNumbers() {
        assertEquals(-5.0, calculator.add(-2.0, -3.0), 1e-9);
    }

    @Test
    @DisplayName("Віднімання — позитивний результат")
    void testSubtract_positiveResult() {
        assertEquals(1.0, calculator.subtract(5.0, 4.0), 1e-9);
    }

    @Test
    @DisplayName("Віднімання — від'ємний результат")
    void testSubtract_negativeResult() {
        assertEquals(-3.0, calculator.subtract(1.0, 4.0), 1e-9);
    }

    @Test
    @DisplayName("Множення двох чисел")
    void testMultiply() {
        assertEquals(12.0, calculator.multiply(3.0, 4.0), 1e-9);
    }

    @Test
    @DisplayName("Множення на нуль дає нуль")
    void testMultiply_byZero() {
        assertEquals(0.0, calculator.multiply(99.0, 0.0), 1e-9);
    }

    @Test
    @DisplayName("Ділення — звичайний випадок")
    void testDivide() {
        assertEquals(2.5, calculator.divide(5.0, 2.0), 1e-9);
    }

    @Test
    @DisplayName("Ділення на нуль — ArithmeticException")
    void testDivide_byZero_throwsArithmeticException() {
        ArithmeticException ex = assertThrows(
            ArithmeticException.class,
            () -> calculator.divide(10.0, 0.0)
        );
        assertTrue(ex.getMessage().contains("нуль"),
            "Повідомлення має згадувати 'нуль'");
    }

    @Test
    @DisplayName("sqrt від'ємного числа — InvalidInputException")
    void testSqrt_negativeNumber_throwsInvalidInputException() {
        InvalidInputException ex = assertThrows(
            InvalidInputException.class,
            () -> calculator.sqrt(-9.0)
        );
        assertTrue(ex.getMessage().contains("-9.0"),
            "Повідомлення повинно містити проблемне значення");
    }

    @Test
    @DisplayName("sqrt нуля дорівнює 0")
    void testSqrt_zero() throws InvalidInputException {
        assertEquals(0.0, calculator.sqrt(0.0), 1e-9);
    }

    @Test
    @DisplayName("sqrt від 9 дорівнює 3")
    void testSqrt_nine() throws InvalidInputException {
        assertEquals(3.0, calculator.sqrt(9.0), 1e-9);
    }

    @Test
    @DisplayName("Ділення: дробовий результат")
    void testDivide_fractionResult() {
        assertEquals(0.3333333333, calculator.divide(1.0, 3.0), 1e-9);
    }

    @Test
    @DisplayName("Додавання дуже великих чисел")
    void testAdd_largeNumbers() {
        assertEquals(Double.MAX_VALUE,
            calculator.add(Double.MAX_VALUE / 2, Double.MAX_VALUE / 2), 1.0);
    }
}
