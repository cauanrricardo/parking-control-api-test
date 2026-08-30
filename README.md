<p align="center">
  🇧🇷 <a href="#pt">Português</a> •
  🇺🇸 <a href="#en">English</a>
</p>
---

# 🚗 Controle de Estacionamento API <a name="pt"></a>

[![CI](https://github.com/cauanrricardo/parking-control-api-test/actions/workflows/maven.yml/badge.svg)](https://github.com/cauanrricardo/parking-control-api-test/actions/workflows/maven.yml)

API REST para gerenciamento de estacionamento, desenvolvida com foco em **boas práticas de arquitetura** e **testes automatizados** utilizando Spring Boot.

## 🛠 Tecnologias

- Java 21
- Spring Boot 3.5
- Spring Data JPA  
- PostgreSQL  
- JUnit 5 & Mockito  
- Maven  
- Swagger (OpenAPI)  
- Thymeleaf
- Docker Compose

## 🏗 Arquitetura

- Arquitetura em camadas: Controller → Service → Repository  
- Tratamento global de exceções  
- Logs com SLF4J  
- Código orientado a testes  

## 🚀 Executar com Docker

Pré-requisito: Docker com o plugin Compose instalado.

```bash
git clone https://github.com/cauanrricardo/parking-control-api-test.git
cd parking-control-api-test
docker compose up --build
```

O Compose cria a imagem da API, inicia o PostgreSQL, aguarda o banco ficar saudável e então inicia a aplicação.

- Aplicação web: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

Para encerrar:

```bash
docker compose down
```

Os dados ficam preservados no volume `postgres_data`. Para também apagar os dados locais:

```bash
docker compose down -v
```

As credenciais padrão são somente para desenvolvimento. Para personalizá-las, copie `.env.example` para `.env` antes de iniciar o Compose.

## 🧪 Testes

```bash
./mvnw test
```

## 💻 Executar sem Docker

É necessário ter Java 21 e PostgreSQL disponíveis. Configure `DB_URL`, `DB_USERNAME` e `DB_PASSWORD` quando os valores locais forem diferentes dos padrões de desenvolvimento.

```bash
export DB_URL=jdbc:postgresql://localhost:5432/parking-control-db
export DB_USERNAME=postgres
export DB_PASSWORD=sua-senha
./mvnw spring-boot:run
```

## ⚙️ CI

Pipeline configurado com GitHub Actions:

- Build automático
- Execução de testes
- Validação em push e pull request

---

# 🚗 Parking Control API <a name="en"></a>

[![CI](https://github.com/cauanrricardo/parking-control-api-test/actions/workflows/maven.yml/badge.svg)](https://github.com/cauanrricardo/parking-control-api-test/actions/workflows/maven.yml)

REST API for parking management, developed with a focus on **architecture best practices** and **automated testing** using Spring Boot.

## 🛠 Technologies

- Java 21
- Spring Boot 3.5
- Spring Data JPA  
- PostgreSQL  
- JUnit 5 & Mockito  
- Maven  
- Swagger (OpenAPI)  
- Thymeleaf
- Docker Compose

## 🏗 Architecture

- Layered architecture: Controller → Service → Repository  
- Global exception handling  
- Logging with SLF4J  
- Test-driven code  

## 🚀 Run with Docker

Prerequisite: Docker with the Compose plugin installed.

```bash
git clone https://github.com/cauanrricardo/parking-control-api-test.git
cd parking-control-api-test
docker compose up --build
```

Compose builds the API image, starts PostgreSQL, waits for the database to become healthy, and then starts the application.

- Web application: http://localhost:8080
- Swagger UI: http://localhost:8080/swagger-ui/index.html
- OpenAPI JSON: http://localhost:8080/v3/api-docs

To stop the services:

```bash
docker compose down
```

Data is preserved in the `postgres_data` volume. To also remove local data:

```bash
docker compose down -v
```

The default credentials are intended for development only. To customize them, copy `.env.example` to `.env` before starting Compose.

## 🧪 Tests

```bash
./mvnw test
```

## 💻 Run without Docker

Java 21 and PostgreSQL must be available. Configure `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD` when your local values differ from the development defaults.

```bash
export DB_URL=jdbc:postgresql://localhost:5432/parking-control-db
export DB_USERNAME=postgres
export DB_PASSWORD=your-password
./mvnw spring-boot:run
```

## ⚙️ CI

Pipeline configured with GitHub Actions:

- Automatic build
- Test execution
- Validation on push and pull request
