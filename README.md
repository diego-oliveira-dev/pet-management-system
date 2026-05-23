### 🇧🇷 Português

# Sistema de Cadastro de Pets

Aplicação backend desenvolvida em Java com JDBC e MySQL para gerenciamento de cadastro de pets.

O projeto foi criado com foco em prática de desenvolvimento backend, arquitetura em camadas, persistência de dados relacionais e organização de responsabilidades utilizando boas práticas de Programação Orientada a Objetos.

## Funcionalidades

* Cadastro de pets
* Atualização de dados cadastrados
* Remoção de pets
* Listagem de todos os pets
* Busca de pets por:
  * nome
  * sexo
  * idade
  * peso
  * raça
  * endereço

## Tecnologias utilizadas

* Java 21
* JDBC
* MySQL
* Maven
* Lombok
* Log4j2
* JUnit

## Conceitos aplicados

* Programação Orientada a Objetos (POO)
* Arquitetura MVC
* Service Layer
* Repository Pattern
* Separação de responsabilidades
* Persistência de dados com JDBC
* Tratamento de exceções
* Validação e normalização de dados

## Arquitetura

O projeto foi estruturado utilizando arquitetura em camadas para separar responsabilidades entre regras de negócio, acesso a dados e interação com o usuário.

### Camadas principais

* `controller` → coordenação do fluxo da aplicação
* `service` → regras de negócio e validações
* `repository` → acesso e persistência de dados
* `domain` → entidades do sistema
* `view` → interação com usuário via terminal

O acesso ao banco de dados foi implementado manualmente com JDBC utilizando `PreparedStatement` e `ResultSet`.

## Estrutura do projeto

```text
src/main/java
├── controller
├── domain
├── repository
├── service
├── util
└── view
```

## Como executar

### Pré-requisitos

* Java 21
* MySQL
* Maven

### Passos

Clone o repositório:

```bash
git clone https://github.com/diego-oliveira-dev/sistema-de-cadastro-de-pets.git
```

Configure o banco de dados MySQL e atualize as credenciais de conexão da aplicação.

Compile o projeto:

```bash
mvn clean install
```

Execute a aplicação pela classe principal do projeto.
## Próximos passos

* Evoluir o projeto para uma API REST com Spring Boot
* Implementar persistência com JPA/Hibernate
* Adicionar injeção de dependência
* Containerizar a aplicação com Docker
* Expandir cobertura de testes automatizados

---

## 🇺🇸 English

# Pet Management System 

Backend application developed in Java with JDBC and MySQL for managing pet registrations.

The project was created with a focus on backend development practice, layered architecture, relational database persistence, and responsibility separation using Object-Oriented Programming best practices.

## Features

* Register pets
* Update existing pet data
* Delete pets
* List all registered pets
* Search pets by:
  * name
  * sex
  * age
  * weight
  * breed
  * address

## Technologies used

* Java 21
* JDBC
* MySQL
* Maven
* Lombok
* Log4j2
* JUnit

## Applied concepts

* Object-Oriented Programming (OOP)
* MVC Architecture
* Service Layer
* Repository Pattern
* Separation of concerns
* Data persistence using JDBC
* Exception handling
* Data validation and normalization

## Architecture

The project was structured using a layered architecture to separate business rules, data access, and user interaction.

### Main layers

* `controller` → application flow coordination
* `service` → business rules and validations
* `repository` → data access and persistence
* `domain` → system entities
* `view` → terminal-based user interaction

Database access was implemented manually using JDBC with `PreparedStatement` and `ResultSet`.

## Project structure

```text
src/main/java
├── controller
├── domain
├── repository
├── service
├── util
└── view
```

## How to run

### Prerequisites
* Java 21
* MySQL
* Maven

## Steps

Clone the repository:

```bash
git clone https://github.com/diego-oliveira-dev/sistema-de-cadastro-de-pets.git
```

Configure the MySQL database and update the application's database connection credentials.

Build the project:

```bash
mvn clean install
```

Run the application using the main class.

## Future improvements

* Evolve the project into a REST API using Spring Boot
* Implement persistence with JPA/Hibernate
* Add dependency injection
* Containerize the application with Docker
* Expand automated test coverage
