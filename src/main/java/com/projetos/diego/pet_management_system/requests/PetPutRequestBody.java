package com.projetos.diego.pet_management_system.requests;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PetPutRequestBody {
    @NotNull(message = "Pet id is required")
    @Schema(description = "This is the ID of the pet",
            example = "42",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long id;

    @NotBlank(message = "Pet name cannot be empty")
    @Schema(description = "This is the name that will be applied to the pet",
            example = "Zaya",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;
}
