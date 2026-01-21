package com.abrahamlara.authservice.user.dto;

import com.abrahamlara.authservice.user.model.Role;
import com.abrahamlara.authservice.user.model.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Request body for updating an existing user.
 * Supports administrative resets and role changes.
 */
public record UserUpdateRequest(
        @Schema(description = "New username for the account", example = "abrahamlara")
        String username,

        @Schema(description = "New email address", example = "abraham@example.com")
        String email,

        @Schema(description = "Optional password reset. Only used by admins.")
        String password,

        @Schema(description = "Updated role assigned to the user", example = "ADMIN")
        Role role,

        @Schema(description = "Updated account status", example = "ACTIVE")
        UserStatus status
) {}