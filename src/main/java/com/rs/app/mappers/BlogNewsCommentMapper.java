package com.rs.app.mappers;

import com.rs.app.domain.entities.BlogNewsComment;
import com.rs.app.dto.comment.CommentDTO;
import com.rs.app.dto.comment.CommentRequest;

public interface BlogNewsCommentMapper {
    BlogNewsComment fromRequest(CommentRequest commentRequest);
    CommentDTO toDto(BlogNewsComment blogNewsComment);
}
