package com.abrahamlara.authservice.user.dto;

import com.abrahamlara.authservice.user.model.Role;
import com.abrahamlara.authservice.user.model.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Represents a user returned by API operations.")
public record UserResponse(
        @Schema(description = "Unique identifier of the user", example = "42")
        Long id,

        @Schema(description = "Username for login and identification", example = "abrahamlara")
        String username,

        @Schema(description = "Registered email of the user", example = "abraham@example.com")
        String email,

        @Schema(description = "Assigned role defining authorization level", example = "ADMIN")
        Role role,

        @Schema(description = "Current account status", example = "ACTIVE")
        UserStatus status
) {}