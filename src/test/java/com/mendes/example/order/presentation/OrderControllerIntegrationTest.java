package com.mendes.example.order.presentation;

import com.mendes.example.customer.domain.Customer;
import com.mendes.example.customer.infrastructure.CustomerRepository;
import com.mendes.example.order.application.dto.CreateOrderRequest;
import com.mendes.example.order.application.dto.OrderItemRequest;
import com.mendes.example.order.domain.OrderStatus;
import com.mendes.example.order.infrastructure.OrderRepository;
import com.mendes.example.pizza.domain.Pizza;
import com.mendes.example.pizza.domain.PizzaSize;
import com.mendes.example.pizza.infrastructure.PizzaRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.webmvc.test.autoconfigure.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import tools.jackson.databind.ObjectMapper;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Testes Integrados da Slice de Pedidos (Order)
 *
 * Implementação de testes seguindo os ADRs do projeto:
 * - ADR-009: API Versioning (endpoints sem /v1)
 * - ADR-010: H2 Database (@ActiveProfiles("test"))
 * - ADR-004: RFC 9457 Error Handling (respostas padronizadas)
 * - ADR-008: Order Status State Machine (fluxo de estados)
 * - ADR-012: OpenTelemetry (correlação de traces)
 *
 * Esta classe demonstra como testar uma slice completa com:
 * - Criação de clientes
 * - Criação de pizzas
 * - Criação de pedidos
 * - Fluxo de status de pedidos (máquina de estados)
 * - Validações e tratamento de erros (RFC 9457)
 */
