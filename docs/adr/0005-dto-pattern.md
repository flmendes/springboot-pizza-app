# ADR-005: DTO Pattern para Transferência de Dados

**Status:** Accepted

**Date:** 2024-11-28

**Decision Owner:** API Architect

---

## Context

O projeto necessitava decidir como transferir dados entre as camadas da aplicação:

1. **Expor Entidades Diretamente** - Retornar @Entity nas APIs
2. **DTO Pattern** - Criar classes de transferência de dados
3. **MapStruct** - Usar biblioteca para mapping automático
4. **Hibridizar** - Usar DTOs em alguns lugares apenas

Considerações:
- Segurança (não expor dados internos)
- Performance (serialização)
- Manutenibilidade
- Flexibilidade

## Decision

Adotamos **DTO Pattern** para todas as transferências de dados entre camadas.

Cada slice tem:
- **Request DTOs** - Para receber dados do cliente
- **Response DTOs** - Para enviar dados ao cliente
- **Mapping** - Conversão Entidade ↔️ DTO

## Rationale

### Por que DTO Pattern

1. **Segurança** - Não expor entidades internas
2. **Flexibilidade** - Resposta independente da entidade
3. **Manutenibilidade** - Mudança em entidade não afeta API
4. **Performance** - Apenas dados necessários
5. **Validação** - Validação específica por endpoint
6. **Versioning** - Suportar múltiplas versões de API
7. **Clean API** - Contrato claro com cliente

### Cenários Problemáticos Evitados

```java
// ❌ RUIM - Expor entidade diretamente
@GetMapping("/{id}")
public Order getOrder(@PathVariable Long id) {
    return orderRepository.findById(id).orElse(null);
}

// Problemas:
// - Expõe campos internos
// - Se adicionar @Transient, quebra API
// - Difícil versionar API
// - Exposição de dados sensíveis

// ✅ BOM - Usar DTO
@GetMapping("/{id}")
public OrderResponse getOrder(@PathVariable Long id) {
    Order order = orderRepository.findById(id).orElse(null);
    return OrderResponse.fromEntity(order);
}
```

## Consequences

### Positivas ✅

1. Entidades protegidas de expor dados internos
2. API independente do modelo de dados
3. Fácil adicionar campos na resposta sem quebrar cliente
4. Validação específica por endpoint
5. Suporte a múltiplas versões de API
6. Controle total sobre o que é serializado
7. Performance - apenas campos necessários
8. Manutenção - mudanças isoladas

### Negativas ⚠️

1. Mais código (DTOs)
2. Conversão Entidade ↔️ DTO tem overhead
3. Precisa manter DTOs sincronizados
4. Pode parecer verboso para aplicações simples

## Alternatives Considered

### 1. Expor Entidades Diretamente (Rejeitada)

**Pros:**
- Menos código
- Desenvolvimento mais rápido

**Cons:**
- **Segurança comprometida** - Expõe campos internos
- Acoplamento API ↔️ BD
- Difícil versionar
- Não recomendado para produção

**Decisão:** Rejeitada - Padrão inseguro

### 2. Usar MapStruct (Considerada)

**Exemplo:**
```java
@Mapper(componentModel = "spring")
public interface OrderMapper {
    OrderResponse toResponse(Order order);
    Order toEntity(CreateOrderRequest request);
}
```

**Pros:**
- Mapping automático
- Menos boilerplate
- Compile-time type checking

**Cons:**
- Dependency adicional
- Para projeto pequeno, overkill
- Curva de aprendizado

**Decisão:** Considerada para futuro - Por enquanto, manual é suficiente

### 3. Usar ModelMapper (Considerada)

**Pros:**
- Automático como MapStruct
- Mais flexível

**Cons:**
- Runtime reflection
- Mais lento que MapStruct
- Menos type-safe

**Decisão:** Considerada para futuro - MapStruct seria melhor se necessário

## Implementação

### Estrutura de DTOs

```
order/application/dto/
├── CreateOrderRequest.java
├── OrderItemRequest.java
├── OrderResponse.java
├── OrderItemResponse.java
└── OrderDetailResponse.java
```

### Request DTO

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
public class CreateOrderRequest {
    
    @NotNull(message = "Customer ID is required")
    private Long customerId;
    
    @NotEmpty(message = "Order must have at least one item")
    private List<OrderItemRequest> items;
    
    private String notes;
}
```

### Response DTO

```java
@Data
@Builder
public class OrderResponse {
    
