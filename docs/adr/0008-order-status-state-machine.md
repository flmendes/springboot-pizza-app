# ADR-008: State Machine para Status de Pedidos

**Status:** Accepted

**Date:** 2024-11-28

**Decision Owner:** Domain Architect

---

## Context

Pedidos têm diferentes estados (PENDING, CONFIRMED, PREPARING, etc) e transições entre eles têm regras de negócio.

Opções:
1. **Enums Simples** - Apenas usar enum
2. **Validação Manual** - Lógica espalhada no código
3. **State Pattern** - Implementar state pattern
4. **State Machine Framework** - Spring Statemachine ou similar
5. **Domain-Driven Design** - Value Objects com regras

## Decision

Adotamos **State Machine Implícita** usando enums com validação concentrada no Service.

Estados permitidos:
```
PENDING → CONFIRMED → PREPARING → READY → IN_DELIVERY → DELIVERED
                 ↓
             CANCELLED
```

## Rationale

### Por que State Machine

1. **Regras de Negócio** - Estados com transições válidas
2. **Segurança** - Impedir transições inválidas
3. **Claridade** - Diagrama claro de estados
4. **Manutenibilidade** - Fácil adicionar novos estados
5. **Testing** - Fácil testar transições

### Por que NÃO Spring Statemachine

1. **Overkill** - Projeto não é tão complexo
2. **Curve of Learning** - Adiciona complexity
3. **Manual é Simples** - Validação manual é suficiente

## Consequences

### Positivas ✅

1. Transições claras e documentadas
2. Validação de transições
3. Fácil adicionar novos estados
4. Implementação simples
5. Sem dependências extras
6. Testing straightforward

### Negativas ⚠️

1. Validações espalhadas em múltiplos métodos
2. Sem diagrama automático
3. Manual pode ter duplicação

## Alternatives Considered

### 1. Spring Statemachine (Rejeitada)

**Exemplo:**
```java
@Configuration
@EnableStateMachine
public class OrderStateMachineConfig {
    @Bean
    public StateMachineBuilder.Builder<OrderStatus, OrderEvent> 
            builder(StateMachineBuilder.Builder<OrderStatus, OrderEvent> builder) {
        // Complex configuration
    }
}
```

**Pros:**
- Profissional
- Diagrama automático

**Cons:**
- Overkill para este projeto
- Curva de aprendizado
- Dependency adicional

**Decisão:** Rejeitada - Manual é mais simples

### 2. Validação Espalhada (Rejeitada)

```java
// ❌ RUIM - Validação em múltiplos places
if (order.getStatus() == PENDING) {
    // Permitir confirmação
}
```

**Cons:**
- Difícil manter
- Risco de inconsistência
- Sem visão clara de transições

**Decisão:** Rejeitada - Centralizar validação

### 3. State Pattern (Considerada)

**Exemplo:**
```java
public interface OrderState {
    void confirm(Order order);
    void startPreparing(Order order);
}

public class PendingOrderState implements OrderState {
    // ...
}
```

**Pros:**
- Elegante
- Fácil adicionar comportamentos

**Cons:**
- Mais classes
- Overkill para nossa simplicity

**Decisão:** Não usar - Validação manual é suficiente

## Implementation

### Enum de Estados

```java
public enum OrderStatus {
    PENDING("Pending"),
    CONFIRMED("Confirmed"),
    PREPARING("Preparing"),
    READY("Ready"),
    IN_DELIVERY("In Delivery"),
    DELIVERED("Delivered"),
    CANCELLED("Cancelled");
    
    private final String label;
    
    OrderStatus(String label) {
        this.label = label;
    }
}
```

### Transições Válidas

```
PENDING → {
    CONFIRMED ✅
    CANCELLED ✅
}

CONFIRMED → {
    PREPARING ✅
    CANCELLED ✅
}

PREPARING → {
    READY ✅
    CANCELLED ❌ (não permitido)
}

READY → {
    IN_DELIVERY ✅
    CANCELLED ❌ (não permitido)
}

IN_DELIVERY → {
    DELIVERED ✅
    CANCELLED ❌ (não permitido)
}

DELIVERED → {
    Nada ✅ (terminal state)
}

CANCELLED → {
    Nada ✅ (terminal state)
}
```

### Validação no Service

```java
@Service
public class OrderService {
    
    public Order confirmOrder(Long orderId) {
        Order order = getOrderById(orderId);
        
        // Validar transição
        if (order.getStatus() != OrderStatus.PENDING) {
            throw new InvalidOperationException(
                "Only pending orders can be confirmed. " +
                "Current status: " + order.getStatus()
            );
        }
        
        // Realizar transição
        order.setStatus(OrderStatus.CONFIRMED);
        return orderRepository.save(order);
    }
    
    public Order startPreparing(Long orderId) {
        Order order = getOrderById(orderId);
        
        // Validar transição
        if (order.getStatus() != OrderStatus.CONFIRMED) {
            throw new InvalidOperationException(
                "Only confirmed orders can start preparing. " +
                "Current status: " + order.getStatus()
            );
        }
        
        // Realizar transição
        order.setStatus(OrderStatus.PREPARING);
        return orderRepository.save(order);
    }
    
    // ... mais métodos de transição ...
}
```