@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class OrderControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CustomerRepository customerRepository;

    @Autowired
    private PizzaRepository pizzaRepository;

    @Autowired
    private OrderRepository orderRepository;

    private Customer customer;
    private Pizza pizza1;
    private Pizza pizza2;

    @BeforeEach
    void setUp() {
        // Limpar dados
        orderRepository.deleteAll();
        pizzaRepository.deleteAll();
        customerRepository.deleteAll();

        // Criar cliente de teste
        customer = Customer.builder()
                .name("João Silva")
                .email("joao.silva@test.com")
                .phone("11999999999")
                .address("Rua Teste, 123")
                .zipCode("01234-567")
                .city("São Paulo")
                .state("SP")
                .build();
        customer = customerRepository.save(customer);

        // Criar pizzas de teste
        pizza1 = Pizza.builder()
                .name("Margherita")
                .description("Pizza clássica")
                .price(BigDecimal.valueOf(45.00))
                .size(PizzaSize.MEDIUM)
                .available(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        pizza1 = pizzaRepository.save(pizza1);

        pizza2 = Pizza.builder()
                .name("Pepperoni")
                .description("Pizza com pepperoni")
                .price(BigDecimal.valueOf(50.00))
                .size(PizzaSize.MEDIUM)
                .available(true)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        pizza2 = pizzaRepository.save(pizza2);
    }

    @Test
    void testCreateOrder_Success() throws Exception {
        // Arrange
        List<OrderItemRequest> items = new ArrayList<>();
        items.add(OrderItemRequest.builder()
                .pizzaId(pizza1.getId())
                .quantity(2)
                .build());
        items.add(OrderItemRequest.builder()
                .pizzaId(pizza2.getId())
                .quantity(1)
                .build());

        CreateOrderRequest request = CreateOrderRequest.builder()
                .customerId(customer.getId())
                .items(items)
                .notes("Sem cebola na primeira pizza")
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        // Act & Assert
        // ADR-009: API Versioning - Endpoint sem /v1/ (versionamento via @GetMapping(version="1"))
        // Note: MockMvc não aplica context-path, então usamos /orders (context-path=/api é aplicado em produção)
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.customerId").value(customer.getId()))
                .andExpect(jsonPath("$.status").value(OrderStatus.PENDING.toString()))
                .andExpect(jsonPath("$.totalAmount").value(140.00))
                .andExpect(jsonPath("$.items.length()").value(2))
                .andExpect(jsonPath("$.notes").value("Sem cebola na primeira pizza"));
    }

    @Test
    void testGetAllOrders_Success() throws Exception {
        // Já temos dados de setUp

        // Act & Assert
        mockMvc.perform(get("/orders")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0));  // Ainda nenhum pedido
    }

    @Test
    void testConfirmOrder_Success() throws Exception {
        // Arrange - Criar pedido
        List<OrderItemRequest> items = new ArrayList<>();
        items.add(OrderItemRequest.builder()
                .pizzaId(pizza1.getId())
                .quantity(1)
                .build());

        CreateOrderRequest createRequest = CreateOrderRequest.builder()
                .customerId(customer.getId())
                .items(items)
                .build();

        String createJson = objectMapper.writeValueAsString(createRequest);

        // Criar pedido via API
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated());

        // Obter ID do pedido criado
        Long orderId = orderRepository.findAll().get(0).getId();

        // Act & Assert - Confirmar pedido
        mockMvc.perform(put("/orders/{id}/confirm", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.CONFIRMED.toString()));
    }

    @Test
    void testCompleteOrderFlow_Success() throws Exception {
        // Arrange - Criar pedido
        List<OrderItemRequest> items = new ArrayList<>();
        items.add(OrderItemRequest.builder()
                .pizzaId(pizza1.getId())
                .quantity(1)
                .build());

        CreateOrderRequest createRequest = CreateOrderRequest.builder()
                .customerId(customer.getId())
                .items(items)
                .notes("Entregar rápido")
                .build();

        String createJson = objectMapper.writeValueAsString(createRequest);

        // Criar pedido
        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value(OrderStatus.PENDING.toString()));

        Long orderId = orderRepository.findAll().get(0).getId();

        // Act & Assert - Fluxo completo de status
        // ADR-008: Order Status State Machine - Validar transições de estado

        // 1. PENDING -> CONFIRMED
        mockMvc.perform(put("/orders/{id}/confirm", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.CONFIRMED.toString()));

        // 2. CONFIRMED -> PREPARING
        mockMvc.perform(put("/orders/{id}/start-preparing", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.PREPARING.toString()));

        // 3. PREPARING -> READY
        mockMvc.perform(put("/orders/{id}/mark-ready", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.READY.toString()));

        // 4. READY -> IN_DELIVERY
        mockMvc.perform(put("/orders/{id}/mark-in-delivery", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.IN_DELIVERY.toString()));

        // 5. IN_DELIVERY -> DELIVERED
        mockMvc.perform(put("/orders/{id}/mark-delivered", orderId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.DELIVERED.toString()));
    }

    @Test
    void testGetOrdersByCustomerId_Success() throws Exception {
        // Arrange - Criar pedido
        List<OrderItemRequest> items = new ArrayList<>();
        items.add(OrderItemRequest.builder()
                .pizzaId(pizza1.getId())
                .quantity(1)
                .build());

        CreateOrderRequest createRequest = CreateOrderRequest.builder()
                .customerId(customer.getId())
                .items(items)
                .build();

        String createJson = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated());

        // Act & Assert
        mockMvc.perform(get("/orders/customer/{customerId}", customer.getId())
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].customerId").value(customer.getId()));
    }

    @Test
    void testGetOrdersByStatus_Success() throws Exception {
        // Arrange - Criar pedido via API primeiro
        List<OrderItemRequest> items = new ArrayList<>();
        items.add(OrderItemRequest.builder()
                .pizzaId(pizza1.getId())
                .quantity(1)
                .build());

        CreateOrderRequest createRequest = CreateOrderRequest.builder()
                .customerId(customer.getId())
                .items(items)
                .build();

        String createJson = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated());

        Long orderId = orderRepository.findAll().get(0).getId();

        // Confirmar pedido para ter status CONFIRMED
        mockMvc.perform(put("/orders/{id}/confirm", orderId));

        // Act & Assert - Buscar por status CONFIRMED
        mockMvc.perform(get("/orders/status/{status}", OrderStatus.CONFIRMED)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].status").value(OrderStatus.CONFIRMED.toString()));
    }

    @Test
    void testCancelOrder_Success() throws Exception {
        // Arrange - Criar pedido
        List<OrderItemRequest> items = new ArrayList<>();
        items.add(OrderItemRequest.builder()
                .pizzaId(pizza1.getId())
                .quantity(1)
                .build());

        CreateOrderRequest createRequest = CreateOrderRequest.builder()
                .customerId(customer.getId())
                .items(items)
                .build();

        String createJson = objectMapper.writeValueAsString(createRequest);

        mockMvc.perform(post("/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(createJson))
                .andExpect(status().isCreated());

        Long orderId = orderRepository.findAll().get(0).getId();

        // Act & Assert
        mockMvc.perform(put("/orders/{id}/cancel", orderId)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value(OrderStatus.CANCELLED.toString()));
    }

    @Test
    void testCreateOrder_WithNullItems_ShouldFail() throws Exception {
        // Arrange - RFC 9457: Problem Details for HTTP APIs
        CreateOrderRequest request = CreateOrderRequest.builder()
                .customerId(customer.getId())
                .items(new ArrayList<>())  // Items vazio
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        // Act & Assert - Verificar resposta RFC 9457
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.title").value("Invalid Request"))
                .andExpect(jsonPath("$.detail", containsString("Order must have at least one item")));
    }

    @Test
    void testCreateOrder_WithInvalidPizzaId_ShouldFail() throws Exception {
        // Arrange - RFC 9457: Problem Details for HTTP APIs
        List<OrderItemRequest> items = new ArrayList<>();
        items.add(OrderItemRequest.builder()
                .pizzaId(999L)  // Pizza inexistente
                .quantity(1)
                .build());

        CreateOrderRequest request = CreateOrderRequest.builder()
                .customerId(customer.getId())
                .items(items)
                .build();

        String requestJson = objectMapper.writeValueAsString(request);

        // Act & Assert - Verificar resposta RFC 9457 (404 pois pizza não foi encontrada)
        mockMvc.perform(post("/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestJson))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.title").value("Resource Not Found"))
                .andExpect(jsonPath("$.detail", containsString("Pizza not found")));
    }
}

