# Sistema de Cadastro de Pets

Sistema de cadastro de pets desenvolvido em Java com JDBC e MySQL, com foco em prática de arquitetura em camadas, manipulação de banco de dados relacional e organização de responsabilidades no backend.

## Funcionalidades

* Cadastro de pets
* Atualização de dados cadastrados
* Remoção de pets
* Listagem de todos os pets
* Busca por critérios:

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

## Arquitetura

O projeto foi estruturado utilizando separação em camadas, organizando responsabilidades entre:

* controller
* service
* repository
* domain
* view

## Melhorias futuras

* API REST com Spring Boot
* Hibernate / JPA
* Injeção de dependência
* Docker
* Testes automatizados mais completos
