package com.example.booktalk.domain.review.dto.response;

public record ReviewUpdateRes(
    Long id,
    Long productId,
    String title,
    String content,
    String reviewImagePathUrl
) {
}
