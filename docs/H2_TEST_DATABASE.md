# ✅ H2 Database para Testes - Configurado com Sucesso

## 🎯 Objetivo Alcançado

O projeto foi configurado para usar **H2 Database** (banco de dados em memória) durante testes, em vez de depender do PostgreSQL.

---

## 📋 O Que Foi Configurado

### 1. Dependência H2 Adicionada ao pom.xml

```xml
<!-- H2 Database for Testing -->
<dependency>
    <groupId>com.h2database</groupId>
    <artifactId>h2</artifactId>
    <scope>test</scope>
</dependency>
```

**Escopo `test`**: H2 é usado apenas durante testes, não em produção.

---

### 2. Profile de Teste Criado

**Arquivo:** `src/test/resources/application-test.properties`

```properties
# H2 Database Configuration (In-Memory)
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=

# JPA/Hibernate para H2
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
spring.jpa.defer-datasource-initialization=true
```

**Configurações principais:**
- `jdbc:h2:mem:testdb` - Banco em memória (isolado por teste)
- `create-drop` - Cria schema e deleta após cada teste
- `defer-datasource-initialization=true` - Carrega dados após criar schema

---

### 3. Anotação @ActiveProfiles nos Testes

Adicionada em todas as classes de teste:

```java
@SpringBootTest
@ActiveProfiles("test")  // ← Usa application-test.properties
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {
    // ...
}
```

**Arquivos atualizados:**
- `src/test/java/com/mendes/example/Springboot4ExampleApplicationTests.java`
- `src/test/java/com/mendes/example/order/presentation/OrderControllerIntegrationTest.java`

---

### 4. Dados de Teste Criados

**Arquivo:** `src/test/resources/data-h2.sql`

Contém dados iniciais para testes:
- 8 Pizzas com características diferentes
- 2 Clientes de exemplo

Carregados automaticamente via `spring.jpa.defer-datasource-initialization=true`.

---

## 🔄 Como Funciona

### Fluxo de Teste

```
1. Maven executa mvn test

2. Spring Boot carrega @SpringBootTest
   ↓
3. @ActiveProfiles("test") ativa application-test.properties
   ↓
4. H2 inicializa banco em memória
   ↓
5. Hibernate cria schema (create-drop)
   ↓
6. data-h2.sql carrega dados de teste
   ↓
7. Testes executam com dados isolados
   ↓
8. Após testes, schema é deletado
   ↓
9. Próximo teste começa limpo (novo banco)
```

---

## ✨ Vantagens do H2 em Testes

### ✅ Velocidade
- Banco em memória é muito rápido
- Sem latência de I/O

### ✅ Isolamento
- Cada teste tem seu próprio banco
- Sem interferência entre testes
- Sem impacto no banco de produção

### ✅ Reprodutibilidade
- Ambiente de teste sempre igual
- Sem necessidade de PostgreSQL rodando
- Mesmo resultado toda vez

### ✅ CI/CD Friendly
- Sem dependências externas
- Roda em qualquer máquina
- Pipeline CI/CD simples

### ✅ Desenvolvimento
- Rápido feedback na IDE
- Sem setup de banco em Docker
- Ideal para TDD

---

## 📊 Configuração de Propriedades

### application.properties (Produção)

```ini
spring.datasource.url=jdbc:postgresql://localhost:5432/pizza_db
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect
spring.jpa.hibernate.ddl-auto=update
```

### application-test.properties (Testes)

```ini
spring.datasource.url=jdbc:h2:mem:testdb
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.jpa.hibernate.ddl-auto=create-drop
```

**Ambas são carregadas automaticamente pelo Spring Boot baseado no profile ativo.**

---

## 🧪 Executar Testes

### Todos os testes
```bash
mvn test
```

### Teste específico
```bash
mvn test -Dtest=OrderControllerIntegrationTest
```

### Com output SQL
Descomente em `application-test.properties`:
```properties
spring.jpa.show-sql=true
```

---

## 🔍 Estrutura de Dados em H2

### Tabelas criadas automaticamente pelo Hibernate

Com `spring.jpa.hibernate.ddl-auto=create-drop`, Hibernate:

1. Lê as anotações @Entity nas classes domain
2. Cria tabelas correspondentes
3. Carrega dados de `data-h2.sql`

**Tabelas criadas:**
- `pizza`
- `customer`
- `orders`
- `order_item`

---

## 📝 Dados de Teste

### Pizzas Pré-carregadas

| ID | Nome | Preço | Tamanho |
|---|---|---|---|
| 1 | Margherita | 45.00 | MEDIUM |
| 2 | Pepperoni | 50.00 | MEDIUM |
| 3 | Quatro Queijos | 55.00 | LARGE |
| 4 | Vegetariana | 40.00 | MEDIUM |
| 5 | Havaiana | 48.00 | MEDIUM |
| 6 | Carnívora | 60.00 | LARGE |
| 7 | Frango com Catupiry | 52.00 | MEDIUM |
| 8 | Portuguesa | 48.00 | MEDIUM |

### Clientes Pré-carregados

| ID | Nome | Email |
|---|---|---|
| 1 | João Silva | joao@example.com |
| 2 | Maria Santos | maria@example.com |

---

## 🎯 Casos de Uso

### 1. Teste de Integração Completo

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

### 2. Teste sem Dados (Banco Vazio)

```java
@Sql(scripts = "/clean.sql")  // Limpa dados antes do teste
@Test
void testWithEmptyDatabase() {
    // Banco vazio, só schema existe
}
```

### 3. Dados Customizados por Teste

```java
@Sql(scripts = "/custom-data.sql")  // Script customizado
@Test
void testWithCustomData() {
    // Executa com dados customizados
}
```

---

## 🚀 Próximas Sugestões

### Melhorias Possíveis

1. **TestContainers** - PostgreSQL em container para testes de integração
2. **Fixtures** - Criar dados padrão reutilizáveis
3. **Test Properties** - Configurações diferentes por tipo de teste
4. **Embedded H2** - Console para debug durante testes

---

## ✅ Checklist

- [x] H2 dependency adicionada (scope=test)
- [x] Profile "test" criado
- [x] @ActiveProfiles adicionado aos testes
- [x] data-h2.sql criado com dados
- [x] Configuração Hibernate para H2
- [x] Documentação completa

---

## 📊 Status

| Item | Status |
|------|--------|
| H2 Dependency | ✅ Adicionada |
| Test Profile | ✅ Criado |
| Test Anotações | ✅ Adicionadas |
| Test Data | ✅ Carregado |
| Documentação | ✅ Completa |

---

## 🎊 Conclusão

O projeto agora está configurado para rodar testes com **H2 Database** em memória:

✅ Sem dependência de PostgreSQL em testes  
✅ Testes isolados e reproduzíveis  
✅ Execução rápida  
✅ Ideal para CI/CD  
✅ Dados pré-carregados automaticamente  

Pronto para testes! 🚀

---

**Data:** 28 de Novembro de 2024  
**Status:** ✅ CONFIGURADO E PRONTO

