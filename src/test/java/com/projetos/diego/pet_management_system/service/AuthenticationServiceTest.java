package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.owner.Address;
import com.projetos.diego.pet_management_system.domain.owner.PetOwner;
import com.projetos.diego.pet_management_system.domain.owner.UserRole;
import com.projetos.diego.pet_management_system.dto.request.LoginRequest;
import com.projetos.diego.pet_management_system.dto.request.RegisterRequest;
import com.projetos.diego.pet_management_system.dto.response.AuthenticationResponse;
import com.projetos.diego.pet_management_system.dto.response.PetOwnerResponse;
import com.projetos.diego.pet_management_system.exception.InvalidCredentialsException;
import com.projetos.diego.pet_management_system.exception.InvalidPostalCodeException;
import com.projetos.diego.pet_management_system.exception.UsernameAlreadyExistsException;
import com.projetos.diego.pet_management_system.exception.ViaCepPostalCodeNotFoundException;
import com.projetos.diego.pet_management_system.mapper.PetOwnerMapper;
import com.projetos.diego.pet_management_system.repository.PetOwnerRepository;
import com.projetos.diego.pet_management_system.security.JwtService;
import com.projetos.diego.pet_management_system.security.UserAuthenticated;
import com.projetos.diego.pet_management_system.util.PetOwnerCreator;
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
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
class AuthenticationServiceTest {

    @InjectMocks
    private AuthenticationService authenticationService;

    @Mock
    private JwtService jwtServiceMock;

    @Mock
    private AuthenticationManager authenticationManagerMock;

    @Mock
    private PetOwnerMapper petOwnerMapperMock;

    @Mock
    private PasswordEncoder passwordEncoderMock;

    @Mock
    private PetOwnerRepository petOwnerRepositoryMock;

    @Mock
    private AddressLookupService addressLookupServiceMock;

    @Test
    @DisplayName("register returns token and persists owner when successful")
    void register_ReturnsTokenAndPersistsOwner_WhenSuccessful() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Diego Oliveira")
                .username("diego123")
                .password("secret123")
                .postalCode("64009100")
                .build();
        String encodedPassword = "encoded password";
        PetOwner owner = PetOwnerCreator.createValidPetOwner();
        owner.setId(1L);
        PetOwnerResponse ownerResponse = PetOwnerCreator.createResponse(owner);
        Address expectedAddress = PetOwnerCreator.createValidAddress();

        BDDMockito.when(petOwnerRepositoryMock.existsByUsername(request.getUsername()))
                .thenReturn(false);
        BDDMockito.when(passwordEncoderMock.encode(request.getPassword()))
                .thenReturn(encodedPassword);
        BDDMockito.when(addressLookupServiceMock.findByPostalCode(request.getPostalCode()))
                .thenReturn(expectedAddress);
        BDDMockito.when(petOwnerMapperMock.fromPostRequestToEntity(
                        request, encodedPassword, UserRole.USER, expectedAddress))
                .thenReturn(owner);
        BDDMockito.when(petOwnerMapperMock.toResponse(owner))
                .thenReturn(ownerResponse);
        BDDMockito.when(petOwnerRepositoryMock.save(owner))
                .thenReturn(owner);
        BDDMockito.when(jwtServiceMock.generateToken(ArgumentMatchers.any(UserAuthenticated.class)))
                .thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.register(request);

