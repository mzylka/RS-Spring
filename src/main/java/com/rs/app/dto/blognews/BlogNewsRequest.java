package com.rs.app.dto.blognews;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record BlogNewsRequest(
        @NotBlank String title,
        @NotBlank String content,
        @NotNull boolean published,
        Long gameId
) {
}
