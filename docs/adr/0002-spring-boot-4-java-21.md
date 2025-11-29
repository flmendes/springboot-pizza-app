# ADR-002: Spring Boot 4 com Java 21

**Status:** Accepted

**Date:** 2024-11-28

**Decision Owner:** Arquiteto Senior

---

## Context

O projeto necessitava escolher:

1. **Versão do Spring Boot** - 3.x (stable) vs 4.0 (nova)
2. **Versão do Java** - Java 17 (LTS anterior) vs Java 21 (LTS atual)

Ambas as escolhas têm implicações para:
- Suporte a longo prazo (LTS)
- Features da linguagem
- Performance
- Compatibilidade
- Ecossistema

## Decision

Adotamos:

- **Spring Boot 4.0.0** - Versão mais recente do framework
- **Java 21** - Versão LTS atual (suportada até 2031)

Ambas as escolhas representam as versões mais recentes com suporte LTS.

## Rationale

### Spring Boot 4.0

**Razões para escolher:**

1. **Spring Framework 7.x** - Suporte nativo a RFC 9457 (Problem Details)
2. **Suporte LTS** - Recebera atualizações de segurança até 2026
3. **Melhorias de Performance** - Otimizações em relação ao 3.x
4. **Novas Features** - Virtual Threads, Records, Sealed Classes
5. **Documentação** - Comunidade grande e documentação excelente
6. **Ecossistema** - Todas as bibliotecas suportam Spring Boot 4

### Java 21

**Razões para escolher:**

1. **LTS (Long-Term Support)** - Suportada até setembro de 2031
2. **Features modernas:**
   - Virtual Threads (Project Loom)
   - Pattern Matching
   - Records
   - Sealed Classes
   - Text Blocks
3. **Performance** - Melhorias significativas em relação ao Java 17
4. **Security** - Últimos patches de segurança
5. **Preparation for Future** - Java 25+ traz mais features experimentais

## Consequences

### Positivas ✅

1. **Suporte LTS** - Ambas têm suporte de longo prazo
2. **Moderno** - Acesso a features da linguagem mais recentes
3. **Performance** - Melhorias sobre versões anteriores
4. **RFC 9457** - Spring Boot 4 suporta nativamente
5. **Community** - Grande comunidade e suporte
6. **Future-proof** - Preparado para evoluções futuras
7. **Virtual Threads** - Escalabilidade melhorada

### Negativas ⚠️

1. **Versão Nova** - Spring Boot 4 é relativamente nova
2. **Breaking Changes** - Spring Boot 4 pode ter incompatibilidades com 3.x
3. **Biblioteca Compat** - Algumas bibliotecas podem não ser 100% compatíveis ainda
4. **Documentation** - Menos stack overflow answers vs versões mais antigas
5. **Production Readiness** - Spring Boot 4 foi lançado recentemente

## Alternatives Considered

### 1. Spring Boot 3.x + Java 21 (Parcialmente Rejeitada)

**Pros:**
- Spring Boot 3.x é mais estável
- Menos breaking changes

**Cons:**
- Spring Framework 6.x não tem RFC 9457 nativo
- Menos features de Java 21 aproveitadas
- Versão anterior (não é o latest LTS)

**Decisão:** Rejeitada - Preferir versões mais recentes

### 2. Spring Boot 4.0 + Java 17 (Parcialmente Rejeitada)

**Pros:**
- Spring Boot 4 mais recente com features novas
- Java 17 é uma versão LTS estável

**Cons:**
- Java 17 é LTS anterior
- Perde features de Java 21
- Suporte de Java 17 termina em 2026 (vs 2031 com Java 21)

**Decisão:** Rejeitada - Preferir Java 21 (mais recente e melhor suportado)

### 3. Spring Boot 3.x + Java 17 (Rejeitada)

**Pros:**
- Ambas versões estáveis e maduras
- Grande comunidade
- Muita documentação

