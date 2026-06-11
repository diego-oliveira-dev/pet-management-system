package com.projetos.diego.pet_management_system.util;

import com.projetos.diego.pet_management_system.requests.PetPutRequestBody;

public class PetPutRequestBodyCreator {
    public static PetPutRequestBody createPetPutRequestBody() {
        return PetPutRequestBody.builder()
                .id(1L)
                .name("Rex")
                .build();
    }
}
