package com.mendes.example.pizza.infrastructure;

import com.mendes.example.pizza.domain.Pizza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repositório JPA para acesso a dados de {@link Pizza}.
 */
@Repository
public interface PizzaRepository extends JpaRepository<Pizza, Long> {
    /**
     * Retorna somente as pizzas marcadas como disponíveis.
     */
    List<Pizza> findByAvailableTrue();

    /**
     * Busca pizzas cujo nome contenha o termo informado (case-insensitive).
     */
    List<Pizza> findByNameContainingIgnoreCase(String name);
}
