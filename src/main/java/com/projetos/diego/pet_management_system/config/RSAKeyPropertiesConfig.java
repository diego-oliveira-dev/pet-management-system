package com.projetos.diego.pet_management_system.config;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

@ConfigurationProperties(prefix = "rsa")
public record RSAKeyPropertiesConfig(
        RSAPublicKey publicKey,
        RSAPrivateKey privateKey
) {}
