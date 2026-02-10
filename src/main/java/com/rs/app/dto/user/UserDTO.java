package com.rs.app.dto.user;

import java.time.Instant;

public record UserDTO(
        Long id,
        String discordName,
        Instant lastLogged,
        String avatar,
        String steamUrl,
        Long youtubeChannelId,
        Long twitchStreamId
) {}
