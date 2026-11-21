# Sistema de Helpdesk e Gestão de Chamados — API REST

API RESTful desenvolvida em Java com Spring Boot para gerenciamento de chamados de suporte técnico, controle de usuários e atribuição de atendimentos por perfil de acesso.

## Status do Projeto

* **Back-end (API REST, Segurança, Regras de Negócio e Persistência):** Concluído
* **Testes Automatizados (JUnit 5 e Mockito):** Em desenvolvimento
* **Front-end (Angular 22):** Em desenvolvimento

---

## Arquitetura e Decisões Técnicas

O projeto segue o padrão de **arquitetura em camadas** (`Controller`, `Service`, `Repository`, `Model` e `DTO`), priorizando separação de responsabilidades, segurança e manutenibilidade:

* **Autenticação e Autorização (Spring Security + JWT):** Fluxo *stateless* com geração de tokens com tempo de expiração no login e armazenamento seguro de senhas criptografadas (BCrypt).
* **Filtros de Segurança e Controle de Acesso (RBAC):** Interceptação de requisições para validação de assinatura/validade do token JWT e restrição de rotas conforme o perfil do usuário (ex.: Administrador, Técnico e Cliente).
* **Isolamento de Dados com DTOs:** Uso de objetos de transferência de dados nas entradas e saídas da API, garantindo que informações sensíveis (como hash de senha e metadados internos das entidades) nunca sejam expostas nas respostas JSON.
* **Tratamento Global de Exceções:** Centralização de erros via `@RestControllerAdvice` e `@ExceptionHandler`, convertendo falhas de validação, regras de negócio e acesso negado em respostas HTTP padronizadas sem vazar logs de pilha (*stack trace*) para o cliente.
* **Persistência e Integridade:** Mapeamento objeto-relacional com Spring Data JPA / Hibernate, relacionamentos entre usuários e chamados, Bean Validation nas entidades/DTOs e consultas customizadas nos repositórios.

---

## Tecnologias Utilizadas

* **Linguagem & Framework:** Java 25, Spring Boot 4
* **Segurança:** Spring Security, JSON Web Token (JWT)
* **Persistência:** Spring Data JPA, Hibernate, PostgreSQL
* **Testes:** JUnit 5, Mockito *(em implementação)*
* **Front-end:** Angular 22, TypeScript, Bootstrap 5 *(em implementação)*

---

## Principais Recursos da API

* **Autenticação (`/login`):** Login de usuário com emissão de token JWT e prazo de expiração.
* **Usuários (`/usuarios`):** Cadastro, consulta, atualização e controle de perfis de acesso.
* **Chamados (`/chamados`):** Abertura de tickets, atribuição a técnicos, atualização de status/prioridade e listagem filtrada.

---

## Como Executar Localmente

### Pré-requisitos
* Java 25 e Maven instalados
* PostgreSQL ativo

### Passos
1. Configure as variáveis de conexão com o banco de dados e a chave secreta do JWT no arquivo `src/main/resources/application.properties`
2. Na raiz do projeto, execute a aplicação via Maven:
   ```bash
   ./mvnw spring-boot:run