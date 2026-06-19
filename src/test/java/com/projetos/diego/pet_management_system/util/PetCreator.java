package com.projetos.diego.pet_management_system.util;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.dto.PetPostRequest;
import com.projetos.diego.pet_management_system.dto.PetPutRequest;

import java.time.LocalDate;

public class PetCreator {
    public static Pet createPetToBeSaved() {
        return createGenericPet();
    }

    public static Pet createPetToBeSavedWithOnlyRequiredFields() {
        Pet pet = createGenericPet();
        pet.setBreed(null);
        pet.setAddress(null);
        return pet;
    }

    public static Pet createValidPet() {
        Pet pet = createGenericPet();
        pet.setId(1L);
        return pet;
    }

    public static Pet createGenericPet() {
        return Pet.builder()
                .name("Zaya")
                .type(Pet.Type.DOG)
                .sex(Pet.Sex.FEMALE)
                .birthDate(LocalDate.of(2020, 12, 8))
                .weight(20.0)
                .breed("Vira-lata")
                .address(createValidAddress())
                .owner("Diego Oliveira")
                .build();
    }

    public static String createValidAddress() {
        return "Rua José Marques da Rocha, Memorare, Teresina - PI, 64009-100";
    }

    public static PetPostRequest createPetPostRequestBody() {
        return PetPostRequest.builder()
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

    public static PetPostRequest createPetPostRequestBodyWithOnlyRequiredFields() {
        return PetPostRequest.builder()
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

    public static PetPutRequest createPetPutRequestBody() {
        return PetPutRequest.builder()
                .id(1L)
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
}
