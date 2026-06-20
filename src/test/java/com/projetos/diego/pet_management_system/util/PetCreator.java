package com.projetos.diego.pet_management_system.util;

import com.projetos.diego.pet_management_system.domain.Address;
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

    public static Pet createPetToBeUpdated(Pet savedPet) {
        return Pet.builder()
                .id(1L)
                .name("Rex")
                .type(Pet.Type.DOG)
                .sex(Pet.Sex.MALE)
                .birthDate(LocalDate.of(2018, 5, 8))
                .weight(25.0)
                .breed("Vira-lata")
                .address(createValidAddress())
                .petOwner(savedPet.getPetOwner())
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

    public static PetPutRequest createPetPutRequest() {
        return PetPutRequest.builder()
                .id(1L)
                .name("Rex")
                .type(Pet.Type.DOG)
                .sex(Pet.Sex.MALE)
                .birthDate(LocalDate.of(2018, 5, 8))
                .weight(25.0)
                .breed("Vira-lata")
                .postalCode("64009100")
                .ownerId(1L)
                .build();
    }
}
