package com.projetos.diego.pet_management_system.repository;

import com.projetos.diego.pet_management_system.domain.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet, Long> {
}
