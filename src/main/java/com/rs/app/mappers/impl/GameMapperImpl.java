package com.rs.app.mappers.impl;

import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.game.GameDTO;
import com.rs.app.dto.game.GameRequest;
import com.rs.app.dto.game.GameSimpleDTO;
import com.rs.app.domain.entities.Game;
import com.rs.app.mappers.GameMapper;
import org.springframework.stereotype.Component;

@Component
public class GameMapperImpl implements GameMapper {

    @Override
    public Game fromRequest(GameRequest gameRequest) {
        PublicationStatus status = (gameRequest.isPublished()) ? PublicationStatus.PUBLISHED : PublicationStatus.DRAFT;
        return new Game(
                gameRequest.title(),
                gameRequest.content(),
                gameRequest.websiteUrl(),
                gameRequest.xUrl(),
                gameRequest.fbUrl(),
                gameRequest.steamUrl(),
                gameRequest.releaseDate(),
                gameRequest.steamAppId(),
                status
        );
    }

    @Override
    public GameDTO toDto(Game game) {
        return new GameDTO(
                game.getId(),
                game.getTitle(),
                game.getSlug(),
                game.getThumbnail(),
                game.getThumbnailMin(),
                game.getContent(),
                game.getWebsiteUrl(),
                game.getxUrl(),
                game.getFbUrl(),
                game.getSteamUrl(),
                game.getReleaseDate(),
                game.getAppId()
        );
    }

    @Override
    public GameSimpleDTO toSimpleDto(Game game){
        return new GameSimpleDTO(
                game.getId(),
                game.getTitle(),
                game.getSlug(),
                game.getThumbnail(),
                game.getThumbnailMin(),
                game.getIconUrl()
        );
    }
}
