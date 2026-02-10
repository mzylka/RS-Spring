package com.rs.app.dto.game;


import java.util.Date;

public record GameDTO(
        Long id,
        String title,
        String slug,
        String thumbnail,
        String thumbnailMin,
        String content,
        String websiteUrl,
        String xUrl,
        String fbUrl,
        String steamUrl,
        Date releaseDate,
        String steamAppId
) {}
