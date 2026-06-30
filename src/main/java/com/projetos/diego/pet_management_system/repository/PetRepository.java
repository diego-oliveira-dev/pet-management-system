package com.projetos.diego.pet_management_system.repository;

import com.projetos.diego.pet_management_system.domain.pet.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetRepository extends JpaRepository<Pet, Long> {
    List<Pet> findByNameContainingAndPetOwnerId(String name, long ownerId);
    List<Pet> findByPetOwnerId(Long id);
}
