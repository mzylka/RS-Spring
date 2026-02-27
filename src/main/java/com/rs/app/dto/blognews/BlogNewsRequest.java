package com.rs.app.dto.blognews;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record BlogNewsRequest(
        @NotBlank String thumbnail,
        @NotBlank String thumbnailMin,
        @NotBlank String title,
        @NotBlank String content,
        @NotEmpty boolean published,
        Long gameId
) {
}
