package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.client.ViaCepClient;
import com.projetos.diego.pet_management_system.dto.response.ViaCepResponse;
import com.projetos.diego.pet_management_system.domain.owner.Address;
import com.projetos.diego.pet_management_system.exception.InvalidPostalCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViaCepAddressLookupService implements AddressLookupService {
    private final ViaCepClient viaCepClient;

    @Override
    public Address findByPostalCode(String postalCode) {
        validatePostalCode(postalCode);
        ViaCepResponse response = viaCepClient.findByPostalCode(postalCode);
        return Address.builder()
                .street(response.getLogradouro())
                .neighbourhood(response.getBairro())
                .city(response.getLocalidade())
                .state(response.getUf().toUpperCase())
                .postalCode(response.getCep().replace("-", ""))
                .build();
    }

    private void validatePostalCode(String postalCode) {
        if (!postalCode.matches("^\\d{8}$")) {
            throw new InvalidPostalCodeException("Invalid postal code format");
        }
    }
}
