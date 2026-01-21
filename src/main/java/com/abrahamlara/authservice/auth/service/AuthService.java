package com.abrahamlara.authservice.auth.service;

import com.abrahamlara.authservice.auth.dto.AuthResponse;
import com.abrahamlara.authservice.auth.dto.LoginRequest;

/**
 * Authentication use-case boundary for the auth domain.
 */
public interface AuthService {
    /**
     * Attempts to authenticate a user and issue JWT tokens.
     */
    AuthResponse login(LoginRequest request);

    /**
     * Attempts to refresh JWT tokens using a valid refresh token.
     */
    AuthResponse refreshToken(String refreshToken);

}
