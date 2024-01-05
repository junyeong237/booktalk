package com.example.booktalk.domain.comment.dto.request;

import lombok.Builder;

@Builder
public record CommentGetListReq (
        Long reviewId
) {
}
