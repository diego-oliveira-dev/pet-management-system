package com.projetos.diego.pet_management_system.dto;

import com.projetos.diego.pet_management_system.domain.Pet;
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
        String address,
        Long ownerId
)
{}
