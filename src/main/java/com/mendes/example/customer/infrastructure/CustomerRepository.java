package com.mendes.example.customer.infrastructure;

import com.mendes.example.customer.domain.Customer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

/**
 * Repositório JPA para acesso a dados de {@link Customer}.
 */
@Repository
public interface CustomerRepository extends JpaRepository<Customer, Long> {
    /**
     * Busca um cliente por e-mail.
     */
    Optional<Customer> findByEmail(String email);

    /**
     * Verifica se um e-mail já está cadastrado.
     */
    boolean existsByEmail(String email);
}
