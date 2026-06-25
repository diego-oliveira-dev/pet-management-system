package com.projetos.diego.pet_management_system.mapper;

import com.projetos.diego.pet_management_system.domain.owner.PetOwner;
import com.projetos.diego.pet_management_system.dto.PetOwnerRequest;
import com.projetos.diego.pet_management_system.dto.PetOwnerResponse;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class PetOwnerMapper {

    public PetOwnerResponse toResponse(PetOwner owner) {
        return PetOwnerResponse.builder()
                .id(owner.getId())
                .name(owner.getName())
                .build();
    }

    public PetOwner fromPostRequestToEntity(PetOwnerRequest request) {
        return PetOwner.builder()
                .name(request.getName())
                .pets(List.of())
                .build();
    }
}
