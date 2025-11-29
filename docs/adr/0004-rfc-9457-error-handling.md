# ADR-004: RFC 9457 para Error Handling

**Status:** Accepted

**Date:** 2024-11-28

**Decision Owner:** API Architect

---

## Context

O projeto necessitava definir um padrão para respostas de erro em APIs REST.

Várias abordagens foram consideradas:

1. **Custom Error Response** - Formato proprietário customizado
2. **HTTP Status Codes Apenas** - Apenas aproveitar status codes HTTP
3. **RFC 9457 (Problem Details)** - Padrão IETF para error handling
4. **GraphQL Errors** - Para uma API GraphQL (não aplicável)

Considerações:
- Compatibilidade com clientes
- Padronização da industria
- Facilidade de implementação
- Informações ricas de erro

## Decision

Adotamos **RFC 9457 - Problem Details for HTTP APIs** como padrão para todas as respostas de erro.

```json
{
  "type": "https://api.example.com/problems/invalid-request",
  "title": "Invalid Request",
  "status": 400,
  "detail": "Order must have at least one item",
  "instance": "/api/v1/orders",
  "timestamp": "2024-11-28T10:30:00Z",
  "errorCode": "VALIDATION_ERROR"
}
```

## Rationale

### Por que RFC 9457

1. **Padrão IETF** - Oficialmente padronizado
2. **Spring Framework 7.x** - Suporte nativo ao `ProblemDetail`
3. **Interoperabilidade** - Clientes já conhecem o padrão
4. **Informações Ricas** - Contexto completo do erro
5. **Rastreabilidade** - Timestamp e instance para debugging
6. **Escalabilidade** - Extensível com campos customizados
7. **Industry Standard** - Adotado por grandes APIs (Google, AWS)

### Benefícios

```
✅ Formato Padrão - Não é proprietário
✅ Clientes Preparados - Bibliotecas já existem
✅ Debugging - Informações completas do erro
✅ Monitoramento - Fácil agregar erros
✅ Documentação - Self-documenting
✅ Spring Native - ProblemDetail é nativo
✅ RESTful - Aproveita HTTP status codes
```

## Consequences

### Positivas ✅

1. Formato padronizado e consistente
2. Clientes podem processar erros genéricamente
3. Informações estruturadas para debugging
4. Suporte nativo em Spring Framework 7.x
5. Fácil implementar global error handler
6. URLs type podem oferecer documentação
7. Monitoramento facilitado
8. Compliance com especificação IETF

### Negativas ⚠️

1. Mais verbose que alternativas simples
2. Clientes precisam conhecer o padrão
3. Requer implementação no GlobalExceptionHandler
4. Pode ter um pequeno overhead de serialização

## Alternatives Considered

### 1. Custom Error Response (Rejeitada)

**Exemplo:**
```json
{
  "error": "Invalid Request",
  "message": "Order must have at least one item",
  "code": "VALIDATION_ERROR",
  "timestamp": "2024-11-28T10:30:00Z"
}
```

**Razão da rejeição:**
- Não é padrão
- Cada empresa faz do seu jeito
- Clientes precisam implementar parser customizado
- Não aproveita HTTP status codes

**Decisão:** Rejeitada - RFC 9457 é melhor

### 2. HTTP Status Codes Apenas (Rejeitada)

**Exemplo:**
```
HTTP/1.1 400 Bad Request
```

**Razão da rejeição:**
- Informação muito limitada
- Clientes não sabem o motivo específico
- Difícil debugging
- Sem contexto

**Decisão:** Rejeitada - RFC 9457 fornece mais contexto

### 3. JSON API Spec (Considerada)

**Exemplo:**
```json
{
  "errors": [{
    "status": "400",
    "code": "VALIDATION_ERROR",
    "title": "Invalid Request",
    "detail": "Order must have at least one item"
  }]
}
```

**Razão da rejeição:**
- Mais complexo que RFC 9457
- Não é suportado nativamente pelo Spring
- Padrão menos consolidado para erro handling

**Decisão:** Rejeitada - RFC 9457 é mais simples

## Status HTTP Mapeados

