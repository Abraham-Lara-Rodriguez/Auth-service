package com.abrahamlara.authservice.config.properties;

/**
 * Defines security-related endpoint groups for configuring access rules.
 */
public final class SecurityEndpoints {

    private SecurityEndpoints() {}

    public static final String[] PUBLIC = {
            "/api/v1/auth/login"
    };

    public static final String[] MONITORING = {
            "/actuator/health"
    };

    public static final String[] SWAGGER = {
            "/swagger-ui/**",
            "/swagger-ui.html",
            "/v3/api-docs/**"
    };
}
