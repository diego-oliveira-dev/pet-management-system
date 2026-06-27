package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.domain.owner.PetOwner;
import com.projetos.diego.pet_management_system.domain.owner.UserRole;
import com.projetos.diego.pet_management_system.dto.response.AuthenticationResponse;
import com.projetos.diego.pet_management_system.dto.request.LoginRequest;
import com.projetos.diego.pet_management_system.dto.request.RegisterRequest;
import com.projetos.diego.pet_management_system.dto.response.PetOwnerResponse;
import com.projetos.diego.pet_management_system.exception.InvalidCredentialsException;
import com.projetos.diego.pet_management_system.exception.UsernameAlreadyExistsException;
import com.projetos.diego.pet_management_system.mapper.PetOwnerMapper;
import com.projetos.diego.pet_management_system.repository.PetOwnerRepository;
import com.projetos.diego.pet_management_system.security.JwtService;
import com.projetos.diego.pet_management_system.security.UserAuthenticated;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final PetOwnerRepository petOwnerRepository;
    private final PetOwnerMapper petOwnerMapper;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    @Transactional
    public AuthenticationResponse register(RegisterRequest request) {
        if (petOwnerRepository.existsByUsername(request.getUsername())) {
            throw new UsernameAlreadyExistsException("Username is already in use");
        }
        PetOwner ownerToBeSaved = petOwnerMapper.fromPostRequestToEntity(
                request,
                passwordEncoder.encode(request.getPassword()),
                UserRole.USER);
        PetOwner savedOwner = petOwnerRepository.save(ownerToBeSaved);
        return issueToken(new UserAuthenticated(savedOwner));
    }

    public AuthenticationResponse login(LoginRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(
                            request.getUsername(), request.getPassword())
            );
            UserAuthenticated user = (UserAuthenticated) authentication.getPrincipal();
            return issueToken(user);
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }

    public AuthenticationResponse issueToken(UserAuthenticated user) {
        PetOwnerResponse ownerResponse = petOwnerMapper.toResponse(user.getPetOwner());
        return AuthenticationResponse.builder()
                .token(jwtService.generateToken(user))
                .type("Bearer")
                .owner(ownerResponse)
                .build();
    }
}