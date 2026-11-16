package com.example.order.processing.model;

import lombok.Builder;
import lombok.Value;

import java.math.BigDecimal;

@Value
@Builder
public class Clothing implements Product {
    String name;
    BigDecimal price;
    String size;
    String material;
    String color;
}
