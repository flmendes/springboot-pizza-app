# ADR-003: PostgreSQL com JPA/Hibernate

**Status:** Accepted

**Date:** 2024-11-28

**Decision Owner:** Database Architect

---

## Context

O projeto necessitava escolher:

1. **Banco de Dados** - Relacional (PostgreSQL, MySQL) vs NoSQL (MongoDB) vs Híbrido
2. **ORM** - JPA/Hibernate vs QueryDSL vs Spring Data JDBC vs Raw JDBC

O domínio é:
- Pedidos de pizzas (estrutura relacional clara)
- Relacionamentos bem definidos
- Dados estruturados
- Necessidade de ACID

## Decision

Adotamos:

- **PostgreSQL 13+** - Banco de dados relacional
- **JPA/Hibernate** - ORM para abstração de dados

## Rationale

### PostgreSQL

**Razões para escolher:**

1. **Open Source** - Gratuito e sem vendor lock-in
2. **Confiabilidade** - Reputação excelente, usado em produção
3. **Features Avançadas** - JSONB, Arrays, Full-text search
4. **ACID Compliance** - Garantias transacionais completas
5. **Performance** - Otimizações para queries complexas
6. **Community** - Comunidade grande e ativa
7. **Docker Support** - Fácil para desenvolvimento local
8. **Escalabilidade** - Replicação e particionamento disponíveis

### JPA/Hibernate

**Razões para escolher:**

1. **Standard Java** - JPA é padrão da linguagem
2. **Spring Data Integration** - Integração perfeita com Spring Boot
3. **Menos Boilerplate** - Repositories automáticos
4. **Query Language** - HQL/JPQL independente do BD
5. **Relationships** - Relacionamentos automaticamente mapeados
6. **Transaction Management** - Gerenciamento automático
7. **Caching** - First/second level caching
8. **Portability** - Fácil mudar de BD se necessário

## Consequences

### Positivas ✅

1. **Portability** - Fácil mudar para outro BD relacional
2. **Productivity** - Desenvolvimento rápido com Spring Data JPA
3. **Maintainability** - Código mais legível com anotações
4. **Type Safety** - Compile-time checking de queries
5. **ACID** - Garantias transacionais completas
6. **Relationships** - Relacionamentos automatic mapping
7. **Performance** - Queries otimizadas pelo Hibernate
8. **Community** - Muita documentação e exemplos

### Negativas ⚠️

1. **N+1 Queries** - Requer cuidado com lazy loading
2. **Complex Queries** - Queries muito complexas podem ser difíceis em HQL
3. **Performance Overhead** - Pequeno overhead comparado a SQL puro
4. **Learning Curve** - JPA tem conceitos complexos
5. **Debugging** - SQL gerado pode ser difícil de debugar

## Alternatives Considered

### 1. MySQL (Rejeitada)

**Pros:**
- Versátil, muito usado
- Boa performance para leitura

**Cons:**
- Menos features avançadas que PostgreSQL
- Community menor
- Para este projeto, PostgreSQL é mais robusto

**Decisão:** Rejeitada - PostgreSQL é superior

### 2. MongoDB (Rejeitada)

**Pros:**
- Schema flexível
- Escalabilidade horizontal

**Cons:**
- **Nosso domínio é relacional** - Relacionamentos claros
- Sem ACID transacional
- Mais complexo para queries complexas
- Não é ideal para pedidos estruturados

**Decisão:** Rejeitada - Nosso domínio é relacional

### 3. Spring Data JDBC (Rejeitada)

**Pros:**
- Mais simples que JPA
- Menos magic

**Cons:**
- Sem relacionamentos automáticos
- Queries complexas requerem SQL
- Menos produtivo que JPA/Hibernate
- Menos flexível

**Decisão:** Rejeitada - JPA oferece mais produtividade

### 4. Raw JDBC (Rejeitada)

**Pros:**
- Máximo controle
- Melhor performance

**Cons:**
- Muito boilerplate
- Não portável
- Mais propenso a erros
- Lento para desenvolver

**Decisão:** Rejeitada - JPA é mais produtivo

## Trade-offs

