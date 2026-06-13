package com.projetos.diego.pet_management_system.util;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.requests.PetPostRequestBody;

import java.time.LocalDate;

public class PetPostRequestBodyCreator {
    public static PetPostRequestBody createPetPostRequestBody() {
        return PetPostRequestBody.builder()
                .name("Zaya")
                .type(Pet.Type.DOG)
                .sex(Pet.Sex.FEMALE)
                .birthDate(LocalDate.of(2020, 12, 8))
                .weight(20.0)
                .breed("Vira-lata")
                .postalCode("64009100")
                .owner("Diego Oliveira")
                .build();
    }

    public static PetPostRequestBody createPetPostRequestBodyWithOnlyRequiredFields() {
        return PetPostRequestBody.builder()
                .name("Zaya")
                .type(Pet.Type.DOG)
                .sex(Pet.Sex.FEMALE)
                .birthDate(LocalDate.of(2020, 12, 8))
                .weight(20.0)
                .breed(null)
                .postalCode(null)
                .owner("Diego Oliveira")
                .build();
    }
}
