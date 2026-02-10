package com.rs.app.dto;

import com.rs.app.dto.user.UserSimpleDTO;

public record YoutubeChannelDTO(
        Long id,
        String url,
        String channelName,
        UserSimpleDTO owner
) {
}
