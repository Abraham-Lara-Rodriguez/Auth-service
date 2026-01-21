package com.abrahamlara.authservice.user.mapper;

import com.abrahamlara.authservice.user.dto.UserResponse;
import com.abrahamlara.authservice.user.model.User;
import org.springframework.stereotype.Component;

@Component
public class UserMapper {
    public UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                user.getStatus()
        );
    }
}