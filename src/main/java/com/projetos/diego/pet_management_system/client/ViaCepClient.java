package com.projetos.diego.pet_management_system.client;

import com.projetos.diego.pet_management_system.exception.PostalCodeNotFoundException;
import com.projetos.diego.pet_management_system.responses.ViaCepResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

@Component
@RequiredArgsConstructor
public class ViaCepClient {
    private final RestTemplate restTemplate;

    public ViaCepResponse findByPostalCode(String postalCode) {
        String url = String.format("https://viacep.com.br/ws/%s/json/", postalCode);
        try {
            ViaCepResponse response = restTemplate.getForObject(url, ViaCepResponse.class);
            if (response == null) throw new RuntimeException("An error occurred while getting the address. Try again later");
            if (("true").equals(response.getErro())) throw new PostalCodeNotFoundException("Postal code not found");
            return response;
        } catch (HttpServerErrorException e) {
            throw new RuntimeException("An error occurred while getting the address. Try again later");
        }
    }
}
