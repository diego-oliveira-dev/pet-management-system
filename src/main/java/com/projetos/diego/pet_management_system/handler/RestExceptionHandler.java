package com.projetos.diego.pet_management_system.handler;

import com.projetos.diego.pet_management_system.exception.BadRequestException;
import com.projetos.diego.pet_management_system.exception.details.BadRequestExceptionDetails;
import com.projetos.diego.pet_management_system.exception.ResourceNotFoundException;
import com.projetos.diego.pet_management_system.exception.details.ResourceNotFoundExceptionDetails;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class RestExceptionHandler {

    @ExceptionHandler(BadRequestException.class)
    public ResponseEntity<BadRequestExceptionDetails> handleBadRequestException
            (BadRequestException badRequestException) {
        return new ResponseEntity<>(
                BadRequestExceptionDetails.builder()
                        .title("Bad Request Exception")
                        .status(HttpStatus.BAD_REQUEST.value())
                        .details(badRequestException.getMessage())
                        .developerMessage(badRequestException.getClass().getName())
                        .timestamp(LocalDateTime.now())
                        .build(), HttpStatus.BAD_REQUEST
        );
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ResourceNotFoundExceptionDetails> handleResourceNotFoundException
            (ResourceNotFoundException resourceNotFoundException) {
        return new ResponseEntity<>(
                ResourceNotFoundExceptionDetails.builder()
                        .title("Resource Not Found Exception")
                        .status(HttpStatus.NOT_FOUND.value())
                        .details(resourceNotFoundException.getMessage())
                        .developerMessage(resourceNotFoundException.getClass().getName())
                        .timestamp(LocalDateTime.now())
                        .build(), HttpStatus.NOT_FOUND
        );
    }
}
