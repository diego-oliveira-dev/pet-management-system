package com.projetos.diego.pet_management_system.dto.response;

import com.projetos.diego.pet_management_system.domain.owner.Address;
import lombok.Builder;

@Builder
public record PetOwnerResponse(
        Long id,
        String name,
        String username,
        Address address
)
{}