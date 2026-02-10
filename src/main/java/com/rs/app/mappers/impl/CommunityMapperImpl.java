package com.rs.app.mappers.impl;

import com.rs.app.domain.entities.Community;
import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.community.CommunityDTO;
import com.rs.app.dto.community.CommunityRequest;
import com.rs.app.dto.community.CommunitySimpleDTO;
import com.rs.app.dto.game.GameSimpleDTO;
import com.rs.app.dto.user.UserSimpleDTO;
import com.rs.app.mappers.CommunityMapper;
import com.rs.app.mappers.GameMapper;
import com.rs.app.mappers.UserMapper;
import org.springframework.stereotype.Component;

@Component
public class CommunityMapperImpl implements CommunityMapper {
    private final GameMapper gameMapper;
    private final UserMapper userMapper;

    public CommunityMapperImpl(GameMapper gameMapper, UserMapper userMapper) {
        this.gameMapper = gameMapper;
        this.userMapper = userMapper;
    }

    @Override
    public Community fromRequest(CommunityRequest communityRequest) {
        PublicationStatus status = (communityRequest.isPublished()) ? PublicationStatus.PUBLISHED : PublicationStatus.DRAFT;
        return new Community(communityRequest.name(),
                communityRequest.description(),
                communityRequest.websiteUrl(),
                communityRequest.discordUrl(),
                status
                );
    }

    @Override
    public CommunityDTO toDto(Community element) {
        UserSimpleDTO owner = (element.getOwner() != null) ? userMapper.toSimpleDto(element.getOwner()) : null;
        GameSimpleDTO game = gameMapper.toSimpleDto(element.getGame());
        UserSimpleDTO author = userMapper.toSimpleDto(element.getAuthor());
        return new CommunityDTO(
                element.getId(),
                element.getTitle(),
                element.getSlug(),
                element.getContent(),
                element.getWebsiteUrl(),
                element.getDiscordUrl(),
                owner,
                game,
                element.getStatus(),
                author);
    }

    @Override
    public CommunitySimpleDTO toSimpleDTO(Community element){
        return new CommunitySimpleDTO(
                element.getId(),
                element.getTitle(),
                element.getSlug(),
                element.getThumbnail(),
                element.getThumbnailMin(),
                element.getStatus()
        );
    }
}
