package com.projetos.diego.pet_management_system.exception;

public class InvalidPostalCodeException extends RuntimeException {
    public InvalidPostalCodeException(String message) {
        super(message);
    }
}
