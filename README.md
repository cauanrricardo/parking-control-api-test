# 🚗 Parking Management API 

[![CI](https://github.com/cauanrricardo/parking-control-api-test/actions/workflows/ci.yml/badge.svg)](https://github.com/cauanrricardo/parking-control-api-test/actions/workflows/ci.yml)
![Java](https://img.shields.io/badge/Java-17-orange?logo=openjdk)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.9-green?logo=springboot)
![Maven](https://img.shields.io/badge/Maven-3.9+-blue?logo=apachemaven)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15+-blue?logo=postgresql)
![Thymeleaf](https://img.shields.io/badge/Thymeleaf-3.1+-green?logo=thymeleaf)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85ea2d?logo=swagger)
![JUnit 5](https://img.shields.io/badge/Testing-JUnit5_&_Mockito-red?logo=junit5)

> API RESTful para gestão de estacionamentos desenvolvida como projeto pessoal, com foco total na aprendizagem de **Testes Unitários (Mockito)** e Arquitetura Spring Boot.

## 📋 Índice

- [Sobre o Projeto](#-sobre-o-projeto)
- [Tecnologias Utilizadas](#-tecnologias-utilizadas)
- [Destaques Técnicos](#-destaques-técnicos--refatoração)
- [Pré-requisitos](#-pré-requisitos)
- [Como Executar](#-como-executar-o-projeto)
- [Documentação da API](#-documentação-da-api-swagger)
- [Interface Web](#-interface-web-thymeleaf)
- [Testes](#-testes)
- [CI/CD](#-cicd)
- [Contribuição](#-contribuição)
- [Licença](#-licença)

---

## 📖 Sobre o Projeto

Este projeto consiste em uma API robusta para controle de entrada e saída de veículos, cálculo de permanência e gerenciamento de tickets de estacionamento. 

O foco principal deste repositório foi a aplicação de **Boas Práticas de Engenharia de Software**, saindo de uma implementação básica para uma arquitetura profissional, testável e documentada.

---

## 🛠️ Tecnologias Utilizadas

### Backend
* **Linguagem:** Java 17
* **Framework:** Spring Boot 3.5.9
* **Banco de Dados:** PostgreSQL 15+
* **Persistência:** Spring Data JPA / Hibernate
* **Documentação:** SpringDoc OpenAPI (Swagger UI)
* **Testes:** JUnit 5 & Mockito
* **Ferramentas:** Maven, Lombok

### Frontend
* **Motor de Templates:** Thymeleaf 3.1+ (Server-Side Rendering)
* **Interface Web:** Views HTML renderizadas pelo Thymeleaf para gerenciamento de motoristas

> 💡 **Sobre o Thymeleaf**: Este projeto utiliza **Thymeleaf** como motor de templates para renderizar páginas HTML dinâmicas no lado do servidor. As views estão localizadas em `src/main/resources/templates/` e fornecem uma interface web simples para visualização e cadastro de motoristas.
---

## ⚙️ Destaques Técnicos & Refatoração

Este projeto passou por uma refatoração completa para garantir qualidade de código e manutenibilidade:

### 1. Arquitetura em Camadas (Clean Architecture)
Separação estrita de responsabilidades:
* **Controller:** Apenas recebe requisições e devolve `ResponseEntity` com status HTTP corretos (200, 201, 404).
* **Service:** Contém toda a regra de negócio e validações, utilizando `@Transactional` para integridade dos dados.
* **Repository:** Interface com o banco de dados.

### 2. Estratégia de Testes Avançada 
Cobertura de testes unitários robusta utilizando **JUnit 5** e **Mockito**:
* **Organização com `@Nested`:** Testes agrupados hierarquicamente por funcionalidade (ex: `class Salvar`, `class Listar`).
* **Legibilidade:** Uso de `@DisplayName` para descrever os cenários de teste em linguagem natural.
* **Cenários:** Cobertura de "Caminho Feliz" (Sucesso) e "Caminho de Erro" (Exceções).
* **Assertividade:** Validação estrita com `assertThrows`, `verify` e `never()` do Mockito.

### 3. Tratamento Global de Erros (Exception Handler)
Implementação de um `GlobalExceptionHandler` centralizado:
* Captura exceções customizadas como `MotoristaNotFoundException` ou `PlacaDuplicadaException`.
* Retorna JSONs de erro padronizados e amigáveis para o cliente da API.

### 4. Observabilidade e Documentação
* **Logs:** Uso do `@Slf4j` para monitoramento das operações críticas no Service.
* **Swagger UI:** Documentação interativa disponível em `/swagger-ui.html`, permitindo testar a API sem necessidade de ferramentas externas.

### 5. Interface Web com Thymeleaf
* **Templates HTML:** Views renderizadas no servidor para gerenciamento de motoristas
* **Localização:** `src/main/resources/templates/motoristas/`
* **Integração:** Integração completa com os controllers Spring MVC

---

## 📦 Pré-requisitos

Antes de executar o projeto, certifique-se de ter instalado:

* ☕ **Java 17** ou superior
* 🐘 **PostgreSQL 15+** instalado e em execução
* 📦 **Maven 3.9+** (ou use o Maven wrapper incluído: `./mvnw`)
* 🔧 **Git** para clonar o repositório

---

## 🚀 Como Executar o Projeto

### 1️⃣ Clone o repositório

```bash
git clone https://github.com/cauanrricardo/parking-control-api-test.git
cd parking-control-api-test
```

### 2️⃣ Configure o Banco de Dados

Crie um banco de dados PostgreSQL com o nome desejado:

```sql
CREATE DATABASE parking_db;
```

### 3️⃣ Configure as credenciais do banco

Edite o arquivo `src/main/resources/application.properties` (ou `application.yml`) com suas credenciais:

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/parking_db
spring.datasource.username=seu_usuario
spring.datasource.password=sua_senha
```

### 4️⃣ Execute a aplicação

Usando Maven Wrapper (recomendado):
```bash
./mvnw spring-boot:run
```

Ou usando Maven instalado:
```bash
mvn spring-boot:run
```

### 5️⃣ Acesse a aplicação

* **API REST:** `http://localhost:8080`
* **Swagger UI (Documentação):** `http://localhost:8080/swagger-ui/index.html`
* **Interface Web (Thymeleaf):** `http://localhost:8080/motoristas`

---

## 📚 Documentação da API (Swagger)

Este projeto utiliza **SpringDoc OpenAPI** para documentação automática da API.

**Acesse:** `http://localhost:8080/swagger-ui/index.html`

Através do Swagger UI você pode:
- ✅ Visualizar todos os endpoints disponíveis
- ✅ Testar requisições diretamente pelo navegador
- ✅ Ver os modelos de request/response
- ✅ Entender os códigos de status HTTP retornados

---

## 🖥️ Interface Web (Thymeleaf)

O projeto possui uma **interface web** desenvolvida com **Thymeleaf**, que permite a visualização e gerenciamento de motoristas através de páginas HTML renderizadas no servidor.

### Recursos da Interface Web:
- 📋 **Lista de Motoristas:** Visualize todos os motoristas cadastrados
- ➕ **Cadastro de Motoristas:** Formulário para adicionar novos motoristas
- 🔄 **Integração com Backend:** Comunicação direta com os serviços Spring

### Acesso:
* **URL:** `http://localhost:8080/motoristas`
* **Templates:** Localizados em `src/main/resources/templates/motoristas/`

>  **Nota:** As views Thymeleaf complementam a API REST, oferecendo uma interface visual para operações básicas.

---

## 🧪 Testes

O projeto possui **cobertura abrangente de testes unitários** com **JUnit 5** e **Mockito**.

### Executar todos os testes:

```bash
./mvnw test
```

### Executar testes com perfil específico:

```bash
./mvnw test -Dspring.profiles.active=test
```

### Estrutura de Testes:
- ✅ **Testes de Service:** Validação de regras de negócio
- ✅ **Testes de Controller:** Validação de endpoints REST
- ✅ **Cobertura de cenários:** Happy path e casos de erro
- ✅ **Mocking:** Isolamento de dependências com Mockito

---

## 🔄 CI/CD

Este repositório possui **pipeline de CI (Integração Contínua)** configurado com **GitHub Actions**.

### O que é executado automaticamente:
- ✅ Build do projeto com Maven
- ✅ Execução de todos os testes unitários
- ✅ Validação a cada push e pull request na branch `main`
- ✅ Utiliza Java 17 e perfil de testes com H2

### Status do Build:
[![CI](https://github.com/cauanrricardo/parking-control-api-test/actions/workflows/ci.yml/badge.svg)](https://github.com/cauanrricardo/parking-control-api-test/actions/workflows/ci.yml)

📌 **Workflow:** [`.github/workflows/ci.yml`](https://github.com/cauanrricardo/parking-control-api-test/blob/main/.github/workflows/ci.yml)

---

## 🤝 Contribuição

Contribuições são bem-vindas! Para contribuir:

1. Faça um fork do projeto
2. Crie uma branch para sua feature (`git checkout -b feature/MinhaFeature`)
3. Commit suas mudanças (`git commit -m 'Adiciona MinhaFeature'`)
4. Push para a branch (`git push origin feature/MinhaFeature`)
5. Abra um Pull Request

---

## 👤 Autor

**Cauan Ricardo**

- GitHub: [@cauanrricardo](https://github.com/cauanrricardo)
- Repositório: [parking-control-api-test](https://github.com/cauanrricardo/parking-control-api-test)

---

<div align="center">
  
**⭐ Se este projeto foi útil, considere dar uma estrela!**

</div>
