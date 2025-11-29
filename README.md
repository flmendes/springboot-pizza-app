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
curl http://localhost:8080/api/v1/pizzas | jq
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

### Criar Order

```bash
curl -X POST http://localhost:8080/api/v1/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [
      {"pizzaId": 1, "quantity": 2, "unitPrice": 45.00}
    ]
  }' | jq
```

**Response (RFC 9457 compliant):**

```json
HTTP/1.1 201 Created
Content-Type: application/json

{
  "id": 1,
  "customerId": 1,
  "status": "PENDING",
  "totalAmount": 90.00,
  "items": [
    {
      "pizzaId": 1,
      "pizzaName": "Margherita",
      "quantity": 2,
      "unitPrice": 45.00,
      "totalPrice": 90.00
    }
  ],
  "createdAt": "2024-11-28T10:30:00"
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

