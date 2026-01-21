package com.abrahamlara.authservice.shared.dto;

public enum ErrorCode {
    FORBIDDEN,
    BAD_REQUEST,
    UNAUTHORIZED,
    RESOURCE_NOT_FOUND,
    DUPLICATE_RESOURCE,
    INVALID_CREDENTIALS,
    VALIDATION_ERROR,
    JSON_PARSE_ERROR,
    INTERNAL_ERROR
}
