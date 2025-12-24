# Stage 1: Build the application
FROM maven:3.9-eclipse-temurin-21 AS builder
WORKDIR /app

# Otimização de cache
COPY pom.xml .
RUN mvn dependency:go-offline

# Compilação
COPY src ./src
RUN mvn clean package -DskipTests

# Stage 2: Create Custom JRE (jlink)
# Criamos uma JRE contendo APENAS os módulos necessários para o Spring Boot
FROM eclipse-temurin:21-jdk AS jlink-builder
WORKDIR /app

# Lista de módulos essenciais para Spring Boot 3+/4+ Web & Data JPA
# java.desktop é necessário para AWT/Beans (comum em libs de imagem ou processamento de beans)
# jdk.unsupported é necessário para frameworks que usam Unsafe (como Netty/Spring)
RUN $JAVA_HOME/bin/jlink \
    --add-modules java.base,java.compiler,java.scripting,java.logging,java.naming,java.desktop,java.management,java.security.jgss,java.instrument,java.sql,jdk.unsupported,java.rmi,java.xml,jdk.crypto.ec,java.net.http,jdk.management \
    --strip-debug \
    --no-man-pages \
    --no-header-files \
    --compress=2 \
    --output /javaruntime

# Stage 3: Extract layers
FROM eclipse-temurin:21-jre AS layers
WORKDIR /app
COPY --from=builder /app/target/pizza-app-0.0.1-SNAPSHOT.jar app.jar
RUN java -Djarmode=layertools -jar app.jar extract

# Stage 4: Runtime - Distroless Base (Sem Java pré-instalado)
# Usamos a imagem "base" e copiamos nossa JRE customizada
FROM gcr.io/distroless/base-debian12:nonroot AS runner
WORKDIR /app

# Copia a JRE customizada do stage 2
COPY --from=jlink-builder /javaruntime /opt/java-runtime

# Adiciona a JRE ao PATH
ENV PATH="/opt/java-runtime/bin:${PATH}"
ENV JAVA_HOME=/opt/java-runtime

# Copia as camadas da aplicação do stage 3
COPY --from=layers /app/dependencies/ ./
COPY --from=layers /app/spring-boot-loader/ ./
COPY --from=layers /app/snapshot-dependencies/ ./
COPY --from=layers /app/application/ ./

EXPOSE 8080

ENV SPRING_DATASOURCE_URL=jdbc:postgresql://postgres:5432/pizza_db
ENV SPRING_DATASOURCE_USERNAME=postgres
ENV SPRING_DATASOURCE_PASSWORD=postgres
ENV SPRING_JPA_HIBERNATE_DDL_AUTO=create-drop

# Executa usando o java da nossa JRE customizada
ENTRYPOINT ["/opt/java-runtime/bin/java", "org.springframework.boot.loader.launch.JarLauncher"]
