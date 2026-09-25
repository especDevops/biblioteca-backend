# ==========================================
# Etapa 1: Build da aplicação (JDK 25)
# ==========================================
FROM eclipse-temurin:25-jdk-alpine AS builder

WORKDIR /app

# Copia arquivos do Maven Wrapper e configuração de dependências
COPY .mvn/ .mvn/
COPY mvnw pom.xml ./

# Garante permissão de execução e quebra de linha Unix para o wrapper
RUN chmod +x ./mvnw && sed -i 's/\r$//' ./mvnw

# Baixa as dependências do projeto para cache de camadas
RUN ./mvnw dependency:go-offline -B || true

# Copia o código-fonte da aplicação
COPY src/ src/

# Compila e empacota a aplicação em formato JAR
RUN ./mvnw clean package -DskipTests

# ==========================================
# Etapa 2: Execução com JRE enxuto
# ==========================================
FROM eclipse-temurin:25-jre-alpine

WORKDIR /app

# Cria usuário sem privilégios de root para segurança
RUN addgroup -S appgroup && adduser -S appuser -G appgroup

# Copia o artefato executável gerado
COPY --from=builder /app/target/*.jar app.jar

# Configura o usuário da execução
USER appuser:appgroup

# Expõe a porta padrão do Spring Boot
EXPOSE 8080

# Inicializa a aplicação
ENTRYPOINT ["java", "-jar", "app.jar"]
