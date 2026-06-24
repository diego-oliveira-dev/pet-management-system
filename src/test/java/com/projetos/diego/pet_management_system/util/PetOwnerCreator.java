package com.projetos.diego.pet_management_system.util;

import com.projetos.diego.pet_management_system.domain.PetOwner;
import com.projetos.diego.pet_management_system.dto.PetOwnerRequest;
import com.projetos.diego.pet_management_system.dto.PetOwnerResponse;

import java.util.List;

public class PetOwnerCreator {
    public static PetOwner createValidPetOwner() {
        return PetOwner.builder()
                .id(1L)
                .name("Diego Oliveira")
                .pets(List.of())
                .build();
    }

    public static PetOwnerResponse createResponse(PetOwner owner) {
        return PetOwnerResponse.builder()
                .id(owner.getId())
                .name(owner.getName())
                .build();
    }

    public static PetOwnerRequest createPetOwnerPostRequest() {
        return PetOwnerRequest.builder()
                .name("Diego Oliveira")
                .build();
    }
}
