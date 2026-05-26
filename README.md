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
