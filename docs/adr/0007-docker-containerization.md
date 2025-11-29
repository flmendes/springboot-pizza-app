# ADR-007: Docker para Containerização

**Status:** Accepted

**Date:** 2024-11-28

**Decision Owner:** DevOps Architect

---

## Context

O projeto necessitava decidir como fazer deploy da aplicação:

1. **Jar executável** - Rodar JAR diretamente
2. **Docker Container** - Containerizar com Docker
3. **Kubernetes** - Usar K8s para orquestração
4. **Cloud Native** - Serverless (AWS Lambda, etc)

Considerações:
- Desenvolvimento local
- Consistência entre ambientes
- Escalabilidade
- Facilidade de deploy

## Decision

Adotamos **Docker** para containerização com:

1. **Dockerfile** - Multi-stage build otimizado
2. **docker-compose.yml** - Orquestração local
3. **Health checks** - Verificação de saúde
4. **Volumes persistentes** - Dados do PostgreSQL

## Rationale

### Por que Docker

1. **Consistência** - "Funciona em meu PC" → "Funciona em produção"
2. **Isolamento** - Aplicação isolada do sistema
3. **Escalabilidade** - Fácil replicar containers
4. **DevOps** - Padrão de industria
5. **CI/CD** - Integração contínua facilitada
6. **Microserviços** - Preparado para evoluir
7. **Cloud Ready** - Suportado por AWS, GCP, Azure

### Benefícios

```
✅ Reprodutibilidade - Mesmo ambiente local e produção
✅ Portabilidade - Roda em qualquer máquina com Docker
✅ Isolamento - Não afeta outros processos
✅ Escalabilidade - Múltiplos containers facilmente
✅ Simplificação - docker-compose simplifica setup local
✅ DevOps Friendly - Padrão na industria
```

## Consequences

### Positivas ✅

1. Ambiente de desenvolvimento igual produção
2. Onboarding simplificado (just `docker-compose up`)
3. Preparado para CI/CD
4. Escalabilidade horizontal
5. Menos "works on my machine"
6. Isolamento de dependências

### Negativas ⚠️

1. Overhead de aprendizado de Docker
2. Pequeno overhead de performance
3. Necessita Docker instalado
4. Debugging pode ser mais complexo

## Alternatives Considered

### 1. JAR Executável Direto (Rejeitada)

**Pros:**
- Simples
- Sem dependências

**Cons:**
- Diferentes ambientes = problemas
- Setup manual em produção
- Não escalável
- Hard para CI/CD

**Decisão:** Rejeitada - Docker é superior

### 2. Virtual Machines (Rejeitada)

**Pros:**
- Isolamento completo

**Cons:**
- Muito overhead
- Lento para iniciar
- Consome mais recursos
- Docker é mais eficiente

**Decisão:** Rejeitada - Docker é mais eficiente

### 3. Kubernetes (Considerada para futuro)

**Pros:**
- Orquestração profissional
- Auto-scaling

**Cons:**
- Overkill para este projeto agora
- Curva de aprendizado

**Decisão:** Não usar agora - Docker Compose é suficiente

## Implementation

### Dockerfile (Multi-stage)

```dockerfile
# Stage 1: Build
FROM maven:3.9-eclipse-temurin-21 AS builder

WORKDIR /build
COPY pom.xml .
COPY src ./src

RUN mvn clean package -DskipTests

# Stage 2: Runtime
FROM eclipse-temurin:21-jre-alpine

WORKDIR /app

COPY --from=builder /build/target/*.jar app.jar

EXPOSE 8080

ENTRYPOINT ["java", "-jar", "app.jar"]
```

### docker-compose.yml

```yaml
version: '3.8'

services:
  postgres:
    image: postgres:15-alpine
    environment:
      POSTGRES_DB: pizza_db
      POSTGRES_PASSWORD: postgres
    ports:
      - "5432:5432"
    volumes:
      - postgres_data:/var/lib/postgresql/data
    healthcheck:
      test: ["CMD-SHELL", "pg_isready -U postgres"]
      interval: 10s
      timeout: 5s
      retries: 5

  app:
    build: .
    environment:
      SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/pizza_db
      SPRING_DATASOURCE_USERNAME: postgres
      SPRING_DATASOURCE_PASSWORD: postgres
    ports:
      - "8080:8080"
    depends_on:
      postgres:
        condition: service_healthy
    healthcheck:
      test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
      interval: 10s
      timeout: 5s
      retries: 5

volumes:
  postgres_data:
```

