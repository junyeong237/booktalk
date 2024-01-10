package com.example.booktalk.domain.review.dto.request;

import lombok.Builder;

@Builder
public record ReviewUpdateReq (
        String title,
        String content
) {
}
