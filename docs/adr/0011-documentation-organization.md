# ADR-011: Organização de Documentação - GitHub Standard

**Status:** Accepted

**Date:** 2024-11-28

**Decision Owner:** Project Lead

---

## Context

O projeto havia acumulado uma grande quantidade de arquivos de documentação na raiz (15+ arquivos `.md`), o que gerava:

1. Raiz poluída e desorganizada
2. Difícil de navegar
3. Não seguia padrões GitHub
4. Experiência de usuário ruim
5. Sem estrutura clara

## Decision

Adotamos o padrão de organização de documentação recomendado pelo GitHub:

1. **Raiz minimalista** - Apenas `README.md` e `CHANGELOG.md`
2. **Documentação centralizada** - Tudo em pasta `docs/`
3. **Organização por categorias** - Subdivisão lógica
4. **Padrão GitHub** - Seguindo convenções da comunidade

### Estrutura Final

```
raiz/
├── README.md          (Documentação principal)
├── CHANGELOG.md       (Histórico de versões)
└── docs/
    ├── INDEX.md       (Índice de navegação)
    ├── guides/        (Guias práticos)
    ├── architecture/  (Arquitetura)
    ├── api/          (Documentação de API)
    ├── rfc9457/      (RFC 9457)
    └── adr/          (Architecture Decision Records)
```

## Rationale

### Por que Padrão GitHub

1. **Reconhecível** - Desenvolvedores já conhecem
2. **Profissional** - Projeto vê-se maduro
3. **Escalável** - Fácil adicionar mais docs
4. **Navegável** - Estrutura clara
5. **Standard** - Aceito pela comunidade
6. **Manutenível** - Organização lógica

### Comparação de Abordagens

| Aspecto | Tudo na Raiz | Único README | Pasta docs |
|---------|-------------|-------------|-----------|
| Clareza | ⭐ | ⭐⭐ | ⭐⭐⭐⭐⭐ |
| Organização | ❌ | ⭐⭐ | ⭐⭐⭐⭐⭐ |
| Profissionalismo | ⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Escalabilidade | ⭐ | ⭐⭐ | ⭐⭐⭐⭐⭐ |
| GitHub Standard | ❌ | ⭐⭐ | ⭐⭐⭐⭐⭐ |

## Consequences

### Positivas ✅

1. Raiz limpa e profissional
2. Documentação bem organizada
3. Fácil para novos developers entenderem
4. Pronto para colaboração
5. Padrão conhecido
6. Escalável para novo conteúdo
7. Melhor experiência de usuário

### Negativas ⚠️

1. Requer buscar documentação em `docs/`
2. Mais diretórios para navegar
3. Inicialmente confuso para estrutura

## Alternatives Considered

### 1. Tudo na Raiz (Rejeitada)

**Pros:** Fácil acessar

**Cons:** Desorganizado, não profissional, não escalável

### 2. Apenas README.md (Rejeitada)

**Pros:** Simples, focado

**Cons:** Documentação incompleta, não escalável

### 3. Múltiplas Pastas Soltas (Rejeitada)

**Pros:** Organizado

**Cons:** Sem padrão, confuso, não é GitHub standard

## Implementation Details

### Raiz - Arquivos Mantidos

```
README.md              # Porta de entrada
CHANGELOG.md          # Histórico de versões
pom.xml               # Build Maven
docker-compose.yml    # Orquestração
Dockerfile            # Build Docker
mvnw / mvnw.cmd       # Maven wrapper
src/                  # Código-fonte
docs/                 # Documentação
```

### Pastas de Documentação

**docs/guides/** - Guias de Início
```
QUICK_START.md          (5 minutos)
SETUP_AND_TESTS.md      (Setup local)
GETTING_STARTED.md      (Guia completo)
```

**docs/architecture/** - Arquitetura
```
VERTICAL_SLICE_ARCHITECTURE.md  (Padrão)
PROJECT_ARCHITECTURE.md         (Detalhes)
DIAGRAMS.md                     (Visualizações)
```

**docs/api/** - API Reference
```
ENDPOINTS.md     (Todos os endpoints)
EXAMPLES.md      (Exemplos de uso)
```

**docs/rfc9457/** - RFC 9457
```
RFC_9457.md        (Especificação)
TESTS.md          (Testes práticos)
IMPLEMENTATION.md (Detalhes técnicos)
CHANGELOG.md      (Histórico)
```

**docs/adr/** - Architecture Decision Records
```
README.md
0001-vertical-slice-architecture.md
0002-spring-boot-4-java-21.md
0003-postgresql-jpa.md
... (até ADR-011)
```

### README.md na Raiz

**Conteúdo:**
```markdown
# 🍕 Pizza Order Backend

## Quick Summary
- Descrição breve
- Badges (versão, licença, status)
- Links para docs

## Quick Start
- 5 minutos para começar

## 📚 Documentação
- Link para docs/INDEX.md

## Stack Tecnológico
- Resumo das versões

## Como Contribuir
- Diretrizes básicas
```

### CHANGELOG.md na Raiz

**Conteúdo:**
```markdown
# Changelog

## [1.1.0] - 2024-11-28

### Added
- RFC 9457 implementation
- H2 Database for tests
- API Versioning

### Changed
- Documentation reorganization
- Lombok warning fixed

### Related
- See docs/ for full details
```

## Impacto da Reorganização

### Antes
- 15+ arquivos `.md` na raiz
- Difícil de navegar
- Não profissional

### Depois
- 2 arquivos na raiz
- 15+ organizados em `docs/`
- Profissional e escalável
- 86% redução de clutter

## Migration Path

### Fase 1 (Atual)
- Estrutura básica em docs/
- 8 ADRs criados
- Documentação centralizada

### Fase 2 (Futuro)
- Mais ADRs conforme necessário
- Página wiki do GitHub
- Blog de desenvolvimento

## Arquivos Reorganizados

**De raiz para docs/guides/**
- QUICK_START.md
- SETUP_AND_TESTS.md
- GETTING_STARTED.md

**De raiz para docs/architecture/**
- VERTICAL_SLICE_ARCHITECTURE.md
- PROJECT_ARCHITECTURE.md
- DIAGRAMS.md

**De raiz para docs/api/**
- ENDPOINTS.md
- EXAMPLES.md

**De raiz para docs/rfc9457/**
- RFC_9457.md
- TESTS.md
- IMPLEMENTATION.md
- CHANGELOG.md

**De raiz para docs/adr/**
- 0001 a 0008 (criados anteriormente)

**Removidos (Obsoletos)**
- 19 arquivos `.md` e `.txt` obsoletos

## Related ADRs

- ADR-001: Vertical Slice Architecture
- ADR-004: RFC 9457 para Error Handling

## References

- [GitHub - Documentation Best Practices](https://docs.github.com/en/repositories/managing-your-repositorys-settings-and-features/customizing-your-repository/about-readmes)
- [Readme Standard](https://github.com/othneildrew/Best-README-Template)
- [Google - Open Source Documentation](https://google.github.io/styleguide/docguide/)

## Decision Log

- **2024-11-28**: Decisão aceita - Documentação reorganizada com padrão GitHub

---

**Status:** ✅ ACCEPTED

**Impact:** Médio - Afeta navegação, não afeta funcionalidade

**Reversibility:** Alta - Fácil reorganizar se necessário

