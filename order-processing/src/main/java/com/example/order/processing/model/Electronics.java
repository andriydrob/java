package com.example.order.processing.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Electronics implements Product {
    String name;
    BigDecimal price;
    String brand;
    int warrantyMonths;
}
