package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.client.ViaCepClient;
import com.projetos.diego.pet_management_system.client.ViaCepResponse;
import com.projetos.diego.pet_management_system.exception.InvalidPostalCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViaCepAddressLookupService implements AddressLookupService {
    private final ViaCepClient viaCepClient;

    @Override
    public String findByPostalCode(String postalCode) {
        if (postalCode == null) return null;
        if (!postalCode.matches("^\\d{8}$")) {
            throw new InvalidPostalCodeException("Invalid postal code format");
        }
        ViaCepResponse response = viaCepClient.findByPostalCode(postalCode);
        return String.format("%s, %s, %s - %s, %s",
                response.getLogradouro(),
                response.getBairro(),
                response.getLocalidade(),
                response.getUf().toUpperCase(),
                response.getCep()
        );
    }
}
