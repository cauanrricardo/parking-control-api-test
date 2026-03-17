 # 🇧🇷 Parking Management API

[![CI](https://github.com/cauanrricardo/parking-control-api-test/actions/workflows/maven.yml/badge.svg)](https://github.com/cauanrricardo/parking-control-api-test/actions/workflows/ci.yml)

API REST para gerenciamento de estacionamento, com foco em boas práticas de arquitetura e testes automatizados com Spring Boot.

## Tecnologias

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- JUnit 5 & Mockito
- Maven
- Swagger (OpenAPI)
- Thymeleaf

## Arquitetura

- Camadas: Controller → Service → Repository  
- Tratamento global de exceções  
- Logs com SLF4J  
- Código orientado a testes  

## Testes

./mvnw test

## Como executar

bash
git clone https://github.com/cauanrricardo/parking-control-api-test.git
cd parking-control-api-test
./mvnw spring-boot:run

## CI

- Pipeline configurado com GitHub Actions:
- Build automático
- Execução de testes
- Validação em push e pull request

---

# 🇺🇸 Parking Management API

[![CI](https://github.com/cauanrricardo/parking-control-api-test/actions/workflows/maven.yml/badge.svg)](https://github.com/cauanrricardo/parking-control-api-test/actions/workflows/ci.yml)

REST API for parking management, focused on clean architecture and automated testing using Spring Boot.

##  Tech Stack

- Java 17
- Spring Boot
- Spring Data JPA
- PostgreSQL
- JUnit 5 & Mockito
- Maven
- Swagger (OpenAPI)
- Thymeleaf

##  Architecture

- Layered architecture: Controller → Service → Repository  
- Global exception handling  
- Logging with SLF4J  
- Test-oriented design  

##  Running the project

```bash
git clone https://github.com/cauanrricardo/parking-control-api-test.git
cd parking-control-api-test
./mvnw spring-boot:run
