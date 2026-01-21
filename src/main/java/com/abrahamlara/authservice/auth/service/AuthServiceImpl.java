package com.abrahamlara.authservice.auth.service;

import com.abrahamlara.authservice.auth.config.jwt.JwtService;
import com.abrahamlara.authservice.auth.dto.AuthResponse;
import com.abrahamlara.authservice.auth.dto.LoginRequest;
import com.abrahamlara.authservice.auth.dto.RefreshTokenRequest;
import com.abrahamlara.authservice.shared.dto.InvalidTokenException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Authentication service layer handling:
 * - User login with username or email/password
 * - Refresh token lifecycle
 * <p>
 * Delegates authentication to Spring Security and issues JWTs via JwtService.
 */
@Service
@RequiredArgsConstructor
@Transactional
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public AuthResponse login(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        UserDetails userDetails = userDetailsService.loadUserByUsername(request.username());

        String access = jwtService.generateAccessToken(userDetails);
        String refresh = jwtService.generateRefreshToken(userDetails);

        return new AuthResponse(access, refresh);
    }

    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtService.isRefreshTokenValid(refreshToken)) {
            throw new InvalidTokenException("Invalid refresh token");
        }
        String username = jwtService.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);
        String newAccess = jwtService.generateAccessToken(userDetails);
        /*
         * NOTE: Refresh rotation is disabled. In real production scenarios it should be enabled.
         * String newRefreshToken = jwtService.generateRefreshToken(userDetails);
         * return new AuthResponse(newAccessToken, newRefreshToken);
         */
        return new AuthResponse(newAccess, refreshToken);
    }
}
