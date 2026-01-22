#  Parking Management API 

> API RESTful para gestão de estacionamentos desenvolvida como projeto pessoal, com foco total na aprendizagem de **Testes Unitários (Mockito)** e Arquitetura Spring Boot.

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.5.9-green)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-blue)
![Swagger](https://img.shields.io/badge/Swagger-OpenAPI-85ea2d)
![JUnit 5](https://img.shields.io/badge/Testing-JUnit5_&_Mockito-red)

##  Sobre o Projeto

Este projeto consiste em uma API robusta para controle de entrada e saída de veículos, cálculo de permanência e gerenciamento de tickets de estacionamento. 

O foco principal deste repositório foi a aplicação de **Boas Práticas de Engenharia de Software**, saindo de uma implementação básica para uma arquitetura profissional, testável e documentada.

---

##  Tecnologias Utilizadas

* **Linguagem:** Java 17
* **Framework:** Spring Boot 3
* **Banco de Dados:** PostgreSQL
* **Persistência:** Spring Data JPA / Hibernate
* **Documentação:** SpringDoc OpenAPI (Swagger UI)
* **Testes:** JUnit 5 & Mockito
* **Ferramentas:** Maven, Lombok

---

##  Destaques Técnicos & Refatoração

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

### 5. CI (Integração Contínua) com GitHub Actions
Este repositório possui um pipeline de **CI** configurado com **GitHub Actions** para garantir qualidade e estabilidade a cada mudança no código:

- Executa automaticamente **build + testes** a cada **push** e **pull request** na branch `main`
- Usa **Java 17** no runner
- Roda os testes com profile `test` (H2) para evitar dependência do PostgreSQL no ambiente de CI
- Publica relatórios de teste (Surefire) como artefato (quando configurado)

📌 Workflow: `.github/workflows/ci.yml`

---

## Como Executar o Projeto

### Pré-requisitos
* Java 17 instalado.
* PostgreSQL instalado e rodando.
* Maven.

### Passo a Passo

1.  **Clone o repositório:**
    ```bash
    git clone [https://github.com/seu-usuario/parking-api.git](https://github.com/seu-usuario/parking-api.git)
    ```

2.  **Configure o Banco de Dados:**
    Crie um banco de dados no PostgreSQL com suas respectivas credencias.

3.  **Execute a aplicação:**
    ```bash
    mvn spring-boot:run
    ```

4.  **Acesse a Documentação (Swagger):**
    Abra o navegador em: `http://localhost:8080/swagger-ui/index.html`
