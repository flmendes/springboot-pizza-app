# ADR-009: Spring Framework 7.0.1+ API Versioning

**Status:** Accepted

**Date:** 2024-11-28

**Decision Owner:** API Architect

---

## Context

Durante o desenvolvimento da API, identificamos a necessidade de um padrão consistente e moderno de versionamento de API. Anteriormente, a versão era hardcoded nas URLs (ex: `/v1/pizzas`), o que torna o código menos flexível e mais difícil de manter com múltiplas versões.

O Spring Framework 7.0.1+ introduziu suporte nativo para **API Versioning** através de:
- Atributo `version` nos mapeadores HTTP (`@GetMapping`, `@PostMapping`, etc)
- Propriedades de configuração centralizadas
- Suporte a versionamento via header HTTP

## Decision

Adotamos o **Spring Framework 7.0.1+ API Versioning** removendo versões hardcoded das URLs e utilizando a anotação `version` nos endpoints.

### Configuração

```properties
# API Versioning (Spring Framework 7.0.1+)
spring.mvc.apiversion.default=1
spring.mvc.apiversion.use.header=X-Version
```

### Implementação

**Antes:**
```java
@RestController
@RequestMapping("/v1/pizzas")
public class PizzaController {
    @GetMapping
    public ResponseEntity<List<PizzaResponse>> listAvailablePizzas() { }
}
```

**Depois:**
```java
@RestController
@RequestMapping("/pizzas")
public class PizzaController {
    @GetMapping(version = "1")
    public ResponseEntity<List<PizzaResponse>> listAvailablePizzas() { }
}
```

## Rationale

### Por que API Versioning Nativo

1. **URLs Mais Limpas** - `/pizzas` em vez de `/v1/pizzas`
2. **Declarativo** - Versão especificada no endpoint, não na URL
3. **Flexibilidade** - Fácil suportar múltiplas versões
4. **Moderno** - Segue padrões Spring Framework 7.0.1+
5. **Header Support** - Versionamento via `X-Version` header
6. **Manutenibilidade** - Mudança centralizada em properties

### Comparação com Alternativas

| Aspecto | URL Fixa | Query Param | Header Only | API Versioning |
|---------|----------|------------|------------|-----------------|
| URLs Limpas | ❌ | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Múltiplas Versões | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Manutenibilidade | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Moderno | ❌ | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |
| Escalabilidade | ⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |

## Consequences

### Positivas ✅

1. URLs mais claras e profissionais
2. Versionamento centralizado em `@GetMapping(version = "1")`
3. Fácil adicionar versão 2, 3, etc
4. Suporte a versionamento via header HTTP
5. Alinhado com Spring Framework 7.0.1+
6. Menos duplicação de código
7. Melhor separação de concerns

### Negativas ⚠️

1. Requer Spring Framework 7.0.1+ (já em uso)
2. Mudança na estrutura dos controllers
3. Clientes podem precisar atualizar (se usavam /v1)

## Alternatives Considered

### 1. URL Versioning (Rejeitada)

**Exemplo:**
```java
@RequestMapping("/v1/pizzas")
@RequestMapping("/v2/pizzas")
```

**Pros:** Simples, conhecido

**Cons:** URLs poluídas, hardcoded, duplicação

### 2. Query Parameter Versioning (Rejeitada)

**Exemplo:**
```
GET /pizzas?v=1
GET /pizzas?version=1.0
```

**Pros:** URL limpa

**Cons:** Menos RESTful, menos visível

### 3. Header Only Versioning (Rejeitada)

**Exemplo:**
```bash
curl -H "X-Version: 1" http://localhost/pizzas
```

**Pros:** URL muito limpa

**Cons:** Menos RESTful, menos visível, difícil debugar

## Implementation Details

### Estrutura dos Controllers

