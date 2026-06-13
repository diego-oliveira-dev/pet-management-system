package com.projetos.diego.pet_management_system.client.viacep;

import com.projetos.diego.pet_management_system.service.AddressLookupService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ViaCepAddressLookupService implements AddressLookupService {
    private final ViaCepClient viaCepClient;

    @Override
    public String findByPostalCode(String postalCode) {
        if (postalCode == null) {
            return null;
        }
        ViaCepResponse viaCepResponse = viaCepClient.findByPostalCode(postalCode);

        String street = viaCepResponse.getLogradouro();
        String neighbourhood = viaCepResponse.getBairro();
        String city = viaCepResponse.getLocalidade();
        String state = viaCepResponse.getUf();

        String address = String.format("%s, %s, %s - %s", street, neighbourhood, city, state.toUpperCase());

        return address;
    }
}