**Cons:**
- Versões anteriores
- Sem RFC 9457 nativo
- Menos features modernas

**Decisão:** Rejeitada - Escolher versões mais recentes para aproveitar features

## Trade-offs

| Aspecto | Boot 3.x+Java17 | Boot 3.x+Java21 | Boot 4.0+Java17 | Boot 4.0+Java21 |
|---------|-----------------|-----------------|-----------------|-----------------|
| Estabilidade | ⭐⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ |
| Modernidade | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| RFC 9457 | ❌ | ❌ | ✅ | ✅ |
| Java LTS Vigente | Até 2026 | Até 2031 | Até 2026 | Até 2031 |
| Performance | ⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| **Escolhida** | ❌ | ❌ | ❌ | ✅ |

## Java 21 Features Aproveitadas

### 1. Virtual Threads (Project Loom)

```java
// Suporte automático via Spring Boot 4
// Escalabilidade melhorada para muitas requisições concurrent
```

### 2. Records

```java
// Pode ser usado para DTOs simples
public record PizzaResponse(Long id, String name, BigDecimal price) {}
```

### 3. Pattern Matching

```java
// Simplifica lógica de validação
if (order instanceof Order o && o.getStatus() == PENDING) {
    // ...
}
```

### 4. Text Blocks

```java
// Strings multilinha (já usamos em testes)
String json = """
    {
      "key": "value"
    }
    """;
```

### 5. Sealed Classes

```java
// Pode ser usado para hierarquias de exceções
sealed class ApplicationException permits ValidationException, NotFoundException {}
```

## Spring Boot 4 Features Aproveitadas

### 1. RFC 9457 Support (Problem Details)

```java
// Suporte nativo a ProblemDetail
ProblemDetail problem = ProblemDetail.forStatus(400);
problem.setType(URI.create("..."));
```

### 2. Spring Framework 7.x

- Melhorias de performance
- Novas anotações e features
- Melhor suporte a async

### 3. Native Image Support

- Preparado para GraalVM
- Aplicações mais rápidas em startup

## Versioning Strategy

### Spring Boot 4.0.0

```
- Release: Novembro 2024
- Support: Até Novembro 2026
- Updates: Patches de segurança regulares
```

### Java 21

```
- Release: Setembro 2023
- LTS: Sim
- Support: Até Setembro 2031
```

## Migration Path

Futuro:
1. **Java 25** (Março 2025) - Novas features experimentais
2. **Spring Boot 4.1** - Possíveis improvements
3. **Java 23** (Setembro 2024) - Versão non-LTS (skip)

Não planejamos atualizar até novas versões LTS, a menos que haja razão forte.

## Implementation Details

### Versão do Spring Boot

```xml
<parent>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-parent</artifactId>
    <version>4.0.0</version>
</parent>
```

### Versão do Java

```xml
<properties>
    <java.version>21</java.version>
</properties>
```

### Compiler Config

```xml
<plugin>
    <groupId>org.apache.maven.plugins</groupId>
    <artifactId>maven-compiler-plugin</artifactId>
    <configuration>
        <source>21</source>
        <target>21</target>
    </configuration>
</plugin>
```

## Related ADRs

- ADR-001: Vertical Slice Architecture
- ADR-003: PostgreSQL com JPA/Hibernate
- ADR-004: RFC 9457 para Error Handling

## References

- [Spring Boot 4 Release Notes](https://github.com/spring-projects/spring-boot/wiki/Spring-Boot-4.0-Release-Notes)
- [Java 21 Features](https://docs.oracle.com/en/java/javase/21/docs/api/)
- [Spring Framework 7](https://spring.io/projects/spring-framework)

## Decision Log

- **2024-11-28**: Decisão aceita - Spring Boot 4.0.0 + Java 21

---

**Status:** ✅ ACCEPTED

**Impact:** Alto - Define versões do projeto

**Reversibility:** Baixa - Significante refatoração necessária para downgrade

