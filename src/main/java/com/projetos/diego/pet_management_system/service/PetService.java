package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.exception.ResourceNotFoundException;
import com.projetos.diego.pet_management_system.repository.PetRepository;
import com.projetos.diego.pet_management_system.requests.PetPostRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;

    public List<Pet> listAll() {
        return petRepository.findAll();
    }

    public Pet findByIdOrThrowResourceNotFoundException(long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
    }

    public Pet save(PetPostRequestBody petPostRequestBody) {
        Pet pet = Pet.builder().name(petPostRequestBody.getName()).build();
        return petRepository.save(pet);
    }
}
