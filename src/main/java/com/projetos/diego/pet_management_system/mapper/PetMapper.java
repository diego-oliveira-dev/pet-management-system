package com.projetos.diego.pet_management_system.mapper;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.dto.PetPostRequest;
import com.projetos.diego.pet_management_system.dto.PetPutRequest;
import com.projetos.diego.pet_management_system.dto.PetResponse;
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
                .address(pet.getAddress())
                .ownerId(pet.getPetOwner().getId())
                .build();
    }

    public Pet fromPostRequestToEntity(PetPostRequest petPostRequest, String address) {
        return Pet.builder()
                .name(petPostRequest.getName())
                .type(petPostRequest.getType())
                .sex(petPostRequest.getSex())
                .birthDate(petPostRequest.getBirthDate())
                .weight(petPostRequest.getWeight())
                .breed(petPostRequest.getBreed())
                .address(address)
                .petOwner(petPostRequest.getOwner())
                .build();
    }

    public Pet fromPutRequestToEntity(PetPutRequest petPutRequest, Long id, String address) {
        return Pet.builder()
                .id(id)
                .name(petPutRequest.getName())
                .type(petPutRequest.getType())
                .sex(petPutRequest.getSex())
                .birthDate(petPutRequest.getBirthDate())
                .weight(petPutRequest.getWeight())
                .breed(petPutRequest.getBreed())
                .address(address)
                .petOwner(petPutRequest.getOwner())
                .build();
    }
}
