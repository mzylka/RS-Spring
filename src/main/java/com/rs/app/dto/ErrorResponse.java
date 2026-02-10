package com.rs.app.dto;

public record ErrorResponse(
        int status,
        String message,
        String details
) {
}
