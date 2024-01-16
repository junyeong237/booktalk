package com.example.booktalk.domain.comment.dto.request;

public record CommentCreateReq (
        Long reviewId,
        String content
) {
}
