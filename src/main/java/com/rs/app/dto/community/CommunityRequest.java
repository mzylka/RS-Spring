package com.rs.app.dto.community;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;

public record CommunityRequest(
        @NotBlank String thumbnail,
        @NotBlank String thumbnailMin,
        @NotBlank String name,
        @NotBlank String description,
        String websiteUrl,
        String discordUrl,
        @NotEmpty boolean isPublished,
        Long ownerId,
        Long gameId
) {
}
