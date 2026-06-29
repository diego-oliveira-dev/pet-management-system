package com.projetos.diego.pet_management_system.dto.request;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class RegisterRequest {
    @NotBlank(message = "Pet owner name cannot be empty, blank or null")
    @Schema(description = "This is the name of the owner.",
            example = "Diego Oliveira",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotBlank(message = "Username cannot be empty, blank or null")
    @Schema(description = "This is the username of the user.",
            example = "diego123",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String username;

    @NotBlank(message = "Password cannot be empty, blank or null")
    @Schema(description = "This is the password of the user.",
            example = "secret123",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String password;

    @Pattern(regexp = "^\\d{8}$", message = "Postal code must be exactly 8 digits long")
    @NotBlank(message = "Postal code cannot be empty, blank or null")
    @Schema(description = "This is the postal code of the owner. Must be exactly 8 digits long without empty spaces",
            example = "64009100",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String postalCode;
}
