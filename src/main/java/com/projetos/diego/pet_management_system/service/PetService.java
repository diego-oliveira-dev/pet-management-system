package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.owner.PetOwner;
import com.projetos.diego.pet_management_system.domain.pet.Pet;
import com.projetos.diego.pet_management_system.dto.request.PetPostRequest;
import com.projetos.diego.pet_management_system.dto.request.PetPutRequest;
import com.projetos.diego.pet_management_system.exception.PetAccessDeniedException;
import com.projetos.diego.pet_management_system.exception.ResourceNotFoundException;
import com.projetos.diego.pet_management_system.mapper.PetMapper;
import com.projetos.diego.pet_management_system.repository.PetOwnerRepository;
import com.projetos.diego.pet_management_system.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final PetMapper petMapper;

    public Page<Pet> listAll(Pageable pageable) {
        return petRepository.findAll(pageable);
    }

    public Pet findPetByIdAndPetOwnerId(long id, long ownerId) {
        Pet pet = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
        if (!pet.getPetOwner().getId().equals(ownerId)) {
            throw new PetAccessDeniedException("Access denied");
        }
        return pet;
    }

    public List<Pet> findPetsByOwnerId(long ownerId) {
        return petRepository.findByPetOwnerId(ownerId);
    }

    public List<Pet> findPetsByName(String name, long userId) {
        return petRepository.findByNameContainingAndPetOwnerId(name, userId);
    }

    public Pet save(PetPostRequest request, long ownerId) {
        PetOwner petOwner = petOwnerRepository.findById(ownerId)
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        Pet pet = petMapper.fromPostRequestToEntity(request, petOwner);
        return petRepository.save(pet);
    }

    public void replace(PetPutRequest request, long ownerId) {
        Pet savedPet = findPetByIdAndPetOwnerId(request.getId(), ownerId);
        Pet petToBeUpdated = petMapper.fromPutRequestToEntity(request, savedPet);
        petRepository.save(petToBeUpdated);
    }

    public void delete(long id) {
        Pet petToBeDeleted = petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));;
        petRepository.delete(petToBeDeleted);
    }
}
