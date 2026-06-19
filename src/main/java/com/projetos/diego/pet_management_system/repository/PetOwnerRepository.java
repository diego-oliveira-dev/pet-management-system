package com.projetos.diego.pet_management_system.repository;

import com.projetos.diego.pet_management_system.domain.PetOwner;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetOwnerRepository extends JpaRepository<PetOwner, Long> {
}
