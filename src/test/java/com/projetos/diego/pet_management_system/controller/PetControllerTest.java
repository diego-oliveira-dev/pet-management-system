package com.projetos.diego.pet_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.exception.ResourceNotFoundException;
import com.projetos.diego.pet_management_system.requests.PetPostRequestBody;
import com.projetos.diego.pet_management_system.requests.PetPutRequestBody;
import com.projetos.diego.pet_management_system.service.PetService;
import com.projetos.diego.pet_management_system.util.PetCreator;
import com.projetos.diego.pet_management_system.util.PetPostRequestBodyCreator;
import com.projetos.diego.pet_management_system.util.PetPutRequestBodyCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PetService petServiceMock;

    @Test
    @DisplayName("list returns 200 when successful")
    void list_ReturnsListOfPets_WhenSuccessful() throws Exception {

        List<Pet> pets = List.of(PetCreator.createValidPet());

        Mockito.when(petServiceMock.listAll()).thenReturn(pets);

        mockMvc.perform(MockMvcRequestBuilders.get("/pets"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name")
                        .value(pets.getFirst().getName()));
    }

    @Test
    @DisplayName("findById returns 200 when successful")
    void findById_ReturnsPet_WhenSuccessful() throws Exception {
        Pet pet = PetCreator.createValidPet();

        Mockito.when(petServiceMock.findByIdOrThrowResourceNotFoundException(pet.getId()))
                .thenReturn(pet);

        mockMvc.perform(MockMvcRequestBuilders.get("/pets/{id}", pet.getId()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(pet.getId()));
    }

    @Test
    @DisplayName("findById returns 404 when pet is not found")
    void findById_Returns400_WhenPetIsNotFound() throws Exception {
        Long id = PetCreator.createValidPet().getId();

        Mockito.when(petServiceMock.findByIdOrThrowResourceNotFoundException(id))
                .thenThrow(new ResourceNotFoundException("Pet not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/pets/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Resource Not Found Exception"));
    }

    @Test
    @DisplayName("findByName returns 200 when successful")
    void findByName_Returns200_WhenSuccessful() throws Exception {
        Pet pet = PetCreator.createValidPet();

        Mockito.when(petServiceMock.findByName(pet.getName()))
                .thenReturn(List.of(pet));

        mockMvc.perform(MockMvcRequestBuilders.get("/pets/find?name={name}", pet.getName()))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name")
                        .value(pet.getName()));
    }

    @Test
    @DisplayName("save returns 201 when successful")
    void save_Returns201_WhenSuccessful() throws Exception {
        Pet pet = PetCreator.createPetToBeSaved();
        PetPostRequestBody petPostRequestBody = PetPostRequestBodyCreator.createPetPostRequestBody();

        Mockito.when(petServiceMock.save(petPostRequestBody)).thenReturn(pet);

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petPostRequestBody)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(pet.getId()));
    }

    @Test
    @DisplayName("replace returns 204 when successful")
    void replace_Returns204_WhenSuccessful() throws Exception {
        PetPutRequestBody petPutRequestBody = PetPutRequestBodyCreator.createPetPutRequestBody();

        mockMvc.perform(MockMvcRequestBuilders.put("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petPutRequestBody)))
                .andExpect(MockMvcResultMatchers.status().isNoContent());
    }

    @Test
    @DisplayName("replace returns 404 when pet is not found")
    void replace_Returns404_WhenPetIsNotFound() throws Exception {
        PetPutRequestBody petPutRequestBody = PetPutRequestBodyCreator.createPetPutRequestBody();

        Mockito.when(petServiceMock.replace(petPutRequestBody))
                .thenThrow(new ResourceNotFoundException("Pet not found"));

        mockMvc.perform(MockMvcRequestBuilders.put("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petPutRequestBody)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}