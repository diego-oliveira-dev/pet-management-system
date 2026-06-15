package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.exception.ResourceNotFoundException;
import com.projetos.diego.pet_management_system.repository.PetRepository;
import com.projetos.diego.pet_management_system.requests.PetPostRequestBody;
import com.projetos.diego.pet_management_system.requests.PetPutRequestBody;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {
    private final PetRepository petRepository;
    private final AddressLookupService addressLookupService;

    public List<Pet> listAll() {
        return petRepository.findAll();
    }

    public Pet findByIdOrThrowResourceNotFoundException(long id) {
        return petRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Pet not found"));
    }

    public List<Pet> findByName(String name) {
        return petRepository.findByName(name);
    }

    @Transactional
    public Pet save(PetPostRequestBody petPostRequestBody) {
        String address = addressLookupService.findByPostalCode(petPostRequestBody.getPostalCode());
        Pet pet = Pet.builder()
                .name(petPostRequestBody.getName())
                .type(petPostRequestBody.getType())
                .sex(petPostRequestBody.getSex())
                .birthDate(petPostRequestBody.getBirthDate())
                .weight(petPostRequestBody.getWeight())
                .breed(petPostRequestBody.getBreed())
                .address(address)
                .owner(petPostRequestBody.getOwner())
                .build();
        return petRepository.save(pet);
    }

    public void replace(PetPutRequestBody petPutRequestBody) {
        Pet alreadySavedPet = findByIdOrThrowResourceNotFoundException(petPutRequestBody.getId());
        Pet petToBeUpdated = Pet.builder().id(alreadySavedPet.getId()).name(petPutRequestBody.getName()).build();
        petRepository.save(petToBeUpdated);
    }

    public void delete(long id) {
        Pet petToBeDeleted = findByIdOrThrowResourceNotFoundException(id);
        petRepository.delete(petToBeDeleted);
    }
}
