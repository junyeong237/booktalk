package com.example.booktalk.domain.review.dto.response;

import lombok.Builder;

@Builder
public record ReviewCreateRes (
        Long reviewId,
        String title,
        String content
) {
}
