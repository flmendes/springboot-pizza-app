# 🛠️ Technology Stack

Visão geral completa das tecnologias, versões e dependências utilizadas no projeto.

## 📊 Stack Overview

```
┌─────────────────────────────────────────────┐
│        PIZZA ORDER BACKEND STACK            │
├─────────────────────────────────────────────┤
│                                             │
│  Language & Runtime:                        │
│  ├─ Java 21 (LTS)                          │
│  └─ JVM (OpenJDK 21)                       │
│                                             │
│  Framework:                                 │
│  ├─ Spring Boot 4.0.0                      │
│  ├─ Spring Framework 7.x                    │
│  └─ Spring Data JPA                        │
│                                             │
│  Database:                                  │
│  ├─ PostgreSQL 13+                         │
│  ├─ Hibernate ORM                          │
│  └─ H2 (para testes)                       │
│                                             │
│  Build & Package:                           │
│  ├─ Maven 3.9+                             │
│  └─ Maven Compiler Plugin                  │
│                                             │
│  Tools & Utilities:                         │
│  ├─ Lombok                                 │
│  ├─ Guava                                  │
│  └─ SLF4J + Logback                        │
│                                             │
│  Testing:                                   │
│  ├─ JUnit 5                                │
│  ├─ Spring Test                            │
│  ├─ Mockito                                │
│  └─ RestAssured (via MockMvc)              │
│                                             │
│  Container & DevOps:                        │
│  ├─ Docker                                 │
│  └─ Docker Compose                         │
│                                             │
└─────────────────────────────────────────────┘
```

## 🔧 Versões Principais

| Component | Version | Status |
|-----------|---------|--------|
| **Java** | 21 LTS | ✅ Latest LTS |
| **Spring Boot** | 4.0.0 | ✅ Latest |
| **Spring Framework** | 7.x | ✅ Latest |
| **PostgreSQL** | 13+ | ✅ Supported |
| **Maven** | 3.9+ | ✅ Latest |
| **Docker** | Latest | ✅ Latest |

---

## 📦 Dependências

### Core Framework

```xml
<!-- Spring Boot Starter Web (REST API) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-web</artifactId>
</dependency>

<!-- Spring Boot Starter Data JPA (ORM) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-data-jpa</artifactId>
</dependency>

<!-- Spring Boot Starter Validation -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-validation</artifactId>
</dependency>
```

### Database

```xml
<!-- PostgreSQL JDBC Driver -->
<dependency>
    <groupId>org.postgresql</groupId>
    <artifactId>postgresql</artifactId>
    <scope>runtime</scope>
</dependency>

<!-- Hibernate ORM (integrado via Spring Data JPA) -->
<!-- Version: Automático (definido pelo Spring Boot) -->
```

### Utilities

```xml
<!-- Lombok (Redução de boilerplate) -->
<dependency>
    <groupId>org.projectlombok</groupId>
    <artifactId>lombok</artifactId>
    <optional>true</optional>
</dependency>

<!-- Guava (Utilities do Google) -->
<dependency>
    <groupId>com.google.guava</groupId>
    <artifactId>guava</artifactId>
    <version>33.0.0-jre</version>
</dependency>
```

### Development

```xml
<!-- Spring Boot DevTools (Hot Reload) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-devtools</artifactId>
    <scope>runtime</scope>
    <optional>true</optional>
</dependency>
```

### Testing

```xml
<!-- Spring Boot Starter Test -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-test</artifactId>
    <scope>test</scope>
</dependency>

<!-- Inclui:
    - JUnit 5
    - Mockito
    - AssertJ
    - Hamcrest
    - MockMvc
    - Spring Test -->
```

---

## 🏗️ Arquitetura de Camadas

```
┌─────────────────────────────────────────┐
│        HTTP REQUEST (Cliente)           │
└────────────────────┬────────────────────┘
                     │
        ┌────────────▼────────────┐
        │  PRESENTATION LAYER     │
        │  Controllers (@RestController)
        │  DTOs (Request/Response)│
        └────────────┬────────────┘
                     │
        ┌────────────▼────────────┐
        │  APPLICATION LAYER      │
        │  Services (@Service)    │
        │  Business Logic         │
        │  @Transactional         │
        └────────────┬────────────┘
                     │
        ┌────────────▼────────────┐
        │  DOMAIN LAYER           │
        │  Entities (@Entity)     │
        │  Enums                  │
        │  Business Rules         │
        └────────────┬────────────┘
                     │
        ┌────────────▼────────────┐
        │  INFRASTRUCTURE LAYER   │
        │  Repositories (@Repository)
        │  JPA Queries            │
        └────────────┬────────────┘
                     │
        ┌────────────▼────────────┐
        │  PERSISTENCE CONTEXT    │
        │  Hibernate ORM          │
        └────────────┬────────────┘
                     │
        ┌────────────▼────────────┐
        │  DATABASE               │
        │  PostgreSQL             │
        └─────────────────────────┘
```

---

## 🗄️ Banco de Dados

### PostgreSQL

```sql
-- Versão: 13+
-- Driver: postgresql (org.postgresql)
-- Port: 5432
-- Database: pizza_db
-- Username: postgres
-- Password: postgres (padrão local)

-- Tabelas:
-- 1. pizza (8 registros pré-carregados)
-- 2. customer
-- 3. orders
-- 4. order_item

-- Features:
-- - Transações ACID
-- - Foreign Keys configuradas
-- - Índices automáticos
-- - Timestamps de auditoria
```

### Hibernate Configuration

```properties
# DDL Auto
spring.jpa.hibernate.ddl-auto=update

# Database Platform
spring.jpa.database-platform=org.hibernate.dialect.PostgreSQLDialect

# SQL Formatting
spring.jpa.properties.hibernate.format_sql=true
spring.jpa.properties.hibernate.use_sql_comments=true

# Show SQL
spring.jpa.show-sql=false (em produção)
```

