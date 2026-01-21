package com.abrahamlara.authservice.user.dto;

import com.abrahamlara.authservice.user.model.Role;
import com.abrahamlara.authservice.user.model.UserStatus;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Filtering options for querying users.")
public record UserFilter(
        @Schema(description = "Free text matching username or email.")
        String search,

        @Schema(description = "Filter by role.")
        Role role,

        @Schema(description = "Filter by status.")
        UserStatus status
) { }