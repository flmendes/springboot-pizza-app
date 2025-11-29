package com.mendes.example.pizza.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Representa a entidade Pizza armazenada na base de dados.
 *
 * Contém atributos básicos como nome, descrição, preço, tamanho e flags de disponibilidade,
 * além de timestamps para criação e atualização.
 *
 * Observações:
 * - Os timestamps são populados automaticamente pelos callbacks JPA {@code @PrePersist} e {@code @PreUpdate}.
 */
@Entity
@Table(name = "pizza")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pizza {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 100)
    private String name;

    @Column(nullable = false, length = 500)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Column(nullable = false, length = 50)
    @Enumerated(EnumType.STRING)
    private PizzaSize size;

    @Column(nullable = false)
    private Boolean available;

    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at")
    private LocalDateTime updatedAt;

    /**
     * Callback JPA executado antes de inserir a entidade.
     * Popula os timestamps {@code createdAt} e {@code updatedAt} com o instante atual.
     */
    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    /**
     * Callback JPA executado antes de atualizar a entidade.
     * Atualiza o timestamp {@code updatedAt} com o instante atual.
     */
    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }
}
