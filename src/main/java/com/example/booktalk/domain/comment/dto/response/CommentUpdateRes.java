package com.example.booktalk.domain.comment.dto.response;

public record CommentUpdateRes (
        Long commentId,
        String content
) {
}
