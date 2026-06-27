package com.projetos.diego.pet_management_system.util;

import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.test.web.servlet.request.RequestPostProcessor;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.jwt;

public class JwtCreator {

    public static RequestPostProcessor createUserJWT() {
        return jwt().jwt(
                jwt -> jwt
                        .issuer("pet-management-system")
                        .subject("1")
                        .claim("authorities", "ROLE_USER")
                        .build())
                .authorities(
                        new SimpleGrantedAuthority("ROLE_USER")
                );
    }

    public static RequestPostProcessor createAdminJWT() {
        return jwt().jwt(
                jwt -> jwt
                        .issuer("pet-management-system")
                        .subject("1")
                        .claim("authorities", "ROLE_USER ROLE_ADMIN")
                        .build())
                .authorities(
                        new SimpleGrantedAuthority("ROLE_ADMIN")
                );
    }
}
