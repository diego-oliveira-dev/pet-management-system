package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.PetOwner;
import com.projetos.diego.pet_management_system.dto.PetOwnerPostRequest;
import com.projetos.diego.pet_management_system.mapper.PetOwnerMapper;
import com.projetos.diego.pet_management_system.repository.PetOwnerRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetOwnerService {
    private final PetOwnerRepository petOwnerRepository;
    private final PetOwnerMapper petOwnerMapper;

    public List<PetOwner> listAll() {
        return petOwnerRepository.findAll();
    }

    public PetOwner save(PetOwnerPostRequest request) {
        PetOwner owner = petOwnerMapper.fromPostRequestToEntity(request);
        return petOwnerRepository.save(owner);
    }
}
