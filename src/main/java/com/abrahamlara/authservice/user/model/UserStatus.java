package com.abrahamlara.authservice.user.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Represents the availability of a user account.
 */
@Schema(description = "Indicates whether the user account can access the platform.")
public enum UserStatus {
    ACTIVE,
    SUSPENDED,
    INACTIVE
}