```
PizzaController (@RequestMapping("/pizzas"))
├─ @GetMapping(version = "1")                    # Listar pizzas
├─ @GetMapping(path = "/{id}", version = "1")   # Obter pizza
├─ @PostMapping(version = "1")                   # Criar pizza
├─ @PutMapping(path = "/{id}", version = "1")   # Atualizar pizza
├─ @DeleteMapping(path = "/{id}", version = "1") # Deletar pizza
└─ @GetMapping(path = "/search", version = "1") # Buscar pizzas

CustomerController (@RequestMapping("/customers"))
├─ @GetMapping(version = "1")
├─ @GetMapping(path = "/{id}", version = "1")
├─ @GetMapping(path = "/email/{email}", version = "1")
├─ @PostMapping(version = "1")
├─ @PutMapping(path = "/{id}", version = "1")
└─ @DeleteMapping(path = "/{id}", version = "1")

OrderController (@RequestMapping("/orders"))
├─ @GetMapping(version = "1")
├─ @GetMapping(path = "/{id}", version = "1")
├─ @GetMapping(path = "/customer/{customerId}", version = "1")
├─ @GetMapping(path = "/status/{status}", version = "1")
├─ @PostMapping(version = "1")
├─ @PutMapping(path = "/{id}/confirm", version = "1")
├─ @PutMapping(path = "/{id}/start-preparing", version = "1")
├─ @PutMapping(path = "/{id}/mark-ready", version = "1")
├─ @PutMapping(path = "/{id}/mark-in-delivery", version = "1")
├─ @PutMapping(path = "/{id}/mark-delivered", version = "1")
├─ @PutMapping(path = "/{id}/cancel", version = "1")
├─ @DeleteMapping(path = "/{id}", version = "1")
└─ @GetMapping(path = "/search/date-range", version = "1")
```

### Propriedades de Configuração

```properties
spring.mvc.apiversion.default=1
spring.mvc.apiversion.use.header=X-Version
```

### Uso do Header

```bash
# Requisição padrão (usa versão 1.0.0)
curl http://localhost:8080/api/pizzas

# Com versão específica via header
curl -H "X-Version: 1.1" http://localhost:8080/api/pizzas
curl -H "X-Version: 2" http://localhost:8080/api/pizzas
```

## Migration Path

### Fase 1 (Atual - v1)
- PizzaController com `version = "1"`
- CustomerController com `version = "1"`
- OrderController com `version = "1"`

### Fase 2 (Futuro - v2)
```java
@GetMapping(version = "2")
public ResponseEntity<List<PizzaResponseV2>> listAvailablePizzas() {
    // Nova versão com campos adicionais
}
```

### Deprecação de Versão
```java
@GetMapping(version = "1")
@Deprecated(since = "1.1.0", forRemoval = true)
public ResponseEntity<List<PizzaResponse>> listAvailablePizzas() {
    // Marcado para remoção em versão futura
}
```

## Arquivos Modificados

- ✅ `application.properties` - Adicionada configuração
- ✅ `application-test.properties` - Adicionada configuração
- ✅ `PizzaController.java` - Removido `/v1`, adicionado `version = "1"`
- ✅ `CustomerController.java` - Removido `/v1`, adicionado `version = "1"`
- ✅ `OrderController.java` - Removido `/v1`, adicionado `version = "1"`

## Related ADRs

- ADR-001: Vertical Slice Architecture
- ADR-002: Spring Boot 4 com Java 21
- ADR-004: RFC 9457 para Error Handling
- ADR-005: DTO Pattern para Transferência de Dados

## References

- [Spring Framework 7.0.1 Release Notes](https://spring.io/blog/2023/11/09/spring-framework-7-0-1-available-now)
- [Spring MVC RequestMapping](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-ann-requestmapping.html)
- [REST API Versioning Best Practices](https://restfulapi.net/versioning/)

## Decision Log

- **2024-11-28**: Decisão aceita - Spring Framework 7.0.1+ API Versioning implementado

---

**Status:** ✅ ACCEPTED

**Impact:** Médio - Afeta estrutura dos controllers, mas não quebra funcionamento

**Reversibility:** Média - Possível voltar para URL versioning se necessário

