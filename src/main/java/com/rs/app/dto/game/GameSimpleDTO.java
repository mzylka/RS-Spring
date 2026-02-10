package com.rs.app.dto.game;

public record GameSimpleDTO(
        Long id,
        String title,
        String slug,
        String thumbnail,
        String thumbnailMin,
        String iconUrl
) {
}
