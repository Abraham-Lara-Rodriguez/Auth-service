package com.abrahamlara.authservice.user.dto;

public record UserProfileResponse(
        String username,
        String email,
        String role,
        String status) { }