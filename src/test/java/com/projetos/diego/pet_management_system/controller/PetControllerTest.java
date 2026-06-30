package com.projetos.diego.pet_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetos.diego.pet_management_system.config.SecurityConfig;
import com.projetos.diego.pet_management_system.domain.owner.PetOwner;
import com.projetos.diego.pet_management_system.domain.pet.Pet;
import com.projetos.diego.pet_management_system.dto.request.PetPostRequest;
import com.projetos.diego.pet_management_system.dto.request.PetPutRequest;
import com.projetos.diego.pet_management_system.dto.response.PetResponse;
import com.projetos.diego.pet_management_system.exception.ResourceNotFoundException;
import com.projetos.diego.pet_management_system.mapper.PetMapper;
import com.projetos.diego.pet_management_system.service.PetService;
import com.projetos.diego.pet_management_system.util.JwtCreator;
import com.projetos.diego.pet_management_system.util.PetCreator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(PetController.class)
@Import(SecurityConfig.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PetService petServiceMock;

    @MockitoBean
    private PetMapper petMapperMock;

    @Test
    @DisplayName("list returns 200 when successful")
    void list_Returns200_WhenSuccessful() throws Exception {
        long userId = 1L;
        Pet pet = PetCreator.createValidPet();
        pet.setId(1L);
        PetResponse response = PetCreator.createResponse(pet);

        Mockito.when(petServiceMock.findPetsByOwnerId(userId))
                .thenReturn(List.of(pet));
        Mockito.when(petMapperMock.toResponse(pet)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/pets")
                        .with(JwtCreator.createUserJWTById(userId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id")
                        .value(response.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name")
                        .value(response.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ownerId")
                        .value(response.ownerId()));
        Mockito.verify(petServiceMock, Mockito.times(1))
                .findPetsByOwnerId(userId);
        Mockito.verify(petMapperMock, Mockito.times(1))
                .toResponse(pet);
    }

    @Test
    @DisplayName("list returns 200 and empty list when no pet was registered")
    void list_Returns200AndEmptyList_WhenNoPetWasRegistered() throws Exception {
        long userId = 1L;
        List<Pet> emptyList = List.of();

        Mockito.when(petServiceMock.findPetsByOwnerId(userId))
                .thenReturn(emptyList);

        mockMvc.perform(MockMvcRequestBuilders.get("/pets")
                        .with(JwtCreator.createUserJWTById(userId)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", Matchers.empty()));
        Mockito.verify(petServiceMock, Mockito.times(1))
                .findPetsByOwnerId(userId);
    }

    @Test
    @DisplayName("list returns 401 when user is not authenticated")
    void list_Returns401_WhenUserIsNotAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pets"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        Mockito.verify(petServiceMock, Mockito.never()).findPetsByOwnerId(ArgumentMatchers.anyLong());
    }

    @Test
    @DisplayName("listAll returns 200 when successful")
    void listAll_Returns200_WhenSuccessful() throws Exception {
        Pet pet = PetCreator.createValidPet();
        PetResponse response = PetCreator.createResponse(pet);
        PageImpl<Pet> petPage = new PageImpl<>(List.of(pet));

        Mockito.when(petServiceMock.listAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(petPage);
        Mockito.when(petMapperMock.toResponse(pet)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/pets/all")
                        .with(JwtCreator.createAdminJWT()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.size")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].id")
                        .value(response.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].ownerId")
                        .value(response.ownerId()));
        Mockito.verify(petServiceMock, Mockito.times(1))
                .listAll(ArgumentMatchers.any(Pageable.class));
        Mockito.verify(petMapperMock, Mockito.times(1))
                .toResponse(pet);
    }

    @Test
    @DisplayName("listAll returns 200 and empty page when no pet exists")
    void listAll_Returns200AndEmptyPage_WhenNoPetExists() throws Exception {
        PageImpl<Pet> petPage = new PageImpl<>(List.of());

        Mockito.when(petServiceMock.listAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(petPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/pets/all")
                        .with(JwtCreator.createAdminJWT()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements")
                        .value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.size")
                        .value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content")
                        .isArray());
        Mockito.verify(petServiceMock, Mockito.times(1))
                .listAll(ArgumentMatchers.any(Pageable.class));
    }

    @Test
    @DisplayName("listAll returns 403 when user is not admin")
    void listAll_Returns403_WhenUserIsNotAdmin() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/pets/all")
                        .with(JwtCreator.createUserJWT()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
        Mockito.verifyNoInteractions(petServiceMock);
    }

    @Test
    @DisplayName("findById returns 200 when successful")
    void findById_Returns200_WhenSuccessful() throws Exception {
        Pet pet = PetCreator.createValidPet();
        pet.setId(1L);
        PetOwner petOwner = pet.getPetOwner();
        PetResponse response = PetCreator.createResponse(pet);

        Mockito.when(petServiceMock.findPetByIdAndPetOwnerId(pet.getId(), petOwner.getId()))
                .thenReturn(pet);
        Mockito.when(petMapperMock.toResponse(pet)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/pets/{id}", pet.getId())
                        .with(JwtCreator.createUserJWTById(petOwner.getId())))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(response.id()));
        Mockito.verify(petServiceMock, Mockito.times(1))
                .findPetByIdAndPetOwnerId(pet.getId(), petOwner.getId());
        Mockito.verify(petMapperMock, Mockito.times(1))
                .toResponse(pet);
    }

    @Test
    @DisplayName("findById returns 404 when pet is not found")
    void findById_Returns404_WhenPetIsNotFound() throws Exception {
        Mockito.when(petServiceMock.findPetByIdAndPetOwnerId(ArgumentMatchers.anyLong(), ArgumentMatchers.anyLong()))
                .thenThrow(new ResourceNotFoundException("Pet not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/pets/{id}", 1L)
                        .with(JwtCreator.createUserJWT()))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Resource Not Found"));
    }

    @Test
    @DisplayName("findByName returns 200 when successful")
    void findByName_Returns200_WhenSuccessful() throws Exception {
        Pet pet = PetCreator.createValidPet();
        pet.setId(1L);
        PetResponse response = PetCreator.createResponse(pet);

        Mockito.when(petServiceMock.findPetsByName(pet.getName(), pet.getPetOwner().getId()))
                .thenReturn(List.of(pet));
        Mockito.when(petMapperMock.toResponse(pet)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/pets/find?name={name}", pet.getName())
                        .with(JwtCreator.createUserJWT()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id")
                        .value(response.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name")
                        .value(response.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].ownerId")
                        .value(response.ownerId()));
        Mockito.verify(petServiceMock, Mockito.times(1))
                .findPetsByName(pet.getName(), pet.getPetOwner().getId());
        Mockito.verify(petMapperMock, Mockito.times(1))
                .toResponse(pet);
    }

    @Test
    @DisplayName("save returns 201 when successful")
    void save_Returns201_WhenSuccessful() throws Exception {
        PetPostRequest request = PetCreator.createPetPostRequest();
        Pet pet = PetCreator.createValidPet();
        PetResponse response = PetCreator.createResponse(pet);

        Mockito.when(petServiceMock.save(request, pet.getPetOwner().getId())).thenReturn(pet);
        Mockito.when(petMapperMock.toResponse(pet)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .with(JwtCreator.createUserJWT())
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(response.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value(response.name()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.ownerId")
                        .value(response.ownerId()));
    }

    @Test
    @DisplayName("save returns 401 when request is not authenticated")
    void save_Returns401_WhenRequestIsNotAuthenticated() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.post("/pets"))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
        Mockito.verifyNoInteractions(petServiceMock);
    }

    @Test
    @DisplayName("save returns 400 when weight is negative")
    void save_Returns400_WhenWeightIsNegative() throws Exception {
        PetPostRequest petPostRequest = PetCreator.createPetPostRequest();
        petPostRequest.setWeight(-32.0);

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .with(JwtCreator.createUserJWT())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petPostRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Validation Failed"));
        Mockito.verifyNoInteractions(petServiceMock);
    }

    @Test
    @DisplayName("save returns 400 when name is blank")
    void save_Returns400_WhenNameIsBlank() throws Exception {
        PetPostRequest petPostRequest = PetCreator.createPetPostRequest();
        petPostRequest.setName("");

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .with(JwtCreator.createUserJWT())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petPostRequest))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Validation Failed"));
        Mockito.verifyNoInteractions(petServiceMock);
    }

    @Test
    @DisplayName("replace returns 204 when successful")
    void replace_Returns204_WhenSuccessful() throws Exception {
        long ownerId = 1L;
        PetPutRequest request = PetCreator.createPetPutRequest();

        mockMvc.perform(MockMvcRequestBuilders.put("/pets")
                        .with(JwtCreator.createUserJWTById(ownerId))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
        Mockito.verify(petServiceMock, Mockito.times(1))
                .replace(request, ownerId);
    }

    @Test
    @DisplayName("replace returns 400 when ID is null")
    void replace_Returns400_WhenIdIsNull() throws Exception {
        PetPutRequest petPutRequest = PetCreator.createPetPutRequest();
        petPutRequest.setId(null);

        mockMvc.perform(MockMvcRequestBuilders.put("/pets")
                        .with(JwtCreator.createUserJWT())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petPutRequest)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Validation Failed"));
        Mockito.verifyNoInteractions(petServiceMock);
    }

    @Test
    @DisplayName("replace returns 404 when pet is not found")
    void replace_Returns404_WhenPetIsNotFound() throws Exception {
        long ownerId = 1L;
        PetPutRequest petPutRequest = PetCreator.createPetPutRequest();
        BDDMockito.willThrow(new ResourceNotFoundException("Pet not found"))
                .given(petServiceMock)
                .replace(petPutRequest, ownerId);

        mockMvc.perform(MockMvcRequestBuilders.put("/pets")
                        .with(JwtCreator.createUserJWT())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petPutRequest)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("delete returns 204 when successful")
    void delete_Returns204_WhenSuccessful() throws Exception {
        long petId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/pets/{id}", petId)
                        .with(JwtCreator.createAdminJWT()))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(petServiceMock, Mockito.times(1))
                .delete(petId);
    }

    @Test
    @DisplayName("delete returns 404 when pet is not found")
    void delete_Returns404_WhenPetIsNotFound() throws Exception {
        long petId = 1L;
        BDDMockito.willThrow(new ResourceNotFoundException("Pet not found"))
                .given(petServiceMock)
                .delete(ArgumentMatchers.anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/pets/{id}", petId)
                        .with(JwtCreator.createAdminJWT()))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("delete returns 403 when user is not admin")
    void delete_Returns403_WhenUserIsNotAdmin() throws Exception {
        long petId = 1L;
        mockMvc.perform(MockMvcRequestBuilders.delete("/pets/{id}", petId)
                        .with(JwtCreator.createUserJWT()))
                .andExpect(MockMvcResultMatchers.status().isForbidden());
        Mockito.verifyNoInteractions(petServiceMock);
    }
}