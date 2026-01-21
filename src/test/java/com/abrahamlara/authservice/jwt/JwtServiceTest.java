package com.abrahamlara.authservice.jwt;

import com.abrahamlara.authservice.auth.config.jwt.JwtProperties;
import com.abrahamlara.authservice.auth.config.jwt.JwtService;
import io.jsonwebtoken.Claims;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

class JwtServiceTest {

    private JwtService jwtService;
    private JwtProperties jwtProperties;

    private User userDetails;

    @BeforeEach
    void setup() {
        jwtProperties = new JwtProperties();
        jwtProperties.setSecret("MYSECRETKEY"); // base64 32+ chars
        jwtProperties.setAccessTokenExpiration(60_000); // 1 min
        jwtProperties.setRefreshTokenExpiration(120_000);

        jwtService = new JwtService(jwtProperties);

        userDetails = new User(
                "johndoe",
                "password",
                List.of(new SimpleGrantedAuthority("ROLE_USER"))
        );
    }

    @Test
    void shouldGenerateAccessToken() {
        String token = jwtService.generateAccessToken(userDetails);

        assertThat(token).isNotBlank();
        Claims claims = jwtService.parse(token);

        assertThat(claims.getSubject()).isEqualTo("johndoe");
        assertThat(claims.get("type")).isEqualTo("access");
        assertThat((List<String>) claims.get("roles")).containsExactly("ROLE_USER");
    }

    @Test
    void shouldGenerateRefreshToken() {
        String token = jwtService.generateRefreshToken(userDetails);

        assertThat(token).isNotBlank();
        Claims claims = jwtService.parse(token);

        assertThat(claims.getSubject()).isEqualTo("johndoe");
        assertThat(claims.get("type")).isEqualTo("refresh");
        assertThat(claims.get("roles")).isNull(); // for refresh tokens, roles are not included
    }

    @Test
    void shouldValidateValidAccessToken() {
        String token = jwtService.generateAccessToken(userDetails);

        boolean valid = jwtService.isTokenValid(token, userDetails);

        assertThat(valid).isTrue();
    }

    @Test
    void shouldInvalidateTokenWithWrongUser() {
        String token = jwtService.generateAccessToken(userDetails);

        User otherUser = new User("someone_else", "pass", List.of());

        boolean valid = jwtService.isTokenValid(token, otherUser);

        assertThat(valid).isFalse();
    }

    @Test
    void shouldValidateRefreshTokenProperly() {
        String token = jwtService.generateRefreshToken(userDetails);

        boolean valid = jwtService.isRefreshTokenValid(token);

        assertThat(valid).isTrue();
    }

    @Test
    void shouldInvalidateAccessTokenOnRefreshValidator() {
        String token = jwtService.generateAccessToken(userDetails);

        boolean valid = jwtService.isRefreshTokenValid(token);

        assertThat(valid).isFalse();
    }

    @Test
    void shouldExtractUsername() {
        String token = jwtService.generateAccessToken(userDetails);

        String username = jwtService.extractUsername(token);

        assertThat(username).isEqualTo("johndoe");
    }

    @Test
    void shouldReturnFalseOnCorruptedToken() {
        String token = "invalid.token.value";

        boolean valid = jwtService.isTokenValid(token, userDetails);

        assertThat(valid).isFalse();
    }
}
