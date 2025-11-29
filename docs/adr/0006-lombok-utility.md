# ADR-006: Lombok para Redução de Boilerplate

**Status:** Accepted

**Date:** 2024-11-28

**Decision Owner:** Tech Lead

---

## Context

Entidades e DTOs em Java frequentemente requerem muito boilerplate: getters, setters, toString(), equals(), hashCode(), constructors.

Opções:
1. Escrever todo boilerplate manualmente
2. Usar IDE para gerar código
3. Usar anotações (Lombok)
4. Usar Records (Java 14+)

## Decision

Adotamos **Lombok** para reduzir boilerplate usando anotações.

```java
@Data              // Getter, setter, toString, equals, hashCode
@NoArgsConstructor // Constructor sem argumentos
@AllArgsConstructor // Constructor com todos os argumentos
@Builder          // Builder pattern
public class Order {
    private Long id;
    private Long customerId;
    private OrderStatus status;
}
```

## Rationale

1. **Redução de Código** - ~70% menos boilerplate
2. **Manutenibilidade** - Menos código para manter
3. **Claridade** - Código focado em lógica
4. **Compile-time** - Não runtime reflection
5. **Industry Standard** - Usado em muitos projetos

## Consequences

### Positivas ✅

- Código muito mais limpo
- Menos linhas para manter
- Fácil adicionar getters/setters
- Builder pattern automático
- Log automático

### Negativas ⚠️

- Dependency adicional
- IDE precisa suporte a Lombok
- Debugging pode ser confuso
- Compile-time code generation

## Alternatives Considered

### 1. Records (Java 14+) - Considerada

**Exemplo:**
```java
public record Order(Long id, Long customerId, OrderStatus status) {}
```

**Pros:** Moderno, imutável

**Cons:** Imutável (inadequado para entidades), menos flexível

**Decisão:** Não usar - Records são para imutáveis, nossas entidades são mutáveis

### 2. Escrever Tudo Manualmente - Rejeitada

**Pros:** Sem dependencies

**Cons:** Muito verboso, difícil manter

**Decisão:** Rejeitada - Lombok reduz código significantemente

## Implementation

### Anotações Usadas

```java
@Data              // Getter + Setter + ToString + Equals + HashCode
@NoArgsConstructor // Constructor()
@AllArgsConstructor // Constructor(field1, field2, ...)
@Builder          // Pattern builder
@Slf4j            // Logger SLF4J
```

### Exemplo Completo

```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Entity
@Table(name = "orders")
public class Order {
    
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(nullable = false)
    private Long customerId;
    
    @Enumerated(EnumType.STRING)
    private OrderStatus status;
    
    @Column(precision = 19, scale = 2)
    private BigDecimal totalAmount;
    
    @Column(length = 500)
    private String notes;
    
    @OneToMany(mappedBy = "order", cascade = CascadeType.ALL)
    private List<OrderItem> items = new ArrayList<>();
    
    @Column(name = "created_at", nullable = false, updatable = false)
    private LocalDateTime createdAt;
    
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
    
    // Métodos de negócio
    public void addItem(OrderItem item) {
        items.add(item);
        item.setOrder(this);
    }
    
    public void calculateTotalAmount() {
        totalAmount = items.stream()
            .map(OrderItem::getTotalPrice)
            .reduce(BigDecimal.ZERO, BigDecimal::add);
    }
}
```

### Sem Lombok (Para Comparação)

```java
@Entity
@Table(name = "orders")
public class Order {
    // ... fields ...
    
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    
    public Long getCustomerId() { return customerId; }
    public void setCustomerId(Long customerId) { this.customerId = customerId; }
    
    // ... mais getters/setters ...
    
    @Override
    public String toString() {
        return "Order{" +
            "id=" + id +
            ", customerId=" + customerId +
            // ... mais campos ...
            '}';
    }
    
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Order order = (Order) o;
        return Objects.equals(id, order.id);
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
```

## Configuration

### pom.xml

```xml
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<plugin>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-maven-plugin</artifactId>
    <configuration>
        <excludes>
            <exclude>
                <groupId>org.projectlombok</groupId>
                <artifactId>lombok</artifactId>
            </exclude>
        </excludes>
    </configuration>
</plugin>
```

## Lombok Configuration

### lombok.config

```properties
# Lombok configuration
lombok.log.fieldName = LOGGER
config.stopBubbling = true
```

## IDE Setup

### IntelliJ IDEA

1. Instalar plugin Lombok
2. Enable annotation processing
3. Settings → Compiler → Annotation Processors → Enable

### Eclipse

1. Instalar Lombok
2. Execute: java -jar lombok.jar

## Related ADRs

- ADR-005: DTO Pattern para Transferência de Dados

## References

- [Lombok Official](https://projectlombok.org/)
- [Lombok Features](https://projectlombok.org/features/all)

## Decision Log

- **2024-11-28**: Decisão aceita - Lombok para redução de boilerplate

---

**Status:** ✅ ACCEPTED

**Impact:** Baixo - Apenas facilita desenvolvimento

**Reversibility:** Alta - Fácil remover Lombok se necessário

