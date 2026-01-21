package com.abrahamlara.authservice.user.model;

import lombok.Getter;
import java.util.Set;

/**
 * Defines system roles that group application permissions.
 */
@Getter
public enum Role {

    ADMIN(Set.of(

            Permission.ADMIN_CREATE,
            Permission.ADMIN_READ,
            Permission.ADMIN_UPDATE,
            Permission.ADMIN_DELETE
    )),

    USER(Set.of(
            Permission.USER_READ
    ));

    public String asAuthority() {
        return "ROLE_" + this.name();
    }

    private final Set<Permission> permissions;
    Role(Set<Permission> permissions) {
        this.permissions = permissions;
    }

}
