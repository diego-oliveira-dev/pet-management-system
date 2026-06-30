package com.projetos.diego.pet_management_system.client;

import com.projetos.diego.pet_management_system.dto.response.ViaCepResponse;
import com.projetos.diego.pet_management_system.exception.ViaCepMalformedDataException;
import com.projetos.diego.pet_management_system.exception.ViaCepPostalCodeNotFoundException;
import com.projetos.diego.pet_management_system.exception.ViaCepUnavailableException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

@Component
@RequiredArgsConstructor
public class ViaCepClient {
    private final RestTemplate restTemplate;

    public ViaCepResponse findByPostalCode(String postalCode) {
        String url = String.format("https://viacep.com.br/ws/%s/json/", postalCode);
        try {
            ResponseEntity<ViaCepResponse> response = restTemplate.getForEntity(url, ViaCepResponse.class);
            ViaCepResponse body = response.getBody();
            if (body == null) {
                throw new ViaCepMalformedDataException("ViaCEP returned an empty response");
            }
            if (("true").equals(body.getErro())) {
                throw new ViaCepPostalCodeNotFoundException("Postal code not found");
            }
            return body;
        } catch (UnknownContentTypeException e) {
            throw new ViaCepMalformedDataException("ViaCEP returned an invalid response");
        } catch (HttpServerErrorException | ResourceAccessException e) {
            throw new ViaCepUnavailableException("An error occurred while getting the address. Try again later");
        }
    }
}