### Classe de Suporte (Futuro)

Para evitar duplicação, poderia criar:

```java
public class OrderStateValidator {
    
    public static void validateTransition(
            OrderStatus current, OrderStatus target) {
        
        switch (current) {
            case PENDING:
                if (target != CONFIRMED && target != CANCELLED) {
                    throw new InvalidOperationException(...);
                }
                break;
            case CONFIRMED:
                if (target != PREPARING && target != CANCELLED) {
                    throw new InvalidOperationException(...);
                }
                break;
            // ... mais casos ...
        }
    }
}
```

## Diagrama de Estados

```
┌─────────────────────────────────────────────────────┐
│                   ORDER STATES                       │
└─────────────────────────────────────────────────────┘

                    ┌─────────────┐
                    │   PENDING   │
                    └──────┬──────┘
                           │
                ┌──────────┴──────────┐
                │                     │
                ▼                     ▼
        ┌──────────────┐      ┌───────────────┐
        │  CONFIRMED   │      │   CANCELLED   │
        └──────┬───────┘      └───────────────┘
               │
               ▼
        ┌──────────────┐
        │  PREPARING   │
        └──────┬───────┘
               │
               ▼
        ┌──────────────┐
        │    READY     │
        └──────┬───────┘
               │
               ▼
        ┌──────────────────┐
        │   IN_DELIVERY    │
        └──────┬───────────┘
               │
               ▼
        ┌──────────────────┐
        │   DELIVERED      │
        └──────────────────┘

Legenda:
→ Transição permitida
✗ Transição não permitida
```

## Testes

### Teste de Transições Válidas

```java
@Test
void testConfirmOrder_Success() {
    Order order = createOrder(OrderStatus.PENDING);
    
    Order confirmed = orderService.confirmOrder(order.getId());
    
    assertThat(confirmed.getStatus()).isEqualTo(OrderStatus.CONFIRMED);
}
```

### Teste de Transições Inválidas

```java
@Test
void testConfirmOrder_InvalidStatus_ShouldFail() {
    Order order = createOrder(OrderStatus.CONFIRMED); // Já confirmado
    
    assertThatThrownBy(() -> orderService.confirmOrder(order.getId()))
        .isInstanceOf(InvalidOperationException.class)
        .hasMessageContaining("Only pending orders");
}
```

## Eventos (Futuro)

Para adicionar mais funcionalidades (notificações, eventos):

```java
public interface OrderEvent {
    void onStatusChanged(Order order, OrderStatus oldStatus, OrderStatus newStatus);
}

// Exemplo: Enviar email quando DELIVERED
public class OrderDeliveredNotifier implements OrderEvent {
    @Override
    public void onStatusChanged(Order order, OrderStatus old, OrderStatus newStatus) {
        if (newStatus == OrderStatus.DELIVERED) {
            emailService.sendDeliveryNotification(order);
        }
    }
}
```

## Auditing (Futuro)

Para rastrear todas as transições:

```java
@Entity
public class OrderStatusHistory {
    private Long orderId;
    private OrderStatus fromStatus;
    private OrderStatus toStatus;
    private LocalDateTime timestamp;
    private String reason; // Por que a transição?
}
```

## Terminal States

Estados de "fim" (nenhuma transição posterior):

```
DELIVERED  - Ordem completada com sucesso
CANCELLED  - Ordem cancelada
```

## Migration Path

### Fase 1 (Atual)
- State Machine simples via enums
- Validação manual no Service

### Fase 2 (Futuro)
- Classe de validação centralizada
- Testes mais estruturados

### Fase 3 (Futuro)
- Spring Statemachine se necessário
- Auditing de transições

## Related ADRs

- ADR-001: Vertical Slice Architecture
- ADR-003: PostgreSQL com JPA/Hibernate

## References

- [State Machine Pattern](https://refactoring.guru/design-patterns/state)
- [Spring Statemachine](https://spring.io/projects/spring-statemachine)
- [FSM - Finite State Machine](https://en.wikipedia.org/wiki/Finite-state_machine)

## Decision Log

- **2024-11-28**: Decisão aceita - State Machine para status de pedidos

---

**Status:** ✅ ACCEPTED

**Impact:** Médio - Define lógica de transições de estado

**Reversibility:** Média - Possível refatorar se necessário

