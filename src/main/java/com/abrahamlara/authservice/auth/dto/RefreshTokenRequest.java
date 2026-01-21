package com.abrahamlara.authservice.auth.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object representing a refresh token request.
 *
 * @param refreshToken the JWT refresh token used to obtain a new access token
 */
public record RefreshTokenRequest(@NotBlank(message = "Refresh token is required") String refreshToken) { }