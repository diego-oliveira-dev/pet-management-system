package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.exception.ResourceNotFoundException;
import com.projetos.diego.pet_management_system.repository.PetRepository;
import com.projetos.diego.pet_management_system.requests.PetPostRequestBody;
import com.projetos.diego.pet_management_system.requests.PetPutRequestBody;
import com.projetos.diego.pet_management_system.util.PetCreator;
import com.projetos.diego.pet_management_system.util.PetPostRequestBodyCreator;
import com.projetos.diego.pet_management_system.util.PetPutRequestBodyCreator;
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
    private AddressLookupService addressLookupServiceMock;

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
    @DisplayName("save persists pet when successful")
    void save_PersistsPet_WhenSuccessful() {
        PetPostRequestBody petPostRequestBody = PetPostRequestBodyCreator.createPetPostRequestBody();
        Pet pet = PetCreator.createPetToBeSaved();

        BDDMockito.when(addressLookupServiceMock.findByPostalCode(petPostRequestBody.getPostalCode()))
                .thenReturn(PetCreator.createValidAddress());
        BDDMockito.when(petRepositoryMock.save(pet)).thenReturn(pet);

        Pet savedPet = petService.save(petPostRequestBody);

        Assertions.assertThat(savedPet).isNotNull();
        Assertions.assertThat(savedPet).isEqualTo(pet);
    }

    @Test
    @DisplayName("save persists pet when only required fields are provided")
    void save_PersistsPet_WhenOnlyRequiredFieldsAreProvided() {
        PetPostRequestBody petPostRequestBody = PetPostRequestBodyCreator.createPetPostRequestBodyWithOnlyRequiredFields();
        Pet pet = PetCreator.createPetToBeSavedWithOnlyRequiredFields();

        BDDMockito.when(addressLookupServiceMock.findByPostalCode(petPostRequestBody.getPostalCode()))
                .thenReturn(null);
        BDDMockito.when(petRepositoryMock.save(pet)).thenReturn(pet);

        Pet savedPet = petService.save(petPostRequestBody);

        Assertions.assertThat(savedPet).isNotNull();
        Assertions.assertThat(savedPet).isEqualTo(pet);
    }

    @Test
    @DisplayName("save does not persists pet when postal code is not found")
    void save_DoesNotPersistsPet_WhenPostalCodeIsNotFound() {
        PetPostRequestBody petPostRequestBody = PetPostRequestBodyCreator.createPetPostRequestBody();
        petPostRequestBody.setPostalCode("99999999");

        BDDMockito.when(addressLookupServiceMock.findByPostalCode(petPostRequestBody.getPostalCode()))
                .thenThrow(new RuntimeException("Postal code not found"));

        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> this.petService.save(petPostRequestBody))
                .withMessageContaining("Postal code not found");

        Mockito.verify(petRepositoryMock, Mockito.never()).save(ArgumentMatchers.any(Pet.class));
    }

    @Test
    @DisplayName("save does not persists pet when postal code is invalid")
    void save_DoesNotPersistsPet_WhenPostalCodeIsInvalid() {
        PetPostRequestBody petPostRequestBody = PetPostRequestBodyCreator.createPetPostRequestBody();
        petPostRequestBody.setPostalCode("99   9999999");

        BDDMockito.when(addressLookupServiceMock.findByPostalCode(petPostRequestBody.getPostalCode()))
                .thenThrow(new RuntimeException("Invalid postal code format"));

        Assertions.assertThatExceptionOfType(RuntimeException.class)
                .isThrownBy(() -> this.petService.save(petPostRequestBody))
                .withMessageContaining("Invalid postal code format");

        Mockito.verify(petRepositoryMock, Mockito.never()).save(ArgumentMatchers.any(Pet.class));
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