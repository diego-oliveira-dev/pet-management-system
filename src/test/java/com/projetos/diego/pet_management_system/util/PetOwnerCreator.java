package com.projetos.diego.pet_management_system.util;

import com.projetos.diego.pet_management_system.domain.PetOwner;

import java.util.List;

public class PetOwnerCreator {
    public static PetOwner createValidPetOwner() {
        return PetOwner.builder()
                .id(1L)
                .name("Diego Oliveira")
                .pets(List.of())
                .build();
    }
}