---

## 🐳 Container Stack

### Docker

```dockerfile
# Multi-stage Build
FROM maven:3.9-eclipse-temurin-21 AS builder
# Build stage
# Compila o aplicativo

FROM eclipse-temurin:21-jre-alpine
# Runtime stage
# Executa o JAR compilado
```

### Docker Compose

```yaml
services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: pizza_db
      POSTGRES_PASSWORD: postgres
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck: ✅
    
  app:
    build: .
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/pizza_db
    depends_on:
      postgres:
        condition: service_healthy
```

---

## 🧪 Testing Stack

### JUnit 5 + Spring Test

```
JUnit 5:
├─ Platform
├─ Jupiter (Testes)
└─ Vintage (Compatibilidade)

Spring Test:
├─ @SpringBootTest
├─ @MockBean
├─ MockMvc
└─ TestRestTemplate

Mockito:
├─ Mocks
├─ Stubs
└─ Spies

AssertJ:
└─ Fluent Assertions
```

### Exemplo de Teste

```java
@SpringBootTest
@AutoConfigureMockMvc
class OrderControllerIntegrationTest {
    
    @Autowired
    private MockMvc mockMvc;
    
    @MockBean
    private OrderService orderService;
    
    @Test
    void testCreateOrder_Success() {
        // Test implementation
    }
}
```

---

## 📊 Build Process

### Maven

```
mvn clean install          # Clean + compile + test + package
mvn compile               # Compila código
mvn test                  # Executa testes
mvn package              # Cria JAR
mvn spring-boot:run      # Executa direto

Plugins:
├─ maven-compiler-plugin       # Compilação
├─ spring-boot-maven-plugin    # Boot packaging
├─ native-maven-plugin         # GraalVM Native
└─ Lombok processor            # Geração de código
```

### Artifacts

```
target/
├─ classes/              # Arquivos compilados
├─ test-classes/         # Testes compilados
├─ generated-sources/    # Código gerado
└─ pizza-app-0.0.1-SNAPSHOT.jar
   └─ Aplicativo executável
```

---

## 🔐 Security & Compliance

### Spring Security
- ❌ Não implementado (futuro)
- Será adicionado em próximas versões

### RFC 9457 Compliance
- ✅ ProblemDetail do Spring Framework 7.x
- ✅ Application/problem+json media type
- ✅ Exceções customizadas (404, 409)
- ✅ GlobalExceptionHandler

### Logging
- SLF4J (API)
- Logback (Implementação)
- Logs estruturados

---

## 🚀 Performance

### JVM Options

```bash
# Default (maven)
-Xmx512m -XX:MaxPermSize=256m

# Production
-Xmx2g -XX:+UseG1GC -XX:MaxGCPauseMillis=200

# Development
-Xmx1g -XX:+UnlockDiagnosticVMOptions
```

### Database Optimization

```
- Connection Pooling (HikariCP)
- Índices em Foreign Keys
- Timestamps para auditoria
- Lazy loading relacionamentos
```

---

## 📈 Escalabilidade

### Pronto Para

- ✅ Microserviços (cada slice pode ser independente)
- ✅ Cache (Redis ready)
- ✅ Messaging (Spring AMQP ready)
- ✅ Cloud (Spring Cloud ready)
- ✅ Observability (Micrometer ready)

### Extensões Futuras

- [ ] Spring Cloud (Config, Service Discovery)
- [ ] Spring Data Redis (Cache)
- [ ] Spring Data Elasticsearch (Search)
- [ ] Spring Cloud Stream (Messaging)
- [ ] Prometheus/Grafana (Monitoring)

---

## 🔄 Continuous Integration

### GitHub Actions (Futura)

```yaml
name: CI/CD

on: [push, pull_request]

jobs:
  build:
    runs-on: ubuntu-latest
    steps:
      - uses: actions/checkout@v2
      - name: Set up JDK 21
        uses: actions/setup-java@v2
      - name: Build with Maven
        run: mvn clean install
      - name: Run Tests
        run: mvn test
```

---

## 📚 Versão do Java

### Java 21 Features Utilizadas

```java
// Text Blocks (Java 13+)
String json = """
    {
      "key": "value"
    }
    """;

// Records (Java 14+)
record OrderDto(Long id, String status) {}

// Pattern Matching (Java 16+)
if (obj instanceof String s) {
    System.out.println(s.length());
}

// Sealed Classes (Java 15+)
sealed class Vehicle permits Car, Truck {}
```

---

## 📊 Dependency Tree

```
pizza-app
├── org.springframework.boot:spring-boot-starter-web:4.0.0
│   └── org.springframework:spring-web:7.x
│       └── org.springframework:spring-core:7.x
├── org.springframework.boot:spring-boot-starter-data-jpa:4.0.0
│   └── org.hibernate:hibernate-core
├── org.postgresql:postgresql:42.x
├── org.projectlombok:lombok:1.18.x
├── com.google.guava:guava:33.0.0-jre
└── org.springframework.boot:spring-boot-starter-test:4.0.0
    ├── org.junit.jupiter:junit-jupiter
    └── org.mockito:mockito-core
```

---

## 🔗 Links Úteis

- [Java 21 Documentation](https://docs.oracle.com/en/java/javase/21/)
- [Spring Boot 4 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes)
- [Spring Data JPA Docs](https://spring.io/projects/spring-data-jpa)
- [PostgreSQL Documentation](https://www.postgresql.org/docs/)
- [Maven Documentation](https://maven.apache.org/guides/)

---

**Last Updated**: 28 de Novembro de 2024

[← Voltar ao INDEX](INDEX.md)

