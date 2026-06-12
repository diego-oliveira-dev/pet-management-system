package com.projetos.diego.pet_management_system.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class PostalCodeNotFoundException extends RuntimeException {
    public PostalCodeNotFoundException(String message) {
        super(message);
    }
}
