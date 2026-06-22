package com.projetos.diego.pet_management_system.mapper;

import com.projetos.diego.pet_management_system.domain.PetOwner;
import com.projetos.diego.pet_management_system.dto.PetOwnerResponse;
import org.springframework.stereotype.Component;

@Component
public class PetOwnerMapper {

    public PetOwnerResponse toResponse(PetOwner owner) {
        return PetOwnerResponse.builder()
                .id(owner.getId())
                .name(owner.getName())
                .build();
    }
}
