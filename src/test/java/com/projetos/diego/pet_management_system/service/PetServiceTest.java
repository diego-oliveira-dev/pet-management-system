package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.owner.PetOwner;
import com.projetos.diego.pet_management_system.domain.pet.Pet;
import com.projetos.diego.pet_management_system.dto.request.PetPostRequest;
import com.projetos.diego.pet_management_system.dto.request.PetPutRequest;
import com.projetos.diego.pet_management_system.exception.PetAccessDeniedException;
import com.projetos.diego.pet_management_system.exception.ResourceNotFoundException;
import com.projetos.diego.pet_management_system.mapper.PetMapper;
import com.projetos.diego.pet_management_system.repository.PetOwnerRepository;
import com.projetos.diego.pet_management_system.repository.PetRepository;
import com.projetos.diego.pet_management_system.util.PetCreator;
import com.projetos.diego.pet_management_system.util.PetOwnerCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {
    @InjectMocks
    private PetService petService;

    @Mock
    private PetRepository petRepositoryMock;

    @Mock
    private PetMapper petMapperMock;

    @Mock
    private PetOwnerRepository petOwnerRepositoryMock;

    @Test
    @DisplayName("findPetByIdAndPetOwnerId returns pet when successful")
    void findPetByIdAndPetOwnerId_ReturnsPet_WhenSuccessful() {
        Pet expectedPet = PetCreator.createValidPet();
        Long expectedPetId = expectedPet.getId();
        Long ownerId = expectedPet.getPetOwner().getId();

        BDDMockito.when(petRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(expectedPet));

        Pet pet = petService.findPetByIdAndPetOwnerId(3L, ownerId);

        Assertions.assertThat(pet).isNotNull();
        Assertions.assertThat(pet.getId()).isEqualTo(expectedPetId);
        Assertions.assertThat(pet.getPetOwner().getId()).isEqualTo(ownerId);
    }

    @Test
    @DisplayName("findPetByIdAndPetOwnerId throws ResourceNotFoundException when pet is not found")
    void findPetByIdAndPetOwnerId_ThrowsResourceNotFoundException_WhenPetIsNotFound() {
        BDDMockito.when(petRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> this.petService.findPetByIdAndPetOwnerId(3L, 1L))
                .withMessageContaining("Pet not found");
    }

    @Test
    @DisplayName("findPetByIdAndPetOwnerId throws PetAccessDeniedException when pet belongs to another user")
    void findPetByIdAndPetOwnerId_ThrowsPetAccessDeniedException_WhenPetBelongsToAnotherUser() {
        long petId = 5L;
        long petOwnerId = 1L;
        long currentUserId = 2L;
        PetOwner petOwner = PetOwnerCreator.createValidPetOwner();
        petOwner.setId(petOwnerId);
        Pet pet = PetCreator.createValidPet();
        pet.setId(petId);
        pet.setPetOwner(petOwner);

        BDDMockito.when(petRepositoryMock.findById(petId))
                .thenReturn(Optional.of(pet));

        Assertions.assertThatExceptionOfType(PetAccessDeniedException.class)
                .isThrownBy(() -> this.petService.findPetByIdAndPetOwnerId(pet.getId(), currentUserId))
                .withMessageContaining("Access denied");
    }

    @Test
    @DisplayName("save persists pet when successful")
    void save_PersistsPet_WhenSuccessful() {
        PetPostRequest request = PetCreator.createPetPostRequest();
        PetOwner petOwner = PetOwnerCreator.createValidPetOwner();
        petOwner.setId(1L);
        Pet pet = PetCreator.createValidPet();
        pet.setPetOwner(petOwner);

        BDDMockito.when(petOwnerRepositoryMock.findById(petOwner.getId()))
                .thenReturn(Optional.of(petOwner));
        BDDMockito.when(petMapperMock.fromPostRequestToEntity(request, petOwner))
                .thenReturn(pet);
        BDDMockito.when(petRepositoryMock.save(pet))
                .thenReturn(pet);

        Pet savedPet = petService.save(request, petOwner.getId());

        Assertions.assertThat(savedPet).isNotNull().isEqualTo(pet);
    }

    @Test
    @DisplayName("save throws ResourceNotFoundException when owner is not found")
    void save_ThrowsResourceNotFoundException_WhenOwnerIsNotFound() {
        long ownerId = 1L;
        PetPostRequest request = PetCreator.createPetPostRequest();
        BDDMockito.when(petOwnerRepositoryMock.findById(ownerId))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> this.petService.save(request, ownerId))
                .withMessageContaining("Owner not found");
        Mockito.verify(petOwnerRepositoryMock).findById(ownerId);
        Mockito.verifyNoInteractions(petRepositoryMock);
        Mockito.verifyNoInteractions(petMapperMock);
    }

    @Test
    @DisplayName("replace updates the pet when successful")
    void replace_UpdatesThePet_WhenSuccessful() {
        Pet savedPet = PetCreator.createValidPet();
        savedPet.setId(1L);
        Long ownerId = savedPet.getPetOwner().getId();
        Pet expectedPet = PetCreator.createPetToBeUpdated(savedPet);
        PetPutRequest request = PetCreator.createPetPutRequest();

        BDDMockito.when(petRepositoryMock.findById(1L))
                .thenReturn(Optional.of(savedPet));
        BDDMockito.when(petMapperMock.fromPutRequestToEntity(request, savedPet))
                .thenReturn(expectedPet);
        BDDMockito.when(petRepositoryMock.save(ArgumentMatchers.any(Pet.class)))
                .thenReturn(expectedPet);

        petService.replace(request, ownerId);

        Mockito.verify(petRepositoryMock, Mockito.times(1))
                .save(expectedPet);
        Mockito.verify(petMapperMock, Mockito.times(1))
                .fromPutRequestToEntity(request, savedPet);
    }

    @Test
    @DisplayName("replace throws ResourceNotFoundException when pet is not found")
    void replace_ThrowsResourceNotFoundException_WhenPetIsNotFound() {
        long ownerId = 1L;

        BDDMockito.when(petRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        PetPutRequest invalidPetPutRequest = PetCreator.createPetPutRequest();

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> this.petService.replace(invalidPetPutRequest, ownerId))
                .withMessageContaining("Pet not found");
        Mockito.verifyNoInteractions(petMapperMock);
        Mockito.verify(petRepositoryMock, Mockito.never()).save(ArgumentMatchers.any(Pet.class));
    }

    @Test
    @DisplayName("replace throws PetAccessDeniedException when pet belongs to another user")
    void replace_ThrowsPetAccessDeniedException_WhenPetBelongsToAnotherUser() {
        long petId = 1L;
        long petOwnerId = 1L;
        long currentUserId = 2L;
        PetOwner petOwner = PetOwnerCreator.createValidPetOwner();
        petOwner.setId(petOwnerId);
        Pet pet = PetCreator.createValidPet();
        pet.setId(petId);
        pet.setPetOwner(petOwner);

        BDDMockito.when(petRepositoryMock.findById(pet.getId()))
                .thenReturn(Optional.of(pet));

        PetPutRequest request = PetCreator.createPetPutRequest();

        Assertions.assertThatExceptionOfType(PetAccessDeniedException.class)
                .isThrownBy(() -> this.petService.replace(request, currentUserId))
                .withMessageContaining("Access denied");
        Mockito.verifyNoInteractions(petMapperMock);
        Mockito.verify(petRepositoryMock, Mockito.never()).save(ArgumentMatchers.any(Pet.class));
    }

    @Test
    @DisplayName("delete removes the pet when successful")
    void delete_RemovesThePet_WhenSuccessful() {
        Pet alreadySavedPet = PetCreator.createValidPet();
        long id = 1L;

        BDDMockito.when(petRepositoryMock.findById(id))
                .thenReturn(Optional.of(alreadySavedPet));

        petService.delete(id);

        Mockito.verify(petRepositoryMock, Mockito.times(1)).delete(alreadySavedPet);
    }

    @Test
    @DisplayName("delete throws ResourceNotFoundException when pet is not found")
    void delete_ThrowsResourceNotFoundException_WhenPetIsNotFound() {
        BDDMockito.when(petRepositoryMock.findById(ArgumentMatchers.any()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> this.petService.delete(1L))
                .withMessageContaining("Pet not found");

        Mockito.verify(petRepositoryMock, Mockito.never()).delete(ArgumentMatchers.any(Pet.class));
    }
}