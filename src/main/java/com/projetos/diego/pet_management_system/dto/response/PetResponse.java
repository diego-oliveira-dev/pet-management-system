package com.projetos.diego.pet_management_system.dto;

import com.projetos.diego.pet_management_system.domain.pet.Address;
import com.projetos.diego.pet_management_system.domain.pet.Pet;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record PetResponse(
        Long id,
        String name,
        Pet.Type type,
        Pet.Sex sex,
        LocalDate birthDate,
        Double weight,
        String breed,
        Address address,
        Long ownerId
)
{}
