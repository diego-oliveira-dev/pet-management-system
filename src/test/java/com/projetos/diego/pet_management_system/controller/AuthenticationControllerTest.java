package com.projetos.diego.pet_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetos.diego.pet_management_system.config.SecurityConfig;
import com.projetos.diego.pet_management_system.dto.request.LoginRequest;
import com.projetos.diego.pet_management_system.dto.request.RegisterRequest;
import com.projetos.diego.pet_management_system.dto.response.AuthenticationResponse;
import com.projetos.diego.pet_management_system.exception.InvalidCredentialsException;
import com.projetos.diego.pet_management_system.exception.InvalidPostalCodeException;
import com.projetos.diego.pet_management_system.exception.UsernameAlreadyExistsException;
import com.projetos.diego.pet_management_system.exception.ViaCepPostalCodeNotFoundException;
import com.projetos.diego.pet_management_system.service.AuthenticationService;
import com.projetos.diego.pet_management_system.util.JwtCreator;
import com.projetos.diego.pet_management_system.util.PetOwnerCreator;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(AuthenticationController.class)
@Import(SecurityConfig.class)
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthenticationService authenticationServiceMock;

    @Test
    @DisplayName("register returns 201 when successful")
    void register_Returns201_WhenSuccessful() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("Diego Oliveira")
                .username("diego123")
                .password("secret123")
                .postalCode("64009100")
                .build();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("jwt-token")
                .type("Bearer")
                .owner(PetOwnerCreator.createResponse(PetOwnerCreator.createValidPetOwner()))
                .build();

        BDDMockito.when(authenticationServiceMock.register(request))
                .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token")
                        .value(response.token()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type")
                        .value(response.type()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.username")
                        .value(response.owner().username()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.address.postalCode")
                        .value(response.owner().address().getPostalCode()));
    }

    @Test
    @DisplayName("register returns 400 when credentials are invalid")
    void register_Returns400_WhenCredentialsAreInvalid() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("                   ")
                .username("")
                .password(null)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Validation Failed"));;
    }

    @Test
    @DisplayName("register returns 400 when postal code is invalid")
    void register_Returns400_WhenPostalCodeIsInvalid() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("Diego Oliveira")
                .username("diego123")
                .password("secret123")
                .postalCode("invalid postal code")
                .build();
        Mockito.when(authenticationServiceMock.register(request))
                .thenThrow(new InvalidPostalCodeException("Invalid postal code format"));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(JwtCreator.createUserJWT())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Validation Failed"));
    }

    @Test
    @DisplayName("register returns 404 when postal code is not found")
    void register_Returns404_WhenPostalCodeIsNotFound() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("Diego Oliveira")
                .username("diego123")
                .password("secret123")
                .postalCode("99999999")
                .build();
        Mockito.when(authenticationServiceMock.register(request))
                .thenThrow(new ViaCepPostalCodeNotFoundException("Postal code not found"));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .with(JwtCreator.createUserJWT())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request))
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Postal Code Not Found"));
    }

    @Test
    @DisplayName("register returns 409 when username already exists")
    void register_Returns409_WhenUsernameAlreadyExists() throws Exception {
        RegisterRequest request = RegisterRequest.builder()
                .name("Diego Oliveira")
                .username("diego123")
                .password("secret123")
                .postalCode("64009100")
                .build();

        BDDMockito.when(authenticationServiceMock.register(request))
                .thenThrow(new UsernameAlreadyExistsException("Username is already in use"));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/register")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isConflict());
    }

    @Test
    @DisplayName("login returns 200 when successful")
    void login_Returns200_WhenSuccessful() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .username("diego123")
                .password("secret123")
                .build();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("token-value")
                .type("Bearer")
                .owner(PetOwnerCreator.createResponse(PetOwnerCreator.createValidPetOwner()))
                .build();

        BDDMockito.when(authenticationServiceMock.login(request))
                        .thenReturn(response);

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.token")
                        .value(response.token()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.type")
                        .value(response.type()))
                .andExpect(MockMvcResultMatchers.jsonPath("$.owner.username")
                        .value(response.owner().username()));
    }

    @Test
    @DisplayName("login returns 400 when credentials are invalid")
    void login_Returns400_WhenCredentialsAreInvalid() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .username("")
                .password(null)
                .build();

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.jsonPath("$.title")
                        .value("Validation Failed"));;
    }

    @Test
    @DisplayName("login returns 401 when user is not found or password is incorrect")
    void login_Returns401_WhenUserIsNotFoundOrPasswordIsIncorrect() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .username("invalid")
                .password("not a password")
                .build();

        BDDMockito.when(authenticationServiceMock.login(request))
                .thenThrow(new InvalidCredentialsException("Invalid username or password"));

        mockMvc.perform(MockMvcRequestBuilders.post("/auth/login")
                        .accept(MediaType.APPLICATION_JSON)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(MockMvcResultMatchers.status().isUnauthorized());
    }
}