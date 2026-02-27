package com.rs.app.dto.comment;

import jakarta.validation.constraints.NotBlank;

public record CommentRequest(@NotBlank String content) {
}
