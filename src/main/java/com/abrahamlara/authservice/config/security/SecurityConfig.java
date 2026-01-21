package com.abrahamlara.authservice.config.security;

import com.abrahamlara.authservice.auth.config.jwt.JwtAuthenticationFilter;
import com.abrahamlara.authservice.auth.config.jwt.JwtService;
import com.abrahamlara.authservice.config.properties.SecurityEndpoints;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfigurationSource;

import java.time.Duration;
import java.util.List;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    //Allowed origin for CORS configuration, typically the client application URL.
    @Value("${CLIENT_ORIGIN}")
    private String clientOrigins;

    /**
     * Exposes the AuthenticationManager so it can be used by services (e.g. login).
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)  {
        return authenticationConfiguration.getAuthenticationManager();
    }

    /**
     * Custom JWT filter that extracts and validates tokens on each request.
     */
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter(JwtService jwtService , UserDetailsService userDetailsService) {
        return new JwtAuthenticationFilter(jwtService, userDetailsService);
    }

    /**
     * Enables CORS for the frontend or other services that consume this API.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource(){
        var configuration = new org.springframework.web.cors.CorsConfiguration();
        configuration.setAllowedOrigins(java.util.List.of(clientOrigins.split(",")));
        configuration.setAllowedMethods(java.util.List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        configuration.setAllowedHeaders(List.of("Content-Type", "Authorization", "*"));
        configuration.setExposedHeaders(java.util.List.of("Authorization"));
        configuration.setAllowCredentials(true);
        configuration.setMaxAge(Duration.ofHours(1));
        var source = new org.springframework.web.cors.UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/**", configuration);
        return source;
    }

    /**
     * Main Spring Security entry point.
     * - Stateless because JWT is used instead of sessions
     * - Applies public vs. secured endpoint rules
     * - Registers the JWT authentication filter
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http, JwtAuthenticationFilter jwtAuthenticationFilter) {
        http
                // Register JWT filter before username/password auth filter
                .addFilterBefore(jwtAuthenticationFilter , UsernamePasswordAuthenticationFilter.class)
                // No HTTP session stored on the server
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                // Routing rules: allow public endpoints, secure everything else
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(SecurityEndpoints.PUBLIC).permitAll()
                        .requestMatchers(SecurityEndpoints.MONITORING).permitAll()
                        .requestMatchers(SecurityEndpoints.SWAGGER).permitAll()
                        .anyRequest().authenticated()
                )
                // JWT eliminates need for CSRF tokens
                .csrf(AbstractHttpConfigurer::disable)
                // Enables configured CORS policy
                .cors(Customizer.withDefaults())
                // These auth mechanisms are not used in an API-first service
                .httpBasic(AbstractHttpConfigurer::disable)
                .formLogin(AbstractHttpConfigurer::disable);
        return http.build();
    }

}
