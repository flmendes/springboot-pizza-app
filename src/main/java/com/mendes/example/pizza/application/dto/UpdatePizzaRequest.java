package com.mendes.example.pizza.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * DTO para requisição de atualização de Pizza.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UpdatePizzaRequest {
    private String name;
    private String description;
    private java.math.BigDecimal price;
    private com.mendes.example.pizza.domain.PizzaSize size;
}
