package com.projetos.diego.pet_management_system.util;

import com.projetos.diego.pet_management_system.domain.pet.Address;
import com.projetos.diego.pet_management_system.domain.pet.Pet;
import com.projetos.diego.pet_management_system.dto.request.PetPostRequest;
import com.projetos.diego.pet_management_system.dto.request.PetPutRequest;
import com.projetos.diego.pet_management_system.dto.response.PetResponse;

import java.time.LocalDate;

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
                .petOwner(PetOwnerCreator.createValidPetOwner())
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

    public static PetResponse createResponse(Pet pet) {
        return PetResponse.builder()
                .id(pet.getId())
                .name(pet.getName())
                .type(pet.getType())
                .sex(pet.getSex())
                .birthDate(pet.getBirthDate())
                .weight(pet.getWeight())
                .breed(pet.getBreed())
                .address(pet.getAddress())
                .ownerId(pet.getPetOwner().getId())
                .build();
    }
}
