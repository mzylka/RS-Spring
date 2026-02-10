package com.rs.app.mappers.impl;

import com.rs.app.domain.entities.BlogNewsComment;
import com.rs.app.dto.comment.CommentDTO;
import com.rs.app.dto.comment.CommentRequest;
import com.rs.app.mappers.BlogNewsCommentMapper;
import org.springframework.stereotype.Component;

@Component
public class BlogNewsCommentMapperImpl implements BlogNewsCommentMapper {

    @Override
    public CommentDTO toDto(BlogNewsComment element) {
        return null;
    }

    @Override
    public BlogNewsComment fromRequest(CommentRequest commentRequest) {
        return new BlogNewsComment(commentRequest.content());
    }
}
