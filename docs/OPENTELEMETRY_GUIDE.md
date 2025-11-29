# ✅ OpenTelemetry - Guia Completo

## 🎯 O Que é OpenTelemetry?

OpenTelemetry é um conjunto de ferramentas, APIs e SDKs padronizados para coletar dados de observabilidade de aplicações distribuídas, incluindo:

- **Traces (Rastreamento)** - Acompanhar requisições através de múltiplos serviços
- **Metrics (Métricas)** - Coletar dados sobre performance e negócio
- **Logs (Logs)** - Registrar eventos com contexto distribuído

---

## 🚀 Como Iniciar

### 1. Iniciar Containers

```bash
docker-compose up --build
```

Serviços iniciados:
- **PostgreSQL:** localhost:5432
- **Jaeger UI:** http://localhost:16686
- **Prometheus:** http://localhost:9090
- **Aplicação:** http://localhost:8080/api

### 2. Fazer Requisições

```bash
# Listar pizzas (gera traces)
curl http://localhost:8080/api/pizzas | jq

# Ver métricas
curl http://localhost:8080/api/actuator/prometheus | head -20
```

### 3. Visualizar Traces em Jaeger

1. Abra http://localhost:16686
2. Service: `pizza-app`
3. Clique em "Find Traces"
4. Selecione uma trace para detalhar

### 4. Visualizar Métricas em Prometheus

1. Abra http://localhost:9090
2. No campo de query, entre:
   ```
   http_server_requests_seconds_count
   ```
3. Clique em "Graph" para visualizar

---

## 📊 Componentes

### Jaeger (Tracing)

**URL:** http://localhost:16686

**Funcionalidades:**
- Visualizar traces distribuídos
- Ver latência de operações
- Identificar gargalos
- Correlacionar requisições

**Exemplo de Trace:**
```
GET /api/pizzas
├─ PostgreSQL Query (10ms)
│   └─ SELECT * FROM pizza
├─ JSON Serialization (5ms)
└─ HTTP Response (2ms)
Total: 17ms
```

### Prometheus (Metrics)

**URL:** http://localhost:9090

**Métricas Coletadas:**
- HTTP request duration
- HTTP request size
- Database query duration
- JVM memory
- Thread count
- GC statistics

**Exemplo de Query:**
```
rate(http_server_requests_seconds_sum[1m]) / rate(http_server_requests_seconds_count[1m])
```

### Spring Boot Actuator

**URL:** http://localhost:8080/api/actuator

**Endpoints Disponíveis:**
```
/api/actuator/health          # Status da aplicação
/api/actuator/metrics         # Lista de métricas
/api/actuator/prometheus      # Métricas em formato Prometheus
/api/actuator/info            # Informações da app
```

---

## 🔍 Exemplo Prático

### Scenario: Criar Pedido e Rastrear

#### 1. Fazer Requisição

```bash
curl -X POST http://localhost:8080/api/orders \
  -H "Content-Type: application/json" \
  -d '{
    "customerId": 1,
    "items": [
      {
        "pizzaId": 1,
        "quantity": 2,
        "unitPrice": 45.00
      }
    ]
  }' | jq
```

#### 2. Buscar Trace em Jaeger

1. Abra http://localhost:16686
2. Service: `springboot-4-example`
3. Operation: `POST /api/orders` (será exibida)
4. Visualizar a trace completa

#### 3. Ver Detalhes da Trace

```
POST /api/orders
├─ spring.mvc.handleMethod (30ms)
│   ├─ spring.jpa.statement (15ms)
│   │   └─ SELECT customer FROM customer WHERE id=1
│   ├─ spring.jpa.statement (8ms)
│   │   └─ SELECT pizza FROM pizza WHERE id=1
│   └─ spring.jpa.statement (5ms)
│       └─ INSERT INTO orders...
└─ spring.mvc.handleMethod (2ms)

Trace ID: 1234567890abcdef
Duration: 32ms
```

#### 4. Consultar Métrica em Prometheus

```
Query: http_server_requests_seconds_sum{method="POST",uri="/orders"}
Result: 0.032 (32ms)
```

---

## 📈 Métricas Importantes

### HTTP Requests

```
http_server_requests_seconds_count      # Número de requisições
http_server_requests_seconds_sum        # Tempo total
http_server_requests_seconds_max        # Tempo máximo
```

### Database

```
db_client_operation_duration            # Duração de queries
db_client_operation_count               # Número de queries
db_client_connection_pool_size          # Pool de conexões
```

### JVM

