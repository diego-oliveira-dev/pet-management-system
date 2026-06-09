package com.projetos.diego.pet_management_system.util;

import com.projetos.diego.pet_management_system.domain.Pet;

public class PetCreator {
    public static Pet createPetToBeSaved() {
        return Pet.builder()
                .name("Zaya")
                .build();
    }

    public static Pet createValidPet() {
        return Pet.builder()
                .id(1L)
                .name("Zaya")
                .build();
    }
}
