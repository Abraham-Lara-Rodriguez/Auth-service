package com.abrahamlara.authservice.auth.service;

import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import com.abrahamlara.authservice.user.model.User;
import com.abrahamlara.authservice.user.model.UserStatus;
import org.springframework.security.core.GrantedAuthority;
import com.abrahamlara.authservice.user.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import java.util.Set;
import java.util.HashSet;
import java.util.Collection;

/**
 * Implementation of UserDetailsService to load user-specific data.
 */
@Service
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Locates the user based on the username or email.
     * @param username the username or email identifying the user whose data is required.
     * @return a fully populated UserDetails object (never null)
     * @throws UsernameNotFoundException if the user could not be found
     */
    @Override
    public @NonNull UserDetails loadUserByUsername(@NonNull String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsernameOrEmail(username , username).orElseThrow(() ->
                new UsernameNotFoundException("User not found: " + username)
        );
        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),                    // username
                user.getPassword(),                    // password
                user.getStatus() == UserStatus.ACTIVE, // enabled
                true,                                  // accountNonExpired
                true,                                  // credentialsNonExpired
                user.getStatus() != UserStatus.SUSPENDED, // accountNonLocked
                mapAuthorities(user)
        );
    }

    /**
     * Converts user roles and permissions into GrantedAuthority collection.
     * @param user the user entity
     * @return collection of granted authorities
     */
    private Collection<? extends GrantedAuthority> mapAuthorities(User user) {
        Set<GrantedAuthority> authorities = new HashSet<>();
        // ROLE_
        authorities.add(new SimpleGrantedAuthority(user.getRole().asAuthority()));
        // PERMISSIONS
        user.getRole().getPermissions().forEach(permission ->
                authorities.add(new SimpleGrantedAuthority(permission.name()))
        );
        return authorities;
    }
}