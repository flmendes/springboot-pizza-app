package com.mendes.example.order.presentation;

import com.mendes.example.order.application.OrderService;
import com.mendes.example.order.application.dto.CreateOrderRequest;
import com.mendes.example.order.application.dto.OrderResponse;
import com.mendes.example.order.domain.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.UUID;


@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;
    private static final DateTimeFormatter DATE_FORMATTER = DateTimeFormatter.ISO_DATE_TIME;

    @GetMapping(version = "1")
    public ResponseEntity<List<OrderResponse>> listAllOrders() {
        List<OrderResponse> responses = orderService.listAllOrders();
        return ResponseEntity.ok(responses);
    }

    @GetMapping(path = "/{id}", version = "1")
    public ResponseEntity<OrderResponse> getOrderById(@PathVariable UUID id) {
        OrderResponse response = orderService.getOrderById(id);
        return ResponseEntity.ok(response);
    }

    @GetMapping(path = "/customer/{customerId}", version = "1")
    public ResponseEntity<List<OrderResponse>> getOrdersByCustomerId(@PathVariable UUID customerId) {
        List<OrderResponse> responses = orderService.getOrdersByCustomerId(customerId);
        return ResponseEntity.ok(responses);
    }

    @GetMapping(path = "/status/{status}", version = "1")
    public ResponseEntity<List<OrderResponse>> getOrdersByStatus(@PathVariable OrderStatus status) {
        List<OrderResponse> responses = orderService.getOrdersByStatus(status);
        return ResponseEntity.ok(responses);
    }

    @PostMapping(version = "1")
    public ResponseEntity<OrderResponse> createOrder(@RequestBody CreateOrderRequest request) {
        OrderResponse response = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping(path = "/{id}/confirm", version = "1")
    public ResponseEntity<OrderResponse> confirmOrder(@PathVariable UUID id) {
        OrderResponse response = orderService.confirmOrder(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}/start-preparing", version = "1")
    public ResponseEntity<OrderResponse> startPreparing(@PathVariable UUID id) {
        OrderResponse response = orderService.startPreparing(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}/mark-ready", version = "1")
    public ResponseEntity<OrderResponse> markAsReady(@PathVariable UUID id) {
        OrderResponse response = orderService.markAsReady(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}/mark-in-delivery", version = "1")
    public ResponseEntity<OrderResponse> markAsInDelivery(@PathVariable UUID id) {
        OrderResponse response = orderService.markAsInDelivery(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}/mark-delivered", version = "1")
    public ResponseEntity<OrderResponse> markAsDelivered(@PathVariable UUID id) {
        OrderResponse response = orderService.markAsDelivered(id);
        return ResponseEntity.ok(response);
    }

    @PutMapping(path = "/{id}/cancel", version = "1")
    public ResponseEntity<OrderResponse> cancelOrder(@PathVariable UUID id) {
        OrderResponse response = orderService.cancelOrder(id);
        return ResponseEntity.ok(response);
    }

    @DeleteMapping(path = "/{id}", version = "1")
    public ResponseEntity<Void> deleteOrder(@PathVariable UUID id) {
        orderService.deleteOrder(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping(path = "/search/date-range", version = "1")
    public ResponseEntity<List<OrderResponse>> getOrdersByDateRange(
            @RequestParam String startDate,
            @RequestParam String endDate) {
        LocalDateTime start = LocalDateTime.parse(startDate, DATE_FORMATTER);
        LocalDateTime end = LocalDateTime.parse(endDate, DATE_FORMATTER);
        List<OrderResponse> responses = orderService.getOrdersByDateRange(start, end);
        return ResponseEntity.ok(responses);
    }
}

