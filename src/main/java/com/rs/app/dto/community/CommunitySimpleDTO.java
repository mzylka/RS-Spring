package com.rs.app.dto.community;

import com.rs.app.domain.enums.PublicationStatus;

public record CommunitySimpleDTO(
        Long id,
        String title,
        String slug,
        String thumbnail,
        String thumbnailMin,
        PublicationStatus status
) {
}
