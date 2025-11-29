package com.mendes.example.pizza.application.dto;

import com.mendes.example.pizza.domain.PizzaSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * DTO para requisição de criação de Pizza.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CreatePizzaRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private PizzaSize size;
}
