package com.example.booktalk.domain.review.dto.response;

public record ReviewCreateRes(
    Long reviewId,
    String title,
    String content,
    String nickname
) {
}
