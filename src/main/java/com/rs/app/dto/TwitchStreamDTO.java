package com.rs.app.dto;

import com.rs.app.dto.user.UserSimpleDTO;

public record TwitchStreamDTO(
        Long id,
        String url,
        String twitchName,
        UserSimpleDTO owner
) {
}
