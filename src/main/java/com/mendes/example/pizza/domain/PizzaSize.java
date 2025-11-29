package com.mendes.example.pizza.domain;

/**
 * Enumeração dos tamanhos de pizza suportados pela aplicação.
 * Cada tamanho define uma descrição legível ao usuário e a dimensão em centímetros.
 */
public enum PizzaSize {
    SMALL("Pequena", 30),
    MEDIUM("Média", 40),
    LARGE("Grande", 50),
    EXTRA_LARGE("Extra Grande", 60);

    private final String description;
    private final Integer centimeters;

    PizzaSize(String description, Integer centimeters) {
        this.description = description;
        this.centimeters = centimeters;
    }

    /**
     * Retorna a descrição legível do tamanho.
     */
    public String getDescription() {
        return description;
    }

    /**
     * Retorna a dimensão do tamanho em centímetros.
     */
    public Integer getCentimeters() {
        return centimeters;
    }
}
