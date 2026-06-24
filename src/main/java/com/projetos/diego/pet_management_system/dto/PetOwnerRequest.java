package com.projetos.diego.pet_management_system.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PetOwnerRequest {
    @NotBlank(message = "Pet owner name cannot be empty, blank or null")
    @Schema(description = "This is the name of the owner.",
            example = "Diego Oliveira",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
