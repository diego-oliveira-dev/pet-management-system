package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.exception.ResourceNotFoundException;
import com.projetos.diego.pet_management_system.repository.PetRepository;
import com.projetos.diego.pet_management_system.requests.PetPutRequestBody;
import com.projetos.diego.pet_management_system.util.PetCreator;
import com.projetos.diego.pet_management_system.util.PetPutRequestBodyCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

@ExtendWith(MockitoExtension.class)
class PetServiceTest {
    @InjectMocks
    private PetService petService;

    @Mock
    private PetRepository petRepositoryMock;

    @Test
    @DisplayName("findByIdOrThrowResourceNotFoundException returns pet when successful")
    void findByIdOrThrowResourceNotFoundException_ReturnsPet_WhenSuccessful() {
        BDDMockito.when(petRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(PetCreator.createValidPet()));

        Long expectedId = PetCreator.createValidPet().getId();
        Pet pet = petService.findByIdOrThrowResourceNotFoundException(3L);

        Assertions.assertThat(pet).isNotNull();
        Assertions.assertThat(pet.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findByIdOrThrowResourceNotFoundException throws ResourceNotFoundException when pet is not found")
    void findByIdOrThrowResourceNotFoundException_ThrowsResourceNotFoundException_WhenPetIsNotFound() {
        BDDMockito.when(petRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> this.petService.findByIdOrThrowResourceNotFoundException(3L))
                .withMessageContaining("Pet not found");
    }

    @Test
    @DisplayName("findByName returns list of pets when successful")
    void findByName_ReturnsListOfPets_WhenSuccessful() {
        BDDMockito.when(petRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of(PetCreator.createValidPet()));

        Pet petThatWeAreLookingFor = PetCreator.createValidPet();
        List<Pet> pets = petService.findByName(petThatWeAreLookingFor.getName());

        Assertions.assertThat(pets).isNotNull().hasSize(1);
        Assertions.assertThat(pets.getFirst().getId()).isEqualTo(petThatWeAreLookingFor.getId());
    }

    @Test
    @DisplayName("findByName returns empty list when pet is not found")
    void findByName_ReturnsEmptyList_WhenNoPetIsFound() {
        BDDMockito.when(petRepositoryMock.findByName(ArgumentMatchers.anyString()))
                .thenReturn(List.of());

        List<Pet> pets = petService.findByName("random test name");

        Assertions.assertThat(pets).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("replace updates the pet when successful")
    void replace_UpdatesThePet_WhenSuccessful() {
        Pet alreadySavedPet = PetCreator.createValidPet();
        BDDMockito.when(petRepositoryMock.findById(alreadySavedPet.getId()))
                .thenReturn(Optional.of(alreadySavedPet));
        BDDMockito.when(petRepositoryMock.save(ArgumentMatchers.any(Pet.class)))
                .thenAnswer(argument -> argument.getArgument(0));

        PetPutRequestBody petPutRequestBody = PetPutRequestBodyCreator.createPetPutRequestBody();
        petService.replace(petPutRequestBody);

        ArgumentCaptor<Pet> petCaptor = ArgumentCaptor.forClass(Pet.class);
        Mockito.verify(petRepositoryMock).save(petCaptor.capture());
        Pet capturedPet = petCaptor.getValue();

        Assertions.assertThat(capturedPet.getId()).isEqualTo(alreadySavedPet.getId());
        Assertions.assertThat(capturedPet.getName()).isEqualTo(petPutRequestBody.getName());
    }

    @Test
    @DisplayName("replace throws ResourceNotFoundException when pet is not found")
    void replace_ThrowsResourceNotFoundException_WhenPetIsNotFound() {
        BDDMockito.when(petRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        PetPutRequestBody invalidPetPutRequestBody = PetPutRequestBodyCreator.createPetPutRequestBody();

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> this.petService.replace(invalidPetPutRequestBody))
                .withMessageContaining("Pet not found");
    }

    @Test
    @DisplayName("delete removes the pet when successful")
    void delete_RemovesThePet_WhenSuccessful() {
        Pet alreadySavedPet = PetCreator.createValidPet();
        Long id = alreadySavedPet.getId();

        BDDMockito.when(petRepositoryMock.findById(id))
                .thenReturn(Optional.of(alreadySavedPet));

        petService.delete(id);

        Mockito.verify(petRepositoryMock, Mockito.times(1)).delete(alreadySavedPet);
    }

    @Test
    @DisplayName("delete throws ResourceNotFoundException when pet is not found")
    void delete_ThrowsResourceNotFoundException_WhenPetIsNotFound() {
        BDDMockito.when(petRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Pet pet = PetCreator.createValidPet();
        Long id = pet.getId();

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> this.petService.delete(id))
                .withMessageContaining("Pet not found");

        Mockito.verify(petRepositoryMock, Mockito.never()).delete(ArgumentMatchers.any(Pet.class));
    }
}