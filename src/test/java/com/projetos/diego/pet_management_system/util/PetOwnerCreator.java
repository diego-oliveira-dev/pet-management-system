package com.projetos.diego.pet_management_system.util;

import com.projetos.diego.pet_management_system.domain.owner.Address;
import com.projetos.diego.pet_management_system.domain.owner.PetOwner;
import com.projetos.diego.pet_management_system.domain.owner.UserRole;
import com.projetos.diego.pet_management_system.dto.request.RegisterRequest;
import com.projetos.diego.pet_management_system.dto.response.PetOwnerResponse;

import java.util.List;

public class PetOwnerCreator {
    public static PetOwner createValidPetOwner() {
        return PetOwner.builder()
                .id(1L)
                .name("Diego Oliveira")
                .username("diego123")
                .password("secret123")
                .role(UserRole.USER)
                .pets(List.of())
                .build();
    }

    public static Address createValidAddress() {
        return Address.builder()
                .street("Rua José Marques da Rocha")
                .neighbourhood("Memorare")
                .city("Teresina")
                .state("PI")
                .postalCode("64009100")
                .build();
    }

    public static PetOwnerResponse createResponse(PetOwner owner) {
        return PetOwnerResponse.builder()
                .id(owner.getId())
                .name(owner.getName())
                .username(owner.getUsername())
                .address(createValidAddress())
                .build();
    }
}
