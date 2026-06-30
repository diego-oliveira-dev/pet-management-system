package com.projetos.diego.pet_management_system.mapper;

import com.projetos.diego.pet_management_system.domain.owner.Address;
import com.projetos.diego.pet_management_system.domain.pet.Pet;
import com.projetos.diego.pet_management_system.domain.owner.PetOwner;
import com.projetos.diego.pet_management_system.dto.request.PetPostRequest;
import com.projetos.diego.pet_management_system.dto.request.PetPutRequest;
import com.projetos.diego.pet_management_system.dto.response.PetResponse;
import org.springframework.stereotype.Component;

@Component
public class PetMapper {
    public PetResponse toResponse(Pet pet) {
        return PetResponse.builder()
                .id(pet.getId())
                .name(pet.getName())
                .type(pet.getType())
                .sex(pet.getSex())
                .birthDate(pet.getBirthDate())
                .weight(pet.getWeight())
                .breed(pet.getBreed())
                .ownerId(pet.getPetOwner().getId())
                .build();
    }

    public Pet fromPostRequestToEntity(PetPostRequest petPostRequest, PetOwner petOwner) {
        return Pet.builder()
                .name(petPostRequest.getName())
                .type(petPostRequest.getType())
                .sex(petPostRequest.getSex())
                .birthDate(petPostRequest.getBirthDate())
                .weight(petPostRequest.getWeight())
                .breed(petPostRequest.getBreed())
                .petOwner(petOwner)
                .build();
    }

    public Pet fromPutRequestToEntity(PetPutRequest petPutRequest, Pet savedPet) {
        return Pet.builder()
                .id(savedPet.getId())
                .name(petPutRequest.getName())
                .type(petPutRequest.getType())
                .sex(petPutRequest.getSex())
                .birthDate(petPutRequest.getBirthDate())
                .weight(petPutRequest.getWeight())
                .breed(petPutRequest.getBreed())
                .petOwner(savedPet.getPetOwner())
                .build();
    }
}
