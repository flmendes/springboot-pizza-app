# 🚀 Como Usar as Configurações Docker

## 📋 Configurações Disponíveis

### 1. **Versão Completa com OpenTelemetry** (Produção)
- **Arquivo:** `docker-compose.yml`
- **Inclui:** PostgreSQL + App + Jaeger + OTLP Collector + Prometheus
- **Observabilidade:** ✅ Completa (traces, metrics, logs)
- **URL Base:** http://localhost:8080

### 2. **Versão Simples** (Desenvolvimento)  
- **Arquivo:** `docker-compose-simple.yml`
- **Inclui:** PostgreSQL + App (sem telemetria)
- **Observabilidade:** ❌ Desabilitada (apenas logs básicos)
- **URL Base:** http://localhost:8080

## 🎯 Como Executar

### Versão Completa (Com OpenTelemetry)
```bash
# Iniciar todos os serviços
docker compose up --build

# Verificar aplicação
curl http://localhost:8080/actuator/health
curl http://localhost:8080/pizzas

# Acessar interfaces
# - Aplicação: http://localhost:8080
# - Jaeger UI: http://localhost:16686
# - Prometheus: http://localhost:9090
```

### Versão Simples (Sem OpenTelemetry)
```bash
# Iniciar apenas app + banco
docker compose -f docker-compose-simple.yml up --build

# Verificar aplicação (sem context-path)
curl http://localhost:8080/actuator/health
curl http://localhost:8080/pizzas

# Apenas aplicação disponível: http://localhost:8080
```

## 🔧 Arquivos Organizados

### Mantidos:
- ✅ `docker-compose.yml` - Configuração completa
- ✅ `docker-compose-simple.yml` - Configuração simples
- ✅ `Dockerfile` - Build principal
- ✅ `Dockerfile.simple` - Build simplificado
- ✅ `otel-collector-config.yaml` - Configuração OpenTelemetry

### Removidos:
- ❌ `docker-compose-debug.yml`
- ❌ `docker-compose-fixed.yml`
- ❌ `docker-compose-minimal.yml`
- ❌ `Dockerfile.fixed`
- ❌ `Dockerfile.minimal`
- ❌ `otel-collector-simple.yaml`
- ❌ `diagnose.sh`

## 📊 Comparação

| Aspecto | Completa | Simples |
|---------|----------|---------|
| Containers | 5 | 2 |
| Memória RAM | ~2GB | ~1GB |
| Tempo de start | 60s | 30s |
| Observabilidade | Full | Básica |
| URL | `/pizzas` | `/pizzas` |
| Health check | `/actuator/health` | `/actuator/health` |

## ✅ Problema do Actuator Resolvido

**Erro anterior:** `404 - Not Found` em `/actuator/health`

**Soluções aplicadas:**
1. ✅ Adicionada dependência `spring-boot-starter-actuator` no `pom.xml`
2. ✅ Configuradas propriedades do Actuator
3. ✅ Corrigidas URLs considerando context-path

**URLs corretas:**
- **Ambas as versões:** `http://localhost:8080/actuator/health`
- **API Gateway gerenciará:** Prefixo `/api` via reverse proxy

## ✅ Problema do OpenTelemetry Collector Resolvido

**Erro anterior:** `mapping key "extensions" already defined at line 56`

**Soluções aplicadas:**
1. ✅ Removida duplicação da seção `extensions` no `otel-collector-config.yaml`
2. ✅ Consolidadas extensões `health_check` e `pprof` em uma única seção
3. ✅ Resolvido conflito de porta 8888 → alterado para 8889
4. ✅ Adicionada porta 8889 no `docker-compose.yml` para métricas Prometheus

**Status:**
- ✅ OTLP Collector rodando nas portas: 4317 (gRPC), 4318 (HTTP), 8889 (metrics)
- ✅ Configuração YAML válida e funcional
- ✅ Pipeline de traces e métricas operacional

---

**Recomendação:** Use a versão simples para desenvolvimento e a completa para produção.
