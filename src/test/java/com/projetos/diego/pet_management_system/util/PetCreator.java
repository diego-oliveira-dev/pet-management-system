package com.projetos.diego.pet_management_system.util;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.domain.PetOwner;
import com.projetos.diego.pet_management_system.dto.PetPostRequest;
import com.projetos.diego.pet_management_system.dto.PetPutRequest;

import java.time.LocalDate;
import java.util.List;

public class PetCreator {

    public static Pet createValidPet() {
        return Pet.builder()
                .name("Zaya")
                .type(Pet.Type.DOG)
                .sex(Pet.Sex.FEMALE)
                .birthDate(LocalDate.of(2020, 12, 8))
                .weight(20.0)
                .breed("Vira-lata")
                .address(createValidAddress())
                .petOwner(new PetOwner(1L, "Diego Oliveira", List.of()))
                .build();
    }

    public static String createValidAddress() {
        return "Rua José Marques da Rocha, Memorare, Teresina - PI, 64009-100";
    }

    public static PetPostRequest createPetPostRequest() {
        return PetPostRequest.builder()
                .name("Zaya")
                .type(Pet.Type.DOG)
                .sex(Pet.Sex.FEMALE)
                .birthDate(LocalDate.of(2020, 12, 8))
                .weight(20.0)
                .breed("Vira-lata")
                .postalCode("64009100")
                .ownerId(1L)
                .build();
    }

    public static PetPostRequest createPetPostRequestWithOnlyRequiredFields() {
        return PetPostRequest.builder()
                .name("Zaya")
                .type(Pet.Type.DOG)
                .sex(Pet.Sex.FEMALE)
                .birthDate(LocalDate.of(2020, 12, 8))
                .weight(20.0)
                .breed(null)
                .postalCode(null)
                .ownerId(1L)
                .build();
    }

    public static PetPutRequest createPetPutRequest() {
        return PetPutRequest.builder()
                .id(1L)
                .name("Zaya")
                .type(Pet.Type.DOG)
                .sex(Pet.Sex.FEMALE)
                .birthDate(LocalDate.of(2020, 12, 8))
                .weight(20.0)
                .breed("Vira-lata")
                .postalCode("64009100")
                .ownerId(1L)
                .build();
    }
}
