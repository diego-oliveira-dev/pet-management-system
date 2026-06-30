package com.projetos.diego.pet_management_system.controller;

import com.projetos.diego.pet_management_system.dto.request.LoginRequest;
import com.projetos.diego.pet_management_system.dto.request.RegisterRequest;
import com.projetos.diego.pet_management_system.dto.response.AuthenticationResponse;
import com.projetos.diego.pet_management_system.service.AuthenticationService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("auth")
@RequiredArgsConstructor
public class AuthenticationController {
    private final AuthenticationService authenticationService;

    @PostMapping("/register")
    public ResponseEntity<AuthenticationResponse> register(
            @RequestBody
            @Valid RegisterRequest request) {
        return new ResponseEntity<>(authenticationService.register(request), HttpStatus.CREATED);
    }

    @PostMapping("/login")
    public ResponseEntity<AuthenticationResponse> login(
            @RequestBody
            @Valid LoginRequest request) {
        return ResponseEntity.ok(authenticationService.login(request));
    }
}
