# 📚 Documentação

Bem-vindo à documentação do **Pizza Order Backend**! Aqui você encontrará tudo o que precisa para entender, usar e estender o projeto.

## 🚀 Começando

**Novo no projeto?** Comece aqui:

1. **[Technology Stack](TECHNOLOGY_STACK.md)** (5 minutos)
   - Stack tecnológico completo
   - Versões de dependências
   - Componentes principais

2. **[Project Structure](PROJECT_STRUCTURE.md)** (15 minutos)
   - Estrutura do projeto
   - Organização de pastas
   - Padrão Vertical Slice Architecture

3. **[API Versioning](API_VERSIONING.md)** (10 minutos)
   - Versionamento de API
   - Endpoints disponíveis
   - Padrão utilizado

---

## 🏗️ Arquitetura

Entenda como o projeto é organizado:

- **[Project Structure](PROJECT_STRUCTURE.md)**
  - Vertical Slice Architecture
  - Organização de módulos
  - Separação de concerns

---

## 🌐 API

Documentação completa dos endpoints:

- **[API Versioning](API_VERSIONING.md)**
  - Todos os endpoints disponíveis
  - Padrão de versionamento
  - Exemplos de requisições

---

## 📋 RFC 9457

Implementação do padrão RFC 9457 (Problem Details for HTTP APIs):

- **Recursos técnicos:**
  - Padrão RFC 9457 para respostas de erro
  - Estrutura padronizada de erros
  - Integração com Spring Boot

---

## 🔍 H2 Database para Testes

Configuração e uso de H2 em testes:

- **[H2 Test Database](H2_TEST_DATABASE.md)**
  - Banco em memória para testes
  - Isolamento de testes
  - Performance

- **[H2 Setup Guide](H2_SETUP_GUIDE.md)**
  - Guia de configuração
  - Propriedades
  - Exemplos

---

## 📡 OpenTelemetry

Observabilidade com tracing e métricas:

- **[OpenTelemetry Guide](OPENTELEMETRY_GUIDE.md)**
  - Configuração de observabilidade
  - Jaeger e Prometheus
  - Integração com Spring Boot

---

## 📋 Architecture Decision Records (ADRs)

Decisões arquiteturais importantes documentadas:

- **[ADR Index](adr/README.md)** - Índice de todos os ADRs

### Principais ADRs

1. **[ADR-001: Vertical Slice Architecture](adr/0001-vertical-slice-architecture.md)**
   - Arquitetura escolhida
   - Benefícios

2. **[ADR-002: Spring Boot 4 com Java 21](adr/0002-spring-boot-4-java-21.md)**
   - Stack escolhido

3. **[ADR-003: PostgreSQL com JPA](adr/0003-postgresql-jpa.md)**
   - Persistência

4. **[ADR-004: RFC 9457 Error Handling](adr/0004-rfc-9457-error-handling.md)**
   - Tratamento de erros

5. **[ADR-005: DTO Pattern](adr/0005-dto-pattern.md)**
   - Transferência de dados

6. **[ADR-006: Lombok Utility](adr/0006-lombok-utility.md)**
   - Redução de boilerplate

7. **[ADR-007: Docker Containerization](adr/0007-docker-containerization.md)**
   - Containerização

8. **[ADR-008: Order Status State Machine](adr/0008-order-status-state-machine.md)**
   - Máquina de estados

9. **[ADR-009: Spring Framework 7.0.1+ API Versioning](adr/0009-api-versioning.md)**
   - Versionamento de API

10. **[ADR-010: H2 Database para Testes](adr/0010-h2-test-database.md)**
    - Banco em memória para testes

11. **[ADR-011: Organização de Documentação](adr/0011-documentation-organization.md)**
    - Padrão GitHub

12. **[ADR-012: OpenTelemetry para Observabilidade](adr/0012-opentelemetry-observability.md)**
    - Observabilidade

---

## 📖 Referência

### Estrutura do Projeto

```
docs/
├── adr/                        # Architecture Decision Records (12 ADRs)
├── API_VERSIONING.md           # Versionamento de API
├── H2_TEST_DATABASE.md         # H2 para Testes
├── H2_SETUP_GUIDE.md           # Setup H2
├── OPENTELEMETRY_GUIDE.md      # OpenTelemetry
├── INDEX.md                    # Este arquivo
├── PROJECT_STRUCTURE.md        # Estrutura de Projeto
└── TECHNOLOGY_STACK.md         # Stack Tecnológico
```

---

## 🎓 Como Usar Este Projeto

### Para Iniciantes

1. Leia: [Technology Stack](TECHNOLOGY_STACK.md)
2. Leia: [Project Structure](PROJECT_STRUCTURE.md)
3. Execute: `docker-compose up --build`
4. Teste: `curl http://localhost:8080/api/pizzas | jq`

### Para Arquitetos

1. Leia: [Project Structure](PROJECT_STRUCTURE.md)
2. Revise: [ADR-001 (VSA)](adr/0001-vertical-slice-architecture.md)
3. Revise: [ADR-012 (OpenTelemetry)](adr/0012-opentelemetry-observability.md)
4. Explore: Código fonte

### Para Desenvolvedores

1. Leia: [Technology Stack](TECHNOLOGY_STACK.md)
2. Leia: [API Versioning](API_VERSIONING.md)
3. Leia: [H2 Test Database](H2_TEST_DATABASE.md)
4. Execute: Testes com `mvn test`

### Para Integrações

1. Leia: [API Versioning](API_VERSIONING.md)
2. Use: Exemplos de requisições
3. Configure: Endpoint OTLP (OpenTelemetry)

---

## 🔧 Quick Setup

```bash
# 1. Build
mvn clean install

# 2. Start
docker-compose up --build

# 3. Test
curl http://localhost:8080/api/pizzas | jq

# 4. View
# Jaeger: http://localhost:16686
# Prometheus: http://localhost:9090
# App: http://localhost:8080/api
```

---

## 📚 Recursos Externos

- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [Spring Data JPA](https://spring.io/projects/spring-data-jpa)
- [PostgreSQL](https://www.postgresql.org/)
- [RFC 9457](https://tools.ietf.org/html/rfc9457)
- [Docker](https://www.docker.com/)
- [OpenTelemetry](https://opentelemetry.io/)

---

## 🤝 Contribuindo

Para sugerir melhorias na documentação ou relatar erros:

1. Abra uma issue
2. Descreva o problema/sugestão
3. Se possível, sugira a solução

---

**Last Updated**: 28 de Novembro de 2024

[← Voltar ao README](../README.md)

