package com.projetos.diego.pet_management_system.exception.details;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@SuperBuilder
public class FieldValidationExceptionDetails extends ExceptionDetails {
    private List<ValidationError> errors;
}
