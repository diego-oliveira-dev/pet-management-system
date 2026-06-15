package com.projetos.diego.pet_management_system.handler;

import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import com.projetos.diego.pet_management_system.exception.*;
import com.projetos.diego.pet_management_system.exception.details.ExceptionDetails;
import com.projetos.diego.pet_management_system.exception.details.FieldValidationExceptionDetails;
import com.projetos.diego.pet_management_system.exception.details.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;

@RestControllerAdvice
public class RestExceptionHandler {

    private ExceptionDetails createExceptionDetails(Exception exception, String title, HttpStatus status) {
        return ExceptionDetails.builder()
                .title(title)
                .status(status.value())
                .details(exception.getMessage())
                .developerMessage(exception.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private ExceptionDetails createExceptionDetailsForFieldValidation(Exception exception,
                                                                      List<ValidationError> validationFieldErrors) {
        return FieldValidationExceptionDetails.builder()
                .errors(validationFieldErrors)
                .title("Validation Failed")
                .status(HttpStatus.BAD_REQUEST.value())
                .details("One or more fields contain invalid values")
                .developerMessage(exception.getClass().getName())
                .timestamp(LocalDateTime.now())
                .build();
    }

    private String extractMessageBasedOnFieldType(Class<?> targetType, Object invalidValue) {
        if (targetType.isEnum()) {
            Object[] validValues = targetType.getEnumConstants();
            return String.format("Invalid value '%s'. Allowed values are '%s'",
                    invalidValue, Arrays.toString(validValues));
        }
        if (targetType.equals(LocalDate.class)) {
           return "Invalid date format. Expected format: yyyy-MM-dd";
        }
        if (Number.class.isAssignableFrom(targetType)) {
            return "Invalid value. Expected a numeric value";
        }
        return "Invalid value. Check the documentation";
    }

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handleResourceNotFound(ResourceNotFoundException exception) {
        ExceptionDetails exceptionDetails = createExceptionDetails(
                exception,
                "Resource Not Found",
                HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(ViaCepPostalCodeNotFoundException.class)
    public ResponseEntity<ExceptionDetails> handlePostalCodeNotFound(ViaCepPostalCodeNotFoundException exception) {
        ExceptionDetails exceptionDetails = createExceptionDetails(
                exception,
                "Postal Code Not Found",
                HttpStatus.NOT_FOUND);

        return new ResponseEntity<>(exceptionDetails, HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPostalCodeException.class)
    public ResponseEntity<ExceptionDetails> handleInvalidPostalCode(InvalidPostalCodeException exception) {
        ExceptionDetails exceptionDetails = createExceptionDetails(
                exception,
                "Invalid Postal Code Format",
                HttpStatus.BAD_REQUEST);

        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(ViaCepUnavailableException.class)
    public ResponseEntity<ExceptionDetails> handleViaCepUnavailable(ViaCepUnavailableException exception) {
        ExceptionDetails exceptionDetails = createExceptionDetails(
                exception,
                "ViaCEP Internal Server Error",
                HttpStatus.INTERNAL_SERVER_ERROR);

        return new ResponseEntity<>(exceptionDetails, HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(ViaCepMalformedDataException.class)
    public ResponseEntity<ExceptionDetails> handleViaCepMalformedData(ViaCepMalformedDataException exception) {
        ExceptionDetails exceptionDetails = createExceptionDetails(
                exception,
                "ViaCEP Bad Gateway",
                HttpStatus.BAD_GATEWAY);

        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_GATEWAY);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionDetails> handleMethodArgumentNotValid(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        List<ValidationError> validationFieldErrors = fieldErrors.stream().map(error ->
                ValidationError.builder()
                        .field(error.getField())
                        .fieldMessage(error.getDefaultMessage())
                        .build()
        ).toList();
        ExceptionDetails exceptionDetails = createExceptionDetailsForFieldValidation(
                exception,
                validationFieldErrors);
        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ExceptionDetails> handleHttpMessageNotReadable(HttpMessageNotReadableException exception) {
        if (exception.getCause() instanceof InvalidFormatException invalidFormatException) {
            String message = extractMessageBasedOnFieldType(
                    invalidFormatException.getTargetType(),
                    invalidFormatException.getValue());
            ValidationError error = ValidationError.builder()
                            .field(invalidFormatException.getPath().getFirst().getFieldName())
                            .fieldMessage(message)
                            .build();
            ExceptionDetails exceptionDetails = createExceptionDetailsForFieldValidation(
                    exception,
                    List.of(error));
            return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
        }
        ValidationError error =ValidationError.builder()
                .field("requestBody")
                .fieldMessage("Malformed JSON request body")
                .build();
        ExceptionDetails exceptionDetails = createExceptionDetailsForFieldValidation(
                exception,
                List.of(error));
        return new ResponseEntity<>(exceptionDetails, HttpStatus.BAD_REQUEST);
    }
}
