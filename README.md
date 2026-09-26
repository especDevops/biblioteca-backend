# Biblioteca Backend API 📚

API REST para gerenciamento de acervo da biblioteca virtual, desenvolvida com Spring Boot e Java moderno.

---

## 🛠️ Tecnologias Utilizadas

- **Java:** 25 (LTS)
- **Framework:** Spring Boot 4.1.1
- **Persistência & ORM:** Spring Data JPA / Hibernate
- **Banco de Dados:** H2 Database (em memória para desenvolvimento) e suporte a PostgreSQL
- **Mapeamento & Utilitários:** MapStruct, Project Lombok
- **Validação:** Jakarta Bean Validation (`@Valid`, `@NotBlank`, `@NotNull`)
- **Testes:** JUnit 5, Mockito, Spring Boot Test

---

## 📋 Pré-requisitos

- **JDK 25 ou superior**:
  > [!IMPORTANT]
  > Este projeto utiliza recursos do Java moderno (como Records e novas diretivas de compilação) configurados para a versão 25 no `pom.xml`.
  > Certifique-se de que a variável de ambiente `JAVA_HOME` esteja apontando para o JDK 25 antes de executar o Maven.

### Localização do JDK na sua máquina Windows:
O JDK 25 está localizado em:
```text
C:\Users\nando\.jdk\jdk-25\jdk-25.0.2
```

---

## 🚀 Passo a Passo para Execução (Desenvolvimento)

### 1. Configurar o JAVA_HOME na sessão

#### Windows (PowerShell):
```powershell
$env:JAVA_HOME = "C:\Users\nando\.jdk\jdk-25\jdk-25.0.2"
$env:Path = "$env:JAVA_HOME\bin;" + $env:Path
```

#### Windows (CMD):
```cmd
set JAVA_HOME=C:\Users\nando\.jdk\jdk-25\jdk-25.0.2
set PATH=%JAVA_HOME%\bin;%PATH%
```

#### Linux / macOS:
```bash
export JAVA_HOME=/caminho/para/jdk-25
export PATH=$JAVA_HOME/bin:$PATH
```

> [!TIP]
> **Definir permanentemente no Windows:**  
> Abra o PowerShell como Administrador ou de Usuário e execute:
> ```powershell
> [System.Environment]::SetEnvironmentVariable("JAVA_HOME", "C:\Users\nando\.jdk\jdk-25\jdk-25.0.2", "User")
> ```

---

### 2. Iniciar a Aplicação

Execute o Maven Wrapper a partir da raiz da pasta `biblioteca-backend`:

#### Windows:
```powershell
.\mvnw.cmd spring-boot:run
```

#### Linux / macOS:
```bash
./mvnw spring-boot:run
```

A aplicação subirá na porta padrão **8080**: `http://localhost:8080`.

---

## 🔗 Endpoints da API & Console H2

| Método | Endpoint | Descrição |
| :--- | :--- | :--- |
| `GET` | `/livros` | Lista todos os livros cadastrados |
| `POST` | `/livros` | Cadastra um novo livro |
| `DELETE` | `/livros/{id}` | Remove um livro pelo ID |

### Exemplo de Payload para Cadastro (`POST /livros`):
```json
{
  "titulo": "O Senhor dos Anéis",
  "autor": "J.R.R. Tolkien",
  "genero": "Fantasia",
  "anoPublicacao": 1954,
  "descricao": "A Sociedade do Anel"
}
```

### Console do Banco H2:
- **URL:** `http://localhost:8080/h2-console`
- **JDBC URL:** `jdbc:h2:mem:testdb`
- **User Name:** `sa`
- **Password:** *(em branco)*

---

## 🧪 Execução de Testes Automatizados

A suíte inclui testes unitários com Mockito e testes de integração com `@SpringBootTest`.

Para rodar todos os testes:
```powershell
.\mvnw.cmd test
```

Para rodar uma classe específica de testes:
```powershell
.\mvnw.cmd test -Dtest=LivroControllerTest
```

---

## 📦 Build e Deploy (Produção)

### 1. Gerar o arquivo JAR executável

```powershell
.\mvnw.cmd clean package -DskipTests
```
O pacote será gerado em: `target/biblioteca-0.0.1-SNAPSHOT.jar`.

---

### 2. Executar o JAR em Produção

Para rodar o arquivo JAR compilado:
```powershell
java -jar target/biblioteca-0.0.1-SNAPSHOT.jar
```

