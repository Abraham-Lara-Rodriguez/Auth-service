package com.abrahamlara.authservice.user.repository;

import com.abrahamlara.authservice.user.dto.UserFilter;
import com.abrahamlara.authservice.user.model.Role;
import com.abrahamlara.authservice.user.model.User;
import com.abrahamlara.authservice.user.model.UserStatus;
import org.springframework.data.jpa.domain.Specification;

/**
 * Provides dynamic JPA Specifications for filtering and searching User entities.
 * Supports free-text search, role filtering, status filtering and date ranges.
 * All filters are optional and ignored when null or blank.
 */
public class UserSpecifications {

    /**
     * Builds a composite specification from the provided filter.
     * Each criterion is applied using logical AND.
     *
     * @param filter the set of filters to apply
     * @return a specification for querying users
     */
    public static Specification<User> withFilters(UserFilter filter) {
        return Specification.where(search(filter.search()))
                .and(roleEquals(filter.role()))
                .and(statusEquals(filter.status()));
    }

    /**
     * Case-insensitive partial search against username and email.
     * Example: "abra" matches "Abraham" and "abra@example.com".
     */
    private static Specification<User> search(String search) {
        return (root, query, cb) -> {
            if (search == null || search.isBlank()) return null;
            String like = "%" + search.toLowerCase() + "%";
            return cb.or(
                    cb.like(cb.lower(root.get("username")), like),
                    cb.like(cb.lower(root.get("email")), like)
            );
        };
    }

    /**
     * Filters by role when present.
     */
    private static Specification<User> roleEquals(Role role) {
        return (root, query, cb) ->
                role == null ? null : cb.equal(root.get("role"), role);
    }

    /**
     * Filters by account status when present.
     */
    private static Specification<User> statusEquals(UserStatus status) {
        return (root, query, cb) ->
                status == null ? null : cb.equal(root.get("status"), status);
    }
}
