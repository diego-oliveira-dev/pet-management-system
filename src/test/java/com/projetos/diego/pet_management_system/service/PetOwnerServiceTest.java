package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.owner.PetOwner;
import com.projetos.diego.pet_management_system.dto.PetOwnerRequest;
import com.projetos.diego.pet_management_system.mapper.PetOwnerMapper;
import com.projetos.diego.pet_management_system.repository.PetOwnerRepository;
import com.projetos.diego.pet_management_system.util.PetOwnerCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

@ExtendWith(MockitoExtension.class)
class PetOwnerServiceTest {

    @InjectMocks
    private PetOwnerService petOwnerService;

    @Mock
    private PetOwnerRepository petOwnerRepositoryMock;

    @Mock
    private PetOwnerMapper petOwnerMapperMock;

    @Test
    @DisplayName("listAll returns list of owners when successful")
    void listAll_ReturnsListOfOwners_WhenSuccessful() {
        List<PetOwner> owners = List.of(PetOwnerCreator.createValidPetOwner());

        BDDMockito.when(petOwnerRepositoryMock.findAll()).thenReturn(owners);

        List<PetOwner> returnedOwnerList = petOwnerService.listAll();

        Assertions.assertThat(returnedOwnerList).isNotNull().isNotEmpty().isEqualTo(owners);
        Mockito.verify(petOwnerRepositoryMock, Mockito.times(1)).findAll();
    }

    @Test
    @DisplayName("save persists owner when successful")
    void save_PersistsOwner_WhenSuccessful() {
        PetOwnerRequest request = PetOwnerCreator.createPetOwnerPostRequest();

        PetOwner petOwner = PetOwnerCreator.createValidPetOwner();

        BDDMockito.when(petOwnerMapperMock.fromPostRequestToEntity(request))
                .thenReturn(petOwner);
        BDDMockito.when(petOwnerRepositoryMock.save(petOwner))
                .thenReturn(petOwner);

        PetOwner savedOwner = petOwnerService.save(request);

        Assertions.assertThat(savedOwner).isNotNull().isEqualTo(petOwner);
    }
}