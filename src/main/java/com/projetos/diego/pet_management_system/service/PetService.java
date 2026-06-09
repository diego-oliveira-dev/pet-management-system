package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.exception.BadRequestException;
import com.projetos.diego.pet_management_system.repository.PetRepository;
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

    public Pet findByIdOrThrowBadRequestException(long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new BadRequestException("Pet not found"));
    }
}