```
jvm_memory_used_bytes                   # Memória usada
jvm_memory_max_bytes                    # Memória máxima
jvm_threads_live                        # Threads ativas
jvm_gc_pause_seconds                    # Tempo de GC
```

---

## 🔧 Configuração Avançada

### Mudar Sampling Rate

Em `application.properties`:

```properties
# Produção: 10% dos traces
management.tracing.sampling.probability=0.1

# Desenvolvimento: 100%
management.tracing.sampling.probability=1.0
```

### Adicionar Custom Spans

```java
@Slf4j
public class PizzaService {
    private final Tracer tracer;

    public Pizza createPizza(CreatePizzaRequest request) {
        Span span = tracer.nextSpan().name("pizza.create").start();
        try (Tracer.SpanInScope ws = tracer.withSpan(span)) {
            // Seu código aqui
            span.tag("pizza.name", request.getName());
            span.tag("pizza.price", request.getPrice().toString());
        } finally {
            span.finish();
        }
    }
}
```

### Correlacionar com Logging

```java
@Slf4j
public class OrderController {
    @PostMapping
    public ResponseEntity<OrderResponse> createOrder(CreateOrderRequest request) {
        String traceId = TraceContext.current().traceId();
        log.info("Creating order - traceId: {}", traceId);
        // ...
    }
}
```

---

## 📊 Dashboard com Grafana (Opcional)

### Adicionar ao docker-compose.yml

```yaml
grafana:
  image: grafana/grafana:latest
  ports:
    - "3000:3000"
  environment:
    GF_SECURITY_ADMIN_PASSWORD: admin
    GF_USERS_ALLOW_SIGN_UP: false
  depends_on:
    - prometheus
```

### Acessar Grafana

```
URL: http://localhost:3000
Username: admin
Password: admin
```

### Adicionar Prometheus como Datasource

1. Configuration → Data Sources
2. Add Prometheus
3. URL: http://prometheus:9090
4. Save & Test

---

## 🚨 Troubleshooting

### Traces não aparecem em Jaeger

**Verificar:**
1. Jaeger está rodando: `curl http://localhost:16686`
2. Enviar requisições: `curl http://localhost:8080/api/pizzas`
3. Verificar logs da app

### Métricas vazias em Prometheus

**Verificar:**
1. Prometheus está rodando: `curl http://localhost:9090`
2. Endpoint está correto: `curl http://localhost:8080/api/actuator/prometheus`
3. Requisições estão sendo feitas

### Overhead de Performance Alto

**Solução:**
1. Reduzir sampling: `management.tracing.sampling.probability=0.01`
2. Desabilitar alguns instrumentos:
   ```properties
   otel.instrumentation.jdbc.enabled=false
   ```

---

## 📚 Conceitos Importantes

### Trace

Uma trace representa uma requisição completa através de múltiplos componentes:

```
Trace ID: abc123def456
└─ Span 1: HTTP Request (100ms)
   ├─ Span 2: Database Query (50ms)
   └─ Span 3: JSON Serialization (30ms)
```

### Span

Uma span é uma unidade de trabalho dentro de uma trace:

```
Span {
  traceId: abc123def456
  spanId: xyz789
  operationName: "GET /api/pizzas"
  duration: 50ms
  tags: {
    http.method: "GET"
    http.url: "/api/pizzas"
    http.status_code: 200
  }
}
```

### Sampling

Sampling reduz o volume de dados coletados. Exemplo:

```
0.1 = Coletar 10% das traces
0.01 = Coletar 1% das traces
1.0 = Coletar 100% das traces
```

---

## ✅ Checklist

- [x] OpenTelemetry dependencies adicionadas
- [x] Jaeger configurado
- [x] Prometheus configurado
- [x] Docker-compose atualizado
- [x] prometheus.yml criado
- [x] application.properties com config OTel
- [x] ADR-012 documentado
- [x] Tudo funcionando

---

## 🎯 Próximas Etapas

1. ✅ Jaeger e Prometheus rodando
2. ⏭️ Adicionar Grafana para dashboards
3. ⏭️ Custom spans em classes importantes
4. ⏭️ Alertas em Prometheus
5. ⏭️ Logging correlacionado com traces

---

## 📖 Referências

- [OpenTelemetry Docs](https://opentelemetry.io/docs/)
- [Jaeger Tutorial](https://www.jaegertracing.io/docs/)
- [Prometheus Queries](https://prometheus.io/docs/prometheus/latest/querying/basics/)
- [Spring Boot Observability](https://spring.io/blog/2022/12/01/observability-with-spring-boot-3)

---

**Status:** ✅ OpenTelemetry 100% Funcional!

Pronto para observabilidade profissional! 🚀

