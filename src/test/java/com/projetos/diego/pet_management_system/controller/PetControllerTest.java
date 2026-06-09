package com.projetos.diego.pet_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.exception.ResourceNotFoundException;
import com.projetos.diego.pet_management_system.requests.PetPostRequestBody;
import com.projetos.diego.pet_management_system.service.PetService;
import com.projetos.diego.pet_management_system.util.PetCreator;
import com.projetos.diego.pet_management_system.util.PetPostRequestBodyCreator;
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
    private ObjectMapper mapper;

    @MockitoBean
    private PetService petService;

    @Test
    @DisplayName("list returns list of pets when successful")
    void list_ReturnsListOfPets_WhenSuccessful() throws Exception {

        List<Pet> pets = List.of(PetCreator.createValidPet());

        Mockito.when(petService.listAll()).thenReturn(pets);

        mockMvc.perform(MockMvcRequestBuilders.get("/pets"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name")
                        .value(pets.getFirst().getName()));
    }

    @Test
    @DisplayName("findById returns pet when successful")
    void findById_ReturnsPet_WhenSuccessful() throws Exception {
        Pet pet = PetCreator.createValidPet();

        Mockito.when(petService.findByIdOrThrowResourceNotFoundException(pet.getId()))
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

        Mockito.when(petService.findByIdOrThrowResourceNotFoundException(id))
                .thenThrow(new ResourceNotFoundException("Pet not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/pets/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Resource Not Found Exception"));
    }

    @Test
    @DisplayName("save returns 200 when successful")
    void save_Returns200_WhenSuccessful() throws Exception {
        Pet pet = PetCreator.createPetToBeSaved();
        PetPostRequestBody petPostRequestBody = PetPostRequestBodyCreator.createPetPostRequestBody();

        Mockito.when(petService.save(petPostRequestBody)).thenReturn(pet);

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(petPostRequestBody)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(pet.getId()));
    }
}