# Parking Management API - SDET Study

Este projeto é uma API REST para gerenciamento de um estacionamento, desenvolvida com Spring Boot. O objetivo principal é consolidar conhecimentos em arquitetura de camadas, persistência de dados e, principalmente, preparar uma base sólida para a aplicação de testes automatizados (Unitários e de API).

---

##  Tecnologias

- **Java 21 & Spring Boot 3**
- **Spring Data JPA** (Persistência de dados)
- **H2 Database** (Banco de dados em memória para agilidade no desenvolvimento)
- **Lombok** (Para um código mais limpo e produtivo)
- **Bean Validation** (Para garantir a integridade dos dados inseridos)

---
##  O que a API faz (Features)

✅ **Gestão de Motoristas:** Cadastro, listagem, atualização e exclusão (CRUD). Valida campos obrigatórios e RG único.  
✅ **Gestão de Veículos:** Vincula carros a motoristas já cadastrados no sistema.  
✅ **Controle de Estacionamento (Tickets):** Registro de entrada e saída de veículos com cálculo automático de tarifação.  
✅ **Regras de Negócio de QA:**
  - Impede a entrada de um veículo que já está estacionado.
  - Validação de existência de motorista antes de vincular um veículo.
  - Tratamento de exceções com retornos JSON claros (404 para não encontrado, 400 para erros de validação).

---

##  Foco em SDET (Quality Assurance)

O projeto foi estruturado seguindo os padrões de mercado para facilitar a automação de testes:

- **Testes de Integração:** Planejados para serem executados com RestAssured.
- **Testes de Unidade:** Foco nas regras de negócio da camada de Service utilizando JUnit 5 e Mockito.
- **Tratamento de Erros:** Implementado para garantir que os testes de "Caminho Infeliz" (Negative Testing) recebam as mensagens de erro corretas.

