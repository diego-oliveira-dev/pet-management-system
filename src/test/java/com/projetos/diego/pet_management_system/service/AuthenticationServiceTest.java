package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.dto.request.LoginRequest;
import com.projetos.diego.pet_management_system.dto.response.AuthenticationResponse;
import com.projetos.diego.pet_management_system.security.JwtService;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtServiceMock;

    @Mock
    private AuthenticationManager authenticationManagerMock;

    @Test
    @DisplayName("login returns token when credentials are valid")
    void login_ReturnsToken_WhenCredentialsAreValid() {
        LoginRequest request = LoginRequest.builder()
                .username("diego123")
                .password("secret123")
                .build();
        Authentication authentication = Mockito.mock(Authentication.class);
        BDDMockito.when(authenticationManagerMock.authenticate(ArgumentMatchers.any()))
                .thenReturn(authentication);
        BDDMockito.when(jwtServiceMock.generateToken(authentication))
                .thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.login(request);

        Assertions.assertThat("jwt-token").isEqualTo(response.token());
        Assertions.assertThat("Bearer").isEqualTo(response.type());
        Mockito.verify(jwtServiceMock, Mockito.times(1))
                .generateToken(authentication);
    }

    @Test
    @DisplayName("login throws UsernameNotFoundException when user does not exist")
    void login_ThrowsUsernameNotFoundException_WhenUserDoesNotExist() {
        LoginRequest request = LoginRequest.builder()
                .username("invalid")
                .password("password")
                .build();
        BDDMockito.when(authenticationManagerMock.authenticate(ArgumentMatchers.any()))
                .thenThrow(new UsernameNotFoundException("Owner not found with username 'invalid'"));

        Assertions.assertThatExceptionOfType(UsernameNotFoundException.class)
                .isThrownBy(() -> this.authenticationService.login(request))
                .withMessageContaining("Owner not found with username 'invalid'");
        Mockito.verify(jwtServiceMock, Mockito.never()).generateToken(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("login throws BadCredentialsException when password is invalid")
    void login_ThrowsBadCredentialsException_WhenPasswordIsInvalid() {
        LoginRequest request = LoginRequest.builder()
                .username("diego123")
                .password("wrong")
                .build();
        BDDMockito.when(authenticationManagerMock.authenticate(ArgumentMatchers.any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        Assertions.assertThatExceptionOfType(BadCredentialsException.class)
                .isThrownBy(() -> this.authenticationService.login(request))
                .withMessageContaining("Invalid credentials");
        Mockito.verify(jwtServiceMock, Mockito.never()).generateToken(ArgumentMatchers.any());
    }
}