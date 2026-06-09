package com.projetos.diego.pet_management_system.controller;

import com.projetos.diego.pet_management_system.domain.Pet;
import com.projetos.diego.pet_management_system.service.PetService;
import com.projetos.diego.pet_management_system.util.PetCreator;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.util.List;

@WebMvcTest(PetController.class)
class PetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PetService petService;

    @Test
    void list_ReturnsListOfPets_WhenSuccessful() throws Exception {

        List<Pet> pets = List.of(PetCreator.createValidPet());

        Mockito.when(petService.listAll()).thenReturn(pets);

        mockMvc.perform(MockMvcRequestBuilders.get("/pets"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].name")
                        .value(pets.getFirst().getName()));
    }
}