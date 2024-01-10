package com.example.booktalk.domain.comment.dto.response;

import lombok.Builder;

@Builder
public record CommentUpdateRes (
        Long commentId,
        String content
) {
}
