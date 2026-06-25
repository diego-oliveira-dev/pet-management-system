package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.dto.AuthenticationRequest;
import com.projetos.diego.pet_management_system.dto.AuthenticationResponse;
import com.projetos.diego.pet_management_system.exception.InvalidCredentialsException;
import com.projetos.diego.pet_management_system.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationResponse login(AuthenticationRequest request) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
            );
            String token = jwtService.generateToken(authentication);
            return AuthenticationResponse.builder()
                    .token(token)
                    .type("Bearer")
                    .build();
        } catch (BadCredentialsException | UsernameNotFoundException e) {
            throw new InvalidCredentialsException("Invalid username or password");
        }
    }
}