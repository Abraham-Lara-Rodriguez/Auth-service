package com.abrahamlara.authservice.auth.config.jwt;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "security.jwt")
public class JwtProperties {
    @NotBlank
    @Size(min = 32, message = "JWT secret must be at least 32 characters")
    private String secret;
    @Positive
    private long accessTokenExpiration;
    @Positive
    private long refreshTokenExpiration;
}
