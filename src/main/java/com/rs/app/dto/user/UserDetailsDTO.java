package com.rs.app.dto.user;

import com.rs.app.dto.role.RoleDTO;

import java.time.Instant;
import java.util.Set;

//For moderation purposes
public record UserDetailsDTO(
        Long id,
        String discordName,
        Instant createdAt,
        Instant lastLogged,
        Instant bannedTo,
        String avatar,
        String steamUrl,
        Long youtubeChannelId,
        Long twitchStreamId,
        Set<RoleDTO> roles
) {
}
