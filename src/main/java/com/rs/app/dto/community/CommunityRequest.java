package com.rs.app.dto.community;

public record CommunityRequest(
        String name,
        String description,
        String websiteUrl,
        String discordUrl,
        boolean isPublished,
        Long ownerId,
        Long gameId
) {
}
