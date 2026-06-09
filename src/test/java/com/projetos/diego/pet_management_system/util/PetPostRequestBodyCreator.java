package com.projetos.diego.pet_management_system.util;

import com.projetos.diego.pet_management_system.requests.PetPostRequestBody;

public class PetPostRequestBodyCreator {
    public static PetPostRequestBody createPetPostRequestBody() {
        return PetPostRequestBody.builder()
                .name("Zaya")
                .build();
    }
}
