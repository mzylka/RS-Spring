package com.rs.app.dto.blognews;

import java.time.Instant;

public record BlogNewsSimpleDTO(
        Long id,
        String title,
        String slug,
        String thumbnail,
        String thumbnailMin,
        Instant createdAt
) {
}
