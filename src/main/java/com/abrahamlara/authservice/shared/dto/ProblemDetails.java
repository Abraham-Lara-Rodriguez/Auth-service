package com.abrahamlara.authservice.shared.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;

import java.time.Instant;

@Schema(description = "Standard API error response")
@JsonInclude(JsonInclude.Include.NON_NULL)
public record ProblemDetails(
        String type,      // URI that identifies the error category
        String title,     // Human-readable summary
        int status,       // HTTP status code
        String detail,    // Detailed explanation
        String instance,  // URI of the resource involved
        Instant timestamp,
        ErrorCode code    // Custom application code
) {}