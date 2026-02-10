package com.rs.app.mappers.impl;

import com.rs.app.domain.entities.User;
import com.rs.app.dto.user.UserDTO;
import com.rs.app.dto.user.UserSimpleDTO;
import com.rs.app.mappers.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class UserMapperImpl implements UserMapper {

    @Override
    public UserDTO toDto(User user) {
        return new UserDTO(
                user.getId(),
                user.getDiscordName(),
                user.getLastLogged(),
                user.getAvatarUrl(),
                user.getSteamUrl(),
                user.getYoutubeChannel().getId(),
                user.getTwitchStream().getId()
        );
    }

    public UserSimpleDTO toSimpleDto(User user){
        return new UserSimpleDTO(
                user.getId(),
                user.getDiscordName(),
                user.getAvatarUrl()
        );
    }
}
