package com.rs.app.dto.game;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

import java.util.Date;

public record GameRequest(
        @NotBlank String thumbnail,
        @NotBlank String thumbnailMin,
        @NotBlank String title,
        @NotBlank String iconUrl,
        @NotBlank String content,
        String websiteUrl,
        String xUrl,
        String fbUrl,
        String steamUrl,
        Date releaseDate,
        String steamAppId,
        @NotEmpty boolean isPublished
) {
}
