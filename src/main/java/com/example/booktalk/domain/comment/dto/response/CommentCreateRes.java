package com.example.booktalk.domain.comment.dto.response;

public record CommentCreateRes (
        Long commentId,
        String content,
        String nickname
) {
}
