package com.example.booktalk.domain.review.dto.response;

import lombok.Builder;

@Builder
public record ReviewGetListRes(
        Long reviewId,
        String title
) {
}