        Assertions.assertThat("jwt-token").isEqualTo(response.token());
        Assertions.assertThat("Bearer").isEqualTo(response.type());
        Assertions.assertThat(request.getUsername()).isEqualTo(response.owner().username());
        Assertions.assertThat(owner.getRole()).isEqualTo(UserRole.USER);
        Assertions.assertThat(request.getPostalCode()).isEqualTo(response.owner().address().getPostalCode());
        Mockito.verify(petOwnerRepositoryMock, Mockito.times(1))
                .existsByUsername(request.getUsername());
        Mockito.verify(passwordEncoderMock, Mockito.times(1))
                .encode(request.getPassword());
        Mockito.verify(petOwnerRepositoryMock, Mockito.times(1))
                .save(owner);
        Mockito.verify(jwtServiceMock, Mockito.times(1))
                .generateToken(ArgumentMatchers.any(UserAuthenticated.class));
    }

    @Test
    @DisplayName("register throws UsernameAlreadyExistsException when username already exists")
    void register_ThrowsUsernameAlreadyExistsException_WhenUsernameAlreadyExists() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Diego Oliveira")
                .username("diego123")
                .password("secret123")
                .build();

        BDDMockito.when(petOwnerRepositoryMock.existsByUsername(request.getUsername()))
                .thenReturn(true);

        Assertions.assertThatExceptionOfType(UsernameAlreadyExistsException.class)
                .isThrownBy(() -> this.authenticationService.register(request))
                .withMessageContaining("Username is already in use");
        Mockito.verify(petOwnerRepositoryMock, Mockito.never()).save(ArgumentMatchers.any());
        Mockito.verify(jwtServiceMock, Mockito.never()).generateToken(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("register throws ViaCepPostalCodeNotFoundException when postal code is not found")
    void register_ThrowsViaCepPostalCodeNotFoundException_WhenPostalCodeIsNotFound() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Diego Oliveira")
                .username("diego123")
                .password("secret123")
                .postalCode("non existing postal code")
                .build();
        String encodedPassword = "encoded password";

        BDDMockito.when(petOwnerRepositoryMock.existsByUsername(request.getUsername()))
                .thenReturn(false);
        BDDMockito.when(passwordEncoderMock.encode(request.getPassword()))
                .thenReturn(encodedPassword);
        BDDMockito.when(addressLookupServiceMock.findByPostalCode(request.getPostalCode()))
                .thenThrow(new ViaCepPostalCodeNotFoundException("Postal code not found"));

        Assertions.assertThatExceptionOfType(ViaCepPostalCodeNotFoundException.class)
                .isThrownBy(() -> this.authenticationService.register(request))
                .withMessageContaining("Postal code not found");
        Mockito.verify(petOwnerRepositoryMock, Mockito.never()).save(ArgumentMatchers.any());
        Mockito.verify(petOwnerMapperMock, Mockito.never()).toResponse(ArgumentMatchers.any());
        Mockito.verify(jwtServiceMock, Mockito.never()).generateToken(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("register throws InvalidPostalCodeException when postal code is invalid")
    void register_ThrowsInvalidPostalCodeException_WhenPostalCodeIsInvalid() {
        RegisterRequest request = RegisterRequest.builder()
                .name("Diego Oliveira")
                .username("diego123")
                .password("secret123")
                .postalCode("invalid postal code")
                .build();
        String encodedPassword = "encoded password";

        BDDMockito.when(petOwnerRepositoryMock.existsByUsername(request.getUsername()))
                .thenReturn(false);
        BDDMockito.when(passwordEncoderMock.encode(request.getPassword()))
                .thenReturn(encodedPassword);
        BDDMockito.when(addressLookupServiceMock.findByPostalCode(request.getPostalCode()))
                .thenThrow(new InvalidPostalCodeException("Invalid postal code format"));

        Assertions.assertThatExceptionOfType(InvalidPostalCodeException.class)
                .isThrownBy(() -> this.authenticationService.register(request))
                .withMessageContaining("Invalid postal code format");
        Mockito.verify(petOwnerRepositoryMock, Mockito.never()).save(ArgumentMatchers.any());
        Mockito.verify(petOwnerMapperMock, Mockito.never()).toResponse(ArgumentMatchers.any());
        Mockito.verify(jwtServiceMock, Mockito.never()).generateToken(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("login returns token when credentials are valid")
    void login_ReturnsToken_WhenCredentialsAreValid() {
        LoginRequest request = LoginRequest.builder()
                .username("diego123")
                .password("secret123")
                .build();
        PetOwner owner = PetOwnerCreator.createValidPetOwner();
        Authentication authentication = Mockito.mock(Authentication.class);
        UserAuthenticated user = Mockito.mock(UserAuthenticated.class);
        PetOwnerResponse ownerResponse = PetOwnerCreator.createResponse(owner);

        BDDMockito.when(authentication.getPrincipal()).thenReturn(user);
        BDDMockito.when(authenticationManagerMock.authenticate(ArgumentMatchers.any()))
                .thenReturn(authentication);
        BDDMockito.when(petOwnerMapperMock.toResponse(user.getPetOwner()))
                .thenReturn(ownerResponse);
        BDDMockito.when(jwtServiceMock.generateToken(user))
                .thenReturn("jwt-token");

        AuthenticationResponse response = authenticationService.login(request);

        Assertions.assertThat("jwt-token").isEqualTo(response.token());
        Assertions.assertThat("Bearer").isEqualTo(response.type());
        Assertions.assertThat(request.getUsername()).isEqualTo(response.owner().username());
        Mockito.verify(jwtServiceMock, Mockito.times(1))
                .generateToken(user);
    }

    @Test
    @DisplayName("login throws InvalidCredentialsException when user does not exist")
    void login_ThrowsInvalidCredentialsException_WhenUserDoesNotExist() {
        LoginRequest request = LoginRequest.builder()
                .username("invalid")
                .password("password")
                .build();

        BDDMockito.when(authenticationManagerMock.authenticate(ArgumentMatchers.any()))
                .thenThrow(new UsernameNotFoundException("Owner not found with username 'invalid'"));

        Assertions.assertThatExceptionOfType(InvalidCredentialsException.class)
                .isThrownBy(() -> this.authenticationService.login(request))
                .withMessageContaining("Invalid username or password");
        Mockito.verify(jwtServiceMock, Mockito.never()).generateToken(ArgumentMatchers.any());
    }

    @Test
    @DisplayName("login throws InvalidCredentialsException when password is invalid")
    void login_ThrowsInvalidCredentialsException_WhenPasswordIsInvalid() {
        LoginRequest request = LoginRequest.builder()
                .username("diego123")
                .password("wrong")
                .build();

        BDDMockito.when(authenticationManagerMock.authenticate(ArgumentMatchers.any()))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        Assertions.assertThatExceptionOfType(InvalidCredentialsException.class)
                .isThrownBy(() -> this.authenticationService.login(request))
                .withMessageContaining("Invalid username or password");
        Mockito.verify(jwtServiceMock, Mockito.never()).generateToken(ArgumentMatchers.any());
    }
}