# ADR-012: OpenTelemetry para Observabilidade

**Status:** Accepted

**Date:** 2024-11-28

**Decision Owner:** DevOps/Platform Architect

---

## Context

À medida que a aplicação cresce, há necessidade de observabilidade robusta para:

1. **Tracing Distribuído** - Rastrear requisições através de múltiplos serviços
2. **Métricas** - Coletar métricas de performance e negócio
3. **Debugging** - Investigar problemas em produção
4. **Performance** - Identificar gargalos

Anteriormente, essas funcionalidades não existiam, dificultando a observabilidade.

## Decision

Adotamos **OpenTelemetry** com **Spring Boot 4 Starter** (configuração simplificada) usando:

1. **OTLP Collector** - Receptor centralizado de traces e métricas
2. **Jaeger** - Backend para visualização de traces
3. **Prometheus** - Coleta de métricas
4. **Logback Integration** - Correlação de Trace ID com logs

### Arquitetura

```
Aplicação Spring Boot 4
    ↓ (spring-boot-starter-opentelemetry)
OTLP Endpoint (http://localhost:4318)
    ↓
OpenTelemetry Collector
    ├─ Jaeger Exporter (tracing)
    │   └─ Jaeger (localhost:16686)
    │
    └─ Prometheus Exporter (métricas)
        └─ Prometheus (localhost:9090)
```

## Rationale

### Por que Spring Boot 4 Starter

1. **Simplificado** - Uma única dependência para tudo
2. **Auto-configuração** - Zero boilerplate
3. **Padrão CNCF** - Usa OTLP nativo
4. **Agnóstico** - Funciona com qualquer backend (Jaeger, Tempo, Grafana)
5. **Melhor Performance** - Otimizado para Spring 4
6. **Logging Correlacionado** - Integração nativa com Logback

### Comparação com Alternativas

| Aspecto | DataDog | New Relic | Splunk | OpenTelemetry |
|---------|---------|-----------|--------|---------------|
| Open Source | ❌ | ❌ | ⭐⭐ | ⭐⭐⭐⭐⭐ |
| Vendor Lock-in | ⭐⭐ | ⭐⭐ | ⭐⭐ | ⭐⭐⭐⭐⭐ |
| Custo | ❌ | ❌ | ⭐⭐ | FREE |
| CNCF Standard | ✅ | ❌ | ⭐⭐ | ✅ |
| Self-hosted | ⭐⭐ | ❌ | ⭐⭐⭐ | ⭐⭐⭐⭐⭐ |

## Consequences

### Positivas ✅

1. Observabilidade completa (tracing + métricas)
2. Padrão CNCF - futuro-proof
3. Sem vendor lock-in
4. Integração automática com Spring Boot
5. Jaeger e Prometheus open source e gratuitos
6. Fácil escalar para múltiplos serviços
7. Debugging e troubleshooting facilitado

### Negativas ⚠️

1. Overhead de performance (pequeno)
2. Precisa de Jaeger + Prometheus rodando
3. Curva de aprendizado inicial
4. Requer storage para traces e métricas

## Alternatives Considered

### 1. Spring Cloud Sleuth + Zipkin (Rejeitada)

**Pros:** Spring nativo, simples

**Cons:** Menos features, não é padrão CNCF

### 2. DataDog (Rejeitada)

**Pros:** Completo, suporte profissional

**Cons:** Caro, vendor lock-in

### 3. Sem Observabilidade (Rejeitada)

**Pros:** Nenhum overhead

**Cons:** Impossível debugar problemas

## Implementation Details

### Dependências Adicionadas

```xml
<!-- Spring Boot 4 OpenTelemetry Starter (Simplified) -->
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-opentelemetry</artifactId>
</dependency>

<!-- Logback Integration for Trace ID Correlation -->
<dependency>
    <groupId>io.opentelemetry.instrumentation</groupId>
    <artifactId>opentelemetry-logback-appender-1.0</artifactId>
</dependency>

<!-- Prometheus Metrics -->
<dependency>
    <groupId>io.micrometer</groupId>
    <artifactId>micrometer-registry-prometheus</artifactId>
</dependency>
```

### Configuração

