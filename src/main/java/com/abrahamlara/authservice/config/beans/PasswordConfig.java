package com.abrahamlara.authservice.config.beans;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * Configuration for password hashing strategy used by the Auth Service.
 * BCrypt is used as it applies salting and is computationally expensive,
 * increasing resistance against brute-force attacks.
 */
@Configuration
public class PasswordConfig {

    @Value("${PASSWORD_STRENGTH:10}")
    private int bcryptStrength;

    /**
     * Exposes the PasswordEncoder bean for use across authentication and registration flows.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder(bcryptStrength);
    }
}
