package com.projetos.diego.pet_management_system.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.domain.PetOwner;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PetPostRequest {
    @NotBlank(message = "Pet name cannot be empty or blank")
    @Schema(description = "This is the name of the pet.",
            example = "Rex",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private String name;

    @NotNull(message = "Pet type cannot be null")
    @Schema(description = "This is the type of the pet. Allowed values are DOG and CAT.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Pet.Type type;

    @NotNull(message = "Pet sex cannot be null")
    @Schema(description = "This is the type of the pet. Allowed values are MALE and FEMALE.",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Pet.Sex sex;

    @PastOrPresent(message = "Pet birth date cannot be a future date")
    @NotNull(message = "Pet birth date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @Schema(description = "This is the birth date of the pet. Valid date format: yyyy-MM-dd.",
            example = "2022-03-10",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private LocalDate birthDate;

    @NotNull(message = "Pet weight cannot be empty")
    @DecimalMin(value = "0.5", message = "Pet weight must be greater than or equal to 0.5")
    @DecimalMax(value = "150.0", message = "Pet weight must be less than or equal to 150.0")
    @Schema(description = "This is the weight of the pet. Must be a number between 0.5 and 150.0 inclusive.",
            example = "95.3",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Double weight;

    @Pattern(regexp = ".*\\S+.*", message = "Pet breed cannot be empty or blank")
    @Schema(description = "This is the breed of the pet.",
            example = "Golden Retriever",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    private String breed;

    @Pattern(regexp = "^\\d{8}$", message = "Postal code must be exactly 8 digits long")
    @Pattern(regexp = ".*\\S+.*", message = "Postal code cannot be empty or blank")
    @Schema(description = "This is the postal code of the owner. Must be exactly 8 digits long without empty spaces",
            example = "64009100",
            requiredMode = Schema.RequiredMode.NOT_REQUIRED,
            nullable = true)
    private String postalCode;

    @NotNull(message = "Pet owner ID cannot be null")
    @Schema(description = "This is the pet's owner name.",
            example = "Diego Oliveira",
            requiredMode = Schema.RequiredMode.REQUIRED)
    private Long ownerId;
}
