package com.abrahamlara.authservice.user.service;

import com.abrahamlara.authservice.shared.exceptions.DuplicateResourceException;
import com.abrahamlara.authservice.shared.exceptions.ResourceNotFoundException;
import com.abrahamlara.authservice.user.dto.*;
import com.abrahamlara.authservice.user.mapper.UserMapper;
import com.abrahamlara.authservice.user.model.User;
import com.abrahamlara.authservice.user.model.UserStatus;
import com.abrahamlara.authservice.user.repository.UserRepository;
import com.abrahamlara.authservice.user.repository.UserSpecifications;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final UserMapper userMapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public Page<UserResponse> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable)
                .map(userMapper::toResponse);
    }

    @Override
    public Page<UserResponse> search(UserFilter filter, Pageable pageable) {
        return userRepository.findAll(UserSpecifications.withFilters(filter), pageable)
                .map(userMapper::toResponse);
    }

    @Override
    public UserResponse getUserById(Long id) {
        User user = findOrThrow(id);
        return userMapper.toResponse(user);
    }

    @Override
    @Transactional
    public UserResponse createUser(UserCreateRequest request) {

        if (request.status() == UserStatus.INACTIVE) {
            throw new IllegalArgumentException("Cannot create a user with INACTIVE status.");
        }

        if (userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username already exists: " + request.username());
        }
        if (userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already exists: " + request.email());
        }

        User user = new User();
        user.changeUsername(request.username());
        user.changeEmail(request.email());
        user.changePassword(passwordEncoder.encode(request.password()));
        user.changeRole(request.role());
        user.changeStatus(request.status());

        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public UserResponse updateUser(Long id, UserUpdateRequest request) {
        User user = findOrThrow(id);

        if (request.status() == UserStatus.INACTIVE) {
            throw new IllegalArgumentException("Use DELETE endpoint to deactivate a user.");
        }

        if (!user.getUsername().equals(request.username()) &&
                userRepository.existsByUsername(request.username())) {
            throw new DuplicateResourceException("Username already exists: " + request.username());
        }

        if (!user.getEmail().equals(request.email()) &&
                userRepository.existsByEmail(request.email())) {
            throw new DuplicateResourceException("Email already exists: " + request.email());
        }

        if (request.password() != null) {
            user.changePassword(passwordEncoder.encode(request.password()));
        }

        user.changeUsername(request.username());
        user.changeEmail(request.email());
        user.changeRole(request.role());
        user.changeStatus(request.status());
        return userMapper.toResponse(userRepository.save(user));
    }

    @Override
    @Transactional
    public void deleteUser(Long id) {
        User user = findOrThrow(id);
        // Soft delete — keep record for audit/tracking
        user.changeStatus(UserStatus.INACTIVE);
    }

    @Override
    public UserProfileResponse profile() {
        var auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth == null || !auth.isAuthenticated()) {
            throw new ResourceNotFoundException("No authenticated user found");
        }

        User user = userRepository.findByUsername(auth.getName()).orElseThrow(() -> new ResourceNotFoundException("User not found with username: " + auth.getName()));

        return new UserProfileResponse(
                user.getUsername(),
                user.getEmail(),
                user.getRole().name(),
                user.getStatus().name()
        );
    }

    private User findOrThrow(Long id) {
        return userRepository.findById(id).orElseThrow(() -> new ResourceNotFoundException("User not found with id: " + id));
    }
}
