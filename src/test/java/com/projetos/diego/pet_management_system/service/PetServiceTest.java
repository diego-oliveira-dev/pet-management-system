package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.exception.InvalidPostalCodeException;
import com.projetos.diego.pet_management_system.exception.ResourceNotFoundException;
import com.projetos.diego.pet_management_system.exception.ViaCepPostalCodeNotFoundException;
import com.projetos.diego.pet_management_system.repository.PetRepository;
import com.projetos.diego.pet_management_system.requests.PetPostRequestBody;
import com.projetos.diego.pet_management_system.requests.PetPutRequestBody;
import com.projetos.diego.pet_management_system.util.PetCreator;
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
        PetPostRequestBody petPostRequestBody = PetCreator.createPetPostRequestBody();
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
        PetPostRequestBody petPostRequestBody = PetCreator.createPetPostRequestBodyWithOnlyRequiredFields();
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
        PetPostRequestBody petPostRequestBody = PetCreator.createPetPostRequestBody();
        petPostRequestBody.setPostalCode("99999999");

        BDDMockito.when(addressLookupServiceMock.findByPostalCode(petPostRequestBody.getPostalCode()))
                .thenThrow(new RuntimeException("Postal code not found"));

        Assertions.assertThatExceptionOfType(ViaCepPostalCodeNotFoundException.class)
                .isThrownBy(() -> this.petService.save(petPostRequestBody))
                .withMessageContaining("Postal code not found");

        Mockito.verify(petRepositoryMock, Mockito.never()).save(ArgumentMatchers.any(Pet.class));
    }

    @Test
    @DisplayName("save does not persists pet when postal code is invalid")
    void save_DoesNotPersistsPet_WhenPostalCodeIsInvalid() {
        PetPostRequestBody petPostRequestBody = PetCreator.createPetPostRequestBody();
        petPostRequestBody.setPostalCode("99   9999999");

        BDDMockito.when(addressLookupServiceMock.findByPostalCode(petPostRequestBody.getPostalCode()))
                .thenThrow(new RuntimeException("Invalid postal code format"));

        Assertions.assertThatExceptionOfType(InvalidPostalCodeException.class)
                .isThrownBy(() -> this.petService.save(petPostRequestBody))
                .withMessageContaining("Invalid postal code format");

        Mockito.verify(petRepositoryMock, Mockito.never()).save(ArgumentMatchers.any(Pet.class));
    }

    @Test
    @DisplayName("replace updates the pet when successful")
    void replace_UpdatesThePet_WhenSuccessful() {
        Pet alreadySavedPet = PetCreator.createValidPet();
        String address = PetCreator.createValidAddress();
        BDDMockito.when(petRepositoryMock.findById(alreadySavedPet.getId()))
                .thenReturn(Optional.of(alreadySavedPet));
        BDDMockito.when(addressLookupServiceMock.findByPostalCode(ArgumentMatchers.anyString()))
                .thenReturn(address);
        ArgumentCaptor<Pet> petCaptor = ArgumentCaptor.forClass(Pet.class);

        PetPutRequestBody petPutRequestBody = PetCreator.createPetPutRequestBody();
        petService.replace(petPutRequestBody);
        Mockito.verify(petRepositoryMock).save(petCaptor.capture());
        Pet capturedPet = petCaptor.getValue();
        Pet expectedPet = Pet.builder()
                .id(alreadySavedPet.getId())
                .name(petPutRequestBody.getName())
                .type(petPutRequestBody.getType())
                .sex(petPutRequestBody.getSex())
                .birthDate(petPutRequestBody.getBirthDate())
                .weight(petPutRequestBody.getWeight())
                .breed(petPutRequestBody.getBreed())
                .address(address)
                .owner(petPutRequestBody.getOwner())
                .build();

        Mockito.verify(addressLookupServiceMock)
                .findByPostalCode(petPutRequestBody.getPostalCode());
        Assertions.assertThat(capturedPet).isNotNull().isEqualTo(expectedPet);
    }

    @Test
    @DisplayName("replace throws ResourceNotFoundException when pet is not found")
    void replace_ThrowsResourceNotFoundException_WhenPetIsNotFound() {
        BDDMockito.when(petRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.empty());

        PetPutRequestBody invalidPetPutRequestBody = PetCreator.createPetPutRequestBody();

        Assertions.assertThatExceptionOfType(ResourceNotFoundException.class)
                .isThrownBy(() -> this.petService.replace(invalidPetPutRequestBody))
                .withMessageContaining("Pet not found");
    }

    @Test
    @DisplayName("replace does not updates pet when postal code is not found")
    void replace_DoesNotUpdatesPet_WhenPostalCodeIsNotFound() {
        PetPutRequestBody petPutRequestBody = PetCreator.createPetPutRequestBody();
        petPutRequestBody.setPostalCode("99999999");
        BDDMockito.when(petRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(PetCreator.createValidPet()));
        BDDMockito.when(addressLookupServiceMock.findByPostalCode(petPutRequestBody.getPostalCode()))
                .thenThrow(new RuntimeException("Postal code not found"));

        Assertions.assertThatExceptionOfType(ViaCepPostalCodeNotFoundException.class)
                .isThrownBy(() -> this.petService.replace(petPutRequestBody))
                .withMessageContaining("Postal code not found");

        Mockito.verify(petRepositoryMock, Mockito.never()).save(ArgumentMatchers.any(Pet.class));
    }

    @Test
    @DisplayName("replace throws InvalidPostalCodeException when postal code is invalid")
    void replace_ThrowsInvalidPostalCodeException_WhenPostalCodeIsInvalid() {
        PetPutRequestBody petPutRequestBody = PetCreator.createPetPutRequestBody();
        petPutRequestBody.setPostalCode("99   9999999");
        BDDMockito.when(petRepositoryMock.findById(ArgumentMatchers.anyLong()))
                .thenReturn(Optional.of(PetCreator.createValidPet()));
        BDDMockito.when(addressLookupServiceMock.findByPostalCode(petPutRequestBody.getPostalCode()))
                .thenThrow(new RuntimeException("Invalid postal code format"));

        Assertions.assertThatExceptionOfType(InvalidPostalCodeException.class)
                .isThrownBy(() -> this.petService.replace(petPutRequestBody))
                .withMessageContaining("Invalid postal code format");

        Mockito.verify(petRepositoryMock, Mockito.never()).save(ArgumentMatchers.any(Pet.class));
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