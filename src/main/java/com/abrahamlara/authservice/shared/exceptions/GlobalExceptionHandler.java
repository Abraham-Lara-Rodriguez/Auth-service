package com.abrahamlara.authservice.shared.exceptions;

import com.abrahamlara.authservice.shared.dto.ErrorCode;
import com.abrahamlara.authservice.shared.dto.InvalidTokenException;
import com.abrahamlara.authservice.shared.dto.ProblemDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.time.Instant;
import java.util.stream.Collectors;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    /**
     * Utility to build ProblemDetails RFC 7807
     */
    private ProblemDetails buildProblem(String type, HttpStatus status, String detail, HttpServletRequest request, ErrorCode code) {
        return new ProblemDetails(
                type,
                status.getReasonPhrase(),
                status.value(),
                detail,
                request.getRequestURI(),
                Instant.now(),
                code
        );
    }

    // =====================================================================
    // 400 — Validation / Client errors
    // =====================================================================
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ProblemDetails> handleValidation(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String detail = ex.getBindingResult().getFieldErrors()
                .stream()
                .map(e -> e.getField() + ": " + e.getDefaultMessage())
                .collect(Collectors.joining(", "));

        return ResponseEntity.badRequest().body(
                buildProblem("https://authservice/errors/validation-error",
                        HttpStatus.BAD_REQUEST,
                        detail,
                        req,
                        ErrorCode.VALIDATION_ERROR)
        );
    }

    @ExceptionHandler({MethodArgumentTypeMismatchException.class, HttpMessageNotReadableException.class, IllegalArgumentException.class})
    public ResponseEntity<ProblemDetails> handleBadRequest(Exception ex, HttpServletRequest req) {
        return ResponseEntity.badRequest().body(
                buildProblem("https://authservice/errors/bad-request",
                        HttpStatus.BAD_REQUEST,
                        ex.getMessage(),
                        req,
                        ErrorCode.BAD_REQUEST)
        );
    }

    // =====================================================================
    // 401 / 403 — Authentication & Authorization
    // =====================================================================
    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ProblemDetails> handleBadCreds(BadCredentialsException ex, HttpServletRequest req) {
        log.warn("Invalid credentials: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                buildProblem("https://authservice/errors/invalid-credentials",
                        HttpStatus.UNAUTHORIZED,
                        "Invalid username or password",
                        req,
                        ErrorCode.INVALID_CREDENTIALS)
        );
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ProblemDetails> handleAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        log.warn("Access denied: {}", ex.getMessage());
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body(
                buildProblem("https://authservice/errors/forbidden",
                        HttpStatus.FORBIDDEN,
                        "Access denied",
                        req,
                        ErrorCode.FORBIDDEN)
        );
    }

    @ExceptionHandler(InvalidTokenException.class)
    public ResponseEntity<ProblemDetails> handleInvalidToken(InvalidTokenException ex) {
        log.warn("invalid_token: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(
                buildProblem("https://authservice/errors/invlalid-token",
                        HttpStatus.UNAUTHORIZED,
                        "Invalid or expired token",
                        null,
                        ErrorCode.UNAUTHORIZED)
        );
    }

    // =====================================================================
    // 404 / 409 — Domain / Resource errors
    // =====================================================================
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ProblemDetails> handleNotFound(ResourceNotFoundException ex, HttpServletRequest req) {
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(
                buildProblem("https://authservice/errors/resource-not-found",
                        HttpStatus.NOT_FOUND,
                        ex.getMessage(),
                        req,
                        ErrorCode.RESOURCE_NOT_FOUND)
        );
    }

    @ExceptionHandler({DuplicateResourceException.class, DataIntegrityViolationException.class})
    public ResponseEntity<ProblemDetails> handleConflict(Exception ex, HttpServletRequest req) {
        String msg = ex instanceof DataIntegrityViolationException
                ? "Unique constraint violated"
                : ex.getMessage();

        log.warn("Conflict: {}", ex.getMessage());

        return ResponseEntity.status(HttpStatus.CONFLICT).body(
                buildProblem("https://authservice/errors/conflict",
                        HttpStatus.CONFLICT, msg, req, ErrorCode.DUPLICATE_RESOURCE)
        );
    }

    // =====================================================================
    // 500 — Internal server errors
    // =====================================================================
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ProblemDetails> handleGeneric(Exception ex, HttpServletRequest req) {
        log.error("Unexpected error", ex);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(
                buildProblem("https://authservice/errors/internal-error",
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "An unexpected error occurred",
                        req,
                        ErrorCode.INTERNAL_ERROR)
        );
    }
}
