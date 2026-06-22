package com.projetos.diego.pet_management_system.dto;

import lombok.Builder;

@Builder
public record PetOwnerResponse(
        Long id,
        String name
)
{}