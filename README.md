# Biblioteca Backend API 📚

API REST para gerenciamento do acervo de uma biblioteca, desenvolvida com Spring Boot, autenticação JWT e persistência em PostgreSQL.

---

## 🛠️ Tecnologias

- Java 21 no `pom.xml` (compatível com JDK 21+; a imagem Docker pode usar Temurin 25)
- Spring Boot 4.1.1
- Spring Web MVC
- Spring Data JPA / Hibernate
- PostgreSQL como banco principal em execução local/produção
- Spring Security com OAuth2 Resource Server e JWT
- MapStruct e Lombok
- Bean Validation com Jakarta Validation
- JUnit 5 + Spring Boot Test

---

## 📋 Pré-requisitos

- JDK 21 ou superior recomendado
- Maven Wrapper incluído no projeto (`mvnw` / `mvnw.cmd`)
- Banco PostgreSQL disponível, ou uso do perfil de testes com H2

> Importante: a configuração padrão em `src/main/resources/application.yaml` aponta para PostgreSQL em `localhost:5432`, não para H2. O H2 é usado no perfil de teste (`src/test/resources/application-test.properties`).

### JDK local no Windows

Exemplo padrão da máquina local:

```text
C:\Users\nando\.jdk\jdk-25\jdk-25.0.2
```

---

## 🚀 Execução local

### 1. Preparar o ambiente Java

#### Windows (PowerShell)
```powershell
$env:JAVA_HOME = "C:\Users\nando\.jdk\jdk-25\jdk-25.0.2"
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
```

#### Windows (CMD)
```cmd
set JAVA_HOME=C:\Users\nando\.jdk\jdk-25\jdk-25.0.2
set PATH=%JAVA_HOME%\bin;%PATH%
```

#### Linux / macOS
```bash
export JAVA_HOME=/caminho/para/jdk-21-ou-25
export PATH=$JAVA_HOME/bin:$PATH
```

---

### 2. Iniciar a aplicação

Na raiz do projeto:

#### Windows
```powershell
.\mvnw.cmd spring-boot:run
```

#### Linux / macOS
```bash
./mvnw spring-boot:run
```

A aplicação sobe em:

```text
http://localhost:8080
```

---

## 🔐 Autenticação e autorização

O backend usa autenticação JWT e controle de acesso por perfis:

- `ADMIN`
- `PADRAO`

Regras principais de segurança:

- `/auth/**` → público
- `/h2-console/**` → público (quando habilitado no perfil de teste)
- `GET /livros/**` → permitido para `ADMIN` e `PADRAO`
- `POST/PUT/PATCH/DELETE /livros/**` → permitido somente para `ADMIN`

---

## 🔗 Endpoints principais

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `POST` | `/auth/cadastro` | Cria um novo usuário |
| `POST` | `/auth/login` | Autentica usuário e retorna JWT |
| `GET` | `/livros` | Lista livros |
| `GET` | `/livros/{id}` | Busca livro por ID |
| `POST` | `/livros` | Cria livro |
| `PUT` | `/livros/{id}` | Atualiza livro completo |
| `PATCH` | `/livros/{id}` | Atualização parcial |
| `DELETE` | `/livros/{id}` | Remove livro |

### Cadastro de usuário (`POST /auth/cadastro`)

```json
{
  "nome": "Maria Silva",
  "email": "maria@biblioteca.com",
  "senha": "123456",
  "perfil": "ADMIN"
}
```

### Login (`POST /auth/login`)

```json
{
  "email": "maria@biblioteca.com",
  "senha": "123456"
}
```

### Criação de livro (`POST /livros`)

```json
{
  "titulo": "O Senhor dos Anéis",
  "autor": "J.R.R. Tolkien",
  "genero": "Fantasia",
  "anoPublicacao": 1954,
  "descricao": "A Sociedade do Anel"
}
```

---

## 🧪 Testes

Para executar todos os testes:

```powershell
.\mvnw.cmd test
```

Para rodar uma classe específica:

```powershell
.\mvnw.cmd test -Dtest=LivroControllerTest
```

O perfil de teste usa H2 em memória:

```properties
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.username=sa
spring.datasource.password=
```

---

## 📦 Build e execução em produção

### Gerar o JAR

```powershell
.\mvnw.cmd clean package -DskipTests
```

O JAR será gerado em:

```text
target\biblioteca-0.0.1-SNAPSHOT.jar
```

### Executar o JAR

```powershell
java -jar target\biblioteca-0.0.1-SNAPSHOT.jar
```

### Configuração de banco em produção

A aplicação usa PostgreSQL por padrão. Exemplo de configuração via variáveis de ambiente:

```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/biblioteca"
$env:SPRING_DATASOURCE_USERNAME = "postgres"
$env:SPRING_DATASOURCE_PASSWORD = "postgres"
$env:JWT_SECRET = "sua_chave_segura_aqui"

java -jar target\biblioteca-0.0.1-SNAPSHOT.jar
```

Valores configurados na aplicação:

- `SPRING_DATASOURCE_URL`: `jdbc:postgresql://localhost:5432/biblioteca`
- `SPRING_DATASOURCE_USERNAME`: `postgres`
- `SPRING_DATASOURCE_PASSWORD`: `postgres`
- `JWT_SECRET`: `FEÇD58D4531F5827E82374S5WD51Ç72D` (valor padrão para ambiente local)
- `WEB_ORIGENS_PERMITIDAS`: `http://localhost:5173`

---

## 🐳 Docker

Há dois Dockerfiles na raiz do projeto:

- `Dockerfile` — imagem baseada em Temurin 25
- `Dockerfile.backend` — build alternativo com JDK/JRE 21

### Construir a imagem principal

```bash
docker build -t biblioteca-backend .
```

### Executar o container

```bash
docker run -d -p 8080:8080 --name biblioteca-backend biblioteca-backend
```

### Conectar a PostgreSQL externo

```bash
docker run -d -p 8080:8080 --name biblioteca-backend \
  -e SPRING_DATASOURCE_URL="jdbc:postgresql://host.docker.internal:5432/biblioteca" \
  -e SPRING_DATASOURCE_USERNAME="postgres" \
  -e SPRING_DATASOURCE_PASSWORD="postgres" \
  -e JWT_SECRET="sua_chave_segura" \
  biblioteca-backend
```

### Comandos úteis

```bash
docker logs -f biblioteca-backend
docker stop biblioteca-backend
docker rm biblioteca-backend
```

---

## ⚙️ Execução em segundo plano

### Opção 1: PM2

```powershell
npm install -g pm2
pm2 start "java -jar target\biblioteca-0.0.1-SNAPSHOT.jar" --name "biblioteca-backend"
```

Comandos úteis:

```powershell
pm2 status
pm2 logs biblioteca-backend
pm2 restart biblioteca-backend
pm2 stop biblioteca-backend
```

### Opção 2: PowerShell em background

```powershell
Start-Process -FilePath "java" -ArgumentList "-jar", "target\biblioteca-0.0.1-SNAPSHOT.jar" -WindowStyle Hidden
```

---

## 📘 Runbook

Para procedimentos operacionais, troubleshooting, verificações de saúde e rollback, consulte o [RUNBOOK.md](./RUNBOOK.md).