#### Conectando a um PostgreSQL em Produção:
Você pode passar os parâmetros de conexão por variáveis de ambiente sem alterar o código:
```powershell
$env:SPRING_DATASOURCE_URL = "jdbc:postgresql://localhost:5432/biblioteca_db"
$env:SPRING_DATASOURCE_USERNAME = "postgres"
$env:SPRING_DATASOURCE_PASSWORD = "sua_senha_segura"
$env:SPRING_JPA_HIBERNATE_DDL_AUTO = "validate"

java -jar target/biblioteca-0.0.1-SNAPSHOT.jar
```

---

### 3. Execução e Deploy com Docker 🐳

O projeto já conta com [`Dockerfile`](./Dockerfile) e [`.dockerignore`](./.dockerignore) configurados para build multi-stage de produção.

#### Características da imagem:
- **Build Stage**: Utiliza `eclipse-temurin:25-jdk-alpine` com cache inteligente de dependências do Maven Wrapper.
- **Runtime Stage**: Imagem final enxuta baseada em `eclipse-temurin:25-jre-alpine`.
- **Segurança**: Execução sob usuário dedicado não-root (`appuser:appgroup`).
- **Porta padrão**: `8080`.

#### 1. Construir a imagem Docker:
```bash
docker build -t biblioteca-backend .
```

#### 2. Executar o container:
- **Modo padrão (H2 em memória):**
  ```bash
  docker run -d -p 8080:8080 --name backend-container biblioteca-backend
  ```

- **Conectando a um banco PostgreSQL externo:**
  ```bash
  docker run -d -p 8080:8080 --name backend-container \
    -e SPRING_DATASOURCE_URL="jdbc:postgresql://host.docker.internal:5432/biblioteca_db" \
    -e SPRING_DATASOURCE_USERNAME="postgres" \
    -e SPRING_DATASOURCE_PASSWORD="sua_senha_segura" \
    -e SPRING_JPA_HIBERNATE_DDL_AUTO="validate" \
    biblioteca-backend
  ```

#### 3. Comandos úteis:
```bash
docker logs -f backend-container     # Ver logs em tempo real
docker stop backend-container        # Parar o container
docker rm backend-container          # Remover o container
```

---

## ⚙️ Como Deixar Rodando na Máquina (Segundo Plano / Serviço)

### Opção 1: Usando PM2 (Recomendado para Dev/Staging com Node.js instalado)
O PM2 pode gerenciar o processo Java, reiniciar em caso de falha e iniciar automaticamente no boot do Windows:

1. Instale o PM2 globalmente (se já possuir Node.js):
   ```powershell
   npm install -g pm2
   ```

2. Inicie o JAR gerenciado pelo PM2:
   ```powershell
   pm2 start "java -jar target/biblioteca-0.0.1-SNAPSHOT.jar" --name "biblioteca-backend"
   ```

3. Comandos úteis:
   ```powershell
   pm2 status               # Verifica o status
   pm2 logs biblioteca-backend # Visualiza os logs em tempo real
   pm2 stop biblioteca-backend # Para a execução
   pm2 restart biblioteca-backend # Reinicia o serviço
   ```

---

### Opção 2: PowerShell em Segundo Plano (Background Job)
Inicie o processo desanexado do console:
```powershell
Start-Process -FilePath "java" -ArgumentList "-jar", "target\biblioteca-0.0.1-SNAPSHOT.jar" -WindowStyle Hidden
```

- Para verificar se está rodando:
  ```powershell
  Get-Process -Name "java"
  ```
- Para parar o processo:
  ```powershell
  Stop-Process -Name "java"
  ```

---

### Opção 3: Como Serviço do Windows (NSSM - Produção Local)
Para que a aplicação suba automaticamente mesmo após reiniciar a máquina sem precisar de login:

1. Baixe o [NSSM](https://nssm.cc/) (Non-Sucking Service Manager).
2. Abra o terminal como Administrador e instale o serviço:
   ```powershell
   nssm install BibliotecaBackendService "C:\Users\nando\.jdk\jdk-25\jdk-25.0.2\bin\java.exe" "-jar C:\Users\nando\GitHub\biblioteca-backend\target\biblioteca-0.0.1-SNAPSHOT.jar"
   nssm start BibliotecaBackendService
   ```
3. O serviço pode ser controlado pelo painel `services.msc` do Windows.

---

## 📘 Manual Operacional (Runbook)

Para procedimentos de operação contínua, health checks, troubleshooting de incidentes e planos de contingência/rollback, consulte o [Runbook Operacional do Backend](./RUNBOOK.md).

