package com.projetos.diego.pet_management_system.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.projetos.diego.pet_management_system.config.SecurityConfig;
import com.projetos.diego.pet_management_system.dto.request.LoginRequest;
import com.projetos.diego.pet_management_system.dto.response.AuthenticationResponse;
import com.projetos.diego.pet_management_system.exception.InvalidCredentialsException;
import com.projetos.diego.pet_management_system.service.AuthenticationService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
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
    @DisplayName("login returns 200 when successful")
    void login_Returns200_WhenSuccessful() throws Exception {
        LoginRequest request = LoginRequest.builder()
                .username("diego123")
                .password("secret123")
                .build();
        AuthenticationResponse response = AuthenticationResponse.builder()
                .token("token-value")
                .type("Bearer")
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
                        .value(response.type()));
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