package com.projetos.diego.pet_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetos.diego.pet_management_system.domain.PetOwner;
import com.projetos.diego.pet_management_system.dto.PetOwnerRequest;
import com.projetos.diego.pet_management_system.dto.PetOwnerResponse;
import com.projetos.diego.pet_management_system.mapper.PetOwnerMapper;
import com.projetos.diego.pet_management_system.service.PetOwnerService;
import com.projetos.diego.pet_management_system.util.PetOwnerCreator;
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

@WebMvcTest(PetOwnerController.class)
class PetOwnerControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PetOwnerService petOwnerServiceMock;

    @MockitoBean
    private PetOwnerMapper petOwnerMapperMock;

    @Test
    @DisplayName("listAll returns 200 when successful")
    void listAll_Returns200_WhenSuccessful() throws Exception {
        PetOwner petOwner = PetOwnerCreator.createValidPetOwner();
        PetOwnerResponse response = PetOwnerCreator.createResponse(petOwner);

        Mockito.when(petOwnerServiceMock.listAll()).thenReturn(List.of(petOwner));
        Mockito.when(petOwnerMapperMock.toResponse(petOwner)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.get("/owners/all"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].id")
                        .value(response.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name")
                        .value(response.name()));
    }

    @Test
    @DisplayName("save returns 201 when successful")
    void save_Returns201_WhenSuccessful() throws Exception {
        PetOwnerRequest request = PetOwnerCreator.createPetOwnerPostRequest();
        PetOwner petOwner = PetOwnerCreator.createValidPetOwner();
        PetOwnerResponse response = PetOwnerCreator.createResponse(petOwner);

        Mockito.when(petOwnerServiceMock.save(request)).thenReturn(petOwner);
        Mockito.when(petOwnerMapperMock.toResponse(petOwner)).thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/owners")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.id")
                        .value(response.id()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.name")
                        .value(response.name()));
    }
}