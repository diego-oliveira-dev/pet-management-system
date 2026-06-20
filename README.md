# Pet Management System

A REST API built with Spring Boot for managing pets and their owners. The project provides basic functionalities and was developed to solidify my knowledge in Spring Boot and REST APIs.

## Features
- CRUD operations for pet management (create, update, delete, list and find specific pets)
- Paginated and non-paginated listing of pets
- External API integration with ViaCEP to retrieve address information from Brazilian postal codes
- Field validation for POST and PUT requests
- Global exception handling and custom exceptions
- OpenAPI/Swagger documentation
- Automated tests

## Technologies

- Java 21
- Spring Boot
- Spring Data JPA and Hibernate
- Bean Validation
- MySQL
- JUnit 5 and Mockito
- OpenAPI / Swagger

## How to run

### 1. Clone the repository

```bash
git clone https://github.com/diego-oliveira-dev/pet-management-system
cd pet-management-system
```

### 2. Configure environment variables

Copy `.env.example` to `.env` and adjust the values if necessary.

Example:

```env
DB_USER=root
DB_PASSWORD=root
DB_URL=jdbc:mysql://localhost:3306/pet_management_system
```

### 3. Start MySQL

```bash
docker-compose up
```

### 4. Configure Spring Boot

Configure the same environment variables from the `.env` file in your IDE or terminal.

### 5. Run the application

```bash
./mvnw spring-boot:run
```

The database will be created automatically on first startup.

## API Documentation

After starting the application, Swagger UI is available at:

http://localhost:8080/swagger-ui.html

## Next steps

Currently working on the implementation of the following features:
- Basic authentication and authorization using Spring Security
- Owner and pet relationship management
- Refactoring of PUT DTO to allow for changes in other fields
- Integration tests
These changes are currently being developed in the `development-v2` branch.