| Erro | Status | RFC 9457 Type |
|------|--------|---------------|
| Validação | 400 | `/invalid-request` |
| Não autorizado | 401 | `/unauthorized` |
| Não permitido | 403 | `/forbidden` |
| Não encontrado | 404 | `/resource-not-found` |
| Conflito | 409 | `/invalid-operation` |
| Erro interno | 500 | `/internal-server-error` |

## Implementação

### GlobalExceptionHandler

```java
@RestControllerAdvice
public class GlobalExceptionHandler {
    
    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ProblemDetail> handleValidation(
            IllegalArgumentException ex, WebRequest request) {
        
        HttpStatus status = HttpStatus.BAD_REQUEST;
        
        ProblemDetail problem = ProblemDetail.forStatus(status);
        problem.setType(URI.create("https://api.example.com/problems/invalid-request"));
        problem.setTitle("Invalid Request");
        problem.setDetail(ex.getMessage());
        problem.setInstance(URI.create(request.getDescription(false)));
        problem.setProperty("timestamp", LocalDateTime.now());
        problem.setProperty("errorCode", "VALIDATION_ERROR");
        
        return new ResponseEntity<>(problem, status);
    }
}
```

### Exceções Customizadas

```java
public class ResourceNotFoundException extends RuntimeException {}
public class InvalidOperationException extends RuntimeException {}
```

### Cliente HTTP (Exemplo)

```javascript
async function handleApiError(response) {
    if (!response.ok) {
        const problem = await response.json();
        
        console.log(`Error: ${problem.title}`);
        console.log(`Status: ${problem.status}`);
        console.log(`Detail: ${problem.detail}`);
        console.log(`Type: ${problem.type}`);
        
        // Tratar baseado no errorCode
        if (problem.errorCode === 'VALIDATION_ERROR') {
            // Mostrar erro de validação ao usuário
        }
    }
}
```

## Media Type

```
Content-Type: application/problem+json
```

## Extensibilidade

### Campos Customizados

Além dos campos padrão (type, title, status, detail, instance), adicionamos:

```json
{
  "type": "...",
  "title": "...",
  "status": 400,
  "detail": "...",
  "instance": "...",
  
  "timestamp": "2024-11-28T10:30:00Z",
  "errorCode": "VALIDATION_ERROR",
  "traceId": "abc-123-def-456",
  "path": "/api/v1/orders"
}
```

## Documentation

### RFC 9457 Links

- [RFC 9457 Official](https://tools.ietf.org/html/rfc9457)
- [RFC 9457 Summary](https://tools.ietf.org/html/rfc9457#section-3.1)

### Exemplo Completo

Veja: `docs/rfc9457/RFC_9457.md`

## Migration Path

### Fase 1 (Atual)
- GlobalExceptionHandler com RFC 9457
- Exceções: IllegalArgumentException, ResourceNotFoundException, InvalidOperationException

### Fase 2 (Futuro)
- Adicionar validação com @Valid
- Adicionar MethodArgumentNotValidException handler
- Integrar com Spring Security (401, 403)

### Fase 3 (Futuro)
- OpenAPI/Swagger documentação de erros
- Client code generation com tratamento de erros

## Performance Impact

RFC 9457 tem **impacto negligenciável** na performance:

```
Serialização JSON: ~1ms
Memory overhead: ~100 bytes por erro
Network impact: Mínimo (~200-300 bytes por erro)
```

## Related ADRs

- ADR-001: Vertical Slice Architecture
- ADR-002: Spring Boot 4 com Java 21
- ADR-005: DTO Pattern para Transferência de Dados

## References

- [RFC 9457 - Problem Details for HTTP APIs](https://tools.ietf.org/html/rfc9457)
- [Spring ProblemDetail Documentation](https://docs.spring.io/spring-framework/reference/web/webmvc/mvc-exceptionhandlers.html#mvc-ann-rest-exceptions)
- [REST API Best Practices](https://restfulapi.net/http-status-codes/)

## Decision Log

- **2024-11-28**: Decisão aceita - RFC 9457 para error handling

---

**Status:** ✅ ACCEPTED

**Impact:** Alto - Define padrão de resposta de erro

**Reversibility:** Alta - Fácil mudar implementação mantendo RFC 9457

