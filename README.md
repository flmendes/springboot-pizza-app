# 🍕 Pizza Order Backend

[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.0-green)](https://spring.io/projects/spring-boot)
[![Java](https://img.shields.io/badge/Java-21%20LTS-orange)](https://www.oracle.com/java/technologies/javase/jdk21-archive.html)
[![PostgreSQL](https://img.shields.io/badge/PostgreSQL-13+-blue)](https://www.postgresql.org/)
[![RFC 9457](https://img.shields.io/badge/RFC-9457-brightgreen)](https://tools.ietf.org/html/rfc9457)
[![License](https://img.shields.io/badge/License-MIT-yellow)](LICENSE)

Uma aplicação backend robusta para gerenciar pedidos de pizzas, desenvolvida com **Spring Boot 4**, **Java 21** e seguindo o padrão **Vertical Slice Architecture** com implementação completa do **RFC 9457 - Problem Details for HTTP APIs**.

## 🎯 Features

- ✅ **Spring Boot 4** + **Java 21** (LTS)
- ✅ **Vertical Slice Architecture** (organização moderna)
- ✅ **PostgreSQL** com JPA/Hibernate
- ✅ **27+ Endpoints REST** completamente funcionais
- ✅ **RFC 9457** - Problem Details for HTTP APIs
- ✅ **Docker & Docker Compose** pronto
- ✅ **10+ Testes Integrados**
- ✅ **Documentação Profissional**

## 🚀 Quick Start

### Com Docker (Recomendado)

```bash
docker-compose up --build
# A API usa versionamento via annotations do Spring (default = 1). Portanto a chamada abaixo
# acessa a versão 1 por padrão.
curl http://localhost:8080/api/pizzas | jq

# Para solicitar uma versão específica via header:
# curl -H "X-Version: 2" http://localhost:8080/api/pizzas | jq
```

### Localmente

```bash
# Instalar dependências
mvn clean install

# Executar
mvn spring-boot:run

# Ou via IDE
# Abra em IntelliJ e execute: Run → Run 'Springboot4ExampleApplication'
```

## 📚 Documentação

### Começando
- **[Quick Start](docs/guides/QUICK_START.md)** - Primeiros 5 minutos
- **[Setup & Tests](docs/guides/SETUP_AND_TESTS.md)** - Configuração e testes
- **[Getting Started](docs/guides/GETTING_STARTED.md)** - Guia completo

### Arquitetura
- **[Vertical Slice Architecture](docs/architecture/VERTICAL_SLICE_ARCHITECTURE.md)** - Padrão de organização
- **[Arquitetura do Projeto](docs/architecture/PROJECT_ARCHITECTURE.md)** - Detalhes técnicos
- **[Diagramas](docs/architecture/DIAGRAMS.md)** - Visualizações

### API
- **[Documentação API](docs/api/ENDPOINTS.md)** - Todos os endpoints
- **[Exemplos de Uso](docs/api/EXAMPLES.md)** - cURL, Postman, JavaScript

### RFC 9457
- **[RFC 9457 Guide](docs/rfc9457/RFC_9457.md)** - Especificação e padrão
- **[Testes RFC 9457](docs/rfc9457/TESTS.md)** - Casos de teste
- **[Implementação](docs/rfc9457/IMPLEMENTATION.md)** - Detalhes técnicos

### Referência
- **[Estrutura do Projeto](docs/PROJECT_STRUCTURE.md)** - Organização de pastas
- **[Stack Tecnológico](docs/TECHNOLOGY_STACK.md)** - Versões e dependências

## 📋 Resumo do Projeto

| Aspecto | Detalhe |
|---------|---------|
| **Language** | Java 21 (LTS) |
| **Framework** | Spring Boot 4.0.0 |
| **Database** | PostgreSQL 13+ |
| **Architecture** | Vertical Slice Architecture |
| **Slices** | 3 (Pizza, Customer, Order) |
| **Endpoints** | 27+ |
| **Tests** | 10+ testes integrados |
| **RFC 9457** | Problem Details for HTTP APIs |

## 🏗️ Arquitetura

### Slices Verticais

```
pizza/              customer/           order/
├─ domain/         ├─ domain/          ├─ domain/
├─ infrastructure/ ├─ infrastructure/  ├─ infrastructure/
├─ application/    ├─ application/     ├─ application/
└─ presentation/   └─ presentation/    └─ presentation/
```

Cada slice é independente e autossuficiente, contendo todas as camadas necessárias.

## 🌐 Exemplo de Uso

Abaixo estão exemplos práticos dos endpoints e do fluxo de pedidos utilizados nos testes de integração (`OrderControllerIntegrationTest`). Em produção as rotas são prefixadas com `/api` (ex.: `/api/orders`) e o versionamento pode ser controlado via header `X-Version` conforme ADR-009.

### 1) Criar um pedido

Endpoint: POST /api/orders

Request (JSON):

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [
      {"pizzaId": 1, "quantity": 2},
      {"pizzaId": 2, "quantity": 1}
    ],
    "notes": "Sem cebola na primeira pizza"
  }' | jq
```

Response (201 Created - exemplo):

```json
{
  "id": 1,
  "customerId": 1,
  "status": "PENDING",
  "totalAmount": 140.00,
  "items": [
    {"pizzaId": 1, "pizzaName": "Margherita", "quantity": 2, "unitPrice": 45.00, "totalPrice": 90.00},
    {"pizzaId": 2, "pizzaName": "Pepperoni", "quantity": 1, "unitPrice": 50.00, "totalPrice": 50.00}
  ],
  "notes": "Sem cebola na primeira pizza",
  "createdAt": "2024-11-28T10:30:00"
}
```

> Observação: nos testes de integração (MockMvc) usamos `/orders` pois o MockMvc não aplica o context-path; em execução normal do aplicativo o caminho completo é `/api/orders`.

### 2) Confirmar pedido (PENDING -> CONFIRMED)

Endpoint: PUT /api/orders/{id}/confirm

```bash
curl -X PUT http://localhost:8080/api/orders/1/confirm -H "Content-Type: application/json" | jq
```

Exemplo de resposta (200 OK):
```json
{ "id": 1, "status": "CONFIRMED", ... }
```

### 3) Fluxo completo de status (exemplos)

- Iniciar preparo (CONFIRMED -> PREPARING):
  PUT /api/orders/{id}/start-preparing

- Marcar pronto (PREPARING -> READY):
  PUT /api/orders/{id}/mark-ready

- Marcar em entrega (READY -> IN_DELIVERY):
  PUT /api/orders/{id}/mark-in-delivery

- Marcar entregue (IN_DELIVERY -> DELIVERED):
  PUT /api/orders/{id}/mark-delivered

- Cancelar (qualquer estado permitido pela máquina de estados):
  PUT /api/orders/{id}/cancel

Exemplo (sequência):

```bash
curl -X PUT http://localhost:8080/api/orders/1/start-preparing
curl -X PUT http://localhost:8080/api/orders/1/mark-ready
curl -X PUT http://localhost:8080/api/orders/1/mark-in-delivery
curl -X PUT http://localhost:8080/api/orders/1/mark-delivered
```

Cada chamada retorna o `OrderResponse` atualizado (com o novo `status`).

### 4) Consultas

- Listar todos os pedidos: GET /api/orders

```bash
curl http://localhost:8080/api/orders | jq
```

- Buscar por cliente: GET /api/orders/customer/{customerId}

```bash
curl http://localhost:8080/api/orders/customer/1 | jq
```

- Buscar por status: GET /api/orders/status/{status}

```bash
curl http://localhost:8080/api/orders/status/CONFIRMED | jq
```

### 5) Validações e erros (RFC 9457)

Erros de validação e recursos não encontrados seguem o formato Problem Details (RFC 9457). Exemplos:

- Pedido sem itens (400 Bad Request):
```json
{
  "type": "https://api.example.com/problems/invalid-request",
  "title": "Invalid Request",
  "status": 400,
  "detail": "Order must have at least one item",
  "timestamp": "2024-11-28T10:30:00"
}
```

- Pizza não encontrada (404 Not Found):
```json
{
  "type": "https://api.example.com/problems/resource-not-found",
  "title": "Resource Not Found",
  "status": 404,
  "detail": "Pizza not found with id: 999",
  "timestamp": "2024-11-28T10:30:00"
}
```

## 🧪 Testes

```bash
# Rodar testes
mvn test

# Ou apenas OrderControllerIntegrationTest
mvn test -Dtest=OrderControllerIntegrationTest
```

## 🛠️ Stack Tecnológico

```
Backend:
  - Spring Boot 4.0.0
  - Spring Data JPA
  - Spring Web

Database:
  - PostgreSQL 13+
  - Hibernate ORM

Tools:
  - Maven 3.9+
  - Lombok
  - Guava

Container:
  - Docker
  - Docker Compose

Testing:
  - JUnit 5
  - Spring Test
```

## 📖 Estrutura de Pastas

```
pizza-app/
├── src/
│   ├── main/java/com/mendes/example/
│   │   ├── pizza/
│   │   ├── customer/
│   │   ├── order/
│   │   └── shared/
│   └── resources/
├── docs/                    # Documentação
│   ├── guides/             # Guias de início
│   ├── architecture/       # Arquitetura
│   ├── api/               # Documentação API
│   └── rfc9457/           # RFC 9457
├── pom.xml
├── Dockerfile
├── docker-compose.yml
├── README.md              # Este arquivo
└── CHANGELOG.md           # Histórico de mudanças
```

## 🎓 Aprenda

Este projeto é perfeito para:

- ✅ Aprender Spring Boot 4 com Java 21
- ✅ Entender Vertical Slice Architecture
- ✅ Implementar RFC 9457 corretamente
- ✅ Desenvolver REST APIs profissionais
- ✅ Usar Docker para containerização

## 🤝 Contribuindo

1. Fork o projeto
2. Crie uma branch para sua feature (`git checkout -b feature/AmazingFeature`)
3. Commit suas mudanças (`git commit -m 'Add some AmazingFeature'`)
4. Push para a branch (`git push origin feature/AmazingFeature`)
5. Abra um Pull Request

## 📄 Licença

Este projeto é licenciado sob a Licença MIT - veja o arquivo [LICENSE](LICENSE) para detalhes.

## 🔗 Links Úteis

- **Spring Boot Documentation**: https://spring.io/projects/spring-boot
- **Spring Data JPA**: https://spring.io/projects/spring-data-jpa
- **PostgreSQL**: https://www.postgresql.org/
- **RFC 9457**: https://tools.ietf.org/html/rfc9457
- **Docker**: https://www.docker.com/

## 👨‍💻 Autor

Desenvolvido com ❤️ usando Spring Boot 4 e Java 21

**Versão**: 1.1.0  
**Last Updated**: 28 de Novembro de 2024

---

**Pronto para começar?** Leia [Quick Start](docs/guides/QUICK_START.md) ou [Setup & Tests](docs/guides/SETUP_AND_TESTS.md)

<!-- PR: atualização da seção 'Exemplo de Uso' conforme ADR-009 (API Versioning). -->
