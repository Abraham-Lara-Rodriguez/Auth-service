package com.abrahamlara.authservice.auth.controller;

import com.abrahamlara.authservice.auth.dto.AuthResponse;
import com.abrahamlara.authservice.auth.dto.LoginRequest;
import com.abrahamlara.authservice.auth.dto.RefreshTokenRequest;
import com.abrahamlara.authservice.auth.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/login")
    @Operation(summary = "User login with username/email and password")
    public ResponseEntity<AuthResponse> login(@Validated @RequestBody LoginRequest loginRequest) {
        return ResponseEntity.ok(authService.login(loginRequest));
    }

    @Operation(summary = "Refresh JWT tokens using a valid refresh token")
    @PostMapping("/refresh-token")
    public ResponseEntity<AuthResponse> refreshToken(@RequestBody RefreshTokenRequest refreshToken) {
        return ResponseEntity.ok(authService.refreshToken(refreshToken.refreshToken()));
    }

}
