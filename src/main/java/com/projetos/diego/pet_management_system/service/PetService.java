package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.Address;
import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.domain.PetOwner;
import com.projetos.diego.pet_management_system.dto.PetPostRequest;
import com.projetos.diego.pet_management_system.dto.PetPutRequest;
import com.projetos.diego.pet_management_system.exception.InvalidPostalCodeException;
import com.projetos.diego.pet_management_system.exception.ResourceNotFoundException;
import com.projetos.diego.pet_management_system.mapper.PetMapper;
import com.projetos.diego.pet_management_system.repository.PetOwnerRepository;
import com.projetos.diego.pet_management_system.repository.PetRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final PetOwnerRepository petOwnerRepository;
    private final PetMapper petMapper;
    private final AddressLookupService addressLookupService;

    public Page<Pet> listAll(Pageable pageable) {
        return petRepository.findAll(pageable);
    }

    public List<Pet> listAllNonPageable() {
        return petRepository.findAll();
    }

    public Pet findByIdOrThrowResourceNotFoundException(long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
    }

    public List<Pet> findByName(String name) {
        return petRepository.findByNameContaining(name);
    }

    public Pet save(PetPostRequest request) {
        PetOwner petOwner = petOwnerRepository.findById(request.getOwnerId())
                .orElseThrow(() -> new ResourceNotFoundException("Owner not found"));
        Address address = addressLookupService.findByPostalCode(request.getPostalCode());
        Pet pet = petMapper.fromPostRequestToEntity(request, address, petOwner);
        return petRepository.save(pet);
    }

    public void replace(PetPutRequest request) {
        Pet savedPet = findByIdOrThrowResourceNotFoundException(request.getId());
        Address address = resolveAddress(request, savedPet);
        Pet petToBeUpdated = petMapper.fromPutRequestToEntity(request, savedPet, address);
        petRepository.save(petToBeUpdated);
    }

    public void delete(long id) {
        Pet petToBeDeleted = findByIdOrThrowResourceNotFoundException(id);
        petRepository.delete(petToBeDeleted);
    }

    public boolean postalCodeChanged(PetPutRequest request, Pet savedPet) {
        if (request.getPostalCode() == null) {
            throw new InvalidPostalCodeException("Postal code cannot be null");
        }
        return !(request.getPostalCode().equals(savedPet.getAddress().getPostalCode()));
    }

    public Address resolveAddress(PetPutRequest request, Pet savedPet) {
        if (postalCodeChanged(request, savedPet)) {
            return addressLookupService.findByPostalCode(request.getPostalCode());
        }
        return savedPet.getAddress();
    }
}
