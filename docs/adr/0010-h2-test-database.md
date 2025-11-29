# ADR-010: H2 Database para Testes (em vez de PostgreSQL)

**Status:** Accepted

**Date:** 2024-11-28

**Decision Owner:** DevOps Architect

---

## Context

Durante o desenvolvimento e testes, havia a necessidade de um banco de dados para executar testes de integração. Inicialmente, os testes dependiam de uma instância PostgreSQL rodando localmente, o que causava:

1. Complexidade no setup local
2. Dependência externa
3. Testes lentos (I/O de rede)
4. Dificuldade em CI/CD
5. Testes não isolados

## Decision

Adotamos **H2 Database** como banco de dados em memória para testes, eliminando a dependência de PostgreSQL durante testes.

### Configuração

**Propriedades:** `application-test.properties`

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.defer-datasource-initialization=true
```

### Implementação

**pom.xml:**
```xml
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

**Testes:**
```java
@SpringBootTest
@ActiveProfiles("test")
class OrderControllerIntegrationTest {
    // Testes executam com H2 em memória
}
```

## Rationale

### Por que H2 em Memória

1. **Velocidade** - Banco em memória é extremamente rápido
2. **Isolamento** - Cada teste começa com banco limpo
3. **Independência** - Sem dependência de PostgreSQL
4. **Reprodutibilidade** - Mesmo resultado sempre
5. **CI/CD Ready** - Sem setup externo necessário
6. **Desenvolvimento Local** - Sem complexidade

### Comparação de Estratégias

| Aspecto | PostgreSQL Local | Docker Container | H2 Memória |
|---------|-----------------|------------------|-----------|
| Velocidade | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Isolamento | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Setup | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| CI/CD | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Realismo | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐ |

## Consequences

### Positivas ✅

1. Testes executam muito rápido (memória)
2. Cada teste tem banco limpo (create-drop)
3. Sem setup local complexo
4. CI/CD simplificado (sem dependências)
5. Testes mais previsíveis
6. Desenvolvimento mais ágil
7. Dados pré-carregados automaticamente

### Negativas ⚠️

1. Banco em memória não é 100% igual ao PostgreSQL
2. Algumas features PostgreSQL não existem em H2
3. Pode não pegar bugs específicos do PostgreSQL
4. Requer profile "test" separado

## Alternatives Considered

### 1. PostgreSQL Local (Rejeitada)

**Pros:** Realismo, testa banco real

**Cons:** Setup complexo, lento, testes acoplados

### 2. Docker PostgreSQL (Considerada)

**Pros:** Realismo, isolamento

**Cons:** Mais lento que H2, ainda é setup externo

### 3. TestContainers (Considerada para Futuro)

**Pros:** Container Docker real, realismo

**Cons:** Overhead inicial, setup mais complexo

## Implementation Details

### Estrutura de Configuração

```
src/
├── main/resources/
│   ├── application.properties          (Produção - PostgreSQL)
│   └── data.sql                        (Dados produção)
│
└── test/resources/
    ├── application-test.properties     (Testes - H2)
    └── data-h2.sql                     (Dados testes)
```

### Propriedades - Produção (PostgreSQL)

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/pizza_db
spring.datasource.username=postgres
spring.datasource.password=postgres
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

### Propriedades - Testes (H2)

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.defer-datasource-initialization=true
```

### Dados Pré-carregados (data-h2.sql)

```sql
-- 8 Pizzas
INSERT INTO pizza (name, price, size) VALUES
('Margherita', 45.00, 'MEDIUM'),
('Pepperoni', 50.00, 'MEDIUM'),
-- ... mais pizzas

-- 2 Clientes
INSERT INTO customer (name, email) VALUES
('João Silva', 'joao@example.com'),
('Maria Santos', 'maria@example.com');
```

### Fluxo de Teste

```
1. mvn test executado
   ↓
2. @ActiveProfiles("test") ativa application-test.properties
   ↓
3. H2 inicializa em memória (jdbc:h2:mem:testdb)
   ↓
4. Hibernate cria schema (create-drop)
   ↓
5. data-h2.sql carrega dados automaticamente
   ↓
6. Testes executam com dados isolados
   ↓
7. Schema deletado após testes
   ↓
8. Próximo teste começa com banco novo
```

## Testing Strategy

### Teste de Integração Completo

```java
@SpringBootTest
@ActiveProfiles("test")
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {
    
    @Test
    void createOrder_Success() {
        // H2 já tem pizzas e clientes carregados
        // Teste executa com dados de exemplo
    }
}
```

### Teste com Dados Customizados (Futuro)

```java
@Sql(scripts = "/custom-data.sql")
@Test
void testWithCustomData() {
    // Executa com dados customizados
}
```

### TestContainers (Futuro - para realismo)

```java
@Testcontainers
class OrderIntegrationTest {
    @Container
    static PostgreSQLContainer<?> postgres = 
        new PostgreSQLContainer<>("postgres:15");
}
```

## Migration Path

### Fase 1 (Atual)
- H2 para unit tests
- Dados pré-carregados em data-h2.sql
- Profile "test" com H2

### Fase 2 (Futuro)
- TestContainers para integration tests
- PostgreSQL em container para testes de realismo
- Mantém H2 para unit tests rápidos

## Arquivos Criados/Modificados

- ✅ `pom.xml` - H2 dependency (scope=test)
- ✅ `src/test/resources/application-test.properties` - Configuração H2
- ✅ `src/test/resources/data-h2.sql` - Dados de teste
- ✅ `Springboot4ExampleApplicationTests.java` - @ActiveProfiles("test")
- ✅ `OrderControllerIntegrationTest.java` - @ActiveProfiles("test")

## Related ADRs

- ADR-002: Spring Boot 4 com Java 21
- ADR-003: PostgreSQL com JPA/Hibernate

## References

- [H2 Database Official](https://www.h2database.com/)
- [Spring Boot Testing Guide](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [TestContainers Documentation](https://www.testcontainers.org/)

## Decision Log

- **2024-11-28**: Decisão aceita - H2 Database para testes implementado

---

**Status:** ✅ ACCEPTED

**Impact:** Médio - Afeta execução de testes, não afeta produção

**Reversibility:** Alta - Fácil migrar para TestContainers/PostgreSQL se necessário

