package com.projetos.diego.pet_management_system.requests;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class PetPostRequestBody {
    @NotBlank(message = "Pet name cannot be empty")
    private String name;
}
