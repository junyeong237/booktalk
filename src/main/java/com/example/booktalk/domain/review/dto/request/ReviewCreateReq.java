package com.example.booktalk.domain.review.dto.request;

import lombok.Builder;

@Builder
public record ReviewCreateReq (
        String title,
        String content
) {
}
