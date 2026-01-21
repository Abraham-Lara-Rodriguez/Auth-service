package com.abrahamlara.authservice.user.model;

import io.swagger.v3.oas.annotations.media.Schema;

/**
 * Fine-grained access permissions used for authorization.
 */
@Schema(description = "Defines the platform-level permissions assigned to roles.")
public enum Permission {
    // Admin permissions
    ADMIN_CREATE,
    ADMIN_READ,
    ADMIN_UPDATE,
    ADMIN_DELETE,
    // User permissions
    USER_CREATE,
    USER_READ,
    USER_UPDATE,
    USER_DELETE
}
