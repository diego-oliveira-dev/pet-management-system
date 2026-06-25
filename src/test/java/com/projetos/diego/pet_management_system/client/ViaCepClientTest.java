package com.projetos.diego.pet_management_system.client;

import com.projetos.diego.pet_management_system.dto.ViaCepResponse;
import com.projetos.diego.pet_management_system.exception.ViaCepMalformedDataException;
import com.projetos.diego.pet_management_system.exception.ViaCepPostalCodeNotFoundException;
import com.projetos.diego.pet_management_system.exception.ViaCepUnavailableException;
import com.projetos.diego.pet_management_system.util.ViaCepResponseCreator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.client.UnknownContentTypeException;

@ExtendWith(MockitoExtension.class)
class ViaCepClientTest {
    @InjectMocks
    private ViaCepClient viaCepClient;

    @Mock
    private RestTemplate restTemplateMock;

    @Test
    @DisplayName("findByPostalCode returns ViaCepResponse when successful")
    void findByPostalCode_ReturnsViaCepResponse_WhenSuccessful() {
        String postalCode = "64009100";
        String url = String.format("https://viacep.com.br/ws/%s/json/", postalCode);
        ViaCepResponse response = ViaCepResponseCreator.createViaCepResponse();
        BDDMockito.when(restTemplateMock.getForEntity(url, ViaCepResponse.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        ViaCepResponse viaCepResponse = viaCepClient.findByPostalCode(postalCode);

        Assertions.assertThat(viaCepResponse).isNotNull().isEqualTo(response);
    }

    @Test
    @DisplayName("findByPostalCode throws ViaCepMalformedDataException when response is null")
    void findByPostalCode_ThrowsViaCepMalformedDataException_WhenResponseIsNull() {
        String postalCode = "64009100";
        String url = String.format("https://viacep.com.br/ws/%s/json/", postalCode);
        BDDMockito.when(restTemplateMock.getForEntity(url, ViaCepResponse.class))
                .thenReturn(new ResponseEntity<>(null, HttpStatus.OK));

        Assertions.assertThatExceptionOfType(ViaCepMalformedDataException.class)
                .isThrownBy(() -> this.viaCepClient.findByPostalCode(postalCode))
                .withMessageContaining("ViaCEP returned an empty response");
    }

    @Test
    @DisplayName("findByPostalCode throws ViaCepPostalCodeNotFoundException when postal code is not found")
    void findByPostalCode_ThrowsViaCepPostalCodeNotFoundException_WhenPostalCodeIsNotFound() {
        String postalCode = "99999999";
        String url = String.format("https://viacep.com.br/ws/%s/json/", postalCode);
        ViaCepResponse response = ViaCepResponseCreator.createViaCepResponse();
        response.setErro("true");
        BDDMockito.when(restTemplateMock.getForEntity(url, ViaCepResponse.class))
                .thenReturn(new ResponseEntity<>(response, HttpStatus.OK));

        Assertions.assertThatExceptionOfType(ViaCepPostalCodeNotFoundException.class)
                .isThrownBy(() -> this.viaCepClient.findByPostalCode(postalCode))
                .withMessageContaining("Postal code not found");
    }

    @Test
    @DisplayName("findByPostalCode throws ViaCepMalformedDataException when response is malformed")
    void findByPostalCode_ThrowsViaCepMalformedDataException_WhenResponseIsMalformed() {
        String postalCode = "99999999";
        String url = String.format("https://viacep.com.br/ws/%s/json/", postalCode);
        UnknownContentTypeException exception =
                new UnknownContentTypeException(
                        ViaCepResponse.class,
                        MediaType.TEXT_HTML,
                        HttpStatus.OK,
                        "OK",
                        HttpHeaders.EMPTY,
                        new byte[0]
                );
        BDDMockito.when(restTemplateMock.getForEntity(url, ViaCepResponse.class))
                .thenThrow(exception);

        Assertions.assertThatExceptionOfType(ViaCepMalformedDataException.class)
                .isThrownBy(() -> this.viaCepClient.findByPostalCode(postalCode))
                .withMessageContaining("ViaCEP returned an invalid response");
    }

    @Test
    @DisplayName("findByPostalCode throws ViaCepUnavailableException when server or network error occur")
    void findByPostalCode_ThrowsViaCepUnavailableException_WhenServerOrNetworkErrorOccur() {
        String postalCode = "99999999";
        String url = String.format("https://viacep.com.br/ws/%s/json/", postalCode);
        BDDMockito.when(restTemplateMock.getForEntity(url, ViaCepResponse.class))
                .thenThrow(new ResourceAccessException("Error"));

        Assertions.assertThatExceptionOfType(ViaCepUnavailableException.class)
                .isThrownBy(() -> this.viaCepClient.findByPostalCode(postalCode))
                .withMessageContaining("An error occurred while getting the address. Try again later");
    }
}