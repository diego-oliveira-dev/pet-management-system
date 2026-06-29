package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.owner.PetOwner;
import com.projetos.diego.pet_management_system.domain.pet.Pet;
import com.projetos.diego.pet_management_system.dto.request.PetPostRequest;
import com.projetos.diego.pet_management_system.dto.request.PetPutRequest;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.List;
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
    @DisplayName("listAll returns list of pets inside page when successful")
    void listAll_ReturnsListOfPetsInsidePage_WhenSuccessful() {
        PageImpl<Pet> petPage = new PageImpl<>(List.of(PetCreator.createValidPet()));
        PageRequest pageable = PageRequest.of(0, 3);

        BDDMockito.when(petRepositoryMock.findAll(pageable))
                .thenReturn(petPage);

        Page<Pet> returnedPetPage = petService.listAll(pageable);

        Assertions.assertThat(returnedPetPage).isNotNull().isNotEmpty().isEqualTo(petPage);
        Mockito.verify(petRepositoryMock, Mockito.times(1)).findAll(pageable);
    }

    @Test
    @DisplayName("listAllNonPageable returns list of pets when successful")
    void listAllNonPageable_ReturnsListOfPets_WhenSuccessful() {
        List<Pet> petList = List.of(PetCreator.createValidPet());

        BDDMockito.when(petRepositoryMock.findAll())
                .thenReturn(petList);

        List<Pet> returnedPetList = petService.listAllNonPageable();

        Assertions.assertThat(returnedPetList).isNotNull().isNotEmpty().isEqualTo(petList);
        Mockito.verify(petRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("findPetsById returns pet when successful")
    void findPetsById_ReturnsPet_WhenSuccessful() {
        BDDMockito.when(petRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(PetCreator.createValidPet()));

        Long expectedId = PetCreator.createValidPet().getId();
        Pet pet = petService.findPetsById(3L);

        Assertions.assertThat(pet).isNotNull();
        Assertions.assertThat(pet.getId()).isEqualTo(expectedId);
    }

    @Test
    @DisplayName("findPetsById throws ResourceNotFoundException when pet is not found")
    void findPetsById_ThrowsResourceNotFoundException_WhenPetIsNotFound() {
        BDDMockito.when(petRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> this.petService.findPetsById(3L))
                .withMessageContaining("Pet not found");
    }

    @Test
    @DisplayName("findByName returns list of pets when successful")
    void findByName_ReturnsListOfPets_WhenSuccessful() {
        BDDMockito.when(petRepositoryMock.findByNameContaining(ArgumentMatchers.anyString()))
                .thenReturn(List.of(PetCreator.createValidPet()));

        Pet petThatWeAreLookingFor = PetCreator.createValidPet();
        List<Pet> pets = petService.findByName(petThatWeAreLookingFor.getName());

        Assertions.assertThat(pets).isNotNull().hasSize(1);
        Assertions.assertThat(pets.getFirst().getId()).isEqualTo(petThatWeAreLookingFor.getId());
    }

    @Test
    @DisplayName("findByName returns empty list when pet is not found")
    void findByName_ReturnsEmptyList_WhenNoPetIsFound() {
        BDDMockito.when(petRepositoryMock.findByNameContaining(ArgumentMatchers.anyString()))
                .thenReturn(List.of());

        List<Pet> pets = petService.findByName("random test name");

        Assertions.assertThat(pets).isNotNull().isEmpty();
    }

    @Test
    @DisplayName("save persists pet when successful")
    void save_PersistsPet_WhenSuccessful() {
        PetPostRequest request = PetCreator.createPetPostRequest();
        PetOwner petOwner = PetOwnerCreator.createValidPetOwner();
        Pet pet = PetCreator.createValidPet();

        BDDMockito.when(petOwnerRepositoryMock.findById(request.getOwnerId()))
                .thenReturn(Optional.of(petOwner));
        BDDMockito.when(petMapperMock.fromPostRequestToEntity(request, petOwner))
                .thenReturn(pet);
        BDDMockito.when(petRepositoryMock.save(pet))
                .thenReturn(pet);

        Pet savedPet = petService.save(request);

        Assertions.assertThat(savedPet).isNotNull().isEqualTo(pet);
    }

    @Test
    @DisplayName("save persists pet when only required fields are provided")
    void save_PersistsPet_WhenOnlyRequiredFieldsAreProvided() {
        PetPostRequest request = PetCreator.createPetPostRequest();
        request.setBreed(null);
        PetOwner petOwner = PetOwnerCreator.createValidPetOwner();
        Pet pet = PetCreator.createValidPet();

        BDDMockito.when(petOwnerRepositoryMock.findById(request.getOwnerId()))
                .thenReturn(Optional.of(petOwner));
        BDDMockito.when(petMapperMock.fromPostRequestToEntity(request, petOwner))
                .thenReturn(pet);
        BDDMockito.when(petRepositoryMock.save(pet))
                .thenReturn(pet);

        Pet savedPet = petService.save(request);

        Assertions.assertThat(savedPet).isNotNull().isEqualTo(pet);
    }

    @Test
    @DisplayName("save does not persists pet when owner is not found")
    void save_DoesNotPersistsPet_WhenOwnerIsNotFound() {
        PetPostRequest request = PetCreator.createPetPostRequest();
        BDDMockito.when(petOwnerRepositoryMock.findById(request.getOwnerId()))
                .thenReturn(Optional.empty());

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> this.petService.save(request))
                .withMessageContaining("Owner not found");
        Mockito.verify(petOwnerRepositoryMock).findById(request.getOwnerId());
        Mockito.verifyNoInteractions(petRepositoryMock);
        Mockito.verifyNoInteractions(petMapperMock);
    }

    @Test
    @DisplayName("replace updates the pet when successful")
    void replace_UpdatesThePet_WhenSuccessful() {
        Pet savedPet = PetCreator.createValidPet();
        savedPet.setId(1L);
        Pet expectedPet = PetCreator.createPetToBeUpdated(savedPet);
        PetPutRequest request = PetCreator.createPetPutRequest();
        ArgumentCaptor<Pet> petCaptor = ArgumentCaptor.forClass(Pet.class);

        BDDMockito.when(petRepositoryMock.findById(1L))
                .thenReturn(Optional.of(savedPet));
        BDDMockito.when(petMapperMock.fromPutRequestToEntity(request, savedPet))
                .thenReturn(expectedPet);
        BDDMockito.when(petRepositoryMock.save(ArgumentMatchers.any(Pet.class)))
                .thenReturn(expectedPet);

        petService.replace(request);

        Mockito.verify(petRepositoryMock).save(petCaptor.capture());
        Assertions.assertThat(petCaptor.getValue()).isNotNull().isEqualTo(expectedPet);
    }

    @Test
    @DisplayName("replace throws ResourceNotFoundException when pet is not found")
    void replace_ThrowsResourceNotFoundException_WhenPetIsNotFound() {
        BDDMockito.when(petRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        PetPutRequest invalidPetPutRequest = PetCreator.createPetPutRequest();

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> this.petService.replace(invalidPetPutRequest))
                .withMessageContaining("Pet not found");
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
                .isThrownBy(() -> this.petService.delete(ArgumentMatchers.anyLong()))
                .withMessageContaining("Pet not found");

        Mockito.verify(petRepositoryMock, Mockito.never()).delete(ArgumentMatchers.any(Pet.class));
    }
}