**application.yaml (ou properties):**

```yaml
spring:
  application:
    name: springboot-4-example

management:
  opentelemetry:
    # OTLP Endpoint (Collector - Jaeger/Tempo)
    tracing.export.otlp.endpoint: http://localhost:4318/v1/traces
  
  # OTLP Metrics Endpoint
  otlp:
    metrics.export.url: http://localhost:4318/v1/metrics
  
  # Tracing Configuration
  tracing:
    sampling.probability: 0.1
  
  observations:
    enable:
      http.server.requests: true
      http.client.requests: true
  
  # Prometheus Metrics
  endpoints.web.exposure.include: prometheus,health,info,metrics
  metrics.export.prometheus.enabled: true
```

### Logback Configuration (logback.xml)

```xml
<appender name="OTEL" class="io.opentelemetry.instrumentation.logback.v1_0.OpenTelemetryAppender"/>

<encoder>
  <pattern>%d %-5level [%X{trace_id} %X{span_id}] %logger - %msg%n</pattern>
</encoder>
```

### Acessar Dashboards

**Jaeger Tracing:**
```
http://localhost:16686
```

**Prometheus Metrics:**
```
http://localhost:9090
```

**Application Metrics (Prometheus endpoint):**
```
http://localhost:8080/api/actuator/prometheus
```

## O Que é Rastreado Automaticamente

### HTTP Requests
```
GET /api/pizzas
├─ client.request.duration
├─ http.server.request.duration
└─ http.request.size
```

### Database Queries
```
SELECT * FROM pizza
├─ db.client.operations.count
├─ db.client.operation.duration
└─ db.statement
```

### Spring Boot
```
Method Execution
├─ spring.web.request.duration
├─ spring.mvc.request.duration
└─ spring.jpa.statement.duration
```

## Exemplos de Uso

### Consultar Traces em Jaeger

```
Service: springboot-4-example
Operation: GET /api/pizzas
Duration: 50ms
Status: success
```

### Consultar Métricas em Prometheus

```
http_server_requests_seconds_count{method="GET",uri="/api/pizzas"}
http_server_requests_seconds_sum{method="GET",uri="/api/pizzas"}
```

## Grafana Integration (Futuro)

Possível adicionar Grafana para dashboards mais sofisticados:

```yaml
grafana:
  image: grafana/grafana:latest
  ports:
    - "3000:3000"
  environment:
    GF_SECURITY_ADMIN_PASSWORD: admin
  depends_on:
    - prometheus
```

## Logging Integration (Futuro)

Correlacionar logs com traces:

```java
@Slf4j
public class PizzaController {
    @GetMapping
    public ResponseEntity<List<PizzaResponse>> listPizzas() {
        String traceId = Tracer.current().spanContext().getTraceId();
        log.info("Listando pizzas - traceId: {}", traceId);
        // ...
    }
}
```

## Produção Considerations

### Sampling Strategy

```properties
# Produção: Sample 10% dos traces
management.tracing.sampling.probability=0.1

# Desenvolvimento: Sample 100%
management.tracing.sampling.probability=1.0
```

### Storage

```yaml
jaeger:
  environment:
    SPAN_STORAGE_TYPE: badger
  volumes:
    - jaeger_data:/badger/data
```

### Performance Impact

- **Tracing Overhead:** ~2-5%
- **Metrics Overhead:** ~1-2%
- **Total:** ~3-7% (aceitável)

## Related ADRs

- ADR-001: Vertical Slice Architecture
- ADR-002: Spring Boot 4 com Java 21
- ADR-007: Docker Containerization

## References

- [OpenTelemetry Official](https://opentelemetry.io/)
- [Jaeger Documentation](https://www.jaegertracing.io/docs/)
- [Prometheus Documentation](https://prometheus.io/docs/)
- [Spring Boot Observability](https://spring.io/projects/spring-cloud)

## Decision Log

- **2024-11-28**: Decisão aceita - OpenTelemetry implementado

---

**Status:** ✅ ACCEPTED

**Impact:** Médio - Adiciona observabilidade sem quebrar funcionalidade

**Reversibility:** Alta - Fácil remover se necessário

