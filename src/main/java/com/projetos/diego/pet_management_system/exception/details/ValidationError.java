package com.projetos.diego.pet_management_system.exception.details;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class ValidationError {
    private String field;
    private String fieldMessage;
}
