package com.abrahamlara.authservice.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object representing a login request.
 *
 * @param username the username or email of the user attempting to log in
 * @param password the password of the user attempting to log in
 */
public record LoginRequest(
        @NotBlank(message = "Username is required")
        String username,

        @NotBlank(message = "Password is required")
        String password
) {}