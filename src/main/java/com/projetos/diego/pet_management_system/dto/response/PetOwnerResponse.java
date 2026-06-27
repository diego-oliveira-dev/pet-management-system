package com.projetos.diego.pet_management_system.dto.response;

import lombok.Builder;

@Builder
public record PetOwnerResponse(
        Long id,
        String name,
        String username
)
{}