package com.projetos.diego.pet_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.exception.InvalidPostalCodeException;
import com.projetos.diego.pet_management_system.exception.ResourceNotFoundException;
import com.projetos.diego.pet_management_system.exception.ViaCepPostalCodeNotFoundException;
import com.projetos.diego.pet_management_system.requests.PetPostRequestBody;
import com.projetos.diego.pet_management_system.requests.PetPutRequestBody;
import com.projetos.diego.pet_management_system.service.PetService;
import com.projetos.diego.pet_management_system.util.PetCreator;
import com.projetos.diego.pet_management_system.util.PetPostRequestBodyCreator;
import com.projetos.diego.pet_management_system.util.PetPutRequestBodyCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
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
    @DisplayName("listAll returns 200 when successful")
    void listAll_Returns200_WhenSuccessful() throws Exception {

        List<Pet> pets = List.of(PetCreator.createValidPet());

        Mockito.when(petServiceMock.listAllNonPageable()).thenReturn(pets);

        mockMvc.perform(MockMvcRequestBuilders.get("/pets/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name")
                        .value(pets.getFirst().getName()));
    }

    @Test
    @DisplayName("list returns 200 when successful")
    void list_Returns200_WhenSuccessful() throws Exception {
        PageImpl<Pet> petPage = new PageImpl<>(List.of(PetCreator.createValidPet()));

        Mockito.when(petServiceMock.listAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(petPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/pets"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.size")
                        .value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.content[0].name")
                        .value(petPage.getContent().getFirst().getName()));
    }

    @Test
    @DisplayName("list returns 200 and empty page when no pet exists")
    void list_Returns200AndEmptyPage_WhenNoPetExists() throws Exception {
        PageImpl<Pet> petPage = new PageImpl<>(List.of());

        Mockito.when(petServiceMock.listAll(ArgumentMatchers.any(Pageable.class)))
                .thenReturn(petPage);

        mockMvc.perform(MockMvcRequestBuilders.get("/pets"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.totalElements")
                        .value(0))
                .andExpect(MockMvcResultMatchers.jsonPath("$.page.size")
                        .value(0));
    }

    @Test
    @DisplayName("findById returns 200 when successful")
    void findById_Returns200_WhenSuccessful() throws Exception {
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
    void findById_Returns404_WhenPetIsNotFound() throws Exception {
        Long id = PetCreator.createValidPet().getId();

        Mockito.when(petServiceMock.findByIdOrThrowResourceNotFoundException(id))
                .thenThrow(new ResourceNotFoundException("Pet not found"));

        mockMvc.perform(MockMvcRequestBuilders.get("/pets/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Resource Not Found"));
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
    @DisplayName("save returns 404 when postal code is not found")
    void save_Returns404_WhenPostalCodeIsNotFound() throws Exception {
        PetPostRequestBody petPostRequestBody = PetPostRequestBodyCreator.createPetPostRequestBody();
        Mockito.when(petServiceMock.save(petPostRequestBody))
                .thenThrow(new ViaCepPostalCodeNotFoundException("Postal code not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petPostRequestBody))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Postal Code Not Found"));
    }

    @Test
    @DisplayName("save returns 400 when postal code is invalid")
    void save_Returns400_WhenPostalCodeIsNotFound() throws Exception {
        PetPostRequestBody petPostRequestBody = PetPostRequestBodyCreator.createPetPostRequestBody();
        Mockito.when(petServiceMock.save(petPostRequestBody))
                .thenThrow(new InvalidPostalCodeException("Invalid postal code format"));

        mockMvc.perform(MockMvcRequestBuilders.post("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petPostRequestBody))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Invalid Postal Code Format"));
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

        BDDMockito.willThrow(new ResourceNotFoundException("Pet not found"))
                .given(petServiceMock)
                .replace(petPutRequestBody);

        mockMvc.perform(MockMvcRequestBuilders.put("/pets")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(petPutRequestBody)))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }

    @Test
    @DisplayName("delete returns 204 when successful")
    void delete_Returns204_WhenSuccessful() throws Exception {
        Long id = PetCreator.createValidPet().getId();

        mockMvc.perform(MockMvcRequestBuilders.delete("/pets/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNoContent());

        Mockito.verify(petServiceMock, Mockito.times(1)).delete(id);
    }

    @Test
    @DisplayName("delete returns 404 when pet is not found")
    void delete_Returns404_WhenPetIsNotFound() throws Exception {
        Long id = PetCreator.createValidPet().getId();

        BDDMockito.willThrow(new ResourceNotFoundException("Pet not found"))
                .given(petServiceMock)
                .delete(ArgumentMatchers.anyLong());

        mockMvc.perform(MockMvcRequestBuilders.delete("/pets/{id}", id))
                .andExpect(MockMvcResultMatchers.status().isNotFound());
    }
}