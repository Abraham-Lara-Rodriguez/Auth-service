package com.abrahamlara.authservice.user.service;

import com.abrahamlara.authservice.user.dto.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * User service contract defining use cases related to user management.
 * Supports pagination, dynamic filtering and standard CRUD operations.
 */
public interface UserService {

    /**
     * Returns a paginated list of all users.
     * Pagination must be requested explicitly by clients.
     */
    Page<UserResponse> getAllUsers(Pageable pageable);

    /**
     * Returns users filtered and paginated according to the provided criteria.
     * Useful for administrative dashboards and management panels.
     */
    Page<UserResponse> search(UserFilter filter, Pageable pageable);

    /**
     * Retrieves a single user by its identifier.
     *
     * @param id user identifier
     * @return user response
     */
    UserResponse getUserById(Long id);

    /**
     * Creates a new user in the system.
     */
    UserResponse createUser(UserCreateRequest request);

    /**
     * Updates an existing user.
     */
    UserResponse updateUser(Long id, UserUpdateRequest request);

    /**
     * Deletes a user. Depending on system rules this may be
     * a logical delete (status change) or a physical removal.
     */
    void deleteUser(Long id);

    /**
     * Returns authenticated user context using the current security principal.
     */
    UserProfileResponse profile();
}
