package com.mendes.example.order.application;

import com.mendes.example.order.application.dto.CreateOrderRequest;
import com.mendes.example.order.application.dto.OrderItemRequest;
import com.mendes.example.order.application.dto.OrderResponse;
import com.mendes.example.order.application.mapper.OrderItemMapper;
import com.mendes.example.order.application.mapper.OrderMapper;
import com.mendes.example.order.domain.Order;
import com.mendes.example.order.domain.OrderItem;
import com.mendes.example.order.domain.OrderStatus;
import com.mendes.example.order.infrastructure.OrderRepository;
import com.mendes.example.order.infrastructure.OrderItemRepository;
import com.mendes.example.pizza.application.PizzaService;
import com.mendes.example.pizza.domain.Pizza;
import com.mendes.example.shared.exception.ResourceNotFoundException;
import com.mendes.example.shared.exception.InvalidOperationException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final PizzaService pizzaService;
    private final OrderMapper orderMapper;

    @Transactional(readOnly = true)
    public List<OrderResponse> listAllOrders() {
        List<Order> orders = orderRepository.findAll();
        return orderMapper.toResponseList(orders);
    }

    @Transactional(readOnly = true)
    public OrderResponse getOrderById(Long id) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Order not found with id: " + id
                ));
        return orderMapper.toResponse(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByCustomerId(Long customerId) {
        List<Order> orders = orderRepository.findByCustomerId(customerId);
        return orderMapper.toResponseList(orders);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByStatus(OrderStatus status) {
        List<Order> orders = orderRepository.findByStatus(status);
        return orderMapper.toResponseList(orders);
    }

    public OrderResponse createOrder(CreateOrderRequest request) {
        if (request.getItems() == null || request.getItems().isEmpty()) {
            throw new IllegalArgumentException(
                "Order must have at least one item"
            );
        }

        Order order = Order.builder()
                .customerId(request.getCustomerId())
                .notes(request.getNotes())
                .build();

        for (OrderItemRequest itemRequest : request.getItems()) {
            Pizza pizza = pizzaService.getPizzaEntityById(itemRequest.getPizzaId());

            BigDecimal unitPrice = pizza.getPrice();
            BigDecimal totalPrice = unitPrice.multiply(BigDecimal.valueOf(itemRequest.getQuantity()));

            OrderItem item = OrderItem.builder()
                    .pizzaId(pizza.getId())
                    .pizzaName(pizza.getName())
                    .quantity(itemRequest.getQuantity())
                    .unitPrice(unitPrice) // Fix: usar preço da pizza, não do request
                    .totalPrice(totalPrice) // Fix: calcular total price explicitamente
                    .build();

            order.addItem(item);
        }

        order.calculateTotalAmount();
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    public OrderResponse confirmOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Order not found with id: " + orderId
                ));

        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOperationException(
                "Only pending orders can be confirmed. Current status: " + order.getStatus()
            );
        }

        order.setStatus(OrderStatus.CONFIRMED);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    public OrderResponse startPreparing(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Order not found with id: " + orderId
                ));

        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new InvalidOperationException(
                "Only confirmed orders can start preparing. Current status: " + order.getStatus()
            );
        }

        order.setStatus(OrderStatus.PREPARING);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    public OrderResponse markAsReady(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Order not found with id: " + orderId
                ));

        if (order.getStatus() != OrderStatus.PREPARING) {
            throw new InvalidOperationException(
                "Only preparing orders can be marked as ready. Current status: " + order.getStatus()
            );
        }

        order.setStatus(OrderStatus.READY);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    public OrderResponse markAsInDelivery(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Order not found with id: " + orderId
                ));

        if (order.getStatus() != OrderStatus.READY) {
            throw new InvalidOperationException(
                "Only ready orders can be marked as in delivery. Current status: " + order.getStatus()
            );
        }

        order.setStatus(OrderStatus.IN_DELIVERY);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    public OrderResponse markAsDelivered(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Order not found with id: " + orderId
                ));

        if (order.getStatus() != OrderStatus.IN_DELIVERY) {
            throw new InvalidOperationException(
                "Only in delivery orders can be marked as delivered. Current status: " + order.getStatus()
            );
        }

        order.setStatus(OrderStatus.DELIVERED);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    public OrderResponse cancelOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Order not found with id: " + orderId
                ));

        if (order.getStatus() == OrderStatus.DELIVERED || order.getStatus() == OrderStatus.CANCELLED) {
            throw new InvalidOperationException(
                "Cannot cancel delivered or already cancelled orders. Current status: " + order.getStatus()
            );
        }

        order.setStatus(OrderStatus.CANCELLED);
        Order savedOrder = orderRepository.save(order);
        return orderMapper.toResponse(savedOrder);
    }

    public void deleteOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new ResourceNotFoundException(
                    "Order not found with id: " + orderId
                ));
        orderRepository.delete(order);
    }

    @Transactional(readOnly = true)
    public List<OrderResponse> getOrdersByDateRange(LocalDateTime startDate, LocalDateTime endDate) {
        List<Order> orders = orderRepository.findByCreatedAtBetween(startDate, endDate);
        return orderMapper.toResponseList(orders);
    }
}