    private Long id;
    private Long customerId;
    private OrderStatus status;
    private BigDecimal totalAmount;
    private String notes;
    private List<OrderItemResponse> items;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    
    public static OrderResponse fromEntity(Order order) {
        return OrderResponse.builder()
            .id(order.getId())
            .customerId(order.getCustomerId())
            .status(order.getStatus())
            .totalAmount(order.getTotalAmount())
            .notes(order.getNotes())
            .items(order.getItems().stream()
                .map(OrderItemResponse::fromEntity)
                .toList()))
            .createdAt(order.getCreatedAt())
            .updatedAt(order.getUpdatedAt())
            .build();
    }
}
```

### Mapping Pattern

```java
// Em Service
public OrderResponse createOrder(CreateOrderRequest request) {
    // Validação
    if (request.getItems() == null || request.getItems().isEmpty()) {
        throw new IllegalArgumentException("Order must have items");
    }
    
    // Criar entidade
    Order order = Order.builder()
        .customerId(request.getCustomerId())
        .notes(request.getNotes())
        .build();
    
    // Adicionar itens
    for (OrderItemRequest itemRequest : request.getItems()) {
        OrderItem item = OrderItem.builder()
            .pizzaId(itemRequest.getPizzaId())
            .quantity(itemRequest.getQuantity())
            .unitPrice(itemRequest.getUnitPrice())
            .build();
        order.addItem(item);
    }
    
    // Calcular total
    order.calculateTotalAmount();
    
    // Persistir
    Order saved = orderRepository.save(order);
    
    // Converter para response
    return OrderResponse.fromEntity(saved);
}
```

## Validação

### Request Validation

```java
// Validação em DTO com anotações
@Data
public class CreateOrderRequest {
    
    @NotNull
    @Positive
    private Long customerId;
    
    @NotEmpty
    @Size(max = 100)
    private List<OrderItemRequest> items;
}

// No Controller
@PostMapping
public ResponseEntity<OrderResponse> create(
        @Valid @RequestBody CreateOrderRequest request) {
    // request já foi validado pelo Spring
    return ResponseEntity.status(201)
        .body(orderService.createOrder(request));
}
```

### Business Validation

```java
// Validação de negócio no Service
public OrderResponse createOrder(CreateOrderRequest request) {
    // Validação de negócio
    if (request.getItems() == null || request.getItems().isEmpty()) {
        throw new IllegalArgumentException("Order must have at least one item");
    }
    
    // ... resto da lógica
}
```

## Versioning Strategy

### URL Versioning

```
/api/v1/orders   - Versão 1
/api/v2/orders   - Versão 2 (futuro)
```

### DTO Versioning

```java
// v1 - Simples
public class OrderResponseV1 {
    private Long id;
    private OrderStatus status;
}

// v2 - Com mais informações
public class OrderResponseV2 {
    private Long id;
    private OrderStatus status;
    private CustomerInfo customer;
    private List<OrderItemDetail> items;
}
```

## Performance Considerations

### Lazy Loading

```java
// ❌ Ruim - N+1 queries
public OrderResponse getOrder(Long id) {
    Order order = orderRepository.findById(id).orElse(null);
    // order.getItems().size() - Query adicional!
    return OrderResponse.fromEntity(order);
}

// ✅ Bom - Eager loading
@Query("SELECT o FROM Order o JOIN FETCH o.items WHERE o.id = ?1")
Order findByIdWithItems(Long id);
```

### Sparse Fieldsets (Futuro)

```
GET /api/v1/orders/1?fields=id,status,total
# Retorna apenas campos solicitados
```

## Security Considerations

### Dados Sensíveis

```java
// ❌ RUIM - Expor dados sensíveis
public class CustomerResponse {
    private String email;
    private String password; // NUNCA!
    private String ssn;      // NUNCA!
}

// ✅ BOM - Apenas dados públicos
public class CustomerResponse {
    private Long id;
    private String name;
    private String email;  // OK se necessário
    // Sem password, SSN, etc
}
```

## Related ADRs

- ADR-001: Vertical Slice Architecture
- ADR-004: RFC 9457 para Error Handling

## References

- [DTO Pattern - Martin Fowler](https://martinfowler.com/eaaCatalog/dataTransferObject.html)
- [Spring Data Transfer Objects](https://spring.io/projects/spring-data-rest)
- [API Design Best Practices](https://swagger.io/resources/articles/best-practices-in-api-design/)

## Decision Log

- **2024-11-28**: Decisão aceita - DTO Pattern para transferência de dados

---

**Status:** ✅ ACCEPTED

**Impact:** Médio - Define padrão de transferência de dados

**Reversibility:** Alta - Fácil refatorar se necessário

