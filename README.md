<p align="center">
  🇧🇷 <a href="#pt">Português</a> •
  🇺🇸 <a href="#en">English</a>
</p>

---

# 🚗 Controle de Estacionamento API <a name="pt"></a>

[![CI](https://github.com/cauanrricardo/parking-control-api-test/actions/workflows/maven.yml/badge.svg)](https://github.com/cauanrricardo/parking-control-api-test/actions/workflows/maven.yml)

API REST para gerenciamento de estacionamento, desenvolvida com foco em **boas práticas de arquitetura** e **testes automatizados** utilizando Spring Boot.

## 🛠 Tecnologias

- Java 17  
- Spring Boot  
- Spring Data JPA  
- PostgreSQL  
- JUnit 5 & Mockito  
- Maven  
- Swagger (OpenAPI)  
- Thymeleaf  

## 🏗 Arquitetura

- Arquitetura em camadas: Controller → Service → Repository  
- Tratamento global de exceções  
- Logs com SLF4J  
- Código orientado a testes  

## 🧪 Testes

```bash
./mvnw test
```

## 🚀 Como executar

```bash
git clone https://github.com/cauanrricardo/parking-control-api-test.git
cd parking-control-api-test
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

- Java 17  
- Spring Boot  
- Spring Data JPA  
- PostgreSQL  
- JUnit 5 & Mockito  
- Maven  
- Swagger (OpenAPI)  
- Thymeleaf  

## 🏗 Architecture

- Layered architecture: Controller → Service → Repository  
- Global exception handling  
- Logging with SLF4J  
- Test-driven code  

## 🧪 Tests

```bash
./mvnw test
```

## 🚀 How to run

```bash
git clone https://github.com/cauanrricardo/parking-control-api-test.git
cd parking-control-api-test
./mvnw spring-boot:run
```

## ⚙️ CI

Pipeline configured with GitHub Actions:

- Automatic build
- Test execution
- Validation on push and pull request
