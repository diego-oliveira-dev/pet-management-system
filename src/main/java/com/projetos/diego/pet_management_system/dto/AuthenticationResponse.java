package com.projetos.diego.pet_management_system.dto;

import lombok.Builder;

@Builder
public record AuthenticationResponse(
        String token,
        String type
) {}
