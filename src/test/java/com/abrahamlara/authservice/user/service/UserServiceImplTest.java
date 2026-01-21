package com.abrahamlara.authservice.user.service;

import com.abrahamlara.authservice.shared.exceptions.DuplicateResourceException;
import com.abrahamlara.authservice.shared.exceptions.ResourceNotFoundException;
import com.abrahamlara.authservice.user.dto.*;
import com.abrahamlara.authservice.user.mapper.UserMapper;
import com.abrahamlara.authservice.user.model.Role;
import com.abrahamlara.authservice.user.model.User;
import com.abrahamlara.authservice.user.model.UserStatus;
import com.abrahamlara.authservice.user.repository.UserRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class UserServiceImplTest {

    private final UserRepository userRepository = mock(UserRepository.class);
    private final UserMapper userMapper = mock(UserMapper.class);
    private final PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);

    private final UserServiceImpl userService =
            new UserServiceImpl(userRepository, userMapper, passwordEncoder);

    @Test
    void createUser_ThrowsDuplicate_WhenUsernameExists() {
        var req = new UserCreateRequest("user", "email@test.com","pass", Role.ADMIN, UserStatus.ACTIVE);

        when(userRepository.existsByUsername("user")).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser(req))
                .isInstanceOf(DuplicateResourceException.class);
    }

    @Test
    void updateUser_ThrowsNotFound_WhenIdMissing() {
        when(userRepository.findById(1L)).thenReturn(Optional.empty());

        var req = new UserUpdateRequest("user","email",null, Role.ADMIN, UserStatus.ACTIVE);

        assertThatThrownBy(() -> userService.updateUser(1L, req))
                .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    void deleteUser_MarksStatusInactive() {
        User mockUser = mock(User.class);
        when(userRepository.findById(1L)).thenReturn(Optional.of(mockUser));

        userService.deleteUser(1L);

        verify(mockUser).changeStatus(UserStatus.INACTIVE);
    }
}