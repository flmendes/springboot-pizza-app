# 📁 Estrutura do Projeto

Guia completo da organização de pastas e arquivos do projeto.

## 📂 Estrutura Principal

```
pizza-app/
│
├── README.md              # Documentação principal
│   ├── main/
│   │   ├── java/com/mendes/example/
│   │   │   ├── pizza/             # SLICE: Pizza Management
│   │   │   │   ├── domain/        # Pizza.java, PizzaSize.java
│   │   │   │   ├── infrastructure/# PizzaRepository.java
│   │   │   │   ├── application/   # PizzaService.java, DTOs
│   │   │   │   └── presentation/  # PizzaController.java
│   │   │   │
│   │   │   ├── customer/          # SLICE: Customer Management
│   │   │   │   ├── domain/        # Customer.java
│   │   │   │   ├── infrastructure/# CustomerRepository.java
│   │   │   │   ├── application/   # CustomerService.java, DTOs
│   │   │   │   └── presentation/  # CustomerController.java
│   │   │   │
│   │   │   ├── order/             # SLICE: Order Management (Principal)
│   │   │   │   ├── domain/        # Order.java, OrderItem.java, OrderStatus.java
│   │   │   │   ├── infrastructure/# OrderRepository.java, OrderItemRepository.java
│   │   │   │   ├── application/   # OrderService.java, DTOs
│   │   │   │   └── presentation/  # OrderController.java
│   │   │   │
│   │   │   ├── shared/            # Código Compartilhado
│   │   │   │   ├── exception/     # GlobalExceptionHandler.java, exceções
│   │   │   │   └── config/        # JpaConfig.java
│   │   │   │
│   │   │   └── Springboot4ExampleApplication.java  # Main class
│   │   │
│   │   └── resources/
│   │       ├── application.properties  # Configurações
│   │       ├── data.sql               # Dados iniciais (pizzas)
│   │       ├── static/                # Arquivos estáticos
│   │       └── templates/             # Templates (se houver)
│   │
│   └── test/
│       └── java/com/mendes/example/
│           └── order/presentation/
│               └── OrderControllerIntegrationTest.java
│
├── docs/                          # Documentação
│   ├── guides/                    # Guias de início
│   │   ├── QUICK_START.md
│   │   ├── SETUP_AND_TESTS.md
│   │   └── GETTING_STARTED.md
│   │
│   ├── architecture/              # Documentação de arquitetura
│   │   ├── VERTICAL_SLICE_ARCHITECTURE.md
│   │   ├── PROJECT_ARCHITECTURE.md
│   │   └── DIAGRAMS.md
│   │
│   ├── api/                       # Documentação de API
│   │   ├── ENDPOINTS.md
│   │   └── EXAMPLES.md
│   │
│   ├── rfc9457/                   # RFC 9457
│   │   ├── RFC_9457.md
│   │   ├── TESTS.md
│   │   ├── IMPLEMENTATION.md
│   │   └── CHANGELOG.md
│   │
│   ├── PROJECT_STRUCTURE.md       # Este arquivo
│   ├── TECHNOLOGY_STACK.md        # Stack tecnológico
│   └── INDEX.md                   # Índice da documentação
│
├── pom.xml                        # Dependências Maven
├── Dockerfile                     # Docker image
├── docker-compose.yml             # Docker Compose
├── README.md                      # Documentação principal
├── CHANGELOG.md                   # Histórico de mudanças
├── HELP.md                        # Ajuda
└── LICENSE                        # Licença (MIT)
```

## 🏗️ Organização por Slice

### Pizza Slice

```
pizza/
├── domain/
│   ├── Pizza.java              # Entidade JPA com relacionamentos
│   └── PizzaSize.java          # Enum de tamanhos (SMALL, MEDIUM, LARGE, EXTRA_LARGE)
│
├── infrastructure/
│   └── PizzaRepository.java    # JPA Repository com queries customizadas
│
├── application/
│   ├── PizzaService.java       # Lógica de negócio com @Transactional
│   └── dto/
│       ├── CreatePizzaRequest.java   # DTO de entrada
│       └── PizzaResponse.java        # DTO de saída
│
└── presentation/
    └── PizzaController.java    # REST Controller com endpoints
```

### Customer Slice

```
customer/
├── domain/
│   └── Customer.java           # Entidade JPA
│
├── infrastructure/
│   └── CustomerRepository.java # JPA Repository
│
├── application/
│   ├── CustomerService.java    # Lógica de negócio
│   └── dto/
│       ├── CreateCustomerRequest.java
│       └── CustomerResponse.java
│
└── presentation/
    └── CustomerController.java # REST Controller
```

### Order Slice

```
order/
├── domain/
│   ├── Order.java              # Entidade principal
│   ├── OrderItem.java          # Entidade de item
│   └── OrderStatus.java        # Enum de status (PENDING, CONFIRMED, etc)
│
├── infrastructure/
│   ├── OrderRepository.java    # JPA Repository
│   └── OrderItemRepository.java
│
├── application/
│   ├── OrderService.java       # Lógica de negócio completa
│   └── dto/
│       ├── CreateOrderRequest.java
│       ├── OrderItemRequest.java
│       ├── OrderItemResponse.java
│       └── OrderResponse.java
│
└── presentation/
    └── OrderController.java    # REST Controller (15 endpoints)
```

### Shared Layer

