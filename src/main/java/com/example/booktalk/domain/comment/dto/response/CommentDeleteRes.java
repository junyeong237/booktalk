package com.example.booktalk.domain.comment.dto.response;

import lombok.Builder;

@Builder
public record CommentDeleteRes (
        String msg
) {
}
