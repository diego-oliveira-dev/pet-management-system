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
