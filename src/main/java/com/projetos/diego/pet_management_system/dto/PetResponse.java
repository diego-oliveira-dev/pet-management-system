package com.projetos.diego.pet_management_system.dto;

import com.projetos.diego.pet_management_system.domain.Address;
import com.projetos.diego.pet_management_system.domain.Pet;
import lombok.Builder;
import lombok.Data;
import lombok.Getter;

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
