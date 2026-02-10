package com.rs.app.mappers.impl;

import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.blognews.BlogNewsRequest;
import com.rs.app.dto.blognews.BlogNewsSimpleDTO;
import com.rs.app.dto.game.GameSimpleDTO;
import com.rs.app.domain.entities.BlogNews;
import com.rs.app.dto.blognews.BlogNewsDTO;
import com.rs.app.mappers.BlogNewsMapper;
import com.rs.app.mappers.GameMapper;
import com.rs.app.mappers.UserMapper;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class BlogNewsMapperImpl implements BlogNewsMapper {
    private final UserMapper userMapper;
    private final GameMapper gameMapper;

    public BlogNewsMapperImpl(UserMapper userMapper, GameMapper gameMapper) {
        this.userMapper = userMapper;
        this.gameMapper = gameMapper;
    }

    @Override
    public BlogNews fromRequest(BlogNewsRequest blogNewsRequest){
        PublicationStatus status = (blogNewsRequest.published()) ? PublicationStatus.PUBLISHED : PublicationStatus.DRAFT;
        return new BlogNews(blogNewsRequest.title(), blogNewsRequest.content(), status);
    }

    @Override
    public BlogNewsSimpleDTO toSimpleDto(BlogNews blogNews) {
        return new BlogNewsSimpleDTO(
                blogNews.getId(),
                blogNews.getTitle(),
                blogNews.getSlug(),
                blogNews.getThumbnail(),
                blogNews.getThumbnailMin(),
                blogNews.getCreatedAt());
    }

    @Override
    public BlogNewsDTO toDto(BlogNews blogNews) {
        GameSimpleDTO gameSimpleDTO = Optional.ofNullable(blogNews.getGame())
                .map(gameMapper::toSimpleDto)
                .orElse(null);

        return new BlogNewsDTO(
                blogNews.getTitle(),
                blogNews.getSlug(),
                blogNews.getContent(),
                blogNews.getThumbnail(),
                blogNews.getThumbnailMin(),
                blogNews.getCreatedAt(),
                blogNews.getStatus(),
                gameSimpleDTO,
                userMapper.toSimpleDto(blogNews.getAuthor())
        );
    }
}
