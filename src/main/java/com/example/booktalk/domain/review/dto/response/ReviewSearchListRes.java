package com.example.booktalk.domain.review.dto.response;

public record ReviewSearchListRes (
        Long reviewId,
        String title,
        String name,
        Integer reviewLike
) {
}
