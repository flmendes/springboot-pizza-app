package com.mendes.example.order.infrastructure;

import com.mendes.example.order.domain.Order;
import com.mendes.example.order.domain.OrderStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, UUID> {
    List<Order> findByCustomerId(UUID customerId);
    List<Order> findByStatus(OrderStatus status);
    List<Order> findByCustomerIdAndStatus(UUID customerId, OrderStatus status);
    List<Order> findByCreatedAtBetween(LocalDateTime startDate, LocalDateTime endDate);
}

