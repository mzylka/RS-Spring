package com.rs.app.dto.community;

import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.game.GameSimpleDTO;
import com.rs.app.dto.user.UserSimpleDTO;


public record CommunityDTO (
        Long id,
        String title,
        String slug,
        String content,
        String websiteUrl,
        String discordUrl,
        UserSimpleDTO owner,
        GameSimpleDTO game,
        PublicationStatus status,
        UserSimpleDTO author
){}
