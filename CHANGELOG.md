# Changelog

Todos os mudanças notáveis deste projeto serão documentadas neste arquivo.

O formato é baseado em [Keep a Changelog](https://keepachangelog.com/en/1.0.0/),
e este projeto segue [Semantic Versioning](https://semver.org/spec/v2.0.0.html).

## [1.1.0] - 2024-11-28

### Added

#### RFC 9457 - Problem Details for HTTP APIs

- ✅ Implementação completa do RFC 9457 usando Spring Framework 7.x
- ✅ Nova classe `ResourceNotFoundException` para erros 404
- ✅ Nova classe `InvalidOperationException` para erros 409
- ✅ `GlobalExceptionHandler` atualizado com RFC 9457 compliant
- ✅ Media Type `application/problem+json` para respostas de erro
- ✅ Campos customizados: `timestamp` e `errorCode`
- ✅ Tratamento de 4 tipos de erro: 400, 404, 409, 500

#### Documentação

- ✅ Documentação completa do RFC 9457
- ✅ Guias de setup e testes
- ✅ Diagramas da arquitetura
- ✅ Exemplos de uso com cURL, JavaScript e Python
- ✅ Reorganização de documentação em `docs/`

#### Testes

- ✅ 10+ testes integrados para fluxo completo de pedidos
- ✅ Testes de validação e tratamento de erros

### Changed

- 🔄 Atualizada estrutura de pastas do projeto
- 🔄 Services atualizados para usar novas exceções
- 🔄 README.md reformulado com badges e melhor estrutura
- 🔄 Documentação movida para `docs/` pasta

### Deprecated

- ⚠️ Classe `ErrorResponse` marcada como `@deprecated`
- ⚠️ Use `ProblemDetail` do Spring Framework 7.x em vez disso

### Fixed

- 🐛 Tratamento de erros mais preciso com status HTTP apropriados
- 🐛 Validações em múltiplas camadas (Domain, Application, Presentation)

### Security

- 🔒 Mensagens de erro padronizadas sem expor detalhes internos
- 🔒 Logging estruturado de todas as exceções

---

## [1.0.0] - 2024-11-28

### Added

#### Arquitetura - Vertical Slice Architecture

- ✅ 3 Slices verticais independentes:
  - **Pizza Slice** - Gerenciamento de pizzas
  - **Customer Slice** - Gerenciamento de clientes
  - **Order Slice** - Gerenciamento de pedidos

#### Pizza Management (6 endpoints)

- `GET /api/v1/pizzas` - Listar pizzas disponíveis
- `GET /api/v1/pizzas/{id}` - Obter pizza por ID
- `POST /api/v1/pizzas` - Criar nova pizza
- `PUT /api/v1/pizzas/{id}` - Atualizar pizza
- `DELETE /api/v1/pizzas/{id}` - Deletar pizza
- `GET /api/v1/pizzas/search` - Buscar pizzas por nome

#### Customer Management (6 endpoints)

- `GET /api/v1/customers` - Listar clientes
- `GET /api/v1/customers/{id}` - Obter cliente por ID
- `GET /api/v1/customers/email/{email}` - Obter cliente por email
- `POST /api/v1/customers` - Criar novo cliente
- `PUT /api/v1/customers/{id}` - Atualizar cliente
- `DELETE /api/v1/customers/{id}` - Deletar cliente

#### Order Management (15 endpoints)

- `GET /api/v1/orders` - Listar pedidos
- `GET /api/v1/orders/{id}` - Obter pedido por ID
- `GET /api/v1/orders/customer/{customerId}` - Pedidos de um cliente
- `GET /api/v1/orders/status/{status}` - Pedidos por status
- `GET /api/v1/orders/search/date-range` - Pedidos por intervalo de datas
- `POST /api/v1/orders` - Criar pedido
- `PUT /api/v1/orders/{id}/confirm` - Confirmar pedido
- `PUT /api/v1/orders/{id}/start-preparing` - Iniciar preparação
- `PUT /api/v1/orders/{id}/mark-ready` - Marcar como pronto
- `PUT /api/v1/orders/{id}/mark-in-delivery` - Marcar em entrega
- `PUT /api/v1/orders/{id}/mark-delivered` - Marcar como entregue
- `PUT /api/v1/orders/{id}/cancel` - Cancelar pedido
- `DELETE /api/v1/orders/{id}` - Deletar pedido

#### Banco de Dados

- ✅ PostgreSQL 13+ integrado
- ✅ 4 tabelas: pizza, customer, orders, order_item
- ✅ 8 pizzas pré-carregadas em `data.sql`
- ✅ Relacionamentos One-to-Many configurados
- ✅ Timestamps de auditoria (created_at, updated_at)

#### Padrões de Design

- ✅ Dependency Injection (@Autowired)
- ✅ Builder Pattern (Lombok @Builder)
- ✅ Factory Pattern (DTOs fromEntity)
- ✅ Repository Pattern (JPA)
- ✅ Service Layer Pattern
- ✅ DTO Pattern
- ✅ Exception Handling Global
- ✅ Transactional Management

#### Testes

- ✅ 10+ testes integrados
- ✅ Testes de fluxo completo
- ✅ Testes de validação
- ✅ Testes de erro

#### Docker

- ✅ Dockerfile multi-stage otimizado
- ✅ docker-compose.yml com PostgreSQL
- ✅ Health checks configurados
- ✅ Volumes persistentes

#### Documentação

- ✅ API_DOCUMENTATION.md - Endpoints e exemplos
- ✅ ARCHITECTURE.md - Padrão VSA explicado
- ✅ DIAGRAMS.md - Visualizações da arquitetura
- ✅ PROJECT_SUMMARY.md - Resumo técnico
- ✅ SETUP_AND_TESTS.md - Setup e testes
- ✅ QUICK_START.md - Guia rápido
- ✅ Exemplos com cURL, Postman, Python

---

## Roadmap Futuro

### Próximas Features

- [ ] Autenticação e Autorização (Spring Security + JWT)
- [ ] Cache com Redis
- [ ] WebSocket para notificações em tempo real
- [ ] Documentação Swagger/OpenAPI
- [ ] Integração com sistema de pagamento
- [ ] Notificações por email
- [ ] Analytics e relatórios
- [ ] Suporte a múltiplos idiomas

### Melhorias

- [ ] Testes com TestContainers
- [ ] CI/CD com GitHub Actions
- [ ] Monitoring com Prometheus/Grafana
- [ ] Logging estruturado com Logback
- [ ] Rate limiting
- [ ] CORS configurável

---

## Versionamento

Este projeto segue [Semantic Versioning](https://semver.org/):

- **MAJOR** - Mudanças incompatíveis (nova arquitetura, breaking changes)
- **MINOR** - Novas features compatíveis (novo endpoint, nova exceção)
- **PATCH** - Correções de bugs (fixes, melhorias pequenas)

---

## Como Contribuir

Para reportar bugs ou sugerir melhorias, por favor:

1. Verifique se a issue já existe
2. Crie uma nova issue descrevendo o problema ou sugestão
3. Inclua exemplos de uso quando possível

---

## Referências

- [Changelog Guideline](https://keepachangelog.com/)
- [Semantic Versioning](https://semver.org/)
- [RFC 9457](https://tools.ietf.org/html/rfc9457)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)

---

**Last Updated**: 28 de Novembro de 2024  
**Maintainer**: @seu-username

