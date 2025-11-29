package com.mendes.example.order.domain;

public enum OrderStatus {
    PENDING("Pendente"),
    CONFIRMED("Confirmado"),
    PREPARING("Em Preparação"),
    READY("Pronto para Entrega"),
    IN_DELIVERY("Em Entrega"),
    DELIVERED("Entregue"),
    CANCELLED("Cancelado");

    private final String description;

    OrderStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}

