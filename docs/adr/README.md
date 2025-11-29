# Architecture Decision Records (ADR)

Este diretório contém todos os Architecture Decision Records (ADRs) do projeto Pizza Order Backend.

## O que é um ADR?

Um **Architecture Decision Record** é um documento que captura uma decisão arquitetural importante tomada durante o desenvolvimento do projeto, incluindo:

- **Contexto**: Por que a decisão foi necessária?
- **Decisão**: Qual foi a decisão tomada?
- **Consequências**: Quais são as consequências positivas e negativas?
- **Alternativas consideradas**: O que mais foi considerado?
- **Rationale**: Por que essa foi a melhor opção?

## Por que ADRs?

✅ **Rastreabilidade** - Saber o histórico de decisões  
✅ **Justificativa** - Entender o "por quê" das decisões  
✅ **Comunicação** - Documentar discussões e consensos  
✅ **Manutenção** - Facilitar futuras mudanças  
✅ **Onboarding** - Novos membros entendem a arquitetura  

## Formato

Cada ADR segue o template:

```markdown
# ADR-001: Título da Decisão

**Status:** Accepted/Rejected/Deprecated/Superseded by ADR-XXX

**Date:** YYYY-MM-DD

## Context

Descrição do contexto e problema.

## Decision

Qual foi a decisão.

## Rationale

Por que essa decisão.

## Consequences

Impactos positivos e negativos.

## Alternatives Considered

- Alternativa 1: Por que rejeitada
- Alternativa 2: Por que rejeitada

## Related ADRs

- Relacionado com ADR-XXX
```

## ADRs Existentes

| # | Título | Status | Data |
|---|--------|--------|------|
| [001](./0001-vertical-slice-architecture.md) | Vertical Slice Architecture | Accepted | 2024-11-28 |
| [002](./0002-spring-boot-4-java-21.md) | Spring Boot 4 com Java 21 | Accepted | 2024-11-28 |
| [003](./0003-postgresql-jpa.md) | PostgreSQL com JPA/Hibernate | Accepted | 2024-11-28 |
| [004](./0004-rfc-9457-error-handling.md) | RFC 9457 para Error Handling | Accepted | 2024-11-28 |
| [005](./0005-dto-pattern.md) | DTO Pattern para Transferência de Dados | Accepted | 2024-11-28 |
| [006](./0006-lombok-utility.md) | Lombok para Redução de Boilerplate | Accepted | 2024-11-28 |
| [007](./0007-docker-containerization.md) | Docker para Containerização | Accepted | 2024-11-28 |
| [008](./0008-order-status-state-machine.md) | State Machine para Status de Pedidos | Accepted | 2024-11-28 |
| [009](./0009-api-versioning.md) | Spring Framework 7.0.1+ API Versioning | Accepted | 2024-11-28 |
| [010](./0010-h2-test-database.md) | H2 Database para Testes | Accepted | 2024-11-28 |
| [011](./0011-documentation-organization.md) | Organização de Documentação - GitHub Standard | Accepted | 2024-11-28 |
| [012](./0012-opentelemetry-observability.md) | OpenTelemetry para Observabilidade | Accepted | 2024-11-28 |

## Como Adicionar um Novo ADR

1. Crie um arquivo: `000X-titulo-da-decisao.md`
2. Use o template acima
3. Incremente o número
4. Atualize este README

## Referências

- [ADR GitHub](https://adr.github.io/)
- [Michael Nygard's ADR Blog](http://thinkrelevant.com/blog/2011/11/15/documenting-architecture-decisions/)
- [ADR Tool](https://github.com/npryce/adr-tools)

---

**Last Updated**: 28 de Novembro de 2024

