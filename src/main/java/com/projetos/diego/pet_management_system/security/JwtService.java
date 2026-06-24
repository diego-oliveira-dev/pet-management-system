package com.projetos.diego.pet_management_system.security;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Service;
import org.springframework.security.oauth2.jwt.JwtEncoder;

import java.time.Instant;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class JwtService {
    private final JwtEncoder encoder;

    public String generateToken(Authentication authentication) {
        Instant now = Instant.now();
        long expiry = 36000L;
        String authorities = authentication.getAuthorities()
                .stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        UserAuthenticated userAuthenticated = (UserAuthenticated) authentication.getPrincipal();
        String ownerId = String.valueOf(userAuthenticated.getPetOwner().getId());
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("pet-management-system")
                .issuedAt(now)
                .expiresAt(now.plusSeconds(expiry))
                .subject(ownerId)
                .claim("authorities", authorities)
                .build();
        return encoder.encode(JwtEncoderParameters.from(claims))
                .getTokenValue();
    }
}
