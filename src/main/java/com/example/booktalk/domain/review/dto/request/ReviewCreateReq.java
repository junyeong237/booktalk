package com.example.booktalk.domain.review.dto.request;

public record ReviewCreateReq (
        String title,
        String content,
        Long productId
) {
}
