package com.projetos.diego.pet_management_system.requests;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PetPutRequestBody {
    @NotNull(message = "Pet id is required")
    private Long id;
    @NotBlank(message = "Pet name cannot be empty")
    private String name;
}
