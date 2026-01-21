package com.abrahamlara.authservice.auth.dto;

/**
 * Data Transfer Object representing an authentication response.
 * <p>
 * Contains the access token and refresh token issued upon successful authentication.
 *
 * @param accessToken  the JWT access token
 * @param refreshToken the JWT refresh token
 */
public record AuthResponse(String accessToken, String refreshToken) { }