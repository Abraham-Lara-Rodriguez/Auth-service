package com.abrahamlara.authservice.user.dto;

import com.abrahamlara.authservice.user.model.Role;
import com.abrahamlara.authservice.user.model.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Payload for creating a new user via admin.")
public record UserCreateRequest(
        @Schema(description = "Unique username for login.", example = "abrahamlara")
        @NotBlank
        String username,

        @Schema(description = "User email address.", example = "abraham@example.com")
        @NotBlank @Email
        String email,

        @Schema(description = "Raw password. Must meet security requirements.")
        @NotBlank
        String password,

        @Schema(description = "Assigned role for the new user.", example = "ADMIN")
        @NotNull
        Role role,

        @Schema(description = "Initial account status.", example = "ACTIVE")
        @NotNull
        UserStatus status
) {}