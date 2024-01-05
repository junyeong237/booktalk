package com.example.booktalk.domain.review.dto.response;

import lombok.Builder;

@Builder
public record ReviewGetRes (
        Long reviewId,
        String title,
        String content
) {
}
