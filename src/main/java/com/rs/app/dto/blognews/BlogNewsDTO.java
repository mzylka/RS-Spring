package com.rs.app.dto.blognews;

import com.rs.app.domain.enums.PublicationStatus;
import com.rs.app.dto.Authorable;
import com.rs.app.dto.game.GameSimpleDTO;
import com.rs.app.dto.user.UserSimpleDTO;

import java.time.Instant;

public record BlogNewsDTO(
        String title,
        String slug,
        String content,
        String thumbnail,
        String thumbnailMin,
        Instant createdAt,
        PublicationStatus status,
        GameSimpleDTO game,
        UserSimpleDTO author
) implements Authorable
{
    @Override
    public Long authorId(){
        return author.id();
    }
}
