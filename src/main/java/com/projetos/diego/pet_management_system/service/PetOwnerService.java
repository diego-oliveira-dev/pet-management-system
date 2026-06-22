package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.PetOwner;
import com.projetos.diego.pet_management_system.repository.PetOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetOwnerService {
    private final PetOwnerRepository petOwnerRepository;

    public List<PetOwner> listAll() {
        return petOwnerRepository.findAll();
    }
}
