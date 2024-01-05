package com.example.booktalk.domain.comment.dto.response;

import lombok.Builder;

@Builder
public record CommentCreateRes (
        Long commentId,
        String content
) {
}
