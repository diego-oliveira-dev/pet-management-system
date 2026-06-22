package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.client.ViaCepClient;
import com.projetos.diego.pet_management_system.client.ViaCepResponse;
import com.projetos.diego.pet_management_system.domain.Address;
import com.projetos.diego.pet_management_system.exception.InvalidPostalCodeException;
import com.projetos.diego.pet_management_system.exception.ViaCepPostalCodeNotFoundException;
import com.projetos.diego.pet_management_system.util.PetCreator;
import com.projetos.diego.pet_management_system.util.ViaCepResponseCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ViaCepAddressLookupServiceTest {

    @InjectMocks
    private ViaCepAddressLookupService viaCepAddressLookupService;

    @Mock
    private ViaCepClient viaCepClientMock;

    @Test
    @DisplayName("findByPostalCode returns address when successful")
    void findByPostalCode_ReturnsAddress_WhenSuccessful() {
        String validPostalCode = "64009100";
        ViaCepResponse response = ViaCepResponseCreator.createViaCepResponse();
        BDDMockito.when(viaCepClientMock.findByPostalCode(validPostalCode))
                .thenReturn(response);

        Address address = viaCepAddressLookupService.findByPostalCode(validPostalCode);
        Address expectedAddress = PetCreator.createValidAddress();

        Assertions.assertThat(address).isNotNull().isEqualTo(expectedAddress);
        Mockito.verify(viaCepClientMock, Mockito.times(1))
                .findByPostalCode(validPostalCode);
    }

    @Test
    @DisplayName("findByPostalCode throws ViaCepPostalCodeNotFoundException when postal code is not found")
    void findByPostalCode_ThrowsViaCepPostalCodeNotFoundException_WhenPostalCodeIsNotFound() {
        String invalidPostalCode = "99999999";
        BDDMockito.when(viaCepClientMock.findByPostalCode(invalidPostalCode))
                .thenThrow(new ViaCepPostalCodeNotFoundException("Postal code not found"));

        Assertions.assertThatExceptionOfType(ViaCepPostalCodeNotFoundException.class)
                .isThrownBy(() -> this.viaCepAddressLookupService.findByPostalCode(invalidPostalCode))
                .withMessageContaining("Postal code not found");
        Mockito.verify(viaCepClientMock).findByPostalCode(invalidPostalCode);
    }

    @Test
    @DisplayName("findByPostalCode throws InvalidPostalCodeException when postal code format is invalid")
    void findByPostalCode_ThrowsInvalidPostalCodeException_WhenPostalCodeFormatIsInvalid() {
        String invalidPostalCode = "6400910";

        Assertions.assertThatExceptionOfType(InvalidPostalCodeException.class)
                .isThrownBy(() -> this.viaCepAddressLookupService.findByPostalCode(invalidPostalCode))
                .withMessageContaining("Invalid postal code format");
        Mockito.verifyNoInteractions(viaCepClientMock);
    }

}