```
shared/
├── exception/
│   ├── GlobalExceptionHandler.java     # RFC 9457 compliant
│   ├── ResourceNotFoundException.java   # 404
│   ├── InvalidOperationException.java   # 409
│   └── ErrorResponse.java              # @deprecated
│
└── config/
    └── JpaConfig.java                  # Configurações JPA
```

## 📚 Documentação

### guides/ - Guias de Início

```
guides/
├── QUICK_START.md          # Primeiros 5 minutos
├── SETUP_AND_TESTS.md      # Setup local e testes
└── GETTING_STARTED.md      # Guia completo de início
```

**Quando usar:**
- QUICK_START.md: Quer começar rápido (Docker)
- SETUP_AND_TESTS.md: Quer rodar localmente
- GETTING_STARTED.md: Quer entender tudo

### architecture/ - Arquitetura

```
architecture/
├── VERTICAL_SLICE_ARCHITECTURE.md  # Padrão VSA
├── PROJECT_ARCHITECTURE.md         # Detalhes técnicos
└── DIAGRAMS.md                     # Visualizações
```

**Quando usar:**
- VERTICAL_SLICE_ARCHITECTURE.md: Quer aprender VSA
- PROJECT_ARCHITECTURE.md: Quer entender a implementação
- DIAGRAMS.md: Prefere visualizações

### api/ - API Reference

```
api/
├── ENDPOINTS.md   # Documentação completa dos endpoints
└── EXAMPLES.md    # Exemplos com cURL, Postman, JS, Python
```

**Quando usar:**
- ENDPOINTS.md: Quer saber quais endpoints existem
- EXAMPLES.md: Quer exemplos de como usar

### rfc9457/ - RFC 9457

```
rfc9457/
├── RFC_9457.md        # Especificação e padrão
├── TESTS.md          # Casos de teste
├── IMPLEMENTATION.md # Detalhes técnicos
└── CHANGELOG.md      # Histórico de mudanças RFC 9457
```

**Quando usar:**
- RFC_9457.md: Quer entender o padrão
- TESTS.md: Quer testar a implementação
- IMPLEMENTATION.md: Quer ver os detalhes técnicos

## 🗂️ Convenções

### Nomenclatura de Classes

```
✅ CORRETO:
├── Pizza.java                      # Entidade (substantivo singular)
├── PizzaService.java              # Service (classe + Service)
├── PizzaRepository.java           # Repository (classe + Repository)
├── PizzaController.java           # Controller (classe + Controller)
├── CreatePizzaRequest.java        # DTO (Verbo + classe + Request)
└── PizzaResponse.java             # DTO (classe + Response)

❌ EVITAR:
├── pizza.java
├── ServicePizza.java
├── pizzaRepository.java
└── CreateRequest.java
```

### Nomenclatura de Pacotes

```
✅ CORRETO:
├── com.mendes.example.pizza.domain
├── com.mendes.example.pizza.infrastructure
├── com.mendes.example.pizza.application
└── com.mendes.example.pizza.presentation

❌ EVITAR:
├── com.mendes.example.pizza.model
├── com.mendes.example.pizza.repository
├── com.mendes.example.pizza.service
```

## 📋 Arquivo

### application.properties

Configurações da aplicação:
- Server (porta 8080, context-path /api)
- PostgreSQL (URL, usuário, senha)
- JPA/Hibernate (DDL, show-sql, etc)
- Logging levels

### data.sql

Dados iniciais:
- 8 pizzas com informações completas
- Carregado automaticamente ao iniciar

### pom.xml

Dependências Maven:
- Spring Boot 4.0.0
- Spring Data JPA
- PostgreSQL driver
- Lombok
- Guava
- Plugins (compiler, spring-boot-maven-plugin)

### Dockerfile

Imagem Docker:
- Multi-stage build (Maven + JRE)
- Baseado em eclipse-temurin:21-jre-alpine
- Otimizado para tamanho

### docker-compose.yml

Orquestração:
- PostgreSQL service
- Application service
- Health checks
- Volumes persistentes

## 🔍 Buscar Arquivos

### Por Funcionalidade

```bash
# Tudo relacionado a Pizza
find src -path "*pizza*"

# Todos os Controllers
find src -name "*Controller.java"

# Todos os DTOs
find src -name "*Request.java" -o -name "*Response.java"

# Toda documentação
find docs -name "*.md"
```

### Por Padrão

```bash
# Repositories (Infrastructure)
find src -name "*Repository.java"

# Services (Application)
find src -name "*Service.java"

# Exceções
find src -path "*exception*" -name "*.java"

# Testes
find src/test -name "*Test.java"
```

## 📈 Estatísticas

```
Total de Arquivos:
├── Java: 29+
├── Documentação MD: 15+
├── Configuração: 5 (pom.xml, properties, Dockerfile, etc)
└── Total: ~50 arquivos

Linhas de Código:
├── Java: ~2500 linhas
├── Testes: ~500 linhas
├── Documentação: ~2000 linhas
└── Total: ~5000 linhas

Arquivos por Tipo:
├── Slices: 3 (pizza, customer, order)
├── Camadas por Slice: 4 (domain, infrastructure, application, presentation)
├── Testes: 1 classe com 10+ métodos
└── Documentação: 15+ arquivos MD
```

---

**Last Updated**: 28 de Novembro de 2024

[← Voltar ao INDEX](INDEX.md)