## Multi-stage Build

### Benefícios

1. **Imagem Menor** - Apenas runtime necessário
2. **Segurança** - Maven build tools não inclusos
3. **Performance** - Build fast, run fast

### Tamanho da Imagem

```
Sem multi-stage: ~800 MB (Maven + JDK + App)
Com multi-stage: ~150-200 MB (JRE + App)
```

## Health Checks

### PostgreSQL

```yaml
healthcheck:
  test: ["CMD-SHELL", "pg_isready -U postgres"]
  interval: 10s
  timeout: 5s
  retries: 5
```

### Application

```yaml
healthcheck:
  test: ["CMD", "curl", "-f", "http://localhost:8080/actuator/health"]
  interval: 10s
  timeout: 5s
  retries: 5
```

## Volumes

### Data Persistence

```yaml
volumes:
  - postgres_data:/var/lib/postgresql/data
```

### Local Development

```yaml
volumes:
  - .:/app
```

## Environment Configuration

### Local (docker-compose)

```yaml
environment:
  SPRING_PROFILES_ACTIVE: local
  SPRING_DATASOURCE_URL: jdbc:postgresql://postgres:5432/pizza_db
```

### Production

```
Environment variables:
- SPRING_PROFILES_ACTIVE=production
- SPRING_DATASOURCE_URL=jdbc:postgresql://prod-db:5432/pizza_db
- etc
```

## Commands

### Build Image

```bash
docker build -t pizza-backend:1.0.0 .
```

### Run Local

```bash
docker-compose up --build
```

### Run Single Container

```bash
docker run -p 8080:8080 -e SPRING_DATASOURCE_URL=... pizza-backend:1.0.0
```

## CI/CD Integration

### GitHub Actions Example

```yaml
- name: Build Docker Image
  run: docker build -t pizza-backend:${{ github.sha }} .

- name: Push to Registry
  run: docker push pizza-backend:${{ github.sha }}
```

## Security Considerations

### Base Image

```dockerfile
# ✅ BOM - Alpine é pequeno e seguro
FROM eclipse-temurin:21-jre-alpine

# ❌ RUIM - Full JDK desnecessário em produção
FROM eclipse-temurin:21-jdk
```

### Secrets

```bash
# ❌ RUIM - Secrets no Dockerfile
RUN echo "password=secret" > config.properties

# ✅ BOM - Secrets via environment variables
docker run -e DB_PASSWORD=$SECURE_PASSWORD ...
```

## Performance Optimization

### Layer Caching

```dockerfile
# ✅ Bom - Aproveita cache
COPY pom.xml .
RUN mvn dependency:go-offline
COPY src ./src
RUN mvn package

# ❌ Ruim - Invalida cache
COPY . .
RUN mvn package
```

### Resource Limits

```yaml
services:
  app:
    deploy:
      resources:
        limits:
          cpus: '1'
          memory: 512M
        reservations:
          cpus: '0.5'
          memory: 256M
```

## Logging

### Container Logs

```bash
docker logs -f pizza-backend
```

### Log Forwarding (Futuro)

```yaml
logging:
  driver: "json-file"
  options:
    max-size: "10m"
    max-file: "3"
```

## Related ADRs

- ADR-002: Spring Boot 4 com Java 21
- ADR-003: PostgreSQL com JPA/Hibernate

## References

- [Docker Official Documentation](https://docs.docker.com/)
- [Docker Best Practices](https://docs.docker.com/develop/dev-best-practices/)
- [docker-compose Reference](https://docs.docker.com/compose/compose-file/)

## Decision Log

- **2024-11-28**: Decisão aceita - Docker para containerização

---

**Status:** ✅ ACCEPTED

**Impact:** Médio - Facilita deployment e escalabilidade

**Reversibility:** Alta - Fácil remover Docker se necessário

