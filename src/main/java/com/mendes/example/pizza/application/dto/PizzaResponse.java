package com.mendes.example.pizza.application.dto;

import com.mendes.example.pizza.domain.PizzaSize;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * DTO de resposta para operações com Pizza.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PizzaResponse {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private PizzaSize size;
    private Boolean available;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
