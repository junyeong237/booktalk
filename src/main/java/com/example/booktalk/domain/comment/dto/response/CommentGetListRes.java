package com.example.booktalk.domain.comment.dto.response;

public record CommentGetListRes (
        Long commentId,
        String content,
        String nickname
) {
}
