package com.rs.app.dto.game;

import java.util.Date;

public record GameRequest(
        Long id,
        String title,
        String thumbnail,
        String thumbnailMin,
        String iconUrl,
        String content,
        String websiteUrl,
        String xUrl,
        String fbUrl,
        String steamUrl,
        Date releaseDate,
        String steamAppId,
        boolean isPublished
) {
}
