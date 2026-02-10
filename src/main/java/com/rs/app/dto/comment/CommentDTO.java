package com.rs.app.dto.comment;

import com.rs.app.dto.user.UserSimpleDTO;

public record CommentDTO(
        Long id,
        String content,
        Integer likes,
        UserSimpleDTO author
) {
}
