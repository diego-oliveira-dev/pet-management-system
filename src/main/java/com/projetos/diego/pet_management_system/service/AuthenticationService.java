package com.projetos.diego.pet_management_system.service;

import com.projetos.diego.pet_management_system.dto.AuthenticationRequest;
import com.projetos.diego.pet_management_system.dto.AuthenticationResponse;
import com.projetos.diego.pet_management_system.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthenticationService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;

    public AuthenticationResponse authenticate(AuthenticationRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );
        String token = jwtService.generateToken(authentication);
        return AuthenticationResponse.builder().token(token).type("Bearer").build();
    }
}
