package com.projetos.diego.pet_management_system.util;

import com.projetos.diego.pet_management_system.dto.ViaCepResponse;

public class ViaCepResponseCreator {
    public static ViaCepResponse createViaCepResponse() {
        return ViaCepResponse.builder()
                .cep("64009-100")
                .logradouro("Rua José Marques da Rocha")
                .complemento("")
                .bairro("Memorare")
                .localidade("Teresina")
                .uf("PI")
                .ibge("2211001")
                .gia("")
                .ddd("86")
                .siafi("1219")
                .erro(null)
                .build();
    }
}
