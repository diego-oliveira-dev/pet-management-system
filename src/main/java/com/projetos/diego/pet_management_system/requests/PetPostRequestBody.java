package com.projetos.diego.pet_management_system.requests;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.projetos.diego.pet_management_system.domain.Pet;
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
public class PetPostRequestBody {
    @NotBlank(message = "Pet name cannot be empty")
    private String name;

    @NotNull(message = "Pet type cannot be null")
    private Pet.Type type;

    @NotNull(message = "Pet sex cannot be null")
    private Pet.Sex sex;

    @Past
    @NotNull(message = "Pet birth date cannot be null")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    @NotNull(message = "Pet weight cannot be empty")
    @DecimalMin(value = "0.5")
    @DecimalMax(value = "150.0")
    private Double weight;

    @Pattern(regexp = "^$|\\s*\\S+.*", message = "Field must not be empty or blank")
    private String breed;

    @Pattern(regexp = "^\\d{8}$", message = "Postal code must be exactly 8 digits long")
    @Pattern(regexp = "^$|\\s*\\S+.*", message = "Field must not be empty or blank")
    private String postalCode;

    @NotBlank(message = "Pet owner cannot be empty")
    private String owner;
}