| Aspecto | MySQL | PostgreSQL | MongoDB | Cassandra |
|---------|-------|-----------|---------|-----------|
| Relacional | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ❌ | ⭐⭐ |
| ACID | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐ |
| Performance | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Escalabilidade | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Features | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ |
| Community | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐ |
| **Escolhido** | ❌ | ✅ | ❌ | ❌ |

## Justificação do Domínio Relacional

```
Pizza (1) ----< (M) OrderItem (M) >---- (1) Order
             
Order (1) ----< (M) Customer

Características:
✅ Relacionamentos bem definidos
✅ Dados estruturados
✅ Regras de integridade referencial
✅ Queries com JOINs frequentes
✅ Necessidade de ACID transacional
```

## PostgreSQL Configuration

### Versão Suportada

```
- 13+ (mínimo)
- Recomendado: 15+ (features novas)
```

### Features PostgreSQL Utilizadas

1. **ENUM Types** - Para OrderStatus, PizzaSize
2. **Foreign Keys** - Integridade referencial
3. **Constraints** - Validação no BD
4. **Indexes** - Performance de queries
5. **Timestamps** - created_at, updated_at

### Connection Pool

```
HikariCP (padrão do Spring Boot):
- Pool size: 10 connections (default)
- Maximum pool size: 10
- Connection timeout: 30s
```

## JPA/Hibernate Configuration

### DDL Strategy

```properties
spring.jpa.hibernate.ddl-auto=update
# Opciones:
# - validate: Valida schema, erro se diferente
# - update: Atualiza schema (padrão)
# - create: Cria novo (cuidado em produção!)
# - create-drop: Cria e depois deleta
```

### Performance Tuning

```properties
# Show SQL generado
spring.jpa.show-sql=false (em produção)

# Format SQL para legibilidade
spring.jpa.properties.hibernate.format_sql=true

# Batch processing
spring.jpa.properties.hibernate.jdbc.batch_size=20
spring.jpa.properties.hibernate.order_inserts=true
spring.jpa.properties.hibernate.order_updates=true

# Connection Release
spring.jpa.properties.hibernate.connection.release_mode=auto
```

## Migration Strategy

### Schema Evolution

Usamos **Liquibase** ou **Flyway** no futuro para:
1. Versionamento de schema
2. Migrações rollback
3. Múltiplos ambientes

Para agora, DDL auto=update é suficiente.

## Performance Considerations

### N+1 Problem

```java
// ❌ Ruim - N+1 queries
List<Order> orders = orderRepository.findAll();
for (Order order : orders) {
    order.getCustomer().getName(); // Query por ordem!
}

// ✅ Bom - Eager loading
@Query("SELECT o FROM Order o JOIN FETCH o.customer")
List<Order> findAllWithCustomer();
```

### Indexing Strategy

```sql
-- Índices criados automaticamente:
- PRIMARY KEY (id)
- FOREIGN KEY (customer_id)
- Unique (email) em Customer

-- Índices adicionais considerados no futuro:
- order(created_at) para filtros de data
- order(status) para filtros de status
```

## Backup e Recovery

### Recomendações

```bash
# Backup regular
pg_dump pizza_db > backup.sql

# Restore
psql pizza_db < backup.sql

# Com Docker: volume persistente
volumes:
  - postgres_data:/var/lib/postgresql/data
```

## Related ADRs

- ADR-001: Vertical Slice Architecture
- ADR-002: Spring Boot 4 com Java 21
- ADR-005: DTO Pattern para Transferência de Dados

## References

- [PostgreSQL Official Docs](https://www.postgresql.org/docs/)
- [Spring Data JPA Reference](https://spring.io/projects/spring-data-jpa)
- [Hibernate ORM](https://hibernate.org/)
- [Database Design Best Practices](https://www.postgresql.org/docs/current/ddl-intro.html)

## Decision Log

- **2024-11-28**: Decisão aceita - PostgreSQL + JPA/Hibernate

---

**Status:** ✅ ACCEPTED

**Impact:** Alto - Define persistência do projeto

**Reversibility:** Média - Possível migrar para outro BD com esforço, mas JPA abstrai muito disso

