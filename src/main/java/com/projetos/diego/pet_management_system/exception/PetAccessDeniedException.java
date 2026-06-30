package com.projetos.diego.pet_management_system.exception;

public class PetAccessDeniedException extends RuntimeException {
    public PetAccessDeniedException(String message) {
        super(message);
    }
}
