# ADR-001: Vertical Slice Architecture

**Status:** Accepted

**Date:** 2024-11-28

**Decision Owner:** Arquiteto Senior

---

## Context

No início do projeto, precisávamos decidir sobre o padrão de organização de código que seria usado. Havia várias opções disponíveis, cada uma com seus próprios trade-offs:

1. **Arquitetura em Camadas (N-Layer)** - Organização por tipo de responsabilidade
2. **Vertical Slice Architecture** - Organização por feature/domínio
3. **Arquitetura Hexagonal** - Organização por limites de domínio
4. **Domain-Driven Design (DDD)** - Design orientado ao domínio

O projeto é um backend para gerenciar pedidos de pizzas, com funcionalidades bem definidas (Pizza Management, Customer Management, Order Management).

## Decision

Adotamos **Vertical Slice Architecture (VSA)** como padrão de organização do projeto.

Cada slice (pizza, customer, order) é uma unidade independente e autossuficiente que contém:

- **Domain Layer** - Entidades e lógica de domínio
- **Infrastructure Layer** - Acesso a dados (Repositories)
- **Application Layer** - Orquestração e lógica de negócio
- **Presentation Layer** - REST Controllers e DTOs

## Rationale

### Por que VSA foi escolhida:

1. **Independência** - Cada slice pode ser desenvolvida, testada e deployada independentemente
2. **Escalabilidade** - Fácil de adicionar novos slices sem afetar os existentes
3. **Manutenibilidade** - Código relacionado fica junto, fácil de encontrar
4. **Microserviços** - Cada slice pode se tornar um microserviço no futuro
5. **Team Autonomy** - Diferentes times podem trabalhar em diferentes slices
6. **Testabilidade** - Cada slice pode ser testada isoladamente
7. **Clarity** - Visão clara de toda uma funcionalidade em um único lugar

### Por que NÃO as alternativas:

- **N-Layer Architecture**: Muito acoplamento horizontal, difícil de remover features
- **Hexagonal**: Mais complexa para um projeto pequeno/médio
- **DDD**: Requer modelagem de domínio mais complexa, overkill para este projeto

## Consequences

### Positivas ✅

1. Cada slice é independente e autossuficiente
2. Fácil adicionar novas features (novos slices)
3. Código bem organizado e fácil de localizar
4. Preparado para evolução para microserviços
5. Melhor separação de responsabilidades
6. Testes isolados por slice
7. Documentação de arquitetura mais clara

### Negativas ⚠️

1. Código duplicado pode ocorrer entre slices
2. Compartilhamento de código pode ser mais complexo
3. Requer disciplina de desenvolvimento
4. Pode gerar pastas extras em comparação com n-layer
5. Curva de aprendizado para developers não familiarizados com VSA

## Alternatives Considered

### 1. N-Layer Architecture (Rejeitada)

**Estrutura:**
```
src/
├── controller/
├── service/
├── repository/
└── entity/
```

**Razão da rejeição:**
- Alto acoplamento horizontal
- Mudança em uma feature afeta múltiplas pastas
- Difícil remover ou refatorar features
- Menos escalável para múltiplos times

### 2. Hexagonal Architecture (Rejeitada)

**Estrutura:**
```
src/
├── application/
├── domain/
├── ports/
└── adapters/
```

**Razão da rejeição:**
- Mais complexa que o necessário para este projeto
- Curva de aprendizado mais alta
- Overkill para o escopo atual

### 3. Domain-Driven Design (Rejeitada)

**Estrutura:**
```
src/
├── bounded_contexts/
├── shared_kernel/
└── infrastructure/
```

**Razão da rejeição:**
- Requer análise de domínio mais profunda
- Overhead para projeto de tamanho pequeno/médio
- Pode ser adotado futuramente se necessário

## Trade-offs

| Aspecto | N-Layer | Hexagonal | DDD | VSA |
|---------|---------|-----------|-----|-----|
| Facilidade | ⭐⭐⭐⭐⭐ | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ |
| Escalabilidade | ⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Manutenibilidade | ⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| Testabilidade | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Para Microserviços | ⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |

## Implementation Details

VSA foi implementada com:

1. **Slices independentes:**
   - `pizza/` - Gerenciamento de pizzas
   - `customer/` - Gerenciamento de clientes
   - `order/` - Gerenciamento de pedidos

2. **Camadas dentro de cada slice:**
   - `domain/` - Entidades e lógica de domínio
   - `infrastructure/` - Repositories e acesso a dados
   - `application/` - Services e orquestração
   - `presentation/` - Controllers e DTOs

3. **Shared Layer:**
   - `shared/` - Código compartilhado (exceções, configurações)

## Related ADRs

- ADR-002: Spring Boot 4 com Java 21
- ADR-003: PostgreSQL com JPA/Hibernate

## References

- [Vertical Slice Architecture - Jimmy Bogard](https://jimmybogard.com/vertical-slice-architecture/)
- [Clean Architecture - Robert C. Martin](https://blog.cleancoder.com/uncle-bob/2012/08/13/the-clean-architecture.html)
- [Modular Monolith - Simon Brown](https://www.codingthearchitecture.com/2014/01/24/c4_model_notations.html)

## Decision Log

- **2024-11-28**: Decisão aceita após análise de alternativas e discussão de trade-offs

---

**Status:** ✅ ACCEPTED

**Impact:** Alto - Define toda a estrutura do projeto

**Reversibility:** Média - Possível refatorar para outro padrão, mas requer esforço significativo

